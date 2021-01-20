package com.example.myapplicationinst;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplicationinst.adapter.SearchFragmentAdapter;
import com.example.myapplicationinst.model.User;
import com.example.myapplicationinst.modelclass.SearchFragmentModel;
import com.example.myapplicationinst.util.SearchToUserProfileFragmentArg;

import java.util.ArrayList;
import java.util.List;

import static com.example.myapplicationinst.adapter.SearchFragmentAdapter.SearchFragmentInterface;

public class SearchFragment extends Fragment {

    private static final String TAG = "SearchFragment";

    //view model
    private SearchFragmentModel searchFragmentModel;

    //ui
    private Toolbar toolbar;
    private RecyclerView recyclerView;

    //adapter
    private SearchFragmentAdapter searchFragmentAdapter;

    //list
    private List<User> allUser;
    private List<User> seacrhUser;
    private User mCurrentUser;
    private String currentUser;
    private List<String> searchUserId;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        toolbar = view.findViewById(R.id.fragment_search_toolbar);
        recyclerView = view.findViewById(R.id.fragment_search_recy);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).setTitle("");
        setHasOptionsMenu(true);
        searchFragmentModel = ViewModelProviders.of(this).get(SearchFragmentModel.class);

        allUser = new ArrayList<>();
        seacrhUser = new ArrayList<>();
        searchFragmentAdapter = new SearchFragmentAdapter(allUser, getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(searchFragmentAdapter);


        System.out.println("qqqqqqqqqqqqqqqqqqqqqqqqqqqqqq");
        searchFragmentModel.getAllUserForServer().observe(getViewLifecycleOwner(), new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                searchFragmentAdapter.setData(seacrhUser, users);
            }
        });

        searchFragmentAdapter.setSearchFragmentInterface(new SearchFragmentInterface() {
            @Override
            public void getPosition(int pos, User user) {
                Log.d(TAG, "getPosition: ---- " + pos + " " + user);
                SearchToUserProfileFragmentArg arg = new SearchToUserProfileFragmentArg(user);
                Navigation.findNavController(getView()).navigate(SearchFragmentDirections.actionSearchFragmentToUserProfile
                        (arg));
            }
        });
        return view;
    }

    private void setDataForAdapter(List<User> users) {
        System.out.println("qqqqqqqqqqqqqqqqqqqqqqqqqqqqq");
        for (String item : mCurrentUser.getSearchingList()) {
            for (User user : users) {
                if (item.equals(user.getUserKey())) {
                    seacrhUser.add(user);
                    break;
                }
            }
        }
        searchFragmentAdapter.setData(seacrhUser, users);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.search_fragment_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search_fragment_menu_search:
                SearchView searchView = (SearchView) item.getActionView();
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        searchFragmentAdapter.getFilter().filter(newText);
                        searchFragmentAdapter.notifyDataSetChanged();
                        return true;
                    }
                });
                return true;
        }
        return false;
    }
}