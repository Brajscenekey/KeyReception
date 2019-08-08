package com.key.keyreception.activity.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Ravi Birla on 28,March,2019
 */
public class PropertyData {


    public boolean isclick = false;
    private int _id;
    private String propertyName;
    private String bedroom;
    private String bathroom;
    private String propertySize;
    private String propertyAddress;
    private String propertyLat;
    private String propertyLong;
    private int userId;
    private int status;
    private String crd;
    private List<PropertyImgBean> propertyImg;
    private List<OwnerDetailBean> ownerDetail;

    public PropertyData(int _id, String propertyName, String bedroom, String bathroom, String propertySize, String propertyAddress, String propertyLat, String propertyLong, int userId, int status, String crd, List<PropertyImgBean> propertyImg, List<OwnerDetailBean> ownerDetail) {
        this._id = _id;
        this.propertyName = propertyName;
        this.bedroom = bedroom;
        this.bathroom = bathroom;
        this.propertySize = propertySize;
        this.propertyAddress = propertyAddress;
        this.propertyLat = propertyLat;
        this.propertyLong = propertyLong;
        this.userId = userId;
        this.status = status;
        this.crd = crd;
        this.propertyImg = propertyImg;
        this.ownerDetail = ownerDetail;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getBedroom() {
        return bedroom;
    }

    public void setBedroom(String bedroom) {
        this.bedroom = bedroom;
    }

    public String getBathroom() {
        return bathroom;
    }

    public void setBathroom(String bathroom) {
        this.bathroom = bathroom;
    }

    public String getPropertySize() {
        return propertySize;
    }

    public void setPropertySize(String propertySize) {
        this.propertySize = propertySize;
    }

    public String getPropertyAddress() {
        return propertyAddress;
    }

    public void setPropertyAddress(String propertyAddress) {
        this.propertyAddress = propertyAddress;
    }

    public String getPropertyLat() {
        return propertyLat;
    }

    public void setPropertyLat(String propertyLat) {
        this.propertyLat = propertyLat;
    }

    public String getPropertyLong() {
        return propertyLong;
    }

    public void setPropertyLong(String propertyLong) {
        this.propertyLong = propertyLong;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCrd() {
        return crd;
    }

    public void setCrd(String crd) {
        this.crd = crd;
    }

    public List<PropertyImgBean> getPropertyImg() {
        return propertyImg;
    }

    public void setPropertyImg(List<PropertyImgBean> propertyImg) {
        this.propertyImg = propertyImg;
    }

    public List<OwnerDetailBean> getOwnerDetail() {
        return ownerDetail;
    }

    public void setOwnerDetail(List<OwnerDetailBean> ownerDetail) {
        this.ownerDetail = ownerDetail;
    }

    public static class PropertyImgBean implements Serializable {
        /**
         * _id : 3
         * propertyImage : http://3.17.192.198:8042/uploads/property/prop_1556628503244.jpg
         */


        private int _id;
        private String propertyImage;

        public PropertyImgBean() {
        }
        public PropertyImgBean(int _id, String propertyImage) {
            this._id = _id;
            this.propertyImage = propertyImage;
        }

        public int get_id() {
            return _id;
        }

        public void set_id(int _id) {
            this._id = _id;
        }

        public String getPropertyImage() {
            return propertyImage;
        }

        public void setPropertyImage(String propertyImage) {
            this.propertyImage = propertyImage;
        }
    }

    public static class OwnerDetailBean {
        /**
         * _id : 2
         * profileImage :
         * fullName : deepika
         */


        private int _id;
        private String profileImage;
        private String fullName;
        public OwnerDetailBean(int _id, String profileImage, String fullName) {
            this._id = _id;
            this.profileImage = profileImage;
            this.fullName = fullName;
        }

        public int get_id() {
            return _id;
        }

        public void set_id(int _id) {
            this._id = _id;
        }

        public String getProfileImage() {
            return profileImage;
        }

        public void setProfileImage(String profileImage) {
            this.profileImage = profileImage;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }
    }
}


