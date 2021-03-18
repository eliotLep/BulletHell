package com.example.bullethell.core;

import com.example.bullethell.View.Animator;

public class Bullet extends Entity{

    private float range;


    public Bullet(java.util.List<Entity> entities, Vector2 size, float hpMax, float speed, float invincibleTime, Vector2 position, Vector2 direction, int team, float damage, float range, Animator animator){
        super(entities,size,hpMax,speed,invincibleTime,position,direction,team,damage,animator);
        this.range=range;
        this.layer=this.layer-1;

    }

    public float getRange(){
        return this.range;
    }
    public void setRange(float range){
        this.range=range;
    }

    public void run(){
        super.run();
        this.move();
    }

    public void onContact(Entity entity) {
        if (entity.team != this.team && !(entity instanceof Bullet)) {
            this.doDamage(entity);
        }
    }


    public void move(){

        int maxNbPas=1000;
        float transX=this.direction.x*this.speed*(float)DeltaTime.getDeltaTime();
        float transY=this.direction.y*this.speed*(float)DeltaTime.getDeltaTime();

        int nbPas = (int)(Math.abs( Vector2.distance2Vector2(this.position, new Vector2(this.position.x + transX, this.position.y + transY) ) )/((this.size.x+this.size.y)/2)+1);
        if(nbPas<=maxNbPas){
            for(int i=0;i<nbPas;i++) {
                Vector2 oldPos=this.position;
                this.position = new Vector2(this.position.x + transX / nbPas, this.position.y + transY / nbPas);
                this.applyOnListContact(this.checkContacts(this.entities));

                this.range-= Vector2.distance2Vector2(oldPos,this.position);
                if(this.noRange()){
                    return;
                }

            }
        }else {
            Vector2 oldPos=this.position;
            this.position = new Vector2(this.position.x + transX, this.position.y + transY);
            this.applyOnListContact(this.checkContacts(this.entities));

            this.range-= Math.abs(Vector2.distance2Vector2(oldPos,this.position));
            if(this.noRange()){
                return;
            }
        }
    }


    public Boolean noRange(){
        if(this.range<=0) {
            this.onDeath();
            return true;
        }
        return false;
    }

    public void doDamage(Entity entity){
        this.takeDamage( entity.takeDamage(this.damage) );
    }

    public void onDeath(){
        //balle detruite == suppression de la balle des entités
        super.onDeath();
        //if(this.entities!=null)
            this.entities.remove(this);

    }
}
