package com.example.pacman.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.pacman.database.Scores;

import java.util.List;
@Dao
public interface ScoresDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Scores scores);
    @Update
    void updateScore(Scores scores);
    @Delete
    void deleteScore(Scores scores);
    @Query("select * from score_list order by Score desc")
    LiveData<List<Scores>> getAllScores();


    @Query("Delete from score_list")
    void deleteAll();
}