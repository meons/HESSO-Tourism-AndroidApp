package com.dsv.tourism.model;

/**
 * Created by Vince on 28.08.2015.
 */
public class Result {
    /**
     * Result Id
     */
    @com.google.gson.annotations.SerializedName("id")
    private Integer mId;

    /**
     * Result answer id
     */
    @com.google.gson.annotations.SerializedName("answer_id")
    private Integer mAnswerId;

    /**
     * Result tourist id
     */
    @com.google.gson.annotations.SerializedName("tourist_id")
    private Integer mTouristId;

    /**
     * Result quiz id
     */
    @com.google.gson.annotations.SerializedName("quiz_id")
    private Integer mQuizId;

    public Result() {
    }

    public Result(Integer mId, Integer mAnswerId, Integer mTouristId, Integer mQuizId) {
        this.mId = mId;
        this.mAnswerId = mAnswerId;
        this.mTouristId = mTouristId;
        this.mQuizId = mQuizId;
    }

    public Integer getmId() {
        return mId;
    }

    public void setmId(Integer mId) {
        this.mId = mId;
    }

    public Integer getmAnswerId() {
        return mAnswerId;
    }

    public void setmAnswerId(Integer mAnswerId) {
        this.mAnswerId = mAnswerId;
    }

    public Integer getmTouristId() {
        return mTouristId;
    }

    public void setmTouristId(Integer mTouristId) {
        this.mTouristId = mTouristId;
    }

    public Integer getmQuizId() {
        return mQuizId;
    }

    public void setmQuizId(Integer mQuizId) {
        this.mQuizId = mQuizId;
    }
}
