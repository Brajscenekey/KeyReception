package com.key.keyreception.activity.recepnist;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.key.keyreception.R;
import com.key.keyreception.Session;
import com.key.keyreception.activity.owner.RequestActivity;
import com.key.keyreception.base.BaseActivity;
import com.key.keyreception.connection.RetrofitClient;
import com.key.keyreception.helper.PDialog;
import com.key.keyreception.recepnistFragment.AppointmentFragment;
import com.key.keyreception.recepnistFragment.EarningFragment;
import com.key.keyreception.recepnistFragment.MessageFragment;
import com.key.keyreception.recepnistFragment.NotificationFragment;
import com.key.keyreception.recepnistFragment.ProfileFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class TabActivity extends BaseActivity implements View.OnClickListener {


    public TextView tv_head;
    public RelativeLayout rtlv_one, rtlv_two, rtlv_three, rtlv_four, rtlv_five, headrl;
    private ImageView appoint, not, chart, pro, chat, requsetlist;
    private Fragment fragment = null;
    private boolean doubleBackToExitPressedOnce;
    private Runnable runnable;
    private View view1, view2, view3, view4, view5;
    private Session session;
    private PDialog pDialog;
    private String propertyImage = "";
    private String propertyName;
    private String address;
    private String sdfullName;
    private String sdprofileImage;
    private String serviceDate;
    private String expireTime;
    private String currentTime;
    private String jobid;
    private String senderid;
    private String reqid;
    private int lastClick = 0;

    private BroadcastReceiver myReceiver = new BroadcastReceiver() {


        public void onReceive(Context context, Intent intent) {
            if (intent.getStringExtra("notifyId") != null) {
                String rid = intent.getStringExtra("notifyId");
                RequestDetailApiData(rid);
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(myReceiver, new IntentFilter("BroadcastNotification"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(myReceiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decor = getWindow().getDecorView();
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.activity_tab);
        session = new Session(this);
        pDialog = new PDialog();
        init();
        if (getIntent() != null && getIntent().hasExtra("checkid")) {
            alertNFManage();
        }
        if (getIntent().getStringExtra("notifyId") != null) {
            String reference_id = getIntent().getStringExtra("notifyId");
            RequestDetailApiData(reference_id);
        }

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("order")) {
            String s = intent.getStringExtra("order");

            if (s.equals("3")) {

                if (lastClick != R.id.rtlv_five) {
                    lastClick = R.id.rtlv_five;
                    tv_head.setText(getResources().getString(R.string.profile));
                    fragment = new ProfileFragment();
                    displaySelectedFragment(fragment);
                    headrl.setVisibility(View.GONE);
                    appoint.setImageResource(R.drawable.ic_inactive_appointment);
                    chart.setImageResource(R.drawable.ic_inactive_bar_chart);
                    chat.setImageResource(R.drawable.ic_inactive_chat_ico);
                    not.setImageResource(R.drawable.ic_inactive_ball_ico);
                    pro.setImageResource(R.drawable.ic_active_user);
                    view1.setVisibility(View.GONE);
                    view2.setVisibility(View.GONE);
                    view3.setVisibility(View.GONE);
                    view4.setVisibility(View.GONE);
                    view5.setVisibility(View.VISIBLE);
                }
            } else if (s.equals("12")) {
                if (lastClick != R.id.rtlv_one) {
                    lastClick = R.id.rtlv_one;
                    tv_head.setText(getResources().getString(R.string.appo));
                    fragment = new AppointmentFragment();
                    displaySelectedFragment(fragment);
                    appoint.setImageResource(R.drawable.ic_active_appointment);
                    chart.setImageResource(R.drawable.ic_inactive_bar_chart);
                    chat.setImageResource(R.drawable.ic_inactive_chat_ico);
                    not.setImageResource(R.drawable.ic_inactive_ball_ico);
                    pro.setImageResource(R.drawable.ic_inactive_user);
                    view1.setVisibility(View.VISIBLE);
                    view2.setVisibility(View.GONE);
                    view3.setVisibility(View.GONE);
                    view4.setVisibility(View.GONE);
                    view5.setVisibility(View.GONE);
                }
            }
        } else {
            if (lastClick != R.id.rtlv_one) {
                lastClick = R.id.rtlv_one;
                tv_head.setText(getResources().getString(R.string.appo));
                fragment = new AppointmentFragment();
                displaySelectedFragment(fragment);
                appoint.setImageResource(R.drawable.ic_active_appointment);
                chart.setImageResource(R.drawable.ic_inactive_bar_chart);
                chat.setImageResource(R.drawable.ic_inactive_chat_ico);
                not.setImageResource(R.drawable.ic_inactive_ball_ico);
                pro.setImageResource(R.drawable.ic_inactive_user);
                view1.setVisibility(View.VISIBLE);
                view2.setVisibility(View.GONE);
                view3.setVisibility(View.GONE);
                view4.setVisibility(View.GONE);
                view5.setVisibility(View.GONE);
            }
        }
    }

    public void init() {
        tv_head = findViewById(R.id.tv_head);
        headrl = findViewById(R.id.chomerr);
        requsetlist = findViewById(R.id.request_list);
        appoint = findViewById(R.id.img_one);
        chat = findViewById(R.id.img_two);
        chart = findViewById(R.id.img_three);
        not = findViewById(R.id.img_four);
        pro = findViewById(R.id.img_five);
        view1 = findViewById(R.id.view1);
        view2 = findViewById(R.id.view2);
        view3 = findViewById(R.id.view3);
        view4 = findViewById(R.id.view4);
        view5 = findViewById(R.id.view5);
        rtlv_one = findViewById(R.id.rtlv_one);
        rtlv_two = findViewById(R.id.rtlv_two);
        rtlv_three = findViewById(R.id.rtlv_three);
        rtlv_four = findViewById(R.id.rtlv_four);
        rtlv_five = findViewById(R.id.rtlv_five);
        rtlv_one.setOnClickListener(this);
        rtlv_two.setOnClickListener(this);
        rtlv_three.setOnClickListener(this);
        rtlv_four.setOnClickListener(this);
        rtlv_five.setOnClickListener(this);
        requsetlist.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.rtlv_one:

                if (lastClick != R.id.rtlv_one) {
                    lastClick = R.id.rtlv_one;
                    tv_head.setText(getResources().getString(R.string.appo));
                    fragment = new AppointmentFragment();
                    displaySelectedFragment(fragment);
                    updateTabView(R.id.rtlv_one);
                    headrl.setVisibility(View.VISIBLE);
                    requsetlist.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.rtlv_two:
                if (lastClick != R.id.rtlv_two) {
                    lastClick = R.id.rtlv_two;
                    tv_head.setText(getResources().getString(R.string.mess));
                    fragment = new MessageFragment();
                    displaySelectedFragment(fragment);
                    updateTabView(R.id.rtlv_two);
                    headrl.setVisibility(View.VISIBLE);
                    requsetlist.setVisibility(View.GONE);
                }
                break;
            case R.id.rtlv_three:
                if (lastClick != R.id.rtlv_three) {
                    lastClick = R.id.rtlv_three;
                    tv_head.setText(getResources().getString(R.string.earn));
                    fragment = new EarningFragment();
                    displaySelectedFragment(fragment);
                    updateTabView(R.id.rtlv_three);
                    headrl.setVisibility(View.VISIBLE);
                    requsetlist.setVisibility(View.GONE);
                }
                break;
            case R.id.rtlv_four:
                if (lastClick != R.id.rtlv_four) {
                    lastClick = R.id.rtlv_four;
                    tv_head.setText(getResources().getString(R.string.not));
                    fragment = new NotificationFragment();
                    displaySelectedFragment(fragment);
                    updateTabView(R.id.rtlv_four);
                    headrl.setVisibility(View.VISIBLE);
                    requsetlist.setVisibility(View.GONE);
                }
                break;
            case R.id.rtlv_five:
                if (lastClick != R.id.rtlv_five) {
                    lastClick = R.id.rtlv_five;
                    tv_head.setText(getResources().getString(R.string.profile));
                    fragment = new ProfileFragment();
                    displaySelectedFragment(fragment);
                    updateTabView(R.id.rtlv_five);
                    headrl.setVisibility(View.GONE);
                }
                break;
            case R.id.request_list:
                Intent intent = new Intent(TabActivity.this, RequestActivity.class);
                startActivity(intent);
                break;
            default:
                if (lastClick != R.id.rtlv_one) {
                    lastClick = R.id.rtlv_one;
                    tv_head.setText(getResources().getString(R.string.appo));
                    fragment = new AppointmentFragment();
                    displaySelectedFragment(fragment);
                    updateTabView(R.id.rtlv_one);
                    headrl.setVisibility(View.VISIBLE);
                    requsetlist.setVisibility(View.VISIBLE);
                }
                break;

        }
    }


    private void updateTabView(int flag) {
        switch (flag) {
            case R.id.rtlv_one:
                appoint.setImageResource(R.drawable.ic_active_appointment);
                chart.setImageResource(R.drawable.ic_inactive_bar_chart);
                chat.setImageResource(R.drawable.ic_inactive_chat_ico);
                not.setImageResource(R.drawable.ic_inactive_ball_ico);
                pro.setImageResource(R.drawable.ic_inactive_user);
                view1.setVisibility(View.VISIBLE);
                view2.setVisibility(View.GONE);
                view3.setVisibility(View.GONE);
                view4.setVisibility(View.GONE);
                view5.setVisibility(View.GONE);
                break;

            case R.id.rtlv_two:
                appoint.setImageResource(R.drawable.ic_inactive_appointment);
                chart.setImageResource(R.drawable.ic_inactive_bar_chart);
                chat.setImageResource(R.drawable.ic_active_chat_ico);
                not.setImageResource(R.drawable.ic_inactive_ball_ico);
                pro.setImageResource(R.drawable.ic_inactive_user);
                view1.setVisibility(View.GONE);
                view2.setVisibility(View.VISIBLE);
                view3.setVisibility(View.GONE);
                view4.setVisibility(View.GONE);
                view5.setVisibility(View.GONE);
                break;
            case R.id.rtlv_three:
                appoint.setImageResource(R.drawable.ic_inactive_appointment);
                chart.setImageResource(R.drawable.ic_active_bar_chart);
                chat.setImageResource(R.drawable.ic_inactive_chat_ico);
                not.setImageResource(R.drawable.ic_inactive_ball_ico);
                pro.setImageResource(R.drawable.ic_inactive_user);
                view1.setVisibility(View.GONE);
                view2.setVisibility(View.GONE);
                view3.setVisibility(View.VISIBLE);
                view4.setVisibility(View.GONE);
                view5.setVisibility(View.GONE);
                break;
            case R.id.rtlv_four:
                appoint.setImageResource(R.drawable.ic_inactive_appointment);
                chart.setImageResource(R.drawable.ic_inactive_bar_chart);
                chat.setImageResource(R.drawable.ic_inactive_chat_ico);
                not.setImageResource(R.drawable.ic_active_ball_ico);
                pro.setImageResource(R.drawable.ic_inactive_user);
                view1.setVisibility(View.GONE);
                view2.setVisibility(View.GONE);
                view3.setVisibility(View.GONE);
                view4.setVisibility(View.VISIBLE);
                view5.setVisibility(View.GONE);
                break;

            case R.id.rtlv_five:
                appoint.setImageResource(R.drawable.ic_inactive_appointment);
                chart.setImageResource(R.drawable.ic_inactive_bar_chart);
                chat.setImageResource(R.drawable.ic_inactive_chat_ico);
                not.setImageResource(R.drawable.ic_inactive_ball_ico);
                pro.setImageResource(R.drawable.ic_active_user);
                view1.setVisibility(View.GONE);
                view2.setVisibility(View.GONE);
                view3.setVisibility(View.GONE);
                view4.setVisibility(View.GONE);
                view5.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void displaySelectedFragment(Fragment fragment) {
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.commit();
    }


    @SuppressLint("NewApi")
    @Override
    public void onBackPressed() {
        /* Handle double click to finish activity*/
        Handler handler = new Handler();
        FragmentManager fm = getSupportFragmentManager();
        int i = fm.getBackStackEntryCount();
        if (i > 0) {
            fm.popBackStack();
        } else if (!doubleBackToExitPressedOnce) {

            doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Click again to exit", Toast.LENGTH_SHORT).show();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);

        } else {
            handler.removeCallbacks(runnable);
            finishAffinity();
        }
    }

    @SuppressLint("CheckResult")
    private void initiatePopupWindow() {
        try {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            LayoutInflater inflater = (LayoutInflater) TabActivity.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert inflater != null;
            final View layout = inflater.inflate(R.layout.requestdetail,
                    (ViewGroup) findViewById(R.id.req_popup));
            final PopupWindow popUp = new PopupWindow(layout, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    true);
            popUp.showAtLocation(layout, Gravity.CENTER, 0, 0);

            Button accept = layout.findViewById(R.id.dialog_accept);
            TextView tv_rdetail_propname = layout.findViewById(R.id.tv_rdetail_propname);
            TextView tv_rdetail_propaddress = layout.findViewById(R.id.tv_rdetail_propaddress);
            TextView tv_rdetail_sdate = layout.findViewById(R.id.tv_rdetail_sdate);
            final TextView tv_rdetail_timer = layout.findViewById(R.id.tv_rdetail_timer); //Timer text
            TextView tv_rdetail_vname = layout.findViewById(R.id.tv_rdetail_vname);
            ImageView iv_rdetail_vimg = layout.findViewById(R.id.iv_rdetail_vimg);
            ImageView pop_back = layout.findViewById(R.id.pop_back);
            ImageView iv_selecttype_image = layout.findViewById(R.id.iv_selecttype_image);
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
                Glide.with(TabActivity.this).load(sdprofileImage).apply(options).into(iv_rdetail_vimg);
            }

            if (propertyImage.length() != 0) {
                RequestOptions options = new RequestOptions();
                options.placeholder(R.drawable.ic_new_property_img);
                options.diskCacheStrategy(DiskCacheStrategy.RESOURCE);
                Glide.with(TabActivity.this).load(propertyImage).apply(options).into(iv_selecttype_image);
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
                        long daysInMilli = hoursInMilli * 24;
                        long hours = millisUntilFinished / hoursInMilli;
                        millisUntilFinished = millisUntilFinished % hoursInMilli;
                        long min = millisUntilFinished / minutesInMilli;
                        millisUntilFinished = millisUntilFinished % minutesInMilli;
                        long sec = millisUntilFinished / secondsInMilli;
                        tv_rdetail_timer.setText(f.format(hours) + ":" + f.format(min) + ":" + f.format(sec));
                    }

                    public void onFinish() {
                        popUp.dismiss();
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                        alert();
                    }
                }.start();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void RequestDetailApiData(String id) {
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
                                reqid = String.valueOf(jsonObject1.getInt("_id"));
                                jobid = String.valueOf(jsonObject1.getInt("jobId"));
                                senderid = String.valueOf(jsonObject1.getInt("senderId"));
                                expireTime = jsonObject1.getString("expireTime");
                                currentTime = jsonObject1.getString("currentTime");
                                JSONArray jsonArray3 = jsonObject1.getJSONArray("propertyImg");
                                if (jsonArray3.length() > 0) {
                                    JSONObject jsonObject6 = jsonArray3.getJSONObject(0);
                                    propertyImage = jsonObject6.getString("propertyImage");
                                }

                                JSONArray jsonArray2 = jsonObject1.getJSONArray("senderDetail");
                                for (int k = 0; k < jsonArray2.length(); k++) {
                                    JSONObject jsonObject3 = jsonArray2.getJSONObject(k);
                                    sdprofileImage = jsonObject3.getString("profileImage");
                                    sdfullName = jsonObject3.getString("fullName");
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


                                initiatePopupWindow();
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
                                Toast.makeText(TabActivity.this, msg, Toast.LENGTH_SHORT).show();
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
                                popUp.dismiss();
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);


                            } else {
                                Toast.makeText(TabActivity.this, msg, Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(TabActivity.this, msg, Toast.LENGTH_SHORT).show();
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

    public void alert() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(TabActivity.this);

        alertDialog.setTitle("Alert");
        alertDialog.setMessage("Your expiration has expired");
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }


    public void alertJobStatus() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(TabActivity.this);

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


    public void alertNFManage() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(TabActivity.this);

        alertDialog.setTitle("Alert");
        alertDialog.setMessage(getResources().getString(R.string.please_switch_user_mode_to_do_this));
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(TabActivity.this, TabActivity.class);
                intent.putExtra("order", "3");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        alertDialog.show();
    }

}
