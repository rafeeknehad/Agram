package com.example.myapplicationinst;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplicationinst.adapter.ProfileFragmentAdapter;
import com.example.myapplicationinst.model.Post;
import com.example.myapplicationinst.model.User;
import com.example.myapplicationinst.modelclass.UserProfileModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class UserProfile extends Fragment {

    private static final String TAG = "UserProfile";

    //firebase
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    //ui
    private TextView mUserName1;
    private CircleImageView mUserProfileImage;
    private TextView mUserName2;
    private TextView mAbout;
    private Button mFollowBtn;
    private Button mMessageBtn;
    private RecyclerView mRecyclerView;
    private TextView mNumberPost;
    private TextView mNumberFollowing;
    private TextView mNumberFollower;

    //dialog
    private LoadingDialog mLoadingDialog;

    private User mUser;
    private User mCurrentUser;

    private List<Post> userPostList;

    public UserProfile() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        mLoadingDialog = new LoadingDialog(getActivity());
        mLoadingDialog.loadingDailog();
        mUserName1 = view.findViewById(R.id.user_profile_fragment_username1);
        mUserProfileImage = view.findViewById(R.id.user_fragment_userprofile);
        mUserName2 = view.findViewById(R.id.user_fragment_underpoto);
        mAbout = view.findViewById(R.id.user_fragment_about);
        mFollowBtn = view.findViewById(R.id.user_fragment_following);
        mMessageBtn = view.findViewById(R.id.user_fragment_message);
        mRecyclerView = view.findViewById(R.id.user_fragment_recycler);
        mNumberFollower = view.findViewById(R.id.user_fragment_number_followers);
        mNumberFollowing = view.findViewById(R.id.user_fragment_number_following);
        mNumberPost = view.findViewById(R.id.user_fragment_number_posts);

        if (getArguments() != null) {
            mUser = UserProfileArgs.fromBundle(getArguments()).getSelectedUser().getUser();
        }
        //model
        UserProfileModel mUserProfileModel = ViewModelProviders.of(this).get(UserProfileModel.class);

        mUserProfileModel.getUserData().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                mLoadingDialog.dismissDialog();
                mCurrentUser = user;
                Log.d(TAG, "onChanged: ---- " + user);
                init();
            }
        });
        return view;
    }

    private void updateUserDataFun() {
        HashMap<String, Object> receiveMap = new HashMap<>();
        HashMap<String, Object> waitMap = new HashMap<>();
        List<String> waitList = mCurrentUser.getWaitList();
        List<String> receiveList = mUser.getRecieveList();
        waitList.add(mUser.getUserKey());
        receiveList.add(mCurrentUser.getUserKey());
        waitMap.put("waitList", waitList);
        receiveMap.put("recieveList", receiveList);
        firebaseFirestore.collection("Users")
                .document(mCurrentUser.getUserKey())
                .update(waitMap);

        firebaseFirestore.collection("Users")
                .document(mUser.getUserKey())
                .update(receiveMap);

    }

    @SuppressLint("SetTextI18n")
    private void init() {
        Picasso
                .with(getActivity())
                .load(Uri.parse(mUser.getUserProfile()))
                .placeholder(R.drawable.download)
                .into(mUserProfileImage);

        mUserName1.setText(mUser.getUserName());
        mUserName2.setText(mUser.getUserName());
        mAbout.setText(mUser.getUserAbout());
        mNumberPost.setText("" + mUser.getPosts().size());
        mNumberFollowing.setText("" + mUser.getFollowings().size());
        mNumberFollower.setText("" + mUser.getFollewers().size());
        if (HomeFragment.userInfo.getWaitList().contains(mUser.getUserKey())) {
            mFollowBtn.setText("Request");
            mFollowBtn.setBackgroundColor(getResources().getColor(R.color.darkGray));
        } else if (HomeFragment.userInfo.getFollowings().contains(mUser.getUserKey()) || HomeFragment.userInfo.getFollewers().contains(mUser.getUserKey())) {
            mFollowBtn.setText("Following");
            mFollowBtn.setBackgroundColor(getResources().getColor(R.color.black));
            mMessageBtn.setVisibility(View.VISIBLE);
        } else {
            mFollowBtn.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: ---- click");
                    updateUserDataFun();
                    mFollowBtn.setBackgroundColor(R.color.black);
                    mFollowBtn.setText("Requested");
                }
            });
            mMessageBtn.setVisibility(View.GONE);
        }

        userPostList = new ArrayList<>();
        FirebaseFirestore.getInstance()
                .collection("Post")
                .document(mUser.getUserKey())
                .collection("Posts")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                            Post post = snapshot.toObject(Post.class);
                            post.setPostKey(snapshot.getId());
                            userPostList.add(post);
                            if (userPostList.size() == mUser.getPosts().size()) {
                                initAdapter();
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    private void initAdapter() {
        ProfileFragmentAdapter profileFragmentAdapter = new ProfileFragmentAdapter(userPostList, getActivity());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2, RecyclerView.VERTICAL, false));
        mRecyclerView.setAdapter(profileFragmentAdapter);
    }
}