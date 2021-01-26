package com.example.myapplicationinst;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplicationinst.model.Message;
import com.example.myapplicationinst.modelclass.ChatFragmentModel;

import java.util.List;


public class ChatFragment extends Fragment {


    private SearchView mSearchView;
    private RecyclerView mRecyclerView;

    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        mSearchView = view.findViewById(R.id.fragment_chat_search);
        mRecyclerView = view.findViewById(R.id.fragment_chat_recycler);
        ImageView mImageView = view.findViewById(R.id.fragment_chat_back);

        //model
        ChatFragmentModel mChatFragmentModel = ViewModelProviders.of(this).get(ChatFragmentModel.class);
        mChatFragmentModel.getAllData().observe(getViewLifecycleOwner(), new Observer<List<Message>>() {
            @Override
            public void onChanged(List<Message> messages) {

            }
        });

        return view;
    }
}