package com.pixeladventure.game.assets;

public class Snowball extends Model {

    private float originalPositionX;

    public float getOriginalPositionX() {
        return originalPositionX;
    }

    public void setOriginalPositionX(float originalPositionX) {
        this.originalPositionX = originalPositionX;
    }

    public void setPosition(float x , float y){

        super.setPosX(x);
        super.setPosY(y);
    }
}
