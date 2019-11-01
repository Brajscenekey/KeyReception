package com.key.keyreception.activity.owner;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.key.keyreception.R;
import com.key.keyreception.Session;
import com.key.keyreception.base.BaseActivity;
import com.key.keyreception.helper.PDialog;

/**
 * Created by Ravi Birla on 23,April,2019
 */
public class MakePaymentActivity extends BaseActivity implements View.OnClickListener {


    private String paymenttype = "", jobId, receiverId, amount;
    private Session session;
    private PDialog pDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_payment);
        session = new Session(this);
        pDialog = new PDialog();
        Intent intent = getIntent();
        jobId = intent.getStringExtra("jobId");
        receiverId = intent.getStringExtra("receiverId");
        amount = intent.getStringExtra("amount");
        init();
    }

    public void init() {
        Button btnpayment = findViewById(R.id.btn_makepayment);
        TextView totamount = findViewById(R.id.tv_paytotalamount);
        ImageView iv_paypal = findViewById(R.id.iv_makepay_paypal);
        RelativeLayout rl_makepay_paypal = findViewById(R.id.rl_makepay_paypal);
        TextView tv_makepay_paypal = findViewById(R.id.tv_makepay_paypal);
        ImageView ivback = findViewById(R.id.iv_leftarrow_payback);
        paymenttype = session.getPayment();
        btnpayment.setOnClickListener(this);
        ivback.setOnClickListener(this);
        totamount.setText(amount);

        if (!paymenttype.isEmpty()) {
            switch (paymenttype) {
                case "paypal":
                    rl_makepay_paypal.setVisibility(View.VISIBLE);
                    iv_paypal.setImageDrawable(getResources().getDrawable(R.drawable.ic_paypal_ico));
                    tv_makepay_paypal.setText(getResources().getString(R.string.paypal));
                    break;
                case "venmo":
                    rl_makepay_paypal.setVisibility(View.VISIBLE);
                    iv_paypal.setImageDrawable(getResources().getDrawable(R.drawable.ic_nvenmo_ico));
                    tv_makepay_paypal.setText(getResources().getString(R.string.venmo));
                    break;
                default:
                    rl_makepay_paypal.setVisibility(View.VISIBLE);
                    iv_paypal.setImageDrawable(getResources().getDrawable(R.drawable.ic_visa));
                    tv_makepay_paypal.setText(getResources().getString(R.string.credit));
                    break;
            }
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_makepayment: {
                if (!paymenttype.isEmpty()) {
                }
            }
            break;
            case R.id.iv_leftarrow_payback: {
                backPress();
            }
        }
    }




}
