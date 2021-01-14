package com.example.myapplicationinst.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SearchFragmentAdapter extends RecyclerView.Adapter<SearchFragmentAdapter.SearchFragmntViewHolder> {
    @NonNull
    @Override
    public SearchFragmntViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchFragmntViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class SearchFragmntViewHolder extends RecyclerView.ViewHolder {

        public SearchFragmntViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
