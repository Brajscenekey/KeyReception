package com.key.keyreception.recepnistFragment;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.key.keyreception.R;
import com.key.keyreception.Session;
import com.key.keyreception.activity.model.MyJobData;
import com.key.keyreception.base.BaseFragment;
import com.key.keyreception.connection.RetrofitClient;
import com.key.keyreception.helper.PDialog;
import com.key.keyreception.recepnistFragment.adapter.AppoAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * A simple {@link Fragment} subclass.
 */
public class AppointmentFragment extends BaseFragment implements View.OnClickListener {


    private static double latitude, longitude;
    private AppoAdapter adapter;
    private Session session;
    private PDialog pDialog;
    private RelativeLayout rl_filter;
    private EditText et_jobsearch;
    private TextView tv_complete, tv_inprogress, tv_Apponotstart, tv_Appoall;
    private List<MyJobData> jobDataList = new ArrayList<>();
    private List<MyJobData.CategoryBean> categorylist = new ArrayList<>();
    private List<MyJobData.OwnerDetailBean> ownerList;
    private List<MyJobData.PropertyImgBean> imglist = new ArrayList<>();
    private String address12;
    private Geocoder geocoder;
    private TextView tv_no_record;
    private String status = "";
    private RecyclerView recyclerView;
    private TextWatcher watcherClass_search = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {

            if (editable == et_jobsearch.getEditableText()) {

                myJobListApiData1(status, et_jobsearch.getText().toString());


            }
        }
    };


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_appointment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        pDialog = new PDialog();
        session = new Session(mContext);

        geocoder = new Geocoder(mContext, Locale.getDefault());
        if (session.isUpdateLocIn()) {
            requestPermission();

        }
        init(view);
        tv_Appoall.setTextColor(getResources().getColor(R.color.red));
        tv_inprogress.setTextColor(getResources().getColor(R.color.colorPrimary));
        tv_complete.setTextColor(getResources().getColor(R.color.colorPrimary));
        tv_Apponotstart.setTextColor(getResources().getColor(R.color.colorPrimary));
        myJobListApiData1("", "");
        status = "";
        rl_filter.setVisibility(View.GONE);
    }

    public void init(View view) {
        ImageView iv_filter = view.findViewById(R.id.iv_Appofilter);
        rl_filter = view.findViewById(R.id.rl_Appofilter);
        tv_complete = view.findViewById(R.id.tv_Appocomplete);
        et_jobsearch = view.findViewById(R.id.et_Appojobsearch);
        et_jobsearch.addTextChangedListener(watcherClass_search);
        tv_inprogress = view.findViewById(R.id.tv_Appoinprogress);
        tv_no_record = view.findViewById(R.id.tv_no_record);
        tv_Apponotstart = view.findViewById(R.id.tv_Apponotstart);
        tv_Appoall = view.findViewById(R.id.tv_Appoall);
        LinearLayout ly_complete = view.findViewById(R.id.ly_Appocomplete);
        LinearLayout ly_inprogress = view.findViewById(R.id.ly_Appoinprogress);
        LinearLayout ly_Apponnotstart = view.findViewById(R.id.ly_Apponnotstart);
        LinearLayout ly_Appoall = view.findViewById(R.id.ly_Appoall);
        ly_complete.setOnClickListener(this);
        ly_inprogress.setOnClickListener(this);
        ly_Apponnotstart.setOnClickListener(this);
        iv_filter.setOnClickListener(this);
        ly_Appoall.setOnClickListener(this);
        recyclerView = view.findViewById(R.id.Appojob_recycler_view);
        adapter = new AppoAdapter(mContext, jobDataList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.ly_Appocomplete: {

                tv_Appoall.setTextColor(getResources().getColor(R.color.colorPrimary));
                tv_complete.setTextColor(getResources().getColor(R.color.colorDarkGreen));
                tv_inprogress.setTextColor(getResources().getColor(R.color.colorPrimary));
                tv_Apponotstart.setTextColor(getResources().getColor(R.color.colorPrimary));
                myJobListApiData1("4", "");
                status = "4";
                rl_filter.setVisibility(View.GONE);

            }
            break;

            case R.id.ly_Appoinprogress: {

                tv_Appoall.setTextColor(getResources().getColor(R.color.colorPrimary));
                tv_inprogress.setTextColor(getResources().getColor(R.color.colorInprogress));
                tv_complete.setTextColor(getResources().getColor(R.color.colorPrimary));
                tv_Apponotstart.setTextColor(getResources().getColor(R.color.colorPrimary));

                myJobListApiData1("3", "");
                status = "3";
                rl_filter.setVisibility(View.GONE);


            }
            break;

            case R.id.ly_Apponnotstart: {


                tv_Appoall.setTextColor(getResources().getColor(R.color.colorPrimary));
                tv_inprogress.setTextColor(getResources().getColor(R.color.colorPrimary));
                tv_complete.setTextColor(getResources().getColor(R.color.colorPrimary));
                tv_Apponotstart.setTextColor(getResources().getColor(R.color.appcolor));
                myJobListApiData1("2", "");
                status = "2";
                rl_filter.setVisibility(View.GONE);


            }
            break;
            case R.id.ly_Appoall: {

                tv_Appoall.setTextColor(getResources().getColor(R.color.red));
                tv_inprogress.setTextColor(getResources().getColor(R.color.colorPrimary));
                tv_complete.setTextColor(getResources().getColor(R.color.colorPrimary));
                tv_Apponotstart.setTextColor(getResources().getColor(R.color.colorPrimary));
                myJobListApiData1("", "");
                status = "";
                rl_filter.setVisibility(View.GONE);


            }
            break;

            case R.id.iv_Appofilter: {

                if (rl_filter.getVisibility() == View.VISIBLE) {
                    rl_filter.setVisibility(View.GONE);
                } else if (rl_filter.getVisibility() == View.GONE) {
                    rl_filter.setVisibility(View.VISIBLE);
                }
            }
            break;
        }
    }


    public void myJobListApiData(String status, String searchdata) {

        String authtoken = session.getAuthtoken();
        String type = session.getusertype();
        pDialog.pdialog(mContext);
        Call<ResponseBody> call = RetrofitClient.getInstance()
                .getApi().jobList(authtoken, type, "10", status, searchdata);
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
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
                            if (statusCode.equals("success")) {
                                jobDataList.clear();
                                tv_no_record.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    MyJobData myJobData;
                                    JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                                    int _id = jsonObject2.getInt("_id");
                                    int propertyId = jsonObject2.getInt("propertyId");
                                    String propertyName = jsonObject2.getString("propertyName");
                                    String bedroom = jsonObject2.getString("bedroom");
                                    String bathroom = jsonObject2.getString("bathroom");
                                    String price = jsonObject2.getString("price");
                                    String propertySize = jsonObject2.getString("propertySize");
                                    String serviceDate = jsonObject2.getString("serviceDate");
                                    String checkIn = jsonObject2.getString("checkIn");
                                    String checkOut = jsonObject2.getString("checkOut");
                                    String address = jsonObject2.getString("address");
                                    String latitude = jsonObject2.getString("latitude");
                                    String longitude = jsonObject2.getString("longitude");
                                    String description = jsonObject2.getString("description");
                                    int status = jsonObject2.getInt("status");
                                    String crd = jsonObject2.getString("crd");

                                    JSONArray jsonArray1 = jsonObject2.getJSONArray("category");
                                    for (int j = 0; j < jsonArray1.length(); j++) {

                                        MyJobData.CategoryBean categoryBean;
                                        JSONObject jsonObject3 = jsonArray1.getJSONObject(j);
                                        int _id1 = jsonObject3.getInt("_id");
                                        String title = jsonObject3.getString("title");

                                        categoryBean = new MyJobData.CategoryBean(_id1, title);
                                        categorylist.add(categoryBean);
                                    }
                                    List<MyJobData.PropertyDataBean> propertyDatalist = new ArrayList<>();
                                    JSONArray jsonArraydata = jsonObject2.getJSONArray("propertyData");
                                    JSONObject jsonObjectdata = jsonArraydata.getJSONObject(0);
                                    {
                                        String bathroom1 = jsonObjectdata.getString("bathroom");
                                        String bedroom1 = jsonObjectdata.getString("bedroom");
                                        String isImageAdd1 = jsonObjectdata.getString("isImageAdd");
                                        String propertyAddress1 = jsonObjectdata.getString("propertyAddress");
                                        String propertyId1 = jsonObjectdata.getString("propertyId");
                                        String propertyLat1 = jsonObjectdata.getString("propertyLat");
                                        String propertyLong1 = jsonObjectdata.getString("propertyLong");
                                        String propertyName1 = jsonObjectdata.getString("propertyName");
                                        String propertySize1 = jsonObjectdata.getString("propertySize");
                                        MyJobData.PropertyDataBean propertyDataBean = new MyJobData.PropertyDataBean(bathroom1, bedroom1, isImageAdd1, propertyAddress1, propertyId1, propertyLat1, propertyLong1, propertyName1, propertySize1);
                                        propertyDatalist.add(propertyDataBean);
                                    }
                                    JSONArray jsonArray3 = jsonObject2.getJSONArray("propertyImg");
                                    for (int m = 0; m < jsonArray3.length(); m++) {

                                        MyJobData.PropertyImgBean propertyImgBean;
                                        JSONObject jsonObject5 = jsonArray3.getJSONObject(m);
                                        int _id1 = jsonObject5.getInt("_id");
                                        String propertyImage = jsonObject5.getString("propertyImage");

                                        propertyImgBean = new MyJobData.PropertyImgBean(_id1, propertyImage);
                                        imglist.add(propertyImgBean);
                                    }
                                    JSONArray jsonArray2 = jsonObject2.getJSONArray("ownerDetail");
                                    for (int k = 0; k < jsonArray2.length(); k++) {

                                        MyJobData.OwnerDetailBean ownerDetailBean;
                                        JSONObject jsonObject4 = jsonArray2.getJSONObject(k);
                                        int _id1 = jsonObject4.getInt("_id");
                                        String profileImage = jsonObject4.getString("profileImage");
                                        String fullName = jsonObject4.getString("fullName");

                                        ownerDetailBean = new MyJobData.OwnerDetailBean(_id1, profileImage, fullName);
                                        ownerList.add(ownerDetailBean);
                                    }

                                    myJobData = new MyJobData(_id, propertyId, propertyName, bedroom, bathroom, price, propertySize, serviceDate, checkIn, checkOut, address, latitude, longitude, description, status, crd, propertyDatalist, imglist, ownerList, categorylist);
                                    jobDataList.add(myJobData);
                                }
                                adapter.notifyDataSetChanged();
                            } else {
                                recyclerView.setVisibility(View.GONE);
                                tv_no_record.setVisibility(View.VISIBLE);
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

    public void myJobListApiData1(String status, String searchdata) {

        String authtoken = session.getAuthtoken();
        String type = session.getusertype();
        Call<ResponseBody> call = RetrofitClient.getInstance()
                .getApi().jobList(authtoken, type, "10", status, searchdata);
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
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
                            if (statusCode.equals("success")) {
                                jobDataList.clear();
                                tv_no_record.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    MyJobData myJobData;
                                    JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                                    int _id = jsonObject2.getInt("_id");
                                    int propertyId = jsonObject2.getInt("propertyId");
                                    String propertyName = jsonObject2.getString("propertyName");
                                    String bedroom = jsonObject2.getString("bedroom");
                                    String bathroom = jsonObject2.getString("bathroom");
                                    String price = jsonObject2.getString("price");
                                    String propertySize = jsonObject2.getString("propertySize");
                                    String serviceDate = jsonObject2.getString("serviceDate");
                                    String checkIn = jsonObject2.getString("checkIn");
                                    String checkOut = jsonObject2.getString("checkOut");
                                    String address = jsonObject2.getString("address");
                                    String latitude = jsonObject2.getString("latitude");
                                    String longitude = jsonObject2.getString("longitude");
                                    String description = jsonObject2.getString("description");
                                    int status = jsonObject2.getInt("status");
                                    String crd = jsonObject2.getString("crd");

                                    JSONArray jsonArray1 = jsonObject2.getJSONArray("category");
                                    for (int j = 0; j < jsonArray1.length(); j++) {

                                        MyJobData.CategoryBean categoryBean;
                                        JSONObject jsonObject3 = jsonArray1.getJSONObject(j);
                                        int _id1 = jsonObject3.getInt("_id");
                                        String title = jsonObject3.getString("title");

                                        categoryBean = new MyJobData.CategoryBean(_id1, title);
                                        categorylist.add(categoryBean);
                                    }
                                    List<MyJobData.PropertyDataBean> propertyDatalist = new ArrayList<>();
                                    JSONArray jsonArraydata = jsonObject2.getJSONArray("propertyData");
                                    JSONObject jsonObjectdata = jsonArraydata.getJSONObject(0);
                                    {
                                        String bathroom1 = jsonObjectdata.getString("bathroom");
                                        String bedroom1 = jsonObjectdata.getString("bedroom");
                                        String isImageAdd1 = jsonObjectdata.getString("isImageAdd");
                                        String propertyAddress1 = jsonObjectdata.getString("propertyAddress");
                                        String propertyId1 = jsonObjectdata.getString("propertyId");
                                        String propertyLat1 = jsonObjectdata.getString("propertyLat");
                                        String propertyLong1 = jsonObjectdata.getString("propertyLong");
                                        String propertyName1 = jsonObjectdata.getString("propertyName");
                                        String propertySize1 = jsonObjectdata.getString("propertySize");
                                        MyJobData.PropertyDataBean propertyDataBean = new MyJobData.PropertyDataBean(bathroom1, bedroom1, isImageAdd1, propertyAddress1, propertyId1, propertyLat1, propertyLong1, propertyName1, propertySize1);
                                        propertyDatalist.add(propertyDataBean);
                                    }

                                    JSONArray jsonArray3 = jsonObject2.getJSONArray("propertyImg");
                                    for (int m = 0; m < jsonArray3.length(); m++) {

                                        String propertyImage = "";
                                        MyJobData.PropertyImgBean propertyImgBean;
                                        JSONObject jsonObject5 = jsonArray3.getJSONObject(m);
                                        int _id1 = jsonObject5.getInt("_id");
                                        if (jsonObject5.has("propertyImage")) {
                                            propertyImage = jsonObject5.getString("propertyImage");
                                        }
                                        propertyImgBean = new MyJobData.PropertyImgBean(_id1, propertyImage);
                                        imglist.add(propertyImgBean);
                                    }
                                    ownerList = new ArrayList<>();
                                    JSONArray jsonArray2 = jsonObject2.getJSONArray("ownerDetail");
                                    for (int k = 0; k < jsonArray2.length(); k++) {

                                        MyJobData.OwnerDetailBean ownerDetailBean;
                                        JSONObject jsonObject4 = jsonArray2.getJSONObject(k);
                                        int _id1 = jsonObject4.getInt("_id");
                                        String profileImage = jsonObject4.getString("profileImage");
                                        String fullName = jsonObject4.getString("fullName");

                                        ownerDetailBean = new MyJobData.OwnerDetailBean(_id1, profileImage, fullName);
                                        ownerList.add(ownerDetailBean);
                                    }

                                    myJobData = new MyJobData(_id, propertyId, propertyName, bedroom, bathroom, price, propertySize, serviceDate, checkIn, checkOut, address, latitude, longitude, description, status, crd, propertyDatalist, imglist, ownerList, categorylist);
                                    jobDataList.add(myJobData);
                                }
                                adapter.notifyDataSetChanged();
                            } else {
                                recyclerView.setVisibility(View.GONE);
                                tv_no_record.setVisibility(View.VISIBLE);
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

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 2001);
        } else {
            // Permission granted do your stuffs here
            initFusedLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 2001) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted do your stuffs here
                initFusedLocation();
            } else {
                Toast.makeText(mContext, "Permission is deny", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initFusedLocation() {
        FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext);
        try {
            mFusedLocationClient.getLastLocation().addOnSuccessListener((Activity) mContext, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        // Logic to handle location object
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        Log.v("latitude", String.valueOf(latitude));
                        Log.v("longitude", String.valueOf(longitude));
                        address12 = getCompleteAddress(latitude, longitude);
                        Log.v("address", address12);

                        if (latitude != 0.0) {
                            upLocationApiData(address12, String.valueOf(latitude), String.valueOf(longitude));
                        }
                    } else {
                        if (latitude != 0.0) {
                            upLocationApiData(address12, String.valueOf(latitude), String.valueOf(longitude));
                        }
                    }
                }
            });
        } catch (SecurityException se) {
            se.printStackTrace();
        }
    }

    private String getCompleteAddress(double latitude, double longitude) {
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

    public void upLocationApiData(String add, String lat, String lon) {
        Log.d("TESTING", "Requesting...");
        String authtoken = session.getAuthtoken();
        String notstatus = session.getnotificationStatus();
        /*RequestBody add1 = RequestBody.create(MediaType.parse("text/plain"), add);
        RequestBody lat1 = RequestBody.create(MediaType.parse("text/plain"), lat);
        RequestBody lon1 = RequestBody.create(MediaType.parse("text/plain"), lon);*/

        MediaType text = MediaType.parse("text/plain");
        RequestBody add1 = RequestBody.create(text, add);
        RequestBody lat1 = RequestBody.create(text, lat);
        RequestBody lon1 = RequestBody.create(text, lon);
        RequestBody notstatus1 = RequestBody.create(text, notstatus);

        RequestBody requestBody = new FormBody.Builder().add("address", add).add("latitude", lat).add("longitude", lon).build();


        Call<ResponseBody> call = RetrofitClient.getInstance()
                .getApi().updateLocation(authtoken, add1, lat1, lon1, notstatus1);
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @SuppressLint("NewApi")
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull retrofit2.Response<ResponseBody> response) {
                Log.d("TESTING", "" + response.code() + " " + response.message());
                try {
                    switch (response.code()) {
                        case 200: {
                            String stresult = Objects.requireNonNull(response.body()).string();
                            Log.d("response", stresult);
                            JSONObject jsonObject = new JSONObject(stresult);
                            String statusCode = jsonObject.optString("status");
                            String msg = jsonObject.optString("message");
                            if (statusCode.equals("success")) {

                                session.isSetUpdateLocIn(false);

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
                Log.e("TESTING", "ERROR", t);
            }
        });

    }


}


