package com.key.keyreception.activity.model;

import java.util.List;

/**
 * Created by Ravi Birla on 29,March,2019
 */
public class MyJobData {


    private int _id;
    private int propertyId;
    private String propertyName;
    private String bedroom;
    private String bathroom;
    private String price;
    private String propertySize;
    private String serviceDate;
    private String checkIn;
    private String checkOut;
    private String address;
    private String latitude;
    private String longitude;
    private String description;
    private int status;
    private String crd;
    private List<PropertyDataBean> propertyData;
    private List<PropertyImgBean> propertyImg;
    private List<OwnerDetailBean> ownerDetail;
    private List<?> receptionistDetail;
    private List<CategoryBean> category;

    public MyJobData(int _id, int propertyId, String propertyName, String bedroom, String bathroom, String price, String propertySize, String serviceDate, String checkIn, String checkOut, String address, String latitude, String longitude, String description, int status, String crd, List<PropertyDataBean> propertyData, List<PropertyImgBean> propertyImg, List<OwnerDetailBean> ownerDetail, List<CategoryBean> category) {
        this._id = _id;
        this.propertyId = propertyId;
        this.propertyName = propertyName;
        this.bedroom = bedroom;
        this.bathroom = bathroom;
        this.price = price;
        this.propertySize = propertySize;
        this.serviceDate = serviceDate;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        this.status = status;
        this.crd = crd;
        this.propertyData = propertyData;
        this.propertyImg = propertyImg;
        this.ownerDetail = ownerDetail;
        this.category = category;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(int propertyId) {
        this.propertyId = propertyId;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPropertySize() {
        return propertySize;
    }

    public void setPropertySize(String propertySize) {
        this.propertySize = propertySize;
    }

    public String getServiceDate() {
        return serviceDate;
    }

    public void setServiceDate(String serviceDate) {
        this.serviceDate = serviceDate;
    }

    public String getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(String checkIn) {
        this.checkIn = checkIn;
    }

    public String getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(String checkOut) {
        this.checkOut = checkOut;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public List<PropertyDataBean> getPropertyData() {
        return propertyData;
    }

    public void setPropertyData(List<PropertyDataBean> propertyData) {
        this.propertyData = propertyData;
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

    public List<?> getReceptionistDetail() {
        return receptionistDetail;
    }

    public void setReceptionistDetail(List<?> receptionistDetail) {
        this.receptionistDetail = receptionistDetail;
    }

    public List<CategoryBean> getCategory() {
        return category;
    }

    public void setCategory(List<CategoryBean> category) {
        this.category = category;
    }

    public static class PropertyDataBean {
        /**
         * bathroom : 4
         * bedroom : 5
         * isImageAdd : 1
         * propertyAddress : 502, 503 & 504 Krishna Tower Above ICICI Bank, Main Rd, Brajeshwari Extension, Pipliyahana, Indore, Madhya Pradesh 452016, India
         * propertyId : 9
         * propertyLat : 22.7054662
         * propertyLong : 75.9085569
         * propertyName : Developer
         * propertySize : 12
         */


        private String bathroom;
        private String bedroom;
        private String isImageAdd;
        private String propertyAddress;
        private String propertyId;
        private String propertyLat;
        private String propertyLong;
        private String propertyName;
        private String propertySize;
        public PropertyDataBean(String bathroom, String bedroom, String isImageAdd, String propertyAddress, String propertyId, String propertyLat, String propertyLong, String propertyName, String propertySize) {
            this.bathroom = bathroom;
            this.bedroom = bedroom;
            this.isImageAdd = isImageAdd;
            this.propertyAddress = propertyAddress;
            this.propertyId = propertyId;
            this.propertyLat = propertyLat;
            this.propertyLong = propertyLong;
            this.propertyName = propertyName;
            this.propertySize = propertySize;
        }

        public String getBathroom() {
            return bathroom;
        }

        public void setBathroom(String bathroom) {
            this.bathroom = bathroom;
        }

        public String getBedroom() {
            return bedroom;
        }

        public void setBedroom(String bedroom) {
            this.bedroom = bedroom;
        }

        public String getIsImageAdd() {
            return isImageAdd;
        }

        public void setIsImageAdd(String isImageAdd) {
            this.isImageAdd = isImageAdd;
        }

        public String getPropertyAddress() {
            return propertyAddress;
        }

        public void setPropertyAddress(String propertyAddress) {
            this.propertyAddress = propertyAddress;
        }

        public String getPropertyId() {
            return propertyId;
        }

        public void setPropertyId(String propertyId) {
            this.propertyId = propertyId;
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

        public String getPropertyName() {
            return propertyName;
        }

        public void setPropertyName(String propertyName) {
            this.propertyName = propertyName;
        }

        public String getPropertySize() {
            return propertySize;
        }

        public void setPropertySize(String propertySize) {
            this.propertySize = propertySize;
        }
    }

    public static class PropertyImgBean {
        /**
         * _id : 16
         * propertyImage : http://3.17.192.198:8042/uploads/property/prop_1557831002741.jpg
         */


        private int _id;
        private String propertyImage;
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
         * _id : 6
         * profileImage :
         * fullName : vivek
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

    public static class CategoryBean {
        /**
         * _id : 1
         * title : Pulmber
         */


        private int _id;
        private String title;
        public CategoryBean(int _id, String title) {
            this._id = _id;
            this.title = title;
        }

        public int get_id() {
            return _id;
        }

        public void set_id(int _id) {
            this._id = _id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }




   /* private int _id;
    private int propertyId;
    private String propertyName;
    private String bedroom;
    private String bathroom;
    private String price;
    private String propertySize;
    private String serviceDate;
    private String checkIn;
    private String checkOut;
    private String address;
    private String latitude;
    private String longitude;
    private String description;
    private int status;
    private String crd;
    private List<PropertyImgBean> propertyImg;
    private List<OwnerDetailBean> ownerDetail;
    private List<?> receptionistDetail;
    private List<CategoryBean> category;

    public MyJobData(int _id, int propertyId, String propertyName, String bedroom, String bathroom, String price, String propertySize, String serviceDate, String checkIn, String checkOut, String address, String latitude, String longitude, String description, int status, String crd, List<PropertyImgBean> propertyImg, List<OwnerDetailBean> ownerDetail, List<CategoryBean> category) {
        this._id = _id;
        this.propertyId = propertyId;
        this.propertyName = propertyName;
        this.bedroom = bedroom;
        this.bathroom = bathroom;
        this.price = price;
        this.propertySize = propertySize;
        this.serviceDate = serviceDate;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        this.status = status;
        this.crd = crd;
        this.propertyImg = propertyImg;
        this.ownerDetail = ownerDetail;
        this.category = category;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(int propertyId) {
        this.propertyId = propertyId;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPropertySize() {
        return propertySize;
    }

    public void setPropertySize(String propertySize) {
        this.propertySize = propertySize;
    }

    public String getServiceDate() {
        return serviceDate;
    }

    public void setServiceDate(String serviceDate) {
        this.serviceDate = serviceDate;
    }

    public String getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(String checkIn) {
        this.checkIn = checkIn;
    }

    public String getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(String checkOut) {
        this.checkOut = checkOut;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public List<?> getReceptionistDetail() {
        return receptionistDetail;
    }

    public void setReceptionistDetail(List<?> receptionistDetail) {
        this.receptionistDetail = receptionistDetail;
    }

    public List<CategoryBean> getCategory() {
        return category;
    }

    public void setCategory(List<CategoryBean> category) {
        this.category = category;
    }

    public static class PropertyImgBean {
        *//**
     * _id : 1
     * propertyImage : http://3.17.192.198:8042/uploads/property/prop_1556792771369.jpg
     *//*


        private int _id;
        private String propertyImage;
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
        *//**
     * _id : 2
     * profileImage : http://3.17.192.198:8042/uploads/profile/profile1556775100740.jpg
     * fullName : deepak
     *//*

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

    public static class CategoryBean {
        *//**
     * _id : 1
     * title : Pulmber
     *//*


        private int _id;
        private String title;
        public CategoryBean(int _id, String title) {
            this._id = _id;
            this.title = title;
        }

        public int get_id() {
            return _id;
        }

        public void set_id(int _id) {
            this._id = _id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }*/
}


