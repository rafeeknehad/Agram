package com.example.myapplicationinst;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplicationinst.adapter.FavFragmentAdapter;
import com.example.myapplicationinst.model.User;
import com.example.myapplicationinst.modelclass.FavFragmentModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;


public class FavFragment extends Fragment {

    private static final String TAG = "FavFragment";

    //ui
    private ImageView mBackPress;
    private RecyclerView mRecyclerView;

    //list
    private List<Object> mListAdapter;

    //adapter
    private FavFragmentAdapter mFavFragmentAdapter;

    //model
    private FavFragmentModel mFavFragmentModel;

    private List<Object> mRequestFollow;

    //currentuser
    private User mCurrentUser;


    public FavFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fav, container, false);
        mBackPress = view.findViewById(R.id.fragment_fav_back);
        mRecyclerView = view.findViewById(R.id.fragment_fav_recyclerview);
        mFavFragmentModel = ViewModelProviders.of(this).get(FavFragmentModel.class);
        mRequestFollow = new ArrayList<>();
        mFavFragmentModel.getData().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                mCurrentUser = user;
                Log.d(TAG, "onChanged: ///// " + user.getUserKey());
                getDataForAllUser();
            }
        });

        return view;
    }

    private void getDataForAllUser() {
        Log.d(TAG, "getDataForAllUser: //// " + mCurrentUser.getRecieveList());
        for (final String userId : mCurrentUser.getRecieveList()) {
            FirebaseFirestore
                    .getInstance()
                    .collection("Users")
                    .document(userId)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            User user = documentSnapshot.toObject(User.class);
                            user.setUserKey(documentSnapshot.getId());
                            mRequestFollow.add(user);
                            Log.d(TAG, "onSuccess: //// " + user.getUserName());
                            if (userId.equals(mCurrentUser.getRecieveList().get(mCurrentUser.getRecieveList().size() - 1))) {
                                setDataForAdapter();
                            }
                        }
                    });
        }
    }

    private void setDataForAdapter() {
        Log.d(TAG, "setDataForAdapter: //// " + mRequestFollow.size());
        mFavFragmentAdapter = new FavFragmentAdapter(mRequestFollow, getActivity());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        mRecyclerView.setAdapter(mFavFragmentAdapter);
    }
}