package com.pixeladventure.game.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.pixeladventure.game.preferences.GameSettings;

import java.util.ArrayList;
import java.util.List;

import static com.badlogic.gdx.net.HttpRequestBuilder.json;

/**
 * This class is responsible for managing all assets through out the game
 *
 * @author      Caio Vinicius
 * @since       1.0
 */

public class Assets {

    private static float idealHeight = 1440f;
    private static final int RUN_SLIDE_QTD = 11;
    private static final int JUMP_IDLE_TILES_QTD = 16;
    private static final int WALK_QTD = 13;
    private static final int DEAD_QTD = 17;
    private static final int SNOWBALL_QTD = 6;

    public enum AssetType { TREE , SIGN , TILE , ICE_BOX , CRATE , CRYSTAL , IGLOO , SNOWMAN , STONE  , WATER}

    public static ArrayList<Model> map1 = json.fromJson(ArrayList.class, Model.class, Gdx.files.internal("map/Map1.json"));
    public static ArrayList<Model> map2 = json.fromJson(ArrayList.class, Model.class, Gdx.files.internal("map/Map2.json"));
    public static ArrayList<Model> map3 = json.fromJson(ArrayList.class, Model.class, Gdx.files.internal("map/Map3.json"));

    public static final int STAGE_QTD = 3;
    public static List<Snowball> snowball;

    private static final String BG = "png/BG/BG.png";
    private static final String SKIN = "skin/freezing-ui.json";
    private static final String ATLAS = "skin/freezing-ui.atlas";
    private static final String PLAYER = "player/png/";
    private static final String SNOWBALL = "snowball/snowball_0";

    public static final String gameMusic = "sounds/Lynn Music Boulangerie - Gaming Background Music (HD).mp3";
    public static final String menuMusic = "sounds/Cinema Sins Background Song (Clowning Around) - Background Music (HD).mp3";

    public static final String buttonClickSound = "sounds/effects/zapsplat_multimedia_button_click_fast_plastic_49161.mp3";
    public static final String jumpSound = "sounds/effects/zapsplat_cartoon_boing_spring_jump_17682.mp3";
    public static final String slideSound = "sounds/effects/zapsplat_cartoon_slide_whistle_ascend_climb_very_fast_006_35475.mp3";
    public static final String deadSound = "sounds/effects/zapsplat_cartoon_descend_slide_whistle_fall_down_18055.mp3";
    public static final String victorySound = "sounds/effects/cartoon_success_fanfair.mp3";

