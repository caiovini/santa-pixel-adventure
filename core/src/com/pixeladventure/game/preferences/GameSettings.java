package com.pixeladventure.game.preferences;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class GameSettings {

    public static final String VOLUME_MUSIC = "volume_music";
    public static final String VOLUME_EFFECT = "volume_effect";
    public static final String STAGE = "current_stage";
    private final Preferences prefs = Gdx.app.getPreferences("Game Preferences");

    private boolean containsVolumeMusic;
    private boolean containsVolumeEffect;
    private boolean containsStage;


    public GameSettings(){

        this.containsVolumeMusic = prefs.contains(VOLUME_MUSIC);
        this.containsVolumeEffect = prefs.contains(VOLUME_EFFECT);
        this.containsStage = prefs.contains(STAGE);
    }

    public void saveVolume(String k , float v){

        prefs.putFloat(k , v);
        prefs.flush();
    }

    public float getVolume(String k){

        return prefs.getFloat(k);
    }

    public void saveStage(String stage , int s){

        prefs.putInteger(stage , s);
        prefs.flush();
    }

    public int getStage(String stage){

        return prefs.getInteger(stage);
    }

    public boolean isContainsVolumeMusic(){
        return containsVolumeMusic;
    }

    public boolean isContainsVolumeEffect(){
        return containsVolumeEffect;
    }

    public boolean isContainsStage() {
        return containsStage;
    }
}
