package com.mygdx.core.states;

import static com.mygdx.core.handlers.B2DVars.BIT_ENEMY;
import static com.mygdx.core.handlers.B2DVars.PPM;

import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.JointEdge;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.PerformanceCounter;
import com.badlogic.gdx.utils.Timer;
import com.mygdx.core.MyGdxGame;
import com.mygdx.core.entities.Brick;
import com.mygdx.core.entities.Coin;
import com.mygdx.core.entities.Enemy;
import com.mygdx.core.entities.FireBall;
import com.mygdx.core.entities.Player;
import com.mygdx.core.entities.Princess;
import com.mygdx.core.handlers.Animation;
import com.mygdx.core.handlers.B2DVars;
import com.mygdx.core.handlers.BoundedCamera;
import com.mygdx.core.handlers.GameStateManager;
import com.mygdx.core.handlers.MyContactListener;
import com.mygdx.core.handlers.Save;
import com.mygdx.core.handlers.SimpleDirectionGestureDetector;

public class Play extends GameState {

    private static int NJUMP = 2;
    private boolean debug = false;
    private boolean stopMain = false;
    private boolean jump1 = false;
    public World world;
    private Box2DDebugRenderer b2dr;
    private BoundedCamera b2dCam, cam2;
    private MyContactListener cl;
    private int tileMapWidth, tileMapHeight;
    private float tileSize;
    private OrthogonalTiledMapRenderer tmr;
    private Player player;
    private Princess princess;
    private Array<Enemy> enemies;
    //private Array<Brick> bricks;
    private Brick brick;
    private Array<FireBall> fireBalls;
    private ScheduledExecutorService executor;
    private Array<Coin> coins;
    long d = 3000;
    private int cpt0 = 0, cpt1 = 0, cpt2 = 0, cpt_cameraIntro = 0;
    private int player_selector = 0;
    private Stage stage1, stageUiContinue;
    private Label labelScore, labelMoney;
    private TextButtonStyle buttonStyle;
    private Button.ButtonStyle pauseButtonStyle;
    private TextButton button;
    private Rectangle viewport;
    private Image fade;
    private int fly = 0;
    private int cpt_fly;
    private Vector2 crop;
    private Button buttonPause, buttonSound, buttonPlay, imageCoin, imageUiBackground;
    private Skin skin;
    private boolean cameraMotionOver = false, playerStartMoving = false;
    private int cpt_translate_animation = 0;
    private int cptJump = 0;
    private int enemiesKilled = 0;
    private int jumpDelay = 0;
    private boolean isSlideInEnd = false;
    private boolean isSlideOutEnd = false;
    private  boolean uiIsSliding = false;
    private boolean gameover = false;
    private boolean submit = false;
    private boolean fire = false;
    private Button buttonCamera;
    private Button buttonCoin;
    private Button buttonNo;
    private boolean left = false;
    private boolean right = false;
    private boolean falling = false;
    private boolean addNewEnemy = false;
    private boolean addNewFireball = false;
    private boolean isMalicious = false;
    private boolean toggle = true;
    private boolean isTouchingEnemy = false;
    private float lastClickPos = 0;
    private float offsetY = 0.0f;
    private float beamWidth = 0.0f;
    private float beamX = 0.0f;
    private float reloadKamehameha = 0.0f;
    private float boost = 1.2f;
    private double dificulty = 0.0;
    boolean blockKamehameha = false;
    boolean kamehaReachLimit = false;
    private Runnable runnable;
    private Boolean stopEnemies = false;
    private Boolean step0 = false;
    private Boolean step1 = false;
    private Boolean step2 = false;
    private Boolean step3 = false;
    private Boolean jump = false;
    public Boolean enableBrick = true;
    private Button buttonLeft, buttonRight, buttonFire, buttonJump;
    private Stage stageUiControl;
    private SpriteBatch spriteBatch;
    private Animation animKamehameha0, animKamehameha0_rev, animKamehameha1, animKamehameha1_rev, animKamehameha2, animKamehameha2_rev;
    protected BoundingBox boundingBoxCastle, boundingBoxKamehameha;
    protected double cpt_sound_hit = 0;
    private Stage stage0;
    private Image intro;

    public PerformanceCounter performanceCounter;

    public boolean getRandomBoolean() {
        Random random = new Random();
        return random.nextBoolean();
    }

    public Play(final GameStateManager gsm) {
        super(gsm);

        performanceCounter = new PerformanceCounter(" ");

        MyGdxGame.setPause(false);
        viewport = new Rectangle();

        Save.load();

        /*if(MyGdxGame.isSoundEnable()) {
            MyGdxGame.res.getMusic("main").setVolume(0.15f);
            if(!MyGdxGame.res.getMusic("main").isPlaying())
                MyGdxGame.res.getMusic("main").play();
        }*/
        intro = new Image(MyGdxGame.atlas.findRegion("backgroundSky"));
        intro.setFillParent(true);
        stage0 = new Stage();
        stage0.addActor(intro);

        Vector2 origin = new Vector2(Gdx.graphics.getWidth()/2, 0);
        boundingBoxCastle = new BoundingBox();
        boundingBoxCastle.set(new Vector3((int)origin.x - Gdx.graphics.getWidth()/3.1f,(int)origin.y,0), new Vector3((int)origin.x+Gdx.graphics.getWidth()/3.1f,(int)origin.y+Gdx.graphics.getHeight(),10));

        boundingBoxKamehameha = new BoundingBox();

        Sprite tex = new Sprite(MyGdxGame.atlas.findRegion("kameha1"));
        TextureRegion[] sprites = tex.split(16, 43)[0];
        animKamehameha0 = new Animation(sprites, 1 / 5f);

        TextureRegion[] spritesFliped = tex.split(16, 43)[0];
        for (int i = 0; i < spritesFliped.length; i++)
            spritesFliped[i].flip(true, false);
        animKamehameha0_rev = new Animation(spritesFliped, 1 / 5f);

        tex = new Sprite(MyGdxGame.atlas.findRegion("kameha2"));
        sprites = tex.split(2, 43)[0];
        animKamehameha1 = new Animation(sprites, 1 / 5f);

        spritesFliped = tex.split(2, 43)[0];
        for (int i = 0; i < spritesFliped.length; i++)
            spritesFliped[i].flip(true, false);
        animKamehameha1_rev = new Animation(spritesFliped, 1 / 5f);

        tex = new Sprite(MyGdxGame.atlas.findRegion("kameha3"));
        sprites = tex.split(42, 43)[0];
        animKamehameha2 = new Animation(sprites, 1 / 5f);

        spritesFliped = tex.split(42, 43)[0];
        for (int i = 0; i < spritesFliped.length; i++)
            spritesFliped[i].flip(true, false);
        animKamehameha2_rev = new Animation(spritesFliped, 1 / 5f);


        if(Save.gd.isPlayerBlue() && Save.gd.isPlayerGreen() && Save.gd.isPlayerYellow() && Save.gd.isPlayerRed()){
            MyGdxGame.actionResolver.unlockAchievementGPGS(MyGdxGame.achievementPeace);
        }

        if(Save.gd.getSelector().equals("random")){
            for (int i = 0; i < 3; i++) {
                Random number = new Random();
                player_selector = number.nextInt(4);
            }
        }
        if(Save.gd.getSelector().equals("yellow")) {
            player_selector = 0;
            Save.gd.setPlayerYellow(true);
            Save.save();
        }
        if(Save.gd.getSelector().equals("green")){
            player_selector = 1;
            Save.gd.setPlayerGreen(true);
            Save.save();
        }
        if(Save.gd.getSelector().equals("red")){
            player_selector = 2;
            Save.gd.setPlayerRed(true);
            Save.save();
        }
        if(Save.gd.getSelector().equals("blue")){
            Save.gd.setPlayerBlue(true);
            player_selector = 3;
            Save.save();
        }

        // WINGS
        if (Save.gd.getFullBarPurchased() == false) {
            Save.gd.setWingState(1);
            if(MyGdxGame.isSecretDiscovered()) {
                Save.gd.setWingState(2);
                MyGdxGame.setSecretDiscovered(false);
            }
        }
        else
            Save.gd.setWingState(3);

        cpt_fly = (int) Save.gd.getWingState();

        // set up box2d
        world = new World(new Vector2(0, -9.81f), false);

        b2dr = new Box2DDebugRenderer();

        spriteBatch = new SpriteBatch();

        cam.setToOrtho(false, MyGdxGame.V_WIDTH, MyGdxGame.V_HEIGHT);
        b2dCam = new BoundedCamera();
        b2dCam.setToOrtho(false, MyGdxGame.V_WIDTH / PPM, MyGdxGame.V_HEIGHT
                / PPM);
        b2dCam.setBounds(0, (tileMapWidth * tileSize) / PPM, 0,
                (tileMapHeight * tileSize) / PPM);

        cam2 = new BoundedCamera();
        cam2.setToOrtho(false, MyGdxGame.V_WIDTH, MyGdxGame.V_HEIGHT);

        skin = new Skin();
        skin.addRegions(MyGdxGame.atlas);

        MyGdxGame.background_cloud.setVector(+10, 0);
        //MyGdxGame.background_skyDay.setVector(0, 0);
        MyGdxGame.background_wood1.setVector(+10, 0);

        stageUiContinue = new Stage();
        stageUiContinue.addAction(Actions.sequence(Actions.alpha(0.5f), Actions.fadeIn(0.2f)));

        Button.ButtonStyle style = new Button.ButtonStyle();
        style.up = skin.getDrawable("uiContinueBackground");
        style.down = skin.getDrawable("uiContinueBackground");
        imageUiBackground = new Button(style);
        imageUiBackground.setWidth(Gdx.graphics.getWidth());
        imageUiBackground.setHeight(Gdx.graphics.getHeight() / 11f);
        imageUiBackground.setPosition((Gdx.graphics.getWidth() - imageUiBackground.getWidth()) / 2, (Gdx.graphics.getHeight() - imageUiBackground.getHeight()) / 1.45f);
        stageUiContinue.addActor(imageUiBackground);

        Label labelContinue = new Label("", new LabelStyle(new BitmapFont( Gdx.files.internal(MyGdxGame.fontScorePath), false), Color.WHITE));
        labelContinue.setWidth(2 * (Gdx.graphics.getWidth()) / 3f);
        labelContinue.setHeight(Gdx.graphics.getHeight() / 8);
        labelContinue.setFontScaleY(Gdx.graphics.getWidth() / 210f);
        labelContinue.setFontScaleX(Gdx.graphics.getWidth() / 180f);
        labelContinue.setAlignment(Align.center);
        labelContinue.setPosition(((Gdx.graphics.getWidth() - labelContinue.getWidth()) / 2) + Gdx.graphics.getWidth() / 40f,
                ((Gdx.graphics.getHeight() - labelContinue.getHeight()) / 2) + labelContinue.getHeight() * 1.5f);

        Label labelContinueShadow = new Label("", new LabelStyle(new BitmapFont( Gdx.files.internal(MyGdxGame.fontScorePath), false), Color.BLACK));
        labelContinueShadow.setWidth(labelContinue.getWidth());
        labelContinueShadow.setHeight(labelContinue.getHeight());
        labelContinueShadow.setFontScaleX(labelContinue.getFontScaleX());
        labelContinueShadow.setFontScaleY(labelContinue.getFontScaleY());
        labelContinueShadow.setAlignment(Align.center);
        labelContinueShadow.setPosition(labelContinue.getX(), labelContinue.getY() - labelContinue.getHeight() / 14f);
        stageUiContinue.addActor(labelContinueShadow);
        stageUiContinue.addActor(labelContinue);

        Button.ButtonStyle cameraButtonStyle = new Button.ButtonStyle();
        cameraButtonStyle.up = skin.getDrawable("buttonUiContinueCameraUp");
        cameraButtonStyle.down = skin.getDrawable("buttonUiContinueCameraDown");
        buttonCamera = new Button(cameraButtonStyle);
        buttonCamera.setWidth(Gdx.graphics.getWidth() / 3f);
        buttonCamera.setHeight(Gdx.graphics.getHeight() / 6.5f);
        buttonCamera.setPosition(((Gdx.graphics.getWidth() - buttonCamera.getWidth()) / 2), labelContinue.getY() - buttonCamera.getHeight() * 1.09f);
        stageUiContinue.addActor(buttonCamera);

        Button.ButtonStyle coinButtonStyle = new Button.ButtonStyle();
        coinButtonStyle.up = skin.getDrawable("buttonUiContinueCoinUp");
        coinButtonStyle.down = skin.getDrawable("buttonUiContinueCoinDown");
        buttonCoin = new Button(coinButtonStyle);
        buttonCoin.setWidth(buttonCamera.getWidth());
        buttonCoin.setHeight(buttonCamera.getHeight());
        buttonCoin.setPosition(buttonCamera.getX(), buttonCamera.getY() - buttonCamera.getHeight() * 1.0f);
        stageUiContinue.addActor(buttonCoin);

        Button.ButtonStyle noButtonStyle = new Button.ButtonStyle();
        noButtonStyle.up = skin.getDrawable("buttonUiContinueNoUp");
        noButtonStyle.down = skin.getDrawable("buttonUiContinueNoDown");
        buttonNo = new Button(noButtonStyle);
        buttonNo.setWidth(buttonCamera.getWidth());
        buttonNo.setHeight(buttonCamera.getHeight());
        buttonNo.setPosition(buttonCamera.getX(), buttonCoin.getY() - buttonNo.getHeight() * 1.0f);
        stageUiContinue.addActor(buttonNo);

        stageUiControl = new Stage();
        stageUiControl.addAction(Actions.sequence(Actions.alpha(0.5f), Actions.fadeIn(0.2f)));
        Button.ButtonStyle leftButtonStyle = new Button.ButtonStyle();
        leftButtonStyle.up = skin.getDrawable("buttonUiBossLeftUp");
        leftButtonStyle.down = skin.getDrawable("buttonUiBossLeftDown");
        buttonLeft = new Button(leftButtonStyle);
        float size = (Gdx.graphics.getWidth() / 4.5f);
        float space = (Gdx.graphics.getWidth() - (size*4))/5;
        buttonLeft.setWidth(size);
        buttonLeft.setHeight(size);
        buttonLeft.setPosition(space, space*2);
        //buttonLeft.setBounds(0,0,Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight());
        stageUiControl.addActor(buttonLeft);



        Button.ButtonStyle rightButtonStyle = new Button.ButtonStyle();
        rightButtonStyle.up = skin.getDrawable("buttonUiBossRightUp");
        rightButtonStyle.down = skin.getDrawable("buttonUiBossRightDown");
        buttonRight = new Button(rightButtonStyle);
        buttonRight.setWidth(buttonLeft.getWidth());
        buttonRight.setHeight(buttonLeft.getHeight());
        buttonRight.setPosition(buttonLeft.getRight()+space, buttonLeft.getY());
        stageUiControl.addActor(buttonRight);

        Button.ButtonStyle fireButtonStyle = new Button.ButtonStyle();
        if(Save.gd.isFireBallEquiped() | Save.gd.isKamehamehaEquiped()){
            fireButtonStyle.up = skin.getDrawable("buttonUiBossJumpUp");
            fireButtonStyle.down = skin.getDrawable("buttonUiBossJumpDown");
        }else{
            fireButtonStyle.up = skin.getDrawable("buttonSecret");
            fireButtonStyle.down = skin.getDrawable("buttonSecret");
        }


        buttonFire = new Button(fireButtonStyle);
        buttonFire.setWidth(buttonLeft.getWidth());
        buttonFire.setHeight(buttonLeft.getHeight());
        buttonFire.setPosition(buttonRight.getRight()+space, buttonLeft.getY());
        stageUiControl.addActor(buttonFire);


        Button.ButtonStyle jumpButtonStyle = new Button.ButtonStyle();
        jumpButtonStyle.up = skin.getDrawable("buttonJumpUp");
        jumpButtonStyle.down = skin.getDrawable("buttonJumpDown");
        buttonJump = new Button(jumpButtonStyle);
        buttonJump.setDisabled(true);
        buttonJump.setWidth(buttonFire.getWidth());
        buttonJump.setHeight(buttonFire.getHeight());
        buttonJump.setPosition(buttonFire.getRight()+space, buttonLeft.getY());
        stageUiControl.addActor(buttonJump);






        buttonFire.addListener(new InputListener() {
            public boolean touchDown(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                if(Save.gd.isFireBallEquiped()){
                    System.out.println("clicked fire!");
                    fire = true;
                }else if(Save.gd.isKamehamehaEquiped() && !blockKamehameha){
                    fire = true;
                    if((MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2)&& reloadKamehameha == 0 && beamWidth == 0){
                        MyGdxGame.res.getSound("ya").play();
                    }
                }else{
                    fire = false;
                }

                return true;
            }

            ;

            public void touchUp(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                //System.out.println("clicked fire!");
                //fire = false;

            }

            ;
        });


      //stageUiControl.addActor(buttonJump);

        buttonJump.addListener(new InputListener() {
            public boolean touchDown(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {

                jump = true;

                return true;
            }

            ;

            public void touchUp(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                //System.out.println("clicked fire!");
                //fire = false;
                jump = false;

            }

            ;
        });



















        buttonLeft.addListener(new InputListener() {
            public boolean touchDown(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                left = true;

                return true;
            }

            ;

            public void touchUp(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                //System.out.println("clicked left!");
                left = false;

            }

            ;
        });

        buttonRight.addListener(new InputListener() {
            public boolean touchDown(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                right = true;

                return true;
            }

            ;

            public void touchUp(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                //System.out.println("clicked right!");
                right = false;

            }

            ;
        });


        stage1 = new Stage();
        stage1.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(0.2f)));

