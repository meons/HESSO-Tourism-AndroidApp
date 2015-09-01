package com.dsv.tourism.model;

import java.sql.Date;

/**
 * Created by Vince on 24.08.2015.
 */
public class Quiz {

    /**
     * Quiz Id
     */
    @com.google.gson.annotations.SerializedName("id")
    private Integer mId;

    /**
     * Quiz name
     */
    @com.google.gson.annotations.SerializedName("name")
    private String mName;

    private Date mAnsweredDate;

    private int mTouristId;

    public Quiz(Integer mId, String mName) {
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

    public Date getmAnsweredDate() {
        return mAnsweredDate;
    }

    public void setmAnsweredDate(Date mAnsweredDate) {
        this.mAnsweredDate = mAnsweredDate;
    }

    public int getmTouristId() {
        return mTouristId;
    }

    public void setmTouristId(int mTouristId) {
        this.mTouristId = mTouristId;
    }
}
