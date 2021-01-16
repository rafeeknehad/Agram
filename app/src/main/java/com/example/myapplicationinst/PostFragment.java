package com.example.myapplicationinst;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.myapplicationinst.adapter.MyPagerAdapter;
import com.example.myapplicationinst.model.Post;
import com.example.myapplicationinst.model.UriString;
import com.example.myapplicationinst.util.ImageViewPager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PostFragment extends Fragment {

    private static final String TAG = "PostFragment";
    //ui
    private ImageView mClosePost;
    private TextView mPost;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private EditText mEditText;

    //variable
    private List<ImageViewPager> fragmentList;
    StorageReference firebaseStorage;
    private List<UriString> mImageUris;
    private String time;
    private String date;


    //firebase
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private int counter;
    private StorageReference mStorageRef;
    private LoadingDialog loadingDialog;

    public PostFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_post, container, false);

        loadingDialog = new LoadingDialog(getActivity());
        mImageUris = new ArrayList<>();
        mStorageRef = FirebaseStorage.getInstance().getReference(firebaseAuth.getCurrentUser().getUid());
        mClosePost = view.findViewById(R.id.post_fragment_close);
        mPost = view.findViewById(R.id.post_fragment_post);
        mViewPager = view.findViewById(R.id.post_fragment_view_pager);
        mTabLayout = view.findViewById(R.id.post_fragment_tab_layout);
        mEditText = view.findViewById(R.id.post_fragment_description);
        fragmentList = new ArrayList<>();

        if (getArguments() != null) {
            fragmentList = PostFragmentArgs.fromBundle(getArguments()).getImagePost().getImageViewPagers();
            initPostFun();
        }

        mPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPostFun();
            }
        });

        return view;
    }

    private void addPostFun() {
        counter = 0;
        loadingDialog.loadingDailog();
        Log.d(TAG, "addPostFun: 123456789 ");
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss a");
        time = simpleDateFormat.format(currentTime.getTime());
        date = DateFormat.getDateInstance(DateFormat.FULL).format(currentTime);
        firebaseFirestore.collection("Post")
                .document(firebaseAuth.getCurrentUser().getUid())
                .collection("Posts")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        firebaseStorage = mStorageRef.child("Posts").child(String.valueOf(queryDocumentSnapshots.size() + 1));
                    }
                });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setData();
            }
        }, 5000);

    }

    private void initPostFun() {
        List<Fragment> mFragmentList = new ArrayList<>();
        for (ImageViewPager imageViewPager : fragmentList) {
            ViewPagerItemFragment viewPagerItemFragment = ViewPagerItemFragment.getInstance(imageViewPager);
            mFragmentList.add(viewPagerItemFragment);
        }
        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getActivity().getSupportFragmentManager(), mFragmentList);
        mViewPager.setAdapter(myPagerAdapter);
        Log.d(TAG, "onCreateView: " + getActivity().getString(R.string.logd) + " hello");
        mTabLayout.setupWithViewPager(mViewPager, true);
        Log.d(TAG, "onCreateView: " + getActivity().getString(R.string.logd) + "  " + fragmentList.size());
    }

    private void confirmPostFun() {
        Log.d(TAG, "confirmPostFun: 123456789 1");
        Post post = new Post(firebaseAuth.getCurrentUser().getUid(),
                mImageUris,
                date,
                time,
                mEditText.getText().toString().trim());
        Log.d(TAG, "confirmPostFun: 123456789 2");

        firebaseFirestore.collection("Post")
                .document(firebaseAuth.getCurrentUser().getUid())
                .collection("Posts")
                .add(post)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "onSuccess: 123456789 " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: 123456789 " + e.getMessage());
                    }
                });
    }

    private void setData() {
        for (ImageViewPager imageViewPager : fragmentList) {
            Uri uri = Uri.fromFile(new File(imageViewPager.getImgaeSrc()));
            firebaseStorage = firebaseStorage.child(System.currentTimeMillis() + "");
            firebaseStorage.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            firebaseStorage.getDownloadUrl()
                                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            Log.d(TAG, "onSuccess: 123456789 uri " + uri);
                                            mImageUris.add(new UriString(uri.toString()));
                                            counter++;
                                            if (counter == fragmentList.size()) {
                                                confirmPostFun();
                                                loadingDialog.dismissDialog();
                                            }
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d(TAG, "onFailure: 123456789 " + e.getMessage());
                                        }
                                    });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "onFailure: 123456789");
                        }
                    });

        }
    }
}