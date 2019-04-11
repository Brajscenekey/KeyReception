package com.key.keyreception;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Base64;


import com.key.keyreception.Activity.UserTypeActivity;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class Session {

    private static final String PREF_NAME = "imLink";
    private static final String PREF_NAME2 = "appSession";
    private static final String PREF_NAME3 = "newSession";
    private static final String IS_LOGGEDIN = "isLoggedIn";
    private static final String IS_FIrebaseLogin = "isFirebaseLogin";
    private static final String IS_UPDATE_UID = "isUpdateUid";
    public boolean IS_ownerlocation = false;
    public boolean IS_recelocation = false;
    private Context _context;
    private SharedPreferences mypref, mypref2,mypref3;
    private SharedPreferences.Editor editor, editor2,editor3;

    public Session(Context context) {
        this._context = context;
        mypref = _context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        mypref2 = _context.getSharedPreferences(PREF_NAME2, Context.MODE_PRIVATE);
        mypref3 = _context.getSharedPreferences(PREF_NAME3, Context.MODE_PRIVATE);
        editor = mypref.edit();
        editor2 = mypref2.edit();
        editor3 = mypref3.edit();
        editor.apply();
        editor2.apply();
        editor3.apply();
    }





    public void putusertype(String type){
        editor2.putString("type",type);

        editor2.apply();
    }
    public String getusertype(){
        return mypref2.getString("type", "");
    }



    public HashMap<String, String> getHeader() {
        HashMap<String, String> mHeaderMap = new HashMap<>();
        return mHeaderMap;
    }


    public boolean isUpdateUid() {
        return mypref.getBoolean(IS_UPDATE_UID, false);
    }

    public void setUpdateUid(boolean isUpdate) {
        editor.putBoolean(IS_UPDATE_UID, isUpdate);
        editor.commit();
    }

    public void createSession() {
        createSession(false);
    }

    private void createSession(boolean isFirebaseLogin) {
//        Gson gson = new Gson();
//        String json = gson.toJson(user); // myObject - instance of MyObject
//        editor.putString("user", json);

        editor.putBoolean(IS_LOGGEDIN, true);
        editor.putBoolean(IS_FIrebaseLogin, isFirebaseLogin);
        // editor.putString("authToken", user.authToken);
        editor.apply();
    }



    public void setPassword(String pwd) {
        try {
            byte[] data = pwd.getBytes("UTF-8");
            String base64 = Base64.encodeToString(data, Base64.DEFAULT);
            editor.putString("pwd", base64);
            editor.commit();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public String getPassword(){
        // Receiving side
        try {
            byte[] data = Base64.decode(mypref.getString("pwd", null), Base64.DEFAULT);
            return new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }



    public void putAuthtoken(String token){
        editor2.putString("token",token);
        editor2.apply();
    }

    public String getAuthtoken(){ return mypref2.getString("token", ""); }

    public void putjobid(String jobid){
        editor2.putString("jobid",jobid);
        editor2.apply();
    }

    public String getjobid(){ return mypref2.getString("jobid", ""); }







    public String getUpdatedinDb(){
        return mypref.getString("IsUpdated", "");
    }

    public boolean getIsOutCallFilter() {
        return mypref.getBoolean("outcall",false);
    }

    public void setIsOutCallFilter(boolean value){
        editor.putBoolean("outcall", value);
        this.editor.commit();
    }

    public String getUserChangedLocLat() {
        return mypref.getString("lat", "");
    }


    public void setUserChangedLocLat(String lat){
        editor.putString("lat", lat);
        this.editor.commit();
    }

    public String getUserChangedLocLng() {
        return mypref.getString("lng", "");
    }


    public void setUserChangedLocLng(String lng){
        editor.putString("lng", lng);
        this.editor.commit();
    }

    public String getUserChangedLocName() {
        return mypref.getString("locName", "");
    }

    public void setUserChangedLocName(String locName){
        editor.putString("locName", locName);
        this.editor.commit();
    }

    public boolean getIsFirebaseLogin() {
        return mypref.getBoolean(IS_FIrebaseLogin, false);
    }



    public void logout() {
        editor.clear();
        editor.apply();
       /* try {
            FirebaseInstanceId.getInstance().deleteInstanceId();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        Intent showLogin = new Intent(_context, UserTypeActivity.class);
        showLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        showLogin.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(showLogin);
    }

    public boolean isLoggedIn() {
        return mypref.getBoolean(IS_LOGGEDIN, false);
    }

    public void issetLoggedIn(boolean value)
    {
        editor.putBoolean(IS_LOGGEDIN, true);
        this.editor.apply();
    }



}
