package com.key.keyreception.Activity.Owner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.key.keyreception.R;
import com.key.keyreception.base.BaseActivity;

public class ServiceActivity extends BaseActivity implements View.OnClickListener {

    LinearLayout ll_subcrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        init();
    }

    public void init() {

        ll_subcrip = findViewById(R.id.li_sub);
        ll_subcrip.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.li_sub: {
                Intent intent = new Intent(ServiceActivity.this, SubscriptionActivity.class);
                startActivity(intent);
            }
        }

    }
}
