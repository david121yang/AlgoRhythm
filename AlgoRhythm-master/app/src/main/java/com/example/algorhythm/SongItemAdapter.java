package com.example.algorhythm;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 */
public class SongItemAdapter extends BaseExpandableListAdapter {

    private int resource;
    private Context context;
    int realPosition;
    SongItem realjb;

    private List<SongItem> items;

    public SongItemAdapter(Context ctx, int res, List<SongItem> items)
    {
        this.context = ctx;
        this.resource = res;
        this.items = items;
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return this.items.get(listPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int position, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        SongItem jb = items.get(position);
        realjb = jb;
        realPosition = position;
        //final String expandedListText = (String) getChild(position, expandedListPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.song_item_child, null);
        }
        TextView scoreView = (TextView) convertView.findViewById(R.id.highscore_text);
        TextView comboView = (TextView) convertView.findViewById(R.id.combo_text);
        TextView rankView = (TextView) convertView.findViewById(R.id.rank_text);
        Button start = (Button) convertView.findViewById(R.id.start_button);
        Button delete = (Button) convertView.findViewById(R.id.delete_button);

        start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                Snackbar.make(v, "Selected #" + position, Snackbar.LENGTH_SHORT)
//                        .setAction("Action", null).show();
                Intent intent = new Intent(context.getApplicationContext(), Game.class);
                intent.putExtra("name", realjb.getTitle());
                intent.putExtra("length", realjb.getLength());
                intent.putExtra("textFile", realjb.getTextFileName());
                intent.putExtra("position", realPosition);
                context.startActivity(intent);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SongSelect.jobItems.remove(realPosition);
                        Intent intent = new Intent(context.getApplicationContext(), SongSelect.class);
                        context.startActivity(intent);
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Do nothing
                    }
                });

                builder.setMessage("Are you sure you want to delete this entry?");

                AlertDialog d = builder.create();
                d.show();

            }
        });

        String setText = "High Score: " + jb.getHighScore();
        scoreView.setText(setText);
        setText = "Max Combo: " + jb.getMaxCombo();
        comboView.setText(setText);
        rankView.setText(jb.getRank());

        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.items.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return this.items.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }



    @Override
    public View getGroupView(int position, boolean isExpanded, View convertView, ViewGroup parent) {
        LinearLayout jobView;
        SongItem jb = items.get(position);

        if (convertView == null) {
            jobView = new LinearLayout(context);
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi = (LayoutInflater) context.getSystemService(inflater);
            vi.inflate(resource, jobView, true);
        } else {
            jobView = (LinearLayout) convertView;
        }

        TextView titleView = (TextView) jobView.findViewById(R.id.title_text);
        TextView difficultyView = (TextView) jobView.findViewById(R.id.difficulty_text);
        TextView timeView = (TextView) jobView.findViewById(R.id.time_text);


        titleView.setText(jb.getTitle());
        String setText = "";
        for(int i = 0; i < 5; i++) {
            if(i < jb.getDifficulty()) {
                setText += "★ ";
            }
            else setText += "☆ ";
        }
        difficultyView.setText(setText);
        timeView.setText(jb.getLength());

        return jobView;
    }


}
