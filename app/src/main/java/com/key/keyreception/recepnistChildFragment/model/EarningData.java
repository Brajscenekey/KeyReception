package com.key.keyreception.recepnistChildFragment.model;

import java.util.List;

/**
 * Created by Ravi Birla on 27,April,2019
 */
public class EarningData {



        private int _id;
        private int jobId;
        private int senderId;
        private int receiverId;
        private String amount;
        private String paymentType;
        private int status;
        private JobDetailBean jobDetail;
        private List<CategoryBean> category;
        private List<SenderDetailBean> senderDetail;

    public EarningData(int _id, int jobId, int senderId, int receiverId, String amount, String paymentType, int status, JobDetailBean jobDetail, List<CategoryBean> category, List<SenderDetailBean> senderDetail) {
        this._id = _id;
        this.jobId = jobId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.amount = amount;
        this.paymentType = paymentType;
        this.status = status;
        this.jobDetail = jobDetail;
        this.category = category;
        this.senderDetail = senderDetail;
    }

    public int get_id() {
            return _id;
        }

        public void set_id(int _id) {
            this._id = _id;
        }

        public int getJobId() {
            return jobId;
        }

        public void setJobId(int jobId) {
            this.jobId = jobId;
        }

        public int getSenderId() {
            return senderId;
        }

        public void setSenderId(int senderId) {
            this.senderId = senderId;
        }

        public int getReceiverId() {
            return receiverId;
        }

        public void setReceiverId(int receiverId) {
            this.receiverId = receiverId;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getPaymentType() {
            return paymentType;
        }

        public void setPaymentType(String paymentType) {
            this.paymentType = paymentType;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public JobDetailBean getJobDetail() {
            return jobDetail;
        }

        public void setJobDetail(JobDetailBean jobDetail) {
            this.jobDetail = jobDetail;
        }

        public List<CategoryBean> getCategory() {
            return category;
        }

        public void setCategory(List<CategoryBean> category) {
            this.category = category;
        }

        public List<SenderDetailBean> getSenderDetail() {
            return senderDetail;
        }

        public void setSenderDetail(List<SenderDetailBean> senderDetail) {
            this.senderDetail = senderDetail;
        }

        public static class JobDetailBean {
            /**
             * propertyId : 2
             * propertyName : Effotel Hotel
             * price : 1
             * serviceDate : 2019-04-27 13:52:00
             * checkIn : 15,March 12:00 AM
             * checkOut : 15,March 12:00 AM
             * address : vijay nagar indore
             * latitude : 22.7532848
             * longitude : 75.8936962
             * description : 1
             * status : 5
             */

            private String propertyId;
            private String propertyName;
            private String price;
            private String serviceDate;
            private String checkIn;
            private String checkOut;
            private String address;
            private String latitude;
            private String longitude;
            private String description;
            private int status;


            public JobDetailBean(String propertyId, String propertyName, String price, String serviceDate, String checkIn, String checkOut, String address, String latitude, String longitude, String description, int status) {
                this.propertyId = propertyId;
                this.propertyName = propertyName;
                this.price = price;
                this.serviceDate = serviceDate;
                this.checkIn = checkIn;
                this.checkOut = checkOut;
                this.address = address;
                this.latitude = latitude;
                this.longitude = longitude;
                this.description = description;
                this.status = status;
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

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
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
        }

        public static class CategoryBean {
            public CategoryBean(int _id, String title) {
                this._id = _id;
                this.title = title;
            }

            /**
             * _id : 1
             * title : Pulmber
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

        public static class SenderDetailBean {
            public SenderDetailBean(int _id, String profileImage, String fullName) {
                this._id = _id;
                this.profileImage = profileImage;
                this.fullName = fullName;
            }

            /**
             * _id : 12
             * profileImage :
             * fullName : deepika
             */


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

