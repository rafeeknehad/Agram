package com.example.myapplicationinst.util;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.myapplicationinst.model.Post;

import java.util.List;

public class UserPostFragmentSelerization implements Parcelable {

    private List<Post> postList;
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
    private int pos;

    public UserPostFragmentSelerization(List<Post> postList, int pos) {
        this.postList = postList;
        this.pos = pos;
    }

    protected UserPostFragmentSelerization(Parcel in) {
        pos = in.readInt();
    }

    public int getPos() {
        return pos;
    }


    public List<Post> getPostList() {
        return postList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(pos);
    }
}
