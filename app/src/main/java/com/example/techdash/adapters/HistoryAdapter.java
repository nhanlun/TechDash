package com.example.techdash.adapters;

import android.content.Context;
import android.icu.util.LocaleData;
import android.os.Build;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techdash.models.History;

import com.example.techdash.R;

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

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    ArrayList<History> historyArrayList;
    Context context;
    int layout;

    public HistoryAdapter(ArrayList<History> historyArrayList, Context context, int layout) {
        this.historyArrayList = historyArrayList;
        this.context = context;
        this.layout = layout;

    }

    public void setHistoryArrayList(ArrayList<History> historyArrayList) {
        this.historyArrayList = historyArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(layout,parent,false);

        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(historyArrayList.size()==0)
            return;
        String datetime = historyArrayList.get(position).getDateTime();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy",Locale.ENGLISH);
        SimpleDateFormat sdateFormat = new SimpleDateFormat("EEE, MMM dd, yyyy",Locale.ENGLISH);
        SimpleDateFormat stimeFormat = new SimpleDateFormat("HH:mm:ss",Locale.ENGLISH);

        try {
            Date date = simpleDateFormat.parse(datetime);
            String dateText = sdateFormat.format(date);
            String timeText = stimeFormat.format(date);
            Log.d("AAA",dateText);
            Log.d("AAA",timeText);
            holder.date.setText(dateText);
            holder.time.setText(timeText);
        } catch (ParseException e) {
            e.printStackTrace();
        }

//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
//        LocalDateTime date = LocalDateTime.parse(datetime, formatter);
//
//        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("E, MMM dd, yyyy",Locale.ENGLISH);
//        String mDate = dateFormat.format(date);
//        holder.date.setText(mDate);
//        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss",Locale.ENGLISH);
//        String mTime = timeFormat.format(date);
//        holder.time.setText(mTime);


        holder.imageView.setImageResource(R.drawable.map_button);
        holder.distance.setText(context.getString(R.string.distance)+": "+String.format("%.2f km", historyArrayList.get(position).getDistance()));

        long totalTime = historyArrayList.get(position).getTotalTime();
        holder.timeRun.setText(context.getString(R.string.time)+": "+String.format(Locale.getDefault(), "%02d:%02d:%02d",
                TimeUnit.SECONDS.toHours(totalTime),
                TimeUnit.SECONDS.toMinutes(totalTime) % 60,
                TimeUnit.SECONDS.toSeconds(totalTime) % 60
        ));
        holder.pace.setText(context.getString(R.string.pace)+": "+String.format("%.1f /km", historyArrayList.get(position).getPace()));
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
