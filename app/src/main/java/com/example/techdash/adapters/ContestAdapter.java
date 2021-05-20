package com.example.techdash.adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techdash.fragments.ContestFragmentDirections;
import com.example.techdash.viewmodels.ContestViewModel;
import com.google.firebase.storage.FirebaseStorage;

import com.example.techdash.R;
import com.example.techdash.models.Contest;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ContestAdapter extends RecyclerView.Adapter<ContestAdapter.ViewHolder>{
    private ArrayList<Contest> contestList;
    private Context context;
    private int layout;
    FirebaseStorage firebaseStorage;

    public ContestAdapter(ArrayList<Contest> contestList, Context context, int layout) {
        this.contestList = contestList;
        this.context = context;
        this.layout = layout;
        firebaseStorage = FirebaseStorage.getInstance();
    }

    public void setContestArrayList(ArrayList<Contest> contestList) { this.contestList = contestList; }

    public ArrayList<Contest> getContestArrayList() { return contestList; }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(layout,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(v -> {
            int position = viewHolder.getAdapterPosition();
            Contest contest = contestList.get(position);

//            ContestFragmentDirections.DisplayContest action = ContestFragmentDirections.displayContest(contest);
//            Navigation.findNavController(v).navigate(action);
        });
        return viewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(contestList.size()==0) {
            Toast.makeText(context, "No results!", Toast.LENGTH_LONG).show();
            return;
        }
        Contest contest = contestList.get(position);
        long start_InMili = Long.parseLong(contest.getStartTime());
        long end_InMili = Long.parseLong(contest.getEndTime());

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat sdateFormat = new SimpleDateFormat("EEE, MMM dd, yyyy",Locale.ENGLISH);
        SimpleDateFormat stimeFormat = new SimpleDateFormat("HH:mm", Locale.ENGLISH);

        calendar.setTimeInMillis(start_InMili);
        String dateText = sdateFormat.format(calendar.getTime());
        String timeText = stimeFormat.format(calendar.getTime());
        calendar.setTimeInMillis(end_InMili);
        timeText = timeText + " - " + stimeFormat.format(calendar.getTime());
        holder.date.setText(dateText);
        holder.time.setText(timeText);

        /*Task<Uri> url = firebaseStorage.getReference().child("images/" + history.getId()).getDownloadUrl();
        holder.imageView.setImageResource(R.drawable.map_button);
        url.addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri.toString()).into(holder.imageView);
            }
        });*/

        holder.imageView.setImageResource(R.drawable.map_button);
        holder.destination.setText(contest.getDestination());
        holder.name.setText(contest.getName());
        holder.creator_username.setText(String.format("Created by: %s",contest.getCreator()));

        if(contest.isEnded()) {
            holder.imageView.setAlpha((float) 0.5);
            holder.destination.setAlpha((float) 0.5);
            holder.name.setAlpha((float) 0.5);
            holder.creator_username.setAlpha((float) 0.5);
            holder.date.setAlpha((float) 0.5);
            holder.time.setAlpha((float) 0.5);
        }
    }

    @Override
    public int getItemCount() { return contestList.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView time, name, destination, creator_username, date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageMap);
            time = itemView.findViewById(R.id.tvTime);
            date = itemView.findViewById(R.id.tvDate);
            name = itemView.findViewById(R.id.tvName);
            destination = itemView.findViewById(R.id.tvDestination);
            creator_username = itemView.findViewById(R.id.tvCreator);
        }
    }
}
