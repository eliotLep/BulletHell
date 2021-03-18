package com.example.bullethell.core;

public class InfoBulletGenerator {
    public Vector2 sizeB;
    public  Vector2 posB;
    public  Vector2 dirB;
    public  long timeToGoB;
    public  int rangeB;
    public  int speedB;
    public  String spriteNameB;
    public  long timePerFrameB;
    public  int damageB;

    public InfoBulletGenerator(Vector2 sizeB,Vector2 posB,Vector2 dirB,long timeToGoB,int rangeB,int speedB,String spriteNameB,long timePerFrameB,int damageB){
        this.sizeB=sizeB;
        this.posB=posB;
        this.dirB=dirB;
        this.timeToGoB=timeToGoB;
        this.rangeB=rangeB;
        this.speedB=speedB;
        this.spriteNameB=spriteNameB;
        this.timePerFrameB=timePerFrameB;
        this.damageB=damageB;
    }
}
