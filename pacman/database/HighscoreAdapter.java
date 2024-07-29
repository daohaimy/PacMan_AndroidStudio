package com.example.pacman.database;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

public class HighscoreAdapter extends ListAdapter<Scores, ScoreViewHolder> {

    public HighscoreAdapter(@NonNull DiffUtil.ItemCallback<Scores> diffCallback){
        super(diffCallback);
    }

    @NonNull
    @Override
    public ScoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        return ScoreViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(ScoreViewHolder holder, int position){
        Scores current = getItem(position);
        holder.bind(current);
    }

    public static class ScoreDiff extends DiffUtil.ItemCallback<Scores>{
        @Override
        public boolean areItemsTheSame(@NonNull Scores oldScore, @NonNull Scores newScore){
            return oldScore == newScore;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Scores oldScore, @NonNull Scores newScore){
            return oldScore.getScore() == newScore.getScore()
                    && oldScore.getName().equals(newScore.getName());
        }
    }
}