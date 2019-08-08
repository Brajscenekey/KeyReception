package com.key.keyreception.activity.owner;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.key.keyreception.R;
import com.key.keyreception.base.BaseActivity;
import com.key.keyreception.ownerFragment.OwnerEarningFragment;
import com.key.keyreception.ownerFragment.OwnerMessageFragment;
import com.key.keyreception.ownerFragment.OwnerNotificationFragment;
import com.key.keyreception.ownerFragment.OwnerProfileFragment;
import com.key.keyreception.ownerFragment.OwnerReservationFragment;

public class OwnerTabActivity extends BaseActivity implements View.OnClickListener {

    public TextView tv_head;
    public RelativeLayout rtlv_one, rtlv_two, rtlv_three, rtlv_four, rtlv_five, headrl;
    ImageView appoint, not, chart, pro, chat, iv_addprop;
    Fragment fragment = null;
    Runnable runnable;
    private boolean doubleBackToExitPressedOnce;
    private View view1, view2, view3, view4, view5;
    private int lastClick = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decor = getWindow().getDecorView();
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.activity_owner_tab);
        init();

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("checkid")) {
            alertNFManage();
        }
        if (intent != null && intent.hasExtra("order")) {
            String s = intent.getStringExtra("order");
            if (s.equals("3")) {
                if (lastClick != R.id.owner_rtlv_five) {
                    lastClick = R.id.owner_rtlv_five;
                    tv_head.setText(getResources().getString(R.string.profile));
                    fragment = new OwnerProfileFragment();
                    displaySelectedFragment(fragment);
                    headrl.setVisibility(View.GONE);
                    iv_addprop.setVisibility(View.GONE);
                    appoint.setImageResource(R.drawable.ic_inactive_calendar);
                    chart.setImageResource(R.drawable.ic_inactive_bar_chart);
                    chat.setImageResource(R.drawable.ic_inactive_chat_ico);
                    not.setImageResource(R.drawable.ic_inactive_ball_ico);
                    pro.setImageResource(R.drawable.ic_active_user);
                    view1.setVisibility(View.GONE);
                    view2.setVisibility(View.GONE);
                    view3.setVisibility(View.GONE);
                    view4.setVisibility(View.GONE);
                    view5.setVisibility(View.VISIBLE);
                }


            }

        } else {


            if (lastClick != R.id.owner_rtlv_one) {
                lastClick = R.id.owner_rtlv_one;
                tv_head.setText(getResources().getString(R.string.reserv));
                fragment = new OwnerReservationFragment();
                displaySelectedFragment(fragment);
                iv_addprop.setVisibility(View.GONE);
                appoint.setImageResource(R.drawable.ic_active_calendar);
                chart.setImageResource(R.drawable.ic_inactive_bar_chart);
                chat.setImageResource(R.drawable.ic_inactive_chat_ico);
                not.setImageResource(R.drawable.ic_inactive_ball_ico);
                pro.setImageResource(R.drawable.ic_inactive_user);
                view1.setVisibility(View.VISIBLE);
                view2.setVisibility(View.GONE);
                view3.setVisibility(View.GONE);
                view4.setVisibility(View.GONE);
                view5.setVisibility(View.GONE);
            }


        }


    }

    public void init() {
        tv_head = findViewById(R.id.owner_tv_head);
        headrl = findViewById(R.id.owner_chomerr);
        iv_addprop = findViewById(R.id.iv_addprop);

        iv_addprop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OwnerTabActivity.this, AddpropertyActivity.class);
                intent.putExtra("intentid", "101");
                startActivityForResult(intent, 10006);
            }
        });

        appoint = findViewById(R.id.owner_img_one);
        chat = findViewById(R.id.owner_img_two);
        chart = findViewById(R.id.owner_img_three);
        not = findViewById(R.id.owner_img_four);
        pro = findViewById(R.id.owner_img_five);
        view1 = findViewById(R.id.owner_view1);
        view2 = findViewById(R.id.owner_view2);
        view3 = findViewById(R.id.owner_view3);
        view4 = findViewById(R.id.owner_view4);
        view5 = findViewById(R.id.owner_view5);
        rtlv_one = findViewById(R.id.owner_rtlv_one);
        rtlv_two = findViewById(R.id.owner_rtlv_two);
        rtlv_three = findViewById(R.id.owner_rtlv_three);
        rtlv_four = findViewById(R.id.owner_rtlv_four);
        rtlv_five = findViewById(R.id.owner_rtlv_five);
        rtlv_one.setOnClickListener(this);
        rtlv_two.setOnClickListener(this);
        rtlv_three.setOnClickListener(this);
        rtlv_four.setOnClickListener(this);
        rtlv_five.setOnClickListener(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 10001) {
            if (resultCode == Activity.RESULT_OK && fragment instanceof OwnerReservationFragment) {
                ((OwnerReservationFragment) fragment).switchPager();
            }
        }
        if (requestCode == 10006) {
            if (resultCode == Activity.RESULT_OK && fragment instanceof OwnerEarningFragment) {
                ((OwnerEarningFragment) fragment).ownerSwitchPager();
            }
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.owner_rtlv_one:
                if (lastClick != R.id.owner_rtlv_one) {
                    lastClick = R.id.owner_rtlv_one;
                    tv_head.setText(getResources().getString(R.string.reserv));
                    fragment = new OwnerReservationFragment();
                    displaySelectedFragment(fragment);
                    updateTabView(R.id.owner_rtlv_one);
                    headrl.setVisibility(View.VISIBLE);
                    iv_addprop.setVisibility(View.GONE);
                }


                break;
            case R.id.owner_rtlv_two:
                if (lastClick != R.id.owner_rtlv_two) {
                    lastClick = R.id.owner_rtlv_two;
                    tv_head.setText(getResources().getString(R.string.mess));
                    fragment = new OwnerMessageFragment();
                    displaySelectedFragment(fragment);
                    updateTabView(R.id.owner_rtlv_two);
                    headrl.setVisibility(View.VISIBLE);
                    iv_addprop.setVisibility(View.GONE);
                }


                break;
            case R.id.owner_rtlv_three:
                if (lastClick != R.id.owner_rtlv_three) {
                    lastClick = R.id.owner_rtlv_three;
                    tv_head.setText(getResources().getString(R.string.earn));
                    fragment = new OwnerEarningFragment();
                    displaySelectedFragment(fragment);
                    updateTabView(R.id.owner_rtlv_three);
                    headrl.setVisibility(View.VISIBLE);
                    iv_addprop.setVisibility(View.VISIBLE);
                }


                break;
            case R.id.owner_rtlv_four:

                if (lastClick != R.id.owner_rtlv_four) {
                    lastClick = R.id.owner_rtlv_four;
                    tv_head.setText(getResources().getString(R.string.not));
                    fragment = new OwnerNotificationFragment();
                    displaySelectedFragment(fragment);
                    updateTabView(R.id.owner_rtlv_four);
                    headrl.setVisibility(View.VISIBLE);
                    iv_addprop.setVisibility(View.GONE);

                }


                break;
            case R.id.owner_rtlv_five:
                if (lastClick != R.id.owner_rtlv_five) {
                    lastClick = R.id.owner_rtlv_five;
                    tv_head.setText(getResources().getString(R.string.profile));
                    fragment = new OwnerProfileFragment();
                    displaySelectedFragment(fragment);
                    updateTabView(R.id.owner_rtlv_five);
                    headrl.setVisibility(View.GONE);
                }


                break;
            default:
                if (lastClick != R.id.owner_rtlv_one) {
                    lastClick = R.id.owner_rtlv_one;
                    tv_head.setText(getResources().getString(R.string.reserv));
                    fragment = new OwnerReservationFragment();
                    displaySelectedFragment(fragment);
                    updateTabView(R.id.owner_rtlv_one);
                    headrl.setVisibility(View.VISIBLE);
                    iv_addprop.setVisibility(View.GONE);
                }

                break;
        }
    }


    private void updateTabView(int flag) {
        switch (flag) {
            case R.id.owner_rtlv_one:
                appoint.setImageResource(R.drawable.ic_active_calendar);
                chart.setImageResource(R.drawable.ic_inactive_bar_chart);
                chat.setImageResource(R.drawable.ic_inactive_chat_ico);
                not.setImageResource(R.drawable.ic_inactive_ball_ico);
                pro.setImageResource(R.drawable.ic_inactive_user);
                view1.setVisibility(View.VISIBLE);
                view2.setVisibility(View.GONE);
                view3.setVisibility(View.GONE);
                view4.setVisibility(View.GONE);
                view5.setVisibility(View.GONE);
                break;
            case R.id.owner_rtlv_two:
                appoint.setImageResource(R.drawable.ic_inactive_calendar);
                chart.setImageResource(R.drawable.ic_inactive_bar_chart);
                chat.setImageResource(R.drawable.ic_active_chat_ico);
                not.setImageResource(R.drawable.ic_inactive_ball_ico);
                pro.setImageResource(R.drawable.ic_inactive_user);
                view1.setVisibility(View.GONE);
                view2.setVisibility(View.VISIBLE);
                view3.setVisibility(View.GONE);
                view4.setVisibility(View.GONE);
                view5.setVisibility(View.GONE);
                break;
            case R.id.owner_rtlv_three:
                appoint.setImageResource(R.drawable.ic_inactive_calendar);
                chart.setImageResource(R.drawable.ic_active_bar_chart);
                chat.setImageResource(R.drawable.ic_inactive_chat_ico);
                not.setImageResource(R.drawable.ic_inactive_ball_ico);
                pro.setImageResource(R.drawable.ic_inactive_user);
                view1.setVisibility(View.GONE);
                view2.setVisibility(View.GONE);
                view3.setVisibility(View.VISIBLE);
                view4.setVisibility(View.GONE);
                view5.setVisibility(View.GONE);
                break;
            case R.id.owner_rtlv_four:
                appoint.setImageResource(R.drawable.ic_inactive_calendar);
                chart.setImageResource(R.drawable.ic_inactive_bar_chart);
                chat.setImageResource(R.drawable.ic_inactive_chat_ico);
                not.setImageResource(R.drawable.ic_active_ball_ico);
                pro.setImageResource(R.drawable.ic_inactive_user);
                view1.setVisibility(View.GONE);
                view2.setVisibility(View.GONE);
                view3.setVisibility(View.GONE);
                view4.setVisibility(View.VISIBLE);
                view5.setVisibility(View.GONE);
                break;
            case R.id.owner_rtlv_five:
                appoint.setImageResource(R.drawable.ic_inactive_calendar);
                chart.setImageResource(R.drawable.ic_inactive_bar_chart);
                chat.setImageResource(R.drawable.ic_inactive_chat_ico);
                not.setImageResource(R.drawable.ic_inactive_ball_ico);
                pro.setImageResource(R.drawable.ic_active_user);
                view1.setVisibility(View.GONE);
                view2.setVisibility(View.GONE);
                view3.setVisibility(View.GONE);
                view4.setVisibility(View.GONE);
                view5.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void displaySelectedFragment(Fragment fragment) {
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.commit();
    }


    @SuppressLint("NewApi")
    @Override
    public void onBackPressed() {
        Handler handler = new Handler();
        FragmentManager fm = getSupportFragmentManager();
        int i = fm.getBackStackEntryCount();
        if (i > 0) {
            fm.popBackStack();
        } else if (!doubleBackToExitPressedOnce) {

            doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Click again to exit", Toast.LENGTH_SHORT).show();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);

        } else {
            handler.removeCallbacks(runnable);
            finishAffinity();
        }
    }


    public void alertNFManage() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(OwnerTabActivity.this);

        alertDialog.setTitle("Alert");
        alertDialog.setMessage(getResources().getString(R.string.please_switch_user_mode_to_do_this));
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(OwnerTabActivity.this, OwnerTabActivity.class);
                intent.putExtra("order", "3");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        alertDialog.show();
    }
}
