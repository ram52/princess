package com.mygdx.core;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.mygdx.core.entities.ActionResolver;
import com.mygdx.core.entities.B2DSprite;
import com.mygdx.core.handlers.Background;
import com.mygdx.core.handlers.BoundedCamera;
import com.mygdx.core.handlers.Content;
import com.mygdx.core.handlers.GameStateManager;
import com.mygdx.core.handlers.MyInputProcessor;
import com.mygdx.core.handlers.Save;

public class MyGdxGame implements ApplicationListener {
    private static final String LOG_TAG = MyGdxGame.class.getSimpleName();
    private Stage stage0;
    private Image intro;
    private FPSLogger fps;
    public static Rectangle viewport;
    public static final String TITLE = "PRINCESS";
    public static final int V_WIDTH = 640;
    public static final int V_HEIGHT = 960;
    public static int PAD_ZONE = 0;
    public static int BRICK_SUMON_ZONE = 0;
    public static final int SCALE = 1;
    public static final float ASPECT_RATIO = (float) V_WIDTH / (float) V_HEIGHT;
    public static float STEP = 1 / 30f;
    public static boolean StartCameraMotion = true;
    private SpriteBatch sb;
    private static ShapeRenderer shapeRenderer;
    private BoundedCamera cam;
    private OrthographicCamera hudCam;
    public static GameStateManager gsm;
    public static Content res;
    private AssetManager assets;
    public static TextureAtlas atlas;
    public static float offsetx;
    public static float offsety;
    public static TiledMap tileMap;
    public static boolean iSeverythingLoaded = false;
    private static int soundEnable = 1;
    private static boolean android = true;
    public static B2DSprite fadeIn, fadeOut;
    public static float FADE_DELAY = 1/50f;
    public static BitmapFont font, font2;
    public static String debugString = "";
    public static float GROUND = 2.5621998f; //todo use box2d
    public static int MONEY_BY_ENEMY = 1;
    public static boolean DEBUG = false;
    public static boolean TEST = false;
    public static Vector2 lastPlayerPosition = new Vector2(0,0);
    public static Vector2 lastBrickPosition = new Vector2(0,0);
    public static Vector2 lastPrincessPosition = new Vector2(0,0);
    public static int lastScoreInTutorial = 0;


    public static void setIsBoosTerritory(boolean isBoosTerritory) {
        MyGdxGame.isBoosTerritory = isBoosTerritory;
    }

