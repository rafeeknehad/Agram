package com.example.myapplicationinst;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplicationinst.adapter.ProfileFragmentAdapter;
import com.example.myapplicationinst.model.Post;
import com.example.myapplicationinst.modelclass.ProfileFragmentModel;
import com.example.myapplicationinst.util.UserPostFragmentSelerization;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment ****";
    protected RecyclerView mRecyclerView;
    //ui
    private Toolbar toolbar;
    //Adapter
    private ProfileFragmentAdapter mProfileFragmentAdapter;

    //model
    private ProfileFragmentModel mProfileFragmentModel;

    //list
    private List<Post> mPostList;


    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        toolbar = view.findViewById(R.id.fragment_profile_toolbar);
        mRecyclerView = view.findViewById(R.id.fragment_profile_recyclerview);
        mPostList = new ArrayList<>();

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).setTitle("");



        mProfileFragmentModel = ViewModelProviders.of(this).get(ProfileFragmentModel.class);
        mProfileFragmentModel.getData().observe(getViewLifecycleOwner(), new Observer<List<Post>>() {
            @Override
            public void onChanged(List<Post> posts) {
                mPostList = posts;
                initComponent(posts);
            }
        });

        return view;
    }

    private void initComponent(final List<Post> posts) {
        mProfileFragmentAdapter = new ProfileFragmentAdapter(posts, getActivity());
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2, RecyclerView.VERTICAL, false));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mProfileFragmentAdapter);

        mProfileFragmentAdapter.setInteface(new ProfileFragmentAdapter.InterfaceProfileFragment() {
            @Override
            public void getSelectedItem(int pos, Post post) {
                UserPostFragmentSelerization selerization = new UserPostFragmentSelerization(posts,pos);
                Navigation.findNavController(getView()).navigate(ProfileFragmentDirections.actionProfileFragmentToUserPostFragment(selerization));
            }
        });
    }

}