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
     * Result participation id
     */
    @com.google.gson.annotations.SerializedName("participation_id")
    private Integer mParticipationId;

    public Result() {
    }

    public Result(Integer mId, Integer mAnswerId, Integer mTouristId, Integer mParticipationId) {
        this.mId = mId;
        this.mAnswerId = mAnswerId;
        this.mTouristId = mTouristId;
        this.mParticipationId = mParticipationId;
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

    public Integer getmParticipationId() {
        return mParticipationId;
    }

    public void setmParticipationId(Integer mParticipationId) {
        this.mParticipationId = mParticipationId;
    }
}
