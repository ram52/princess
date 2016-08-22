package com.mygdx.core;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
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
import com.mygdx.core.handlers.Background;
import com.mygdx.core.handlers.BoundedCamera;
import com.mygdx.core.handlers.Content;
import com.mygdx.core.handlers.GameStateManager;
import com.mygdx.core.handlers.MyInputProcessor;
import com.mygdx.core.handlers.Save;

public class MyGdxGame implements ApplicationListener {
    private Stage stage0;
    private Image intro;
    //private com.badlogic.gdx.graphics.g2d.Animation loading;
    private Rectangle viewport;
    public static ActionResolver actionResolver;
    public static final String TITLE = "Running Bird";
    public static final int V_WIDTH = 640;
    public static final int V_HEIGHT = 960;
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
    RequestHandler requestHandler;
    public static float offsetx;
    public static float offsety;
    public static TiledMap tileMap;
    public static boolean iSeverythingLoaded = false;
    private static int soundEnable = 1;
    private static boolean android = true;


    public static void setIsBoosTerritory(boolean isBoosTerritory) {
        MyGdxGame.isBoosTerritory = isBoosTerritory;
    }

    private static boolean isBoosTerritory = false;
    public static boolean pause = false;
    public static boolean nightEnable = true;
    public static Background background_cloud, background_skyNight, background_skyDay, background_wood1,
    //background_storyLine,
    background_title, background_tuto;
    public static String spritesPackPath = "data/sprite/atlas.pack";
    public static String errorSoundPath = "data/sound/error.ogg";
    //public static String reviveSoundPath = "data/sound/revive.ogg";
    public static String yaSoundPath = "data/sound/yaa!.ogg";
    public static String jump1SoundPath = "data/sound/jump1.ogg";
    public static String jump2SoundPath = "data/sound/jump2.ogg";
    public static String deathSoundPath = "data/sound/death.ogg";
    public static String bossDeathSoundPath = "data/sound/bossDeath.ogg";
    public static String secretUnlockSoundPath = "data/sound/secretUnlock.ogg";
    public static String secretBossSoundPath = "data/sound/secretboss.ogg";
    public static String newScreenSoundPath = "data/sound/new_screen.ogg";
    public static String pointSoundPath = "data/sound/point.ogg";
    public static String fallingSoundPath = "data/sound/falling.ogg";
    public static String onSoundPath = "data/sound/on.ogg";
    public static String crushSoundPath = "data/sound/crush.ogg";
    public static String laughSoundPath = "data/sound/laugh.ogg";
    public static String slashSoundPath = "data/sound/slash.ogg";
    public static String selectSoundPath = "data/sound/select.ogg";
    public static String equipedSoundPath = "data/sound/equiped.ogg";
    public static String moveSoundPath = "data/sound/move.ogg";
    public static String boomSoundPath = "data/sound/boom.ogg";
    public static String enemyHitMusicdPath = "data/sound/hit.ogg";
    public static String kamehamehaSoundPath = "data/sound/kamehameha.ogg";
    public static String epicMusicPath = "data/sound/epic.ogg";
    public static String reloadedSoundPath = "data/sound/reloaded.ogg";
    public static String fireballSoundPath = "data/sound/fireball.ogg";
    public static String mainMusicPath = "data/sound/main.ogg";
    public static String gameOverMusicPath = "data/sound/gameover.ogg";
    public static String bossMusicPath = "data/sound/boss.ogg";
    public static String openingMusicPath = "data/sound/opening.ogg";
    public static String successMusicPath = "data/sound/success.ogg";
    public static String fontCreditsPath = "data/font/m05a-12.fnt";
    public static String fontTextPath = "data/font/font1.fnt";
    public static String fontScorePath = "data/font/font2.fnt";
    public static String fontPointPath = "data/font/font3.fnt";
    public static String mapPath = "data/map/map1.tmx";
    public static String desktopIconPath = "data/sprite/icon/ic_launcher.png";
    public static String AndroidPlayStoreGameUrl = "https://play.google.com/store/apps/details?id=com.axldotm.runningbird";
    public static String AndroidFacebookGameUrl = "https://www.facebook.com/Axl.RunningBird/";
    public static String AndroidTwitterGameUrl = "https://twitter.com/axldotm";
    public static String developerWebSite = "https://axldotm.com";
    public static String achievementScore10 = "CgkI_sup0ooGEAIQAQ";
    public static String achievementScore50 = "CgkI_sup0ooGEAIQAg";
    public static String achievementScore70 = "CgkI_sup0ooGEAIQAw";
    public static String achievementScore80 = "CgkI_sup0ooGEAIQBA";
    public static String achievementScore90 = "CgkI_sup0ooGEAIQBQ";
    public static String achievementScore100 = "CgkI_sup0ooGEAIQBg";
    public static String achievementPeace = "CgkI_sup0ooGEAIQCw";
    public static String achievementBossDefeated = "CgkI_sup0ooGEAIQBg";
    public static String achievementSecretLadyBug = "CgkI_sup0ooGEAIQCA";
    public static String achievementSecretBossShortcut = "CgkI_sup0ooGEAIQCQ";
    public static String achievementDecriptSecretMessage = "CgkI_sup0ooGEAIQCg";

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
    private static boolean secretDiscovered = false;
    private static int TAPNUMB_SECRET = 50;
    private static boolean shortcutDiscovered = false;
    private static boolean wait = false;
    private static boolean init = false;
    public static int continueCount = 0;
    public static float PLAYER_VELOCITY = 1.4f;

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

