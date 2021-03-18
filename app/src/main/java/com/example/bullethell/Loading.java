package com.example.bullethell;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.bullethell.Controller.LoadingController;

public class Loading extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        long durationSplashScreen=8000;
        LoadingController controller = new LoadingController(this,durationSplashScreen);
        setContentView(controller.getView());
        controller.start();
    }

    public void goToMainMenu(){         //essayer de supprimer l'animation de fin d'activité
        Intent intent = new Intent (this, MainMenu.class);
        startActivity(intent);
        finish();
    }





}