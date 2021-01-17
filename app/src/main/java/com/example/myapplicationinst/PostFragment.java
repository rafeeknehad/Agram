package com.example.myapplicationinst;

import android.net.Uri;
import android.os.Bundle;
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
import com.example.myapplicationinst.util.ImageViewPager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
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
    private List<String> mImageUris;
    private String time;
    private String date;


    //firebase
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private int counter;
    private StorageReference mStorageRef;
    private LoadingDialog loadingDialog;
    private int userPost;
    private int nambarOfImage;

    private Boolean confirmUpload = false;

    public PostFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_post, container, false);
        counter = 0;
        nambarOfImage = 0;
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
            userPost = PostFragmentArgs.fromBundle(getArguments()).getUserPost();
            uploadImageToFirestorageFun();
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

    private void uploadImageToFirestorageFun() {
        firebaseStorage = mStorageRef.child("Posts").child("Post" + String.valueOf(userPost + 1));
        for (ImageViewPager imageViewPager : fragmentList) {
            nambarOfImage++;
            Uri uri = Uri.fromFile(new File(imageViewPager.getImgaeSrc()));
            firebaseStorage = firebaseStorage.
                    child(System.currentTimeMillis() + ".");
            firebaseStorage.putFile(uri)
                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            task.getResult().getStorage().getDownloadUrl()
                                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            mImageUris.add(uri.toString());
                                            if (mImageUris.size() == fragmentList.size()) {
                                                confirmPostFun();
                                            }
                                        }
                                    });
                        }
                    });
        }
    }

    private void addPostFun() {
        loadingDialog.loadingDailog();
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss a");
        time = simpleDateFormat.format(currentTime.getTime());
        date = DateFormat.getDateInstance(DateFormat.FULL).format(currentTime);
    }

    private void initPostFun() {
        List<String> stingArrayList = new ArrayList<>();
        for (ImageViewPager imageViewPager : fragmentList) {
            ViewPagerItemFragment viewPagerItemFragment = ViewPagerItemFragment.getInstance(imageViewPager);
            stingArrayList.add(imageViewPager.getImgaeSrc());
        }
        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(stingArrayList, getActivity(), "Decode");
        mViewPager.setAdapter(myPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager, true);
    }

    private void confirmPostFun() {
        loadingDialog.dismissDialog();

        Post post = new Post(firebaseAuth.getCurrentUser().getUid(),
                mImageUris,
                date,
                time,
                mEditText.getText().toString().trim());

        firebaseFirestore.collection("Post")
                .document(firebaseAuth.getCurrentUser().getUid())
                .collection("Posts")
                .add(post)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }
}