package com.key.keyreception.activity.model;

/**
 * Created by Ravi Birla on 22,August,2019
 */
public class Addservicemodel {
    public int count ;
    public boolean isselect ;

    public Addservicemodel() {
    }

    public Addservicemodel(int count, boolean isselect) {
        this.count = count;
        this.isselect = isselect;
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
