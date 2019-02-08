package com.mopchik.mopchik;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.dropbox.core.android.Auth;
import com.mopchik.mopchik.MusicService.MusicBinder;
import android.widget.MediaController.MediaPlayerControl;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MusicFragment extends Fragment implements View.OnClickListener/*MediaPlayerControl*/{

    private ArrayList<Song> songList;
    private ListView songView;

    public View view;
    public Activity activity;
    public Context context;

    RelativeLayout rel;

    LayoutInflater inflater;

    public static MusicService musicSrv;
    public static Intent playIntent;
    private boolean musicBound=false;
    SongAdapter songAdt;
    private boolean playbackPaused = false;

    ImageButton play;
    ImageButton pause;
    TextView name;
    TextView author;

    View view1;
    RelativeLayout activity_music;

    public void onAttach(Context context) {
        super.onAttach(context);
        Log.e("onClick","onAttach");
        this.context = context;
        Model.instance().setContext(context);
        if (context instanceof Activity){
            activity=(Activity) context;
        }

    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        this.inflater=inflater;
        view = inflater.inflate(R.layout.activity_music, container, false);
        activity_music = (RelativeLayout)view.findViewById(R.id.activity_music);
        Model.instance().setRel(activity_music);



        view1 = inflater.inflate(R.layout.rel,activity_music,false);
        Model.instance().setView(view1);
        name = (TextView)view1.findViewById(R.id.name);
        author = (TextView)view1.findViewById(R.id.author);

        Model.instance().setTextName(name);
        Model.instance().setTextAuthor(author);

        pause = (ImageButton)view1.findViewById(R.id.pause);
        play = (ImageButton)view1.findViewById(R.id.play);

        if(Model.instance().getPlayed()){
            RelativeLayout.LayoutParams lp= (RelativeLayout.LayoutParams) view1.getLayoutParams();
            lp.bottomMargin=0;

            name.setText(Model.instance().getName());
            author.setText(Model.instance().getAuthor());

            pause.setOnClickListener(MusicFragment.this);
            play.setOnClickListener(MusicFragment.this);



            activity_music.addView(view1);
            Model.instance().setAdded(view1);
        }





        //rel=(RelativeLayout)view.findViewById(R.id.rel);

        //if(musicSrv==null) rel.setVisibility(View.GONE);


        /*name=(TextView)view.findViewById(R.id.name);
        author=(TextView)view.findViewById(R.id.author);
        play = (ImageButton)view.findViewById(R.id.play);
        pause=(ImageButton)view.findViewById(R.id.pause);

        play.setVisibility(View.GONE);

        play.setOnClickListener(this);
        pause.setOnClickListener(this);*/


        //ctr = new Controller(activity);
        /*songView = (ListView)view.findViewById(R.id.song_list);

        songList = new ArrayList<Song>();*/

        init();

        getSongList(songList);


        Collections.sort(songList, new Comparator<Song>(){
            public int compare(Song a, Song b){
                return a.getTitle().compareTo(b.getTitle());
            }
        });

        Model.instance().setSongList(songList);

        songAdt = new SongAdapter(context, songList, songView);
        songView.setAdapter(songAdt);

        //setController();

        return view;
    }


    private void init() {
        songList = new ArrayList<>();
        songView = (ListView) view.findViewById(R.id.song_list);

        // Sets each song with a functionality.
        songView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                // Sets the respective song in the Service, and then plays it.
                //name.setText(songList.get(position).getTitle());
                //author.setText(songList.get(position).getArtist());
                //rel.setVisibility(View.VISIBLE);
                //MainActivity m = new MainActivity();
                //m.setRelVisible();

                if (position != musicSrv.getNumber()) {
                    if (!Model.instance().getPlayed()) {
                        view1 = inflater.inflate(R.layout.rel, activity_music, false);
                        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) view1.getLayoutParams();
                        lp.bottomMargin = 0;

                        activity_music.addView(view1);
                        Model.instance().setAdded(view1);
                        Model.instance().setPlayed(true);
                    }

                    name = (TextView) view1.findViewById(R.id.name);
                    author = (TextView) view1.findViewById(R.id.author);
                    pause = (ImageButton) view1.findViewById(R.id.pause);
                    play = (ImageButton) view1.findViewById(R.id.play);

                    play.setVisibility(View.GONE);
                    pause.setVisibility(View.VISIBLE);
                    Model.instance().setPause(false);

                    name.setText(songList.get(position).getTitle());
                    author.setText(songList.get(position).getArtist());
                    pause.setOnClickListener(MusicFragment.this);
                    play.setOnClickListener(MusicFragment.this);

                    Model.instance().setName(songList.get(position).getTitle());
                    Model.instance().setAuthor(songList.get(position).getArtist());

                    musicSrv.setSong(position);
                    musicSrv.playSong();

                    // Sets the flag to false for the controller's duration and position purposes.
                    if (playbackPaused) playbackPaused = false;
                }else{
                    Intent intent = new Intent(context,ControllerActivity.class);
                    startActivity(intent);
                }
            }
        });
    }


    public void getSongList(ArrayList<Song> songList) {
        //retrieve song info
        ContentResolver musicResolver = activity.getContentResolver();
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);

        if(musicCursor!=null && musicCursor.moveToFirst()){
            //get columns
            int titleColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.ARTIST);
            //add songs to list
            do {
                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                songList.add(new Song(thisId, thisTitle, thisArtist));
            }
            while (musicCursor.moveToNext());
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play:
                musicSrv.go();
                play.setVisibility(View.GONE);
                pause.setVisibility(View.VISIBLE);
                Model.instance().setPause(false);
                break;
            case R.id.pause:
                musicSrv.pausePlayer();
                pause.setVisibility(View.GONE);
                play.setVisibility(View.VISIBLE);
                Model.instance().setPause(true);
                break;
        }
    }



    private ServiceConnection musicConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicBinder binder = (MusicBinder)service;
            //get service
            musicSrv = binder.getService();
            //pass list
            musicSrv.setList(songList);
            //Model.instance().setMusicSrv(musicSrv);
            musicBound = true;
            Model.instance().setmusicBound(musicBound);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
            Model.instance().setmusicBound(musicBound);
        }

    };

    @Override
    public void onStart() {
        super.onStart();
        if(musicSrv==null){
            playIntent = new Intent(activity, MusicService.class);
            Log.e("oncClick","увы-увы");
            context.bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            context.startService(playIntent);
        }
        if(Model.instance().getPause()) pause.setVisibility(View.GONE);
        else play.setVisibility(View.GONE);
    }


    @Override
    public void onDestroy() {

        super.onDestroy();
    }



    /*public static void showController() {
        controller.show(0);
    }




    @Override
    public void onStop() {
        controller.chetkiy_hide();
        Model.instance().setPaused(true);
        super.onStop();
    }


    public void setController(){
        controller = new MusicController(context);

        controller.setPrevNextListeners(new View.OnClickListener() {
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
        controller.setMediaPlayer(this);
        controller.setAnchorView(songView);
        controller.setEnabled(true);
    }

    //play next
    private void playNext(){
        musicSrv.playNext();
        Model.instance().setplaybackPaused(false);
    }

    //play previous
    private void playPrev(){
        musicSrv.playPrev();
        Model.instance().setplaybackPaused(false);
    }


    //implement methods
    @Override
    public void start() {
        musicSrv.go();
    }

    @Override
    public void pause() {
        musicSrv.pausePlayer();
        playbackPaused=true;
    }

    @Override
    public int getDuration() {
        if(musicSrv!=null && musicBound && musicSrv.isPng())
            return musicSrv.getDur();
        else if(playbackPaused) return musicSrv.getDur();
        else return 0;
    }

    @Override
    public int getCurrentPosition() {
        if(musicSrv!=null && musicBound && musicSrv.isPng())
            return musicSrv.getPosn();
        else if(playbackPaused) return musicSrv.getPosn();
        else return 0;
    }

    @Override
    public void seekTo(int pos) {
        musicSrv.seek(pos);
    }

    @Override
    public boolean isPlaying() {
        if(musicSrv!=null && musicBound)
            return musicSrv.isPng();
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
        return 0;
    }
*/
}