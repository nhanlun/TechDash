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
    LiveData<ArrayList<User>> listLiveData;
    ArrayList<User> refFriendList = new ArrayList<User>();
    boolean[] isFriend = new boolean[100];

    public FriendAdapter(ArrayList<User> friends, FriendViewModel friendViewModel) {
        this.friends = friends;
        this.friendViewModel = friendViewModel;
        Arrays.fill(isFriend, false);
//        listLiveData = friendViewModel.getFriendList().observe(LifecycleOwner owner, new Observer<ArrayList<User>>() {
//            @Override
//            public void onChanged(ArrayList<User> users) {
//                refFriendList = users;
//                for(int i=0;i<refFriendList.size();++i){
//                    if(refFriendList.contains(friends.get(i))){
//                        isFriend[i]=true;
//                    }
//                }
//            }
//        });

    }

    public void setRefFriendList(ArrayList<User> refFriendList) {
        this.refFriendList = refFriendList;
        Log.d("Size of ref", String.valueOf(refFriendList.size()));
        checkAreFriends();
    }

    public void checkAreFriends() {
        Log.d("Hello", String.valueOf(friends.size()));
        for (int i = 0; i < friends.size(); i++) {
            for (int j = 0; j < refFriendList.size(); j++) {
                Log.d("Friends", friends.get(i).getUid());
                Log.d("Ref Fr", refFriendList.get(j).getUid());
                if (friends.get(i).getUid().equals(refFriendList.get(j).getUid())) {
                    isFriend[i] = true;
                    Log.d("Ye", "Ye");
                }
            }
        }
    }

    public void setFriendArrayList(ArrayList<User> friends) {
        this.friends = friends;
        Log.d("size of friend", String.valueOf(friends.size()));
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
        Log.d("sth", String.valueOf(position));
        Log.d("isFriend", String.valueOf(isFriend[position]));
        if (isFriend[position]) {
            holder.btnAddFriend.setImageResource(R.drawable.ic_baseline_check_30);
        } else {
            holder.btnAddFriend.setImageResource(R.drawable.add_friend);
            holder.btnAddFriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //goi len firestore addfriend nha
                    friendViewModel.addFriend(friends.get(position));
                    ((ImageButton) v).setImageResource(R.drawable.ic_baseline_check_30);
                }
            });
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
