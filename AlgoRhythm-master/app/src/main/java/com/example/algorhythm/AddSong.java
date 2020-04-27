package com.example.algorhythm;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.util.ArrayList;

public class AddSong extends AppCompatActivity {

    private ListView listView;
    private String songNames[];
    private ArrayList<String> songs3;
    private ArrayList<String> songs2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stored_songs);

        listView = (ListView) findViewById(R.id.list);

        //ArrayList<String> songs = readSongs(this);

        int permissionCheck2 = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionCheck2 != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);
        }

        File dir = Environment.getExternalStorageDirectory();

        System.out.println(dir.toString()+ File.separator + Environment.DIRECTORY_DOWNLOADS);

        if(dir == null) {
            System.out.println("dir is null");
        }
        ArrayList<String> songs = readSongs(dir);

        //ArrayList<String> songs = new ArrayList<String>();
/*
        songs.add("Music");
        songs.add("Song");
        songs.add("Songs");
        songs.add("Songer");
        songs.add("Music");
        songs.add("Song");
        songs.add("Songs");
        songs.add("Songer");
        songs.add("Music");
        songs.add("Song");
        songs.add("Songs");
        songs.add("Songer");
        songs.add("Music");
        songs.add("Song");
        songs.add("Songs");
        songs.add("Songer");

 */

        songNames = new String[songs.size()];


        for(int i = 0; i < songs.size(); ++i) {
            songNames[i] = songs.get(i);
        }

        for(String s : songNames) {
            System.out.println(s);
        }

        System.out.println("Test");
        System.out.println("Test2");
        System.out.println("Test3");




        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_expandable_list_item_1, songNames);

        if(adapter == null) {
            System.out.println("bruh");
        }

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int permissionCheck2 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
                if (permissionCheck2 != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(AddSong.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.READ_EXTERNAL_STORAGE},
                            1);
                }

                String name = songNames[position];
                String path = songs3.get(position);
                //File file = new File(path);
                //Uri resource = Uri.fromFile(file);
                //MediaPlayer mp = MediaPlayer.create(getApplicationContext(), resource);
                //mp.start();
                ImportSong.songName = name;
                ImportSong.path = path;
                System.out.println(name);
                System.out.println(path);
                ImportSong.title.setText(path);
                finish();
            }
        });
    }
    /*
    private ArrayList<String> readSongs(final Context context) {
        ArrayList<String> arrayList = new ArrayList<String>();

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Audio.AudioColumns.DATA, MediaStore.Audio.AudioColumns.TITLE, MediaStore.Audio.AudioColumns.ALBUM, MediaStore.Audio.ArtistColumns.ARTIST,};
        Cursor c = context.getContentResolver().query(uri, projection, MediaStore.Audio.Media.DATA + " like ? ", new String[]{"%utm%"}, null);
        if(c == null) {
            System.out.println("C is null");
        }
        if (c != null) {
            while (c.moveToNext()) {
                System.out.println("One found");
                String path = c.getString(0);

                arrayList.add(path);
            }
            c.close();
        }

        return arrayList;
    }
    */

    /*
    private ArrayList<String> readSongs(File root) {

        ArrayList<String> arrayList = new ArrayList<String>();

        File[] files = root.listFiles();

        if(files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    arrayList.addAll(readSongs(file));
                } else {
                    if (file.getName().endsWith(".mp3")) {
                        arrayList.add(file.getName());
                    }
                }
            }
        } else {
            arrayList.add("NoFiles");
        }

        return arrayList;
    }
    */
    private ArrayList<String> readSongs(File root) {
        final Cursor mCursor = getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Audio.Media.DISPLAY_NAME, MediaStore.Audio.Media.DATA }, null, null,
                "LOWER(" + MediaStore.Audio.Media.TITLE + ") ASC");

        int count = mCursor.getCount();

        String[] songs = new String[count];
        String[] mAudioPath = new String[count];
        ArrayList<String> songs2 = new ArrayList<String>();
        songs3 = new ArrayList<String>();
        int i = 0;
        if (mCursor.moveToFirst()) {
            do {
                //songs[i] = mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
                songs3.add(mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));
                songs2.add(mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)));
                i++;
            } while (mCursor.moveToNext());
        }

        mCursor.close();

        System.out.println(songs2.size());

        return songs2;
    }

}
