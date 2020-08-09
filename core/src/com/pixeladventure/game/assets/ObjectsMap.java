package com.pixeladventure.game.assets;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class ObjectsMap extends Sprite {

    private Assets.AssetType assetType;

    public ObjectsMap(Texture te , Assets.AssetType assetType){
        super(te);
        this.assetType = assetType;
    }

    public Assets.AssetType getAssetType() {
        return assetType;
    }

    public void setAssetType(Assets.AssetType assetType) {
        this.assetType = assetType;
    }
}
