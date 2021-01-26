package com.example.myapplicationinst.modelclass;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplicationinst.HomeFragment;
import com.example.myapplicationinst.model.User;
import com.example.myapplicationinst.model.Message;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ChatBodyFragmentModel extends AndroidViewModel {

    private static final String TAG = "ChatBodyFragmentModel";

    private MutableLiveData<List<Message>> mLiveData;
    private List<Message> mMessageList;

    public ChatBodyFragmentModel(@NonNull Application application) {
        super(application);
        mLiveData = new MutableLiveData<>();
        mMessageList = new ArrayList<>();
    }

    public LiveData<List<Message>> getData(final User mUser) {

        FirebaseFirestore
                .getInstance()
                .collection("Chat")
                .document(HomeFragment.userInfo.getUserKey())
                .collection("Chats")
                .document(mUser.getUserKey())
                .collection("Message")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                            Log.d(TAG, "onSuccess: 00000888");
                            Message message = snapshot.toObject(Message.class);
                            message.setMessageIds(snapshot.getId());
                            mMessageList.add(message);
                        }
                        Log.d(TAG, "onSuccess: 0000 first");
                        getTheSecondUser(mUser);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
        return mLiveData;
    }

    private void getTheSecondUser(User mUser) {
        FirebaseFirestore
                .getInstance()
                .collection("Chat")
                .document(mUser.getUserKey())
                .collection("Chats")
                .document(HomeFragment.userInfo.getUserKey())
                .collection("Message")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                            Log.d(TAG, "onSuccess: 00000777");
                            Message message = snapshot.toObject(Message.class);
                            message.setMessageIds(snapshot.getId());
                            mMessageList.add(message);
                        }
                        Log.d(TAG, "onSuccess: 00000 secand");
                        mLiveData.setValue(mMessageList);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }
}
