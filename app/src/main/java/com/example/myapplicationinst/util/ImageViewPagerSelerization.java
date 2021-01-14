package com.example.myapplicationinst.util;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class ImageViewPagerSelerization implements Parcelable {
    public static final Creator<ImageViewPagerSelerization> CREATOR = new Creator<ImageViewPagerSelerization>() {
        @Override
        public ImageViewPagerSelerization createFromParcel(Parcel in) {
            return new ImageViewPagerSelerization(in);
        }

        @Override
        public ImageViewPagerSelerization[] newArray(int size) {
            return new ImageViewPagerSelerization[size];
        }
    };
    private List<ImageViewPager> imageViewPagers;

    public ImageViewPagerSelerization(List<ImageViewPager> imageViewPagers) {
        this.imageViewPagers = imageViewPagers;
    }

    protected ImageViewPagerSelerization(Parcel in) {
        imageViewPagers = in.createTypedArrayList(ImageViewPager.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(imageViewPagers);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public List<ImageViewPager> getImageViewPagers() {
        return imageViewPagers;
    }
}
