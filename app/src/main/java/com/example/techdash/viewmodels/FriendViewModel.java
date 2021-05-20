package com.example.techdash.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.techdash.models.User;
import com.example.techdash.repositories.UserRepository;

import java.util.ArrayList;

public class FriendViewModel extends ViewModel {
    private static final String TAG = FriendViewModel.class.getSimpleName();
    UserRepository userRepository;

    public FriendViewModel(){
        Log.d(TAG,"Created");
        userRepository=UserRepository.getInstance();
    }

    public LiveData<ArrayList<User>> searchFriendToAdd(String uid){
        return userRepository.searchUserToAddFriend(uid);
    }

    public void addFriend(User friend){
        userRepository.addFriend(friend);
    }

    public LiveData<ArrayList<User>> getFriendList(){
        return userRepository.getFriendList();
    }

    public LiveData<User> getCurrentUser(){
        return UserRepository.getInstance().getUser();
    }


}
