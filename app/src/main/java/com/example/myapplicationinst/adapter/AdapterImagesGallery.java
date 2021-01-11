package com.example.myapplicationinst.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplicationinst.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterImagesGallery extends RecyclerView.Adapter<AdapterImagesGallery.ViewHolderImageGallery> {

    private static final String TAG = "AdapterImagesGallery";

    public List<String> vDataList;
    public Context vContext;

    public AdapterImagesGallery(List<String> vDataList, Context vContext) {
        Log.d(TAG, "AdapterImagesGallery: ****"+vDataList.size());
        this.vDataList = vDataList;
        this.vContext = vContext;
    }

    @NonNull
    @Override
    public ViewHolderImageGallery onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_callary_image, parent, false);
        return new ViewHolderImageGallery(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderImageGallery holder, int position) {
        String item = vDataList.get(position);
        Bitmap bitmap = BitmapFactory.decodeFile(item);
        holder.mImageView.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: **** "+vDataList.size());
        return vDataList.size();
    }

    public class ViewHolderImageGallery extends RecyclerView.ViewHolder {
        private ImageView mImageView;

        public ViewHolderImageGallery(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.cardview_callary_image_imageview);
        }
    }
}
