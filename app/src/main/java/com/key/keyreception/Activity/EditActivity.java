package com.key.keyreception.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
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

public class EditActivity extends BaseActivity implements View.OnClickListener {

    private static final int LOC_REQ_CODE = 3;
    private static final int PLACE_PICKER_REQ_CODE = 2;
    Session session;
    String img;
    Bitmap bm;
    File file;
    ImageView profile;
    private EditText etname, etaddress;
    private Validation validation;
    private Utility utility;
    private String usertype, latitude = "", longitude = "";
    private PDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        validation = new Validation(this);
        session = new Session(EditActivity.this);
        pDialog = new PDialog();
        utility = new Utility();
        init();
    }

    public void gallerycameramethod() {
        final boolean result = Utility.checkPermission(EditActivity.this);

        final CharSequence[] items = {"Camera", "Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
        builder.setTitle("Add Image");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (result) {
                    if (items[i].equals("Camera")) {
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, 1);

                    } else if (items[i].equals("Gallery")) {

                        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(galleryIntent, 1);


                    } else if (items[i].equals("Cancel")) {
                        dialogInterface.dismiss();
                    }
                }
            }
        });

        builder.show();

    }

    private void init() {
        etname = findViewById(R.id.et_editname);
        etaddress = findViewById(R.id.et_editAddress);
        profile = findViewById(R.id.iv_edit_profile);
        ImageView ivback = findViewById(R.id.iv_editleftarrow);
        Button btnsingup = findViewById(R.id.btn_edit);
        btnsingup.setOnClickListener(this);
        ivback.setOnClickListener(this);
        profile.setOnClickListener(this);
        etaddress.setOnClickListener(this);
        dataintent();
    }

    @SuppressLint("NewApi")
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_edit: {
                if (utility.checkInternetConnection(this)) {
                    if (validation.isFirstNameValid(etname) && validation.isaddNameValid(etaddress)) {
                        editProfileData();

                    }
                } else {
                    Toast.makeText(this, "Please check your network", Toast.LENGTH_SHORT).show();
                }
            }
            break;

            case R.id.iv_edit_profile: {
                gallerycameramethod();
            }
            break;
            case R.id.iv_editleftarrow: {
                onBackPressed();
            }
            break;
            case R.id.et_editAddress: {
                getCurrentPlaceItems();
            }
            break;
        }
    }

    public void editProfileData() {


        pDialog.pdialog(EditActivity.this);

        String name = etname.getText().toString().trim();
        String add = etaddress.getText().toString().trim();


        usertype = session.getusertype();
        String token = session.getAuthtoken();
        //all text in multipart
        MediaType text = MediaType.parse("text/plain");
        RequestBody name1 = RequestBody.create(text, name);
        RequestBody add1 = RequestBody.create(text, add);
        RequestBody latitude1 = RequestBody.create(text, latitude);
        RequestBody longitude1 = RequestBody.create(text, longitude);


        //image multipart

        MultipartBody.Part fileToUpload1 = null;
        if (file != null) {
            RequestBody mFile1 = RequestBody.create(MediaType.parse("image/*"), file);
            fileToUpload1 = MultipartBody.Part.createFormData("profileImage", file.getName(), mFile1);
        }

        Call<ResponseBody> call = RetrofitClient.getInstance()
                .getApi().updateProfile(token, name1, add1, latitude1, longitude1, fileToUpload1);
//
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


                                if (usertype.equals("owner")) {
                                    Intent intent = new Intent(EditActivity.this, OwnerTabActivity.class);
                                    intent.putExtra("order", "3");
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                    Toast.makeText(EditActivity.this, msg, Toast.LENGTH_SHORT).show();

                                } else {
                                    Intent intent = new Intent(EditActivity.this, TabActivity.class);
                                    intent.putExtra("order", "3");
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                    Toast.makeText(EditActivity.this, msg, Toast.LENGTH_SHORT).show();


                                }

                            } else {
                                Toast.makeText(EditActivity.this, msg, Toast.LENGTH_SHORT).show();

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
                                Toast.makeText(EditActivity.this, msg, Toast.LENGTH_SHORT).show();

                            }

                            break;
                        }
                        case 401:

                            try {

                                session.logout();
                                Toast.makeText(EditActivity.this, "Session expired, please login again.", Toast.LENGTH_SHORT).show();

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
    protected void onActivityResult(int requestCode,
                                    int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 1:
                if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
                    Uri filePath = data.getData();
                    ExifInterface exif;

                    try {
                        //Getting the Bitmap from Gallery
                        bm = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                        Uri selectedPicture = data.getData();
                        // Get and resize profile image
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        Cursor cursor = getContentResolver().query(selectedPicture, filePathColumn, null, null, null);
                        cursor.moveToFirst();

                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String picturePath = cursor.getString(columnIndex);
                        cursor.close();

                        file = new File(picturePath);
                        exif = new ExifInterface(picturePath);
                        //Setting the Bitmap to ImageView
                        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                                ExifInterface.ORIENTATION_UNDEFINED);
                        Bitmap bmRotated = Utility.rotateBitmap(bm, orientation);
                        profile.setImageBitmap(bmRotated);
                        img = utility.saveImage(bmRotated, EditActivity.this);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (requestCode == 1 && resultCode != RESULT_CANCELED) {
                    try {
                        bm = (Bitmap) data.getExtras().get("data");
                        profile.setImageBitmap(bm);
                        img = utility.saveImage(bm, EditActivity.this);
                        Uri tempUri6 = utility.getImageUri(bm, EditActivity.this);
                        // CALL THIS METHOD TO GET THE ACTUAL PATH
                        file = new File(utility.getRealPathFromURI(tempUri6, EditActivity.this));


                    } catch (Exception ignored) {
                    }


                }
                //if resultCode Case 1
                break;
            case 2:


                if (requestCode == PLACE_PICKER_REQ_CODE) {
                    if (resultCode == RESULT_OK) {
                        Place place = PlacePicker.getPlace(this, data);
                        //  add.setText(place.getName());
                        Geocoder geocoder = new Geocoder(EditActivity.this);
                        try {
                            List<Address> addresses = geocoder.getFromLocation(place.getLatLng().latitude, place.getLatLng().longitude, 1);
                            @SuppressLint({"NewApi", "LocalSuppress"}) String address = Objects.requireNonNull(place.getAddress()).toString();
//                            String city = addresses.get(0).getLocality();
                            latitude = String.valueOf(addresses.get(0).getLatitude());
                            longitude = String.valueOf(addresses.get(0).getLongitude());
                            /* pincode = addresses.get(0).getPostalCode();
                             */
                            //String country = addresses.get(0).getAddressLine(2);
                            etaddress.setText(address);

                        } catch (IOException e) {

                            e.printStackTrace();
                        }
                    }
                }


        }


    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
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
            startActivityForResult(builder.build(Objects.requireNonNull(EditActivity.this)), PLACE_PICKER_REQ_CODE);
        } catch (Exception e) {
            Log.e("tag", Arrays.toString(e.getStackTrace()));
        }
    }

    @SuppressLint("NewApi")
    private boolean isLocationAccessPermitted() {
        return ContextCompat.checkSelfPermission(Objects.requireNonNull(EditActivity.this),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    @SuppressLint("NewApi")
    private void requestLocationAccessPermission() {
        ActivityCompat.requestPermissions(Objects.requireNonNull(EditActivity.this),
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                LOC_REQ_CODE);
    }

    public void dataintent() {
        Intent intent = getIntent();
        String fname = intent.getStringExtra("fullName");
        String address = intent.getStringExtra("address");
        String profileImage = intent.getStringExtra("profileImage");
        latitude = intent.getStringExtra("latitude");
        longitude = intent.getStringExtra("longitude");

        etname.setText(fname);
        etaddress.setText(address);

        if (profileImage != null) {
            if (profileImage.length() != 0) {
                RequestOptions options = new RequestOptions();
                options.placeholder(R.drawable.ic_user_ico);
                options.diskCacheStrategy(DiskCacheStrategy.RESOURCE);
                Glide.with(EditActivity.this).load(profileImage).apply(options).into(profile);

            }
        }
    }
}



