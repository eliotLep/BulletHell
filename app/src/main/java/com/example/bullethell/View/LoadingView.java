package com.example.bullethell.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.bullethell.core.Vector2;

import java.util.ArrayList;

public class LoadingView extends SurfaceView {

    private Animator animator;
    private java.util.List<Animator> drawables;
    private int systemUIVisibilityNormal;

    public LoadingView(Context context,long timeSplashScreen){
        super(context);

        this.drawables= new ArrayList<Animator>();

        Vector2 frameSplashScreen = new Vector2(11,1);
        Sprite sprite = new Sprite(Drawer.getBitmap(context.getAssets(),"splashScreen.png") ,frameSplashScreen,new Vector2(50,100) );
        this.animator = new Animator( sprite ,(long)(timeSplashScreen/(frameSplashScreen.x+frameSplashScreen.y)));
        this.drawables.add(this.animator);

        this.systemUIVisibilityNormal=getSystemUiVisibility();

    }

    public Animator getAnimator(){
        return this.animator;
    }

    public void displayGraphics(){
        SurfaceHolder surfaceHolder = getHolder();
        synchronized (surfaceHolder) {
            if (surfaceHolder.getSurface().isValid()) {
                Canvas canvas = surfaceHolder.lockHardwareCanvas();
                canvas.drawColor(Color.BLACK);
                for (Animator a : this.drawables) {
                    a.anime(canvas, new RectF(0, 0, 1000, 2000));
                }
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }

    }

    public void setScreenPlayMod(){
        this.setSystemUiVisibility(SYSTEM_UI_FLAG_IMMERSIVE | SYSTEM_UI_FLAG_FULLSCREEN | SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }
    public void setScreenNormalMod(){
        this.setSystemUiVisibility(this.systemUIVisibilityNormal);
    }


}
