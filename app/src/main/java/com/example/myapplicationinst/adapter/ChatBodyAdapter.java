package com.example.myapplicationinst.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplicationinst.HomeFragment;
import com.example.myapplicationinst.R;
import com.example.myapplicationinst.model.Message;

import java.util.List;


public class ChatBodyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "ChatBodyAdapter";

    private List<Message> mListMessage;

    public ChatBodyAdapter(List<Message> mListMessage) {
        this.mListMessage = mListMessage;
    }

    @Override
    public int getItemViewType(int position) {
        Log.d(TAG, "getItemViewType: 00000 " + mListMessage.size() + " " + position);
        if (position < mListMessage.size() && this.mListMessage.get(position).getCurrentUser().equals(HomeFragment.userInfo.getUserKey())) {
            return 0;
        } else
            return 1;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (this.getItemViewType(viewType) == 0) {
            return new SendMessageViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.cardview_send_message,
                            parent, false));
        } else {
            return new RecieveMessageViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.cardview_receive_message,
                            parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (this.getItemViewType(position) == 0) {
            SendMessageViewHolder viewHolder = (SendMessageViewHolder) holder;
            viewHolder.mTextView.setText(mListMessage.get(position).getMessage());
        } else if (this.getItemViewType(position) == 1) {
            RecieveMessageViewHolder viewHolder = (RecieveMessageViewHolder) holder;
            viewHolder.mTextView.setText(mListMessage.get(position).getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return this.mListMessage.size();
    }

    public static class SendMessageViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextView;

        public SendMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.card_send_message_text);
        }
    }

    public static class RecieveMessageViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextView;

        public RecieveMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.card_receive_message_text);
        }
    }
}
