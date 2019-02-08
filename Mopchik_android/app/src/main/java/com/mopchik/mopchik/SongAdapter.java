package com.mopchik.mopchik;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.util.ArrayList;


public class SongAdapter extends BaseAdapter /*implements  View.OnClickListener*/ {

    private ArrayList<Song> songs;
    private LayoutInflater songInf;
    Context context;
    private ListView songView;


    public SongAdapter(Context c, ArrayList<Song> theSongs, ListView songView) {
        songs = theSongs;
        songInf = LayoutInflater.from(c);
        context = c;
        this.songView = songView;

    }

    @Override
    public int getCount() {
        return songs.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //map to song layout
        LinearLayout songLay = (LinearLayout) songInf.inflate
                (R.layout.song, parent, false);
        //get title and artist views
        TextView songView = (TextView) songLay.findViewById(R.id.song_title);
        TextView artistView = (TextView) songLay.findViewById(R.id.song_artist);

        //songLay.setOnClickListener(this);
        //get song using position
        Song currSong = songs.get(position);
        //get title and artist strings
        songView.setText(currSong.getTitle());
        artistView.setText(currSong.getArtist());

        //songLay.setOnClickListener(this);
        //set position as tag
        songLay.setTag(position);

        return songLay;
    }

    /*@Override
    public void onClick(View v) {
        //rel.setVisibility(View.VISIBLE);


        Model.instance().getMusicSrv().setSong(Integer.parseInt(v.getTag().toString()));
        //controller.setMusicSrv(Model.instance().getMusicSrv());
        Model.instance().getMusicSrv().playSong();

            controller = new Controller(Model.instance().getContext(), songView);
            controller.setMusicSrv(Model.instance().getMusicSrv());
            controller.setController();

            controller.setPlaybackPaused(false);

        controller.show();


        //if(Model.instance().getplaybackPaused()){
        //setController();
        //Model.instance().setplaybackPaused(false);
        //}
        //Model.instance().getMusicController().show(0);
    }*/


}

    //public MusicController controller;


    /*public void setController(){
        //MusicController controller = new MusicController(context);

        Model.instance().setMusicController(new MusicController(context));

        Model.instance().getMusicController().setPrevNextListeners(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playNext();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPrev();
            }
        });
        Model.instance().getMusicController().setMediaPlayer(this);
        Model.instance().getMusicController().setAnchorView(songView);
        Model.instance().getMusicController().setEnabled(true);
        //Model.instance().setMusicController(controller);
    }

    //public MusicController getController(){return controller;}
    //play next
    private void playNext(){
        Model.instance().getMusicSrv().playNext();
        if(Model.instance().getplaybackPaused()){
            setController();
            Model.instance().setplaybackPaused(false);
        }
        Model.instance().getMusicController().show(0);
    }

    //play previous
    private void playPrev(){
        Model.instance().getMusicSrv().playPrev();
        if(Model.instance().getplaybackPaused()){
            setController();
            Model.instance().setplaybackPaused(false);
        }
        Model.instance().getMusicController().show(0);
    }
*/

    /*//implement methods
    @Override
    public void start() {
        Model.instance().setplaybackPaused(false);
        Model.instance().getMusicSrv().go();
    }

    @Override
    public void pause() {
        Model.instance().setplaybackPaused(true);
        Model.instance().getMusicSrv().pausePlayer();
    }

    @Override
    public int getDuration() {
        if(Model.instance().getMusicSrv()!=null && Model.instance().getmusicBound() /*&& Model.instance().getMusicSrv().isPng())
            return Model.instance().getMusicSrv().getDur();
        else return 0;
    }

    @Override
    public int getCurrentPosition() {
        if(Model.instance().getMusicSrv()!=null && Model.instance().getmusicBound() /*&& Model.instance().getMusicSrv().isPng())
            return Model.instance().getMusicSrv().getPosn();
        else return 0;
    }

    @Override
    public void seekTo(int pos) {
        Model.instance().getMusicSrv().seek(pos);
    }

    @Override
    public boolean isPlaying() {
        if(Model.instance().getMusicSrv()!=null && Model.instance().getmusicBound())
            return Model.instance().getMusicSrv().isPng();
        return false;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return 0;*/

