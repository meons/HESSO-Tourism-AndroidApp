package com.dsv.tourism.model;

/**
 * Created by Vince on 03.09.2015.
 */
public class RecommendationCriteria {

    /**
     * Quiz Id
     */
    @com.google.gson.annotations.SerializedName("id")
    private Integer mId;

    /**
     * RecommendationCriteria mThresholdMin
     */
    @com.google.gson.annotations.SerializedName("threshold_min")
    private Integer mThresholdMin;

    /**
     * RecommendationCriteria mThresholdMin
     */
    @com.google.gson.annotations.SerializedName("threshold_max")
    private Integer mThresholdMax;

    /**
     * RecommendationCriteria mThresholdMin
     */
    @com.google.gson.annotations.SerializedName("message")
    private String mMessage;

    public RecommendationCriteria(Integer mId, Integer mThresholdMin, Integer mThresholdMax, String mMessage) {
        this.mId = mId;
        this.mThresholdMin = mThresholdMin;
        this.mThresholdMax = mThresholdMax;
        this.mMessage = mMessage;
    }

    public Integer getmId() {
        return mId;
    }

    public void setmId(Integer mId) {
        this.mId = mId;
    }

    public Integer getmThresholdMin() {
        return mThresholdMin;
    }

    public void setmThresholdMin(Integer mThresholdMin) {
        this.mThresholdMin = mThresholdMin;
    }

    public Integer getmThresholdMax() {
        return mThresholdMax;
    }

    public void setmThresholdMax(Integer mThresholdMax) {
        this.mThresholdMax = mThresholdMax;
    }

    public String getmMessage() {
        return mMessage;
    }

    public void setmMessage(String mMessage) {
        this.mMessage = mMessage;
    }
}
