package com.example.algorhythm;

import java.util.Comparator;

public class SongSorter implements Comparator<SongItem> {
    @Override
    public int compare(SongItem s1, SongItem s2) {
        return s1.getTitle().compareToIgnoreCase(s1.getTitle());
    }
}
