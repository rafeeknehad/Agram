package com.example.myapplicationinst;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplicationinst.model.Comment;
import com.example.myapplicationinst.model.Post;
import com.example.myapplicationinst.modelclass.CommentFragmentModel;

import java.util.List;

public class CommentFragment extends Fragment {


    private static final String TAG = "CommentFragment";

    private RecyclerView mRecyclerView;
    private EditText mEditText;
    private Post mCurrentPost;

    public CommentFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comment_frgment, container, false);
        mRecyclerView = view.findViewById(R.id.fragment_comment_recy);
        mEditText = view.findViewById(R.id.fragment_comment_edited);
        TextView mTextView = view.findViewById(R.id.fragment_comment_post);

        if (getArguments() != null) {
            mCurrentPost = CommentFrgmentArgs.fromBundle(getArguments())
                    .getPost().getPost();
        }

        //model
        CommentFragmentModel mCommentFragemtModel = ViewModelProviders.of(this).get(CommentFragmentModel.class);
        mCommentFragemtModel.getData(mCurrentPost.getPostKey())
                .observe(getViewLifecycleOwner(), new Observer<List<Comment>>() {
                    @Override
                    public void onChanged(List<Comment> comments) {
                        Log.d(TAG, "onChanged: ==== " + comments.size());
                    }
                });

        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = mEditText.getText().toString().trim();

            }
        });

        return view;
    }

    private void initRecyclerView(List<Comment> commentList) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        mRecyclerView.setHasFixedSize(true);
    }
}