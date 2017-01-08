package com.example.micha.player;

import android.app.Activity;
import android.content.ContentUris;
import android.media.MediaPlayer;
import android.net.Uri;

/**
 * Created by Michal on 08.01.2017.
 */

 public class Player {

    private static MediaPlayer mp;
    private Activity activity;

    public boolean isFinished() {
        return finished;
    }



    private boolean finished;
    public Player(Activity activity)
    {
        this.activity=activity;
        finished=false;

    }
    public void newSong(Uri trackUri)
    {
        finished=false;
        if(mp != null) {

            mp.stop();
            mp.release();

        }
        mp=MediaPlayer.create(activity,trackUri);
        mp.setOnCompletionListener( new MediaPlayer.OnCompletionListener(){
            public void onCompletion(MediaPlayer player) {
                finished=true;
            }});
        mp.start();

    }
    public boolean isPlaying()
    {
        return mp.isPlaying();
    }
    public boolean isNull()
    {
        return mp==null;
    }
    public void pause()
    {
        mp.pause();
    }
    public void start()
    {
        finished=false;
        mp.start();
    }

    public int getCurrentPosition() {
        return mp.getCurrentPosition();
    }

    public int getDuration() {
        return mp.getDuration();
    }

    public void seekTo(int i) {
        finished=false;
        mp.seekTo(i);
    }

}

