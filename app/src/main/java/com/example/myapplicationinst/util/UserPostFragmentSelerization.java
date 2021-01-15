package com.example.myapplicationinst.util;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.myapplicationinst.model.Post;

import java.util.List;

public class UserPostFragmentSelerization implements Parcelable {
    public static final Creator<UserPostFragmentSelerization> CREATOR = new Creator<UserPostFragmentSelerization>() {
        @Override
        public UserPostFragmentSelerization createFromParcel(Parcel in) {
            return new UserPostFragmentSelerization(in);
        }

        @Override
        public UserPostFragmentSelerization[] newArray(int size) {
            return new UserPostFragmentSelerization[size];
        }
    };
    private List<Post> postList;

    public UserPostFragmentSelerization(List<Post> postList) {
        this.postList = postList;
    }

    protected UserPostFragmentSelerization(Parcel in) {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
