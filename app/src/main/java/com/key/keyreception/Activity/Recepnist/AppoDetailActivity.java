package com.key.keyreception.Activity.Recepnist;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.key.keyreception.Activity.DetailActivity;
import com.key.keyreception.R;
import com.key.keyreception.Session;
import com.key.keyreception.base.BaseActivity;
import com.key.keyreception.connection.RetrofitClient;
import com.key.keyreception.helper.PDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class AppoDetailActivity extends BaseActivity implements View.OnClickListener {


    private PDialog pDialog;
    private String usertype;
    private Session session;
    private TextView tvpropname,tvaddress,tvbedroom,tvbathroom,tvpropsize,tvpropprice,tvserdate,tvstatus,tvservicename,tvdescription,tvvendorname;
    private ImageView serviceimage;
    private ImageView vendorimage;
    private ImageView ivchat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appo_detail);
        session = new Session(this);
        pDialog = new PDialog();
        init();
        jobdetailApiData();
    }
    private void init()
    {
        tvpropname = findViewById(R.id.tv_appo_detail_pname);
        tvaddress = findViewById(R.id.tv_appo_detail_address);
        tvbedroom = findViewById(R.id.tv_appo_detail_bedroom);
        tvbathroom = findViewById(R.id.tv_appo_detail_bathroom);
        tvpropsize = findViewById(R.id.tv_appo_detail_psize);
        tvpropprice = findViewById(R.id.tv_appo_detail_price);
        tvserdate = findViewById(R.id.tv_appo_detail_sdate);
        tvstatus = findViewById(R.id.tv_appo_detail_status);
        tvservicename = findViewById(R.id.tv_appo_detail_servicename);
        tvdescription = findViewById(R.id.tv_appo_detail_description);
        tvvendorname = findViewById(R.id.tv_appo_detail_vendorname);
        ImageView ivback = findViewById(R.id.iv_appo_detail_back);
        ivback.setOnClickListener(this);

        serviceimage = findViewById(R.id.iv_appo_detail_serviceimage);
        vendorimage = findViewById(R.id.iv_appo_detail_vendorimage);
        ivchat = findViewById(R.id.iv_appo_detail_vendorchat);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.iv_appo_detail_back:
                backPress();
        }
    }

    public void jobdetailApiData() {
        pDialog.pdialog(AppoDetailActivity.this);
        String authtoken = session.getAuthtoken();
        String jobid = session.getjobid();
        Call<ResponseBody> call = RetrofitClient.getInstance()
                .getApi().jobDetail(authtoken,jobid);
        call.enqueue(new Callback<ResponseBody>() {
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
                                JSONObject jsonObject2 = jsonObject.getJSONObject("data");
                                String _id = jsonObject2.getString("_id");
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
                                String status = jsonObject2.getString("status");
                                String crd = jsonObject2.getString("crd");
                                JSONArray jsonArray1 = jsonObject2.optJSONArray("ownerDetail");
                                JSONObject jsonObject211 = jsonArray1.getJSONObject(0);
                                String profileImage = jsonObject211.getString("profileImage");
                                String fullName = jsonObject211.getString("fullName");
                                JSONArray jsonArray = jsonObject2.optJSONArray("category");
                                JSONObject jsonObject21 = jsonArray.getJSONObject(0);
                                String title = jsonObject21.getString("title");
                                String image = jsonObject21.getString("image");

                                setDetailData(propertyName,address,bedroom,bathroom,propertySize,price,serviceDate,status,title,description,image,fullName,profileImage);
                            } else {
                                Toast.makeText(AppoDetailActivity.this, msg, Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(AppoDetailActivity.this, msg, Toast.LENGTH_SHORT).show();
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
    @SuppressLint("SetTextI18n")
    private void setDetailData(String pname, String add, String bed, String bath, String psize, String price, String stdate, String status, String sname, String des, String simage,String ownname,String ownimage)
    {
        tvpropname.setText(pname);
        tvaddress.setText(add);
        tvbedroom.setText(bed+" Bedroom");
        tvbathroom.setText(bath+" Bathroom");
        tvpropsize.setText(psize+" Sq Feet");
        tvpropprice.setText("$"+price+".00");
        try {
            if(stdate.contains("-")) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
                Date date = sdf.parse(stdate);
                SimpleDateFormat mSDF = new SimpleDateFormat("dd, MMMM hh:mm a", Locale.getDefault());
                String fDate = mSDF.format(date);
                tvserdate.setText(fDate);
            } else { tvserdate.setText(stdate); }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        switch (status) {
            case "1":
                tvstatus.setText(getResources().getString(R.string.Pending));
                tvstatus.setTextColor(getResources().getColor(R.color.colorPending));

                break;
            case "2":
                tvstatus.setText(getResources().getString(R.string.inpro));
                tvstatus.setTextColor(getResources().getColor(R.color.colorInprogress));


                break;
            default:
                tvstatus.setText(getResources().getString(R.string.complete));
                tvstatus.setTextColor(getResources().getColor(R.color.colorDarkGreen));

                break;
        }

        tvservicename.setText(sname);
        tvdescription.setText(des);
        tvvendorname.setText(ownname);
        if (simage.length() != 0)
        {
            Glide.with(AppoDetailActivity.this).load(simage).into(serviceimage);
        }
        if (ownimage.length() != 0)
        {
            Glide.with(AppoDetailActivity.this).load(ownimage).into(vendorimage);
        }
    }
}
