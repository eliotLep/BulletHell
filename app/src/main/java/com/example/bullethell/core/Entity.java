package com.example.bullethell.core;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.RectF;

import com.example.bullethell.View.Animator;
import com.example.bullethell.View.Drawable2;

public abstract class Entity extends Drawable2 implements movable{
    private HealthManager healthmanager;
    protected Vector2 position;
    protected Vector2 direction; //entre -1 et 1 pour x et y
    protected Vector2 size; //largeur hauteur
    protected float speed;
    protected int team;
    protected float damage;
    protected boolean isDead;

    public Animator animator;

    public java.util.List<Entity> entities; //liste des entités n temps reel

    public long myID;
    public static long uniqID=0;

    public Entity (java.util.List<Entity> entities,Vector2 size,float hpMax,float speed,float invincibleTime,Vector2 position,Vector2 direction,int team,float damage,Animator animator){
        this.entities=entities;
        this.healthmanager=new HealthManager(this,hpMax,invincibleTime);
        this.animator=animator;
        this.position=position;
        this.speed=speed;
        this.direction=direction;
        this.team=team;
        this.damage=damage;
        this.size=size;
        this.isDead=false;
        this.layer=10;

        this.myID=this.uniqID;
        this.uniqID++;
    }

    protected java.util.List<Entity> checkContacts(java.util.List<Entity> entities){
        java.util.List listContact=new java.util.ArrayList<Entity>();
        for(Entity e : entities){
            if(this.checkContact(e))
                listContact.add(e);
        }
        return listContact;
    }

    protected boolean checkContact(Entity entity){

         //pour facilité l'esquive du joueur on retire 2/div de sa taille sur les deux axes et on centre.
        if(this instanceof Player || entity instanceof Player) {
            Vector2 myPos=Vector2.copy(this.position);
            Vector2 mySize=Vector2.copy(this.size);
            Vector2 hisPos=Vector2.copy(entity.position);
            Vector2 hisSize=Vector2.copy(entity.size);
            double div;

            if (this instanceof Player) {
                div=((Player)(this)).sizeFactor;
                double modifier=1/div; //]0,+infini[    ->0 qd div tres grand ET ->+infini qd div tres petit
                myPos.x += (mySize.x /(2+modifier) );
                myPos.y += (mySize.y /(2+modifier) );
                mySize.x -= ((mySize.x /(2+modifier) )*2.0) ;
                mySize.y -= ((mySize.y / (2+modifier) )*2.0) ;
            }
            if (entity instanceof Player) {
                div=((Player)(entity)).sizeFactor;
                double modifier=0;
                hisPos.x += (hisSize.x /(2+modifier) );
                hisPos.y += (hisSize.y /(2+modifier) );
                hisSize.x -= ((hisSize.x /(2+modifier) )*2.0) ;
                hisSize.y -= ((hisSize.y / (2+modifier) )*2.0) ;
            }
            if (entity.myID != this.myID) {
                return checkC(myPos,mySize,hisPos,hisSize);
            }
            return false;
        }
        else if (entity.myID != this.myID) {
            return checkC(this.position,this.size   ,entity.position,entity.size);
        }

        return false;
    }

    protected boolean checkC(Vector2 myPos,Vector2 mySize,Vector2 hisPos,Vector2 hisSize){
        if (myPos.x > hisPos.x + hisSize.x || myPos.x + mySize.x < hisPos.x)
            return false;
        if (myPos.y > hisPos.y + hisSize.y || myPos.y + mySize.y < hisPos.y)
            return false;
        return true;
    }

    public void move(){
        int maxNbPas=1000;
        float transX=this.direction.x*this.speed*(float)DeltaTime.getDeltaTime();
        float transY=this.direction.y*this.speed*(float)DeltaTime.getDeltaTime();
        int nbPas = (int)(Math.abs( Vector2.distance2Vector2(this.position, new Vector2(this.position.x + transX, this.position.y + transY) ) )/((this.size.x+this.size.y)/2)+1);
        if(nbPas<=maxNbPas){
            for(int i=0;i<nbPas;i++) {
                this.position = new Vector2(this.position.x + transX / nbPas, this.position.y + transY / nbPas);
                this.applyOnListContact(this.checkContacts(this.entities));
            }
        }else {
            this.position = new Vector2(this.position.x + transX, this.position.y + transY);
            this.applyOnListContact(this.checkContacts(this.entities));
        }
    }



