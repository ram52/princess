package com.mygdx.core.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.utils.Timer;
import com.mygdx.core.MyGdxGame;
import com.mygdx.core.handlers.Animation;
import com.mygdx.core.handlers.AnimeActor;
import com.mygdx.core.handlers.GameStateManager;
import com.mygdx.core.handlers.Save;
import com.mygdx.core.handlers.ShopDialog;
import com.mygdx.core.handlers.SimpleDirectionGestureDetector;

import java.io.IOException;
import java.net.URL;
/**
 * Copyright Axel MONTOUT
 * started in 2016
 *
 *
 *          PRINCESS!
 *
 *
 * Protect your loved one.
 * */
public class Shop extends GameState {
    private static String LOG_TAG = Shop.class.getSimpleName();
    private boolean click_on_play;
    private Stage stage1, stage2;
    private AlphaAction fade1, fade2, fade3;
    private Rectangle viewport;
    private AlphaAction fade;
    private Button buttonPlay, buttonSecret1, buttonSecret2,imageCoin;
    private Button buttonFireBall, buttonExcalibur, buttonKamehameha, buttonBoot;
    private Button buttonFireBall2, buttonMegaJump, buttonLightning, buttonBrick2;
    private Button buttonAds, buttonCoin1, buttonCoin2,buttonCoin3;
    private int cpt_secret1 = 0;
    private int cpt_translate_animation1 = 0;
    private Label labelMoney;
    public Skin skin;
    private Vector2 crop;
    private Animation animTitle, animationCoin;
    private Animation animationEnemy, animationEnemyMock;
    private float time = 0.0f;
    private Stage stage0;
    private Image intro;
    private float offsetY = 0;

