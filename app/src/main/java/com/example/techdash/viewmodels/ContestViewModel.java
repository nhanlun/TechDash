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

    public LiveData<ArrayList<String>> fetchParticipants(String contestId) {
        return ContestRepository.getInstance().fetchParticipants(contestId);
    }

    public void addParticipant(String contestId, String uid) {
        ContestRepository.getInstance().addParticipant(contestId, uid);
    }

    public void removeParticipant(String contestId, String uid) {
        ContestRepository.getInstance().removeParticipant(contestId, uid);
    }

    public void save(Contest contest) {
        ContestRepository.getInstance().save(contest);
    }
}
