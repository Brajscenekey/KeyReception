package com.key.keyreception.recepnistChildFragment.model;

/**
 * Created by Ravi Birla on 20,September,2019
 */
public class RatingModel {


    private int _id;
    private int rating;
    private String review;
    private String crd;
    private int jobId;
    private int _userid;
    private String profileImage;
    private String fullName;


    public RatingModel(int _id, int rating, String review, String crd, int jobId, int _userid, String profileImage, String fullName) {
        this._id = _id;
        this.rating = rating;
        this.review = review;
        this.crd = crd;
        this.jobId = jobId;
        this._userid = _userid;
        this.profileImage = profileImage;
        this.fullName = fullName;
    }

    public int get_userid() {
        return _userid;
    }

    public void set_userid(int _userid) {
        this._userid = _userid;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getCrd() {
        return crd;
    }

    public void setCrd(String crd) {
        this.crd = crd;
    }

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

