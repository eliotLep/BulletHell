package com.example.bullethell.core;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import com.example.bullethell.Controller.GameController;
import com.example.bullethell.View.Animator;
import com.example.bullethell.View.Drawer;
import com.example.bullethell.View.GameView;
import com.example.bullethell.View.Sprite;

public class GameThread extends Thread{
    private boolean isPlaying;
    private boolean isPaused;
    private int maxFPS;
    private GameController controller;

    private Player player;
    private Mob mob;
    private java.util.List<Entity> entities;

    private boolean playerMove;
    private Vector2 lastPlayerDestination;

    private int frame;
    private long start;


    public boolean getIsPlaying(){
        return this.isPlaying;
    }
    public boolean getIsPaused(){
        return this.isPaused;
    }
    public java.util.List<Entity> getEntities(){
        return this.entities;
    }
    public Vector2 getLastPlayerDestination(){
        return this.lastPlayerDestination;
    }
    public void setLastPlayerDestination(Vector2 v){
        this.lastPlayerDestination=v;
    }
    public boolean getPlayerMove(){
        return this.playerMove;
    }
    public void setPlayerMove(boolean b){
        this.playerMove=b;
    }



    public GameThread(int maxFPS, GameView gameView, String nomMob, GameController controller){
        DeltaTime.setDeltaTime();
        this.controller=controller;

        this.entities= new ArrayList<Entity>();
        this.lastPlayerDestination=new Vector2(0,0);
        this.playerMove=false;
        this.maxFPS=maxFPS;

        this.isPlaying=false;

        //=============
        //a recuperer via un intent a partir du choix niveau
        this.mob=null;
        this.initEntities(nomMob);
        this.frame=0;
    }

    private void initEntities(String nomMob){
        //player
        Vector2 sizePlayer = new Vector2(Drawer.getValFromVal(75),Drawer.getValFromVal(110));

        Vector2 posTmpe=Drawer.getPosFromPerc(new Vector2(50f,80) );
        Vector2 positionPlayer = new Vector2(posTmpe.x - sizePlayer.x/2,posTmpe.y - sizePlayer.y/2 );

        Vector2 directionPlayer = new Vector2(0,-1);
        int teamPlayer = 0;
        Sprite spritePlayer=GameView.sprites.get("player1");
        Animator animatorPlayer=new Animator(spritePlayer,300);
        this.player=new Player(entities,sizePlayer,30,Drawer.getValFromVal(200),1000,positionPlayer,directionPlayer,teamPlayer,1,animatorPlayer);

        //mob
        this.generateMob(nomMob);
        //============

        this.entities.add(mob);
        this.entities.add(player);
    }

    public Player getPlayer(){
        return this.player;
    }
    public Mob getMob(){
        return this.mob;
    }


    public void start(){
        this.controller.initUI();
        this.isPlaying=true;
        super.start();
        this.start=java.lang.System.currentTimeMillis();
    }


    @Override
    public void run() {
        while(this.isPlaying) {
            this.frame++;
            DeltaTime.setDeltaTime();
            long startTime = System.currentTimeMillis();
            long endTime = 0;
            int minLoopTime = 1000 / this.maxFPS;


            if (this.isPaused == false) {
                this.updateInput();
                this.runEntities();
                this.displayGraphics();
                this.updateGameState();
            }


            endTime = System.currentTimeMillis();
            try {
                long sleepTime = minLoopTime - (endTime - startTime);
                if (sleepTime > 0) {
                    Thread.sleep(sleepTime);
                }
            } catch (InterruptedException e) {
            }

        }

        Log.e("INFO","Fin du thread de jeu, fps moyen :"+ (this.frame/((java.lang.System.currentTimeMillis()-this.start)/1000))) ;

    }

    private void updateInput(){

        if(this.playerMove==true){
            this.player.move(this.lastPlayerDestination);
        }
    }


    private void runEntities(){
        java.util.List<Entity> runEntities = new ArrayList<Entity>();
        runEntities.addAll(entities);
        for(Entity e : runEntities){
            e.run();
        }
    }

    private void displayGraphics(){
        //affichage graphique du jeu
        this.controller.displayGraphics(this.entities);
    }

    private void updateGameState(){
        //update l'etat gagné ou perdu du jeu

        for(Entity e : this.entities){
            if(e.isDead){
                if(e instanceof Player){
                    //faire perdre
                    this.controller.onLose( this.getScore() );

                }else if(e instanceof Mob){
                    //faire gagner
                    this.controller.onWin( this.getScore() );

                }
            }
        }
    }

    public float getScore(){
        if(this.player.getHpPercentage()>=0 && this.mob.motifDoneScore>0)
            return (this.player.getHpPercentage()/100)*(100000/(this.mob.motifDoneScore));
        return 0;
    }


    private void generateMob(String nomMob){
        XmlPullParserFactory parserFactory;
        try {
            parserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserFactory.newPullParser();
            InputStream is = this.controller.getXmlData("Mobs.xml");
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
            parser.setInput(is, null);

            processMob(parser,nomMob);

        } catch (XmlPullParserException e) {

        } catch (IOException e) {
        }
    }

