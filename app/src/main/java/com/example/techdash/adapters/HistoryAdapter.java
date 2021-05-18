package com.example.techdash.adapters;

import android.content.Context;
import android.icu.util.LocaleData;
import android.net.Uri;
import android.os.Build;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.techdash.fragments.HistoryFragmentDirections;
import com.example.techdash.models.History;

import com.example.techdash.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder>{
    ArrayList<History> historyArrayList;
    Context context;
    int layout;
    FirebaseStorage firebaseStorage;

    public HistoryAdapter(ArrayList<History> historyArrayList, Context context, int layout) {
        this.historyArrayList = historyArrayList;
        this.context = context;
        this.layout = layout;
        firebaseStorage = FirebaseStorage.getInstance();
    }

    public void setHistoryArrayList(ArrayList<History> historyArrayList) {
        this.historyArrayList = historyArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(layout,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(v -> {
            int position = viewHolder.getAdapterPosition();
            History history = historyArrayList.get(position);

            HistoryFragmentDirections.DisplayMap action = HistoryFragmentDirections.displayMap(history);
            Navigation.findNavController(v).navigate(action);
        });
        return viewHolder ;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(historyArrayList.size()==0)
            return;
        History history = historyArrayList.get(position);
        String datetime = history.getDateTime();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy",Locale.ENGLISH);
        SimpleDateFormat sdateFormat = new SimpleDateFormat("EEE, MMM dd, yyyy",Locale.ENGLISH);
        SimpleDateFormat stimeFormat = new SimpleDateFormat("HH:mm:ss",Locale.ENGLISH);

        try {
            Date date = simpleDateFormat.parse(datetime);
            String dateText = sdateFormat.format(date);
            String timeText = stimeFormat.format(date);
            holder.date.setText(dateText);
            holder.time.setText(timeText);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Task<Uri> url = firebaseStorage.getReference().child("images/" + history.getId()).getDownloadUrl();
        holder.imageView.setImageResource(R.drawable.map_button);
        url.addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri.toString()).into(holder.imageView);
            }
        });
//        holder.imageView.setImageResource(R.drawable.map_button);
        holder.distance.setText(context.getString(R.string.distance)+": "+String.format("%.2f km", history.getDistance()));

        long totalTime = history.getTotalTime();
        holder.timeRun.setText(context.getString(R.string.time)+": "+String.format(Locale.getDefault(), "%02d:%02d:%02d",
                TimeUnit.SECONDS.toHours(totalTime),
                TimeUnit.SECONDS.toMinutes(totalTime) % 60,
                TimeUnit.SECONDS.toSeconds(totalTime) % 60
        ));
        holder.pace.setText(context.getString(R.string.pace)+": "+String.format("%.1f /km", history.getPace()));
    }


    @Override
    public int getItemCount() {
        return historyArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView date, time, distance, timeRun, pace;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageMap);
            date = itemView.findViewById(R.id.tvDate);
            time = itemView.findViewById(R.id.tvTime);
            distance = itemView.findViewById(R.id.tvDistance);
            timeRun = itemView.findViewById(R.id.tvTimeRun);
            pace = itemView.findViewById(R.id.tvPace);
        }
    }
}
