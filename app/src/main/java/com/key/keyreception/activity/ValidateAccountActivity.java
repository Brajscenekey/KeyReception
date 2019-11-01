package com.key.keyreception.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.exifinterface.media.ExifInterface;
import androidx.appcompat.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.key.keyreception.R;
import com.key.keyreception.Session;
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
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class ValidateAccountActivity extends BaseActivity implements View.OnClickListener {

    private EditText et_socialnumber;
    private ImageView iv_plusbox;
    private Session session;
    private File file;
    private Utility utility;
    private PDialog pDialog;
    private Bitmap bm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validate_account);
        Validation validation = new Validation(this);
        session = new Session(ValidateAccountActivity.this);
        pDialog = new PDialog();
        utility = new Utility();
        init();
    }

    private void init() {
        et_socialnumber = findViewById(R.id.et_socialnumber);
        iv_plusbox = findViewById(R.id.iv_plusbox);
        Button btn_validaccount = findViewById(R.id.btn_validaccount);
        btn_validaccount.setOnClickListener(this);
        iv_plusbox.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_validaccount: {
                if (utility.checkInternetConnection(this)) {
                    if (!et_socialnumber.getText().toString().isEmpty() && bm != null) {
                        validaccountData();
                    } else {
                        Toast.makeText(this, "Please Choose both", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Toast.makeText(this, "Please check your Network", Toast.LENGTH_SHORT).show();
                }
            }
            break;

            case R.id.iv_plusbox: {
                if (Utility.checkCameraPermission(ValidateAccountActivity.this)&&Utility.checkPermission(ValidateAccountActivity.this)) {
                    gallerycameramethod();
                }

            }

        }
    }


    public void gallerycameramethod() {

        final CharSequence[] items = {"Camera", "Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(ValidateAccountActivity.this);
        builder.setTitle("Add Image");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                    if (items[i].equals("Camera")) {
                        utility.dispatchTakePictureIntent(ValidateAccountActivity.this);

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

    public void validaccountData() {
        pDialog.pdialog(ValidateAccountActivity.this);
        String number = et_socialnumber.getText().toString();
        String authtoken = session.getAuthtoken();
        String notificationStatus = session.getnotificationStatus();
        MediaType text = MediaType.parse("text/plain");
        RequestBody number1 = RequestBody.create(text, number);
        RequestBody number2 = RequestBody.create(text, "");
        RequestBody notificationStatus1 = RequestBody.create(text, notificationStatus);
        MultipartBody.Part fileToUpload1 = null;
        if (file != null) {
            RequestBody mFile1 = RequestBody.create(MediaType.parse("image/*"), file);
            fileToUpload1 = MultipartBody.Part.createFormData("document", file.getName(), mFile1);

        }

        Call<ResponseBody> call = RetrofitClient.getInstance()
                .getApi().updateDoc(authtoken, number1, number2, number2, number2, number2, notificationStatus1, fileToUpload1);
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
                                Toast.makeText(ValidateAccountActivity.this, msg, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ValidateAccountActivity.this, TabActivity.class);
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
                                Toast.makeText(ValidateAccountActivity.this, msg, Toast.LENGTH_SHORT).show();
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
            file = new File(utility.getRealPathFromURI(filePath, ValidateAccountActivity.this));
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

                /*
                exif = new ExifInterface(picturePath);
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_UNDEFINED);
                Bitmap bmRotated = Utility.rotateBitmap(bm, orientation);
          */      iv_plusbox.setImageBitmap(bm);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == 1 && resultCode != RESULT_CANCELED) {
            try {
                Uri uri1 = Uri.fromFile(new File(Utility.mCurrentPhotoPath));
                bm = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri1);
                iv_plusbox.setImageBitmap(bm);
                file = new File(utility.getRealPathFromURI(uri1, ValidateAccountActivity.this));
            } catch (Exception ignored) {
            }
        }
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
