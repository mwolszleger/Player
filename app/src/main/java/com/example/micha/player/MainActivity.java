package com.example.micha.player;

import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import android.net.Uri;
import android.content.ContentResolver;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.support.v4.content.ContextCompat;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity  {


    public static ArrayList<Song> songList;
    private static ListView songView;
    private Player player=new Player(this);

    private int SongId;
    private static SongAdapter songAdt;


    private static boolean firstTime=true;
    private Button button;
    private SeekBar seek;
    private Handler mHandler = new Handler();
    private Button buttonNext;
    private Button buttonPrevious;
    private TextView text;
    @Override
    protected void onStart() {
        super.onStart();
        Log.e("test","resume");
        songAdt = new SongAdapter(this, songList);
        songView.setAdapter(songAdt);
        songView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("test","cre");
        button = (Button) findViewById(R.id.button3);
        buttonNext = (Button) findViewById(R.id.button4);
        buttonPrevious = (Button) findViewById(R.id.button2);
        seek=(SeekBar)findViewById(R.id.seekBar);
        text=(TextView) findViewById(R.id.textView);

        songView = (ListView) findViewById(R.id.song_list);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(player.isNull())
                    return;
                if(player.isPlaying())
                {
                    player.pause();
                    button.setText("PLAY");
                }
                else {
                    player.start();
                    button.setText("PAUSE");
                }
            }
        });

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextSong();
            }
        });
        buttonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previousSong();
            }
        });
        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {


                if(fromUser) {
                    seekBar.setMax(player.getDuration() / 1000);

                    player.seekTo(progress * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        runOnUiThread(new Runnable() {

            @Override
            public void run() {



                if(!player.isNull()){
                    int mCurrentPosition = player.getCurrentPosition() / 1000;
                    seek.setProgress(mCurrentPosition);
                }
                mHandler.postDelayed(this, 1000);
                if(player.isFinished())
                    nextSong();
            }
        });
        if(firstTime) {
            songList = new ArrayList<Song>();
            getSongList(songList);
            if(PlayLists.allSongsList.size()==0)
                getSongList(PlayLists.allSongsList);
            firstTime=false;



        Collections.sort(songList, new Comparator<Song>() {
            public int compare(Song a, Song b) {
                return a.getTitle().compareTo(b.getTitle());
            }
        });
            PlayLists.allSongsList=new ArrayList<Song>(songList);
        }
        songAdt = new SongAdapter(this, songList);
        songView.setAdapter(songAdt);
        songView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

    Log.e("trzy",Integer.toString(songList.size()));
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);


        text.setText(savedInstanceState.getString("name"));
        button.setText(savedInstanceState.getString("button"));

    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("name",text.getText().toString());
        outState.putString("button",button.getText().toString());

    }
    public void getSongList(ArrayList<Song> songsList) {
        //retrieve song info

        ContentResolver musicResolver = getContentResolver();
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        0);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
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
                songsList.add(new Song(thisId, thisTitle, thisArtist));
            }
            while (musicCursor.moveToNext());

        }

    }
    public void newSong(int id)
    {
        Uri trackUri = ContentUris.withAppendedId(
                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                songList.get(id).getID());
        player.newSong(trackUri);
        text.setText(songList.get(id).getTitle());
        button.setText("PAUSE");
    }
    public void playLists(View view)
    {
        if(!player.isNull()&&player.isPlaying())
            player.pause();
        button.setText("PLAY");
        text.setText("");
        Intent intent = new Intent(this, ListsHandling.class);
        startActivity(intent);

    }
    void nextSong()
    {
        if(songList.size()!=SongId+1)
        {
            SongId++;
        }
        else SongId=0;
        newSong(SongId);


    }
    void previousSong()
    {

        if(SongId!=0)
        {
            SongId--;
        }
        else SongId=songList.size()-1;
        newSong(SongId);

    }
    public void songPicked(View view){

        int id = Integer.parseInt(view.getTag().toString());
        newSong(id);

        SongId=id;
    }


}
