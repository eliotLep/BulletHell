package com.example.bullethell.core;

public class DeltaTime {
    private static double timePreviousFrame=-1;
    private static double deltaTime=-1;

    private static double deltaFactor=16;

    public static void setDeltaTime(){
        if(java.lang.System.currentTimeMillis()-timePreviousFrame>1000 || timePreviousFrame==-1 ){
            DeltaTime.deltaTime=16; //valeur de base pour 60 fps, on ignore les frames perdu si cela dure trop longtemps
        }else{
            DeltaTime.deltaTime=java.lang.System.currentTimeMillis()-timePreviousFrame;
        }
        timePreviousFrame=java.lang.System.currentTimeMillis();
    }

    public static double getDeltaTime(){
        return (DeltaTime.deltaTime)/DeltaTime.deltaFactor;
    }

    public static double getDeltaTime(double previousFrame, double thisFrame){
        return (thisFrame-previousFrame)/DeltaTime.deltaFactor;
    }

}
