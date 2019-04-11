package com.key.keyreception.Activity.Owner;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.key.keyreception.Activity.ActivityAdapter.SelecttypeAdapter;
import com.key.keyreception.Activity.ActivityAdapter.ServicetypeAdapter;
import com.key.keyreception.Activity.model.PropertyData;
import com.key.keyreception.Activity.model.ServiceCategory;
import com.key.keyreception.R;
import com.key.keyreception.Session;
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

    private ServicetypeAdapter servicetypeAdapter;
    private List<ServiceCategory> categorylist = new ArrayList<>();
    private List<PropertyData> propertyList = new ArrayList<>();
    private SelecttypeAdapter selecttypeAdapter;
    private EditText etdatetime, etpost_price, etpost_property_size, etpost_Description;
    private String name, address, tin, tout, image, lat, lon, categoryid = "", bathroom, bedroom, propertyid, servicecheck = "", categorycheck = "", bath = "", bed = "";
    private Session session;
    private PDialog pDialog;
    private TextView tv_property_name, tv_property_address, tv_post_intime, tv_post_outtime;
    private ImageView iv_property_image;
    private LinearLayout li_selectproperty1;
    private RelativeLayout rl_propertydata;
    private Utility utility;
    private Validation validation;

    //First time spinner bedroom selector
    private TextView ftsBedroomSelector, ftsBathroomSelector;
    private Boolean isFTSSelected = false;
    private Boolean isFTSSelected1 = false;

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
        final Spinner spinner_bathroom = findViewById(R.id.Spinner_bathroom);
        final Spinner spinner_bedroom = findViewById(R.id.Spinner_bedroom);
//        spinner_bedroom.setSelection(-1);
        etpost_price = findViewById(R.id.post_price);
        etpost_property_size = findViewById(R.id.post_property_size);
        etpost_Description = findViewById(R.id.post_Description);
        Button btnpost = findViewById(R.id.btnpost);
        btnpost.setOnClickListener(this);
        ftsBedroomSelector = findViewById(R.id.spinner_bedroom_selector);
        ftsBathroomSelector = findViewById(R.id.spinner_bathroom_selector);
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
//        Spinner_bathroom.setOnItemSelectedListener(this);
//        Spinner_bedroom.setOnItemSelectedListener(this);
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
                startdatemathod();
            }
            break;

            case R.id.tv_change_property: {
                initiatePopupWindow(view);
            }
            break;

            case R.id.card_selcecttype: {
                initiatePopupWindow(view);
            }
            break;
            case R.id.iv_leftarrow_post: {
                backPress();
            }
            case R.id.btnpost: {
                if (utility.checkInternetConnection(this)) {
                    if (validation.isselectpropValid(categorycheck) && validation.isbedroomValid(bed) && validation.isbathroomValid(bath) && validation.ispriceValid(etpost_price) && validation.ispropsizeValid(etpost_property_size) && validation.isdateValid(etdatetime) && validation.isserviceValid(servicecheck) && validation.isdesValid(etpost_Description)) {

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
                        etdatetime.setText(day + "," + (monthString) + " " + formattedDate);
                    }
                }, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private void initiatePopupWindow(View v) {
        try {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            LayoutInflater inflater = (LayoutInflater) CreatePostActivity.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert inflater != null;
            final View layout = inflater.inflate(R.layout.selecttypedialog,
                    (ViewGroup) findViewById(R.id.popup_element));
            final PopupWindow popUp = new PopupWindow(layout, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    true);
            popUp.showAtLocation(v, Gravity.CENTER, 0, 0);
            popUp.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

                }
            });

            // Button done = layout.findViewById(R.id.dialog_done);
            Button cancel = layout.findViewById(R.id.dialog_cancel);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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
                    image = propertyList.get(i).getPropertyImage();
                    lat = propertyList.get(i).getPropertyLat();
                    lon = propertyList.get(i).getPropertyLong();


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


    public void serviceCategoryApiData() {
        pDialog.pdialog(CreatePostActivity.this);
//        String authtoken = session.getAuthtoken();
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
                .getApi().propertyList(authtoken);
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
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    PropertyData propertyData;

                                    JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                                    int _id = jsonObject2.getInt("_id");
                                    String propertyName = jsonObject2.getString("propertyName");
                                    String propertyImage = jsonObject2.getString("propertyImage");
                                    String propertyAddress = jsonObject2.getString("propertyAddress");
                                    String propertyLat = jsonObject2.getString("propertyLat");
                                    String propertyLong = jsonObject2.getString("propertyLong");
                                    String propertyCheckIn = jsonObject2.getString("propertyCheckIn");
                                    String propertyCheckOut = jsonObject2.getString("propertyCheckOut");

                                    propertyData = new PropertyData(_id, propertyName, propertyImage, propertyAddress, propertyLat, propertyLong, propertyCheckIn, propertyCheckOut);

                                    propertyList.add(propertyData);

                                }

                                selecttypeAdapter.notifyDataSetChanged();


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

    public void postApiData() {
        String price = etpost_price.getText().toString();
        String propsize = etpost_property_size.getText().toString();
        String date = etdatetime.getText().toString();
        String des = etpost_Description.getText().toString();
        String authtoken = session.getAuthtoken();
        pDialog.pdialog(this);
        Call<ResponseBody> call = RetrofitClient.getInstance()
                .getApi().createJob(authtoken, propertyid, bedroom, bathroom, address, lat, lon, price, propsize, date, tin, tout, categoryid, des, name);
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


}