    /**
     Reads an array of models which contains all tiles and objects to be assembled.
     Return a list of these objects

     @param manager     Object containing all assets previously loaded, may contain musics, sounds, tiles and objects of the map.
     @return            a list containing tiles and objects to be drawn
     */
    public static List<ObjectsMap> getSpritesMap(AssetManager manager , ArrayList<Model> map){

        List<ObjectsMap> sprites = new ArrayList<>();
        snowball = new ArrayList<>();
        float calcHeight = idealHeight - Gdx.graphics.getHeight();

        for (Model m : map){

            if (m.getMaterial().substring(4 , 9).equals("Tiles")){

                AssetType type = m.getMaterial().substring(10 , 15).equals("water") ? AssetType.WATER : AssetType.TILE;

                ObjectsMap tile = new ObjectsMap(manager.get(m.getMaterial() , Texture.class) , type);
                tile.setPosition(m.getPosX() , m.getPosY());
                if (calcHeight > 0) tile.setY(m.getPosY() - calcHeight / 2);
                sprites.add(tile);

            }else if(m.getMaterial().substring(11 , 15).equals("Tree")){

                ObjectsMap tree = new ObjectsMap(manager.get(m.getMaterial() , Texture.class) , AssetType.TREE);
                tree.setPosition(m.getPosX(), m.getPosY());
                if (calcHeight > 0) tree.setY(m.getPosY() - calcHeight / 2);
                sprites.add(tree);
            }else if(m.getMaterial().substring(11 , 15).equals("Sign")){

                ObjectsMap sign = new ObjectsMap(manager.get(m.getMaterial() , Texture.class) , AssetType.SIGN);
                sign.setPosition(m.getPosX(), m.getPosY());
                if (calcHeight > 0) sign.setY(m.getPosY() - calcHeight / 2);
                sprites.add(sign);
            }else if(m.getMaterial().substring(11 , 17).equals("IceBox")) {

                ObjectsMap iceBox = new ObjectsMap(manager.get(m.getMaterial() , Texture.class), AssetType.ICE_BOX);
                iceBox.setPosition(m.getPosX(), m.getPosY());
                if (calcHeight > 0) iceBox.setY(m.getPosY() - calcHeight / 2);
                sprites.add(iceBox);
            }else if(m.getMaterial().substring(11 , 16).equals("Crate")) {

                ObjectsMap crate = new ObjectsMap(manager.get(m.getMaterial() , Texture.class), AssetType.CRATE);
                crate.setPosition(m.getPosX(), m.getPosY());
                if (calcHeight > 0) crate.setY(m.getPosY() - calcHeight / 2);
                sprites.add(crate);
            }else if(m.getMaterial().substring(11 , 18).equals("Crystal")) {

                ObjectsMap crystal = new ObjectsMap(manager.get(m.getMaterial() , Texture.class), AssetType.CRYSTAL);
                crystal.setPosition(m.getPosX(), m.getPosY());
                if (calcHeight > 0) crystal.setY(m.getPosY() - calcHeight / 2);
                sprites.add(crystal);
            }else if(m.getMaterial().substring(11 , 16).equals("Igloo")) {

                ObjectsMap igloo = new ObjectsMap(manager.get(m.getMaterial() , Texture.class), AssetType.IGLOO);
                igloo.setPosition(m.getPosX(), m.getPosY());

                // Flipping it, looks better
                igloo.flip(true , false);
                if (calcHeight > 0) igloo.setY(m.getPosY() - calcHeight / 2);
                sprites.add(igloo);
            }else if(m.getMaterial().substring(11 , 18).equals("SnowMan")) {

                ObjectsMap snowman = new ObjectsMap(manager.get(m.getMaterial() , Texture.class), AssetType.SNOWMAN);
                snowman.setPosition(m.getPosX(), m.getPosY());

                // Flipping it, looks better
                snowman.flip(true , false);
                if (calcHeight > 0) snowman.setY(m.getPosY() - calcHeight / 2);
                sprites.add(snowman);

                // Snowballs
                Snowball snb = new Snowball();
                snb.setPosition(snowman.getX() , snowman.getY());
                snb.setOriginalPositionX(snowman.getX());
                snowball.add(snb);
            }else if(m.getMaterial().substring(11 , 16).equals("Stone")) {

                ObjectsMap stone = new ObjectsMap(manager.get(m.getMaterial() , Texture.class), AssetType.STONE);
                stone.setPosition(m.getPosX(), m.getPosY());
                if (calcHeight > 0) stone.setY(m.getPosY() - calcHeight / 2);
                sprites.add(stone);
            }
        }

        return sprites;
    }

    /**
     Returns a list of Textures containing player walking

     @param manager     Object containing all assets previously loaded, may contain musics, sounds, tiles and objects of the map.
     @return            a list containing textures
     */

    public static Texture[] getPlayerWalk(AssetManager manager){

        Texture[] walk = new Texture[WALK_QTD];

        for (int i = 1; i <= WALK_QTD; i++){

            walk[i - 1] = manager.get(PLAYER + "/Walk (" + i + ").png" , Texture.class);
        }
        return walk;
    }

