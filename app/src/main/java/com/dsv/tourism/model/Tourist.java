package com.dsv.tourism.model;

import java.sql.Timestamp;

/**
 * Created by Vince on 30.08.2015.
 */
public class Tourist {
    /**
     * Item Id
     */
    @com.google.gson.annotations.SerializedName("id")
    private Integer mId;

    /**
     * Item text
     */
    @com.google.gson.annotations.SerializedName("reference")
    private String mReference;

    /**
     * Result answer id
     */
    @com.google.gson.annotations.SerializedName("creation_date")
    private Timestamp mCreationDate;

    public Tourist() {
    }

    public Tourist(Integer mId, String mReference, Timestamp mCreationDate) {
        this.mId = mId;
        this.mReference = mReference;
        this.mCreationDate = mCreationDate;
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
}
