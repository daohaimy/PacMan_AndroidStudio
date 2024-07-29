package com.example.pacman.activities;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.pacman.database.HighscoreAdapter;
import com.example.pacman.database.HighscoreViewModel;
import com.example.pacman.R;
import com.example.pacman.database.Scores;


public class HighscoreActivity extends AppCompatActivity {

    private Button back;
    private MediaPlayer playerHighscore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscoreactivity);
        playerHighscore = MediaPlayer.create(this, R.raw.highscore_theme);
        playerHighscore.setVolume(100, 100);
        playerHighscore.setLooping(true);
        playerHighscore.start();
        final HighscoreAdapter adapter = CreateAdapter();
        CreateViewModel(adapter);
        views();
    }

    private void CreateViewModel(HighscoreAdapter adapter) {
        HighscoreViewModel model = new ViewModelProvider(this).get(HighscoreViewModel.class);
        model.allScores().observe(this, adapter::submitList);

        Intent intent = getIntent();
        if(intent.getIntExtra("PlayerScore", 0) != 0){
            int receivedScore = intent.getIntExtra("PlayerScore", 0);
            String receivedName;
            if (intent.getStringExtra("PlayerName").matches("")){
                receivedName = "Player";
            } else {
                receivedName = intent.getStringExtra("PlayerName");
            }
            Scores receivedValues = new Scores(receivedScore, receivedName);
            model.insert(receivedValues);
        }
    }

    @NonNull
    private HighscoreAdapter CreateAdapter() {
        RecyclerView recyclerView = findViewById(R.id.highscore_liste);
        final HighscoreAdapter adapter = new HighscoreAdapter(new HighscoreAdapter.ScoreDiff());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        return adapter;
    }


    private void views() {
        back = findViewById(R.id.zurueck);
        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                backToMenu();
            }
        });
    }

    public void backToMenu(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onPause() {
        super.onPause();
        playerHighscore.pause();
    }

    @Override
    public void onResume() {
        Log.i("info", "MainActivity onResume");
        super.onResume();
        playerHighscore.start();
    }
}


