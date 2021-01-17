package com.example.myapplicationinst.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.myapplicationinst.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyPagerAdapter extends PagerAdapter {

    private List<String> imageViewPagers;
    private Context mContext;
    private String mFrom;
    private ImageView mImageView;

    public MyPagerAdapter(List<String> imageViewPagers, Context mContext, String from) {
        this.imageViewPagers = imageViewPagers;
        this.mContext = mContext;
        mFrom = from;
    }

    @Override
    public int getCount() {
        return imageViewPagers.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE
        );
        View view = inflater.inflate(R.layout.fragment_viewpager_item, null);
        mImageView = view.findViewById(R.id.fragment_image);
        String path = imageViewPagers.get(position);
        if (mFrom.equals("Url")) {
            urlFun(path);
        } else if (mFrom.equals("Decode")) {
            decodeFun(path);
        }
        ViewPager viewPager = (ViewPager) container;
        viewPager.addView(view, 0);
        return view;
    }

    private void urlFun(String path) {
        Picasso
                .with(mContext)
                .load(path)
                .into(mImageView);
    }

    private void decodeFun(String path) {
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        mImageView.setImageBitmap(bitmap);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ViewPager viewPager = (ViewPager) container;
        View view = (View) object;
        viewPager.removeView(view);
    }
}