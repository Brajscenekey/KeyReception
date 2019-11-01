package com.key.keyreception.ownerChildFragment;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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

import com.key.keyreception.R;
import com.key.keyreception.Session;
import com.key.keyreception.activity.model.MyJobData;
import com.key.keyreception.activity.owner.CreatePostActivity;
import com.key.keyreception.base.BaseFragment;
import com.key.keyreception.connection.RetrofitClient;
import com.key.keyreception.helper.ProgressDialog;
import com.key.keyreception.ownerChildFragment.adapterchild.MyjobAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyjobFragment extends BaseFragment implements View.OnClickListener {


    private MyjobAdapter adapter;
    private Session session;
    private ProgressDialog pDialog;
    private RelativeLayout rl_filter;
    private EditText et_jobsearch;
    private TextView tv_complete, tv_pending, tv_inprogress, tv_notstarted, tv_all;
    private List<MyJobData> jobDataList = new ArrayList<>();
    private List<MyJobData.CategoryBean> categorylist = new ArrayList<>();
    private List<MyJobData.OwnerDetailBean> ownerlist = new ArrayList<>();
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
        return inflater.inflate(R.layout.fragment_myjob, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        pDialog = new ProgressDialog(mContext);
        session = new Session(mContext);
        init(view);
        tv_all.setTextColor(getResources().getColor(R.color.red));
        tv_pending.setTextColor(getResources().getColor(R.color.colorPrimary));
        tv_inprogress.setTextColor(getResources().getColor(R.color.colorPrimary));
        tv_complete.setTextColor(getResources().getColor(R.color.colorPrimary));
        tv_notstarted.setTextColor(getResources().getColor(R.color.colorPrimary));
        myJobListApiData1("", "");
        status = "";
        rl_filter.setVisibility(View.GONE);
    }

    public void init(View view) {
        ImageView creat_post = view.findViewById(R.id.creat_post);
        ImageView iv_filter = view.findViewById(R.id.iv_filter);
        rl_filter = view.findViewById(R.id.rl_filter);
        tv_complete = view.findViewById(R.id.tv_complete);
        tv_notstarted = view.findViewById(R.id.tv_notstarted);
        tv_pending = view.findViewById(R.id.tv_pending);
        et_jobsearch = view.findViewById(R.id.et_jobsearch);
        et_jobsearch.addTextChangedListener(watcherClass_search);
        tv_inprogress = view.findViewById(R.id.tv_inprogress);
        tv_all = view.findViewById(R.id.tv_all);
        tv_no_record = view.findViewById(R.id.tv_no_record);
        LinearLayout ly_complete = view.findViewById(R.id.ly_complete);
        LinearLayout ly_notstarted = view.findViewById(R.id.ly_notstarted);
        LinearLayout ly_pending = view.findViewById(R.id.ly_pending);
        LinearLayout ly_inprogress = view.findViewById(R.id.ly_inprogress);
        LinearLayout ly_all = view.findViewById(R.id.ly_all);
        ly_complete.setOnClickListener(this);
        ly_notstarted.setOnClickListener(this);
        ly_pending.setOnClickListener(this);
        ly_inprogress.setOnClickListener(this);
        iv_filter.setOnClickListener(this);
        ly_all.setOnClickListener(this);
        recyclerView = view.findViewById(R.id.reservmyjob_recycler_view);
        adapter = new MyjobAdapter(mContext, jobDataList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        creat_post.setOnClickListener(this);
    }

    @SuppressLint("NewApi")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.creat_post: {
                rl_filter.setVisibility(View.GONE);
                Intent intent = new Intent(mContext, CreatePostActivity.class);
                Objects.requireNonNull(getActivity()).startActivityForResult(intent, 10001);

            }
            break;

            case R.id.ly_complete: {
                tv_all.setTextColor(getResources().getColor(R.color.colorPrimary));
                tv_complete.setTextColor(getResources().getColor(R.color.colorDarkGreen));
                tv_inprogress.setTextColor(getResources().getColor(R.color.colorPrimary));
                tv_pending.setTextColor(getResources().getColor(R.color.colorPrimary));
                tv_notstarted.setTextColor(getResources().getColor(R.color.colorPrimary));
                myJobListApiData1("4", "");
                status = "4";
                rl_filter.setVisibility(View.GONE);

            }
            break;

            case R.id.ly_inprogress: {
                tv_all.setTextColor(getResources().getColor(R.color.colorPrimary));
                tv_inprogress.setTextColor(getResources().getColor(R.color.colorInprogress));
                tv_complete.setTextColor(getResources().getColor(R.color.colorPrimary));
                tv_pending.setTextColor(getResources().getColor(R.color.colorPrimary));
                tv_notstarted.setTextColor(getResources().getColor(R.color.colorPrimary));
                myJobListApiData1("3", "");
                status = "3";
                rl_filter.setVisibility(View.GONE);
            }
            break;
            case R.id.ly_notstarted: {
                tv_all.setTextColor(getResources().getColor(R.color.colorPrimary));
                tv_inprogress.setTextColor(getResources().getColor(R.color.colorPrimary));
                tv_complete.setTextColor(getResources().getColor(R.color.colorPrimary));
                tv_pending.setTextColor(getResources().getColor(R.color.colorPrimary));
                tv_notstarted.setTextColor(getResources().getColor(R.color.appcolor));
                myJobListApiData1("2", "");
                status = "2";
                rl_filter.setVisibility(View.GONE);
            }
            break;
            case R.id.ly_pending: {
                tv_all.setTextColor(getResources().getColor(R.color.colorPrimary));
                tv_pending.setTextColor(getResources().getColor(R.color.colorPending));
                tv_inprogress.setTextColor(getResources().getColor(R.color.colorPrimary));
                tv_complete.setTextColor(getResources().getColor(R.color.colorPrimary));
                tv_notstarted.setTextColor(getResources().getColor(R.color.colorPrimary));
                myJobListApiData1("1", "");
                status = "1";
                rl_filter.setVisibility(View.GONE);
            }
            break;
            case R.id.ly_all: {
                tv_all.setTextColor(getResources().getColor(R.color.red));
                tv_pending.setTextColor(getResources().getColor(R.color.colorPrimary));
                tv_inprogress.setTextColor(getResources().getColor(R.color.colorPrimary));
                tv_complete.setTextColor(getResources().getColor(R.color.colorPrimary));
                tv_notstarted.setTextColor(getResources().getColor(R.color.colorPrimary));
                myJobListApiData1("", "");
                status = "";
                rl_filter.setVisibility(View.GONE);
            }
            break;
            case R.id.iv_filter: {
                if (rl_filter.getVisibility() == View.VISIBLE) {
                    rl_filter.setVisibility(View.GONE);
                } else if (rl_filter.getVisibility() == View.GONE) {
                    rl_filter.setVisibility(View.VISIBLE);
                }
            }
            break;
        }
    }

    public void switchApi() {
        myJobListApiData1("1", "");
    }

    public void myJobListApiData(String status, String searchdata) {

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
                            String msg = jsonObject.optString("message");
                            if (statusCode.equals("success")) {
                                jobDataList.clear();
                                tv_no_record.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    MyJobData myJobData;
                                    JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                                    int propertyId = 0;
                                    int _id = jsonObject2.getInt("_id");
                                    if (jsonObject2.has("propertyId")) {
                                        propertyId = jsonObject2.getInt("propertyId");
                                    }
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
                                    List<MyJobData.PropertyDataBean> propertyDatalist = new ArrayList<>();
                                    JSONArray jsonArraydata = jsonObject2.getJSONArray("propertyData");
                                    JSONObject jsonObjectdata = jsonArraydata.getJSONObject(0);
                                    {
                                        String propertyId1 = "";
                                        String propertyAddress1 = "", propertyLat1 = "", propertyLong1 = "", propertyName1 = "", propertySize1 = "";

                                        String bathroom1 = jsonObjectdata.getString("bathroom");
                                        String bedroom1 = jsonObjectdata.getString("bedroom");
                                        String isImageAdd1 = jsonObjectdata.getString("isImageAdd");

                                        if (jsonObjectdata.has("propertyAddress")) {
                                            propertyAddress1 = jsonObjectdata.getString("propertyAddress");
                                        }
                                        if (jsonObjectdata.has("propertyId")) {
                                            propertyId1 = jsonObjectdata.getString("propertyId");
                                        }

                                        if (jsonObjectdata.has("propertyLat")) {
                                            propertyLat1 = jsonObjectdata.getString("propertyLat");
                                        }
                                        if (jsonObjectdata.has("propertyLong")) {
                                            propertyLong1 = jsonObjectdata.getString("propertyLong");
                                        }
                                        if (jsonObjectdata.has("propertyName")) {
                                            propertyName1 = jsonObjectdata.getString("propertyName");
                                        }
                                        if (jsonObjectdata.has("propertySize")) {
                                            propertySize1 = jsonObjectdata.getString("propertySize");
                                        }
                                        MyJobData.PropertyDataBean propertyDataBean = new MyJobData.PropertyDataBean(bathroom1, bedroom1, isImageAdd1, propertyAddress1, propertyId1, propertyLat1, propertyLong1, propertyName1, propertySize1);
                                        propertyDatalist.add(propertyDataBean);
                                    }

                                    JSONArray jsonArray1 = jsonObject2.getJSONArray("category");
                                    for (int j = 0; j < jsonArray1.length(); j++) {

                                        MyJobData.CategoryBean categoryBean;
                                        JSONObject jsonObject3 = jsonArray1.getJSONObject(j);
                                        int _id1 = jsonObject3.getInt("_id");
                                        String title = jsonObject3.getString("title");

                                        categoryBean = new MyJobData.CategoryBean(_id1, title);
                                        categorylist.add(categoryBean);
                                    }
                                    List<MyJobData.PropertyImgBean> imglist = new ArrayList<>();
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
                                        ownerlist.add(ownerDetailBean);
                                    }

                                    myJobData = new MyJobData(_id, propertyId, propertyName, bedroom, bathroom, price, propertySize, serviceDate, checkIn, checkOut, address, latitude, longitude, description, status, crd, propertyDatalist, imglist, ownerlist, categorylist);
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
            }
        });

    }

    public void myJobListApiData1(String status, String searchdata) {

        pDialog.show();
        String authtoken = session.getAuthtoken();
        String type = session.getusertype();
        Call<ResponseBody> call = RetrofitClient.getInstance()
                .getApi().jobList(authtoken, type, "10", status, searchdata);
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @SuppressLint("NewApi")
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull retrofit2.Response<ResponseBody> response) {
                try {

                    pDialog.hide();
                    switch (response.code()) {
                        case 200: {
                            String stresult = Objects.requireNonNull(response.body()).string();
                            Log.d("response", stresult);
                            JSONObject jsonObject = new JSONObject(stresult);
                            String statusCode = jsonObject.optString("status");
                            String msg = jsonObject.optString("message");
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
                                        String propertyAddress1 = "", propertyLat1 = "", propertyLong1 = "", propertyName1 = "", propertySize1 = "";
                                        int propertyId1 = 0;
                                        String bathroom1 = jsonObjectdata.getString("bathroom");
                                        String bedroom1 = jsonObjectdata.getString("bedroom");
                                        String isImageAdd1 = jsonObjectdata.getString("isImageAdd");
                                        if (jsonObjectdata.has("propertyAddress")) {
                                            propertyAddress1 = jsonObjectdata.getString("propertyAddress");
                                        }
                                        if (jsonObjectdata.has("propertyId")) {
                                            propertyId1 = jsonObjectdata.getInt("propertyId");
                                        }

                                        if (jsonObjectdata.has("propertyLat")) {
                                            propertyLat1 = jsonObjectdata.getString("propertyLat");
                                        }
                                        if (jsonObjectdata.has("propertyLong")) {
                                            propertyLong1 = jsonObjectdata.getString("propertyLong");
                                        }
                                        if (jsonObjectdata.has("propertyName")) {
                                            propertyName1 = jsonObjectdata.getString("propertyName");
                                        }
                                        if (jsonObjectdata.has("propertySize")) {
                                            propertySize1 = jsonObjectdata.getString("propertySize");
                                        }
                                        MyJobData.PropertyDataBean propertyDataBean = new MyJobData.PropertyDataBean(bathroom1, bedroom1, isImageAdd1, propertyAddress1, String.valueOf(propertyId1), propertyLat1, propertyLong1, propertyName1, propertySize1);
                                        propertyDatalist.add(propertyDataBean);
                                    }
                                    List<MyJobData.PropertyImgBean> imglist = new ArrayList<>();
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
                                        ownerlist.add(ownerDetailBean);
                                    }
                                    myJobData = new MyJobData(_id, propertyId, propertyName, bedroom, bathroom, price, propertySize, serviceDate, checkIn, checkOut, address, latitude, longitude, description, status, crd, propertyDatalist, imglist, ownerlist, categorylist);
                                    jobDataList.add(myJobData);
                                }
                                adapter.filter();
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
                pDialog.hide();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        myJobListApiData1(status, et_jobsearch.getText().toString());
    }
}


