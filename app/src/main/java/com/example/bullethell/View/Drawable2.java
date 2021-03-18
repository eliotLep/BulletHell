package com.example.bullethell.View;

import android.graphics.drawable.Drawable;

public abstract class Drawable2 extends Drawable implements Comparable{
    protected int layer; //affiche en premeir les faibles layer, un objet avec un layer plus haut sera affiché par dessus un objet avec un layer plus faible

    @Override
    public int compareTo(Object o){
        if(o instanceof Drawable2){
            if( this.layer>((Drawable2) o).layer )return 1;
            if( this.layer<((Drawable2) o).layer )return -1;
            return 0;
        }
        return 0;
    }

    public int getLayer(){
        return this.layer;
    }
}
