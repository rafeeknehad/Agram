package com.example.myapplicationinst.adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplicationinst.R;
import com.example.myapplicationinst.model.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchFragmentAdapter extends RecyclerView.Adapter<SearchFragmentAdapter.SearchFragmntViewHolder>
        implements Filterable {

    private static final String TAG = "SearchFragmentAdapter";

    public List<User> mAlUserList;
    public List<User> mAlUserList1;
    public List<User> mAllUsetListFull;
    public Context mContext;

    public SearchFragmentInterface linsener;

    public SearchFragmentAdapter(List<User> mAlUserList, Context mContext) {
        this.mAlUserList = mAlUserList;
        mAllUsetListFull = new ArrayList<>(mAlUserList);
        this.mContext = mContext;
    }

    public void setSearchFragmentInterface(SearchFragmentInterface linsener) {
        this.linsener = linsener;
    }

    @NonNull
    @Override
    public SearchFragmntViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_search_fragment, parent, false);
        return new SearchFragmntViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchFragmntViewHolder holder, int position) {
        System.out.println("qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqww "+position);
        User user = mAlUserList.get(position);
        Uri uri = Uri.parse(user.getUserProfile());
        Picasso.with(mContext)
                .load(uri)
                .resize(100, 100)
                .placeholder(R.drawable.download)
                .into(holder.vImageView);
        holder.vTextView.setText(user.getUserName());
    }

    @Override
    public int getItemCount() {
        return mAlUserList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<User> filterList = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    filterList = mAlUserList1;
                } else {
                    String subString = constraint.toString().toLowerCase().trim();
                    for (User item : mAllUsetListFull) {
                        if (item.getUserName().toLowerCase().contains(subString)) {
                            filterList.add(item);
                        }
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filterList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mAlUserList.clear();
                mAlUserList.addAll((List) results.values);
                notifyDataSetChanged();
            }
        };
    }

    public interface SearchFragmentInterface {
        public void getPosition(int pos, User user);
    }

    public class SearchFragmntViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView vImageView;
        private TextView vTextView;
        private ImageView vClose;

        public SearchFragmntViewHolder(@NonNull View itemView) {
            super(itemView);
            vImageView = itemView.findViewById(R.id.cardview_search_fragment_userprofiel);
            vTextView = itemView.findViewById(R.id.cardview_search_fragment_username);
            vClose = itemView.findViewById(R.id.cardview_search_fragment_delete);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                        Log.d(TAG, "onClick: ---- " + getAdapterPosition() + "  " + mAlUserList.get(getAdapterPosition()).getUserName());
                        linsener.getPosition(getAdapterPosition(), mAlUserList.get(getAdapterPosition()));
                    }
                }
            });
        }
    }

    public void setData(List<User> data,List<User> allData)
    {
        System.out.println("wwwwwwwwwwwwwwwwwwwwwwww");
        mAllUsetListFull = new ArrayList<>(allData);
        mAlUserList1 = new ArrayList<>(data);
        mAlUserList = new ArrayList<>(data);
        notifyDataSetChanged();
    }
}
