package com.example.bullethell.core;

public class Score {
    public String user;
    public String niveau;
    public float score;

    public Score(String user,String niveau,String score){
        this.user=user;
        this.niveau=niveau;
        this.score=Float.parseFloat(score);
    }

}
