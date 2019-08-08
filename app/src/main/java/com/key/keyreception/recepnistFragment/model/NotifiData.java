package com.key.keyreception.recepnistFragment.model;

import java.util.List;

/**
 * Created by Ravi Birla on 16,April,2019
 */
public class NotifiData {



        private int _id;
        private int notifyId;
        private int senderId;
        private int receiverId;
        private int notifincationType;
        private String crd;
        private String forUserType;
        private String message;

    public String getForUserType() {
        return forUserType;
    }

    public void setForUserType(String forUserType) {
        this.forUserType = forUserType;
    }

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    private String current;
        private List<SenderDetailBean> senderDetail;

    public NotifiData(String forUserType,int _id, int notifyId, int senderId, int receiverId, int notifincationType, String crd, String message,String current, List<SenderDetailBean> senderDetail) {
        this.forUserType = forUserType;
        this._id = _id;
        this.notifyId = notifyId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.notifincationType = notifincationType;
        this.crd = crd;
        this.message = message;
        this.current = current;
        this.senderDetail = senderDetail;
    }

    public int get_id() {
            return _id;
        }

        public void set_id(int _id) {
            this._id = _id;
        }

        public int getNotifyId() {
            return notifyId;
        }

        public void setNotifyId(int notifyId) {
            this.notifyId = notifyId;
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

        public int getNotifincationType() {
            return notifincationType;
        }

        public void setNotifincationType(int notifincationType) {
            this.notifincationType = notifincationType;
        }

        public String getCrd() {
            return crd;
        }

        public void setCrd(String crd) {
            this.crd = crd;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public List<SenderDetailBean> getSenderDetail() {
            return senderDetail;
        }

        public void setSenderDetail(List<SenderDetailBean> senderDetail) {
            this.senderDetail = senderDetail;
        }

        public static class SenderDetailBean {
            public SenderDetailBean(int _id, String profileImage, String fullName) {
                this._id = _id;
                this.profileImage = profileImage;
                this.fullName = fullName;
            }

            /**
             * _id : 7
             * profileImage :
             * fullName : goku
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

