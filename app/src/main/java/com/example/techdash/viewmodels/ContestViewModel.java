package com.example.techdash.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.example.techdash.models.Contest;
import com.example.techdash.repositories.ContestRepository;

import java.util.ArrayList;

public class ContestViewModel extends ViewModel {

    public ContestViewModel() {
    }

    public LiveData<ArrayList<Contest>> getContests() {
        return ContestRepository.getInstance().fetch();
    }

    public void save(Contest contest) {
        ContestRepository.getInstance().save(contest);
    }
}
