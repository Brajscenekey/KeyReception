package com.key.keyreception.Activity.model;

import java.util.List;

/**
 * Created by Ravi Birla on 29,March,2019
 */
public class MyJobData {




        private int _id;
        private String propertyId;
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
        private List<CategoryBean> category;
        private List<OwnerDetailBean> ownerDetail;

    public List<OwnerDetailBean> getOwnerDetail() {
        return ownerDetail;
    }

    public void setOwnerDetail(List<OwnerDetailBean> ownerDetail) {
        this.ownerDetail = ownerDetail;
    }

    public MyJobData(int _id, String propertyId, String propertyName, String bedroom, String bathroom, String price, String propertySize, String serviceDate, String checkIn, String checkOut, String address, String latitude, String longitude, String description, int status, String crd, List<CategoryBean> category,List<OwnerDetailBean> ownerDetail) {
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
        this.category = category;
        this.ownerDetail = ownerDetail;
    }

    public int get_id() {
            return _id;
        }

        public void set_id(int _id) {
            this._id = _id;
        }

        public String getPropertyId() {
            return propertyId;
        }

        public void setPropertyId(String propertyId) {
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

        public List<CategoryBean> getCategory() {
            return category;
        }

        public void setCategory(List<CategoryBean> category) {
            this.category = category;
        }

        public static class CategoryBean {
            public CategoryBean(int _id, String title) {
                this._id = _id;
                this.title = title;
            }

            /**
             * _id : 2
             * title : Carpenter
             */


            private int _id;
            private String title;

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

    public static class OwnerDetailBean {
        public OwnerDetailBean(int _id, String profileImage,String fullName) {
            this._id = _id;
            this.profileImage = profileImage;
            this.fullName = fullName;
        }
        private int _id;
        private String profileImage;
        private String fullName;

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

