package com.example.myapplicationinst.modelclass;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplicationinst.HomeFragment;
import com.example.myapplicationinst.model.Message;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ChatFragmentModel extends AndroidViewModel {

    private List<Message> mList;
    private MutableLiveData<List<Message>> mLiveData;

    public ChatFragmentModel(@NonNull Application application) {
        super(application);
        mList = new ArrayList<>();
        mLiveData = new MutableLiveData<>();
    }

    public LiveData<List<Message>> getAllData() {
        FirebaseFirestore
                .getInstance()
                .collection("Chat")
                .document(HomeFragment.userInfo.getUserKey())
                .collection("Chats")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                            Message message = snapshot.toObject(Message.class);
                            message.setMessageIds(snapshot.getId());
                            mList.add(message);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
        return mLiveData;
    }
}
