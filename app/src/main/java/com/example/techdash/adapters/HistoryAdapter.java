package com.example.techdash.adapters;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techdash.models.Record;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    ArrayList<Record> recordArrayList;
    Context context;
    int layout;

    public HistoryAdapter(ArrayList<Record> recordArrayList, Context context, int layout) {
        this.recordArrayList = recordArrayList;
        this.context = context;
        this.layout = layout;
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
//        holder.imageView.setImageBitmap(recordArrayList.get(position).getUrl());
        holder.datetime.setText(recordArrayList.get(position).getDate());
//        holder.time.setText();
    }


    @Override
    public int getItemCount() {
        return recordArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView datetime, time, distance, pace;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            int idImage = 0, idDate=0, idTime=0, idDistance=0,idPace=0;
            imageView = itemView.findViewById(idImage);
            datetime = itemView.findViewById(idDate);
            time = itemView.findViewById(idTime);
            distance = itemView.findViewById(idDistance);
            pace = itemView.findViewById(idPace);
        }
    }
}
