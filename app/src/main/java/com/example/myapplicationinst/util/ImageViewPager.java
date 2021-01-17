package com.example.myapplicationinst.util;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;


public class ImageViewPager implements Parcelable {

    private String imgaeSrc;

    public ImageViewPager() {
    }

    public ImageViewPager(String imgaeSrc) {
        this.imgaeSrc = imgaeSrc;
    }

    protected ImageViewPager(Parcel in) {
        imgaeSrc = in.readString();
    }

    public static final Creator<ImageViewPager> CREATOR = new Creator<ImageViewPager>() {
        @Override
        public ImageViewPager createFromParcel(Parcel in) {
            return new ImageViewPager(in);
        }

        @Override
        public ImageViewPager[] newArray(int size) {
            return new ImageViewPager[size];
        }
    };



    public String getImgaeSrc() {
        return imgaeSrc;
    }

    public void setImgaeSrc(String imgaeSrc) {
        this.imgaeSrc = imgaeSrc;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imgaeSrc);
    }
}
