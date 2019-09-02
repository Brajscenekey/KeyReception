package com.key.keyreception.ownerFragment;


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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.key.keyreception.R;
import com.key.keyreception.Session;
import com.key.keyreception.activity.EditActivity;
import com.key.keyreception.activity.owner.PayOptionActivity;
import com.key.keyreception.activity.recepnist.CompleteValidActivity;
import com.key.keyreception.activity.recepnist.RsettingActivity;
import com.key.keyreception.activity.recepnist.TabActivity;
import com.key.keyreception.base.BaseFragment;
import com.key.keyreception.connection.RetrofitClient;
import com.key.keyreception.helper.PDialog;

import org.json.JSONObject;

import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * A simple {@link Fragment} subclass.
 */
public class OwnerProfileFragment extends BaseFragment implements View.OnClickListener {


    private PDialog pDialog;
    private Session session;
    private ImageView ivprofile;
    private TextView name, email, add;
    private String fullName, mail, address, profileImage, latitude, longitude;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_owner_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        pDialog = new PDialog();
        session = new Session(mContext);
        init(view);
    }

    public void init(View view) {
        RelativeLayout setting = view.findViewById(R.id.rl_owner_setting);
        RelativeLayout rl_owner_pay = view.findViewById(R.id.rl_owner_pay);
        ImageView iv_edit = view.findViewById(R.id.iv_owner_edit);
        RelativeLayout service = view.findViewById(R.id.rl_owner_service);
        ivprofile = view.findViewById(R.id.owner_prof_img);
        name = view.findViewById(R.id.owner_pro_name);
        email = view.findViewById(R.id.owner_pro_email);
        add = view.findViewById(R.id.owner_pro_add);
        RelativeLayout rl_owner_switch_resep = view.findViewById(R.id.rl_owner_switch_resep);
        rl_owner_switch_resep.setOnClickListener(this);
        setting.setOnClickListener(this);
        iv_edit.setOnClickListener(this);
        service.setOnClickListener(this);
        rl_owner_pay.setOnClickListener(this);
        ProfileApiData();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_owner_pay: {
                Intent intent = new Intent(mContext, PayOptionActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.rl_owner_setting: {
                Intent intent = new Intent(mContext, RsettingActivity.class);
                intent.putExtra("bi","0");
                startActivity(intent);
            }
            break;
            case R.id.rl_owner_switch_resep: {
                switchUserApiData("receptionist");
            }
            break;
            case R.id.iv_owner_edit: {
                Intent intent = new Intent(mContext, EditActivity.class);
                intent.putExtra("fullName", fullName);
                intent.putExtra("address", address);
                intent.putExtra("profileImage", profileImage);
                intent.putExtra("mail", mail);
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
                startActivity(intent);
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
                                String billingAddress = jsonObject2.getString("billingAddress");
                                session.putPaymentAddress(billingAddress);
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
    public void switchUserApiData(final String owner) {


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
                                if (isProfileComplete.equals("0")) {
                                    Intent intent = new Intent(mContext, CompleteValidActivity.class);
                                    startActivity(intent);
                                } else {
                                    session.putusertype(owner);
                                    Intent intent = new Intent(mContext, TabActivity.class);
                                    startActivity(intent);
                                }
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


}




