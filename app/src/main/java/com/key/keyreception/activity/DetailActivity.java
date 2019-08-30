package com.key.keyreception.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.key.keyreception.R;
import com.key.keyreception.Session;
import com.key.keyreception.activity.ActivityAdapter.AdditionalServiceAdapter;
import com.key.keyreception.activity.ActivityAdapter.GetAddServiceAdapter;
import com.key.keyreception.activity.model.Addservicemodel;
import com.key.keyreception.activity.owner.CreatePostActivity;
import com.key.keyreception.activity.owner.MakePaymentActivity;
import com.key.keyreception.activity.owner.PayOptionActivity;
import com.key.keyreception.activity.owner.PaymentdetailActivity;
import com.key.keyreception.base.BaseActivity;
import com.key.keyreception.connection.RetrofitClient;
import com.key.keyreception.helper.PDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class DetailActivity extends BaseActivity implements View.OnClickListener {

    private PDialog pDialog;
    private String usertype, price, receiverId = "";
    private Session session;
    private TextView tv_addservice_price,spcomdetail_tv_post, tvpropname, tvaddress, tvbedroom, tvbathroom, tvpropsize, tvpropprice, tvserdate, tvstatus, tvservicename, tvdescription, tvvendorname;
    private ImageView serviceimage;
    private ImageView vendorimage, own_detailimg;
    private ImageView ivchat;
    private String jobid, paymenttype = "";
    private Button btn_detail_payment, btn_review_payment;
    private RelativeLayout rl_post_completejob;
    private RecyclerView recyclerView;
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

        if (getIntent().getStringExtra("notifyId") != null) {

            String reference_id = getIntent().getStringExtra("notifyId");
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
        paymenttype = session.getPayment();
        ivchat.setOnClickListener(this);
        recyclerView = findViewById(R.id.recycler_view);
        tv_addservice_price = findViewById(R.id.tv_addservice_price);

        serviceAdditional();
    }

    private void serviceAdditional() {
        GetAddServiceAdapter adapter = new GetAddServiceAdapter(DetailActivity.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(DetailActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.iv_myjob_detail_back: {
                backPress();
            }
            break;
            /*case R.id.btn_detail_payment: {
                if (!paymenttype.isEmpty()) {
                    Intent intent = new Intent(DetailActivity.this, MakePaymentActivity.class);
                    intent.putExtra("jobId", jobid);
                    intent.putExtra("receiverId", receiverId);
                    intent.putExtra("amount", price);
                    startActivity(intent);
                } else {
                    alertPayOption();
                }

            }
            break;*/
            case R.id.btn_detail_payment: {
                Intent intent = new Intent(DetailActivity.this, PaymentdetailActivity.class);
                intent.putExtra("key","1");
                startActivity(intent);
            }
            break;
            case R.id.iv_myjob_detail_vendorchat: {
                Intent intent = new Intent(this, ChattingActivity.class);
                intent.putExtra("id", recepid);
                intent.putExtra("profileImage", recepprofileImage);
                intent.putExtra("fullName", recepfullName);
                startActivity(intent);
                finish();
            }
            break;
        }

    }

    public void jobdetailApiData() {
        pDialog.pdialog(DetailActivity.this);
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
                                String propertySize = jsonObject2.getString("propertySize");
                                String serviceDate = jsonObject2.getString("serviceDate");
                                String description = jsonObject2.getString("description");
                                String status = jsonObject2.getString("status");
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
                                JSONArray jsonArray1 = jsonObject2.optJSONArray("receptionistDetail");
                                if (jsonArray1.length() > 0) {
                                    JSONObject jsonObject22 = jsonArray1.getJSONObject(0);
                                    receiverId = jsonObject22.getString("_id");
                                }

                                JSONArray jsonarrrecep = jsonObject2.getJSONArray("receptionistDetail");
                                if (jsonarrrecep.length() > 0) {
                                    JSONObject jsonobjrecep = jsonarrrecep.getJSONObject(0);
                                    recepid = String.valueOf(jsonobjrecep.getInt("_id"));
                                    recepprofileImage = jsonobjrecep.getString("profileImage");
                                    recepfullName = jsonobjrecep.getString("fullName");
                                }
                                String propertyImage = "";
                                JSONArray jsonArray3 = jsonObject2.getJSONArray("propertyImg");
                                if (jsonArray3.length() > 0) {
                                    JSONObject jsonObject5 = jsonArray3.getJSONObject(0);
                                    propertyImage = jsonObject5.getString("propertyImage");

                                }
                                setDetailData(propertyName, address, bedroom, bathroom, propertySize, price, serviceDate, status, title, description, recepfullName, image, propertyImage, recepprofileImage);
                            } else {
                                Toast.makeText(DetailActivity.this, msg, Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(DetailActivity.this, msg, Toast.LENGTH_SHORT).show();
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
            Glide.with(DetailActivity.this).load(propimg).apply(options).into(own_detailimg);
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
                btn_detail_payment.setVisibility(View.VISIBLE);
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

        Glide.with(DetailActivity.this).load(simage).into(serviceimage);
    }

    public void alertPayOption() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(DetailActivity.this);

        alertDialog.setTitle("Alert");
        alertDialog.setMessage("Please add your payment option");
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(DetailActivity.this, PayOptionActivity.class);
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


    private void initiateReviewPopupWindow() {
        try {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            LayoutInflater inflater = (LayoutInflater) DetailActivity.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert inflater != null;
            final View layout = inflater.inflate(R.layout.reviewlayout,
                    (ViewGroup) findViewById(R.id.popup_element));
            final PopupWindow popUp = new PopupWindow(layout, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    true);


            popUp.showAtLocation(layout, Gravity.CENTER, 0, 0);
            popUp.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

                }
            });


            // Button done = layout.findViewById(R.id.dialog_done);
            LinearLayout ll_sadface = layout.findViewById(R.id.ll_sadface);
            LinearLayout ll_smalleyesmile = layout.findViewById(R.id.ll_smalleyesmile);
            LinearLayout ll_mehface = layout.findViewById(R.id.ll_mehface);
            LinearLayout ll_bigeyesmile = layout.findViewById(R.id.ll_bigeyesmile);
            LinearLayout ll_happysmile = layout.findViewById(R.id.ll_happysmile);

            final ImageView iv_sadface = layout.findViewById(R.id.iv_sadface);
            final ImageView iv_smalleyesmile = layout.findViewById(R.id.iv_smalleyesmile);
            final ImageView iv_mehface = layout.findViewById(R.id.iv_mehface);
            final ImageView iv_bigeyesmile = layout.findViewById(R.id.iv_bigeyesmile);
            final ImageView iv_happysmile = layout.findViewById(R.id.iv_happysmile);

            final TextView tv_sadface = layout.findViewById(R.id.tv_sadface);
            final TextView tv_smalleyesmile = layout.findViewById(R.id.tv_smalleyesmile);
            final TextView tv_mehface = layout.findViewById(R.id.tv_mehface);
            final TextView tv_bigeyesmile = layout.findViewById(R.id.tv_bigeyesmile);
            final TextView tv_happysmile = layout.findViewById(R.id.tv_happysmile);

            final boolean[] sadcheck = {true};
            final boolean[] smalleyesmilecheck = {true};
            final boolean[] mehfacecheck = {true};
            final boolean[] bigeyesmilecheck = {true};
            final boolean[] happysmilecheck = {true};

            ll_sadface.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (sadcheck[0]) {
                        iv_sadface.setColorFilter(ContextCompat.getColor(DetailActivity.this, R.color.colordarkRed));
                        tv_sadface.setVisibility(View.VISIBLE);
                        tv_sadface.setTextColor(getResources().getColor(R.color.colordarkRed));
                        sadcheck[0] = false;
                    } else {
                        iv_sadface.setColorFilter(ContextCompat.getColor(DetailActivity.this, R.color.inactiveColor));
                        tv_sadface.setVisibility(View.GONE);
                        sadcheck[0] = true;

                    }

                }
            });

            ll_smalleyesmile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (smalleyesmilecheck[0]) {
                        iv_smalleyesmile.setColorFilter(ContextCompat.getColor(DetailActivity.this, R.color.colorexDarkGreen));
                        tv_smalleyesmile.setVisibility(View.VISIBLE);
                        tv_smalleyesmile.setTextColor(getResources().getColor(R.color.colorexDarkGreen));
                        smalleyesmilecheck[0] = false;
                    } else {
                        iv_smalleyesmile.setColorFilter(ContextCompat.getColor(DetailActivity.this, R.color.inactiveColor));
                        ;
                        tv_smalleyesmile.setVisibility(View.GONE);
                        smalleyesmilecheck[0] = true;

                    }


                }
            });

            ll_mehface.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mehfacecheck[0]) {
                        iv_mehface.setColorFilter(ContextCompat.getColor(DetailActivity.this, R.color.colorDarkYellow));
                        tv_mehface.setVisibility(View.VISIBLE);
                        tv_mehface.setTextColor(getResources().getColor(R.color.colorDarkYellow));
                        mehfacecheck[0] = false;
                    } else {
                        iv_mehface.setColorFilter(ContextCompat.getColor(DetailActivity.this, R.color.inactiveColor));
                        ;
                        tv_mehface.setVisibility(View.GONE);
                        mehfacecheck[0] = true;

                    }

                }
            });


            ll_bigeyesmile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (bigeyesmilecheck[0]) {
                        iv_bigeyesmile.setColorFilter(ContextCompat.getColor(DetailActivity.this, R.color.smileYellow));
                        tv_bigeyesmile.setVisibility(View.VISIBLE);
                        tv_bigeyesmile.setTextColor(getResources().getColor(R.color.smileYellow));
                        bigeyesmilecheck[0] = false;
                    } else {
                        iv_bigeyesmile.setColorFilter(ContextCompat.getColor(DetailActivity.this, R.color.inactiveColor));
                        ;
                        tv_bigeyesmile.setVisibility(View.GONE);
                        bigeyesmilecheck[0] = true;

                    }

                }
            });

            ll_happysmile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (happysmilecheck[0]) {
                        iv_happysmile.setColorFilter(ContextCompat.getColor(DetailActivity.this, R.color.happyGreen));
                        tv_happysmile.setVisibility(View.VISIBLE);
                        tv_happysmile.setTextColor(getResources().getColor(R.color.happyGreen));
                        happysmilecheck[0] = false;
                    } else {
                        iv_happysmile.setColorFilter(ContextCompat.getColor(DetailActivity.this, R.color.inactiveColor));
                        ;
                        tv_happysmile.setVisibility(View.GONE);
                        happysmilecheck[0] = true;

                    }

                }
            });

            Button cancel = layout.findViewById(R.id.dialog_status_cancel);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    popUp.dismiss();

                }
            });

            Button done = layout.findViewById(R.id.dialog_status_done);
            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    popUp.dismiss();

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
