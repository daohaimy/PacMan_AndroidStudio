package com.example.pacman.database;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.pacman.R;

public class ScoreViewHolder extends RecyclerView.ViewHolder {

    private final TextView scoreWert;
    private final TextView scoreName;

    private ScoreViewHolder(View scoreView){
        super(scoreView);
        scoreWert = scoreView.findViewById(R.id.score_wert);
        scoreName = scoreView.findViewById(R.id.score_name);
    }

    public void bind(Scores score){
        scoreWert.setText(String.valueOf(score.getScore()));
        scoreName.setText(score.getName());
    }

    public static ScoreViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.score_list, parent, false);
        return new ScoreViewHolder(view);
    }
}