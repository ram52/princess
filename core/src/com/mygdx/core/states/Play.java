package com.mygdx.core.states;

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
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.mygdx.core.MyGdxGame;
import com.mygdx.core.entities.B2DSprite;
import com.mygdx.core.entities.Brick;
import com.mygdx.core.entities.Enemy;
import com.mygdx.core.entities.FireBall;
import com.mygdx.core.entities.Hand;
import com.mygdx.core.entities.Lightning;
import com.mygdx.core.entities.Player;
import com.mygdx.core.entities.Princess;
import com.mygdx.core.handlers.Animation;
import com.mygdx.core.handlers.B2DVars;
import com.mygdx.core.handlers.BoundedCamera;
import com.mygdx.core.handlers.GameStateManager;
import com.mygdx.core.handlers.MyContactListener;
import com.mygdx.core.handlers.MyInputProcessor;
import com.mygdx.core.handlers.Save;
import com.mygdx.core.handlers.ScreenShake;
import com.mygdx.core.handlers.SimpleDirectionGestureDetector;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.mygdx.core.MyGdxGame.*;
import static com.mygdx.core.MyGdxGame.pointer;
import static com.mygdx.core.entities.Player.MAXFIREBALLCOUNT;
import static com.mygdx.core.entities.Player.PLAYER_VELOCITY;
import static com.mygdx.core.entities.Player.PLAYER_VELOCITYBOOST;
import static com.mygdx.core.handlers.B2DVars.BIT_BLOCK;
import static com.mygdx.core.handlers.B2DVars.PPM;

/** Main Game class
 * Copyright Axel MONTOUT
 * started in 2016
 *
 *
 *          PRINCESS!
 *
 *
 * Protect your loved one.
 * */
public class Play extends GameState {

    private static String LOG_TAG = Play.class.getSimpleName();
    public Player player;

    private boolean getRandomBoolean() {
        return random.nextBoolean();
    }

    public Play(final GameStateManager gsm, final boolean isTutorial) {
        super(gsm);
        MyGdxGame.isTutorial = isTutorial;

        Gdx.app.debug(LOG_TAG,"TUTORIAL--> "+MyGdxGame.isTutorial);
        if(MyGdxGame.isTutorial){


            pickedGameplay = -1;
        }else{
            if(MyGdxGame.isSoundEnable() == 2)
                if(!MyGdxGame.res.getMusic("level1").isPlaying()){
                    MyGdxGame.res.getMusic("level1").setVolume(1f);
                    MyGdxGame.res.getMusic("level1").play();
                }
        }

        if(sbKyaa == null)
            sbKyaa = new SpriteBatch();

        if(bodyToDestroy == null)
            bodyToDestroy = new Array<B2DSprite>();

        if(screenShake == null)
            screenShake = new ScreenShake(50000.0f,20.0f);

        if(!MyGdxGame.isTutorial)
            MyGdxGame.setPause(false);

        if(viewportPlay == null)
            viewportPlay = new Rectangle();

        Save.load();


        if(!Save.gd.isFireBall2Purchased()){
            Save.gd.setFireBall2Equiped(false);
        }
        if(!Save.gd.isBootPurchased()){
            Save.gd.setBootEquiped(false);
        }

        if(!Save.gd.isFireBallEquiped() && !Save.gd.isFireBall2Equiped() && !Save.gd.isKamehamehaEquiped() && !Save.gd.isLightningEquiped()){
            Save.gd.setFireBallEquiped(true);
            Save.save();
        }

        if(stage0Play == null){
            stage0Play = new Stage();
            Image backgroundSky = new Image(MyGdxGame.atlas.findRegion("backgroundSky"));
            backgroundSky.setFillParent(true);
            stage0Play.addActor(backgroundSky);
        }

        if(boundingBoxCastle == null){
            Vector2 origin = new Vector2(Gdx.graphics.getWidth()/2, 0);
            boundingBoxCastle = new BoundingBox();
            boundingBoxCastle.set(new Vector3((int)origin.x - Gdx.graphics.getWidth()/3.1f,(int)origin.y,0), new Vector3((int)origin.x+Gdx.graphics.getWidth()/3.1f,(int)origin.y+Gdx.graphics.getHeight(),10));
        }

        if(boundingBoxKamehameha == null){
            boundingBoxKamehameha = new BoundingBox();

            Sprite tex = new Sprite(MyGdxGame.atlas.findRegion("kameha1"));
            TextureRegion[] sprites = tex.split(16, 43)[0];
            animKamehameha0 = new Animation(sprites, 1 / 15f);

            TextureRegion[] spritesFliped = tex.split(16, 43)[0];
            for (TextureRegion sprite: spritesFliped) {
                sprite.flip(true, false);
            }
            animKamehameha0_rev = new Animation(spritesFliped, 1 / 15f);

            tex = new Sprite(MyGdxGame.atlas.findRegion("kameha2"));
            sprites = tex.split(2, 43)[0];
            animKamehameha1 = new Animation(sprites, 1 / 15f);

            spritesFliped = tex.split(2, 43)[0];
            for (TextureRegion sprite: spritesFliped) {
                sprite.flip(true, false);
            }
            animKamehameha1_rev = new Animation(spritesFliped, 1 / 15f);

            tex = new Sprite(MyGdxGame.atlas.findRegion("kameha3"));
            sprites = tex.split(42, 43)[0];
            animKamehameha2 = new Animation(sprites, 1 / 15f);

            spritesFliped = tex.split(42, 43)[0];
            for (TextureRegion sprite: spritesFliped) {
                sprite.flip(true, false);
            }
            animKamehameha2_rev = new Animation(spritesFliped, 1 / 15f);

            tex = new Sprite(MyGdxGame.atlas.findRegion("plusMoney"));
            sprites = tex.split(107, 112)[0];
            animLabelMoney = new Animation(sprites, 1 / 15f);

            tex = new Sprite(MyGdxGame.atlas.findRegion("killEnemy"));
            sprites = tex.split(137, 111)[0];
            animKillEnemy = new Animation(sprites, 1 / 15f);

            tex = new Sprite(MyGdxGame.atlas.findRegion("ah"));
            sprites = tex.split(137, 111)[0];
            animAh = new Animation(sprites, 1 / 15f);

            tex = new Sprite(MyGdxGame.atlas.findRegion("tip"));
            sprites = tex.split(238, 482)[0];
            animTip = new Animation(sprites, 1 / 15f);

        }

        // set up box2d


        world = new World(new Vector2(0, -9.81f), false);


        cl = new MyContactListener();

        world.setContactListener(cl);


        if(b2dr == null)
            b2dr = new Box2DDebugRenderer();

        if(spriteBatch == null)
            spriteBatch = new SpriteBatch();

        if(spriteBatchLightning == null)
            spriteBatchLightning = new SpriteBatch();

        cam.setToOrtho(false, MyGdxGame.V_WIDTH, MyGdxGame.V_HEIGHT);

        if(b2dCam == null)
            b2dCam = new BoundedCamera();

        b2dCam.setToOrtho(false, MyGdxGame.V_WIDTH / PPM, MyGdxGame.V_HEIGHT
                / PPM);
        b2dCam.setBounds(0, (tileMapWidth * tileSize) / PPM, 0,
                (tileMapHeight * tileSize) / PPM);


        if(cam2 == null)
            cam2 = new BoundedCamera();

        cam2.setToOrtho(false, MyGdxGame.V_WIDTH, MyGdxGame.V_HEIGHT);

        if(skinPlay == null){
            skinPlay = new Skin();
            skinPlay.addRegions(MyGdxGame.atlas);
        }

        MyGdxGame.background_cloud.setVector(+10, 0);
        //MyGdxGame.background_skyDay.setVector(0, 0);
        MyGdxGame.background_wood1.setVector(+10, 0);

        if(stageUiControl == null){
            stageUiControl = new Stage();
            //stageUiControl.addAction(Actions.sequence(Actions.alpha(0.5f), Actions.fadeIn(0.2f)));
            Button.ButtonStyle leftButtonStyle = new Button.ButtonStyle();
            leftButtonStyle.up = skinPlay.getDrawable("buttonUiBossLeftUp");
            leftButtonStyle.down = skinPlay.getDrawable("buttonUiBossLeftDown");
            buttonLeft = new Button(leftButtonStyle);
            float size = (Gdx.graphics.getWidth() / 4.2f);
            space = (Gdx.graphics.getWidth() - (size*4))/5;
            buttonLeft.setWidth(size);
            buttonLeft.setHeight(size/1.2f);
            buttonLeft.setPosition(space, space*12f);
            //buttonLeft.setBounds(0,0,Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight());
            stageUiControl.addActor(buttonLeft);
        }

        if(buttonRight == null){
            Button.ButtonStyle rightButtonStyle = new Button.ButtonStyle();
            rightButtonStyle.up = skinPlay.getDrawable("buttonUiBossRightUp");
            rightButtonStyle.down = skinPlay.getDrawable("buttonUiBossRightDown");
            buttonRight = new Button(rightButtonStyle);
            buttonRight.setWidth(buttonLeft.getWidth());
            buttonRight.setHeight(buttonLeft.getHeight());
            buttonRight.setPosition(buttonLeft.getRight()+space, buttonLeft.getY());
            stageUiControl.addActor(buttonRight);
        }

        if(buttonFire == null){
            Button.ButtonStyle fireButtonStyle = new Button.ButtonStyle();
            fireButtonStyle.up = skinPlay.getDrawable("buttonActionUp");
            fireButtonStyle.down = skinPlay.getDrawable("buttonActionDown");
            buttonFire = new Button(fireButtonStyle);
            buttonFire.setWidth(buttonLeft.getWidth());
            buttonFire.setHeight(buttonLeft.getHeight());
        }

        if(MyGdxGame.pickedGameplay == 2){
            buttonFire.setPosition(buttonRight.getRight()+space, buttonLeft.getY());
        }else{
            buttonFire.setPosition((Gdx.graphics.getWidth()-buttonFire.getWidth())/2, buttonLeft.getY());
        }

        if(buttonGray == null){
            Button.ButtonStyle grayButtonStyle = new Button.ButtonStyle();
            grayButtonStyle.up = skinPlay.getDrawable("buttonActionUp");
            grayButtonStyle.down = skinPlay.getDrawable("buttonActionUp");
            buttonGray = new Button(grayButtonStyle);
            buttonGray.setWidth(buttonFire.getWidth());
            buttonGray.setHeight(buttonFire.getHeight()/1.7f);
            buttonGray.setPosition(buttonFire.getX(), buttonFire.getY()/2 + buttonGray.getHeight()/2);
            //stageUiControl.addActor(buttonGray);
        }

        if(buttonRed == null){
            Button.ButtonStyle redButtonStyle = new Button.ButtonStyle();
            redButtonStyle.up = skinPlay.getDrawable("buttonActionUp");
            redButtonStyle.down = skinPlay.getDrawable("buttonActionUp");
            buttonRed = new Button(redButtonStyle);
            buttonRed.setWidth(buttonFire.getWidth());
        }

        powerUpBar_MaxHeight = buttonFire.getHeight()/1.46f - offsetYPlay;
        buttonRed.setHeight(powerUpBar_MaxHeight);
        buttonRed.setPosition(buttonFire.getX(), buttonFire.getY()+space);
        //stageUiControl.addActor(buttonRed);

        if(buttonJump == null){
            stageUiControl.addActor(buttonFire);
            Button.ButtonStyle jumpButtonStyle = new Button.ButtonStyle();
            jumpButtonStyle.up = skinPlay.getDrawable("buttonActionUp");
            jumpButtonStyle.down = skinPlay.getDrawable("buttonActionDown");
            buttonJump = new Button(jumpButtonStyle);
            buttonJump.setDisabled(true);
            buttonJump.setWidth(buttonFire.getWidth());
            buttonJump.setHeight(buttonFire.getHeight());
            buttonJump.setPosition(buttonFire.getRight()+space, buttonLeft.getY());
            stageUiControl.addActor(buttonJump);
        }

        setupButtonListenners();

        if(stage1Play == null){
            stage1Play = new Stage();
            stage1Play.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(0.2f)));

