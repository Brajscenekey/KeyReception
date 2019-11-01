package com.key.keyreception.activity.owner;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.key.keyreception.R;
import com.key.keyreception.Session;
import com.key.keyreception.activity.DetailActivity;
import com.key.keyreception.connection.RetrofitClient;
import com.key.keyreception.helper.ProgressDialog;
import com.key.keyreception.helper.Utility;
import com.key.keyreception.helper.Validation;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.Token;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class MakePaymentCardActivity extends AppCompatActivity implements View.OnClickListener {

    private ProgressDialog pDialog;
    private Session session;
    private String error;
    private EditText etcardnum, etexdate, etcvv;
    private int width, year1, month1;
    private Utility utility;
    private Validation validation;
    private String paymenttype = "paypal", jobId, receiverId, amount, token = "";
    private int lastClick = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_payment_card);
        init();
    }

    private void init() {
        pDialog = new ProgressDialog(this);
        utility = new Utility();
        validation = new Validation(this);
        session = new Session(MakePaymentCardActivity.this);
        etcardnum = findViewById(R.id.etcardnum);
        etexdate = findViewById(R.id.etexdate);
        etcvv = findViewById(R.id.etcvv);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        width = displaymetrics.widthPixels;
        ImageView ivBack = findViewById(R.id.ivBack);
        Button btn_addcard = findViewById(R.id.btn_addcard);
        ivBack.setOnClickListener(this);
        etexdate.setOnClickListener(this);
        btn_addcard.setOnClickListener(this);
        jobId = session.getjobId();
        receiverId = session.getreceiverId();
        amount = session.getamount();



    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.etexdate:
                showMonthYearDialog();
                break;
            case R.id.ivBack:
                onBackPressed();
                break;
            case R.id.btn_addcard:
                     cardpaymanage();

                break;

        }

    }

    public void cardpaymanage() {


        if (utility.checkInternetConnection(this)) {
            if (validation.isCNumValid(etcardnum) && validation.isEDateValid(etexdate) && validation.isCvvValid(etcvv)) {
                if (lastClick != R.id.btn_addcard) {
                    lastClick = R.id.btn_addcard;

                    MakePaymentCardActivity.AsyncTaskRunner asyncTaskRunner = new MakePaymentCardActivity.AsyncTaskRunner();
                asyncTaskRunner.execute();}
            }
        } else {

            Toast.makeText(this, "Please check your network", Toast.LENGTH_SHORT).show();

        }
    }

    @SuppressLint("StaticFieldLeak")
    private void saveCreditCard(final String id) {
        pDialog.show();

        new AsyncTask<Void, Void, Customer>() {
            @Override
            protected Customer doInBackground(Void... voids) {
                Stripe.apiKey = getResources().getString(R.string.sk_test);

                Customer customer = null;
                try {

                  String customer_stripe_id = session.getstripeCustomerId();

                    customer = Customer.retrieve(customer_stripe_id);
                    Map<String, Object> params = new HashMap<>();
                    params.put("source", id);
                    customer.getSources().create(params);

                } catch (StripeException e) {
                    e.printStackTrace();
                }
                return customer;
            }

            @Override
            protected void onPostExecute(Customer customer) {
                super.onPostExecute(customer);
                hideDialog();
                if (customer != null) {
                    Toast.makeText(MakePaymentCardActivity.this, "Your card is saved successfully.", Toast.LENGTH_SHORT).show();

                    finish();
                } else {
                    lastClick =0;
                    Toast.makeText(MakePaymentCardActivity.this, "Stripe Error", Toast.LENGTH_SHORT).show();

                }
            }
        }.execute();

    }

    private void hideDialog() {
        pDialog.dismiss();
    }

    //*************show  MonthYear  Dialog *******************//
    @SuppressLint("NewApi")
    private void showMonthYearDialog() {
        final int year = Calendar.getInstance().get(Calendar.YEAR);
        final int month = Calendar.getInstance().get(Calendar.MONTH);
        final Dialog yearDialog;
        yearDialog = new Dialog(this);
        yearDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        yearDialog.setContentView(R.layout.dialog_month_year);
        Objects.requireNonNull(yearDialog.getWindow()).setLayout((width * 10) / 11, WindowManager.LayoutParams.WRAP_CONTENT);
        Button set = yearDialog.findViewById(R.id.button1);
        Button cancel = yearDialog.findViewById(R.id.button2);
        final NumberPicker yearPicker = yearDialog.findViewById(R.id.numberPicker1);
        final NumberPicker monthPicker = yearDialog.findViewById(R.id.numberPicker2);

        monthPicker.setMinValue(1);
        monthPicker.setMaxValue(12);
        monthPicker.setWrapSelectorWheel(false);
        monthPicker.setValue(month);
        monthPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        yearPicker.setMaxValue(2050);
        yearPicker.setMinValue(year);
        yearPicker.setWrapSelectorWheel(false);
        yearPicker.setValue(year);
        yearPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        set.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                String year = String.valueOf(yearPicker.getValue());
                year1 = yearPicker.getValue();
                month1 = monthPicker.getValue();
                etexdate.setText(month1 + "/" + year1);
                yearDialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yearDialog.dismiss();
            }
        });
        yearDialog.show();
    }

    @SuppressLint("StaticFieldLeak")
    private class AsyncTaskRunner extends AsyncTask<Void, Void, Token> {

        @Override
        protected Token doInBackground(Void... voids) {
            Stripe.apiKey = getResources().getString(R.string.sk_test);

            Token token = null;
            Map<String, Object> tokenParams = new HashMap<>();
            Map<String, Object> cardParams = new HashMap<>();
            cardParams.put("number", etcardnum.getText().toString().trim());
            cardParams.put("exp_month", month1);
            cardParams.put("exp_year", year1);
            cardParams.put("cvc", etcvv.getText().toString().trim());
            tokenParams.put("card", cardParams);
            try {
                token = Token.create(tokenParams);
            } catch (Exception e) {
                error = e.getLocalizedMessage();


                Log.e("error", e.getMessage());
            }
            return token;
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected void onPostExecute(Token token) {
            super.onPostExecute(token);
            if (token != null) {
                jobPaymentApi(token.getId());
            } else {
                lastClick =0;
                Toast.makeText(MakePaymentCardActivity.this, error, Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void jobPaymentApi(String tokenid) {
        pDialog.show();
        String token = session.getAuthtoken();
        String accid = session.getaccountId();
        String cusid = session.getstripeCustomerId();
        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().jobPayment(token,
                jobId, receiverId, amount, paymenttype, tokenid, "token", accid, cusid);

        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @SuppressLint("NewApi")
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull retrofit2.Response<ResponseBody> response) {

                try {
                    pDialog.dismiss();


                    switch (response.code()) {
                        case 200: {

                            @SuppressLint({"NewApi", "LocalSuppress"}) String stresult = Objects.requireNonNull(response.body()).string();
                            Log.d("response", stresult);
                            JSONObject jsonObject = new JSONObject(stresult);
                            String statusCode = jsonObject.optString("status");
                            String msg = jsonObject.optString("message");
                            if (statusCode.equals("success")) {
                                Toast.makeText(MakePaymentCardActivity.this, msg, Toast.LENGTH_SHORT).show();
                                Intent intent  = new Intent(MakePaymentCardActivity.this, DetailActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            } else {
                                lastClick =0;
                                Toast.makeText(MakePaymentCardActivity.this, msg, Toast.LENGTH_SHORT).show();

                            }

                            break;
                        }
                        case 400: {
                            @SuppressLint({"NewApi", "LocalSuppress"}) String result = Objects.requireNonNull(response.errorBody()).string();
                            Log.d("response400", result);
                            JSONObject jsonObject = new JSONObject(result);
                            String statusCode = jsonObject.optString("status");
                            String msg = jsonObject.optString("message");
                            if (statusCode.equals("true")) {
                                Toast.makeText(MakePaymentCardActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }

                            break;
                        }
                        case 401:

                            try {
                                session.logout();
                                Toast.makeText(MakePaymentCardActivity.this, "Session expired, please login again.", Toast.LENGTH_SHORT).show();
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

                pDialog.dismiss();


            }
        });

    }
}
