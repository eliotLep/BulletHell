package com.example.bullethell.core;

import com.example.bullethell.View.Animator;

import java.util.ArrayList;

public class Mob extends Entity{
    protected java.util.List<Motif> motifs;
    protected java.util.List<Motif> motifsActu;

    protected long timeStartFight;
    protected int motifStarted;
    protected int motifMaxSameTime;

    protected int currentWeight;
    protected int motifDoneScore;

    public Mob(java.util.List<Entity> entities, Vector2 size, float hpMax, float speed, float invincibleTime, Vector2 position, Vector2 direction, int team, float damage, int motifMaxSameTime, Animator animator){
        super(entities,size,hpMax,speed,invincibleTime,position,direction,team,damage,animator);
        this.motifMaxSameTime=motifMaxSameTime;

        this.motifs=new java.util.ArrayList<Motif>();
        this.motifsActu=new java.util.ArrayList<Motif>();

        this.timeStartFight=java.lang.System.currentTimeMillis();
        this.motifStarted=0;
        this.motifDoneScore=0;
        this.currentWeight=0;
    }

    public void run(){
        super.run();
        this.attack();
    }

    private void attack(){ //prend des motifs random en fonction de son etat et les execute

        java.util.List motifStartedID=new ArrayList();
        for(Motif m : motifsActu){
            if( m.isRunning() ){
                motifStartedID.add(m.motifID);
            }
        }


        //lance les nouveaux motifs (a rendre aleatoire)
        if(this.motifStarted < this.motifMaxSameTime) {
            for (Motif m : this.motifsActu) {
                if (this.motifStarted < this.motifMaxSameTime) {
                    if (!m.isStarted && m.canProc( this.timeStartFight,this.getHpPercentage() ) && motifStartedID.contains(m.motifID)==false && this.currentWeight+m.motifWeight<=this.motifMaxSameTime ) {
                        m.start(this.entities);
                        motifStartedID.add(m.motifID);
                        this.motifStarted++;
                        this.currentWeight+=m.motifWeight;
                        this.motifDoneScore++;
                        //Log.e("test","new start motif:"+java.lang.System.currentTimeMillis());
                    }
                }
            }
        }

        //update l'etat des motifs et les run
        this.motifStarted=0;
        this.currentWeight=0;
        for(Motif m : motifsActu){
            if(m.isStarted) {
                this.motifStarted++;
                this.currentWeight+=m.motifWeight;
                if(m.isDone){
                    this.motifStarted--;
                    this.currentWeight-=m.motifWeight;
                }else{
                    m.run();
                }
            }
        }


        if(this.motifStarted<=0 && testAllMotifDone()){
            this.relaunchMotif();
        }

    }

    private void relaunchMotif(){
        for(Motif m : motifsActu){
            m.isStarted=false;
            m.isDone=false;
            m.relaunchBulletGenerators();
        }
        motifsActu.clear();
        for(Motif m : motifs){
            if( m.canProc( this.timeStartFight,this.getHpPercentage()) ){
                motifsActu.add(m);
            }
        }
        this.currentWeight=0;
    }

    private boolean testAllMotifDone(){
        for(Motif m : motifsActu){
            if( (!m.isDone && m.canProc( this.timeStartFight,this.getHpPercentage())) && m.motifWeight<=this.motifMaxSameTime){
                //Log.e("mob","succes reload motif");
                return false;
            }
        }
        return true;
    }



}
