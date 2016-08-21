package com.mygdx.core.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import com.badlogic.gdx.utils.Timer;
import com.mygdx.core.MyGdxGame;
import com.mygdx.core.handlers.Animation;
import com.mygdx.core.handlers.GameStateManager;
import com.mygdx.core.handlers.Save;
import com.mygdx.core.handlers.ShopDialog;

import java.text.NumberFormat;

public class Shop extends GameState {
    private boolean click_on_play;
    private Stage stage1, stage2;
    private AlphaAction fade1, fade2, fade3;
    private Rectangle viewport;
    private AlphaAction fade;
    private float offset = 0;
    private float offsetx;
    private float offsetY = 0.0f;
    private Button buttonPlay, buttonSecret1, buttonSecret2,imageCoin, buttonItem0;
    private Button buttonItem1, buttonItem2, buttonItem3, buttonItem4, buttonItem5;
    private int cpt_secret1 = 0, cpt_secret2 = 0;
    private int cpt_translate_animation1 = 0;
    private Label labelMoney;
    public Skin skin;
    private Vector2 crop;
    private Animation animTitle;

    public Shop(GameStateManager gsm) {

        super(gsm);
        viewport = new Rectangle();
        skin = new Skin();
        skin.addRegions(MyGdxGame.atlas);

        Save.load();

        animTitle = new Animation(new Sprite(MyGdxGame.atlas.findRegion("shop")).split(57,23)[0], 1 / 5f);

        if (!Save.gd.getAdsRemoverPurchased()) {
            String network = game.actionResolver.getNetworkClass();
            if(network == null) network = "ABSENT";
            System.out.print("NETWORK: "+network);
            if(network.equals("4G")|network.equals("3G")|network.equals("WIFI"))
                game.actionResolver.showOrLoadInterstital();
            System.out.println(network);
        }

        MyGdxGame.background_cloud.setVector(+10, 0);
        MyGdxGame.background_skyNight.setVector(0, 0);
        MyGdxGame.background_skyDay.setVector(0, 0);
        MyGdxGame.background_wood1.setVector(-3, 0);

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
        buttonStyleSecret.up = skinButtonSecret.getDrawable("buttonSecret");
        buttonStyleSecret.down = skinButtonSecret.getDrawable("buttonSecret");
        buttonSecret1 = new Button(buttonStyleSecret);
        buttonSecret1.setWidth(Gdx.graphics.getWidth() / 8f);
        buttonSecret1.setHeight(Gdx.graphics.getWidth() / 8f);
        buttonSecret1.setPosition((Gdx.graphics.getWidth() - buttonSecret1.getWidth())/ 2.05f, (Gdx.graphics.getHeight() - buttonSecret1.getWidth())/4.3f );
        stage1.addActor(buttonSecret1);

        buttonSecret2 = new Button(buttonStyleSecret);
        buttonSecret2.setWidth(Gdx.graphics.getWidth() / 8f);
        buttonSecret2.setHeight(Gdx.graphics.getWidth() / 8f);
        buttonSecret2.setPosition((Gdx.graphics.getWidth() - buttonSecret1.getWidth())/ 1.12f, (Gdx.graphics.getHeight() - buttonSecret1.getWidth())/21f );
        stage1.addActor(buttonSecret2);


        ButtonStyle style = new ButtonStyle();
        style = new Button.ButtonStyle();
        style.up = skin.getDrawable("coin");
        style.down = skin.getDrawable("coin");
        imageCoin = new Button(style);
        imageCoin.setWidth(Gdx.graphics.getWidth() / 10f);
        imageCoin.setHeight(Gdx.graphics.getWidth() / 10f);
        imageCoin.setPosition(imageCoin.getWidth() / 4, (Gdx.graphics.getHeight() - imageCoin.getHeight() - 20));
        stage1.addActor(imageCoin);

        style = new Button.ButtonStyle();
        style.up = skin.getDrawable("buttonAdsUp");
        style.down = skin.getDrawable("buttonAdsDown");
        buttonItem0 = new Button(style);
        buttonItem0.setWidth(Gdx.graphics.getWidth() / 6f);
        buttonItem0.setHeight(Gdx.graphics.getWidth() / 6f);
        buttonItem0.setPosition(Gdx.graphics.getWidth()/4.5f, Gdx.graphics.getHeight()/1.5f);
        stage1.addActor(buttonItem0);


        style = new Button.ButtonStyle();
        style.up = skin.getDrawable("buttonCoins1Up");
        style.down = skin.getDrawable("buttonCoins1Down");
        buttonItem1 = new Button(style);
        buttonItem1.setWidth(buttonItem0.getWidth());
        buttonItem1.setHeight(buttonItem0.getWidth());
        buttonItem1.setPosition(buttonItem0.getX() + buttonItem0.getWidth()*1.2f , buttonItem0.getY());
        stage1.addActor(buttonItem1);


        style = new Button.ButtonStyle();
        style.up = skin.getDrawable("buttonCoins2Up");
        style.down = skin.getDrawable("buttonCoins2Down");
        buttonItem2 = new Button(style);
        buttonItem2.setWidth(buttonItem0.getWidth());
        buttonItem2.setHeight(buttonItem0.getWidth());
        buttonItem2.setPosition(buttonItem1.getX() + buttonItem0.getWidth()*1.2f , buttonItem0.getY());
        stage1.addActor(buttonItem2);


        style = new Button.ButtonStyle();
        style.up = skin.getDrawable("buttonFireBallUp");
        style.down = skin.getDrawable("buttonFireBallDown");
        buttonItem3 = new Button(style);
        buttonItem3.setWidth(buttonItem0.getWidth());
        buttonItem3.setHeight(buttonItem0.getWidth());
        buttonItem3.setPosition(buttonItem0.getX() , buttonItem0.getY()*1.185f);
        stage1.addActor(buttonItem3);

        style = new Button.ButtonStyle();
        style.up = skin.getDrawable("buttonExcaliburUp");
        style.down = skin.getDrawable("buttonExcaliburDown");
        buttonItem4 = new Button(style);
        buttonItem4.setWidth(buttonItem0.getWidth());
        buttonItem4.setHeight(buttonItem0.getWidth());
        buttonItem4.setPosition(buttonItem3.getX() + buttonItem0.getWidth()*1.2f , buttonItem3.getY());
        stage1.addActor(buttonItem4);

        style = new Button.ButtonStyle();
        style.up = skin.getDrawable("buttonKamehamehaUp");
        style.down = skin.getDrawable("buttonKamehamehaDown");
        buttonItem5 = new Button(style);
        buttonItem5.setWidth(buttonItem0.getWidth());
        buttonItem5.setHeight(buttonItem0.getWidth());
        buttonItem5.setPosition(buttonItem4.getX() + buttonItem4.getWidth()*1.2f , buttonItem4.getY());
        stage1.addActor(buttonItem5);

        labelMoney = new Label("0", new Label.LabelStyle(new BitmapFont(Gdx.files.internal(MyGdxGame.fontPointPath), false), Color.WHITE));
        labelMoney.setFontScale(Gdx.graphics.getWidth() / 1200f);
        labelMoney.setWidth(Gdx.graphics.getWidth() / 2);
        labelMoney.setHeight(imageCoin.getHeight());
        labelMoney.setAlignment(Align.center | Align.left);
        labelMoney.setPosition(imageCoin.getRight() * 1.1f, imageCoin.getY());
        stage1.addActor(labelMoney);
        labelMoney.setText(Integer.toString(Save.gd.getMoney()));


        Gdx.input.setInputProcessor(stage1);
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

        buttonItem0.addListener(new InputListener() {
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
                System.out.println("buttonItem0 clicked!");
                Save.load();
                if(!Save.gd.getAdsRemoverPurchased()){
                    NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance();
                    String cur = currencyFormatter.format(1).replaceAll("[0-9.,]","");;
                    ShopDialog dialog = new ShopDialog(Shop.this,
                            "\n\n" +
                                    "ADS FREE\n" +
                                    "\n" +
                                    "Removes all ads in game.\n" +
                                    "\n\n" +
                                    "Buy Ads Free?", "buttonAdsUp"){
                        public void result(Object obj) {
                            System.out.println("result Equip ads free?"+obj);
                            System.out.println(obj);
                            if(obj.toString().equals("true")){
                                if ((MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2)) {
                                    MyGdxGame.res.getSound("equiped").play();
                                }
                                Save.gd.setAdsRemoverPurchased(true);
                                Save.save();
                            }
                        }
                    };
                    stage1.addActor(dialog);
                }
                Save.save();
                Save.load();
            }

            ;
        });