            labelScorePlay = new Label("0", new LabelStyle(new BitmapFont(Gdx.files.internal(MyGdxGame.fontPointPath), false), Color.WHITE));
            labelScorePlay.setFontScaleY(Gdx.graphics.getWidth() / 450f);
            labelScorePlay.setFontScaleX(Gdx.graphics.getHeight() / 900f);
            labelScorePlay.setPosition((Gdx.graphics.getWidth() - labelScorePlay.getWidth()) / 2,
                    Gdx.graphics.getHeight() / 1.27f);
            labelScorePlay.setAlignment(Align.center);
            stage1Play.addActor(labelScorePlay);
        }

        if(MyGdxGame.pickedGameplay == 2){
            buttonFire.setPosition(buttonRight.getRight()+space, buttonLeft.getY());
        }else{
            buttonFire.setPosition((Gdx.graphics.getWidth()-buttonFire.getWidth())/2, buttonLeft.getY());
        }

        //createBrick(MyGdxGame.V_WIDTH/5/PPM, player.getPosition().yMenu - player.getHeight()/2.8f/PPM);
        createTiles();
        createPlayer();

        if(!MyGdxGame.isTutorial){
            if(!MyGdxGame.TEST)
                createPrincess(MyGdxGame.V_WIDTH/2/PPM, 2*MyGdxGame.GROUND);
        }

        if(lastBrickPosition.x != 0)
            brick = createBrick(lastBrickPosition.x, lastBrickPosition.y);

        if(!MyGdxGame.isTutorial)
            lastBrickPosition = new Vector2(0,0);

        if(enemies == null)
            enemies = new Array<Enemy>();

        if(fireBalls == null)
            fireBalls = new Array<FireBall>();

        executor = Executors.newScheduledThreadPool(1);

        if(runnable == null)
            runnable = new Runnable() {
                public void run() {

                    if(!MyGdxGame.pause){
                        if(!stopEnemies){

                            addNewEnemy = true;
                            //Gdx.app.debug(LOG_TAG,"addNewEnemy! "+"stopEnemies:"+stopEnemies+" MyGdxGame.pause:"+MyGdxGame.pause);

                            toggle = !toggle;

                            isMalicious = getRandomBoolean();

                            //d = (d < 500)? 500:3000-player.getNumCoins()*100;

                            if(player.getNumCoins() >= 5 && !step0){
                                step0 = true;
                                maxEnemiesOnScreen++;
                                executor = Executors.newScheduledThreadPool(1);
                                executor.scheduleAtFixedRate(runnable, 0, 2500, TimeUnit.MILLISECONDS);
                            }

                            if(player.getNumCoins() >= 10 && !step1){
                                step1 = true;
                                maxEnemiesOnScreen++;
                                executor = Executors.newScheduledThreadPool(1);
                                executor.scheduleAtFixedRate(runnable, 0, 2000, TimeUnit.MILLISECONDS);
                            }

                            if(player.getNumCoins() >= 20 && !step2){
                                step2 = true;
                                maxEnemiesOnScreen++;
                                executor = Executors.newScheduledThreadPool(1);
                                executor.scheduleAtFixedRate(runnable, 0, 1000, TimeUnit.MILLISECONDS);
                            }

                            if(player.getNumCoins() >= 50 && !step3){
                                step3 = true;
                                maxEnemiesOnScreen++;
                                executor = Executors.newScheduledThreadPool(1);
                                executor.scheduleAtFixedRate(runnable, 0, 900, TimeUnit.MILLISECONDS);
                            }
                        }
                    }
                }
            };


        if(!MyGdxGame.isTutorial){
            executor.scheduleAtFixedRate(runnable, 0, 3000, TimeUnit.MILLISECONDS);
        }


        player.setNumCoins(0);

        if(pauseButtonStyle == null){
            pauseButtonStyle = new Button.ButtonStyle();
            pauseButtonStyle.up = skinPlay.getDrawable("buttonPause");
            pauseButtonStyle.down = skinPlay.getDrawable("buttonPause");
            buttonPausePlay = new Button(pauseButtonStyle);
            buttonPausePlay.setWidth(Gdx.graphics.getWidth() / 8f);
            buttonPausePlay.setHeight(Gdx.graphics.getWidth() / 8f);
            buttonPausePlay.setPosition((Gdx.graphics.getWidth() - buttonPausePlay.getWidth() - 20),
                    (Gdx.graphics.getHeight() - buttonPausePlay.getHeight() - Gdx.graphics.getHeight()/10f));
                stage1Play.addActor(buttonPausePlay);
        }

        if(MyGdxGame.isTutorial){
            buttonPausePlay.setVisible(false);
        }else {
            buttonPausePlay.setVisible(true);
        }

        if(originalTouchableTouchButton == null)
            originalTouchableTouchButton = buttonPausePlay.getTouchable();

        buttonPausePlay.setTouchable(originalTouchableTouchButton);



        if(imageCoinPlay == null){
            Button.ButtonStyle coinButtonStyle = new Button.ButtonStyle();
            coinButtonStyle.up = skinPlay.getDrawable("coin");
            coinButtonStyle.down = skinPlay.getDrawable("coin");
            imageCoinPlay = new Button(coinButtonStyle);
            imageCoinPlay.setWidth(buttonPausePlay.getWidth());
            imageCoinPlay.setHeight(buttonPausePlay.getWidth());
            imageCoinPlay.setPosition(imageCoinPlay.getWidth() / 4, buttonPausePlay.getY());
            stage1Play.addActor(imageCoinPlay);
        }

        if(labelMoneyPlay == null){
            labelMoneyPlay = new Label("0", new LabelStyle(new BitmapFont(Gdx.files.internal(MyGdxGame.fontPointPath), false), Color.WHITE));
            labelMoneyPlay.setFontScale(Gdx.graphics.getWidth() / 1200f);
            labelMoneyPlay.setWidth(Gdx.graphics.getWidth() / 2);
            labelMoneyPlay.setHeight(imageCoinPlay.getHeight());
            labelMoneyPlay.setAlignment(Align.center | Align.left);
            labelMoneyPlay.setPosition(imageCoinPlay.getRight() * 1.1f, imageCoinPlay.getY());
            stage1Play.addActor(labelMoneyPlay);
        }


        //if(MyGdxGame.pickedGameplay == 2) ymargin = 24.5f;
        if(buttonPlayPlay == null){
            Skin skinButtonPlay = new Skin();
            skinButtonPlay.addRegions(MyGdxGame.atlas);
            Button.ButtonStyle buttonStylePlay = new Button.ButtonStyle();
            buttonStylePlay.up = skinButtonPlay.getDrawable("buttonExit2Up");
            buttonStylePlay.down = skinButtonPlay.getDrawable("buttonExit2Down");
            buttonPlayPlay = new Button(buttonStylePlay);
            buttonPlayPlay.setWidth(Gdx.graphics.getWidth() / 5f);
            buttonPlayPlay.setHeight(Gdx.graphics.getHeight() / 7.8f);
            buttonPlayPlay.setPosition(-400, Gdx.graphics.getHeight() / 4.5f);
            stage1Play.addActor(buttonPlayPlay);
        }

        if(buttonSoundPlay == null){
            Button.ButtonStyle soundButtonStyle = new Button.ButtonStyle();
            buttonSoundPlay = new Button(soundButtonStyle);
            buttonSoundPlay.setWidth(Gdx.graphics.getWidth() / 5f);
            buttonSoundPlay.setHeight(Gdx.graphics.getHeight() / 7.8f);
            buttonSoundPlay.setPosition((Gdx.graphics.getWidth() / 1f) + 400, Gdx.graphics.getHeight() / 4.5f);
            stage1Play.addActor(buttonSoundPlay);
        }

        if(gamePlaySelection == null){
            gamePlaySelection = new Image(MyGdxGame.atlas.findRegion("gamePlaySelection"));

            gamePlaySelection.setWidth(Gdx.graphics.getWidth());
            gamePlaySelection.setHeight(Gdx.graphics.getHeight());
            gamePlaySelection.setPosition(0,(Gdx.graphics.getHeight())/2 - gamePlaySelection.getHeight()/2);
            stage1Play.addActor(gamePlaySelection);
        }

        if(MyGdxGame.isTutorial){
            gamePlaySelection.setVisible(true);
        }else {
            gamePlaySelection.setVisible(false);
        }


        if(buttonGp1 == null){
            Skin skinButtonGamePlay1 = new Skin();
            skinButtonGamePlay1.addRegions(MyGdxGame.atlas);
            Button.ButtonStyle buttonStyleGp1 = new Button.ButtonStyle();
            buttonStyleGp1.up = skinButtonGamePlay1.getDrawable("gameplay2Up");
            buttonStyleGp1.down = skinButtonGamePlay1.getDrawable("gameplay2Down");
            buttonGp1 = new Button(buttonStyleGp1);
            buttonGp1.setWidth(Gdx.graphics.getWidth() / 3f);
            buttonGp1.setHeight(Gdx.graphics.getWidth() / 3f);
            buttonGp1.setPosition((Gdx.graphics.getWidth()-(buttonGp1.getWidth() + buttonGp1.getWidth()))/3.5f, (Gdx.graphics.getHeight() - buttonGp1.getWidth())/ 2.2f);

            buttonStyleGp1 = new Button.ButtonStyle();
            buttonStyleGp1.up = skinButtonGamePlay1.getDrawable("buttonGp1Up");
            buttonStyleGp1.down = skinButtonGamePlay1.getDrawable("buttonGp1Down");
            buttonGp1_label = new Button(buttonStyleGp1);
            buttonGp1_label.setWidth(Gdx.graphics.getWidth() / 3f);
            buttonGp1_label.setHeight(Gdx.graphics.getWidth() / 7.8f);
            buttonGp1_label.setPosition((Gdx.graphics.getWidth()-(buttonGp1_label.getWidth() + buttonGp1_label.getWidth()))/3.5f, buttonGp1.getTop());

            Skin skinButtonGamePlay2 = new Skin();
            skinButtonGamePlay2.addRegions(MyGdxGame.atlas);
            Button.ButtonStyle buttonStyleGp2 = new Button.ButtonStyle();
            buttonStyleGp2.up = skinButtonGamePlay2.getDrawable("gameplay1Up");
            buttonStyleGp2.down = skinButtonGamePlay2.getDrawable("gameplay1Down");
            buttonGp2 = new Button(buttonStyleGp2);
            buttonGp2.setWidth(buttonGp1.getWidth());
            buttonGp2.setHeight(buttonGp1.getWidth());
            buttonGp2.setPosition(buttonGp1.getX()+buttonGp1.getWidth()*1.45f, buttonGp1.getY());

            buttonStyleGp2 = new Button.ButtonStyle();
            buttonStyleGp2.up = skinButtonGamePlay1.getDrawable("buttonGp2Up");
            buttonStyleGp2.down = skinButtonGamePlay1.getDrawable("buttonGp2Down");
            buttonGp2_label = new Button(buttonStyleGp2);
            buttonGp2_label.setWidth(Gdx.graphics.getWidth() / 3f);
            buttonGp2_label.setHeight(Gdx.graphics.getWidth() / 7.8f);
            buttonGp2_label.setPosition(buttonGp1.getX()+buttonGp2_label.getWidth()*1.40f, buttonGp1.getTop());

            stage1Play.addActor(buttonGp1);
            stage1Play.addActor(buttonGp1_label);
            stage1Play.addActor(buttonGp2);
            stage1Play.addActor(buttonGp2_label);
        }

        if(MyGdxGame.isTutorial){
            buttonGp1.setVisible(true);
            buttonGp1_label.setVisible(true);
            buttonGp2.setVisible(true);
            buttonGp2_label.setVisible(true);

        }else {
            buttonGp1.setVisible(false);
            buttonGp1_label.setVisible(false);
            buttonGp2.setVisible(false);
            buttonGp2_label.setVisible(false);
        }


        if(buttonSkip == null){
            Skin skinButtonSkip = new Skin();
            skinButtonSkip.addRegions(MyGdxGame.atlas);
            Button.ButtonStyle buttonStyleSkip = new Button.ButtonStyle();
            buttonStyleSkip.up = skinButtonSkip.getDrawable("buttonSkipUp");
            buttonStyleSkip.down = skinButtonSkip.getDrawable("buttonSkipDown");
            buttonSkip = new Button(buttonStyleSkip);
            buttonSkip.setWidth(Gdx.graphics.getWidth() / 5f);
            buttonSkip.setHeight(Gdx.graphics.getHeight() / 13f);
            buttonSkip.setPosition(Gdx.graphics.getWidth() - buttonSkip.getWidth()*1.1f, Gdx.graphics.getHeight() / 1.24f);
            stage1Play.addActor(buttonSkip);
        }

        if(MyGdxGame.isTutorial){
            buttonSkip.setVisible(true);
        }else{
            buttonSkip.setVisible(false);
        }

        if(gd == null){
            setupButtonListenners2();
            stage1Play.addListener(new InputListener() {
                @Override
                public boolean keyUp(InputEvent event, int keycode) {
                    if (keycode == Input.Keys.LEFT) {
                        Gdx.app.debug(LOG_TAG,"keyUp LEFT");
                        left = false;
                    }
                    if (keycode == Input.Keys.RIGHT) {
                        Gdx.app.debug(LOG_TAG,"keyUp RIGHT");
                        right  = false;
                    }

                    if (keycode == Input.Keys.ENTER | keycode == Input.Keys.SPACE) {
                        InputEvent event2 = new InputEvent();
                        event2.setType(InputEvent.Type.touchUp);
                        buttonFire.fire(event2);
                    }
                    return false;
                }

                @Override
                public boolean keyDown(InputEvent event, int keycode) {
                    if (keycode == Input.Keys.LEFT) {
                        Gdx.app.debug(LOG_TAG,"keyUp LEFT");
                        left = true;
                    }
                    if (keycode == Input.Keys.RIGHT) {
                        Gdx.app.debug(LOG_TAG,"keyUp RIGHT");
                        right = true;
                    }
                    if (keycode == Input.Keys.ENTER | keycode == Input.Keys.SPACE) {
                        firePlay = false;
                        InputEvent event1 = new InputEvent();
                        event1.setType(InputEvent.Type.touchDown);
                        buttonFire.fire(event1);
                    }
                    return false;
                }
            });

            gd = new SimpleDirectionGestureDetector(
                    new SimpleDirectionGestureDetector.DirectionListener() {

                        @Override
                        public void onUp() {
                            if(MyGdxGame.pickedGameplay == 1){
                                Gdx.app.debug(LOG_TAG,"onUp"+" "+Gdx.input.getY()+" "+MyGdxGame.PAD_ZONE);
                                if(Gdx.input.getY() < MyGdxGame.PAD_ZONE){
                                    if(!player.isPlayerDead() && !MyGdxGame.pause && !isGamePadTouched()) {
                                        jump = true;
                                        Gdx.app.debug(LOG_TAG,"up");
                                    }
                                }
                            }

                        }

                        @Override
                        public void onRight() {
                            if(MyGdxGame.pickedGameplay == 1){
                                Gdx.app.debug(LOG_TAG,"onRight"+" "+Gdx.input.getY()+" "+MyGdxGame.PAD_ZONE);
                                if(Gdx.input.getY() < MyGdxGame.PAD_ZONE && !MyGdxGame.pause && !isGamePadTouched() ){
                                    right = true;
                                    left = false;
                                }
                            }
                        }

                        @Override
                        public void onLeft() {
                            if(MyGdxGame.pickedGameplay == 1){
                                Gdx.app.debug(LOG_TAG,"onLeft"+" "+Gdx.input.getY()+" "+MyGdxGame.PAD_ZONE+" "+isGamePadTouched());
                                if(Gdx.input.getY() < MyGdxGame.PAD_ZONE && !MyGdxGame.pause && !isGamePadTouched()){
                                    left = true;
                                    right = false;
                                }
                            }
                        }

                        @Override
                        public void onDown() {
                            if(MyGdxGame.pickedGameplay == 1){
                                Gdx.app.debug(LOG_TAG,"onDown");
                                left = false;
                                right = false;
                            }
                        }
                    });
        }


        InputMultiplexer im = new InputMultiplexer();
        im.addProcessor(gd);
        im.addProcessor(new MyInputProcessor());
        im.addProcessor(stage1Play);
        im.addProcessor(stageUiControl);

        Gdx.input.setInputProcessor(im);


        MyGdxGame.setIsBoosTerritory(false);

        Gdx.app.debug(LOG_TAG,"init completed without errors.");


        Timer.schedule(new Timer.Task(){
            @Override
            public void run() {
                hideScream = true;
            }
        }, 1.5f);

    }

    private void setupButtonListenners2(){
        buttonGp1.addListener(new InputListener() {
            public boolean touchDown(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                if(MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getSound("interaction").play();
                return true;
            }
            public void touchUp(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                MyGdxGame.pickedGameplay = 2;
                Save.load();
                Save.gd.setPickedGamePlay(2);
                Save.save();
                tuto_step0 = true;
                updateButtonPosition();
                createHand(MyGdxGame.V_WIDTH/8f/PPM , MyGdxGame.GROUND/1.15f);

                if(MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getMusic("newScreen").play();
            }
        });

        buttonGp1_label.addListener(new InputListener() {
            public boolean touchDown(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                if(MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getSound("interaction").play();
                return true;
            }
            public void touchUp(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                MyGdxGame.pickedGameplay = 2;
                Save.load();
                Save.gd.setPickedGamePlay(2);
                Save.save();
                tuto_step0 = true;
                updateButtonPosition();
                createHand(MyGdxGame.V_WIDTH/8f/PPM , MyGdxGame.GROUND/1.15f);

                if(MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getMusic("newScreen").play();
            }
        });

        buttonGp2.addListener(new InputListener() {
            public boolean touchDown(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                if(MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getSound("interaction").play();
                return true;
            }
            public void touchUp(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                MyGdxGame.pickedGameplay = 1;
                Save.load();
                Save.gd.setPickedGamePlay(1);
                Save.save();
                tuto_step0 = true;
                updateButtonPosition();
                createHand(MyGdxGame.V_WIDTH/PPM , MyGdxGame.GROUND/1.15f);
                if(MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getMusic("newScreen").play();
            }
        });

        buttonGp2_label.addListener(new InputListener() {
            public boolean touchDown(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                if(MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getSound("interaction").play();
                return true;
            }
            public void touchUp(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                MyGdxGame.pickedGameplay = 1;
                Save.load();
                Save.gd.setPickedGamePlay(1);
                Save.save();
                tuto_step0 = true;
                updateButtonPosition();
                createHand(MyGdxGame.V_WIDTH/PPM , MyGdxGame.GROUND/1.15f);
                if(MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getMusic("newScreen").play();
            }
        });

        buttonSkip.addListener(new InputListener() {
            public boolean touchDown(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                MyGdxGame.isTutorial = false;
                MyGdxGame.lastPlayerPosition = new Vector2(0,0);
                Timer.instance().clear();
                //if(MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getSound("select").play();
                return true;
            }
            public void touchUp(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                Gdx.app.debug(LOG_TAG,"clicked play!");

                if(MyGdxGame.pickedGameplay == -1) MyGdxGame.pickedGameplay = 2;
                gsm.setState(GameStateManager.PLAY);
                MyGdxGame.tuto_step1 = false;
                MyGdxGame.tuto_step2 = false;
                MyGdxGame.tuto_step3 = false;
                MyGdxGame.tuto_step4 = false;
                MyGdxGame.tuto_step5 = false;
                MyGdxGame.tuto_step6 = false;
                Save.gd.setFirstPlay(false);
                Save.save();

                //if(MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getMusic("newScreen").play();
            }
        });

        buttonPausePlay.addListener(new InputListener() {
            public boolean touchDown(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                return true;
            }
            public void touchUp(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                if(!gameOverPlay){
                    Gdx.app.debug(LOG_TAG,"clicked pause!");
                    if(MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getMusic("newScreen").play();
                    MyGdxGame.setPause(!MyGdxGame.pause);
                }
            }
        });

        buttonPlayPlay.addListener(new InputListener() {
            public boolean touchDown(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                MyGdxGame.isTutorial = false;
                if(MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getSound("select").play();
                return true;
            }
            public void touchUp(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                Gdx.app.debug(LOG_TAG,"clicked play!");
                gsm.setState(GameStateManager.MENU);
                pauseUiAnimation();
                MyGdxGame.lastPlayerPosition.set(0,0);
                if(MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getMusic("newScreen").play();
            }
        });

        buttonSoundPlay.addListener(new InputListener() {
            public boolean touchDown(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                return true;
            }

            public void touchUp(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                Gdx.app.debug(LOG_TAG,"clicked mute!");
                if(MyGdxGame.isSoundEnable() == 2){
                    MyGdxGame.setSoundEnable(0);
                }else{
                    MyGdxGame.setSoundEnable(MyGdxGame.isSoundEnable()+1);
                }

                if(MyGdxGame.isSoundEnable() == 0) {
                    MyGdxGame.res.getSound("select").play();
                    if (MyGdxGame.res.getMusic("level1").isPlaying()) MyGdxGame.res.getMusic("level1").stop();
                    //if (MyGdxGame.res.getSound("alarm").isPlaying()) MyGdxGame.res.getSound("alarm").stop();
                    Button.ButtonStyle soundButtonStyle = new Button.ButtonStyle();
                    soundButtonStyle.up = skinPlay.getDrawable("buttonSound1Mute");
                    soundButtonStyle.down = skinPlay.getDrawable("buttonSound1Mute");
                    buttonSoundPlay.setStyle(soundButtonStyle);
                }

                if(MyGdxGame.isSoundEnable() == 1) {
                    MyGdxGame.res.getSound("select").play();
                    if (MyGdxGame.res.getMusic("level1").isPlaying()) MyGdxGame.res.getMusic("level1").stop();
                    Button.ButtonStyle soundButtonStyle = new Button.ButtonStyle();
                    soundButtonStyle.up = skinPlay.getDrawable("buttonSound1Fx");
                    soundButtonStyle.down = skinPlay.getDrawable("buttonSound1Fx");
                    buttonSoundPlay.setStyle(soundButtonStyle);
                }

                if(MyGdxGame.isSoundEnable() == 2) {
                    MyGdxGame.res.getSound("select").play();
                    if (!MyGdxGame.res.getMusic("level1").isPlaying()) MyGdxGame.res.getMusic("level1").play();
                    Button.ButtonStyle soundButtonStyle = new Button.ButtonStyle();
                    soundButtonStyle.up = skinPlay.getDrawable("buttonSound1Full");
                    soundButtonStyle.down = skinPlay.getDrawable("buttonSound1Full");
                    buttonSoundPlay.setStyle(soundButtonStyle);
                }
            }
        });

    }

    private void setupButtonListenners(){
        buttonFire.addListener(new ClickListener() {
            public boolean isOver (Actor actor, float x, float y) {
                Gdx.app.debug(LOG_TAG, "isOver");
                return true;
            }

            public void enter (InputEvent event, float x, float y, int pointer, Actor fromActor) {
                Gdx.app.debug(LOG_TAG, "enter");
            }

            public void exit (InputEvent event, float x, float y, int pointer, Actor toActor) {
                Gdx.app.debug(LOG_TAG, "exit");
            }

            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.debug(LOG_TAG, "touchDown");
                buttonFire.setChecked(true);
                if(Save.gd.isFireBallEquiped()|Save.gd.isFireBall2Equiped()){
                    Gdx.app.debug(LOG_TAG,"clicked firePlay!");
                    firePlay = true;
                }else if(Save.gd.isKamehamehaEquiped() && !blockKamehameha){
                    firePlay = true;
                    if((MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2)&& reloadKamehameha == 0 && beamWidth == 0){
                        if((pickedGameplay == 1 && tuto_step5) | (pickedGameplay == 2 && tuto_step3) | !MyGdxGame.isTutorial){
                            MyGdxGame.res.getSound("kame").play();
                        }

                    }
                }else if(Save.gd.isLightningEquiped() && !blockLightning){
                    firePlay = true;
                    if((MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2)&& reloadLightning == 0 && beamWidth == 0 ){
                        if((pickedGameplay == 1 && tuto_step5) | (pickedGameplay == 2 && tuto_step3) | !MyGdxGame.isTutorial){
                            MyGdxGame.res.getSound("lightning").play();
                        }
                    }
                } else{
                    firePlay = false;
                }
                if(player.getFireBallCount() <= 0){
                    if(MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2)
                    {
                        MyGdxGame.res.getSound("no_ammo").play();
                    }
                }
                return true;
            }

            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.debug(LOG_TAG, "touchUp");
                buttonFire.setChecked(false);
            }

        });

        buttonJump.addListener(new ClickListener() {
            public boolean isOver (Actor actor, float x, float y) {
                Gdx.app.debug(LOG_TAG, "isOver");
                return true;
            }

            public void enter (InputEvent event, float x, float y, int pointer, Actor fromActor) {
                Gdx.app.debug(LOG_TAG, "enter");
            }

            public void exit (InputEvent event, float x, float y, int pointer, Actor toActor) {
                Gdx.app.debug(LOG_TAG, "exit");
            }

            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.debug(LOG_TAG, "touchDown");
                jump = true;
                return true;
            }

            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.debug(LOG_TAG, "touchUp");
                jump = false;
            }

        });

        buttonLeft.addListener(new ClickListener() {
            public boolean isOver (Actor actor, float x, float y) {
                Gdx.app.debug(LOG_TAG, "buttonLeft isOver");
                return true;
            }

            public void enter (InputEvent event, float x, float y, int pointer, Actor fromActor) {
                Gdx.app.debug(LOG_TAG, "buttonLeft enter");
                left = true;
            }

            public void exit (InputEvent event, float x, float y, int pointer, Actor toActor) {
                Gdx.app.debug(LOG_TAG, "buttonLeft exit");
                left = false;

            }

            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.debug(LOG_TAG, "buttonLeft touchDown");
                left = true;
                return true;
            }

            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.debug(LOG_TAG, "buttonLeft ouchUp");
            }

        });

        buttonRight.addListener(new ClickListener() {
            public boolean isOver (Actor actor, float x, float y) {
                Gdx.app.debug(LOG_TAG, "buttonRight isOver");
                return true;
            }

            public void enter (InputEvent event, float x, float y, int pointer, Actor fromActor) {
                Gdx.app.debug(LOG_TAG, "buttonRight enter");
                right = true;
            }

            public void exit (InputEvent event, float x, float y, int pointer, Actor toActor) {
                Gdx.app.debug(LOG_TAG, "buttonRight exit");
                right = false;

            }

            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.debug(LOG_TAG, "buttonRight touchDown");
                right = true;
                return true;
            }

            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.debug(LOG_TAG, "buttonRight touchUp");
            }

        });
    }

    private boolean isGamePadTouched(){
        boolean isTouched = false;
        if(buttonFire.isChecked()) isTouched = true;
        if(buttonJump.isChecked()) isTouched = true;
        if(buttonLeft.isChecked()) isTouched = true;
        if(buttonRight.isChecked()) isTouched = true;
        return isTouched;
    }

    private void updateButtonPosition(){
        float size = (Gdx.graphics.getWidth() / 4.2f);
        float space = (Gdx.graphics.getWidth() - (size*4))/5;

        if(offsetYPlay > 0){
            buttonLeft.setPosition(space, space*15f);
            buttonRight.setPosition(buttonRight.getX(), buttonLeft.getY());
        }


        if(pickedGameplay == 2){
            buttonFire.setPosition(buttonRight.getRight()+space, buttonLeft.getY());
        }else{
            buttonFire.setPosition((Gdx.graphics.getWidth()-buttonFire.getWidth())/2, buttonLeft.getY());
        }

        buttonJump.setPosition(buttonFire.getRight()+space, buttonLeft.getY());

    }

    public void handleInput() {
        updateButtonPosition();

//        if(Gdx.input.justTouched() && Gdx.input.getY() > MyGdxGame.BRICK_SUMON_ZONE && pickedGameplay == 1){
//            Gdx.app.debug(LOG_TAG,"firePlay!");
//            InputEvent event1 = new InputEvent();
//            event1.setType(InputEvent.Type.touchDown);
//            buttonFire.firePlay(event1);
//
//            InputEvent event2 = new InputEvent();
//            event2.setType(InputEvent.Type.touchUp);
//            buttonFire.firePlay(event2);
//        }

        //brick summon
        if(brick == null && ((MyGdxGame.isTutorial && tuto_step4 && pickedGameplay == 2) | (MyGdxGame.isTutorial && tuto_step6 && pickedGameplay == 1) | !MyGdxGame.isTutorial))
            if(Gdx.input.justTouched() && Gdx.input.getY() < MyGdxGame.BRICK_SUMON_ZONE && !MyGdxGame.pause && !buttonPausePlay.isOver() && !buttonPlayPlay.isOver() && !buttonSkip.isOver()){
                if(!Save.gd.isBrick2Equiped()){
                    if(brick == null){
                        brick = createBrick(lastClickPos, Gdx.graphics.getHeight()/PPM);
                    }else{
                        if(brick.getDead()){
                            brick = createBrick(lastClickPos, Gdx.graphics.getHeight()/PPM);
                        }
                    }

                    //brick.setLife(400);

                    if(!MyGdxGame.pause){
                        if(!brick.isSummoned() && enableBrick){
                            enableBrick = false;
                            brick.setFalling(true);
                            brick.setSummoned(true);
                            if(MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getMusic("newScreen").play();
                        }
                    }
                }


                if(Save.gd.isBrick2Equiped()){
                    if(brick == null){
                        brick = createBrick(lastClickPos, Gdx.graphics.getHeight()/PPM);
                    }else{
                        if(brick.getDead()){
                            brick = createBrick(lastClickPos, Gdx.graphics.getHeight()/PPM);
                        }
                    }

                    //fe(400*5);

                    if(!MyGdxGame.pause){
                        if(!brick.isSummoned() && enableBrick){
                            enableBrick = false;
                            brick.setFalling(true);
                            brick.setSummoned(true);
                            if(MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getMusic("newScreen").play();
                        }
                    }
                }
            }




//        if(!buttonRight.isOver() && buttonRight.isChecked()){
//            rightMenu = false;
//        }
//        if(!buttonLeft.isOver() && buttonLeft.isChecked()){
//            leftMenu = false;
//        }

//        //reach screen extrem
//        if(player.getPosition().x - player.getWidth()/2/PPM < 0 && leftMenu){
//            player.getBody().setTransform(MyGdxGame.V_WIDTH/PPM + player.getWidth()/2/PPM , player.getPosition().yMenu, player.getBody().getAngle());
//            leftMenu = true;
//            rightMenu = false;
//        }
//        if(player.getPosition().x + player.getWidth()/2/PPM >  MyGdxGame.V_WIDTH/PPM && rightMenu){
//            player.getBody().setTransform(-player.getWidth()/2/PPM, player.getPosition().yMenu, player.getBody().getAngle());
//            leftMenu = false;
//            rightMenu = true;
//        }

        //bound player in screen
        if(player.getPosition().x - player.getWidth()/2/PPM < 0){
            Gdx.app.debug(LOG_TAG,"OUT1");
            player.getBody().setTransform(player.getWidth()/2/PPM , player.getPosition().y, player.getBody().getAngle());
            left = false;
            right = false;
        }

        if(player.getPosition().x + player.getWidth()/2/PPM >  MyGdxGame.V_WIDTH/PPM){
            Gdx.app.debug(LOG_TAG,"OUT2");
            player.getBody().setTransform(MyGdxGame.V_WIDTH/PPM - player.getWidth()/2/PPM, player.getPosition().y, player.getBody().getAngle());
            left = false;
            right = false;
        }



        //player not moving
        if((!left | !right)){
            //Gdx.app.debug(LOG_TAG,"not moving");

            if(Save.gd.isBootEquiped()) {
                if (player.getBody().getLinearVelocity().x != 0 && Math.abs(player.getBody().getLinearVelocity().x) <= PLAYER_VELOCITY * Player.PLAYER_VELOCITYBOOST) {
                    //Gdx.app.debug(LOG_TAG,"STILL");
                    player.getBody().setLinearVelocity(0, player.getBody().getLinearVelocity().y);
                } else {
                    if (player.getBody().getLinearVelocity().x != 0 && Math.abs(player.getBody().getLinearVelocity().x) <= PLAYER_VELOCITY) {
                        //Gdx.app.debug(LOG_TAG,"STILL");
                        player.getBody().setLinearVelocity(0, player.getBody().getLinearVelocity().y);
                    }
                }

            }else{
                player.getBody().setLinearVelocity(0, player.getBody().getLinearVelocity().y);
            }
        }


        /**Handle keyboard input**/
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            if(     (cl.isPlayerOnGround() |cl.isPlayerOnBrick() ) &&
                    !jump &&
                    !player.isJumpLeft() &&
                    !player.isJumpRight()
//                    &&
//                    (player.getPosition().yMenu*PPM) < 325
                    )
            {
                player.getBody().setLinearVelocity(new Vector2(player.getBody().getLinearVelocity().x,0));
                player.getBody().setAngularVelocity(0);

                if(Save.gd.isMegaJumpEquiped()){
                    player.getBody().applyForceToCenter(new Vector2(0,220),false);
                }else{
                    player.getBody().applyForceToCenter(new Vector2(0,160),false);
                }
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            //todo summon brick
            if(brick == null){
                lastClickPos = player.getPosition().x;
                brick = createBrick(lastClickPos, Gdx.graphics.getHeight()/PPM);
                brick.setX(lastClickPos);
                if(!MyGdxGame.pause){
                    if(!brick.isSummoned() && enableBrick){
                        enableBrick = false;
                        brick.setFalling(true);
                        brick.setSummoned(true);
                        if(MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getMusic("newScreen").play();
                    }
                }
            }
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
            Gdx.app.debug(LOG_TAG,"keyUp LEFT");
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
            Gdx.app.debug(LOG_TAG,"keyUp RIGHT");
        }

        //LIGHTNING
        if(Save.gd.isLightningEquiped() && firePlay && !lightningRunning){
            firePlay = false;
            //todo sound
            //if (MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getSound("lightning").play();
            lightning = createLightning(player.getPosition().x, 0,4);
            lightningRunning = true;

        }
        //FIRE BALL 1
        if(Save.gd.isFireBallEquiped() && firePlay && player.getFireBallCount()>0){
            firePlay = false;
            //if(!isTutorial){
            if(!TEST)
                player.setFireBallCount(player.getFireBallCount()-4);
            //}
            if (MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) {

                MyGdxGame.res.getSound("fireball_small").play();
            }
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
        //FIRE BALL2
        if(Save.gd.isFireBall2Equiped() && firePlay && player.getFireBallCount()>0){
            firePlay = false;
            //if(!isTutorial){
            if(!TEST)
                player.setFireBallCount(player.getFireBallCount()-1);
            //}

            if (MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) {
                MyGdxGame.res.getSound("fireball_big").play();
            }

            float gap = 10.0f/PPM;
            if(player.isRight()){
                FireBall fireball = createFireBall(player.getPosition().x + gap, player.getPosition().y,2.5f);
                if(!fireball.getRight())
                    fireball.normalAnimation_rev();
                fireBalls.add(fireball);
            }
            else {
                fireBalls.add(createFireBall(player.getPosition().x - gap, player.getPosition().y,-2.5f));
            }

        }

        if (left && player.getBody().getLinearVelocity().x != -PLAYER_VELOCITY) {
            //Gdx.app.debug(LOG_TAG,"LEFT");
            if(Save.gd.isBootEquiped()) {
                player.getBody().setLinearVelocity(-PLAYER_VELOCITY * PLAYER_VELOCITYBOOST, player.getBody().getLinearVelocity().y);
            }else{
                player.getBody().setLinearVelocity(-PLAYER_VELOCITY, player.getBody().getLinearVelocity().y);
            }
        }

        //moves rightMenu
        if (right && player.getBody().getLinearVelocity().x != PLAYER_VELOCITY) {
            //Gdx.app.debug(LOG_TAG,"RIGHT");
            if(Save.gd.isBootEquiped()) {
                player.getBody().setLinearVelocity(PLAYER_VELOCITY * PLAYER_VELOCITYBOOST, player.getBody().getLinearVelocity().y);
            }else{
                player.getBody().setLinearVelocity(PLAYER_VELOCITYBOOST, player.getBody().getLinearVelocity().y);
            }
        }


    }

    private void makeEnemySensor(Enemy enemy, boolean isSensor){
        for (Fixture fixture: enemy.getBody().getFixtureList()) {
            fixture.setSensor(isSensor);
        }
    }

    private void makeBrickSensor(Brick brick, boolean isSensor){
        try{
            Array<Fixture> fixtures = brick.getBody().getFixtureList();
            for (int i = 0; i<fixtures.size; i++) {
                fixtures.get(i).setSensor(isSensor);
            }
        }catch (Exception e){
            Gdx.app.error(LOG_TAG,"error while setting brick sensor",e);
        }
    }

    private void makeEnemyBounce(Enemy enemy){
        Array<Fixture> fixtures = enemy.getBody().getFixtureList();

        for (int i = 0; i<fixtures.size; i++) {
            fixtures.get(i).setRestitution(10.0f);
        }
    }

    private void  updatePlayerAnimation(){

//        Iterator<Enemy> iter = enemies.iterator();
//        while (iter.hasNext()) {
//            Enemy enemy = iter.next();
        for (Enemy enemy : enemies) {
            player.updateBoundingBox(player,64,64);

            if(princess != null){
                princess.updateBoundingBox(princess,64,64);
            }

            enemy.updateBoundingBox(enemy,64,64);

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
//                rightMenu = false;
//                leftMenu = false;
                isTouchingEnemy = true;
            }else{
                enemy.setTouched(false);
            }

            //Gdx.app.debug(LOG_TAG,enemy.getPosition().x*PPM+" "+beamX);
            //kamehameha
            if(enemy.getBoundingBox().intersects(boundingBoxKamehameha)){
                enemy.setTouched(true);
                if(megaKameha){
                    enemy.setHealth(enemy.getHealth()-99f);
                }else{
                    enemy.setHealth(enemy.getHealth()-0.6f);
                }

            }else{
                enemy.setTouched(false);
            }

            if(lightning != null){
                lightning.updateBoundingBox(lightning, Gdx.graphics.getWidth()/10, 2*Gdx.graphics.getHeight() );

                if(enemy.getBoundingBox().intersects(lightning.getBoundingBox())){
                    enemy.setTouched(true);
                    enemy.setHealth(-99);
                    sb.begin();
                    sb.end();
                }else{
                    enemy.setTouched(false);
                    sb.begin();
                    sb.end();
                }
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
            //Gdx.app.debug(LOG_TAG,"touch");
            if ((player.isStillRight() | player.isRunningRight())) {
                player.slash_animation(player_selector);
            }
            if ((player.isStillLeft() | player.isRunningLeft())) {
                player.slash_animation_rev(player_selector);
            }

            isTouchingEnemy = false;
        }
        if(jump && cl.isPlayerOnGround()){
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
    }

    private void pauseUiAnimation(){
        float speed = 8f;

        if((MyGdxGame.pause) && !player.isPlayerDead()) {

            cpt_translate_animationPlay++;
            //Gdx.app.debug(LOG_TAG,cpt_translate_animationPlay);

            if (stage1Play.getActors().items[4].getX() <= -5) {
                stage1Play.getActors().items[4].setPosition(-stage1Play.getActors().items[4].getWidth() + cpt_translate_animationPlay * speed, stage1Play.getActors().items[4].getY());

            }else {
                cpt_translate_animationPlay = 0;
                isSlideInEnd = true;
            }

            if(!MyGdxGame.isTutorial)
                if (stage1Play.getActors().items[5]!= null && stage1Play.getActors().items[5].getRight() >= Gdx.graphics.getWidth() + 5) {
                    stage1Play.getActors().items[5].setPosition((Gdx.graphics.getWidth() / 1f) - (cpt_translate_animationPlay * speed), stage1Play.getActors().items[5].getY());
                }

        }else{

            if(isSlideInEnd) {
                cpt_translate_animationPlay++;

                if (stage1Play.getActors().items[4].getX() > -stage1Play.getActors().items[4].getWidth()) {
                    stage1Play.getActors().items[4].setPosition(stage1Play.getActors().items[4].getX() - cpt_translate_animationPlay * speed, stage1Play.getActors().items[4].getY());

                }else {
                    isSlideInEnd = false;
                }

                if(!MyGdxGame.isTutorial)
                    if (stage1Play.getActors().items[5]!= null && stage1Play.getActors().items[5].getRight() <= Gdx.graphics.getWidth() + stage1Play.getActors().items[5].getWidth()) {
                        stage1Play.getActors().items[5].setPosition(stage1Play.getActors().items[5].getX() + (cpt_translate_animationPlay * speed), stage1Play.getActors().items[5].getY());
                    }
            }
        }

        if(MyGdxGame.pause) {
            buttonSoundPlay.setVisible(true);
            buttonPlayPlay.setVisible(true);
            Timer.instance().stop();
        }
        else{
            Timer.instance().start();
            /*if(buttonSoundPlay.isVisible())
                buttonSoundPlay.setVisible(false);
            if(buttonExitGameOver.isVisible())
                buttonExitGameOver.setVisible(false);*/
        }

        if(MyGdxGame.isSoundEnable() == 0){
            Button.ButtonStyle buttonSoundStyle = new Button.ButtonStyle();
            buttonSoundStyle.up = skinPlay.getDrawable("buttonSound2Mute");
            buttonSoundStyle.down = skinPlay.getDrawable("buttonSound2Mute");
            buttonSoundPlay.setStyle(buttonSoundStyle);
        }
        if(MyGdxGame.isSoundEnable() == 1){
            Button.ButtonStyle soundButtonStyle = new Button.ButtonStyle();
            soundButtonStyle.up = skinPlay.getDrawable("buttonSound2Fx");
            soundButtonStyle.down = skinPlay.getDrawable("buttonSound2Fx");
            buttonSoundPlay.setStyle(soundButtonStyle);
        }
        if(MyGdxGame.isSoundEnable() == 2){
            Button.ButtonStyle soundButtonStyle = new Button.ButtonStyle();
            soundButtonStyle.up = skinPlay.getDrawable("buttonSound2Full");
            soundButtonStyle.down = skinPlay.getDrawable("buttonSound2Full");
            buttonSoundPlay.setStyle(soundButtonStyle);
        }
    }

    private void setButtonColor(Button button, String color1, String color2){
        Button.ButtonStyle buttonStyle = new Button.ButtonStyle();
        buttonStyle.up = skinPlay.getDrawable(color1);
        buttonStyle.down = skinPlay.getDrawable(color2);
        button.setStyle(buttonStyle);
    }

    private void fireBallIA(Enemy enemy){
        //Iterator<FireBall> iter = fireBalls.iterator();
        //while (iter.hasNext()) {
        //final FireBall fireBall = iter.next();
        for (FireBall fireBall: fireBalls) {
            //fireBall.render(sb);
            if(!MyGdxGame.pause){

                //fireBall.update(1/6f);
                fireBall.updateBoundingBox(fireBall,64,64);

                if(!fireBall.getDead() && (fireBall.getPosition().x < 0 | fireBall.getPosition().x > Gdx.graphics.getWidth()/PPM)){
                    Gdx.app.debug(LOG_TAG,"FIRE BALL REMOVED!");
                    fireBall.setDead(true);
                    fireBalls.removeValue(fireBall, true);
                    //removeBodySafely(fireBall.getBody());
                    bodyToDestroy.add(fireBall);
                    //fireBall.destroy();
                }

                if(!enemy.isDead() && cl.intersect(fireBall,enemy)){
                    if(Save.gd.isFireBallEquiped()){
                        fireBall.setDead(true);
                        fireBalls.removeValue(fireBall, true);
                        bodyToDestroy.add(fireBall);
                        //fireBall.destroy();
                        enemy.setHealth(0);
                        Gdx.app.debug(LOG_TAG,"FIRE BALL 1 TOUCH ENEMY!");
                    }

                    if(Save.gd.isFireBall2Equiped()){
                        fireBall.setHealth(fireBall.getHealth()-1);
                        enemy.setHealth(0);
                        if (fireBall.getHealth() <= 0){
                            fireBall.setDead(true);
                            fireBalls.removeValue(fireBall, true);
                            bodyToDestroy.add(fireBall);
                        }
                        Gdx.app.debug(LOG_TAG,"FIRE BALL 2 TOUCH ENEMY!");
                    }

                }

            }
        }
    }

    private void lightningIA(Enemy enemy){
        if(!MyGdxGame.pause && lightning != null){
            if(!MyGdxGame.pause){
                lightning.update(MyGdxGame.STEP);
            }
            enemy.updateBoundingBox(enemy,64,64);
            if(cl.intersect(lightning,enemy)){
                enemy.setHealth(-99);
            }
        }
    }

    private void lightningUpdate(float dt){

        if(!MyGdxGame.pause && lightning != null){


            lightning.update(dt);

            if(lightning.getBody().getPosition().x + lightning.getWidth()/PPM >= MyGdxGame.V_WIDTH/PPM){
                lightning.getBody().setLinearVelocity(-4, lightning.getBody().getLinearVelocity().y);
                lightning.normalAnimation();
            }
            if(lightning.getBody().getPosition().x <= 0){
                lightning.normalAnimation_rev();
                lightning.getBody().setLinearVelocity(4, lightning.getBody().getLinearVelocity().y);
            }



            float speed = Gdx.graphics.getWidth()/64f;

            if(Math.abs(beamWidth)/PPM > powerUpBar_MaxHeight){
                lightningReachLimit = !lightningReachLimit;
            }


            //Gdx.app.debug(LOG_TAG,"beamWidth:"+beamWidth+" "+lightningReachLimit);

            if(!lightningReachLimit){
                if(!MyGdxGame.pause){
                    beamWidth+= speed;
                }

            }else{
                firePlay = false;
                lightningReachLimit = false;
                beamWidth = 0;
                blockLightning = true;
                lightningRunning = false;
                bodyToDestroy.add(lightning);
                //lightning.destroy();
                lightning = null;

            }



        }

    }

    private void kamehamehaIA(){
        //kamehameha
        float gap = 1.02f*PPM;
        float gap2 = 0.4f*PPM;
        float scale = 4.0f;

        float x = 0.0f;
        float y = 0.0f;
        float w = 0.0f;
        float h = 0.0f;

        float speed = Gdx.graphics.getWidth()/64f;

        float reduce = 1.0f;

        float scaledValue = Math.abs(1 - ((beamWidth - 0) / (powerUpBar_MaxHeight*PPM)));


        if(Math.abs(beamWidth)/PPM > powerUpBar_MaxHeight){
            kamehaReachLimit = !kamehaReachLimit;
        }

        if(Math.abs(beamWidth)/PPM > powerUpBar_MaxHeight/1.5f){
            reduce = (float) Math.exp(scaledValue*1.75f);
            megaKameha = true;
        }else{
            megaKameha = false;
        }


        if(!kamehaReachLimit){
            if(!MyGdxGame.pause)
                beamWidth+= speed;
        }else{
            firePlay = !firePlay;
            kamehaReachLimit = false;
            beamWidth = 0;
            blockKamehameha = true;
        }

        sb.begin();
        if(beamWidth>0){


            float beamX;
            if(player.isRight()){
                sb.draw(animKamehameha0.getFrame(),
                        player.getPosition().x*PPM + gap2,
                        player.getPosition().y*PPM - animKamehameha0.getFrame().getRegionHeight()*reduce*scale/2,
                        animKamehameha0.getFrame().getRegionWidth()*scale,
                        animKamehameha0.getFrame().getRegionHeight()*scale*reduce);

                sb.draw(animKamehameha1.getFrame(),
                        player.getPosition().x*PPM + gap2 + animKamehameha0.getFrame().getRegionWidth()*scale,
                        player.getPosition().y*PPM - animKamehameha1.getFrame().getRegionHeight()*reduce*scale/2,
                        beamWidth,
                        animKamehameha1.getFrame().getRegionHeight()*scale*reduce);

                sb.draw(animKamehameha2.getFrame(),
                        player.getPosition().x*PPM + gap2 + beamWidth +  animKamehameha2.getFrame().getRegionWidth()*scale/4,
                        player.getPosition().y*PPM - animKamehameha2.getFrame().getRegionHeight()*reduce*scale/2,
                        animKamehameha2.getFrame().getRegionWidth()*scale,
                        animKamehameha2.getFrame().getRegionHeight()*scale*reduce);

                beamX = player.getPosition().x*PPM + gap2 + beamWidth +  animKamehameha2.getFrame().getRegionWidth()*scale/4 + animKamehameha2.getFrame().getRegionWidth()*scale;

                x = player.getPosition().x*PPM + gap2;
                y = player.getPosition().y*PPM - animKamehameha0.getFrame().getRegionHeight()*scale/2;
                w = beamX + animKamehameha2.getFrame().getRegionWidth()/3f;
                h = y + animKamehameha2.getFrame().getRegionHeight()*scale;

            }
            else {
                sb.draw(animKamehameha0_rev.getFrame(),
                        player.getPosition().x*PPM - gap,
                        player.getPosition().y*PPM - animKamehameha0_rev.getFrame().getRegionHeight()*reduce*scale/2,
                        animKamehameha0.getFrame().getRegionWidth()*scale,
                        animKamehameha0.getFrame().getRegionHeight()*scale*reduce);

                sb.draw(animKamehameha1_rev.getFrame(),
                        player.getPosition().x*PPM - gap/1.15f,
                        player.getPosition().y*PPM - animKamehameha1.getFrame().getRegionHeight()*reduce*scale/2,
                        animKamehameha1_rev.getFrame().getRegionWidth()*scale - beamWidth,
                        animKamehameha1.getFrame().getRegionHeight()*scale*reduce);

                sb.draw(animKamehameha2_rev.getFrame(),
                        player.getPosition().x*PPM - gap/1.15f - beamWidth - animKamehameha2_rev.getFrame().getRegionWidth()*scale + animKamehameha1_rev.getFrame().getRegionWidth()*scale,
                        player.getPosition().y*PPM - animKamehameha2_rev.getFrame().getRegionHeight()*reduce*scale/2,
                        animKamehameha2_rev.getFrame().getRegionWidth()*scale,
                        animKamehameha2_rev.getFrame().getRegionHeight()*scale*reduce);

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

        //Gdx.app.debug(LOG_TAG,"beamX="+beamX);

    }


    private boolean isEnemyTouchingBrick(){
        boolean result = false;
        for(Enemy enemy:enemies) {
            if(cl.intersect(brick,enemy) && !enemy.isDead()){
                result = true;
            }
        }
        return result;
    }

    private void stopEnemyIfTouchBrick(Enemy enemy){
        if(!enemy.isFading() && !enemy.isDead()&& !brick.getBroken() && cl.intersect(brick,enemy)){
            enemy.setStop(true);
            if(enemy.isFromLeft()){
                if(!enemy.isNormalLeft())
                    enemy.normalAnimation_rev();
            }
            brick.setLife(brick.getLife()-1);
        }else {
            enemy.setStop(false);
        }
    }


    private void brickIA(Enemy enemy){

        if(brick.isFalling()){
            makeBrickSensor(brick, true);
        }else{
            makeBrickSensor(brick, false);
        }

        if(brick != null){
            brick.updateBoundingBox(brick,64,64);
            brick.update(MyGdxGame.STEP);
        }
        //make brick fall from the sky when summoned
        if(brick != null && brick.isFalling()){

            if(brick.getX() == 0){
                brick.setX(lastClickPos);
            }
            if(cl.intersect(brick,enemy)){
                enemy.setHealth(-99);
            }

        }else{
            stopEnemyIfTouchBrick(enemy);
            if(brick.getLife() <= 0){
                //brick destroyed in anim function
                if(!brick.getBroken() && brick != null){
                    new java.util.Timer().schedule(
                            new java.util.TimerTask() {
                                @Override
                                public void run() {
                                    if(brick != null){
                                        brick.getBody().setType(BodyType.DynamicBody);//can't destroy static body for some reason.
                                        bodyToDestroy.add(brick);
                                        //brick.destroy();
                                        brick = null;
                                    }
                                }
                            },
                            500
                    );

                    brick.brokeAnimation();
                    if (MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) {
                        MyGdxGame.res.getSound("broken").play();
                    }
                    brick.setDead(true);
                    brick.getBody().setGravityScale(0);
                    makeBrickSensor(brick, true);
                    cl.setNumFootBrickContacts(0);
                    enableBrick = true;
                }
            }
        }
    }

    private void climbingIA(Enemy enemy){
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

    private void enemyJumpOverIA(Enemy enemy){
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

    private void enemiesIA(){

//        Iterator<Enemy> iter = enemies.iterator();
//
//        while (iter.hasNext()) {
        //final Enemy enemy = iter.next();
        for (Enemy enemy: enemies) {

            //stop rendering the enemy after die animation
            if(enemy.getCptDieRunning() < 80){
                enemy.render(sb);
            }

            if (enemy.isStop()){
                enemy.getBody().setLinearVelocity(0,0);
            }

            if(!MyGdxGame.pause){
                enemy.update(MyGdxGame.STEP);

                //if(!MyGdxGame.pause){
                enemy.updateBoundingBox(enemy,64,64);

                if(brick != null){
                    brickIA(enemy);
                }

                lightningIA(enemy);
                if(princess != null){
                    princessIA(enemy);
                }

                fireBallIA(enemy);

                if(enemy.getHealth()<=0 | enemy.isDead()){

                    /**ENEMY IS DEAD**/
                    if(MyGdxGame.isTutorial){
                        sb.begin();
                        sb.draw(animLabelMoney.getFrame(),
                                player.getPosition().x*PPM - animLabelMoney.getFrame().getRegionWidth()/2,
                                player.getPosition().y*PPM + animLabelMoney.getFrame().getRegionHeight()/4,
                                (float)animLabelMoney.getFrame().getRegionWidth(),
                                (float)animLabelMoney.getFrame().getRegionHeight());
                        sb.end();
                    }

                    enemy.getBody().setLinearVelocity(new Vector2(0,0));
                    enemy.setCptDieRunning(enemy.getCptDieRunning()+1);

                    if(!enemy.isDead()){
                        player.collectCoin();
                        if (MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getSound("boom").play();
                        if(Save.gd.isFireBallEquiped() && enemiesKilled >= 4 && player.getFireBallCount()< MAXFIREBALLCOUNT){
                            firePlay = false;
                            player.setFireBallCount(player.getFireBallCount()+4);
                            enemiesKilled = 0;
                            Gdx.app.debug(LOG_TAG,"increment fireball count!");
                        }else{
                            enemiesKilled++;
                            Gdx.app.debug(LOG_TAG,"enemy killed="+enemiesKilled);
                        }

                        if(Save.gd.isFireBall2Equiped() && enemiesKilled >= 10 && player.getFireBallCount()<MAXFIREBALLCOUNT){
                            firePlay = false;
                            player.setFireBallCount(player.getFireBallCount()+1);
                            enemiesKilled = 0;
                            Gdx.app.debug(LOG_TAG,"increment fireball count!");
                        }else{
                            enemiesKilled++;
                            Gdx.app.debug(LOG_TAG,"enemy killed="+enemiesKilled);
                        }
                        enemy.setDead(true);
                    }

                    climbingIA(enemy);

                    //todo timer
                    if(enemy.getCptDieRunning() > 80){
                        enemy.setDead(true);
                        enemies.removeValue(enemy, true);
                        bodyToDestroy.add(enemy);
                        //enemy.destroy();
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

                                //nemy.getBody().setLinearVelocity(enemy.getBody().getLinearVelocity().x/2, enemy.getBody().getLinearVelocity().yMenu);
                                if(enemy.isClimbing()){

                                    if(!enemy.isFromLeft()){

                                        if(!enemy.isHurtRight()){
                                            //enemy.getBody().setTransform((MyGdxGame.V_WIDTH/PPM)-65/PPM, enemy.getPosition().yMenu, enemy.getBody().getAngle());
                                            enemy.hurtAnimation(true);
                                        }
                                    }
                                    if(enemy.isFromLeft()){
                                        if(!enemy.isHurtLeft()){
                                            //enemy.getBody().setTransform(65/PPM, enemy.getPosition().yMenu, enemy.getBody().getAngle());
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

                                        posClimb = (float) (posClimb+ difficulty /1000.0f);

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
                                }
                            }
                        }
                    }
                }
                //}
            }
        }
    }

    private void princessIA(Enemy enemy){




        if(MyGdxGame.pause){
            princess.update(MyGdxGame.STEP);
            princess.updateBoundingBox(princess,64,64);
        }
        if(!enemy.isDead() && princess.getBoundingBox().intersects(enemy.getBoundingBox()) && enemy.getBody().getLinearVelocity().x != 0 ){
            princess.setTouched(true);

            if(MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getSound("alarm").stop();

            if((enemy.getBody().getLinearVelocity().x < 0) && !enemy.isMockRight()){
                enemy.mockAnimation();
                Gdx.app.debug(LOG_TAG,"mockAnimation");
            }
            if((enemy.getBody().getLinearVelocity().x > 0) && !enemy.isMockLeft()){
                enemy.mockAnimation_rev();
                Gdx.app.debug(LOG_TAG,"mockAnimation_rev");
            }
        }


        if(princess.isTouched()){
            stopEnemies = true;
            enemy.setStop(true);
            //player.setPlayerDead(true);

            princess.getBody().setLinearVelocity(0,0);
            princess.getBody().setType(BodyType.StaticBody);

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

            if(MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2){
                if(MyGdxGame.res.getMusic("level1").isPlaying()){
                    MyGdxGame.res.getMusic("level1").stop();
                }
            }

            //TODO go to game over
            if(!gameOverPlay){
                gameOverPlay = true;
                Gdx.app.debug(LOG_TAG,"GAMEOVER!");
                submitPlay = true;
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

    }

    private void purgeBodyToDestroy(){
        for (B2DSprite sprite: bodyToDestroy) {

            if (sprite.getBody() == null) continue;

            sprite.destroy();

            bodyToDestroy.removeValue(sprite, true);
        }
    }

    private boolean isAddingBody(){
        return (addNewEnemy | addPowerUpOrBrick);
    }

    public void updateTutorial(){

        MyGdxGame.pause = false;

        if(!tuto_step0){
            if (MyGdxGame.isSoundEnable() == 2){
                if(!MyGdxGame.res.getMusic("shop").isPlaying()){
                    MyGdxGame.res.getMusic("shop").play();
                }
            }
            labelScorePlay.setVisible(false);
        }else {
            labelScorePlay.setVisible(true);

        }

        //user picked gamplay style
        if(!tuto_step0){
            gamePlaySelection.setVisible(true);
            buttonGp1.setVisible(true);
            buttonGp2.setVisible(true);
            buttonGp1_label.setVisible(true);
            buttonGp2_label.setVisible(true);
            buttonFire.setVisible(false);
        }else{
            gamePlaySelection.setVisible(false);
            buttonGp1.setVisible(false);
            buttonGp2.setVisible(false);
            buttonGp1_label.setVisible(false);
            buttonGp2_label.setVisible(false);
            buttonFire.setVisible(true);
        }

        if(MyGdxGame.pickedGameplay == 2){
            buttonLeft.setVisible(true);
            buttonRight.setVisible(true);
            buttonJump.setVisible(true);
        }else {
            buttonLeft.setVisible(false);
            buttonRight.setVisible(false);
            buttonJump.setVisible(false);
        }

        MyGdxGame.lastPlayerPosition = new Vector2(player.getPosition());

        if(princess != null)
            MyGdxGame.lastPrincessPosition = new Vector2(princess.getPosition());

        if(brick != null){
            MyGdxGame.lastBrickPosition = new Vector2(brick.getPosition());

            if(brick.getDead()){
                MyGdxGame.lastBrickPosition = new Vector2(0,0);
            }
        }


        buttonPausePlay.setVisible(false);
        buttonPausePlay.setTouchable(null);

        if(MyGdxGame.pickedGameplay == 1){
            tutoGamePlay1();
        }

        if(MyGdxGame.pickedGameplay == 2){
            tutoGamePlay2();
        }

        try
        {
            MyGdxGame.lastScoreInTutorial = Integer.valueOf(labelScorePlay.getText().toString());
        }catch (NumberFormatException e){
            Gdx.app.error(LOG_TAG,"error while parsing scoreGameOver",e);
        }


    }

    private void tutoGamePlay1(){
        //pointer.getBody().setTransform(MyGdxGame.V_WIDTH/2/PPM - pointer.getWidth()/6/PPM, pointer.getPosition().yMenu, pointer.getBody().getAngle());

        if(!tuto_step2) right = false;
        if(!tuto_step4) jump = false;
        if(!tuto_step5) firePlay = false;


        //move pointer from rightMenu to leftMenu
        if(!tuto_step1 && !tuto_step2){

            if(!pointer.getRotate45()) pointer.rotateAnimation45();

            if(pointer.getPosition().x < pointer.getWidth()/PPM){
                cpt_tuto = 0;
                pointer.getBody().setTransform(MyGdxGame.V_WIDTH/PPM - pointer.getWidth()/PPM , MyGdxGame.V_HEIGHT/3/PPM, pointer.getBody().getAngle());
            }else{
                cpt_tuto++;
                pointer.getBody().setTransform(MyGdxGame.V_WIDTH/PPM - pointer.getWidth()/PPM - (float) cpt_tuto/15f, MyGdxGame.V_HEIGHT/3/PPM, pointer.getBody().getAngle());
            }
        }

        if(left && !tuto_step2 && player.getPosition().x <= 64/PPM ){

            if(MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getSound("interaction").play();
            cpt_tuto = 0;
            hidePointer = true;
            tuto_step1 = true;
            tuto_step2 = true;
            pointer.getBody().setTransform(pointer.getWidth()/PPM , MyGdxGame.V_HEIGHT/3/PPM, pointer.getBody().getAngle());
        }

        //move pointer from leftMenu to rightMenu
        if(!left && tuto_step1 && tuto_step2 && !tuto_step3){



            hidePointer = false;

            if(!pointer.getRotate45f()) pointer.rotateAnimation45_rev();

            if(pointer.getPosition().x < MyGdxGame.V_WIDTH/PPM - pointer.getWidth()/PPM){
                cpt_tuto++;
                pointer.getBody().setTransform(pointer.getPosition().x + (float) cpt_tuto/300f, MyGdxGame.V_HEIGHT/3/PPM, pointer.getBody().getAngle());
            }else{
                pointer.getBody().setTransform(pointer.getWidth()/PPM , MyGdxGame.V_HEIGHT/3/PPM, pointer.getBody().getAngle());
                cpt_tuto = 0;
            }
        }

        if(right && tuto_step2 && !tuto_step3){


            if(player.getPosition().x > MyGdxGame.V_WIDTH/1.8f/PPM ){
                if(!tuto_step3)
                    if(MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getSound("interaction").play();
                tuto_step3 = true;
            }


            hidePointer = true;
            pointer.getBody().setTransform(MyGdxGame.V_WIDTH/2/PPM, MyGdxGame.V_HEIGHT/1.8f/PPM, pointer.getBody().getAngle());
            cpt_tuto = 0;
        }


        //move pointer from top to bottom
        if (tuto_step3 && !tuto_step4){



            if(!pointer.getRotate90f()) pointer.rotateAnimation90_rev();

            if(hidePointer)
                //if(MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getSound("interaction").play();

                hidePointer = false;

            if(player.getPosition().x <= MyGdxGame.V_WIDTH/8/PPM ){
                System.out.println("test3");
                left = false;
                right = true;
            }else if(player.getPosition().x > MyGdxGame.V_WIDTH/1.1f/PPM ){
                right = false;
                left = true;
            }


            if(pointer.getPosition().y < MyGdxGame.V_HEIGHT/1.9f/PPM - pointer.getHeight()/PPM){
                pointer.getBody().setTransform(MyGdxGame.V_WIDTH/2/PPM, MyGdxGame.V_HEIGHT/1.7f/PPM, pointer.getBody().getAngle());
                cpt_tuto = 0;
            }else{
                cpt_tuto++;
                pointer.getBody().setTransform(pointer.getPosition().x, pointer.getPosition().y - (float) cpt_tuto/400f, pointer.getBody().getAngle());
            }



        }

        if(!tuto_step4 && tuto_step3 && !right && !left){
            hidePointer = true;
            tuto_step4 = true;
            if(MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getSound("interaction").play();
            pointer.getBody().setTransform( pointer.getPosition().x, MyGdxGame.V_WIDTH/1.1f/PPM - pointer.getHeight()/PPM, pointer.getBody().getAngle());
        }

        //move pointer from bottom to top
        if(!tuto_step5 && tuto_step4){

            hidePointer = false;

            if(!pointer.getRotate90()) pointer.rotateAnimation90();

            if(pointer.getPosition().y > MyGdxGame.V_HEIGHT/1.1f/PPM - pointer.getHeight()/PPM){
                pointer.getBody().setTransform(pointer.getPosition().x, MyGdxGame.V_WIDTH/1.2f/PPM - pointer.getHeight()/PPM , pointer.getBody().getAngle());
                cpt_tuto = 0;
            }else{
                cpt_tuto++;
                pointer.getBody().setTransform(pointer.getPosition().x, pointer.getPosition().y + (float) cpt_tuto/500f, pointer.getBody().getAngle());
            }
        }

        if(!tuto_step5 && jump){



            Timer.schedule(new Timer.Task(){
                @Override
                public void run() {
                    tuto_step5 = true;
                    if(MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getSound("interaction").play();
                    if(!pointer.getPlaying())
                        pointer.normalAnimation();

                    pointer.getBody().setTransform(pointer.getPosition().x, MyGdxGame.GROUND/1.25f, pointer.getBody().getAngle());
                }
            }, 1);


        }

        if(firePlay && !tuto_step6 && tuto_step5){
            tuto_step6 = true;
            displayBrickTip = true;
            if(!pointer.getWithCircles())
                pointer.normalAnimation_with_circles();
            pointer.getBody().setTransform(MyGdxGame.V_WIDTH/2.9f/PPM, pointer.getPosition().y*3.5f, pointer.getBody().getAngle());
        }

        if(brick != null && !tuto_step7){
            if(MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getSound("interaction").play();
            hidePointer = true;
            tuto_step7 = true;
            //bodyToDestroy.add(pointer);
            pointer = null;
            //displayBrickTip = false;


            Timer.schedule(new Timer.Task(){
                @Override
                public void run() {
                    isMalicious = false;
                    if(MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) {
                        if(!MyGdxGame.res.getMusic("level1").isPlaying()){
                            MyGdxGame.res.getMusic("level1").setVolume(1f);
                            MyGdxGame.res.getMusic("level1").play();
                            if(MyGdxGame.res.getMusic("shop").isPlaying())
                                MyGdxGame.res.getMusic("shop").pause();
                        }
                    }
                    enemies.add(createEnemy(1/PPM , MyGdxGame.GROUND, true));
                    enemies.add(createEnemy(MyGdxGame.V_WIDTH*1.0f/PPM , MyGdxGame.GROUND, false));
                }
            }, 2);

        }

        if(!tuto_step8 && player.getNumCoins() == 2){
            Timer.schedule(new Timer.Task(){
                @Override
                public void run() {
                    createPrincess(MyGdxGame.V_WIDTH/2/PPM, 2*MyGdxGame.GROUND);
                    if(MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getSound("interaction").play();
                }
            }, 2);

            Timer.schedule(new Timer.Task(){
                @Override
                public void run() {
                    //start
                    gsm.setState(GameStateManager.PLAY);
                    tuto_step1 = false;
                    tuto_step2 = false;
                    tuto_step3 = false;
                    tuto_step4 = false;
                    tuto_step5 = false;
                    tuto_step6 = false;
                    tuto_step7 = false;
                    tuto_step8 = false;
                    Save.gd.setFirstPlay(false);
                    Save.save();
                }
            }, 5);

            tuto_step8 = true;
        }
    }

    private void tutoGamePlay2(){
        if(!tuto_step1) right = false;
        if(!tuto_step3) firePlay = false;

        //if(!tuto_step4) brick = null;

        if(!tuto_step2) jump = false;

        if(!tuto_step1 && player.getPosition().x*PPM < 70){

            left = false;
            tuto_step1 = true;
            if(MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getSound("interaction").play();
            pointer.getBody().setTransform(MyGdxGame.V_WIDTH/2.8f/PPM, pointer.getPosition().y, pointer.getBody().getAngle());
        }

        if(tuto_step1 && !tuto_step2) {
            left = false;
            System.out.println("tuto_step2="+tuto_step2+" tuto_step1="+tuto_step1);
        }

        if(!tuto_step2 && tuto_step1 && player.getPosition().x*PPM > 300 ){
            right = false;
            tuto_step2 = true;
            if(MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getSound("interaction").play();
            pointer.getBody().setTransform(MyGdxGame.V_WIDTH/1.15f/PPM, pointer.getPosition().y, pointer.getBody().getAngle());
        }

        if(!tuto_step3 && tuto_step2 && jump){

            Timer.schedule(new Timer.Task(){
                @Override
                public void run() {
                    pointer.getBody().setTransform(MyGdxGame.V_WIDTH/1.7f/PPM, pointer.getPosition().y, pointer.getBody().getAngle());
                    tuto_step3 = true;
                    if(MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getSound("interaction").play();
                }
            }, 1);
        }

        if(!tuto_step4 && tuto_step3 && firePlay){
            tuto_step4 = true;


            Timer.schedule(new Timer.Task(){
                @Override
                public void run() {
                    if(pointer != null){
                        pointer.getBody().setTransform(MyGdxGame.V_WIDTH/2.9f/PPM, pointer.getPosition().y*3f, pointer.getBody().getAngle());

                        if(!pointer.getWithCircles())
                            pointer.normalAnimation_with_circles();
                    }


                    if(MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getSound("interaction").play();
                    displayBrickTip = true;
                }
            }, 1);
        }

        if(brick != null && !tuto_step5){
            displayBrickTip = false;
            tuto_step5 = true;
            if(MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getSound("interaction").play();
            //bodyToDestroy.add(pointer);
            pointer = null;


            Timer.schedule(new Timer.Task(){
                @Override
                public void run() {
                    isMalicious = false;
                    if(MyGdxGame.isSoundEnable() == 2) {
                        if(!MyGdxGame.res.getMusic("level1").isPlaying()){
                            MyGdxGame.res.getMusic("level1").setVolume(1f);
                            MyGdxGame.res.getMusic("level1").play();
                            if(MyGdxGame.res.getMusic("shop").isPlaying())
                                MyGdxGame.res.getMusic("shop").pause();
                        }
                    }
                    enemies.add(createEnemy(1/PPM , MyGdxGame.GROUND, true));
                    enemies.add(createEnemy(MyGdxGame.V_WIDTH*1.0f/PPM , MyGdxGame.GROUND, false));
                }
            }, 2);

        }

        if(!tuto_step6 && player.getNumCoins() == 2){
            Timer.schedule(new Timer.Task(){
                @Override
                public void run() {
                    createPrincess(MyGdxGame.V_WIDTH/2/PPM, 2*MyGdxGame.GROUND);
                    if(MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getSound("interaction").play();
                }
            }, 2);


            Timer.schedule(new Timer.Task(){
                @Override
                public void run() {
                    //start
                    gsm.setState(GameStateManager.PLAY);
                    tuto_step1 = false;
                    tuto_step2 = false;
                    tuto_step3 = false;
                    tuto_step4 = false;
                    tuto_step5 = false;
                    tuto_step6 = false;
                    Save.gd.setFirstPlay(false);
                    Save.save();
                }
            }, 5);

            tuto_step6 = true;
        }
    }

    public void update(float dt) {
        if(pointer != null ) {
            if(!pointer.getPlaying() && MyGdxGame.pickedGameplay == 2)pointer.normalAnimation();
            //if(MyGdxGame.pickedGameplay == 2 | (MyGdxGame.pickedGameplay == 1 && (tuto_step5|tuto_step6)))
            pointer.update(dt);
        }

        if(MyGdxGame.isTutorial){
            try{
                updateTutorial();
            }catch (NullPointerException e){
                Gdx.app.error(LOG_TAG,"error in tutorial objects",e);
                createHand(-99,-99);
            }

        }else{
            if(MyGdxGame.pickedGameplay == 2){
                buttonLeft.setVisible(true);
                buttonRight.setVisible(true);
                buttonJump.setVisible(true);
                buttonFire.setVisible(true);
            }else {
                buttonLeft.setVisible(false);
                buttonRight.setVisible(false);
                buttonJump.setVisible(false);
                buttonFire.setVisible(true);
            }
        }

        stopEnemies = enemies.size > 6;

        if(princess != null){
            //Iterator<Enemy> iter = enemies.iterator();
            for (Enemy enemy : enemies) {
                //while (iter.hasNext()) {
                //final Enemy enemy = iter.next();
                if(!MyGdxGame.pause){
                    if(!princess.isTouched()){
                        if(!enemy.isDead() && enemy.getPosition().y*PPM > 350){
                            princess.setCry(true);
                            if(MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) {
                                //if(!MyGdxGame.res.getSound("alarm").isPlaying()){


                                if(cpt_alarm == 0){
                                    MyGdxGame.res.getSound("alarm").play();
                                }
                                if(cpt_alarm > 30){
                                    cpt_alarm = 0;
                                    MyGdxGame.res.getSound("alarm").play();
                                }

                                cpt_alarm++;

                                //}
                            }
                            break;
                        }else{
                            princess.setCry(false);
                            //cpt_alarm = 0;
                            if(MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) {
                                //MyGdxGame.res.getSound("alarm").stop();
                                //if(MyGdxGame.res.getSound("alarm").isPlaying()){
                                //MyGdxGame.res.getSound("alarm").pause();
                                //System.out.println("2");
                                //}
                            }
                        }
                    }
                }
            }

        }

        String xyBrick = String.valueOf((brick == null)? "null":brick.getPosition().x*PPM+":"+brick.getPosition().y*PPM);

        MyGdxGame.debugString = "fps: "+Gdx.graphics.getFramesPerSecond()+'\n'+
                "java heap: "+ (int)(Gdx.app.getJavaHeap()/Math.pow(10, 6))+" Mb"+'\n'+
                "native heap: "+ (int)(Gdx.app.getNativeHeap()/Math.pow(10, 6))+" Mb"+'\n'+
                "bodyToDestroy: "+bodyToDestroy.size+'\n'+
                "isAddingBody: "+isAddingBody()+'\n'+
                "isStepping: "+isStepping+'\n'+
                "beamWidth: "+beamWidth+'\n'+
                "FireBallCount:"+player.getFireBallCount()+'\n'+
                "blockKamehameha: "+blockKamehameha+'\n'+
                "firePlay: "+ firePlay +'\n'+
                "maxEnemiesOnScreen: "+ maxEnemiesOnScreen+'\n'+
                "tuto_step1: "+ tuto_step1+'\n'+
                "tuto_step2: "+ tuto_step2+'\n'+
                "tuto_step3: "+ tuto_step3+'\n'+
                "tuto_step4: "+ tuto_step4+'\n'+
                "tuto_step5: "+ tuto_step5+'\n'+
                "tuto_step6: "+ tuto_step6+'\n'+
                "world contact: "+ world.getContactCount()+'\n'+
                "offDisplay: "+ offsetYPlay +'\n'+
                "enemy count: "+ enemies.size+'\n'+
                "onGround: "+ cl.isPlayerOnGround()+'\n'+
                "onBrick: "+ cl.isPlayerOnBrick()+'\n'+
                "xPlayer: "+ (int)(player.getPosition().x*PPM)+'\n'+
                "yPlayer: "+ (int)(player.getPosition().y*PPM)+'\n'+
                "xyBrick: "+ xyBrick+'\n'+
                "velocity: "+ player.getBody().getLinearVelocity()+'\n'+
                "R: "+ player.isRight()+'\n'+
                "L: "+ player.isLeft()+'\n'+
                "oSwipe: "+ lastClickPos+'\n';

        MyGdxGame.fadeIn.update(dt);

        if(brick != null){

            if(brick.getPosition().y*PPM <= 264.8){
                brick.setFalling(false);
            }

            if(brick.isFalling() | brick.getDead()){
                makeBrickSensor(brick, true);
            }else{
                makeBrickSensor(brick, false);
            }

            if(brick.getBody().getLinearVelocity().x != 0.0f){
                if ((MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2)) {
                    if(!MyGdxGame.res.getMusic("move").isPlaying())
                        MyGdxGame.res.getMusic("move").play();
                }
            }

            //block brick at screen extremities
            if(!brick.isFalling()){
                if (brick.getPosition().x * PPM <= brick.getWidth()/2) {
                    brick.getBody().setType(BodyType.DynamicBody);
                    brick.getBody().setLinearVelocity(0,0);
                    brick.getBody().setTransform(brick.getWidth()/2/PPM, brick.getPosition().y, brick.getBody().getAngle());
                }else if(brick.getPosition().x*PPM + brick.getWidth()/2 >= Gdx.graphics.getWidth()){
                    //brick.getBody().setGravityScale(0);
                    brick.getBody().setType(BodyType.DynamicBody);
                    brick.getBody().setLinearVelocity(0,0);
                    brick.getBody().setTransform(MyGdxGame.V_WIDTH/PPM - brick.getWidth()/2/PPM, brick.getPosition().y, brick.getBody().getAngle());
                }
            }


            if(player.getPosition().y*PPM > 256.5 + 64*1.5f){
                cl.setNumFootBrickContacts(0);
            }


            if( brick.getPosition().y*PPM <= 256.5 && (isEnemyTouchingBrick())){
                brick.hurtAnimation();
                //Gdx.app.debug(LOG_TAG,"hurt"+" "+world.getContactCount()+" "+Gdx.graphics.getFramesPerSecond());
                brick.getBody().setType(BodyType.StaticBody);

            }else{
                brick.normalAnimation();
                brick.getBody().setType(BodyType.DynamicBody);
            }

            if(brick != null && !brick.isFalling()){
                brick.getBody().setLinearVelocity(0,0);
            }
        }

//        if(lightning != null && Save.gd.isLightningEquiped()){
//            lightningUpdate(dt);
//        }

        if(princess != null){
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


            if(princess.isDieLeft()||princess.isDieRight()){
                princess.getBody().setLinearVelocity(0,0);
                princess.getBody().setType(BodyType.StaticBody);
            }
        }



        if( (cl.isPlayerOnGround() |cl.isPlayerOnBrick() ) && jump && !player.isJumpRight() && !player.isJumpRight() /*&& (player.getPosition().yMenu*PPM) < 625*/ ){
            jump = false;
            if (MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getSound("jump1").play();
            player.getBody().setLinearVelocity(new Vector2(player.getBody().getLinearVelocity().x,0));
            player.getBody().setAngularVelocity(0);
            //player.getBody().applyForceToCenter(new Vector2(0,180),false);
            if(Save.gd.isMegaJumpEquiped()){
                player.getBody().applyForceToCenter(new Vector2(0,220),false);
            }else{
                player.getBody().applyForceToCenter(new Vector2(0,180),false);
            }
        }


        if (Math.abs(player.getBody().getLinearVelocity().x) != 0) {
            MyGdxGame.background_wood1.setVector(+10, 0);
        }else{
            MyGdxGame.background_wood1.setVector(+10, 0);
        }

        //Gdx.app.debug(LOG_TAG,"X: " + player.getBody().getPosition().x);
        labelMoneyPlay.setText(Integer.toString(Save.gd.getMoney()));


        if(MyGdxGame.pause) dt = 0;

        pauseUiAnimation();

        updatePlayerAnimation();

        sb.setProjectionMatrix(hudCam.combined);
        sb.begin();
        stage1Play.getActors().items[2].draw(sb, 1f);
        sb.end();

        MyGdxGame.background_wood1.update(dt);


        if(!isAddingBody()){
            world.step(dt, 6, 2);
            isStepping = true;
            purgeBodyToDestroy(); /**MUST BE DONE AFTER WORLD STEP http://badlogicgames.com/forum/viewtopic.php?t=8459&p=38446**/
        }else{
            isStepping = false;
            //if(!isTutorial){
            difficulty = 1.0+player.getNumCoins()/50;
            if(getRandomBoolean()){
                //if(!firePlay) //helps fireball animation when many enemies
                enemies.add(createEnemy(MyGdxGame.V_WIDTH*1.0f/PPM , MyGdxGame.GROUND, false));
            }else{
                //if(!firePlay) //helps fireball animation when many enemies
                enemies.add(createEnemy(1/PPM , MyGdxGame.GROUND, true));
            }
            //}

        }


        if(!MyGdxGame.pause){
            player.update(dt);

            player.updateBoundingBox(player,64,64);
            if(princess != null){
                princess.update(dt);
                princess.updateBoundingBox(princess,64,64);
            }

            if(MyGdxGame.isTutorial){
                animLabelMoney.update(dt);
                animKillEnemy.update(dt);
                animAh.update(dt);
                animTip.update(dt);
            }

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


        labelScorePlay.setText(Integer.toString(player.getNumCoins()));

        if(player.getNumCoins() >= 100){
            enemyIsNextLevel = getRandomBoolean();
        }

        if(player.getBody().getPosition().y < -0.3f){

            if ((MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) && !falling) {
                MyGdxGame.res.getMusic("falling").play();
                falling = true;
            }
            player.getBody().setLinearVelocity(0, -3f);
        }

        //SUBMIT SCORE ONLY ONCE!
        if(submitPlay && !MyGdxGame.isTutorial) {
            submitPlay = false;
            MyGdxGame.setContinue(false);
            Save.load();
            boolean newHighScore = false;
            long highScores[] = Save.gd.getHighScores();
            for (long highScore : highScores) {
                newHighScore = (player.getNumCoins() > highScore);
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

            if(MyGdxGame.actionResolver != null){
                if (game.actionResolver.getSignedInGPGS()) {
                    game.actionResolver.getLeaderboardGPGS(true,player.getNumCoins());
                }
            }

        }

    }

    public void render() {
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        //MyGdxGame.background_skyDay.render(sb);



        for (FireBall fireBall: fireBalls) {
            if(fireBall.getDead()) continue;
            //final FireBall fireBall = iter.next();
            if(!MyGdxGame.pause){
                fireBall.update(1/5f);
            }
        }
        //Gdx.app.debug(LOG_TAG,"lightningRunning:"+lightningRunning);

        if(!lightningRunning){
            MyGdxGame.background_wood1.render(sb);
            MyGdxGame.background_cloud.render(sb);
        }

        if(lightning != null && Save.gd.isLightningEquiped()){
            lightning.updateBoundingBox(lightning, Gdx.graphics.getWidth()/10, 2*Gdx.graphics.getHeight() );
            lightningUpdate(MyGdxGame.STEP);
        }

        //if (MyGdxGame.isNightEnable())MyGdxGame.displayBlinkingStars();

        if(lightningRunning && !MyGdxGame.pause && !gameOverPlay){
            cam.position.x = baseX;
            cam.position.y = baseY;
            screenShake.update(MyGdxGame.STEP, cam);
            spriteBatch.setProjectionMatrix(cam.combined);
            spriteBatchLightning.setProjectionMatrix(cam.combined);
            cam.update();
        }else {
            if(baseX > 0){
                cam.position.x = baseX;
                cam.position.y = baseY;
            }
        }

        sb.setProjectionMatrix(cam.combined);

        cam.update();
        tmr.setView(cam);
        tmr.render();

        if(Gdx.input.justTouched()){
            Vector3 v = new Vector3(Gdx.input.getX(),Gdx.input.getY(),0);
            cam.unproject(v);
            lastClickPos = v.x/PPM;
        }

        if(!player.isPlayerDead()) {
            if(!MyGdxGame.pause)
                handleInput();

            if(!playerStartMoving) {
                baseX = cam.position.x;
                baseY = cam.position.y;
                Gdx.app.debug(LOG_TAG,"START MOVING!");
                MyGdxGame.background_cloud.setVector(+10, 0);
                player.getBody().setLinearVelocity(PLAYER_VELOCITY, 0);
                playerStartMoving = true;
            }
        }

        spriteBatch.begin();



        stage1Play.getActors().items[0].setPosition((Gdx.graphics.getWidth() - labelScorePlay.getWidth()) / 2,
                Gdx.graphics.getHeight() / 1.27f);


        if(lightning != null){
            //lightning.render(sb);
            sb.begin();
            sb.draw(lightning.getAnimation().getFrame(),(lightning.getPosition().x*PPM) - Gdx.graphics.getWidth()/3/2 ,-200,Gdx.graphics.getWidth()/3,1.2f*Gdx.graphics.getHeight());
            sb.end();
        }

        if(MyGdxGame.pickedGameplay == 2 | MyGdxGame.pickedGameplay == 1) {
            //power up bar

            float h = buttonRed.getHeight();

//            buttonRed.setHeight(buttonFire.getHeight()/1.6f);
//            buttonRed.setPosition(buttonFire.getX(), buttonFire.getY()*2.7f);

            if(Save.gd.isKamehamehaEquiped()){
                buttonRed.setHeight(h);

                if(reloadKamehameha != 0)
                    setButtonColor(buttonFire,"buttonActionDisabledUp", "buttonActionDisabledDown");
                else
                    setButtonColor(buttonFire,"buttonActionUp","buttonActionDown");


                if(!blockKamehameha){
                    reloadKamehameha = 0;
                    h = powerUpBar_MaxHeight - (beamWidth/ powerUpBar_MaxHeight)* offsetYPlay /PPM;
                    buttonRed.setHeight(h);
                }else{
                    if(reloadKamehameha <= powerUpBar_MaxHeight){
                        if(!MyGdxGame.pause) reloadKamehameha+=0.3;
                    }
                    else{
                        Gdx.app.debug(LOG_TAG,"kamehameha reloaded!");
                        if ((MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2)) {
                            MyGdxGame.res.getSound("equiped").play();
                        }
                        blockKamehameha = false;
                        reloadKamehameha = 0;
                    }
                    buttonRed.setHeight(reloadKamehameha);
                }
            }


            if(Save.gd.isLightningEquiped()){
                buttonRed.setHeight(h);
                if(reloadLightning != 0)
                    setButtonColor(buttonFire,"buttonActionDisabledUp", "buttonActionDisabledDown");
                else {
                    setButtonColor(buttonFire,"buttonActionUp","buttonActionDown");
                }

                if(!blockLightning){
                    reloadLightning = 0;
                    h = powerUpBar_MaxHeight - (beamWidth/ powerUpBar_MaxHeight)* offsetYPlay /PPM;
                    buttonRed.setHeight(h);
                }else{
                    if(reloadLightning <= powerUpBar_MaxHeight){
                        //Gdx.app.debug(LOG_TAG,"reloadLightning"+reloadLightning+" "+max);
                        if(!MyGdxGame.pause) reloadLightning+=0.1;
                    } else{

                        Gdx.app.debug(LOG_TAG,"lightning reloaded!");

                        if ((MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2)) {
                            MyGdxGame.res.getSound("equiped").play();
                        }
                        blockLightning = false;
                        reloadLightning = 0;
                        beamWidth = 0;
                    }
                    buttonRed.setHeight(reloadLightning);
                }

            }

            if(Save.gd.isFireBallEquiped()|Save.gd.isFireBall2Equiped()){
                buttonRed.setHeight(h);
                //setButtonColor(buttonFire,"red","buttonUiBossJumpDown");
                h = (powerUpBar_MaxHeight /MAXFIREBALLCOUNT)*player.getFireBallCount();
                if(player.getFireBallCount()>0){
                    buttonRed.setHeight(h);
                }
                if(player.getFireBallCount() <= 0){
                    buttonRed.setHeight(0);
                }
            }
        }

        spriteBatch.end();

        if(princess != null && !princess.isDead()) {
            princess.render(sb);
        }

        if(!player.isPlayerDead()) {
            player.render(sb);
        }

        for (FireBall fireBall: fireBalls) {
            fireBall.render(sb);
        }

        if(Save.gd.isKamehamehaEquiped() && firePlay){
            kamehamehaIA();
        }


        //performanceCounter.start();
        enemiesIA();
        // performanceCounter.stop();

        //Gdx.app.debug(LOG_TAG,performanceCounter.current);

        //princessIA();

        if(brick != null){
            brick.render(sb);
        }

        //equipment bar
        equipmentBar();

        spriteBatch.begin();
        stage1Play.act();
        stage1Play.draw();
        spriteBatch.end();



        //if(!MyGdxGame.pause){
        if(MyGdxGame.pickedGameplay > 0){
            stageUiControl.act();
            stageUiControl.draw();
        }

        labelScorePlay.setVisible(true);
        buttonPausePlay.setVisible(true);

        //}


        if(isTutorial && pointer != null && tuto_step0 && !hidePointer) {
            pointer.render(sb);
        }

        sb.setProjectionMatrix(cam2.combined);
        cam2.setPosition(player.getPosition().x, player.getPosition().y, 0);

        cam.update();

        if(DEBUG){

            shapeRenderer.setColor(Color.GREEN);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

            shapeRenderer.rect(player.getBoundingBox().getMin(new Vector3()).x, player.getBoundingBox().getMin(new Vector3()).y, player.getBoundingBox().getWidth(), player.getBoundingBox().getHeight());
            if(princess != null)
                shapeRenderer.rect(princess.getBoundingBox().getMin(new Vector3()).x, princess.getBoundingBox().getMin(new Vector3()).y, princess.getBoundingBox().getWidth(), princess.getBoundingBox().getHeight());
            shapeRenderer.rect(boundingBoxKamehameha.getMin(new Vector3()).x, boundingBoxKamehameha.getMin(new Vector3()).y, boundingBoxKamehameha.getWidth(), boundingBoxKamehameha.getHeight());
            //shapeRenderer.rect(boundingBoxCastle.getMin().x, boundingBoxCastle.getMin().yMenu, boundingBoxCastle.getWidth(), boundingBoxCastle.getHeight());
            if(lightning != null){
                shapeRenderer.setColor(Color.RED);
                shapeRenderer.rect(lightning.getBoundingBox().getMin(new Vector3()).x, lightning.getBoundingBox().getMin(new Vector3()).y, lightning.getBoundingBox().getWidth(), lightning.getBoundingBox().getHeight());
                shapeRenderer.setColor(Color.GREEN);
            }

            shapeRenderer.end();

            //Iterator<Enemy> iterEnemies = enemies.iterator();
            //while (iterEnemies.hasNext()) {
            for (Enemy enemy: enemies) {
                //Enemy enemy = iterEnemies.next();
                if(enemy != null && lightning != null){
                    if(cl.intersect(enemy,lightning)){
                        spriteBatch.begin();
                        font.setColor(Color.BLUE);
                        //font.drawMultiLine(spriteBatch, "true", enemy.getBoundingBox().getMin(new Vector3()).x, enemy.getBoundingBox().getMin(new Vector3()).yMenu, enemy.getBoundingBox().getWidth(), BitmapFont.HAlignment.LEFT);
                        spriteBatch.end();
                    }else{
                        spriteBatch.begin();
                        font.setColor(Color.RED);
                        //font.drawMultiLine(spriteBatch, "false", enemy.getBoundingBox().getMin(new Vector3()).x, enemy.getBoundingBox().getMin(new Vector3()).yMenu, enemy.getBoundingBox().getWidth(), BitmapFont.HAlignment.LEFT);
                        spriteBatch.end();
                    }


                }
            }

        }


        if(MyGdxGame.isTutorial && princess != null){
            sb.begin();
            sb.draw(animKillEnemy.getFrame(),
                    princess.getPosition().x*PPM - animKillEnemy.getFrame().getRegionWidth()/2,
                    princess.getPosition().y*PPM + animKillEnemy.getFrame().getRegionHeight()/4,
                    (float)animKillEnemy.getFrame().getRegionWidth(),
                    (float)animKillEnemy.getFrame().getRegionHeight());
            sb.end();
        }


        //compute scream princess bubble animation
        if(!hideScream && princess != null && MyGdxGame.createdPrincessInTuTo){
            cpt_scream++;
            float w;
            float h;
            float a = (float)animAh.getFrame().getRegionWidth();
            float b = (float)animAh.getFrame().getRegionWidth()*(float)cpt_scream/50f;
            float c = (float)animAh.getFrame().getRegionHeight();
            float d = (float)animAh.getFrame().getRegionHeight()*(float)cpt_scream/50f;

            if(b > a) w = b;
            else w = a;

            if(d > c) h = d;
            else h = c;

            sbKyaa.setProjectionMatrix(cam.combined);
            sbKyaa.begin();

            float alpha = 1.0f;
            if(cpt_scream > 40){
                cpt_scream_fade++;
                alpha = 1 - (float)Math.pow(cpt_scream_fade,2)/6/100f;
                if(alpha < 0) alpha = 0;
            }

            sbKyaa.setColor(sb.getColor().r,sb.getColor().g,sb.getColor().b,alpha);

            sbKyaa.draw(animAh.getFrame(),
                    princess.getPosition().x*PPM - w/2,
                    princess.getPosition().y*PPM + h/4,
                    w,
                    h);
            sbKyaa.end();
        }

        if(MyGdxGame.isTutorial && tuto_step5 && !tuto_step6 && pickedGameplay == 1){
            sb.begin();
            sb.draw(animTip.getFrame(),
                    player.getPosition().x*PPM - animTip.getFrame().getRegionWidth()/2,
                    player.getPosition().y*PPM + animTip.getFrame().getRegionHeight()/13,
                    (float)animTip.getFrame().getRegionWidth(),
                    (float)animTip.getFrame().getRegionHeight());
            sb.end();
        }



        sb.begin();

        font2.setColor(Color.WHITE);

        String value = String.valueOf(player.getFireBallCount());

        if(Save.gd.isFireBallEquiped()){
            value = String.valueOf((int)Math.round((float)player.getFireBallCount()/3f));
        }

        if(player.getFireBallCount()< 1){
            value = "0";
        }

        if(Save.gd.isFireBall2Equiped() | Save.gd.isFireBallEquiped()){
            if(pickedGameplay == 2){
                //font2Credit.setColor(Color.WHITE);


//                font2Credit.drawMultiLine(sb,
//                        value ,
//                        MyGdxGame.V_WIDTH/1.733f,
//                        (MyGdxGame.V_HEIGHT/7.5f) - 11f*offsetYPlay/PPM,
//                        MyGdxGame.V_WIDTH/10f,
//                        BitmapFont.HAlignment.CENTER);
            }

            if(pickedGameplay == 1){

//                font2Credit.drawMultiLine(sb,
//                        value ,
//                        MyGdxGame.V_WIDTH/2.205f,
//                        (MyGdxGame.V_HEIGHT/7.5f) - 11f*offsetYPlay/PPM,
//                        MyGdxGame.V_WIDTH/10f,
//                        BitmapFont.HAlignment.CENTER);
            }
        }


        sb.end();


        sb.setProjectionMatrix(hudCam.combined);

        if(!player.isPlayerDead()) {
            labelMoneyPlay.setVisible(false);
            labelScorePlay.setVisible(true);
            imageCoinPlay.setVisible(false);
        }

        if(player.isPlayerDead()) buttonPausePlay.setVisible(false);

        if (DEBUG) {
            b2dr.render(world, b2dCam.combined);
        }

        Gdx.gl.glViewport(0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        MyGdxGame.fadeIn.render(sb);

    }

    void equipmentBar(){

        /*if(brick == null && isTutorial && displayBrickTip){
            sb.begin();
            String label = "TAP IN THE SKY!";
            font2Credit.drawMultiLine(sb,
                    label ,
                    Gdx.graphics.getWidth()/2.5f,
                    Gdx.graphics.getHeight()/1.35f,
                    font2Credit.getBounds(label).width,
                    BitmapFont.HAlignment.LEFT);
            sb.end();
        }*/

        if(!MyGdxGame.isTutorial|tuto_step0){
            shapeRenderer.setColor(new Color(11f/255f,8f/255f,8f/255f,1f));
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.rect(0,0,Gdx.graphics.getWidth(), MyGdxGame.V_WIDTH/9.35f);
            shapeRenderer.end();


            Animation coin = new Animation(new Sprite(MyGdxGame.atlas.findRegion("coin")).split(64, 64)[0], 1 / 5f);
            coin.update(MyGdxGame.STEP);

            Animation brick = new Animation(new Sprite(MyGdxGame.atlas.findRegion("brick")).split(64, 64)[0], 1 / 5f);
            if(Save.gd.isBrick2Equiped()){
                brick = new Animation(new Sprite(MyGdxGame.atlas.findRegion("brick2")).split(64, 64)[0], 1 / 5f);
            }
            brick.update(MyGdxGame.STEP);

            Animation power = null;
            if(Save.gd.isFireBallEquiped()){
                power = new Animation(new Sprite(MyGdxGame.atlas.findRegion("ui_fireball")).split(64, 64)[0], 1 / 5f);
            }
            if(Save.gd.isFireBall2Equiped()){
                power = new Animation(new Sprite(MyGdxGame.atlas.findRegion("ui_fireball2")).split(64, 64)[0], 1 / 5f);
            }
            if(power != null)
                power.update(MyGdxGame.STEP);

            sb.begin();
            float x = Gdx.graphics.getHeight()/60f;
            float y = Gdx.graphics.getWidth()/100f;
            float w = MyGdxGame.V_WIDTH/13f;
            float h = MyGdxGame.V_WIDTH/13f;
            font2.getData().scaleX = 1.5f;
            font2.getData().scaleY = 1.5f;

            sb.draw(coin.getFrame(), x , y, w, h);
            x += w*1.1f;

            String value = "x"+String.valueOf(Save.gd.getMoney());

            glyphLayoutCredit.setText(font2,value);
            font2.draw(sb, glyphLayoutCredit, x , h*1.0f);

//            font2Credit.drawMultiLine(sb,
//                    value ,
//                    x ,
//                    h*1.0f,
//                    font2Credit.getBounds(value).width,
//                    BitmapFont.HAlignment.CENTER);

            x += glyphLayoutCredit.width*1.1f;


            if(Save.gd.isFireBallEquiped()|Save.gd.isFireBall2Equiped() && power.getFrames().length>0){
                sb.draw(power.getFrame(), x, y, w, h);
                x += w*1.1f;


                value = String.valueOf(player.getFireBallCount());
                if(Save.gd.isFireBallEquiped()){
                    value = String.valueOf(Math.round((float)player.getFireBallCount()/3f));
                }
                if(player.getFireBallCount()< 1){
                    value = "0";
                }

                if(value.equals("0"))
                    setButtonColor(buttonFire,"buttonActionDisabledUp", "buttonActionDisabledDown");
                else
                    setButtonColor(buttonFire,"buttonActionUp","buttonActionDown");

                value = "x"+value;

                glyphLayoutCredit.setText(font2,value);
                font2.draw(sb, glyphLayoutCredit, x , h*1.0f);
                x += glyphLayoutCredit.width*1.2f;

//                font2Credit.drawMultiLine(sb,
//                        value ,
//                        x,
//                        h*1.0f,
//                        font2Credit.getBounds(value).width,
//                        BitmapFont.HAlignment.LEFT);
//
//                x += font2Credit.getBounds(value).width*1.2f;


            }
            if(MyGdxGame.brick != null && !MyGdxGame.brick.isFalling()){
                sb.draw(brick.getFrame(), x, y, w, h);
                x += w*1.1f;

                float damage;
                damage = (MyGdxGame.brick.getLife()/(float)MyGdxGame.brick.getMaxLife())*100.0f;

                value = ""+String.valueOf((int)damage)+"%";
                glyphLayoutCredit.setText(font2,value);
                font2.draw(sb, glyphLayoutCredit, x , h*1.0f);
                x += glyphLayoutCredit.width*1.2f;
//                font2Credit.drawMultiLine(sb,
//                        value ,
//                        x ,
//                        h*1.0f,
//                        font2Credit.getBounds(value).width,
//                        BitmapFont.HAlignment.CENTER);
//
//                x += font2Credit.getBounds(value).width*1.1f;

            }

            sb.end();

        }
    }

    private Enemy createEnemy(float x, float y, boolean fromLeft) {
        addNewEnemy = true;
        Gdx.app.debug(LOG_TAG,"create enemy...");
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
        fdef.filter.maskBits = /*B2DVars.BIT_PRINCESS | B2DVars.BIT_BLOCK |*/ B2DVars.BIT_GROUND;
        body.setGravityScale(0);
        body.createFixture(fdef).setUserData("enemy");

        shape.dispose();
        float speed = 1.0f;
        if(step4) speed = 1.2f;
        if(step5) speed = 1.4f;

        return new Enemy(body, fromLeft, isMalicious, speed, enemyIsNextLevel);
    }

    private void createHand(float x, float y) {
        Gdx.app.debug(LOG_TAG,"create pointer...");
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        float w = 86/PPM;
        float h = 108/PPM;
        bdef.position.set(x,y);
        bdef.type = BodyType.DynamicBody;
        Body body = world.createBody(bdef);
        shape.setAsBox(w, h, new Vector2(0 ,0), 0);
        fdef.shape = shape;
        fdef.density = 0;
        fdef.friction = 0;
        fdef.isSensor = true;
        fdef.restitution = 0;
//        fdef.filter.categoryBits = B2DVars.BIT_PRINCESS;
//        fdef.filter.maskBits = B2DVars.BIT_ENEMY;
        body.setGravityScale(0);
        body.createFixture(fdef).setUserData("pointer");
        shape.dispose();
        body.setLinearVelocity(0,0);
        pointer =  new Hand(body);
    }

    private FireBall createFireBall(float x, float y, float velocity) {
        addPowerUpOrBrick = true;
        Gdx.app.debug(LOG_TAG,"create fireball...");
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        float w = 64/PPM;
        float h = 64/PPM;
        bdef.position.set(x,y);
        bdef.type = BodyType.DynamicBody;
        Body body = world.createBody(bdef);
        addPowerUpOrBrick = false;
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

        return new FireBall(body);
    }

    private Lightning createLightning(float x, float y, float velocity) {
        addPowerUpOrBrick = true;
        Gdx.app.debug(LOG_TAG,"create lightning...");
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        float w = 60/PPM;
        float h = 64*8/PPM;
        bdef.position.set(x,y);
        bdef.type = BodyType.DynamicBody;
        Body body = world.createBody(bdef);
        addPowerUpOrBrick = false;
        shape.setAsBox(w, h, new Vector2(0 ,0), 0);
        fdef.shape = shape;
        fdef.density = 0;
        fdef.friction = 0;
        fdef.isSensor = true;
        fdef.restitution = 0;
        fdef.filter.categoryBits = B2DVars.BIT_PRINCESS;
        fdef.filter.maskBits = B2DVars.BIT_ENEMY;
        body.setGravityScale(0);
        body.createFixture(fdef).setUserData("lightning");
        shape.dispose();

        body.setLinearVelocity(velocity,0);
        return new Lightning(body);
    }

    private Brick createBrick(float x, float y) {
        addPowerUpOrBrick = true;
        Gdx.app.debug(LOG_TAG,"create brick...");
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        float w = 32/PPM;
        float h = 32/PPM;
        bdef.position.set(x,y);

        bdef.type = BodyType.DynamicBody;
        Body body = world.createBody(bdef);
        addPowerUpOrBrick = false;
        shape.setAsBox(w, h, new Vector2(0 ,0), 0);
        fdef.shape = shape;
        fdef.density = 0;
        fdef.friction = 1;
        fdef.isSensor = false;
        fdef.restitution = 0;
        fdef.filter.categoryBits = B2DVars.BIT_BLOCK ;
        fdef.filter.maskBits =  B2DVars.BIT_GROUND | B2DVars.BIT_ENEMY /*| B2DVars.BIT_PLAYER*/;
        body.createFixture(fdef).setUserData("brick");
        // foot
        fdef = new FixtureDef();
        shape.setAsBox(w, 0.001f, new Vector2(0 , h), 0);
        fdef.shape = shape;
        fdef.filter.categoryBits = B2DVars.BIT_BLOCK;
        fdef.filter.maskBits = B2DVars.BIT_PLAYER;
        fdef.isSensor = true;
        fdef.density = 0;
        fdef.friction = 10;
        fdef.restitution = 0;
        body.createFixture(fdef).setUserData("bricktop");

        body.setGravityScale(1.2f);

        shape.dispose();

        body.setLinearVelocity(0,0);

        int life = 400;
        if(Save.gd.isBrick2Equiped()){
            life = 400*5;
        }

        return new Brick(body, life);
    }

    private void createPrincess(float x, float y) {
        if(MyGdxGame.isTutorial){
            MyGdxGame.createdPrincessInTuTo = true;
        }

        Gdx.app.debug(LOG_TAG,"create princess...");
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        float w = 64/PPM;
        float h = 64/PPM;

        if(lastPrincessPosition.x != 0 && !MyGdxGame.isTutorial){
            bdef.position.set(lastPrincessPosition.x, lastPrincessPosition.y);
        }else{
            bdef.position.set(x, y);
        }

        //bdef.position.set(x,yMenu);

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
        shape.dispose();

        body.setLinearVelocity(-1,0);

        princess = new Princess(body);
    }

    private void createPlayer() {
        Gdx.app.debug(LOG_TAG,"create player...");
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        MapLayer layer = MyGdxGame.tileMap.getLayers().get("player");

        for (MapObject mo : layer.getObjects()) {

            Gdx.app.debug(LOG_TAG,"PLAYER COUNT: "+layer.getObjects().getCount());

            float x , y, w, h;
            w = ((RectangleMapObject) mo).getRectangle().getWidth()/PPM;
            h = ((RectangleMapObject) mo).getRectangle().getHeight()/PPM;

            x = ((RectangleMapObject) mo).getRectangle().x / PPM + w/2;
            y = ((RectangleMapObject) mo).getRectangle().y / PPM + h/2;

            if(MyGdxGame.lastPlayerPosition.x != 0 && !MyGdxGame.isTutorial){
                bdef.position.set(lastPlayerPosition.x, lastPlayerPosition.y);
            }else{
                bdef.position.set(x, y);
            }


            if(MyGdxGame.isShortcutDiscovered()) {
                bdef.position.set(262.37415f, y);
                MyGdxGame.setShortcutDiscovered(false);
            }

            Gdx.app.debug(LOG_TAG,"PLAYER START POSITION: " + x);
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
            fdef.filter.maskBits =  B2DVars.BIT_GROUND|BIT_BLOCK;
            body.createFixture(fdef).setUserData("player");
            // foot
            fdef = new FixtureDef();
            shape.setAsBox(w*2f, h/5, new Vector2(0 , -h*0.99f ), 0);
            fdef.shape = shape;
            fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
            fdef.filter.maskBits = B2DVars.BIT_GROUND|BIT_BLOCK;
            fdef.isSensor = true;
            fdef.density = 0;
            fdef.friction = 0;
            fdef.restitution = 0;
            body.createFixture(fdef).setUserData("foot");

            shape.dispose();

            body.setGravityScale(1);

            player = new Player(body, player_selector);

            if(player == null)
                player = new Player(body, player_selector);
            else {
                player.reset();
                player.setBody(body);
                player.setSelector(player_selector);
            }

        }
    }

    private void createTiles() {
        Gdx.app.debug(LOG_TAG,"create tiles...");
        if(tileMap != null){
            tileMapWidth = MyGdxGame.tileMap.getProperties().get("width", Integer.class);
            tileMapHeight = MyGdxGame.tileMap.getProperties().get("height", Integer.class);
            tileSize = MyGdxGame.tileMap.getProperties()
                    .get("tilewidth", Integer.class);
            if(tmr == null)
                tmr = new OrthogonalTiledMapRenderer(tileMap);
            else
                tmr.setMap(tileMap);
            TiledMapTileLayer layer;
            layer = (TiledMapTileLayer) MyGdxGame.tileMap.getLayers().get("ground");
            createLayer(layer, B2DVars.BIT_GROUND);
        }

    }

    private void createLayer(TiledMapTileLayer layer, short bits) {
        Gdx.app.debug(LOG_TAG,"create layer...");
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
                v[0] = new Vector2(-10*tileSize / 2 / PPM, tileSize / 2 / PPM);
                v[1] = new Vector2(tileSize / 2 / PPM, tileSize / 2 / PPM);
                cs.createChain(v);
                fdef.friction = 1;
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

    private void resize(int width, int height) {
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
        viewportPlay = new Rectangle(crop.x, 0, w, h);


        offsetYPlay = crop.y;



        float offsetY = crop.y;
        float offsetX = crop.x;

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Gdx.gl.glViewport(0,0, (int) viewportPlay.width+ (int)offsetX, Gdx.graphics.getHeight());
        stage0Play.act();
        sb.begin();

        if(!lightningRunning){
            //hide sky
            stage0Play.draw();
        }

        sb.end();

        Gdx.gl.glViewport((int) viewportPlay.x, (int) viewportPlay.y, (int) viewportPlay.width - (int)offsetX, (int) viewportPlay.height - (int)offsetY);

        stage1Play.getViewport().update((int) (width - offsetX), (int) (height - offsetY), true);
        stageUiControl.getViewport().update((int) (width - offsetX), (int) (height - offsetY), true);

    }

    public void dispose() {
        isTutorial = false;
        tuto_step0 = false;
        tuto_step1 = false;
        tuto_step2 = false;
        tuto_step3 = false;
        tuto_step4 = false;
        tuto_step5 = false;
        tuto_step6 = false;
        tuto_step7 = false;
        tuto_step8 = false;

        tileMapWidth = 0;
        tileMapHeight = 0;
        tileSize = 0;
        enemyIsNextLevel = false;
        player_selector = 0;
        cpt_alarm = 0;
        playerStartMoving = false;
        cpt_translate_animationPlay = 0;
        enemiesKilled = 0;
        isSlideInEnd = false;
        gameOverPlay = false;
        submitPlay = false;
        firePlay = false;
        left = false;
        right = false;
        buttonLeft_isTouched = false;
        buttonRight_isTouched = false;
        falling = false;
        addNewEnemy = false;
        addPowerUpOrBrick = false;
        isMalicious = false;
        toggle = true;
        isTouchingEnemy = false;
        lastClickPos = 0;
        offsetYPlay = 0.0f;
        beamWidth = 0.0f;
        reloadKamehameha = 0.0f;
        reloadLightning = 0.0f;
        lightningRunning = false;
        difficulty = 0.0;
        blockKamehameha = false;
        blockLightning = false;
        kamehaReachLimit = false;
        megaKameha = false;
        lightningReachLimit = false;
        maxEnemiesOnScreen = 10;
        stopEnemies = false;
        isStepping = false;
        step0 = false;
        step1 = false;
        step2 = false;
        step3 = false;
        step4 = false;
        step5 = false;
        jump = false;
        createdPrincessInTuTo = false;
        enableBrick = true;
        cpt_sound_hit = 0;
        cpt_scream = 0;
        cpt_scream_fade = 0;
        baseX = 0f;
        baseY = 0f;
        displayBrickTip = false;
        hidePointer = false;
        hideScream = false;
        cpt_tuto = 0;
        powerUpBar_MaxHeight = 0.0f;
        princess = null;
        for(Enemy enemy:enemies){
            enemy.destroy();
        }
        enemies.clear();

        if(brick != null){
            brick.destroy();
            brick = null;
        }

        for(FireBall fireBall:fireBalls){
            fireBall.destroy();
        }
        fireBalls.clear();

        lightning = null;

        MyGdxGame.setPause(false);

        executor.shutdown();

        buttonPlayPlay.setPosition(-400, Gdx.graphics.getHeight() / 4.5f);
        buttonSoundPlay.setPosition((Gdx.graphics.getWidth() / 1f) + 400, Gdx.graphics.getHeight() / 4.5f);
        buttonPausePlay.setPosition((Gdx.graphics.getWidth() - buttonPausePlay.getWidth() - 20),
                (Gdx.graphics.getHeight() - buttonPausePlay.getHeight() - Gdx.graphics.getHeight()/10f));
        buttonPausePlay.setTouchable(originalTouchableTouchButton);
        //buttonPausePlay = new Button(pauseButtonStyle);
        //enemies.clear();
        //brick.destroy();

//        lastPlayerPosition.set(0,0);
//        lastBrickPosition.set(0,0);
//        lastPrincessPosition.set(0,0);
        lastScoreInTutorial = 0;

        font2.getData().scaleX = 1f;
        font2.getData().scaleY = 1f;
        //spriteBatch.dispose();
        //spriteBatchLightning.dispose();
        //if (MyGdxGame.res.getSound("alarm").isPlaying()) MyGdxGame.res.getSound("alarm").pause();
        if(MyGdxGame.isSoundEnable() == 2) {
            if(MyGdxGame.res.getMusic("shop").isPlaying()){
                MyGdxGame.res.getMusic("shop").pause();
                MyGdxGame.res.getMusic("shop").stop();
            }
            if(lastPlayerPosition.x == 0){
                if(MyGdxGame.res.getMusic("level1").isPlaying()){
                    MyGdxGame.res.getMusic("level1").pause();
                    MyGdxGame.res.getMusic("level1").stop();
                }
            }else{
                MyGdxGame.res.getMusic("level1").pause();
            }
        }
    }

}
