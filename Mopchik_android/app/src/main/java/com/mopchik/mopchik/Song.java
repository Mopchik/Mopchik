package com.mopchik.mopchik;

import android.media.Image;

public class Song {
    private long id;
    private String title;
    private String artist;
    private Image image;

    public Song(long songID, String songTitle, String songArtist) {
        id=songID;
        title=songTitle;
        artist=songArtist;
    }

    public long getID(){return id;}
    public String getTitle(){return title;}
    public String getArtist(){return artist;}
}
