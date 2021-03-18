package com.example.bullethell;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.bullethell.Controller.GameController;

public class Niveau extends AppCompatActivity {

    private GameController controller;
    private String niveauName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String nomMob=getIntent().getStringExtra("EXTRA_MOB_NAME");
        niveauName=getIntent().getStringExtra("EXTRA_NIVEAU_NAME");

        this.controller=new GameController(this,this,nomMob);

        setContentView( this.controller.getView() );
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
        this.controller.resume();
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
        this.controller.pause();

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
        this.controller.pause();
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
        this.controller.stop();
        super.onDestroy();
    }

    public void onLose(float score){
        goNextActivity(score,"Perdu :(");
    }

    public void onWin(float score){
        goNextActivity(score,"Gagné!");
    }

    private void goNextActivity(float score,String winLose){
        finish();
        Intent intent = new Intent (this, EndLevel.class);
        intent.putExtra("EXTRA_PLAYER_SCORE",score+"");
        intent.putExtra("EXTRA_NIVEAU_NAME",this.niveauName);
        intent.putExtra("EXTRA_WINLOSE_TEXT",winLose);
        startActivity(intent);
    }

}