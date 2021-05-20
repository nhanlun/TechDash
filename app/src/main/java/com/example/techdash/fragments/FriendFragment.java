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

    public FriendFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        friendViewModel = new ViewModelProvider(this).get(FriendViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_friend, container, false);

//        searchBar = v.findViewById(R.id.searchBar);
//        searchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
////                    search();
//                    return true;
//                }
//                return false;
//            }
//        });
//        searchButton = v.findViewById(R.id.searchButton);
//        searchButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                search();
//                String key = searchBar.getText().toString();
//                UserRepository.getInstance().searchUserToAddFriend(key);
//                friendViewModel.searchFriendToAdd(key).observe(getViewLifecycleOwner(), new Observer<ArrayList<User>>() {
//                    @Override
//                    public void onChanged(ArrayList<User> users) {
//                        friendAdapter.setFriendArrayList(users);
//                        friendAdapter.notifyDataSetChanged();
//                        searchResultList.setVisibility(View.VISIBLE);
//                        friendList.setVisibility(View.GONE);
//                    }
//                });
//            }
//        });

        searchResultList = v.findViewById(R.id.searchResultfriendList);
        ArrayList<User> friendArrayList = new ArrayList<User>();
        friendAdapter = new FriendAdapter(friendArrayList, friendViewModel);
        searchResultList.setAdapter(friendAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        searchResultList.setLayoutManager(linearLayoutManager);

        friendList = v.findViewById(R.id.friendList);
        LinearLayoutManager linearLayoutManagerForFriendList = new LinearLayoutManager(getContext());
        friendList.setLayoutManager(linearLayoutManagerForFriendList);
        ArrayList<User> arrayListForFriendList = new ArrayList<User>();
        friendListAdapter = new FriendListAdapter(arrayListForFriendList, friendViewModel);
        friendList.setAdapter(friendListAdapter);

        friendViewModel.getFriendList().observe(getViewLifecycleOwner(), new Observer<ArrayList<User>>() {
            @Override
            public void onChanged(ArrayList<User> users) {
                friendListAdapter.setFriendArrayList(users);
                friendListAdapter.notifyDataSetChanged();
                friendAdapter.setRefFriendList(users);
                friendAdapter.notifyDataSetChanged();
            }
        });
        friendViewModel.getCurrentUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                friendAdapter.setCurrentUser(user);
            }
        });

        return v;
    }

    private void search(String key) {
//        String key = searchBar.getText().toString();
        friendViewModel.searchFriendToAdd(key).observe(getViewLifecycleOwner(), new Observer<ArrayList<User>>() {
            @Override
            public void onChanged(ArrayList<User> users) {
                friendAdapter.setFriendArrayList(users);
                friendAdapter.checkAreFriends();
                friendAdapter.checkIsCurrentUser();
                friendAdapter.notifyDataSetChanged();
                searchResultList.setVisibility(View.VISIBLE);
                friendList.setVisibility(View.GONE);
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

    /*ArrayList<Contest> filter(String s) {
        if (s.equals("")) return contestList;
        ArrayList<Contest> tempList = new ArrayList<Contest>();
        if(contestList != null) {
            int length = contestList.size();
            int i=0;
            while(i < length){
                Contest item = contestList.get(i);
                if (item.getName().startsWith(s))
                    tempList.add(item);
                i++;
            }
        }
        return tempList;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        inflater.inflate(R.menu.contest_search_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_icon);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search for contest name");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                contestAdapter.setContestArrayList(filter(s));
                contestAdapter.notifyDataSetChanged();
                return true;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }*/
}