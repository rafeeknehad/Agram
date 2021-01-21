package com.example.myapplicationinst.modelclass;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplicationinst.model.User;
import com.example.myapplicationinst.util.RequestAndLikeAndComment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class FavFragmentModel extends AndroidViewModel {

    private static final String TAG = "FavFragmentModel";

    private RequestAndLikeAndComment mData;
    private MutableLiveData<User> mDataLive;
    private User user;

    public FavFragmentModel(@NonNull Application application) {
        super(application);
        mData = new RequestAndLikeAndComment();
        mDataLive = new MutableLiveData<>();
    }

    public LiveData<User> getData() {
        Query query = FirebaseFirestore
                .getInstance()
                .collection("Users")
                .whereEqualTo("userId", FirebaseAuth.getInstance().getCurrentUser().getUid());

        query
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            user = documentSnapshot.toObject(User.class);
                            user.setUserKey(documentSnapshot.getId());
                        }
                        Log.d(TAG, "onComplete: //// "+user.getUserName());
                        mDataLive.setValue(user);
                    }
                });

        return mDataLive;
    }
}