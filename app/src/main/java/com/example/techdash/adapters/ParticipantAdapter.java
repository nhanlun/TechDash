package com.example.techdash.adapters;

import android.content.Context;
import android.os.Build;
import android.util.Log;
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
import com.example.techdash.models.User;
import com.example.techdash.viewmodels.ContestViewModel;
import com.example.techdash.viewmodels.UserViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
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
import java.util.Map;

public class ParticipantAdapter extends RecyclerView.Adapter<ParticipantAdapter.ViewHolder>{
    private ArrayList<String> participantIDs;
    private Context context;
    private int layout;
    FirebaseFirestore fireStore;

    public ParticipantAdapter(ArrayList<String> participantIDs, Context context, int layout) {
        this.participantIDs = participantIDs;
        this.context = context;
        this.layout = layout;
        fireStore = FirebaseFirestore.getInstance();
    }

    public void setParticipantIDList(ArrayList<String> IDList) { this.participantIDs = IDList; }

    public ArrayList<String> getParticipantIDList() { return participantIDs; }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(layout, parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        /*view.setOnClickListener(v -> {
            int position = viewHolder.getAdapterPosition();
            Contest contest = contestList.get(position);

            ContestFragmentDirections.DisplayContest action = ContestFragmentDirections.displayContest(contest);
            Navigation.findNavController(v).navigate(action);
        });*/
        return viewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(participantIDs.size() == 0) {
            Toast.makeText(context, "No results!", Toast.LENGTH_LONG).show();
            return;
        }
//        if (position == 0) holder.host.setVisibility(View.VISIBLE);
        String uid = participantIDs.get(position);
        fireStore.collection("users").document(uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            Map<String, Object> map = task.getResult().getData();
                            if (map != null) {
                                holder.name.setText((String) map.get("name"));
                                holder.energy.setText(String.format("Energy: %d", (long) map.get("energy")));
                            }
                        }
                    }
                });
    }

    @Override
    public int getItemCount() { return participantIDs.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView host;
        TextView name, energy;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            host = itemView.findViewById(R.id.ct_host);
            name = itemView.findViewById(R.id.ct_participant_name);
            energy = itemView.findViewById(R.id.ct_participant_energy);
        }
    }
}
