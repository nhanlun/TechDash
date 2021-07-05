package com.example.techdash.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.techdash.models.History;
import com.example.techdash.repositories.RecordRunRepository;

import java.util.ArrayList;

public class HistoryViewModel extends ViewModel {
    public LiveData<ArrayList<History>> getHistories(String uid) {
        return RecordRunRepository.getInstance().fetch(uid);
    }
}
