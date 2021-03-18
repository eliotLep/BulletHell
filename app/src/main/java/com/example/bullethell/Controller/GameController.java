package com.example.bullethell.Controller;

import android.content.Context;
import android.util.Log;

import com.example.bullethell.Niveau;
import com.example.bullethell.core.Entity;
import com.example.bullethell.core.GameThread;
import com.example.bullethell.View.GameView;
import com.example.bullethell.core.SoundManager;

import java.io.IOException;
import java.io.InputStream;

public class GameController {
    private GameView view;
    private GameThread model;
    private Niveau niveau;

    public GameController(Context context, Niveau niveau, String nomMob){
        this.niveau=niveau;
        this.view=new GameView(context,niveau,this);

        this.model=new GameThread(60,this.view,nomMob,this);
        this.model.start(); //lance le thread du jeu
    }



    public void displayGraphics(java.util.List<Entity> entities){
        this.view.displayGraphics(entities);
    }

    public void onLose(float score){
        this.stop();
        this.niveau.onLose(score);
    }
    public void onWin(float score){
        this.stop();
        this.niveau.onWin(score);
    }

    public InputStream getXmlData(String fileName){
        InputStream is=null;
        try {
            is=this.niveau.getAssets().open(fileName);
        }catch(IOException e){
            Log.e("INFO CONTROLLER","Failed to retrieved Xml data from : "+fileName);
        }
        return is;
    }


    public void initUI(){
        this.view.initUI();
    }

    public SoundManager getSoundManager(){
        return this.view.getSoundManager();
    }

    public void stop(){
        this.view.stop();
        this.model.stopp();
    }
    public void pause(){
        this.view.pause();
        this.model.pause();
    }
    public void resume(){
        this.view.resume();
        this.model.resumee();
    }


    public GameView getView(){
        return this.view;
    }
    public GameThread getModel(){
        return this.model;
    }

}
