package com.dsv.tourism.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Date;

/**
 * Created by Vince on 24.08.2015.
 */
public class Quiz implements Parcelable {

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

    private int mParticipationId;

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

    public int getmParticipationId() {
        return mParticipationId;
    }

    public void setmParticipationId(int mParticipationId) {
        this.mParticipationId = mParticipationId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mName);
        dest.writeLong(mAnsweredDate.getTime());
        dest.writeInt(mParticipationId);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<Quiz> CREATOR = new Parcelable.Creator<Quiz>() {
        public Quiz createFromParcel(Parcel in) {
            return new Quiz(in);
        }

        public Quiz[] newArray(int size) {
            return new Quiz[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private Quiz(Parcel in) {
        mId = in.readInt();
        mName = in.readString();
        mAnsweredDate = new Date(in.readLong());
        mParticipationId = in.readInt();
    }
}
