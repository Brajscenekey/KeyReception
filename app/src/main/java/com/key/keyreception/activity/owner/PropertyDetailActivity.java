package com.key.keyreception.activity.owner;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.key.keyreception.R;
import com.key.keyreception.Session;
import com.key.keyreception.activity.ActivityAdapter.DetailsSliderAdapter;
import com.key.keyreception.activity.model.PropertyData;
import com.key.keyreception.base.BaseActivity;
import com.key.keyreception.connection.RetrofitClient;
import com.key.keyreception.helper.PDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class PropertyDetailActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {


    ArrayList<PropertyData> propertylist;
    private DetailsSliderAdapter detailsSliderAdapter;
    private int _id;
    private ViewPager viewPager;
    private ArrayList<PropertyData.PropertyImgBean> propertyImgsList;
    private String propertyid;
    private TextView tv_psize, tv_pname, tv_pbed, tv_pbath, tv_address;
    private PDialog pDialog;
    private Session session;
    private String position = "", propertyName = "", bedroom = "", bathroom = "", propertySize = "", propertyLat = "", propertyAddress = "", propertyLong = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        setContentView(R.layout.activity_property_detail);
        pDialog = new PDialog();
        session = new Session(this);
        inItView();
        propertyListApiData();
    }

    public void inItView() {
        propertyImgsList = new ArrayList<>();
        Intent intent = getIntent();
        propertyid = intent.getStringExtra("propertyid");
        viewPager = findViewById(R.id.detail_slider_pager);
        tv_pname = findViewById(R.id.tv_pdetail_pname);
        tv_psize = findViewById(R.id.tv_pdetail_psize);
        tv_pbath = findViewById(R.id.tv_pdetail_bathroom);
        tv_pbed = findViewById(R.id.tv_pdetail_bedroom);
        tv_address = findViewById(R.id.tv_pdetail_address);
        ImageView iv_delete = findViewById(R.id.iv_pdetail_delete);
        ImageView iv_propjob_detail_back = findViewById(R.id.iv_propjob_detail_back);
        ImageView iv_edit = findViewById(R.id.iv_pdetail_edit);
        iv_delete.setOnClickListener(this);
        iv_propjob_detail_back.setOnClickListener(this);
        iv_edit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_propjob_detail_back: {
                backPress();
            }
            break;
            case R.id.iv_pdetail_delete: {
                propertyDeleteApiData();
            }
            break;
            case R.id.iv_pdetail_edit: {
                Intent intent = new Intent(PropertyDetailActivity.this, EditPropertyActivity.class);
                intent.putExtra("propertyName", propertyName);
                intent.putExtra("bedroom", bedroom);
                intent.putExtra("intent", "2");
                intent.putExtra("bathroom", bathroom);
                intent.putExtra("propertySize", propertySize);
                intent.putExtra("propertyAddress", propertyAddress);
                intent.putExtra("propertyLat", propertyLat);
                intent.putExtra("propertyLong", propertyLong);
                intent.putExtra("propertyid", propertyid);
                Bundle args = new Bundle();
                args.putSerializable("ARRAYLIST", propertyImgsList);
                intent.putExtra("BUNDLE", args);
                startActivityForResult(intent, 10006);
                finish();
            }
            break;

        }
    }

    public void propertyListApiData() {
        String authtoken = session.getAuthtoken();
        pDialog.pdialog(this);
        Call<ResponseBody> call = RetrofitClient.getInstance()
                .getApi().propertyDetail(authtoken, propertyid);
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
                            String msg = jsonObject.optString("message");
                            if (statusCode.equals("success")) {
                                JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                                _id = jsonObject1.getInt("_id");
                                propertyName = jsonObject1.getString("propertyName");
                                bedroom = jsonObject1.getString("bedroom");
                                bathroom = jsonObject1.getString("bathroom");
                                propertySize = jsonObject1.getString("propertySize");
                                propertyAddress = jsonObject1.getString("propertyAddress");
                                propertyLat = jsonObject1.getString("propertyLat");
                                propertyLong = jsonObject1.getString("propertyLong");
                                String pimage = "";
                                JSONArray jsonArray1 = jsonObject1.getJSONArray("propertyImg");
                                for (int j = 0; j < jsonArray1.length(); j++) {
                                    JSONObject jsonObject3 = jsonArray1.getJSONObject(j);
                                    int id = jsonObject3.getInt("_id");
                                    pimage = jsonObject3.getString("propertyImage");
                                    PropertyData.PropertyImgBean propertyImgBean = new PropertyData.PropertyImgBean(id, pimage);
                                    propertyImgsList.add(propertyImgBean);
                                }
                                detailsSliderAdapter = new DetailsSliderAdapter(PropertyDetailActivity.this, propertyImgsList);
                                viewPager.setAdapter(detailsSliderAdapter);
                                viewPager.setOnPageChangeListener(PropertyDetailActivity.this);
                                setPropertyData(propertyName, propertyAddress, propertySize, bedroom, bathroom, pimage);
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
                                Toast.makeText(PropertyDetailActivity.this, msg, Toast.LENGTH_SHORT).show();
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

    @SuppressLint({"CheckResult", "SetTextI18n"})
    public void setPropertyData(String pname, String paddress, String psize, String pbed, String pbath, String propimage) {
        tv_pname.setText(pname);
        tv_psize.setText(psize + " Sq Feet");
        tv_address.setText(paddress);
        tv_pbed.setText(pbed + " Bedroom");
        tv_pbath.setText(pbath + " Bathroom");
        if (!propimage.isEmpty()) {
            RequestOptions options = new RequestOptions();
            options.placeholder(R.drawable.home_image);
            options.diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        }
    }

    public void propertyDeleteApiData() {
        String authtoken = session.getAuthtoken();
        pDialog.pdialog(this);
        Call<ResponseBody> call = RetrofitClient.getInstance()
                .getApi().deleteProperty(authtoken, propertyid);
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
                            String msg = jsonObject.optString("message");
                            if (statusCode.equals("success")) {
                                Toast.makeText(PropertyDetailActivity.this, msg, Toast.LENGTH_SHORT).show();
                                setResult(Activity.RESULT_OK);
                                finish();
                            } else {
                                Toast.makeText(PropertyDetailActivity.this, msg, Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(PropertyDetailActivity.this, msg, Toast.LENGTH_SHORT).show();
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

    @Override
    public void onPageScrolled(int i, float v, int i1) {
    }

    @Override
    public void onPageSelected(int i) {

    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
}

