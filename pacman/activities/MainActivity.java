package com.example.pacman.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.pacman.R;

public class MainActivity extends AppCompatActivity {
    private static MediaPlayer player;
    private Button startgame;
    private Button highscore;
    private Button howtoplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startgame = findViewById(R.id.startgame);
        highscore = findViewById(R.id.highscore);
        howtoplay = findViewById(R.id.howtoplay);

        player = MediaPlayer.create(this, R.raw.pacman_theme);
        player.setVolume(100, 100);
        player.setLooping(true);
        player.start();

        startgame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                                             startePlaySeite();
                                         }
        }
        );
        highscore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                starteHighscoreSeite();
            }
        }
        );
        howtoplay.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                starteHowtoplaySeite();
            }
        }
        );
    }
    public void startePlaySeite() {
        Intent playIntent = new Intent(this, PlayActivity.class);
        startActivity(playIntent);
    }
    public void starteHighscoreSeite(){
        Intent intent = new Intent(this, HighscoreActivity.class);
        startActivity(intent);
    }

    public void starteHowtoplaySeite(){
        Intent intent = new Intent(this, HowtoplayActivity.class);
        startActivity(intent);
    }
    public static MediaPlayer getPlayer() {
        return player;
    }

    @Override
    public void onPause() {
        super.onPause();
        player.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        player.start();
    }


}