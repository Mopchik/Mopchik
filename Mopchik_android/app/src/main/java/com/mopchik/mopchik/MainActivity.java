package com.mopchik.mopchik;


import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.lang.reflect.Method;

import static com.mopchik.mopchik.MusicFragment.musicSrv;
import static com.mopchik.mopchik.MusicFragment.playIntent;


public class MainActivity extends AppCompatActivity{

    public static Context context;
    public static RelativeLayout rel;
    ImageButton play;
    ImageButton pause;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(Build.VERSION.SDK_INT>=24){
            try{
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        setContentView(R.layout.activity_main);
        LinearLayout activity_main = (LinearLayout)findViewById(R.id.container);

        context=this;



        //rel=(RelativeLayout)findViewById(R.id.rel);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //rel.setVisibility(View.GONE);
        loadFragment(new MusicFragment());

    }
    BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener=new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_download:
                    loadFragment(new DownloadFragment());
                    return true;
                case R.id.action_music:
                    loadFragment(new MusicFragment());
                    return true;
            }
            return false;
        }
    };
    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fl_content, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    public void Clicked(View v){
        Intent intent = new Intent(Model.instance().getContext(),ControllerActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDestroy(){
        musicSrv.stopService(playIntent);
        musicSrv=null;
        Log.e("onClick","onDestroy()");
        super.onDestroy();
    }
}