    /**
     Returns a list of Textures containing player Idle

     @param manager     Object containing all assets previously loaded, may contain musics, sounds, tiles and objects of the map.
     @return            a list containing textures
     */
    public static Texture[] getPlayerIdle(AssetManager manager){

        Texture[] idle = new Texture[JUMP_IDLE_TILES_QTD];

        for (int i = 1; i <= JUMP_IDLE_TILES_QTD; i++){

            idle[i - 1] = manager.get(PLAYER + "/Idle (" + i + ").png" , Texture.class);
        }
        return idle;
    }

    /**
     Returns a list of Textures containing player jump

     @param manager     Object containing all assets previously loaded, may contain musics, sounds, tiles and objects of the map.
     @return            a list containing textures
     */
    public static Texture[] getPlayerJump(AssetManager manager){

        Texture[] jump = new Texture[JUMP_IDLE_TILES_QTD];

        for (int i = 1; i <= JUMP_IDLE_TILES_QTD; i++){

            jump[i - 1] = manager.get(PLAYER + "/Jump (" + i + ").png" , Texture.class);
        }
        return jump;
    }

    /**
     Returns a list of Textures containing player run

     @param manager     Object containing all assets previously loaded, may contain musics, sounds, tiles and objects of the map.
     @return            a list containing textures
     */
    public static Texture[] getPlayerRun(AssetManager manager){

        Texture[] run = new Texture[RUN_SLIDE_QTD];

        for (int i = 1; i <= RUN_SLIDE_QTD; i++){

            run[i - 1] = manager.get(PLAYER + "/Run (" + i + ").png" , Texture.class);
        }
        return run;
    }

    /**
     Returns a list of Textures containing player slide

     @param manager     Object containing all assets previously loaded, may contain musics, sounds, tiles and objects of the map.
     @return            a list containing textures
     */
    public static Texture[] getPlayerSlide(AssetManager manager){

        Texture[] slide = new Texture[RUN_SLIDE_QTD];

        for (int i = 1; i <= RUN_SLIDE_QTD; i++){

            slide[i - 1] = manager.get(PLAYER + "/Slide (" + i + ").png" , Texture.class);
        }
        return slide;
    }

    /**
     Returns a list of Textures containing player dead

     @param manager     Object containing all assets previously loaded, may contain musics, sounds, tiles and objects of the map.
     @return            a list containing textures
     */
    public static Texture[] getPlayerDead(AssetManager manager){

        Texture[] dead = new Texture[DEAD_QTD];

        for (int i = 1; i <= DEAD_QTD; i++){

            dead[i - 1] = manager.get(PLAYER + "/Dead (" + i + ").png" , Texture.class);
        }
        return dead;
    }

    /**
     Returns a list of Textures containing snowballs

     @param manager     Object containing all assets previously loaded, may contain musics, sounds, tiles and objects of the map.
     @return            a list containing textures
     */
    public static Texture[] getSnowBalls(AssetManager manager){

        Texture[] snowball = new Texture[SNOWBALL_QTD];

        for (int i = 1; i <= SNOWBALL_QTD; i++){

            snowball[i - 1] = manager.get(SNOWBALL + i + ".png" , Texture.class);
        }
        return snowball;
    }

    /**
     Returns the main background image

     @param manager     Object containing all assets previously loaded, may contain musics, sounds, tiles and objects of the map.
     @return            a texture of the background image
     */
    public static Sprite getBackground(AssetManager manager){ return new Sprite(manager.get(BG , Texture.class)); }

    /**
     Play background music

     @param manager     Object containing all assets previously loaded, may contain musics, sounds, tiles and objects of the map.
     @param loop        Define if music will be played over
     @param m           Music path to be played
     */
    public static void playMusic(String m , AssetManager manager , boolean loop){

        GameSettings gameSettings = new GameSettings();
        float vol = gameSettings.isContainsVolumeMusic() ? gameSettings.getVolume(GameSettings.VOLUME_MUSIC) : 5;
        Music music = manager.get(m , Music.class);
        music.setLooping(loop);
        music.setVolume(vol);
        music.play();
    }

