package com.key.keyreception.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.key.keyreception.Session;


/**
 * Created by Ravi Birla on 31,December,2018
 */
public class KeyReception extends Application{
    public static KeyReception mInstance;

    private Session session;
    private Activity activeActivity;

    //service tag
    private SharedPreferences mSharedPreferences;
    private static final String SHARED_PREF_NAME = "menuz_tag_preferences";

    public static KeyReception getInstance() {
        if (mInstance.mSharedPreferences == null) {
             mInstance.mSharedPreferences = mInstance.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        }
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setupActivityListener();
//        Fabric.with(this, new Crashlytics());

        mInstance = this;
        mInstance.getSessionManager();
        session.setIsOutCallFilter(false);


    }

    public Session getSessionManager() {
        if (session == null)
            session = new Session(getApplicationContext());
        return session;
    }


    /* public ArrayList<TaggedPhoto> getTaggedPhotos() {
String json = getString(Keys.TAGGED_PHOTOS.getKeyName());
ArrayList<TaggedPhoto> taggedPhotoArrayList;
if (!json.equals("")) {
taggedPhotoArrayList =
new Gson().fromJson(json, new TypeToken<ArrayList<TaggedPhoto>>() {
}.getType());
} else {
taggedPhotoArrayList = new ArrayList<>();
}
return taggedPhotoArrayList;
}

public void setTaggedPhotos(ArrayList<TaggedPhoto> taggedPhotoArrayList) {
putString(Keys.TAGGED_PHOTOS.getKeyName(), toJson(taggedPhotoArrayList));
}*/

    public void clear() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    private enum Keys {
        TAGGED_PHOTOS("TAGGED_PHOTOS");
        private final String keyName;

        Keys(String label) {
            this.keyName = label;
        }

        public String getKeyName() {
            return keyName;
        }
    }


    public static String toJson(Object object) {
        try {
//            Gson gson = new Gson();
//            return gson.toJson(object);
        } catch (Exception e) {
            Log.e(SHARED_PREF_NAME, "Error In Converting ModelToJson", e);
        }
        return "";
    }

    private void setupActivityListener() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
//                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

            }

            @Override
            public void onActivityStarted(Activity activity) {
            }

            @Override
            public void onActivityResumed(Activity activity) {
                activeActivity = activity;
            }

            @Override
            public void onActivityPaused(Activity activity) {
                activeActivity = null;
            }

            @Override
            public void onActivityStopped(Activity activity) {
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
            }
        });
    }

    public Activity getActiveActivity() {
        return activeActivity;
    }

}

