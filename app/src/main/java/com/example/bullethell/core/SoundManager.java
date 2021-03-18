package com.example.bullethell.core;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

import com.example.bullethell.Niveau;

import java.io.IOException;
import java.io.InputStream;

public class SoundManager {

    private MediaPlayer mediaPlayer;
    private Context context;

    public SoundManager(Context context){
        this.context=context;
        this.mediaPlayer=null;
    }

    public void setSound(int sound){
        mediaPlayer = MediaPlayer.create(context,sound);
    }

    public void start(){
        if(mediaPlayer!=null)
            mediaPlayer.start();
    }

    public void pause(){
        if(mediaPlayer!=null)
            mediaPlayer.pause();
    }

    public void stop(){
        if(mediaPlayer==null)return;
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer=null;
    }

    public void resume(){
        start();
    }




}