        labelScore = new Label("0", new LabelStyle(new BitmapFont(Gdx.files.internal(MyGdxGame.fontPointPath), false), Color.WHITE));
        labelScore.setFontScaleY(Gdx.graphics.getWidth() / 450f);
        labelScore.setFontScaleX(Gdx.graphics.getHeight() / 900f);
        labelScore.setPosition((Gdx.graphics.getWidth() - labelScore.getWidth()) / 2,
                Gdx.graphics.getHeight() / 1.27f);
        labelScore.setAlignment(Align.center);
        stage1.addActor(labelScore);

        createTiles();
        createPlayer();
        //createBrick(MyGdxGame.V_WIDTH/5/PPM, player.getPosition().y - player.getHeight()/2.8f/PPM);

        createPrincess(MyGdxGame.V_WIDTH/2/PPM, 2*MyGdxGame.GROUND);

        enemies = new Array<Enemy>();

        fireBalls = new Array<FireBall>();

        executor = Executors.newScheduledThreadPool(1);

        runnable = new Runnable() {
            public void run() {
                //System.out.println("addNewEnemy! "+"stopEnemies:"+stopEnemies+"MyGdxGame.pause:"+MyGdxGame.pause);
                if(!MyGdxGame.pause){
                    if(!stopEnemies){
                        addNewEnemy = true;
                        toggle = !toggle;

                        isMalicious = getRandomBoolean();

                        //d = (d < 500)? 500:3000-player.getNumCoins()*100;

                        if(player.getNumCoins() >= 5 && !step0){
                            step0 = true;
                            executor = Executors.newScheduledThreadPool(1);
                            executor.scheduleAtFixedRate(runnable, 0, 2500, TimeUnit.MILLISECONDS);
                        }

                        if(player.getNumCoins() >= 10 && !step1){
                            step1 = true;
                            executor = Executors.newScheduledThreadPool(1);
                            executor.scheduleAtFixedRate(runnable, 0, 2000, TimeUnit.MILLISECONDS);
                        }

                        if(player.getNumCoins() >= 20 && !step2){
                            step2 = true;
                            executor = Executors.newScheduledThreadPool(1);
                            executor.scheduleAtFixedRate(runnable, 0, 1000, TimeUnit.MILLISECONDS);
                        }

//                        if(player.getNumCoins() >= 50 && !step3){
//                            step3 = true;
//                            executor = Executors.newScheduledThreadPool(1);
//                            executor.scheduleAtFixedRate(runnable, 0, 800, TimeUnit.MILLISECONDS);
//                        }

                    }
                }
            }
        };

        executor.scheduleAtFixedRate(runnable, 0, 3000, TimeUnit.MILLISECONDS);

        //enemies.add(createEnemy(MyGdxGame.V_WIDTH*1.0f/PPM, MyGdxGame.GROUND, false));
        //enemies.add(createEnemy(1/PPM, MyGdxGame.GROUND, true));

        cl = new MyContactListener(this);
        world.setContactListener(cl);

        player.setNumCoins(0);

        pauseButtonStyle = new Button.ButtonStyle();
        pauseButtonStyle.up = skin.getDrawable("buttonPause");
        pauseButtonStyle.down = skin.getDrawable("buttonPause");
        buttonPause = new Button(pauseButtonStyle);
        buttonPause.setWidth(Gdx.graphics.getWidth() / 8f);
        buttonPause.setHeight(Gdx.graphics.getWidth() / 8f);
        buttonPause.setPosition((Gdx.graphics.getWidth() - buttonPause.getWidth() - 20),
                (Gdx.graphics.getHeight() - buttonPause.getHeight() - 60));
        stage1.addActor(buttonPause);

        coinButtonStyle = new Button.ButtonStyle();
        coinButtonStyle.up = skin.getDrawable("coin");
        coinButtonStyle.down = skin.getDrawable("coin");
        imageCoin = new Button(coinButtonStyle);
        imageCoin.setWidth(buttonPause.getWidth());
        imageCoin.setHeight(buttonPause.getWidth());
        imageCoin.setPosition(imageCoin.getWidth() / 4, buttonPause.getY());
        stage1.addActor(imageCoin);

        labelMoney = new Label("0", new LabelStyle(new BitmapFont(Gdx.files.internal(MyGdxGame.fontPointPath), false), Color.WHITE));
        labelMoney.setFontScale(Gdx.graphics.getWidth() / 1200f);
        labelMoney.setWidth(Gdx.graphics.getWidth() / 2);
        labelMoney.setHeight(imageCoin.getHeight());
        labelMoney.setAlignment(Align.center | Align.left);
        labelMoney.setPosition(imageCoin.getRight() * 1.1f, imageCoin.getY());
        stage1.addActor(labelMoney);

        Label labelPause = new Label("PAUSE", new LabelStyle(new BitmapFont( Gdx.files.internal(MyGdxGame.fontCreditsPath), false), Color.WHITE));

        labelPause.setWidth(Gdx.graphics.getWidth() / 2);
        labelPause.setHeight(Gdx.graphics.getHeight() / 8);
        labelPause.setFontScale(Gdx.graphics.getWidth() / 100f);
        labelPause.setAlignment(Align.center);
        labelPause.setPosition((Gdx.graphics.getWidth() - labelPause.getWidth()) / 2, (Gdx.graphics.getHeight() - labelPause.getHeight()) / 2);
        //stage1.addActor(labelPause);

        Skin skinButtonPlay = new Skin();
        skinButtonPlay.addRegions(MyGdxGame.atlas);
        Button.ButtonStyle buttonStylePlay = new Button.ButtonStyle();
        buttonStylePlay.up = skinButtonPlay.getDrawable("buttonExit2Up");
        buttonStylePlay.down = skinButtonPlay.getDrawable("buttonExit2Down");
        buttonPlay = new Button(buttonStylePlay);
        buttonPlay.setWidth(Gdx.graphics.getWidth() / 5f);
        buttonPlay.setHeight(Gdx.graphics.getHeight() / 7.8f);
        buttonPlay.setPosition(-400, Gdx.graphics.getHeight() / 36f);

        stage1.addActor(buttonPlay);


        Button.ButtonStyle soundButtonStyle = new Button.ButtonStyle();
//        soundButtonStyle.up = skin.getDrawable("buttonSound2Up");
//        soundButtonStyle.down = skin.getDrawable("buttonSound2Down");
        buttonSound = new Button(soundButtonStyle);
        buttonSound.setWidth(Gdx.graphics.getWidth() / 5f);
        buttonSound.setHeight(Gdx.graphics.getHeight() / 7.8f);
        buttonSound.setPosition((Gdx.graphics.getWidth() / 1f) + 400, Gdx.graphics.getHeight() / 36f);

        stage1.addActor(buttonSound);

