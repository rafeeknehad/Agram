package com.example.myapplicationinst;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.myapplicationinst.adapter.AdapterImagesGallery;
import com.example.myapplicationinst.adapter.MyPagerAdapter;
import com.example.myapplicationinst.model.User;
import com.example.myapplicationinst.util.ImageGallery;
import com.example.myapplicationinst.util.ImageViewPager;
import com.example.myapplicationinst.util.ImageViewPagerSelerization;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AddPhotoFragment extends Fragment {

    private static final int READ_PERMISSION_CODE = 1;
    private static final int RQEUEST_IMAGE_CAPTURE = 2;
    private static final String TAG = "AddPhotoFragment";

    //ui
    private ImageView mCollapsingImage;
    private RecyclerView mRecyclerView;
    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private TabLayout mTableLayout;

    //list
    private List<String> vImageGallaryList;
    private List<ImageViewPager> vPostImage;
    private List<String> vPostImageIndex;
    //adapter
    private AdapterImagesGallery vAdapterImagesGallery;

    //firebase
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private int userPost;
    private User mUserInfo;

    public AddPhotoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_photo, container, false);

        vPostImage = new ArrayList<>();
        vPostImageIndex = new ArrayList<>();
        mViewPager = view.findViewById(R.id.fragment_add_photo_view_pager);
        mTableLayout = view.findViewById(R.id.fragment_add_photo_tab_layout);
        mRecyclerView = view.findViewById(R.id.cardview_callary_image_recycler_view);
        mToolbar = view.findViewById(R.id.fragment_add_photo_toolbar);

        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("");

        setHasOptionsMenu(true);
        checkReadStoragePermissionFun();
        getUserPostFun();
        //setDataFun();
        return view;
    }

    private void getUserPostFun() {
        firebaseFirestore.collection("Post")
                .document(firebaseAuth.getCurrentUser().getUid())
                .collection("Posts")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        userPost = queryDocumentSnapshots.size();
                    }
                });
    }

    private void setDataFun() {
        vImageGallaryList = ImageGallery.getAllImages(getActivity());
        vAdapterImagesGallery = new AdapterImagesGallery(vImageGallaryList, getActivity());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3, RecyclerView.VERTICAL, false));
        mRecyclerView.setAdapter(vAdapterImagesGallery);
        vAdapterImagesGallery.setAdapterImageGalleryInterface(new AdapterImagesGallery.AdapterImageGalleryInterface() {
            @Override
            public void getImagePosition(int pos, String src, boolean check) {
                if (check == true) {
                    vPostImage.add(new ImageViewPager(src));
                    vPostImageIndex.add(src);
                    initViewOagerFun(vPostImage);
                } else {
                    vPostImage.remove(vPostImageIndex.indexOf(src));
                    vPostImageIndex.remove(src);
                    initViewOagerFun(vPostImage);
                }
            }
        });
    }

    private void checkReadStoragePermissionFun() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) //not add
        {
            requestPermissions(
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_PERMISSION_CODE);
        } else {
            setDataFun();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == READ_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getActivity(), "Read external storage permission granded", Toast.LENGTH_SHORT).show();
                setDataFun();
            } else {
                Toast.makeText(getActivity(), "Read external storage permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.add_photo_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_photo_camera:
                Toast.makeText(getActivity(), "Camera", Toast.LENGTH_SHORT).show();
                openCameraFun();
                return true;

            case R.id.add_photo_check:
                Toast.makeText(getActivity(), "Check", Toast.LENGTH_SHORT).show();
                AddPost();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void AddPost() {
        ImageViewPagerSelerization selerization = new ImageViewPagerSelerization(vPostImage);
        AddPhotoFragmentDirections.ActionAddPhotoFragmentToPostFragment action = AddPhotoFragmentDirections.actionAddPhotoFragmentToPostFragment(selerization);
        action.setUserPost(userPost);
        Navigation.findNavController(getView()).navigate(action);
    }

    private void openCameraFun() {
        Intent imageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (imageIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(imageIntent, RQEUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RQEUEST_IMAGE_CAPTURE && resultCode == getActivity().RESULT_OK) {
            Bundle bundle = data.getExtras();
            Bitmap bitmap = (Bitmap) bundle.get("data");
        }
    }

    private void initViewOagerFun(List<ImageViewPager> arrayList) {
        List<String> fragmentArrayList = new ArrayList<>();
        for (ImageViewPager imageViewPager : vPostImage) {
            fragmentArrayList.add(imageViewPager.getImgaeSrc());
        }
        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(fragmentArrayList, getActivity(),"Decode");
        mViewPager.setAdapter(myPagerAdapter);
        mTableLayout.setupWithViewPager(mViewPager, true);
    }
}