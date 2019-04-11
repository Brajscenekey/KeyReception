package com.key.keyreception.Activity.model;

import java.util.List;

/**
 * Created by Ravi Birla on 03,April,2019
 */
public class RequestData {


    public RequestData(int _id,int jobId, int senderId, int receiverId, String requestTime, String expireTime, List<CategoryBean> category, List<SenderDetailBean> senderDetail, List<ReciverDetailBean> reciverDetail, List<JobDetailBean> jobDetail) {
        this._id = _id;
        this.jobId = jobId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.requestTime = requestTime;
        this.expireTime = expireTime;
        this.category = category;
        this.senderDetail = senderDetail;
        this.reciverDetail = reciverDetail;
        this.jobDetail = jobDetail;
    }

    /**
         * _id : 1
         * senderId : 1
         * receiverId : 2
         * requestTime : Tue Apr 02 2019 12:30:16 GMT+0000
         * expireTime : Tue Apr 02 2019 13:00:16 GMT+0000 (UTC)
         * category : [{"_id":2,"title":"Carpenter"}]
         * senderDetail : [{"_id":1,"profileImage":"","fullName":"Raj"}]
         * reciverDetail : [{"_id":2,"profileImage":"","fullName":"Vijay"}]
         * jobDetail : [{"propertyId":"6","propertyName":"FabHotel Prime Avenue","bedroom":"1","bathroom":"1","price":"123","propertySize":"1234","serviceDate":"2019-04-02 17:59:44","checkIn":"30, April 10:00 AM","checkOut":"30, April 10:00 PM","address":"502, 503 & 504 Krishna Tower Above ICICI Bank, Main Rd, Brajeshwari Extension, Pipliyahana, Indore, Madhya Pradesh 452016, India","latitude":"22.705138200000004","longitude":"75.9090618","description":"Welcome to","status":1}]
         */



        private int _id;
        private int jobId;
        private int senderId;
        private int receiverId;
        private String requestTime;
        private String expireTime;
        private List<CategoryBean> category;
        private List<SenderDetailBean> senderDetail;
        private List<ReciverDetailBean> reciverDetail;
        private List<JobDetailBean> jobDetail;

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public int get_id() {
            return _id;
        }

        public void set_id(int _id) {
            this._id = _id;
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

        public String getRequestTime() {
            return requestTime;
        }

        public void setRequestTime(String requestTime) {
            this.requestTime = requestTime;
        }

        public String getExpireTime() {
            return expireTime;
        }

        public void setExpireTime(String expireTime) {
            this.expireTime = expireTime;
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

        public List<ReciverDetailBean> getReciverDetail() {
            return reciverDetail;
        }

        public void setReciverDetail(List<ReciverDetailBean> reciverDetail) {
            this.reciverDetail = reciverDetail;
        }

        public List<JobDetailBean> getJobDetail() {
            return jobDetail;
        }

        public void setJobDetail(List<JobDetailBean> jobDetail) {
            this.jobDetail = jobDetail;
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

        public static class SenderDetailBean {
            public SenderDetailBean(int _id, String profileImage, String fullName) {
                this._id = _id;
                this.profileImage = profileImage;
                this.fullName = fullName;
            }

            /**
             * _id : 1
             * profileImage :
             * fullName : Raj
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

        public static class ReciverDetailBean {
            public ReciverDetailBean(int _id, String profileImage, String fullName) {
                this._id = _id;
                this.profileImage = profileImage;
                this.fullName = fullName;
            }

            /**
             * _id : 2
             * profileImage :
             * fullName : Vijay
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

        public static class JobDetailBean {
            public JobDetailBean(String propertyId, String propertyName, String bedroom, String bathroom, String price, String propertySize, String serviceDate, String checkIn, String checkOut, String address, String latitude, String longitude, String description, int status) {
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
            }

            /**
             * propertyId : 6
             * propertyName : FabHotel Prime Avenue
             * bedroom : 1
             * bathroom : 1
             * price : 123
             * propertySize : 1234
             * serviceDate : 2019-04-02 17:59:44
             * checkIn : 30, April 10:00 AM
             * checkOut : 30, April 10:00 PM
             * address : 502, 503 & 504 Krishna Tower Above ICICI Bank, Main Rd, Brajeshwari Extension, Pipliyahana, Indore, Madhya Pradesh 452016, India
             * latitude : 22.705138200000004
             * longitude : 75.9090618
             * description : Welcome to
             * status : 1
             */



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
        }
    }

