package com.example.bullethell.View;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.SurfaceHolder;

import com.example.bullethell.core.Entity;
import com.example.bullethell.core.Vector2;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;

public class Drawer {

    private Vector2 gameSize;
    public static double coefDisplay=1;
    private SurfaceHolder surfaceHolder;
    public final static Vector2 normalRatio=new Vector2(1080,2088);


    public Drawer(Vector2 screenSize,SurfaceHolder surfaceHolder){
        this.surfaceHolder=surfaceHolder;

        final double coefXtoY = normalRatio.y/normalRatio.x;
        this.gameSize=new Vector2( screenSize.x,(int)(screenSize.x*coefXtoY) );
        //Log.e("Drawer","screen size:"+this.gameSize.x+"/"+this.gameSize.y);
        this.surfaceHolder.setFixedSize((int)this.gameSize.x,(int)this.gameSize.y);

        this.coefDisplay=this.gameSize.x/normalRatio.x;
        //Log.e("Drawer","COEF AFFICHAGE = "+coefDisplay);

    }

    public Vector2 getGameSize(){
        return this.gameSize;
    }
    public SurfaceHolder getSurfaceHolder(){
        return this.surfaceHolder;
    }



    public void displayGraphics(java.util.List<UI> uiItems, java.util.List<Entity> entities){
        //affichage graphique du jeu

        SurfaceHolder surfaceHolder=this.surfaceHolder;
        Canvas canvas;

        java.util.List<Drawable2> objectToDraw= new ArrayList<Drawable2>();

        if (surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockHardwareCanvas();

            canvas.drawColor(Color.BLACK);


            //Genere la liste des objets a afficher;
            objectToDraw.clear();
            objectToDraw.addAll(entities);
            objectToDraw.addAll(uiItems);

            Collections.sort(objectToDraw);

            //dessin des entités
            for (Drawable2 d : objectToDraw) {
                d.draw(canvas);
            }

            surfaceHolder.unlockCanvasAndPost(canvas);
        }else{
            //Log.e("INFO","not valid surface holder");
        }
    }

    public static Bitmap getBitmap(AssetManager manager, String fileName){
        InputStream open=null;
        Bitmap img=null;
        try{
            open = manager.open(fileName);
            img=BitmapFactory.decodeStream(open);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return img;
    }


    public static Vector2 getPosFromPerc(Vector2 percentagePos){
        return new Vector2 (Drawer.getValFromPerc_X(percentagePos.x),Drawer.getValFromPerc_Y(percentagePos.y) );
    }
    public static float getValFromPerc_X(float percentageVal){
        return ((percentageVal/100f)*Drawer.normalRatio.x)*(float)Drawer.coefDisplay;
    }
    public static float getValFromPerc_Y(float percentageVal){
        return ((percentageVal/100f)*Drawer.normalRatio.y)*(float)Drawer.coefDisplay;
    }
    public static Vector2 getPosFromPos(Vector2 pos){
        return new Vector2(pos.x*(float)Drawer.coefDisplay,pos.y*(float)Drawer.coefDisplay);
    }
    public static float getValFromVal(float val){
        return val*(float)Drawer.coefDisplay;
    }


}
