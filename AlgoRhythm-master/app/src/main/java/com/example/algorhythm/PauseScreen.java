package com.example.algorhythm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PauseScreen extends AppCompatActivity {
    Intent launcher;
    private Button playAgainButton;
    private Button songListButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pause_screen);
        playAgainButton = (Button) findViewById(R.id.playAgainButton);
        songListButton = (Button) findViewById(R.id.songListButton);

        launcher = getIntent();

        playAgainButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Game.class);
                intent.putExtra("name", launcher.getStringExtra("songName"));
                intent.putExtra("position", launcher.getIntExtra("position", 0));
                intent.putExtra("length", launcher.getStringExtra("length"));
                intent.putExtra("textFile", launcher.getStringExtra("textFile"));
                startActivity(intent);
            }
        });
        songListButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SongSelect.class);
                startActivity(intent);
            }
        });

    }
}
