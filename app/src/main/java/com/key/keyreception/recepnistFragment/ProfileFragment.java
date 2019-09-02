package com.key.keyreception.recepnistFragment;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.key.keyreception.R;
import com.key.keyreception.Session;
import com.key.keyreception.activity.EditActivity;
import com.key.keyreception.activity.owner.OwnerTabActivity;
import com.key.keyreception.activity.recepnist.GetValidatedataActivity;
import com.key.keyreception.activity.recepnist.RsettingActivity;
import com.key.keyreception.base.BaseFragment;
import com.key.keyreception.connection.RetrofitClient;
import com.key.keyreception.helper.PDialog;

import org.json.JSONObject;

import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends BaseFragment implements View.OnClickListener {


    private PDialog pDialog;
    private Session session;
    private ImageView ivprofile;
    private TextView name, email, add, tv_switch;
    private String fullName, mail, address, profileImage, latitude, longitude;
    private Switch ava_switch;
    private String avability = "";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        pDialog = new PDialog();
        session = new Session(mContext);
        init(view);
        if (!session.getAvabilityStatus().isEmpty()) {
            if (session.getAvabilityStatus().equals("0")) {
                ava_switch.setChecked(false);
            } else {
                ava_switch.setChecked(true);
            }
        }
    }

    public void init(View view) {
        RelativeLayout setting = view.findViewById(R.id.rl_setting);
        ImageView iv_edit = view.findViewById(R.id.iv_editprofile);
        ivprofile = view.findViewById(R.id.rprof_img);
        name = view.findViewById(R.id.rpro_name);
        email = view.findViewById(R.id.rpro_email);
        add = view.findViewById(R.id.rpro_add);
        RelativeLayout rl_owner_switch_resep = view.findViewById(R.id.rl_owner_switch_resep);
        tv_switch = view.findViewById(R.id.tv_switch);
        ava_switch = view.findViewById(R.id.ava_switch);
        RelativeLayout rl_valid_your_acc = view.findViewById(R.id.rl_valid_your_acc);
        rl_valid_your_acc.setOnClickListener(this);
        ava_switch.setOnClickListener(this);
        setting.setOnClickListener(this);
        rl_owner_switch_resep.setOnClickListener(this);
        iv_edit.setOnClickListener(this);
        ProfileApiData();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_setting: {
                Intent intent = new Intent(mContext, RsettingActivity.class);
                intent.putExtra("bi","1");
                startActivity(intent);
            }
            break;
            case R.id.rl_valid_your_acc: {
                Intent intent = new Intent(mContext, GetValidatedataActivity.class);
                startActivity(intent);
            }
            break;

            case R.id.rl_owner_switch_resep: {
                switchUserApiData("owner");
            }
            break;
            case R.id.iv_editprofile: {
                Intent intent = new Intent(mContext, EditActivity.class);
                intent.putExtra("fullName", fullName);
                intent.putExtra("address", address);
                intent.putExtra("mail", mail);
                intent.putExtra("profileImage", profileImage);
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
                startActivity(intent);
            }
            break;

            case R.id.ava_switch:
                if (ava_switch.isChecked()) {
                    avability = "1";
                    availabilityStatus();
                } else {
                    avability = "0";
                    availabilityStatus();
                }
                break;
        }
    }

    @SuppressLint("NewApi")
    public void ProfileApiData() {


        pDialog.pdialog(mContext);
        String token = session.getAuthtoken();
        Call<ResponseBody> call = RetrofitClient.getInstance()
                .getApi().userInfo(token);
//
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
                                fullName = jsonObject2.getString("fullName");
                                mail = jsonObject2.getString("email");
                                address = jsonObject2.getString("address");
                                profileImage = jsonObject2.getString("profileImage");
                                latitude = jsonObject2.getString("latitude");
                                longitude = jsonObject2.getString("longitude");
                                setData(fullName, mail, address, profileImage);
                            } else {
                                Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();

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
                                Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                            }
                            break;
                        }
                        case 401:
                            try {
                                session.logout();
                                Toast.makeText(mContext, "Session expired, please login again.", Toast.LENGTH_SHORT).show();
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

    @SuppressLint("NewApi")
    public void switchUserApiData(String owner) {
        pDialog.pdialog(mContext);
        String token = session.getAuthtoken();
        String usertype = session.getusertype();
        Call<ResponseBody> call = RetrofitClient.getInstance()
                .getApi().switchUser(token, owner);
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
                            String isProfileComplete = jsonObject.optString("isProfileComplete");
                            if (statusCode.equals("success")) {
                                session.putusertype(getResources().getString(R.string.own));
                                session.putusertype("owner");
                                Intent intent = new Intent(mContext, OwnerTabActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                            }
                            break;
                        }
                        case 401:

                            try {
                                session.logout();
                                Toast.makeText(mContext, "Session expired, please login again.", Toast.LENGTH_SHORT).show();
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
    public void setData(String name1, String email1, String address1, String image1) {
        name.setText(name1);
        email.setText(email1);
        add.setText(address1);

        if (image1.length() != 0) {
            RequestOptions options = new RequestOptions();
            options.placeholder(R.drawable.ic_user_ico);
            options.diskCacheStrategy(DiskCacheStrategy.RESOURCE);
            Glide.with(mContext).load(image1).apply(options).into(ivprofile);
        }
    }

    public void availabilityStatus() {
        pDialog.pdialog(mContext);
        String token = session.getAuthtoken();
        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().availabilityStatus(token, avability);
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
                                session.putAvabilityStatus(avability);
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
}



