package com.example.myapplicationinst;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplicationinst.util.ImageViewPager;
import com.squareup.picasso.Picasso;

public class ViewPagerItemFragment extends Fragment {
    private static final String TAG = "ViewPagerItemFragment";

    private ImageView imageView;

    private ImageViewPager mImageViewPager;

    public static ViewPagerItemFragment getInstance(ImageViewPager imageViewPager) {
        ViewPagerItemFragment fragment = new ViewPagerItemFragment();
        if (imageViewPager != null) {
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
        imageView = view.findViewById(R.id.fragment_image);
        init();
    }

    private void init() {
        Log.d(TAG, "init: --- " + mImageViewPager.getImgaeSrc());
        Picasso
                .with(getActivity())
                .load(Uri.parse(mImageViewPager.getImgaeSrc()))
                .into(imageView);
    }
}
