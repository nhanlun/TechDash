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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techdash.R;
import com.example.techdash.adapters.FriendAdapter;
import com.example.techdash.adapters.FriendListAdapter;
import com.example.techdash.models.Contest;
import com.example.techdash.models.User;
import com.example.techdash.repositories.UserRepository;
import com.example.techdash.viewmodels.FriendViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class FriendFragment extends Fragment {
    private static final String TAG = FriendFragment.class.getSimpleName();
    private FriendViewModel friendViewModel;
    private EditText searchBar;
    private ImageButton searchButton;
    private RecyclerView searchResultList, friendList;
    private FriendAdapter friendAdapter;
    private FriendListAdapter friendListAdapter;
    private View v;

    public FriendFragment() {
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
        v = inflater.inflate(R.layout.fragment_friend, container, false);

        friendList = v.findViewById(R.id.friendList);
        LinearLayoutManager linearLayoutManagerForFriendList = new LinearLayoutManager(getContext());
        friendList.setLayoutManager(linearLayoutManagerForFriendList);
        ArrayList<User> arrayListForFriendList = new ArrayList<User>();
        friendListAdapter = new FriendListAdapter(arrayListForFriendList, friendViewModel);
        friendList.setAdapter(friendListAdapter);

        friendViewModel.fetchFriendList();
        friendViewModel.getFriendList().observe(getViewLifecycleOwner(), new Observer<ArrayList<User>>() {
            @Override
            public void onChanged(ArrayList<User> users) {
                friendListAdapter.setFriendArrayList(users);
                friendListAdapter.notifyDataSetChanged();
            }
        });

        return v;
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
                FriendFragmentDirections.SearchFriendAction action = FriendFragmentDirections.searchFriendAction(s);
                Navigation.findNavController(v).navigate(action);
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