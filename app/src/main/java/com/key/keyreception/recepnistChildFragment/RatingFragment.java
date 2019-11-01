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
import com.key.keyreception.helper.ProgressDialog;
import com.key.keyreception.recepnistChildFragment.Adapter.RatingAdapter;
import com.key.keyreception.recepnistChildFragment.model.RatingModel;

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
public class RatingFragment extends BaseFragment {

    private RatingAdapter adapter;
    private ProgressDialog pDialog;
    private Session session;
    private TextView tv_no_record;
    private List<RatingModel> ratingList = new ArrayList<>();
    private String currentTime;
    private RecyclerView recyclerView;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rating, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        init();
        pDialog = new ProgressDialog(mContext);
        session = new Session(mContext);
        earnListApiData();
    }

    public void init() {
        recyclerView = getView().findViewById(R.id.recycler_view);
        tv_no_record = getView().findViewById(R.id.tv_record);

    }

    public void earnListApiData() {

        String authtoken = session.getAuthtoken();
        pDialog.show();
        Call<ResponseBody> call = RetrofitClient.getInstance()
                .getApi().ratingList(authtoken);
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @SuppressLint("NewApi")
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull retrofit2.Response<ResponseBody> response) {
                try {
                    pDialog.dismiss();

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
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                    int _id = jsonObject1.getInt("_id");
                                    int rating = jsonObject1.getInt("rating");
                                    String review = jsonObject1.getString("review");
                                    String crd = jsonObject1.getString("crd");
                                    int jobId = jsonObject1.getInt("jobId");

                                    JSONArray senderarray = jsonObject1.getJSONArray("senderDetail");
                                    JSONObject senderObject = senderarray.getJSONObject(0);
                                    int userid = senderObject.getInt("_id");
                                    String profileImage = senderObject.getString("profileImage");
                                    String fullName = senderObject.getString("fullName");
                                    RatingModel ratingModel = new RatingModel(_id,rating,review,crd,jobId,userid,profileImage,fullName);
                                    ratingList.add(ratingModel);
                                }

                                adapter = new RatingAdapter(mContext, ratingList,currentTime);
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
                                recyclerView.setLayoutManager(mLayoutManager);
                                recyclerView.setItemAnimator(new DefaultItemAnimator());
                                recyclerView.setAdapter(adapter);
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
                pDialog.dismiss();
            }
        });

    }

}




