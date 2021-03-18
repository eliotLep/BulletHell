package com.example.bullethell.core;

public class HealthManager {
    public float hp;
    public float hpMax;
    public float invincibilityTime; //in millis
    public double invincibilityLastTime;
    public boolean invincible;
    Entity entity;

    public HealthManager(Entity entity, float hpMax,float invincibilityTime){
        this.hp=hpMax;
        this.hpMax=hpMax;
        this.invincibilityTime=invincibilityTime;
        this.invincible=false;
        this.entity=entity;
    }

    //retourne les degats effectué
    public float takeDamage(float damage){
        this.updateInvincible();
        if(!invincible) {
            this.hp -= damage;
            if(this.hp<=0){
                this.entity.onDeath();
                return this.hp+damage;
            }else{
                this.setInvincible();
                return damage;
            }
        }
        return 0;
    }

    public float getHPPercentage(){
        return (this.hp*100)/this.hpMax;
    }

    public void setInvincible(){
        if(this.invincibilityTime>0) {
            this.invincible = true;
            this.invincibilityLastTime = java.lang.System.currentTimeMillis();
        }
    }

    public boolean isInvincible(){
        return this.invincible;
    }

    public void updateInvincible(){
        if(invincible && this.invincibilityLastTime+invincibilityTime<java.lang.System.currentTimeMillis())
            this.invincible=false;
    }

    public void update(){
        this.updateInvincible();
    }


}
