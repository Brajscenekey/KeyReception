package com.key.keyreception.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.key.keyreception.R;
import com.key.keyreception.connection.RetrofitClient;
import com.key.keyreception.helper.PDialog;
import com.key.keyreception.helper.Utility;
import com.key.keyreception.helper.Validation;

import org.json.JSONObject;

import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;


public class ForgotActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etmail;
    private Validation validation;
    private Utility utility;
    private PDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rece_forgot);
        validation = new Validation(this);
        utility = new Utility();
        pDialog = new PDialog();
        init();
    }

    private void init() {
        etmail = findViewById(R.id.forgot_emailaddress);
        Button btnsend = findViewById(R.id.btn_forgot);
        ImageView ivback = findViewById(R.id.iv_leftarrow_forgot);
        btnsend.setOnClickListener(this);
        ivback.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_forgot: {
                if (utility.checkInternetConnection(this)) {
                    if (validation.isEmailValid(etmail)) {
                        forgotApiData();
                    }
                } else {
                    Toast.makeText(this, "Please Check your network", Toast.LENGTH_SHORT).show();
                }
            }
            break;
            case R.id.iv_leftarrow_forgot: {
                Intent intent = new Intent(ForgotActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ForgotActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @SuppressLint("NewApi")
    public void forgotApiData() {


        pDialog.pdialog(ForgotActivity.this);

        String mail = etmail.getText().toString().trim();
        Call<ResponseBody> call = RetrofitClient.getInstance()
                .getApi().forgot(mail);
//
        call.enqueue(new Callback<ResponseBody>() {

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
                                Intent intent = new Intent(ForgotActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                                Toast.makeText(ForgotActivity.this, msg, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ForgotActivity.this, msg, Toast.LENGTH_SHORT).show();

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
                                Toast.makeText(ForgotActivity.this, msg, Toast.LENGTH_SHORT).show();

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