        //if(sound) MyGdxGame.res.getMusic("main").play();

        //Gdx.input.setInputProcessor();

        SimpleDirectionGestureDetector gd = new SimpleDirectionGestureDetector(
                new SimpleDirectionGestureDetector.DirectionListener() {

                    @Override
                    public void onUp() {
                        if(!player.isPlayerDead() && !MyGdxGame.pause ) {
                            jump = true;

                            System.out.println("up");
                        }
                    }

                    @Override
                    public void onRight() {
                        System.out.println("right");
                    }

                    @Override
                    public void onLeft() {
                        System.out.println("left");
                    }

                    @Override
                    public void onDown() {

                        if(Save.gd.isBrickEquiped()){
                            if(brick == null){
                                brick = createBrick(MyGdxGame.V_WIDTH/5/PPM, player.getHeight()/PPM + Gdx.graphics.getHeight()/PPM);
                            }else{
                                if(brick.getDead()){
                                    brick = createBrick(MyGdxGame.V_WIDTH/5/PPM, player.getHeight()/PPM + Gdx.graphics.getHeight()/PPM);
                                }
                            }

                            if(!MyGdxGame.pause){
                                if(!brick.isSummoned() && enableBrick){
                                    enableBrick = false;
                                    brick.setFalling(true);
                                    brick.setSummoned(true);
                                    if(MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getSound("newScreen").play();
                                }
                            }

                            System.out.println("down");
                        }

                    }
                });

        buttonNo.addListener(new InputListener() {
            public boolean touchDown(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                labelMoney.setColor(Color.WHITE);
                return true;
            }

            ;

            public void touchUp(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                System.out.println("clicked no!");
                if(MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getSound("select").play();
                System.out.println("GAME OVER");
                MyGdxGame.setContinue(false);
                submit = true;
                gsm.setState(GameStateManager.GAME_OVER);

            }

            ;
        });

        buttonCoin.addListener(new InputListener() {
            public boolean touchDown(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                return true;
            }

            ;

            public void touchUp(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                System.out.println("clicked coin!");

                Save.load();

                if(Save.gd.getMoney() >= MyGdxGame.getContinuePrice()){
                    Save.gd.setMoney(Save.gd.getMoney() - MyGdxGame.getContinuePrice());
                    Save.save();
                    Save.load();
                    submit = false;
                    stopEnemies = false;


                    //player.setPlayerDead(false);
                    MyGdxGame.setContinue(true);
                    if(MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getSound("revive").play();

                    //TODO Remove all current enemies
                    for (Enemy enemy:enemies) {
                        enemy.setDead(true);
                        enemy.setCptDieRunning(100);
                        enemies.removeValue(enemy, true);
                        //removeBodySafely(enemy.getBody());
                        enemy.destroy();
                    }
                    princess.setTouched(false);
                    princess.setCry(false);
                    MyGdxGame.pause = false;
                }else{
                    if(MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getSound("error").play();
                    labelMoney.setColor(Color.RED);
                    Timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            labelMoney.setColor(Color.WHITE);
                        }
                    }, 0.2f);

                    //BUY MORE
                    MyGdxGame.actionResolver.purchaseExtraCoins();
                    labelMoney.setText(Integer.toString(Save.gd.getMoney()));
                    labelMoney.setColor(Color.WHITE);
                }

            }

            ;
        });

        buttonCamera.addListener(new InputListener() {
            public boolean touchDown(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                labelMoney.setColor(Color.WHITE);
                return true;
            }

            ;

            public void touchUp(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                System.out.println("clicked camera!");
                if(MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getSound("select").play();
                //MyGdxGame.actionResolver.showOrLoadInterstitalVideo();

                String network = game.actionResolver.getNetworkClass();
                if(network == null) network = "ABSENT";
                System.out.println("NETWORK: " + network);
                if(network.equals("4G")|network.equals("3G")|network.equals("WIFI")) {
                    game.actionResolver.showOrLoadRewardedVideoChartboost();
                    //game.actionResolver.showRewardedVideoChartBoost();
                }
            }

            ;
        });

        buttonPause.addListener(new InputListener() {
            public boolean touchDown(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                return true;
            }

            ;

            public void touchUp(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                if(!gameover){
                    System.out.println("clicked pause!");
                    if(MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getSound("newScreen").play();
                    MyGdxGame.setPause(!MyGdxGame.pause);
                }
            }

            ;
        });

        buttonPlay.addListener(new InputListener() {
            public boolean touchDown(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                return true;
            }

            ;

            public void touchUp(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                System.out.println("clicked play!");
                gsm.setState(GameStateManager.MENU);
                if(MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getSound("newScreen").play();

            }

            ;
        });

        buttonSound.addListener(new InputListener() {
            public boolean touchDown(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                return true;
            }

            ;

            public void touchUp(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                System.out.println("clicked mute!");
                if(MyGdxGame.isSoundEnable() == 2){
                    MyGdxGame.setSoundEnable(0);
                }else{
                    MyGdxGame.setSoundEnable(MyGdxGame.isSoundEnable()+1);
                }

                if(MyGdxGame.isSoundEnable() == 0) {
                    MyGdxGame.res.getSound("select").play();
                    Button.ButtonStyle soundButtonStyle = new Button.ButtonStyle();
                    soundButtonStyle.up = skin.getDrawable("buttonSound1Mute");
                    soundButtonStyle.down = skin.getDrawable("buttonSound1Mute");
                    buttonSound.setStyle(soundButtonStyle);
                }

                if(MyGdxGame.isSoundEnable() == 1) {
                    MyGdxGame.res.getSound("select").play();
                    Button.ButtonStyle soundButtonStyle = new Button.ButtonStyle();
                    soundButtonStyle.up = skin.getDrawable("buttonSound1Fx");
                    soundButtonStyle.down = skin.getDrawable("buttonSound1Fx");
                    buttonSound.setStyle(soundButtonStyle);
                }

                if(MyGdxGame.isSoundEnable() == 2) {
                    MyGdxGame.res.getSound("select").play();
                    if (MyGdxGame.res.getMusic("main").isPlaying())
                        MyGdxGame.res.getMusic("main").pause();
                    Button.ButtonStyle soundButtonStyle = new Button.ButtonStyle();
                    soundButtonStyle.up = skin.getDrawable("buttonSound1Full");
                    soundButtonStyle.down = skin.getDrawable("buttonSound1Full");
                    buttonSound.setStyle(soundButtonStyle);
                }
            }

            ;
        });

        buttonNo.setVisible(false);
        buttonCoin.setVisible(false);
        buttonCamera.setVisible(false);

        stage1.addListener(new InputListener() {
            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                if (keycode == Input.Keys.LEFT) {
                    left = false;
                }
                if (keycode == Input.Keys.RIGHT) {
                    right  = false;
                }

                if (keycode == Input.Keys.ENTER | keycode == Input.Keys.SPACE) {
                    if(!blockKamehameha){
                        fire  = true;
                        System.out.println("pressed enter!");
                    }
                    InputEvent event2 = new InputEvent();
                    event2.setType(InputEvent.Type.touchUp);
                    buttonFire.fire(event2);
                }
                return false;
            }

            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.LEFT) {
                    left = true;
                }
                if (keycode == Input.Keys.RIGHT) {
                    right = true;
                }
                if (keycode == Input.Keys.ENTER | keycode == Input.Keys.SPACE) {
                    fire  = false;
                    InputEvent event1 = new InputEvent();
                    event1.setType(InputEvent.Type.touchDown);
                    buttonFire.fire(event1);
                }
                return false;
            }
        });




        InputMultiplexer im = new InputMultiplexer();
        im.addProcessor(gd);
        im.addProcessor(stageUiContinue);
        im.addProcessor(stage1);
        im.addProcessor(stageUiControl);
        Gdx.input.setInputProcessor(im);

        MyGdxGame.setIsBoosTerritory(false);

        System.out.println("init completed without errors.");


    }

    public void handleInput() {

        //bound player in screen
        if(player.getPosition().x - player.getWidth()/2/PPM < 0){
            //System.out.println("OUT");
            player.getBody().setTransform(player.getWidth()/2/PPM , player.getPosition().y, player.getBody().getAngle());
        }

        if(player.getPosition().x + player.getWidth()/2/PPM >  MyGdxGame.V_WIDTH/PPM){
            //System.out.println("OUT");
            player.getBody().setTransform(MyGdxGame.V_WIDTH/PPM - player.getWidth()/2/PPM, player.getPosition().y, player.getBody().getAngle());
        }

        //player not moving
        if((!left | !right) && cl.isPlayerOnGround()){

            if(Save.gd.isExcaliburEquiped() | Save.gd.isKamehamehaEquiped()) {
                if (player.getBody().getLinearVelocity().x != 0 && Math.abs(player.getBody().getLinearVelocity().x) <= MyGdxGame.PLAYER_VELOCITY * boost) {
                    //System.out.println("STILL");
                    player.getBody().setLinearVelocity(0, player.getBody().getLinearVelocity().y);
                } else {
                    if (player.getBody().getLinearVelocity().x != 0 && Math.abs(player.getBody().getLinearVelocity().x) <= MyGdxGame.PLAYER_VELOCITY) {
                        //System.out.println("STILL");
                        player.getBody().setLinearVelocity(0, player.getBody().getLinearVelocity().y);
                    }
                }

            }else{
                player.getBody().setLinearVelocity(0, player.getBody().getLinearVelocity().y);
            }


        }


        /**Handle keyboard input**/
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            if(cl.isPlayerOnGround() && !jump && (!player.isSlashingLeft()|!player.isSlashingRight()) && (player.getPosition().y*PPM) < 325 ){
                player.getBody().setLinearVelocity(new Vector2(player.getBody().getLinearVelocity().x,0));
                player.getBody().setAngularVelocity(0);
                player.getBody().applyForceToCenter(new Vector2(0,160),false);
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
//            //todo summon brick
//            brick = createBrick(MyGdxGame.V_WIDTH/5/PPM, player.getHeight()/PPM + Gdx.graphics.getHeight()/PPM);
//            if(!MyGdxGame.pause){
//                if(!brick.isSummoned() && enableBrick){
//                    enableBrick = false;
//                    brick.setFalling(true);
//                    brick.setSummoned(true);
//                    if(MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getSound("newScreen").play();
//                }
//            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            InputEvent event1 = new InputEvent();
            event1.setType(InputEvent.Type.touchDown);
            buttonLeft.fire(event1);

            InputEvent event2 = new InputEvent();
            event2.setType(InputEvent.Type.touchUp);
            buttonLeft.fire(event2);
            left = true;
            right = false;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) ) {
            InputEvent event1 = new InputEvent();
            event1.setType(InputEvent.Type.touchDown);
            buttonRight.fire(event1);

            InputEvent event2 = new InputEvent();
            event2.setType(InputEvent.Type.touchUp);
            buttonRight.fire(event2);
            right = true;
            left = false;
        }

