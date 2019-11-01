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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.key.keyreception.activity.model.FirebaseUserModel;
import com.key.keyreception.activity.owner.OwnerTabActivity;
import com.key.keyreception.activity.recepnist.TabActivity;
import com.key.keyreception.R;
import com.key.keyreception.Session;
import com.key.keyreception.connection.RetrofitClient;
import com.key.keyreception.helper.Constant;
import com.key.keyreception.helper.PDialog;
import com.key.keyreception.helper.Utility;
import com.key.keyreception.helper.Validation;

import org.json.JSONObject;

import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Validation validation;
    private Utility utility;
    private EditText etmail, etpassword;
    private PDialog pDialog;
    private Session session;
    private String refreshedToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recep_login);
        validation = new Validation(this);
        session = new Session(this);
        pDialog = new PDialog();
        utility = new Utility();
        init();

        //device token
        refreshedToken = FirebaseInstanceId.getInstance().getToken();

    }

    private void init() {
        TextView tvforgot = findViewById(R.id.tv_forget);
        TextView tvsingup = findViewById(R.id.logtv_signUp);
        Button buttonsignin = findViewById(R.id.logbtn_signin);
        etmail = findViewById(R.id.loget_email);
        etpassword = findViewById(R.id.loget_password);
        ImageView ivback = findViewById(R.id.rlogin_im_leftarrow);
        buttonsignin.setOnClickListener(this);
        tvsingup.setOnClickListener(this);
        tvforgot.setOnClickListener(this);
        ivback.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_forget: {
                Intent intent = new Intent(LoginActivity.this, ForgotActivity.class);
                startActivity(intent);
                finish();
            }
            break;
            case R.id.logtv_signUp: {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
                finish();
            }
            break;
            case R.id.rlogin_im_leftarrow: {
                onBackPressed();
            }
            break;
            case R.id.logbtn_signin: {
                if (utility.checkInternetConnection(this)) {
                    if (validation.isEmailValid(etmail) && validation.isPasswordValid(etpassword)) {

                        loginApiData();
                    }

                } else {
                    Toast.makeText(this, "Please check your Network", Toast.LENGTH_SHORT).show();
                }
            }
            break;

        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(LoginActivity.this, UserTypeActivity.class);
        startActivity(intent);
        finish();
    }

    public void loginApiData() {
        pDialog.pdialog(LoginActivity.this);
        String mail = etmail.getText().toString().trim();
        String pass = etpassword.getText().toString();
        String usertype = session.getusertype();
        Call<ResponseBody> call = RetrofitClient.getInstance()
                .getApi().login(mail, pass, usertype, "1", refreshedToken);
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
                                JSONObject jsonObject2 = jsonObject.getJSONObject("data");
                                String authToken = jsonObject2.getString("authToken");
                                String securityNumber = jsonObject2.getString("securityNumber");
                                String document = jsonObject2.getString("document");
                                String userType = jsonObject2.getString("userType");
                                String fullName = jsonObject2.getString("fullName");
                                String email = jsonObject2.getString("email");
                                String availabilityStatus = jsonObject2.getString("availabilityStatus");
                                String jobStatus = jsonObject2.getString("jobStatus");
                                session.putJobStatus(jobStatus);
                                String stripeAccountId = jsonObject2.getString("stripeAccountId");
                                session.putaccountId(stripeAccountId);
                                String stripeCustomerId = jsonObject2.getString("stripeCustomerId");
                                session.putstripeCustomerId(stripeCustomerId);
                                session.putAvabilityStatus(availabilityStatus);
                                session.isSetUpdateLocIn(true);
                                session.putEmailId(email);
                                String profileImage = jsonObject2.getString("profileImage");
                                String _id = String.valueOf(jsonObject2.getInt("_id"));
                                int notificationStatus = jsonObject2.getInt("notificationStatus");
                                String paymentType = jsonObject2.getString("paymentType");
                                int isBankAccountAdd = jsonObject2.getInt("isBankAccountAdd");
                                session.putisBankAccountAdd(String.valueOf(isBankAccountAdd));
                                session.putPayment(paymentType);
                                session.putAuthtoken(authToken);
                                session.putusertype(userType);
                                session.putuserdata(_id,fullName,profileImage,userType);
                                session.putnotificationStatus(String.valueOf(notificationStatus));
                                session.putEmailId(email);
                                addUserFirebaseDatabase(fullName,email,profileImage,userType,authToken,_id);
                                if (userType.equals("owner")) {
                                    session.issetLoggedIn(true);

                                    Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(LoginActivity.this, OwnerTabActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {

                                    if (securityNumber.isEmpty() || document.isEmpty()) {
                                        if (!securityNumber.isEmpty()) {
                                            session.issetLoggedIn(true);
                                            Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(LoginActivity.this, TabActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else if (!document.isEmpty()) {
                                            session.issetLoggedIn(true);
                                            Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(LoginActivity.this, TabActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Intent intent = new Intent(LoginActivity.this, ValidateAccountActivity.class);
                                            startActivity(intent);
                                            finish();

                                        }
                                    }
                                    else {
                                        session.issetLoggedIn(true);
                                        Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(LoginActivity.this, TabActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }

                                }

                            } else {
                                Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
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
    private void addUserFirebaseDatabase(String name,String email,String pimg,String usertype,String authT,String uId ) {


        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        String device_token = FirebaseInstanceId.getInstance().getToken();
        FirebaseUserModel userModel = new FirebaseUserModel();
        userModel.firebaseToken = device_token;
        userModel.name = name;
        userModel.email =email;
        userModel.profilePic = pimg;
        userModel.usertype=usertype;
        userModel.uid = uId;
        database.child(Constant.USER_TABLE).child(uId).setValue(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
    }
}
