package com.pixeladventure.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.pixeladventure.game.PixelAdventure;
import com.pixeladventure.game.assets.Assets;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;

public class MenuScreen implements Screen {

    private Stage stage;
    private AssetManager manager;
    private Sprite backgroundSprite;

    private Sound soundButton;


    public MenuScreen(Game game , AssetManager assetManager){

        final Game aGame = game;
        this.manager = assetManager;

        backgroundSprite = Assets.getBackground(manager);
        backgroundSprite.setSize(Gdx.graphics.getWidth() , Gdx.graphics.getHeight());
        stage = new Stage(new ScreenViewport());
        Assets.playMusic(Assets.menuMusic, manager , true);

        soundButton = manager.get(Assets.buttonClickSound, Sound.class);


        TextButton startGame = new TextButton("Start game" , PixelAdventure.gameSkin);
        startGame.setWidth(Gdx.graphics.getWidth() / 3);
        startGame.setHeight(Gdx.graphics.getHeight() / 6);
        startGame.getLabel().setFontScale(0.005f * startGame.getWidth() , 0.02f * startGame.getHeight());
        startGame.setPosition(Gdx.graphics.getWidth() / 3,Gdx.graphics.getHeight() / 1.8f);
        startGame.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {

                Assets.stopMusic(Assets.menuMusic, manager);
                soundButton.play();
                aGame.setScreen(new TransitionScreen(aGame , new GameScreen(aGame , manager)));
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        stage.addActor(startGame);

        TextButton options = new TextButton("Options" , PixelAdventure.gameSkin);
        options.setWidth(Gdx.graphics.getWidth() / 3);
        options.setHeight(Gdx.graphics.getHeight() / 6);
        options.getLabel().setFontScale(0.005f * options.getWidth() , 0.02f * options.getHeight());
        options.setPosition(Gdx.graphics.getWidth() / 3,Gdx.graphics.getHeight() / 2.8f);
        options.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                soundButton.play();
                aGame.setScreen(new TransitionScreen(aGame , new OptionScreen(aGame , manager)));
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        stage.addActor(options);

        TextButton quit = new TextButton("Quit" , PixelAdventure.gameSkin);
        quit.setWidth(Gdx.graphics.getWidth() / 3);
        quit.setHeight(Gdx.graphics.getHeight() / 6);
        quit.getLabel().setFontScale(0.005f * quit.getWidth() , 0.02f * quit.getHeight());
        quit.setPosition(Gdx.graphics.getWidth() / 3,Gdx.graphics.getHeight() / 6f);
        quit.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                soundButton.play();
                dispose();
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        stage.addActor(quit);

    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        stage.getRoot().getColor().a = 0;
        stage.getRoot().addAction(fadeIn(0.3f));
    }

    @Override
    public void render(float delta) {

        //Clear all previous frames
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        stage.getBatch().begin();
        backgroundSprite.draw(stage.getBatch());
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

        stage.dispose();
        manager.dispose();
        soundButton.dispose();
        Gdx.app.exit();
        System.exit(0);
    }
}
