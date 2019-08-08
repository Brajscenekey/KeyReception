package com.key.keyreception.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;

import com.crashlytics.android.Crashlytics;
import com.key.keyreception.R;
import com.key.keyreception.Session;
import com.key.keyreception.activity.owner.OwnerTabActivity;
import com.key.keyreception.activity.recepnist.TabActivity;
import com.key.keyreception.base.BaseActivity;

import io.fabric.sdk.android.Fabric;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_splash);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {
                Session session = new Session(SplashActivity.this);
                Log.e("session : ", session.getusertype() + "\n" + session.isLoggedIn());
                if (session.isLoggedIn()) {
                    if (session.getusertype().equals("owner")) {
                        startActivity(new Intent(SplashActivity.this, OwnerTabActivity.class));
                    } else {
                        startActivity(new Intent(SplashActivity.this, TabActivity.class));
                    }
                } else {
                    startActivity(new Intent(SplashActivity.this, UserTypeActivity.class));
                }
                finish();
            }
        }, 2000);
    }
}