    public Shop(GameStateManager gsm) {

        super(gsm);

        if(MyGdxGame.isSoundEnable() == 2){
            if(!MyGdxGame.res.getMusic("shop").isPlaying()){
                MyGdxGame.res.getMusic("shop").setVolume(1.0f);
                MyGdxGame.res.getMusic("shop").play();
            }
        }

        MyGdxGame.lastPlayerPosition = new Vector2(0,0);
        intro = new Image(MyGdxGame.atlas.findRegion("backgroundSky"));
        intro.setFillParent(true);
        stage0 = new Stage();
        stage0.addActor(intro);
        viewport = new Rectangle();
        skin = new Skin();
        skin.addRegions(MyGdxGame.atlas);
        Save.load();

        Sprite tex = new Sprite(MyGdxGame.atlas.findRegion("enemy"));
        TextureRegion[] sprites = tex.split(64, 64)[0];
        for(int i=0;i<sprites.length;i++)
            sprites[i].flip(true,false);
        animationEnemy = new Animation(sprites, 1 / 5f);

        tex = new Sprite(MyGdxGame.atlas.findRegion("enemyMock"));
        sprites = tex.split(64, 64)[0];
        for(int i=0;i<sprites.length;i++)
            sprites[i].flip(true,false);
        animationEnemyMock = new Animation(sprites, 1 / 5f);

        animTitle = new Animation(new Sprite(MyGdxGame.atlas.findRegion("shop")).split(57,23)[0], 1 / 5f);

//        if (!Save.gd.getAdsRemoverPurchased()) {
//            String network = game.actionResolver.getNetworkClass();
//            if(network == null) network = "ABSENT";
//            System.out.print("NETWORK: "+network);
//            if(network.equals("4G")|network.equals("3G")|network.equals("WIFI"))
//                game.actionResolver.showOrLoadInterstital();
//            Gdx.app.debug(LOG_TAG,network);
//        }

        MyGdxGame.background_cloud.setVector(+10, 0);
        //MyGdxGame.background_skyDay.setVector(0, 0);
        MyGdxGame.background_wood1.setVector(-3, 0);


        SimpleDirectionGestureDetector gd = new SimpleDirectionGestureDetector(
                new SimpleDirectionGestureDetector.DirectionListener() {

                    @Override
                    public void onUp() {
                        Gdx.app.debug(LOG_TAG,"up");
                    }

                    @Override
                    public void onRight() {
                        Gdx.app.debug(LOG_TAG,"right");
                    }

                    @Override
                    public void onLeft() {
                        Gdx.app.debug(LOG_TAG,"left");
                    }

                    @Override
                    public void onDown() {

                        if((Save.gd.isSoundEnable() == 1 | Save.gd.isSoundEnable() == 2) && time > 200 && time < 700 && !MyGdxGame.res.getMusic("laugh").isPlaying()){
                            MyGdxGame.res.getMusic("laugh").play();
                            cpt_secret1++;
                            Gdx.app.debug(LOG_TAG,"CLICK SECRET 1: " + cpt_secret1);
                            Input.TextInputListener textListener = new Input.TextInputListener()
                            {
                                @Override
                                public void input(String input)
                                {
                                    Gdx.app.debug(LOG_TAG,input);
                                    if(getSecretCode().equals(input)){
                                        //todo unlock every items
                                        Save.load();
                                        Save.save();
                                        Save.gd.setAdsRemoverPurchased(true);
                                        Save.gd.setMegaJumpPurchased(true);
                                        Save.gd.setLightningPurchased(true);
                                        Save.gd.setBootPurchased(true);
                                        Save.gd.setKamehamehaPurchased(true);
                                        Save.gd.setBrick2Purchased(true);
                                        Save.gd.setBrick2Equiped(true);
                                        Save.gd.setFireBall2Purchased(true);
                                        Save.gd.setFireBallPurchased(true);
                                        Save.gd.setExcaliburEquiped(true);
                                        Save.gd.setExcaliburPurchased(true);
                                        Save.gd.setMegaJumpEquiped(true);
                                        Save.gd.setBootEquiped(true);
                                        Save.gd.setFireBall2Equiped(true);
                                        Save.gd.setFireBallEquiped(false);
                                        Save.save();
                                        Save.load();
                                        //showSecretDialog();
                                    }else if(input.equals("coin")){
                                        Save.gd.setMoney(Save.gd.getMoney()+1);
                                        Save.save();
                                        labelMoney.setText(Integer.toString(Save.gd.getMoney()));
                                        Save.load();
                                    }else if(input.equals("lot of coins")){
//                                        Save.gd.setMoney(Save.gd.getMoney()+10);
//                                        Save.save();
//                                        labelMoney.setText(Integer.toString(Save.gd.getMoney()));
//                                        Save.load();
                                    }
                                }

                                @Override
                                public void canceled()
                                {
                                    Gdx.app.debug(LOG_TAG,"Aborted");
                                }
                            };

                            Gdx.input.getTextInput(textListener, "Secret found!", "", "");
                        }
                        Gdx.app.debug(LOG_TAG,"down");
                    }
                });

        cam.setToOrtho(false, MyGdxGame.V_WIDTH, MyGdxGame.V_HEIGHT);
        click_on_play = false;
        stage1 = new Stage();
        stage2 = new Stage();
        Skin skinButtonPlay = new Skin();
        skinButtonPlay.addRegions(MyGdxGame.atlas);
        ButtonStyle buttonStylePlay = new ButtonStyle();
        buttonStylePlay.up = skinButtonPlay.getDrawable("buttonExit2Up");
        buttonStylePlay.down = skinButtonPlay.getDrawable("buttonExit2Down");
        buttonPlay = new Button(buttonStylePlay);
        buttonPlay.setWidth(Gdx.graphics.getWidth() / 5f);
        buttonPlay.setHeight(Gdx.graphics.getHeight() / 7.8f);
        buttonPlay.setPosition(-400, Gdx.graphics.getHeight()/100f);

        stage1.addActor(buttonPlay);

        Skin skinButtonSecret = new Skin();
        skinButtonSecret.addRegions(MyGdxGame.atlas);
        ButtonStyle buttonStyleSecret = new ButtonStyle();
        buttonStyleSecret.up = skinButtonSecret.getDrawable("black");
        buttonStyleSecret.down = skinButtonSecret.getDrawable("black");
        buttonSecret1 = new Button(buttonStyleSecret);
        buttonSecret1.setWidth(Gdx.graphics.getWidth() / 8f);
        buttonSecret1.setHeight(Gdx.graphics.getWidth() / 8f);
        buttonSecret1.setPosition((Gdx.graphics.getWidth() - buttonSecret1.getWidth())/ 2.05f, (Gdx.graphics.getHeight() - buttonSecret1.getWidth())/4.3f );
        //stage1.addActor(buttonSecret1);

        buttonSecret2 = new Button(buttonStyleSecret);
        buttonSecret2.setWidth(Gdx.graphics.getWidth() / 8f);
        buttonSecret2.setHeight(Gdx.graphics.getWidth() / 8f);
        buttonSecret2.setPosition((Gdx.graphics.getWidth() - buttonSecret1.getWidth())/ 1.12f, (Gdx.graphics.getHeight() - buttonSecret1.getWidth())/21f );
        //stage1.addActor(buttonSecret2);

        ButtonStyle style = new ButtonStyle();
        style = new Button.ButtonStyle();
        style.up = skin.getDrawable("coin");
        style.down = skin.getDrawable("coin");
        imageCoin = new Button(style);
        imageCoin.setWidth(Gdx.graphics.getWidth() / 10f);
        imageCoin.setHeight(Gdx.graphics.getWidth() / 10f);
        imageCoin.setPosition((Gdx.graphics.getWidth()- 5*imageCoin.getWidth()) , imageCoin.getHeight()/2);
        //stage1.addActor(imageCoin);

        /**FIRST ITEM ROW*/
        style = new Button.ButtonStyle();
        style.up = skin.getDrawable("buttonFireBallUp");
        style.down = skin.getDrawable("buttonFireBallDown");
        buttonFireBall = new Button(style);
        buttonFireBall.setWidth(Gdx.graphics.getWidth() / 6f);
        buttonFireBall.setHeight(Gdx.graphics.getWidth() / 6f);
        buttonFireBall.setPosition(Gdx.graphics.getWidth()/8.5f, Gdx.graphics.getHeight()/1.30f);
        stage1.addActor(buttonFireBall);

        style = new Button.ButtonStyle();
        style.up = skin.getDrawable("buttonExcaliburUp");
        style.down = skin.getDrawable("buttonExcaliburDown");
        buttonExcalibur = new Button(style);
        buttonExcalibur.setWidth(buttonFireBall.getWidth());
        buttonExcalibur.setHeight(buttonFireBall.getWidth());
        buttonExcalibur.setPosition(buttonFireBall.getX() + buttonFireBall.getWidth()*1.2f , buttonFireBall.getY());
        stage1.addActor(buttonExcalibur);

        style = new Button.ButtonStyle();
        style.up = skin.getDrawable("buttonKamehamehaUp");
        style.down = skin.getDrawable("buttonKamehamehaDown");
        buttonKamehameha = new Button(style);
        buttonKamehameha.setWidth(buttonFireBall.getWidth());
        buttonKamehameha.setHeight(buttonFireBall.getWidth());
        buttonKamehameha.setPosition(buttonExcalibur.getX() + buttonExcalibur.getWidth()*1.2f , buttonExcalibur.getY());
        stage1.addActor(buttonKamehameha);

        style = new Button.ButtonStyle();
        style.up = skin.getDrawable("buttonBootUp");
        style.down = skin.getDrawable("buttonBootDown");
        buttonBoot = new Button(style);
        buttonBoot.setWidth(buttonFireBall.getWidth());
        buttonBoot.setHeight(buttonFireBall.getWidth());
        buttonBoot.setPosition(buttonKamehameha.getX() + buttonBoot.getWidth()*1.2f , buttonKamehameha.getY());
        stage1.addActor(buttonBoot);

        /**SECOND ITEM ROW*/
        style = new Button.ButtonStyle();
        style.up = skin.getDrawable("buttonFireBall2Up");
        style.down = skin.getDrawable("buttonFireBall2Down");
        buttonFireBall2 = new Button(style);
        buttonFireBall2.setWidth(buttonFireBall.getWidth());
        buttonFireBall2.setHeight(buttonFireBall.getWidth());
        buttonFireBall2.setPosition(buttonFireBall.getX(), buttonFireBall.getY() - buttonFireBall.getHeight()*1.2f);
        stage1.addActor(buttonFireBall2);

        style = new Button.ButtonStyle();
        style.up = skin.getDrawable("buttonMegaJumpUp");
        style.down = skin.getDrawable("buttonMegaJumpDown");
        buttonMegaJump = new Button(style);
        buttonMegaJump.setWidth(buttonFireBall.getWidth());
        buttonMegaJump.setHeight(buttonFireBall.getWidth());
        buttonMegaJump.setPosition(buttonFireBall2.getX() + buttonFireBall2.getWidth()*1.2f, buttonFireBall2.getY());
        stage1.addActor(buttonMegaJump);

        style = new Button.ButtonStyle();
        style.up = skin.getDrawable("buttonLightningUp");
        style.down = skin.getDrawable("buttonLightningDown");
        buttonLightning = new Button(style);
        buttonLightning.setWidth(buttonFireBall.getWidth());
        buttonLightning.setHeight(buttonFireBall.getWidth());
        buttonLightning.setPosition(buttonMegaJump.getX() + buttonFireBall2.getWidth()*1.2f, buttonFireBall2.getY());
        stage1.addActor(buttonLightning);

        style = new Button.ButtonStyle();
        style.up = skin.getDrawable("buttonBrick2Up");
        style.down = skin.getDrawable("buttonBrick2Down");
        buttonBrick2 = new Button(style);
        buttonBrick2.setWidth(buttonFireBall.getWidth());
        buttonBrick2.setHeight(buttonFireBall.getWidth());
        buttonBrick2.setPosition(buttonLightning.getX() + buttonFireBall2.getWidth()*1.2f, buttonFireBall2.getY());
        stage1.addActor(buttonBrick2);


        /**THIRD ITEM ROW*/
        style = new Button.ButtonStyle();
        style.up = skin.getDrawable("buttonAdsUp");
        style.down = skin.getDrawable("buttonAdsDown");
        buttonAds = new Button(style);
        buttonAds.setWidth(buttonFireBall.getWidth());
        buttonAds.setHeight(buttonFireBall.getWidth());
        buttonAds.setPosition(Gdx.graphics.getWidth()/8.5f, buttonFireBall2.getY() - buttonFireBall.getHeight()*1.2f);
        stage1.addActor(buttonAds);

        style = new Button.ButtonStyle();
        style.up = skin.getDrawable("buttonCoins1Up");
        style.down = skin.getDrawable("buttonCoins1Down");
        buttonCoin1 = new Button(style);
        buttonCoin1.setWidth(buttonAds.getWidth());
        buttonCoin1.setHeight(buttonAds.getWidth());
        buttonCoin1.setPosition(buttonAds.getX() + buttonAds.getWidth()*1.2f , buttonAds.getY());
        stage1.addActor(buttonCoin1);

        style = new Button.ButtonStyle();
        style.up = skin.getDrawable("buttonCoins2Up");
        style.down = skin.getDrawable("buttonCoins2Down");
        buttonCoin2 = new Button(style);
        buttonCoin2.setWidth(buttonAds.getWidth());
        buttonCoin2.setHeight(buttonAds.getWidth());
        buttonCoin2.setPosition(buttonCoin1.getX() + buttonAds.getWidth()*1.2f , buttonAds.getY());
        stage1.addActor(buttonCoin2);

        style = new Button.ButtonStyle();
        style.up = skin.getDrawable("buttonCoins3Up");
        style.down = skin.getDrawable("buttonCoins3Down");
        buttonCoin3 = new Button(style);
        buttonCoin3.setWidth(buttonAds.getWidth());
        buttonCoin3.setHeight(buttonAds.getWidth());
        buttonCoin3.setPosition(buttonCoin2.getX() + buttonCoin3.getWidth()*1.2f , buttonCoin2.getY());
        stage1.addActor(buttonCoin3);


        labelMoney = new Label("0", new Label.LabelStyle(new BitmapFont(Gdx.files.internal(MyGdxGame.fontScorePath), false), Color.WHITE));
        float fScale = Gdx.graphics.getWidth() / 400f;
        labelMoney.setFontScale(fScale);

        labelMoney.setWidth(Gdx.graphics.getWidth());
        labelMoney.setHeight(Gdx.graphics.getHeight()/15f);
        labelMoney.setPosition((Gdx.graphics.getWidth()-labelMoney.getWidth())/2, Gdx.graphics.getHeight() - labelMoney.getHeight()*1.1f);
        labelMoney.setAlignment(Align.center);

        stage1.addActor(labelMoney);
        labelMoney.setText(Integer.toString(Save.gd.getMoney()));

        animationCoin = new Animation(new Sprite(MyGdxGame.atlas.findRegion("coinsprite")).split(64,64)[0], 1 / 5f);

        com.badlogic.gdx.graphics.g2d.Animation a = new com.badlogic.gdx.graphics.g2d.Animation(1/10f,animationCoin.getFrames());

        AnimeActor anim = new AnimeActor(a);
        anim.setHeight(labelMoney.getHeight());
        anim.setWidth(labelMoney.getHeight());
        anim.setPosition(100,labelMoney.getY());
        stage1.addActor(anim);

        InputMultiplexer im = new InputMultiplexer();
        im.addProcessor(gd);
        im.addProcessor(stage1);
        Gdx.input.setInputProcessor(im);

        fade = new AlphaAction();
        fade.setDuration(0f);
        //scoreLabel.setText(Float.toString(offset));
        fade1 = new AlphaAction();
        fade1.setDuration(0f);
        fade2 = new AlphaAction();
        fade2.setDuration(0f);
        stage2.addAction(fade2);
        fade3 = new AlphaAction();
        fade3.setDuration(0f);
        stage1.addAction(Actions.sequence(Actions.alpha(1), Actions.fadeIn(0f)));

        buttonAds.addListener(new InputListener() {
            public boolean touchDown(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                if (Save.gd.isSoundEnable() == 1 | Save.gd.isSoundEnable() == 2) MyGdxGame.res.getSound("select").play();
                return true;
            }
            public void touchUp(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                Gdx.app.debug(LOG_TAG,"buttonAds clicked!");
                Save.load();
                if(!Save.gd.getAdsRemoverPurchased()){
//                    NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance();
//                    String cur = currencyFormatter.format(1).replaceAll("[0-9.,]","");;
                    ShopDialog dialog = new ShopDialog(Shop.this,
                            "\n\n" +
                                    "ADS REMOVER\n" +
                                    "\n" +
                                    "Removes all ads in game.\n" +
                                    "\n\n" +
                                    "Remove ads?", "buttonAdsUp"){
                        public void result(Object obj) {
                            Gdx.app.debug(LOG_TAG,"result Equip ads free?"+obj);

                            if(obj.toString().equals("true")){
//                                if ((MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2)) {
//                                    MyGdxGame.res.getSound("equiped").play();
//                                }
//                                Save.gd.setAdsRemoverPurchased(true);
//                                Save.save();
                            }
                            Save.save();
                            Save.load();
                        }
                    };
                    stage1.addActor(dialog);
                }
            }
        });

        buttonCoin1.addListener(new InputListener() {
            public boolean touchDown(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                if (Save.gd.isSoundEnable() == 1 | Save.gd.isSoundEnable() == 2) MyGdxGame.res.getSound("select").play();
                return true;
            }
            public void touchUp(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                Gdx.app.debug(LOG_TAG,"buttonCoin1 clicked!");
                Save.load();
//                    NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance();
//                    String cur = currencyFormatter.format(1).replaceAll("[0-9.,]","");
                    final int money = 100;
                    ShopDialog dialog = new ShopDialog(Shop.this,
                            "\n\n" +
                                    money+" COINS\n" +
                                    "\n" +
                                    "Purchase "+money+" coins.\n" +
                                    "\n\n" +
                                    "Buy "+money+" coins.?", "buttonCoins1Up"){
                        public void result(Object obj) {
                            Gdx.app.debug(LOG_TAG,"result"+obj);
                            if(obj.toString().equals("true")){
//                                if ((MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2)) {
//                                    MyGdxGame.res.getSound("point").play();
//                                }
//                                Save.gd.setMoney(Save.gd.getMoney()+money);
//                                Save.save();
//                                labelMoney.setText(Integer.toString(Save.gd.getMoney()));
                            }
                            Save.save();
                            Save.load();
                        }
                    };
                    stage1.addActor(dialog);
            }
        });

        buttonCoin2.addListener(new InputListener() {
            public boolean touchDown(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                if (Save.gd.isSoundEnable() == 1 | Save.gd.isSoundEnable() == 2) MyGdxGame.res.getSound("select").play();
                return true;
            }

            public void touchUp(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                Gdx.app.debug(LOG_TAG,"buttonCoin2 clicked!");
                Gdx.app.debug(LOG_TAG,"buttonCoin1 clicked!");
                Save.load();
//                NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance();
//                String cur = currencyFormatter.format(1).replaceAll("[0-9.,]","");
                final int money = 1000;
                ShopDialog dialog = new ShopDialog(Shop.this,
                        "\n\n" +
                                money+" COINS\n" +
                                "\n" +
                                "Purchase "+money+" coins.\n" +
                                "\n\n" +
                                "Buy "+money+" coins.?", "buttonCoins2Up"){
                    public void result(Object obj) {
                        Gdx.app.debug(LOG_TAG,"result"+obj);
                        
                        if(obj.toString().equals("true")){
//                            if ((MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2)) {
//                                MyGdxGame.res.getSound("point").play();
//                            }
//                            Save.gd.setMoney(Save.gd.getMoney()+money);
//                            Save.save();
//                            labelMoney.setText(Integer.toString(Save.gd.getMoney()));
                        }
                        Save.save();
                        Save.load();
                    }
                };
                stage1.addActor(dialog);
            }
        });

        buttonCoin3.addListener(new InputListener() {
            public boolean touchDown(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                if (Save.gd.isSoundEnable() == 1 | Save.gd.isSoundEnable() == 2) MyGdxGame.res.getSound("select").play();
                return true;
            }

            public void touchUp(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                Gdx.app.debug(LOG_TAG,"buttonCoin3 clicked!");
                Save.load();
//                NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance();
//                String cur = currencyFormatter.format(1).replaceAll("[0-9.,]","");;
                int value = 40;
                ShopDialog dialog = new ShopDialog(Shop.this,
                        "\n\n" +
                                value+" FREE COINS\n" +
                                "\n" +
                                "Watch a video for "+value+" coins FREE.\n" +
                                "\n\n" +
                                "Watch video ?", "buttonCoins3Up"){
                    public void result(Object obj) {
                        Gdx.app.debug(LOG_TAG,"result"+obj);

                        if(obj.toString().equals("true")){
//                            if ((MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2)) {
//                                MyGdxGame.res.getSound("point").play();
//                            }
//                            Save.gd.setMoney(Save.gd.getMoney()+20);
//                            Save.save();
//                            labelMoney.setText(Integer.toString(Save.gd.getMoney()));
                        }
                        Save.save();
                        Save.load();
                    }
                };
                stage1.addActor(dialog);
            }
        });


        buttonFireBall.addListener(new InputListener() {
            public boolean touchDown(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                if (Save.gd.isSoundEnable() == 1 | Save.gd.isSoundEnable() == 2) MyGdxGame.res.getSound("select").play();
                return true;
            }

            public void touchUp(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                Gdx.app.debug(LOG_TAG,"buttonFireBall clicked!");
                Save.load();
                final int cost = 1000;
                if(!Save.gd.isFireBallPurchased()){
                    ShopDialog dialog = new ShopDialog(Shop.this,
                            "\n\n" +
                                    "FIRE BALL\n" +
                                    "\n" +
                                    "Reloads every 2 enemies killed.\n" +
                                    "COST: "+cost+" coins.\n" +
                                    "\n\n" +
                                    "Buy Fire ball?", "buttonFireBallUp"){
                        public void result(Object obj) {
                            Gdx.app.debug(LOG_TAG,"result Buy Fire ball?"+obj);

                            if(obj.toString().equals("true") && Save.gd.getMoney() >= cost){
                                if ((MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2)) {
                                    MyGdxGame.res.getSound("fireball_small").play();
                                }
                                Save.gd.setFireBallPurchased(true);
                                Save.gd.setFireBallEquiped(true);
                                //Save.gd.setExcaliburEquiped(false);
                                Save.gd.setKamehamehaEquiped(false);
                                Save.gd.setLightningEquiped(false);
                                Save.gd.setFireBall2Equiped(false);
                                Save.gd.setMoney(Save.gd.getMoney() - cost);
                                Save.save();
                                labelMoney.setText(Integer.toString(Save.gd.getMoney()));
                            }else{
                                if(obj.toString().equals("true")){
                                    if (MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getSound("error").play();
                                    labelMoney.setColor(Color.RED);

//                                    new java.util.Timer().schedule(
//                                            new java.util.TimerTask() {
//                                                @Override
//                                                public void run() {
//                                                    // your code here
//                                                    labelMoney.setColor(Color.WHITE);
//                                                }
//                                            }, 500);

                                    Timer.schedule(new Timer.Task() {
                                        @Override
                                        public void run() {
                                            labelMoney.setColor(Color.WHITE);
                                        }
                                    }, 0.5f);
                                }

                            }
                        }
                    };
                    stage1.addActor(dialog);
                }else{


                    ShopDialog dialog = new ShopDialog(Shop.this,
                            "\n\n" +
                                    "FIRE BALL\n" +
                                    "\n" +
                                    "Reloads by killing enemies.\n" +
                                    "\n\n" +
                                    "Equip Fire ball?", "buttonFireBallUp"){
                        public void result(Object obj) {
                            Gdx.app.debug(LOG_TAG,"result Equip Fire ball?"+obj);

                            if(obj.toString().equals("true")){
                                if ((MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2)) {
                                    MyGdxGame.res.getSound("fireball_small").play();
                                }
                                Save.gd.setFireBallEquiped(true);
                                //Save.gd.setExcaliburEquiped(false);
                                Save.gd.setKamehamehaEquiped(false);
                                Save.gd.setLightningEquiped(false);
                                Save.gd.setFireBall2Equiped(false);
                                if (Save.gd.isFireBallEquiped() && (MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2)) {
                                    MyGdxGame.res.getSound("fireball_small").play();
                                }
                            }
                            if(obj.toString().equals("false")){
                                Save.gd.setFireBallEquiped(false);
                            }
                            Save.save();
                            Save.load();
                        }
                    };
                    stage1.addActor(dialog);


                }


            }
        });


        buttonFireBall2.addListener(new InputListener() {
            public boolean touchDown(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                if (Save.gd.isSoundEnable() == 1 | Save.gd.isSoundEnable() == 2) MyGdxGame.res.getSound("select").play();
                return true;
            }

            public void touchUp(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                Gdx.app.debug(LOG_TAG,"buttonFireBall2 clicked!");
                Save.load();
                final int cost = 8000;
                if(!Save.gd.isFireBall2Purchased()){
                    ShopDialog dialog = new ShopDialog(Shop.this,
                            "\n\n" +
                                    "HADOU BALL\n" +
                                    "\n" +
                                    "Can kill multiple enemies.\n"+
                                    "Reloads by killing enemies.\n" +
                                    "COST: "+cost+" coins.\n" +
                                    "\n\n" +
                                    "Buy Hadou ball?", "buttonFireBall2Up"){
                        public void result(Object obj) {
                            Gdx.app.debug(LOG_TAG,"result Equip Fire ball2?"+obj);

                            if(obj.toString().equals("true") && Save.gd.getMoney() >= cost){
                                if ((MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2)) {
                                    MyGdxGame.res.getSound("fireball_big").play();
                                }
                                Save.gd.setFireBall2Purchased(true);
                                Save.gd.setFireBall2Equiped(true);
                                //Save.gd.setExcaliburEquiped(false);
                                Save.gd.setKamehamehaEquiped(false);
                                Save.gd.setLightningEquiped(false);
                                Save.gd.setFireBallEquiped(false);
                                Save.gd.setMoney(Save.gd.getMoney() - cost);
                                Save.save();
                                labelMoney.setText(Integer.toString(Save.gd.getMoney()));
                                //MyGdxGame.actionResolver.purchaseHadouBall();
                            }else{
                                if(obj.toString().equals("true")){
                                    if (MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getSound("error").play();
                                    labelMoney.setColor(Color.RED);

//                                    new java.util.Timer().schedule(
//                                            new java.util.TimerTask() {
//                                                @Override
//                                                public void run() {
//                                                    // your code here
//                                                    labelMoney.setColor(Color.WHITE);
//                                                }
//                                            }, 500);

                                    Timer.schedule(new Timer.Task() {
                                        @Override
                                        public void run() {
                                            labelMoney.setColor(Color.WHITE);
                                        }
                                    }, 0.5f);
                                }

                            }
                        }
                    };
                    stage1.addActor(dialog);
                }else{


                    ShopDialog dialog = new ShopDialog(Shop.this,
                            "\n\n" +
                                    "HADOU BALL\n" +
                                    "\n" +
                                    "Can kill multiple enemies.\n"+
                                    "Reloads every 4 enemies killed.\n" +
                                    "\n" +
                                    "Equip Hadou ball?", "buttonFireBall2Up"){
                        public void result(Object obj) {
                            Gdx.app.debug(LOG_TAG,"result Equip Fire ball?"+obj);

                            if(obj.toString().equals("true")){
                                if ((MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2)) {
                                    MyGdxGame.res.getSound("fireball_big").play();
                                }
                                Save.gd.setFireBall2Equiped(true);
                                //Save.gd.setExcaliburEquiped(false);
                                Save.gd.setKamehamehaEquiped(false);
                                Save.gd.setLightningEquiped(false);
                                Save.gd.setFireBallEquiped(false);
                                if (Save.gd.isFireBallEquiped() && (MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2)) {
                                    MyGdxGame.res.getSound("fireball_big").play();
                                }
                            }
                            if(obj.toString().equals("false")){
                                Save.gd.setFireBall2Equiped(false);
                            }
                            Save.save();
                            Save.load();
                        }
                    };
                    stage1.addActor(dialog);
                }
            }
        });

        buttonExcalibur.addListener(new InputListener() {
            public boolean touchDown(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                if (Save.gd.isSoundEnable() == 1 | Save.gd.isSoundEnable() == 2) MyGdxGame.res.getSound("select").play();
                return true;
            }

            ;

            public void touchUp(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                Gdx.app.debug(LOG_TAG,"buttonExcalibur clicked!");
                Save.load();
                final int cost = 9000;
                if(!Save.gd.isExcaliburPurchased()){
                ShopDialog dialog = new ShopDialog(Shop.this,
                        "\n\n" +
                                "EXCALIBUR\n" +
                                "\n" +
                                "Excalibur kill enemy in one swing.\n" +
                                "COST: "+cost+" coins.\n" +
                                "\n" +
                                "Buy Excalibur?", "buttonExcaliburUp"){
                    public void result(Object obj) {
                        Gdx.app.debug(LOG_TAG,"result Equip Excalibur?"+obj);

                        if(obj.toString().equals("true") && Save.gd.getMoney() >= cost){
                            if ((MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2)) {
                                MyGdxGame.res.getMusic("slash").play();
                            }
                            Save.gd.setExcaliburPurchased(true);
                            Save.gd.setExcaliburEquiped(true);
                            Save.gd.setFireBallEquiped(false);
                            //Save.gd.setKamehamehaEquiped(false);
                            Save.gd.setMoney(Save.gd.getMoney() - cost);
                            Save.save();
                            labelMoney.setText(Integer.toString(Save.gd.getMoney()));
                            //MyGdxGame.actionResolver.purchaseExcalibur();
                        }else{
                            if(obj.toString().equals("true")){
                                if (MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getSound("error").play();
                                labelMoney.setColor(Color.RED);

//                                new java.util.Timer().schedule(
//                                        new java.util.TimerTask() {
//                                            @Override
//                                            public void run() {
//                                                // your code here
//                                                labelMoney.setColor(Color.WHITE);
//                                            }
//                                        }, 500);
                                Timer.schedule(new Timer.Task() {
                                    @Override
                                    public void run() {
                                        labelMoney.setColor(Color.WHITE);
                                    }
                                }, 0.5f);
                            }

                        }
                    }
                };
                stage1.addActor(dialog);
                }else{
                    ShopDialog dialog = new ShopDialog(Shop.this,
                            "\n\n" +
                                    "EXCALIBUR\n" +
                                    "\n" +
                                    "Excalibur kill enemy in one swing.\n" +
                                    "\n" +
                                    "Equip Excalibur?", "buttonExcaliburUp"){
                        public void result(Object obj) {
                            Gdx.app.debug(LOG_TAG,"result Equip Fire ball?"+obj);

                            if(obj.toString().equals("true")){
                                Save.gd.setExcaliburEquiped(true);
                                //Save.gd.setFireBallEquiped(false);
                                //Save.gd.setKamehamehaEquiped(false);
                                if (Save.gd.isExcaliburEquiped() && (MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2)) {
                                    MyGdxGame.res.getMusic("slash").play();
                                }
                            }
                            if(obj.toString().equals("false")){
                                Save.gd.setExcaliburEquiped(false);
                            }
                            Save.save();
                            Save.load();
                        }
                    };
                    stage1.addActor(dialog);
                }
            }
        });

        buttonKamehameha.addListener(new InputListener() {
            public boolean touchDown(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                if (Save.gd.isSoundEnable() == 1 | Save.gd.isSoundEnable() == 2) MyGdxGame.res.getSound("select").play();
                return true;
            }

            public void touchUp(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                Gdx.app.debug(LOG_TAG,"buttonKamehameha clicked!");
                Save.load();
                final int cost = 10000;
                if(!Save.gd.isKamehamehaPurchased()){
                    ShopDialog dialog = new ShopDialog(Shop.this,
                            "\n\n" +
                                    "KAME BEAM\n" +
                                    "\n" +
                                    "Destroys everything.\n" +
                                    "COST: "+cost+" coins.\n" +
                                    "\n" +
                                    "Buy Kame beam?", "buttonKamehamehaUp"){
                        public void result(Object obj) {
                            Gdx.app.debug(LOG_TAG,"result Buy Kamehameha?"+obj);


                            if(obj.toString().equals("true") && Save.gd.getMoney() >= cost){
                                if ((MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2)) {
                                    MyGdxGame.res.getMusic("kame").play();
                                }
                                Save.gd.setKamehamehaPurchased(true);
                                Save.gd.setKamehamehaEquiped(true);
                                Save.gd.setFireBallEquiped(false);
                                Save.gd.setFireBall2Equiped(false);
                                Save.gd.setLightningEquiped(false);
                                Save.gd.setMoney(Save.gd.getMoney() - cost);
                                Save.save();
                                labelMoney.setText(Integer.toString(Save.gd.getMoney()));
                                //MyGdxGame.actionResolver.purchaseKamebeam();
                            }else{

                                if(obj.toString().equals("true")){
                                    if (MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getSound("error").play();
                                    labelMoney.setColor(Color.RED);

//                                    new java.util.Timer().schedule(
//                                            new java.util.TimerTask() {
//                                                @Override
//                                                public void run() {
//                                                    // your code here
//                                                    labelMoney.setColor(Color.WHITE);
//                                                }
//                                            }, 500);
                                    Timer.schedule(new Timer.Task() {
                                        @Override
                                        public void run() {
                                            labelMoney.setColor(Color.WHITE);
                                        }
                                    }, 0.5f);
                                }

                            }
                        }
                    };
                    stage1.addActor(dialog);
                }else{

                    ShopDialog dialog = new ShopDialog(Shop.this,
                            "\n\n" +
                                    "KAME BEAM\n" +
                                    "\n" +
                                    "Destroys everything.\n" +
                                    "\n" +
                                    "Equip Kame beam?", "buttonKamehamehaUp"){
                        public void result(Object obj) {
                            Gdx.app.debug(LOG_TAG,"result Equip Fire ball?"+obj);

                            if(obj.toString().equals("true")){
                                Save.gd.setKamehamehaEquiped(true);
                                Save.gd.setFireBallEquiped(false);
                                Save.gd.setFireBall2Equiped(false);
                                Save.gd.setLightningEquiped(false);
                                if (Save.gd.isKamehamehaEquiped() && (MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2)) {
                                    MyGdxGame.res.getSound("kame").play();
                                }
                            }
                            if(obj.toString().equals("false")){
                                Save.gd.setKamehamehaEquiped(false);
                            }
                            Save.save();
                            Save.load();
                        }
                    };
                    stage1.addActor(dialog);
                }


            }
        });


        buttonBoot.addListener(new InputListener() {
            public boolean touchDown(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                if (Save.gd.isSoundEnable() == 1 | Save.gd.isSoundEnable() == 2) MyGdxGame.res.getSound("select").play();
                return true;
            }

            public void touchUp(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                Gdx.app.debug(LOG_TAG,"buttonBoot clicked!");
                Save.load();
                final int cost = 3000;
                if(!Save.gd.isBootPurchased()){
                    ShopDialog dialog = new ShopDialog(Shop.this,
                            "\n\n" +
                                    "BOOTS\n" +
                                    "\n" +
                                    "Make you faster.\n" +
                                    "COST: "+cost+" coins.\n" +
                                    "\n" +
                                    "Buy Boots?", "buttonBootUp"){
                        public void result(Object obj) {
                            Gdx.app.debug(LOG_TAG,"result Buy boot?"+obj);


                            if(obj.toString().equals("true") && Save.gd.getMoney() >= cost){
                                if ((MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2)) {
                                    MyGdxGame.res.getMusic("newScreen").play();
                                }
                                Save.gd.setBootPurchased(true);
                                Save.gd.setBootEquiped(true);
                                Save.gd.setMoney(Save.gd.getMoney() - cost);
                                Save.save();
                                labelMoney.setText(Integer.toString(Save.gd.getMoney()));
                                //MyGdxGame.actionResolver.purchaseBoots();
                            }else{

                                if(obj.toString().equals("true")){
                                    if (MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getSound("error").play();
                                    labelMoney.setColor(Color.RED);

//                                    new java.util.Timer().schedule(
//                                            new java.util.TimerTask() {
//                                                @Override
//                                                public void run() {
//                                                    // your code here
//                                                    labelMoney.setColor(Color.WHITE);
//                                                }
//                                            }, 500);
                                    Timer.schedule(new Timer.Task() {
                                        @Override
                                        public void run() {
                                            labelMoney.setColor(Color.WHITE);
                                        }
                                    }, 0.5f);
                                }

                            }
                        }
                    };
                    stage1.addActor(dialog);
                }else{
                    ShopDialog dialog = new ShopDialog(Shop.this,
                            "\n\n" +
                                    "BOOTS\n" +
                                    "\n" +
                                    "Make you faster.\n" +
                                    "\n" +
                                    "Equip Boots?", "buttonBootUp"){
                        public void result(Object obj) {
                            Gdx.app.debug(LOG_TAG,"result Equip boots ball?"+obj);

                            if(obj.toString().equals("true")){
                                Save.gd.setBootEquiped(true);
                                if (Save.gd.isBootEquiped() && (MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2)) {
                                    MyGdxGame.res.getMusic("newScreen").play();
                                }
                            }
                            if(obj.toString().equals("false")){
                                Save.gd.setBootEquiped(false);
                            }
                            Save.save();
                            Save.load();
                        }
                    };
                    stage1.addActor(dialog);
                }
            }
        });


        buttonBrick2.addListener(new InputListener() {
            public boolean touchDown(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                if (Save.gd.isSoundEnable() == 1 | Save.gd.isSoundEnable() == 2) MyGdxGame.res.getSound("select").play();
                return true;
            }
            public void touchUp(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                Gdx.app.debug(LOG_TAG,"buttonBrick2 clicked!");
                Save.load();
                final int cost = 5000;
                if(!Save.gd.isBrick2Purchased()){
                    ShopDialog dialog = new ShopDialog(Shop.this,
                            "\n\n" +
                                    "SUPER BRICK SUMMON\n" +
                                    "\n" +
                                    "Stronger than regular brick.\n" +
                                    "COST: "+cost+" coins.\n" +
                                    "\n" +
                                    "Buy Super brick summon?", "buttonBrick2Up"){
                        public void result(Object obj) {
                            Gdx.app.debug(LOG_TAG,"result Buy Brick?"+obj);


                            if(obj.toString().equals("true") && Save.gd.getMoney() >= cost){
                                if ((MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2)) {
                                    MyGdxGame.res.getMusic("newScreen").play();
                                }
                                Save.gd.setBrick2Purchased(true);
                                Save.gd.setBrick2Equiped(true);
                                Save.gd.setMoney(Save.gd.getMoney() - cost);
                                Save.save();
                                labelMoney.setText(Integer.toString(Save.gd.getMoney()));
                                //MyGdxGame.actionResolver.purchaseSuperBrick();
                            }else{

                                if(obj.toString().equals("true")){
                                    if (MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getSound("error").play();
                                    labelMoney.setColor(Color.RED);

//                                    new java.util.Timer().schedule(
//                                            new java.util.TimerTask() {
//                                                @Override
//                                                public void run() {
//                                                    // your code here
//                                                    labelMoney.setColor(Color.WHITE);
//                                                }
//                                            }, 500);
                                    Timer.schedule(new Timer.Task() {
                                        @Override
                                        public void run() {
                                            labelMoney.setColor(Color.WHITE);
                                        }
                                    }, 0.5f);
                                }

                            }
                        }
                    };
                    stage1.addActor(dialog);
                }else{
                    ShopDialog dialog = new ShopDialog(Shop.this,
                            "\n\n" +
                                    "SUPER BRICK SUMMON\n" +
                                    "\n" +
                                    "Stronger than regular brick.\n" +
                                    "\n" +
                                    "Equip Super brick summon?", "buttonBrick2Up"){
                        public void result(Object obj) {
                            Gdx.app.debug(LOG_TAG,"result Equip Fire ball?"+obj);

                            if(obj.toString().equals("true")){
                                Save.gd.setBrick2Equiped(true);
                                if (Save.gd.isBootEquiped() && (MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2)) {
                                    MyGdxGame.res.getMusic("newScreen").play();
                                }
                            }
                            if(obj.toString().equals("false")){
                                Save.gd.setBrick2Equiped(false);
                            }

                            Save.save();
                            Save.load();
                        }
                    };
                    stage1.addActor(dialog);
                }

            }
        });

        buttonLightning.addListener(new InputListener() {
            public boolean touchDown(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                if (Save.gd.isSoundEnable() == 1 | Save.gd.isSoundEnable() == 2) MyGdxGame.res.getSound("select").play();
                return true;
            }
            public void touchUp(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                Gdx.app.debug(LOG_TAG,"buttonLightning clicked!");
                Save.load();
                final int cost = 12000;
                if(!Save.gd.isLightningPurchased()){
                    ShopDialog dialog = new ShopDialog(Shop.this,
                            "\n\n" +
                                    "LIGHTNING SUMMON\n" +
                                    "\n" +
                                    "Unleash mighty attack.\n" +
                                    "COST: "+cost+" coins.\n" +
                                    "\n" +
                                    "Buy Lightning summon?", "buttonLightningUp"){
                        public void result(Object obj) {
                            Gdx.app.debug(LOG_TAG,"result Buy Brick?"+obj);


                            if(obj.toString().equals("true") && Save.gd.getMoney() >= cost){
                                if ((MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2)) {
                                    MyGdxGame.res.getSound("lightning").play();
                                }

                                Save.gd.setLightningPurchased(true);
                                Save.gd.setLightningEquiped(true);
                                Save.gd.setFireBallEquiped(false);
                                Save.gd.setFireBall2Equiped(false);
                                Save.gd.setKamehamehaEquiped(false);
                                Save.gd.setMoney(Save.gd.getMoney() - cost);
                                Save.save();
                                labelMoney.setText(Integer.toString(Save.gd.getMoney()));

                            }else{

                                if(obj.toString().equals("true")){
                                    if (MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getSound("error").play();
                                    labelMoney.setColor(Color.RED);

//                                    new java.util.Timer().schedule(
//                                            new java.util.TimerTask() {
//                                                @Override
//                                                public void run() {
//                                                    // your code here
//                                                    labelMoney.setColor(Color.WHITE);
//                                                }
//                                            }, 500);
                                    Timer.schedule(new Timer.Task() {
                                        @Override
                                        public void run() {
                                            labelMoney.setColor(Color.WHITE);
                                        }
                                    }, 0.5f);
                                }

                            }
                        }
                    };
                    stage1.addActor(dialog);
                }
                else{
                    ShopDialog dialog = new ShopDialog(Shop.this,
                            "\n\n" +
                                    "LIGHTNING SUMMON\n" +
                                    "\n" +
                                    "Unleash mighty attack.\n" +
                                    "\n" +
                                    "Equip Lightning summon?", "buttonLightningUp"){
                        public void result(Object obj) {
                            Gdx.app.debug(LOG_TAG,"result Equip buttonLightningUp"+obj);

                            if(obj.toString().equals("true")){
                                Save.gd.setLightningEquiped(true);
                                Save.gd.setFireBallEquiped(false);
                                Save.gd.setFireBall2Equiped(false);
                                Save.gd.setKamehamehaEquiped(false);
                                if (Save.gd.isBrick2Equiped() && (MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2)) {
                                    MyGdxGame.res.getSound("lightning").play();
                                }
                            }
                            if(obj.toString().equals("false")){
                                Save.gd.setLightningEquiped(false);
                            }
                            Save.save();
                            Save.load();
                        }
                    };
                    stage1.addActor(dialog);
                }
            }
        });

        buttonMegaJump.addListener(new InputListener() {
            public boolean touchDown(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                if (Save.gd.isSoundEnable() == 1 | Save.gd.isSoundEnable() == 2) MyGdxGame.res.getSound("select").play();
                return true;
            }
            public void touchUp(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                Gdx.app.debug(LOG_TAG,"buttonMegaJump clicked!");
                Save.load();
                final int cost = 2500;
                if(!Save.gd.isMegaJumpPurchased()){
                    ShopDialog dialog = new ShopDialog(Shop.this,
                            "\n\n" +
                                    "MEGA JUMP\n" +
                                    "\n" +
                                    "Jump higher.\n" +
                                    "COST: "+cost+" coins.\n" +
                                    "\n" +
                                    "Buy Mega jump", "buttonMegaJumpUp"){
                        public void result(Object obj) {
                            Gdx.app.debug(LOG_TAG,"result Buy Mega jump?"+obj);


                            if(obj.toString().equals("true") && Save.gd.getMoney() >= cost){
                                if ((MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2)) {
                                    MyGdxGame.res.getMusic("newScreen").play();
                                }

                                Save.gd.setMegaJumpPurchased(true);
                                Save.gd.setMegaJumpEquiped(true);
                                Save.gd.setMoney(Save.gd.getMoney() - cost);
                                Save.save();
                                labelMoney.setText(Integer.toString(Save.gd.getMoney()));
                            }else{

                                if(obj.toString().equals("true")){
                                    if (MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getSound("error").play();
                                    labelMoney.setColor(Color.RED);

//                                    new java.util.Timer().schedule(
//                                            new java.util.TimerTask() {
//                                                @Override
//                                                public void run() {
//                                                    // your code here
//                                                    labelMoney.setColor(Color.WHITE);
//                                                }
//                                            }, 500);
                                    Timer.schedule(new Timer.Task() {
                                        @Override
                                        public void run() {
                                            labelMoney.setColor(Color.WHITE);
                                        }
                                    }, 0.5f);
                                }
                            }
                        }
                    };
                    stage1.addActor(dialog);
                }
                else{

                    ShopDialog dialog = new ShopDialog(Shop.this,
                            "\n\n" +
                                    "MEGA JUMP\n" +
                                    "\n" +
                                    "Jump higher.\n" +
                                    "\n" +
                                    "Equip Mega jump", "buttonMegaJumpUp"){
                        public void result(Object obj) {
                            Gdx.app.debug(LOG_TAG,"result Equip Mega jump"+obj);

                            if(obj.toString().equals("true")){
                                Save.gd.setMegaJumpEquiped(true);
                                if (Save.gd.isBrick2Equiped() && (MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2)) {
                                    MyGdxGame.res.getMusic("newScreen").play();
                                }
                            }
                            if(obj.toString().equals("false")){
                                Save.gd.setMegaJumpEquiped(false);
                            }
                            Save.save();
                            Save.load();
                        }
                    };
                    stage1.addActor(dialog);
                }
            }
        });

        buttonPlay.addListener(new InputListener() {
            public boolean touchDown(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                if (Save.gd.isSoundEnable() == 1 | Save.gd.isSoundEnable() == 2) MyGdxGame.res.getSound("select").play();
                return true;
            }
            public void touchUp(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                click_on_play = true;
                //Gdx.gl.glClearColor(0, 0, 0, 1);
            }
        });

        buttonSecret2.addListener(new InputListener() {
            public boolean touchDown(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {

                return true;
            }

            public void touchUp(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                buttonSecret2.setDisabled(true);
                buttonSecret2.setVisible(false);
                //Gdx.gl.glClearColor(0, 0, 0, 1);
            }
        });

        buttonSecret1.addListener(new InputListener() {
            public boolean touchDown(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                cpt_secret1++;
                Gdx.app.debug(LOG_TAG,"CLICK SECRET 1: " + cpt_secret1);
                if(Save.gd.isSoundEnable() == 1 | Save.gd.isSoundEnable() == 2) MyGdxGame.res.getMusic("laugh").play();
                return true;
            }

            public void touchUp(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                //Gdx.gl.glClearColor(0, 0, 0, 1);
            }
        });

        MyGdxGame.setIsBoosTerritory(false);
    }

    public void resize(int width, int height) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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
        viewport = new Rectangle(crop.x, 0, w, h);
        offsetY = crop.y;
        float offsetX = crop.x;

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glViewport(0,0, (int)viewport.width+ (int)offsetX, Gdx.graphics.getHeight());
        stage0.act();
        sb.begin();
        stage0.draw();
        sb.end();

        Gdx.gl.glViewport((int) viewport.x, (int) viewport.y, (int) viewport.width - (int)offsetX, (int) viewport.height - (int)offsetY);

        stage1.getViewport().update((int) (width - offsetX), (int) (height - offsetY), true);
        stage2.getViewport().update((int) (width - offsetX), (int) (height - offsetY), true);
    }

    public void handleInput() {
        if (click_on_play) {
            click_on_play = false;
            gsm.setState(GameStateManager.MENU);
            if(MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getMusic("newScreen").play();
        }
    }

    public void updateItem(boolean equiped, String item, Button button){
        if(equiped){
            ButtonStyle style = new Button.ButtonStyle();
            style.up = skin.getDrawable(item+"Down");
            style.down = skin.getDrawable(item+"Down");
            button.setStyle(style);
        }else{
            ButtonStyle style = new Button.ButtonStyle();
            style.up = skin.getDrawable(item+"Up");
            style.down = skin.getDrawable(item+"Up");
            button.setStyle(style);
        }
    }

    public void update(float dt) {

        if(MyGdxGame.isSoundEnable() == 2){
            if(!MyGdxGame.res.getMusic("shop").isPlaying()){
                MyGdxGame.res.getMusic("shop").setVolume(1.0f);
                MyGdxGame.res.getMusic("shop").play();
            }
        }

        labelMoney.setText(Integer.toString(Save.gd.getMoney()));

        animationEnemy.update(dt);
        animationEnemyMock.update(dt);
        animationCoin.update(dt);

        updateItem(Save.gd.isFireBallEquiped(), "buttonFireBall", buttonFireBall);
        updateItem(Save.gd.isFireBall2Equiped(), "buttonFireBall2", buttonFireBall2);
        updateItem(Save.gd.isExcaliburEquiped(), "buttonExcalibur", buttonExcalibur);
        updateItem(Save.gd.isKamehamehaEquiped(), "buttonKamehameha", buttonKamehameha);
        updateItem(Save.gd.isLightningEquiped(), "buttonLightning", buttonLightning);
        updateItem(Save.gd.isMegaJumpEquiped(), "buttonMegaJump", buttonMegaJump);
        updateItem(Save.gd.isBootEquiped(), "buttonBoot", buttonBoot);
        updateItem(Save.gd.isBrick2Equiped(), "buttonBrick2", buttonBrick2);
        updateItem(Save.gd.getAdsRemoverPurchased(), "buttonAds", buttonAds);

//        if(MyGdxGame.isSoundEnable() == 2) {
//            //MyGdxGame.res.getMusic("main").setVolume(0.6f);
//            if(!MyGdxGame.res.getMusic("main").isPlaying())
//                MyGdxGame.res.getMusic("main").play();
//        }

        //MyGdxGame.updateBGM();
        handleInput();
        MyGdxGame.background_cloud.update(dt);
        MyGdxGame.background_wood1.update(dt);

        if (fade1.getTime() > fade1.getDuration()) click_on_play = true;

//        if(cpt_secret1 == MyGdxGame.getTapnumbSecret()){
//            cpt_secret1 = 0;
//            game.actionResolver.unlockAchievementGPGS(MyGdxGame.achievementSecretBossShortcut);
//
//            Gdx.app.debug(LOG_TAG,"BOSS SHORTCUT DISCOVERED");
//            MyGdxGame.setShortcutDiscovered(true);
//            gsm.setState(GameStateManager.PLAY);
//
//            if(MyGdxGame.res.getMusic("main").isPlaying())MyGdxGame.res.getMusic("main").setVolume(0);
//
//            if(MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getMusic("secretUnlock").play();
//        }
    }

    public void render() {

        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        if (fade2.getTime() < fade2.getDuration()) {
            stage2.act();
            sb.begin();
            stage2.draw();
            sb.end();
        }
        else {

            sb.setProjectionMatrix(cam.combined);
            shapeRenderer.setProjectionMatrix(cam.combined);
            //MyGdxGame.background_skyDay.render(sb);
            MyGdxGame.background_cloud.render(sb);
            MyGdxGame.background_wood1.render(sb);
            //bg1.render(sb);
            MyGdxGame.background_shop.render(sb);
            float w = 57*3.5f;
            float h = 23*3.5f;
//            sb.begin();
//            sb.draw(animTitle.getFrame(), MyGdxGame.V_WIDTH/2 - w/2, MyGdxGame.V_HEIGHT/2.75f , MyGdxGame.V_WIDTH/2, 670.0f -95/2 ,  w, h,1,1, 0);
//            sb.end();

            shapeRenderer.setColor(Color.BLACK);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            float height = MyGdxGame.V_HEIGHT/12;
            shapeRenderer.rect(0, MyGdxGame.V_HEIGHT-height , MyGdxGame.V_WIDTH, height);
            shapeRenderer.end();

            stage1.act();
            sb.begin();
            //drawStageBackground(Batch batch, float parentAlpha, float x, float y, float width, float height)
            stage1.draw();
            sb.end();

            sb.begin();

            //GEAR BUTTON
            float speed = 8f;
            if (stage1.getActors().items[0].getX() <= -5) {
                stage1.getActors().items[0].setPosition(-stage1.getActors().items[0].getWidth() + cpt_translate_animation1 * speed, stage1.getActors().items[0].getY());
                cpt_translate_animation1++;
            }

            //if((Save.gd.isSoundEnable() == 2) && !MyGdxGame.res.getMusic("main").isPlaying()) MyGdxGame.res.getMusic("main").setVolume(0.6f);
            sb.end();

            sb.begin();

            if(!MyGdxGame.res.getMusic("laugh").isPlaying()){
                time+=4.0f;
                sb.draw(animationEnemy.getFrame(), -250 + time , 202);
            }else{
                sb.draw(animationEnemyMock.getFrame(), -250 + time , 202);
            }

            float coef = 3.0f;
            //sb.draw(animationCoin.getFrame(), (1*imageCoin.getWidth()) , labelMoney.getY());

            //buttonSecret1.setPosition( (-250.0f + ((float) time * a)) + (float)buttonSecret1.getWidth(), 202+(offsetY/coef)+buttonSecret1.getHeight());
            //stage1.getActors().items[1].setPosition(-250 + ((float) time * 3f), 502);
            //offsetY = (offsetY<=0)? 1:offsetY;
            //stage1.getActors().items[1].setPosition((-250 + ((float) time * 1.7f)), 202-(offsetY/coef));
            //stage1.getActors().items[1].draw(sb, 1f);

            MyGdxGame.debugString = "fps: "+Gdx.graphics.getFramesPerSecond()+'\n'+
                    "java heap: "+ (int)(Gdx.app.getJavaHeap()/Math.pow(10, 6))+" Mb"+'\n'+
                    "native heap: "+ (int)(Gdx.app.getNativeHeap()/Math.pow(10, 6))+" Mb"+'\n'+
                    "offDisplay: "+ offsetY+'\n'+
                    "offset: "+ offsetY/coef+'\n'+
                    "secret: "+ cpt_secret1+'\n'+
                    "sX: "+ animationEnemy.getFrame().getRegionX()+'\n'+
                    "sY: "+ buttonSecret1.getY()+'\n'+
                    "tick: "+ time;

            sb.end();

            if(time > 900){
                time = -200;
            }
        }
    }

    public void dispose() {
        stage2.dispose();
        stage1.dispose();
        stage0.dispose();
        if (MyGdxGame.res.getMusic("shop").isPlaying()) MyGdxGame.res.getMusic("shop").stop();
    }

    public String getSecretCode(){
        String out = "";
        return out;
    }


    public void showSecretDialog()
    {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                // do stuff here
                ShopDialog dialog = new ShopDialog(Shop.this,
                        "\n\n" +
                                "UNLOCK ALL POWERS\n" +
                                "\n" +
                                "You found the password!\n" +
                                "\n\n" +
                                " ", "buttonSecret"){
                    public void result(Object obj) {
                        Gdx.app.debug(LOG_TAG,"result Equip Fire ball?"+obj);

                        if(obj.toString().equals("true")){
                        }
                        if(obj.toString().equals("false")){
                        }
                        Save.save();
                        Save.load();
                    }
                };
                stage1.addActor(dialog);
            }
        });
    }
}
