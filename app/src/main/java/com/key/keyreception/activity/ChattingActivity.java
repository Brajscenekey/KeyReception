package com.key.keyreception.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.key.keyreception.R;
import com.key.keyreception.Session;
import com.key.keyreception.activity.ActivityAdapter.ChattingAdapter;
import com.key.keyreception.activity.model.Chat;
import com.key.keyreception.activity.model.FirebaseUserModel;
import com.key.keyreception.base.BaseActivity;
import com.key.keyreception.helper.Constant;
import com.key.keyreception.helper.PDialog;
import com.key.keyreception.helper.Utility;
import com.key.keyreception.helper.croper.ImagePicker;
import com.key.keyreception.supportnoti.service.FcmNotificationBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.key.keyreception.helper.Constant.Other_User_id;


public class ChattingActivity extends BaseActivity implements View.OnClickListener {

    public Boolean isCompleteChatLoad = false, isLoadFirst = true;
    private String otherid = "", otherprofileImage = "", otherfullName = "";
    private ImageView chat_userimg;
    private ImageView chat_send_msg;
    private ImageView icotraingle;
    private TextView chat_username, tv_chat_date, tv_user_block;
    private FirebaseDatabase firebaseDatabase;
    private EditText et_chatmessage;
    private Session session;
    private String username, userid, userimage, usertype;
    private Utility utility;
    private DatabaseReference databaseReference;
    private int listIndex = 0, increment = 0, totalCount = 0, tempCount = 0;
    private String lastIndexmessagekey;
    private boolean ischeck = true;
    private PDialog pDialog;
    private ChattingAdapter chattingAdapter;
    private ArrayList<Chat> chatList;
    private RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    private Map<String, Chat> map;
    private SwipeRefreshLayout swipeRefreshLayout;
    private long deleteTime;
    private String chatNode = "";
    private Uri image_FirebaseURL;
    private String blockedId = "";
    private CardView cv_chat_menu;
    private long mLastClickTime = 0;
    private String Othertype = "";
    private FirebaseUserModel otherUserInfo;
    private String otherprofilePic = "";
    private boolean isNotification = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);
        session = new Session(this);
        utility = new Utility();
        pDialog = new PDialog();
        chatList = new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance();

        map = new HashMap<>();
        inItView();
        chatshow();
        chatNode = gettingNotes();
        getBlockUserData();
        isNotification();


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.chat_send_msg: {
                if (blockedId.equals(userid)) {
                    et_chatmessage.setText("");
                    closeKeyboard();
                    cv_chat_menu.setVisibility(View.GONE);
                    icotraingle.setVisibility(View.GONE);
                    Constant.openAlertDialog(this, "You block " + otherfullName + ". Can't send any message.");
                } else if (blockedId.equals(otherid)) {
                    et_chatmessage.setText("");
                    closeKeyboard();
                    cv_chat_menu.setVisibility(View.GONE);
                    icotraingle.setVisibility(View.GONE);
                    Constant.openAlertDialog(this, "You are blocked by " + otherfullName + ". Can't send any message.");
                } else if (blockedId.equals("Both")) {
                    et_chatmessage.setText("");
                    cv_chat_menu.setVisibility(View.GONE);
                    icotraingle.setVisibility(View.GONE);
                    closeKeyboard();
                    Constant.openAlertDialog(this, "You block " + otherfullName + ". Can't send any message.");
                } else {
                    if (!et_chatmessage.getText().toString().trim().isEmpty() || image_FirebaseURL != null) {

                        cv_chat_menu.setVisibility(View.GONE);
                        icotraingle.setVisibility(View.GONE);
                        chatMassage();

                    }
                }

            }
            break;
            case R.id.chat_gallery:
                cv_chat_menu.setVisibility(View.GONE);
                icotraingle.setVisibility(View.GONE);
                if (Build.VERSION.SDK_INT >= 23) {
                    if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(
                                new String[]{Manifest.permission.CAMERA,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                Constant.MY_PERMISSIONS_REQUEST_CAMERA);
                    } else {
                        ImagePicker.pickImage(ChattingActivity.this);
                    }
                } else {
                    ImagePicker.pickImage(ChattingActivity.this);
                }
                break;
            case R.id.ly_reportuser: {

                Intent intent = new Intent(ChattingActivity.this,ChatUserReportActivity.class);
                intent.putExtra("username",otherfullName);
                startActivity(intent);
                cv_chat_menu.setVisibility(View.GONE);
                icotraingle.setVisibility(View.GONE);

            }
            break;


            case R.id.iv_menu:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                if (cv_chat_menu.getVisibility() == View.VISIBLE) {
                    cv_chat_menu.setVisibility(View.GONE);
                    icotraingle.setVisibility(View.GONE);
                } else if (cv_chat_menu.getVisibility() == View.GONE) {
                    getBlockUserData();
                    cv_chat_menu.setVisibility(View.VISIBLE);
                    icotraingle.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.ly_block_user: {

                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                cv_chat_menu.setVisibility(View.GONE);
                icotraingle.setVisibility(View.GONE);
                if (blockedId.equals("")) {
                    blockChatDialog("Are you want to block ");
                } else if (blockedId.equals(userid)) {
                    blockChatDialog("Are you want to unblock ");
                } else if (blockedId.equals(otherid)) {
                    blockChatDialog("Are you want to block ");
                } else if (blockedId.equals("Both")) {
                    blockChatDialog("Are you want to unblock ");
                }
                break;
            }
            case R.id.ly_delete_chat: {
                cv_chat_menu.setVisibility(View.GONE);
                icotraingle.setVisibility(View.GONE);
                deleteChatDialog("Are you sure you want to delete conversation with");
            }
            break;
            case R.id.chat_back_logo: {
                onBackPressed();
            }
            break;

        }
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void inItView() {
        tv_chat_date = findViewById(R.id.tv_chat_date);
        chat_userimg = findViewById(R.id.chat_userimg);
        chat_username = findViewById(R.id.chat_username);
        et_chatmessage = findViewById(R.id.et_chatmessage);
        chat_send_msg = findViewById(R.id.chat_send_msg);
        cv_chat_menu = findViewById(R.id.cv_chat_menu);
        ImageView chat_gallery = findViewById(R.id.chat_gallery);
        recyclerView = findViewById(R.id.chat_recycler_view);
        tv_user_block = findViewById(R.id.tv_user_block);
        icotraingle = findViewById(R.id.icotraingle);
        ImageView iv_menu = findViewById(R.id.iv_menu);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        LinearLayout ly_block_user = findViewById(R.id.ly_block_user);
        LinearLayout ly_delete_chat = findViewById(R.id.ly_delete_chat);
        LinearLayout ly_reportuser = findViewById(R.id.ly_reportuser);

        ly_reportuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChattingActivity.this,ChatUserReportActivity.class);
                intent.putExtra("username",otherfullName);
                startActivity(intent);
            }
        });

        ImageView chat_back_logo = findViewById(R.id.chat_back_logo);
        ly_delete_chat.setOnClickListener(this);
        ly_block_user.setOnClickListener(this);
        chat_send_msg.setOnClickListener(this);
        chat_back_logo.setOnClickListener(this);
        iv_menu.setOnClickListener(this);
        chat_gallery.setOnClickListener(this);
        ly_reportuser.setOnClickListener(this);
        intentData();
        myUserData();
        Othertype = session.getusertype().equals("owner") ? "receptionist" : "owner"; // get value of other user data

        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                InputMethodManager input = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                input.hideSoftInputFromWindow(view.getWindowToken(), 0);
                cv_chat_menu.setVisibility(View.GONE);
                icotraingle.setVisibility(View.GONE);
                return false;

            }
        });
        gettingDataFromUserTable();

    }

    private void myUserData() {
        username = session.getusername();
        userid = session.getuserid();
        userimage = session.getuserimg();
        usertype = session.getusertype();
    }

    private void intentData() {
        Intent intent = getIntent();

        if (getIntent().getStringExtra("serviceProviderId") != null) {
            otherid = getIntent().getStringExtra("serviceProviderId");
            Other_User_id = Integer.parseInt(otherid);
        }

        if (intent.getStringExtra("id") != null) {
            otherid = intent.getStringExtra("id");
            otherprofileImage = intent.getStringExtra("profileImage");
            otherfullName = intent.getStringExtra("fullName");
            Other_User_id = Integer.parseInt(otherid);
        } else {
            Bundle bundle = getIntent().getExtras();

            if (bundle.getString("opponentChatId") != null) {
                otherid = bundle.getString("opponentChatId");
                Other_User_id = Integer.parseInt(otherid);

            }

        }

        intentSetData();
    }

    private void intentSetData() {
        if (otherprofileImage != null) {
            RequestOptions options = new RequestOptions();
            options.placeholder(R.drawable.user_upload);
            options.diskCacheStrategy(DiskCacheStrategy.RESOURCE);
            Glide.with(this).load(otherprofileImage).apply(options).into(chat_userimg);
        }
        chat_username.setText(otherfullName);

    }

    private void gettingDataFromUserTable() {
//        firebaseDatabase.getReference().child(Constant.ARG_USERS).child(Othertype).child(otherid).addValueEventListener(new ValueEventListener() {
        firebaseDatabase.getReference().child(Constant.ARG_USERS).child(otherid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue(FirebaseUserModel.class) != null) {
                    otherUserInfo = dataSnapshot.getValue(FirebaseUserModel.class);
                    chat_username.setText(otherUserInfo.name + "");
                    if (!otherUserInfo.profilePic.equals("")) {
                        otherprofilePic = otherUserInfo.profilePic;
                        try {
                            Glide.with(chat_userimg.getContext()).load(otherprofilePic).into(chat_userimg);

                        } catch (Exception e) {
                            Log.e("", "");
                        }


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void chatMassage() {
        String pushkey = firebaseDatabase.getReference().child(Constant.CHAT_ROOMS_TABLE).push().getKey();
        String chatmsg = et_chatmessage.getText().toString().trim();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        String device_token = FirebaseInstanceId.getInstance().getToken();
        String firebaseToken = FirebaseInstanceId.getInstance().getToken();

        // user chat
        Chat mychat = new Chat();
        //  mychat.deleteby = "";
        // mychat.firebaseId = "";
        mychat.firebaseToken = device_token;
        mychat.message = chatmsg;
        mychat.name = username;
        // mychat.profilePic = userimage;
        if (image_FirebaseURL != null) {
            mychat.imageUrl = image_FirebaseURL.toString();
            mychat.message = "";
            mychat.isImage = 1;
            String holdKeyForImage = pushkey;

        } else {
            mychat.imageUrl = "";
            mychat.message = chatmsg;
            mychat.isImage = 0;
        }
        mychat.timestamp = ServerValue.TIMESTAMP;
        mychat.uid = userid;
        mychat.lastMsg = "";
        mychat.firebaseToken = firebaseToken;
        // mychat.type = usertype;

        //other chat
        Chat otherchat = new Chat();
        // otherchat.deleteby = "";
        //  otherchat.firebaseId = "";
        otherchat.firebaseToken = device_token;
        otherchat.message = chatmsg;
        otherchat.name = otherfullName;
        //  otherchat.profilePic = otherprofileImage;
        otherchat.timestamp = ServerValue.TIMESTAMP;
        otherchat.uid = otherid;
        if (image_FirebaseURL != null) {
            otherchat.imageUrl = image_FirebaseURL.toString();
            otherchat.message = "";
            otherchat.isImage = 1;

//            image_FirebaseURL = null;
        } else {
            otherchat.imageUrl = "";
            otherchat.message = chatmsg;
            otherchat.isImage = 0;
        }
        otherchat.lastMsg = "";
        otherchat.firebaseToken = firebaseToken;
        /*otherchat.banner_date = "";
        otherchat.type = usertype;*/

        String pushid = database.push().getKey();

        database.child(Constant.CHAT_ROOMS_TABLE).child(userid).child(otherid).child(pushid).setValue(mychat);
        database.child(Constant.CHAT_ROOMS_TABLE).child(otherid).child(userid).child(pushid).setValue(mychat);


        database.child(Constant.CHAT_HISTORY_TABLE).child(userid).child(otherid).setValue(otherchat);
        database.child(Constant.CHAT_HISTORY_TABLE).child(otherid).child(userid).setValue(mychat);


        if (isNotification) {
            if (image_FirebaseURL != null) {
                if (firebaseToken != null && otherUserInfo != null) {
                    sendPushNotificationToReceiver(username, "Image", username, userid, firebaseToken);
                }
            } else {
                if (firebaseToken != null && otherUserInfo != null)
                    sendPushNotificationToReceiver(username, chatmsg, username, userid, firebaseToken);
            }
        }
        image_FirebaseURL = null;
        et_chatmessage.setText("");

    }

    public void chatshow() {

        if (utility.checkInternetConnection(this)) {
            bothEndChatList();
            chattingAdapter = new ChattingAdapter(ChattingActivity.this, chatList, userid, new ChattingAdapter.AdapterPositionListener() {
                @Override
                public void getPosition(final int position) {
                    recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                            super.onScrollStateChanged(recyclerView, newState);
                            if (newState == RecyclerView.SCROLL_STATE_IDLE) {

                                if (chatList.size() > position) {
                                    if (chatList.get(mLayoutManager.findFirstVisibleItemPosition()).banner_date != null && chatList.get(mLayoutManager.findFirstVisibleItemPosition()).banner_date.isEmpty()) {
                                        if (chatList.get(position).isImage == 1) {
                                            tv_chat_date.setVisibility(View.VISIBLE);
                                            tv_chat_date.setText(chatList.get(mLayoutManager.findFirstVisibleItemPosition()).banner_date);

                                        } else {
                                            tv_chat_date.setVisibility(View.GONE);
                                            tv_chat_date.setText(chatList.get(mLayoutManager.findFirstVisibleItemPosition()).banner_date);

                                        }

                                    }
                                }
                            }
                        }

                        @Override
                        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);
                            if (isLoadFirst) {
                                if (chatList.size() > 0) {
                                    if (chatList.get(mLayoutManager.findFirstVisibleItemPosition()).banner_date != null && !chatList.get(mLayoutManager.findFirstVisibleItemPosition()).banner_date.isEmpty()) {
                                        if (chatList.get(position).isImage == 1) {
                                            tv_chat_date.setVisibility(View.VISIBLE);
                                            tv_chat_date.setText(chatList.get(mLayoutManager.findFirstVisibleItemPosition()).banner_date);

                                        } else {
                                            tv_chat_date.setVisibility(View.GONE);
                                            tv_chat_date.setText(chatList.get(mLayoutManager.findFirstVisibleItemPosition()).banner_date);

                                        }
                                    }
                                }


                                isLoadFirst = false;
                            } else {
                                if (mLayoutManager.findFirstVisibleItemPosition() != 0 && mLayoutManager.findFirstVisibleItemPosition() != -1) {
                                    if (chatList.get(mLayoutManager.findFirstVisibleItemPosition()).banner_date != null && !chatList.get(mLayoutManager.findFirstVisibleItemPosition()).banner_date.isEmpty()) {

                                        if (mLayoutManager.findFirstVisibleItemPosition() != -1) {
                                            tv_chat_date.setText(chatList.get(mLayoutManager.findFirstVisibleItemPosition()).banner_date);
                                            tv_chat_date.setVisibility(View.VISIBLE);
                                        }
                                    }
                                }


                            }
                        }
                    });
                }
            });
            mLayoutManager = new LinearLayoutManager(ChattingActivity.this);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(chattingAdapter);
            recyclerView.scrollToPosition(map.size() - 1);

            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    ischeck = true;
                    if (lastIndexmessagekey != null) {
                        if (totalCount >= tempCount) {
                            increment += 10;
                            getChatLoadMore(lastIndexmessagekey);
                        } else {
                            swipeRefreshLayout.setEnabled(false);
                        }
                    } else {
                        swipeRefreshLayout.setRefreshing(false);
                    }

                }
            });


        } else {
            Toast.makeText(this, "Network not available", Toast.LENGTH_LONG).show();
        }

