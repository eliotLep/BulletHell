package com.example.bullethell.core;

import android.util.Log;

import com.example.bullethell.Controller.MainMenuController;

public class MainMenuThread extends Thread{
    private MainMenuController controller;
    private boolean isRunning;
    private boolean isPaused=false;

    public MainMenuThread(MainMenuController controller){
        this.controller=controller;
        this.isRunning=false;
    }

    @Override
    public void start() {
        super.start();
        this.isRunning=true;
    }


    @Override
    public void run() {
        do {
            while (this.isRunning) {
                this.controller.displayGraphics();
                Log.e("test","boucleIn");
            }
            Log.e("test","boucleOut");
        }while(isPaused || this.isRunning);
    }

    public void onQuit(){
        Log.e("test","quit");
        this.isRunning=false;
        this.isPaused=false;
    }

    public void onPause(){
        Log.e("test","pause");
        this.isPaused=true;
        this.isRunning=false;
    }

    public void onResume(){
        Log.e("test","resume");
        this.isRunning=true;
        this.isPaused=false;
    }

}
