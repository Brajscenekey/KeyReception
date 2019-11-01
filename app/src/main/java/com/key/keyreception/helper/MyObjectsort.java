package com.key.keyreception.helper;

import java.util.Date;

/**
 * Created by Ravi Birla on 04,September,2019
 */
public class MyObjectsort implements Comparable<MyObjectsort> {

    private Date dateTime;

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date datetime) {
        this.dateTime = datetime;
    }

    @Override
    public int compareTo(MyObjectsort o) {
        if (getDateTime() == null || o.getDateTime() == null)
            return 0;
        return getDateTime().compareTo(o.getDateTime());
    }
}
