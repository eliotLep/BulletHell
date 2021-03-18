package com.example.bullethell;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.bullethell.View.ChoiceLevelView;
import com.example.bullethell.View.Drawer;
import com.example.bullethell.core.LevelInfos;
import com.example.bullethell.core.Score;
import com.example.bullethell.core.ScoreManager;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ChoiceLevel extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choix_niveaux);

        int backGroundColor=Color.DKGRAY;

        final GridView gridView = new GridView(this);
        gridView.setAdapter( new ChoiceLevelView(this,this,this.getNiveaux() ) );
        gridView.setNumColumns(3);
        gridView.setBackgroundColor(backGroundColor);
        gridView.setHorizontalSpacing(10);
        gridView.setVerticalSpacing(10);


        ImageView headerNiveaux=findViewById(R.id.header);
        headerNiveaux.setImageBitmap(Drawer.getBitmap(getAssets(),"headerNiveaux.png"));

        LinearLayout layoutNiveaux=findViewById(R.id.layoutNiveaux);
        layoutNiveaux.addView(gridView);
        layoutNiveaux.setBackgroundColor(backGroundColor);

        ConstraintLayout mainLayout=findViewById(R.id.layerFond);
        mainLayout.setBackgroundColor(backGroundColor);

    }

    /** Appelee apres onCreate, à utiliser pour restaurer l'etat de l'interface */
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        //Restaurer l'interface utilisateur à partir de savedInstanceState.
        //Ne sera appelee que si l'activite a ete tuee par le systême
        //depuis qu'elle a ete visible pour la dernière fois.
    }


    /**
     * Appelee avant les cycles visibles d'une activite.
     * La fonction onRestart() est suivie de la fonction onStart().
     */
    @Override
    protected void onRestart() {
        super.onRestart();
    }

    /**
     * Execute lorsque l'activité devient visible à l'utilisateur.
     * La fonction onStart() est suivie de la fonction onResume().
     */
    @Override
    protected void onStart() {
        super.onStart();

        // Récupération des anciens paramètres
        {
            SharedPreferences settings = getSharedPreferences("cycle_vie_prefs", Context.MODE_PRIVATE);

        }

    }

    /**
     * Exécutée à chaque passage en premier plan de l'activité.
     * Ou bien, si l'activité passe à nouveau en premier
     *  (si une autre activité était passée en premier plan entre temps).
     *
     * La fonction onResume() est suivie de l'exécution de l'activité.
     */
    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * La fonction onPause() est suivie :
     * - d'un onResume() si l'activité passe à nouveau en premier plan;
     * - d'un onStop() si elle devient invisible à l'utilisateur;
     *
     * L'exécution de la fonction onPause() doit être rapide,
     * car la prochaine activité ne démarrera pas tant que l'execution
     * de la fonction onPause() n'est pas terminee.
     */
    @Override
    protected void onPause() {
        super.onPause();

        // Sauvegarde des parametres
        // pour pouvoir les restaurer au prochain demarrage
        {
            SharedPreferences settings = getSharedPreferences("cycle_vie_prefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.commit();
        }

        if (isFinishing()) {
            //popUp("onPause, l'utilisateur a demande la fermeture via un finish()");
        } else {
            //popUp("onPause, l'utilisateur n'a pas demande la fermeture via un finish()");
        }
    }

    /**
     * La fonction onStop() est executee :
     * - lorsque l'activite n'est plus en premier plan
     * - ou bien lorsque l'activite va etre detruite
     *
     * Cette fonction est suivie :
     * - de la fonction onRestart() si l'activite passe à nouveau en premier plan;
     * - de la fonction onDestroy() lorsque l'activite se termine ou bien lorsque le systeme decide de l'arreter
     */
    @Override
    protected void onStop() {
        super.onStop();
    }

    /**
     * Cette fonction est executee lorsque l'activite se termine ou bien lorsque
     * le systeme decide de l'arreter.
     *
     * La fonction onCreate() devra a nouveau etre executee pour obtenir à nouveau l'activite.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }






    private java.util.List<LevelInfos> getNiveaux(){

        java.util.List<LevelInfos> niveaux=new ArrayList<LevelInfos>();
        java.util.List<Score> bestScores= ScoreManager.getBestScores(this);


        XmlPullParserFactory parserFactory;
        try {
            parserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserFactory.newPullParser();
            InputStream is = getAssets().open("Niveaux.xml");
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
            parser.setInput(is, null);


            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String eltName = null;

                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        eltName = parser.getName();

                        if(eltName.equals("Niveau")) {

                            String niveau = parser.getAttributeValue(1);
                            String mob = parser.getAttributeValue(2);
                            String imageNiveau = parser.getAttributeValue(3);
                            String name="nobody";
                            String score="0";

                            for(Score s : bestScores){
                                if( niveau.equals( s.niveau ) ){
                                    name=s.user;
                                    score=s.score+"";
                                }
                            }

                            niveaux.add( new LevelInfos(niveau,name,Float.parseFloat(score),mob,imageNiveau) );

                        }

                }
                eventType = parser.next();
            }


        } catch (XmlPullParserException e) {

        } catch (IOException e) {
        }

        return niveaux;
    }




}