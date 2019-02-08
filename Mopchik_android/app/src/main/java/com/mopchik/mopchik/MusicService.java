package com.mopchik.mopchik;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.ContentUris;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.app.Service;
import android.os.PowerManager;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Random;

import java.util.ArrayList;
import java.util.ResourceBundle;

public class MusicService extends Service implements
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener {

    //media player
    private MediaPlayer player;
    //song list
    private ArrayList<Song> songs;
    //current position
    private int songPosn;
    private final IBinder musicBind = new MusicBinder();

    private String songTitle="";
    private static final int NOTIFY_ID=1;

    SeekBar seekbar;

    @Override
    public IBinder onBind(Intent intent) {
        /*View view = LayoutInflater.from(MainActivity.context).inflate(R.layout.activity_main,null,false);
        RelativeLayout rel = (RelativeLayout)view.findViewById(R.id.rel);
        rel.setVisibility(View.VISIBLE);*/
        //RelativeLayout rel = (RelativeLayout)view.findViewById(R.id.rel);


        return musicBind;
    }

    @Override
    public boolean onUnbind(Intent intent){
        player.stop();
        player.release();
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if(player.getCurrentPosition()>0){
            mp.reset();
            playNext();

        }

    }


    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mp.reset();
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        //start playback
        mp.start();
        //MusicFragment.showController();
        /*Intent notIntent = new Intent(this, MainActivity.class);
        notIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendInt = PendingIntent.getActivity(this, 0,
                notIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(this);

        builder.setContentIntent(pendInt)
                .setSmallIcon(R.drawable.play)
                .setTicker(songTitle)
                .setOngoing(true)
                .setContentTitle("Playing").setContentText(songTitle);
        Notification not = builder.build();

        startForeground(NOTIFY_ID, not);*/
    }

    @Override
    public void onCreate(){
        //create the service
        super.onCreate();
        //initialize position
        songPosn=0;
        //create player
        player = new MediaPlayer();
        initMusicPlayer();
    }

    public void initMusicPlayer(){
        //set player properties
        player.setWakeMode(getApplicationContext(),
                PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
    }

    public void setList(ArrayList<Song> theSongs){
        songs=theSongs;
    }



    public class MusicBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }

    public void setSong(int songIndex){
        songPosn=songIndex;
    }

    public void playSong(){
        Log.e("onClick","playSong()");
        //play a song
        player.reset();
        //get song
        Song playSong = songs.get(songPosn);

        songTitle=playSong.getTitle();
    //get id
        long currSong = playSong.getID();
    //set uri
        Uri trackUri = ContentUris.withAppendedId(
                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                currSong);
        try{
            player.setDataSource(getApplicationContext(), trackUri);
        }
        catch(Exception e){
            Log.e("MUSIC SERVICE", "Error setting data source", e);
        }

        player.prepareAsync();
    }


    public int getPosn(){
        return player.getCurrentPosition();
    }

    public int getDur(){
        return player.getDuration();
    }

    public boolean isPng(){
        return player.isPlaying();
    }


    public void pausePlayer(){
        player.pause();
    }

    public void seek(int posn){
        player.seekTo(posn);
    }

    public void go(){
        player.start();
    }

    public void playPrev(){
        songPosn--;
        if(songPosn<0) songPosn=songs.size()-1;
        if(songs!=null) {
            Model.instance().setName(songs.get(songPosn).getTitle());
            Model.instance().setAuthor(songs.get(songPosn).getArtist());

            if(!Model.instance().getIsControllerActivity()) {
                View view = Model.instance().getView();

                TextView name = (TextView) view.findViewById(R.id.name);
                TextView author = (TextView) view.findViewById(R.id.author);

                name.setText(songs.get(songPosn).getTitle());

                author.setText(songs.get(songPosn).getArtist());

                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) view.getLayoutParams();
                lp.bottomMargin = 0;

                Model.instance().getRel().removeView(Model.instance().getAdded());
                Model.instance().getRel().addView(view);
            }
            else{
                View view = Model.instance().getView();

                TextView Controller_Name=(TextView)view.findViewById(R.id.Controller_Name);
                TextView Controller_Author=(TextView)view.findViewById(R.id.Controller_Author);
                Controller_Name.setText(songs.get(songPosn).getTitle());
                Controller_Author.setText(songs.get(songPosn).getArtist());

                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) view.getLayoutParams();
                lp.bottomMargin = 0;

                Model.instance().getRel().removeView(Model.instance().getAdded());
                Model.instance().getRel().addView(view);
            }

            playSong();
        }
    }

    //skip to next
    public void playNext(){
        Log.e("onClick","playNext()");
        songPosn++;
        if(songPosn>=songs.size()) songPosn=0;
        if(songs!=null) {
            Model.instance().setName(songs.get(songPosn).getTitle());
            Model.instance().setAuthor(songs.get(songPosn).getArtist());

            if(!Model.instance().getIsControllerActivity()) {
                View view = Model.instance().getView();

                TextView name = (TextView) view.findViewById(R.id.name);
                TextView author = (TextView) view.findViewById(R.id.author);
                name.setText(songs.get(songPosn).getTitle());
                author.setText(songs.get(songPosn).getArtist());

                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) view.getLayoutParams();
                lp.bottomMargin = 0;

                Model.instance().getRel().removeView(Model.instance().getAdded());

                Model.instance().getRel().addView(view);
            }
            else{
                View view = Model.instance().getView();

                TextView Controller_Name=(TextView)view.findViewById(R.id.Controller_Name);
                TextView Controller_Author=(TextView)view.findViewById(R.id.Controller_Author);
                Controller_Name.setText(songs.get(songPosn).getTitle());
                Controller_Author.setText(songs.get(songPosn).getArtist());

                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) view.getLayoutParams();
                lp.bottomMargin = 0;

                Model.instance().getRel().removeView(Model.instance().getAdded());

                Model.instance().getRel().addView(view);


                //Model.instance().getRel().removeView(Model.instance().getRelativeLayout());
               // Model.instance().getRelativeLayout().addView(relativeLayout);

                //Model.instance().getRel().removeView(Model.instance().getRelativeLayout());
                //Model.instance().getRel().addView(Controller_Author);
            }

            playSong();
        }
    }

    public int getNumber(){return songPosn;}

    @Override
    public void onDestroy() {
        stopForeground(true);
    }


}
