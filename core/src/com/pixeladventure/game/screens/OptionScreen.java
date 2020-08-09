package com.pixeladventure.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.pixeladventure.game.PixelAdventure;
import com.pixeladventure.game.assets.Assets;
import com.pixeladventure.game.preferences.GameSettings;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;

public class OptionScreen implements Screen {

    private Stage stage;
    private Sprite backgroundSprite;

    private Sound soundButton;

    private Slider volumeMusic;
    private Slider volumeEffect;
    private GameSettings gameSettings = new GameSettings();
    private AssetManager manager;

    public OptionScreen(Game game , AssetManager assetManager){

        this.manager = assetManager;
        final Game aGame = game;

        backgroundSprite = Assets.getBackground(manager);
        backgroundSprite.setSize(Gdx.graphics.getWidth() , Gdx.graphics.getHeight());
        soundButton = manager.get(Assets.buttonClickSound, Sound.class);

        final Sound soundSlide = manager.get(Assets.slideSound , Sound.class);
        stage = new Stage(new ScreenViewport());
        float volMusic = gameSettings.isContainsVolumeMusic() ? gameSettings.getVolume(GameSettings.VOLUME_MUSIC) * 10 : 5;


        volumeMusic = new Slider(0f , 10f , 1f , false , PixelAdventure.gameSkin);
        volumeMusic.setValue(volMusic);
        Container<Slider> containerMusic = new Container<Slider>(volumeMusic);
        containerMusic.setTransform(true);
        containerMusic.setWidth(Gdx.graphics.getWidth()/5);
        containerMusic.setHeight(Gdx.graphics.getHeight()/5);
        containerMusic.setPosition(Gdx.graphics.getWidth()/2-volumeMusic.getWidth() * 3.5f,Gdx.graphics.getHeight()/4-volumeMusic.getHeight()/2);
        containerMusic.setScale(3);
        containerMusic.addListener(new ChangeListener(){

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                saveVolumeMusic(GameSettings.VOLUME_MUSIC , volumeMusic.getValue() / volumeMusic.getMaxValue());
            }

        });
        stage.addActor(containerMusic);

        Label volumeLabel = new Label("Music" , PixelAdventure.gameSkin);
        volumeLabel.setWidth(Gdx.graphics.getWidth()/14);
        volumeLabel.setHeight(Gdx.graphics.getHeight()/10);
        volumeLabel.setFontScale(0.04f * volumeLabel.getWidth() , 0.03f * volumeLabel.getHeight());
        volumeLabel.setPosition(containerMusic.getX() - volumeLabel.getWidth() / 3  , Gdx.graphics.getHeight()/2);
        stage.addActor(volumeLabel);


        final float volEffect = gameSettings.isContainsVolumeMusic() ? gameSettings.getVolume(GameSettings.VOLUME_EFFECT) * 10 : 5;

        volumeEffect = new Slider(0f , 10f , 1f , false , PixelAdventure.gameSkin);
        volumeEffect.setValue(volEffect);
        Container<Slider> containerEffect = new Container<Slider>(volumeEffect);
        containerEffect.setTransform(true);
        containerEffect.setWidth(Gdx.graphics.getWidth()/5);
        containerEffect.setHeight(Gdx.graphics.getHeight()/6);
        containerEffect.setPosition(Gdx.graphics.getWidth()/2-volumeEffect.getWidth() * 3.5f,Gdx.graphics.getHeight()/7-volumeEffect.getHeight()/2);
        containerEffect.setScale(3);
        containerEffect.addListener(new ChangeListener(){

            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {

                saveVolumeEffect(GameSettings.VOLUME_EFFECT , volumeEffect.getValue() / volumeEffect.getMaxValue());
                soundSlide.play(volumeEffect.getValue() / volumeEffect.getMaxValue());
            }
        });
        stage.addActor(containerEffect);

        Label volumeLabelEffect = new Label("Effect" , PixelAdventure.gameSkin);
        volumeLabelEffect.setWidth(Gdx.graphics.getWidth()/16);
        volumeLabelEffect.setHeight(Gdx.graphics.getHeight()/10);
        volumeLabelEffect.setFontScale(0.04f * volumeLabel.getWidth() , 0.03f * volumeLabel.getHeight());
        volumeLabelEffect.setPosition(containerEffect.getX() - volumeLabel.getWidth() / 3  , Gdx.graphics.getHeight()/3);
        stage.addActor(volumeLabelEffect);


        TextButton returnButton = new TextButton("Return" , PixelAdventure.gameSkin);
        returnButton.setWidth(Gdx.graphics.getWidth()/5);
        returnButton.setHeight(Gdx.graphics.getHeight()/7);
        returnButton.getLabel().setFontScale(0.005f * returnButton.getWidth() , 0.02f * returnButton.getHeight());
        returnButton.setPosition(returnButton.getWidth() / 4, returnButton.getHeight() / 2);
        returnButton.addListener(new InputListener(){
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
        stage.addActor(returnButton);
    }

    @Override
    public void show() {

        Gdx.input.setInputProcessor(stage);
        stage.getRoot().getColor().a = 0;
        stage.getRoot().addAction(fadeIn(0.3f));
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(.1f, .12f, .16f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.getBatch().begin();
        backgroundSprite.draw(stage.getBatch());
        stage.getBatch().end();

        stage.act();
        stage.draw();


    }

    private void saveVolumeMusic(String k , float v){

        gameSettings.saveVolume(k , v);
        Assets.setMusicVolume(Assets.menuMusic , manager , v);
    }

    private void saveVolumeEffect(String k , float v){

        gameSettings.saveVolume(k , v);
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
    }
}
