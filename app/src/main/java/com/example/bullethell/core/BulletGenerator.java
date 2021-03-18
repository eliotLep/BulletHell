package com.example.bullethell.core;

import android.util.Log;

public class BulletGenerator {
    private long timeToGo;
    private Bullet bullet;
    private boolean isDone;

    public BulletGenerator(Bullet bullet,long timeToGo){
        this.bullet=bullet;
        this.timeToGo=timeToGo;
        this.isDone=false;
    }

    public boolean canStart(double timeStart){
        if(timeStart>=this.timeToGo && this.isDone==false)
            return true;
        return false;
    }

    public long getTimeToGo(){
        return this.timeToGo;
    }

    public boolean isDone(){
        return this.isDone;
    }

    public void setIsDone(boolean b){
        this.isDone=b;
    }

    public void setEntities(java.util.List<Entity> entities){
        this.bullet.entities=entities;
    }

    public Bullet generateBullet(double timeStart){
        Bullet b = new Bullet(bullet.entities,new Vector2(bullet.size.x,bullet.size.y),bullet.getHpMax(),bullet.speed,bullet.getInvincibilityTime(),new Vector2(bullet.position.x,bullet.position.y),new Vector2(bullet.direction.x,bullet.direction.y),bullet.team,bullet.damage,bullet.getRange(),bullet.animator.copy());

        Vector2 oldPos=b.position;
        float deltaTime = (float)DeltaTime.getDeltaTime(this.timeToGo, timeStart);

        float distanceX=Math.abs((b.direction.x*b.speed)* deltaTime );
        float distanceY=Math.abs((b.direction.y*b.speed)* deltaTime );
        b.position=new Vector2(b.position.x+distanceX,b.position.y+distanceY);
        b.setRange( b.getRange()-Vector2.distance2Vector2(oldPos,b.position) );
        this.isDone=true;
        return b;
    }


}
