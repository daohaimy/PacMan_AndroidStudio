package com.example.pacman.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.example.pacman.logic.SpielLogik;

public class PlayActivity extends Activity {
    static PlayActivity activity;
    private SpielLogik spielLogik;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        spielLogik = new SpielLogik(this);
        setContentView(spielLogik);
        activity = this;
    }

    @Override
    protected void onPause() {
        Log.i("info", "onPause");
        super.onPause();
        spielLogik.pause();
    }

    @Override
    protected void onResume() {
        Log.i("info", "onResume");
        super.onResume();
        spielLogik.resume();
    }

    public static PlayActivity getInstance() {
        return activity;
    }

}