    private static boolean isBoosTerritory = false;
    public static boolean pause = false;
    public static Background background_cloud, background_wood1,
    //background_skyDay
    //background_storyLine,
    background_title, background_tuto, background_shop;
    public static String spritesPackPath = "data/sprite/atlas.pack";
    public static String errorSoundPath = "data/sound/error.ogg";
    public static String kameBeamSoundPath = "data/sound/kame.ogg";
    public static String jump1SoundPath = "data/sound/jump1.ogg";
    public static String deathSoundPath = "data/sound/death.ogg";
    public static String bossDeathSoundPath = "data/sound/bossDeath.ogg";
    public static String secretUnlockSoundPath = "data/sound/secretUnlock.ogg";
    public static String secretBossSoundPath = "data/sound/secretboss.ogg";
    public static String newScreenSoundPath = "data/sound/new_screen.ogg";
    public static String pointSoundPath = "data/sound/point.ogg";
    public static String fallingSoundPath = "data/sound/falling.ogg";
    public static String onSoundPath = "data/sound/on.ogg";
    public static String crushSoundPath = "data/sound/crush.ogg";
    public static String lightningSoundPath = "data/sound/lightning.ogg";
    public static String laughSoundPath = "data/sound/laugh.ogg";
    public static String slashSoundPath = "data/sound/slash.ogg";
    public static String pauseInSoundPath = "data/sound/pause1_in.ogg";
    public static String pauseOutSoundPath = "data/sound/pause1_out.ogg";
    public static String alarmSoundPath = "data/sound/alarm.ogg";
    public static String interactionSoundPath = "data/sound/interaction.ogg";
    public static String selectSoundPath = "data/sound/select.ogg";
    public static String equipedSoundPath = "data/sound/equiped.ogg";
    public static String moveSoundPath = "data/sound/move.ogg";
    public static String boomSoundPath = "data/sound/boom.ogg";
    public static String enemyHitMusicdPath = "data/sound/hit.ogg";
    public static String noAmmoSoundPath = "data/sound/no_ammo.ogg";
    public static String kamehamehaSoundPath = "data/sound/kamehameha.ogg";
    public static String fireballSmallSoundPath = "data/sound/fireball_small.ogg";
    public static String fireballBigSoundPath = "data/sound/fireball_big.ogg";
    public static String brokenSoundPath = "data/sound/broken.ogg";
    public static String titleMusicPath = "data/sound/title.ogg";
    public static String shopMusicPath = "data/sound/shop.ogg";
    public static String level1MusicPath = "data/sound/level1.ogg";
    public static String gameOverMusicPath = "data/sound/gameover.ogg";
    public static String bossMusicPath = "data/sound/boss.ogg";
    public static String openingMusicPath = "data/sound/opening.ogg";
    public static String successMusicPath = "data/sound/success.ogg";
    public static String fontCreditsPath = "data/font/m05a-12.fnt";
    public static String fontTextPath = "data/font/font1.fnt";
    public static String fontScorePath = "data/font/font2.fnt";
    public static String fontPointPath = "data/font/font3.fnt";
    public static String fontfsexPath = "data/font/FSEX300-32.fnt";
    public static String mapPath = "data/map/map1.tmx";
    public static String desktopIconPath = "data/sprite/icon/ic_launcher.png";
    public static String AndroidPlayStoreGameUrl = "https://ram52.com";
    public static String AndroidFacebookGameUrl = "https://www.facebook.com/Axl.RunningBird/";
    public static String AndroidTwitterGameUrl = "https://twitter.com/axldotm";
    public static String developerWebSite = "https://axldotm.com";
    public static String achievement1 = "CgkIw6iVjJQdEAIQAg";
    public static String achievement2 = "CgkIw6iVjJQdEAIQAw";
    public static String achievement3 = "CgkIw6iVjJQdEAIQBA";
    public static String achievement4 = "CgkIw6iVjJQdEAIQBQ";
    public static String achievement5 = "CgkIw6iVjJQdEAIQBg";
    public static String achievementSecret = "CgkIw6iVjJQdEAIQBw";

    private static float cptBlinkingStarSlow = 0.0f;
    private static float cptBlinkingStarNormal = 0.0f;
    private static float cptBlinkingStarFast = 0.0f;
    private static boolean isAscendingSlow = true;
    private static boolean isAscendingNormal = true;
    private static boolean isAscendingFast = true;
    private static float starBlinkingSpeedSlow = 0.1f;
    private static float starBlinkingSpeedNormal = 0.5f;
    private static float starBlinkingSpeedFast = 1f;
    private float progress = 0f;
    private float numberOfAssets = 14f;

    private static int CONTINUE_MAX = 9999999;
    private static int CONTINUE_PRICE = 100;
    private static int CONTINUE_STORE_PACK = 500;
    public static int TWENTY_COINS_STORE_PACK = 40;
    public static int HUNDRED_COINS_STORE_PACK = 100;
    public static int THOUSAND_COINS_STORE_PACK = 1000;
    private static boolean secretDiscovered = false;
    private static int TAPNUMB_SECRET = 50;
    private static boolean shortcutDiscovered = false;
    private static boolean wait = false;
    private static boolean init = false;
    public static int continueCount = 0;

    public static int pickedGameplay = 2;


    public static boolean isContinue() {
        return Continue;
    }

    public static void setContinue(boolean aContinue) {
        Continue = aContinue;
    }

    private static boolean Continue = false;

    public interface RequestHandler {
        public void confirm(ConfirmInterface confirmInterface);
    }

    public interface ConfirmInterface {
        public void yes();

        public void no();
    }

    public MyGdxGame() {
    }

    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        //loading  = GifDecoder.loadGIFAnimation(com.badlogic.gdx.graphics.g2d.Animation.PlayMode.LOOP, Gdx.files.internal("data/sprite/loading.gif").readBytes());
        //font = new BitmapFont();
        fps = new FPSLogger();
        font = new BitmapFont(Gdx.files.internal(MyGdxGame.fontTextPath), false);
        font2 = new BitmapFont(Gdx.files.internal(MyGdxGame.fontfsexPath), false);

