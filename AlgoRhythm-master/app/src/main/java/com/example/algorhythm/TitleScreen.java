package com.example.algorhythm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class TitleScreen extends AppCompatActivity {

    public static ArrayList<SongItem> tempArray = new ArrayList<SongItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title_screen);

        //set up the play button
        /*playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go to a different Activity

            }
        });*/

    }

    public void goToSongSelect(View view) {
        Intent intent = new Intent(TitleScreen.this, SongSelect.class);
        startActivity(intent);
    }

    public void goToImportSong(View view) {
        //Intent intent = new Intent(TitleScreen.this, ImportSong.class);
        //Intent intent = new Intent(TitleScreen.this, AddSong.class);
        //startActivity(intent);
        Toast.makeText(getApplicationContext(), "Feature Not Yet Implemented. Coming Soon.", Toast.LENGTH_SHORT).show();
    }

    public void goToSettings(View view) {
        Intent intent = new Intent(TitleScreen.this, Settings.class);
        startActivity(intent);
    }

}
