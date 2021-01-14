package com.example.myapplicationinst;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplicationinst.util.ImageViewPager;

public class ViewPagerItemFragment extends Fragment {
    private ImageView imageView;


    private ImageViewPager mImageViewPager;

    public static ViewPagerItemFragment getInstance(ImageViewPager imageViewPager)
    {
        ViewPagerItemFragment fragment = new ViewPagerItemFragment();
        if(imageViewPager!=null)
        {
            Bundle bundle = new Bundle();
            bundle.putParcelable("image",imageViewPager);
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null)
        {
            mImageViewPager = getArguments().getParcelable("image");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_viewpager_item,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        imageView = view.findViewById(R.id.image);
        init();
    }

    private void init() {
        Bitmap bitmap = BitmapFactory.decodeFile(mImageViewPager.getImgaeSrc());
        imageView.setImageBitmap(bitmap);
    }


}