        viewport = new Rectangle();
        intro = new Image(new Texture(Gdx.files.internal("data/sprite/loading.png")));
        intro.setFillParent(true);
        stage0 = new Stage();
        stage0.addActor(intro);
        //stage0.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(0.1f)));

        res = new Content();
        res.loadMusic(onSoundPath, "on");
        res.loadSound(errorSoundPath, "error");
        res.loadSound(kameBeamSoundPath, "kame");
        res.loadSound(jump1SoundPath, "jump1");
        res.loadMusic(newScreenSoundPath, "newScreen");
        res.loadSound(pointSoundPath, "point");
        res.loadSound(fallingSoundPath, "falling");
        res.loadSound(selectSoundPath, "select");
        res.loadSound(pauseInSoundPath, "pause_in");
        res.loadSound(pauseOutSoundPath, "pause_out");
        res.loadSound(equipedSoundPath, "equiped");
        res.loadSound(boomSoundPath, "boom");
        res.loadSound(noAmmoSoundPath, "no_ammo");
        res.loadMusic(laughSoundPath, "laugh");
        res.loadSound(lightningSoundPath,"lightning");
        res.loadSound(fireballSmallSoundPath, "fireball_small");
        res.loadSound(fireballBigSoundPath, "fireball_big");
        res.loadMusic(moveSoundPath, "move");
        res.loadSound(enemyHitMusicdPath, "hit");
        res.loadSound(crushSoundPath, "crush");
        res.loadMusic(successMusicPath, "success");
        res.loadMusic(level1MusicPath, "level1");
        res.loadMusic(titleMusicPath, "title");
        res.loadMusic(gameOverMusicPath, "game_over");
        res.loadMusic(shopMusicPath, "shop");
        res.loadSound(secretBossSoundPath, "secretboss");
        res.loadSound(kamehamehaSoundPath, "kamehameha");
        res.loadSound(brokenSoundPath, "broken");
        res.loadSound(alarmSoundPath, "alarm");
        res.loadSound(interactionSoundPath, "interaction");
        res.loadSound(pointSoundPath, "secret");
        res.loadSound(secretUnlockSoundPath, "secretUnlock");
        res.loadMusic(deathSoundPath, "death");
        res.loadSound(bossDeathSoundPath, "bossDeath");
        res.loadMusic(slashSoundPath, "slash");
        res.getMusic("success").setLooping(false);
        res.getMusic("title").setLooping(false);
        res.getMusic("level1").setLooping(true);
        //res.getMusic("alarm").setLooping(true);
        res.getMusic("shop").setLooping(true);


        assets = new AssetManager();
        assets.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
        assets.load(mapPath, TiledMap.class);
        assets.load(spritesPackPath, TextureAtlas.class);

        //assets.finishLoading();

        sb = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        cam = new BoundedCamera();
        cam.setToOrtho(false, V_WIDTH, V_HEIGHT);
        hudCam = new OrthographicCamera();
        hudCam.setToOrtho(false, V_WIDTH, V_HEIGHT);
        size(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.app.debug(LOG_TAG,"-->"+ "SCREEN SIZE: " + Gdx.graphics.getWidth() + "X" + Gdx.graphics.getHeight());
        Save.init();
        Save.load();

        Save.gd.setFireBallPurchased(true);
        Save.gd.setFireBallEquiped(true);
        Save.save();

        soundEnable = Save.gd.isSoundEnable();
        PAD_ZONE= Gdx.graphics.getHeight();
        BRICK_SUMON_ZONE= (int) (Gdx.graphics.getHeight() - (Gdx.graphics.getHeight() / 2f));
        pickedGameplay = Save.gd.getPickedGamePlay();

    }

    public static void initFade(){
        fadeIn = new B2DSprite(new Sprite(MyGdxGame.atlas.findRegion("fadeIn2")).split(8,8)[0], FADE_DELAY);
        fadeIn.setHeight(Gdx.graphics.getHeight());
        fadeIn.setWidth(Gdx.graphics.getWidth());
        fadeIn.setLoop(false);

        fadeOut = new B2DSprite(new Sprite(MyGdxGame.atlas.findRegion("fadeOut2")).split(8,8)[0], FADE_DELAY);
        fadeOut.setHeight(Gdx.graphics.getHeight());
        fadeOut.setWidth(Gdx.graphics.getWidth());
        fadeOut.setLoop(false);
    }

