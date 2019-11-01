package com.key.keyreception.activity.model;

/**
 * Created by Ravi Birla on 19,September,2019
 */
public class RatingInfo {


    public RatingInfo(int _id, int rating, String review, String givenTo, String crd, int jobId, int ownerId, int receptionistId, int __v) {
        this._id = _id;
        this.rating = rating;
        this.review = review;
        this.givenTo = givenTo;
        this.crd = crd;
        this.jobId = jobId;
        this.ownerId = ownerId;
        this.receptionistId = receptionistId;
        this.__v = __v;
    }

    private int _id;
    private int rating;
    private String review;
    private String givenTo;
    private String crd;
    private int jobId;
    private int ownerId;
    private int receptionistId;
    private int __v;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
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

    public String getGivenTo() {
        return givenTo;
    }

    public void setGivenTo(String givenTo) {
        this.givenTo = givenTo;
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

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public int getReceptionistId() {
        return receptionistId;
    }

    public void setReceptionistId(int receptionistId) {
        this.receptionistId = receptionistId;
    }

    public int get__v() {
        return __v;
    }

    public void set__v(int __v) {
        this.__v = __v;
    }

}
