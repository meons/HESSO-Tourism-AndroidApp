package com.dsv.tourism.model;

/**
 * Created by Vince on 27.08.2015.
 */
public class Question {
    /**
     * Item Id
     */
    @com.google.gson.annotations.SerializedName("id")
    private Integer mId;

    /**
     * Item text
     */
    @com.google.gson.annotations.SerializedName("text")
    private String mText;

    /**
     * Item text
     */
    @com.google.gson.annotations.SerializedName("quiz_id")
    private Integer mQuizId;

    public Question(Integer mId, String mText) {
        this.mId = mId;
        this.mText = mText;
    }

    public Integer getmId() {
        return mId;
    }

    public void setmId(Integer mId) {
        this.mId = mId;
    }

    public String getmText() {
        return mText;
    }

    public void setmText(String mText) {
        this.mText = mText;
    }

    public Integer getmQuizId() {
        return mQuizId;
    }

    public void setmQuizId(Integer mQuizId) {
        this.mQuizId = mQuizId;
    }
}
