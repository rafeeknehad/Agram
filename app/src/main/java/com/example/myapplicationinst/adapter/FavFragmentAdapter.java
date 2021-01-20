package com.example.myapplicationinst.adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplicationinst.R;
import com.example.myapplicationinst.model.User;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FavFragmentAdapter extends RecyclerView.Adapter {

    private static final String TAG = "FavFragmentAdapter";

    private List<Object> mObjectList;
    private Context mContext;

    public FavFragmentAdapter(List<Object> mObjectList, Context mContext) {
        this.mObjectList = mObjectList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (this.getItemViewType(viewType) == 1) {
            return new RequestFollowingViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_send_request,
                    parent, false));
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (this.getItemViewType(position) == 1) {
            User user = (User) mObjectList.get(position);
            RequestFollowingViewHolder viewHolder = (RequestFollowingViewHolder) holder;
            String text = user.getUserName() + " is request to follow";
            viewHolder.mUserName.setText(text);
            Picasso
                    .with(mContext)
                    .load(Uri.parse(user.getUserProfile()))
                    .resize(100, 100)
                    .placeholder(R.drawable.download)
                    .into(viewHolder.mImageView);

        }
    }

    @Override
    public int getItemViewType(int position) {
        //x.class.Isinstace(list.get(position))
        Log.d(TAG, "getItemViewType: //// " + position);
        if (User.class.isInstance(mObjectList.get(0))) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public int getItemCount() {
        return this.mObjectList.size();
    }

    public class RequestFollowingViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView mImageView;
        private TextView mUserName;
        private Button mConfirm;
        private Button mDelete;

        public RequestFollowingViewHolder(@NonNull View itemView) {
            super(itemView);
            mConfirm = itemView.findViewById(R.id.cardview_send_request_confirm);
            mDelete = itemView.findViewById(R.id.cardview_send_request_delete);
            mImageView = itemView.findViewById(R.id.cardview_send_request_profileuser);
            mUserName = itemView.findViewById(R.id.cardview_send_request_text);
        }
    }

}