    /**
     stop background music

     @param manager     Object containing all assets previously loaded, may contain musics, sounds, tiles and objects of the map.
     @param m           Music path to be stopped
     */
    public static void stopMusic(String m , AssetManager manager){

        Music music = manager.get(m , Music.class);
        music.stop();
    }

    /**
     Set volume for the background music

     @param manager     Object containing all assets previously loaded, may contain musics, sounds, tiles and objects of the map.
     @param m           Music path to have its volume adjusted
     @param v           Value of the volume to be set
     */
    public static void setMusicVolume(String m , AssetManager manager , float v){

        Music music = manager.get(m , Music.class);
        music.setVolume(v);
    }


    /**
     Pause background music

     @param manager     Object containing all assets previously loaded, may contain musics, sounds, tiles and objects of the map.
     @param m           Music path to have its volume adjusted
     */
    public static void pauseMusic(String m , AssetManager manager){

        Music music = manager.get(m , Music.class);
        music.pause();
    }

    /**
     Returns game skin

     @param manager     Object containing all assets previously loaded, may contain musics, sounds, tiles and objects of the map.
     @return            Returns the main skin
     */
    public Skin getSkin(AssetManager manager){ return manager.get(SKIN , Skin.class); }


    /**
     Load skin to the asset manager

     @param manager     Object containing all assets previously loaded, may contain musics, sounds, tiles and objects of the map.
     */
    public void loadSkin(AssetManager manager){

        manager.load(SKIN , Skin.class , new SkinLoader.SkinParameter(ATLAS));
        manager.finishLoading();
    }

    /**
     Load the main assets to the asset manager

     @param manager     Object containing all assets previously loaded, may contain musics, sounds, tiles and objects of the map.
     */
    public void loadAssets(AssetManager manager){

        manager.load(BG , Texture.class);

        for (int i = 1; i < 18; i++){

            if(i <= SNOWBALL_QTD)
                manager.load(SNOWBALL + i + ".png", Texture.class);

            if(i <= RUN_SLIDE_QTD) {
                manager.load(PLAYER + "/Slide (" + i + ").png", Texture.class);
                manager.load(PLAYER + "/Run (" + i + ").png", Texture.class);
            }

            if(i <= JUMP_IDLE_TILES_QTD){
                manager.load(PLAYER + "/Jump (" + i + ").png" , Texture.class);
                manager.load(PLAYER + "/Idle (" + i + ").png" , Texture.class);

                // Tiles
                manager.load("png/Tiles/" + i + ".png" , Texture.class);
            }

            if(i <= WALK_QTD)
                manager.load(PLAYER + "/Walk (" + i + ").png" , Texture.class);


            manager.load(PLAYER + "/Dead (" + i + ").png" , Texture.class);
        }

        manager.load("png/Tiles/water_bottom.png" , Texture.class);
        manager.load("png/Tiles/water_surface.png" , Texture.class);
        manager.load("png/Object/Crate.png" , Texture.class);
        manager.load("png/Object/Crystal.png" , Texture.class);
        manager.load("png/Object/IceBox.png" , Texture.class);
        manager.load("png/Object/Igloo.png" , Texture.class);
        manager.load("png/Object/Sign_1.png" , Texture.class);
        manager.load("png/Object/Sign_2.png" , Texture.class);
        manager.load("png/Object/SnowMan.png" , Texture.class);
        manager.load("png/Object/Stone.png" , Texture.class);
        manager.load("png/Object/Tree_1.png" , Texture.class);
        manager.load("png/Object/Tree_2.png" , Texture.class);
        manager.load("menu.png" , Texture.class);

        manager.load(menuMusic, Music.class);
        manager.load(gameMusic, Music.class);
        manager.load(buttonClickSound, Sound.class);
        manager.load(jumpSound, Sound.class);
        manager.load(slideSound, Sound.class);
        manager.load(deadSound, Sound.class);
        manager.load(victorySound , Sound.class);
    }

}
