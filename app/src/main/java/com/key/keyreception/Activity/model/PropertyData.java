package com.key.keyreception.Activity.model;

/**
 * Created by Ravi Birla on 28,March,2019
 */
public class PropertyData {



        private int _id;
        private String propertyName;
        private String propertyImage;
        private String propertyAddress;
        private String propertyLat;
        private String propertyLong;
        private String propertyCheckIn;
        private String propertyCheckOut;

        public boolean isclick = false;

    public PropertyData(int _id, String propertyName, String propertyImage, String propertyAddress, String propertyLat, String propertyLong, String propertyCheckIn, String propertyCheckOut) {
        this._id = _id;
        this.propertyName = propertyName;
        this.propertyImage = propertyImage;
        this.propertyAddress = propertyAddress;
        this.propertyLat = propertyLat;
        this.propertyLong = propertyLong;
        this.propertyCheckIn = propertyCheckIn;
        this.propertyCheckOut = propertyCheckOut;
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

        public String getPropertyImage() {
            return propertyImage;
        }

        public void setPropertyImage(String propertyImage) {
            this.propertyImage = propertyImage;
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

        public String getPropertyCheckIn() {
            return propertyCheckIn;
        }

        public void setPropertyCheckIn(String propertyCheckIn) {
            this.propertyCheckIn = propertyCheckIn;
        }

        public String getPropertyCheckOut() {
            return propertyCheckOut;
        }

        public void setPropertyCheckOut(String propertyCheckOut) {
            this.propertyCheckOut = propertyCheckOut;
        }
    }

