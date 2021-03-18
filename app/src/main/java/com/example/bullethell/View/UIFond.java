package com.example.bullethell.View;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;

public class UIFond extends UI{

    private Animator animator;

    public UIFond(){
        this.animator=null;
        this.layer=-1;
    }

    public void setAnimator(Animator animator){
        this.animator=animator;
    }
    public Animator getAnimator(){
        return this.animator;
    }

    public void draw(Canvas canvas){
        if(this.animator!=null){
            RectF dest = new RectF(0,0,(int) Drawer.getValFromVal((float)(this.animator.getSprite().size.x*2)),(int)Drawer.getValFromVal((float)(this.animator.getSprite().size.y*2)));
            this.animator.anime(canvas,dest);
        }else {
            canvas.drawColor(Color.DKGRAY);
        }
    }
}
