package com.key.keyreception.activity.recepnist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
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
import com.key.keyreception.activity.ChattingActivity;
import com.key.keyreception.activity.model.GetAdditionalModel;
import com.key.keyreception.activity.model.RatingInfo;
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

public class AppoDetailActivity extends BaseActivity implements View.OnClickListener {


    private ProgressDialog pDialog;
    private Session session;
    private TextView tv_des, tv_subcategoryprice, Addservice, tv_addservice_price, tvpropname, tvaddress, tvbedroom, tvbathroom, tvpropsize, tvpropprice, tvserdate, tvstatus, tvservicename, tvdescription, tvvendorname;
    private ImageView serviceimage;
    private ImageView vendorimage, rece_detailimg;
    private String senderid;
    private String jobid12;
    private String status,status1;
    private Button btn_appodetail_changestatus, btn_review_payment;
    private String profileImage = "", fullName = "", Rating = "", receid = "", givenTo = "";
    private GetAddServiceAdapter adapter;
    private double totalprice = 0.0;
    private RelativeLayout rl_subcategory;
    private List<GetAdditionalModel> subCategoryList = new ArrayList<>();
    private List<RatingInfo> ratingList = new ArrayList<>();
    private View view, view1;
    private RecyclerView recyclerView, recyclerView1;
    private LinearLayout ll_rating;
    private DetailRatingAdapter ratingAdapter;
    private Validation validation;
    private RatingBar rating;


    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     /*   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }*/
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_OVERSCAN);

        }

        setContentView(R.layout.activity_appo_detail);
        session = new Session(this);
        pDialog = new ProgressDialog(this);
        validation = new Validation(this);

        init();
        String reqid;
        if (getIntent().getStringExtra("notifyId") != null) {
            reqid = getIntent().getStringExtra("notifyId");
            session.putjobid(reqid);
            btn_appodetail_changestatus.setVisibility(View.GONE);
        }
        if (getIntent().getStringExtra("notificationDetail_Jobid") != null) {
            reqid = getIntent().getStringExtra("notificationDetail_Jobid");
            session.putjobid(reqid);
            btn_appodetail_changestatus.setVisibility(View.GONE);
        }
        jobdetailApiData();

    }

    private void init() {
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
        rece_detailimg = findViewById(R.id.rece_detailimg);
        btn_appodetail_changestatus = findViewById(R.id.btn_appodetail_changestatus);
        ImageView ivback = findViewById(R.id.iv_appo_detail_back);
        ivback.setOnClickListener(this);
        btn_appodetail_changestatus.setOnClickListener(this);
        serviceimage = findViewById(R.id.iv_appo_detail_serviceimage);
        vendorimage = findViewById(R.id.iv_appo_detail_vendorimage);
        ImageView ivchat = findViewById(R.id.iv_appo_detail_vendorchat);
        ivchat.setOnClickListener(this);

        recyclerView = findViewById(R.id.recycler_view);
        tv_addservice_price = findViewById(R.id.tv_addservice_price);
        Addservice = findViewById(R.id.Addservice);
        tv_subcategoryprice = findViewById(R.id.tv_subcategoryprice);
        rl_subcategory = findViewById(R.id.rl_subcategory);
        btn_review_payment = findViewById(R.id.btn_review_payment);
        tv_des = findViewById(R.id.tv_des);
        btn_review_payment.setOnClickListener(this);
        view = findViewById(R.id.view);
        view1 = findViewById(R.id.view1);
        recyclerView1 = findViewById(R.id.recycler_view1);
        ll_rating = findViewById(R.id.ll_rating);
        rating = findViewById(R.id.rating);


        serviceAdditional();
        setReviewAdapter();
    }

    private void serviceAdditional() {

        adapter = new GetAddServiceAdapter(AppoDetailActivity.this, subCategoryList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(AppoDetailActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

    }

    private void setReviewAdapter() {

        ratingAdapter = new DetailRatingAdapter(AppoDetailActivity.this, ratingList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(AppoDetailActivity.this);
        recyclerView1.setLayoutManager(mLayoutManager);
        recyclerView1.setItemAnimator(new DefaultItemAnimator());
        recyclerView1.setAdapter(ratingAdapter);

    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.iv_appo_detail_back:
                Intent intent = new Intent(AppoDetailActivity.this, TabActivity.class);
                intent.putExtra("appoid", "12");
                startActivity(intent);
                break;
            case R.id.btn_appodetail_changestatus:
                initiatePopupWindow();
                break;

            case R.id.btn_review_payment:
                getReviewPopupWindow();
                break;
            case R.id.iv_appo_detail_vendorchat: {
                Intent intent1 = new Intent(this, ChattingActivity.class);
                intent1.putExtra("id", senderid);
                intent1.putExtra("profileImage", profileImage);
                intent1.putExtra("fullName", fullName);
                startActivity(intent1);
                finish();
            }
            break;

        }
    }

    public void jobdetailApiData() {
        pDialog.show();
        String authtoken = session.getAuthtoken();
        final String jobid = session.getjobid();
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
                                jobid12 = jsonObject2.getString("_id");
                                String price = jsonObject2.getString("price");
                                String serviceDate = jsonObject2.getString("serviceDate");
                                String description = jsonObject2.getString("description");
                                status = jsonObject2.getString("status");

                                JSONArray recepArray = jsonObject2.optJSONArray("receptionistDetail");
                                JSONObject receobject = recepArray.getJSONObject(0);
                                receid = String.valueOf(receobject.getInt("_id"));


                                JSONArray jsonArray1 = jsonObject2.optJSONArray("ownerDetail");
                                JSONObject jsonObject211 = jsonArray1.getJSONObject(0);
                                senderid = jsonObject211.getString("_id");
                                profileImage = jsonObject211.getString("profileImage");
                                fullName = jsonObject211.getString("fullName");
                                String ownerRatingCount="";
                                if (jsonObject211.has("ownerRatingCount")) {
                                    ownerRatingCount = String.valueOf(jsonObject211.getInt("ownerRatingCount"));
                                }
                                String ownerReviewCount="";
                                if (jsonObject211.has("ownerReviewCount")) {
                                    ownerReviewCount = String.valueOf(jsonObject211.getInt("ownerReviewCount"));
                                }
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
                                        String price1 = subobject.getString("price");
                                        String quantity = "0.0";
                                        if (subobject.has("quantity")) {
                                            quantity = subobject.getString("quantity");
                                        }
                                        if (subobject.has("quntity")) {
                                            quantity = subobject.getString("quntity");
                                        }

                                        calculatePrice(Double.valueOf(price1), Double.valueOf(quantity));


                                        if (subobject.has("title")) {
                                            title1 = subobject.getString("title");
                                        }
                                        GetAdditionalModel model = new GetAdditionalModel(id, quantity, price1, title1);
                                        subCategoryList.add(model);
                                    }
                                }
                                adapter.notifyDataSetChanged();
                                ratingAdapter.notifyDataSetChanged();
                                setDetailData(propertyName, address, bedroom, bathroom, propertySize, price, serviceDate, status, title, description, image, fullName, profileImage, propertyImage,ownerRatingCount,ownerReviewCount);
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
                pDialog.dismiss();
            }
        });

    }

    @SuppressLint({"SetTextI18n", "CheckResult"})
    private void setDetailData(String pname, String add, String bed, String bath, String psize, String price, String stdate, String status, String sname, String des, String simage, String ownname, String ownimage, String propimg,String ownerRatingCount,String ownerReviewCount) {
        tvpropname.setText(pname);
        tvaddress.setText(add);
        tvbedroom.setText(bed + " Bedroom");
        tvbathroom.setText(bath + " Bathroom");
        tvpropsize.setText(psize + " Sq Feet");
        tvpropprice.setText("$" + price);

        tv_addservice_price.setText("$" + price);

        if (!ownerRatingCount.isEmpty()) {
            rating.setRating(Float.parseFloat(ownerRatingCount));
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

            double totprice = Double.valueOf(price) + totalprice;
            tv_addservice_price.setText("$" + String.valueOf(totprice));

        } else {
            tv_addservice_price.setText("$" + price);

        }

        if (propimg.length() != 0) {
            RequestOptions options = new RequestOptions();
            options.placeholder(R.drawable.ic_new_big_placeholder_img);
            options.diskCacheStrategy(DiskCacheStrategy.RESOURCE);
            Glide.with(AppoDetailActivity.this).load(propimg).apply(options).into(rece_detailimg);
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
                btn_appodetail_changestatus.setVisibility(View.VISIBLE);
                break;
            case "2":
                tvstatus.setText(getResources().getString(R.string.nostart));
                tvstatus.setTextColor(getResources().getColor(R.color.appcolor));
                btn_appodetail_changestatus.setVisibility(View.VISIBLE);
                break;
            case "3":
                tvstatus.setText(getResources().getString(R.string.inpro));
                tvstatus.setTextColor(getResources().getColor(R.color.colorInprogress));
                btn_appodetail_changestatus.setVisibility(View.VISIBLE);
                break;
            case "5":
                tvstatus.setText(getResources().getString(R.string.paidcompleted));
                tvstatus.setTextColor(getResources().getColor(R.color.colorDarkGreen));
                btn_appodetail_changestatus.setVisibility(View.GONE);

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
                btn_appodetail_changestatus.setVisibility(View.GONE);
                break;
        }

        tvservicename.setText(sname);
        tvdescription.setText(des);
        tvvendorname.setText(ownname);
        if (simage.length() != 0) {
            Glide.with(AppoDetailActivity.this).load(simage).into(serviceimage);
        }
        if (ownimage.length() != 0) {
            RequestOptions options = new RequestOptions();
            options.placeholder(R.drawable.user_img);
            options.diskCacheStrategy(DiskCacheStrategy.RESOURCE);
            Glide.with(AppoDetailActivity.this).load(ownimage).apply(options).into(vendorimage);
        }
    }

    private void initiatePopupWindow() {
        try {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            LayoutInflater inflater = (LayoutInflater) AppoDetailActivity.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert inflater != null;
            final View layout = inflater.inflate(R.layout.statuspop,
                    (ViewGroup) findViewById(R.id.popup_element));
            final PopupWindow popUp = new PopupWindow(layout, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    true);

            final boolean[] startservice = {false};
            LinearLayout ll_start_service = layout.findViewById(R.id.ll_start_service);
            LinearLayout ll_inprogress_service = layout.findViewById(R.id.ll_inprogress_service);
            LinearLayout ll_end_service = layout.findViewById(R.id.ll_end_service);
            final ImageView iv_start_service = layout.findViewById(R.id.iv_start_service);
            final ImageView iv_inprogress_service = layout.findViewById(R.id.iv_inprogress_service);
            final ImageView iv_end_service = layout.findViewById(R.id.iv_end_service);
            final TextView tv_start_service = layout.findViewById(R.id.tv_start_service);
            final TextView tv_inprogress_service = layout.findViewById(R.id.tv_inprogress_service);
            final TextView tv_end_service = layout.findViewById(R.id.tv_end_service);

            switch (status) {

                case "2":
                    startservice[0] = true;
                    iv_start_service.setBackground(getResources().getDrawable(R.drawable.circlechat));
                    iv_start_service.setColorFilter(ContextCompat.getColor(AppoDetailActivity.this, R.color.colorwhite));
                    tv_start_service.setTextColor(getResources().getColor(R.color.colorPrimary));
                    iv_inprogress_service.setBackground(getResources().getDrawable(R.drawable.circletransparent));
                    iv_inprogress_service.setColorFilter(ContextCompat.getColor(AppoDetailActivity.this, R.color.colorgray));
                    tv_inprogress_service.setTextColor(getResources().getColor(R.color.colorgray));
                    iv_end_service.setBackground(getResources().getDrawable(R.drawable.circletransparent));
                    iv_end_service.setColorFilter(ContextCompat.getColor(AppoDetailActivity.this, R.color.colorgray));
                    tv_end_service.setTextColor(getResources().getColor(R.color.colorgray));
                    btn_appodetail_changestatus.setVisibility(View.VISIBLE);
                    break;
                case "3":
                    startservice[0] = true;
                    iv_start_service.setBackground(getResources().getDrawable(R.drawable.circletransparent));
                    iv_start_service.setColorFilter(ContextCompat.getColor(AppoDetailActivity.this, R.color.colorgray));
                    tv_start_service.setTextColor(getResources().getColor(R.color.colorgray));
                    iv_inprogress_service.setBackground(getResources().getDrawable(R.drawable.circlechat));
                    iv_inprogress_service.setColorFilter(ContextCompat.getColor(AppoDetailActivity.this, R.color.colorwhite));
                    tv_inprogress_service.setTextColor(getResources().getColor(R.color.colorPrimary));
                    iv_end_service.setBackground(getResources().getDrawable(R.drawable.circletransparent));
                    iv_end_service.setColorFilter(ContextCompat.getColor(AppoDetailActivity.this, R.color.colorgray));
                    tv_end_service.setTextColor(getResources().getColor(R.color.colorgray));
                    btn_appodetail_changestatus.setVisibility(View.VISIBLE);
                    break;
                case "4":
                    iv_start_service.setBackground(getResources().getDrawable(R.drawable.circletransparent));
                    iv_start_service.setColorFilter(ContextCompat.getColor(AppoDetailActivity.this, R.color.colorgray));
                    tv_start_service.setTextColor(getResources().getColor(R.color.colorgray));
                    iv_inprogress_service.setBackground(getResources().getDrawable(R.drawable.circletransparent));
                    iv_inprogress_service.setColorFilter(ContextCompat.getColor(AppoDetailActivity.this, R.color.colorgray));
                    tv_inprogress_service.setTextColor(getResources().getColor(R.color.colorgray));
                    iv_end_service.setBackground(getResources().getDrawable(R.drawable.circlechat));
                    iv_end_service.setColorFilter(ContextCompat.getColor(AppoDetailActivity.this, R.color.colorwhite));
                    tv_end_service.setTextColor(getResources().getColor(R.color.colorPrimary));
                    btn_appodetail_changestatus.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }


            if (!status.equals("3") && !status.equals("4")) {
                ll_start_service.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        status1 = "2";
                        iv_start_service.setBackground(getResources().getDrawable(R.drawable.circlechat));
                        iv_start_service.setColorFilter(ContextCompat.getColor(AppoDetailActivity.this, R.color.colorwhite));
                        tv_start_service.setTextColor(getResources().getColor(R.color.colorPrimary));

                        iv_inprogress_service.setBackground(getResources().getDrawable(R.drawable.circletransparent));
                        iv_inprogress_service.setColorFilter(ContextCompat.getColor(AppoDetailActivity.this, R.color.colorgray));
                        tv_inprogress_service.setTextColor(getResources().getColor(R.color.colorgray));

                        iv_end_service.setBackground(getResources().getDrawable(R.drawable.circletransparent));
                        iv_end_service.setColorFilter(ContextCompat.getColor(AppoDetailActivity.this, R.color.colorgray));
                        tv_end_service.setTextColor(getResources().getColor(R.color.colorgray));
                    }
                });
            }

            if (!status.equals("4") && !status.equals("3") && status.equals("2")) {
                ll_inprogress_service.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        status1 = "3";
                        iv_start_service.setBackground(getResources().getDrawable(R.drawable.circletransparent));
                        iv_start_service.setColorFilter(ContextCompat.getColor(AppoDetailActivity.this, R.color.colorgray));
                        tv_start_service.setTextColor(getResources().getColor(R.color.colorgray));

                        iv_inprogress_service.setBackground(getResources().getDrawable(R.drawable.circlechat));
                        iv_inprogress_service.setColorFilter(ContextCompat.getColor(AppoDetailActivity.this, R.color.colorwhite));
                        tv_inprogress_service.setTextColor(getResources().getColor(R.color.colorPrimary));

                        iv_end_service.setBackground(getResources().getDrawable(R.drawable.circletransparent));
                        iv_end_service.setColorFilter(ContextCompat.getColor(AppoDetailActivity.this, R.color.colorgray));
                        tv_end_service.setTextColor(getResources().getColor(R.color.colorgray));

                    }

                });

            }
            if (status.equals("2") && !status.equals("3")) {
                ll_end_service.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        status1 = "4";
                        iv_start_service.setBackground(getResources().getDrawable(R.drawable.circletransparent));
                        iv_start_service.setColorFilter(ContextCompat.getColor(AppoDetailActivity.this, R.color.colorgray));
                        tv_start_service.setTextColor(getResources().getColor(R.color.colorgray));

                        iv_inprogress_service.setBackground(getResources().getDrawable(R.drawable.circletransparent));
                        iv_inprogress_service.setColorFilter(ContextCompat.getColor(AppoDetailActivity.this, R.color.colorgray));
                        tv_inprogress_service.setTextColor(getResources().getColor(R.color.colorgray));

                        iv_end_service.setBackground(getResources().getDrawable(R.drawable.circlechat));
                        iv_end_service.setColorFilter(ContextCompat.getColor(AppoDetailActivity.this, R.color.colorwhite));
                        tv_end_service.setTextColor(getResources().getColor(R.color.colorPrimary));


                    }
                });

            }
            if (!status.equals("2") && status.equals("3")) {
                ll_end_service.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        status1 = "4";
                        iv_start_service.setBackground(getResources().getDrawable(R.drawable.circletransparent));
                        iv_start_service.setColorFilter(ContextCompat.getColor(AppoDetailActivity.this, R.color.colorgray));
                        tv_start_service.setTextColor(getResources().getColor(R.color.colorgray));

                        iv_inprogress_service.setBackground(getResources().getDrawable(R.drawable.circletransparent));
                        iv_inprogress_service.setColorFilter(ContextCompat.getColor(AppoDetailActivity.this, R.color.colorgray));
                        tv_inprogress_service.setTextColor(getResources().getColor(R.color.colorgray));

                        iv_end_service.setBackground(getResources().getDrawable(R.drawable.circlechat));
                        iv_end_service.setColorFilter(ContextCompat.getColor(AppoDetailActivity.this, R.color.colorwhite));
                        tv_end_service.setTextColor(getResources().getColor(R.color.colorPrimary));


                    }
                });

            }
            popUp.showAtLocation(layout, Gravity.CENTER, 0, 0);
            popUp.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

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
                    status = status1;
                    if (status.equals("4")) {
                        session.putJobStatus("0");
                    }
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    popUp.dismiss();
                    statusCheckApiData(status);


                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void statusCheckApiData(String status) {
        String jobid = session.getjobid();
        pDialog.show();
        String authtoken = session.getAuthtoken();
        Call<ResponseBody> call = RetrofitClient.getInstance()
                .getApi().acceptReject(authtoken, "application/x-www-form-urlencoded", jobid12, jobid12, senderid, status, "1");
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

                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

                                jobdetailApiData();

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
                pDialog.dismiss();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(AppoDetailActivity.this, TabActivity.class);
        intent.putExtra("appoid", "12");
        startActivity(intent);
    }


    public void calculatePrice(double price, double quntity) {

        totalprice += price * quntity;
        Log.e("totalprice", String.valueOf(totalprice));
    }

    private void getReviewPopupWindow() {
        try {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            LayoutInflater inflater = (LayoutInflater) AppoDetailActivity.this
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

                    iv_sadface.setColorFilter(ContextCompat.getColor(AppoDetailActivity.this, R.color.colordarkRed));
                    iv_smalleyesmile.setColorFilter(ContextCompat.getColor(AppoDetailActivity.this, R.color.inactiveColor));
                    iv_mehface.setColorFilter(ContextCompat.getColor(AppoDetailActivity.this, R.color.inactiveColor));
                    iv_bigeyesmile.setColorFilter(ContextCompat.getColor(AppoDetailActivity.this, R.color.inactiveColor));
                    iv_happysmile.setColorFilter(ContextCompat.getColor(AppoDetailActivity.this, R.color.inactiveColor));


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
                    iv_smalleyesmile.setColorFilter(ContextCompat.getColor(AppoDetailActivity.this, R.color.colorexDarkGreen));
                    iv_sadface.setColorFilter(ContextCompat.getColor(AppoDetailActivity.this, R.color.inactiveColor));
                    iv_mehface.setColorFilter(ContextCompat.getColor(AppoDetailActivity.this, R.color.inactiveColor));
                    iv_bigeyesmile.setColorFilter(ContextCompat.getColor(AppoDetailActivity.this, R.color.inactiveColor));
                    iv_happysmile.setColorFilter(ContextCompat.getColor(AppoDetailActivity.this, R.color.inactiveColor));

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
                    iv_mehface.setColorFilter(ContextCompat.getColor(AppoDetailActivity.this, R.color.colorDarkYellow));
                    iv_sadface.setColorFilter(ContextCompat.getColor(AppoDetailActivity.this, R.color.inactiveColor));
                    iv_smalleyesmile.setColorFilter(ContextCompat.getColor(AppoDetailActivity.this, R.color.inactiveColor));
                    iv_bigeyesmile.setColorFilter(ContextCompat.getColor(AppoDetailActivity.this, R.color.inactiveColor));
                    iv_happysmile.setColorFilter(ContextCompat.getColor(AppoDetailActivity.this, R.color.inactiveColor));

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
                    iv_bigeyesmile.setColorFilter(ContextCompat.getColor(AppoDetailActivity.this, R.color.smileYellow));
                    iv_sadface.setColorFilter(ContextCompat.getColor(AppoDetailActivity.this, R.color.inactiveColor));
                    iv_smalleyesmile.setColorFilter(ContextCompat.getColor(AppoDetailActivity.this, R.color.inactiveColor));
                    iv_mehface.setColorFilter(ContextCompat.getColor(AppoDetailActivity.this, R.color.inactiveColor));
                    iv_happysmile.setColorFilter(ContextCompat.getColor(AppoDetailActivity.this, R.color.inactiveColor));

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
                    iv_happysmile.setColorFilter(ContextCompat.getColor(AppoDetailActivity.this, R.color.happyGreen));
                    iv_sadface.setColorFilter(ContextCompat.getColor(AppoDetailActivity.this, R.color.inactiveColor));
                    iv_smalleyesmile.setColorFilter(ContextCompat.getColor(AppoDetailActivity.this, R.color.inactiveColor));
                    iv_mehface.setColorFilter(ContextCompat.getColor(AppoDetailActivity.this, R.color.inactiveColor));
                    iv_bigeyesmile.setColorFilter(ContextCompat.getColor(AppoDetailActivity.this, R.color.inactiveColor));


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

    public void giveReviewApiData(String rating, String exprience) {
        pDialog.show();
        String authtoken = session.getAuthtoken();
        String jobid = session.getjobid();
        Call<ResponseBody> call = RetrofitClient.getInstance()
                .getApi().reviewRating(authtoken, jobid, senderid, receid, rating, exprience, "receptionist");

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
                                Toast.makeText(AppoDetailActivity.this, msg, Toast.LENGTH_SHORT).show();

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
                pDialog.dismiss();
            }
        });

    }

}
