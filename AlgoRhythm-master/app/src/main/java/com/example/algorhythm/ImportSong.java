package com.example.algorhythm;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import core.be.tarsos.AndroidFFMPEGLocator;


public class ImportSong extends AppCompatActivity {

    public static TextView title;
    public static String songName = "";
    public static String path = "";
    public static String duration = "";

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
                int permissionCheck2 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
                int permissionCheck3 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
                if (permissionCheck2 != PackageManager.PERMISSION_GRANTED || permissionCheck3 != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ImportSong.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.READ_EXTERNAL_STORAGE},
                            1);
                } else {
                    Intent intent = new Intent(ImportSong.this, AddSong.class);
                    startActivity(intent);
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ImportSong.title.setText("");
                ImportSong.songName = "";
                ImportSong.path = "";
                ImportSong.duration = "";
                finish();
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new AndroidFFMPEGLocator(getApplicationContext());
                if(ImportSong.path.equals("")) {
                    ImportSong.title.setText("");
                    ImportSong.songName = "";
                    ImportSong.path = "";
                    ImportSong.duration = "";
                    finish();
                } else {
                    Algorhythm.importSong(songName);
                    Algorhythm.songString();

                    try {
                        FileOutputStream fs = new FileOutputStream(songName);
                        fs.write(Algorhythm.songString().getBytes());
                        fs.close();
                    } catch (IOException e) {
                        System.err.println("File output failed.");
                    }

                    try {
                        SongItem song = new SongItem(ImportSong.songName, 2, duration, "sea_shanty_2", ImportSong.path);
                        SongSelect.jobItems.add(song);
                        finish();
                    } catch(Exception e) {
                        SongItem song = new SongItem(ImportSong.songName, 2, duration, "sea_shanty_2", ImportSong.path);
                        TitleScreen.tempArray.add(song);
                        finish();
                    }
                }
            }
        });

    }

}
