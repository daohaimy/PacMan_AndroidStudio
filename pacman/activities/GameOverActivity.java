package com.example.pacman.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pacman.R;

public class GameOverActivity extends AppCompatActivity {
    private TextView score;
    private EditText enterName;
    private Button save;
    private Button back;
    private int textLength = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameover);
        score = findViewById(R.id.score_amount);
        enterName = findViewById(R.id.name);
        back = findViewById(R.id.zurueck);
        save = findViewById(R.id.save);
        enterName.setFilters(new InputFilter[]{new InputFilter.LengthFilter(textLength)});

        Intent intent = getIntent();
        int receivedValue = intent.getIntExtra("intValue", 0);
        score.setText(Integer.toString(receivedValue));


        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                backToMenu();
            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name  = enterName.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(getApplicationContext(), "enter a username", Toast.LENGTH_SHORT).show();
                } else {
                    saveScore(receivedValue, name);

                }
            }
        });




    }

    public void backToMenu(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void saveScore(int score, String name){

        Intent intent = new Intent(this, HighscoreActivity.class);
        intent.putExtra("PlayerName", name);
        intent.putExtra("PlayerScore", score);
        startActivity(intent);
    }

}
