package com.example.myapplicationinst.modelclass;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplicationinst.model.Comment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CommentFragmentModel extends AndroidViewModel {

    private static final String TAG = "CommentFragmentModel";

    private List<Comment> commentList;
    private MutableLiveData<List<Comment>> liveData;

    public CommentFragmentModel(@NonNull Application application) {
        super(application);

        commentList = new ArrayList<>();
        liveData = new MutableLiveData<>();

    }

    public LiveData<List<Comment>> getData(String postId) {
        FirebaseFirestore.getInstance()
                .collection("Comment")
                .document(postId)
                .collection("Comments")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<Comment> commentList = queryDocumentSnapshots.toObjects(Comment.class);
                        Log.d(TAG, "onSuccess: ==== " + commentList.size());
                        liveData.setValue(commentList);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
        return liveData;
    }
}