    public void render() {

        if (assets.update()) {
            //wait a little bit at 100% . Prettier this way.
            if(!wait){
                resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                Gdx.gl.glViewport((int) viewport.x, (int) viewport.y, (int) viewport.width, (int) viewport.height);

                progress = 100;

                if(!res.getMusic("on").isPlaying()){
                    if((MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2))
                        res.getMusic("on").play();
                }

                Gdx.app.debug(LOG_TAG,progress+"%");

                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
                sb.setProjectionMatrix(cam.combined);
                stage0.act();
                sb.begin();
                stage0.draw();
                sb.end();

                displayLoadingBar(255, 255, 255, V_HEIGHT / 10f, true);
                //displayLoadingBar(255, 255, 255, V_HEIGHT / 10f, false);

                if(!init){
                    init = true;
                    new java.util.Timer().schedule(
                            new java.util.TimerTask() {
                                @Override
                                public void run() {
                                    wait = true;
                                }
                            },
                            1000
                    );
                }
            }
            //Everything is loaded ready to start the game
            if (gsm == null && wait) {
                //tileMap = new TmxMapLoader().load(mapPath);
                tileMap = assets.get(mapPath);
                atlas = assets.get(spritesPackPath);
                setBackgrounds();
                gsm = new GameStateManager(this);
                Gdx.input.setInputProcessor(new MyInputProcessor());
                Gdx.input.setCatchBackKey(true);
                initFade();
                iSeverythingLoaded = true;
            }

            /** RENDER THE GAME STATE **/
            if(gsm != null){
                if(!gsm.flag_play | !gsm.flag_shop){
                    debugString = "fps: "+Gdx.graphics.getFramesPerSecond()+'\n'+
                            "java heap: "+ (int)(Gdx.app.getJavaHeap()/Math.pow(10, 6))+"Mb"+'\n'+
                            "native heap: "+ (int)(Gdx.app.getNativeHeap()/Math.pow(10, 6))+"Mb";
                }

                gsm.update(STEP);
                gsm.render();

                if (Gdx.input.isKeyPressed(Keys.BACK)) showConfirmDialog();
            }

            if(DEBUG){
                sb.begin();
                font.setColor(Color.RED);
                font.drawMultiLine(sb, debugString, MyGdxGame.V_WIDTH/15 , MyGdxGame.V_HEIGHT/1.1f, MyGdxGame.V_WIDTH, BitmapFont.HAlignment.LEFT);
                sb.end();
            }

        } else {
            //display loading screen
            resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            Gdx.gl.glViewport((int) viewport.x, (int) viewport.y, (int) viewport.width, (int) viewport.height);
            progress = 100 * (assets.getLoadedAssets() / numberOfAssets);
            Gdx.app.debug(LOG_TAG, progress +"%");
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            sb.setProjectionMatrix(cam.combined);
            stage0.act();
            sb.begin();
            stage0.draw();
            sb.end();
            displayLoadingBar(255, 255, 255, V_HEIGHT / 10f, false);
            //displayLoadingBar(255, 255, 255, V_HEIGHT / 10f, false);
            //displayLoadingBar(112, 146, 190, V_HEIGHT / 10f, false);
        }
    }

    private void displayLoadingBar(int r, int g, int b, float posY, boolean full) {
        float recSize = 40f;
        float barWidth = V_WIDTH/1.5f;
        float first = (V_WIDTH - barWidth)/2;
        Gdx.gl.glEnable(GL20.GL_BLEND);
        shapeRenderer.setProjectionMatrix(cam.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        if(full){
            shapeRenderer.setColor(r / 255f, g / 255f, b / 255f, 1f);
            shapeRenderer.rect(first, posY, barWidth, recSize);
        }else{
            shapeRenderer.setColor(r / 255f, g / 255f, b / 255f, 1f);
            shapeRenderer.rect(first, posY, barWidth*(progress/100), recSize);
        }

        shapeRenderer.setColor(r / 155f, g / 155f, b / 155f, 0.1f);
        shapeRenderer.rect(first, posY, barWidth, recSize);

        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
        //for vanity
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            Gdx.app.error(LOG_TAG,"error in thread sleep",e);
        }
    }

