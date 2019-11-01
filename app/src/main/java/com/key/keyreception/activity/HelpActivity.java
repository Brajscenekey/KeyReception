package com.key.keyreception.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.key.keyreception.R;
import com.key.keyreception.activity.ActivityAdapter.HelpAdapter;
import com.key.keyreception.activity.model.HelpModal;
import com.key.keyreception.base.BaseActivity;
import com.key.keyreception.connection.RetrofitClient;
import com.key.keyreception.helper.ProgressDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class HelpActivity extends BaseActivity {

    private RecyclerView rv_help;
    private HelpAdapter helpAdapter;
    private TextView no_data_found;
    private ProgressDialog progressbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        progressbar = new ProgressDialog(this);


        ImageView iv_back_icon = findViewById(R.id.iv_back_icon);
        rv_help = findViewById(R.id.rv_help);
        no_data_found = findViewById(R.id.no_data_found);

        iv_back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        loadHelp();
    }

    void loadHelp() {

        progressbar.show();
        Call<ResponseBody> call = RetrofitClient.getInstance()
                .getAnotherApi().getHelpList("de4b20c7699b14fe1a38251be1fbd820f2039a9c");
        call.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("NewApi")
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull retrofit2.Response<ResponseBody> response) {
                try {
                    progressbar.dismiss();
                    switch (response.code()) {
                        case 200: {
                            String stresult = Objects.requireNonNull(response.body()).string();
                            Log.d("response", stresult);
                            JSONObject jsonObject = new JSONObject(stresult);
                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");

                            if (status.equals("success")) {

                                JSONArray data = jsonObject.getJSONArray("data");

                                if (data.length() != 0) {
                                    ArrayList<HelpModal.DataBean> arrayList = new ArrayList<>();
                                    for (int i = 0; i < data.length(); i++) {

                                        JSONObject jsonObject1 = data.getJSONObject(i);
                                        HelpModal.DataBean dataBean = new HelpModal.DataBean();
                                        int j = 0;
                                        j += 1 + i;

                                        dataBean.setId(Integer.toString(j));
                                        dataBean.setQuestion(jsonObject1.getString("question"));
                                        dataBean.setAnswer(jsonObject1.getString("answer"));
                                        arrayList.add(dataBean);
                                    }

                                    helpAdapter = new HelpAdapter(HelpActivity.this, arrayList);
                                    rv_help.setAdapter(helpAdapter);

                                } else {
                                    no_data_found.setVisibility(View.VISIBLE);
                                }

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
                                Toast.makeText(HelpActivity.this, msg, Toast.LENGTH_SHORT).show();
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
                progressbar.dismiss();
            }
        });


    }
}
