package com.key.keyreception.Activity.Recepnist;

import android.annotation.SuppressLint;
import android.app.Dialog;
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
import android.widget.Toast;

import com.key.keyreception.R;
import com.key.keyreception.Session;
import com.key.keyreception.base.BaseActivity;
import com.key.keyreception.connection.RetrofitClient;
import com.key.keyreception.helper.PDialog;
import com.key.keyreception.helper.Utility;
import com.key.keyreception.helper.Validation;

import org.json.JSONObject;

import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class RsettingActivity extends BaseActivity implements View.OnClickListener {

    private Session session;
    private PDialog pDialog;
    private Utility utility;
    private Validation validation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rsetting);
        init();
    }

    public void init() {
        RelativeLayout rl_chan_pass = findViewById(R.id.rl_chan_pass);
        session = new Session(this);
        validation = new Validation(this);
        pDialog = new PDialog();
        utility = new Utility();
        RelativeLayout log = findViewById(R.id.rl_log);
        ImageView iv_leftarrow_setting = findViewById(R.id.iv_leftarrow_setting);
        log.setOnClickListener(this);
        rl_chan_pass.setOnClickListener(this);
        iv_leftarrow_setting.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_log: {
                session.logout();
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
        wlp.gravity = Gravity.TOP;
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
}
