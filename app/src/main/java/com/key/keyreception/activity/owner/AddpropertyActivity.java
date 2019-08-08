package com.key.keyreception.activity.owner;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.key.keyreception.BuildConfig;
import com.key.keyreception.R;
import com.key.keyreception.Session;
import com.key.keyreception.activity.ActivityAdapter.UploadImageAdapter;
import com.key.keyreception.activity.model.UploadImageModal;
import com.key.keyreception.base.BaseActivity;
import com.key.keyreception.connection.RetrofitClient;
import com.key.keyreception.helper.AppConstants;
import com.key.keyreception.helper.Constant;
import com.key.keyreception.helper.PDialog;
import com.key.keyreception.helper.Utility;
import com.key.keyreception.helper.Validation;
import com.key.keyreception.helper.croper.CropImage;
import com.key.keyreception.helper.croper.CropImageView;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

import static com.key.keyreception.helper.Constant.PLACE_AUTOCOMPLETE_REQUEST_CODE;

public class AddpropertyActivity extends BaseActivity implements View.OnClickListener {

    private static final int LOC_REQ_CODE = 3;
    private static final int PLACE_PICKER_REQ_CODE = 2;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private String mCurrentPhotoPath = "";
    private UploadImageAdapter uploadImageAdapter;
    private PDialog pDialog;
    private Session session;
    private String propid = "";
    private String uploadimage = "0";
    private File file;
    private EditText etaddress, etpropname, etpropsize;
    private Spinner spinnerbed, spinnerbath;
    private TextView ftsBedroomSelector, ftsBathroomSelector;
    private Boolean isFTSSelected = false;
    private Boolean isFTSSelected1 = false;
    private String latitude, longitude;
    private String bath = "", bed = "", bathroom, bedroom, delpropid, delimageid;
    private Utility utility;
    private Validation validation;
    private ArrayList<UploadImageModal> uploadImageModalArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addproperty);
        pDialog = new PDialog();
        utility = new Utility();
        validation = new Validation(this);
        session = new Session(this);
        inItView();

    }

    private void inItView() {

        uploadImageModalArrayList = new ArrayList<>();
        RecyclerView addprop_recycler_view = findViewById(R.id.addprop_recycler_view);
        RelativeLayout rtl_upload_photo = findViewById(R.id.rtl_upload_photo);
        RelativeLayout rl_bathroom = findViewById(R.id.rl_bathroom);
        RelativeLayout rl_bedroom = findViewById(R.id.rl_bedroom);
        spinnerbath = findViewById(R.id.addprop_Spinner_bathroom);
        spinnerbed = findViewById(R.id.addprop_Spinner_bedroom);
        ftsBedroomSelector = findViewById(R.id.addprop_selectbed);
        ftsBathroomSelector = findViewById(R.id.addprop_selectbath);
        etaddress = findViewById(R.id.Addprop_address);
        etpropname = findViewById(R.id.Addprop_name);
        etpropsize = findViewById(R.id.Addprop_size);
        Button btnaddprop = findViewById(R.id.btnaddprop);
        ImageView iv_leftarrow_addprop = findViewById(R.id.iv_leftarrow_addprop);
        btnaddprop.setOnClickListener(this);
        etaddress.setOnClickListener(this);
        rl_bedroom.setOnClickListener(this);
        rl_bathroom.setOnClickListener(this);
        iv_leftarrow_addprop.setOnClickListener(this);


        rtl_upload_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (uploadImageModalArrayList.size() <= 4) {
                    gallerycameramethod();
                } else {

                    alertImageSizeOption();
                }
            }
        });

        uploadImageAdapter = new UploadImageAdapter(AddpropertyActivity.this, uploadImageModalArrayList, new UploadImageAdapter.Deletelist() {
            @Override
            public void deletpos(int i) {

                delimageid = uploadImageModalArrayList.get(i).imageid;
                delpropid = uploadImageModalArrayList.get(i).propertyId;
                deleteImageApiData(delimageid, delpropid);
                uploadImageModalArrayList.remove(i);


            }
        }, new UploadImageAdapter.DeleteProperty() {
            @Override
            public void deletproperty(int i) {
                String delpropid1 = uploadImageModalArrayList.get(i).propertyId;
                String delimageid1 = uploadImageModalArrayList.get(i).imageid;

                alertDeleteProperty(delimageid1, delpropid1, i);

            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(AddpropertyActivity.this, LinearLayoutManager.HORIZONTAL, false);
        addprop_recycler_view.setLayoutManager(mLayoutManager);
        addprop_recycler_view.setAdapter(uploadImageAdapter);
        SpinnerListener();

    }

    @SuppressLint("NewApi")
    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.addprop_selectbed: {
                closeKeyboard();
                spinnerbed.dispatchTouchEvent(MotionEvent.obtain(0, 0, MotionEvent.ACTION_DOWN, 100, 100, 0.5f, 5, 0, 1, 1, 0, 0));


            }
            case R.id.rl_bathroom: {
                closeKeyboard();
            }
            break;
            case R.id.rl_bedroom: {
                closeKeyboard();
            }
            break;
            case R.id.addprop_selectbath: {

                closeKeyboard();
                spinnerbath.dispatchTouchEvent(MotionEvent.obtain(0, 0, MotionEvent.ACTION_DOWN, 100, 100, 0.5f, 5, 0, 1, 1, 0, 0));

            }
            break;
            case R.id.btnaddprop: {
                if (utility.checkInternetConnection(this)) {
                    if (validation.iscoNameValid(etpropname) && validation.isaddNameValid(etaddress) && validation.ispropsizeValid(etpropsize) && validation.isbedroomValid(bed) && validation.isbathroomValid(bath) && validation.isimagefileValid(file)) {

                        if (uploadImageModalArrayList.size() != 0) {
                            addpropertyApiData();
                        } else {
                            Toast.makeText(this, "Minimum one image Require", Toast.LENGTH_SHORT).show();
                        }

                    }
                } else {
                    Toast.makeText(this, "Please check your network", Toast.LENGTH_SHORT).show();
                }
            }

            break;

            case R.id.Addprop_address: {
                Utility.autoCompletePlacePicker(this);
            }
            break;

            case R.id.iv_leftarrow_addprop: {
                if (getIntent().getStringExtra("intentid").equals("100")) {
                    Intent intent = new Intent(AddpropertyActivity.this, CreatePostActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    backPress();
                }
            }
            break;
        }

    }

    public void gallerycameramethod() {
        final boolean result = Utility.checkPermission(AddpropertyActivity.this);

        final CharSequence[] items = {"Camera", "Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(AddpropertyActivity.this);
        builder.setTitle("Add Image");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (result) {
                    if (items[i].equals("Camera")) {
                        if (Utility.checkCameraPermission(AddpropertyActivity.this)) {
                            dispatchTakePictureIntent();
                        }
                    } else if (items[i].equals("Gallery")) {
                        callIntent(AppConstants.RESULT_LOAD);
                    } else if (items[i].equals("Cancel")) {
                        dialogInterface.dismiss();
                    }
                }
            }
        });

        builder.show();
    }


    public void callIntent(int caseId) {

        switch (caseId) {
            case AppConstants.INTENT_CAMERA:
                try {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.setAction(Intent.ACTION_PICK);
                    startActivityForResult(intent, AppConstants.REQUEST_CAMERA);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case AppConstants.RESULT_LOAD:
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                photoPickerIntent.setAction(Intent.ACTION_PICK);
                startActivityForResult(photoPickerIntent, AppConstants.RESULT_LOAD);
                break;

            case AppConstants.REQUEST_CAMERA:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                            AppConstants.MY_PERMISSIONS_REQUEST_CAMERA);
                }
                break;

            case AppConstants.MY_PERMISSIONS_REQUEST_EXTERNAL:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            AppConstants.MY_PERMISSIONS_REQUEST_EXTERNAL);
                }
                break;
        }
    }
    // New Code
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        BuildConfig.APPLICATION_ID + ".fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, AppConstants.REQUEST_CAMERA);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        @SuppressLint("SimpleDateFormat")
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //*********Autocomplete place p[icker****************//
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                assert data != null;
                Place place = PlaceAutocomplete.getPlace(this, data);
                etaddress.setText(place.getAddress());
                etaddress.setTextColor(ContextCompat.getColor(this, R.color.colorgray));
                latitude = String.valueOf(place.getLatLng().latitude);
                longitude = String.valueOf(place.getLatLng().longitude);
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
            } else if (resultCode == RESULT_CANCELED) {
                Constant.hideSoftKeyBoard(AddpropertyActivity.this, etaddress);
            }
        }
        if (PLACE_PICKER_REQ_CODE == 2) {
            if (requestCode == PLACE_PICKER_REQ_CODE) {
                if (resultCode == RESULT_OK) {
                    Place place = PlacePicker.getPlace(this, data);
                    Geocoder geocoder = new Geocoder(AddpropertyActivity.this);
                    try {
                        List<Address> addresses = geocoder.getFromLocation(place.getLatLng().latitude, place.getLatLng().longitude, 1);
                        @SuppressLint({"NewApi", "LocalSuppress"}) String address = Objects.requireNonNull(place.getAddress()).toString();
                        latitude = String.valueOf(addresses.get(0).getLatitude());
                        longitude = String.valueOf(addresses.get(0).getLongitude());
                        etaddress.setText(address);

                    } catch (IOException e) {

                        e.printStackTrace();
                    }
                }
            }
        }


        if (resultCode != 0) {
            switch (requestCode) {
                case AppConstants.RESULT_LOAD:
                    Uri temPhoto = data.getData();
                    if (temPhoto != null) {
                        CropImage.activity(temPhoto).setCropShape(CropImageView.CropShape.RECTANGLE)
                                .setAspectRatio(4, 4).start(this);
                    } else {
                        Toast.makeText(AddpropertyActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                    break;

                case AppConstants.REQUEST_CAMERA:
                    temPhoto = Uri.fromFile(new File(mCurrentPhotoPath));
                    if (temPhoto != null) {
                        CropImage.activity(temPhoto).setCropShape(CropImageView.CropShape.RECTANGLE)
                                .setAspectRatio(4, 4).start(AddpropertyActivity.this);
                    } else {
                        Toast.makeText(AddpropertyActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:// Image Cropper
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    try {
                        if (result != null) {
                            temPhoto = result.getUri();
                            file = new File(temPhoto.getPath());
                            uploadImageApiData();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(AddpropertyActivity.this, getResources().getString(R.string.alertImageException), Toast.LENGTH_SHORT).show();
                    } catch (OutOfMemoryError error) {
                        Toast.makeText(AddpropertyActivity.this, getResources().getString(R.string.alertOutOfMemory), Toast.LENGTH_SHORT).show();
                    }
                    break;

                default:
                    super.onActivityResult(requestCode, resultCode, data);
                    break;
            }
        }
    }


    public void uploadImageApiData() {
        pDialog.pdialog(AddpropertyActivity.this);
        String authtoken = session.getAuthtoken();

        String isImageAdd;
        if (propid.equals("")) {
            isImageAdd = "0";
        } else {
            isImageAdd = "1";
        }


        MediaType text = MediaType.parse("text/plain");
        RequestBody propid1 = RequestBody.create(text, propid);
        RequestBody isImageAdd1 = RequestBody.create(text, isImageAdd);

        MultipartBody.Part fileToUpload1 = null;
        if (file != null) {
            RequestBody mFile1 = RequestBody.create(MediaType.parse("image/*"), file);
            fileToUpload1 = MultipartBody.Part.createFormData("propertyImg", file.getName(), mFile1);
            }

        Call<ResponseBody> call = RetrofitClient.getInstance()
                .getApi().addPropertyImage(authtoken, isImageAdd1, propid1, fileToUpload1);
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
                            if (statusCode.equals("success")) {
                                JSONObject jsonObject1 = jsonObject.getJSONObject("data");

                                uploadimage = "1";
                                UploadImageModal uploadImageModal = new UploadImageModal();
                                propid = String.valueOf(jsonObject1.getInt("propertyId"));
                                uploadImageModal.propertyId = String.valueOf(jsonObject1.getInt("propertyId"));
                                uploadImageModal.imageid = String.valueOf(jsonObject1.getInt("_id"));
                                uploadImageModal.propertyImage = jsonObject1.getString("propertyImage");
                                uploadImageModalArrayList.add(uploadImageModal);
                                session.putpropertyId(uploadImageModal);

                            }
                            uploadImageAdapter.notifyDataSetChanged();
                            break;

                        }
                        case 400: {
                            String result = Objects.requireNonNull(response.errorBody()).string();
                            Log.d("response400", result);
                            JSONObject jsonObject = new JSONObject(result);
                            String statusCode = jsonObject.optString("status");
                            String msg = jsonObject.optString("message");
                            if (statusCode.equals("true")) {
                                Toast.makeText(AddpropertyActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }
                            break;
                        }
                        case 401:
                            try {

                                session.logout();
                                Toast.makeText(AddpropertyActivity.this, "Session expired, please login again.", Toast.LENGTH_SHORT).show();

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

    public void SpinnerListener() {

        spinnerbath.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                bathroom = adapterView.getItemAtPosition(i).toString();
                closeKeyboard();

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

        spinnerbath.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    closeKeyboard();

                    v.performClick();
                    bathroom = spinnerbath.getSelectedItem().toString();
                    if (isFTSSelected1) {
                        bath = "1";
                        ftsBathroomSelector.setVisibility(View.GONE);
                        bathroom = spinnerbath.getSelectedItem().toString();
                    }
                }
                return true;
            }
        });

        spinnerbed.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                bedroom = adapterView.getItemAtPosition(i).toString();
                closeKeyboard();

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

        spinnerbed.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.performClick();
                    closeKeyboard();

                    bedroom = spinnerbed.getSelectedItem().toString();
                    if (isFTSSelected1) {
                        bed = "1";
                        ftsBedroomSelector.setVisibility(View.GONE);
                        bedroom = spinnerbed.getSelectedItem().toString();
                    }
                }
                return true;
            }
        });


    }

    public void addpropertyApiData() {
        pDialog.pdialog(AddpropertyActivity.this);
        String authtoken = session.getAuthtoken();
        String name = etpropname.getText().toString();
        String size = etpropsize.getText().toString();
        String add = etaddress.getText().toString();
        Call<ResponseBody> call = RetrofitClient.getInstance()
                .getApi().addProperty(authtoken, uploadimage, name, bedroom, bathroom, add, latitude, longitude, size, propid);
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
                                if (getIntent() != null && getIntent().hasExtra("intentid")) {
                                    if (getIntent().getStringExtra("intentid").equals("100")) {
                                        Toast.makeText(AddpropertyActivity.this, msg, Toast.LENGTH_SHORT).show();
                                        onBackPressed();
                                    } else {
                                        Toast.makeText(AddpropertyActivity.this, msg, Toast.LENGTH_SHORT).show();
                                        setResult(Activity.RESULT_OK);
                                        finish();
                                        }
                                }

                            } else {
                                Toast.makeText(AddpropertyActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }

                            uploadImageAdapter.notifyDataSetChanged();
                            break;

                        }
                        case 400: {
                            String result = Objects.requireNonNull(response.errorBody()).string();
                            Log.d("response400", result);
                            JSONObject jsonObject = new JSONObject(result);
                            String statusCode = jsonObject.optString("status");
                            String msg = jsonObject.optString("message");
                            if (statusCode.equals("true")) {
                                Toast.makeText(AddpropertyActivity.this, msg, Toast.LENGTH_SHORT).show();
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


    public void deleteImageApiData(String imageid, String pid) {
        pDialog.pdialog(AddpropertyActivity.this);
        String authtoken = session.getAuthtoken();

        Call<ResponseBody> call = RetrofitClient.getInstance()
                .getApi().deletePropertyImage(authtoken, pid, imageid);
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
                                Toast.makeText(AddpropertyActivity.this, msg, Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(AddpropertyActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }

                            uploadImageAdapter.notifyDataSetChanged();
                            break;

                        }
                        case 400: {
                            String result = Objects.requireNonNull(response.errorBody()).string();
                            Log.d("response400", result);
                            JSONObject jsonObject = new JSONObject(result);
                            String statusCode = jsonObject.optString("status");
                            String msg = jsonObject.optString("message");
                            if (statusCode.equals("true")) {
                                Toast.makeText(AddpropertyActivity.this, msg, Toast.LENGTH_SHORT).show();
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





    public void alertImageSizeOption() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddpropertyActivity.this);
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("You can't upload more than five image.");
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        if (getIntent() != null && getIntent().hasExtra("intentid")) {
            if (getIntent().getStringExtra("intentid").equals("100")) {
                finish();
            } else {
                setResult(Activity.RESULT_OK);
                finish();

            }
        }
    }

    public void alertDeleteProperty(final String imgid, final String propid, final int i) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddpropertyActivity.this);

        alertDialog.setTitle("Alert");
        alertDialog.setMessage("The property will be deleted, do you want to proceed");
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public void onClick(DialogInterface dialog, int which) {
                deleteImageApiData(imgid, propid);
                propertyDeleteApiData(propid);
                uploadImageModalArrayList.remove(i);
                dialog.dismiss();
            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constant.MY_PERMISSIONS_REQUEST_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {
                Toast.makeText(this, "Need read gallery or camera permission for pick or take image.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void propertyDeleteApiData(String pid) {
        String authtoken = session.getAuthtoken();
        Call<ResponseBody> call = RetrofitClient.getInstance()
                .getApi().deleteProperty(authtoken, pid);
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
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
                                Log.e("msg", msg);
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
                                Toast.makeText(AddpropertyActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }
                            break;
                        }
                        case 401:
                            try {
                                session.logout();
                                Toast.makeText(AddpropertyActivity.this, "Session expired, please login again.", Toast.LENGTH_SHORT).show();

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
            }
        });

    }

}
