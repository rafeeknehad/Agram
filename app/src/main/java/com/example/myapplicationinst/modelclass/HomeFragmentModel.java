package com.example.myapplicationinst.modelclass;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplicationinst.model.Post;
import com.example.myapplicationinst.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeFragmentModel extends AndroidViewModel {

    private static final String TAG = "HomeFragmentModel";

    private List<Post> mPostList;
    private MutableLiveData<List<String>> mPostLiveData;
    private List<String> mUserIdList;

    public HomeFragmentModel(@NonNull Application application) {
        super(application);
        mUserIdList = new ArrayList<>();
        mPostList = new ArrayList<>();
        mPostLiveData = new MutableLiveData<>();
    }

    public LiveData<List<String>> getPostData() {
        Query query = FirebaseFirestore.getInstance()
                .collection("Users")
                .whereEqualTo("userId", FirebaseAuth.getInstance().getCurrentUser().getUid());

        query.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            User user = documentSnapshot.toObject(User.class);
                            user.setUserKey(documentSnapshot.getId());
                            mUserIdList.add(documentSnapshot.getId());
                            mUserIdList.addAll(user.getFollewers());
                            mUserIdList.addAll(user.getFollowings());
                            getAllPosts();
                            ;
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
        return mPostLiveData;
    }

    public void getAllPosts() {
        final List<String> postsList = new ArrayList<>();
        Log.d(TAG, "getAllPosts: //// " + mUserIdList.size());
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        for (String userId : mUserIdList) {
            Query query = firebaseFirestore.getInstance()
                    .collection("Post")
                    .document(userId)
                    .collection("Posts");

            query.get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                postsList.add(documentSnapshot.getId());
                                Log.d(TAG, "onSuccess: //// " + documentSnapshot.getId());
                            }
                            mPostLiveData.setValue(postsList);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
        }

    }
}
