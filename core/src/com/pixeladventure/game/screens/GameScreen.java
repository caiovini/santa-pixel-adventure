package com.pixeladventure.game.screens;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.pixeladventure.game.PixelAdventure;
import com.pixeladventure.game.assets.Assets;
import com.pixeladventure.game.assets.ObjectsMap;
import com.pixeladventure.game.assets.Snowball;
import com.pixeladventure.game.preferences.GameSettings;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the main class of the game, it is responsible for processing all maps,
 * it has validation for all defined collisions and asserts directions where player
 * is or is not allowed to go.
 *
 * @author      Caio Vinicius
 * @since       1.0
 */
public class GameScreen extends ApplicationAdapter implements Screen {

    private Game game;
    private Sprite backgroundSprite;
    private List<ObjectsMap> map;

    private Stage stage;

    private OrthographicCamera camera;
    private Touchpad touchpad;

    private TextButton slide;
    private TextButton up;
    private TextButton run;
    private TextButton restart;
    private TextButton menu;


    private Sound soundJump;
    private Sound soundSlide;
    private Sound soundDead;
    private Sound victorySound;

    private Animation walkAnimation;
    private Animation idleAnimation;
    private Animation jumpAnimation;
    private Animation runAnimation;
    private Animation slideAnimation;
    private Animation deadAnimation;
    private Animation snowballAnimation;

    private float stateTime;
    private float runTime;
    private float speed;

    private float playerInitialX = 330f;
    private float playerInitialY = 0f;

    private float playerHeightDefault = 250f;
    private float playerWidthDefault = 350f;

    private float playerPositionX = playerInitialX;
    private float playerPositionY = playerInitialY;

    private enum positionPlayer { LEFT , RIGHT };
    private positionPlayer position = positionPlayer.RIGHT;
    private boolean jump = false;

    private float jumpState = 0f;
    private float slideState = 0f;
    private float snowballState = 0f;
    private float deadState = 0f;

    private Rectangle playerRectangle;

    private boolean isCollisionTile = false;
    private boolean isCollisionIceBox = false;
    private boolean isCollisionIceCrate = false;
    private boolean isCollisionWater = false;
    private boolean isRun = false;
    private boolean isSlide = false;
    private boolean moveStage = false;

    private float jumpHeight = 0f;
    private float volEffect = 0f;

    private ShapeRenderer shapeRenderer;
    private boolean canItGoToTheRight = true;
    private boolean canItGoToTheLeft = true;
    private boolean isDead = false;
    private boolean isPlayerAboveObject = false;
    private boolean isStageComplete = false;

    private final String STAGE_COMPLETE = "Stage complete !";
    private BitmapFont messageFont;

    private float w = 0f;
    private float h = 0f;

    private AssetManager manager;

    /**
     * Class constructor
     *
     * @param game          the main object comes from PixelAdventure.class
     * @param assetManager  Object containing all assets previously loaded, may contain musics, sounds, tiles and objects
     */

