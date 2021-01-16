package com.example.myapplicationinst;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplicationinst.adapter.PostsAdapter;
import com.example.myapplicationinst.model.Post;
import com.example.myapplicationinst.util.ImageViewPager;

import java.util.List;


public class UserPostFragment extends Fragment {

    private static final String TAG = "UserPostFragment";

    //ui
    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private ImageView mBackPress;

    public UserPostFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_post, container, false);
        mToolbar = view.findViewById(R.id.fragment_user_post_toolbar);
        mRecyclerView = view.findViewById(R.id.fragment_user_post_recyclerview);
        mBackPress = view.findViewById(R.id.fragment_user_post_back_arrow);
        Log.d(TAG, "onCreateView: **** ");
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity) getActivity()).setTitle("");

        if (getArguments() != null) {
            /*List<Post> userPosts = UserPostFragmentArgs.fromBundle(getArguments()).getUserPosts().getPostList();
            for (Post post : userPosts)
            {
                Log.d(TAG, "onCreateView: 999999999999999 "+post.getImageViewPagers().size());
                for (ImageViewPager imageViewPager : post.getImageViewPagers())
                {
                    Log.d(TAG, "onCreateView: 9999999999999 "+imageViewPager.getImgaeSrc());
                }
                Log.d(TAG, "onCreateView: 99999999999999999999999");
            }
            Log.d(TAG, "onCreateView: **** "+userPosts.size());
            PostsAdapter postsAdapter = new PostsAdapter(userPosts,getActivity());
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
            mRecyclerView.setAdapter(postsAdapter);*/
        }

        return view;
    }
}