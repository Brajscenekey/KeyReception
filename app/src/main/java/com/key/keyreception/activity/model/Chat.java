package com.key.keyreception.activity.model;

import java.io.Serializable;

/**
 * Created by Ravi Birla on 23,January,2019
 */
public class Chat implements Serializable {
    public String deleteby;
    public String firebaseId;
    public String firebaseToken;


    public int isImage;
    public String imageUrl = "";
    public String message;
    public String name;
    public String profilePic;
    public Object timestamp;
    public String uid;
    public String lastMsg;
    public String banner_date;
    public String type;
    public int unreadCount;

    public String getDeleteby() {
        return deleteby;
    }

    public void setDeleteby(String deleteby) {
        this.deleteby = deleteby;
    }

    public String getFirebaseId() {
        return firebaseId;
    }

    public void setFirebaseId(String firebaseId) {
        this.firebaseId = firebaseId;
    }

    public String getFirebaseToken() {
        return firebaseToken;
    }

    public void setFirebaseToken(String firebaseToken) {
        this.firebaseToken = firebaseToken;
    }

    public int getIsImage() {
        return isImage;
    }

    public void setIsImage(int isImage) {
        this.isImage = isImage;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public Object getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Object timestamp) {
        this.timestamp = timestamp;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getLastMsg() {
        return lastMsg;
    }

    public void setLastMsg(String lastMsg) {
        this.lastMsg = lastMsg;
    }

    public String getBanner_date() {
        return banner_date;
    }

    public void setBanner_date(String banner_date) {
        this.banner_date = banner_date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }
}