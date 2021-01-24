package com.example.myapplicationinst;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplicationinst.adapter.CommentFragmentAdapter;
import com.example.myapplicationinst.model.Comment;
import com.example.myapplicationinst.model.Post;
import com.example.myapplicationinst.modelclass.CommentFragmentModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommentFragment extends Fragment {


    private static final String TAG = "CommentFragment";

    private RecyclerView mRecyclerView;
    private EditText mEditText;
    private Post mCurrentPost;
    private List<Comment> mPostComments;
    private CommentFragmentAdapter mCommentFragmentAdapter;
    private String mCurrentComment;

    public CommentFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        System.out.println("//////");
        View view = inflater.inflate(R.layout.fragment_comment_frgment, container, false);
        mRecyclerView = view.findViewById(R.id.fragment_comment_recy);
        mEditText = view.findViewById(R.id.fragment_comment_edited);
        TextView mTextView = view.findViewById(R.id.fragment_comment_post);
        mPostComments = new ArrayList<>();
        if (getArguments() != null) {
            mCurrentPost = CommentFragmentArgs.fromBundle(getArguments()).getPost().getPost();
        }
        //model
        CommentFragmentModel mCommentFragmentModel = ViewModelProviders.of(this).get(CommentFragmentModel.class);
        mCommentFragmentModel.getData(mCurrentPost.getPostKey())
                .observe(getViewLifecycleOwner(), new Observer<List<Comment>>() {
                    @Override
                    public void onChanged(List<Comment> comments) {
                        mPostComments = comments;
                        initRecyclerView();
                    }
                });

        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = mEditText.getText().toString().trim();
                Comment commentObj = new Comment(mCurrentPost.getPostKey(),
                        HomeFragment.userInfo.getUserKey(), comment);
                mPostComments.add(commentObj);

                FirebaseFirestore.getInstance()
                        .collection("Comment")
                        .document(mCurrentPost.getPostKey())
                        .collection("Comments")
                        .add(commentObj)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                mCurrentComment = documentReference.getId();
                                mCommentFragmentAdapter.setData(mPostComments);
                                updatePost();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
            }
        });

        return view;
    }

    private void initRecyclerView() {
        mCommentFragmentAdapter = new CommentFragmentAdapter(mPostComments, getActivity());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mCommentFragmentAdapter);

        mCommentFragmentAdapter.setCommentFragmentInteface(new CommentFragmentAdapter.CommentFragmentInterface() {
            @Override
            public void likeCommentFun(int pos, Comment comment) {
                Log.d(TAG, "likeCommentFun: //// " + pos + "  " + comment.getCommentId());
                updateComment(comment);
            }
        });
    }

    private void updateComment(Comment comment) {
        WriteBatch batch = FirebaseFirestore.getInstance().batch();

        DocumentReference documentReference = FirebaseFirestore.getInstance()
                .collection("Comment")
                .document(mCurrentPost.getPostKey())
                .collection("Comments")
                .document(comment.getCommentId());

        HashMap<String, Object> map = new HashMap<>();
        List<String> like = comment.getUserFav();
        like.add(HomeFragment.userInfo.getUserKey());
        map.put("userFav", like);
        batch.update(documentReference, map)
                .commit()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: ///// sucess");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: ////// " + e.getMessage());
                    }
                });
    }

    private void updatePost() {
        WriteBatch batch = FirebaseFirestore.getInstance().batch();

        DocumentReference documentReference = FirebaseFirestore.getInstance()
                .collection("Post")
                .document(HomeFragment.userInfo.getUserKey())
                .collection("Posts")
                .document(mCurrentPost.getPostKey());

        HashMap<String, Object> map = new HashMap<>();
        List<String> comment = mCurrentPost.getCommentList();
        Log.d(TAG, "updateComment: ///// " + mCurrentComment);
        comment.add(mCurrentComment);
        map.put("commentList", comment);
        batch.update(documentReference, map)
                .commit()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: ///// sucess");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: ////// " + e.getMessage());
                    }
                });
    }
}