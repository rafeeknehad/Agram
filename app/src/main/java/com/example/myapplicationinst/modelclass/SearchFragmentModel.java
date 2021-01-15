package com.example.myapplicationinst.modelclass;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplicationinst.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SearchFragmentModel extends AndroidViewModel {

    private static final String TAG = "SearchFragmentModel";

    private MutableLiveData<List<User>> allUsers;
    private List<User> userList;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    public SearchFragmentModel(@NonNull Application application) {
        super(application);
        allUsers = new MutableLiveData<>();
        userList = new ArrayList<>();
    }

    public LiveData<List<User>> getAllUserForServer() {
        firebaseFirestore.collection("Users").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            User user = documentSnapshot.toObject(User.class);
                            userList.add(user);
                        }
                        Log.d(TAG, "onSuccess: **** " + userList.size());
                        allUsers.setValue(userList);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: **** " + e.getMessage());
                    }
                });
        return allUsers;
    }
}
