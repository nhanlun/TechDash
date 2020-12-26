package com.example.techdash.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techdash.models.History;

import com.example.techdash.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    ArrayList<History> historyArrayList;
    Context context;
    int layout;

    public HistoryAdapter(ArrayList<History> historyArrayList, Context context, int layout) {
        this.historyArrayList = historyArrayList;
        this.context = context;
        this.layout = layout;
        Log.d("AAA","constructor");
        Log.d("AAA",String.valueOf(historyArrayList.size()));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(layout,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String datetime = historyArrayList.get(position).getDateTime();
        try {
            Date date = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy").parse(datetime);
            SimpleDateFormat dateFormat = new SimpleDateFormat("E, MMM dd, yyyy");
            holder.date.setText(dateFormat.format(date));
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
            holder.time.setText("at"+timeFormat.format(date));

        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.imageView.setImageResource(R.drawable.map_button);
        holder.distance.setText("Distance: "+String.format("%.2f km", historyArrayList.get(position).getDistance()));

        long totalTime = historyArrayList.get(position).getTotalTime();
        holder.timeRun.setText("Time: "+String.format("%02d:%02d:%02d",
                TimeUnit.SECONDS.toHours(totalTime),
                TimeUnit.SECONDS.toMinutes(totalTime),
                TimeUnit.SECONDS.toSeconds(totalTime)
        ));
        holder.pace.setText("Pace: "+String.format("%.1f /km", historyArrayList.get(position).getPace()));
    }


    @Override
    public int getItemCount() {
        return historyArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
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
