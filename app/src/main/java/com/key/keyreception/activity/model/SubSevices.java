package com.key.keyreception.activity.model;

/**
 * Created by Ravi Birla on 28,March,2019
 */
public class SubSevices {


    private int _id;
    private String title;
    private String price;
    public int count ;
    public boolean isselect ;


    public SubSevices(int _id, String title, String price,int count, boolean isselect) {
        this._id = _id;
        this.title = title;
        this.price = price;
        this.count = count;
        this.isselect = isselect;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isIsselect() {
        return isselect;
    }

    public void setIsselect(boolean isselect) {
        this.isselect = isselect;
    }
}

