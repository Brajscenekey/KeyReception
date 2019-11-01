package com.key.keyreception.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.key.keyreception.R;
import com.key.keyreception.Session;
import com.key.keyreception.activity.ActivityAdapter.DetailRatingAdapter;
import com.key.keyreception.activity.ActivityAdapter.GetAddServiceAdapter;
import com.key.keyreception.activity.model.GetAdditionalModel;
import com.key.keyreception.activity.model.RatingInfo;
import com.key.keyreception.activity.owner.PayOptionActivity;
import com.key.keyreception.activity.owner.PaymentdetailActivity;
import com.key.keyreception.base.BaseActivity;
import com.key.keyreception.connection.RetrofitClient;
import com.key.keyreception.helper.ProgressDialog;
import com.key.keyreception.helper.Validation;

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

    private ProgressDialog pDialog;
    private String usertype, price, receiverId = "", givenTo = "";
    private Session session;
    private TextView tv_des, tv_subcategoryprice, Addservice, tv_addservice_price, spcomdetail_tv_post, tvpropname, tvaddress, tvbedroom, tvbathroom, tvpropsize, tvpropprice, tvserdate, tvstatus, tvservicename, tvdescription, tvvendorname;
    private ImageView serviceimage;
    private ImageView vendorimage, own_detailimg;
    private ImageView ivchat;
    private String jobid, paymenttype = "";
    private Button btn_detail_payment, btn_review_payment;
    private RelativeLayout rl_post_completejob;
    private RecyclerView recyclerView, recyclerView1;
    private List<GetAdditionalModel> subCategoryList = new ArrayList<>();
    private List<RatingInfo> ratingList = new ArrayList<>();
    private String recepid = "", recepprofileImage = "", recepfullName = "", ownerid = "", Rating = "";
    private GetAddServiceAdapter adapter;
    private double totalprice = 0.0;
    private RelativeLayout rl_subcategory;
    private View view, view1;
    private double totprice = 0.0;
    private LinearLayout ll_rating;
    private DetailRatingAdapter ratingAdapter;
    private Validation validation;
    private RatingBar rating;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        setContentView(R.layout.activity_detail);
        session = new Session(this);
        validation = new Validation(this);
        pDialog = new ProgressDialog(this);

        init();

        if (getIntent().getStringExtra("notifyId") != null) {

            String reference_id = getIntent().getStringExtra("notifyId");
            session.putjobid(reference_id);
        }

        jobdetailApiData();
        setReviewAdapter();

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
        recyclerView1 = findViewById(R.id.recycler_view1);
        tv_addservice_price = findViewById(R.id.tv_addservice_price);
        Addservice = findViewById(R.id.Addservice);
        tv_subcategoryprice = findViewById(R.id.tv_subcategoryprice);
        rl_subcategory = findViewById(R.id.rl_subcategory);
        tv_des = findViewById(R.id.tv_des);
        view = findViewById(R.id.view);
        view1 = findViewById(R.id.view1);
        ll_rating = findViewById(R.id.ll_rating);
        rating = findViewById(R.id.rating);

        serviceAdditional();

    }

    private void serviceAdditional() {

        adapter = new GetAddServiceAdapter(DetailActivity.this, subCategoryList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(DetailActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

    }

    private void setReviewAdapter() {

        ratingAdapter = new DetailRatingAdapter(DetailActivity.this, ratingList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(DetailActivity.this);
        recyclerView1.setLayoutManager(mLayoutManager);
        recyclerView1.setItemAnimator(new DefaultItemAnimator());
        recyclerView1.setAdapter(ratingAdapter);

    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.iv_myjob_detail_back: {
                backPress();
            }
            break;

            case R.id.btn_review_payment: {
                getReviewPopupWindow();
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
                session.putPaymentData(jobid, receiverId, price);
                Intent intent = new Intent(DetailActivity.this, PaymentdetailActivity.class);
                intent.putExtra("key", "1");
                intent.putExtra("jobId", jobid);
                intent.putExtra("receiverId", receiverId);
                intent.putExtra("amount", String.valueOf(totalprice));
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
        pDialog.show();
        String authtoken = session.getAuthtoken();
        jobid = session.getjobid();
        Call<ResponseBody> call = RetrofitClient.getInstance()
                .getApi().jobDetail(authtoken, jobid);
        call.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("NewApi")
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull retrofit2.Response<ResponseBody> response) {
                try {
                    pDialog.dismiss();
                    switch (response.code()) {
                        case 200: {
                            ratingList.clear();
                            String stresult = Objects.requireNonNull(response.body()).string();
                            Log.d("response", stresult);
                            JSONObject jsonObject = new JSONObject(stresult);
                            String statusCode = jsonObject.optString("status");
                            String msg = jsonObject.optString("message");
                            if (statusCode.equals("success")) {
                                JSONObject jsonObject2 = jsonObject.getJSONObject("data");
                                price = jsonObject2.getString("price");
                                String address = "";
                                String propertyName = "";
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
                                if (jsonObjectdata.has("propertyAddress")) {
                                    address = jsonObjectdata.getString("propertyAddress");
                                }
                                if (jsonObjectdata.has("propertyName")) {
                                    propertyName = jsonObjectdata.getString("propertyName");
                                }
                                JSONArray jsonArray1 = jsonObject2.optJSONArray("receptionistDetail");
                                if (jsonArray1.length() > 0) {
                                    JSONObject jsonObject22 = jsonArray1.getJSONObject(0);
                                    receiverId = jsonObject22.getString("_id");
                                }

                                JSONArray jsonarrrecep = jsonObject2.getJSONArray("receptionistDetail");
                                String recRatingCount="";
                                String recReviewCount="";
                                if (jsonarrrecep.length() > 0) {
                                    JSONObject jsonobjrecep = jsonarrrecep.getJSONObject(0);
                                    recepid = String.valueOf(jsonobjrecep.getInt("_id"));
                                    recepprofileImage = jsonobjrecep.getString("profileImage");
                                    String stripeAccountId = jsonobjrecep.getString("stripeAccountId");
                                    session.putaccountId(stripeAccountId);
                                    recepfullName = jsonobjrecep.getString("fullName");
                                    if (jsonobjrecep.has("recRatingCount")) {
                                        recRatingCount = String.valueOf(jsonobjrecep.getInt("recRatingCount"));
                                    }
                                    if (jsonobjrecep.has("recRatingCount")) {
                                        recReviewCount = String.valueOf(jsonobjrecep.getInt("recReviewCount"));
                                    }


                                }

                                JSONArray ownerDetailarray = jsonObject2.getJSONArray("ownerDetail");
                                if (ownerDetailarray.length() > 0) {
                                    JSONObject jsonobjowner = ownerDetailarray.getJSONObject(0);
                                    ownerid = String.valueOf(jsonobjowner.getInt("_id"));
                                }
                                String propertyImage = "";
                                JSONArray jsonArray3 = jsonObject2.getJSONArray("propertyImg");
                                if (jsonArray3.length() > 0) {
                                    JSONObject jsonObject5 = jsonArray3.getJSONObject(0);
                                    propertyImage = jsonObject5.getString("propertyImage");

                                }


                                JSONArray ratingarray = jsonObject2.getJSONArray("ratingInfo");
                                for (int i = 0; i < ratingarray.length(); i++) {
                                    JSONObject ratingobject = ratingarray.getJSONObject(i);
                                    int _id = ratingobject.getInt("_id");
                                    int rating = ratingobject.getInt("rating");
                                    String review = ratingobject.getString("review");
                                    givenTo = ratingobject.getString("givenTo");
                                    String crd = ratingobject.getString("crd");
                                    int jobId = ratingobject.getInt("jobId");
                                    int ownerId = ratingobject.getInt("ownerId");
                                    int receptionistId = ratingobject.getInt("receptionistId");
                                    int __v = ratingobject.getInt("__v");

                                    if (givenTo.equals(session.getusertype())) {
                                        btn_review_payment.setVisibility(View.GONE);
                                    } else {
                                        btn_review_payment.setVisibility(View.VISIBLE);
                                    }


                                    RatingInfo model = new RatingInfo(_id, rating, review, givenTo, crd, jobId, ownerId, receptionistId, __v);
                                    ratingList.add(model);
                                }


                                if (!jsonObject2.getString("subCategoryData").isEmpty()) {
                                    JSONArray subCategoryData = jsonObject2.getJSONArray("subCategoryData");
                                    for (int i = 0; i < subCategoryData.length(); i++) {
                                        String title1 = "";
                                        JSONObject subobject = subCategoryData.getJSONObject(i);
                                        String id = subobject.getString("id");
                                        String price = subobject.getString("price");
                                        String quantity = "0.0";
                                        if (subobject.has("quantity")) {
                                            quantity = subobject.getString("quantity");
                                        }
                                        if (subobject.has("quntity")) {
                                            quantity = subobject.getString("quntity");
                                        }
                                        calculatePrice(Double.valueOf(price), Double.valueOf(quantity));


                                        if (subobject.has("title")) {
                                            title1 = subobject.getString("title");
                                        }
                                        GetAdditionalModel model = new GetAdditionalModel(id, quantity, price, title1);
                                        subCategoryList.add(model);
                                    }
                                }

                                adapter.notifyDataSetChanged();
                                ratingAdapter.notifyDataSetChanged();

                                setDetailData(propertyName, address, bedroom, bathroom, propertySize, price, serviceDate, status, title, description, recepfullName, image, propertyImage, recepprofileImage,recRatingCount,recReviewCount);
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
                pDialog.dismiss();
            }
        });

    }

    @SuppressLint({"SetTextI18n", "CheckResult"})
    private void setDetailData(String pname, String add, String bed, String bath, String psize, String price, String stdate, String status, String sname, String des, String vname, String simage, String propimg, String rimage,String recRatingCount,String recReviewCount) {
        tvpropname.setText(pname);
        tvaddress.setText(add);
        tvbedroom.setText(bed + " Bedroom");
        tvbathroom.setText(bath + " Bathroom");
        tvpropsize.setText(psize + " Sq Feet");
        tvpropprice.setText("$" + price);
        tv_addservice_price.setText("$" + price);

        if (!recRatingCount.isEmpty()) {
            rating.setRating(Float.parseFloat(recRatingCount));
        }

        if (des.isEmpty()) {
            tv_des.setVisibility(View.GONE);
            tvdescription.setVisibility(View.GONE);
            view1.setVisibility(View.GONE);
        }


        if (subCategoryList.size() != 0) {
            recyclerView.setVisibility(View.VISIBLE);
            rl_subcategory.setVisibility(View.VISIBLE);
            view.setVisibility(View.VISIBLE);
            Addservice.setVisibility(View.VISIBLE);
            tv_subcategoryprice.setText("$" + String.valueOf(totalprice));

            totprice = Double.valueOf(price) + totalprice;

            tv_addservice_price.setText("$" + String.valueOf(totprice));

        } else {
            tv_addservice_price.setText("$" + price);

        }
        tvvendorname.setText(vname);
        if (rimage.length() != 0) {
            RequestOptions options = new RequestOptions();
            options.placeholder(R.drawable.user_img);
            options.diskCacheStrategy(DiskCacheStrategy.RESOURCE);
            Glide.with(this).load(rimage).apply(options).into(vendorimage);
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
            options.placeholder(R.drawable.ic_new_big_placeholder_img);
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
                tvstatus.setText(getResources().getString(R.string.paidcompleted));
                tvstatus.setTextColor(getResources().getColor(R.color.colorDarkGreen));
                btn_detail_payment.setVisibility(View.GONE);
                if (ratingList.size() != 0) {
                    ll_rating.setVisibility(View.VISIBLE);
                } else {
                    btn_review_payment.setVisibility(View.VISIBLE);
                    }


                if (ratingList.size() == 2) {
                    btn_review_payment.setVisibility(View.GONE);
                }


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


    private void getReviewPopupWindow() {
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
            LinearLayout ll_sadface = layout.findViewById(R.id.ll_face1);
            LinearLayout ll_smalleyesmile = layout.findViewById(R.id.ll_face2);
            LinearLayout ll_mehface = layout.findViewById(R.id.ll_face3);
            LinearLayout ll_bigeyesmile = layout.findViewById(R.id.ll_face4);
            LinearLayout ll_happysmile = layout.findViewById(R.id.ll_face5);

            final ImageView iv_sadface = layout.findViewById(R.id.iv_face1);
            final ImageView iv_smalleyesmile = layout.findViewById(R.id.iv_face2);
            final ImageView iv_mehface = layout.findViewById(R.id.iv_face3);
            final ImageView iv_bigeyesmile = layout.findViewById(R.id.iv_face4);
            final ImageView iv_happysmile = layout.findViewById(R.id.iv_face5);

            final TextView tv_sadface = layout.findViewById(R.id.tv_face1);
            final TextView tv_smalleyesmile = layout.findViewById(R.id.tv_face2);
            final TextView tv_mehface = layout.findViewById(R.id.tv_face3);
            final TextView tv_bigeyesmile = layout.findViewById(R.id.tv_face4);
            final TextView tv_happysmile = layout.findViewById(R.id.tv_face5);


            final EditText et_exprience = layout.findViewById(R.id.et_exprience);

            ll_sadface.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    iv_sadface.setColorFilter(ContextCompat.getColor(DetailActivity.this, R.color.colordarkRed));
                    iv_smalleyesmile.setColorFilter(ContextCompat.getColor(DetailActivity.this, R.color.inactiveColor));
                    iv_mehface.setColorFilter(ContextCompat.getColor(DetailActivity.this, R.color.inactiveColor));
                    iv_bigeyesmile.setColorFilter(ContextCompat.getColor(DetailActivity.this, R.color.inactiveColor));
                    iv_happysmile.setColorFilter(ContextCompat.getColor(DetailActivity.this, R.color.inactiveColor));


                    tv_sadface.setVisibility(View.VISIBLE);
                    tv_smalleyesmile.setVisibility(View.GONE);
                    tv_mehface.setVisibility(View.GONE);
                    tv_bigeyesmile.setVisibility(View.GONE);
                    tv_happysmile.setVisibility(View.GONE);

                    tv_sadface.setTextColor(getResources().getColor(R.color.colordarkRed));
                    tv_smalleyesmile.setTextColor(getResources().getColor(R.color.inactiveColor));
                    tv_mehface.setTextColor(getResources().getColor(R.color.inactiveColor));
                    tv_bigeyesmile.setTextColor(getResources().getColor(R.color.inactiveColor));
                    tv_happysmile.setTextColor(getResources().getColor(R.color.inactiveColor));
                    Rating = "1";

                }
            });

            ll_smalleyesmile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    iv_smalleyesmile.setColorFilter(ContextCompat.getColor(DetailActivity.this, R.color.colorexDarkGreen));
                    iv_sadface.setColorFilter(ContextCompat.getColor(DetailActivity.this, R.color.inactiveColor));
                    iv_mehface.setColorFilter(ContextCompat.getColor(DetailActivity.this, R.color.inactiveColor));
                    iv_bigeyesmile.setColorFilter(ContextCompat.getColor(DetailActivity.this, R.color.inactiveColor));
                    iv_happysmile.setColorFilter(ContextCompat.getColor(DetailActivity.this, R.color.inactiveColor));

                    tv_sadface.setVisibility(View.GONE);
                    tv_smalleyesmile.setVisibility(View.VISIBLE);
                    tv_mehface.setVisibility(View.GONE);
                    tv_bigeyesmile.setVisibility(View.GONE);
                    tv_happysmile.setVisibility(View.GONE);

                    tv_sadface.setTextColor(getResources().getColor(R.color.inactiveColor));
                    tv_smalleyesmile.setTextColor(getResources().getColor(R.color.colorexDarkGreen));
                    tv_mehface.setTextColor(getResources().getColor(R.color.inactiveColor));
                    tv_bigeyesmile.setTextColor(getResources().getColor(R.color.inactiveColor));
                    tv_happysmile.setTextColor(getResources().getColor(R.color.inactiveColor));
                    Rating = "2";


                }
            });

            ll_mehface.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    iv_mehface.setColorFilter(ContextCompat.getColor(DetailActivity.this, R.color.colorDarkYellow));
                    iv_sadface.setColorFilter(ContextCompat.getColor(DetailActivity.this, R.color.inactiveColor));
                    iv_smalleyesmile.setColorFilter(ContextCompat.getColor(DetailActivity.this, R.color.inactiveColor));
                    iv_bigeyesmile.setColorFilter(ContextCompat.getColor(DetailActivity.this, R.color.inactiveColor));
                    iv_happysmile.setColorFilter(ContextCompat.getColor(DetailActivity.this, R.color.inactiveColor));

                    tv_sadface.setVisibility(View.GONE);
                    tv_smalleyesmile.setVisibility(View.GONE);
                    tv_mehface.setVisibility(View.VISIBLE);
                    tv_bigeyesmile.setVisibility(View.GONE);
                    tv_happysmile.setVisibility(View.GONE);

                    tv_sadface.setTextColor(getResources().getColor(R.color.inactiveColor));
                    tv_smalleyesmile.setTextColor(getResources().getColor(R.color.inactiveColor));
                    tv_mehface.setTextColor(getResources().getColor(R.color.colorDarkYellow));
                    tv_bigeyesmile.setTextColor(getResources().getColor(R.color.inactiveColor));
                    tv_happysmile.setTextColor(getResources().getColor(R.color.inactiveColor));
                    Rating = "3";

                }
            });


            ll_bigeyesmile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    iv_bigeyesmile.setColorFilter(ContextCompat.getColor(DetailActivity.this, R.color.smileYellow));
                    iv_sadface.setColorFilter(ContextCompat.getColor(DetailActivity.this, R.color.inactiveColor));
                    iv_smalleyesmile.setColorFilter(ContextCompat.getColor(DetailActivity.this, R.color.inactiveColor));
                    iv_mehface.setColorFilter(ContextCompat.getColor(DetailActivity.this, R.color.inactiveColor));
                    iv_happysmile.setColorFilter(ContextCompat.getColor(DetailActivity.this, R.color.inactiveColor));

                    tv_sadface.setVisibility(View.GONE);
                    tv_smalleyesmile.setVisibility(View.GONE);
                    tv_mehface.setVisibility(View.GONE);
                    tv_bigeyesmile.setVisibility(View.VISIBLE);
                    tv_happysmile.setVisibility(View.GONE);

                    tv_sadface.setTextColor(getResources().getColor(R.color.inactiveColor));
                    tv_smalleyesmile.setTextColor(getResources().getColor(R.color.inactiveColor));
                    tv_mehface.setTextColor(getResources().getColor(R.color.inactiveColor));
                    tv_bigeyesmile.setTextColor(getResources().getColor(R.color.smileYellow));
                    tv_happysmile.setTextColor(getResources().getColor(R.color.inactiveColor));
                    Rating = "4";


                }
            });

            ll_happysmile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    iv_happysmile.setColorFilter(ContextCompat.getColor(DetailActivity.this, R.color.happyGreen));
                    iv_sadface.setColorFilter(ContextCompat.getColor(DetailActivity.this, R.color.inactiveColor));
                    iv_smalleyesmile.setColorFilter(ContextCompat.getColor(DetailActivity.this, R.color.inactiveColor));
                    iv_mehface.setColorFilter(ContextCompat.getColor(DetailActivity.this, R.color.inactiveColor));
                    iv_bigeyesmile.setColorFilter(ContextCompat.getColor(DetailActivity.this, R.color.inactiveColor));


                    tv_sadface.setVisibility(View.GONE);
                    tv_smalleyesmile.setVisibility(View.GONE);
                    tv_mehface.setVisibility(View.GONE);
                    tv_bigeyesmile.setVisibility(View.GONE);
                    tv_happysmile.setVisibility(View.VISIBLE);

                    tv_sadface.setTextColor(getResources().getColor(R.color.inactiveColor));
                    tv_smalleyesmile.setTextColor(getResources().getColor(R.color.inactiveColor));
                    tv_mehface.setTextColor(getResources().getColor(R.color.inactiveColor));
                    tv_bigeyesmile.setTextColor(getResources().getColor(R.color.inactiveColor));
                    tv_happysmile.setTextColor(getResources().getColor(R.color.happyGreen));
                    Rating = "5";

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
                    if (validation.isRatingValid(Rating) && validation.isReviewValid(et_exprience)) {
                        giveReviewApiData(Rating, et_exprience.getText().toString());
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                        popUp.dismiss();
                    }

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void calculatePrice(double price, double quntity) {

        totalprice += price * quntity;
        Log.e("totalprice", String.valueOf(totalprice));
    }


    public void giveReviewApiData(String rating, String exprience) {
        pDialog.show();
        String authtoken = session.getAuthtoken();
        jobid = session.getjobid();
        Call<ResponseBody> call = RetrofitClient.getInstance()
                .getApi().reviewRating(authtoken, jobid, ownerid, recepid, rating, exprience, "owner");

        call.enqueue(new Callback<ResponseBody>() {
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
                            String msg = jsonObject.optString("message");
                            if (statusCode.equals("success")) {
                                jobdetailApiData();
                                Toast.makeText(DetailActivity.this, msg, Toast.LENGTH_SHORT).show();

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
                pDialog.dismiss();
            }
        });

    }

}
