package com.example.myapplicationinst;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplicationinst.adapter.ChatBodyAdapter;
import com.example.myapplicationinst.model.Message;
import com.example.myapplicationinst.model.User;
import com.example.myapplicationinst.modelclass.ChatBodyFragmentModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class ChatBody extends Fragment {

    private static final String TAG = "ChatBody";

    private RecyclerView mRecyclerView;
    private CircleImageView mUserImage;
    private TextView mUserName;
    private EditText mMessageBody;
    private User mCurrentUser;

    public ChatBody() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_body, container, false);
        mRecyclerView = view.findViewById(R.id.fragment_chat_body_recy);
        mMessageBody = view.findViewById(R.id.fragment_chat_body_message_body);
        mUserImage = view.findViewById(R.id.fragment_chat_body_profile_image);
        mUserName = view.findViewById(R.id.fragment_chat_body_username);

        if (getArguments() != null) {
            mCurrentUser = ChatBodyArgs.fromBundle(getArguments()).getPost().getUser();
            setUserData();
        }
        ChatBodyFragmentModel mChatBodyFragmentModel = ViewModelProviders.of(this).get(ChatBodyFragmentModel.class);
        mChatBodyFragmentModel.getData(mCurrentUser)
                .observe(getViewLifecycleOwner(), new Observer<List<Message>>() {
                    @Override
                    public void onChanged(List<Message> messages) {
                        Log.d(TAG, "onChanged: 00000 change" + messages.size());
                        Collections.sort(messages);
                        initRecyclerView(messages);
                    }
                });

        mMessageBody.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    addMessage();
                }
                return false;
            }
        });
        return view;
    }

    private void addMessage() {
        Message message = new Message(HomeFragment.userInfo.getUserKey(),
                mMessageBody.getText().toString().trim());

        FirebaseFirestore
                .getInstance()
                .collection("Chat")
                .document(HomeFragment.userInfo.getUserKey())
                .collection("Chats")
                .document(mCurrentUser.getUserKey())
                .collection("Message")
                .add(message)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "onSuccess: 0000 add message");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
        mMessageBody.setText("");

    }

    private void setUserData() {
        Picasso
                .with(getActivity())
                .load(Uri.parse(mCurrentUser.getUserProfile()))
                .placeholder(R.drawable.download)
                .into(mUserImage);
        mUserName.setText(mCurrentUser.getUserName());

    }

    private void initRecyclerView(List<Message> list) {
        ChatBodyAdapter mChatBodyAdapter = new ChatBodyAdapter(list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        mRecyclerView.setAdapter(mChatBodyAdapter);
    }
}