package com.example.myapplicationinst;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplicationinst.adapter.LikeFragmentAdapter;
import com.example.myapplicationinst.model.User;
import com.example.myapplicationinst.util.PostSerealization;
import com.example.myapplicationinst.util.SearchToUserProfileFragmentArg;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;


public class LikeFragment extends Fragment {

    private RecyclerView mRecyclerView;

    private List<String> mLikeUserList;

    private User mUserInfo;

    public LikeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_like, container, false);
        if (getArguments() != null) {
            PostSerealization mPostSerialization = LikeFragmentArgs.fromBundle(getArguments()).getPost();
            mLikeUserList = mPostSerialization.getPost().getLikeList();
            getUserInfo();
        }
        //SearchView mSearchView = view.findViewById(R.id.fagment_like_searchview);
        //ImageView mBackPress = view.findViewById(R.id.fragment_like_back);
        mRecyclerView = view.findViewById(R.id.fragment_like_recyclerview);

        return view;
    }

    private void getUserInfo() {
        String mUid = "";
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            mUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
        Query query = FirebaseFirestore.getInstance()
                .collection("Users")
                .whereEqualTo("userId", mUid);
        query.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            mUserInfo = documentSnapshot.toObject(User.class);
                            mUserInfo.setUserKey(documentSnapshot.getId());
                            initialAdapter();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    private void initialAdapter() {
        LikeFragmentAdapter mLikeFragmentAdapter = new LikeFragmentAdapter(mLikeUserList,
                getActivity(), mUserInfo);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mLikeFragmentAdapter);

        mLikeFragmentAdapter.setLikeFragmentInterface(new LikeFragmentAdapter.LikeFragmentAdapterInterface() {
            @Override
            public void showUserProfile(int pos, String userId) {
                FirebaseFirestore.getInstance()
                        .collection("Users")
                        .document(userId)
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                User user = documentSnapshot.toObject(User.class);
                                if (user != null) {
                                    user.setUserKey(documentSnapshot.getId());
                                }
                                Navigation.findNavController(getView())
                                        .navigate(LikeFragmentDirections.actionLikeFragmentToUserProfile(
                                                new SearchToUserProfileFragmentArg(user)
                                        ));
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });


            }

            @Override
            public void sendUserRequest(int pos, User user) {

            }
        });
    }
}