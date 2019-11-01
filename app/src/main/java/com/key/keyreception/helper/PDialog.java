package com.key.keyreception.helper;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;

import androidx.annotation.RequiresApi;

import com.key.keyreception.R;

import java.util.Objects;

/**
 * Created by Ravi Birla on 26,December,2018
 */
public class PDialog {

     ProgressDialog pDialog;

    public void pdialog(Context context)
    {
        pDialog = new ProgressDialog(context);
        Objects.requireNonNull(pDialog.getWindow()).setBackgroundDrawable(new
                ColorDrawable(android.graphics.Color.TRANSPARENT));
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(false);
        pDialog.show();
        pDialog.setContentView(R.layout.my_progress);
    }


    public void hideDialog() {

        if (pDialog!=null && pDialog.isShowing())
            pDialog.dismiss();
    }
   }