    public MyGdxGame(ActionResolver actionResolver, RequestHandler requestHandler) {
        this.actionResolver = actionResolver;
        this.requestHandler = requestHandler;
    }

    public void create() {
        //loading  = GifDecoder.loadGIFAnimation(com.badlogic.gdx.graphics.g2d.Animation.PlayMode.LOOP, Gdx.files.internal("data/sprite/loading.gif").readBytes());
        viewport = new Rectangle();
        intro = new Image(new Texture(Gdx.files.internal("data/sprite/loading.png")));
        intro.setFillParent(true);
        stage0 = new Stage();
        stage0.addActor(intro);
        //stage0.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(0.1f)));

        res = new Content();
        res.loadMusic(onSoundPath, "on");
        res.getMusic("on").play();
        res.loadSound(errorSoundPath, "error");
        //res.loadSound(reviveSoundPath, "revive");
        res.loadSound(yaSoundPath, "ya");
        res.loadSound(jump1SoundPath, "jump1");
        res.loadSound(jump2SoundPath, "jump2");
        res.loadSound(newScreenSoundPath, "newScreen");
        res.loadSound(pointSoundPath, "point");
        res.loadSound(fallingSoundPath, "falling");
        res.loadSound(selectSoundPath, "select");
        res.loadSound(equipedSoundPath, "equiped");
        res.loadSound(boomSoundPath, "boom");
        res.loadMusic(laughSoundPath, "laugh");
        res.loadSound(fireballSoundPath, "fireball");
        res.loadSound(moveSoundPath, "move");
        res.loadSound(enemyHitMusicdPath, "hit");
        res.loadMusic(crushSoundPath, "crush");
        res.loadMusic(openingMusicPath, "opening");
        res.loadMusic(successMusicPath, "success");
        res.loadMusic(mainMusicPath, "main");
        res.loadMusic(gameOverMusicPath, "gameOver");
        res.loadMusic(bossMusicPath, "boss");
        res.loadMusic(secretBossSoundPath, "secretboss");
        res.loadSound(kamehamehaSoundPath, "kamehameha");
        res.loadSound(reloadedSoundPath, "reloaded");
        res.loadMusic(epicMusicPath, "epic");
        res.loadMusic(pointSoundPath, "secret");
        res.loadMusic(secretUnlockSoundPath, "secretUnlock");
        res.loadMusic(deathSoundPath, "death");
        res.loadMusic(bossDeathSoundPath, "bossDeath");
        res.loadMusic(slashSoundPath, "slash");
        res.getMusic("epic").setLooping(true);
        res.getMusic("opening").setLooping(true);
        res.getMusic("success").setLooping(false);
        res.getMusic("main").setLooping(true);
        res.getMusic("gameOver").setLooping(true);
        res.getMusic("boss").setLooping(true);
        res.getMusic("crush").setLooping(false);
        res.getMusic("secret").setLooping(false);
        res.getMusic("secretUnlock").setLooping(false);

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
        System.out.println("SCREEN SIZE: " + Gdx.graphics.getWidth() + "X" + Gdx.graphics.getHeight());
        Save.load();
        soundEnable = Save.gd.isSoundEnable();
        nightEnable = Save.gd.isNight();
    }

