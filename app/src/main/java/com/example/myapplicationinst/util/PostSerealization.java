package com.example.myapplicationinst.util;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.myapplicationinst.model.Post;

public class PostSerealization implements Parcelable {
    Post post;

    public PostSerealization(Post post) {
        this.post = post;
    }

    protected PostSerealization(Parcel in) {
    }

    public static final Creator<PostSerealization> CREATOR = new Creator<PostSerealization>() {
        @Override
        public PostSerealization createFromParcel(Parcel in) {
            return new PostSerealization(in);
        }

        @Override
        public PostSerealization[] newArray(int size) {
            return new PostSerealization[size];
        }
    };

    public Post getPost() {
        return post;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
