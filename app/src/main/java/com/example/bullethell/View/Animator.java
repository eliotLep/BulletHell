package com.example.bullethell.View;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

import com.example.bullethell.core.Vector2;

public class Animator {
    private Sprite sprite;
    private long startTime;
    private long timePerFrame;


    public Animator(Sprite sprite,long timePerFrame){
        this.sprite=sprite;
        this.startTime=-1;
        this.timePerFrame=timePerFrame;
    }

    public Sprite getSprite(){
        return this.sprite;
    }

    public Animator copy(){
        return new Animator(this.sprite,this.timePerFrame);
    }

    public void anime(Canvas canvas,RectF dest){
        if(this.sprite==null)return;

        Vector2 frameActu;
        if(this.startTime==-1){
            this.startTime=java.lang.System.currentTimeMillis();
            frameActu = new Vector2(0,0);
        }else {
            int nbTotalFrame= (int)(this.sprite.nbFrame.x*this.sprite.nbFrame.y);
            long timeSinceStart=java.lang.System.currentTimeMillis()-startTime;
            long nbFrameSinceStart= timeSinceStart / timePerFrame;
            if(nbFrameSinceStart+1 <= nbTotalFrame){
                int x= (int)(nbFrameSinceStart % this.sprite.nbFrame.x);
                int y= (int)(nbFrameSinceStart / this.sprite.nbFrame.x);
                frameActu = new Vector2(x,y);
            }else{
                this.startTime=java.lang.System.currentTimeMillis();
                frameActu = new Vector2(0,0);
            }
        }
        Rect source=new Rect((int)(0+this.sprite.size.x*frameActu.x),(int)(0+this.sprite.size.y*frameActu.y),(int)(this.sprite.size.x+this.sprite.size.x*frameActu.x),(int)(this.sprite.size.y+this.sprite.size.y*frameActu.y) );
        canvas.drawBitmap(this.sprite.sprite,source,dest,null);

    }


}