    public GameScreen(final Game game , AssetManager assetManager){

        w = Gdx.graphics.getWidth();
        h = Gdx.graphics.getHeight();

        manager = assetManager;

        messageFont = new BitmapFont();
        messageFont.setColor(Color.YELLOW);
        messageFont.getData().setScale(4);

        Texture[] walkFrames = Assets.getPlayerWalk(manager);
        Texture[] idleFrames = Assets.getPlayerIdle(manager);
        Texture[] jumpFrames = Assets.getPlayerJump(manager);
        Texture[] runFrames = Assets.getPlayerRun(manager);
        Texture[] slideFrames = Assets.getPlayerSlide(manager);
        Texture[] deadFrames = Assets.getPlayerDead(manager);
        Texture[] snowballFrames = Assets.getSnowBalls(manager);
        walkAnimation = new Animation(0.04f , walkFrames);  // initialize the animation class
        idleAnimation = new Animation(0.04f , idleFrames);
        runAnimation = new Animation(0.04f , runFrames);
        jumpAnimation = new Animation(0.1f , jumpFrames);
        slideAnimation = new Animation(0.1f , slideFrames);
        deadAnimation = new Animation(0.1f , deadFrames);
        snowballAnimation = new Animation(0.05f , snowballFrames);
        stateTime = 0f;
        runTime = 0f;
        speed = 0f;

        GameSettings gameSettings = new GameSettings();
        volEffect = gameSettings.isContainsVolumeEffect() ? gameSettings.getVolume(GameSettings.VOLUME_EFFECT) : 0.5f;


        int levelStage;
        if(gameSettings.isContainsStage()){

            levelStage = gameSettings.getStage(GameSettings.STAGE);
        }else{

            levelStage = 1;
            gameSettings.saveStage(GameSettings.STAGE , levelStage);
        }

        switch (levelStage){

            case 1:
                map = Assets.getSpritesMap(manager , Assets.map1);
                break;

            case 2:
                map = Assets.getSpritesMap(manager , Assets.map2);
                break;

            case 3:
                map = Assets.getSpritesMap(manager , Assets.map3);
                break;

        }

        soundJump = manager.get(Assets.jumpSound , Sound.class);
        soundSlide = manager.get(Assets.slideSound , Sound.class);
        soundDead = manager.get(Assets.deadSound , Sound.class);
        victorySound = manager.get(Assets.victorySound , Sound.class);

        Assets.playMusic(Assets.gameMusic, manager , true);

        this.game = game;
        shapeRenderer = new ShapeRenderer();

        stage = new Stage(new ScreenViewport());
        playerPositionY = h;
        
        backgroundSprite = Assets.getBackground(manager);
        backgroundSprite.setSize(w , h);
        camera = new OrthographicCamera();

        touchpad = new Touchpad(0f , PixelAdventure.gameSkin);
        touchpad.setBounds(touchpad.getWidth() / 3 , touchpad.getWidth() / 3 , w * 0.105f , h * 0.209f);
        touchpad.addListener(new InputListener(){

            @Override
            public boolean mouseMoved(InputEvent event, float x, float y) {
                super.mouseMoved(event , x , y);
                return true;
            }

            @Override
            public boolean scrolled(InputEvent event, float x, float y, int amount) {
                super.scrolled(event , x , y , amount);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                speed = 0;
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        stage.addActor(touchpad);

        slide = new TextButton("S" , PixelAdventure.gameSkin);
        slide.setWidth(w / 16);
        slide.setHeight(h / 8);
        slide.getLabel().setFontScale(0.03f * slide.getWidth() , 0.02f * slide.getHeight());
        slide.setPosition(w - slide.getWidth() * 1.5f , slide.getHeight() / 2);
        slide.addListener(new InputListener(){

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                if ((isCollisionIceBox || isCollisionTile || isCollisionIceCrate) && !isStageComplete && !isSlide && !isDead)  {
                    soundSlide.play(volEffect);
                    isSlide = true;
                }
                return true;
            }
        });
        stage.addActor(slide);

        run = new TextButton("R" , PixelAdventure.gameSkin);
        run.setWidth(w / 16);
        run.setHeight(h / 8);
        run.getLabel().setFontScale(0.03f * run.getWidth() , 0.02f * run.getHeight());
        run.setPosition( slide.getX() - run.getWidth() * 1.5f ,run.getHeight() / 2);
        run.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                isRun = false;
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                isRun = true;
                return true;
            }
        });
        stage.addActor(run);

        up = new TextButton("J" , PixelAdventure.gameSkin);
        up.setWidth(w / 16);
        up.setHeight(h / 8);
        up.getLabel().setFontScale(0.03f * up.getWidth() , 0.02f * up.getHeight());
        up.setPosition(run.getX() - up.getWidth() * 1.5f ,up.getHeight() / 2);
        up.addListener(new InputListener(){

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {

                if ((isCollisionIceBox || isCollisionTile || isCollisionIceCrate) && !isStageComplete && !isDead) {
                    soundJump.play(volEffect);
                    jump = true;
                }
                return true;
            }
        });
        stage.addActor(up);

        restart = new TextButton("Restart" , PixelAdventure.gameSkin);
        restart.setWidth(w / 4);
        restart.setHeight(h / 6);
        restart.getLabel().setFontScale(0.03f * up.getWidth() , 0.02f * up.getHeight());
        restart.setPosition(w / 2.5f ,h / 2.8f);
        restart.addListener(new InputListener(){

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {

                game.setScreen(new GameScreen(game , manager));
                return true;
            }
        });
        stage.addActor(restart);
        restart.setVisible(false);

