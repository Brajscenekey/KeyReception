package com.key.keyreception.activity.recepnist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
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

public class NotiDetailActivity extends BaseActivity implements View.OnClickListener {

    Button btn_appodetail_changestatus;
    private PDialog pDialog;
    private Session session;
    private TextView tv_des,tv_subcategoryprice,Addservice,tv_addservice_price,tvpropname, tvaddress, tvbedroom, tvbathroom, tvpropsize, tvpropprice, tvserdate, tvstatus, tvservicename, tvdescription, tvvendorname;
    private ImageView serviceimage, rece_detailimg;
    private ImageView vendorimage;
    private ImageView ivchat;
    private String reqid, senderid, status;
    private String profileImage = "", fullName = "";
    private GetAddServiceAdapter adapter;
    private double totalprice = 0.0;
    private RelativeLayout rl_subcategory;
    private List<GetAdditionalModel> subCategoryList = new ArrayList<>();
    private List<RatingInfo> ratingList  = new ArrayList<>();
    private View view,view1;
    private RecyclerView recyclerView,recyclerView1;
    private LinearLayout ll_rating;
    private DetailRatingAdapter ratingAdapter;
    private RatingBar rating;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        setContentView(R.layout.activity_appo_detail);
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
        btn_appodetail_changestatus = findViewById(R.id.btn_appodetail_changestatus);
        rece_detailimg = findViewById(R.id.rece_detailimg);
        ImageView ivback = findViewById(R.id.iv_appo_detail_back);
        ivback.setOnClickListener(this);
        btn_appodetail_changestatus.setOnClickListener(this);
        serviceimage = findViewById(R.id.iv_appo_detail_serviceimage);
        vendorimage = findViewById(R.id.iv_appo_detail_vendorimage);
        ivchat = findViewById(R.id.iv_appo_detail_vendorchat);
        ivchat.setOnClickListener(this);
        recyclerView = findViewById(R.id.recycler_view);
        tv_addservice_price = findViewById(R.id.tv_addservice_price);
        Addservice = findViewById(R.id.Addservice);
        tv_subcategoryprice = findViewById(R.id.tv_subcategoryprice);
        rl_subcategory = findViewById(R.id.rl_subcategory);
        tv_des = findViewById(R.id.tv_des);
        view = findViewById(R.id.view);
        view1 = findViewById(R.id.view1);
        recyclerView1 = findViewById(R.id.recycler_view1);
        ll_rating = findViewById(R.id.ll_rating);
        rating = findViewById(R.id.rating);


