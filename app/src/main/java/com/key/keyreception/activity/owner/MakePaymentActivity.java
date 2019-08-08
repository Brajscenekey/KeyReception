package com.key.keyreception.activity.owner;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.key.keyreception.R;
import com.key.keyreception.Session;
import com.key.keyreception.base.BaseActivity;
import com.key.keyreception.connection.RetrofitClient;
import com.key.keyreception.helper.PDialog;

import org.json.JSONObject;

import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Created by Ravi Birla on 23,April,2019
 */
public class MakePaymentActivity extends BaseActivity implements View.OnClickListener {


    private String paymenttype = "", jobId, receiverId, amount;
    private Session session;
    private PDialog pDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_payment);
        session = new Session(this);
        pDialog = new PDialog();
        Intent intent = getIntent();
        jobId = intent.getStringExtra("jobId");
        receiverId = intent.getStringExtra("receiverId");
        amount = intent.getStringExtra("amount");
        init();
    }

    public void init() {
        Button btnpayment = findViewById(R.id.btn_makepayment);
        TextView totamount = findViewById(R.id.tv_paytotalamount);
        ImageView iv_paypal = findViewById(R.id.iv_makepay_paypal);
        RelativeLayout rl_makepay_paypal = findViewById(R.id.rl_makepay_paypal);
        TextView tv_makepay_paypal = findViewById(R.id.tv_makepay_paypal);
        ImageView ivback = findViewById(R.id.iv_leftarrow_payback);
        paymenttype = session.getPayment();
        btnpayment.setOnClickListener(this);
        ivback.setOnClickListener(this);
        totamount.setText(amount);

        if (!paymenttype.isEmpty()) {
            switch (paymenttype) {
                case "paypal":
                    rl_makepay_paypal.setVisibility(View.VISIBLE);
                    iv_paypal.setImageDrawable(getResources().getDrawable(R.drawable.ic_paypal_ico));
                    tv_makepay_paypal.setText(getResources().getString(R.string.paypal));
                    break;
                case "venmo":
                    rl_makepay_paypal.setVisibility(View.VISIBLE);
                    iv_paypal.setImageDrawable(getResources().getDrawable(R.drawable.ic_nvenmo_ico));
                    tv_makepay_paypal.setText(getResources().getString(R.string.venmo));
                    break;
                default:
                    rl_makepay_paypal.setVisibility(View.VISIBLE);
                    iv_paypal.setImageDrawable(getResources().getDrawable(R.drawable.ic_visa));
                    tv_makepay_paypal.setText(getResources().getString(R.string.credit));
                    break;
            }
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_makepayment: {
                if (!paymenttype.isEmpty()) {
                    jobPaymentApi();
                }
            }
            break;
            case R.id.iv_leftarrow_payback: {
                backPress();
            }
        }
    }

    @SuppressLint("NewApi")
    private void jobPaymentApi() {
        pDialog.pdialog(this);
        String token = session.getAuthtoken();
        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().jobPayment(token,
                jobId, receiverId, amount, paymenttype);

        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull retrofit2.Response<ResponseBody> response) {

                try {
                    pDialog.hideDialog();


                    switch (response.code()) {
                        case 200: {

                            String stresult = Objects.requireNonNull(response.body()).string();
                            Log.d("response", stresult);
                            JSONObject jsonObject = new JSONObject(stresult);
                            String statusCode = jsonObject.optString("status");
                            String msg = jsonObject.optString("message");
                            if (statusCode.equals("success")) {
                                Toast.makeText(MakePaymentActivity.this, msg, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MakePaymentActivity.this, OwnerTabActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(MakePaymentActivity.this, msg, Toast.LENGTH_SHORT).show();

                            }

                            break;
                        }
                        case 400: {
                            String result = Objects.requireNonNull(response.errorBody()).string();
                            Log.d("response400", result);
                            JSONObject jsonObject = new JSONObject(result);
                            String statusCode = jsonObject.optString("status");
                            String msg = jsonObject.optString("message");
                            if (statusCode.equals("true")) {
                                Toast.makeText(MakePaymentActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }

                            break;
                        }
                        case 401:

                            try {
                                session.logout();
                                Toast.makeText(MakePaymentActivity.this, "Session expired, please login again.", Toast.LENGTH_SHORT).show();
                                Log.d("ResponseInvalid", Objects.requireNonNull(response.errorBody()).string());

                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                            break;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {

                pDialog.hideDialog();


            }
        });

    }


}
