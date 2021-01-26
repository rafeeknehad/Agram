package com.example.myapplicationinst;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

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

    //adapter
    private SearchFragmentAdapter searchFragmentAdapter;
    SearchView searchView;

    private List<User> searchUser;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        //ui
        Toolbar toolbar = view.findViewById(R.id.fragment_search_toolbar);
        RecyclerView recyclerView = view.findViewById(R.id.fragment_search_recy);
        if (getActivity() != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            ((AppCompatActivity) getActivity()).setTitle("");
            setHasOptionsMenu(true);
        }
        //view model
        SearchFragmentModel searchFragmentModel = ViewModelProviders.of(this).get(SearchFragmentModel.class);

        //list
        List<User> allUser = new ArrayList<>();
        searchUser = new ArrayList<>();
        searchFragmentAdapter = new SearchFragmentAdapter(allUser, getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(searchFragmentAdapter);


        System.out.println("qqqqqqqqqqqqqqqqqqqqqqqqqqqqqq");
        searchFragmentModel.getAllUserForServer().observe(getViewLifecycleOwner(), new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                Log.d(TAG, "onChanged: <<<<<");
                for (User user : users) {
                    if (user.getUserKey().equals(HomeFragment.userInfo.getUserKey())) {
                        users.remove(user);
                        break;
                    }
                }
                searchFragmentAdapter.setData(searchUser, users);
            }
        });

        searchFragmentAdapter.setSearchFragmentInterface(new SearchFragmentInterface() {
            @Override
            public void getPosition(int pos, User user) {
                Log.d(TAG, "getPosition: ---- " + pos + " " + user);
                SearchToUserProfileFragmentArg arg = new SearchToUserProfileFragmentArg(user);
                if (getView() != null) {
                    closeKeyboard();
                    Navigation.findNavController(getView()).navigate(SearchFragmentDirections.actionSearchFragmentToUserProfile
                            (arg));
                }
            }
        });
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (getActivity() != null) {
            MenuInflater menuInflater = getActivity().getMenuInflater();
            menuInflater.inflate(R.menu.search_fragment_menu, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.search_fragment_menu_search) {
            searchView = (SearchView) item.getActionView();
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return true;
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


    private void closeKeyboard() {
        if (getActivity() != null) {
            View view = getActivity().getCurrentFocus();
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            if (view != null) {
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }

    }
}