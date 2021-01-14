package com.example.myapplicationinst;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.myapplicationinst.model.User;

import java.util.List;

public class SearchFragment extends Fragment {

    private static final String TAG = "SearchFragment";

    //view model
    private SearchFragmentModel searchFragmentModel;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        searchFragmentModel = ViewModelProviders.of(this).get(SearchFragmentModel.class);

        searchFragmentModel.getAllUserForServer().observe(getViewLifecycleOwner(), new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                Log.d(TAG, "onChanged: " + getActivity().getString(R.string.logd) + " " + users.size());
            }
        });


        return view;
    }
}