        //FIRE BALL
        if(Save.gd.isFireBallEquiped() && fire && player.getFireBallCount()>0){
            fire = false;
            player.setFireBallCount(player.getFireBallCount()-1);

            //todo sound
            if (MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getSound("fireball").play();
            float gap = 10.0f/PPM;
            if(player.isRight()){
                FireBall fireball = createFireBall(player.getPosition().x + gap, player.getPosition().y,2);
                if(!fireball.getRight())
                    fireball.normalAnimation_rev();
                fireBalls.add(fireball);
            }
            else {
                fireBalls.add(createFireBall(player.getPosition().x - gap, player.getPosition().y,-2));
            }
        }


        /*if(!Gdx.input.isTouched()){
            right = false;
            left = false;
        }*/


        //todo click style
//        if(Gdx.input.isTouched()){
//            lastClickPos = Gdx.input.getX()/PPM;
//        }
//        lastClickPos = Float.valueOf(String.format("%.1f", lastClickPos));
//
//        float pos = Float.valueOf(String.format("%.1f", player.getPosition().x));
//
//        System.out.println(lastClickPos+"*****"+pos);
//        //clicked to the right of player
//        if(lastClickPos > pos){
//            right = true;
//            left = false;
//        }
//        if(lastClickPos < pos){
//            //clicked to the left of player
//            right = false;
//            left = true;
//        }
//
//        if(lastClickPos == pos){
//            right = false;
//            left = false;
//        }
//
        //moves left

        if (left && player.getBody().getLinearVelocity().x != -MyGdxGame.PLAYER_VELOCITY) {
            //System.out.println("LEFT");
            if(Save.gd.isExcaliburEquiped() | Save.gd.isKamehamehaEquiped()) {
                player.getBody().setLinearVelocity(-MyGdxGame.PLAYER_VELOCITY*boost, player.getBody().getLinearVelocity().y);
            }else{
                player.getBody().setLinearVelocity(-MyGdxGame.PLAYER_VELOCITY, player.getBody().getLinearVelocity().y);
            }

        }

        //moves right
        if (right && player.getBody().getLinearVelocity().x != MyGdxGame.PLAYER_VELOCITY) {
            //System.out.println("RIGHT");
            if(Save.gd.isExcaliburEquiped() | Save.gd.isKamehamehaEquiped()) {
                player.getBody().setLinearVelocity(MyGdxGame.PLAYER_VELOCITY*boost, player.getBody().getLinearVelocity().y);
            }else{
                player.getBody().setLinearVelocity(MyGdxGame.PLAYER_VELOCITY, player.getBody().getLinearVelocity().y);
            }

        }

        //SIMPLE JUMP
        /*if(!MyGdxGame.pause && Gdx.input.isTouched() && cl.isPlayerOnGround()){

            jump1 = false;

            if (MyGdxGame.isSoundEnable()) MyGdxGame.res.getSound("jump1").play();

            //if(player.getBody().getLinearVelocity().x > 0) {
            if(!player.isFlyingRight()){
                player.getBody().applyForceToCenter(new Vector2(0,180),false);
                player.flying_animation(player_selector);
            }
            //}

            if(player.getBody().getLinearVelocity().x == 0) {

                //System.out.println("STILL RIGHT:"+player.isStillRight() + " STILL LEFT:"+player.isStillLeft() + " RUNNING RIGHT:"+player.isRunningRight() + " RUNNING LEFT:"+player.isRunningLeft()+ " FLYING LEFT:"+player.isFlyingLeft()+ " FLYING RIGHT:"+player.isFlyingRight());

                if(!player.isFlyingRight() && player.isStillRight()){
                    player.getBody().applyForceToCenter(new Vector2(0,180),false);
                    player.flying_animation(player_selector);
                }

                if(!player.isFlyingLeft() && player.isStillLeft()){
                    player.getBody().applyForceToCenter(new Vector2(0,180),false);
                    player.flying_animation_rev(player_selector);
                }

            }

        }*/

    }

    public void makeEnemySensor(Enemy enemy, boolean isSensor){
        for (Fixture fixture: enemy.getBody().getFixtureList()) {
            fixture.setSensor(isSensor);
        }
    }

    public void  updatePlayerAnimation(){

        //TODO fix
        // UPDATE ON THE GROUND ANIMATION
        Iterator<Enemy> iter = enemies.iterator();
        while (iter.hasNext()) {
            Enemy enemy = iter.next();
            player.updateBoundingBox(player);
            princess.updateBoundingBox(princess);
            enemy.updateBoundingBox(enemy);

            if(cl.intersect(player,enemy)){
                makeEnemySensor(enemy,true);
            }

            if(player.getBoundingBox().intersects(enemy.getBoundingBox()) && !enemy.isFading() && !enemy.isDead()){

                //slow down enemy
                if(!enemy.isStop())
                    enemy.getBody().setLinearVelocity(new Vector2(enemy.getSpeed()/6, enemy.getBody().getLinearVelocity().y));

                player.getBody().setLinearVelocity(new Vector2(player.getBody().getLinearVelocity().x/6, player.getBody().getLinearVelocity().y));

                if(Save.gd.isExcaliburEquiped()){
                    enemy.setHealth(enemy.getHealth()-3);
                }else{
                    enemy.setHealth(enemy.getHealth()-1);
                }



                if(!MyGdxGame.pause){
                    if (MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2){
                        if(!MyGdxGame.res.getMusic("slash").isPlaying()){
                            MyGdxGame.res.getMusic("slash").play();
                        }
                    }
                }

                enemy.setTouched(true);
                isTouchingEnemy = true;
            }else{
                enemy.setTouched(false);
            }

            //System.out.println(enemy.getPosition().x*PPM+" "+beamX);
            //kamehameha
            if(enemy.getBoundingBox().intersects(boundingBoxKamehameha)){
                enemy.setTouched(true);
                enemy.setHealth(enemy.getHealth()-0.6f);
            }else{
                enemy.setTouched(false);
            }

            if(isTouchingEnemy && enemy.isFromLeft() && player.isStillRight()){
                player.setLeft(true);
                player.setRight(false);
                player.setStillRight(false);
                player.setStillLeft(true);
            }

            if(isTouchingEnemy && !enemy.isFromLeft() && player.isStillLeft()){
                player.setLeft(false);
                player.setRight(true);
                player.setStillRight(true);
                player.setStillLeft(false);
            }
        }

        if(!isTouchingEnemy){
            if(player.getBody().getLinearVelocity().x > 0){
                if(!player.isRunningRight())player.running_animation(player_selector);
            }

            if(player.getBody().getLinearVelocity().x < 0){
                if(!player.isRunningLeft()) player.running_animation_rev(player_selector);
            }

            if(player.getBody().getLinearVelocity().x == 0){

                if(!player.isStillRight()) {

                    if (player.isRunningRight() | player.isSlashingRight()) {
                        player.still_animation(player_selector);
                    }
                }

                if(!player.isStillRight()) {

                    if(player.isRunningLeft()  | player.isSlashingLeft()){
                        player.still_animation_rev(player_selector);
                    }
                }
            }

        }else{

            //System.out.println("touch");
            if ((player.isStillRight() | player.isRunningRight())) {
                player.slash_animation(player_selector);
            }
            if ((player.isStillLeft() | player.isRunningLeft())) {
                player.slash_animation_rev(player_selector);
            }

            isTouchingEnemy = false;
        }



        if(jump && cl.isPlayerOnGround()){
            player.getBody().applyForceToCenter(new Vector2(0,20),false);
            if(player.isRight() | player.isStillRight()){
                if(!player.isJumpRight()){
                    player.jump_animation();
                }
            }else{
                if(!player.isJumpLeft()){
                    player.jump_animation_rev();
                }
            }
        }




//        }else{
//
//

//
//        }






        // UPDATE IN THE AIR ANIMATION
        /*if(!cl.isPlayerOnGround() && !player.isTiredLeft() && !player.isTiredRight()){
            if(player.getBody().getLinearVelocity().x > 0){
                if(!player.isFlyingRight())player.flying_animation(player_selector);
            }
            if(player.getBody().getLinearVelocity().x < 0){
                if(!player.isFlyingLeft()) player.flying_animation_rev(player_selector);
            }

            if(player.getBody().getLinearVelocity().x == 0){

                if(!player.isFlyingLeft()) {
                    if(player.isStillLeft())
                        player.flying_animation_rev(player_selector);
                }

                if(!player.isFlyingRight()){
                    if(player.isStillRight())
                        player.flying_animation(player_selector);
                }
            }
        }*/


    }

    public void pauseUiAnimation(){
        float speed = 8f;

        if(MyGdxGame.pause && !player.isPlayerDead()) {

            cpt_translate_animation++;
            //System.out.println(cpt_translate_animation);

            if (stage1.getActors().items[4].getX() <= -5) {
                uiIsSliding = true;
                stage1.getActors().items[4].setPosition(-stage1.getActors().items[4].getWidth() + cpt_translate_animation * speed, stage1.getActors().items[4].getY());

            }else {
                cpt_translate_animation = 0;
                isSlideInEnd = true;
                isSlideOutEnd = false;
                uiIsSliding = false;
            }

            if (stage1.getActors().items[5].getRight() >= Gdx.graphics.getWidth() + 5) {
                stage1.getActors().items[5].setPosition((Gdx.graphics.getWidth() / 1f) - (cpt_translate_animation * speed), stage1.getActors().items[5].getY());

            }

        }else{

            if(isSlideInEnd) {
                cpt_translate_animation++;

                if (stage1.getActors().items[4].getX() > -stage1.getActors().items[4].getWidth()) {
                    stage1.getActors().items[4].setPosition(stage1.getActors().items[4].getX() - cpt_translate_animation * speed, stage1.getActors().items[4].getY());

                }else {
                    isSlideInEnd = false;
                    isSlideOutEnd = true;
                }

                if (stage1.getActors().items[5].getRight() <= Gdx.graphics.getWidth() + stage1.getActors().items[5].getWidth()) {
                    stage1.getActors().items[5].setPosition(stage1.getActors().items[5].getX() + (cpt_translate_animation * speed), stage1.getActors().items[5].getY());
                }
            }
        }

        if(MyGdxGame.pause) {
            buttonSound.setVisible(true);
            buttonPlay.setVisible(true);
            Timer.instance().stop();
        }
        else{
            Timer.instance().start();
            /*if(buttonSound.isVisible())
                buttonSound.setVisible(false);
            if(buttonExit.isVisible())
                buttonExit.setVisible(false);*/
        }

        if(MyGdxGame.isSoundEnable() == 0){
            Button.ButtonStyle buttonSoundStyle = new Button.ButtonStyle();
            buttonSoundStyle.up = skin.getDrawable("buttonSound2Mute");
            buttonSoundStyle.down = skin.getDrawable("buttonSound2Mute");
            buttonSound.setStyle(buttonSoundStyle);
        }
        if(MyGdxGame.isSoundEnable() == 1){
            Button.ButtonStyle soundButtonStyle = new Button.ButtonStyle();
            soundButtonStyle.up = skin.getDrawable("buttonSound2Fx");
            soundButtonStyle.down = skin.getDrawable("buttonSound2Fx");
            buttonSound.setStyle(soundButtonStyle);
        }
        if(MyGdxGame.isSoundEnable() == 2){
            Button.ButtonStyle soundButtonStyle = new Button.ButtonStyle();
            soundButtonStyle.up = skin.getDrawable("buttonSound2Full");
            soundButtonStyle.down = skin.getDrawable("buttonSound2Full");
            buttonSound.setStyle(soundButtonStyle);
        }


    }

    public static boolean intersectsWith(BoundingBox boundingBox1, BoundingBox boundingBox2) {
        Vector3 otherMin = boundingBox1.getMin();
        Vector3 otherMax = boundingBox1.getMax();
        Vector3 min = boundingBox2.getMin();
        Vector3 max = boundingBox2.getMax();

        return (min.x < otherMax.x) && (max.x > otherMin.x) && (min.y < otherMax.y) && (max.y > otherMin.y);
    }

    public boolean isEnemyVisible(Enemy enemy){
        return ( (enemy.getPosition().x < MyGdxGame.V_WIDTH/PPM) && enemy.getPosition().x > 0) ? true:false;
    }

    public void fireBallIA(Enemy enemy){

        Iterator<FireBall> iter = fireBalls.iterator();

        while (iter.hasNext()) {
            final FireBall fireBall = iter.next();
            //fireBall.render(sb);
            if(!MyGdxGame.pause){

                fireBall.update(MyGdxGame.STEP);
                fireBall.updateBoundingBox(fireBall);

                if(!fireBall.getDead() && (fireBall.getPosition().x < 0 | fireBall.getPosition().x > Gdx.graphics.getWidth()/PPM)){
                    System.out.println("FIRE BALL REMOVED!");
                    fireBall.setDead(true);
                    fireBalls.removeValue(fireBall, true);
                    //removeBodySafely(fireBall.getBody());
                    fireBall.destroy();
                }

                if(!enemy.isDead() && enemy.getBoundingBox().intersects(fireBall.getBoundingBox())){
                    System.out.println("FIRE BALL TOUCH ENEMY!");
                    enemy.setHealth(0);
                    fireBall.setDead(true);
                    /*enemies.removeValue(enemy, true);
                    removeBodySafely(enemy.getBody());*/
                    fireBalls.removeValue(fireBall, true);
                    //removeBodySafely(fireBall.getBody());
                    fireBall.destroy();
                }

            }
        }
    }


