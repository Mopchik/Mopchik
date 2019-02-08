package com.mopchik.mopchik;

import android.content.Context;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Model {
    private static Model __instance = null;

    private Model() {
    }

    public static Model instance() {
        if (__instance == null) {
            __instance = new Model();
        }
        return __instance;
    }

    //private MusicService musicSrv = null;
    private boolean musicBound = false;
    public boolean played = false;
    public String title;
    public String author;
    public boolean pause=false;
    public TextView textName;
    public TextView textAuthor;
    public RelativeLayout rel;
    public View view;
    public View added;
    public Context context;
    public ArrayList<Song> songList;
    public boolean isControllerActivity=false;




    public void setIsControllerActivity(boolean isControllerActivity){this.isControllerActivity=isControllerActivity;}
    public boolean getIsControllerActivity(){return isControllerActivity;}

    public void setSongList(ArrayList<Song> songList){this.songList=songList;}
    public ArrayList<Song> getSongList(){return songList;}

    public void setContext(Context context){this.context=context;}
    public Context getContext(){return context;}

    public void setAdded(View added){this.added=added;}
    public View getAdded(){return added;}

    public void setRel(RelativeLayout rel){this.rel=rel;}
    public RelativeLayout getRel(){return rel;}

    public void setView(View view){this.view = view;}
    public View getView(){return view;}

    public void setTextName(TextView textName){ this.textName=textName;}
    public TextView getTextName(){return textName;}

    public void setTextAuthor(TextView textAuthor){ this.textAuthor=textAuthor;}
    public TextView getTextAuthor(){return textAuthor;}

    public void setPause(boolean pause){ this.pause = pause;}
    public boolean getPause(){return pause;}

    public void setName(String title){ this.title=title;}
    public String getName(){return  title;}

    public void setAuthor(String author){ this.author=author;}
    public String getAuthor(){return  author;}

    public void setmusicBound(boolean musicBound){
        this.musicBound=musicBound;
    }
    public boolean getmusicBound(){
        return musicBound;
    }

    /*public void setMusicSrv(MusicService mydataToShare) {
        this.musicSrv= mydataToShare;
    }
    public MusicService getMusicSrv() {
        return musicSrv;
    }*/


    public void setPlayed(boolean played){this.played=played;}
    public boolean getPlayed(){return played;}

}