package com.example.algorhythm;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Map;
import java.util.Queue;
import java.util.Timer;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.interpolator.view.animation.FastOutLinearInInterpolator;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.DragEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.algorhythm.R;


public class Game extends AppCompatActivity {

    Intent launcher;

    private TextView et_what;
    private String name;
    private int time;
    private MediaPlayer mp;
    private TreeMap<Integer, Character> notes;
    private int currentNote;
    private ArrayDeque<Integer> nutes;
    private ProgressBar rhythmMeter;
    static int newNote;
    static char newNoteType;
    private int songListPosition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);

        et_what = (TextView) findViewById(R.id.songName);
        nutes = new ArrayDeque<Integer>();

        launcher = getIntent();
        songListPosition = launcher.getIntExtra("position", 0);
        //et_what.setText(launcher.getStringExtra("name"));

        String[] times = (launcher.getStringExtra("length")).split(":");
        time = Integer.parseInt(times[1]) * 1000;
        time += Integer.parseInt(times[0]) * 60 * 1000;

        notes = new TreeMap<Integer, Character>();
        try {
            final InputStream file = getAssets().open(launcher.getStringExtra("textFile"));
            BufferedReader reader = new BufferedReader(new InputStreamReader(file));
            int noteCount = Integer.parseInt(reader.readLine());
            int currentTime = 0;
            for(int i = 0; i < noteCount; i++) {
                String[] fields = reader.readLine().split(" ");
                float timestamp = Float.parseFloat(fields[0]);
                char noteType = fields[1].charAt(0);
                if(noteType == 'h') {
                    //do more stuff
                }
                notes.put((int)(timestamp * 1000), noteType);
            }
        } catch(Exception e) {
            //shouldn't get here

        }

        name = launcher.getStringExtra("name");
        TextView sung = (TextView) findViewById(R.id.songName);
        sung.setText(name);
        name = name.toLowerCase();
        name = name.replace(" ", "_");
        //et_what.setText(Integer.toString(time));
        try {
            int resource = getResources().getIdentifier(name, "raw", getPackageName());

            playSong(0, time, resource, notes);
        } catch (Exception e) {
            //et_what.setText("Error");
        }




       // playSong(1, 1, "test");






        /*button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //((Button) v).setText("" + v.getTranslationY());
                ObjectAnimator animation = ObjectAnimator.ofFloat(
                        v, "translationY", v.getTranslationY() + 250);
                animation.setDuration(2000);
                animation.start();

                createNote('t', 2);

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
                }

                System.out.println("test");
                return true;
            }
        });*/


    //createNote('t', 100);

        ImageView goZone = findViewById(R.id.goZone);
        goZone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!nutes.isEmpty()) {
                    removeNote(nutes.peek());
                }
            }
        });
        rhythmMeter = (ProgressBar) findViewById(R.id.rhythmmeter);
        rhythmMeter.setMax(100);
        rhythmMeter.setProgress(50);

    }


    private void playSong(int delay, int time, int song, TreeMap<Integer, Character> notes) {
        final int nestedsong = song;
        final int nestedtime = time;
        TreeMap<Integer, Character> nutes = new TreeMap<Integer, Character>();
        for(Map.Entry<Integer, Character> entry : notes.entrySet()) {
            nutes.put(entry.getKey() + delay, entry.getValue());
        }
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

        NoteTimer(nutes);
    }

    private void NoteTimer (TreeMap<Integer, Character> notes) {

        //Timer timer = new Timer();
        newNote = 0;
        final Runnable noteMove = new Runnable() {
            public void run() {
                newNote++;
                createNote(newNoteType, newNote);
            }
        };
        Timer timer = new Timer();
        for(Map.Entry<Integer, Character> entry : notes.entrySet()) {
            final Map.Entry<Integer, Character> entree = entry;
            timer.schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            newNoteType = entree.getValue();
                            runOnUiThread(noteMove);
                        }
                    }, entry.getKey());
        }
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

    public void createNote(char type, final int noteNumber){
        ImageView iv = new ImageView(getApplicationContext());

        ConstraintLayout parentLayout = (ConstraintLayout)findViewById(R.id.ConstraintLayout);


        //ImageView childView = new ImageView(this);
        // set view id, else getId() returns -1


        switch(type){
            case 'l':
                iv.setImageDrawable(getResources().getDrawable(R.drawable.noteleft));
                break;
            case 'r':
                iv.setImageDrawable(getResources().getDrawable(R.drawable.noteright));
                break;
            case 't':
                iv.setImageDrawable(getResources().getDrawable(R.drawable.notetap));
                break;
            default:
                iv.setImageDrawable(getResources().getDrawable(R.drawable.notetap));
                break;
        }

        ConstraintSet set = new ConstraintSet();

        //iv.setId(View.generateViewId());// set to current note number?
        iv.setId(noteNumber);
        //ConstraintLayout current = (ConstraintLayout)findViewById(R.id.constraintLayout);
        parentLayout.addView(iv, 0);
        set.clone(parentLayout);
        // connect start and end point of views, in this case top of child to top of parent.
        set.connect(iv.getId(), ConstraintSet.TOP, parentLayout.getId(), ConstraintSet.TOP, 60);
        set.connect(iv.getId(), ConstraintSet.LEFT, parentLayout.getId(), ConstraintSet.LEFT, 60);
        set.connect(iv.getId(), ConstraintSet.RIGHT, parentLayout.getId(), ConstraintSet.RIGHT, 60);


        // ... similarly add other constraints
        set.applyTo(parentLayout);
        iv.bringToFront();
        iv.invalidate();
        //RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
        //        RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        //final TextView start = (TextView) findViewById(R.id.StartPoint);
        //lp.addRule(RelativeLayout.BELOW, start.getId());
        //iv.setLayoutParams(lp);
        //final RelativeLayout r1 = (RelativeLayout) findViewById(R.id.ConstraintLayout);
        //r1.addView(iv);
        ObjectAnimator animation = ObjectAnimator.ofFloat(iv, "translationY", iv.getTranslationY() + 1400);
        animation.setDuration(2000);
        animation.setInterpolator(new LinearInterpolator());
        animation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                nutes.add(noteNumber);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                nutes.poll();
                removeNote(noteNumber);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }


        });
        animation.start();

    }

    public void removeNote(int noteNumber){
        try {
            ImageView note = (ImageView) findViewById(noteNumber);
            note.setVisibility(View.GONE);
            ConstraintLayout parentLayout = (ConstraintLayout) findViewById(R.id.ConstraintLayout);
            parentLayout.removeView(note);
            //removeView(note);
        } catch (Exception e) {
            //whatever
        }

    }

    public void songEnd(){
        Intent intent = new Intent(getApplicationContext(), SongSelect.class);
        SongItem s = SongSelect.jobItems.get(songListPosition);
//        if(currentHighScore > s.getHighScore() || currentCombo > s.getMaxCombo() || currentRank.compareTo(s.getRank()) < 0)
        SongSelect.jobItems.get(songListPosition).updateScore(1, 1, "F");
        startActivity(intent);
    }
}