    public static void computeStarBlinking() {
        if (cptBlinkingStarSlow > 1 | cptBlinkingStarSlow < 0) isAscendingSlow = !isAscendingSlow;
        if (cptBlinkingStarNormal > 1 | cptBlinkingStarNormal < 0)
            isAscendingNormal = !isAscendingNormal;
        if (cptBlinkingStarFast > 1 | cptBlinkingStarFast < 0) isAscendingFast = !isAscendingFast;

        if (isAscendingSlow) cptBlinkingStarSlow += 0.1f * starBlinkingSpeedSlow;
        else cptBlinkingStarSlow -= 0.1f * starBlinkingSpeedSlow;

        if (isAscendingNormal) cptBlinkingStarNormal += 0.1f * starBlinkingSpeedNormal;
        else cptBlinkingStarNormal -= 0.1f * starBlinkingSpeedNormal;

        if (isAscendingFast) cptBlinkingStarFast += 0.1f * starBlinkingSpeedFast;
        else cptBlinkingStarFast -= 0.1f * starBlinkingSpeedFast;
    }

    public static void displayBlinkingStars() {
//        Gdx.gl.glEnable(GL20.GL_BLEND);
//        //Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
//        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//
//        int sizeS = V_WIDTH / 150;
//        int sizeM = V_WIDTH / 110;
//        int sizeL = V_WIDTH / 90;
//
//        shapeRenderer.setColor(1, 1, 1, cptBlinkingStarSlow);
//        shapeRenderer.rect((V_WIDTH - sizeM) / 12, (V_HEIGHT - sizeM) / 1.1f, sizeM, sizeM);
//
//        shapeRenderer.setColor(1, 1, 1, cptBlinkingStarFast);
//        shapeRenderer.rect((V_WIDTH - sizeS) / 10, (V_HEIGHT - sizeS) / 1.02f, sizeS, sizeS);
//
//        shapeRenderer.setColor(1, 1, 1, cptBlinkingStarNormal);
//        shapeRenderer.rect((V_WIDTH - sizeS) / 13, (V_HEIGHT - sizeS) / 1.2f, sizeS, sizeS);
//
//        shapeRenderer.setColor(1, 1, 1, cptBlinkingStarNormal * 0.8f);
//        shapeRenderer.rect((V_WIDTH - sizeM) / 4, (V_HEIGHT - sizeM) / 1.03f, sizeM, sizeM);
//
//        shapeRenderer.setColor(1, 1, 1, cptBlinkingStarNormal * 0.1f);
//        shapeRenderer.rect((V_WIDTH - sizeL) / 16, (V_HEIGHT - sizeL) / 1.05f, sizeL, sizeL);
//
//        shapeRenderer.setColor(1, 1, 1, cptBlinkingStarNormal * 0.5f);
//        shapeRenderer.rect((V_WIDTH - sizeM) / 8, (V_HEIGHT - sizeM) / 1.07f, sizeM, sizeM);
//
//        shapeRenderer.setColor(1, 1, 1, cptBlinkingStarSlow * 0.2f);
//        shapeRenderer.rect((V_WIDTH - sizeM) / 1.5f, (V_HEIGHT - sizeM) / 1.01f, sizeM, sizeM);
//
//        //shapeRenderer.setColor(1, 1, 1, cptBlinkingStarFast);
//        //shapeRenderer.rect((V_WIDTH - sizeM) / 1.2f, (V_HEIGHT - sizeM) / 1.01f, sizeM, sizeM);
//
//        shapeRenderer.setColor(1, 1, 1, cptBlinkingStarSlow);
//        shapeRenderer.rect((V_WIDTH - sizeL) / 1.1f, (V_HEIGHT - sizeL) / 1.2f, sizeL, sizeL);
//
//        shapeRenderer.setColor(1, 1, 1, cptBlinkingStarNormal);
//        shapeRenderer.rect((V_WIDTH - sizeS) / 1.05f, (V_HEIGHT - sizeS) / 1.1f, sizeS, sizeS);
//
//        shapeRenderer.setColor(1, 1, 1, cptBlinkingStarSlow * 0.5f);
//        shapeRenderer.rect((V_WIDTH - sizeS) / 1.14f, (V_HEIGHT - sizeS) / 1.11f, sizeM, sizeM);
//
//        shapeRenderer.setColor(1, 1, 1, cptBlinkingStarFast * 0.7f);
//        shapeRenderer.rect((V_WIDTH - sizeS) / 1.16f, (V_HEIGHT - sizeS) / 1.06f, sizeM, sizeM);
//
//        shapeRenderer.setColor(1, 1, 1, cptBlinkingStarSlow * 0.2f);
//        shapeRenderer.rect((V_WIDTH - sizeL) / 1.19f, (V_HEIGHT - sizeL) / 1.045f, sizeL, sizeL);
//
//        shapeRenderer.setColor(1, 1, 1, cptBlinkingStarNormal * 0.6f);
//        shapeRenderer.rect((V_WIDTH - sizeS) / 1.03f, (V_HEIGHT - sizeS) / 1.03f, sizeS, sizeS);
//
//        shapeRenderer.end();
//        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    private void setBackgrounds() {
        Sprite tex;
        tex = new Sprite(MyGdxGame.atlas.findRegion("buttonSecret"));
        background_cloud = new Background(new TextureRegion(tex), cam, 1f);

        tex = new Sprite(MyGdxGame.atlas.findRegion("backgroundSky"));
        tex.setScale(1, MyGdxGame.V_WIDTH);
        //background_skyDay = new Background(new TextureRegion(tex), hudCam, 1f);

        tex = new Sprite(MyGdxGame.atlas.findRegion("backgroundWood1"));
        background_wood1 = new Background(new TextureRegion(tex), cam, 1f);

        tex = new Sprite(MyGdxGame.atlas.findRegion("backgroundTitlehtml"));
        background_title = new Background(new TextureRegion(tex), cam, 1f);
        background_title.setVector(0, 0);

        tex = new Sprite(MyGdxGame.atlas.findRegion("backgroundShop"));
        background_shop = new Background(new TextureRegion(tex), cam, 1f);
        background_shop.setVector(0, 0);

    }