    public void move(Vector2 destination){
        destination=new Vector2(destination.x-this.size.x/2,destination.y-this.size.y/2);
        this.direction = Vector2.getNormalizedDirection(this.position,destination);
        int maxNbPas=1000;

        float transX=this.direction.x*this.speed*(float)DeltaTime.getDeltaTime();
        float transY=this.direction.y*this.speed*(float)DeltaTime.getDeltaTime();

        float distanceVectorSpeed=Vector2.distance2Vector2(this.position,new Vector2(this.position.x+transX,this.position.y+transY));
        float distanceVectorDestination=Vector2.distance2Vector2(this.position,destination);
        if(distanceVectorSpeed>distanceVectorDestination){
            transX=(this.direction.x * (this.speed*(float)DeltaTime.getDeltaTime()) /( distanceVectorSpeed/distanceVectorDestination ));
            transY=(this.direction.y * (this.speed*(float)DeltaTime.getDeltaTime()) /( distanceVectorSpeed/distanceVectorDestination ));
        }

        double sizefactor=(this.size.x+this.size.y)/2;
        if(this instanceof Player){
            sizefactor=((this.size.x/((Player) this).sizeFactor)+(this.size.y/((Player) this).sizeFactor))/2;
        }
        int nbPas = (int)(Math.abs( Vector2.distance2Vector2(this.position, new Vector2(this.position.x + transX, this.position.y + transY) ) )/(sizefactor))+1;
        /*if(this instanceof  Player){
            Log.e("dta move",""+nbPas+"/"+sizefactor+"/"+transX+"-"+transY);
        }*/
        if(nbPas<=maxNbPas){
            for(int i=0;i<nbPas;i++) {
                this.position = new Vector2(this.position.x + transX / nbPas, this.position.y + transY / nbPas);
                this.applyOnListContact(this.checkContacts(this.entities));
            }
        }else {
            this.position = new Vector2(this.position.x + transX, this.position.y + transY);
            this.applyOnListContact(this.checkContacts(this.entities));
        }
    }


    public Vector2 getCenter(){
        return new Vector2(this.position.x+this.size.x/2,this.position.x+this.size.x/2);
    }


    protected void applyOnListContact(java.util.List<Entity> listContact){
        for(Entity e : listContact){
            this.onContact(e);
        }
    }

    public void run(){
        this.healthmanager.update();
    }

    public void doDamage(Entity entity){
        entity.takeDamage(this.damage);
    }

    public void onContact(Entity entity) {
        if (entity.team == this.team) {

        } else {

        }
    }

    public float takeDamage(float damage){
        return this.healthmanager.takeDamage(damage);
    }
    public float getHp(){
        return this.healthmanager.hp;
    }
    public float getHpPercentage(){
        return this.healthmanager.getHPPercentage();
    }
    public float getHpMax(){
        return this.healthmanager.hpMax;
    }
    public float getInvincibilityTime(){
        return this.healthmanager.invincibilityTime;
    }
    public HealthManager getHealthManager(){
        return this.healthmanager;
    }

    public void onDeath(){
        this.isDead=true;
     }



    public void draw(Canvas canvas){

        /*Paint paint=new Paint();
        paint.setColor(this.color);
        canvas.drawRect(this.position.x,this.position.y,this.position.x+this.size.x,this.position.y+this.size.y,paint);*/

        this.animator.anime(canvas,new RectF(this.position.x,this.position.y,this.position.x+this.size.x,this.position.y+this.size.y));

    }
    public void setAlpha(int alpha){

    }
    public void setColorFilter(ColorFilter colorFilter) {
    }
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }



}
