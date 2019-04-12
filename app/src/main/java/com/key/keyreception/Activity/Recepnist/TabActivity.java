package com.key.keyreception.Activity.Recepnist;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
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
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.key.keyreception.Activity.Owner.OwnerTabActivity;
import com.key.keyreception.Activity.Owner.RequestActivity;
import com.key.keyreception.R;
import com.key.keyreception.Session;
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

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class TabActivity extends BaseActivity implements View.OnClickListener {


    public TextView tv_head;
    public RelativeLayout rtlv_one, rtlv_two, rtlv_three, rtlv_four, rtlv_five, headrl;
    ImageView appoint, not, chart, pro, chat, requsetlist;
    Fragment fragment = null;
    View view;
    private boolean doubleBackToExitPressedOnce;
    private Runnable runnable;
    private View view1, view2, view3, view4, view5;
    private Session session;
    private PDialog pDialog;
    private double latitude,longitude;
    private String address12;
    private Geocoder geocoder;
    private String propertyName, address, sdfullName, sdprofileImage, serviceDate, expireTime, currentTime, jobid, senderid, reqid;

    private BroadcastReceiver myReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            if (intent.getStringExtra("notifyId") != null) {
                reqid = intent.getStringExtra("notifyId");
                RequestDetailApiData(reqid);
//            session.putmyjobdetail(reference_id);
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
        geocoder = new Geocoder(this, Locale.getDefault());
        requestPermission();
        //getLocation();

        if (session.getLocationId().isEmpty())
        {
            alertOwnerLocation();
        }


        if (getIntent().getStringExtra("notifyId") != null) {
            String reference_id = getIntent().getStringExtra("notifyId");
            RequestDetailApiData(reference_id);
//            session.putmyjobdetail(reference_id);
        }

        Intent intent = getIntent();

        if (intent != null && intent.hasExtra("order")) {
            String s = intent.getStringExtra("order");

            if (s.equals("3")) {

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
        }else {
                tv_head.setText(getResources().getString(R.string.appo));
                //Fragment home Bottom Tab layout

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
                tv_head.setText(getResources().getString(R.string.appo));
                // replaceFragment(ProfileFragment.newInstance(),true);
                fragment = new AppointmentFragment();
                displaySelectedFragment(fragment);
                updateTabView(R.id.rtlv_one);
                headrl.setVisibility(View.VISIBLE);

                break;
            case R.id.rtlv_two:
                tv_head.setText(getResources().getString(R.string.mess));
                fragment = new MessageFragment();
                displaySelectedFragment(fragment);
                updateTabView(R.id.rtlv_two);
                headrl.setVisibility(View.VISIBLE);

                break;
            case R.id.rtlv_three:
                tv_head.setText(getResources().getString(R.string.earn));
                fragment = new EarningFragment();
                displaySelectedFragment(fragment);
                updateTabView(R.id.rtlv_three);
                headrl.setVisibility(View.VISIBLE);

                break;
            case R.id.rtlv_four:

                tv_head.setText(getResources().getString(R.string.not));
                fragment = new NotificationFragment();
                displaySelectedFragment(fragment);
                updateTabView(R.id.rtlv_four);
                headrl.setVisibility(View.VISIBLE);

                break;
            case R.id.rtlv_five:
                tv_head.setText(getResources().getString(R.string.profile));
                fragment = new ProfileFragment();
                displaySelectedFragment(fragment);
                updateTabView(R.id.rtlv_five);
                headrl.setVisibility(View.GONE);
                break;
            case R.id.request_list:
                Intent intent = new Intent(TabActivity.this, RequestActivity.class);
                startActivity(intent);
                break;
            default:
                tv_head.setText(getResources().getString(R.string.appo));
                fragment = new AppointmentFragment();
                displaySelectedFragment(fragment);
                headrl.setVisibility(View.VISIBLE);

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
//            MySnackBar.showSnackbar(this, findViewById(R.id.lyCoordinatorLayout), "Click again to exit");

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

            // Button done = layout.findViewById(R.id.dialog_done);
            Button accept = layout.findViewById(R.id.dialog_accept);
            TextView tv_rdetail_propname = layout.findViewById(R.id.tv_rdetail_propname);
            TextView tv_rdetail_propaddress = layout.findViewById(R.id.tv_rdetail_propaddress);
            TextView tv_rdetail_sdate = layout.findViewById(R.id.tv_rdetail_sdate);
            final TextView tv_rdetail_timer = layout.findViewById(R.id.tv_rdetail_timer); //Timer text
            TextView tv_rdetail_vname = layout.findViewById(R.id.tv_rdetail_vname);
            final TextView timeout = layout.findViewById(R.id.timeout);
            final LinearLayout li_btn = layout.findViewById(R.id.li_btn);
            ImageView iv_rdetail_vimg = layout.findViewById(R.id.iv_rdetail_vimg);
            ImageView pop_back = layout.findViewById(R.id.pop_back);
            RatingBar tv_rdetail_vendorrating = layout.findViewById(R.id.tv_rdetail_vendorrating);
            tv_rdetail_propname.setText(propertyName);
            tv_rdetail_propaddress.setText(address);


            try {
                if(serviceDate.contains("-")) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
                    Date date = sdf.parse(serviceDate);
                    SimpleDateFormat mSDF = new SimpleDateFormat("dd, MMMM hh:mm a", Locale.getDefault());
                    String fDate = mSDF.format(date);
                    tv_rdetail_sdate.setText(fDate);                }
                else {
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
//            tv_rdetail_timer.setText(propertyName);
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
                    popUp.dismiss();
                    acceptRejectApiData("2", popUp);
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

                String[] splitGmt = expireTime.split("GMT");
                Date eDate = getExpireDate(splitGmt[0].trim());
                final Long diffMillis = eDate.getTime() - cDate.getTime();
                int hours = (int) (diffMillis / (1000 * 60 * 60)) % 24;
                int mins = (int) (diffMillis / (1000 * 60)) % 60;


                /*let seconds = interval % 60
                let minutes = (interval / 60) % 60
                let hours = (interval / 3600) % 24
                let days = (interval / 86400)*/

                /*if (mins <= 30 && hours < 1) {
                    new CountDownTimer(diffMillis, 1000) {

                        public void onTick(long millisUntilFinished) {
                            NumberFormat f = new DecimalFormat("00");
                            int sec = (int) (millisUntilFinished/1000) % 60;
                            int min = (int) (millisUntilFinished/(1000*60)) % 60;
                            int hours = (int) (millisUnt-mat(hours) + ":" + f.format(min) + ":" + f.format(sec));
                        }

                        public void onFinish() {
                            //ToDo: Write here if needed after finished
                            tv_rdetail_timer.setText("Expired");
                        }
                    }.start();
                }*/
                Log.d("DATE", "C" + new Date().toString() + " E: " + eDate.toString());

                new CountDownTimer(diffMillis, 1000) {

                    public void onTick(long millisUntilFinished) {
                        NumberFormat f = new DecimalFormat("00");
                        /*int sec = (int) (millisUntilFinished / 1000) % 60;
                        int min = (int) (millisUntilFinished / (1000 * 60)) % 60;
                        int hours = (int) (millisUntilFinished / (1000 * 60 * 60)) % 24;*/

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
                        //ToDo: Write here if needed after finished
                        /*li_btn.setVisibility(View.GONE);
                        tv_rdetail_timer.setVisibility(View.GONE);
                        timeout.setText("Time Out");
                        timeout.setTextColor(getResources().getColor(R.color.appcolor));*/
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
                                jobid = String.valueOf(jsonObject1.getInt("_id"));
                                senderid = String.valueOf(jsonObject1.getInt("senderId"));
//                                int receiverId = jsonObject1.getInt("receiverId");
//                                String requestTime = jsonObject1.getString("requestTime");
                                expireTime = jsonObject1.getString("expireTime");
                                currentTime = jsonObject1.getString("currentTime");

//                                JSONArray jsonArray1 = jsonObject1.getJSONArray("category");
//                                for (int j = 0; j < jsonArray1.length(); j++) {
//                                    RequestData.CategoryBean categoryBean;
//                                    JSONObject jsonObject21 = jsonArray1.getJSONObject(j);
                                   /* int _cid = jsonObject21.getInt("_id");
                                    String title = jsonObject21.getString("title");
*/
//                                }

                                JSONArray jsonArray2 = jsonObject1.getJSONArray("senderDetail");
                                for (int k = 0; k < jsonArray2.length(); k++) {
//                                    RequestData.SenderDetailBean senderDetailBean;
                                    JSONObject jsonObject3 = jsonArray2.getJSONObject(k);
//                                    int _sdid = jsonObject3.getInt("_id");
                                    sdprofileImage = jsonObject3.getString("profileImage");
                                    sdfullName = jsonObject3.getString("fullName");
                                }

//                                JSONArray jsonArray3 = jsonObject1.getJSONArray("reciverDetail");
//                                for (int l = 0; l < jsonArray3.length(); l++) {
//                                    RequestData.ReciverDetailBean reciverDetailBean;
//                                    JSONObject jsonObject4 = jsonArray3.getJSONObject(l);
//                                    int _sdid = jsonObject4.getInt("_id");
//                                    String sdprofileImage = jsonObject4.getString("profileImage");
//                                    String sdfullName = jsonObject4.getString("fullName");
//                                }

                                JSONArray jsonArray4 = jsonObject1.getJSONArray("jobDetail");
                                for (int m = 0; m < jsonArray4.length(); m++) {
//                                    RequestData.JobDetailBean jobDetailBean;
                                    JSONObject jsonObject5 = jsonArray4.getJSONObject(m);
//                                    String propertyId = jsonObject5.getString("propertyId");
                                    propertyName = jsonObject5.getString("propertyName");
//                                    String bedroom = jsonObject5.getString("bedroom");
//                                    String bathroom = jsonObject5.getString("bathroom");
//                                    String price = jsonObject5.getString("price");
//                                    String propertySize = jsonObject5.getString("propertySize");
                                    serviceDate = jsonObject5.getString("serviceDate");
//                                    String checkIn = jsonObject5.getString("checkIn");
//                                    String checkOut = jsonObject5.getString("checkOut");
                                    address = jsonObject5.getString("address");
//                                    String latitude = jsonObject5.getString("latitude");
//                                    String longitude = jsonObject5.getString("longitude");
//                                    String description = jsonObject5.getString("description");
//                                    int status = jsonObject5.getInt("status");

                                }


                                initiatePopupWindow();
                            } else {
                                Toast.makeText(/*li_btn.setVisibility(View.GONE);
                        tv_rdetail_timer.setVisibility(View.GONE);
                        timeout.setText("Time Out");
                        timeout.setTextColor(getResources().getColor(R.color.appcolor));*/TabActivity.this, msg, Toast.LENGTH_SHORT).show();

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

    private Date getExpireDate(String eDate) {
        Log.d("DATE", "E> " + eDate);
        //Fri Apr 05 2019 11:46:02 GMT+0000 (UTC)
        //SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd yyyy hh:mm:ss 'GMT'Z '(UTC)'", Locale.getDefault());
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd yyyy hh:mm:ss", Locale.getDefault());
        try {
            //sdf.setTimeZone(TimeZone.getDefault());
            return sdf.parse(eDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Date getCurrentDate(String cDate) {
        Log.d("DATE", "C> " + cDate);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'", Locale.getDefault());
        try {
            return sdf.parse(cDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Date getMyDate(String cDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss z", Locale.getDefault());
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
                .getApi().acceptReject(authtoken, "application/x-www-form-urlencoded", jobid, reqid, senderid, status);
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

    public void alert()
    {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(TabActivity.this);

        // Setting Dialog Title
        alertDialog.setTitle("Alert");

        // Setting Dialog Message
        alertDialog.setMessage("Your expiration has expired");

        // Setting Icon to Dialog


        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public void onClick(DialogInterface dialog, int which) {
               dialog.dismiss();

            }
        });



        // Showing Alert Message
        alertDialog.show();
    }
    public void alertOwnerLocation()
    {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(TabActivity.this);

        // Setting Dialog Title
        alertDialog.setTitle("Alert");

        // Setting Dialog Message
        alertDialog.setMessage("\"Location is disabled\", message: \"We need access to your location to show you relevant search result, Please click on Settings to allow location.\"");

        // Setting Icon to Dialog


        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("Allow", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public void onClick(DialogInterface dialog, int which) {
                session.putLocationId("1");
                upLocationApiData(address12,String.valueOf(latitude),String.valueOf(longitude));
                dialog.dismiss();

            }
        });


        alertDialog.setNegativeButton("Don't Allow", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                session.putLocationId("");
                dialogInterface.dismiss();

            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    private void requestPermission() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION},2001);
        } else {
            // Permission granted do your stuffs here
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    initFusedLocation();
                }
            },1000);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 2001) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted do your stuffs here
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initFusedLocation();
                    }
                },1000);
            } else {
                requestPermission();
            }
        }
    }

    private void initFusedLocation() {
        FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        // Logic to handle location object
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        Log.v("latitude",String.valueOf(latitude));
                        Log.v("longitude",String.valueOf(longitude));
                        address12 = getCompleteAddress(latitude,longitude);
                        Log.v("address",address12);
                    }
                }
            });
        } catch (SecurityException se) {
            se.printStackTrace();
        }
    }
    private String getCompleteAddress(double latitude,double longitude) {
        /* Getting location from target*/
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);

            if (addresses != null) {
                if (addresses.size() != 0) {
                    return addresses.get(0).getAddressLine(0);
                } else {
                    return "Address not found";
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Address not found";
    }

    public void upLocationApiData(String add,String lat, String lon) {
        Log.d("TESTING","Requesting...");
        String authtoken = session.getAuthtoken();
        /*RequestBody add1 = RequestBody.create(MediaType.parse("text/plain"), add);
        RequestBody lat1 = RequestBody.create(MediaType.parse("text/plain"), lat);
        RequestBody lon1 = RequestBody.create(MediaType.parse("text/plain"), lon);*/

        MediaType text = MediaType.parse("text/plain");
        RequestBody add1 = RequestBody.create(text, add);
        RequestBody lat1 = RequestBody.create(text, lat);
        RequestBody lon1 = RequestBody.create(text, lon);

        RequestBody requestBody = new FormBody.Builder().add("address",add).add("latitude",lat).add("longitude",lon).build();



        Call<ResponseBody> call = RetrofitClient.getInstance()
                .getApi().updateLocation(authtoken , add1, lat1, lon1);
        call.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("NewApi")
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull retrofit2.Response<ResponseBody> response) {
                Log.d("TESTING",""+response.code()+" "+response.message());
                try {
                    switch (response.code()) {
                        case 200: {
                            String stresult = Objects.requireNonNull(response.body()).string();
                            Log.d("response", stresult);
                            JSONObject jsonObject = new JSONObject(stresult);
                            String statusCode = jsonObject.optString("status");
                            String msg = jsonObject.optString("message");
                            if (statusCode.equals("success")) {

                                Toast.makeText(TabActivity.this, msg, Toast.LENGTH_SHORT).show();

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
                Log.e("TESTING","ERROR",t);
            }
        });

    }

}
