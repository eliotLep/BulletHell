package com.example.bullethell.View;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bullethell.Niveau;
import com.example.bullethell.core.LevelInfos;

public class LevelLayout extends LinearLayout {

    private ImageButton imageButtonMob;
    private TextView textMob;
    private LevelInfos levelInfos;
    private ChoiceLevelView choiceLevelView;

    private OnClickListener imageButtonMobListener = new OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent intent = new Intent (v.getContext(), Niveau.class);
            intent.putExtra("EXTRA_MOB_NAME", levelInfos.mob);
            intent.putExtra("EXTRA_NIVEAU_NAME", levelInfos.niveau);
            choiceLevelView.choiceLevel.startActivity(intent);
            choiceLevelView.choiceLevel.finish();
        }
    };



    public LevelLayout(Context context, ChoiceLevelView choiceLevelView, LevelInfos levelInfos) {
        super(context);
        this.setOrientation(HORIZONTAL);
        this.setBackgroundColor(Color.LTGRAY);

        this.choiceLevelView = choiceLevelView;
        this.levelInfos = levelInfos;

        this.imageButtonMob=new ImageButton(context);
        this.imageButtonMob.setOnClickListener(imageButtonMobListener);

        AssetManager manager=context.getAssets();
        Bitmap bitmap = Drawer.getBitmap(manager,this.levelInfos.imageNiveau);
        bitmap = Bitmap.createScaledBitmap(bitmap, 75, 110, false);
        this.imageButtonMob.setImageBitmap( bitmap );


        this.textMob=new TextView(context);
        this.textMob.setText("["+this.levelInfos.niveau+"]\n"+this.levelInfos.score+"\n"+this.levelInfos.user);
        this.textMob.setTextColor(Color.BLACK);
        this.textMob.setGravity(Gravity.CENTER);

        this.addView(imageButtonMob);
        this.addView(textMob);
    }





}