    public static void setSoundEnable(int soundEnable) {
        MyGdxGame.soundEnable = soundEnable;
        refreshSoundSetting();
    }

    private static void refreshSoundSetting() {
        Save.load();
        Save.gd.setSound(soundEnable);
        Save.save();
        soundEnable = Save.gd.isSoundEnable();
        Gdx.app.debug(LOG_TAG,"-->"+"SOUND: " + soundEnable);
    }

    public void size(int width, int height) {
        // calculate new viewport
        float aspectRatio = (float) width / (float) height;
        float scale;
        Vector2 crop = new Vector2(0f, 0f);

        if (aspectRatio > MyGdxGame.ASPECT_RATIO) {
            scale = (float) height / (float) MyGdxGame.V_HEIGHT;
            crop.x = (width - MyGdxGame.V_WIDTH * scale) / 2f;
        } else if (aspectRatio < MyGdxGame.ASPECT_RATIO) {
            scale = (float) width / (float) MyGdxGame.V_WIDTH;
            crop.y = (height - MyGdxGame.V_HEIGHT * scale) / 2f;
        } else {
            scale = (float) width / (float) MyGdxGame.V_WIDTH;
        }

        float w = (float) MyGdxGame.V_WIDTH * scale;
        float h = (float) MyGdxGame.V_HEIGHT * scale;
        offsetx = crop.x;
        offsety = crop.y;

    }

    public static int isSoundEnable() {
        return soundEnable;
    }

    public void showConfirmDialog() {

    }

    public ShapeRenderer getShapeRenderer() {
        return shapeRenderer;
    }

    public SpriteBatch getSpriteBatch() {
        return sb;
    }

    public BoundedCamera getCamera() {
        return cam;
    }

    public OrthographicCamera getHUDCamera() {
        return hudCam;
    }

    public GameStateManager getGSM() {
        return gsm;
    }

