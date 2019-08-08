package com.key.keyreception.activity.recepnist;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.media.ExifInterface;
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
import android.widget.Toast;

import com.key.keyreception.R;
import com.key.keyreception.Session;
import com.key.keyreception.activity.ActivityAdapter.Createjobcat_Adapter;
import com.key.keyreception.activity.model.ServiceCategory;
import com.key.keyreception.base.BaseActivity;
import com.key.keyreception.connection.RetrofitClient;
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

public class CompleteValidActivity extends BaseActivity implements View.OnClickListener {

    private EditText et_socialnumber, et_comvalid_category;
    private ImageView iv_plusbox;
    private Session session;
    private File file;
    private Utility utility;
    private PDialog pDialog;
    private Validation validation;
    private Bitmap bm;
    private Createjobcat_Adapter createjobcat_adapter;
    private List<ServiceCategory> categoryList = new ArrayList<>();
    private String categoryid = "", categoryname, pass1 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_valid);
        session = new Session(CompleteValidActivity.this);
        pDialog = new PDialog();
        utility = new Utility();
        validation = new Validation(this);
        init();
        serviceCategoryApiData();
    }

    private void init() {
        et_socialnumber = findViewById(R.id.et_socialnumber);
        iv_plusbox = findViewById(R.id.iv_plusbox);
        Button btn_validaccount = findViewById(R.id.btn_validaccount);
        et_comvalid_category = findViewById(R.id.et_comvalid_category);
        btn_validaccount.setOnClickListener(this);
        iv_plusbox.setOnClickListener(this);
        et_comvalid_category.setOnClickListener(this);
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
                    et_comvalid_category.setText(categoryname);
                } else {
                    categoryname = "";
                    categoryid = "";
                    et_comvalid_category.setText(categoryname);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_validaccount: {
                if (utility.checkInternetConnection(this)) {
                    if (validation.isCategoryValid(et_comvalid_category) && validation.isSocialValid(et_socialnumber) && validation.isDriverImageUpload(bm)) {
                        validaccountData();
                    }
                } else {
                    Toast.makeText(this, "Please check your Network", Toast.LENGTH_SHORT).show();
                }
            }
            break;
            case R.id.iv_plusbox: {
                if (Utility.checkCameraPermission(CompleteValidActivity.this)) {
                    gallerycameramethod();
                }
            }
            break;
            case R.id.et_comvalid_category: {
                openAuthorizationDialog();
            }
            break;

        }
    }

    public void gallerycameramethod() {
        final boolean result = Utility.checkPermission(CompleteValidActivity.this);
        final CharSequence[] items = {"Camera", "Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(CompleteValidActivity.this);
        builder.setTitle("Add Image");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (result) {
                    if (items[i].equals("Camera")) {
                        utility.dispatchTakePictureIntent(CompleteValidActivity.this);
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

    public void validaccountData() {
        pDialog.pdialog(CompleteValidActivity.this);
        String number = et_socialnumber.getText().toString();
        String authtoken = session.getAuthtoken();
        MediaType text = MediaType.parse("text/plain");
        RequestBody number1 = RequestBody.create(text, number);
        RequestBody cat1 = RequestBody.create(text, categoryid);
        RequestBody free = RequestBody.create(text, "");
        RequestBody userType = RequestBody.create(text, "receptionist");
        MultipartBody.Part fileToUpload1 = null;
        if (file != null) {
            RequestBody mFile1 = RequestBody.create(MediaType.parse("image/*"), file);
            fileToUpload1 = MultipartBody.Part.createFormData("document", file.getName(), mFile1);

        }

        Call<ResponseBody> call = RetrofitClient.getInstance()
                .getApi().updateSwitchedUserProfile(authtoken, cat1, number1, free, free, free, free, userType, fileToUpload1);
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
                                session.issetLoggedIn(true);
                                session.putusertype("receptionist");
                                Toast.makeText(CompleteValidActivity.this, msg, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(CompleteValidActivity.this, TabActivity.class);
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
                                Toast.makeText(CompleteValidActivity.this, msg, Toast.LENGTH_SHORT).show();
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
    protected void onActivityResult(int requestCode,
                                    int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            ExifInterface exif;

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
                iv_plusbox.setImageBitmap(bmRotated);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == 1 && resultCode != RESULT_CANCELED) {
            try {
                Uri uri1 = Uri.fromFile(new File(Utility.mCurrentPhotoPath));
                bm = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri1);
                iv_plusbox.setImageBitmap(bm);
                file = new File(utility.getRealPathFromURI(uri1, CompleteValidActivity.this));

            } catch (Exception ignored) {
            }


        }


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
                                Toast.makeText(CompleteValidActivity.this, msg, Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(CompleteValidActivity.this, msg, Toast.LENGTH_SHORT).show();
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
        super.onBackPressed();
    }
}


