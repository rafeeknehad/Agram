package com.example.myapplicationinst;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;


public class HomeFragment extends Fragment {

    private ImageView mFavImage;
    private ImageView mMessageImage;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mFavImage = view.findViewById(R.id.fragment_home_toolbar_fav);
        mMessageImage = view.findViewById(R.id.fragment_home_toolbar_chat);

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