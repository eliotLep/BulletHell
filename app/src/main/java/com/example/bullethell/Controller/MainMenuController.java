package com.example.bullethell.Controller;

import com.example.bullethell.MainMenu;
import com.example.bullethell.View.MainMenuView;
import com.example.bullethell.core.MainMenuThread;

public class MainMenuController {

    private MainMenuThread model;
    private MainMenu view;
    private MainMenuView mainMenuView;

    public MainMenuController(MainMenu view){
        this.view=view;
        this.mainMenuView=new MainMenuView(view);
        this.model=new MainMenuThread(this);
    }

    public void displayGraphics(){
        this.mainMenuView.displayGraphics();
    }


    public MainMenuView getView(){
        return this.mainMenuView;
    }

    public void start(){
        //this.loadingView.setScreenPlayMod();
        this.model.start();
    }

    public void onQuit(){
        this.model.onQuit();
    }

    public void onPause(){
        this.model.onPause();
    }
    public void onResume(){
        this.model.onResume();
    }


}
