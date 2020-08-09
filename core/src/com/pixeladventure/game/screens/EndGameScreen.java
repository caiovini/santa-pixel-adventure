package com.pixeladventure.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.pixeladventure.game.PixelAdventure;
import com.pixeladventure.game.assets.Assets;

public class EndGameScreen implements Screen {

    private Stage stage;
    private Sound soundButton;
    private BitmapFont messageFont;


    private final String STAGE_COMPLETE = "Game complete ! Thanks for playing";

    public EndGameScreen(Game game , final AssetManager manager){

        final Game aGame = game;

        stage = new Stage(new ScreenViewport());
        soundButton = manager.get(Assets.buttonClickSound, Sound.class);

        messageFont = new BitmapFont();
        messageFont.setColor(Color.YELLOW);
        messageFont.getData().setScale(4);

        TextButton menuButton = new TextButton("Menu" , PixelAdventure.gameSkin);
        menuButton.setWidth(Gdx.graphics.getWidth()/5);
        menuButton.setHeight(Gdx.graphics.getHeight()/7);
        menuButton.getLabel().setFontScale(0.005f * menuButton.getWidth() , 0.02f * menuButton.getHeight());
        menuButton.setPosition(menuButton.getWidth() / 4, menuButton.getHeight() / 2);
        menuButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {

            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {

                soundButton.play();
                aGame.setScreen(new TransitionScreen(aGame , new MenuScreen(aGame , manager)));
                return true;
            }
        });
        stage.addActor(menuButton);


    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(.1f, .12f, .16f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.getBatch().begin();
        messageFont.draw(stage.getBatch() , STAGE_COMPLETE , Gdx.graphics.getWidth() / 3f , Gdx.graphics.getHeight() / 2);
        stage.getBatch().end();

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
