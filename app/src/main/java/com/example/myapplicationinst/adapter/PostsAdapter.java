package com.example.myapplicationinst.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.example.myapplicationinst.R;
import com.example.myapplicationinst.ViewPagerItemFragment;
import com.example.myapplicationinst.model.Post;
import com.example.myapplicationinst.model.User;
import com.example.myapplicationinst.util.ImageViewPager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.PostsViewHolder> {

    private static final String TAG = "PostsAdapter";

    private List<Post> mPostList;
    private Context mContext;
    private User mUser;
    private List<Fragment> mFragmentList;

    private ViewGroup mViewGroup;

    public PostsAdapter(List<Post> mPostList, Context mContext) {
        this.mPostList = mPostList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public PostsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_home_post, parent, false);
        mViewGroup = parent;
        return new PostsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PostsViewHolder holder, int position) {
        Post post = mPostList.get(position);
        mFragmentList = new ArrayList<>();
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        Query query = firebaseFirestore.collection("Users")
                .whereEqualTo("userId", FirebaseAuth.getInstance().getCurrentUser().getUid());

        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: **** success");
                            for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                mUser = snapshot.toObject(User.class);
                                Picasso
                                        .with(mContext)
                                        .load(mUser.getUserProfile())
                                        .resize(100, 100)
                                        .into(holder.mProfileImage);

                                holder.mUserProfileName.setText(mUser.getUserName());
                                holder.mUserProfileName2.setText(mUser.getUserName());
                            }
                        }
                    }
                });

        Log.d(TAG, "onBindViewHolder: **** 2 " + post.getImageViewPagers().size());
        for (ImageViewPager imageViewPager : post.getImageViewPagers()) {
            ViewPagerItemFragment viewPagerItemFragment = ViewPagerItemFragment.getInstance(imageViewPager);
            mFragmentList.add(viewPagerItemFragment);
        }


        Log.d(TAG, "onBindViewHolder: **** 1 " + mFragmentList.size());
        FragmentManager fragmentManager = ((FragmentActivity) mContext).getSupportFragmentManager();
        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(fragmentManager, mFragmentList);
        holder.mViewPager.setAdapter(myPagerAdapter);
        holder.mTabLayout.setupWithViewPager(holder.mViewPager, true);


        holder.mDate.setText(post.getTime() + " & " + post.getDate());
        holder.mCommentNumber.setText("" + post.getCommentList().size());
        holder.mLikePost.setText("" + post.getLikeList().size());
        holder.mDescription.setText(post.getDescription());

    }

    @Override
    public int getItemCount() {
        return mPostList.size();
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
        }
    }

}
