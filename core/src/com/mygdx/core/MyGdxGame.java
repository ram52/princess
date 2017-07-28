package com.mygdx.core;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.mygdx.core.entities.ActionResolver;
import com.mygdx.core.entities.B2DSprite;
import com.mygdx.core.entities.Brick;
import com.mygdx.core.entities.Enemy;
import com.mygdx.core.entities.FireBall;
import com.mygdx.core.entities.Hand;
import com.mygdx.core.entities.Lightning;
import com.mygdx.core.entities.Player;
import com.mygdx.core.entities.Princess;
import com.mygdx.core.handlers.Animation;
import com.mygdx.core.handlers.Background;
import com.mygdx.core.handlers.BoundedCamera;
import com.mygdx.core.handlers.Content;
import com.mygdx.core.handlers.GameStateManager;
import com.mygdx.core.handlers.MyContactListener;
import com.mygdx.core.handlers.MyInputProcessor;
import com.mygdx.core.handlers.Save;
import com.mygdx.core.handlers.ScreenShake;
import com.mygdx.core.handlers.ShopDialog;
import com.mygdx.core.handlers.SimpleDirectionGestureDetector;
import com.mygdx.core.states.Shop;

import java.util.Random;
import java.util.concurrent.ScheduledExecutorService;

public class MyGdxGame implements ApplicationListener {
    private static final String LOG_TAG = MyGdxGame.class.getSimpleName();
    private Stage stage0;
    private Image intro;
    private FPSLogger fps;
    public static Rectangle viewport;
    public static ActionResolver actionResolver;
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
    RequestHandler requestHandler;
    public static float offsetx;
    public static float offsety;
    public static TiledMap tileMap;
    public static boolean iSeverythingLoaded = false;
    private static int soundEnable = 1;
    private static boolean android = true;
    public static B2DSprite fadeIn, fadeOut;
    public static float FADE_DELAY = 1/50f;
    public static BitmapFont font, font2, font2Credit;
    public static String debugString = "";
    public static float GROUND = 2.5621998f; //todo use box2d
    public static int MONEY_BY_ENEMY = 2;
    public static boolean DEBUG = false;
    public static boolean TEST = false;
    public static Vector2 lastPlayerPosition = new Vector2(0,0);
    public static Vector2 lastBrickPosition = new Vector2(0,0);
    public static Vector2 lastPrincessPosition = new Vector2(0,0);
    public static int lastScoreInTutorial = 0;

    public static GlyphLayout glyphLayoutCredit;

    public static boolean clickedOnLeaderboard = false;


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
    public static String errorSoundPath = "data/sound/error.mp3";
    public static String kameBeamSoundPath = "data/sound/kame.mp3";
    public static String jump1SoundPath = "data/sound/jump1.mp3";
    public static String deathSoundPath = "data/sound/death.mp3";
    public static String bossDeathSoundPath = "data/sound/bossDeath.mp3";
    public static String secretUnlockSoundPath = "data/sound/secretUnlock.mp3";
    public static String secretBossSoundPath = "data/sound/secretboss.mp3";
    public static String newScreenSoundPath = "data/sound/new_screen.mp3";
    public static String pointSoundPath = "data/sound/point.mp3";
    public static String fallingSoundPath = "data/sound/falling.mp3";
    public static String onSoundPath = "data/sound/on.mp3";
    public static String crushSoundPath = "data/sound/crush.mp3";
    public static String lightningSoundPath = "data/sound/lightning.mp3";
    public static String laughSoundPath = "data/sound/laugh.mp3";
    public static String slashSoundPath = "data/sound/slash.mp3";
    public static String pauseInSoundPath = "data/sound/pause1_in.mp3";
    public static String pauseOutSoundPath = "data/sound/pause1_out.mp3";
    public static String alarmSoundPath = "data/sound/alarm.mp3";
    public static String interactionSoundPath = "data/sound/interaction.mp3";
    public static String selectSoundPath = "data/sound/select.mp3";
    public static String equipedSoundPath = "data/sound/equiped.mp3";
    public static String moveSoundPath = "data/sound/move.mp3";
    public static String boomSoundPath = "data/sound/boom.mp3";
    public static String enemyHitMusicdPath = "data/sound/hit.mp3";
    public static String noAmmoSoundPath = "data/sound/no_ammo.mp3";
    public static String kamehamehaSoundPath = "data/sound/kamehameha.mp3";
    public static String fireballSmallSoundPath = "data/sound/fireball_small.mp3";
    public static String fireballBigSoundPath = "data/sound/fireball_big.mp3";
    public static String brokenSoundPath = "data/sound/broken.mp3";
    public static String titleMusicPath = "data/sound/title.mp3";
    public static String shopMusicPath = "data/sound/shop.mp3";
    public static String level1MusicPath = "data/sound/level1.mp3";
    public static String gameOverMusicPath = "data/sound/gameover.mp3";
    public static String bossMusicPath = "data/sound/boss.mp3";
    public static String openingMusicPath = "data/sound/opening.mp3";
    public static String successMusicPath = "data/sound/success.mp3";
    public static String fontCreditsPath = "data/font/m05a-12.fnt";
    public static String fontTextPath = "data/font/font1.fnt";
    public static String fontScorePath = "data/font/font2.fnt";
    public static String fontPointPath = "data/font/font3.fnt";
    public static String fontfsexPath = "data/font/FSEX300-32.fnt";
    public static String mapPath = "data/map/map1.tmx";
    public static String desktopIconPath = "data/sprite/icon/ic_launcher.png";
    public static String AndroidPlayStoreGameUrl = "https://play.google.com/store/apps/details?id=com.axldotm.runningbird";
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
    public static int TWENTY_COINS_STORE_PACK = 20;
    public static int HUNDRED_COINS_STORE_PACK = 100;
    public static int THOUSAND_COINS_STORE_PACK = 1000;
    private static boolean secretDiscovered = false;
    private static int TAPNUMB_SECRET = 50;
    private static boolean shortcutDiscovered = false;
    private static boolean wait = false;
    private static boolean init = false;
    public static int continueCount = 0;

