package com.key.keyreception.helper;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import androidx.annotation.NonNull;
import android.view.Window;

import com.key.keyreception.R;

/**
 * Created by Ravi Birla on 14,May,2019
 */
public class ProgressDialog extends Dialog{
    private Context context;
    public ProgressDialog(@NonNull Context context) {
        super(context);
        this.context = context;
        // This is the fragment_search_details XML file that describes your Dialog fragment_search_details
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        this.setCancelable(false);
        this.setContentView(R.layout.my_progress);
    }
}
