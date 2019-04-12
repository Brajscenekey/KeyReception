package com.key.keyreception.Activity.Owner;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.key.keyreception.R;
import com.key.keyreception.Session;
import com.key.keyreception.base.BaseActivity;
import com.key.keyreception.connection.RetrofitClient;
import com.key.keyreception.ownerFragment.OwnerEarningFragment;
import com.key.keyreception.ownerFragment.OwnerMessageFragment;
import com.key.keyreception.ownerFragment.OwnerNotificationFragment;
import com.key.keyreception.ownerFragment.OwnerProfileFragment;
import com.key.keyreception.ownerFragment.OwnerReservationFragment;
import org.json.JSONObject;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class OwnerTabActivity extends BaseActivity implements View.OnClickListener {

    public TextView tv_head;
    public RelativeLayout rtlv_one, rtlv_two, rtlv_three, rtlv_four, rtlv_five, headrl;
    ImageView appoint, not, chart, pro, chat;
    Fragment fragment = null;
    private boolean doubleBackToExitPressedOnce;
    Runnable runnable;
    private View view1, view2, view3, view4, view5;
    private double latitude,longitude;
    private String address;
    private Session session;

    private Geocoder geocoder;
    boolean checkloc = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decor = getWindow().getDecorView();
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.activity_owner_tab);
        session = new Session(this);
        geocoder = new Geocoder(this, Locale.getDefault());
        init();
       /* requestPermission();
        //getLocation();

        if (!checkloc)
        {
            alertOwnerLocation();
        }*/
        Intent intent = getIntent();

        if (intent != null && intent.hasExtra("order")) {
            String s = intent.getStringExtra("order");

            if (s.equals("3")) {

                tv_head.setText(getResources().getString(R.string.profile));
                fragment = new OwnerProfileFragment();
                displaySelectedFragment(fragment);
                headrl.setVisibility(View.GONE);
                appoint.setImageResource(R.drawable.ic_inactive_calendar);
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


                tv_head.setText(getResources().getString(R.string.reserv));
                //Fragment home Bottom Tab layout

                fragment = new OwnerReservationFragment();
                displaySelectedFragment(fragment);

                appoint.setImageResource(R.drawable.ic_active_calendar);
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
        tv_head = findViewById(R.id.owner_tv_head);
        headrl = findViewById(R.id.owner_chomerr);

        appoint = findViewById(R.id.owner_img_one);
        chat = findViewById(R.id.owner_img_two);
        chart = findViewById(R.id.owner_img_three);
        not = findViewById(R.id.owner_img_four);
        pro = findViewById(R.id.owner_img_five);

        view1 = findViewById(R.id.owner_view1);
        view2 = findViewById(R.id.owner_view2);
        view3 = findViewById(R.id.owner_view3);
        view4 = findViewById(R.id.owner_view4);
        view5 = findViewById(R.id.owner_view5);


        rtlv_one = findViewById(R.id.owner_rtlv_one);
        rtlv_two = findViewById(R.id.owner_rtlv_two);
        rtlv_three = findViewById(R.id.owner_rtlv_three);
        rtlv_four = findViewById(R.id.owner_rtlv_four);
        rtlv_five = findViewById(R.id.owner_rtlv_five);

        rtlv_one.setOnClickListener(this);
        rtlv_two.setOnClickListener(this);
        rtlv_three.setOnClickListener(this);
        rtlv_four.setOnClickListener(this);
        rtlv_five.setOnClickListener(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 10001) {
            if (resultCode == Activity.RESULT_OK && fragment instanceof OwnerReservationFragment) {
                ((OwnerReservationFragment) fragment).switchPager();
            }
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.owner_rtlv_one:
                tv_head.setText(getResources().getString(R.string.reserv));
                // replaceFragment(ProfileFragment.newInstance(),true);
                fragment = new OwnerReservationFragment();
                displaySelectedFragment(fragment);
                updateTabView(R.id.owner_rtlv_one);
                headrl.setVisibility(View.VISIBLE);

                break;
            case R.id.owner_rtlv_two:
                tv_head.setText(getResources().getString(R.string.mess));
                fragment = new OwnerMessageFragment();
                displaySelectedFragment(fragment);
                updateTabView(R.id.owner_rtlv_two);
                headrl.setVisibility(View.VISIBLE);

                break;
            case R.id.owner_rtlv_three:
                tv_head.setText(getResources().getString(R.string.earn));
                fragment = new OwnerEarningFragment();
                displaySelectedFragment(fragment);
                updateTabView(R.id.owner_rtlv_three);
                headrl.setVisibility(View.VISIBLE);

                break;
            case R.id.owner_rtlv_four:

                tv_head.setText(getResources().getString(R.string.not));
                fragment = new OwnerNotificationFragment();
                displaySelectedFragment(fragment);
                updateTabView(R.id.owner_rtlv_four);
                headrl.setVisibility(View.VISIBLE);

                break;
            case R.id.owner_rtlv_five:
                tv_head.setText(getResources().getString(R.string.profile));
                fragment = new OwnerProfileFragment();
                displaySelectedFragment(fragment);
                updateTabView(R.id.owner_rtlv_five);
                headrl.setVisibility(View.GONE);
                break;
            default:
                tv_head.setText(getResources().getString(R.string.reserv));
                fragment = new OwnerReservationFragment();
                displaySelectedFragment(fragment);
                headrl.setVisibility(View.VISIBLE);

                break;
        }
    }


    private void updateTabView(int flag) {
        switch (flag) {
            case R.id.owner_rtlv_one:
                appoint.setImageResource(R.drawable.ic_active_calendar);
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

            case R.id.owner_rtlv_two:
                appoint.setImageResource(R.drawable.ic_inactive_calendar);
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
            case R.id.owner_rtlv_three:
                appoint.setImageResource(R.drawable.ic_inactive_calendar);
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

            case R.id.owner_rtlv_four:
                appoint.setImageResource(R.drawable.ic_inactive_calendar);
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

            case R.id.owner_rtlv_five:
                appoint.setImageResource(R.drawable.ic_inactive_calendar);
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


    public void alertOwnerLocation()
    {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(OwnerTabActivity.this);

        // Setting Dialog Title
        alertDialog.setTitle("Alert");

        // Setting Dialog Message
        alertDialog.setMessage("\"Location is disabled\", message: \"We need access to your location to show you relevant search result, Please click on Settings to allow location.\"");

        // Setting Icon to Dialog


        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("Allow", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public void onClick(DialogInterface dialog, int which) {
                checkloc =true;
                upLocationApiData(address,String.valueOf(latitude),String.valueOf(longitude));
                dialog.dismiss();

            }
        });


        alertDialog.setNegativeButton("Don't Allow", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                checkloc =false;
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
                        address = getCompleteAddress(latitude,longitude);
                        Log.v("address",address);
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

                                Toast.makeText(OwnerTabActivity.this, msg, Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(OwnerTabActivity.this, msg, Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(OwnerTabActivity.this, msg, Toast.LENGTH_SHORT).show();
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
