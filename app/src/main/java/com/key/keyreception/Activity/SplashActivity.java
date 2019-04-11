package com.key.keyreception.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;

import com.key.keyreception.Activity.Owner.OwnerTabActivity;
import com.key.keyreception.Activity.Recepnist.TabActivity;
import com.key.keyreception.R;
import com.key.keyreception.Session;
import com.key.keyreception.base.BaseActivity;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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