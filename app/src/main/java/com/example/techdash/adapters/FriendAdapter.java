package com.example.techdash.adapters;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techdash.R;
import com.example.techdash.models.User;
import com.example.techdash.viewmodels.FriendViewModel;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import static android.content.ContentValues.TAG;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {
    ArrayList<User> friends = new ArrayList<User>();
    FriendViewModel friendViewModel;
    ArrayList<User> refFriendList = new ArrayList<User>();
    boolean[] isFriend = new boolean[100];
    boolean[] isCurrentUser = new boolean[100];
    User currentUser;

    public FriendAdapter(ArrayList<User> friends, FriendViewModel friendViewModel) {
        this.friends = friends;
        this.friendViewModel = friendViewModel;
        Arrays.fill(isFriend, false);
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public void checkIsCurrentUser() {
        for (int i = 0; i < friends.size(); ++i) {
            if (friends.get(i).getUid().equals(currentUser.getUid())) {
                isCurrentUser[i] = true;
            } else {
                isCurrentUser[i] = false;
            }
        }
    }

    public void setRefFriendList(ArrayList<User> refFriendList) {
        this.refFriendList = refFriendList;
        Log.d("Size of ref", String.valueOf(refFriendList.size()));
        checkAreFriends();
    }

    public void checkAreFriends() {
        for (int i = 0; i < friends.size(); i++) {
            for (int j = 0; j < refFriendList.size(); j++) {
                if (friends.get(i).getUid().equals(refFriendList.get(j).getUid())) {
                    isFriend[i] = true;
                    Log.d("a","a");
                    break;
                }
                else{
                    isFriend[i]=false;
                    Log.d("b","b");
                }
            }
        }
    }

    public void setFriendArrayList(ArrayList<User> friends) {
        this.friends = friends;
    }

    @NonNull
    @Override
    public FriendAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_search_result_row, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendAdapter.ViewHolder holder, int position) {
        holder.tvFriendName.setText(friends.get(position).getName());
        holder.tvFriendId.setText(friends.get(position).getUid());
        //holder.btnAddFriend.setImageResource(R.drawable.ic_baseline_person_add_24);
        holder.btnAddFriend.setVisibility(View.VISIBLE);
        Log.d("sth", String.valueOf(position));
        Log.d("isFriend", String.valueOf(isFriend[position]));
        if (isFriend[position]) {
            holder.btnAddFriend.setImageResource(R.drawable.check);
        } else {
            if (isCurrentUser[position]) {
                holder.btnAddFriend.setVisibility(View.INVISIBLE);
            } else {
                holder.btnAddFriend.setImageResource(R.drawable.add_friend);
                holder.btnAddFriend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        friendViewModel.addFriend(friends.get(position));
                        ((ImageButton) v).setImageResource(R.drawable.check);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvFriendName, tvFriendId;
        ImageButton btnAddFriend;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvFriendName = itemView.findViewById(R.id.tvFriendName);
            tvFriendId = itemView.findViewById(R.id.tvFriendId);
            btnAddFriend = itemView.findViewById(R.id.addFriendButton);
        }
    }
}
