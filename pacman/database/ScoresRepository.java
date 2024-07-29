package com.example.pacman.database;

import android.app.Application;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ScoresRepository {
    private ScoresDAO mScoresDao;
    private LiveData<List<Scores>> mAllScores;

    ScoresRepository(Application application){
        ScoresDatabase db = ScoresDatabase.getDatabase(application);
        mScoresDao = db.scoreDAO();
        mAllScores = mScoresDao.getAllScores();
    }

    public LiveData<List<Scores>> allScores(){
        return mAllScores;
    }

    public void insert(Scores score){
        //new InsertScoreAsncTask(mScoresDao).execute(score);

        ScoresDatabase.databaseWriteExecutor.execute(() -> {
            mScoresDao.insert(score);
        });
    }

    public void update(Scores scores){
        //new UpdateScoreAsncTask(mScoresDao).execute(scores);
    }

    public void delete(Scores scores){
        //new deleteScoreAsncTask(mScoresDao).execute(scores);
    }

    public void deleteAll(){
        //new DeleteAllScoreAsncTask(mScoresDao).execute();
    }
}