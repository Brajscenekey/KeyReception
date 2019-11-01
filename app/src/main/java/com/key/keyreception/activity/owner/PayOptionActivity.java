package com.key.keyreception.activity.owner;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.key.keyreception.R;
import com.key.keyreception.Session;
import com.key.keyreception.activity.DetailActivity;
import com.key.keyreception.base.BaseActivity;
import com.key.keyreception.connection.RetrofitClient;
import com.key.keyreception.helper.Constant;
import com.key.keyreception.helper.PDialog;
import com.key.keyreception.helper.Utility;
import com.key.keyreception.helper.Validation;

import org.json.JSONObject;

import java.io.File;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

import static com.key.keyreception.helper.Constant.PLACE_AUTOCOMPLETE_REQUEST_CODE;

public class PayOptionActivity extends BaseActivity implements View.OnClickListener {

    private static final int LOC_REQ_CODE = 3;
    private static final int PLACE_PICKER_REQ_CODE = 2;
    private TextView tvpaypal, tvvenmo, tvcredit, tvbilling_address;
    private ImageView ivpaypal;
    private ImageView ivvenmo;
    private ImageView ivcredit;
    private RelativeLayout rlpaypal, rlvenmo, rlcredit;
    private EditText etAddress;
    private PDialog pDialog;
    private Session session;
    private String latitude = "";
    private String longitude = "";
    private String ptype = "", Paymentcheck = "";
    private File file;
    private Validation validation;
    private Utility utility;
    private Button btn_paymentmethod;
    private int lastClick = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_option);
        pDialog = new PDialog();
        session = new Session(this);
        validation = new Validation(this);
        utility = new Utility();
        init();
    }

    private void init() {
        tvpaypal = findViewById(R.id.tv_paypal);
        tvvenmo = findViewById(R.id.tv_venemo);
        tvcredit = findViewById(R.id.tv_credit);
        btn_paymentmethod = findViewById(R.id.btn_paymentmethod);
        ivpaypal = findViewById(R.id.iv_paypal);
        ivvenmo = findViewById(R.id.iv_venemo);
        ivcredit = findViewById(R.id.iv_credit);
        rlpaypal = findViewById(R.id.rl_paypal);
        rlvenmo = findViewById(R.id.rl_venemo);
        rlcredit = findViewById(R.id.rl_credit);
        etAddress = findViewById(R.id.payment_address);
        tvbilling_address = findViewById(R.id.tvbilling_address);
        ImageView iv_pay_back = findViewById(R.id.iv_pay_back);
        btn_paymentmethod.setOnClickListener(this);
        etAddress.setOnClickListener(this);
        rlpaypal.setOnClickListener(this);
        rlvenmo.setOnClickListener(this);
        rlcredit.setOnClickListener(this);
        iv_pay_back.setOnClickListener(this);

//        String paytype = session.getPayment();


        ptype = "visa";
        etAddress.setText(session.getPaymentAddress());
        rlcredit.setBackground(getResources().getDrawable(R.drawable.cornergrayback));
        ivcredit.setImageDrawable(getResources().getDrawable(R.drawable.ic_visa));
        tvcredit.setTextColor(getResources().getColor(R.color.colorwhite));
        tvbilling_address.setVisibility(View.VISIBLE);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_paypal: {
                ptype = "paypal";
                rlpaypal.setBackground(getResources().getDrawable(R.drawable.cornergrayback));
                rlvenmo.setBackground(getResources().getDrawable(R.drawable.cornerwhiteback));
                rlcredit.setBackground(getResources().getDrawable(R.drawable.cornerwhiteback));
                ivpaypal.setImageDrawable(getResources().getDrawable(R.drawable.ic_active_paypal_ico));
                ivvenmo.setImageDrawable(getResources().getDrawable(R.drawable.ic_nvenmo_ico));
                ivcredit.setImageDrawable(getResources().getDrawable(R.drawable.ic_visa));
                tvpaypal.setTextColor(getResources().getColor(R.color.colorwhite));
                tvvenmo.setTextColor(getResources().getColor(R.color.colorDarkgray));
                tvcredit.setTextColor(getResources().getColor(R.color.colorDarkgray));
                tvbilling_address.setVisibility(View.GONE);
                etAddress.setVisibility(View.GONE);
                btn_paymentmethod.setVisibility(View.VISIBLE);


            }
            break;

            case R.id.rl_venemo: {
                ptype = "venmo";
                rlpaypal.setBackground(getResources().getDrawable(R.drawable.cornerwhiteback));
                rlvenmo.setBackground(getResources().getDrawable(R.drawable.cornergrayback));
                rlcredit.setBackground(getResources().getDrawable(R.drawable.cornerwhiteback));
                ivpaypal.setImageDrawable(getResources().getDrawable(R.drawable.ic_paypal_ico));
                ivvenmo.setImageDrawable(getResources().getDrawable(R.drawable.ic_active_nvenmo_ico));
                ivcredit.setImageDrawable(getResources().getDrawable(R.drawable.ic_visa));
                tvpaypal.setTextColor(getResources().getColor(R.color.colorDarkgray));
                tvvenmo.setTextColor(getResources().getColor(R.color.colorwhite));
                tvcredit.setTextColor(getResources().getColor(R.color.colorDarkgray));
                tvbilling_address.setVisibility(View.GONE);
                etAddress.setVisibility(View.GONE);
                btn_paymentmethod.setVisibility(View.GONE);


            }
            break;

            case R.id.rl_credit: {
                ptype = "visa";
               /* rlpaypal.setBackground(getResources().getDrawable(R.drawable.cornerwhiteback));
                rlvenmo.setBackground(getResources().getDrawable(R.drawable.cornerwhiteback));
                rlcredit.setBackground(getResources().getDrawable(R.drawable.cornergrayback));
                ivpaypal.setImageDrawable(getResources().getDrawable(R.drawable.ic_paypal_ico));
                ivvenmo.setImageDrawable(getResources().getDrawable(R.drawable.ic_nvenmo_ico));
                ivcredit.setImageDrawable(getResources().getDrawable(R.drawable.ic_visa));
                tvpaypal.setTextColor(getResources().getColor(R.color.colorDarkgray));
                tvvenmo.setTextColor(getResources().getColor(R.color.colorDarkgray));
                tvcredit.setTextColor(getResources().getColor(R.color.colorwhite));
                tvbilling_address.setVisibility(View.VISIBLE);
                etAddress.setVisibility(View.VISIBLE);
                btn_paymentmethod.setVisibility(View.VISIBLE);*/
                Intent intent = new Intent(PayOptionActivity.this, PaymentdetailActivity.class);
                intent.putExtra("key", "0");
                startActivity(intent);


            }
            break;


            case R.id.payment_address: {
                Utility.autoCompletePlacePicker(this);
            }
            break;
            case R.id.iv_pay_back: {
                backPress();
            }

            break;
            case R.id.btn_paymentmethod: {
                if (utility.checkInternetConnection(this)) {
                    if (!ptype.isEmpty()) {
                        if (ptype.equals("paypal") || ptype.equals("venmo")) {
                            paymentApiData();
                        }
                        if (ptype.equals("visa")) {
                            if (!etAddress.getText().toString().isEmpty()) {
                                paymentApiData();
                            } else {
                                Toast.makeText(this, "Please billing address", Toast.LENGTH_SHORT).show();
                            }
                        }

                    } else {
                        Toast.makeText(this, "Please Select payment type", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Toast.makeText(this, "Please check your Network", Toast.LENGTH_SHORT).show();
                }


            }
            break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);


        //*********Autocomplete place p[icker****************//
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                assert data != null;
                Place place = PlaceAutocomplete.getPlace(this, data);
                etAddress.setText(place.getAddress());
                latitude = String.valueOf(place.getLatLng().latitude);
                longitude = String.valueOf(place.getLatLng().longitude);
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status1 = PlaceAutocomplete.getStatus(this, data);
            } else if (resultCode == RESULT_CANCELED) {
                Constant.hideSoftKeyBoard(PayOptionActivity.this, etAddress);
            }
        }

    }


    public void paymentApiData() {

        String authtoken = session.getAuthtoken();
        String notificationStatus = session.getnotificationStatus();
        String address = etAddress.getText().toString();
        MediaType text = MediaType.parse("text/plain");
        RequestBody address1 = RequestBody.create(text, address);
        RequestBody latitude1 = RequestBody.create(text, latitude);
        RequestBody longitude1 = RequestBody.create(text, longitude);
        RequestBody ptype1 = RequestBody.create(text, ptype);
        RequestBody num = RequestBody.create(text, "");
        RequestBody notificationStatus1 = RequestBody.create(text, notificationStatus);
        MultipartBody.Part fileToUpload1 = null;

        if (file != null) {
            RequestBody mFile1 = RequestBody.create(MediaType.parse("image/*"), file);
            fileToUpload1 = MultipartBody.Part.createFormData("profileImage", file.getName(), mFile1);
        }

        pDialog.pdialog(this);
        Call<ResponseBody> call = RetrofitClient.getInstance()
                .getApi().updateDoc(authtoken, num, address1, latitude1, longitude1, ptype1, notificationStatus1, fileToUpload1);
        call.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("NewApi")
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

                                JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                                String billingAddress = jsonObject1.optString("billingAddress");
                                session.putPayment(ptype);
                                session.putPaymentAddress(billingAddress);
                                etAddress.setText(billingAddress);

                                if (getIntent().getStringExtra("payid") != null) {
                                    Intent intent = new Intent(PayOptionActivity.this, DetailActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Intent intent = new Intent(PayOptionActivity.this, OwnerTabActivity.class);
                                    intent.putExtra("order", "3");
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                    Toast.makeText(PayOptionActivity.this, msg, Toast.LENGTH_SHORT).show();

                                }
                               /* if (getIntent().getStringExtra("payid") != null) {
                                     Intent intent = new Intent(PayOptionActivity.this, PaymentdetailActivity.class);
                                     intent.putExtra("key","0");
                                     startActivity(intent);
                                } else {
                                    Intent intent = new Intent(PayOptionActivity.this, OwnerTabActivity.class);
                                    intent.putExtra("order", "3");
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();

                                    Toast.makeText(PayOptionActivity.this, msg, Toast.LENGTH_SHORT).show();

                                }*/
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
                                Toast.makeText(PayOptionActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }

                            break;
                        }
                        case 401:

                            try {
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








/* switch (paytype) {
            case "paypal": {
                ptype = "paypal";
                rlpaypal.setBackground(getResources().getDrawable(R.drawable.cornergrayback));
                rlvenmo.setBackground(getResources().getDrawable(R.drawable.cornerwhiteback));
                rlcredit.setBackground(getResources().getDrawable(R.drawable.cornerwhiteback));
                ivpaypal.setImageDrawable(getResources().getDrawable(R.drawable.ic_active_paypal_ico));
                ivvenmo.setImageDrawable(getResources().getDrawable(R.drawable.ic_nvenmo_ico));
                ivcredit.setImageDrawable(getResources().getDrawable(R.drawable.ic_visa));
                tvpaypal.setTextColor(getResources().getColor(R.color.colorwhite));
                tvvenmo.setTextColor(getResources().getColor(R.color.colorDarkgray));
                tvcredit.setTextColor(getResources().getColor(R.color.colorDarkgray));
                tvbilling_address.setVisibility(View.GONE);
                etAddress.setVisibility(View.GONE);
                btn_paymentmethod.setVisibility(View.VISIBLE);

            }
            break;

            case "venmo": {
                ptype = "venmo";
                rlpaypal.setBackground(getResources().getDrawable(R.drawable.cornerwhiteback));
                rlvenmo.setBackground(getResources().getDrawable(R.drawable.cornergrayback));
                rlcredit.setBackground(getResources().getDrawable(R.drawable.cornerwhiteback));
                ivpaypal.setImageDrawable(getResources().getDrawable(R.drawable.ic_paypal_ico));
                ivvenmo.setImageDrawable(getResources().getDrawable(R.drawable.ic_active_nvenmo_ico));
                ivcredit.setImageDrawable(getResources().getDrawable(R.drawable.ic_visa));
                tvpaypal.setTextColor(getResources().getColor(R.color.colorDarkgray));
                tvvenmo.setTextColor(getResources().getColor(R.color.colorwhite));
                tvcredit.setTextColor(getResources().getColor(R.color.colorDarkgray));
                tvbilling_address.setVisibility(View.GONE);
                etAddress.setVisibility(View.GONE);
                btn_paymentmethod.setVisibility(View.GONE);


            }
            break;

            case "visa": {
                ptype = "visa";
                rlpaypal.setBackground(getResources().getDrawable(R.drawable.cornerwhiteback));
                rlvenmo.setBackground(getResources().getDrawable(R.drawable.cornerwhiteback));
                rlcredit.setBackground(getResources().getDrawable(R.drawable.cornergrayback));
                ivpaypal.setImageDrawable(getResources().getDrawable(R.drawable.ic_paypal_ico));
                ivvenmo.setImageDrawable(getResources().getDrawable(R.drawable.ic_nvenmo_ico));
                ivcredit.setImageDrawable(getResources().getDrawable(R.drawable.ic_visa));
                tvpaypal.setTextColor(getResources().getColor(R.color.colorDarkgray));
                tvvenmo.setTextColor(getResources().getColor(R.color.colorDarkgray));
                tvcredit.setTextColor(getResources().getColor(R.color.colorwhite));
                tvbilling_address.setVisibility(View.VISIBLE);
                etAddress.setVisibility(View.VISIBLE);
                etAddress.setText(session.getPaymentAddress());
                btn_paymentmethod.setVisibility(View.VISIBLE);

            }
            break;

        }
*/




















