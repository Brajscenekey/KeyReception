package com.key.keyreception.activity.recepnist;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.key.keyreception.R;
import com.key.keyreception.Session;
import com.key.keyreception.connection.RetrofitClient;
import com.key.keyreception.helper.ProgressDialog;
import com.key.keyreception.helper.Utility;
import com.key.keyreception.helper.Validation;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class BankinfoEditActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_holderFName, et_holderLName, et_accountNumber, et_routingNumber;
    private ProgressDialog pDialog;
    private Session session;
    private Validation validation;
    private Utility utility;
    private String accountId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bankinfoedit);
        inItView();
        getBankInfoApi();
    }

    private void inItView() {
        pDialog = new ProgressDialog(this);
        session = new Session(this);
        utility = new Utility();
        validation = new Validation(this);
        et_holderFName = findViewById(R.id.et_holderFName);
        et_holderLName = findViewById(R.id.et_holderLName);
        et_accountNumber = findViewById(R.id.et_accountNumber);
        et_routingNumber = findViewById(R.id.et_routingNumber);
        ImageView iv_back_icon = findViewById(R.id.iv_back_icon);
        Button btn_addcard = findViewById(R.id.btn_addcard);
        btn_addcard.setOnClickListener(this);
        iv_back_icon.setOnClickListener(this);
    }

    private void bankInfoValidation() {
        if (utility.checkInternetConnection(this)) {
            if (validation.isAHFNameValid(et_holderFName) && validation.isAHLNameValid(et_holderLName) &&
                    validation.isARountingValid(et_routingNumber) && validation.isANumberValid(et_accountNumber))

            {
                postBankInfoApi();
            }
        } else {
            Toast.makeText(this, "Please check your network", Toast.LENGTH_SHORT).show();
        }
    }

    public void postBankInfoApi() {
        pDialog.show();
        String authtoken = session.getAuthtoken();

        Call<ResponseBody> call = RetrofitClient.getInstance()
                .getApi().stripeUpdateAccount(authtoken, et_holderFName.getText().toString(), et_holderLName.getText().toString(),et_routingNumber.getText().toString()
                        ,et_accountNumber.getText().toString(),"US","USD","individual",session.getaccountId());
        call.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("NewApi")
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull retrofit2.Response<ResponseBody> response) {
                try {
                    pDialog.dismiss();
                    switch (response.code()) {
                        case 200: {
                            String stresult = Objects.requireNonNull(response.body()).string();
                            Log.d("response", stresult);
                            JSONObject jsonObject = new JSONObject(stresult);
                            String statusCode = jsonObject.optString("status");
                            String msg = jsonObject.optString("message");
                            if (statusCode.equals("success")) {
                                JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                                accountId = jsonObject1.getString("accountId");
//acct_1FDW6rLM6jhk1iPK
                                Toast.makeText(BankinfoEditActivity.this, msg, Toast.LENGTH_SHORT).show();
                                onBackPressed();
                            } else {
                                Toast.makeText(BankinfoEditActivity.this, msg, Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(BankinfoEditActivity.this, msg, Toast.LENGTH_SHORT).show();
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
                pDialog.dismiss();
            }
        });
    }

    public void getBankInfoApi() {
        pDialog.show();
        String authtoken = session.getAuthtoken();
        String aid = session.getaccountId();
        Call<ResponseBody> call = RetrofitClient.getInstance()
                .getApi().stripeGetAccount(authtoken,aid);
        call.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("NewApi")
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull retrofit2.Response<ResponseBody> response) {
                try {
                    pDialog.dismiss();
                    switch (response.code()) {
                        case 200: {
                            String stresult = Objects.requireNonNull(response.body()).string();
                            Log.d("response", stresult);
                            JSONObject jsonObject = new JSONObject(stresult);
                            String statusCode = jsonObject.optString("status");
                            String msg = jsonObject.optString("message");
                            if (statusCode.equals("sucess")) {
                                JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                                JSONObject legal_entity = jsonObject1.getJSONObject("legal_entity");
                               String first_name = legal_entity.getString("first_name");
                               String last_name = legal_entity.getString("last_name");
                               JSONObject external_accounts = jsonObject1.getJSONObject("external_accounts");
                                JSONArray jsonArray = external_accounts.getJSONArray("data");
                                JSONObject jsonObject2 = jsonArray.getJSONObject(0);
                                String routing_number = jsonObject2.getString("routing_number");

                                et_holderFName.setText(first_name);
                                et_holderLName.setText(last_name);
                                et_routingNumber.setText(routing_number);


                            } else {
                                Toast.makeText(BankinfoEditActivity.this, msg, Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(BankinfoEditActivity.this, msg, Toast.LENGTH_SHORT).show();
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
                pDialog.dismiss();
            }
        });
    }


    @Override
    public void onClick(View view) {

        switch (view.getId())
        {

            case R.id.btn_addcard:{
                bankInfoValidation();
            }
            break;

            case R.id.iv_back_icon:{
                onBackPressed();
            }
            break;
        }

    }
}