    public void render() {

        if (assets.update()) {

            if(!wait){
            resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            Gdx.gl.glViewport((int) viewport.x, (int) viewport.y,
                    (int) viewport.width, (int) viewport.height);

            progress = 100;
            System.out.println("LOADING...  " + progress);

            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            sb.setProjectionMatrix(cam.combined);
            stage0.act();
            sb.begin();
            stage0.draw();
            sb.end();

            displayLoadingBar(112, 146, 190, V_HEIGHT / 10f, false);

                if(!init){
                    init = true;
                    new java.util.Timer().schedule(
                            new java.util.TimerTask() {
                                @Override
                                public void run() {
                                    wait = true;
                                }
                            },
                            500
                    );
                }

            }

            if (gsm == null && wait) {
                //tileMap = new TmxMapLoader().load(mapPath);
                tileMap = assets.get(mapPath);
                atlas = assets.get(spritesPackPath);
                setBackgrounds();
                gsm = new GameStateManager(this);
                Gdx.input.setInputProcessor(new MyInputProcessor());
                Gdx.input.setCatchBackKey(true);
                iSeverythingLoaded = true;
            }
            if(gsm != null){
                gsm.update(STEP);
                gsm.render();
                if (Gdx.input.isKeyPressed(Keys.BACK)) showConfirmDialog();
            }

        } else {
            resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            Gdx.gl.glViewport((int) viewport.x, (int) viewport.y,
                    (int) viewport.width, (int) viewport.height);

            progress = 100 * (assets.getLoadedAssets() / numberOfAssets);
            System.out.println("LOADING...  " + progress);

            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            sb.setProjectionMatrix(cam.combined);
            stage0.act();
            sb.begin();
            stage0.draw();
            sb.end();

            displayLoadingBar(112, 146, 190, V_HEIGHT / 10f, false);
        }
    }

