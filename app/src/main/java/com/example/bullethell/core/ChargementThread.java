package com.example.bullethell.core;

import com.example.bullethell.Controller.LoadingController;

public class ChargementThread extends Thread{
    private LoadingController controller;
    private long start;
    private long duration;
    private boolean isRunning;

    public ChargementThread(LoadingController controller, long durationSplashScreen){
        this.controller=controller;
        this.duration=durationSplashScreen;
        this.isRunning=false;
    }

    @Override
    public void start() {
        super.start();
        this.start=System.currentTimeMillis();
        this.isRunning=true;
    }

    public boolean canStop(){
        if(System.currentTimeMillis()-this.start > duration ){
            return true;
        }
        return false;
    }

    @Override
    public void run() {
        while(this.isRunning) {
            this.controller.displayGraphics();
            if (this.canStop()) {
                this.onQuit();
                this.isRunning=false;
            }
        }
    }

    public void onQuit(){
        this.controller.onQuit();
    }

}
