package com.example.algorhythm;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

public class ImportSong extends AppCompatActivity {

    public static TextView title;
    public static String songName = "";
    public static String path = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_song);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button selector = (Button) findViewById(R.id.songSelect);
        title = (TextView) findViewById(R.id.textView3);
        Button cancel = (Button) findViewById(R.id.cancelButton);
        Button create = (Button) findViewById(R.id.createTrackButton);

        selector.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ImportSong.this, AddSong.class);
                startActivity(intent);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ImportSong.title.setText("");
                ImportSong.songName = "";
                ImportSong.path = "";
                finish();
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(ImportSong.path.equals("")) {
                    ImportSong.title.setText("");
                    ImportSong.songName = "";
                    ImportSong.path = "";
                    finish();
                } else {
                    SongItem song = new SongItem(ImportSong.songName, 2,"1:00","sea_shanty_2", ImportSong.path);
                    SongSelect.jobItems.add(song);
                    //SongSelect.searchedItems.add(song);
                }
            }
        });

    }

}
