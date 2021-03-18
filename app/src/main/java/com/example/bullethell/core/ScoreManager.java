package com.example.bullethell.core;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ScoreManager {

    public static java.util.List<Score> getBestScores(Context context){
        java.util.List<Score> bestScores = new ArrayList<Score>();

        File newXml = new File(context.getFilesDir()+"/Scores.xml");
        if(!newXml.exists()){
           return bestScores;
        }

        XmlPullParserFactory parserFactory;
        try {

            parserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserFactory.newPullParser();
            InputStream is = context.openFileInput("Scores.xml");
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
            parser.setInput(is, null);


            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String eltName = null;

                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        eltName = parser.getName();

                        if(eltName.equals("Niveau")) {

                            String name=parser.getAttributeValue(0);
                            String score=parser.getAttributeValue(1);
                            String user=parser.getAttributeValue(2);

                            bestScores.add(new Score(user,name,score) );

                        }

                }
                eventType = parser.next();
            }


        } catch (XmlPullParserException e) {

        } catch (IOException e) {
        }

        return bestScores;

    }

    public static Score getBetterScore(java.util.List<Score> bestScores,Score score){
        Score better=null;
        for(Score s : bestScores){
            if(s.niveau.equals(score.niveau) && s.score>score.score){
                better=s;
            }
        }
        return better;
    }

    public static void uploadScore(Context context,String niveauName,String score,String user){
        java.util.List<Score> bestScores;
        Score playerScore=new Score(user,niveauName,score);

        bestScores=getBestScores(context);

        Score betterScore=getBetterScore( bestScores,playerScore);

        if( betterScore!=null )return; //on leave si le score n'est pas meilleur, on ne touche pas au fichier score

        java.util.List<Score> temp=new ArrayList<Score>();
        temp.addAll(bestScores);
        for(Score s : temp){
            if(s.niveau.equals(playerScore.niveau)){
                bestScores.remove(s);
            }
        }

        bestScores.add(playerScore);

        //boolean fileExist=false;
        try {

            /*File newXml = new File(context.getFilesDir()+"/Scores.xml");
            if(newXml.exists()){
                fileExist=true;
            }*/

            FileOutputStream fos = context.openFileOutput("Scores.xml", Context.MODE_PRIVATE);
            XmlSerializer serializer = Xml.newSerializer();
            serializer.setOutput(fos, "UTF-8");
            //if(!fileExist) {
                serializer.startDocument(null, Boolean.valueOf(true));
                serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
                serializer.startTag(null, "Root");
            //}

            for(Score s : bestScores) {
                serializer.startTag(null, "Niveau");
                serializer.attribute(null, "Name", s.niveau);
                serializer.attribute(null, "Score", s.score+"");
                serializer.attribute(null, "User", s.user);
                serializer.endTag(null, "Niveau");
            }

            serializer.endDocument();
            serializer.flush();

            fos.close();

        }catch (IOException e) {
        }

    }


}