    private void processMob(XmlPullParser parser,String nomMob) throws IOException, XmlPullParserException{

        java.util.List<String> motifsMob=new ArrayList<String>();
        boolean done=false;
        boolean itemFound=false;
        int damage;
        int maxMotifSameTime;
        int hpMax;
        float posX;
        float posY;
        float sizeX;
        float sizeY;
        float motifCDReduction=1;
        String musiqueMob;


        int eventType = parser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT && done==false) {
            String eltName = null;

            switch (eventType) {
                case XmlPullParser.START_TAG:
                    eltName = parser.getName();

                    //on trouve le bon mob
                    if(eltName.equals("Mob") && parser.getAttributeCount()>1 && parser.getAttributeValue(1).equals(nomMob) ) {
                        itemFound = true;

                        hpMax= Integer.parseInt(parser.getAttributeValue(3));
                        sizeX= Integer.parseInt(parser.getAttributeValue(4));
                        sizeY= Integer.parseInt(parser.getAttributeValue(5));
                        posX= Integer.parseInt(parser.getAttributeValue(6));
                        posY= Integer.parseInt(parser.getAttributeValue(7));
                        damage= Integer.parseInt(parser.getAttributeValue(8));
                        maxMotifSameTime= Integer.parseInt(parser.getAttributeValue(9));
                        motifCDReduction= Float.parseFloat(parser.getAttributeValue(10));
                        musiqueMob= parser.getAttributeValue(11);

                        this.controller.getSoundManager().setSound( GameView.mapSound.get(musiqueMob) );

                        Vector2 positionMob = Drawer.getPosFromPerc(new Vector2(posX, posY));
                        Vector2 sizeMob = new Vector2(Drawer.getValFromVal(sizeX), Drawer.getValFromVal(sizeY));
                        Vector2 directionMob = new Vector2(0, 1);
                        int teamMob = 1;
                        Sprite spriteMob = GameView.sprites.get(nomMob);
                        Animator animatorMob = new Animator(spriteMob, 300);
                        this.mob = new Mob(entities, sizeMob, hpMax, 0, 0, new Vector2(positionMob.x-sizeMob.x/2,positionMob.y-sizeMob.y/2), directionMob, teamMob, damage, maxMotifSameTime, animatorMob);

                    }else if(eltName.equals("Mob") && itemFound==true){
                        done=true;
                    }

                    if(itemFound==true && eltName.equals("Motif")) {
                        motifsMob.add( parser.getAttributeValue(1) );
                    }
            }
            eventType = parser.next();
        }


        for(String nomMotif : motifsMob){
            this.generateMotifMob(nomMotif,motifCDReduction);
        }


        //si le mob n'a pas été trouvé on arrete le jeu
        if(this.mob==null){
            this.stopp();
            Log.e("INFO","Failed to generate the mob:"+nomMob);
        }
    }

    private void generateMotifMob(String nomMotif,float motifCDReduction){
        XmlPullParserFactory parserFactory;
        try {
            parserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserFactory.newPullParser();
            InputStream is = this.controller.getXmlData("Motifs.xml");
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
            parser.setInput(is, null);

            processMotif(parser,nomMotif,motifCDReduction);

        } catch (XmlPullParserException e) {

        } catch (IOException e) {
        }
    }


    private void processMotif(XmlPullParser parser,String nomMotif,float motifCDReduction) throws IOException, XmlPullParserException{

        boolean done=false;
        boolean itemFound=false;

        long procConditionTime;
        float procConditionPercentageHp;
        long duration;
        int motifID;
        int motifWeight;

        Motif motif=null;


        int eventType = parser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT && done==false) {
            String eltName = null;

            switch (eventType) {
                case XmlPullParser.START_TAG:
                    eltName = parser.getName();

                    //on trouve le bon motif
                    if(eltName.equals("Motif") && parser.getAttributeCount()>1 && parser.getAttributeValue(1).equals(nomMotif) ) {
                        itemFound = true;

                        motifID= Integer.parseInt(parser.getAttributeValue(0));
                        procConditionPercentageHp= Float.parseFloat(parser.getAttributeValue(2));
                        procConditionTime= Long.parseLong(parser.getAttributeValue(3));
                        duration= Long.parseLong(parser.getAttributeValue(4));
                        motifWeight= Integer.parseInt(parser.getAttributeValue(5));

                        motif=new Motif(procConditionPercentageHp,procConditionTime,this.mob.team,(long)(duration*motifCDReduction),motifID,motifWeight);


                    }else if(eltName.equals("Motif") && itemFound==true){
                        done=true;
                    }

                    if(itemFound==true && eltName.equals("BulletGenerator")) {

                        float sizex=Float.parseFloat(parser.getAttributeValue(0));
                        float sizey=Float.parseFloat(parser.getAttributeValue(1));
                        float posx=Float.parseFloat(parser.getAttributeValue(2));
                        float posy=Float.parseFloat(parser.getAttributeValue(3));
                        float angle=Float.parseFloat(parser.getAttributeValue(4));
                        long timetogo=Long.parseLong(parser.getAttributeValue(5));
                        int range=Integer.parseInt(parser.getAttributeValue(6));
                        int speed=Integer.parseInt(parser.getAttributeValue(7));
                        String nameSprite=parser.getAttributeValue(8);
                        long timeperanim=Long.parseLong(parser.getAttributeValue(9));
                        int damage=Integer.parseInt(parser.getAttributeValue(10));

                        motif.addBulletGenerator(new Vector2(sizex,sizey),new Vector2(posx,posy),Vector2.getVector2NormDirFromAngle(angle+180),timetogo,range,speed,nameSprite,timeperanim,damage );

                    }
            }
            eventType = parser.next();
        }



        if(motif!=null){
            this.mob.motifs.add(motif);
        }else{
            Log.e("INFO","Failed to generate one motif:"+nomMotif);
        }


    }




    public void stopp(){
        this.isPlaying=false;
    }
    public void pause(){
        this.isPaused=true;
    }
    public void resumee(){
        this.isPaused=false;
    }



}