        serviceAdditional();
        setReviewAdapter();
    }

    private void serviceAdditional() {

        adapter = new GetAddServiceAdapter(NotiDetailActivity.this,subCategoryList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(NotiDetailActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

    }

    private void setReviewAdapter() {

        ratingAdapter = new DetailRatingAdapter(NotiDetailActivity.this, ratingList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(NotiDetailActivity.this);
        recyclerView1.setLayoutManager(mLayoutManager);
        recyclerView1.setItemAnimator(new DefaultItemAnimator());
        recyclerView1.setAdapter(ratingAdapter);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.iv_appo_detail_back:
                backPress();
                break;
            case R.id.btn_appodetail_changestatus:
                initiatePopupWindow();
                break;
            case R.id.iv_appo_detail_vendorchat: {
                /*Intent intent1 = new Intent(this,ChattingActivity.class);
                intent1.putExtra("id",senderid);
                intent1.putExtra("profileImage",profileImage);
                intent1.putExtra("fullName",fullName);
                startActivity(intent1);
                finish();*/
            }
            break;
        }
    }

    public void jobdetailApiData() {
        pDialog.pdialog(NotiDetailActivity.this);
        String authtoken = session.getAuthtoken();
        String jobid = session.getjobid();
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
                                reqid = jsonObject2.getString("_id");
                                String price = jsonObject2.getString("price");
                                String serviceDate = jsonObject2.getString("serviceDate");
                                String description = jsonObject2.getString("description");
                                status = jsonObject2.getString("status");
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
                                String propertyImage = "";
                                JSONArray jsonArray3 = jsonObject2.getJSONArray("propertyImg");
                                if (jsonArray3.length() > 0) {
                                    JSONObject jsonObject5 = jsonArray3.getJSONObject(0);
                                    propertyImage = jsonObject5.getString("propertyImage");
                                }
                                JSONArray jsonArraydata = jsonObject2.getJSONArray("propertyData");
                                JSONObject jsonObjectdata = jsonArraydata.getJSONObject(0);
                                String bathroom = jsonObjectdata.getString("bathroom");
                                String bedroom = jsonObjectdata.getString("bedroom");
                                String address = jsonObjectdata.getString("propertyAddress");
                                String propertyName = jsonObjectdata.getString("propertyName");
                                String propertySize = jsonObjectdata.getString("propertySize");


                                JSONArray ratingarray = jsonObject2.getJSONArray("ratingInfo");
                                for (int i = 0; i < ratingarray.length(); i++) {
                                    JSONObject ratingobject = ratingarray.getJSONObject(i);
                                    int _id = ratingobject.getInt("_id");
                                    int rating = ratingobject.getInt("rating");
                                    String review = ratingobject.getString("review");
                                    String givenTo = ratingobject.getString("givenTo");
                                    String crd = ratingobject.getString("crd");
                                    int jobId = ratingobject.getInt("jobId");
                                    int ownerId = ratingobject.getInt("ownerId");
                                    int receptionistId = ratingobject.getInt("receptionistId");
                                    int __v = ratingobject.getInt("__v");

                                    RatingInfo model = new RatingInfo(_id, rating, review, givenTo,crd,jobId,ownerId,receptionistId,__v);
                                    ratingList.add(model);
                                }



                                if (!jsonObject2.getString("subCategoryData").isEmpty()) {
                                    JSONArray subCategoryData = jsonObject2.getJSONArray("subCategoryData");
                                    for (int i = 0; i < subCategoryData.length(); i++) {
                                        String title1 = "";
                                        JSONObject subobject = subCategoryData.getJSONObject(i);
                                        String id = subobject.getString("id");
                                        String price1 = subobject.getString("price");
                                        String quantity="0.0";
                                        if(subobject.has("quantity")) {
                                            quantity = subobject.getString("quantity");
                                        }
                                        if(subobject.has("quntity")) {
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
                                Toast.makeText(NotiDetailActivity.this, msg, Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(NotiDetailActivity.this, msg, Toast.LENGTH_SHORT).show();
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

        if (des.isEmpty()){
            tv_des.setVisibility(View.GONE);
            tvdescription.setVisibility(View.GONE);
            view1.setVisibility(View.GONE);
        }

        if (subCategoryList.size() != 0)
        {
            recyclerView.setVisibility(View.VISIBLE);
            rl_subcategory.setVisibility(View.VISIBLE);
            view.setVisibility(View.VISIBLE);
            Addservice.setVisibility(View.VISIBLE);
            tv_subcategoryprice.setText("$"+String.valueOf(totalprice));

            double totprice = Double.valueOf(price)+totalprice;
            tv_addservice_price.setText("$"+String.valueOf(totprice));

        }
        else {
            tv_addservice_price.setText("$" + price);

        }

        if (propimg.length() != 0) {
            RequestOptions options = new RequestOptions();
            options.placeholder(R.drawable.app_ico);
            options.diskCacheStrategy(DiskCacheStrategy.RESOURCE);
            Glide.with(NotiDetailActivity.this).load(propimg).apply(options).into(rece_detailimg);
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
                btn_appodetail_changestatus.setVisibility(View.GONE);

                break;
            case "2":
                tvstatus.setText(getResources().getString(R.string.confirm));
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
                if (ratingList.size() != 0){
                    ll_rating.setVisibility(View.VISIBLE);
                }
                else {
                    ll_rating.setVisibility(View.GONE);
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
            Glide.with(NotiDetailActivity.this).load(simage).into(serviceimage);
        }
        if (ownimage.length() != 0) {
            RequestOptions options = new RequestOptions();
            options.placeholder(R.drawable.user_img);
            options.diskCacheStrategy(DiskCacheStrategy.RESOURCE);
            Glide.with(NotiDetailActivity.this).load(ownimage).into(vendorimage);
        }
    }

    private void initiatePopupWindow() {
        try {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            LayoutInflater inflater = (LayoutInflater) NotiDetailActivity.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert inflater != null;
            final View layout = inflater.inflate(R.layout.statuspop,
                    (ViewGroup) findViewById(R.id.popup_element));
            final PopupWindow popUp = new PopupWindow(layout, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    true);
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
                    iv_start_service.setBackground(getResources().getDrawable(R.drawable.circlechat));
                    iv_start_service.setColorFilter(ContextCompat.getColor(NotiDetailActivity.this, R.color.colorwhite));
                    tv_start_service.setTextColor(getResources().getColor(R.color.colorPrimary));
                    iv_inprogress_service.setBackground(getResources().getDrawable(R.drawable.circletransparent));
                    iv_inprogress_service.setColorFilter(ContextCompat.getColor(NotiDetailActivity.this, R.color.colorgray));
                    tv_inprogress_service.setTextColor(getResources().getColor(R.color.colorgray));
                    iv_end_service.setBackground(getResources().getDrawable(R.drawable.circletransparent));
                    iv_end_service.setColorFilter(ContextCompat.getColor(NotiDetailActivity.this, R.color.colorgray));
                    tv_end_service.setTextColor(getResources().getColor(R.color.colorgray));
                    btn_appodetail_changestatus.setVisibility(View.VISIBLE);
                    break;
                case "3":
                    iv_start_service.setBackground(getResources().getDrawable(R.drawable.circletransparent));
                    iv_start_service.setColorFilter(ContextCompat.getColor(NotiDetailActivity.this, R.color.colorgray));
                    tv_start_service.setTextColor(getResources().getColor(R.color.colorgray));
                    iv_inprogress_service.setBackground(getResources().getDrawable(R.drawable.circlechat));
                    iv_inprogress_service.setColorFilter(ContextCompat.getColor(NotiDetailActivity.this, R.color.colorwhite));
                    tv_inprogress_service.setTextColor(getResources().getColor(R.color.colorPrimary));
                    iv_end_service.setBackground(getResources().getDrawable(R.drawable.circletransparent));
                    iv_end_service.setColorFilter(ContextCompat.getColor(NotiDetailActivity.this, R.color.colorgray));
                    tv_end_service.setTextColor(getResources().getColor(R.color.colorgray));
                    btn_appodetail_changestatus.setVisibility(View.VISIBLE);
                    break;
                case "4":
                    iv_start_service.setBackground(getResources().getDrawable(R.drawable.circletransparent));
                    iv_start_service.setColorFilter(ContextCompat.getColor(NotiDetailActivity.this, R.color.colorgray));
                    tv_start_service.setTextColor(getResources().getColor(R.color.colorgray));
                    iv_inprogress_service.setBackground(getResources().getDrawable(R.drawable.circletransparent));
                    iv_inprogress_service.setColorFilter(ContextCompat.getColor(NotiDetailActivity.this, R.color.colorgray));
                    tv_inprogress_service.setTextColor(getResources().getColor(R.color.colorgray));
                    iv_end_service.setBackground(getResources().getDrawable(R.drawable.circlechat));
                    iv_end_service.setColorFilter(ContextCompat.getColor(NotiDetailActivity.this, R.color.colorwhite));
                    tv_end_service.setTextColor(getResources().getColor(R.color.colorPrimary));
                    btn_appodetail_changestatus.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }


            ll_start_service.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    status = "2";
                    iv_start_service.setBackground(getResources().getDrawable(R.drawable.circlechat));
                    iv_start_service.setColorFilter(ContextCompat.getColor(NotiDetailActivity.this, R.color.colorwhite));
                    tv_start_service.setTextColor(getResources().getColor(R.color.colorPrimary));

                    iv_inprogress_service.setBackground(getResources().getDrawable(R.drawable.circletransparent));
                    iv_inprogress_service.setColorFilter(ContextCompat.getColor(NotiDetailActivity.this, R.color.colorgray));
                    tv_inprogress_service.setTextColor(getResources().getColor(R.color.colorgray));

                    iv_end_service.setBackground(getResources().getDrawable(R.drawable.circletransparent));
                    iv_end_service.setColorFilter(ContextCompat.getColor(NotiDetailActivity.this, R.color.colorgray));
                    tv_end_service.setTextColor(getResources().getColor(R.color.colorgray));

                }
            });

            ll_inprogress_service.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    status = "3";
                    iv_start_service.setBackground(getResources().getDrawable(R.drawable.circletransparent));
                    iv_start_service.setColorFilter(ContextCompat.getColor(NotiDetailActivity.this, R.color.colorgray));
                    tv_start_service.setTextColor(getResources().getColor(R.color.colorgray));

                    iv_inprogress_service.setBackground(getResources().getDrawable(R.drawable.circlechat));
                    iv_inprogress_service.setColorFilter(ContextCompat.getColor(NotiDetailActivity.this, R.color.colorwhite));
                    tv_inprogress_service.setTextColor(getResources().getColor(R.color.colorPrimary));

                    iv_end_service.setBackground(getResources().getDrawable(R.drawable.circletransparent));
                    iv_end_service.setColorFilter(ContextCompat.getColor(NotiDetailActivity.this, R.color.colorgray));
                    tv_end_service.setTextColor(getResources().getColor(R.color.colorgray));

                }

            });

            ll_end_service.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    status = "4";
                    iv_start_service.setBackground(getResources().getDrawable(R.drawable.circletransparent));
                    iv_start_service.setColorFilter(ContextCompat.getColor(NotiDetailActivity.this, R.color.colorgray));
                    tv_start_service.setTextColor(getResources().getColor(R.color.colorgray));

                    iv_inprogress_service.setBackground(getResources().getDrawable(R.drawable.circletransparent));
                    iv_inprogress_service.setColorFilter(ContextCompat.getColor(NotiDetailActivity.this, R.color.colorgray));
                    tv_inprogress_service.setTextColor(getResources().getColor(R.color.colorgray));

                    iv_end_service.setBackground(getResources().getDrawable(R.drawable.circlechat));
                    iv_end_service.setColorFilter(ContextCompat.getColor(NotiDetailActivity.this, R.color.colorwhite));
                    tv_end_service.setTextColor(getResources().getColor(R.color.colorPrimary));


                }
            });

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
        pDialog.pdialog(NotiDetailActivity.this);
        String authtoken = session.getAuthtoken();
        Call<ResponseBody> call = RetrofitClient.getInstance()
                .getApi().acceptReject(authtoken, "application/x-www-form-urlencoded", reqid, reqid, senderid, status, "1");
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

                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

                                jobdetailApiData();

                            } else {
                                Toast.makeText(NotiDetailActivity.this, msg, Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(NotiDetailActivity.this, msg, Toast.LENGTH_SHORT).show();
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
    public void calculatePrice(double price,double quntity){

        totalprice += price * quntity;
        Log.e("totalprice",String.valueOf(totalprice));
    }


}
