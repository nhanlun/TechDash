package com.example.techdash.adapters;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techdash.R;
import com.example.techdash.models.User;
import com.example.techdash.viewmodels.FriendViewModel;

import java.util.ArrayList;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {
    ArrayList<User> friends = new ArrayList<User>();
    FriendViewModel friendViewModel;

    public FriendAdapter(ArrayList<User> friends, FriendViewModel friendViewModel){
        this.friends = friends;
        this.friendViewModel = friendViewModel;
    }

    public void setFriendArrayList(ArrayList<User> friends){
        this.friends=friends;
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
            holder.btnAddFriend.setImageResource(R.drawable.ic_baseline_person_add_24);
            holder.btnAddFriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //goi len firestore addfriend nha
                    friendViewModel.addFriend(friends.get(position));
                    ((ImageButton) v).setImageResource(R.drawable.ic_baseline_check_30);
                }
            });
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
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