//        getBlockUserData();


    }

    public void bothEndChatList() {

//        https://hourlabors-1542366643679.firebaseio.com/chat_rooms/26
//        https://hourlabors-1542366643679.firebaseio.com/


//        pDialog.pdialog(this);
        databaseReference = FirebaseDatabase.getInstance().getReference(Constant.CHAT_ROOMS_TABLE).child(userid).child(otherid);
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                pDialog.hideDialog();
                Chat chat = dataSnapshot.getValue(Chat.class);
                chat.banner_date = getDateBanner(chat.getTimestamp());
                chatList.add(chat);
                //chattingAdapter.notifyDataSetChanged();
                int size = chatList.size() - 1;
                chattingAdapter.notifyItemInserted(size);
                recyclerView.scrollToPosition(size);
                //getChatLoadMore(dataSnapshot.getKey());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                getChatLoadMore(dataSnapshot.getKey());
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                pDialog.hideDialog();
            }
        });
    }


    private void getChatLoadMore(String dataKey) {
        Query query = databaseReference.orderByKey().endAt(dataKey).limitToLast(50);

        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Chat chat = dataSnapshot.getValue(Chat.class);
                tempCount += 1;

                if (totalCount < tempCount) isCompleteChatLoad = true;

                assert chat != null;
                if ((Long) chat.getTimestamp() > deleteTime) {
                    if (ischeck) {
                        lastIndexmessagekey = dataSnapshot.getKey();
                        ischeck = false;
                        listIndex = increment;
                    }
                    getChatDataInmap(dataSnapshot.getKey(), chat);
                } else {
                    map.remove(dataSnapshot.getKey());
                    chatList.clear();
                    chattingAdapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Chat chat = dataSnapshot.getValue(Chat.class);
                tempCount += 1;

                if (totalCount < tempCount) isCompleteChatLoad = true;
                assert chat != null;

                if ((Long) chat.timestamp > deleteTime) {
                    if (ischeck) {
                        lastIndexmessagekey = dataSnapshot.getKey();
                        ischeck = false;
                        listIndex = increment;
                    }
                    getChatDataInmap(dataSnapshot.getKey(), chat);
                } else {
                    map.remove(dataSnapshot.getKey());
                    chatList.clear();
                    chattingAdapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getChatDataInmap(String key, Chat chat) {
        swipeRefreshLayout.setRefreshing(false);
        if (chat != null) {
//            if (chat.deleteby != null) {
//                if (chat.deleteby.equals(session.getchatid())) {
//                    return;
//                } else {
            chat.banner_date = getDateBanner(chat.getTimestamp());
            map.put(key, chat);
            chatList.clear();
            Collection<Chat> values = map.values();
            chatList.addAll(values);
//                }
//            }
        }
        shortList();

        if (listIndex == 0) {
            recyclerView.scrollToPosition(chatList.size() - 1);
        } else if (chatList.size() != (totalCount - 10)) {
            recyclerView.scrollToPosition(19);
        } else if (chatList.size() == totalCount - 10) {
            swipeRefreshLayout.setEnabled(false);
            isCompleteChatLoad = true;
        }

        if (totalCount <= 30) {
            isCompleteChatLoad = true;
            isLoadFirst = true;
            swipeRefreshLayout.setEnabled(false);
        }

    }

    private String getDateBanner(Object timeStamp) {
        String banner_date = "";
        SimpleDateFormat sim = new SimpleDateFormat("d MMMM yyyy", Locale.US);
        try {
            String date_str = sim.format(new Date((Long) timeStamp)).trim();
            String currentDate = sim.format(Calendar.getInstance().getTime()).trim();
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -1);
            String yesterdayDate = sim.format(calendar.getTime()).trim();

            if (date_str.equals(currentDate)) {
                banner_date = getString(R.string.dummy_time).trim();
            } else if (date_str.equals(yesterdayDate)) {
                banner_date = getString(R.string.yesterday).trim();
            } else {
                banner_date = date_str.trim();
            }

            return banner_date;
        } catch (Exception e) {
            e.printStackTrace();
            return banner_date;
        }
    }

    private void shortList() {
        Collections.sort(chatList, new Comparator<Chat>() {

            @Override
            public int compare(Chat a1, Chat a2) {
                if (a1.getTimestamp() == null || a2.getTimestamp() == null)
                    return -1;
                else {
                    Long long1 = Long.valueOf(String.valueOf(a1.getTimestamp()));
                    Long long2 = Long.valueOf(String.valueOf(a2.getTimestamp()));
                    return long1.compareTo(long2);
                }
            }
        });

        chattingAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {

            case Constant.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ImagePicker.pickImage(ChattingActivity.this);
                }
            }
            break;

            case Constant.MY_PERMISSIONS_REQUEST_CAMERA: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ImagePicker.pickImage(ChattingActivity.this);
                }
            }
            break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            if (requestCode == 234) {    // Image Picker
                pDialog.pdialog(this);
                Uri imageUri = ImagePicker.getImageURIFromResult(ChattingActivity.this, requestCode, resultCode, data);
                creatFirebaseProfilePicUrl(imageUri);

            }
        }
    }

    private void creatFirebaseProfilePicUrl(Uri selectedImageUri) {

        StorageReference storageRef;
        FirebaseStorage storage;
        FirebaseApp app;

        app = FirebaseApp.getInstance();
        assert app != null;
        storage = FirebaseStorage.getInstance(app);

        storageRef = storage.getReference("images/");
        StorageReference photoRef = storageRef.child(selectedImageUri.getLastPathSegment());
        photoRef.putFile(selectedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.e("image", String.valueOf(image_FirebaseURL));
                        image_FirebaseURL = uri;
                        chat_send_msg.callOnClick();
                        if (pDialog != null) {
                            pDialog.hideDialog();
                        }
                    }
                });
            }
        });

    }

    private void getBlockUserData() {
        firebaseDatabase.getReference().child(Constant.BlockUsers).child(chatNode).child(Constant.blockedBy).
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue(String.class) != null) {
                            blockedId = dataSnapshot.getValue(String.class);

                            if (blockedId.equals("Both")) {
                                //  chat_block.setImageDrawable(getResources().getDrawable(R.drawable.ic_block));
                                tv_user_block.setText("Unblock User");//color change
                            } else if (blockedId.equals("")) {
                                //chat_block.setImageDrawable(getResources().getDrawable(R.drawable.unblock));
                                tv_user_block.setText("Block User");//default
                            } else if (blockedId.equals(otherid)) {
                                //    chat_block.setImageDrawable(getResources().getDrawable(R.drawable.unblock));
                                tv_user_block.setText("Block User");
                            } else if (blockedId.equals(userid)) {
                                //   chat_block.setImageDrawable(getResources().getDrawable(R.drawable.ic_block));
                                tv_user_block.setText("Unblock User");
                            }

                        } else {
                            blockedId = "";
                            //   chat_block.setImageDrawable(getResources().getDrawable(R.drawable.unblock));
                            tv_user_block.setText("Block User");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    private void blockChatDialog(String msg) {
        final Dialog _dialog = new Dialog(ChattingActivity.this);
        _dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        _dialog.setContentView(R.layout.unfriend_dialog_layout);
        _dialog.setCancelable(false);
        _dialog.setCanceledOnTouchOutside(false);

        TextView tv_name = _dialog.findViewById(R.id.tv_name);
        TextView tv_yes = _dialog.findViewById(R.id.tv_yes);
        TextView tv_dialog_txt = _dialog.findViewById(R.id.tv_dialog_txt);
        ImageView iv_closeDialog = _dialog.findViewById(R.id.iv_closeDialog);

        tv_dialog_txt.setText(msg);
        tv_name.setText(otherfullName + " ?");
        tv_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (blockedId.equals("Both")) {
                    firebaseDatabase.getReference().child(Constant.BlockUsers).child(chatNode).child(Constant.blockedBy).setValue(otherid);
                } else if (blockedId.equals("")) {
                    firebaseDatabase.getReference().child(Constant.BlockUsers).child(chatNode).child(Constant.blockedBy).setValue(userid);
                } else if (blockedId.equals(otherid)) {
                    firebaseDatabase.getReference().child(Constant.BlockUsers).child(chatNode).child(Constant.blockedBy).setValue("Both");
                } else if (blockedId.equals(userid)) {
                    firebaseDatabase.getReference().child(Constant.BlockUsers).child(chatNode).child(Constant.blockedBy).setValue(null);
                }
                _dialog.dismiss();
            }
        });

        iv_closeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _dialog.dismiss();
            }
        });
        _dialog.show();
    }

    private String gettingNotes() {
        //create note for chatroom
        int myUid_ = Integer.parseInt(userid);
        int otherUID_ = Integer.parseInt(otherid);

        if (myUid_ < otherUID_) {
            chatNode = userid + "_" + otherid;
        } else {
            chatNode = otherid + "_" + userid;
        }

        return chatNode;
    }

    //delete dialog
    private void deleteChatDialog(String msg) {
        final Dialog _dialog = new Dialog(ChattingActivity.this);
        _dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        _dialog.setContentView(R.layout.unfriend_dialog_layout);
        _dialog.setCancelable(false);
        _dialog.setCanceledOnTouchOutside(false);

        TextView tv_name = _dialog.findViewById(R.id.tv_name);
        TextView tv_yes = _dialog.findViewById(R.id.tv_yes);
        TextView tv_dialog_txt = _dialog.findViewById(R.id.tv_dialog_txt);
        ImageView iv_closeDialog = _dialog.findViewById(R.id.iv_closeDialog);

        tv_dialog_txt.setText(msg);
        tv_name.setText(otherfullName + " ?");
        tv_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseDatabase.getReference().child(Constant.CHAT_HISTORY_TABLE).child(userid).child(otherid).setValue(null);
                firebaseDatabase.getReference().child(Constant.CHAT_ROOMS_TABLE).child(userid).child(otherid).setValue(null);

                map.clear();
                chatList.clear();
                chattingAdapter.notifyDataSetChanged();
                _dialog.dismiss();
            }
        });

        iv_closeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _dialog.dismiss();
            }
        });
        _dialog.show();
    }


    private void isNotification() {
        FirebaseDatabase.getInstance().getReference().child(Constant.ARG_USERS).child(otherid).child(Constant.isNotification)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue(String.class) != null) {
                            String isNotificationValue = dataSnapshot.getValue(String.class);
                            assert isNotificationValue != null;
                            if (isNotificationValue.equals("1")) {
                                isNotification = true;
                            } else {
                                isNotification = true;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void sendPushNotificationToReceiver(String title, String message, String username, String uid, String firebaseToken) {
        String oppToken = firebaseToken.equals(otherUserInfo.firebaseToken) ? "" : otherUserInfo.firebaseToken;
        FcmNotificationBuilder.initialize()
                .title(title)
                .message(message)
                .username(username)
                .uid(uid)
                .firebaseToken(firebaseToken)
                .receiverFirebaseToken(oppToken).send();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cv_chat_menu.setVisibility(View.GONE);
        icotraingle.setVisibility(View.GONE);
        Constant.ChatOpponentId = "";
        Other_User_id = 0;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Other_User_id = 0;
    }
}
