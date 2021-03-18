package com.example.bullethell.core;

public class LevelInfos {

    public String niveau;
    public String mob;
    public String imageNiveau;
    public String user;
    public float score;

    public LevelInfos(String niveau, String user, float score, String mob, String imageNiveau){
        this.niveau=niveau;
        this.user=user;
        this.score=score;
        this.mob=mob;
        this.imageNiveau=imageNiveau;
    }
}