        buttonItem1.addListener(new InputListener() {
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
                System.out.println("buttonItem1 clicked!");
                Save.load();
                    NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance();
                    String cur = currencyFormatter.format(1).replaceAll("[0-9.,]","");;
                    ShopDialog dialog = new ShopDialog(Shop.this,
                            "\n\n" +
                                    "1000 Coins\n" +
                                    "\n" +
                                    "Purchase 1000 coins.\n" +
                                    "\n\n" +
                                    "Buy 1000 coins.?", "buttonCoins1Down"){
                        public void result(Object obj) {
                            System.out.println("result"+obj);
                            System.out.println(obj);
                            if(obj.toString().equals("true")){
                                if ((MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2)) {
                                    MyGdxGame.res.getSound("point").play();
                                }
                                Save.gd.setMoney(Save.gd.getMoney()+1000);
                                Save.save();
                                labelMoney.setText(Integer.toString(Save.gd.getMoney()));
                            }
                        }
                    };
                    stage1.addActor(dialog);

                Save.save();
                Save.load();
            }

            ;
        });

        buttonItem2.addListener(new InputListener() {
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
                System.out.println("buttonItem2 clicked!");
                System.out.println("buttonItem1 clicked!");
                Save.load();
                NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance();
                String cur = currencyFormatter.format(1).replaceAll("[0-9.,]","");;
                ShopDialog dialog = new ShopDialog(Shop.this,
                        "\n\n" +
                                "10000 Coins\n" +
                                "\n" +
                                "Purchase 10000 coins.\n" +
                                "\n\n" +
                                "Buy 10000 coins.?", "buttonCoins2Down"){
                    public void result(Object obj) {
                        System.out.println("result"+obj);
                        System.out.println(obj);
                        if(obj.toString().equals("true")){
                            if ((MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2)) {
                                MyGdxGame.res.getSound("point").play();
                            }
                            Save.gd.setMoney(Save.gd.getMoney()+10000);
                            Save.save();
                            labelMoney.setText(Integer.toString(Save.gd.getMoney()));
                        }
                    }
                };
                stage1.addActor(dialog);

                Save.save();
                Save.load();
            }

            ;
        });

        buttonItem3.addListener(new InputListener() {
            public boolean touchDown(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                if (Save.gd.isSoundEnable() == 1 | Save.gd.isSoundEnable() == 2) MyGdxGame.res.getSound("select").play();
                return true;
            }

            public void touchUp(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                System.out.println("buttonItem3 clicked!");
                Save.load();
                final int cost = 10000;
                if(!Save.gd.isFireBallPurchased()){
                    ShopDialog dialog = new ShopDialog(Shop.this,
                            "\n\n" +
                                    "FIRE BALL\n" +
                                    "\n" +
                                    "Fire ball can destroy one enemy  \nin one hit.\n" +
                                    "COST: 1000 coins.\n" +
                                    "\n\n" +
                                    "Equip Fire ball?", "buttonFireBallUp"){
                        public void result(Object obj) {
                            System.out.println("result Equip Fire ball?"+obj);
                            System.out.println(obj);
                            if(obj.toString().equals("true") && Save.gd.getMoney() >= cost){
                                if ((MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2)) {
                                    MyGdxGame.res.getSound("fireball").play();
                                }
                                Save.gd.setFireBallPurchased(true);
                                Save.gd.setFireBallEquiped(true);
                                Save.gd.setExcaliburEquiped(false);
                                Save.gd.setKamehamehaEquiped(false);
                                Save.gd.setMoney(Save.gd.getMoney() - cost);
                                Save.save();
                                labelMoney.setText(Integer.toString(Save.gd.getMoney()));
                            }else{
                                if (MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getSound("error").play();
                                labelMoney.setColor(Color.RED);

                                new java.util.Timer().schedule(
                                        new java.util.TimerTask() {
                                            @Override
                                            public void run() {
                                                // your code here
                                                labelMoney.setColor(Color.WHITE);
                                            }
                                        }, 500);

                            }
                        }
                    };
                    stage1.addActor(dialog);
                }else{
                    Save.gd.setFireBallEquiped(!Save.gd.isFireBallEquiped());
                    Save.gd.setExcaliburEquiped(false);
                    Save.gd.setKamehamehaEquiped(false);
                    if (Save.gd.isFireBallEquiped() && (MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2)) {
                        MyGdxGame.res.getSound("fireball").play();
                    }
                }
                Save.save();
                Save.load();

            }
        });

        buttonItem4.addListener(new InputListener() {
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
                System.out.println("buttonItem4 clicked!");
                Save.load();
                final int cost = 50000;
                if(!Save.gd.isExcaliburPurchased()){
                ShopDialog dialog = new ShopDialog(Shop.this,
                        "\n\n" +
                                "EXCALIBUR\n" +
                                "\n" +
                                "Excalibur kill enemy in one swing.\n" +
                                "COST: "+cost+" coins.\n" +
                                "\n\n" +
                                "Equip Excalibur?", "buttonExcaliburUp"){
                    public void result(Object obj) {
                        System.out.println("result Equip Excalibur?"+obj);
                        System.out.println(obj);
                        if(obj.toString().equals("true") && Save.gd.getMoney() >= cost){
                            if ((MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2)) {
                                MyGdxGame.res.getMusic("slash").play();
                            }
                            Save.gd.setExcaliburPurchased(true);
                            Save.gd.setExcaliburEquiped(true);
                            Save.gd.setFireBallEquiped(false);
                            Save.gd.setKamehamehaEquiped(false);
                            Save.gd.setMoney(Save.gd.getMoney() - cost);
                            Save.save();
                            labelMoney.setText(Integer.toString(Save.gd.getMoney()));
                        }else{
                            if (MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getSound("error").play();
                            labelMoney.setColor(Color.RED);

                            new java.util.Timer().schedule(
                                    new java.util.TimerTask() {
                                        @Override
                                        public void run() {
                                            // your code here
                                            labelMoney.setColor(Color.WHITE);
                                        }
                                    }, 500);

                        }
                    }
                };
                stage1.addActor(dialog);
                }else{
                    Save.gd.setExcaliburEquiped(!Save.gd.isExcaliburEquiped());
                    Save.gd.setFireBallEquiped(false);
                    Save.gd.setKamehamehaEquiped(false);
                    if (Save.gd.isExcaliburEquiped() && (MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2)) {
                        MyGdxGame.res.getMusic("slash").play();
                    }
                }
                Save.save();
                Save.load();
            }

            ;
        });

        buttonItem5.addListener(new InputListener() {
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
                System.out.println("buttonItem5 clicked!");
                Save.load();
                final int cost = 100000;
                if(!Save.gd.isKamehamehaPurchased()){
                    ShopDialog dialog = new ShopDialog(Shop.this,
                            "\n\n" +
                                    "KAMEHAMEHA\n" +
                                    "\n" +
                                    "Kamehameha destroys everything.\n" +
                                    "COST: "+cost+" coins.\n" +
                                    "\n\n" +
                                    "Buy Kamehameha?", "buttonKamehamehaUp"){
                        public void result(Object obj) {
                            System.out.println("result Buy Kamehameha?"+obj);
                            System.out.println(obj);

                            if(obj.toString().equals("true") && Save.gd.getMoney() >= cost){
                                if ((MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2)) {
                                    MyGdxGame.res.getSound("ya").play();
                                }
                                Save.gd.setKamehamehaPurchased(true);
                                Save.gd.setKamehamehaEquiped(true);
                                Save.gd.setExcaliburEquiped(false);
                                Save.gd.setFireBallEquiped(false);
                                Save.gd.setMoney(Save.gd.getMoney() - cost);
                                Save.save();
                                labelMoney.setText(Integer.toString(Save.gd.getMoney()));
                            }else{
                                if (MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getSound("error").play();
                                labelMoney.setColor(Color.RED);

                                new java.util.Timer().schedule(
                                        new java.util.TimerTask() {
                                            @Override
                                            public void run() {
                                                // your code here
                                                labelMoney.setColor(Color.WHITE);
                                            }
                                        }, 500);

                            }
                        }
                    };
                    stage1.addActor(dialog);
                }else{
                    Save.gd.setKamehamehaEquiped(!Save.gd.isKamehamehaEquiped());
                    Save.gd.setExcaliburEquiped(false);
                    Save.gd.setFireBallEquiped(false);
                    if (Save.gd.isKamehamehaEquiped() && (MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2)) {
                        MyGdxGame.res.getSound("ya").play();
                    }
                }
                Save.save();
                Save.load();
            }
        });

        buttonPlay.addListener(new InputListener() {
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
                click_on_play = true;
                //Gdx.gl.glClearColor(0, 0, 0, 1);
            }

            ;
        });

        buttonSecret2.addListener(new InputListener() {
            public boolean touchDown(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {


                return true;
            }

            ;

            public void touchUp(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                buttonSecret2.setDisabled(true);
                buttonSecret2.setVisible(false);
                //Gdx.gl.glClearColor(0, 0, 0, 1);
            }

            ;
        });

        buttonSecret1.addListener(new InputListener() {
            public boolean touchDown(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {

                cpt_secret1++;
                System.out.println("CLICK SECRET 1: " + cpt_secret1);
                if(Save.gd.isSoundEnable() == 1 | Save.gd.isSoundEnable() == 2) MyGdxGame.res.getMusic("secretboss").play();

                return true;
            }

            ;

            public void touchUp(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                //Gdx.gl.glClearColor(0, 0, 0, 1);
            }

            ;
        });

        MyGdxGame.setIsBoosTerritory(false);
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
        float offsetX = crop.x;

        //System.out.println(crop);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glViewport((int) viewport.x, (int) viewport.y, (int) viewport.width - (int)offsetX, (int) viewport.height - (int)offsetY);

        stage1.getViewport().update((int) (width - offsetX), (int) (height - offsetY), true);
        stage2.getViewport().update((int) (width - offsetX), (int) (height - offsetY), true);
    }

    public void handleInput() {
        if (click_on_play) {
            click_on_play = false;
            gsm.setState(GameStateManager.MENU);
            if(MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getSound("newScreen").play();
        }
    }

    public void update(float dt) {

        if (Save.gd.getAdsRemoverPurchased()) {
                game.actionResolver.hideBannerAd();
        }

        if(Save.gd.isFireBallEquiped()){
            ButtonStyle style = new Button.ButtonStyle();
            style.up = skin.getDrawable("buttonFireBallDown");
            style.down = skin.getDrawable("buttonFireBallDown");
            buttonItem3.setStyle(style);
        }else{
            ButtonStyle style = new Button.ButtonStyle();
            style.up = skin.getDrawable("buttonFireBallUp");
            style.down = skin.getDrawable("buttonFireBallUp");
            buttonItem3.setStyle(style);
        }

        if(Save.gd.isExcaliburEquiped()){
            ButtonStyle style = new Button.ButtonStyle();
            style.up = skin.getDrawable("buttonExcaliburDown");
            style.down = skin.getDrawable("buttonExcaliburDown");
            buttonItem4.setStyle(style);
        }else{
            ButtonStyle style = new Button.ButtonStyle();
            style.up = skin.getDrawable("buttonExcaliburUp");
            style.down = skin.getDrawable("buttonExcaliburUp");
            buttonItem4.setStyle(style);
        }

        if(Save.gd.isKamehamehaEquiped()){
            ButtonStyle style = new Button.ButtonStyle();
            style.up = skin.getDrawable("buttonKamehamehaDown");
            style.down = skin.getDrawable("buttonKamehamehaDown");
            buttonItem5.setStyle(style);
        }else{
            ButtonStyle style = new Button.ButtonStyle();
            style.up = skin.getDrawable("buttonKamehamehaUp");
            style.down = skin.getDrawable("buttonKamehamehaUp");
            buttonItem5.setStyle(style);
        }

        if(Save.gd.getAdsRemoverPurchased()){
            ButtonStyle style = new Button.ButtonStyle();
            style.up = skin.getDrawable("buttonAdsDown");
            style.down = skin.getDrawable("buttonAdsDown");
            buttonItem0.setStyle(style);
        }else{
            ButtonStyle style = new Button.ButtonStyle();
            style.up = skin.getDrawable("buttonAdsUp");
            style.down = skin.getDrawable("buttonAdsUp");
            buttonItem0.setStyle(style);
        }

        if(MyGdxGame.isSoundEnable() == 2) {
            MyGdxGame.res.getMusic("main").setVolume(0.4f);
            if(!MyGdxGame.res.getMusic("main").isPlaying())
                MyGdxGame.res.getMusic("main").play();
        }

        MyGdxGame.updateBGM();
        handleInput();
        if(!MyGdxGame.isNightEnable()) {
            MyGdxGame.background_cloud.update(dt);
            MyGdxGame.background_skyDay.update(dt);
        }
        else{
            MyGdxGame.background_skyNight.update(dt);
        }
        //MyGdxGame.background_secret1.update(dt);
        MyGdxGame.background_wood1.update(dt);

        if (fade1.getTime() > fade1.getDuration()) click_on_play = true;

        if(cpt_secret1 == MyGdxGame.getTapnumbSecret()){
            cpt_secret1 = 0;
            game.actionResolver.unlockAchievementGPGS(MyGdxGame.achievementSecretBossShortcut);

            System.out.println("BOSS SHORTCUT DISCOVERED");
            MyGdxGame.setShortcutDiscovered(true);
            gsm.setState(GameStateManager.PLAY);

            if(MyGdxGame.res.getMusic("main").isPlaying())MyGdxGame.res.getMusic("main").setVolume(0);

            if(MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getMusic("secretUnlock").play();
        }
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

            if(MyGdxGame.isNightEnable())
                Gdx.gl.glClearColor(65f / 255f, 18f / 255f, 252f / 255f, 1);
            else
                Gdx.gl.glClearColor(65f / 255f, 18f / 255f, 252f / 255f, 1);


            sb.setProjectionMatrix(cam.combined);
            shapeRenderer.setProjectionMatrix(cam.combined);

            if(MyGdxGame.isNightEnable()) {
                MyGdxGame.background_skyNight.render(sb);
            }else{
                MyGdxGame.background_skyDay.render(sb);
            }
            MyGdxGame.background_wood1.render(sb);
            //bg1.render(sb);
            MyGdxGame.background_title.render(sb);
            float w = 57*3.5f;
            float h = 23*3.5f;
            sb.begin();
            sb.draw(animTitle.getFrame(), MyGdxGame.V_WIDTH/2 - w/2, MyGdxGame.V_HEIGHT/2.75f , MyGdxGame.V_WIDTH/2, 670.0f -95/2 ,  w, h,1,1, 0);
            sb.end();

            if(MyGdxGame.isNightEnable())MyGdxGame.displayBlinkingStars();
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

            if((Save.gd.isSoundEnable() == 2) && !MyGdxGame.res.getMusic("main").isPlaying()) MyGdxGame.res.getMusic("main").setVolume(0.4f);
            sb.end();


            if(!MyGdxGame.isNightEnable())
                MyGdxGame.background_cloud.render(sb);

        }
    }

    public void dispose() {
    }

}
