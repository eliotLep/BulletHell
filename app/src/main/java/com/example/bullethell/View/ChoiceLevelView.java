package com.example.bullethell.View;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.bullethell.ChoiceLevel;
import com.example.bullethell.core.LevelInfos;

import java.util.ArrayList;

public class ChoiceLevelView extends BaseAdapter {

    private java.util.List<LevelLayout> levelLayouts;

    protected ChoiceLevel choiceLevel;
    private Context context;


    public ChoiceLevelView(Context context, ChoiceLevel choiceLevel, java.util.List<LevelInfos> niveaux ){

        this.context=context;
        this.choiceLevel = choiceLevel;
        this.levelLayouts =new ArrayList<LevelLayout>();

        for(LevelInfos n : niveaux){
            this.levelLayouts.add( new LevelLayout(context,this,n) );
        }
    }


    @Override
    public int getCount() {
        return this.levelLayouts.size();
    }

    @Override
    public Object getItem(int position) {
        return this.levelLayouts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return this.levelLayouts.get(position);
    }
}
