package com.example.algorhythm;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.SeekBar;
import android.widget.Switch;

public class Settings extends AppCompatActivity {

    private SharedPreferences sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // load shared preference values
        sharedPrefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SeekBar volume = (SeekBar) findViewById(R.id.musicVolume);
        Switch hapticOn = (Switch) findViewById(R.id.vibrateSwitch);

        int progress = sharedPrefs.getInt("volume", 100);
        volume.setProgress(progress);

        boolean checked = sharedPrefs.getBoolean("hapticsOn", true);
        hapticOn.setChecked(checked);
    }


    public void save(View view){
        SharedPreferences.Editor editor = sharedPrefs.edit();
        SeekBar volume = (SeekBar) findViewById(R.id.musicVolume);
        int vol = volume.getProgress();
        //int max = volume.getMax();

        //float ratio = ((float) vol )/ max;

        Switch hapticOn = (Switch) findViewById(R.id.vibrateSwitch);
        boolean isChecked = hapticOn.isChecked();


        editor.putInt("volume", vol);
        editor.putBoolean("hapticsOn", isChecked);
        editor.commit();

        Intent intent = new Intent(Settings.this, TitleScreen.class);
        startActivity(intent);
    }

    public void quit(View view){
        Intent intent = new Intent(Settings.this, TitleScreen.class);
        startActivity(intent);
    }
    // button save operation for sharedprefs
    // cancel intent to main activity, discard changes
}
