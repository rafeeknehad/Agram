package com.example.myapplicationinst;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
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
import com.example.myapplicationinst.model.User;
import com.example.myapplicationinst.util.ImageViewPager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class PostFragment extends Fragment {

    private static final String TAG = "PostFragment";
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private EditText mEditText;

    //variable

    private String time;
    private String date;
    private int userPost;
    private Boolean mPostBoolean = false;
    private Boolean mLoadComplete = false;
    private List<ImageViewPager> fragmentList;
    private List<String> mImageUris;
    private List<String> mUserPosts;
    private User mUserInfo;
    //firebase
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
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
        getUserInfo();
        loadingDialog = new LoadingDialog(getActivity());
        mImageUris = new ArrayList<>();
        mUserPosts = new ArrayList<>();
        mStorageRef = FirebaseStorage.getInstance().getReference(firebaseAuth.getCurrentUser().getUid());
        //ui
        ImageView mClosePost = view.findViewById(R.id.post_fragment_close);
        TextView mPost = view.findViewById(R.id.post_fragment_post);
        mViewPager = view.findViewById(R.id.post_fragment_view_pager);
        mTabLayout = view.findViewById(R.id.post_fragment_tab_layout);
        mEditText = view.findViewById(R.id.post_fragment_description);
        fragmentList = new ArrayList<>();

        if (getArguments() != null) {
            fragmentList = PostFragmentArgs.fromBundle(getArguments()).getImagePost().getImageViewPagers();
            userPost = PostFragmentArgs.fromBundle(getArguments()).getUserPost();
            uploadImageToFirestoneFun();
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

    private void uploadImageToFirestoneFun() {
        StorageReference firebaseStorage = mStorageRef.child("Posts").child("Post" + String.valueOf(userPost + 1));
        for (ImageViewPager imageViewPager : fragmentList) {
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
                                            if (mPostBoolean && mImageUris.size() == fragmentList.size()) {
                                                confirmPostFun();
                                                mLoadComplete = true;
                                            }
                                        }
                                    });
                        }
                    });
        }
    }

    private void addPostFun() {
        mPostBoolean = true;
        loadingDialog.loadingDailog();
        Date currentTime = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss a");
        time = simpleDateFormat.format(currentTime.getTime());
        date = DateFormat.getDateInstance(DateFormat.FULL).format(currentTime);
        if (mLoadComplete) {
            confirmPostFun();
        }
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
        Post post = new Post(mUserInfo.getUserKey(),
                mImageUris,
                date,
                time,
                mEditText.getText().toString().trim());

        firebaseFirestore.collection("Post")
                .document(mUserInfo.getUserKey())
                .collection("Posts")
                .add(post)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        mUserPosts.add(documentReference.getId());
                        updateUserInfo();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }

    private void updateUserInfo() {
        Log.d(TAG, "updateUserInfo: //// " + mUserInfo.getUserName());
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

        WriteBatch batch = firebaseFirestore.batch();
        DocumentReference documentReference = firebaseFirestore.collection("Users")
                .document(mUserInfo.getUserKey());

        HashMap<String, Object> newValue = new HashMap<>();
        newValue.put("posts", mUserPosts);

        batch.update(documentReference, newValue);
        batch.commit()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });
    }

    private void getUserInfo() {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        Query query = firebaseFirestore.collection("Users")
                .whereEqualTo("userId", FirebaseAuth.getInstance().getCurrentUser().getUid());

        query.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            mUserInfo = documentSnapshot.toObject(User.class);
                            mUserInfo.setUserKey(documentSnapshot.getId());
                            mUserPosts = mUserInfo.getPosts();
                            Log.d(TAG, "onSuccess: ///// " + documentSnapshot.getId());
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: ///// " + e.getMessage());
                    }
                });
    }
}