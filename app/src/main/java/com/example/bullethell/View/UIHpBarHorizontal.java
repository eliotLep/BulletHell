package com.example.bullethell.View;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import com.example.bullethell.core.HealthManager;
import com.example.bullethell.core.Vector2;

public class UIHpBarHorizontal extends UI{
    private Vector2 position;
    private Vector2 size;
    private HealthManager healthManager;
    private Animator animator;
    private int colorHpBar;

    private long lastFlashHealthBar;
    private long durationFlashHealthBar;
    private boolean isNormalColor;
    private int repet;



    public UIHpBarHorizontal(Vector2 position, Vector2 size, HealthManager healthManager, Animator animator,int colorHpBar){
        this.position=position;
        this.layer=0;
        this.size=size;
        this.healthManager=healthManager;
        this.animator=animator;
        this.colorHpBar=colorHpBar;
        this.lastFlashHealthBar=-1;
        this.isNormalColor=true;
    }

    public void draw(Canvas canvas){
        RectF destinationOuter = new RectF(this.position.x,this.position.y,this.position.x+this.size.x,this.position.y+this.size.y);
        animator.anime(canvas,destinationOuter);

        float hpCoef = this.healthManager.getHPPercentage()/100;
        RectF destinationInner = new RectF(this.position.x+(this.size.x/200),this.position.y+(this.size.y/20),this.position.x+(this.size.x- ((this.size.x/200)*2))*hpCoef ,this.position.y+this.size.y-(this.size.y/20)*2);
        Paint paint = new Paint();
        paint.setColor(this.getColorHpBar());
        canvas.drawRect(destinationInner,paint);

        /*Paint paintText=new Paint();
        paintText.setColor(Color.WHITE);
        paintText.setTextSize(this.size.y);
        canvas.drawText("["+(int)this.healthManager.hp+"/"+(int)this.healthManager.hpMax+"]",this.position.x,this.position.y+this.size.y,paintText);*/
    }

    private int getColorHpBar(){
        int color=this.colorHpBar;

        if(this.lastFlashHealthBar!=-1 && this.lastFlashHealthBar+this.durationFlashHealthBar<=java.lang.System.currentTimeMillis() && this.repet>0){
            this.lastFlashHealthBar=java.lang.System.currentTimeMillis();
            this.repet--;
            this.isNormalColor=!this.isNormalColor;
        }

        if( this.healthManager.isInvincible() ) {
            if(this.lastFlashHealthBar==-1){
                this.lastFlashHealthBar=java.lang.System.currentTimeMillis();
                this.durationFlashHealthBar=(long)this.healthManager.invincibilityTime/5;
                this.repet=5;
            }

        }else{
            this.lastFlashHealthBar=-1;
            this.isNormalColor=true;
        }


        if(!this.isNormalColor){
            color=Color.BLACK;
        }
        return color;
    }


}
