package com.example.myapplicationinst.modelclass;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplicationinst.model.User;
import com.example.myapplicationinst.repostory.SearchFragmentRepestory;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class SearchFragmentModel extends AndroidViewModel {

    private static final String TAG = "SearchFragmentModel";

    private MutableLiveData<List<User>> allUsers;
    private List<User> userList;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private SearchFragmentRepestory repestory;

    public SearchFragmentModel(@NonNull Application application) {
        super(application);
        allUsers = new MutableLiveData<>();
        userList = new ArrayList<>();
        repestory = new SearchFragmentRepestory(application);
    }

    public LiveData<List<User>> getAllUserForServer() {
        return repestory.callRepseoryClass();
    }
}
