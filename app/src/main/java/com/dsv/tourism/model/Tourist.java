package com.dsv.tourism.model;

import java.sql.Timestamp;

/**
 * Created by Vince on 30.08.2015.
 */
public class Tourist {
    /**
     * Tourist Id
     */
    @com.google.gson.annotations.SerializedName("id")
    private Integer mId;

    /**
     * Tourist reference
     */
    @com.google.gson.annotations.SerializedName("reference")
    private String mReference;

    /**
     * Tourist creation date
     */
    @com.google.gson.annotations.SerializedName("creation_date")
    private Timestamp mCreationDate;

    /**
     * Tourist birth date
     */
    @com.google.gson.annotations.SerializedName("birth_date")
    private Timestamp mBirthDate;

    /**
     * Tourist Id
     */
    @com.google.gson.annotations.SerializedName("gender")
    private Integer mGender;

    public Tourist() {
    }

    public Tourist(Integer mId, String mReference, Timestamp mCreationDate, Timestamp mBirthDate, Integer mGender) {
        this.mId = mId;
        this.mReference = mReference;
        this.mCreationDate = mCreationDate;
        this.mBirthDate = mBirthDate;
        this.mGender = mGender;
    }

    public Integer getmId() {
        return mId;
    }

    public void setmId(Integer mId) {
        this.mId = mId;
    }

    public String getmReference() {
        return mReference;
    }

    public void setmReference(String mReference) {
        this.mReference = mReference;
    }

    public Timestamp getmCreationDate() {
        return mCreationDate;
    }

    public void setmCreationDate(Timestamp mCreationDate) {
        this.mCreationDate = mCreationDate;
    }

    public Timestamp getmBirthDate() {
        return mBirthDate;
    }

    public void setmBirthDate(Timestamp mBirthDate) {
        this.mBirthDate = mBirthDate;
    }

    public Integer getmGender() {
        return mGender;
    }

    public void setmGender(Integer mGender) {
        this.mGender = mGender;
    }
}
