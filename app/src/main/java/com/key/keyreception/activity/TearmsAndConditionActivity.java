package com.key.keyreception.activity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.key.keyreception.R;
import com.key.keyreception.base.BaseActivity;
import com.key.keyreception.connection.RetrofitClient;
import com.key.keyreception.helper.ProgressDialog;

import org.json.JSONObject;

import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class TearmsAndConditionActivity extends BaseActivity implements View.OnClickListener {

    private WebView webView;
    private ProgressDialog progressbar;
    private TextView txt_types;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tearms_and_condition);
        inItView();

    }

    private void inItView() {
        progressbar = new ProgressDialog(this);
        webView = findViewById(R.id.web_view);
        txt_types = findViewById(R.id.txt_types);
        ImageView iv_back_icon = findViewById(R.id.iv_back_icon);
        setOnClick(iv_back_icon);

        getTearmsAndConditions();
    }

    private void getTearmsAndConditions() {
        progressbar.show();

        Call<ResponseBody> call = RetrofitClient.getInstance()
                .getAnotherApi().getContent();
        call.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("NewApi")
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull retrofit2.Response<ResponseBody> response) {
                try {
                    progressbar.dismiss();
                    switch (response.code()) {
                        case 200: {
                            String stresult = Objects.requireNonNull(response.body()).string();
                            Log.d("response", stresult);
                            JSONObject jsonObject = new JSONObject(stresult);
                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");
                            JSONObject data = jsonObject.getJSONObject("data");

                            if (status.equals("success")) {
                                String terms ="";
                                if (getIntent().getStringExtra("terms").equals("terms")) {
                                    terms  = data.getString("trems");
                                    txt_types.setText(getResources().getString(R.string.term_con));
                                }
                                else if (getIntent().getStringExtra("terms").equals("policy")) {
                                    terms = data.getString("policy");
                                    txt_types.setText(getResources().getString(R.string.pri_pol));


                                }
                                else {
                                    terms  = data.getString("trems");
                                    txt_types.setText(getResources().getString(R.string.abo_us));

                                }
                                initWebView(terms);

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
                                Toast.makeText(TearmsAndConditionActivity.this, msg, Toast.LENGTH_SHORT).show();
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
                progressbar.dismiss();
            }
        });

    }


    public void initWebView(String url) {
        webView.setVisibility(View.VISIBLE);
        webView.setWebViewClient(new MyWebViewClient());
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.getSettings().setAllowContentAccess(true);
        //webView.loadUrl(url);
        webView.loadUrl("https://docs.google.com/gview?embedded=true&url=" + url);
    }

    private void setOnClick(View... views) {
        for (View view : views) {
            view.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.iv_back_icon:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    class MyWebViewClient extends WebViewClient {
        private MyWebViewClient() {
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressbar.dismiss();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            progressbar.dismiss();
        }

        @Override
        public void onPageCommitVisible(WebView view, String url) {
            super.onPageCommitVisible(view, url);
        }
    }
}
