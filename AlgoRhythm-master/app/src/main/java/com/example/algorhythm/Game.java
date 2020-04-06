package com.example.algorhythm;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.interpolator.view.animation.FastOutLinearInInterpolator;

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

                createNote("tap", 2);

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

    public void createNote(String type, final int noteNumber){
        ImageView iv = new ImageView(getApplicationContext());

        ConstraintLayout parentLayout = (ConstraintLayout)findViewById(R.id.ConstraintLayout);


        //ImageView childView = new ImageView(this);
        // set view id, else getId() returns -1


        switch(type){
            case "left":
                iv.setImageDrawable(getResources().getDrawable(R.drawable.noteleft));
                break;
            case "right":
                iv.setImageDrawable(getResources().getDrawable(R.drawable.noteright));
                break;
            case "tap":
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

            }

            @Override
            public void onAnimationEnd(Animator animator) {
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
        ImageView note = (ImageView)findViewById(noteNumber);
        note.setVisibility(View.GONE);
        ConstraintLayout parentLayout = (ConstraintLayout)findViewById(R.id.ConstraintLayout);
        parentLayout.removeView(note);
        //removeView(note);
    }
}
