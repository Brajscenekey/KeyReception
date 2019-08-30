package com.key.keyreception.activity.owner;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
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

public class OwnerNotifiDetailActivity extends BaseActivity implements View.OnClickListener {

    private PDialog pDialog;
    private String price, receiverId = "";
    private Session session;
    private TextView tv_addservice_price,spcomdetail_tv_post, tvpropname, tvaddress, tvbedroom, tvbathroom, tvpropsize, tvpropprice, tvserdate, tvstatus, tvservicename, tvdescription, tvvendorname;
    private ImageView serviceimage, own_detailimg;
    private ImageView vendorimage;
    private ImageView ivchat;
    private String jobid, paymenttype = "";
    private Button btn_detail_payment, btn_review_payment;
    private RelativeLayout rl_post_completejob;
    private String recepid = "", recepprofileImage = "", recepfullName = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        setContentView(R.layout.activity_detail);
        session = new Session(this);
        pDialog = new PDialog();
        init();


        if (getIntent().getStringExtra("notificationDetail_Jobid") != null) {
            String reference_id = getIntent().getStringExtra("notificationDetail_Jobid");
            session.putjobid(reference_id);
        }
        jobdetailApiData();

    }

    private void init() {
        tvpropname = findViewById(R.id.tv_myjob_detail_pname);
        tvaddress = findViewById(R.id.tv_myjob_detail_address);
        tvbedroom = findViewById(R.id.tv_myjob_detail_bedroom);
        tvbathroom = findViewById(R.id.tv_myjob_detail_bathroom);
        tvpropsize = findViewById(R.id.tv_myjob_detail_psize);
        tvpropprice = findViewById(R.id.tv_myjob_detail_price);
        tvserdate = findViewById(R.id.tv_myjob_detail_sdate);
        tvstatus = findViewById(R.id.tv_myjob_detail_status);
        tvservicename = findViewById(R.id.tv_myjob_detail_servicename);
        tvdescription = findViewById(R.id.tv_myjob_detail_description);
        tvvendorname = findViewById(R.id.tv_myjob_detail_vendorname);
        btn_detail_payment = findViewById(R.id.btn_detail_payment);
        btn_review_payment = findViewById(R.id.btn_review_payment);
        own_detailimg = findViewById(R.id.own_detailimg);
        ImageView ivback = findViewById(R.id.iv_myjob_detail_back);
        ivback.setOnClickListener(this);
        btn_review_payment.setOnClickListener(this);
        serviceimage = findViewById(R.id.iv_myjob_detail_serviceimage);
        vendorimage = findViewById(R.id.iv_myjob_detail_vendorimage);
        spcomdetail_tv_post = findViewById(R.id.spcomdetail_tv_post);
        rl_post_completejob = findViewById(R.id.rl_post_completejob);
        ivchat = findViewById(R.id.iv_myjob_detail_vendorchat);
        btn_detail_payment.setOnClickListener(this);
        ivchat.setOnClickListener(this);
        paymenttype = session.getPayment();
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        TextView Addservice = findViewById(R.id.Addservice);
        tv_addservice_price = findViewById(R.id.tv_addservice_price);
        recyclerView.setVisibility(View.GONE);
        Addservice.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.iv_myjob_detail_back: {
                backPress();
            }
            break;
            case R.id.btn_detail_payment: {
                if (!paymenttype.isEmpty()) {
                    Intent intent = new Intent(OwnerNotifiDetailActivity.this, MakePaymentActivity.class);
                    intent.putExtra("jobId", jobid);
                    intent.putExtra("receiverId", receiverId);
                    intent.putExtra("amount", price);
                    startActivity(intent);
                } else {
                    alertPayOption();
                }

            }
            break;

            case R.id.btn_review_payment: {
                Toast.makeText(this, "Under in development", Toast.LENGTH_SHORT).show();
            }
            break;
            case R.id.iv_myjob_detail_vendorchat: {
               /* Intent intent = new Intent(this,ChattingActivity.class);
                intent.putExtra("id",recepid);
                intent.putExtra("profileImage",recepprofileImage);
                intent.putExtra("fullName",recepfullName);
                startActivity(intent);
                finish();*/
            }
            break;
        }

    }

    public void jobdetailApiData() {
        pDialog.pdialog(OwnerNotifiDetailActivity.this);
        String authtoken = session.getAuthtoken();
        jobid = session.getjobid();
        Call<ResponseBody> call = RetrofitClient.getInstance()
                .getApi().jobDetail(authtoken, jobid);
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
                                price = jsonObject2.getString("price");
                                String serviceDate = jsonObject2.getString("serviceDate");
                                String description = jsonObject2.getString("description");
                                String status = String.valueOf(jsonObject2.getInt("status"));
                                JSONArray jsonArray = jsonObject2.optJSONArray("category");
                                JSONObject jsonObject21 = jsonArray.getJSONObject(0);
                                String title = jsonObject21.getString("title");
                                String image = jsonObject21.getString("image");
                                JSONArray jsonArraydata = jsonObject2.getJSONArray("propertyData");
                                JSONObject jsonObjectdata = jsonArraydata.getJSONObject(0);
                                String bathroom = jsonObjectdata.getString("bathroom");
                                String bedroom = jsonObjectdata.getString("bedroom");
                                String address = jsonObjectdata.getString("propertyAddress");
                                String propertyName = jsonObjectdata.getString("propertyName");
                                String propertySize = jsonObjectdata.getString("propertySize");

                                JSONArray jsonarrrecep = jsonObject2.getJSONArray("receptionistDetail");
                                if (jsonarrrecep.length() > 0) {
                                    JSONObject jsonobjrecep = jsonarrrecep.getJSONObject(0);
                                    recepid = jsonobjrecep.getString("_id");
                                    recepprofileImage = jsonobjrecep.getString("profileImage");
                                    recepfullName = jsonobjrecep.getString("fullName");
                                }
                                JSONArray jsonArray1 = jsonObject2.optJSONArray("receptionistDetail");
                                if (jsonArray1.length() > 0) {
                                    JSONObject jsonObject22 = jsonArray1.getJSONObject(0);
                                    receiverId = jsonObject22.getString("_id");
                                }
                                String propertyImage = "";
                                JSONArray jsonArray3 = jsonObject2.getJSONArray("propertyImg");
                                if (jsonArray3.length() > 0) {
                                    JSONObject jsonObject5 = jsonArray3.getJSONObject(0);
                                    propertyImage = jsonObject5.getString("propertyImage");
                                }

                                setDetailData(propertyName, address, bedroom, bathroom, propertySize, price, serviceDate, status, title, description, recepfullName, image, propertyImage, recepprofileImage);
                            } else {
                                Toast.makeText(OwnerNotifiDetailActivity.this, msg, Toast.LENGTH_SHORT).show();
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

                                Toast.makeText(OwnerNotifiDetailActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }
                            break;
                        }
                        case 401:
                            try {
                                session.logout();
                                Toast.makeText(OwnerNotifiDetailActivity.this, "Session expired, please login again.", Toast.LENGTH_SHORT).show();

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

    @SuppressLint({"SetTextI18n", "CheckResult"})
    private void setDetailData(String pname, String add, String bed, String bath, String psize, String price, String stdate, String status, String sname, String des, String vname, String simage, String propimg, String rimage) {
        tvpropname.setText(pname);
        tvaddress.setText(add);
        tvbedroom.setText(bed + " Bedroom");
        tvbathroom.setText(bath + " Bathroom");
        tvpropsize.setText(psize + " Sq Feet");
        tvpropprice.setText("$" + price);
        tv_addservice_price.setText("$" + price);

        tvvendorname.setText(vname);
        if (rimage.length() != 0) {
            Glide.with(this).load(rimage).into(vendorimage);
        }

        if (status.equals("1")) {
            rl_post_completejob.setVisibility(View.GONE);
            spcomdetail_tv_post.setVisibility(View.GONE);
            ivchat.setVisibility(View.GONE);
        } else {
            rl_post_completejob.setVisibility(View.VISIBLE);
            spcomdetail_tv_post.setVisibility(View.VISIBLE);
            ivchat.setVisibility(View.VISIBLE);
        }


        if (propimg.length() != 0) {
            RequestOptions options = new RequestOptions();
            options.placeholder(R.drawable.home_image);
            options.diskCacheStrategy(DiskCacheStrategy.RESOURCE);
            Glide.with(OwnerNotifiDetailActivity.this).load(propimg).apply(options).into(own_detailimg);
        }
        try {
            if (stdate.contains("-")) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
                Date date = sdf.parse(stdate);
                SimpleDateFormat mSDF = new SimpleDateFormat("dd, MMMM hh:mm a", Locale.getDefault());
                String fDate = mSDF.format(date);
                tvserdate.setText(fDate);
            } else {
                tvserdate.setText(stdate);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


        switch (status) {
            case "1":
                tvstatus.setText(getResources().getString(R.string.Pending));
                tvstatus.setTextColor(getResources().getColor(R.color.colorPending));
                btn_detail_payment.setVisibility(View.GONE);
                btn_review_payment.setVisibility(View.GONE);

                break;
            case "2":
                tvstatus.setText(getResources().getString(R.string.confirm));
                tvstatus.setTextColor(getResources().getColor(R.color.appcolor));
                btn_detail_payment.setVisibility(View.GONE);
                btn_review_payment.setVisibility(View.GONE);


                break;

            case "3":
                tvstatus.setText(getResources().getString(R.string.inpro));
                tvstatus.setTextColor(getResources().getColor(R.color.colorInprogress));
                btn_detail_payment.setVisibility(View.GONE);
                btn_review_payment.setVisibility(View.GONE);

                break;
            case "5":
                tvstatus.setText(getResources().getString(R.string.paymentcomplete));
                tvstatus.setTextColor(getResources().getColor(R.color.colorDarkGreen));
                btn_detail_payment.setVisibility(View.GONE);
                btn_review_payment.setVisibility(View.VISIBLE);

                break;
            default:
                tvstatus.setText(getResources().getString(R.string.complete));
                tvstatus.setTextColor(getResources().getColor(R.color.colorDarkGreen));
                btn_detail_payment.setVisibility(View.VISIBLE);
                btn_review_payment.setVisibility(View.GONE);

                break;
        }
        tvservicename.setText(sname);
        tvdescription.setText(des);
        tvvendorname.setText(vname);
        Glide.with(OwnerNotifiDetailActivity.this).load(simage).into(serviceimage);
    }

    public void alertPayOption() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(OwnerNotifiDetailActivity.this);

        alertDialog.setTitle("Alert");
        alertDialog.setMessage("Please add your payment option");
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(OwnerNotifiDetailActivity.this, PayOptionActivity.class);
                intent.putExtra("payid", "p1");
                startActivity(intent);
                dialog.dismiss();
            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

            }
        });
        alertDialog.show();
    }
}