    public void kamehamehaIA(){
        //kamehameha
        float gap = 1.02f*PPM;
        float gap2 = 0.4f*PPM;
        float scale = 4.0f;

        float x = 0.0f;
        float y = 0.0f;
        float w = 0.0f;
        float h = 0.0f;

        float speed = 10.0f;

        if(Math.abs(beamWidth) > MyGdxGame.V_WIDTH*10){
            kamehaReachLimit = !kamehaReachLimit;
        }

        //MyGdxGame.res.getMusic("main").stop();

        if(MyGdxGame.pause){
            MyGdxGame.res.getMusic("epic").stop();
        }else{

            if (MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2){
                if(!MyGdxGame.res.getMusic("epic").isPlaying()){
                    //MyGdxGame.res.getMusic("epic").play();

                }
            }else{
                MyGdxGame.res.getMusic("epic").stop();
            }
        }

        if(!kamehaReachLimit){
            if(!MyGdxGame.pause)
                beamWidth+= speed;
        }else{
//            if(Math.abs(beamWidth) > 10){
//                beamWidth-=speed*2;
//            }else {
            fire = !fire;
            kamehaReachLimit = false;
            beamWidth = 0;
            blockKamehameha = true;
//                MyGdxGame.res.getMusic("epic").stop();
//                MyGdxGame.res.getMusic("main").play();
//            }

        }

        sb.begin();
        if(beamWidth>0){


            if(player.isRight()){
                sb.draw(animKamehameha0.getFrame(),
                        player.getPosition().x*PPM + gap2,
                        player.getPosition().y*PPM - animKamehameha0.getFrame().getRegionHeight()*scale/2,
                        animKamehameha0.getFrame().getRegionWidth()*scale,
                        animKamehameha0.getFrame().getRegionHeight()*scale);

                sb.draw(animKamehameha1.getFrame(),
                        player.getPosition().x*PPM + gap2 + animKamehameha0.getFrame().getRegionWidth()*scale,
                        player.getPosition().y*PPM - animKamehameha1.getFrame().getRegionHeight()*scale/2,
                        beamWidth,
                        animKamehameha1.getFrame().getRegionHeight()*scale);

                sb.draw(animKamehameha2.getFrame(),
                        player.getPosition().x*PPM + gap2 + beamWidth +  animKamehameha2.getFrame().getRegionWidth()*scale/4,
                        player.getPosition().y*PPM - animKamehameha2.getFrame().getRegionHeight()*scale/2,
                        animKamehameha2.getFrame().getRegionWidth()*scale,
                        animKamehameha2.getFrame().getRegionHeight()*scale);

                beamX = player.getPosition().x*PPM + gap2 + beamWidth +  animKamehameha2.getFrame().getRegionWidth()*scale/4 + animKamehameha2.getFrame().getRegionWidth()*scale;

                x = player.getPosition().x*PPM + gap2;
                y = player.getPosition().y*PPM - animKamehameha0.getFrame().getRegionHeight()*scale/2;
                w = beamX + animKamehameha2.getFrame().getRegionWidth()/3f;
                h = y + animKamehameha2.getFrame().getRegionHeight()*scale;

            }
            else {
                sb.draw(animKamehameha0_rev.getFrame(),
                        player.getPosition().x*PPM - gap,
                        player.getPosition().y*PPM - animKamehameha0_rev.getFrame().getRegionHeight()*scale/2,
                        animKamehameha0.getFrame().getRegionWidth()*scale,
                        animKamehameha0.getFrame().getRegionHeight()*scale);

                sb.draw(animKamehameha1_rev.getFrame(),
                        player.getPosition().x*PPM - gap,
                        player.getPosition().y*PPM - animKamehameha1.getFrame().getRegionHeight()*scale/2,
                        animKamehameha1_rev.getFrame().getRegionWidth()*scale - beamWidth,
                        animKamehameha1.getFrame().getRegionHeight()*scale);

                sb.draw(animKamehameha2_rev.getFrame(),
                        player.getPosition().x*PPM - gap - beamWidth - animKamehameha2_rev.getFrame().getRegionWidth()*scale + animKamehameha1_rev.getFrame().getRegionWidth()*scale,
                        player.getPosition().y*PPM - animKamehameha2_rev.getFrame().getRegionHeight()*scale/2,
                        animKamehameha2_rev.getFrame().getRegionWidth()*scale,
                        animKamehameha2_rev.getFrame().getRegionHeight()*scale);

                beamX = player.getPosition().x*PPM - gap - beamWidth - animKamehameha2_rev.getFrame().getRegionWidth()*scale + animKamehameha1_rev.getFrame().getRegionWidth()*scale;

                x = player.getPosition().x*PPM - gap;
                y = player.getPosition().y*PPM - animKamehameha0.getFrame().getRegionHeight()*scale/2;
                w = beamX;
                h = y + animKamehameha2.getFrame().getRegionHeight()*scale;
            }
        }
        sb.end();



        boundingBoxKamehameha.set(new Vector3(x,y,0),
                new Vector3(w,h,10));

        //System.out.println("beamX="+beamX);

    }

    public boolean isEnemyTouchingBrick(){
        boolean result = false;
        for(Enemy enemy:enemies) {
            if(cl.intersect(brick,enemy) && !enemy.isDead()){
                result = true;
            }
        }
        return result;
    }

    public void stopEnemyIfTouchBrick(Enemy enemy){
        if(!enemy.isFading() && !enemy.isDead()&& !brick.getBroken() && cl.intersect(brick,enemy)){
            enemy.setStop(true);
            brick.setLife(brick.getLife()-1);
        }else {
            //brick.setLoop(false);
            //brick.setHurt(false);
            enemy.setStop(false);
        }

        //move brick if player push
        //System.out.println(player.isStillLeft()+" "+player.isStillRight()+" "+ brick.getHurt() +" "+ brick.getLife() +" "+ cl.intersect(player, brick)+" "+player.isLeft());
        if(!player.isStillLeft() && !player.isStillRight() && !brick.getHurt() && brick.getLife() > 0 && cl.intersect(player, brick)  && player.isLeft() && player.getPosition().y*PPM < 315){
            float speed = -0.01f;
            float pos = brick.getPosition().x + speed;
            brick.getBody().setTransform(pos, brick.getPosition().y,brick.getBody().getAngle());
        }

        if(!player.isStillLeft() && !player.isStillRight() && !brick.getHurt() && brick.getLife() > 0 && cl.intersect(player, brick)  && player.isRight() && player.getPosition().y*PPM < 315){
            float speed = +0.01f;
            float pos = brick.getPosition().x + speed;
            brick.getBody().setTransform(pos, brick.getPosition().y,brick.getBody().getAngle());
        }

        Iterator<Enemy> iter = brick.getEnemies().iterator();
        while (iter.hasNext()) {
            final Enemy e = iter.next();

            if(!e.isFading() && !e.isDead()&& !brick.getBroken() && cl.intersect(brick,e)){

            }else {
                //brick.setLoop(false);
                //brick.setHurt(false);
                //e.setStop(false);
            }

//            //move brick if player push
//            if(!player.isStillLeft() && !player.isStillRight() && brick.getLife() > 0 && cl.intersect(player, brick) && !cl.intersect(e, brick) && player.isLeft() && player.getPosition().y*PPM < 315){
//                float speed = -0.01f;
//                float pos = brick.getPosition().x + speed;
//                brick.getBody().setTransform(pos, brick.getPosition().y,brick.getBody().getAngle());
//            }
//
//            if(!player.isStillLeft() && !player.isStillRight() && brick.getLife() > 0 && cl.intersect(player, brick) && !cl.intersect(e, brick) && player.isRight() && player.getPosition().y*PPM < 315){
//                float speed = +0.01f;
//                float pos = brick.getPosition().x + speed;
//                brick.getBody().setTransform(pos, brick.getPosition().y,brick.getBody().getAngle());
//            }

        }


    }

    public void brickIA(Enemy enemy){

//        Iterator<Brick> iter = bricks.iterator();
//
//        while (iter.hasNext()) {

            if(!MyGdxGame.pause && brick != null){
                brick.updateBoundingBox(brick);
                brick.update(MyGdxGame.STEP);
            }
            //brick.getBoundingBox().set(brick.getBoundingBox().min, brick.getBoundingBox().max);

            //make brick fall from the sky when summoned
            if(brick.isFalling()){
                System.out.println("falling"+brick.getPosition().y);
                float y = brick.getPosition().y-0.4f;

                if(brick.getX() == 0){
                    brick.setX(lastClickPos);
                }

                brick.getBody().setTransform(brick.getX(),y,brick.getBody().getAngle());

                if(cl.intersect(brick,enemy)){
                    enemy.setHealth(-99);
                }

                if(brick.getPosition().y*PPM <= 264.8){
                    brick.getBody().setTransform(brick.getX(),MyGdxGame.GROUND,brick.getBody().getAngle());
                    brick.setFalling(false);
                }

            }else{

                stopEnemyIfTouchBrick(enemy);

                if(brick.getLife() <= 0){
                    //brick destroyed in anim function
                    brick.brokeAnimation();
                    enableBrick = true;
                }

            }

       // }
    }

    public void climbingIA(Enemy enemy){
        if(enemy.isClimbing()){

            if(!enemy.isFromLeft()){
                if(!enemy.isDieRight()){
                    enemy.getBody().setTransform((MyGdxGame.V_WIDTH/PPM)-65/PPM, enemy.getPosition().y, enemy.getBody().getAngle());
                    enemy.dieAnimation(true);
                }
            }
            if(enemy.isFromLeft()){
                if(!enemy.isDieLeft()){
                    enemy.getBody().setTransform(65/PPM, enemy.getPosition().y, enemy.getBody().getAngle());
                    enemy.dieAnimation_rev(true);
                }
            }

        }else{

            if(!enemy.isFromLeft()){
                if(!enemy.isDieRight()){
                    enemy.dieAnimation(false);
                }
            }
            if(enemy.isFromLeft()){
                if(!enemy.isDieLeft()){
                    enemy.dieAnimation_rev(false);
                }
            }
        }
    }

    public void enemyJumpOverIA(Enemy enemy){
        enemy.setCptFadeInRunning(enemy.getCptFadeInRunning()+1);

        if(enemy.getCptFadeInRunning() > 60){

            if(enemy.getBody().getLinearVelocity().x == 0){
                if(enemy.isFromLeft()){
                    enemy.setLeft(true);
                    enemy.setRight(false);
                }else {
                    enemy.setRight(true);
                    enemy.setLeft(false);
                }
            }

            if(enemy.getPosition().x - enemy.getWidth()/2/PPM < 90/PPM){
                enemy.setRight(true);
                enemy.setLeft(false);
            }

            if(enemy.getPosition().x + enemy.getWidth()/2/PPM > MyGdxGame.V_WIDTH/PPM - 90/PPM){
                enemy.setRight(false);
                enemy.setLeft(true);
            }


            if(enemy.isLeft()){
                if(!enemy.isNormalRight()){
                    enemy.normalAnimation();
                }
                enemy.getBody().setLinearVelocity(-enemy.getSpeed(),0);
            }

            if(enemy.isRight()){
                if(!enemy.isNormalLeft() && !enemy.isClimbRight() && !enemy.isClimblLeft()){
                    enemy.normalAnimation_rev();
                }
                enemy.getBody().setLinearVelocity(enemy.getSpeed(),0);
            }

        }else {

            enemy.getBody().setLinearVelocity(0,enemy.getBody().getLinearVelocity().y);

            if(!enemy.isFromLeft()){
                if(!enemy.isFadeInRight() && !enemy.isMalicious()){
                    enemy.fadeInAnimation();
                }
            }else{
                if(!enemy.isFadeInLeft() && !enemy.isMalicious()){
                    enemy.fadeInAnimation_rev();
                }
            }
            //enemy.setCptFadeInRunning(0);
        }
    }

