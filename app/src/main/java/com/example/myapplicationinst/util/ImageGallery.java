package com.example.myapplicationinst.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import androidx.core.content.FileProvider;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImageGallery {

    private static final String TAG = "ImageGallery";

    public static List<String> getAllImages(Context context)
    {
        Uri uri;
        Cursor cursor;
        int columnIndexData,columnIndexFolderName;
        List<String> arrayList = new ArrayList<>();
        String absolutePathOfImage;

        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String [] projection = {MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME
        };

        String orderBy = MediaStore.Video.Media.DATE_TAKEN;
        cursor = context.getContentResolver().query(uri,projection,null,null,orderBy+" DESC");

        columnIndexData = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);

        while (cursor.moveToNext())
        {
            absolutePathOfImage  = cursor.getString(columnIndexData);
            arrayList.add(absolutePathOfImage);
        }
        return arrayList;
    }
}