    public static int pickedGameplay = -1;


    public static Color creditColor = new Color(0, 0, 0, 0.8f);

    ///////////CREDIT//////////////
    public static boolean click_on_playCredit;
    public static Stage stage1Credit, stage2Credit;
    public static AlphaAction fade1Credit, fade2Credit, fade3Credit;
    public static AlphaAction fadeCredit;
    public static Button buttonPlayCredit, buttonSecret1Credit, buttonSecret2Credit;
    public static int cpt_secret1 = 0;
    public static int cpt_translate_animation1Credit = 0;
    public static Animation animationPBlue1Credit;
    public static Animation animationPrincessCredit;
    public static Animation animationEnemyCredit;
    public static Animation animationHyaCredit;
    public static int timeCredit = 0;
    public static Rectangle viewportCredit;
    public static Stage stage0Credit;
    public static Image introCredit;
    public static String CREDIT = "";
    public static Vector2 credits_size = new Vector2(0,0);
    public static ShapeRenderer shapeRendererCredit;
    ///////////////////////////////
    
    //////////MENU/////////////////
    public static Animation animPlayerIdle, animPlayerIdleFliped, animPlayerSlash, animPrincessIdle, animPrincessIdleFliped, animTitle, animHelpMe;
    public static boolean click_on_playMenu, click_on_leaderboardMenu, click_on_tutoMenu;
    public static int isfadeOutStarted = 0;
    public static Stage stage1Menu , stage2Menu, stage3Menu, stageUiOptionMenu;
    public static Label labelCopyright;
    public static Button buttonPlayMenu, buttonLeaderBoardMenu, buttonTutoMenu, buttonSoundMenu, buttonGearMenu, buttonCreditsMenu, buttonPBlueMenu, buttonPRedMenu,
            buttonPYellow, buttonPGreen, buttonStore, buttonBackImage, buttonFacebook, buttonTwitter, buttonAchievement, buttonCloseUiOption, optionUiBackImage, imageCoinMenu;
    public static Skin skinMenu;
    public static Image loadingMenu;
    public static Rectangle viewportMenu;
    public static int cpt_blinkMenu = 0;
    public static boolean blinkMenu = false;
    public static float blinking_text_alphaMenu = 0;
    public static SpriteBatch sb2Menu, sb3Menu;
    public static boolean start2Menu = false;
    public static int cpt_translate_animation1Menu = 0;
    public static int cpt_translate_animation2Menu = 0;
    public static boolean click_on_creditsMenu = false;
    public static boolean click_on_shopMenu = false;
    public static boolean PBlueMenu = false, PRedMenu = false, PYellowMenu = false, playerIsTouchedMenu = false;
    public static int cptMenu = 0;
    public static int cpt_translate_animationMenu = 0;
    public static int cpt_translate_animation_titleMenu = 0;
    public static Label labelMoneyMenu;
    public static String code = "";
    public static float posXMenu = (MyGdxGame.V_WIDTH / 2f);
    public static float posX2Menu = (MyGdxGame.V_WIDTH / 2f);
    public static boolean rightMenu = true;
    public static boolean leftMenu = false;
    public static boolean right2Menu = true;
    public static boolean left2Menu = false;
    public static boolean jp = false;
    public static float yMenu = 670.0f;
    public static Stage stage0Menu;
    public static Image introMenu;
    public static InputMultiplexer im = new InputMultiplexer();
    ///////////////////////////////
    
