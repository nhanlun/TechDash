package com.example.techdash.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techdash.R;
import com.example.techdash.adapters.FriendAdapter;
import com.example.techdash.adapters.FriendListAdapter;
import com.example.techdash.models.User;
import com.example.techdash.viewmodels.FriendViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class FriendSearchFragment extends Fragment {
    private static final String TAG = FriendSearchFragment.class.getSimpleName();
    private FriendViewModel friendViewModel;
    private EditText searchBar;
    private ImageButton searchButton;
    private RecyclerView searchResultList;
    private FriendAdapter friendAdapter;

    public FriendSearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        friendViewModel = new ViewModelProvider(requireActivity()).get(FriendViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_search_friend, container, false);

        assert getArguments() != null;
        String keyword = FriendSearchFragmentArgs.fromBundle(getArguments()).getKeyword();

        searchResultList = v.findViewById(R.id.searchResultfriendList);
        ArrayList<User> friendArrayList = new ArrayList<User>();
        friendAdapter = new FriendAdapter(friendArrayList, friendViewModel);
        searchResultList.setAdapter(friendAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        searchResultList.setLayoutManager(linearLayoutManager);

        friendViewModel.getCurrentUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                friendAdapter.setCurrentUser(user);
                friendAdapter.notifyDataSetChanged();
            }
        });

        friendViewModel.getFriendList().observe(getViewLifecycleOwner(), new Observer<ArrayList<User>>() {
            @Override
            public void onChanged(ArrayList<User> users) {
                friendAdapter.setRefFriendList(users);
                friendAdapter.notifyDataSetChanged();
            }
        });
        search(keyword);

        return v;
    }

    public void search(String keyword) {
        friendViewModel.searchFriendToAdd(keyword).observe(getViewLifecycleOwner(), new Observer<ArrayList<User>>() {
            @Override
            public void onChanged(ArrayList<User> users) {
                friendAdapter.setFriendArrayList(users);
                friendAdapter.checkAreFriends();
                friendAdapter.checkIsCurrentUser();
                friendAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NotNull Menu menu, @NotNull MenuInflater inflater) {
        Log.d(TAG, "created search view");
        inflater.inflate(R.menu.contest_search_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_icon);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Enter your friend's name or ID here");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (s.isEmpty()) return true;
                Log.d(TAG, "Testing");
                search(s);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }
}
