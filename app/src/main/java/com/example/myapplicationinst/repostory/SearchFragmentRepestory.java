package com.example.myapplicationinst.repostory;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplicationinst.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SearchFragmentRepestory {

    private static final String TAG = "SearchFragmentRepestory";

    public FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private Application mApplication;
    private MutableLiveData<List<User>> mLiveDataOfUser;
    private List<User> mListOfUser;

    public SearchFragmentRepestory(Application mApplication) {
        this.mApplication = mApplication;
    }

    public LiveData<List<User>> callRepseoryClass() {
        mListOfUser = new ArrayList<>();
        mLiveDataOfUser = new MutableLiveData<>();
        new UserDataAsynTask().execute();
        Log.d(TAG, "callRepseoryClass: ---- " + mListOfUser.size());
        //mLiveDataOfUser.setValue(mListOfUser);
        return mLiveDataOfUser;
    }

    public class UserDataAsynTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            Log.d(TAG, "doInBackground: ---- " + Thread.currentThread().getName());
            firebaseFirestore.collection("Users").get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                User user = documentSnapshot.toObject(User.class);
                                user.setUserKey(documentSnapshot.getId());
                                mListOfUser.add(user);
                            }
                            mLiveDataOfUser.setValue(mListOfUser);
                            Log.d(TAG, "onSuccess: ---- " + mListOfUser.size());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "onFailure: ---- " + e.getMessage());
                        }
                    });
            return null;
        }
    }
}
