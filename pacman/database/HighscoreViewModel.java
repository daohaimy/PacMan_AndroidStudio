package com.example.pacman.database;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class HighscoreViewModel extends AndroidViewModel {

    private ScoresRepository mRepository;
    private final LiveData<List<Scores>> mAllScores;
    public HighscoreViewModel(Application application) {
        super(application);
        mRepository = new ScoresRepository(application);
        mAllScores = mRepository.allScores();
    }

    public LiveData<List<Scores>> allScores(){
        return mAllScores;
    }

    public void insert(Scores score){
        mRepository.insert(score);
    }

    public void update(Scores scores){
        mRepository.update(scores);
    }
    public void delete(Scores scores){
        mRepository.delete(scores);
    }
    public void deleteAll(){
        mRepository.deleteAll();
    }
}