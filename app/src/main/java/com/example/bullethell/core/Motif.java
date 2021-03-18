package com.example.bullethell.core;

import com.example.bullethell.View.Animator;
import com.example.bullethell.View.Drawer;
import com.example.bullethell.View.GameView;
import com.example.bullethell.View.Sprite;

import java.util.ArrayList;

public class Motif {
    protected java.util.List<BulletGenerator> bulletGenerator;
    protected long timeStartMotif;
    protected long durationMotif;
    protected boolean isStarted;
    protected boolean isDone;
    protected float procConditionPercentageHp; //pourcentage d'hp restant avant lequel le boss ne pourra pas faire ce motif
    protected double procConditionTime; //temps requis minimum de combat contre ce mob avant qu'il puisse faire ce motif
    protected java.util.List<Entity> entities;
    protected int team;
    protected int motifID;
    protected int motifWeight;

    public Motif(float procConditionPercentageHp,double procConditionTime,int team,long durationMotif,int motifID,int motifWeight){
        this.procConditionPercentageHp=procConditionPercentageHp;
        this.procConditionTime=procConditionTime;
        this.bulletGenerator=new ArrayList<BulletGenerator>();
        this.isStarted=false;
        this.isDone=false;
        this.team=team;
        this.durationMotif=durationMotif;
        this.motifID=motifID;
        this.motifWeight=motifWeight;
    }

    public void start(java.util.List<Entity> entities){
        this.timeStartMotif=java.lang.System.currentTimeMillis();
        this.isStarted=true;
        this.entities=entities;
        this.setEntitiesBulletGenerator(entities);
    }

    public void run(){
        this.launchBullets();
        this.updateIsDone();
    }

    public boolean isRunning(){
        return this.isStarted==true && this.isDone==false;
    }

    public void setEntitiesBulletGenerator(java.util.List<Entity> entities){
        for(BulletGenerator b : bulletGenerator){
            b.setEntities(entities);
        }
    }

    public boolean isMotifDurationOver(){
        return this.timeStartMotif+this.durationMotif<=java.lang.System.currentTimeMillis();
    }

    public void updateIsDone(){
        for(BulletGenerator b : bulletGenerator){
            if(!b.isDone() ){
                this.isDone=false;
                return;
            }
        }
        if(this.isMotifDurationOver()) {
            this.isDone = true;
        }else {
            this.isDone = false;
        }
    }

    public void relaunchBulletGenerators(){
        for(BulletGenerator b : bulletGenerator){
            b.setIsDone(false);
        }
    }

    public double getTimeSinceStart(){
        return (java.lang.System.currentTimeMillis()-this.timeStartMotif);
    }

    protected void launchBullets(){
        for(BulletGenerator b : bulletGenerator){
            if( b.canStart( this.getTimeSinceStart()) ) {
                Bullet bullet = b.generateBullet(this.getTimeSinceStart());
                if (!bullet.noRange()) {
                    this.entities.add(bullet);
                }

            }
        }
    }

    public boolean canProc(long timeStartFight,float mobHpPerc){
        return (this.procConditionTime <= java.lang.System.currentTimeMillis()-timeStartFight && this.procConditionPercentageHp >= mobHpPerc);
    }

    public void addBulletGenerator(Vector2 sizeB,Vector2 posB,Vector2 dirB,long timeToGoB,int rangeB,int speedB,String spriteNameB,long timePerFrameB,int damageB){
        Vector2 sizeBullet = new Vector2((int) Drawer.getValFromVal(sizeB.x),(int)Drawer.getValFromVal(sizeB.y));
        Vector2 positionBullet = Drawer.getPosFromPerc(new Vector2(posB.x,posB.y));

        int range=(int)Drawer.getValFromVal(rangeB);
        int speed=(int)Drawer.getValFromVal(speedB);

        Sprite spriteBullet= GameView.sprites.get(spriteNameB);
        Animator animatorBullet=new Animator(spriteBullet,timePerFrameB);
        Bullet b = new Bullet(this.entities,sizeBullet,damageB,speed,0,new Vector2(positionBullet.x-sizeBullet.x/2,positionBullet.y-sizeBullet.y/2 ),dirB,this.team,damageB,range,animatorBullet);
        this.bulletGenerator.add( new BulletGenerator(b,timeToGoB) );
    }


}
