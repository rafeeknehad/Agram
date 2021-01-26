package com.example.myapplicationinst;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplicationinst.adapter.PostsAdapter;
import com.example.myapplicationinst.model.Post;
import com.example.myapplicationinst.model.User;
import com.example.myapplicationinst.modelclass.HomeFragmentModel;
import com.example.myapplicationinst.util.PostSerealization;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;


public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";
    public static User userInfo;

    private RecyclerView mRecyclerView;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ImageView mFavImage = view.findViewById(R.id.fragment_home_toolbar_fav);
        ImageView mMessageImage = view.findViewById(R.id.fragment_home_toolbar_chat);
        getUserInfo();
        mRecyclerView = view.findViewById(R.id.fragment_home_recycler);
        //model
        HomeFragmentModel mHomeFragmentModel = ViewModelProviders.of(this).get(HomeFragmentModel.class);
        mHomeFragmentModel.getPostData().observe(getViewLifecycleOwner(), new Observer<List<Post>>() {
            @Override
            public void onChanged(List<Post> posts) {
                Log.d(TAG, "onChanged: ////// " + posts.size());
                Collections.sort(posts);
                iniAdapter(posts);

            }
        });
        mFavImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getView() != null) {
                    Navigation.findNavController(getView()).navigate(R.id.action_homeFragment_to_favFragment);
                }
            }
        });

        mMessageImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getView() != null) {
                    Navigation.findNavController(getView()).navigate(R.id.action_homeFragment_to_chatFragment);
                }
            }
        });

        return view;
    }

    private void iniAdapter(List<Post> posts) {
        //adapter
        PostsAdapter mPostsAdapter = new PostsAdapter(posts, getActivity());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        mRecyclerView.setAdapter(mPostsAdapter);

        mPostsAdapter.setPostAdapterInterface(new PostsAdapter.PostAdapterInterface() {
            @Override
            public void likePostFun(int position, Post post) {
                Log.d(TAG, "likePostFun: ///// aa");
                updateAddPost(post);
            }

            @Override
            public void removeLikePost(int position, Post post) {
                updateDeletePost(post);
            }

            @Override
            public void getLikeList(int position, Post post) {
                if (getView() != null) {
                    Navigation.findNavController(getView()).navigate(HomeFragmentDirections.actionHomeFragmentToLikeFragment(
                            new PostSerealization(post)
                    ));
                }
            }

            @Override
            public void commentToPost(int position, Post post) {
                if (getView() != null) {
                    Navigation.findNavController(getView()).navigate(HomeFragmentDirections.actionHomeFragmentToCommentFrgment(
                            new PostSerealization(post)
                    ));
                }
            }
        });
    }

    private void getUserInfo() {
        String mUid = "";
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            mUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
        Query query = FirebaseFirestore.getInstance()
                .collection("Users")
                .whereEqualTo("userId", mUid);

        query.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            userInfo = documentSnapshot.toObject(User.class);
                            Log.d(TAG, "onSuccess: 00000033 " + documentSnapshot.getId());
                            userInfo.setUserKey(documentSnapshot.getId());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }

    private void updateAddPost(Post post) {
        List<String> likeList = post.getLikeList();
        //List<String> commandList = post.getCommentList();

        likeList.add(userInfo.getUserKey());

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        WriteBatch batch = firebaseFirestore.batch();

        DocumentReference documentSnapshot = firebaseFirestore.collection("Post")
                .document(post.getUserId())
                .collection("Posts")
                .document(post.getPostKey());
        HashMap<String, Object> likeMap = new HashMap<>();
        likeMap.put("likeList", likeList);
        batch.update(documentSnapshot, likeMap);
        batch.commit();
    }

    private void updateDeletePost(Post post) {
        List<String> likeList = post.getLikeList();
        //List<String> commandList = post.getCommentList();

        likeList.remove(userInfo.getUserKey());

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        WriteBatch batch = firebaseFirestore.batch();

        DocumentReference documentSnapshot = firebaseFirestore.collection("Post")
                .document(post.getUserId())
                .collection("Posts")
                .document(post.getPostKey());
        HashMap<String, Object> likeMap = new HashMap<>();
        likeMap.put("likeList", likeList);
        batch.update(documentSnapshot, likeMap);
        batch.commit();

    }

}