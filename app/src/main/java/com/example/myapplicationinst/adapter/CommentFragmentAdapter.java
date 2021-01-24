package com.example.myapplicationinst.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplicationinst.R;
import com.example.myapplicationinst.model.Comment;
import com.example.myapplicationinst.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentFragmentAdapter extends RecyclerView.Adapter<CommentFragmentAdapter.CommentFragmentViewHolder> {

    private List<Comment> mCommentList;
    private Context mContext;
    private CommentFragmentInterface mListener;

    public CommentFragmentAdapter(List<Comment> mCommentList, Context mContext) {
        this.mCommentList = mCommentList;
        this.mContext = mContext;
    }

    public void setCommentFragmentInteface(CommentFragmentInterface listener) {
        this.mListener = listener;
    }

    @NonNull
    @Override
    public CommentFragmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_comment_fragment, parent, false);
        return new CommentFragmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CommentFragmentViewHolder holder, int position) {
        final Comment comment = mCommentList.get(position);
        FirebaseFirestore
                .getInstance()
                .collection("Users")
                .document(comment.getUserId())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        User user = documentSnapshot.toObject(User.class);
                        if (user != null) {
                            holder.mUserName.setText(user.getUserName());
                            Picasso
                                    .with(mContext)
                                    .load(Uri.parse(user.getUserProfile()))
                                    .resize(100, 100)
                                    .into(holder.mUserProfile);
                            holder.mCommentDesc.setText(comment.getCommentDescription());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    @Override
    public int getItemCount() {
        return mCommentList.size();
    }

    public void setData(List<Comment> commentList) {
        this.mCommentList = commentList;
        notifyDataSetChanged();
    }

    public interface CommentFragmentInterface {
        public void likeCommentFun(int pos, Comment comment);
    }

    public class CommentFragmentViewHolder extends RecyclerView.ViewHolder {

        public CircleImageView mUserProfile;
        public TextView mUserName;
        public TextView mCommentDesc;
        public ImageView mFav;

        public CommentFragmentViewHolder(@NonNull View itemView) {
            super(itemView);

            mUserProfile = itemView.findViewById(R.id.card_comment_image);
            mUserName = itemView.findViewById(R.id.card_comment_username);
            mCommentDesc = itemView.findViewById(R.id.card_comment_comment_description);
            mFav = itemView.findViewById(R.id.card_comment_fav);

            mFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                        mListener.likeCommentFun(getAdapterPosition(),
                                mCommentList.get(getAdapterPosition()));
                    }
                }
            });
        }
    }
}
