package com.example.algorhythm;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.icu.text.CaseMap;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.widget.SearchView;

import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
//import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.lang.reflect.Array;
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
    static ArrayList<SongItem> searchedItems;

    private Context context;

    private EditText input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_select);
        context = getApplicationContext();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);

        jobsList = (ExpandableListView) findViewById(R.id.joblist);
        // create ArrayList of courses from database
        if(jobItems == null) {
            jobItems = new ArrayList<SongItem>();
            jobItems.add(new SongItem("Zea Shanty 2", 2, "1:00", "test.txt", null));
            jobItems.add(new SongItem("Sea Shanty 2", 2, "1:00", "test.txt", null));
            jobItems.add(new SongItem("through the fire and the flames 2", 4, "0:31","song_output.txt", null));
            jobItems.add(new SongItem("Eea Shanty 2", 2, "1:00", "sea_shanty_2", null));
            for(SongItem item : TitleScreen.tempArray) {
                jobItems.add(item);
            }
        }
        // make array adapter to bind arraylist to listview with new custom item layout

        orderSongs();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.search_icon);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                aa.getFilter().filter(newText);
                return false;
            }
        });

        return true;
    }
    /*
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

    private void orderSongs() {
        //jobItems.sort(new SongSorter());
        ArrayList<SongItem> one = new ArrayList<SongItem>();
        ArrayList<SongItem> two = new ArrayList<SongItem>();
        ArrayList<SongItem> three = new ArrayList<SongItem>();
        ArrayList<SongItem> four = new ArrayList<SongItem>();
        ArrayList<SongItem> five = new ArrayList<SongItem>();
        for(SongItem song : jobItems) {
            if(song.getDifficulty() == 1) {
                one.add(song);
            } else if(song.getDifficulty() == 2) {
                two.add(song);
            } else if(song.getDifficulty() == 3) {
                three.add(song);
            } else if(song.getDifficulty() == 4) {
                four.add(song);
            } else {
                five.add(song);
            }
        }

        sort(one, 0, one.size() - 1);
        sort(two, 0, two.size() - 1);
        sort(three, 0, three.size() - 1);
        sort(four, 0, four.size() - 1);
        sort(five, 0, five.size() - 1);

        jobItems.clear();
        jobItems.addAll(one);
        jobItems.addAll(two);
        jobItems.addAll(three);
        jobItems.addAll(four);
        jobItems.addAll(five);

        //sort(jobItems, 0, jobItems.size() - 1);
    }

    private void sort(ArrayList<SongItem> a, int low, int high) {
        if (low < high) {
            int pi = partition(a, low, high);

            sort(a, low, pi - 1);
            sort(a, pi + 1, high);
        }
    }

    private int partition(ArrayList<SongItem> a, int low, int high) {
        //SongSorter sorter = new SongSorter();
        //s1.getTitle().compareToIgnoreCase(s1.getTitle());
        SongItem pivot = a.get(high);
        int i = low - 1;
        for(int j = low; j < high; j++) {
            if(a.get(j).getTitle().compareToIgnoreCase(pivot.getTitle()) < 0) {
                i++;

                SongItem temp = a.get(i);
                a.set(i, a.get(j));
                a.set(j, temp);
            }
        }
        SongItem temp = a.get(i + 1);
        a.set(i + 1, a.get(high));
        a.set(high, temp);

        return i + 1;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
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