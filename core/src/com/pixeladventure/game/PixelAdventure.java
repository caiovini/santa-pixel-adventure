package com.pixeladventure.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.pixeladventure.game.assets.Assets;
import com.pixeladventure.game.screens.MenuScreen;
import com.pixeladventure.game.screens.TransitionScreen;

public class PixelAdventure extends Game {

	static public Skin gameSkin;
	private AssetManager assetManager;
	private ProgressBar progressBar;

	private Stage stage;
	private String message = "Loading...";
	private BitmapFont messageFont;

	private boolean didCall = false;
	
	@Override
	public void create () {
		Assets assets = new Assets();
		assetManager = new AssetManager();
		assets.loadSkin(assetManager);
		gameSkin = assets.getSkin(assetManager);

		messageFont = new BitmapFont();
		messageFont.setColor(Color.WHITE);
		messageFont.getData().setScale(4);

		progressBar = new ProgressBar( 0 , 1 , 0.0005f , false , gameSkin);
		progressBar.setPosition(0 , Gdx.graphics.getHeight() / 6);
		progressBar.setWidth(Gdx.graphics.getWidth());
		stage = new Stage(new ScreenViewport());
		stage.addActor(progressBar);

		assets.loadAssets(assetManager);
	}

	@Override
	public void render () {
		super.render();

		if (assetManager.update()) {
			if(!didCall)
				this.setScreen(new TransitionScreen(this , new MenuScreen(this , assetManager)));
			didCall = true;
		}else {

			stage.getBatch().begin();
			messageFont.draw(stage.getBatch() , message , 20 , progressBar.getY() + 80);
			stage.getBatch().end();
			progressBar.setValue(assetManager.getProgress());

			stage.act();
			stage.draw();
		}
	}

	@Override
	public void dispose () {
		stage.dispose();
	}
}
