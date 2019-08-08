package com.key.keyreception.ownerChildFragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.key.keyreception.R;
import com.key.keyreception.Session;
import com.key.keyreception.activity.model.PropertyData;
import com.key.keyreception.base.BaseFragment;
import com.key.keyreception.connection.RetrofitClient;
import com.key.keyreception.helper.ProgressDialog;
import com.key.keyreception.ownerChildFragment.adapterchild.MyPropertyAdapter;

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
public class MyPropertyFragment extends BaseFragment {


    private MyPropertyAdapter adapter;
    private Session session;
    private ProgressDialog progressDialog;
    private TextView tv_recode;
    private List<PropertyData> propertyList = new ArrayList<>();


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_property, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        session = new Session(mContext);
        // pDialog = new PDialog();
        progressDialog = new ProgressDialog(mContext);
        init();
        propertyListApiData();
    }


    public void init() {
        RecyclerView recyclerView = getView().findViewById(R.id.myprop_recycler_view);
        tv_recode = getView().findViewById(R.id.tv_myprop_no_record);
        adapter = new MyPropertyAdapter(mContext, propertyList, new MyPropertyAdapter.Deletelist() {
            @Override
            public void deletpos(int i) {
                propertyDeleteApiData(String.valueOf(propertyList.get(i).get_id()));
                propertyList.remove(i);
                if (propertyList.size() == 0) {
                    tv_recode.setVisibility(View.VISIBLE);
                } else {
                    tv_recode.setVisibility(View.GONE);

                }
            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);



    }

    public void propertyListApiData() {
        String authtoken = session.getAuthtoken();
        progressDialog.show();
        Call<ResponseBody> call = RetrofitClient.getInstance()
                .getApi().propertyList(authtoken);
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @SuppressLint("NewApi")
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull retrofit2.Response<ResponseBody> response) {
                try {
                    progressDialog.dismiss();
                    switch (response.code()) {
                        case 200: {
                            String stresult = Objects.requireNonNull(response.body()).string();
                            Log.d("response", stresult);
                            JSONObject jsonObject = new JSONObject(stresult);
                            String statusCode = jsonObject.optString("status");
                            String msg = jsonObject.optString("message");
                            if (statusCode.equals("success")) {
                                propertyList.clear();
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    tv_recode.setVisibility(View.GONE);
                                    PropertyData propertyData;
                                    JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                                    int _id = jsonObject2.getInt("_id");
                                    String propertyName = jsonObject2.getString("propertyName");
                                    String bedroom = jsonObject2.getString("bedroom");
                                    String bathroom = jsonObject2.getString("bathroom");
                                    String propertySize = jsonObject2.getString("propertySize");
                                    String propertyAddress = jsonObject2.getString("propertyAddress");
                                    String propertyLat = jsonObject2.getString("propertyLat");
                                    String propertyLong = jsonObject2.getString("propertyLong");
                                    int userId = jsonObject2.getInt("userId");
                                    int status = jsonObject2.getInt("status");
                                    String crd = jsonObject2.getString("crd");
                                    List<PropertyData.PropertyImgBean> propertyImgs = new ArrayList<>();
                                    JSONArray jsonArray1 = jsonObject2.getJSONArray("propertyImg");
                                    for (int j = 0; j < jsonArray1.length(); j++) {
                                        JSONObject jsonObject3 = jsonArray1.getJSONObject(j);
                                        PropertyData.PropertyImgBean propertyImgBean = new PropertyData.PropertyImgBean();
                                        propertyImgBean.set_id(jsonObject3.getInt("_id"));
                                        propertyImgBean.setPropertyImage(jsonObject3.getString("propertyImage"));
                                        propertyImgs.add(propertyImgBean);
                                    }
                                    List<PropertyData.OwnerDetailBean> ownerDetaillist = new ArrayList<>();
                                    JSONArray jsonArray2 = jsonObject2.getJSONArray("ownerDetail");
                                    for (int j = 0; j < jsonArray2.length(); j++) {
                                        JSONObject jsonObject4 = jsonArray2.getJSONObject(j);
                                        PropertyData.OwnerDetailBean ownerDetailBean;
                                        int _id2 = jsonObject4.getInt("_id");
                                        String profileImage = jsonObject4.getString("profileImage");
                                        String fullName = jsonObject4.getString("fullName");
                                        ownerDetailBean = new PropertyData.OwnerDetailBean(_id2, profileImage, fullName);
                                        ownerDetaillist.add(ownerDetailBean);
                                    }
                                    propertyData = new PropertyData(_id, propertyName, bedroom, bathroom, propertySize, propertyAddress, propertyLat, propertyLong, userId, status, crd, propertyImgs, ownerDetaillist);
                                    propertyList.add(propertyData);
                                }
                                adapter.notifyDataSetChanged();


                            } else {
                                propertyList.clear();
                                adapter.notifyDataSetChanged();
                                tv_recode.setVisibility(View.VISIBLE);
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
                progressDialog.dismiss();
            }
        });

    }

    public void propertyDeleteApiData(String pid) {
        String authtoken = session.getAuthtoken();
        progressDialog.show();
        Call<ResponseBody> call = RetrofitClient.getInstance()
                .getApi().deleteProperty(authtoken, pid);
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @SuppressLint("NewApi")
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull retrofit2.Response<ResponseBody> response) {
                try {
                    progressDialog.dismiss();
                    switch (response.code()) {
                        case 200: {

                            String stresult = Objects.requireNonNull(response.body()).string();
                            Log.d("response", stresult);
                            JSONObject jsonObject = new JSONObject(stresult);
                            String statusCode = jsonObject.optString("status");
                            String msg = jsonObject.optString("message");
                            if (statusCode.equals("success")) {
                                adapter.notifyDataSetChanged();
                                Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
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
                progressDialog.dismiss();
            }
        });

    }


    @Override
    public void onResume() {
        super.onResume();
        propertyListApiData();
    }
}