    ////////SHOP///////////////////
    public static boolean click_on_playShop;
    public static Stage stage1Shop, stage2Shop;
    public static AlphaAction fade1Shop, fade2Shop, fade3Shop;
    public static Rectangle viewportShop;
    public static AlphaAction fadeShop;
    public static Button buttonPlayShop, buttonSecret1Shop, buttonSecret2Shop, imageCoinShop;
    public static Button buttonFireBall, buttonExcalibur, buttonKamehameha, buttonBoot;
    public static Button buttonFireBall2, buttonMegaJump, buttonLightning, buttonBrick2;
    public static Button buttonAds, buttonCoin1, buttonCoin2,buttonCoin3,buttonRestore;
    public static int cpt_secret1Shop = 0;
    public static int cpt_translate_animation1Shop = 0;
    public static Label labelMoneyShop;
    public static Skin skinShop;
    public static Vector2 crop;
    public static Animation animationCoinShop;
    public static Animation animationEnemyShop, animationEnemyMockShop;
    public static float timeShop = 0.0f;
    public static Stage stage0Shop;
    public static Image introShop;
    public static float offsetYShop = 0;
    public static SimpleDirectionGestureDetector gdShop;
    public static ShopDialog dialogAdsRemover;
    public static ShopDialog dialogFireBallNotPurchased, dialogFireBallPurchased;
    public static ShopDialog dialogFireBall2NotPurchased, dialogFireBall2Purchased;
    public static ShopDialog dialogExcaliburNotPurchased, dialogExcaliburPurchased;
    public static ShopDialog dialogKamehamehaNotPurchased, dialogKamehamehaPurchased;
    public static ShopDialog dialogBootNotPurchased, dialogBootPurchased;
    public static ShopDialog dialogBrickNotPurchased, dialogBrickPurchased;
    public static ShopDialog dialogLightningNotPurchased, dialogLightningPurchased;
    public static ShopDialog dialogMegaJumpNotPurchased, dialogMegaJumpPurchased;
    public static ShopDialog dialogCoin1NotPurchased, dialogCoin1Purchased;
    public static ShopDialog dialogCoin2NotPurchased, dialogCoin2Purchased;
    public static ShopDialog dialogCoin3NotPurchased, dialogCoin3Purchased;
    ///////////////////////////////
    