    public void displayLoadingBar(int r, int g, int b, float posY, boolean border) {

        int numberOfRec = 10;
        float recSize = 40f;
        float space = 5f;
        float barWidth = numberOfRec * recSize + space * (recSize - 1);
        float barPosY = posY;
        float first = (V_WIDTH - barWidth) / 0.5f;

        Gdx.gl.glEnable(GL20.GL_BLEND);
        shapeRenderer.setProjectionMatrix(cam.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(r / 255f, g / 255f, b / 255f, 0.1f);
        if (progress > 0) shapeRenderer.rect(first, barPosY, recSize, recSize);
        shapeRenderer.setColor(r / 255f, g / 255f, b / 255f, 0.15f);
        if (progress > 10)
            shapeRenderer.rect(first + (space + recSize) * 1, barPosY, recSize, recSize);
        shapeRenderer.setColor(r / 255f, g / 255f, b / 255f, 0.2f);
        if (progress > 20)
            shapeRenderer.rect(first + (space + recSize) * 2, barPosY, recSize, recSize);
        shapeRenderer.setColor(r / 255f, g / 255f, b / 255f, 0.25f);
        if (progress > 30)
            shapeRenderer.rect(first + (space + recSize) * 3, barPosY, recSize, recSize);
        shapeRenderer.setColor(r / 255f, g / 255f, b / 255f, 0.3f);
        if (progress > 40)
            shapeRenderer.rect(first + (space + recSize) * 4, barPosY, recSize, recSize);
        shapeRenderer.setColor(r / 255f, g / 255f, b / 255f, 0.4f);
        if (progress > 50)
            shapeRenderer.rect(first + (space + recSize) * 5, barPosY, recSize, recSize);
        shapeRenderer.setColor(r / 255f, g / 255f, b / 255f, 0.55f);
        if (progress > 60)
            shapeRenderer.rect(first + (space + recSize) * 6, barPosY, recSize, recSize);
        shapeRenderer.setColor(r / 255f, g / 255f, b / 255f, 0.75f);
        if (progress > 70)
            shapeRenderer.rect(first + (space + recSize) * 7, barPosY, recSize, recSize);
        shapeRenderer.setColor(r / 255f, g / 255f, b / 255f, 0.85f);
        if (progress > 80)
            shapeRenderer.rect(first + (space + recSize) * 8, barPosY, recSize, recSize);
        shapeRenderer.setColor(r / 255f, g / 255f, b / 255f, 1f);
        if (progress > 90)
            shapeRenderer.rect(first + (space + recSize) * 9, barPosY, recSize, recSize);
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        if (border) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            Gdx.gl20.glLineWidth(2.4f);
            shapeRenderer.rect(first - recSize / 2, barPosY - recSize / 2, barWidth + recSize, recSize * 2);
            shapeRenderer.end();
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

    public void setBackgrounds() {
        Sprite tex;
        tex = new Sprite(MyGdxGame.atlas.findRegion("buttonSecret"));
        background_cloud = new Background(new TextureRegion(tex), cam, 1f);

        tex = new Sprite(MyGdxGame.atlas.findRegion("backgroundSky"));
        background_skyNight = new Background(new TextureRegion(tex), hudCam, 1f);

        tex = new Sprite(MyGdxGame.atlas.findRegion("backgroundSky"));
        tex.setScale(1, MyGdxGame.V_WIDTH);
        background_skyDay = new Background(new TextureRegion(tex), hudCam, 1f);

        tex = new Sprite(MyGdxGame.atlas.findRegion("backgroundWood1"));
        background_wood1 = new Background(new TextureRegion(tex), cam, 1f);

        tex = new Sprite(MyGdxGame.atlas.findRegion("backgroundTitle"));
        background_title = new Background(new TextureRegion(tex), cam, 1f);
        background_title.setVector(0, 0);

        tex = new Sprite(MyGdxGame.atlas.findRegion("backgroundTuto"));
        background_tuto = new Background(new TextureRegion(tex), cam, 1f);
        background_tuto.setVector(0, 0);




    }

    public static void updateBGM() {

        if(!isBoosTerritory) {
            if (soundEnable == 0 | soundEnable == 1) {
                if (res.getMusic("main").isPlaying()) res.getMusic("main").pause();
            } else {
                if (!MyGdxGame.pause) {
                    MyGdxGame.res.getMusic("main").setVolume(0.4f);
                    if (!res.getMusic("main").isPlaying()) res.getMusic("main").play();
                }
            }
        }
    }

    public static void setNightEnable(boolean n) {
        Save.load();
        Save.gd.setNight(n);
        Save.save();
        nightEnable = Save.gd.isNight();
        System.out.println("NIGHT: " + nightEnable);
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
        System.out.println("SOUND: " + soundEnable);
    }

    public void size(int width, int height) {
        // calculate new viewport
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
        offsetx = crop.x;
        offsety = crop.y;

    }

    public static void playContinueSound() {
        if (MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getSound("revive").play();
    }

    public static int isSoundEnable() {
        return soundEnable;
    }

    public void showConfirmDialog() {
        requestHandler.confirm(new ConfirmInterface() {
            @Override
            public void yes() {
                dispose();
                Gdx.app.exit();
            }

            @Override
            public void no() {
            }
        });
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
        if(!isBoosTerritory) {
            if (p) {
                res.getMusic("main").setVolume(0f);
                if (res.getMusic("main").isPlaying()) res.getMusic("main").pause();
            } else {
                res.getMusic("main").setVolume(0.4f);
                if (soundEnable == 2) res.getMusic("main").play();
            }

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

    public static boolean isNightEnable() {
        return nightEnable;
    }

    public void dispose() {
        assets.clear();
        tileMap.dispose();
        res.removeAll();
        sb.dispose();
        shapeRenderer.dispose();
        stage0.dispose();
        actionResolver = null;
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

    public static String getAchievementScore10() {
        return achievementScore10;
    }

    public static void setAchievementScore10(String achievementScore10) {
        MyGdxGame.achievementScore10 = achievementScore10;
    }

    public static String getAchievementScore50() {
        return achievementScore50;
    }

    public static void setAchievementScore50(String achievementScore50) {
        MyGdxGame.achievementScore50 = achievementScore50;
    }

    public static String getAchievementScore70() {
        return achievementScore70;
    }

    public static void setAchievementScore70(String achievementScore70) {
        MyGdxGame.achievementScore70 = achievementScore70;
    }

    public static String getAchievementScore80() {
        return achievementScore80;
    }

    public static void setAchievementScore80(String achievementScore80) {
        MyGdxGame.achievementScore80 = achievementScore80;
    }

    public static String getAchievementScore90() {
        return achievementScore90;
    }

    public static void setAchievementScore90(String achievementScore90) {
        MyGdxGame.achievementScore90 = achievementScore90;
    }

    public static String getAchievementScore100() {
        return achievementScore100;
    }

    public static void setAchievementScore100(String achievementScore100) {
        MyGdxGame.achievementScore100 = achievementScore100;
    }

    public static String getAchievementBossDefeated() {
        return achievementBossDefeated;
    }

    public static void setAchievementBossDefeated(String achievementBossDefeated) {
        MyGdxGame.achievementBossDefeated = achievementBossDefeated;
    }

    public static String getAchievementSecretLadyBug() {
        return achievementSecretLadyBug;
    }

    public static void setAchievementSecretLadyBug(String achievementSecretLadyBug) {
        MyGdxGame.achievementSecretLadyBug = achievementSecretLadyBug;
    }

    public static String getAchievementSecretBossShortcut() {
        return achievementSecretBossShortcut;
    }

    public static void setAchievementSecretBossShortcut(String achievementSecretBossShortcut) {
        MyGdxGame.achievementSecretBossShortcut = achievementSecretBossShortcut;
    }

    public static String getAchievementDecriptSecretMessage() {
        return achievementDecriptSecretMessage;
    }

    public static void setAchievementDecriptSecretMessage(String achievementDecriptSecretMessage) {
        MyGdxGame.achievementDecriptSecretMessage = achievementDecriptSecretMessage;
    }

}