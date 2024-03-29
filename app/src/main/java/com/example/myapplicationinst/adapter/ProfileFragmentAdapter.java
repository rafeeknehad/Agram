package com.example.myapplicationinst.adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplicationinst.R;
import com.example.myapplicationinst.model.Post;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProfileFragmentAdapter extends RecyclerView.Adapter<ProfileFragmentAdapter.ProfileFragmentViewHolder> {

    private static final String TAG = "ProfileFragmentAdapter";

    private List<Post> mPostList;
    private Context mContext;
    private InterfaceProfileFragment mLinstener;

    public ProfileFragmentAdapter(List<Post> mPostList, Context mContext) {
        this.mPostList = mPostList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ProfileFragmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_profile_fragment, parent, false);
        return new ProfileFragmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileFragmentViewHolder holder, int position) {
        Post post = mPostList.get(position);
        Picasso
                .with(mContext)
                .load(Uri.parse(post.getImageList().get(0)))
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mPostList.size();
    }

    public void setInteface(InterfaceProfileFragment mLinstener) {
        this.mLinstener = mLinstener;
    }

    public interface InterfaceProfileFragment {
        public void getSelectedItem(int pos, Post post);
    }

    public class ProfileFragmentViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;

        public ProfileFragmentViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.cardview_profile_fragment_image);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                        mLinstener.getSelectedItem(getAdapterPosition(), mPostList.get(getAdapterPosition()));
                    }
                }
            });
        }
    }
}
