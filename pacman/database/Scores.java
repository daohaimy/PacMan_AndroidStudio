package com.example.pacman.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "score_list")
public class Scores {

    @ColumnInfo(name="score_id")
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name="score")
    public int score;

    @ColumnInfo(name="name")
    public String name;

    public Scores(int score, String name) {
        this.score = score;
        this.name = name;
    }


    public int getScore() {
        return score;
    }

//    public void setScore(ArrayList<Integer> score){
//        this.score = score;
//    }

    public String getName() {
        return name;
    }

    public int getID(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

//    public void setName(String name) {
//        this.name = name;
//    }
}