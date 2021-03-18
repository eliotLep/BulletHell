package com.example.bullethell.core;

public class Vector2 {
    public float x;
    public float y;

    public Vector2(float x,float y){
        this.x=x;
        this.y=y;
    }

    static float distance2Vector2(Vector2 v1,Vector2 v2){
        return (float)Math.sqrt((Math.pow((v2.x-v1.x),2)+Math.pow((v2.y-v1.y),2)));
    }

    static Vector2 getNormalizedDirection(Vector2 v1,Vector2 v2){
        float x=v2.x-v1.x;
        float y=v2.y-v1.y;
        return Vector2.normalize(new Vector2(x,y));
    }

    static Vector2 normalize(Vector2 v){
        float distance = (float)Math.sqrt((Math.pow((v.x),2)+Math.pow((v.y),2)));
        if(distance!=0) {
            float x = v.x / distance;
            float y = v.y / distance;
            return new Vector2(x, y);
        }
        return new Vector2(1,1);
    }

    static Vector2 getVector2NormDirFromAngle(float angle){
        double radiantAngle=angle*(Math.PI/180);
        return new Vector2((float)Math.cos(radiantAngle),(float)Math.sin(radiantAngle));
    }

    static double getAngle(Vector2 dir){
        double angleRadian = (dir.y > 0) ? Math.acos(dir.x) : -Math.acos(dir.x);
        return (angleRadian * 180 / Math.PI);
    }

    static Vector2 copy(Vector2 v){
        return new Vector2(v.x,v.y);
    }

}
