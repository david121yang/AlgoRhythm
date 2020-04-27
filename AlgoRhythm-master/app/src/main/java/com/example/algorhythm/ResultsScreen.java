package com.example.algorhythm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ResultsScreen extends AppCompatActivity {

    Intent launcher;

    private TextView completeOrFailed;
    private TextView songName;
    private TextView score;
    private TextView maxCombo;
    private TextView notesHit;
    private TextView rank;
    private Button playAgainButton;
    private Button songListButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.victorydefeatscreen);

        launcher = getIntent();

        completeOrFailed = (TextView)findViewById(R.id.tv_completeOrFailed);
        songName = (TextView)findViewById(R.id.tv_songName);
        score = (TextView)findViewById(R.id.tv_score);
        maxCombo = (TextView)findViewById(R.id.tv_maxCombo);
        notesHit = (TextView)findViewById(R.id.tv_notesHit);
        rank = (TextView)findViewById(R.id.tv_rank);
        playAgainButton = (Button) findViewById(R.id.playAgainButton);
        songListButton = (Button) findViewById(R.id.songListButton);


        if(launcher.getBooleanExtra("complete", false)) {
            completeOrFailed.setText("Song Complete");
        } else completeOrFailed.setText("Song Failed");
        songName.setText(launcher.getStringExtra("songName"));
        score.setText(launcher.getIntExtra("score", 0)+"");
        maxCombo.setText(launcher.getIntExtra("maxCombo", 0)+"");
        notesHit.setText(launcher.getIntExtra("notesHit", 0)+"/"
                +launcher.getIntExtra("noteCount",0));
        rank.setText(launcher.getStringExtra("rank"));
        Log.d("as","shole");

        playAgainButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Game.class);
                intent.putExtra("name", launcher.getStringExtra("songName"));
                intent.putExtra("position", launcher.getIntExtra("position", 0));
                intent.putExtra("length", launcher.getStringExtra("length"));
                intent.putExtra("textFile", launcher.getStringExtra("textFile"));
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
        songListButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SongSelect.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                //finish();
            }
        });

    }
}
