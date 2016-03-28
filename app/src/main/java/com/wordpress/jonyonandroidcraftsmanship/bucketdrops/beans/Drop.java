package com.wordpress.jonyonandroidcraftsmanship.bucketdrops.beans;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Drop extends RealmObject{

    private String what = null;
    @PrimaryKey
    private long added = 0;
    private long when = 0;
    private boolean isCompleted = false;

    public Drop() {

    }

    public Drop(String what, long added, long when, boolean isCompleted) {
        this.what = what;
        this.added = added;
        this.when = when;
        this.isCompleted = isCompleted;
    }

    public String getWhat() {
        return what;
    }

    public void setWhat(String what) {
        this.what = what;
    }

    public long getAdded() {
        return added;
    }

    public void setAdded(long added) {
        this.added = added;
    }

    public long getWhen() {
        return when;
    }

    public void setWhen(long when) {
        this.when = when;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }
}