        menu = new TextButton("<-" , PixelAdventure.gameSkin);
        menu.setWidth(w / 16);
        menu.setHeight(h / 8);
        menu.getLabel().setFontScale(0.03f * menu.getWidth() , 0.02f * menu.getHeight());
        menu.setPosition(menu.getWidth() / 2 ,h - menu.getHeight() * 1.5f);
        menu.addListener(new InputListener(){

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {

                Assets.stopMusic(Assets.gameMusic, manager);
                game.setScreen(new TransitionScreen(game , new MenuScreen( game , manager)));
                return true;
            }
        });
        stage.addActor(menu);

    }

    @Override
    public void create() {
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
    }


    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(.05f, .10f, .12f, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        stage.getBatch().begin();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        backgroundSprite.draw(stage.getBatch());
        if (touchpad.getKnobPercentX() > 0.2) speed = (isRun && (isCollisionIceBox || isCollisionTile || isCollisionIceCrate)) ? 700 * 2f : 700;
        if (touchpad.getKnobPercentX() < -0.2) speed = (isRun && (isCollisionIceBox || isCollisionTile || isCollisionIceCrate)) ? -700 * 2f : -700;

        stateTime += Gdx.graphics.getDeltaTime();
        runTime += Gdx.graphics.getDeltaTime();
        snowballState += Gdx.graphics.getDeltaTime();
        if(stateTime > walkAnimation.getAnimationDuration()) stateTime -= walkAnimation.getAnimationDuration();
        if(runTime > runAnimation.getAnimationDuration()) runTime -= runAnimation.getAnimationDuration();
        if(snowballState > snowballAnimation.getAnimationDuration()) snowballState -= snowballAnimation.getAnimationDuration();

        Texture currentWalkFrame = (Texture) walkAnimation.getKeyFrame(stateTime);
        Texture currentIdleFrame = (Texture) idleAnimation.getKeyFrame(stateTime);
        Texture currentRunFrame = (Texture) runAnimation.getKeyFrame(runTime);
        Texture currentJumpFrame = (Texture) jumpAnimation.getKeyFrame(jumpState);
        Texture currentSlideFrame = (Texture) slideAnimation.getKeyFrame(slideState);
        Texture currentDeadFrame = (Texture) deadAnimation.getKeyFrame(deadState);
        Texture currentSnowballFrame = (Texture) snowballAnimation.getKeyFrame(snowballState);

        Sprite playerSprite = new Sprite(currentIdleFrame);
        playerRectangle = playerSprite.getBoundingRectangle();
        float additionalX;
        if(position.equals(positionPlayer.RIGHT)){
            additionalX = playerWidthDefault * 0.2f;
        }else{
            additionalX = playerWidthDefault * 0.4f;
        }
        playerRectangle.setPosition(playerPositionX + additionalX, playerPositionY + 30);
        playerRectangle.setHeight(currentIdleFrame.getHeight() - playerHeightDefault * 1.7f);
        if (isSlide) playerRectangle.setHeight((currentIdleFrame.getHeight() - playerHeightDefault * 1.7f) / 2);
        playerRectangle.setWidth(currentIdleFrame.getWidth() - playerWidthDefault * 2.3f);

        //shapeRenderer.rect(playerRectangle.getX() , playerRectangle.getY() , playerRectangle.getWidth() , playerRectangle.getHeight());
        isCollisionTile = false;
        isCollisionIceBox = false;
        isCollisionIceCrate = false;
        isPlayerAboveObject = false;
        moveStage = false;

        List<ObjectsMap> aux = filterMap(map);
        canItGoToTheRight = true;
        canItGoToTheLeft = true;
        for (ObjectsMap s : aux) {

            s.draw(stage.getBatch());
            if (s.getAssetType().equals(Assets.AssetType.TILE) || s.getAssetType().equals(Assets.AssetType.ICE_BOX) ||
                 s.getAssetType().equals(Assets.AssetType.CRATE) || s.getAssetType().equals(Assets.AssetType.WATER) ||
                    s.getAssetType().equals(Assets.AssetType.SIGN)) {

                Rectangle tilesRectangle = s.getBoundingRectangle();
                tilesRectangle.setPosition(s.getX(), s.getY());
                tilesRectangle.setHeight(s.getHeight());
                tilesRectangle.setWidth(s.getWidth());
                //shapeRenderer.rect(tilesRectangle.getX() , tilesRectangle.getY() , tilesRectangle.getWidth() , tilesRectangle.getHeight());

                if (Intersector.overlaps(tilesRectangle, playerRectangle)) {
                    float marginTile = s.getHeight() * 0.15f; // 15% Considered
                    float marginPlayer = playerRectangle.getHeight() * 0.15f; // 15% Considered
                    float playerHeight = playerRectangle.getY() + playerRectangle.getHeight();
                    float tileHeight = s.getY() + s.getHeight();

                    if(s.getAssetType().equals(Assets.AssetType.ICE_BOX) ||
                            s.getAssetType().equals(Assets.AssetType.CRATE)) {
                        isCollisionIceBox = true;
                        isCollisionIceCrate = true;

                        // Player is underneath
                        if(playerRectangle.getY() < tileHeight - marginTile) {

                            if (jump && playerHeight - marginPlayer < s.getY()) {

                                // Keep falling
                                jump = false;
                                jumpState = 0;
                                isCollisionIceBox = false;
                                isCollisionIceCrate = false;
                            } else {


                                if (playerRectangle.getX() < s.getX()) {
                                    canItGoToTheRight = false;
                                    canItGoToTheLeft = true;
                                } else {
                                    canItGoToTheRight = true;
                                    canItGoToTheLeft = false;
                                }
                            }
                        }else{
                            isPlayerAboveObject = true;
                        }
                    }else if(
                        s.getAssetType().equals(Assets.AssetType.TILE)){

                        if (playerRectangle.getY() + (h * 0.01) > tileHeight)
                            isCollisionTile = true;

                    }else if(s.getAssetType().equals(Assets.AssetType.WATER)) {

                        isCollisionWater = false;
                        isCollisionIceBox = true;
                        isCollisionIceCrate = true;


                        if (Intersector.overlaps(tilesRectangle, playerRectangle)) {
                            if (!isDead) {
                                restart.setVisible(true);
                                Assets.stopMusic(Assets.gameMusic, manager);
                                soundDead.play(volEffect);
                            }
                            isDead = true;
                        }


                    }else if(s.getAssetType().equals(Assets.AssetType.SIGN)){

                        if (!isStageComplete) {
                            Assets.stopMusic(Assets.gameMusic, manager);
                            victorySound.play(volEffect);
                            loadNextLevel();
                        }
                        messageFont.draw(stage.getBatch() , STAGE_COMPLETE , w / 2.2f , h / 2);
                        isStageComplete = true;

                    }

                    if ((isCollisionIceBox || isCollisionTile || isCollisionIceCrate) && !jump) jumpHeight = playerRectangle.getY();

                }
            }else if(s.getAssetType().equals(Assets.AssetType.SNOWMAN)){

                Rectangle tilesRectangle = s.getBoundingRectangle();
                tilesRectangle.setPosition(s.getX() + s.getWidth() / 2.8f , s.getY());
                tilesRectangle.setHeight(s.getHeight());
                tilesRectangle.setWidth(s.getWidth() / 4);
                //shapeRenderer.rect(tilesRectangle.getX() , tilesRectangle.getY() , tilesRectangle.getWidth() , tilesRectangle.getHeight());

                if (Intersector.overlaps(tilesRectangle, playerRectangle)) {
                    if(!isDead) {
                        restart.setVisible(true);
                        Assets.stopMusic(Assets.gameMusic, manager);
                        soundDead.play(volEffect);
                    }
                    isDead = true;
                }
            }
        }

        if((!canItGoToTheLeft || !canItGoToTheRight) && !isPlayerAboveObject ){

            // Keep falling
            isCollisionIceBox = false;
            isCollisionIceCrate = false;
        }

        if(playerRectangle.getX() < 0){

            canItGoToTheLeft = false;
        }

        for(Snowball b : Assets.snowball){

            Circle snowBallCircle = new Circle();

            if(b.getPosX() < - currentSnowballFrame.getWidth() * 2) b.setPosX(b.getOriginalPositionX());

            if(b.getPosX() < w + currentSnowballFrame.getWidth()) {
                b.setPosX(b.getPosX() - currentSnowballFrame.getWidth() * 0.03f); // Speed of the balls being thrown
                snowBallCircle.set(b.getPosX() + currentSnowballFrame.getHeight() / 6f, b.getPosY() + currentSnowballFrame.getWidth() / 6f, currentSnowballFrame.getHeight() / 8);
                //shapeRenderer.circle(snowBallCircle.x , snowBallCircle.y , snowBallCircle.radius );

                stage.getBatch().draw(currentSnowballFrame, b.getPosX(), b.getPosY(), currentSnowballFrame.getWidth() / 2, currentSnowballFrame.getHeight() / 2);

                if (Intersector.overlaps(snowBallCircle, playerRectangle)) {


                    if (!isDead) {
                        restart.setVisible(true);
                        Assets.stopMusic(Assets.gameMusic, manager);
                        soundDead.play(volEffect);
                        jump = false;
                        jumpState = 0;
                        isCollisionIceBox = false;
                        isCollisionIceCrate = false;
                    }
                    isDead = true;

                }

            }
        }

        if (jump){
            if (playerPositionY > jumpHeight + 300){ // Distance to jump up
                jump = false;
                jumpState = 0;
            } else {
                if (jumpState < jumpAnimation.getKeyFrames().length){
                    jumpState += 0.05 ; //Animation time
                }
                playerPositionY += currentJumpFrame.getWidth() / 80;
            }
        }else{
            if (!isCollisionTile && !isCollisionIceBox && !isCollisionIceCrate)
                playerPositionY -= currentJumpFrame.getWidth() / 80;

            if(playerPositionY < - currentIdleFrame.getHeight() && !isDead) {
                restart.setVisible(true);
                Assets.stopMusic(Assets.gameMusic, manager);
                soundDead.play(volEffect);
                isDead = true;
            }

        }

        if(isStageComplete) speed = 0;

        if(!isDead) {
            if (speed > 200) {
                if (canItGoToTheRight && !moveStage && !isCollisionWater)
                    playerPositionX += speed / ((Texture) idleAnimation.getKeyFrame(0f)).getWidth() * 10;
                if (jump) {
                    stage.getBatch().draw(currentJumpFrame, playerPositionX, playerPositionY, playerWidthDefault, playerHeightDefault);
                } else {
                    if (isSlide) {
                        slideState += 0.5;
                        stage.getBatch().draw(currentSlideFrame, playerPositionX, playerPositionY, playerWidthDefault, playerHeightDefault);
                        if (slideState > slideAnimation.getKeyFrames().length) {
                            isSlide = false;
                            slideState = 0;
                        }
                    } else {
                        if (isRun) {
                            stage.getBatch().draw(currentRunFrame, playerPositionX, playerPositionY, playerWidthDefault, playerHeightDefault);
                        } else {
                            stage.getBatch().draw(currentWalkFrame, playerPositionX, playerPositionY, playerWidthDefault, playerHeightDefault);
                        }
                    }
                }
                position = positionPlayer.RIGHT;
            } else if (speed < 0) {
                Sprite spr;
                if (isSlide) {
                    slideState += 0.5;
                    spr = new Sprite(currentSlideFrame);
                    if (slideState > slideAnimation.getKeyFrames().length) {
                        isSlide = false;
                        slideState = 0;
                    }
                } else {
                    spr = jump ? new Sprite(currentJumpFrame) :
                            isRun ? new Sprite(currentRunFrame) : new Sprite(currentWalkFrame);
                }
                spr.flip(true, false);
                if (canItGoToTheLeft && !moveStage && !isCollisionWater)
                    playerPositionX += speed / ((Texture) idleAnimation.getKeyFrame(0f)).getWidth() * 10;
                stage.getBatch().draw(spr, playerPositionX, playerPositionY, playerWidthDefault, playerHeightDefault);
                position = positionPlayer.LEFT;
            } else if (speed < 200) {
                if (isSlide) {
                    slideState += 0.5;
                    if (position.equals(positionPlayer.RIGHT)) {
                        stage.getBatch().draw(currentSlideFrame, playerPositionX, playerPositionY, playerWidthDefault, playerHeightDefault);
                    } else {
                        Sprite spr = new Sprite(currentSlideFrame);
                        spr.flip(true, false);
                        stage.getBatch().draw(spr, playerPositionX, playerPositionY, playerWidthDefault, playerHeightDefault);
                    }
                    if (slideState > slideAnimation.getKeyFrames().length) {
                        isSlide = false;
                        slideState = 0;
                    }
                } else {
                    if (position.equals(positionPlayer.RIGHT)) {
                        if (jump) {
                            stage.getBatch().draw(currentJumpFrame, playerPositionX, playerPositionY, playerWidthDefault, playerHeightDefault);
                        } else {
                            stage.getBatch().draw(currentIdleFrame, playerPositionX, playerPositionY, playerWidthDefault, playerHeightDefault);
                        }
                    } else {
                        Sprite spr = jump ? new Sprite(currentJumpFrame) : new Sprite(currentIdleFrame);
                        spr.flip(true, false);
                        stage.getBatch().draw(spr, playerPositionX, playerPositionY, playerWidthDefault, playerHeightDefault);
                    }
                }
            }
        }else{
            deadState += 0.5;
            if (position.equals(positionPlayer.RIGHT)) {
                stage.getBatch().draw(currentDeadFrame, playerPositionX, playerPositionY, playerWidthDefault, playerHeightDefault);
            } else {
                Sprite spr = new Sprite(currentDeadFrame);
                spr.flip(true, false);
                stage.getBatch().draw(spr, playerPositionX, playerPositionY, playerWidthDefault, playerHeightDefault);
            }
            if (deadState > deadAnimation.getKeyFrames().length)
                slideState = deadAnimation.getKeyFrames().length;

        }
        stage.getBatch().end();
        shapeRenderer.end();

        stage.act();
        stage.draw();
    }

    /**
    Filters the amount of information that will be displayed for the player
    This allows less effort to write the map once only the essential tiles to be returned

    @param m the map currently being processed
    @return  a list of tiles to be drawn
     */

    private List<ObjectsMap> filterMap(List<ObjectsMap> m){

        boolean didMoveRight = false;

        if (isDead) return m;
        if (isStageComplete) return m;

        List<ObjectsMap> filteredMap = new ArrayList<>();
        ObjectsMap firstBlock = m.get(0);

        if (speed < 0){
            float result = firstBlock.getX() + (speed * -1) / 100;
            if(result >= 0) return m;
        }

        for (ObjectsMap map : m){

            if(playerRectangle.getX() > playerInitialX) {
                if (playerRectangle.getX() > w - (w / 4) && speed > 0 && canItGoToTheRight && !isCollisionWater) {

                    didMoveRight = true;
                    map.setX(map.getX() - speed / 100);
                    if (map.getX() > - map.getWidth() * 4)
                        filteredMap.add(map);
                } else if (playerRectangle.getX() < w / 4 && speed < 0 && canItGoToTheLeft && !isCollisionWater) {

                    map.setX(map.getX() + (speed * -1) / 100);
                    if (map.getX() < w)
                        filteredMap.add(map);
                } else {
                    return m;
                }
            }else{
                return m;
            }
        }

        // Compensate snowball
        for(Snowball b : Assets.snowball){
            if(didMoveRight){
                b.setPosX(b.getPosX() - speed / 100);
                b.setOriginalPositionX(b.getOriginalPositionX() - speed / 100);
            }else{
                b.setPosX(b.getPosX() + (speed * -1) / 100);
                b.setOriginalPositionX(b.getOriginalPositionX() + (speed * -1) / 100);
            }
        }

        // Compensate speed
        moveStage = true;
        return filteredMap;
    }

    private void loadNextLevel(){

        Timer.schedule(new Timer.Task(){
            @Override
            public void run() {
                GameSettings gameSettings = new GameSettings();
                int levelStage = gameSettings.getStage(GameSettings.STAGE);
                levelStage++;
                Gdx.app.log("level" , String.valueOf(levelStage));
                if (levelStage > Assets.STAGE_QTD){
                    levelStage = 1;
                    gameSettings.saveStage(GameSettings.STAGE, levelStage);
                    game.setScreen(new EndGameScreen(game, manager));
                }else{
                    gameSettings.saveStage(GameSettings.STAGE, levelStage);
                    game.setScreen(new TransitionScreen(game, new GameScreen(game, manager)));
                }
            }
        }, 1.5f);
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        super.dispose();
        stage.dispose();
    }
}
