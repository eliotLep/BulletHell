package com.example.bullethell.View;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Point;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceView;

import com.example.bullethell.Controller.GameController;
import com.example.bullethell.Niveau;
import com.example.bullethell.core.Entity;
import com.example.bullethell.core.GameThread;
import com.example.bullethell.core.SoundManager;
import com.example.bullethell.core.Vector2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GameView extends SurfaceView{

    private GameController controller;

    private Niveau niveau;
    private Drawer drawer;
    private int systemUIVisibilityNormal;
    private java.util.List<UI> uiItems;
    private SoundManager soundManager;


    public static HashMap<String, Sprite> sprites=new HashMap<String,Sprite>();
    public static HashMap<String,Integer> mapSound= new HashMap<String,Integer>();




    public GameView(Context context,Niveau niveau,GameController controller) {
        super(context);
        this.niveau=niveau;
        this.controller=controller;

        this.uiItems=new ArrayList<UI>();

        this.systemUIVisibilityNormal=getSystemUiVisibility();

        this.soundManager=new SoundManager(context);
        this.addSound();

        this.setScreenPlayMod();

        this.loadBitMaps();


        //creer le drawer qui gere l'affichage
        Display display = niveau.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        this.drawer=new Drawer(new Vector2(size.x,size.y),getHolder());

    }

    public SoundManager getSoundManager(){
        return this.soundManager;
    }

    private void addSound(){
        mapSound.put("balkany",com.example.bullethell.R.raw.balkany);
    }

    public void displayGraphics(java.util.List<Entity> entities){
        synchronized(getHolder()) {
            this.drawer.displayGraphics(this.uiItems, entities);
        }
    }


    public void initUI(){

        Sprite spriteFond=this.sprites.get("fond1");
        Animator animatorFond=new Animator(spriteFond,300);
        UIFond uiFond=new UIFond();
        uiFond.setAnimator(animatorFond);
        this.uiItems.add(uiFond);

        Sprite spritePlayerHPBar=this.sprites.get("hpBar1");
        Animator animatorPlayerHPBar=new Animator(spritePlayerHPBar,10000);
        this.uiItems.add(new UIHpBarHorizontal(Drawer.getPosFromPerc(new Vector2(0,97) ),Drawer.getPosFromPerc(new Vector2(100,3) ),this.controller.getModel().getPlayer().getHealthManager(),animatorPlayerHPBar,Color.GREEN));

        Sprite spriteMobHPBar=this.sprites.get("hpBar1");
        Animator animatorMobHPBar=new Animator(spriteMobHPBar,10000);
        this.uiItems.add(new UIHpBarHorizontal(Drawer.getPosFromPerc(new Vector2(0,0) ),Drawer.getPosFromPerc(new Vector2(100,3) ),this.controller.getModel().getMob().getHealthManager(),animatorMobHPBar,Color.RED));

    }


    private void loadBitMaps(){
        //recuperer les bitmaps =====================================
        AssetManager manager=niveau.getAssets();

        this.sprites.put("mob1", new Sprite(Drawer.getBitmap(manager,"mob1.png") ,new Vector2(5,1),new Vector2(59,85) ) );

        this.sprites.put("mob2", new Sprite(Drawer.getBitmap(manager,"mob2.png") ,new Vector2(4,1),new Vector2(75,110) ) );

        this.sprites.put("mob3", new Sprite(Drawer.getBitmap(manager,"mob3.png") ,new Vector2(4,1),new Vector2(75,110) ) );

        this.sprites.put("mob4", new Sprite(Drawer.getBitmap(manager,"mob4.png") ,new Vector2(4,1),new Vector2(75,110) ) );

        this.sprites.put("mob5", new Sprite(Drawer.getBitmap(manager,"mob5.png") ,new Vector2(5,1),new Vector2(65,82) ) );

        this.sprites.put("mob6", new Sprite(Drawer.getBitmap(manager,"mob6.png") ,new Vector2(5,1),new Vector2(65,82) ) );

        this.sprites.put("player1", new Sprite(Drawer.getBitmap(manager,"player1.png"),new Vector2(4,1),new Vector2(75,110) ) );

        this.sprites.put("proj1", new Sprite(Drawer.getBitmap(manager,"proj1.png"),new Vector2(1,1),new Vector2(10,10) ) );

        this.sprites.put("proj2", new Sprite(Drawer.getBitmap(manager,"proj2.png"),new Vector2(1,1),new Vector2(10,10) ) );

        this.sprites.put("proj3", new Sprite(Drawer.getBitmap(manager,"proj3.png"),new Vector2(1,1),new Vector2(10,10) ) );

        this.sprites.put("proj4", new Sprite(Drawer.getBitmap(manager,"proj4.png"),new Vector2(1,1),new Vector2(10,10) ) );

        this.sprites.put("hpBar1", new Sprite(Drawer.getBitmap(manager,"hpBar1.png"),new Vector2(1,1),new Vector2(1000,50) ) );

        this.sprites.put("fond1", new Sprite(Drawer.getBitmap(manager,"fond1.png"),new Vector2(3,1),new Vector2(564,1002) ) );

        //manager.close();
        //==========================================================
    }

    private void unloadData(){
        for(Map.Entry<String,Sprite> entry : GameView.sprites.entrySet()){
            entry.getValue().unloadSprite();
        }
    }

    private void setScreenPlayMod(){
        this.setSystemUiVisibility(SYSTEM_UI_FLAG_IMMERSIVE | SYSTEM_UI_FLAG_FULLSCREEN | SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }
    private void setScreenNormalMod(){
        this.setSystemUiVisibility(this.systemUIVisibilityNormal);
    }


    @Override
    public boolean onTouchEvent(MotionEvent e){

        GameThread gameThread = this.controller.getModel();

        float x = e.getX();
        float y = e.getY();

        switch(e.getAction()) {
            case MotionEvent.ACTION_MOVE:
                if(gameThread.getIsPlaying() ) {
                    gameThread.setLastPlayerDestination(new Vector2(x, y));
                    gameThread.setPlayerMove(true);
                    this.setScreenPlayMod();
                }
            case MotionEvent.ACTION_DOWN:
                if(gameThread.getIsPlaying() ) {
                    gameThread.setLastPlayerDestination(new Vector2(x, y));
                    gameThread.setPlayerMove(true);
                    this.setScreenPlayMod();
                }
                //case MotionEvent.ACTION_BUTTON_RELEASE:
                //gameThread.playerMove=false;

        }
        return true;
    }


    public void stop(){
        //this.setScreenNormalMod();
        this.soundManager.stop();
        this.unloadData();
    }
    public void pause(){
        this.setScreenNormalMod();
        this.soundManager.pause();
    }
    public void resume(){
        this.setScreenPlayMod();
        this.soundManager.resume();
    }


}
