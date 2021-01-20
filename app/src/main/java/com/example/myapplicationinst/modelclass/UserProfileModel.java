package com.example.myapplicationinst.modelclass;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplicationinst.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class UserProfileModel extends AndroidViewModel {

    private static final String TAG = "UserProfileModel";

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    private MutableLiveData<User> mUserLiveData;

    public UserProfileModel(@NonNull Application application) {
        super(application);

        mUserLiveData = new MutableLiveData<>();
    }

    public LiveData<User> getUserData() {

        Query query = firebaseFirestore.collection("Users")
                .whereEqualTo("userId", FirebaseAuth.getInstance().getCurrentUser().getUid());

        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            User user = documentSnapshot.toObject(User.class);
                            user.setUserKey(documentSnapshot.getId());
                            Log.d(TAG, "onComplete: ---- " + documentSnapshot.getId());
                            mUserLiveData.setValue(user);
                        }
                    }
                });

        Log.d(TAG, "onClick: ---- hello");
        return mUserLiveData;
    }
}
