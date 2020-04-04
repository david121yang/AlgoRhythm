package com.example.algorhythm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;


public class SongSelect extends AppCompatActivity  {

    public static final int EDIT_JOB = 0;
    public static final int NEW_JOB = 1;

    public static final int MENU_ITEM_EDITVIEW = Menu.FIRST;
    public static final int MENU_ITEM_DELETE = Menu.FIRST + 1;

    private ExpandableListView jobsList;
    private SongItemAdapter aa;

    // this is temporarily set up with package and static access so
    // that job detail can access items --
    // would be changed to private instance if supported by
    // permanent back-end database
    static ArrayList<SongItem> jobItems;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_select);
        context = getApplicationContext();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        jobsList = (ExpandableListView) findViewById(R.id.joblist);
        // create ArrayList of courses from database
        if(jobItems == null) {
            jobItems = new ArrayList<SongItem>();
            jobItems.add(new SongItem("Sea Shanty 2", 2, "1:01"));
            jobItems.add(new SongItem("through the fire and the flames 2", 4, "1:01"));
        }
        // make array adapter to bind arraylist to listview with new custom item layout
        aa = new SongItemAdapter(this, R.layout.song_item_layout, jobItems);
        jobsList.setAdapter(aa);

        registerForContextMenu(jobsList);

        aa.notifyDataSetChanged();  // to refresh items in the list

        // program a short click on the list item
        jobsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Snackbar.make(view, "Selected #" + id, Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }
        });
    }
    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_jobs, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.menu_new) {
            launchNewJob();
            return true;
        }
        // not implemented
        if (id == R.id.menu_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    */

    private void launchNewJob() {
        Intent intentA = new Intent(SongSelect.this, Game.class);
        intentA.putExtra("jobEDIT", false);
        startActivityForResult(intentA, NEW_JOB);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && (requestCode == SongSelect.EDIT_JOB
                || requestCode == SongSelect.NEW_JOB)) {
            aa.notifyDataSetChanged();
        }
        // if resultCode == RESULT_CANCELED no need to update display
        super.onActivityResult(requestCode, resultCode, data);
    }
    /*
    // for a long click on a menu item use ContextMenu
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        // create menu in code instead of in xml file (xml approach preferred)
        menu.setHeaderTitle("Select Job Item");

        // Add menu items
        menu.add(0, MENU_ITEM_EDITVIEW, 0, R.string.menu_editview);
        menu.add(0, MENU_ITEM_DELETE, 0, R.string.menu_delete);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        super.onContextItemSelected(item);

        AdapterView.AdapterContextMenuInfo menuInfo;
        menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = menuInfo.position; // position in array adapter

        switch (item.getItemId()) {
            case MENU_ITEM_EDITVIEW: {

                Toast.makeText(context, "edit request",
                        Toast.LENGTH_SHORT).show();
                Intent intentE = new Intent(JobsList.this, JobDetail.class);
                intentE.putExtra("jobEDIT", true);
                // index in array adapter tells us the job
                intentE.putExtra("jobNUM", index);
                startActivityForResult(intentE, EDIT_JOB);
                return false;
            }
            case MENU_ITEM_DELETE: {
                jobItems.remove(index);
                Toast.makeText(context, "job " + index + " deleted",
                        Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return false;
    }
    */

}