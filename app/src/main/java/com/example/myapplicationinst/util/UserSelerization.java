package com.example.myapplicationinst.util;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.myapplicationinst.model.User;

public class UserSelerization implements Parcelable {
    public static final Creator<UserSelerization> CREATOR = new Creator<UserSelerization>() {
        @Override
        public UserSelerization createFromParcel(Parcel in) {
            return new UserSelerization(in);
        }

        @Override
        public UserSelerization[] newArray(int size) {
            return new UserSelerization[size];
        }
    };
    private User user;

    public UserSelerization(User user) {
        this.user = user;
    }

    protected UserSelerization(Parcel in) {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
