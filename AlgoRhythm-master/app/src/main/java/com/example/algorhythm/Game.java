package com.example.algorhythm;

import android.Manifest;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Timer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import android.os.CountDownTimer;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


public class Game extends AppCompatActivity {

    Intent launcher;

    private TextView et_what;
    private String name;
    private int time;
    private MediaPlayer mp;
    private ArrayDeque<Note> notesOffScreen;
    private ArrayDeque<Note> notesOnScreen;
    private ProgressBar rhythmMeter;
    private ProgressBar songProgress;
    private float vol;
    static int newNote;
    private boolean hapticOn;
    private SharedPreferences sharedPrefs;
    private int songListPosition;
    private int oldbo = 0;
    private int newbo = 0;
    private int maxbo = 0;
    private int score = 0;
    private float target = -1;
    private TextView comboBoard;
    private TextView scoreBoard;
    private Timer noteTimer;
    private Timer songTimer;
    private ImageButton pauseButton;
    private String path;
    private int notesHit;
    private int noteCount;

    class Note {
        public ObjectAnimator animation;
        public final int ID;
        public final char type;
        public final ImageView image;
        public final int timestamp;

        Note(char type, int time) {
            this(++newNote, type, time);
        }

        public Note(int ID, char type, int time) {
            this.ID = ID;
            this.type = type;
            image = new ImageView(getApplicationContext());
            image.setId(ID);
            switch(type){

                case 'l':
                    image.setImageDrawable(getResources().getDrawable(R.drawable.noteleft));
                    break;
                case 'r':
                    image.setImageDrawable(getResources().getDrawable(R.drawable.noteright));
                    break;
                case 't':
                    image.setImageDrawable(getResources().getDrawable(R.drawable.notetap));
                    break;
                default:
                    image.setImageDrawable(getResources().getDrawable(R.drawable.notetap));
                    break;
            }
            timestamp = time;
        }

        public float getY() {
            return image.getTranslationY();
        }

        public void draw() {
            ConstraintLayout parentLayout = (ConstraintLayout)findViewById(R.id.ConstraintLayout);

            // set view id, else getId() returns -1

            ConstraintSet set = new ConstraintSet();

            parentLayout.addView(image, 0);
            set.clone(parentLayout);
            // connect start and end point of views, in this case top of child to top of parent.
            set.connect(image.getId(), ConstraintSet.TOP, parentLayout.getId(), ConstraintSet.TOP, 60);
            set.connect(image.getId(), ConstraintSet.LEFT, parentLayout.getId(), ConstraintSet.LEFT, 60);
            set.connect(image.getId(), ConstraintSet.RIGHT, parentLayout.getId(), ConstraintSet.RIGHT, 60);


            // ... similarly add other constraints
            set.applyTo(parentLayout);
            image.bringToFront();
            image.invalidate();


            DisplayMetrics displayMetrics = new DisplayMetrics();
            WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE); // the results will be higher than using the activity context object or the getWindowManager() shortcut
            wm.getDefaultDisplay().getMetrics(displayMetrics);
            int screenHeight = displayMetrics.heightPixels;

            animation = ObjectAnimator.ofFloat(image, "translationY", image.getTranslationY() + screenHeight);
            animation.setDuration(2000);
            animation.setInterpolator(new LinearInterpolator());
            animation.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    //make sure it's actually fallen all the way down the screen
                    //so that it doesn't just remove the next note

                    if (!notesOnScreen.isEmpty() && notesOnScreen.peek().ID == ID) {
                        removeNextNote();
                    }

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

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);

