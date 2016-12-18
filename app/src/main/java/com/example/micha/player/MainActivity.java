package com.example.micha.player;

import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaPlayer;
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
import android.widget.ListView;
import android.support.v4.content.ContextCompat;
public class MainActivity extends AppCompatActivity  {


    public static ArrayList<Song> songList;
    private static ListView songView;
private static int SongId;
    public static SongAdapter songAdt;
    public static MediaPlayer mp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);//test21

        //mp=new MediaPlayer();
        songView = (ListView) findViewById(R.id.song_list);
        songList = new ArrayList<Song>();


        getSongList();

        Collections.sort(songList, new Comparator<Song>() {
            public int compare(Song a, Song b) {
                return a.getTitle().compareTo(b.getTitle());
            }
        });
        songAdt = new SongAdapter(this, songList);
        songView.setAdapter(songAdt);
        songView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);


    }

    public void getSongList() {
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
                songList.add(new Song(thisId, thisTitle, thisArtist));
            }
            while (musicCursor.moveToNext());

        }


        //mp=MediaPlayer.create(this,trackUri);
//        try {
//            mp.setDataSource(getApplicationContext(),trackUri);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }
    public static void newSong(int id,Activity a)
    {
        Uri trackUri = ContentUris.withAppendedId(
                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                songList.get(id).getID());
        if(mp != null) {

            mp.stop();
            mp.release();

        }
        mp=MediaPlayer.create(a,trackUri);

    }
    static void nextSong(Activity a)
    {


        if(songList.size()!=SongId+1)
        {
            SongId++;


        }
        else SongId=0;
        newSong(SongId,a);

    }
    static void previousSong(Activity a)
    {

        if(SongId!=0)
        {
            SongId--;


        }
        else SongId=songList.size()-1;
        newSong(SongId,a);

    }
    public void songPicked(View view){

        int id = Integer.parseInt(view.getTag().toString());
        newSong(id,this);
        SongId=id;
    }


}
