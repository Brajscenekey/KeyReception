package com.key.keyreception.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.key.keyreception.R;
import com.key.keyreception.base.BaseActivity;

public class ChatUserReportActivity extends BaseActivity implements View.OnClickListener {

    private EditText comment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_user_report);
        inItView();

    }

    public void inItView() {
        Intent intent = getIntent();
        TextView tv_Header = findViewById(R.id.header);
        ImageView iv_back = findViewById(R.id.iv_back);
        comment = findViewById(R.id.et_report_comment);
        TextView tv_btn_submit_report  = findViewById(R.id.tv_btn_submit_report);
        iv_back.setOnClickListener(this);
        tv_btn_submit_report.setOnClickListener(this);
        tv_Header.setText(String.format("Report %s", intent.getStringExtra("username")));
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_btn_submit_report: {
                if (comment.getText().toString().isEmpty())
                {
                    Toast.makeText(this, "Please write your comment", Toast.LENGTH_SHORT).show();
                }
                else {
                    onBackPressed();
                }
            }
            break;

            case R.id.iv_back: {
                onBackPressed();
            }
            break;
        }
    }
}
