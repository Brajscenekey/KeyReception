package com.key.keyreception.activity.owner;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.key.keyreception.R;
import com.key.keyreception.Session;
import com.key.keyreception.activity.ActivityAdapter.SelecttypeAdapter;
import com.key.keyreception.activity.ActivityAdapter.ServicetypeAdapter;
import com.key.keyreception.activity.model.ActivePropertyData;
import com.key.keyreception.activity.model.PropertyJson;
import com.key.keyreception.activity.model.ServiceCategory;
import com.key.keyreception.base.BaseActivity;
import com.key.keyreception.connection.RetrofitClient;
import com.key.keyreception.helper.PDialog;
import com.key.keyreception.helper.Utility;
import com.key.keyreception.helper.Validation;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class CreatePostActivity extends BaseActivity implements View.OnClickListener {

    private Spinner spinner_bathroom;
    private Spinner spinner_bedroom;
    private ServicetypeAdapter servicetypeAdapter;
    private List<ServiceCategory> categorylist = new ArrayList<>();
    private List<ActivePropertyData> propertyList = new ArrayList<>();
    private SelecttypeAdapter selecttypeAdapter;
    private EditText etdatetime, etpost_price, etpost_property_size, etpost_Description;
    private String adminPropertyCalc = "0.0", name, address, tin, tout, image, lat, lon, categoryid = "", bathroom, bedroom, propertyid, servicecheck = "", categorycheck = "", bath = "", bed = "";
    private Session session;
    private PDialog pDialog;
    private TextView tv_property_name, tv_property_address, tv_post_intime, tv_post_outtime;
    private ImageView iv_property_image;
    private LinearLayout li_selectproperty1;
    private RelativeLayout rl_propertydata;
    private Utility utility;
    private Validation validation;
    private String oDate;
    private List<ActivePropertyData.OwnerDetailBean> ownerDetaillist = new ArrayList<>();
    private List<PropertyJson> propertyJsonList = new ArrayList<>();
    private TextView ftsBedroomSelector, ftsBathroomSelector;
    private Boolean isFTSSelected = false;
    private Boolean isFTSSelected1 = false;
    private String propertySize = "0.0";
    private String propertySize1 = "0";
    private String json = "";
    private List<ActivePropertyData.PropertyImgBean> propertyImgs;
    private Button dialog_addprop;
    private TextView tv_dialog_no_record;
    private LinearLayout ll_btn_dialog;
    private TextWatcher watcherClass_search = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {

            if (editable == etpost_property_size.getEditableText()) {

                propertySize = etpost_property_size.getText().toString();
                pricecalculation(getPropertysizePrice(propertySize), propertySize1);




            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        session = new Session(this);
        pDialog = new PDialog();
        utility = new Utility();
        validation = new Validation(this);
        init();
        serviceCategoryApiData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        propertyListApiData();
    }

    public void init() {
        etdatetime = findViewById(R.id.et_date_cpost);
        CardView card_selcecttype = findViewById(R.id.card_selcecttype);
        etdatetime.setOnClickListener(this);
        card_selcecttype.setOnClickListener(this);
        RecyclerView recyclerView = findViewById(R.id.post_recycler_view);
        ImageView iv_leftarrow_post = findViewById(R.id.iv_leftarrow_post);
        TextView tv_change_property = findViewById(R.id.tv_change_property);
        tv_property_name = findViewById(R.id.tv_property_name);
        tv_property_address = findViewById(R.id.tv_property_address);
        iv_property_image = findViewById(R.id.iv_selecttype_image);
        li_selectproperty1 = findViewById(R.id.li_selectproperty1);
        rl_propertydata = findViewById(R.id.rl_propertydata);
        tv_post_intime = findViewById(R.id.tv_post_intime);
        tv_post_outtime = findViewById(R.id.tv_post_outtime);
        spinner_bathroom = findViewById(R.id.Spinner_bathroom);
        spinner_bedroom = findViewById(R.id.Spinner_bedroom);
        etpost_price = findViewById(R.id.post_price);
        etpost_property_size = findViewById(R.id.post_property_size);
        etpost_Description = findViewById(R.id.post_Description);
        Button btnpost = findViewById(R.id.btnpost);
        btnpost.setOnClickListener(this);
        ftsBedroomSelector = findViewById(R.id.spinner_bedroom_selector);
        ftsBathroomSelector = findViewById(R.id.spinner_bathroom_selector);
        etpost_property_size.setFilters(new InputFilter[] {Utility.filter});



        etpost_property_size.addTextChangedListener(watcherClass_search);

        ftsBathroomSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                spinner_bathroom.dispatchTouchEvent(MotionEvent.obtain(0, 0, MotionEvent.ACTION_DOWN, 100, 100, 0.5f, 5, 0, 1, 1, 0, 0));

            }
        });
        ftsBedroomSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                spinner_bedroom.dispatchTouchEvent(MotionEvent.obtain(0, 0, MotionEvent.ACTION_DOWN, 100, 100, 0.5f, 5, 0, 1, 1, 0, 0));
            }
        });
        spinner_bathroom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                bathroom = adapterView.getItemAtPosition(i).toString();
                if (isFTSSelected1) {
                    bath = "1";
                    ftsBathroomSelector.setVisibility(View.GONE);
                    bathroom = adapterView.getItemAtPosition(i).toString();
                }
                isFTSSelected1 = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        spinner_bathroom.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.performClick();
                    bathroom = spinner_bathroom.getSelectedItem().toString();
                    if (isFTSSelected1) {
                        bath = "1";
                        ftsBathroomSelector.setVisibility(View.GONE);
                        bathroom = spinner_bathroom.getSelectedItem().toString();
                    }
                }
                return true;
            }
        });
        spinner_bedroom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                bedroom = adapterView.getItemAtPosition(i).toString();
                if (isFTSSelected) {
                    bed = "1";
                    ftsBedroomSelector.setVisibility(View.GONE);
                    bedroom = adapterView.getItemAtPosition(i).toString();
                }
                isFTSSelected = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        spinner_bedroom.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.performClick();
                    bedroom = spinner_bedroom.getSelectedItem().toString();
                    if (isFTSSelected1) {
                        bed = "1";
                        ftsBedroomSelector.setVisibility(View.GONE);
                        bedroom = spinner_bedroom.getSelectedItem().toString();

                    }
                }
                return true;
            }
        });


        rl_propertydata.setVisibility(View.GONE);
        tv_change_property.setOnClickListener(this);

        servicetypeAdapter = new ServicetypeAdapter(CreatePostActivity.this, categorylist, new ServicetypeAdapter.CatgoryListener() {
            @Override
            public void categoryid(String id, String s) {
                categoryid = id;
                servicecheck = s;
            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(CreatePostActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(servicetypeAdapter);
        iv_leftarrow_post.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.et_date_cpost: {
                utility.hideKeyboardFrom(this, view);
                startdatemathod();
            }
            break;

            case R.id.tv_change_property: {
                initiatePopupWindow();
            }
            break;

            case R.id.card_selcecttype: {
                initiatePopupWindow();
            }
            break;
            case R.id.iv_leftarrow_post: {
                backPress();
            }
            case R.id.btnpost: {
                if (utility.checkInternetConnection(this)) {
                    if (validation.isselectpropValid(categorycheck) && validation.isbedroomValid(bed) && validation.isbathroomValid(bath) && validation.ispriceValid(etpost_price) && validation.ispropsizeValid(etpost_property_size) && validation.isdateValid(etdatetime) && validation.isserviceValid(servicecheck) && validation.isdesValid(etpost_Description) && validation.ispropsizeinValid(Double.parseDouble(etpost_property_size.getText().toString()))) {

                        postApiData();

                    }
                } else {
                    Toast.makeText(this, "Please check your network", Toast.LENGTH_SHORT).show();
                }
            }
            break;
        }
    }

    public void startdatemathod() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(CreatePostActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                        Date date = new Date();
                        String strDateFormat = "hh:mm a";
                        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
                        String formattedDate = dateFormat.format(date);
                        String monthString = new DateFormatSymbols().getMonths()[month];
                        String disDate = day + "," + (monthString) + " " + formattedDate;
                        etdatetime.setText(disDate);
                        //18,April 07:57 PM
                        String tmpDate = day + "/" + monthString + "/" + year + " " + formattedDate;
                        oDate = Utility.formatDate(tmpDate, "dd/MMM/yyyy hh:mm a", "yyyy-MM-dd HH:mm:ss");
                        //2019-04-02 16:21:44
                        Log.e("What", disDate + "  ==  " + oDate);

                    }
                }, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private void initiatePopupWindow() {
        try {

            LayoutInflater inflater = (LayoutInflater) CreatePostActivity.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert inflater != null;
            final View layout = inflater.inflate(R.layout.selecttypedialog,
                    (ViewGroup) findViewById(R.id.popup_element));
            final PopupWindow popUp = new PopupWindow(layout, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    true);
            popUp.showAtLocation(layout, Gravity.CENTER, 0, 0);


            popUp.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    if (getWindow() != null)
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

                }
            });

            if (getWindow() != null)
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

            // Button done = layout.findViewById(R.id.dialog_done);
            Button cancel = layout.findViewById(R.id.dialog_cancel);
            dialog_addprop = layout.findViewById(R.id.dialog_addprop);
            ImageView iv_dialogarrow_post = layout.findViewById(R.id.iv_dialogarrow_post);
            tv_dialog_no_record = layout.findViewById(R.id.tv_dialog_no_record);
            ll_btn_dialog = layout.findViewById(R.id.ll_btn_dialog);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    popUp.dismiss();

                }
            });


            iv_dialogarrow_post.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    popUp.dismiss();

                }
            });
            RecyclerView check_recycler_view = layout.findViewById(R.id.selecttype_recycler_view);

            selecttypeAdapter = new SelecttypeAdapter(CreatePostActivity.this, propertyList, new SelecttypeAdapter.PropertyListener() {
                @Override
                public void pos(int i) {
                    propertyid = String.valueOf(propertyList.get(i).get_id());
                    name = propertyList.get(i).getPropertyName();
                    address = propertyList.get(i).getPropertyAddress();
                    tin = propertyList.get(i).getPropertyCheckIn();
                    tout = propertyList.get(i).getPropertyCheckOut();
                    if (propertyList.get(i).getPropertyImg().size() != 0) {
                        image = propertyList.get(i).getPropertyImg().get(0).getPropertyImage();
                    }
                    lat = propertyList.get(i).getPropertyLat();
                    lon = propertyList.get(i).getPropertyLong();
                    bathroom = propertyList.get(i).getBathroom();
                    bedroom = propertyList.get(i).getBedroom();
//                    adminPropertyCalc = propertyList.get(i).getAdminPropertyCalc();
                    propertySize = propertyList.get(i).getPropertySize();
                    setPropertyData(bedroom, bathroom, propertySize);
                    pricecalculation(getPropertysizePrice(propertySize), propertySize1);


                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault());
                        Date date = sdf.parse(tin);
                        Date date1 = sdf.parse(tout);
                        SimpleDateFormat mSDF = new SimpleDateFormat("dd, MMMM hh:mm a", Locale.getDefault());
                        String fDate = mSDF.format(date);
                        String fDate1 = mSDF.format(date1);
                        tv_post_intime.setText(fDate);
                        tv_post_outtime.setText(fDate1);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    tv_property_name.setText(name);
                    tv_property_address.setText(address);


                }
            });

            updateDialogUI();


            dialog_addprop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(CreatePostActivity.this, AddpropertyActivity.class);
                    intent.putExtra("intentid", "100");
                    startActivity(intent);
                    // finish();
                }
            });

            Button done = layout.findViewById(R.id.dialog_done);
            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    rl_propertydata.setVisibility(View.VISIBLE);
                    li_selectproperty1.setVisibility(View.GONE);
                    tv_post_intime.setVisibility(View.VISIBLE);
                    tv_post_outtime.setVisibility(View.VISIBLE);
                    categorycheck = "dfs";
                    Glide.with(CreatePostActivity.this).load(image).into(iv_property_image);
                    popUp.dismiss();
                    PropertyJson propertyJson = new PropertyJson();
                    propertyJson.setPropertyName(name);
                    propertyJson.setPropertyAddress(address);
                    propertyJson.setPropertyId(propertyid);
                    propertyJson.setPropertyLat(lat);
                    propertyJson.setPropertyLong(lon);
                    propertyJson.setBathroom(bathroom);
                    propertyJson.setBedroom(bedroom);
                    propertyJson.setPropertySize(propertySize);
                    propertyJson.setIsImageAdd("1");
                    propertyJsonList.add(propertyJson);
                    Gson gson = new Gson();
                    json = gson.toJson(propertyJsonList);
                    Log.v("projson", json);

                }
            });

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(CreatePostActivity.this);
            check_recycler_view.setLayoutManager(mLayoutManager);
            check_recycler_view.setItemAnimator(new DefaultItemAnimator());
            check_recycler_view.setAdapter(selecttypeAdapter);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateDialogUI() {
        if (tv_dialog_no_record != null) {
            if (propertyList.size() == 0) {
                tv_dialog_no_record.setVisibility(View.VISIBLE);
                dialog_addprop.setVisibility(View.VISIBLE);
                ll_btn_dialog.setVisibility(View.GONE);
                dialog_addprop.setVisibility(View.VISIBLE);

            } else {
                tv_dialog_no_record.setVisibility(View.GONE);
                dialog_addprop.setVisibility(View.GONE);
                ll_btn_dialog.setVisibility(View.VISIBLE);

            }
        }
    }

    public void serviceCategoryApiData() {
        pDialog.pdialog(CreatePostActivity.this);
        Call<ResponseBody> call = RetrofitClient.getInstance()
                .getAnotherApi().categoryList();
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
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    ServiceCategory serviceCategory;
                                    JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                                    int _id = jsonObject2.getInt("_id");
                                    String title = jsonObject2.getString("title");
                                    String image = jsonObject2.getString("image");
                                    String imageurl = jsonObject2.getString("imageurl");
                                    serviceCategory = new ServiceCategory(_id, title, image, imageurl);
                                    categorylist.add(serviceCategory);

                                }
                                servicetypeAdapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(CreatePostActivity.this, msg, Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(CreatePostActivity.this, msg, Toast.LENGTH_SHORT).show();
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

    public void propertyListApiData() {
        String authtoken = session.getAuthtoken();
        Call<ResponseBody> call = RetrofitClient.getInstance()
                .getApi().activePropertyList(authtoken);
        call.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("NewApi")
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull retrofit2.Response<ResponseBody> response) {
                try {

                    switch (response.code()) {
                        case 200: {
                            String stresult = Objects.requireNonNull(response.body()).string();
                            Log.d("response", stresult);
                            JSONObject jsonObject = new JSONObject(stresult);
                            String statusCode = jsonObject.optString("status");
                            String msg = jsonObject.optString("message");
                            if (statusCode.equals("success")) {
                                if (propertyList == null)
                                    propertyList = new ArrayList<>();
                                propertyList.clear();
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    ActivePropertyData propertyData;

                                    JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                                    int _id = jsonObject2.getInt("_id");
                                    String propertyName = jsonObject2.getString("propertyName");
                                    String bedroom = jsonObject2.getString("bedroom");
                                    String bathroom = jsonObject2.getString("bathroom");
                                    String propertySize = jsonObject2.getString("propertySize");
                                    String propertyAddress = jsonObject2.getString("propertyAddress");
                                    String propertyLat = jsonObject2.getString("propertyLat");
                                    String propertyLong = jsonObject2.getString("propertyLong");
                                    int userId = jsonObject2.getInt("userId");
                                    int status = jsonObject2.getInt("status");
                                    String crd = jsonObject2.getString("crd");
                                    String propertyCheckIn = jsonObject2.getString("propertyCheckIn");
                                    String propertyCheckOut = jsonObject2.getString("propertyCheckOut");
                                    String adminPropertyCalc = jsonObject2.getString("adminPropertyCalc");

                                    JSONArray jsonArray1 = jsonObject2.getJSONArray("propertyImg");
                                    propertyImgs = new ArrayList<>();
                                    for (int j = 0; j < jsonArray1.length(); j++) {
                                        JSONObject jsonObject3 = jsonArray1.getJSONObject(j);
                                        ActivePropertyData.PropertyImgBean propertyImgBean;
                                        int _id1 = jsonObject3.getInt("_id");
                                        String propertyImage = jsonObject3.getString("propertyImage");
                                        propertyImgBean = new ActivePropertyData.PropertyImgBean(_id1, propertyImage);
                                        propertyImgs.add(propertyImgBean);
                                    }

                                    JSONArray jsonArray2 = jsonObject2.getJSONArray("ownerDetail");
                                    for (int j = 0; j < jsonArray2.length(); j++) {
                                        JSONObject jsonObject4 = jsonArray2.getJSONObject(j);
                                        ActivePropertyData.OwnerDetailBean ownerDetailBean;
                                        int _id2 = jsonObject4.getInt("_id");
                                        String profileImage = jsonObject4.getString("profileImage");
                                        String fullName = jsonObject4.getString("fullName");
                                        ownerDetailBean = new ActivePropertyData.OwnerDetailBean(_id2, profileImage, fullName);
                                        ownerDetaillist.add(ownerDetailBean);
                                    }
                                    propertyData = new ActivePropertyData(_id, propertyName, bedroom, bathroom, propertySize, propertyAddress, propertyLat, propertyLong, propertyCheckIn, propertyCheckOut, userId, status, crd, adminPropertyCalc, propertyImgs, ownerDetaillist);

                                    propertyList.add(propertyData);

                                }

                                selecttypeAdapter.notifyDataSetChanged();

                                updateDialogUI();


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
                                Toast.makeText(CreatePostActivity.this, msg, Toast.LENGTH_SHORT).show();
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

    public void postApiData() {
        String price = etpost_price.getText().toString();
        String propsize = etpost_property_size.getText().toString();
        String date = etdatetime.getText().toString();
        String des = etpost_Description.getText().toString();
        String intime = tv_post_intime.getText().toString();
        String outtime = tv_post_outtime.getText().toString();
        String authtoken = session.getAuthtoken();
        pDialog.pdialog(this);
        Call<ResponseBody> call = RetrofitClient.getInstance()
                .getApi().createJob(authtoken, propertyid, bedroom, bathroom, address, lat, lon, price, propsize, oDate, intime, outtime, categoryid, des, name, json);
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
                                setResult(Activity.RESULT_OK);
                                finish();
                            } else {
                                Toast.makeText(CreatePostActivity.this, msg, Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(CreatePostActivity.this, msg, Toast.LENGTH_SHORT).show();
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

    public void setPropertyData(String bedroom1, String bathroom1, String propertySize) {


        ftsBathroomSelector.setVisibility(View.VISIBLE);
        ftsBedroomSelector.setVisibility(View.VISIBLE);

        ftsBathroomSelector.setText(bathroom1);
        ftsBedroomSelector.setText(bedroom1);
        etpost_property_size.setText(propertySize);
        if (!bathroom1.equals("")) {
            bath = "1";
        }
        if (!bedroom1.equals("")) {
            bed = "1";
        }

    }

    public void pricecalculation(String cal, String psize) {
        if (!psize.equals("")) {
            float totPropertyPrice = Float.valueOf(cal) * Float.valueOf(psize);
            etpost_price.setText(String.valueOf(totPropertyPrice));
        } else {
            etpost_price.setText("");

        }
    }

    public String getPropertysizePrice(String propertySize) {
        String calculation = "0.0";

        if (propertySize.equals(""))
        { propertySize ="0"; }

        if (0 <= Double.parseDouble(propertySize) && 750 >= Double.parseDouble(propertySize)) {
            calculation = String.valueOf(0.11);
            propertySize1 = String.valueOf(750);
        } else if (750 <= Double.parseDouble(propertySize) && 1500 >= Double.parseDouble(propertySize)) {
            calculation = String.valueOf(0.065);
            propertySize1 = String.valueOf(1500);
        } else if (1500 <= Double.parseDouble(propertySize) && 2250 >= Double.parseDouble(propertySize)) {
            calculation = String.valueOf(0.045);
            propertySize1 = String.valueOf(2250);
        } else if (2250 <= Double.parseDouble(propertySize) && 3000 >= Double.parseDouble(propertySize)) {
            calculation = String.valueOf(0.04);
            propertySize1 = String.valueOf(3000);
        } else if (3000 <= Double.parseDouble(propertySize) && 3750 >= Double.parseDouble(propertySize)) {
            calculation = String.valueOf(0.035);
            propertySize1 = String.valueOf(3750);
        } else if (3750 <= Double.parseDouble(propertySize) && 4500 >= Double.parseDouble(propertySize)) {
            calculation = String.valueOf(0.032);
            propertySize1 = String.valueOf(4500);
        }else {
            calculation = String.valueOf(0.032);
            propertySize1 = String.valueOf(Double.parseDouble(propertySize));
        }
        return calculation;
    }


}

