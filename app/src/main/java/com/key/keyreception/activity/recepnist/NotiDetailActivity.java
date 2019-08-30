package com.key.keyreception.activity.recepnist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.key.keyreception.R;
import com.key.keyreception.Session;
import com.key.keyreception.base.BaseActivity;
import com.key.keyreception.connection.RetrofitClient;
import com.key.keyreception.helper.PDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class NotiDetailActivity extends BaseActivity implements View.OnClickListener {

    Button btn_appodetail_changestatus;
    private PDialog pDialog;
    private Session session;
    private TextView tvpropname, tvaddress, tvbedroom, tvbathroom, tvpropsize, tvpropprice, tvserdate, tvstatus, tvservicename, tvdescription, tvvendorname;
    private ImageView serviceimage, rece_detailimg;
    private ImageView vendorimage;
    private ImageView ivchat;
    private String reqid, senderid, status;
    private String profileImage = "", fullName = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        setContentView(R.layout.activity_appo_detail);
        session = new Session(this);
        pDialog = new PDialog();
        init();

        if (getIntent().getStringExtra("notificationDetail_Jobid") != null) {
            String reference_id = getIntent().getStringExtra("notificationDetail_Jobid");
            session.putjobid(reference_id);
        }
        jobdetailApiData();
    }

    private void init() {
        tvpropname = findViewById(R.id.tv_appo_detail_pname);
        tvaddress = findViewById(R.id.tv_appo_detail_address);
        tvbedroom = findViewById(R.id.tv_appo_detail_bedroom);
        tvbathroom = findViewById(R.id.tv_appo_detail_bathroom);
        tvpropsize = findViewById(R.id.tv_appo_detail_psize);
        tvpropprice = findViewById(R.id.tv_appo_detail_price);
        tvserdate = findViewById(R.id.tv_appo_detail_sdate);
        tvstatus = findViewById(R.id.tv_appo_detail_status);
        tvservicename = findViewById(R.id.tv_appo_detail_servicename);
        tvdescription = findViewById(R.id.tv_appo_detail_description);
        tvvendorname = findViewById(R.id.tv_appo_detail_vendorname);
        btn_appodetail_changestatus = findViewById(R.id.btn_appodetail_changestatus);
        rece_detailimg = findViewById(R.id.rece_detailimg);
        ImageView ivback = findViewById(R.id.iv_appo_detail_back);
        ivback.setOnClickListener(this);
        btn_appodetail_changestatus.setOnClickListener(this);
        serviceimage = findViewById(R.id.iv_appo_detail_serviceimage);
        vendorimage = findViewById(R.id.iv_appo_detail_vendorimage);
        ivchat = findViewById(R.id.iv_appo_detail_vendorchat);
        ivchat.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.iv_appo_detail_back:
                backPress();
                break;
            case R.id.btn_appodetail_changestatus:
                initiatePopupWindow();
                break;
            case R.id.iv_appo_detail_vendorchat: {
                /*Intent intent1 = new Intent(this,ChattingActivity.class);
                intent1.putExtra("id",senderid);
                intent1.putExtra("profileImage",profileImage);
                intent1.putExtra("fullName",fullName);
                startActivity(intent1);
                finish();*/
            }
            break;
        }
    }

    public void jobdetailApiData() {
        pDialog.pdialog(NotiDetailActivity.this);
        String authtoken = session.getAuthtoken();
        String jobid = session.getjobid();
        Call<ResponseBody> call = RetrofitClient.getInstance()
                .getApi().jobDetail(authtoken, jobid);
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
                                reqid = jsonObject2.getString("_id");
                                String price = jsonObject2.getString("price");
                                String serviceDate = jsonObject2.getString("serviceDate");
                                String description = jsonObject2.getString("description");
                                status = jsonObject2.getString("status");
                                JSONArray jsonArray1 = jsonObject2.optJSONArray("ownerDetail");
                                JSONObject jsonObject211 = jsonArray1.getJSONObject(0);
                                senderid = jsonObject211.getString("_id");
                                profileImage = jsonObject211.getString("profileImage");
                                fullName = jsonObject211.getString("fullName");
                                JSONArray jsonArray = jsonObject2.optJSONArray("category");
                                JSONObject jsonObject21 = jsonArray.getJSONObject(0);
                                String title = jsonObject21.getString("title");
                                String image = jsonObject21.getString("image");
                                String propertyImage = "";
                                JSONArray jsonArray3 = jsonObject2.getJSONArray("propertyImg");
                                if (jsonArray3.length() > 0) {
                                    JSONObject jsonObject5 = jsonArray3.getJSONObject(0);
                                    propertyImage = jsonObject5.getString("propertyImage");
                                }
                                JSONArray jsonArraydata = jsonObject2.getJSONArray("propertyData");
                                JSONObject jsonObjectdata = jsonArraydata.getJSONObject(0);
                                String bathroom = jsonObjectdata.getString("bathroom");
                                String bedroom = jsonObjectdata.getString("bedroom");
                                String address = jsonObjectdata.getString("propertyAddress");
                                String propertyName = jsonObjectdata.getString("propertyName");
                                String propertySize = jsonObjectdata.getString("propertySize");
                                setDetailData(propertyName, address, bedroom, bathroom, propertySize, price, serviceDate, status, title, description, image, fullName, profileImage, propertyImage);
                            } else {
                                Toast.makeText(NotiDetailActivity.this, msg, Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(NotiDetailActivity.this, msg, Toast.LENGTH_SHORT).show();
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

    @SuppressLint({"SetTextI18n", "CheckResult"})
    private void setDetailData(String pname, String add, String bed, String bath, String psize, String price, String stdate, String status, String sname, String des, String simage, String ownname, String ownimage, String propimg) {
        tvpropname.setText(pname);
        tvaddress.setText(add);
        tvbedroom.setText(bed + " Bedroom");
        tvbathroom.setText(bath + " Bathroom");
        tvpropsize.setText(psize + " Sq Feet");
        tvpropprice.setText("$" + price);
        if (propimg.length() != 0) {
            RequestOptions options = new RequestOptions();
            options.placeholder(R.drawable.home_image);
            options.diskCacheStrategy(DiskCacheStrategy.RESOURCE);
            Glide.with(NotiDetailActivity.this).load(propimg).apply(options).into(rece_detailimg);
        }
        try {
            if (stdate.contains("-")) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
                Date date = sdf.parse(stdate);
                SimpleDateFormat mSDF = new SimpleDateFormat("dd, MMMM hh:mm a", Locale.getDefault());
                String fDate = mSDF.format(date);
                tvserdate.setText(fDate);
            } else {
                tvserdate.setText(stdate);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        switch (status) {
            case "1":
                tvstatus.setText(getResources().getString(R.string.Pending));
                tvstatus.setTextColor(getResources().getColor(R.color.colorPending));
                btn_appodetail_changestatus.setVisibility(View.GONE);

                break;
            case "2":
                tvstatus.setText(getResources().getString(R.string.confirm));
                tvstatus.setTextColor(getResources().getColor(R.color.appcolor));
                btn_appodetail_changestatus.setVisibility(View.VISIBLE);


                break;
            case "3":
                tvstatus.setText(getResources().getString(R.string.inpro));
                tvstatus.setTextColor(getResources().getColor(R.color.colorInprogress));
                btn_appodetail_changestatus.setVisibility(View.VISIBLE);


                break;
            case "5":
                tvstatus.setText(getResources().getString(R.string.paymentcomplete));
                tvstatus.setTextColor(getResources().getColor(R.color.colorDarkGreen));
                btn_appodetail_changestatus.setVisibility(View.GONE);
                break;
            default:
                tvstatus.setText(getResources().getString(R.string.complete));
                tvstatus.setTextColor(getResources().getColor(R.color.colorDarkGreen));
                btn_appodetail_changestatus.setVisibility(View.GONE);

                break;
        }
        tvservicename.setText(sname);
        tvdescription.setText(des);
        tvvendorname.setText(ownname);
        if (simage.length() != 0) {
            Glide.with(NotiDetailActivity.this).load(simage).into(serviceimage);
        }
        if (ownimage.length() != 0) {
            Glide.with(NotiDetailActivity.this).load(ownimage).into(vendorimage);
        }
    }

    private void initiatePopupWindow() {
        try {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            LayoutInflater inflater = (LayoutInflater) NotiDetailActivity.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert inflater != null;
            final View layout = inflater.inflate(R.layout.statuspop,
                    (ViewGroup) findViewById(R.id.popup_element));
            final PopupWindow popUp = new PopupWindow(layout, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    true);
            LinearLayout ll_start_service = layout.findViewById(R.id.ll_start_service);
            LinearLayout ll_inprogress_service = layout.findViewById(R.id.ll_inprogress_service);
            LinearLayout ll_end_service = layout.findViewById(R.id.ll_end_service);
            final ImageView iv_start_service = layout.findViewById(R.id.iv_start_service);
            final ImageView iv_inprogress_service = layout.findViewById(R.id.iv_inprogress_service);
            final ImageView iv_end_service = layout.findViewById(R.id.iv_end_service);
            final TextView tv_start_service = layout.findViewById(R.id.tv_start_service);
            final TextView tv_inprogress_service = layout.findViewById(R.id.tv_inprogress_service);
            final TextView tv_end_service = layout.findViewById(R.id.tv_end_service);

            switch (status) {
                case "2":
                    iv_start_service.setBackground(getResources().getDrawable(R.drawable.circlechat));
                    iv_start_service.setColorFilter(ContextCompat.getColor(NotiDetailActivity.this, R.color.colorwhite));
                    tv_start_service.setTextColor(getResources().getColor(R.color.colorPrimary));
                    iv_inprogress_service.setBackground(getResources().getDrawable(R.drawable.circletransparent));
                    iv_inprogress_service.setColorFilter(ContextCompat.getColor(NotiDetailActivity.this, R.color.colorgray));
                    tv_inprogress_service.setTextColor(getResources().getColor(R.color.colorgray));
                    iv_end_service.setBackground(getResources().getDrawable(R.drawable.circletransparent));
                    iv_end_service.setColorFilter(ContextCompat.getColor(NotiDetailActivity.this, R.color.colorgray));
                    tv_end_service.setTextColor(getResources().getColor(R.color.colorgray));
                    btn_appodetail_changestatus.setVisibility(View.VISIBLE);
                    break;
                case "3":
                    iv_start_service.setBackground(getResources().getDrawable(R.drawable.circletransparent));
                    iv_start_service.setColorFilter(ContextCompat.getColor(NotiDetailActivity.this, R.color.colorgray));
                    tv_start_service.setTextColor(getResources().getColor(R.color.colorgray));
                    iv_inprogress_service.setBackground(getResources().getDrawable(R.drawable.circlechat));
                    iv_inprogress_service.setColorFilter(ContextCompat.getColor(NotiDetailActivity.this, R.color.colorwhite));
                    tv_inprogress_service.setTextColor(getResources().getColor(R.color.colorPrimary));
                    iv_end_service.setBackground(getResources().getDrawable(R.drawable.circletransparent));
                    iv_end_service.setColorFilter(ContextCompat.getColor(NotiDetailActivity.this, R.color.colorgray));
                    tv_end_service.setTextColor(getResources().getColor(R.color.colorgray));
                    btn_appodetail_changestatus.setVisibility(View.VISIBLE);
                    break;
                case "4":
                    iv_start_service.setBackground(getResources().getDrawable(R.drawable.circletransparent));
                    iv_start_service.setColorFilter(ContextCompat.getColor(NotiDetailActivity.this, R.color.colorgray));
                    tv_start_service.setTextColor(getResources().getColor(R.color.colorgray));
                    iv_inprogress_service.setBackground(getResources().getDrawable(R.drawable.circletransparent));
                    iv_inprogress_service.setColorFilter(ContextCompat.getColor(NotiDetailActivity.this, R.color.colorgray));
                    tv_inprogress_service.setTextColor(getResources().getColor(R.color.colorgray));
                    iv_end_service.setBackground(getResources().getDrawable(R.drawable.circlechat));
                    iv_end_service.setColorFilter(ContextCompat.getColor(NotiDetailActivity.this, R.color.colorwhite));
                    tv_end_service.setTextColor(getResources().getColor(R.color.colorPrimary));
                    btn_appodetail_changestatus.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }


            ll_start_service.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    status = "2";
                    iv_start_service.setBackground(getResources().getDrawable(R.drawable.circlechat));
                    iv_start_service.setColorFilter(ContextCompat.getColor(NotiDetailActivity.this, R.color.colorwhite));
                    tv_start_service.setTextColor(getResources().getColor(R.color.colorPrimary));

                    iv_inprogress_service.setBackground(getResources().getDrawable(R.drawable.circletransparent));
                    iv_inprogress_service.setColorFilter(ContextCompat.getColor(NotiDetailActivity.this, R.color.colorgray));
                    tv_inprogress_service.setTextColor(getResources().getColor(R.color.colorgray));

                    iv_end_service.setBackground(getResources().getDrawable(R.drawable.circletransparent));
                    iv_end_service.setColorFilter(ContextCompat.getColor(NotiDetailActivity.this, R.color.colorgray));
                    tv_end_service.setTextColor(getResources().getColor(R.color.colorgray));

                }
            });

            ll_inprogress_service.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    status = "3";
                    iv_start_service.setBackground(getResources().getDrawable(R.drawable.circletransparent));
                    iv_start_service.setColorFilter(ContextCompat.getColor(NotiDetailActivity.this, R.color.colorgray));
                    tv_start_service.setTextColor(getResources().getColor(R.color.colorgray));

                    iv_inprogress_service.setBackground(getResources().getDrawable(R.drawable.circlechat));
                    iv_inprogress_service.setColorFilter(ContextCompat.getColor(NotiDetailActivity.this, R.color.colorwhite));
                    tv_inprogress_service.setTextColor(getResources().getColor(R.color.colorPrimary));

                    iv_end_service.setBackground(getResources().getDrawable(R.drawable.circletransparent));
                    iv_end_service.setColorFilter(ContextCompat.getColor(NotiDetailActivity.this, R.color.colorgray));
                    tv_end_service.setTextColor(getResources().getColor(R.color.colorgray));

                }

            });

            ll_end_service.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    status = "4";
                    iv_start_service.setBackground(getResources().getDrawable(R.drawable.circletransparent));
                    iv_start_service.setColorFilter(ContextCompat.getColor(NotiDetailActivity.this, R.color.colorgray));
                    tv_start_service.setTextColor(getResources().getColor(R.color.colorgray));

                    iv_inprogress_service.setBackground(getResources().getDrawable(R.drawable.circletransparent));
                    iv_inprogress_service.setColorFilter(ContextCompat.getColor(NotiDetailActivity.this, R.color.colorgray));
                    tv_inprogress_service.setTextColor(getResources().getColor(R.color.colorgray));

                    iv_end_service.setBackground(getResources().getDrawable(R.drawable.circlechat));
                    iv_end_service.setColorFilter(ContextCompat.getColor(NotiDetailActivity.this, R.color.colorwhite));
                    tv_end_service.setTextColor(getResources().getColor(R.color.colorPrimary));


                }
            });

            popUp.showAtLocation(layout, Gravity.CENTER, 0, 0);
            popUp.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

                }
            });

            Button cancel = layout.findViewById(R.id.dialog_status_cancel);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    popUp.dismiss();

                }
            });

            Button done = layout.findViewById(R.id.dialog_status_done);
            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (status.equals("4")) {
                        session.putJobStatus("0");
                    }
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    popUp.dismiss();
                    statusCheckApiData(status);

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void statusCheckApiData(String status) {
        pDialog.pdialog(NotiDetailActivity.this);
        String authtoken = session.getAuthtoken();
        Call<ResponseBody> call = RetrofitClient.getInstance()
                .getApi().acceptReject(authtoken, "application/x-www-form-urlencoded", reqid, reqid, senderid, status, "1");
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

                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

                                jobdetailApiData();

                            } else {
                                Toast.makeText(NotiDetailActivity.this, msg, Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(NotiDetailActivity.this, msg, Toast.LENGTH_SHORT).show();
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
