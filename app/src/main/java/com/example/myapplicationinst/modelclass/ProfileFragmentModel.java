package com.example.myapplicationinst.modelclass;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplicationinst.HomeFragment;
import com.example.myapplicationinst.model.Post;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragmentModel extends AndroidViewModel {

    private static final String TAG = "ProfileFragmentModel";

    //firebase
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    //list
    private List<Post> mPostList;

    //livedata
    private MutableLiveData<List<Post>> mMutableLiveData;

    public ProfileFragmentModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Post>> getData() {
        mMutableLiveData = new MutableLiveData<>();
        mPostList = new ArrayList<>();
        Log.d(TAG, "getData: ////// " + HomeFragment.userInfo.getUserKey());
        firebaseFirestore.collection("Post")
                .document(HomeFragment.userInfo.getUserKey())
                .collection("Posts")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Post post = documentSnapshot.toObject(Post.class);
                            mPostList.add(post);
                        }
                        mMutableLiveData.setValue(mPostList);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: **** " + e.getMessage());
                    }
                });

        return mMutableLiveData;
    }
}
