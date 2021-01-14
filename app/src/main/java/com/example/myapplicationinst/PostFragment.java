package com.example.myapplicationinst;

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
import com.example.myapplicationinst.util.ImageViewPager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

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


    //firebase
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    public PostFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_post, container, false);
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

        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss a");
        String time = simpleDateFormat.format(currentTime.getTime());
        String date = DateFormat.getDateInstance(DateFormat.FULL).format(currentTime);
        Log.d(TAG, "addPostFun: " + getActivity().getString(R.string.logd) + " " + date.toString());
        Log.d(TAG, "addPostFun: " + getActivity().getString(R.string.logd) + " " + time.toString());

        Post post = new Post(firebaseAuth.getCurrentUser().getUid(), fragmentList, date, time,
                mEditText.getText().toString().trim());

        firebaseFirestore.collection("Post").document(firebaseAuth.getCurrentUser().getUid())
                .collection("Posts").add(post)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        Log.d(TAG, "onComplete: "+getActivity().getString(R.string.logd)+" "+task.toString());
                    }
                });
    }

    private void initPostFun() {
        List<Fragment> mFragmentList = new ArrayList<>();
        for (ImageViewPager imageViewPager : fragmentList) {
            ViewPagerItemFragment viewPagerItemFragment = ViewPagerItemFragment.getInstance(imageViewPager);
            mFragmentList.add(viewPagerItemFragment);
        }
        Log.d(TAG, "onCreateView: " + getActivity().getString(R.string.logd) + fragmentList.size());
        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getActivity().getSupportFragmentManager(), mFragmentList);
        mViewPager.setAdapter(myPagerAdapter);
        Log.d(TAG, "onCreateView: " + getActivity().getString(R.string.logd) + " hello");
        mTabLayout.setupWithViewPager(mViewPager, true);
        Log.d(TAG, "onCreateView: " + getActivity().getString(R.string.logd) + "  " + fragmentList.size());
    }
}