    public void enemiesIA(){

        Iterator<Enemy> iter = enemies.iterator();

        while (iter.hasNext()) {
            final Enemy enemy = iter.next();

            //stop rendering the enemy after die animation
            if(enemy.getCptDieRunning() < 80){
                enemy.render(sb);
            }

            if (enemy.isStop() | stopEnemies){
                enemy.getBody().setLinearVelocity(0,0);
            }

            if(!MyGdxGame.pause){
                enemy.update(MyGdxGame.STEP);

                if(!MyGdxGame.pause | !stopEnemies){
                    enemy.updateBoundingBox(enemy);


                    if(brick != null){
                        brickIA(enemy);
                    }


                    princessIA(enemy);
                    fireBallIA(enemy);

                    if(enemy.getHealth()<=0 | enemy.isDead()){
                        /**ENEMY IS DEAD**/

                        enemy.getBody().setLinearVelocity(new Vector2(0,0));
                        enemy.setCptDieRunning(enemy.getCptDieRunning()+1);

                        if(!enemy.isDead()){
                            player.collectCoin();
                            if (MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getSound("boom").play();
                            if(Save.gd.isFireBallEquiped() && enemiesKilled >= 1 && player.getFireBallCount()<player.MAXFIREBALLCOUNT){
                                fire = false;
                                player.setFireBallCount(player.getFireBallCount()+1);
                                enemiesKilled = 0;
                                System.out.println("increment fireball count!");
                            }else{
                                enemiesKilled++;
                                System.out.println("enemy killed="+enemiesKilled);
                            }
                            enemy.setDead(true);
                        }

                        climbingIA(enemy);

                        //todo timer
                        if(enemy.getCptDieRunning() > 80){
                            enemy.setDead(true);
                            enemies.removeValue(enemy, true);
                            enemy.destroy();
                            //removeBodySafely(enemy.getBody());
                        }

                    }else{
                        /**ENEMY IS NOT DEAD**/
                        if(!enemy.isStop()){

                            if(enemy.isJumpOver()){

                                enemyJumpOverIA(enemy);

                            }else{

                                //todo enemyHurt animation
                                if(enemy.isTouched()){
                                    if (MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2){
                                        if(cpt_sound_hit % 2 == 0){
                                            MyGdxGame.res.getSound("hit").play();
                                        }
                                        if(cpt_sound_hit < 9999){
                                            cpt_sound_hit+=0.5;
                                        }else {
                                            cpt_sound_hit = 0;
                                        }

                                    }

                                    //nemy.getBody().setLinearVelocity(enemy.getBody().getLinearVelocity().x/2, enemy.getBody().getLinearVelocity().y);
                                    if(enemy.isClimbing()){

                                        if(!enemy.isFromLeft()){

                                            if(!enemy.isHurtRight()){
                                                //enemy.getBody().setTransform((MyGdxGame.V_WIDTH/PPM)-65/PPM, enemy.getPosition().y, enemy.getBody().getAngle());
                                                enemy.hurtAnimation(true);
                                            }
                                        }
                                        if(enemy.isFromLeft()){
                                            if(!enemy.isHurtLeft()){
                                                //enemy.getBody().setTransform(65/PPM, enemy.getPosition().y, enemy.getBody().getAngle());
                                                enemy.hurtAnimation_rev(true);
                                            }
                                        }



                                    }else{

                                        if(!enemy.isFromLeft()){
                                            if(!enemy.isHurtRight()){
                                                enemy.hurtAnimation(false);
                                            }
                                        }
                                        if(enemy.isFromLeft()){
                                            if(!enemy.isHurtLeft()){
                                                enemy.hurtAnimation_rev(false);
                                            }
                                        }
                                    }
                                }else{
                                    //enemy on the ground

                                    if(!enemy.isClimbing() ){
                                        if(!enemy.isFromLeft()) {
                                            enemy.getBody().setLinearVelocity(-enemy.getSpeed(),enemy.getBody().getLinearVelocity().y);
                                        }
                                        else {
                                            enemy.getBody().setLinearVelocity(enemy.getSpeed(),enemy.getBody().getLinearVelocity().y);
                                            if(!enemy.isNormalLeft() && !enemy.isFadeOutLeft() && !enemy.isClimbRight() && !enemy.isClimblLeft() && !enemy.isTouched()){
                                                enemy.normalAnimation_rev();
                                            }
                                        }
                                    }else{
                                        if(!enemy.isFromLeft()){
                                            if(!enemy.isClimbRight()){
                                                enemy.climbAnimation();
                                            }
                                        }else{
                                            if(!enemy.isClimblLeft()){
                                                enemy.climbAnimation_rev();
                                            }
                                        }
                                    }

                                    //enemy in door 1
                                    if(!enemy.isMalicious() && (enemy.getPosition().x > (MyGdxGame.V_WIDTH/2/PPM)-0.03f && enemy.getPosition().x < (MyGdxGame.V_WIDTH/2/PPM)+0.03f)){

                                        enemy.getBody().setLinearVelocity(0,enemy.getBody().getLinearVelocity().y);
                                        enemy.setFading(true);
                                        //todo fadeIn
                                        if(!enemy.isFromLeft()){
                                            if(!enemy.isFadeOutRight()){
                                                enemy.fadeOutAnimation();
                                            }
                                        }else{
                                            if(!enemy.isFadeOutLeft()){
                                                enemy.fadeOutAnimation_rev();
                                            }
                                        }

                                        enemy.setCptFadeOutRunning(enemy.getCptFadeOutRunning()+1);

                                        if(enemy.getCptFadeOutRunning() > 30){
                                            enemy.setCptFadeOutRunning(0);
                                            enemy.getBody().setTransform(enemy.getBody().getPosition().x, enemy.getBody().getPosition().y*2, enemy.getBody().getAngle());
                                            enemy.setJumpOver(true);
                                            //enemy.getBody().applyForceToCenter(new Vector2(0,50),false);
                                        }
                                    }


                                    if( enemy.isMalicious() &&
                                            (enemy.isClimbing() |
                                                    ((enemy.getPosition().x > 100/PPM && enemy.isFromLeft())
                                                            | (enemy.getPosition().x < MyGdxGame.V_WIDTH/PPM - 100/PPM && !enemy.isFromLeft()))) ){

                                        enemy.setWaited(true);
                                        enemy.getBody().setLinearVelocity(0,enemy.getBody().getLinearVelocity().y);

                                        enemy.setCptWaitRunning(enemy.getCptWaitRunning()+1);


                                        if(enemy.getCptWaitRunning() > 2 && !enemy.isJumpOver()){
                                            //enemy.setCptWaitRunning(0);
                                            //enemy.setCptWaitRunning(0);

                                            float posClimb = enemy.getBody().getPosition().y + enemy.getCptWaitRunning()/5000f;

                                            posClimb = (float) (posClimb+dificulty/1000.0f);

                                            if(posClimb >= MyGdxGame.GROUND*2f){

                                                enemy.setJumpOver(true);
                                                //stopEnemies = true;

                                                if(!enemy.isFromLeft()){
                                                    enemy.getBody().setTransform(enemy.getBody().getPosition().x-65/PPM, MyGdxGame.GROUND*2f, enemy.getBody().getAngle());
                                                    if(!enemy.isRight()){
                                                        enemy.normalAnimation();
                                                    }
                                                }else{
                                                    enemy.getBody().setTransform(enemy.getBody().getPosition().x+65/PPM, MyGdxGame.GROUND*2f, enemy.getBody().getAngle());
                                                    if(!enemy.isLeft()){
                                                        enemy.normalAnimation_rev();
                                                    }
                                                }

                                            }else{



                                                enemy.getBody().setTransform(enemy.getBody().getPosition().x , posClimb, enemy.getBody().getAngle());

                                                if(!enemy.isFromLeft()){
                                                    enemy.getBody().setTransform((MyGdxGame.V_WIDTH/PPM)-65/PPM, posClimb, enemy.getBody().getAngle());
                                                    if(!enemy.isClimbRight()){

                                                        enemy.climbAnimation();
                                                    }
                                                }else{
                                                    enemy.getBody().setTransform(65/PPM , posClimb, enemy.getBody().getAngle());
                                                    if(!enemy.isClimblLeft()){

                                                        enemy.climbAnimation_rev();
                                                    }
                                                }

                                            }
                                        }
                                    }}
                            }
                        }
                    }
                }else{
                    //
                }

            }
        }



    }

    public void princessIA(Enemy enemy){
        if(MyGdxGame.pause){
            princess.update(MyGdxGame.STEP);
            princess.updateBoundingBox(princess);
        }

        if(!enemy.isDead() && enemy.getPosition().y*PPM > 350){
            princess.setCry(true);
        }

        if(!enemy.isDead() && princess.getBoundingBox().intersects(enemy.getBoundingBox()) && enemy.getBody().getLinearVelocity().x != 0 ){
            princess.setTouched(true);
            if((enemy.getBody().getLinearVelocity().x < 0) && !enemy.isMockRight()){
                enemy.mockAnimation();
                System.out.println("mockAnimation");
            }
            if((enemy.getBody().getLinearVelocity().x > 0) && !enemy.isMockLeft()){
                enemy.mockAnimation_rev();
                System.out.println("mockAnimation_rev");
            }
        }


        if(princess.isTouched()){
            stopEnemies = true;
            enemy.setStop(true);
            //player.setPlayerDead(true);

            princess.getBody().setLinearVelocity(0,0);

            if(princess.isLeft()){
                if(!princess.isDieRight()){
                    princess.dieAnimation();
                    if(MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getMusic("laugh").play();
                }
            }

            if(princess.isRight()){
                if(!princess.isDieLeft()){
                    princess.dieAnimation_rev();
                    if(MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getMusic("laugh").play();
                }
            }



            //TODO go to game over
            if(!gameover){
                gameover = true;
                System.out.println("GAMEOVER!");
                submit = true;
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        gsm.setState(GameStateManager.GAME_OVER);
                    }
                }, 1.5f);
            }


        }else{

            if(princess.isLeft()){
                if(!princess.isNormalRight() && !princess.isCry()){
                    princess.normalAnimation();
                }
                if(!princess.isCryRight() && princess.isCry()){
                    princess.cryAnimation();
                }
                princess.getBody().setLinearVelocity(-1,0);
            }

            if(princess.isRight()){
                if(!princess.isNormalLeft() && !princess.isCry()){
                    princess.normalAnimation_rev();
                }
                if(!princess.isCryLeft() && princess.isCry()){
                    princess.cryAnimation_rev();
                }
                princess.getBody().setLinearVelocity(1,0);
            }
        }


//        if(princess.isLeft()){
//            if(!princess.isNormalRight()){
//                if(princess.isTouched() ){
//                    if(!princess.isCryRight())
//                        princess.cryAnimation();
//                }else{
//                    princess.normalAnimation();
//                }
//            }
//            princess.getBody().setLinearVelocity(-1,0);
//        }
//
//        if(princess.isRight()){
//            if(!princess.isNormalLeft()){
//                if(princess.isTouched()){
//                    if(!princess.isCryLeft())
//                        princess.cryAnimation_rev();
//                }else{
//                    IF5
//                    princess.normalAnimation_rev();
//                }
//            }
//            princess.getBody().setLinearVelocity(1,0);
//        }


    }

    public void removeBodySafely(Body body) {
        //to prevent some obscure c assertion that happened randomly once in a blue moon
        final Array<JointEdge> list = body.getJointList();
        while (list.size > 0) {
            world.destroyJoint(list.get(0).joint);
        }
        // actual remove
        world.destroyBody(body);
    }

