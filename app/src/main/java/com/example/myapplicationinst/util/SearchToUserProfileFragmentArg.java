package com.example.myapplicationinst.util;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.myapplicationinst.model.User;

public class SearchToUserProfileFragmentArg implements Parcelable {
    public static final Creator<SearchToUserProfileFragmentArg> CREATOR = new Creator<SearchToUserProfileFragmentArg>() {
        @Override
        public SearchToUserProfileFragmentArg createFromParcel(Parcel in) {
            return new SearchToUserProfileFragmentArg(in);
        }

        @Override
        public SearchToUserProfileFragmentArg[] newArray(int size) {
            return new SearchToUserProfileFragmentArg[size];
        }
    };
    private User user;

    public SearchToUserProfileFragmentArg(User user) {
        this.user = user;
    }

    protected SearchToUserProfileFragmentArg(Parcel in) {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public User getUser() {
        return user;
    }
}
