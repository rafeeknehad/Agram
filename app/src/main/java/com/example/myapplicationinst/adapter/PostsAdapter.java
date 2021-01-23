package com.example.myapplicationinst.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.example.myapplicationinst.R;
import com.example.myapplicationinst.model.Post;
import com.example.myapplicationinst.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.PostsViewHolder> {

    private static final String TAG = "PostsAdapter";

    private List<Post> mPostList;
    private Context mContext;
    private User mUser;
    private PostAdapterIntefrace mListener;


    public PostsAdapter(List<Post> mPostList, Context mContext) {
        this.mPostList = mPostList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public PostsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_home_post, parent, false);
        return new PostsViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final PostsViewHolder holder, int position) {
        Post post = mPostList.get(position);
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore
                .collection("Users")
                .document(post.getUserId())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        mUser = documentSnapshot.toObject(User.class);
                        Picasso
                                .with(mContext)
                                .load(mUser.getUserProfile())
                                .resize(100, 100)
                                .into(holder.mProfileImage);

                        holder.mUserProfileName.setText(mUser.getUserName());
                        holder.mUserProfileName2.setText(mUser.getUserName());

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
        Log.d(TAG, "onBindViewHolder: ---- " + post.getImageList());
        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(post.getImageList(), mContext, "Url");
        holder.mViewPager.setAdapter(myPagerAdapter);
        holder.mTabLayout.setupWithViewPager(holder.mViewPager, true);
        holder.mDate.setText(post.getTime() + "  " + post.getDate());
        holder.mDescription.setText(post.getDescription());

        if (post.getLikeList().size() == 0) {
            holder.mLikePost.setVisibility(View.GONE);
        } else {
            holder.mLikePost.setText(post.getLikeList().size() + " Likes");

        }
        if (post.getCommentList().size() == 0) {
            holder.mCommentNumber.setVisibility(View.GONE);
        } else {
            holder.mCommentNumber.setText(post.getCommentList().size() + " Comments");
        }

    }

    @Override
    public int getItemCount() {
        return mPostList.size();
    }

    public void setPostAdapterInterface(PostAdapterIntefrace listener) {
        mListener = listener;
    }

    public interface PostAdapterIntefrace {
        public void likePostFun(int position, Post post);

        public void removeLikePost(int position, Post post);

        public void getLikeList(int position, Post post);

        public void commentToPost(int position, Post post);
    }

    public class PostsViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView mProfileImage;
        private TextView mUserProfileName;
        private TextView mLikePost;
        private TextView mUserProfileName2;
        private TextView mCommentNumber;
        private TextView mDate;
        private ReadMoreTextView mDescription;
        private ImageView mFav;
        private ImageView mComment;
        private ImageView mSave;
        private ImageView m3Dots;
        private ViewPager mViewPager;
        private TabLayout mTabLayout;

        public PostsViewHolder(@NonNull View itemView) {
            super(itemView);

            mProfileImage = itemView.findViewById(R.id.cardview_home_post_profile_image);
            mUserProfileName = itemView.findViewById(R.id.cardview_home_post_userprofile);
            mLikePost = itemView.findViewById(R.id.cardview_home_post_number_like);
            mUserProfileName2 = itemView.findViewById(R.id.cardview_home_post_linear_userprofile);
            m3Dots = itemView.findViewById(R.id.cardview_home_post_3dots);

            mDescription = itemView.findViewById(R.id.cardview_home_post_description);
            mCommentNumber = itemView.findViewById(R.id.cardview_home_post_comment);
            mFav = itemView.findViewById(R.id.fragment_home_like);
            mComment = itemView.findViewById(R.id.fragment_home_comment);
            mSave = itemView.findViewById(R.id.fragment_home_save);

            mViewPager = itemView.findViewById(R.id.cardview_home_post_viewPager);
            mTabLayout = itemView.findViewById(R.id.cardview_home_post_tablayout);

            mDate = itemView.findViewById(R.id.cardview_home_post_date);

            mFav.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onClick(View v) {
                    if (mListener != null && getAdapterPosition() != RecyclerView.NO_POSITION && mFav.getTag().equals("dislike")) {
                        mListener.likePostFun(getAdapterPosition(), mPostList.get(getAdapterPosition()));
                        mFav.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_like_fav));
                        String[] str = mLikePost.getText().toString().split(" ");
                        Log.d(TAG, "onClick: <<<< " + str[0]);
                        int numberOfLike = Integer.parseInt(str[0]);
                        mLikePost.setText((numberOfLike + 1) + " Likes");
                        mFav.setTag("like");
                    } else if (mListener != null && getAdapterPosition() != RecyclerView.NO_POSITION && mFav.getTag().equals("like")) {
                        mListener.removeLikePost(getAdapterPosition(), mPostList.get(getAdapterPosition()));
                        mFav.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_like));
                        String[] str = mLikePost.getText().toString().split(" ");
                        Log.d(TAG, "onClick: <<<< " + str[0]);
                        int numberOfLike = Integer.parseInt(str[0]);
                        mLikePost.setText((numberOfLike - 1) + " Likes");
                        mFav.setTag("dislike");
                    }
                }
            });

            mLikePost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null && getAdapterPosition() != RecyclerView.NO_POSITION && mFav.getTag().equals("dislike")) {
                        mListener.getLikeList(getAdapterPosition(), mPostList.get(getAdapterPosition()));
                    }
                }
            });

            mComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                        mListener.commentToPost(getAdapterPosition(),
                                mPostList.get(getAdapterPosition()));
                    }
                }
            });

        }
    }
}
