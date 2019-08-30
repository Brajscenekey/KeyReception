package com.key.keyreception.activity.recepnist;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.key.keyreception.R;
import com.key.keyreception.Session;
import com.key.keyreception.activity.model.FirebaseUserModel;
import com.key.keyreception.activity.owner.OwnerTabActivity;
import com.key.keyreception.activity.owner.PaymentdetailActivity;
import com.key.keyreception.base.BaseActivity;
import com.key.keyreception.connection.RetrofitClient;
import com.key.keyreception.helper.Constant;
import com.key.keyreception.helper.PDialog;
import com.key.keyreception.helper.Utility;
import com.key.keyreception.helper.Validation;

import org.json.JSONObject;

import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RsettingActivity extends BaseActivity implements View.OnClickListener {

    private Session session;
    private PDialog pDialog;
    private Utility utility;
    private Validation validation;
    private Switch aSwitch;
    private String notionoff = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rsetting);
        init();
        if (!session.getnotificationStatus().isEmpty()) {
            if (session.getnotificationStatus().equals("0")) {
                aSwitch.setChecked(false);
            } else {
                aSwitch.setChecked(true);
            }
        }
    }

    public void init() {
        RelativeLayout rl_chan_pass = findViewById(R.id.rl_chan_pass);
        RelativeLayout rl_payment_op = findViewById(R.id.rl_payment_op);
        session = new Session(this);
        validation = new Validation(this);
        pDialog = new PDialog();
        utility = new Utility();
        RelativeLayout log = findViewById(R.id.rl_log);
        aSwitch = findViewById(R.id.switch1);
        aSwitch.setOnClickListener(this);
        ImageView iv_leftarrow_setting = findViewById(R.id.iv_leftarrow_setting);
        log.setOnClickListener(this);
        rl_chan_pass.setOnClickListener(this);
        iv_leftarrow_setting.setOnClickListener(this);
        rl_payment_op.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_log: {
                if (utility.checkInternetConnection(this)) {
                    logoutApiData();
                } else {
                    Toast.makeText(this, "Please check your Network", Toast.LENGTH_SHORT).show();
                }
            }
            break;
            case R.id.rl_chan_pass: {
                openAuthorizationDialog();
            }
            break;
            case R.id.iv_leftarrow_setting: {
                backPress();
            }
            break;

            case R.id.rl_payment_op: {

            }
            break;
            case R.id.switch1:
                if (aSwitch.isChecked()) {
                    notionoff = "1";
                    settingNotifiOnOff();
                } else {
                    notionoff = "0";
                    settingNotifiOnOff();
                }
                break;
        }

    }

    @SuppressLint("NewApi")
    private void openAuthorizationDialog() {
        final Dialog dialog = new Dialog(RsettingActivity.this);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        assert window != null;
        WindowManager.LayoutParams wlp = window.getAttributes();
        dialog.setContentView(R.layout.changelayout);
        wlp.gravity = Gravity.CENTER;
        window.setAttributes(wlp);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ImageView ivdissmis = dialog.findViewById(R.id.change_dissmis);
        final EditText etchange_oldpass = dialog.findViewById(R.id.etchange_oldpass);
        final EditText etchange_newpass = dialog.findViewById(R.id.etchange_newpass);
        final EditText etchange_confirmpass = dialog.findViewById(R.id.etchange_confirmpass);
        Button button = dialog.findViewById(R.id.btnchange_pass);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (utility.checkInternetConnection(RsettingActivity.this)) {
                    if (validation.isoldPasswordValid(etchange_oldpass) && validation.isnewPasswordValid(etchange_newpass) && validation.isconfirmPasswordValid(etchange_confirmpass)) {
                        changepassApiData(etchange_oldpass.getText().toString(), etchange_newpass.getText().toString(), etchange_confirmpass.getText().toString());
                        dialog.dismiss();
                    }
                } else {
                    Toast.makeText(RsettingActivity.this, "Please check your Network", Toast.LENGTH_SHORT).show();
                }
            }

        });
        ivdissmis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        dialog.show();

    }

    public void changepassApiData(String old, String newp, String confirm) {
        pDialog.pdialog(RsettingActivity.this);
        String token = session.getAuthtoken();
        final String usertype = session.getusertype();
        Call<ResponseBody> call = RetrofitClient.getInstance()
                .getApi().changePassword(token, old, newp, confirm);
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

                                Toast.makeText(RsettingActivity.this, msg, Toast.LENGTH_SHORT).show();

                                if (usertype.equals("owner")) {
                                    Intent intent = new Intent(RsettingActivity.this, OwnerTabActivity.class);
                                    intent.putExtra("order", "3");
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Intent intent = new Intent(RsettingActivity.this, TabActivity.class);
                                    intent.putExtra("order", "3");
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                }
                            } else {
                                Toast.makeText(RsettingActivity.this, msg, Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(RsettingActivity.this, msg, Toast.LENGTH_SHORT).show();
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

    public void logoutApiData() {
        pDialog.pdialog(RsettingActivity.this);
        String token = session.getAuthtoken();
        Call<ResponseBody> call = RetrofitClient.getInstance()
                .getApi().logout(token);
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
                                chatNotificationOff("");
                                session.isSetUpdateLocIn(false);
                                session.logout();
                                Toast.makeText(RsettingActivity.this, msg, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(RsettingActivity.this, msg, Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(RsettingActivity.this, msg, Toast.LENGTH_SHORT).show();
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

    public void settingNotifiOnOff() {


        pDialog.pdialog(RsettingActivity.this);
        String token = session.getAuthtoken();
        MediaType text = MediaType.parse("text/plain");
        RequestBody notificationStatus1 = RequestBody.create(text, notionoff);
        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().notificationStatus(token, notificationStatus1);

        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @SuppressLint({"SetTextI18n", "NewApi"})
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try {

                    pDialog.hideDialog();

                    switch (response.code()) {
                        case 200: {
                            @SuppressLint({"NewApi", "LocalSuppress"}) String stresult = Objects.requireNonNull(response.body()).string();
                            Log.d("response", stresult);
                            JSONObject jsonObject = new JSONObject(stresult);
                            String statusCode = jsonObject.optString("status");
                            if (statusCode.equals("success")) {
                                session.putnotificationStatus(notionoff);

                                if (notionoff.equals("0"))
                                    chatNotificationOff("");
                                else
                                    chatNotificationOff(FirebaseInstanceId.getInstance().getToken());
                            }
                            break;
                        }
                        case 400: {
                            @SuppressLint({"NewApi", "LocalSuppress"}) String result = Objects.requireNonNull(response.errorBody()).string();
                            Log.d("response400", result);
                            break;
                        }
                        case 401:
                            try {
                                Log.d("ResponseInvalid", Objects.requireNonNull(response.errorBody()).string());
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                            break;
                        case 404:
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
                Log.d("ResponseInvalid", "problem");

            }
        });

    }


    private void chatNotificationOff(String token) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        FirebaseUserModel userModel = new FirebaseUserModel();
        userModel.firebaseToken = token;
        userModel.name = session.getusername();
        userModel.email = session.getuserEmail();
        userModel.profilePic = session.getuserimg();
        userModel.usertype = session.getusertype();
        userModel.uid = session.getuserid();
        database.child(Constant.USER_TABLE).child(session.getuserid()).setValue(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
    }

}


