package com.example.myapplicationinst;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplicationinst.adapter.AdapterImagesGallery;
import com.example.myapplicationinst.util.ImageGallery;

import java.util.List;

public class AddPhotoFragment extends Fragment {

    private static final int READ_PERMISSION_CODE = 1;
    private static final String TAG = "AddPhotoFragment";

    //ui
    private ImageView mCollapsingImage;
    private RecyclerView mRecyclerView;

    //list
    private List<String> vImageGallaryList;

    //adapter
    private AdapterImagesGallery vAdapterImagesGallery;

    public AddPhotoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_photo, container, false);
        mCollapsingImage = view.findViewById(R.id.cardview_callary_image_imageview);
        mRecyclerView = view.findViewById(R.id.cardview_callary_image_recycler_view);
        checkReadStoragePermissionFun();
        setData();
        return view;
    }

    private void setData() {
        vImageGallaryList = ImageGallery.getAllImages(getActivity());
        Log.d(TAG, "setData: **** "+vImageGallaryList.size());
        vAdapterImagesGallery = new AdapterImagesGallery(vImageGallaryList, getActivity());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2, RecyclerView.VERTICAL, false));
        mRecyclerView.setAdapter(vAdapterImagesGallery);
    }

    private void checkReadStoragePermissionFun() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) //not add
        {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_PERMISSION_CODE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == READ_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getActivity(), "Read external storage permission granded", Toast.LENGTH_SHORT).show();
                setData();
            } else {
                Toast.makeText(getActivity(), "Read external storage permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}