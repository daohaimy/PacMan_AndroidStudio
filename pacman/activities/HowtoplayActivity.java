package com.example.pacman.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.pacman.R;

public class HowtoplayActivity extends AppCompatActivity {

    private Button zurueck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_howtoplayactivity);
        MainActivity.getPlayer().start();

        zurueck = findViewById(R.id.zurueck);

        zurueck.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                zurueckZumMenu();
            }
        });

    }
    public void zurueckZumMenu(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MainActivity.getPlayer().pause();
    }
    @Override
    public void onResume() {
        Log.i("info", "MainActivity onResume");
        super.onResume();
        MainActivity.getPlayer().start();
    }
}