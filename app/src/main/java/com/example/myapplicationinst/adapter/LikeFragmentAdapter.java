package com.example.myapplicationinst.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplicationinst.R;
import com.example.myapplicationinst.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class LikeFragmentAdapter extends RecyclerView.Adapter<LikeFragmentAdapter.LikeFragmentViewHolder> {

    private List<String> mUserList;
    private Context mContext;
    private User mUserInfo;


    private LikeFragmentAdapterInterface mListener;

    public LikeFragmentAdapter(List<String> mUserList, Context mContext, User user) {
        this.mUserList = mUserList;
        this.mContext = mContext;
        this.mUserInfo = user;
    }

    @NonNull
    @Override
    public LikeFragmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_like, parent, false);
        return new LikeFragmentViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull LikeFragmentViewHolder holder, int position) {
        String userId = mUserList.get(position);
        final CircleImageView imageView = holder.mCircleImageView;
        final TextView textView = holder.mTextView;
        DocumentReference documentReference = FirebaseFirestore.getInstance()
                .collection("Users")
                .document(userId);

        documentReference.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        User user = documentSnapshot.toObject(User.class);
                        if (user != null) {
                            textView.setText(user.getUserName());
                            Picasso
                                    .with(mContext)
                                    .load(Uri.parse(user.getUserProfile()))
                                    .placeholder(R.drawable.download)
                                    .resize(100, 100)
                                    .into(imageView);
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });


        if (mUserInfo.getFollewers().contains(userId) || mUserInfo.getFollowings().contains(userId)) {
            holder.mButton.setBackgroundColor(ContextCompat.getColor(mContext, R.color.black));
            holder.mButton.setText("Following");
        }
    }

    @Override
    public int getItemCount() {
        return mUserList.size();
    }

    public void setLikeFragmentInterface(LikeFragmentAdapterInterface listener) {
        this.mListener = listener;
    }

    public interface LikeFragmentAdapterInterface {
        public void showUserProfile(int pos, String userId);

        public void sendUserRequest(int pos, User user);
    }

    public class LikeFragmentViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public CircleImageView mCircleImageView;
        public Button mButton;

        public LikeFragmentViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.cardview_like_username);
            mCircleImageView = itemView.findViewById(R.id.cardview_like_profileimage);
            mButton = itemView.findViewById(R.id.cardview_like_following);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                        mListener.showUserProfile(getAdapterPosition(), mUserList.get(getAdapterPosition()));
                    }
                }
            });
        }
    }
}
