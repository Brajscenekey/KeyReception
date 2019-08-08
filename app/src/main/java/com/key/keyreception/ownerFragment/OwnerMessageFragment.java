package com.key.keyreception.ownerFragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.key.keyreception.R;
import com.key.keyreception.Session;
import com.key.keyreception.activity.model.Chat;
import com.key.keyreception.activity.model.FirebaseUserModel;
import com.key.keyreception.base.BaseFragment;
import com.key.keyreception.helper.Constant;
import com.key.keyreception.helper.Utility;
import com.key.keyreception.recepnistFragment.adapter.MsglstAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class OwnerMessageFragment extends BaseFragment {


    private TextView rhide;
    private Session session;
    private List<Chat> chatList = new ArrayList<>();
    private Utility util;
    private MsglstAdapter msglstAdapter;
    private List<FirebaseUserModel> userList = new ArrayList<>();
    private Map<String, Chat> mapList = new HashMap<>();

//    private PDialog pDialog;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_message, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        session = new Session(mContext);
        util = new Utility();
        init();
    }

    public void init() {

        RecyclerView recyclerView = getView().findViewById(R.id.message_recycler_view);
        rhide = getView().findViewById(R.id.message_tv_no_record);
        if (util.checkInternetConnection(mContext)) {
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
            recyclerView.setLayoutManager(mLayoutManager);
            msglstAdapter = new MsglstAdapter(mContext, chatList, "5");
            recyclerView.setAdapter(msglstAdapter);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            if (chatList.size() == 0) {
                rhide.setVisibility(View.VISIBLE);
            } else {
                rhide.setVisibility(View.GONE);
            }
            msglst();
        } else {
            Toast.makeText(mContext, "Network not available", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        msglst();
    }

    public void msglst() {
        String cusid = session.getuserid();

        FirebaseDatabase.getInstance().getReference().child(Constant.CHAT_HISTORY_TABLE).child(cusid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    rhide.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        DatabaseReference databaseArtists = FirebaseDatabase.getInstance().getReference(Constant.CHAT_HISTORY_TABLE).child(cusid);
        databaseArtists.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.getValue(Chat.class) != null) {
                    Chat chat = dataSnapshot.getValue(Chat.class);
                    gettingDataFromUserTable(dataSnapshot.getKey(), chat);
                    rhide.setVisibility(View.GONE);
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.getValue(Chat.class) != null) {

                    Chat chat = dataSnapshot.getValue(Chat.class);
                    gettingDataFromUserTable(dataSnapshot.getKey(), chat);
                    rhide.setVisibility(View.GONE);
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                for (int i = 0; i < chatList.size(); i++) {
                    if (chatList.get(i).uid.equals(dataSnapshot.getKey())) {
                        chatList.remove(i);
                    }
                }
                if (chatList.size() == 0) {
                    rhide.setVisibility(View.VISIBLE);
                } else {
                    rhide.setVisibility(View.GONE);
                }
                msglstAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void gettingDataFromUserTable(final String key, final Chat chat) {
        FirebaseDatabase.getInstance().getReference().child(Constant.ARG_USERS).child(key).
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue(FirebaseUserModel.class) != null) {
                            FirebaseUserModel infoFCM = dataSnapshot.getValue(FirebaseUserModel.class);
                            userList.add(infoFCM);

                            for (FirebaseUserModel userInfoFCM : userList) {
                                if (userInfoFCM.uid.equals(key)) {

                                    chat.profilePic = userInfoFCM.profilePic;
                                    chat.name = userInfoFCM.name;
                                    chat.firebaseToken = userInfoFCM.firebaseToken;
                                    chat.uid = key;
                                }
                            }
                            mapList.put(chat.uid, chat);
                            chatList.clear();
                            Collection<Chat> values = mapList.values();
                            chatList.addAll(values);
                            shortList();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }


    private void shortList() {
        Collections.sort(chatList, new Comparator<Chat>() {

            @Override
            public int compare(Chat a1, Chat a2) {
                if (a1.timestamp == null || a2.timestamp == null)
                    return -1;
                else {
                    Long long1 = Long.valueOf(String.valueOf(a1.timestamp));
                    Long long2 = Long.valueOf(String.valueOf(a2.timestamp));
                    return long2.compareTo(long1);
                }
            }
        });
        msglstAdapter.notifyDataSetChanged();
    }
}
