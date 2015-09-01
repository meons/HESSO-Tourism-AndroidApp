package com.dsv.tourism.model;

/**
 * Created by Vince on 31.08.2015.
 */
public class Category {

    /**
     * Answer Id
     */
    @com.google.gson.annotations.SerializedName("id")
    private Integer mId;

    /**
     * Answer text
     */
    @com.google.gson.annotations.SerializedName("name")
    private String mName;

    public Category(Integer mId, String mName) {
        this.mId = mId;
        this.mName = mName;
    }

    public Integer getmId() {
        return mId;
    }

    public void setmId(Integer mId) {
        this.mId = mId;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }
}
