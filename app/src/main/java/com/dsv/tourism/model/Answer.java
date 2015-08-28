package com.dsv.tourism.model;

/**
 * Created by Vince on 27.08.2015.
 */
public class Answer {
    /**
     * Answer Id
     */
    @com.google.gson.annotations.SerializedName("id")
    private Integer mId;

    /**
     * Answer text
     */
    @com.google.gson.annotations.SerializedName("text")
    private String mText;

    /**
     * Answer description
     */
    @com.google.gson.annotations.SerializedName("description")
    private String mDescription;

    /**
     * Answer score
     */
    @com.google.gson.annotations.SerializedName("score")
    private Integer mScore;

    /**
     * Answer score
     */
    @com.google.gson.annotations.SerializedName("next_question_id")
    private Integer mNextQuestionId;

    public Answer() {
    }

    public Answer(Integer mId, String mText, String mDescription, Integer mScore) {
        this.mId = mId;
        this.mText = mText;
        this.mDescription = mDescription;
        this.mScore = mScore;
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

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public Integer getmScore() {
        return mScore;
    }

    public void setmScore(Integer mScore) {
        this.mScore = mScore;
    }

    public Integer getmNextQuestionId() {
        return mNextQuestionId;
    }

    public void setmNextQuestionId(Integer mNextQuestionId) {
        this.mNextQuestionId = mNextQuestionId;
    }
}
