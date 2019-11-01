package com.key.keyreception.activity.recepnist;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.key.keyreception.R;
import com.key.keyreception.Session;
import com.key.keyreception.base.BaseActivity;
import com.key.keyreception.connection.RetrofitClient;
import com.key.keyreception.helper.PDialog;

import org.json.JSONObject;

import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class GetValidatedataActivity extends BaseActivity implements View.OnClickListener {

    private EditText et_verified_socialnumber;
    private ImageView iv_verified_lic;
    private PDialog pDialog;
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_validatedata);
        pDialog = new PDialog();
        session = new Session(this);
        init();
        documentApiData();
    }

    public void init() {
        et_verified_socialnumber = findViewById(R.id.et_verified_socialnumber);
        iv_verified_lic = findViewById(R.id.iv_verified_lic);
        ImageView iv_leftarrow_forgot = findViewById(R.id.iv_leftarrow_forgot);
        iv_leftarrow_forgot.setOnClickListener(this);
    }

    @SuppressLint("NewApi")
    public void documentApiData() {
        pDialog.pdialog(this);
        String token = session.getAuthtoken();
        Call<ResponseBody> call = RetrofitClient.getInstance()
                .getApi().userInfo(token);
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
                                JSONObject jsonObject2 = jsonObject.getJSONObject("data");
                                String document = jsonObject2.getString("document");
                                String securityNumber = jsonObject2.getString("securityNumber");
                                setData(document, securityNumber);
                            } else {
                                Toast.makeText(GetValidatedataActivity.this, msg, Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(GetValidatedataActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }
                            break;
                        }
                        case 401:

                            try {
                                session.logout();
                                Toast.makeText(GetValidatedataActivity.this, "Session expired, please login again.", Toast.LENGTH_SHORT).show();
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

    @SuppressLint("CheckResult")
    public void setData(String doc, String serviceno) {
        et_verified_socialnumber.setText(serviceno);
        if (doc.length() != 0) {
            RequestOptions options = new RequestOptions();
            options.placeholder(R.drawable.ic_plus_box_img);
            options.diskCacheStrategy(DiskCacheStrategy.RESOURCE);
            Glide.with(this).load(doc).apply(options).into(iv_verified_lic);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_leftarrow_forgot: {
                backPress();
            }

        }
    }
}