        int permissionCheck2 = ContextCompat.checkSelfPermission(Game.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionCheck2 != PackageManager.PERMISSION_GRANTED) {
            System.out.println("Nope");
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE},
                    2);
        }
        sharedPrefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        int maxVolume = 101;
        int volume = sharedPrefs.getInt("volume", 100);
        hapticOn = sharedPrefs.getBoolean("hapticsOn", true);
        findViewById(R.id.goZone).setHapticFeedbackEnabled(hapticOn);
        vol = (float) (1 - (Math.log(maxVolume - volume) / Math.log(maxVolume)));


        notesHit = 0;

        scoreBoard = (TextView) findViewById(R.id.score);
        comboBoard = (TextView) findViewById(R.id.combo);

        et_what = (TextView) findViewById(R.id.songName);
        notesOffScreen = new ArrayDeque<>();
        notesOnScreen = new ArrayDeque<>();

        launcher = getIntent();
        songListPosition = launcher.getIntExtra("position", 0);
        //et_what.setText(launcher.getStringExtra("name"));

        path = launcher.getStringExtra("path");

        String[] times = (launcher.getStringExtra("length")).split(":");
        time = Integer.parseInt(times[1]) * 1000;
        time += Integer.parseInt(times[0]) * 60 * 1000;

        try {
            final InputStream file = getAssets().open(launcher.getStringExtra("textFile"));
            BufferedReader reader = new BufferedReader(new InputStreamReader(file));
            noteCount = Integer.parseInt(reader.readLine());
            int currentTime = 0;
            for(int i = 0; i < noteCount; i++) {
                String[] fields = reader.readLine().split(" ");
                float timestamp = Float.parseFloat(fields[0]);
                char noteType = fields[1].charAt(0);
                if(noteType == 'h') {
                    //do more stuff
                }
                notesOffScreen.add(new Note(noteType, (int) (timestamp * 1000 - 1500)));
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
            if(path == null) {
                int resource = getResources().getIdentifier(name, "raw", getPackageName());
                playSong(0, time, resource /*, noteMap*/);
            } else {
                System.out.println(path);
                Uri resource = Uri.parse(path);
                playSong(0, time, resource);
                //mp = MediaPlayer.create(this, resource);
                //mp.start();
                //playSong(0, time, resource);
            }
        } catch (Exception e) {
            //et_what.setText("Error");
        }

        ImageView goZone = findViewById(R.id.goZone);
        rhythmMeter = (ProgressBar) findViewById(R.id.rhythmmeter);
        rhythmMeter.setMax(100);
        rhythmMeter.setProgress(50);

        songProgress = (ProgressBar) findViewById(R.id.songProgress);
        songProgress.setMax(time / 1000);
        songProgress.setProgress(0);
        //songProgress.setRotationX(180);
        CountDownTimer songProgresstimer = new CountDownTimer(time, 1000) {

            public void onTick(long millisUntilFinished) {
                songProgress.setProgress(songProgress.getProgress() + 1);
            }

            public void onFinish() {

            }
        }.start();


        //ImageView goZone = (ImageView) findViewById(R.id.goZone);
        //target = goZone.getTranslationY();
        //target = 400;


        goZone.setOnTouchListener(new View.OnTouchListener() {
            final float SWIPE_THRESHHOLD = 250;
            private float x1;
            private float x2;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x1 = event.getX();
                        System.out.println("x1 = " + x1);
                        return true;
                    case MotionEvent.ACTION_UP:
                        x2 = event.getX();

                        Note nextNote;
                        if (!notesOnScreen.isEmpty()) {
                            nextNote = notesOnScreen.peek();
                        } else {
                            //no notes to deal with; break combo (false input)
                            newbo = 0;
                            return true;
                        }

                        try{
                            Character newt = nextNote.type;
                            if (Math.abs(x2 - x1) >= SWIPE_THRESHHOLD) {
                                if (x1 > x2) {
                                    System.out.println("LEFT");

                                    float y = nextNote.getY();

                                    System.out.println(y);
                                    System.out.println(target);
                                    if (newt == 'l' &&  y + 100 > target && y < target + 300) {
                                        newbo++;
                                        notesHit++;
                                    } else {
                                        newbo = 0;
                                    }



                                } else {
                                    System.out.println("RIGHT");

                                    if (target == -1) {
                                        ImageView goZone = (ImageView) findViewById(R.id.goZone);
                                        target = goZone.getTop();
                                    }
                                    float y = nextNote.getY();
                                    if (newt == 'r' &&  y + 100 > target && y < target + 300) {
                                        newbo++;
                                        notesHit++;
                                    } else {
                                        newbo = 0;
                                    }



                                }
                            } else {
                                System.out.println("TAP");

                                if (target == -1) {
                                    ImageView goZone = (ImageView) findViewById(R.id.goZone);
                                    target = goZone.getTop();
                                }
                                float y = nextNote.getY();
                                if (newt == 't' &&  y + 100 > target && y < target + 300) {
                                    newbo++;
                                    notesHit++;
                                } else {
                                    newbo = 0;
                                }



                            }
                        } catch(Exception e) {
                            //
                        }

                        removeNextNote();

                        break;
                }
            if (hapticOn)
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            return true;
            }
        });

        pauseButton = (ImageButton)findViewById(R.id.pauseButton);
        pauseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                noteTimer.cancel();
                mp.stop();
                songTimer.cancel();
                Intent intent = new Intent(getApplicationContext(), PauseScreen.class);
                intent.putExtra("songName", SongSelect.jobItems.get(songListPosition).getTitle());
                intent.putExtra("position", launcher.getIntExtra("position", 0));
                intent.putExtra("length", launcher.getStringExtra("length"));
                intent.putExtra("textFile", launcher.getStringExtra("textFile"));
                startActivity(intent);
            }
        });

    }




    private void playSong(int delay, int time, int song) {

        //delay functionality should probably be moved to setNoteTimers

        final int nestedsong = song;
        final int nestedtime = time;
        //TreeMap<Integer, Character> nutes = new TreeMap<Integer, Character>();
        /*for(Map.Entry<Integer, Character> entry : notes.entrySet()) {
            nutes.put(entry.getKey() + delay, entry.getValue());
        }*/
        mp = MediaPlayer.create(this, nestedsong);
        mp.setVolume(vol, vol);
        songTimer = new Timer();
        songTimer.schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        mp.start();

                        new Timer().schedule(
                                new java.util.TimerTask() {
                                    @Override
                                    public void run() {
                                        mp.stop();
                                        songEnd(true);
                                    }
                                }, nestedtime);
                    }
                }, delay);

        setNoteTimers();
    }

    private void playSong(int delay, int time, Uri song) {

        //delay functionality should probably be moved to setNoteTimers

        final Uri nestedsong = song;
        final int nestedtime = time;
        //TreeMap<Integer, Character> nutes = new TreeMap<Integer, Character>();
        /*for(Map.Entry<Integer, Character> entry : notes.entrySet()) {
            nutes.put(entry.getKey() + delay, entry.getValue());
        }*/
        mp = MediaPlayer.create(this, nestedsong);

        songTimer = new Timer();
        songTimer.schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        mp.start();

                        new Timer().schedule(
                                new java.util.TimerTask() {
                                    @Override
                                    public void run() {
                                        mp.stop();
                                        songEnd(true);
                                    }
                                }, nestedtime);
                    }
                }, delay);

        setNoteTimers();
    }

    private void setNoteTimers() {

        final Runnable noteMove = new Runnable() {
            public void run() {
                if (!notesOffScreen.isEmpty()) {
                    Note nextNote = notesOffScreen.poll();
                    nextNote.draw();
                    notesOnScreen.addLast(nextNote);
                }
            }
        };
        noteTimer = new Timer();


        for(Note n : notesOffScreen) {
            noteTimer.schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            runOnUiThread(noteMove);
                        }
                    }, n.timestamp);
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


    public void removeNextNote(){

        try {
            Note nextNote;
            if (!notesOnScreen.isEmpty()) {
                nextNote = notesOnScreen.poll();
            } else {
                return;
            }
            ImageView note = nextNote.image;
            //nextNote.animation.cancel();
            note.setVisibility(View.GONE);
            ConstraintLayout parentLayout = (ConstraintLayout) findViewById(R.id.ConstraintLayout);
            parentLayout.removeView(note);
            int newProgress = rhythmMeter.getProgress();
            if(oldbo != newbo && newbo > 0) {
                newProgress += 5;
                if (newProgress > 100){
                    newProgress = 100;
                }
                rhythmMeter.setProgress(newProgress);
                oldbo = newbo;
                if(oldbo > maxbo) {
                    maxbo = oldbo;
                }
                score += newbo;
            } else {
                if(newbo > 0) {
                    score++;
                }
                newProgress -= 10;
                if (newProgress < 0){
                    newProgress = 0;
                }
                rhythmMeter.setProgress(newProgress);
                // check lose right here
                if(rhythmMeter.getProgress() == 0) {
                    mp.stop();
                    songEnd(false);
                }
                oldbo = 0;
                newbo = 0;
            }
            scoreBoard.setText(Integer.toString(score));
            comboBoard.setText(Integer.toString(newbo));
           // et_what.setText(Integer.toString(newbo) + " - " + Integer.toString(maxbo) + " - " + Integer.toString(score));
        } catch (Exception e) {
            //whatever
        }

    }

    public void songEnd(boolean complete){
        noteTimer.cancel();
        songTimer.cancel();
        String rank = "A";
        if(!complete) rank = "F";
        SongItem s = SongSelect.jobItems.get(songListPosition);
//        if(currentHighScore > s.getHighScore() || currentCombo > s.getMaxCombo() || currentRank.compareTo(s.getRank()) < 0)
        SongSelect.jobItems.get(songListPosition).updateScore(Math.max(s.getHighScore(), score), Math.max(maxbo, s.getMaxCombo()), rank);
        Intent intent = new Intent(getApplicationContext(), ResultsScreen.class);
        intent.putExtra("complete", complete);
        intent.putExtra("songName", SongSelect.jobItems.get(songListPosition).getTitle());
        intent.putExtra("score", score);
        intent.putExtra("maxCombo", maxbo);
        intent.putExtra("rank", rank);
        intent.putExtra("notesHit", notesHit);
        intent.putExtra("noteCount", noteCount);
        intent.putExtra("position", launcher.getIntExtra("position", 0));
        intent.putExtra("length", launcher.getStringExtra("length"));
        intent.putExtra("textFile", launcher.getStringExtra("textFile"));
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
