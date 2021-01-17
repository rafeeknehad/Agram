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

import java.util.List;

public class AdapterImagesGallery extends RecyclerView.Adapter<AdapterImagesGallery.ViewHolderImageGallery> {

    private static final String TAG = "AdapterImagesGallery";
    public List<String> vDataList;
    public Context vContext;
    private AdapterImageGalleryInterface vListener;

    public AdapterImagesGallery(List<String> vDataList, Context vContext) {
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
        return vDataList.size();
    }

    public interface AdapterImageGalleryInterface {
        public void getImagePosition(int pos, String src,boolean check);
    }

    public void setAdapterImageGalleryInterface(AdapterImageGalleryInterface vListener)
    {
        this.vListener = vListener;
    }

    public class ViewHolderImageGallery extends RecyclerView.ViewHolder {
        private ImageView mImageView;
        private ImageView mImageViewSelected;
        public ViewHolderImageGallery(@NonNull final View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.cardview_callary_image_imageview);
            mImageViewSelected = itemView.findViewById(R.id.cardview_callary_image_selected);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                        if(mImageViewSelected.getVisibility()==View.VISIBLE)
                        {
                            mImageViewSelected.setVisibility(View.GONE);
                            vListener.getImagePosition(getAdapterPosition(),vDataList.get(getAdapterPosition()),false);
                        }
                        else
                        {
                            mImageViewSelected.setVisibility(View.VISIBLE);
                            vListener.getImagePosition(getAdapterPosition(),vDataList.get(getAdapterPosition()),true);
                        }
                    }
                }
            });
        }
    }

}
