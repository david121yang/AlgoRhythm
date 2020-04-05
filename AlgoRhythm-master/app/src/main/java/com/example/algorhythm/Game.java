package com.example.algorhythm;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import java.util.Timer;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.DragEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.algorhythm.R;


public class Game extends AppCompatActivity {

    Intent launcher;

    private TextView et_what;
    private String name;
    private int time;
    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);

        et_what = (TextView) findViewById(R.id.songName);

        launcher = getIntent();

        et_what.setText(launcher.getStringExtra("name"));

        String[] times = (launcher.getStringExtra("length")).split(":");
        time = Integer.parseInt(times[1]) * 1000;
        time += Integer.parseInt(times[0]) * 60 * 1000;


        name = launcher.getStringExtra("name");
        name = name.toLowerCase();
        name = name.replace(" ", "_");
        et_what.setText(Integer.toString(time));
        try {
            int resource = getResources().getIdentifier(name, "raw", getPackageName());
            playSong(0, time, resource);
        } catch (Exception e) {
            et_what.setText("Error");
        }



       // playSong(1, 1, "test");



        Button button = findViewById(R.id.button2);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //((Button) v).setText("" + v.getTranslationY());
                ObjectAnimator animation = ObjectAnimator.ofFloat(
                        v, "translationY", v.getTranslationY() + 250);
                animation.setDuration(2000);
                animation.start();

            }
        });

        button.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                //return true if the action was handled successfully
                /*((Button) v).setText("Dragged2.");

                if (event.getAction() == DragEvent.ACTION_DRAG_STARTED ||
                event.getAction() == DragEvent.ACTION_DRAG_ENTERED) {
                    ((Button) v).setText("Dragged.");
                }*/

                System.out.println("test");
                return true;
            }
        });



    }

    private void playSong(int delay, int time, int song) {
        final int nestedsong = song;
        final int nestedtime = time;
        mp = MediaPlayer.create(this, nestedsong);

        new Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        mp.start();

                        new Timer().schedule(
                                new java.util.TimerTask() {
                                    @Override
                                    public void run() {
                                        mp.stop();
                                    }
                                }, nestedtime);
                    }
                }, delay);
    }

    @Override
    public void onBackPressed() {
        Log.d("Settings", "onBackPressed");
        mp.stop();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            Log.d("Settings", "onOptionsItemSelected");
            mp.stop();
            finish();
        }
        return true;
    }

}
