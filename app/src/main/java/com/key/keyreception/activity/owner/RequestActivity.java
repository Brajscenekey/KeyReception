package com.key.keyreception.activity.owner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.key.keyreception.activity.ActivityAdapter.RequestAdapter;
import com.key.keyreception.activity.model.MyJobData;
import com.key.keyreception.activity.model.RequestData;
import com.key.keyreception.activity.recepnist.TabActivity;
import com.key.keyreception.base.BaseActivity;
import com.key.keyreception.connection.RetrofitClient;
import com.key.keyreception.helper.PDialog;
import com.key.keyreception.helper.Utility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class RequestActivity extends BaseActivity {
    String propertyImage = "";
    private String expireTime;
    private RequestAdapter adapter;
    private PDialog pDialog;
    private Session session;
    private TextView tv_request_no_record;
    private Utility utility;
    private List<RequestData> requestDataList = new ArrayList<>();
    private List<RequestData.CategoryBean> categoryList = new ArrayList<>();
    private List<RequestData.ReciverDetailBean> recievelist = new ArrayList<>();
    private List<RequestData.SenderDetailBean> sendlist = new ArrayList<>();
    private List<RequestData.JobDetailBean> joblist = new ArrayList<>();
    private String propertyName, address, sdfullName, sdprofileImage, serviceDate, reqid, currentTime, jobid, senderid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        pDialog = new PDialog();
        utility = new Utility();
        session = new Session(this);
        init();

    }

    public void init() {
        RecyclerView recyclerView = findViewById(R.id.request_recycler_view);
        tv_request_no_record = findViewById(R.id.tv_request_no_record);
        ImageView iv_leftarrow_request = findViewById(R.id.iv_leftarrow_request);
        iv_leftarrow_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RequestActivity.this, TabActivity.class);
                startActivity(intent);
            }
        });

        adapter = new RequestAdapter(this, requestDataList, new RequestAdapter.ListenerActiveData() {
            @Override
            public void pos(int id, View view) {

                reqid = String.valueOf(requestDataList.get(id).get_id());
                jobid = String.valueOf(requestDataList.get(id).getJobId());
                senderid = String.valueOf(requestDataList.get(id).getSenderId());
                RequestDetailApiData(reqid, view);

            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        if (utility.checkInternetConnection(this)) {
            requestApiData();
        } else {
            Toast.makeText(this, "Please check your network", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("NewApi")
    public void requestApiData() {
        pDialog.pdialog(this);
        String token = session.getAuthtoken();
        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().RequestList(token,
                "", "20");
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull retrofit2.Response<ResponseBody> response) {

                try {
                    pDialog.hideDialog();
                    switch (response.code()) {
                        case 200: {

                            String stresult = Objects.requireNonNull(response.body()).string();
                            Log.d("response", stresult);
                            requestDataList.clear();
                            JSONObject jsonObject = new JSONObject(stresult);
                            String statusCode = jsonObject.optString("status");
                            if (statusCode.equals("success")) {
                                tv_request_no_record.setVisibility(View.GONE);
                                JSONArray jsonArray = jsonObject.optJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    RequestData requestData;
                                    JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                                    int _id = jsonObject2.getInt("_id");
                                    int jobId = jsonObject2.getInt("jobId");
                                    int senderId = jsonObject2.getInt("senderId");
                                    int receiverId = jsonObject2.getInt("receiverId");
                                    String requestTime = jsonObject2.getString("requestTime");
                                    String expireTime = jsonObject2.getString("expireTime");
                                    JSONArray jsonArray1 = jsonObject2.getJSONArray("category");
                                    for (int j = 0; j < jsonArray1.length(); j++) {
                                        RequestData.CategoryBean categoryBean;
                                        JSONObject jsonObject21 = jsonArray1.getJSONObject(j);
                                        int _cid = jsonObject21.getInt("_id");
                                        String title = jsonObject21.getString("title");
                                        categoryBean = new RequestData.CategoryBean(_cid, title);
                                        categoryList.add(categoryBean);

                                    }
                                    JSONArray jsonArray2 = jsonObject2.getJSONArray("senderDetail");
                                    for (int k = 0; k < jsonArray2.length(); k++) {
                                        RequestData.SenderDetailBean senderDetailBean;
                                        JSONObject jsonObject3 = jsonArray2.getJSONObject(k);
                                        int _sdid = jsonObject3.getInt("_id");
                                        String sdprofileImage = jsonObject3.getString("profileImage");
                                        String sdfullName = jsonObject3.getString("fullName");
                                        senderDetailBean = new RequestData.SenderDetailBean(_sdid, sdprofileImage, sdfullName);
                                        sendlist.add(senderDetailBean);
                                    }

                                    JSONArray jsonArray3 = jsonObject2.getJSONArray("reciverDetail");
                                    for (int l = 0; l < jsonArray3.length(); l++) {
                                        RequestData.ReciverDetailBean reciverDetailBean;
                                        JSONObject jsonObject4 = jsonArray3.getJSONObject(l);
                                        int _sdid = jsonObject4.getInt("_id");
                                        String sdprofileImage = jsonObject4.getString("profileImage");
                                        String sdfullName = jsonObject4.getString("fullName");
                                        reciverDetailBean = new RequestData.ReciverDetailBean(_sdid, sdprofileImage, sdfullName);
                                        recievelist.add(reciverDetailBean);
                                    }
                                    List<MyJobData.PropertyImgBean> imglist = new ArrayList<>();
                                    JSONArray jsonArray6 = jsonObject2.getJSONArray("propertyImg");
                                    for (int m = 0; m < jsonArray6.length(); m++) {

                                        MyJobData.PropertyImgBean propertyImgBean;
                                        JSONObject jsonObject5 = jsonArray6.getJSONObject(m);
                                        int _id1 = jsonObject5.getInt("_id");
                                        String propertyImage = jsonObject5.getString("propertyImage");

                                        propertyImgBean = new MyJobData.PropertyImgBean(_id1, propertyImage);
                                        imglist.add(propertyImgBean);
                                    }
                                    JSONArray jsonArray4 = jsonObject2.getJSONArray("jobDetail");
                                    for (int m = 0; m < jsonArray4.length(); m++) {
                                        RequestData.JobDetailBean jobDetailBean;
                                        JSONObject jsonObject5 = jsonArray4.getJSONObject(m);
                                        String price = jsonObject5.getString("price");
                                        String serviceDate = jsonObject5.getString("serviceDate");
                                        String checkIn = jsonObject5.getString("checkIn");
                                        String checkOut = jsonObject5.getString("checkOut");
                                        String description = jsonObject5.getString("description");
                                        int status = jsonObject5.getInt("status");
                                        JSONArray jsonArraydata = jsonObject5.getJSONArray("propertyData");
                                        JSONObject jsonObjectdata = jsonArraydata.getJSONObject(0);
                                        String bathroom = jsonObjectdata.getString("bathroom");
                                        String bedroom = jsonObjectdata.getString("bedroom");
                                        String address = jsonObjectdata.getString("propertyAddress");
                                        String propertyId = jsonObjectdata.getString("propertyId");
                                        String latitude = jsonObjectdata.getString("propertyLat");
                                        String longitude = jsonObjectdata.getString("propertyLong");
                                        String propertyName = jsonObjectdata.getString("propertyName");
                                        String propertySize = jsonObjectdata.getString("propertySize");
                                        jobDetailBean = new RequestData.JobDetailBean(propertyId, propertyName, bedroom, bathroom, price, propertySize, serviceDate, checkIn, checkOut, address, latitude, longitude, description, status);

                                        joblist.add(jobDetailBean);
                                    }

                                    requestData = new RequestData(_id, jobId, senderId, receiverId, requestTime, expireTime, categoryList, sendlist, recievelist, joblist, imglist);
                                    requestDataList.add(requestData);
                                }
                                adapter.notifyDataSetChanged();

                            } else {
                                tv_request_no_record.setVisibility(View.VISIBLE);

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
                                Toast.makeText(RequestActivity.this, msg, Toast.LENGTH_SHORT).show();

                            }
                            break;
                        }
                        case 401:

                            try {
                                session.logout();
                                Toast.makeText(RequestActivity.this, "Session expired, please login again.", Toast.LENGTH_SHORT).show();
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
    private void initiatePopupWindow(View v) {
        try {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            LayoutInflater inflater = (LayoutInflater) RequestActivity.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert inflater != null;
            final View layout = inflater.inflate(R.layout.requestdetail,
                    (ViewGroup) findViewById(R.id.req_popup));
            final PopupWindow popUp = new PopupWindow(layout, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    true);
            popUp.showAtLocation(v, Gravity.CENTER, 0, 0);
            Button accept = layout.findViewById(R.id.dialog_accept);
            TextView tv_rdetail_propname = layout.findViewById(R.id.tv_rdetail_propname);
            TextView tv_rdetail_propaddress = layout.findViewById(R.id.tv_rdetail_propaddress);
            TextView tv_rdetail_sdate = layout.findViewById(R.id.tv_rdetail_sdate);
            final TextView tv_rdetail_timer = layout.findViewById(R.id.tv_rdetail_timer); //Timer text
            TextView tv_rdetail_vname = layout.findViewById(R.id.tv_rdetail_vname);
            final TextView timeout = layout.findViewById(R.id.timeout);
            final LinearLayout li_btn = layout.findViewById(R.id.li_btn);
            ImageView iv_rdetail_vimg = layout.findViewById(R.id.iv_rdetail_vimg);
            ImageView iv_selecttype_image = layout.findViewById(R.id.iv_selecttype_image);
            ImageView pop_back = layout.findViewById(R.id.pop_back);
            tv_rdetail_propname.setText(propertyName);
            tv_rdetail_propaddress.setText(address);
            try {
                if (serviceDate.contains("-")) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
                    Date date = sdf.parse(serviceDate);
                    SimpleDateFormat mSDF = new SimpleDateFormat("dd, MMMM hh:mm a", Locale.getDefault());
                    String fDate = mSDF.format(date);
                    tv_rdetail_sdate.setText(fDate);
                } else {
                    tv_rdetail_sdate.setText(serviceDate);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            pop_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    popUp.dismiss();
                }
            });
            if (sdprofileImage.length() != 0) {
                RequestOptions options = new RequestOptions();
                options.placeholder(R.drawable.ic_user_ico);
                options.diskCacheStrategy(DiskCacheStrategy.RESOURCE);
                Glide.with(RequestActivity.this).load(sdprofileImage).apply(options).into(iv_rdetail_vimg);
            }
            if (propertyImage.length() != 0) {
                RequestOptions options = new RequestOptions();
                options.placeholder(R.drawable.ic_new_property_img);
                options.diskCacheStrategy(DiskCacheStrategy.RESOURCE);
                Glide.with(RequestActivity.this).load(propertyImage).apply(options).into(iv_selecttype_image);
            }
            tv_rdetail_vname.setText(sdfullName);
            popUp.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

                }
            });
            accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!session.getJobStatus().isEmpty()) {
                        if (session.getJobStatus().equals("1")) {
                            alertJobStatus();
                        } else {
                            popUp.dismiss();
                            acceptRejectApiData("2", popUp);
                            session.putJobStatus("1");
                        }
                    }

                }
            });

            Button reject = layout.findViewById(R.id.dialog_reject);
            reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popUp.dismiss();
                    acceptRejectApiData("1", popUp);
                }
            });

            if (currentTime != null && expireTime != null) {
                Date cDate = getCurrentDate(currentTime);
                Date eDate = getCurrentDate(expireTime);
                final Long diffMillis = eDate.getTime() - cDate.getTime();
                Log.d("DATE", "C" + new Date().toString() + " E: " + eDate.toString());
                new CountDownTimer(diffMillis, 1000) {

                    public void onTick(long millisUntilFinished) {
                        NumberFormat f = new DecimalFormat("00");
                        long secondsInMilli = 1000;
                        long minutesInMilli = secondsInMilli * 60;
                        long hoursInMilli = minutesInMilli * 60;
                        long hours = millisUntilFinished / hoursInMilli;
                        millisUntilFinished = millisUntilFinished % hoursInMilli;
                        long min = millisUntilFinished / minutesInMilli;
                        millisUntilFinished = millisUntilFinished % minutesInMilli;
                        long sec = millisUntilFinished / secondsInMilli;
                        tv_rdetail_timer.setText(f.format(hours) + ":" + f.format(min) + ":" + f.format(sec));
                    }

                    public void onFinish() {
                        //ToDo: Write here if needed after finished
                        li_btn.setVisibility(View.GONE);
                        tv_rdetail_timer.setVisibility(View.GONE);
                        timeout.setText("Time Out");
                        timeout.setTextColor(getResources().getColor(R.color.appcolor));
                    }
                }.start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void RequestDetailApiData(String id, final View view) {
        String authtoken = session.getAuthtoken();
        Call<ResponseBody> call = RetrofitClient.getInstance()
                .getApi().RequestDetail(authtoken, "application/x-www-form-urlencoded", id);
        call.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("NewApi")
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull retrofit2.Response<ResponseBody> response) {
                try {

                    switch (response.code()) {
                        case 200: {
                            String stresult = Objects.requireNonNull(response.body()).string();
                            Log.d("response", stresult);
                            JSONObject jsonObject = new JSONObject(stresult);
                            String statusCode = jsonObject.optString("status");
                            String msg = jsonObject.optString("message");
                            if (statusCode.equals("success")) {
                                JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                                expireTime = jsonObject1.getString("expireTime");
                                currentTime = jsonObject1.getString("currentTime");
                                JSONArray jsonArray2 = jsonObject1.getJSONArray("senderDetail");
                                for (int k = 0; k < jsonArray2.length(); k++) {
                                    JSONObject jsonObject3 = jsonArray2.getJSONObject(k);
                                    sdprofileImage = jsonObject3.getString("profileImage");
                                    sdfullName = jsonObject3.getString("fullName");
                                }
                                JSONArray jsonArray3 = jsonObject1.getJSONArray("propertyImg");
                                if (jsonArray3.length() > 0) {
                                    JSONObject jsonObject6 = jsonArray3.getJSONObject(0);
                                    propertyImage = jsonObject6.getString("propertyImage");
                                }

                                JSONArray jsonArray4 = jsonObject1.getJSONArray("jobDetail");
                                for (int m = 0; m < jsonArray4.length(); m++) {
                                    JSONObject jsonObject5 = jsonArray4.getJSONObject(m);
                                    serviceDate = jsonObject5.getString("serviceDate");
                                    JSONArray jsonArraydata = jsonObject5.getJSONArray("propertyData");
                                    JSONObject jsonObjectdata = jsonArraydata.getJSONObject(0);
                                    address = jsonObjectdata.getString("propertyAddress");
                                    propertyName = jsonObjectdata.getString("propertyName");
                                }
                                initiatePopupWindow(view);
                            } else {
                                Toast.makeText(RequestActivity.this, msg, Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(RequestActivity.this, msg, Toast.LENGTH_SHORT).show();
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

    private Date getCurrentDate(String cDate) {
        Log.d("DATE", "C> " + cDate);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.getDefault());
        try {
            return sdf.parse(cDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void acceptRejectApiData(String status, final PopupWindow popUp) {
        String authtoken = session.getAuthtoken();
        Call<ResponseBody> call = RetrofitClient.getInstance()
                .getApi().acceptReject(authtoken, "application/x-www-form-urlencoded", jobid, reqid, senderid, status, "0");
        call.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("NewApi")
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull retrofit2.Response<ResponseBody> response) {
                try {

                    switch (response.code()) {
                        case 200: {
                            String stresult = Objects.requireNonNull(response.body()).string();
                            Log.d("response", stresult);
                            JSONObject jsonObject = new JSONObject(stresult);
                            String statusCode = jsonObject.optString("status");
                            String msg = jsonObject.optString("message");
                            if (statusCode.equals("success")) {
                                requestApiData();
                                popUp.dismiss();
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);


                            } else {
                                Toast.makeText(RequestActivity.this, msg, Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(RequestActivity.this, msg, Toast.LENGTH_SHORT).show();
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


    public void alertJobStatus() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(RequestActivity.this);

        alertDialog.setTitle("Alert");
        alertDialog.setMessage("First complete your previous job");
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }


}

