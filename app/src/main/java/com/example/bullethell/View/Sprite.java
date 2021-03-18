package com.example.bullethell.View;

import android.graphics.Bitmap;

import com.example.bullethell.core.Vector2;

public class Sprite {
    public Bitmap sprite;
    public Vector2 nbFrame; //nb de frame en x et y sur le sprite sheet
    public Vector2 size; //taille d'une frame

    public Sprite(Bitmap sprite,Vector2 nbFrame,Vector2 size){
        this.size=size;
        this.nbFrame=nbFrame;
        this.sprite=sprite;
    }

    public void unloadSprite(){
        if(this.sprite==null)return;
        this.sprite.recycle();
        this.sprite=null;
    }

}