    ///////////PLAY////////////////
    public static World world;
    public static Box2DDebugRenderer b2dr;
    public static BoundedCamera b2dCam, cam2;
    public static MyContactListener cl;
    public static int tileMapWidth, tileMapHeight;
    public static float tileSize;
    public static OrthogonalTiledMapRenderer tmr;
    //public static Player player;
    public static Princess princess;
    public static Array<Enemy> enemies;
    public static boolean enemyIsNextLevel = false;
    public static Array<B2DSprite> bodyToDestroy;
    public static Brick brick;
    public static Lightning lightning;
    public static Array<FireBall> fireBalls;
    public static ScheduledExecutorService executor;
    public static int player_selector = 0;
    public static int cpt_alarm = 0;
    public static Stage stage1Play, stageUiContinue;
    public static Label labelScorePlay, labelMoneyPlay;
    public static Button.ButtonStyle pauseButtonStyle;
    public static Rectangle viewportPlay;
    public static Button buttonPausePlay;
    public static Button buttonSoundPlay;
    public static Button buttonPlayPlay;
    public static Button imageCoinPlay;
    public static Button buttonGp1_label, buttonGp2_label;
    public static Button buttonGp1, buttonGp2;
    public static Skin skinPlay;
    public static boolean playerStartMoving = false;
    public static int cpt_translate_animationPlay = 0;
    public static int enemiesKilled = 0;
    public static boolean isSlideInEnd = false;
    public static boolean gameOverPlay = false;
    public static boolean submitPlay = false;
    public static boolean firePlay = false;
    public static Button buttonCamera;
    public static Button buttonCoin;
    public static Button buttonNo;
    public static boolean left = false;
    public static boolean right = false;
    public static boolean buttonLeft_isTouched = false;
    public static boolean buttonRight_isTouched = false;
    public static boolean falling = false;
    public static boolean addNewEnemy = false;
    public static boolean addPowerUpOrBrick = false;
    public static boolean isMalicious = false;
    public static boolean toggle = true;
    public static boolean isTouchingEnemy = false;
    public static float lastClickPos = 0;
    public static float offsetYPlay = 0.0f;
    public static float beamWidth = 0.0f;
    public static float reloadKamehameha = 0.0f;
    public static float reloadLightning = 0.0f;
    public static boolean lightningRunning = false;
    public static double difficulty = 0.0;
    public static boolean blockKamehameha = false;
    public static boolean blockLightning = false;
    public static boolean kamehaReachLimit = false;
    public static boolean megaKameha = false;
    public static boolean lightningReachLimit = false;
    public static int maxEnemiesOnScreen = 10;
    public static Runnable runnable;
    public static Boolean stopEnemies = false;
    public static Boolean isStepping = false;
    public static Boolean step0 = false;
    public static Boolean step1 = false;
    public static Boolean step2 = false;
    public static Boolean step3 = false;
    public static Boolean step4 = false;
    public static Boolean step5 = false;
    public static Boolean jump = false;
    public static Boolean enableBrick = true;
    public static Button buttonLeft, buttonRight, buttonFire, buttonRed, buttonGray, buttonJump, buttonSkip;
    public static Stage stageUiControl;
    public static SpriteBatch spriteBatch;
    public static SpriteBatch spriteBatchLightning;
    public static Animation animKamehameha0, animKamehameha0_rev, animKamehameha1, animKamehameha1_rev, animKamehameha2, animKamehameha2_rev, animLabelMoney, animKillEnemy, animAh, animTip;
    public static BoundingBox boundingBoxKamehameha;
    public static double cpt_sound_hit = 0;
    public static double cpt_scream = 0;
    public static double cpt_scream_fade = 0;
    public static Stage stage0Play;
    public static ScreenShake screenShake;
    public static float baseX = 0f;
    public static float baseY = 0f;
    public static boolean isTutorial = false;
    public static boolean hideScream = false;
    public static Hand pointer;
    public static Boolean tuto_step0 = false;
    public static Boolean tuto_step1 = false;
    public static Boolean tuto_step2 = false;
    public static Boolean tuto_step3 = false;
    public static Boolean tuto_step4 = false;
    public static Boolean tuto_step5 = false;
    public static Boolean tuto_step6 = false;
    public static Boolean tuto_step7 = false;
    public static Boolean tuto_step8 = false;
    public static Boolean createdPrincessInTuTo = false;
    public static Boolean displayBrickTip = false;
    public static Boolean hidePointer = false;
    public static Image gamePlaySelection;
    public static SpriteBatch sbKyaa;
    public static int cpt_tuto = 0;
    public static float powerUpBar_MaxHeight = 0.0f;
    public static Random random = new Random();
    public static float space = 0;
    public static BoundingBox boundingBoxCastle;
    ///////////////////////////////


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

    public MyGdxGame(ActionResolver actionResolver) {
        this.actionResolver = actionResolver;
    }

    public static void getCreditFromFile(){
        String credits = ":( Sorry could not load credits file.";

        try {
            credits = Gdx.files.internal("data/credits.txt").readString();
        } catch (GdxRuntimeException e) {
            Gdx.app.error(LOG_TAG,"error while accessing file",e);
        }

        Gdx.app.error(LOG_TAG,"credit="+credits);

        font2Credit.getData().setScale(0.85f);
        glyphLayoutCredit.setText(font2Credit,credits);
        credits_size.set(glyphLayoutCredit.width,glyphLayoutCredit.height);

        credits_size.y = glyphLayoutCredit.height;


    }

    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        actionResolver.loginGPGS(true);


        //loading  = GifDecoder.loadGIFAnimation(com.badlogic.gdx.graphics.g2d.Animation.PlayMode.LOOP, Gdx.files.internal("data/sprite/loading.gif").readBytes());
        //font = new BitmapFont();
        fps = new FPSLogger();
        font = new BitmapFont(Gdx.files.internal(MyGdxGame.fontTextPath), false);
        font2 = new BitmapFont(Gdx.files.internal(MyGdxGame.fontfsexPath), false);
        font2Credit = new BitmapFont(Gdx.files.internal(MyGdxGame.fontfsexPath), false);
        glyphLayoutCredit = new GlyphLayout();


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
        Save.load();

        Save.gd.setFireBallPurchased(true);
        if(Save.gd.isFireBall2Equiped()| Save.gd.isKamehamehaEquiped() | Save.gd.isLightningEquiped()){
            Save.gd.setFireBallEquiped(false);
        }else{
            Save.gd.setFireBallEquiped(true);
        }
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
                //font.drawMultiLine(sb, debugString, MyGdxGame.V_WIDTH/15 , MyGdxGame.V_HEIGHT/1.1f, MyGdxGame.V_WIDTH, BitmapFont.HAlignment.LEFT);
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

        tex = new Sprite(MyGdxGame.atlas.findRegion("backgroundTitle"));
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
        actionResolver = null;
        shapeRenderer = null;
        gsm = null;
        res = null;
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