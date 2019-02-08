package com.mopchik.mopchik;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dropbox.core.android.Auth;

import static android.content.Context.MODE_PRIVATE;
import static com.mopchik.mopchik.MusicFragment.musicSrv;

public class DownloadFragment extends Fragment implements  View.OnClickListener{

    Activity activity;
    View view;
    Button dwnld;

    Context context;

    ImageButton play;
    ImageButton pause;

    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
        Model.instance().setContext(context);
        if (context instanceof Activity){
            activity=(Activity) context;
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {




        view=inflater.inflate(R.layout.activity_download, container, false);
        RelativeLayout activity_search= (RelativeLayout) view.findViewById(R.id.fl_home);
        Model.instance().setRel(activity_search);

        Button loginButton = (Button)view.findViewById(R.id.login);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Auth.startOAuth2Authentication(context, getString(R.string.app_key));
            }
        });

        Button filesButton = (Button)view.findViewById(R.id.dwnld);
        filesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(FilesActivity.getIntent(context, ""));
            }
        });

        View view1 = getLayoutInflater().inflate(R.layout.rel,activity_search,false);
        Model.instance().setView(view1);


        if(Model.instance().getPlayed()){

            RelativeLayout.LayoutParams lp= (RelativeLayout.LayoutParams) view1.getLayoutParams();
            lp.bottomMargin=0;
            TextView name = (TextView)view1.findViewById(R.id.name);
            TextView author = (TextView)view1.findViewById(R.id.author);
            pause = (ImageButton)view1.findViewById(R.id.pause);
            play = (ImageButton)view1.findViewById(R.id.play);

            name.setText(Model.instance().getName());
            author.setText(Model.instance().getAuthor());
            pause.setOnClickListener(this);
            play.setOnClickListener(this);

            if(Model.instance().getPause()) pause.setVisibility(View.GONE);
            else play.setVisibility(View.GONE);

            activity_search.addView(view1);
            Model.instance().setAdded(view1);
        }

        return view;
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

    @Override
    public void onResume() {
        super.onResume();



        SharedPreferences prefs = context.getSharedPreferences("dropbox-sample", MODE_PRIVATE);
        String accessToken = prefs.getString("access-token", null);
        if (accessToken == null) {
            accessToken = Auth.getOAuth2Token();
            if (accessToken != null) {
                prefs.edit().putString("access-token", accessToken).apply();
                initAndLoadData(accessToken);
            }
        } else {
            initAndLoadData(accessToken);
        }

        SharedPreferences prefs1 = context.getApplicationContext().getSharedPreferences("dropbox-sample", MODE_PRIVATE);
        //prefs1.edit().putString("access-token", null).apply();

        String uid = Auth.getUid();
        String storedUid = prefs.getString("user-id", null);
        if (uid != null && !uid.equals(storedUid)) {
            prefs.edit().putString("user-id", uid).apply();
        }

        Exception mException=null;

        try{
            DropboxClientFactory.getClient().files();
        }  catch (Exception e) {
            mException = e;
        }

        if (mException==null) {
            view.findViewById(R.id.login).setVisibility(View.GONE);
            view.findViewById(R.id.dwnld).setVisibility(View.VISIBLE);

        } else {
            view.findViewById(R.id.login).setVisibility(View.VISIBLE);
            view.findViewById(R.id.dwnld).setVisibility(View.GONE);
        }
    }

    private void initAndLoadData(String accessToken) {
        DropboxClientFactory.init(accessToken);
        PicassoClient.init(context.getApplicationContext(), DropboxClientFactory.getClient());
    }


    protected boolean hasToken() {
        SharedPreferences prefs = context.getSharedPreferences("dropbox-sample", MODE_PRIVATE);
        String accessToken = prefs.getString("access-token", null);
        return accessToken != null;
    }



    /*public class Main extends ListActivity {
        EditText text;
        Button add;
        RecordsDbHelper mDbHelper;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            //Создаем экземпляр БД
            mDbHelper = new RecordsDbHelper(this);
            //Открываем БД для записи
            mDbHelper.open();
            //Получаем Intent
            Intent intent = getIntent();
            //Проверяем тип Intent
            if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
                //Берем строку запроса из экстры
                String query = intent.getStringExtra(SearchManager.QUERY);
                //Выполняем поиск
                showResults(query);
            }

            add = (Button) findViewById(R.id.add);
            text = (EditText) findViewById(R.id.text);
            add.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    String data = text.getText().toString();
                    if (!data.equals("")) {
                        saveTask(data);
                        text.setText("");
                    }
                }
            });
        }

        private void saveTask(String data) {
            mDbHelper.createRecord(data);
        }

        private void showResults(String query) {
            //Ищем совпадения
            Cursor cursor = mDbHelper.fetchRecordsByQuery(query);
            startManagingCursor(cursor);
            String[] from = new String[] { RecordsDbHelper.KEY_DATA };
            int[] to = new int[] { R.id.text1 };

            SimpleCursorAdapter records = new SimpleCursorAdapter(this,
                    R.layout.record, cursor, from, to);
            //Обновляем адаптер
            setListAdapter(records);
        }
        //Создаем меню для вызова поиска (интерфейс в res/menu/main_menu.xml)
        public boolean onCreateOptionsMenu(Menu menu) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_main, menu);
            return true;
        }

        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.search_record:
                    onSearchRequested();
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }
    }*/
}
