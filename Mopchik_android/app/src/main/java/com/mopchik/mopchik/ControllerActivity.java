package com.mopchik.mopchik;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.os.Handler;

import static com.mopchik.mopchik.MusicFragment.musicSrv;

public class ControllerActivity extends AppCompatActivity implements View.OnClickListener,SeekBar.OnSeekBarChangeListener/*MediaController.MediaPlayerControl*/ {



    ImageButton play_dark;
    ImageButton pause_dark;
    ImageButton previous_dark;
    ImageButton next_dark;
    TextView Controller_Name;
    TextView Controller_Author;
    RelativeLayout relativeLayout;
    RelativeLayout activity_controller;
    SeekBar seekbar;
    private Handler mHandler = new Handler();;
    private Utilities utils;
    TextView songCurrentDurationLabel;
    TextView songTotalDurationLabel;
    ImageButton down;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        utils = new Utilities();

        setContentView(R.layout.activity_controller);
        activity_controller=(RelativeLayout)findViewById(R.id.activity_controller);
        Model.instance().setRel(activity_controller);

        Model.instance().setIsControllerActivity(true);
        Model.instance().setContext(this);

        View view = LayoutInflater.from(this).inflate(R.layout.controller,activity_controller,false);
        Model.instance().setView(view);

        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) view.getLayoutParams();
        lp.bottomMargin=0;

        songCurrentDurationLabel = (TextView) view.findViewById(R.id.songCurrentDurationLabel);
        songTotalDurationLabel = (TextView) view.findViewById(R.id.songTotalDurationLabel);

        play_dark=(ImageButton)view.findViewById(R.id.play_dark);
        pause_dark=(ImageButton)view.findViewById(R.id.pause_dark);
        previous_dark=(ImageButton)view.findViewById(R.id.previous_dark);
        next_dark=(ImageButton)view.findViewById(R.id.next_dark);
        down=(ImageButton)findViewById(R.id.down);

        seekbar=(SeekBar)view.findViewById(R.id.seekbar);

        play_dark.setOnClickListener(this);
        pause_dark.setOnClickListener(this);
        previous_dark.setOnClickListener(this);
        next_dark.setOnClickListener(this);
        down.setOnClickListener(this);

        if(Model.instance().getPause()){
            play_dark.setVisibility(View.VISIBLE);
            pause_dark.setVisibility(View.GONE);
        }else{
            play_dark.setVisibility(View.GONE);
            pause_dark.setVisibility(View.VISIBLE);
        }

        /*seekbar.setMax(Model.instance().getMusicSrv().getDur());
        seekbar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                seekChange(v);
                updateProgressBar();
                return false;
            }
        });*/
        //Model.instance().getMusicSrv().setOnBufferingUpdateListener();



        seekbar.setProgress(0);
        seekbar.setMax(100);
        seekbar.setOnSeekBarChangeListener(this);

        // Updating progress bar
        updateProgressBar();




        Controller_Name=(TextView)view.findViewById(R.id.Controller_Name);
        Controller_Author=(TextView)view.findViewById(R.id.Controller_Author);


        Controller_Name.setText(Model.instance().getName());
        Controller_Author.setText(Model.instance().getAuthor());

        activity_controller.addView(view);
        Model.instance().setAdded(view);

    }



    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.play_dark:
                musicSrv.go();
                Model.instance().setPause(false);
                play_dark.setVisibility(View.GONE);
                pause_dark.setVisibility(View.VISIBLE);
                break;
            case R.id.pause_dark:
                musicSrv.pausePlayer();
                Model.instance().setPause(true);
                play_dark.setVisibility(View.VISIBLE);
                pause_dark.setVisibility(View.GONE);
                break;
            case R.id.next_dark:
                Log.e("onClick","next_dark");
                musicSrv.playNext();
                Model.instance().setPause(false);
                play_dark.setVisibility(View.GONE);
                pause_dark.setVisibility(View.VISIBLE);
                break;
            case R.id.previous_dark:
                musicSrv.playPrev();
                Model.instance().setPause(false);
                play_dark.setVisibility(View.GONE);
                pause_dark.setVisibility(View.VISIBLE);
                break;
            case R.id.down:
                Intent intent=new Intent(this,MainActivity.class);
                startActivity(intent);
        }
    }


    @Override
    public void onStop(){
        Model.instance().setIsControllerActivity(false);
        Log.e("onClick","onStop()");
        super.onStop();
    }

    public void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }

    /**
     * Background Runnable thread
     * */
    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            long totalDuration = musicSrv.getDur();
            long currentDuration = musicSrv.getPosn();

            // Displaying Total Duration time
            songTotalDurationLabel.setText(""+utils.milliSecondsToTimer(totalDuration));
            // Displaying time completed playing
            songCurrentDurationLabel.setText(""+utils.milliSecondsToTimer(currentDuration));

            // Updating progress bar
            int progress = (int)(utils.getProgressPercentage(currentDuration, totalDuration));
            //Log.d("Progress", ""+progress);
            seekbar.setProgress(progress);

            // Running this thread after 1000 milliseconds
            mHandler.postDelayed(this, 1010);
        }
    };

    /**
     *
     * */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // remove message Handler from updating progress bar
        mHandler.removeCallbacks(mUpdateTimeTask);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mHandler.removeCallbacks(mUpdateTimeTask);
        int totalDuration = musicSrv.getDur();
        int currentPosition = utils.progressToTimer(seekBar.getProgress(), totalDuration);

        // forward or backward to certain seconds
        musicSrv.seek(currentPosition);

        // update timer progress again
        updateProgressBar();
    }
}