    public void resize(int width, int height) {
        float aspectRatio = (float) width / (float) height;
        float scale = 1f;
        Vector2 crop = new Vector2(0f, 0f);

        if (aspectRatio > MyGdxGame.ASPECT_RATIO) {
            scale = (float) height / (float) MyGdxGame.V_HEIGHT;
            crop.x = (width - MyGdxGame.V_WIDTH * scale) / 2f;
        } else if (aspectRatio < MyGdxGame.ASPECT_RATIO) {
            scale = (float) width / (float) MyGdxGame.V_WIDTH;
            crop.y = (height - MyGdxGame.V_HEIGHT * scale) / 2f;
        } else {
            scale = (float) width / (float) MyGdxGame.V_WIDTH;
        }

        float w = (float) MyGdxGame.V_WIDTH * scale;
        float h = (float) MyGdxGame.V_HEIGHT * scale;
        viewport = new Rectangle(crop.x, 0, w, h);
    }

    public void pause() {
        pause = true;
    }

    public static void setPause(boolean p) {

            if (p) {
                res.getMusic("level1").setVolume(0f);
                if (res.getMusic("level1").isPlaying()) res.getMusic("level1").pause();
                //if (res.getMusic("alarm").isPlaying()) res.getMusic("alarm").pause();
                if (soundEnable == 2 | soundEnable == 1) res.getSound("pause_in").play();
            } else {
                res.getMusic("level1").setVolume(1f);
                if (soundEnable == 2) res.getMusic("level1").play();
                if (soundEnable == 2 | soundEnable == 1) res.getSound("pause_out").play();
            }

        pause = p;
    }

    public void resume() {

    }

    public static boolean isStartCameraMotion() {
        return StartCameraMotion;
    }

    public static void setStartCameraMotion(boolean startCameraMotion) {
        StartCameraMotion = startCameraMotion;
    }


    public void dispose() {
        assets.clear();
        tileMap.dispose();
        res.removeAll();
        sb.dispose();
        shapeRenderer.dispose();
        stage0.dispose();
        shapeRenderer = null;
        gsm = null;
        ;
        res = null;
        ;
        atlas = null;
        tileMap = null;
    }

    public static int getContinuePrice() {
        return CONTINUE_PRICE;
    }

    public static int getContinueMax() {
        return CONTINUE_MAX;
    }

    public static boolean isSecretDiscovered() {
        return secretDiscovered;
    }

    public static void setSecretDiscovered(boolean secretDiscovered) {
        MyGdxGame.secretDiscovered = secretDiscovered;
    }

    public static int getContinueStorePack() {
        return CONTINUE_STORE_PACK;
    }

    public static void setContinueStorePack(int continueStorePack) {
        CONTINUE_STORE_PACK = continueStorePack;
    }

    public static int getTapnumbSecret() {
        return TAPNUMB_SECRET;
    }

    public static void setTapnumbSecret(int tapnumbSecret) {
        TAPNUMB_SECRET = tapnumbSecret;
    }

    public static boolean isShortcutDiscovered() {
        return shortcutDiscovered;
    }

    public static void setShortcutDiscovered(boolean shortcutDiscovered) {
        MyGdxGame.shortcutDiscovered = shortcutDiscovered;
    }


    public static String getAchievement1() {
        return achievement1;
    }

    public static void setAchievement1(String achievement1) {
        MyGdxGame.achievement1 = achievement1;
    }

    public static String getAchievement2() {
        return achievement2;
    }

    public static void setAchievement2(String achievement2) {
        MyGdxGame.achievement2 = achievement2;
    }

    public static String getAchievement3() {
        return achievement3;
    }

    public static void setAchievement3(String achievement3) {
        MyGdxGame.achievement3 = achievement3;
    }

    public static String getAchievement4() {
        return achievement4;
    }

    public static void setAchievement4(String achievement4) {
        MyGdxGame.achievement4 = achievement4;
    }

    public static String getAchievement5() {
        return achievement5;
    }

    public static void setAchievement5(String achievement5) {
        MyGdxGame.achievement5 = achievement5;
    }

    public static int getPickedGameplay() {
        return pickedGameplay;
    }

    public static void setPickedGameplay(int pickedGameplay) {
        MyGdxGame.pickedGameplay = pickedGameplay;
    }


}