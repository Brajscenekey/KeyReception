package com.key.keyreception.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.location.Address;
import android.location.Geocoder;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
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
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.key.keyreception.R;
import com.key.keyreception.Session;
import com.key.keyreception.activity.model.FirebaseUserModel;
import com.key.keyreception.activity.owner.OwnerTabActivity;
import com.key.keyreception.activity.recepnist.TabActivity;
import com.key.keyreception.base.BaseActivity;
import com.key.keyreception.connection.RetrofitClient;
import com.key.keyreception.helper.Constant;
import com.key.keyreception.helper.PDialog;
import com.key.keyreception.helper.Utility;
import com.key.keyreception.helper.Validation;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

import static com.key.keyreception.helper.Constant.PLACE_AUTOCOMPLETE_REQUEST_CODE;

public class EditActivity extends BaseActivity implements View.OnClickListener {

    private static final int LOC_REQ_CODE = 3;
    private static final int PLACE_PICKER_REQ_CODE = 2;
    private Session session;
    private String img;
    private Bitmap bm;
    private File file;
    private ImageView profile;
    private EditText etname, etaddress, et_editmail;
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
                        utility.dispatchTakePictureIntent(EditActivity.this);
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
        et_editmail = findViewById(R.id.et_editmail);
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
                if (Utility.checkCameraPermission(EditActivity.this)) {
                    gallerycameramethod();
                }
            }
            break;
            case R.id.iv_editleftarrow: {
                onBackPressed();
            }
            break;
            case R.id.et_editAddress: {
                Utility.autoCompletePlacePicker(this);
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
        String logid = session.getuserid();
        String notstatus = session.getnotificationStatus();
        MediaType text = MediaType.parse("text/plain");
        RequestBody name1 = RequestBody.create(text, name);
        RequestBody add1 = RequestBody.create(text, add);
        RequestBody latitude1 = RequestBody.create(text, latitude);
        RequestBody longitude1 = RequestBody.create(text, longitude);
        RequestBody logid1 = RequestBody.create(text, logid);
        RequestBody notstatus1 = RequestBody.create(text, notstatus);
        MultipartBody.Part fileToUpload1 = null;
        if (file != null) {
            RequestBody mFile1 = RequestBody.create(MediaType.parse("image/*"), file);
            fileToUpload1 = MultipartBody.Part.createFormData("profileImage", file.getName(), mFile1);
        }

        Call<ResponseBody> call = RetrofitClient.getInstance()
                .getApi().updateProfile(token, name1, add1, latitude1, longitude1, logid1, notstatus1, fileToUpload1);
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
                                String authToken = jsonObject2.getString("authToken");
                                String userType1 = jsonObject2.getString("userType");
                                String fullName = jsonObject2.getString("fullName");
                                String email = jsonObject2.getString("email");

                                String profileImage = jsonObject2.getString("profileImage");
                                String _id = String.valueOf(jsonObject2.getInt("_id"));
                                int notificationStatus = jsonObject2.getInt("notificationStatus");
                                String paymentType = jsonObject2.getString("paymentType");
                                session.putEmailId(email);

                                session.putPayment(paymentType);
                                session.putAuthtoken(authToken);
                                session.putusertype(userType1);
                                session.putuserdata(_id, fullName, profileImage, userType1);
                                session.putnotificationStatus(String.valueOf(notificationStatus));
                                addUserFirebaseDatabase(fullName, email, profileImage, userType1, authToken, _id);

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
                if (resultCode == RESULT_OK && data != null && data.getData() != null) {
                    Uri filePath = data.getData();
                    ExifInterface exif;

                    try {
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
                        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                                ExifInterface.ORIENTATION_UNDEFINED);
                        Bitmap bmRotated = Utility.rotateBitmap(bm, orientation);
                        profile.setImageBitmap(bmRotated);
                        img = utility.saveImage(bmRotated, EditActivity.this);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (resultCode != RESULT_CANCELED) {
                    try {
                        Uri uri1 = Uri.fromFile(new File(Utility.mCurrentPhotoPath));
                        file = new File(utility.getRealPathFromURI(uri1, EditActivity.this));
                        bm = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri1);
                        Matrix matrix = new Matrix();
                        matrix.postRotate(90);
                        Bitmap rotated = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(),
                                matrix, true);
                        profile.setImageBitmap(rotated);
                    } catch (Exception ignored) {
                        ignored.printStackTrace();
                        Log.e("error", ignored + "");
                    }
                }
                break;
            case 2:


                if (resultCode == RESULT_OK) {
                    Place place = PlacePicker.getPlace(this, data);
                    Geocoder geocoder = new Geocoder(EditActivity.this);
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

                break;
            case PLACE_AUTOCOMPLETE_REQUEST_CODE:

                //*********Autocomplete place p[icker****************//
                if (resultCode == RESULT_OK) {

                    assert data != null;
                    Place place = PlaceAutocomplete.getPlace(this, data);
                    etaddress.setText(place.getAddress());
                    etaddress.setTextColor(ContextCompat.getColor(this, R.color.colorgray));
                    latitude = String.valueOf(place.getLatLng().latitude);
                    longitude = String.valueOf(place.getLatLng().longitude);
                } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                    Status status1 = PlaceAutocomplete.getStatus(this, data);
                } else if (resultCode == RESULT_CANCELED) {
                    Constant.hideSoftKeyBoard(EditActivity.this, etaddress);
                }


        }


    }

    @SuppressLint("CheckResult")
    public void dataintent() {
        Intent intent = getIntent();
        String fname = intent.getStringExtra("fullName");
        String address = intent.getStringExtra("address");
        String mail = intent.getStringExtra("mail");

        String profileImage = intent.getStringExtra("profileImage");
        latitude = intent.getStringExtra("latitude");
        longitude = intent.getStringExtra("longitude");

        etname.setText(fname);
        etaddress.setText(address);
        et_editmail.setText(mail);

        if (profileImage != null) {
            if (profileImage.length() != 0) {
                RequestOptions options = new RequestOptions();
                options.placeholder(R.drawable.ic_user_ico);
                options.diskCacheStrategy(DiskCacheStrategy.RESOURCE);
                Glide.with(EditActivity.this).load(profileImage).apply(options).into(profile);

            }
        }
    }

    //firebase Realtime database code
    private void addUserFirebaseDatabase(String name, String email, String pimg, String usertype, String authT, String uId) {


        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        String device_token = FirebaseInstanceId.getInstance().getToken();
        FirebaseUserModel userModel = new FirebaseUserModel();
        userModel.firebaseToken = device_token;
        userModel.name = name;
        userModel.email = email;
        // userModel.authtoken =authT;
        userModel.profilePic = pimg;
        userModel.usertype = usertype;
        // userModel.timeStamp = ServerValue.TIMESTAMP;
        userModel.uid = uId;

        database.child(Constant.USER_TABLE).child(uId).setValue(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constant.MY_PERMISSIONS_REQUEST_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                gallerycameramethod();
            } else {
                Toast.makeText(this, "Need read gallery or camera permission for pick or take image.", Toast.LENGTH_SHORT).show();
            }
        }
    }


}



