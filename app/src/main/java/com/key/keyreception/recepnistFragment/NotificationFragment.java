package com.key.keyreception.recepnistFragment;


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

import com.key.keyreception.R;
import com.key.keyreception.Session;
import com.key.keyreception.base.BaseFragment;
import com.key.keyreception.connection.RetrofitClient;
import com.key.keyreception.helper.PDialog;
import com.key.keyreception.ownerFragment.ownerAdapter.NotificationAdapter;
import com.key.keyreception.recepnistFragment.model.NotifiData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends BaseFragment {

    private NotificationAdapter adapter;
    private PDialog pDialog;
    private Session session;
    private String currentTime;
    private TextView tv_no_record;
    private ArrayList<NotifiData> notifilist = new ArrayList<>();
    private ArrayList<NotifiData.SenderDetailBean> senderlist = new ArrayList<>();


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //  return inflater.inflate(R.layout.fragment_appointment, container, false);
        return inflater.inflate(R.layout.fragment_notification, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        init(view);
        pDialog = new PDialog();
        session = new Session(mContext);
        notificationListApiData();

    }

    public void init(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.notification_recycler_view);
        tv_no_record = view.findViewById(R.id.notification_tv_no_record);
        adapter = new NotificationAdapter(mContext, notifilist, "r1");
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }


    public void notificationListApiData() {

        String authtoken = session.getAuthtoken();
        pDialog.pdialog(mContext);
        Call<ResponseBody> call = RetrofitClient.getInstance()
                .getApi().notificationList(authtoken);
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
                            currentTime = jsonObject.optString("currentTime");
                            if (statusCode.equals("success")) {

                                tv_no_record.setVisibility(View.GONE);
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    NotifiData notifiData;
                                    JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                                    int _id = jsonObject2.getInt("_id");
                                    int notifyId = jsonObject2.getInt("notifyId");
                                    int senderId = jsonObject2.getInt("senderId");
                                    int receiverId = jsonObject2.getInt("receiverId");
                                    int notifincationType = jsonObject2.getInt("notifincationType");
                                    String crd = jsonObject2.getString("crd");
                                    String forUserType = jsonObject2.getString("forUserType");
                                    String message = jsonObject2.getString("message");
                                    JSONArray jsonArray1 = jsonObject2.getJSONArray("senderDetail");
                                    for (int j = 0; j < jsonArray1.length(); j++) {
                                        NotifiData.SenderDetailBean senderDetailBean;
                                        JSONObject jsonObject3 = jsonArray1.getJSONObject(j);
                                        int _id1 = jsonObject3.getInt("_id");
                                        String profileImage = jsonObject3.getString("profileImage");
                                        String fullName = jsonObject3.getString("fullName");
                                        senderDetailBean = new NotifiData.SenderDetailBean(_id1, profileImage, fullName);
                                        senderlist.add(senderDetailBean);
                                    }

                                    notifiData = new NotifiData(forUserType, _id, notifyId, senderId, receiverId, notifincationType, crd, message, currentTime, senderlist);
                                    notifilist.add(notifiData);
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
