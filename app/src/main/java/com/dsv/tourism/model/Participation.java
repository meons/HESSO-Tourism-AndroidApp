package com.dsv.tourism.model;

import java.sql.Timestamp;

/**
 * Created by Vince on 28.08.2015.
 */
public class Participation {
    /**
     * Result Id
     */
    @com.google.gson.annotations.SerializedName("id")
    private Integer mId;

    /**
     * Result answer id
     */
    @com.google.gson.annotations.SerializedName("quiz_id")
    private Integer mQuizId;

    /**
     * Result answer id
     */
    @com.google.gson.annotations.SerializedName("tourist_id")
    private Integer mTouristId;

    /**
     * Result answer id
     */
    @com.google.gson.annotations.SerializedName("created_at")
    private Timestamp mCreatedAt;

    public Participation() {
    }

    public Participation(Integer mId, Integer mQuizId, Integer mTouristId, Timestamp mCreatedAt) {
        this.mId = mId;
        this.mQuizId = mQuizId;
        this.mTouristId = mTouristId;
        this.mCreatedAt = mCreatedAt;
    }

    public Integer getmId() {
        return mId;
    }

    public void setmId(Integer mId) {
        this.mId = mId;
    }

    public Integer getmQuizId() {
        return mQuizId;
    }

    public void setmQuizId(Integer mQuizId) {
        this.mQuizId = mQuizId;
    }

    public Integer getmTouristId() {
        return mTouristId;
    }

    public void setmTouristId(Integer mTouristId) {
        this.mTouristId = mTouristId;
    }

    public Timestamp getmCreatedAt() {
        return mCreatedAt;
    }

    public void setmCreatedAt(Timestamp mCreatedAt) {
        this.mCreatedAt = mCreatedAt;
    }
}
