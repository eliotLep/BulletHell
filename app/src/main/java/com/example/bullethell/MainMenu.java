package com.example.bullethell;

//import androidx.appcompat.app.AppCompatActivity;
//public class MainActivity extends AppCompatActivity {

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.bullethell.Controller.MainMenuController;


public class MainMenu extends Activity {


        MainMenuController controller;

        /**LISTENER*/

        OnClickListener btnQuitterOnClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                // la methode finish est appelee quand l'activite est finie et
                // doit être fermee
                finish();
            }
        };

        OnClickListener btnChoixNiveauxOnClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent (v.getContext(), ChoiceLevel.class);
                startActivity(intent);
                //finish();
            }
        };




        /** Creation d'un "Toast" pour afficher temporairement les informations
         * a l'ecran
         */
        public void popUp(String message) {
            Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }

        /**
         * Appel au debut du cycle complet
         */
        @SuppressLint("ResourceType")
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            //buttons
            ImageButton btnQuitter = new ImageButton(this);
            btnQuitter.setId(1);
            btnQuitter.setOnClickListener(btnQuitterOnClickListener);
            btnQuitter.setBackgroundResource(R.drawable.quitterbutton);

            ImageButton btnChoixNiveaux = new ImageButton(this);
            btnChoixNiveaux.setId(2);
            btnChoixNiveaux.setOnClickListener(btnChoixNiveauxOnClickListener);
            btnChoixNiveaux.setBackgroundResource(R.drawable.choixbutton);

            //controller
            controller = new MainMenuController(this);
            controller.getView();
            controller.start();

            //add to view
            RelativeLayout layout = new RelativeLayout(this);
            layout.addView(controller.getView());




            //constraints
            RelativeLayout.LayoutParams params0 = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            btnChoixNiveaux.setLayoutParams(params0);
            params0.addRule(RelativeLayout.CENTER_HORIZONTAL);
            params0.addRule(RelativeLayout.CENTER_VERTICAL);
            params0.setMargins(20,20,20,20);
            layout.addView(btnChoixNiveaux);

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            btnQuitter.setLayoutParams(params);
            params.addRule(RelativeLayout.BELOW, btnChoixNiveaux.getId());
            params.addRule(RelativeLayout.CENTER_HORIZONTAL);
            params.setMargins(20,20,20,20);
            layout.addView(btnQuitter);









            setContentView(layout);

        }





        /*@Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.activity_main, menu);
            return true;
        }*/

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
            this.controller.onResume();
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
            this.controller.onPause();


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
            this.controller.onPause();
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
            this.controller.onQuit();
            super.onDestroy();
        }



    }