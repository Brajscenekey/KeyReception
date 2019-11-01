package com.key.keyreception.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.key.keyreception.R;
import com.key.keyreception.Session;

public class UserTypeActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnowner, btnrecep;
    private Session session;
    private String usertype = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decor = getWindow().getDecorView();
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.activity_user_type);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        session = new Session(this);
        init();
    }

    private void init() {
        btnowner = findViewById(R.id.btnut_owner);
        btnrecep = findViewById(R.id.btnut_recep);
        ImageView ivsend = findViewById(R.id.ivut_send);
        btnrecep.setOnClickListener(this);
        btnowner.setOnClickListener(this);
        ivsend.setOnClickListener(this);
    }

    @SuppressLint("NewApi")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnut_owner: {
                session.putusertype("owner");
                usertype = "owner";
                btnowner.setBackground(getResources().getDrawable(R.drawable.recbtn));
                btnrecep.setBackground(getResources().getDrawable(R.drawable.whiteboarder));
            }
            break;
            case R.id.btnut_recep: {
                usertype = "receptionist";
                session.putusertype("receptionist");
                btnowner.setBackground(getResources().getDrawable(R.drawable.whiteboarder));
                btnrecep.setBackground(getResources().getDrawable(R.drawable.recbtn));
            }
            break;
            case R.id.ivut_send: {
                if (!usertype.equals("")) {
                    Intent intent = new Intent(UserTypeActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(UserTypeActivity.this, "Please select your login profile", Toast.LENGTH_SHORT).show();
                }
            }
            break;

        }
    }
}
