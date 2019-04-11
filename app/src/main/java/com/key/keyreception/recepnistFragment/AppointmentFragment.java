package com.key.keyreception.recepnistFragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import com.key.keyreception.Activity.model.MyJobData;
import com.key.keyreception.R;
import com.key.keyreception.Session;
import com.key.keyreception.base.BaseFragment;
import com.key.keyreception.connection.RetrofitClient;
import com.key.keyreception.helper.PDialog;
import com.key.keyreception.recepnistFragment.adapter.AppoAdapter;

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
public class AppointmentFragment extends BaseFragment implements View.OnClickListener {


    private AppoAdapter adapter;
    private Session session;
    private PDialog pDialog;
    private RelativeLayout rl_filter;
    private EditText et_jobsearch;
    private TextView tv_complete, tv_inprogress;
    private List<MyJobData> jobDataList = new ArrayList<>();
    private List<MyJobData.CategoryBean> categorylist = new ArrayList<>();
    private List<MyJobData.OwnerDetailBean> ownerList = new ArrayList<>();
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
        init(view);
        myJobListApiData("2", "");
    }

    public void init(View view) {
        ImageView iv_filter = view.findViewById(R.id.iv_Appofilter);
        rl_filter = view.findViewById(R.id.rl_Appofilter);
        tv_complete = view.findViewById(R.id.tv_Appocomplete);
        et_jobsearch = view.findViewById(R.id.et_Appojobsearch);
        et_jobsearch.addTextChangedListener(watcherClass_search);
        tv_inprogress = view.findViewById(R.id.tv_Appoinprogress);
        tv_no_record = view.findViewById(R.id.tv_no_record);
        LinearLayout ly_complete = view.findViewById(R.id.ly_Appocomplete);
        LinearLayout ly_inprogress = view.findViewById(R.id.ly_Appoinprogress);
        ly_complete.setOnClickListener(this);
        ly_inprogress.setOnClickListener(this);
        iv_filter.setOnClickListener(this);
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

                tv_complete.setTextColor(getResources().getColor(R.color.colorDarkGreen));
                tv_inprogress.setTextColor(getResources().getColor(R.color.colorPrimary));
                myJobListApiData1("3", "");
                status = "3";
                rl_filter.setVisibility(View.GONE);

            }
            break;

            case R.id.ly_Appoinprogress: {

                tv_inprogress.setTextColor(getResources().getColor(R.color.colorInprogress));
                tv_complete.setTextColor(getResources().getColor(R.color.colorPrimary));
                myJobListApiData1("2", "");
                status = "2";
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

    //    public void switchApi() {
//        myJobListApiData("1","");
//    }
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
                                    String propertyId = jsonObject2.getString("propertyId");
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
                                    JSONArray jsonArray2 = jsonObject2.getJSONArray("ownerDetail");
                                    for (int k = 0; k < jsonArray2.length(); k++) {

                                        MyJobData.OwnerDetailBean ownerDetailBean;
                                        JSONObject jsonObject4 = jsonArray2.getJSONObject(k);
                                        int _id1 = jsonObject4.getInt("_id");
                                        String profileImage = jsonObject4.getString("profileImage");
                                        String fullName = jsonObject4.getString("fullName");

                                        ownerDetailBean = new MyJobData.OwnerDetailBean(_id1, profileImage,fullName);
                                        ownerList.add(ownerDetailBean);
                                    }

                                    myJobData = new MyJobData(_id, propertyId, propertyName, bedroom, bathroom, price, propertySize, serviceDate, checkIn, checkOut, address, latitude, longitude, description, status, crd, categorylist,ownerList);
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
                                    String propertyId = jsonObject2.getString("propertyId");
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
                                    JSONArray jsonArray2 = jsonObject2.getJSONArray("ownerDetail");
                                    for (int k = 0; k < jsonArray2.length(); k++) {

                                        MyJobData.OwnerDetailBean ownerDetailBean;
                                        JSONObject jsonObject4 = jsonArray2.getJSONObject(k);
                                        int _id1 = jsonObject4.getInt("_id");
                                        String profileImage = jsonObject4.getString("profileImage");
                                        String fullName = jsonObject4.getString("fullName");

                                        ownerDetailBean = new MyJobData.OwnerDetailBean(_id1, profileImage,fullName);
                                        ownerList.add(ownerDetailBean);
                                    }

                                    myJobData = new MyJobData(_id, propertyId, propertyName, bedroom, bathroom, price, propertySize, serviceDate, checkIn, checkOut, address, latitude, longitude, description, status, crd, categorylist,ownerList);
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
}


