package com.key.keyreception.recepnistChildFragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.key.keyreception.R;
import com.key.keyreception.Session;
import com.key.keyreception.base.BaseFragment;
import com.key.keyreception.connection.RetrofitClient;
import com.key.keyreception.helper.PDialog;
import com.key.keyreception.recepnistChildFragment.Adapter.RearnAdapter;
import com.key.keyreception.recepnistChildFragment.model.EarningData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * A simple {@link Fragment} subclass.
 */
public class RearningFragment extends BaseFragment {

    private RearnAdapter adapter;
    private PDialog pDialog;
    private Session session;
    private TextView tv_no_record;
    private ArrayList<EarningData> earnlist = new ArrayList<>();
    private EarningData.JobDetailBean jobDetailBean;
    private ArrayList<EarningData.CategoryBean> catlist = new ArrayList<>();
    private ArrayList<EarningData.SenderDetailBean> sendlist = new ArrayList<>();


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_earn, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        init();
        pDialog = new PDialog();
        session = new Session(mContext);
        earnListApiData();
    }

    public void init() {
        RecyclerView recyclerView = getView().findViewById(R.id.rearning_recycler_view);
        tv_no_record = getView().findViewById(R.id.rearning_tv_no_record);
        adapter = new RearnAdapter(mContext, earnlist);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    public void earnListApiData() {

        String authtoken = session.getAuthtoken();
        pDialog.pdialog(mContext);
        Call<ResponseBody> call = RetrofitClient.getInstance()
                .getApi().earningList(authtoken);
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

                                tv_no_record.setVisibility(View.GONE);
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    int _id = jsonObject1.getInt("_id");
                                    int jobId = jsonObject1.getInt("jobId");
                                    int senderId = jsonObject1.getInt("senderId");
                                    int receiverId = jsonObject1.getInt("receiverId");
                                    String amount = jsonObject1.getString("amount");
                                    String paymentType = jsonObject1.getString("paymentType");
                                    int status = jsonObject1.getInt("status");
                                    JSONObject jsonObject11 = jsonObject1.getJSONObject("jobDetail");
                                    String propertyId = jsonObject11.getString("propertyId");
                                    String propertyName = jsonObject11.getString("propertyName");
                                    String price = jsonObject11.getString("price");
                                    String serviceDate = jsonObject11.getString("serviceDate");
                                    String checkIn = jsonObject11.getString("checkIn");
                                    String checkOut = jsonObject11.getString("checkOut");
                                    String address = jsonObject11.getString("address");
                                    String latitude = jsonObject11.getString("latitude");
                                    String longitude = jsonObject11.getString("longitude");
                                    String description = jsonObject11.getString("description");
                                    int status1 = jsonObject11.getInt("status");
                                    jobDetailBean = new EarningData.JobDetailBean(propertyId, propertyName, price, serviceDate, checkIn, checkOut, address, latitude, longitude, description, status1);

                                    JSONArray jsonArray1 = jsonObject1.getJSONArray("category");
                                    for (int j = 0; j < jsonArray1.length(); j++) {
                                        JSONObject jsonObject2 = jsonArray1.getJSONObject(j);
                                        int _id2 = jsonObject2.getInt("_id");
                                        String title = jsonObject2.getString("title");
                                        EarningData.CategoryBean categoryBean = new EarningData.CategoryBean(_id2, title);
                                        catlist.add(categoryBean);

                                    }
                                    JSONArray jsonArray2 = jsonObject1.getJSONArray("senderDetail");
                                    for (int j = 0; j < jsonArray2.length(); j++) {
                                        JSONObject jsonObject3 = jsonArray2.getJSONObject(j);
                                        int _id3 = jsonObject3.getInt("_id");
                                        String profileImage = jsonObject3.getString("profileImage");
                                        String fullName = jsonObject3.getString("fullName");
                                        EarningData.SenderDetailBean senderDetailBean = new EarningData.SenderDetailBean(_id3, profileImage, fullName);
                                        sendlist.add(senderDetailBean);
                                    }

                                    EarningData earningData = new EarningData(_id, jobId, senderId, receiverId, amount, paymentType, status, jobDetailBean, catlist, sendlist);
                                    earnlist.add(earningData);
                                }
                                adapter.notifyDataSetChanged();


                            } else {
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

}



