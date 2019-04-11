package com.key.keyreception.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.key.keyreception.Activity.Owner.OwnerTabActivity;
import com.key.keyreception.Activity.Recepnist.TabActivity;
import com.key.keyreception.R;
import com.key.keyreception.Session;
import com.key.keyreception.base.BaseActivity;
import com.key.keyreception.connection.RetrofitClient;
import com.key.keyreception.helper.PDialog;
import com.key.keyreception.helper.Utility;
import com.key.keyreception.helper.Validation;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class PaymentActivity extends BaseActivity implements View.OnClickListener {

    private TextView tvpaypal, tvvenmo, tvcredit;
    private ImageView ivpaypal, ivvenmo, ivcredit;
    private RelativeLayout rlpaypal, rlvenmo, rlcredit;
    private EditText etAddress;
    private PDialog pDialog;
    private static final int LOC_REQ_CODE = 3;
    private static final int PLACE_PICKER_REQ_CODE = 2;
    private Session session;
    private String latitude;
    private String longitude;
    private String ptype="";
    private File file;
    private Validation validation;
    private Utility utility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        pDialog = new PDialog();
        session = new Session(this);
        validation = new Validation(this);
        utility = new Utility();
        init();

    }

    private void init() {
        tvpaypal = findViewById(R.id.tv_paypal);
        tvvenmo = findViewById(R.id.tv_venemo);
        tvcredit = findViewById(R.id.tv_credit);
        TextView skip = findViewById(R.id.skip);
        Button btn_paymentmethod = findViewById(R.id.btn_paymentmethod);
        ivpaypal = findViewById(R.id.iv_paypal);
        ivvenmo = findViewById(R.id.iv_venemo);
        ivcredit = findViewById(R.id.iv_credit);
        rlpaypal = findViewById(R.id.rl_paypal);
        rlvenmo = findViewById(R.id.rl_venemo);
        rlcredit = findViewById(R.id.rl_credit);
        etAddress = findViewById(R.id.payment_address);

        btn_paymentmethod.setOnClickListener(this);
        skip.setOnClickListener(this);
        etAddress.setOnClickListener(this);
        rlpaypal.setOnClickListener(this);
        rlvenmo.setOnClickListener(this);
        rlcredit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_paypal: {
                ptype = "paypal";
                rlpaypal.setBackground(getResources().getDrawable(R.drawable.cornergrayback));
                rlvenmo.setBackground(getResources().getDrawable(R.drawable.cornerwhiteback));
                rlcredit.setBackground(getResources().getDrawable(R.drawable.cornerwhiteback));

                ivpaypal.setImageDrawable(getResources().getDrawable(R.drawable.ic_active_paypal_ico));
                ivvenmo.setImageDrawable(getResources().getDrawable(R.drawable.ic_nvenmo_ico));
                ivcredit.setImageDrawable(getResources().getDrawable(R.drawable.ic_visa));

                tvpaypal.setTextColor(getResources().getColor(R.color.colorwhite));
                tvvenmo.setTextColor(getResources().getColor(R.color.colorDarkgray));
                tvcredit.setTextColor(getResources().getColor(R.color.colorDarkgray));
            }
            break;

            case R.id.rl_venemo: {
                ptype = "paypal";
                rlpaypal.setBackground(getResources().getDrawable(R.drawable.cornerwhiteback));
                rlvenmo.setBackground(getResources().getDrawable(R.drawable.cornergrayback));
                rlcredit.setBackground(getResources().getDrawable(R.drawable.cornerwhiteback));

                ivpaypal.setImageDrawable(getResources().getDrawable(R.drawable.ic_paypal_ico));
                ivvenmo.setImageDrawable(getResources().getDrawable(R.drawable.ic_active_nvenmo_ico));
                ivcredit.setImageDrawable(getResources().getDrawable(R.drawable.ic_visa));

                tvpaypal.setTextColor(getResources().getColor(R.color.colorDarkgray));
                tvvenmo.setTextColor(getResources().getColor(R.color.colorwhite));
                tvcredit.setTextColor(getResources().getColor(R.color.colorDarkgray));

            }
            break;

            case R.id.rl_credit: {
                ptype = "paypal";
                rlpaypal.setBackground(getResources().getDrawable(R.drawable.cornerwhiteback));
                rlvenmo.setBackground(getResources().getDrawable(R.drawable.cornerwhiteback));
                rlcredit.setBackground(getResources().getDrawable(R.drawable.cornergrayback));

                ivpaypal.setImageDrawable(getResources().getDrawable(R.drawable.ic_paypal_ico));
                ivvenmo.setImageDrawable(getResources().getDrawable(R.drawable.ic_nvenmo_ico));
                ivcredit.setImageDrawable(getResources().getDrawable(R.drawable.ic_visa));

                tvpaypal.setTextColor(getResources().getColor(R.color.colorDarkgray));
                tvvenmo.setTextColor(getResources().getColor(R.color.colorDarkgray));
                tvcredit.setTextColor(getResources().getColor(R.color.colorwhite));
            }
            break;


            case R.id.payment_address: {
                getCurrentPlaceItems();
            }
            break;
            case R.id.skip: {
                Intent intent = new Intent(PaymentActivity.this, OwnerTabActivity.class);
                startActivity(intent);
                finish();
            }

            break;
            case R.id.btn_paymentmethod: {
                if (utility.checkInternetConnection(this)) {
                    if (validation.isaddNameValid(etAddress) && validation.isptypeValid(ptype)) {
                        paymentApiData();

                    }
                } else {
                    Toast.makeText(this, "Please check your Network", Toast.LENGTH_SHORT).show();
                }


            }
            break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == PLACE_PICKER_REQ_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                //  add.setText(place.getName());
                Geocoder geocoder = new Geocoder(PaymentActivity.this);
                try {
                    List<Address> addresses = geocoder.getFromLocation(place.getLatLng().latitude, place.getLatLng().longitude, 1);
                    @SuppressLint({"NewApi", "LocalSuppress"}) String address = Objects.requireNonNull(place.getAddress()).toString();
//                    String city = addresses.get(0).getLocality();
                    /* pincode = addresses.get(0).getPostalCode();
                     */
                    latitude = String.valueOf(addresses.get(0).getLatitude());
                    longitude = String.valueOf(addresses.get(0).getLongitude());
//                    //String country = addresses.get(0).getAddressLine(2);
                    etAddress.setText(address);
                    Log.e("", String.valueOf(addresses));

                } catch (IOException e) {

                    e.printStackTrace();
                }
            }
        }


    }


    private void getCurrentPlaceItems() {
        if (isLocationAccessPermitted()) {
            showPlacePicker();
        } else {
            requestLocationAccessPermission();
        }
    }

    @SuppressLint({"MissingPermission", "NewApi"})
    private void showPlacePicker() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(Objects.requireNonNull(PaymentActivity.this)), PLACE_PICKER_REQ_CODE);
        } catch (Exception e) {
            Log.e("tag", Arrays.toString(e.getStackTrace()));
        }
    }

    @SuppressLint("NewApi")
    private boolean isLocationAccessPermitted() {
        return ContextCompat.checkSelfPermission(Objects.requireNonNull(PaymentActivity.this),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    @SuppressLint("NewApi")
    private void requestLocationAccessPermission() {
        ActivityCompat.requestPermissions(Objects.requireNonNull(PaymentActivity.this),
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                LOC_REQ_CODE);
    }

    public void paymentApiData() {

        String authtoken = session.getAuthtoken();
        String address = etAddress.getText().toString();

        MediaType text = MediaType.parse("text/plain");

        RequestBody address1 = RequestBody.create(text, address);
        RequestBody latitude1 = RequestBody.create(text, latitude);
        RequestBody longitude1 = RequestBody.create(text, longitude);
        RequestBody ptype1 = RequestBody.create(text, ptype);
        RequestBody num = RequestBody.create(text, "");

        MultipartBody.Part fileToUpload1 = null;


        if (file != null) {
            RequestBody mFile1 = RequestBody.create(MediaType.parse("image/*"), file);
            fileToUpload1 = MultipartBody.Part.createFormData("profileImage", file.getName(), mFile1);

        }

        pDialog.pdialog(this);
        Call<ResponseBody> call = RetrofitClient.getInstance()
                .getApi().updateDoc(authtoken,num, address1,latitude1,longitude1,ptype1,fileToUpload1);
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

                                Intent intent = new Intent(PaymentActivity.this, OwnerTabActivity.class);
                                startActivity(intent);
                                finish();


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
                                Toast.makeText(PaymentActivity.this, msg, Toast.LENGTH_SHORT).show();

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

