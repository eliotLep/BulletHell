package com.example.bullethell.Controller;

import com.example.bullethell.Loading;
import com.example.bullethell.core.ChargementThread;
import com.example.bullethell.View.LoadingView;

public class LoadingController {
    private ChargementThread model;
    private Loading view;
    private LoadingView loadingView;

    public LoadingController(Loading view, long durationSplashScreen){
        this.view=view;
        this.loadingView=new LoadingView(view,durationSplashScreen);
        this.model=new ChargementThread(this,durationSplashScreen);
    }

    public void displayGraphics(){
        this.loadingView.displayGraphics();
    }

    public void onQuit(){
        //this.loadingView.setScreenNormalMod();
        this.view.goToMainMenu();
    }

    public LoadingView getView(){
        return this.loadingView;
    }

    public void start(){
        //this.loadingView.setScreenPlayMod();
        this.model.start();
    }

}
