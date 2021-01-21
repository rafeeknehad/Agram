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
import androidx.navigation.Navigation;

import com.example.myapplicationinst.model.User;
import com.example.myapplicationinst.modelclass.HomeFragmentModel;

import java.util.Collections;
import java.util.List;


public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";

    public static User mLoginUser;

    private ImageView mFavImage;
    private ImageView mMessageImage;

    //model
    private HomeFragmentModel mHomeFragmentModel;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mFavImage = view.findViewById(R.id.fragment_home_toolbar_fav);
        mMessageImage = view.findViewById(R.id.fragment_home_toolbar_chat);
        mHomeFragmentModel = ViewModelProviders.of(this).get(HomeFragmentModel.class);
        mHomeFragmentModel.getPostData().observe(getViewLifecycleOwner(), new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {
                Log.d(TAG, "onChanged: ////// " + strings.size());
                Collections.sort(strings);
                for (String item : strings) {
                    Log.d(TAG, "onChanged: ///// " + item);
                }
            }
        });
        mFavImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getView()).navigate(R.id.action_homeFragment_to_favFragment);
            }
        });

        mMessageImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    }

}