    public void update(float dt) {

        MyGdxGame.debugString = "fps: "+Gdx.graphics.getFramesPerSecond()+'\n'+
                "java heap: "+ (int)(Gdx.app.getJavaHeap()/Math.pow(10, 6))+" Mb"+'\n'+
                "native heap: "+ (int)(Gdx.app.getNativeHeap()/Math.pow(10, 6))+" Mb"+'\n'+
                "d0: "+ step0+'\n'+
                "d1: "+ step1+'\n'+
                "d2: "+ step2+'\n'+
                "d3: "+ step3+'\n'+
                "offDisplay: "+ offsetY+'\n'+
                "enemy count: "+ enemies.size+'\n'+
                "oGplayer: "+ cl.isPlayerOnGround()+'\n'+
                "yPlayer: "+ (int)(player.getPosition().y*PPM)+'\n'+
                "R: "+ player.isRight()+'\n'+
                "L: "+ player.isLeft()+'\n'+
//                "bricks: "+ brick.isSummoned()+'\n'+
                "oSwipe: "+ lastClickPos+'\n'+
//                "0: "+ bricks.get(0).isFalling()+'\n'+
//                "1: "+ bricks.get(1).isFalling()+'\n'+
                "pPrincess: "+ (int)(princess.getPosition().x*PPM)+'\n'+
                "vPrincess: "+ (int)(princess.getBody().getLinearVelocity().x)+'\n'+
                "princess: "+ princess.isLeft();

        MyGdxGame.fadeIn.update(dt);

        if(brick != null){
            if(isEnemyTouchingBrick()){
                brick.hurtAnimation();
            }else{
                brick.normalAnimation();
            }
        }

        if(princess.getPosition().x - princess.getWidth()/2/PPM <= 150/PPM){
            princess.setRight(true);
            princess.setLeft(false);
            princess.getBody().setLinearVelocity(1,0);
        }

        if(princess.getPosition().x + princess.getWidth()/2/PPM >= MyGdxGame.V_WIDTH/PPM - 150/PPM){
            princess.setRight(false);
            princess.setLeft(true);
            princess.getBody().setLinearVelocity(-1,0);
        }


        if(jump && cl.isPlayerOnGround()){
            jump = false;
            player.getBody().applyForceToCenter(new Vector2(0,180),false);
        }



//        if(player.isPlayerDead() && !submit ) {
//            labelScore.setVisible(false);
//            buttonPause.setVisible(false);
//            buttonNo.setVisible(true);
//            buttonCoin.setVisible(true);
//            buttonCamera.setVisible(true);
//            labelMoney.setVisible(true);
//            imageCoin.setVisible(true);
//            stageUiContinue.act();
//            sb.begin();
//            stageUiContinue.draw();
//            sb.end();
//        }


        if (Math.abs(player.getBody().getLinearVelocity().x) != 0) {
            MyGdxGame.background_wood1.setVector(+10, 0);
        }else{
            MyGdxGame.background_wood1.setVector(+10, 0);
        }

        //System.out.println("X: " + player.getBody().getPosition().x);
        labelMoney.setText(Integer.toString(Save.gd.getMoney()));


        if(MyGdxGame.pause) dt = 0;

        pauseUiAnimation();
        updatePlayerAnimation();

        sb.setProjectionMatrix(hudCam.combined);
        sb.begin();
        stage1.getActors().items[2].draw(sb, 1f);
        sb.end();

        MyGdxGame.updateBGM();

        MyGdxGame.background_wood1.update(dt);


        if(!addNewEnemy){
            world.step(dt, 6, 2);
        }else{
            //createEnemy();
            Random r = new Random();

            dificulty = 1.0+player.getNumCoins()/50;

            double rangeMin = dificulty;
            double rangeMax = dificulty;


            //have'nt decide if speed should be random
            //now linear
            double randomSpeed = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
            if(getRandomBoolean()){
                enemies.add(createEnemy(MyGdxGame.V_WIDTH*1.0f/PPM, MyGdxGame.GROUND, false, (float)randomSpeed));
            }else{
                enemies.add(createEnemy(1/PPM, MyGdxGame.GROUND, true, (float)randomSpeed));
            }
            System.out.println(randomSpeed);
        }

        if(!MyGdxGame.pause){
            player.update(dt);

            player.updateBoundingBox(player);
            princess.update(dt);
            princess.updateBoundingBox(princess);
            animKamehameha0.update(dt);
            animKamehameha0_rev.update(dt);
            animKamehameha1.update(dt);
            animKamehameha1_rev.update(dt);
            animKamehameha2.update(dt);
            animKamehameha2_rev.update(dt);
        }

        MyGdxGame.background_cloud.update(dt);
        //MyGdxGame.background_skyDay.update(dt);
        MyGdxGame.background_cloud.update(dt);


        Array<Body> bodies = cl.getCoinsBodies();
        for (int i = 0; i < bodies.size; i++) {
            //*****
            Body b = bodies.get(i);
            coins.removeValue((Coin) b.getUserData(), true);
            world.destroyBody(bodies.get(i));
            //player.collectCoin();
            if (MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getSound("point").play();
        }
        bodies.clear();

        /*Array<Body> bodiesEnemy = cl.getEnemiesToRemove();
        for (int i = 0; i < bodiesEnemy.size; i++) {
            Body b = bodiesEnemy.get(i);
            //enemies.removeValue((Enemy) b.getUserData(), true);
            world.destroyBody(bodiesEnemy.get(i));
            player.collectCoin();
            if (MyGdxGame.isSoundEnable()) MyGdxGame.res.getSound("point").play();
        }
        bodiesEnemy.clear();*/


        labelScore.setText(Integer.toString(player.getNumCoins()));

        if(player.getBody().getPosition().y < -0.3f){

            if ((MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) && !falling) {
                MyGdxGame.res.getSound("falling").play();
                falling = true;
            }
            player.getBody().setLinearVelocity(0, -3f);
        }

//        if(player.getBody().getPosition().y < -4){
//            player.getBody().setGravityScale(0);
//            player.getBody().setLinearVelocity(0, 0);
//        }

        //SUBMIT SCORE ONLY ONCE!
        if(submit) {
            submit = false;
            MyGdxGame.setContinue(false);
            Save.load();
            boolean newHighScore = false;
            long highScores[] = Save.gd.getHighScores();
            for (int i = 0; i < highScores.length; i++) {
                if (player.getNumCoins() > highScores[i])
                    newHighScore = true;
                else
                    newHighScore = false;
            }
            if (newHighScore) {
                Save.gd.setTenativeScore(player.getNumCoins());
                Save.gd.setNewHighScore(true);
                Save.gd.addHighScore(Save.gd.getTentativeScore(), "player");
                Save.save();
            } else {
                Save.gd.setNewHighScore(false);
                Save.gd.setTenativeScore(player.getNumCoins());
                Save.save();
            }

            if (game.actionResolver.getSignedInGPGS()) {
                game.actionResolver.submitScoreGPGS(player.getNumCoins());
            }

        }

    }

    public void render() {
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        //MyGdxGame.background_skyDay.render(sb);
        MyGdxGame.background_wood1.render(sb);
        MyGdxGame.background_cloud.render(sb);

        //if (MyGdxGame.isNightEnable())MyGdxGame.displayBlinkingStars();

        sb.setProjectionMatrix(cam.combined);

        cam.update();
        tmr.setView(cam);
        tmr.render();

        if(Gdx.input.justTouched()){
            Vector3 v = new Vector3(Gdx.input.getX(),Gdx.input.getY(),0);
            cam.unproject(v);
            lastClickPos = v.x/PPM;
        }

        if(cameraMotionOver && !player.isPlayerDead()) {
            if(!MyGdxGame.pause)
                handleInput();

            if(!playerStartMoving) {
                System.out.println("START MOVING!");
                MyGdxGame.background_cloud.setVector(+10, 0);
                player.getBody().setLinearVelocity(MyGdxGame.PLAYER_VELOCITY, 0);
                playerStartMoving = true;
            }
        }

        if(cameraMotionOver) {

            stage1.act();
            spriteBatch.begin();
            stage1.draw();

            stage1.getActors().items[0].setPosition((Gdx.graphics.getWidth() - labelScore.getWidth()) / 2,
                    Gdx.graphics.getHeight() / 1.27f);

            if(!MyGdxGame.pause ) {
                //power up bar
                float c = MyGdxGame.V_WIDTH/6.7f;
                float w = MyGdxGame.V_WIDTH/6.2f;
                float h = c;
                float max = c;

                Vector3 v = new Vector3(buttonFire.getX()*1.06f,buttonFire.getY(),0);
                cam.unproject(v);
                float x = v.x;

                float y = MyGdxGame.V_WIDTH/13f;

                if(Save.gd.isKamehamehaEquiped()){
                    shapeRenderer.setColor(Color.GRAY);
                    shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                    shapeRenderer.rect(x, y, w, h);

                    if(reloadKamehameha != 0)
                        shapeRenderer.setColor(Color.YELLOW);
                    else {
                        shapeRenderer.setColor(Color.RED);
                    }

                    if(!blockKamehameha){
                        reloadKamehameha = 0;
                        h = max -(beamWidth*1.5f/max);
                        shapeRenderer.rect(x, y, w, h);
                    }else{
                        if(reloadKamehameha <= max){
                            //System.out.println(reloadKamehameha+" "+max);
                            if(!MyGdxGame.pause) reloadKamehameha+=0.1;
                        }
                        else{
                            System.out.println("kamehameha reloaded!");
                            if ((MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2)) {
                                MyGdxGame.res.getSound("reloaded").play();
                            }
                            blockKamehameha = false;
                            reloadKamehameha = 0;
                        }
                        shapeRenderer.rect(x, y, w, reloadKamehameha);
                    }
                }

                if(Save.gd.isFireBallEquiped()){
                    shapeRenderer.setColor(Color.GRAY);
                    shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                    shapeRenderer.rect(x, y, w, h);
                    shapeRenderer.setColor(Color.RED);
                    //System.out.println("h="+h);
                    h = (max/3)*player.getFireBallCount();
                    //System.out.println("h="+h+" "+"player.MAXFIREBALLCOUNT="+player.MAXFIREBALLCOUNT+" "+"player.getFireBallCount()="+player.getFireBallCount());
                    if(player.getFireBallCount()>0)
                        shapeRenderer.rect(x, y, w, h);
                }

                shapeRenderer.end();

            }
            spriteBatch.end();
        }

        if(!princess.isDead()) {
            princess.render(sb);
        }

        if(!player.isPlayerDead()) {
            player.render(sb);
        }

        if(brick != null){
            brick.render(sb);
        }

        Iterator<FireBall> iter = fireBalls.iterator();
        while (iter.hasNext()) {
            final FireBall fireBall = iter.next();
            if(!MyGdxGame.pause){
                fireBall.update(MyGdxGame.STEP);
            }
            fireBall.render(sb);
        }

        if(Save.gd.isKamehamehaEquiped() && fire){
            kamehamehaIA();
        }

        //performanceCounter.start();
        enemiesIA();
       // performanceCounter.stop();

        //System.out.println(performanceCounter.current);

        //princessIA();

        if(!MyGdxGame.pause){
            stageUiControl.act();
            stageUiControl.draw();
            labelScore.setVisible(true);
            buttonPause.setVisible(true);
            buttonNo.setVisible(false);
            buttonCoin.setVisible(false);
            buttonCamera.setVisible(false);

        }

        sb.setProjectionMatrix(cam2.combined);
        cam2.setPosition(player.getPosition().x, player.getPosition().y, 0);




        if(/*MyGdxGame.isStartCameraMotion()*/false) {

            float initialCamPosX = tileMapWidth * PPM / 3.292f;

            if (cpt_cameraIntro < 20) {
                cam.position.set(initialCamPosX, MyGdxGame.V_HEIGHT / 2, 0);
                //System.out.println("cpt camera: " + cpt_cameraIntro);
            } else {
                float newCamPosX = initialCamPosX - (float) Math.exp((double) cpt_cameraIntro / 10);
                float playerPosX = player.getBody().getPosition().x * PPM;
                if (newCamPosX < playerPosX) {
                    cam.position.set(player.getBody().getPosition().x * PPM, MyGdxGame.V_HEIGHT / 2, 0);
                    cameraMotionOver = true;
                } else {
                    //move camera from right to left
                    cam.position.set(newCamPosX, MyGdxGame.V_HEIGHT / 2, 0);
                }
            }
        }else{
            cameraMotionOver = true;
            //cam.position.set(player.getBody().getPosition().x * PPM, MyGdxGame.V_HEIGHT / 2, 0);
        }

        cam.update();
        cpt_cameraIntro++;



        if(debug){
            shapeRenderer.setColor(Color.BLACK);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.circle((float)player.getBoundingBox().getMin().x, (float)player.getBoundingBox().getMin().y, 10);
            shapeRenderer.circle((float)player.getBoundingBox().getMax().x, (float)player.getBoundingBox().getMax().y, 10);

            shapeRenderer.circle(beamX,(float)player.getBoundingBox().getMax().y, 10);

            shapeRenderer.circle((float)princess.getBoundingBox().getMin().x, (float)princess.getBoundingBox().getMin().y, 10);
            shapeRenderer.circle((float)princess.getBoundingBox().getMax().x, (float)princess.getBoundingBox().getMax().y, 10);

            shapeRenderer.setColor(Color.RED);
            shapeRenderer.circle(boundingBoxCastle.getMin().x , boundingBoxCastle.getMin().y, 10);
            shapeRenderer.circle(boundingBoxCastle.getMax().x , boundingBoxCastle.getMax().y, 10);

            shapeRenderer.circle(boundingBoxKamehameha.getMin().x , boundingBoxKamehameha.getMin().y, 10);
            shapeRenderer.circle(boundingBoxKamehameha.getMax().x , boundingBoxKamehameha.getMax().y, 10);



            shapeRenderer.end();

            Iterator<Enemy> iterEnemies = enemies.iterator();
            while (iterEnemies.hasNext()) {
                Enemy enemy = iterEnemies.next();
                if(!enemy.isDead()){
                    shapeRenderer.setColor(Color.BLACK);
                    shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                    shapeRenderer.circle((float)enemy.getBoundingBox().getMin().x, (float)enemy.getBoundingBox().getMin().y, 10);
                    shapeRenderer.circle((float)enemy.getBoundingBox().getMax().x, (float)enemy.getBoundingBox().getMax().y, 10);
                    shapeRenderer.end();
                }
            }
        }

        sb.setProjectionMatrix(hudCam.combined);

        if(!player.isPlayerDead()) {
            labelMoney.setVisible(false);
            labelScore.setVisible(true);
            imageCoin.setVisible(false);
        }

        if(player.isPlayerDead()) buttonPause.setVisible(false);

        if (debug) {
            b2dr.render(world, b2dCam.combined);
        }

        Gdx.gl.glViewport(0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        MyGdxGame.fadeIn.render(sb);

    }

    public Enemy createEnemy(float x, float y, boolean fromLeft, float speed) {
        System.out.println("create enemy...");
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        float w = 32/PPM;
        float h = 32/PPM;
        bdef.position.set(x,y);

        bdef.type = BodyType.DynamicBody;
        Body body = world.createBody(bdef);
        addNewEnemy = false;
        shape.setAsBox(w, h, new Vector2(0 ,0), 0);
        fdef.shape = shape;
        fdef.density = 0;
        fdef.friction = 0;
        fdef.isSensor = true;
        fdef.restitution = 0;
        fdef.filter.categoryBits = B2DVars.BIT_ENEMY;
        fdef.filter.maskBits = B2DVars.BIT_PRINCESS | B2DVars.BIT_BLOCK | B2DVars.BIT_GROUND;
        body.setGravityScale(0);
        body.createFixture(fdef).setUserData("enemy");

        shape.dispose();

        return new Enemy(body, fromLeft, isMalicious, speed);
    }

    public FireBall createFireBall(float x, float y, float velocity) {
        System.out.println("create fireball...");
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        float w = 64/PPM;
        float h = 64/PPM;
        bdef.position.set(x,y);
        addNewFireball = false;
        bdef.type = BodyType.DynamicBody;
        Body body = world.createBody(bdef);
        shape.setAsBox(w, h, new Vector2(0 ,0), 0);
        fdef.shape = shape;
        fdef.density = 0;
        fdef.friction = 0;
        fdef.isSensor = true;
        fdef.restitution = 0;
        fdef.filter.categoryBits = B2DVars.BIT_PRINCESS;
        fdef.filter.maskBits = B2DVars.BIT_ENEMY;
        body.setGravityScale(0);
        body.createFixture(fdef).setUserData("fireBall");
        shape.dispose();

        body.setLinearVelocity(velocity,0);

        FireBall fireBall = new FireBall(body);
        return fireBall;
    }

    public Brick createBrick(float x, float y) {
        System.out.println("create brick...");
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        float w = 32/PPM;
        float h = 32/PPM;
        bdef.position.set(x,y);

        bdef.type = BodyType.StaticBody;
        Body body = world.createBody(bdef);
        shape.setAsBox(w, h, new Vector2(0 ,0), 0);
        fdef.shape = shape;
        fdef.density = 0;
        fdef.friction = 0;
        fdef.isSensor = false;
        fdef.restitution = 0;
        fdef.filter.categoryBits = B2DVars.BIT_BLOCK;
        fdef.filter.maskBits =  B2DVars.BIT_GROUND | B2DVars.BIT_ENEMY | B2DVars.BIT_PLAYER;
        body.createFixture(fdef).setUserData("brick");
        // foot
        shape.setAsBox(w, h, new Vector2(0 , 0 ), 0);
        fdef.shape = shape;
        fdef.filter.categoryBits = B2DVars.BIT_BLOCK;
        fdef.filter.maskBits = B2DVars.BIT_GROUND | B2DVars.BIT_ENEMY;
        fdef.isSensor = true;
        fdef.density = 0;
        fdef.friction = 0;
        fdef.restitution = 0;
        body.createFixture(fdef).setUserData("foot");

        /*shape.setAsBox(w, h, new Vector2(0 , 0 ), 0);
        fdef.shape = shape;
        fdef.filter.categoryBits = B2DVars.BIT_ENEMY;
        fdef.filter.maskBits = B2DVars.BIT_PLAYER;
        fdef.isSensor = true;
        fdef.density = 0;
        fdef.friction = 0;
        fdef.restitution = 0;
        body.createFixture(fdef).setUserData("enemySensor");*/

        shape.dispose();

        body.setLinearVelocity(0,0);

        return new Brick(body);
    }

    public void createPrincess(float x, float y) {
        System.out.println("create princess...");
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        float w = 64/PPM;
        float h = 64/PPM;
        bdef.position.set(x,y);

        bdef.type = BodyType.DynamicBody;
        Body body = world.createBody(bdef);
        shape.setAsBox(w, h, new Vector2(0 ,0), 0);
        fdef.shape = shape;
        fdef.density = 0;
        fdef.friction = 0;
        fdef.isSensor = true;
        fdef.restitution = 0;
        fdef.filter.categoryBits = B2DVars.BIT_PRINCESS;
        fdef.filter.maskBits = B2DVars.BIT_ENEMY;
        body.setGravityScale(0);
        body.createFixture(fdef).setUserData("princess");
        // foot
        /*shape.setAsBox(w, h, new Vector2(0 , 0 ), 0);
        fdef.shape = shape;
        fdef.filter.categoryBits = B2DVars.BIT_ENEMY;
        fdef.filter.maskBits = B2DVars.BIT_GROUND;
        fdef.isSensor = true;
        fdef.density = 0;
        fdef.friction = 0;
        fdef.restitution = 0;
        body.createFixture(fdef).setUserData("foot");*/

        /*shape.setAsBox(w, h, new Vector2(0 , 0 ), 0);
        fdef.shape = shape;
        fdef.filter.categoryBits = B2DVars.BIT_ENEMY;
        fdef.filter.maskBits = B2DVars.BIT_PLAYER;
        fdef.isSensor = true;
        fdef.density = 0;
        fdef.friction = 0;
        fdef.restitution = 0;
        body.createFixture(fdef).setUserData("enemySensor");*/

        shape.dispose();

        body.setLinearVelocity(-1,0);

        princess = new Princess(body);
    }

    private void createPlayer() {
        System.out.println("create player...");
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        MapLayer layer = MyGdxGame.tileMap.getLayers().get("player");

        for (MapObject mo : layer.getObjects()) {

            System.out.println("PLAYER COUNT: "+layer.getObjects().getCount());

            float x = 0, y = 0, w = 0, h = 0;
            w = ((RectangleMapObject) mo).getRectangle().getWidth()/PPM;
            h = ((RectangleMapObject) mo).getRectangle().getHeight()/PPM;

            x = ((RectangleMapObject) mo).getRectangle().x / PPM + w/2;
            y = ((RectangleMapObject) mo).getRectangle().y / PPM + h/2;

            bdef.position.set(x, y);

            if(MyGdxGame.isShortcutDiscovered()) {
                bdef.position.set(262.37415f, y);
                MyGdxGame.setShortcutDiscovered(false);
            }

            System.out.println("PLAYER START POSITION: " + x);
            /*if(!isBossTerritoryDiscovered)
                bdef.position.set(1f * MyGdxGame.V_WIDTH / 2 / PPM, 300 / PPM);*/

            bdef.type = BodyType.DynamicBody;
            Body body = world.createBody(bdef);
            shape.setAsBox(w*2f, h, new Vector2(0 ,0), 0);
            fdef.shape = shape;
            fdef.density = 0;
            fdef.friction = 0;
            fdef.restitution = 0;
            fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
            fdef.filter.maskBits =  B2DVars.BIT_GROUND | BIT_ENEMY;
            body.createFixture(fdef).setUserData("player");
            // foot
            shape.setAsBox(w/1.95f, h/5, new Vector2(0 , -h*0.99f ), 0);
            fdef.shape = shape;
            fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
            fdef.filter.maskBits = B2DVars.BIT_GROUND;
            fdef.isSensor = true;
            fdef.density = 0;
            fdef.friction = 0;
            fdef.restitution = 0;
            body.createFixture(fdef).setUserData("foot");

            shape.dispose();

            body.setGravityScale(1);

            player = new Player(body, player_selector);

        }
    }

    private void createTiles() {
        System.out.println("create tiles...");
        tileMapWidth = MyGdxGame.tileMap.getProperties().get("width", Integer.class);
        tileMapHeight = MyGdxGame.tileMap.getProperties().get("height", Integer.class);
        tileSize = (int) MyGdxGame.tileMap.getProperties()
                .get("tilewidth", Integer.class);
        tmr = new OrthogonalTiledMapRenderer(MyGdxGame.tileMap);
        TiledMapTileLayer layer;
        layer = (TiledMapTileLayer) MyGdxGame.tileMap.getLayers().get("ground");
        createLayer(layer, B2DVars.BIT_GROUND);
    }

    private void createLayer(TiledMapTileLayer layer, short bits) {
        System.out.println("create layer...");
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();

        for (int row = 0; row < layer.getHeight(); row++) {
            for (int col = 0; col < layer.getWidth(); col++) {
                Cell cell = layer.getCell(col, row);
                if (cell == null)
                    continue;
                if (cell.getTile() == null)
                    continue;
                bdef.type = BodyType.StaticBody;
                bdef.position.set((col + 0.5f) * tileSize / PPM, (row + 0.46f)
                        * tileSize / PPM);
                ChainShape cs = new ChainShape();
                Vector2[] v = new Vector2[2];
                v[0] = new Vector2(-tileSize / 2 / PPM, tileSize / 2 / PPM);
                v[1] = new Vector2(tileSize / 2 / PPM, tileSize / 2 / PPM);
                cs.createChain(v);
                fdef.friction = 0;
                fdef.density = 0;
                fdef.restitution = 0;
                fdef.shape = cs;
                fdef.filter.categoryBits = bits;
                fdef.filter.maskBits = B2DVars.BIT_PLAYER | B2DVars.BIT_ENEMY;
                fdef.isSensor = false;

                world.createBody(bdef).createFixture(fdef).setUserData("ground");

            }
        }
    }

    public void resize(int width, int height) {
        float aspectRatio = (float) width / (float) height;
        float scale = 1f;
        crop = new Vector2(0f, 0f);

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
        offsetY = crop.y;
        float offsetY = crop.y;
        float offsetX = crop.x;

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Gdx.gl.glViewport(0,0, (int)viewport.width+ (int)offsetX, Gdx.graphics.getHeight());
        stage0.act();
        sb.begin();
        stage0.draw();
        sb.end();

        Gdx.gl.glViewport((int) viewport.x, (int) viewport.y, (int) viewport.width - (int)offsetX, (int) viewport.height - (int)offsetY);

        stage1.getViewport().update((int) (width - offsetX), (int) (height - offsetY), true);
        stageUiControl.getViewport().update((int) (width - offsetX), (int) (height - offsetY), true);

//        float offsetY = crop.y;
//        float offsetX = crop.x;
//
//        stageUiControl.getViewport().update((int) (width- offsetX), (int) (height- offsetY), true);
//        stage1.getViewport().update((int) (width- offsetX), (int) (height- offsetY), true);
//        stageUiContinue.getViewport().update((int) (width- offsetX), (int) (height- offsetY), true);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//        Gdx.gl.glViewport((int) viewport.x, (int) viewport.y, (int) viewport.width - (int)offsetX, (int) viewport.height);



    }

    public void dispose() {
        spriteBatch.dispose();
    }

}
