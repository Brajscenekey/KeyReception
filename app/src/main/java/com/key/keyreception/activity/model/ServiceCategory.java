package com.key.keyreception.activity.model;

/**
 * Created by Ravi Birla on 28,March,2019
 */
public class ServiceCategory {


    public boolean isselect = false;
    private int _id;
    private String title;
    private String image;
    private String imageurl;


    public ServiceCategory(int _id, String title, String image, String imageurl) {
        this._id = _id;
        this.title = title;
        this.image = image;
        this.imageurl = imageurl;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }
}
