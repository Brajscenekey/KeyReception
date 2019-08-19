package com.key.keyreception.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.key.keyreception.activity.ActivityAdapter.Createjobcat_Adapter;
import com.key.keyreception.activity.model.FirebaseUserModel;
import com.key.keyreception.activity.model.ServiceCategory;
import com.key.keyreception.base.BaseActivity;
import com.key.keyreception.connection.RetrofitClient;
import com.key.keyreception.helper.Constant;
import com.key.keyreception.helper.PDialog;
import com.key.keyreception.helper.Utility;
import com.key.keyreception.helper.Validation;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

import static com.key.keyreception.helper.Constant.PLACE_AUTOCOMPLETE_REQUEST_CODE;

public class SignupActivity extends BaseActivity implements View.OnClickListener {

    private static final int LOC_REQ_CODE = 3;
    private static final int PLACE_PICKER_REQ_CODE = 2;
    private Session session;
    private File file;
    private ImageView profile;
    private EditText etname, etmail, etpass, etaddress, etcat;
    private Validation validation;
    private Utility utility;
    private String latitude = "", longitude = "";
    private String usertype;
    private PDialog pDialog;
    private List<ServiceCategory> categoryList = new ArrayList<>();
    private Createjobcat_Adapter createjobcat_adapter;
    private String categoryid = "", categoryname, pass1 = "";
    private String refreshedToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rece_signup);
        validation = new Validation(this);
        session = new Session(SignupActivity.this);
        pDialog = new PDialog();
        utility = new Utility();
        init();
        serviceCategoryApiData();
        refreshedToken = FirebaseInstanceId.getInstance().getToken();

    }

    public void gallerycameramethod() {
        final CharSequence[] items = {"Camera", "Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
        builder.setTitle("Add Image");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                    if (items[i].equals("Camera")) {
                        utility.dispatchTakePictureIntent(SignupActivity.this);
                    } else if (items[i].equals("Gallery")) {
                        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(galleryIntent, 1);
                    } else if (items[i].equals("Cancel")) {
                        dialogInterface.dismiss();
                    }

            }
        });

        builder.show();

    }

    private void init() {
        etname = findViewById(R.id.rsignet_name);
        etmail = findViewById(R.id.rsignet_email);
        etpass = findViewById(R.id.rsignet_password);
        etaddress = findViewById(R.id.rsignet_address);
        profile = findViewById(R.id.iv_signup_prof);
        ImageView ivback = findViewById(R.id.iv_leftarrow_signup);
        Button btnsingup = findViewById(R.id.rbtn_signup);
        TextView tvsignin = findViewById(R.id.tv_signin);
        etcat = findViewById(R.id.rsigne_category);
        if (session.getusertype().equals("owner")) {
            etcat.setVisibility(View.GONE);
        } else {
            etcat.setVisibility(View.VISIBLE);
        }
        etcat.setOnClickListener(this);
        tvsignin.setOnClickListener(this);
        btnsingup.setOnClickListener(this);
        ivback.setOnClickListener(this);
        profile.setOnClickListener(this);
        etaddress.setOnClickListener(this);
    }

    @SuppressLint("NewApi")
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.rbtn_signup: {
                if (utility.checkInternetConnection(this)) {
                    if (validation.isEmailValid(etmail) && validation.isFirstNameValid(etname) && validation.isPasswordValid(etpass)) {
                        if (session.getusertype().equals("receptionist")) {
                            if (etcat.getText().toString().length() != 0) {
                                RegistrationData();
                            } else {
                                Toast.makeText(this, "Please select category", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            RegistrationData();
                        }
                    }
                } else {
                    Toast.makeText(this, "Please check your network", Toast.LENGTH_SHORT).show();
                }
            }
            break;

            case R.id.rsigne_category: {
                openAuthorizationDialog();
            }
            break;


            case R.id.tv_signin: {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
            case R.id.iv_signup_prof: {
                if (Utility.checkCameraPermission(SignupActivity.this)) {
                    gallerycameramethod();
                }
            }
            break;
            case R.id.iv_leftarrow_signup: {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
            break;
            case R.id.rsignet_address: {
                Utility.autoCompletePlacePicker(this);
            }
            break;
        }
    }

    public void RegistrationData() {


        pDialog.pdialog(SignupActivity.this);

        String name = etname.getText().toString().trim();
        String add = etaddress.getText().toString().trim();
        String mail = etmail.getText().toString().trim();
        String pass = etpass.getText().toString().trim();
        usertype = session.getusertype();
        MediaType text = MediaType.parse("text/plain");
        RequestBody name1 = RequestBody.create(text, name);
        RequestBody add1 = RequestBody.create(text, add);
        RequestBody mail1 = RequestBody.create(text, mail);
        RequestBody pass1 = RequestBody.create(text, pass);
        RequestBody usertype1 = RequestBody.create(text, usertype);
        RequestBody latitude1 = RequestBody.create(text, latitude);
        RequestBody longitude1 = RequestBody.create(text, longitude);
        RequestBody devicetype1 = RequestBody.create(text, "1");
        RequestBody cid = RequestBody.create(text, categoryid);
        RequestBody rtoken = RequestBody.create(text, refreshedToken);
        MultipartBody.Part fileToUpload1 = null;
        if (file != null) {
            RequestBody mFile1 = RequestBody.create(MediaType.parse("image/*"), file);
            fileToUpload1 = MultipartBody.Part.createFormData("profileImage", file.getName(), mFile1);
        }

        Call<ResponseBody> call = RetrofitClient.getInstance()
                .getApi().Signup(name1, mail1, pass1, add1, latitude1, longitude1, devicetype1, usertype1, cid, rtoken, fileToUpload1);
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
                                int notificationStatus = jsonObject2.getInt("notificationStatus");
                                String userType = jsonObject2.getString("userType");
                                String fullName = jsonObject2.getString("fullName");
                                String email = jsonObject2.getString("email");
                                String profileImage = jsonObject2.getString("profileImage");
                                String _id = String.valueOf(jsonObject2.getInt("_id"));
                                String availabilityStatus = jsonObject2.getString("availabilityStatus");
                                session.putAvabilityStatus(availabilityStatus);
                                session.putuserdata(_id, fullName, profileImage, userType);
                                String jobStatus = jsonObject2.getString("jobStatus");
                                session.putJobStatus(jobStatus);
                                session.putAuthtoken(authToken);
                                String paymentType = jsonObject2.getString("paymentType");
                                session.putPayment(paymentType);
                                session.putnotificationStatus(String.valueOf(notificationStatus));
                                session.putEmailId(email);

                                addUserFirebaseDatabase(fullName, email, profileImage, usertype, authToken, _id);

                                if (usertype.equals("owner")) {
                                    session.issetLoggedIn(true);

                                    Toast.makeText(SignupActivity.this, msg, Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(SignupActivity.this, PaymentActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {

                                    session.isSetUpdateLocIn(true);
                                    Toast.makeText(SignupActivity.this, msg, Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(SignupActivity.this, ValidateAccountActivity.class);
                                    startActivity(intent);
                                    finish();
                                }

                            } else {
                                Toast.makeText(SignupActivity.this, msg, Toast.LENGTH_SHORT).show();

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
                                Toast.makeText(SignupActivity.this, msg, Toast.LENGTH_SHORT).show();
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                Bitmap bm;
                if (resultCode == RESULT_OK && data != null && data.getData() != null) {
                    Uri filePath = data.getData();
                    ExifInterface exif = null;
                    try {
                        bm = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                        Uri selectedPicture = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        Cursor cursor = getContentResolver().query(selectedPicture, filePathColumn, null, null, null);
                        assert cursor != null;
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
                        String img = utility.saveImage(bmRotated, SignupActivity.this);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (resultCode != RESULT_CANCELED) {
                    try {
                        Uri uri1 = Uri.fromFile(new File(Utility.mCurrentPhotoPath));
                        bm = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri1);
                        profile.setImageBitmap(bm);
                        file = new File(utility.getRealPathFromURI(uri1, SignupActivity.this));


                    } catch (Exception ignored) {
                    }


                }
                break;
            case 2:


                if (resultCode == RESULT_OK) {
                    Place place = PlacePicker.getPlace(this, data);
                    Geocoder geocoder = new Geocoder(SignupActivity.this);
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
                    Constant.hideSoftKeyBoard(SignupActivity.this, etmail);
                }
                break;


        }


    }

    @SuppressLint("NewApi")
    private void openAuthorizationDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.layout_custom_dialog);

        Button btn_continoue_dialog = dialog.findViewById(R.id.checksub_btn);
        ImageView clereg_logo = dialog.findViewById(R.id.clereg_logo);
        clereg_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        RecyclerView check_recycler_view = dialog.findViewById(R.id.check_recycler_view);
        createjobcat_adapter = new Createjobcat_Adapter(this, categoryList, new Createjobcat_Adapter.checkupdate() {
            @Override
            public void updatecheckcount(List<ServiceCategory> inprodataList) {
                StringBuilder commaSepValueBuilder = new StringBuilder();
                StringBuilder commaSepValueBuilder1 = new StringBuilder();
                for (int i = 0; i < inprodataList.size(); i++) {
                    if (inprodataList.get(i).isselect) {
                        commaSepValueBuilder.append(inprodataList.get(i).get_id());
                        commaSepValueBuilder1.append(inprodataList.get(i).getTitle());
                        commaSepValueBuilder.append(",");
                        commaSepValueBuilder1.append(",");
                        Log.v("id", commaSepValueBuilder.toString());

                    }
                }
                if (commaSepValueBuilder.length() > 0 && commaSepValueBuilder1.length() > 0) {
                    String finalStr = commaSepValueBuilder.toString().substring(0, commaSepValueBuilder.toString().length() - 1).concat("");
                    String finalStr1 = commaSepValueBuilder1.toString().substring(0, commaSepValueBuilder1.toString().length() - 1).concat("");
                    Log.v("id", finalStr);
                    categoryname = finalStr1;
                    categoryid = finalStr;

                    etcat.setText(categoryname);
                } else {
                    categoryname = "";
                    categoryid = "";
                    etcat.setText(categoryname);
                }
            }
        });

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        check_recycler_view.setLayoutManager(mLayoutManager);
        check_recycler_view.setItemAnimator(new DefaultItemAnimator());
        check_recycler_view.setAdapter(createjobcat_adapter);
        btn_continoue_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void serviceCategoryApiData() {

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
                                    categoryList.add(serviceCategory);

                                }
                                createjobcat_adapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(SignupActivity.this, msg, Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(SignupActivity.this, msg, Toast.LENGTH_SHORT).show();
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

    private void addUserFirebaseDatabase(String name, String email, String pimg, String usertype, String authT, String uId) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        String device_token = FirebaseInstanceId.getInstance().getToken();
        FirebaseUserModel userModel = new FirebaseUserModel();
        userModel.firebaseToken = device_token;
        userModel.name = name;
        userModel.email = email;
        userModel.profilePic = pimg;
        userModel.usertype = usertype;
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
