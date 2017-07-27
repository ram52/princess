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
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.mygdx.core.MyGdxGame;
import com.mygdx.core.handlers.Animation;
import com.mygdx.core.handlers.AnimeActor;
import com.mygdx.core.handlers.GameStateManager;
import com.mygdx.core.handlers.Save;
import com.mygdx.core.handlers.ShopDialog;
import com.mygdx.core.handlers.SimpleDirectionGestureDetector;

import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Scanner;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import static com.mygdx.core.MyGdxGame.actionResolver;
import static com.mygdx.core.MyGdxGame.animationCoinShop;
import static com.mygdx.core.MyGdxGame.animationEnemyMockShop;
import static com.mygdx.core.MyGdxGame.animationEnemyShop;
import static com.mygdx.core.MyGdxGame.buttonAds;
import static com.mygdx.core.MyGdxGame.buttonBoot;
import static com.mygdx.core.MyGdxGame.buttonBrick2;
import static com.mygdx.core.MyGdxGame.buttonCoin1;
import static com.mygdx.core.MyGdxGame.buttonCoin2;
import static com.mygdx.core.MyGdxGame.buttonCoin3;
import static com.mygdx.core.MyGdxGame.buttonExcalibur;
import static com.mygdx.core.MyGdxGame.buttonFireBall;
import static com.mygdx.core.MyGdxGame.buttonFireBall2;
import static com.mygdx.core.MyGdxGame.buttonKamehameha;
import static com.mygdx.core.MyGdxGame.buttonLightning;
import static com.mygdx.core.MyGdxGame.buttonMegaJump;
import static com.mygdx.core.MyGdxGame.buttonPlayShop;
import static com.mygdx.core.MyGdxGame.buttonRestore;
import static com.mygdx.core.MyGdxGame.buttonSecret1Shop;
import static com.mygdx.core.MyGdxGame.buttonSecret2Shop;
import static com.mygdx.core.MyGdxGame.click_on_playShop;
import static com.mygdx.core.MyGdxGame.cpt_secret1Shop;
import static com.mygdx.core.MyGdxGame.cpt_translate_animation1Shop;
import static com.mygdx.core.MyGdxGame.dialogAdsRemover;
import static com.mygdx.core.MyGdxGame.dialogBootNotPurchased;
import static com.mygdx.core.MyGdxGame.dialogBootPurchased;
import static com.mygdx.core.MyGdxGame.dialogBrickNotPurchased;
import static com.mygdx.core.MyGdxGame.dialogBrickPurchased;
import static com.mygdx.core.MyGdxGame.dialogCoin1NotPurchased;
import static com.mygdx.core.MyGdxGame.dialogCoin2NotPurchased;
import static com.mygdx.core.MyGdxGame.dialogCoin3NotPurchased;
import static com.mygdx.core.MyGdxGame.dialogExcaliburNotPurchased;
import static com.mygdx.core.MyGdxGame.dialogExcaliburPurchased;
import static com.mygdx.core.MyGdxGame.dialogFireBall2NotPurchased;
import static com.mygdx.core.MyGdxGame.dialogFireBall2Purchased;
import static com.mygdx.core.MyGdxGame.dialogFireBallNotPurchased;
import static com.mygdx.core.MyGdxGame.dialogFireBallPurchased;
import static com.mygdx.core.MyGdxGame.dialogKamehamehaNotPurchased;
import static com.mygdx.core.MyGdxGame.dialogKamehamehaPurchased;
import static com.mygdx.core.MyGdxGame.dialogLightningNotPurchased;
import static com.mygdx.core.MyGdxGame.dialogLightningPurchased;
import static com.mygdx.core.MyGdxGame.dialogMegaJumpNotPurchased;
import static com.mygdx.core.MyGdxGame.dialogMegaJumpPurchased;
import static com.mygdx.core.MyGdxGame.fade1Shop;
import static com.mygdx.core.MyGdxGame.fade2Shop;
import static com.mygdx.core.MyGdxGame.fade3Shop;
import static com.mygdx.core.MyGdxGame.fadeShop;
import static com.mygdx.core.MyGdxGame.imageCoinShop;
import static com.mygdx.core.MyGdxGame.introShop;
import static com.mygdx.core.MyGdxGame.labelMoneyShop;
import static com.mygdx.core.MyGdxGame.offsetYShop;
import static com.mygdx.core.MyGdxGame.skinShop;
import static com.mygdx.core.MyGdxGame.stage0Shop;
import static com.mygdx.core.MyGdxGame.stage1Shop;
import static com.mygdx.core.MyGdxGame.stage2Shop;
import static com.mygdx.core.MyGdxGame.timeShop;
import static com.mygdx.core.MyGdxGame.viewportShop;
import static com.mygdx.core.MyGdxGame.gdShop;


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
    public static String LOG_TAG = Shop.class.getSimpleName();

    public Shop(GameStateManager gsm) {

        super(gsm);

        game.actionResolver.hideBannerAd();

        if(MyGdxGame.isSoundEnable() == 2){
            if(!MyGdxGame.res.getMusic("shop").isPlaying()){
                MyGdxGame.res.getMusic("shop").setVolume(1.0f);
                MyGdxGame.res.getMusic("shop").play();
            }
        }

        if(MyGdxGame.lastPlayerPosition == null)
            MyGdxGame.lastPlayerPosition = new Vector2(0,0);
        else
            MyGdxGame.lastPlayerPosition.set(0,0);

        if(introShop == null){
            introShop = new Image(MyGdxGame.atlas.findRegion("backgroundSky"));
            introShop.setFillParent(true);
            stage0Shop = new Stage();
            stage0Shop.addActor(introShop);
            viewportShop = new Rectangle();
            skinShop = new Skin();
            skinShop.addRegions(MyGdxGame.atlas);
            Save.load();

            Sprite tex = new Sprite(MyGdxGame.atlas.findRegion("enemy"));
            TextureRegion[] sprites = tex.split(64, 64)[0];
            for(int i=0;i<sprites.length;i++)
                sprites[i].flip(true,false);
            animationEnemyShop = new Animation(sprites, 1 / 5f);

            tex = new Sprite(MyGdxGame.atlas.findRegion("enemyMock"));
            sprites = tex.split(64, 64)[0];
            for(int i=0;i<sprites.length;i++)
                sprites[i].flip(true,false);
            animationEnemyMockShop = new Animation(sprites, 1 / 5f);
        }


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


        if(gdShop == null)
            gdShop = new SimpleDirectionGestureDetector(
                    new SimpleDirectionGestureDetector.DirectionListener() {

                        @Override
                        public void onUp() {
                            Gdx.app.debug(LOG_TAG,"up");
                        }

                        @Override
                        public void onRight() {
                            Gdx.app.debug(LOG_TAG,"rightMenu");
                        }

                        @Override
                        public void onLeft() {
                            Gdx.app.debug(LOG_TAG,"leftMenu");
                        }

                        @Override
                        public void onDown() {

                            if((Save.gd.isSoundEnable() == 1 | Save.gd.isSoundEnable() == 2) && timeShop > 200 && timeShop < 700 && !MyGdxGame.res.getMusic("laugh").isPlaying()){
                                MyGdxGame.res.getMusic("laugh").play();
                                cpt_secret1Shop++;
                                Gdx.app.debug(LOG_TAG,"CLICK SECRET 1: " + cpt_secret1Shop);
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
                                            game.actionResolver.unlockAchievementGPGS(MyGdxGame.achievementSecret);
                                            //showSecretDialog();
                                        }else if(input.equals("coin")){
                                            Save.gd.setMoney(Save.gd.getMoney()+1);
                                            Save.save();
                                            labelMoneyShop.setText(Integer.toString(Save.gd.getMoney()));
                                            Save.load();
                                        }else if(input.equals("lot of coins")){
//                                        Save.gd.setMoney(Save.gd.getMoney()+10);
//                                        Save.save();
//                                        labelMoneyShop.setText(Integer.toString(Save.gd.getMoney()));
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
        click_on_playShop = false;

        if(stage1Shop == null){
            stage1Shop = new Stage();
            stage2Shop = new Stage();
            Skin skinButtonPlay = new Skin();
            skinButtonPlay.addRegions(MyGdxGame.atlas);
            ButtonStyle buttonStylePlay = new ButtonStyle();
            buttonStylePlay.up = skinButtonPlay.getDrawable("buttonExit2Up");
            buttonStylePlay.down = skinButtonPlay.getDrawable("buttonExit2Down");
            buttonPlayShop = new Button(buttonStylePlay);
            buttonPlayShop.setWidth(Gdx.graphics.getWidth() / 5f);
            buttonPlayShop.setHeight(Gdx.graphics.getHeight() / 7.8f);
            buttonPlayShop.setPosition(-400, Gdx.graphics.getHeight()/100f);

            stage1Shop.addActor(buttonPlayShop);

            Skin skinButtonSecret = new Skin();
            skinButtonSecret.addRegions(MyGdxGame.atlas);
            ButtonStyle buttonStyleSecret = new ButtonStyle();
            buttonStyleSecret.up = skinButtonSecret.getDrawable("black");
            buttonStyleSecret.down = skinButtonSecret.getDrawable("black");
            buttonSecret1Shop = new Button(buttonStyleSecret);
            buttonSecret1Shop.setWidth(Gdx.graphics.getWidth() / 8f);
            buttonSecret1Shop.setHeight(Gdx.graphics.getWidth() / 8f);
            buttonSecret1Shop.setPosition((Gdx.graphics.getWidth() - buttonSecret1Shop.getWidth())/ 2.05f, (Gdx.graphics.getHeight() - buttonSecret1Shop.getWidth())/4.3f );
            //stage1Shop.addActor(buttonSecret1Shop);

            buttonSecret2Shop = new Button(buttonStyleSecret);
            buttonSecret2Shop.setWidth(Gdx.graphics.getWidth() / 8f);
            buttonSecret2Shop.setHeight(Gdx.graphics.getWidth() / 8f);
            buttonSecret2Shop.setPosition((Gdx.graphics.getWidth() - buttonSecret1Shop.getWidth())/ 1.12f, (Gdx.graphics.getHeight() - buttonSecret1Shop.getWidth())/21f );
            //stage1Shop.addActor(buttonSecret2Shop);

            ButtonStyle style = new ButtonStyle();
            style = new Button.ButtonStyle();
            style.up = skinShop.getDrawable("coin");
            style.down = skinShop.getDrawable("coin");
            imageCoinShop = new Button(style);
            imageCoinShop.setWidth(Gdx.graphics.getWidth() / 10f);
            imageCoinShop.setHeight(Gdx.graphics.getWidth() / 10f);
            imageCoinShop.setPosition((Gdx.graphics.getWidth()- 5* imageCoinShop.getWidth()) , imageCoinShop.getHeight()/2);
            //stage1Shop.addActor(imageCoinShop);

            /**FIRST ITEM ROW*/
            style = new Button.ButtonStyle();
            style.up = skinShop.getDrawable("buttonFireBallUp");
            style.down = skinShop.getDrawable("buttonFireBallDown");
            buttonFireBall = new Button(style);
            buttonFireBall.setWidth(Gdx.graphics.getWidth() / 6f);
            buttonFireBall.setHeight(Gdx.graphics.getWidth() / 6f);
            buttonFireBall.setPosition(Gdx.graphics.getWidth()/8.5f, Gdx.graphics.getHeight()/1.30f);
            stage1Shop.addActor(buttonFireBall);

            style = new Button.ButtonStyle();
            style.up = skinShop.getDrawable("buttonExcaliburUp");
            style.down = skinShop.getDrawable("buttonExcaliburDown");
            buttonExcalibur = new Button(style);
            buttonExcalibur.setWidth(buttonFireBall.getWidth());
            buttonExcalibur.setHeight(buttonFireBall.getWidth());
            buttonExcalibur.setPosition(buttonFireBall.getX() + buttonFireBall.getWidth()*1.2f , buttonFireBall.getY());
            stage1Shop.addActor(buttonExcalibur);

            style = new Button.ButtonStyle();
            style.up = skinShop.getDrawable("buttonKamehamehaUp");
            style.down = skinShop.getDrawable("buttonKamehamehaDown");
            buttonKamehameha = new Button(style);
            buttonKamehameha.setWidth(buttonFireBall.getWidth());
            buttonKamehameha.setHeight(buttonFireBall.getWidth());
            buttonKamehameha.setPosition(buttonExcalibur.getX() + buttonExcalibur.getWidth()*1.2f , buttonExcalibur.getY());
            stage1Shop.addActor(buttonKamehameha);

            style = new Button.ButtonStyle();
            style.up = skinShop.getDrawable("buttonBootUp");
            style.down = skinShop.getDrawable("buttonBootDown");
            buttonBoot = new Button(style);
            buttonBoot.setWidth(buttonFireBall.getWidth());
            buttonBoot.setHeight(buttonFireBall.getWidth());
            buttonBoot.setPosition(buttonKamehameha.getX() + buttonBoot.getWidth()*1.2f , buttonKamehameha.getY());
            stage1Shop.addActor(buttonBoot);

            /**SECOND ITEM ROW*/
            style = new Button.ButtonStyle();
            style.up = skinShop.getDrawable("buttonFireBall2Up");
            style.down = skinShop.getDrawable("buttonFireBall2Down");
            buttonFireBall2 = new Button(style);
            buttonFireBall2.setWidth(buttonFireBall.getWidth());
            buttonFireBall2.setHeight(buttonFireBall.getWidth());
            buttonFireBall2.setPosition(buttonFireBall.getX(), buttonFireBall.getY() - buttonFireBall.getHeight()*1.2f);
            stage1Shop.addActor(buttonFireBall2);

            style = new Button.ButtonStyle();
            style.up = skinShop.getDrawable("buttonMegaJumpUp");
            style.down = skinShop.getDrawable("buttonMegaJumpDown");
            buttonMegaJump = new Button(style);
            buttonMegaJump.setWidth(buttonFireBall.getWidth());
            buttonMegaJump.setHeight(buttonFireBall.getWidth());
            buttonMegaJump.setPosition(buttonFireBall2.getX() + buttonFireBall2.getWidth()*1.2f, buttonFireBall2.getY());
            stage1Shop.addActor(buttonMegaJump);

            style = new Button.ButtonStyle();
            style.up = skinShop.getDrawable("buttonLightningUp");
            style.down = skinShop.getDrawable("buttonLightningDown");
            buttonLightning = new Button(style);
            buttonLightning.setWidth(buttonFireBall.getWidth());
            buttonLightning.setHeight(buttonFireBall.getWidth());
            buttonLightning.setPosition(buttonMegaJump.getX() + buttonFireBall2.getWidth()*1.2f, buttonFireBall2.getY());
            stage1Shop.addActor(buttonLightning);

            style = new Button.ButtonStyle();
            style.up = skinShop.getDrawable("buttonBrick2Up");
            style.down = skinShop.getDrawable("buttonBrick2Down");
            buttonBrick2 = new Button(style);
            buttonBrick2.setWidth(buttonFireBall.getWidth());
            buttonBrick2.setHeight(buttonFireBall.getWidth());
            buttonBrick2.setPosition(buttonLightning.getX() + buttonFireBall2.getWidth()*1.2f, buttonFireBall2.getY());
            stage1Shop.addActor(buttonBrick2);


            /**THIRD ITEM ROW*/
            style = new Button.ButtonStyle();
            style.up = skinShop.getDrawable("buttonAdsUp");
            style.down = skinShop.getDrawable("buttonAdsDown");
            buttonAds = new Button(style);
            buttonAds.setWidth(buttonFireBall.getWidth());
            buttonAds.setHeight(buttonFireBall.getWidth());
            buttonAds.setPosition(Gdx.graphics.getWidth()/8.5f, buttonFireBall2.getY() - buttonFireBall.getHeight()*1.2f);
            stage1Shop.addActor(buttonAds);

            style = new Button.ButtonStyle();
            style.up = skinShop.getDrawable("buttonCoins1Up");
            style.down = skinShop.getDrawable("buttonCoins1Down");
            buttonCoin1 = new Button(style);
            buttonCoin1.setWidth(buttonAds.getWidth());
            buttonCoin1.setHeight(buttonAds.getWidth());
            buttonCoin1.setPosition(buttonAds.getX() + buttonAds.getWidth()*1.2f , buttonAds.getY());
            stage1Shop.addActor(buttonCoin1);

            style = new Button.ButtonStyle();
            style.up = skinShop.getDrawable("buttonCoins2Up");
            style.down = skinShop.getDrawable("buttonCoins2Down");
            buttonCoin2 = new Button(style);
            buttonCoin2.setWidth(buttonAds.getWidth());
            buttonCoin2.setHeight(buttonAds.getWidth());
            buttonCoin2.setPosition(buttonCoin1.getX() + buttonAds.getWidth()*1.2f , buttonAds.getY());
            stage1Shop.addActor(buttonCoin2);

            style = new Button.ButtonStyle();
            style.up = skinShop.getDrawable("buttonCoins3Up");
            style.down = skinShop.getDrawable("buttonCoins3Down");
            buttonCoin3 = new Button(style);
            buttonCoin3.setWidth(buttonAds.getWidth());
            buttonCoin3.setHeight(buttonAds.getWidth());
            buttonCoin3.setPosition(buttonCoin2.getX() + buttonCoin3.getWidth()*1.2f , buttonCoin2.getY());
            stage1Shop.addActor(buttonCoin3);

            style = new Button.ButtonStyle();
            style.up = skinShop.getDrawable("buttonRestoreUp");
            style.down = skinShop.getDrawable("buttonRestoreDown");
            buttonRestore = new Button(style);
            buttonRestore.setWidth(buttonAds.getWidth()*2);
            buttonRestore.setHeight(buttonAds.getWidth()/1.3f);
            buttonRestore.setPosition(Gdx.graphics.getWidth() - buttonRestore.getWidth()*1.15f, buttonRestore.getHeight()/2.45f);
            stage1Shop.addActor(buttonRestore);


            labelMoneyShop = new Label("0", new Label.LabelStyle(new BitmapFont(Gdx.files.internal(MyGdxGame.fontScorePath), false), Color.WHITE));
            float fScale = Gdx.graphics.getWidth() / 400f;
            labelMoneyShop.setFontScale(fScale);

            labelMoneyShop.setWidth(Gdx.graphics.getWidth());
            labelMoneyShop.setHeight(Gdx.graphics.getHeight()/15f);
            labelMoneyShop.setPosition((Gdx.graphics.getWidth()- labelMoneyShop.getWidth())/2, Gdx.graphics.getHeight() - labelMoneyShop.getHeight()*1.1f);
            labelMoneyShop.setAlignment(Align.center);

            stage1Shop.addActor(labelMoneyShop);
            labelMoneyShop.setText(Integer.toString(Save.gd.getMoney()));

            animationCoinShop = new Animation(new Sprite(MyGdxGame.atlas.findRegion("coinsprite")).split(64,64)[0], 1 / 5f);

            com.badlogic.gdx.graphics.g2d.Animation a = new com.badlogic.gdx.graphics.g2d.Animation(1/10f, animationCoinShop.getFrames());

            AnimeActor anim = new AnimeActor(a);
            anim.setHeight(labelMoneyShop.getHeight());
            anim.setWidth(labelMoneyShop.getHeight());
            anim.setPosition(100, labelMoneyShop.getY());
            stage1Shop.addActor(anim);

            fadeShop = new AlphaAction();
            fadeShop.setDuration(0f);
            //scoreLabel.setText(Float.toString(offset));
            fade1Shop = new AlphaAction();
            fade1Shop.setDuration(0f);
            fade2Shop = new AlphaAction();
            fade2Shop.setDuration(0f);
            stage2Shop.addAction(fade2Shop);
            fade3Shop = new AlphaAction();
            fade3Shop.setDuration(0f);
            stage1Shop.addAction(Actions.sequence(Actions.alpha(1), Actions.fadeIn(0f)));
        }

        InputMultiplexer im = new InputMultiplexer();
        im.addProcessor(gdShop);
        im.addProcessor(stage1Shop);
        Gdx.input.setInputProcessor(im);

        buttonRestore.addListener(new InputListener() {
            public boolean touchDown(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                if (Save.gd.isSoundEnable() == 1 | Save.gd.isSoundEnable() == 2) MyGdxGame.res.getSound("select").play();
                return true;
            }
            public void touchUp(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                Gdx.app.debug(LOG_TAG,"buttonRestore clicked!");
                if(actionResolver != null){
                    if (actionResolver.getSignedInGPGS())
                        actionResolver.getAchievementsGPGS();
                    else
                        actionResolver.loginGPGS(false);
                }
            }
        });

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
                    NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance();
                    String cur = currencyFormatter.format(1).replaceAll("[0-9.,]","");;
                    if(dialogAdsRemover == null){
                        dialogAdsRemover = new ShopDialog(Shop.this,
                                "\n\n" +
                                        "ADS REMOVER\n" +
                                        "\n" +
                                        "Removes all ads in game.\n" +
                                        "\n\n" +
                                        "Remove ads?", "buttonAdsUp"){
                            public void result(Object obj) {
                                dialogAdsRemover.remove();
                                Gdx.app.debug(LOG_TAG,"result Equip ads free?"+obj);

                                if(obj.toString().equals("true")){
                                    MyGdxGame.actionResolver.purchaseAdsRemover();
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
                    }
                    dialogAdsRemover.clearActions();
                    stage1Shop.addActor(dialogAdsRemover);

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
                NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance();
                String cur = currencyFormatter.format(1).replaceAll("[0-9.,]","");
                final int money = 100;
                if(dialogCoin1NotPurchased == null){
                    dialogCoin1NotPurchased = new ShopDialog(Shop.this,
                            "\n\n" +
                                    money+" COINS\n" +
                                    "\n" +
                                    "Purchase "+money+" coins.\n" +
                                    "\n\n" +
                                    "Buy "+money+" coins.?", "buttonCoins1Up"){
                        public void result(Object obj) {
                            dialogCoin1NotPurchased.remove();
                            Gdx.app.debug(LOG_TAG,"result"+obj);
                            if(obj.toString().equals("true")){
//                                if ((MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2)) {
//                                    MyGdxGame.res.getSound("point").play();
//                                }
//                                Save.gd.setMoney(Save.gd.getMoney()+money);
//                                Save.save();
//                                labelMoneyShop.setText(Integer.toString(Save.gd.getMoney()));
                                MyGdxGame.actionResolver.purchaseHundredCoins();
                            }
                            Save.save();
                            Save.load();
                        }
                    };
                }
                dialogCoin1NotPurchased.clearActions();
                stage1Shop.addActor(dialogCoin1NotPurchased);
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
                NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance();
                String cur = currencyFormatter.format(1).replaceAll("[0-9.,]","");
                final int money = 1000;
                if(dialogCoin2NotPurchased == null){
                    dialogCoin2NotPurchased = new ShopDialog(Shop.this,
                            "\n\n" +
                                    money+" COINS\n" +
                                    "\n" +
                                    "Purchase "+money+" coins.\n" +
                                    "\n\n" +
                                    "Buy "+money+" coins.?", "buttonCoins2Up"){
                        public void result(Object obj) {
                            dialogCoin2NotPurchased.remove();
                            Gdx.app.debug(LOG_TAG,"result"+obj);

                            if(obj.toString().equals("true")){
//                            if ((MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2)) {
//                                MyGdxGame.res.getSound("point").play();
//                            }
//                            Save.gd.setMoney(Save.gd.getMoney()+money);
//                            Save.save();
//                            labelMoneyShop.setText(Integer.toString(Save.gd.getMoney()));
                                MyGdxGame.actionResolver.purchaseThousandCoins();
                            }
                            Save.save();
                            Save.load();
                        }
                    };
                }
                dialogCoin2NotPurchased.clearActions();
                stage1Shop.addActor(dialogCoin2NotPurchased);
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
                NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance();
                String cur = currencyFormatter.format(1).replaceAll("[0-9.,]","");;
                int value = 40;
                if(dialogCoin3NotPurchased == null){
                    dialogCoin3NotPurchased = new ShopDialog(Shop.this,
                            "\n\n" +
                                    value+" FREE COINS\n" +
                                    "\n" +
                                    "Watch a video for "+value+" coins FREE.\n" +
                                    "\n\n" +
                                    "Watch video ?", "buttonCoins3Up"){
                        public void result(Object obj) {
                            dialogCoin3NotPurchased.remove();
                            Gdx.app.debug(LOG_TAG,"result"+obj);

                            if(obj.toString().equals("true")){
                                String network = MyGdxGame.actionResolver.getNetworkClass();
                                if(network == null) network = "ABSENT";
                                Gdx.app.debug(LOG_TAG,"NETWORK: " + network);
                                if(network.equals("4G")|network.equals("3G")|network.equals("WIFI")) {
                                    MyGdxGame.actionResolver.showOrLoadRewardedVideoChartboost();
                                    //game.actionResolver.showRewardedVideoChartBoost();
                                }
//                            if ((MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2)) {
//                                MyGdxGame.res.getSound("point").play();
//                            }
//                            Save.gd.setMoney(Save.gd.getMoney()+20);
//                            Save.save();
//                            labelMoneyShop.setText(Integer.toString(Save.gd.getMoney()));
                            }
                            Save.save();
                            Save.load();
                        }
                    };
                }
                dialogCoin3NotPurchased.clearActions();
                stage1Shop.addActor(dialogCoin3NotPurchased);
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

                    if(dialogFireBallNotPurchased == null){
                        dialogFireBallNotPurchased = new ShopDialog(Shop.this,
                                "\n\n" +
                                        "FIRE BALL\n" +
                                        "\n" +
                                        "Reloads every 2 enemies killed.\n" +
                                        "COST: "+cost+" coins.\n" +
                                        "\n\n" +
                                        "Buy Fire ball?", "buttonFireBallUp"){
                            public void result(Object obj) {
                                Gdx.app.debug(LOG_TAG,"result Buy Fire ball?"+obj);
                                dialogFireBallPurchased.remove();

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
                                    labelMoneyShop.setText(Integer.toString(Save.gd.getMoney()));
                                }else{
                                    if(obj.toString().equals("true")){
                                        if (MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getSound("error").play();
                                        labelMoneyShop.setColor(Color.RED);

                                        new java.util.Timer().schedule(
                                                new java.util.TimerTask() {
                                                    @Override
                                                    public void run() {
                                                        // your code here
                                                        labelMoneyShop.setColor(Color.WHITE);
                                                    }
                                                }, 500);
                                    }

                                }
                            }

                            @Override
                            public void hide (Action action) {
                            }

                            @Override
                            public void hide () {
                            }
                        };

                        dialogFireBallNotPurchased.clearActions();
                        stage1Shop.addActor(dialogFireBallNotPurchased);
                    }



                }else{

                    if(dialogFireBallPurchased == null){
                        dialogFireBallPurchased = new ShopDialog(Shop.this,
                                "\n\n" +
                                        "FIRE BALL\n" +
                                        "\n" +
                                        "Reloads by killing enemies.\n" +
                                        "\n\n" +
                                        "Equip Fire ball?", "buttonFireBallUp"){
                            public void result(Object obj) {
                                Gdx.app.debug(LOG_TAG,"result Equip Fire ball?"+obj);

                                dialogFireBallPurchased.remove();

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
                                    //stage1Shop.getActors().get(stage1Shop.getActors().indexOf(dialogFireBallPurchased,true)).remove();
                                }
                                if(obj.toString().equals("false")){
                                    Save.gd.setFireBallEquiped(false);
                                }
                                Save.save();
                                Save.load();
                            }

                            @Override
                            public void hide (Action action) {
                            }

                            @Override
                            public void hide () {
                            }

                        };

                    }


                    dialogFireBallPurchased.clearActions();
                    stage1Shop.addActor(dialogFireBallPurchased);

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
                final int cost = 800;
                if(!Save.gd.isFireBall2Purchased()){
                    if(dialogFireBall2NotPurchased == null){
                        dialogFireBall2NotPurchased = new ShopDialog(Shop.this,
                                "\n\n" +
                                        "HADOU BALL\n" +
                                        "\n" +
                                        "Can kill multiple enemies.\n"+
                                        "Reloads by killing enemies.\n" +
                                        "COST: "+cost+" coins.\n" +
                                        "\n\n" +
                                        "Buy Hadou ball?", "buttonFireBall2Up"){
                            public void result(Object obj) {
                                dialogFireBall2NotPurchased.remove();
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
                                    labelMoneyShop.setText(Integer.toString(Save.gd.getMoney()));
                                    //MyGdxGame.actionResolver.purchaseHadouBall();
                                }else{
                                    if(obj.toString().equals("true")){
                                        if (MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getSound("error").play();
                                        labelMoneyShop.setColor(Color.RED);

                                        new java.util.Timer().schedule(
                                                new java.util.TimerTask() {
                                                    @Override
                                                    public void run() {
                                                        // your code here
                                                        labelMoneyShop.setColor(Color.WHITE);
                                                    }
                                                }, 500);
                                    }

                                }
                            }
                            @Override
                            public void hide (Action action) {
                            }

                            @Override
                            public void hide () {
                            }
                        };
                    }

                    dialogFireBall2NotPurchased.clearActions();
                    stage1Shop.addActor(dialogFireBall2NotPurchased);
                }else{


                    if(dialogFireBall2Purchased == null){
                        dialogFireBall2Purchased = new ShopDialog(Shop.this,
                                "\n\n" +
                                        "HADOU BALL\n" +
                                        "\n" +
                                        "Can kill multiple enemies.\n"+
                                        "Reloads every 4 enemies killed.\n" +
                                        "\n" +
                                        "Equip Hadou ball?", "buttonFireBall2Up"){
                            public void result(Object obj) {

                                dialogFireBall2Purchased.remove();
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
                            @Override
                            public void hide (Action action) {
                            }

                            @Override
                            public void hide () {
                            }
                        };
                    }

                    dialogFireBall2Purchased.clearActions();
                    stage1Shop.addActor(dialogFireBall2Purchased);
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



            public void touchUp(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                Gdx.app.debug(LOG_TAG,"buttonExcalibur clicked!");
                Save.load();
                final int cost = 1000;
                if(!Save.gd.isExcaliburPurchased()){
                    if(dialogExcaliburNotPurchased == null){
                        dialogExcaliburNotPurchased = new ShopDialog(Shop.this,
                                "\n\n" +
                                        "EXCALIBUR\n" +
                                        "\n" +
                                        "Excalibur kill enemy in one swing.\n" +
                                        "COST: "+cost+" coins.\n" +
                                        "\n" +
                                        "Buy Excalibur?", "buttonExcaliburUp"){
                            public void result(Object obj) {
                                Gdx.app.debug(LOG_TAG,"result Equip Excalibur?"+obj);
                                dialogExcaliburNotPurchased.remove();

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
                                    labelMoneyShop.setText(Integer.toString(Save.gd.getMoney()));
                                    //MyGdxGame.actionResolver.purchaseExcalibur();
                                }else{
                                    if(obj.toString().equals("true")){
                                        if (MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getSound("error").play();
                                        labelMoneyShop.setColor(Color.RED);

                                        new java.util.Timer().schedule(
                                                new java.util.TimerTask() {
                                                    @Override
                                                    public void run() {
                                                        // your code here
                                                        labelMoneyShop.setColor(Color.WHITE);
                                                    }
                                                }, 500);
                                    }

                                }
                            }
                        };
                    }
                    dialogExcaliburNotPurchased.clearActions();
                    stage1Shop.addActor(dialogExcaliburNotPurchased);
                }else{

                    if(dialogExcaliburPurchased == null){
                        dialogExcaliburPurchased = new ShopDialog(Shop.this,
                                "\n\n" +
                                        "EXCALIBUR\n" +
                                        "\n" +
                                        "Excalibur kill enemy in one swing.\n" +
                                        "\n" +
                                        "Equip Excalibur?", "buttonExcaliburUp"){
                            public void result(Object obj) {
                                dialogExcaliburPurchased.remove();
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
                    }
                    dialogExcaliburPurchased.clearActions();
                    stage1Shop.addActor(dialogExcaliburPurchased);
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
                final int cost = 900;
                if(!Save.gd.isKamehamehaPurchased()){
                    if(dialogKamehamehaNotPurchased == null){
                        dialogKamehamehaNotPurchased = new ShopDialog(Shop.this,
                                "\n\n" +
                                        "KAME BEAM\n" +
                                        "\n" +
                                        "Destroys everything.\n" +
                                        "COST: "+cost+" coins.\n" +
                                        "\n" +
                                        "Buy Kame beam?", "buttonKamehamehaUp"){
                            public void result(Object obj) {
                                dialogKamehamehaNotPurchased.remove();
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
                                    labelMoneyShop.setText(Integer.toString(Save.gd.getMoney()));
                                    //MyGdxGame.actionResolver.purchaseKamebeam();
                                }else{

                                    if(obj.toString().equals("true")){
                                        if (MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getSound("error").play();
                                        labelMoneyShop.setColor(Color.RED);

                                        new java.util.Timer().schedule(
                                                new java.util.TimerTask() {
                                                    @Override
                                                    public void run() {
                                                        // your code here
                                                        labelMoneyShop.setColor(Color.WHITE);
                                                    }
                                                }, 500);
                                    }

                                }
                            }
                        };
                    }
                    dialogKamehamehaNotPurchased.clearActions();
                    stage1Shop.addActor(dialogKamehamehaNotPurchased);
                }else{

                    if(dialogKamehamehaPurchased == null){
                        dialogKamehamehaPurchased = new ShopDialog(Shop.this,
                                "\n\n" +
                                        "KAME BEAM\n" +
                                        "\n" +
                                        "Destroys everything.\n" +
                                        "\n" +
                                        "Equip Kame beam?", "buttonKamehamehaUp"){
                            public void result(Object obj) {
                                dialogKamehamehaPurchased.remove();
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
                    }
                    dialogKamehamehaPurchased.clearActions();
                    stage1Shop.addActor(dialogKamehamehaPurchased);
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
                final int cost = 300;
                if(!Save.gd.isBootPurchased()){
                    if(dialogBootNotPurchased == null){
                        dialogBootNotPurchased = new ShopDialog(Shop.this,
                                "\n\n" +
                                        "BOOTS\n" +
                                        "\n" +
                                        "Make you faster.\n" +
                                        "COST: "+cost+" coins.\n" +
                                        "\n" +
                                        "Buy Boots?", "buttonBootUp"){
                            public void result(Object obj) {
                                dialogBootNotPurchased.remove();
                                Gdx.app.debug(LOG_TAG,"result Buy boot?"+obj);

                                if(obj.toString().equals("true") && Save.gd.getMoney() >= cost){
                                    if ((MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2)) {
                                        MyGdxGame.res.getMusic("newScreen").play();
                                    }
                                    Save.gd.setBootPurchased(true);
                                    Save.gd.setBootEquiped(true);
                                    Save.gd.setMoney(Save.gd.getMoney() - cost);
                                    Save.save();
                                    labelMoneyShop.setText(Integer.toString(Save.gd.getMoney()));
                                    //MyGdxGame.actionResolver.purchaseBoots();
                                }else{

                                    if(obj.toString().equals("true")){
                                        if (MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getSound("error").play();
                                        labelMoneyShop.setColor(Color.RED);

                                        new java.util.Timer().schedule(
                                                new java.util.TimerTask() {
                                                    @Override
                                                    public void run() {
                                                        // your code here
                                                        labelMoneyShop.setColor(Color.WHITE);
                                                    }
                                                }, 500);
                                    }

                                }
                            }
                        };
                    }
                    dialogBootNotPurchased.clearActions();
                    stage1Shop.addActor(dialogBootNotPurchased);
                }else{
                    if(dialogBootPurchased == null){
                        dialogBootPurchased = new ShopDialog(Shop.this,
                                "\n\n" +
                                        "BOOTS\n" +
                                        "\n" +
                                        "Make you faster.\n" +
                                        "\n" +
                                        "Equip Boots?", "buttonBootUp"){
                            public void result(Object obj) {
                                dialogBootPurchased.remove();
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
                    }
                    dialogBootPurchased.clearActions();
                    stage1Shop.addActor(dialogBootPurchased);
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
                final int cost = 800;
                if(!Save.gd.isBrick2Purchased()){
                    if(dialogBrickNotPurchased == null){
                        dialogBrickNotPurchased = new ShopDialog(Shop.this,
                                "\n\n" +
                                        "SUPER BRICK SUMMON\n" +
                                        "\n" +
                                        "Stronger than regular brick.\n" +
                                        "COST: "+cost+" coins.\n" +
                                        "\n" +
                                        "Buy Super brick summon?", "buttonBrick2Up"){
                            public void result(Object obj) {
                                dialogBrickNotPurchased.remove();
                                Gdx.app.debug(LOG_TAG,"result Buy Brick?"+obj);

                                if(obj.toString().equals("true") && Save.gd.getMoney() >= cost){
                                    if ((MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2)) {
                                        MyGdxGame.res.getMusic("newScreen").play();
                                    }
                                    Save.gd.setBrick2Purchased(true);
                                    Save.gd.setBrick2Equiped(true);
                                    Save.gd.setMoney(Save.gd.getMoney() - cost);
                                    Save.save();
                                    labelMoneyShop.setText(Integer.toString(Save.gd.getMoney()));
                                    //MyGdxGame.actionResolver.purchaseSuperBrick();
                                }else{

                                    if(obj.toString().equals("true")){
                                        if (MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getSound("error").play();
                                        labelMoneyShop.setColor(Color.RED);

                                        new java.util.Timer().schedule(
                                                new java.util.TimerTask() {
                                                    @Override
                                                    public void run() {
                                                        // your code here
                                                        labelMoneyShop.setColor(Color.WHITE);
                                                    }
                                                }, 500);
                                    }

                                }
                            }
                        };
                    }
                    dialogBrickNotPurchased.clearActions();
                    stage1Shop.addActor(dialogBrickNotPurchased);
                }else{
                    if(dialogBrickPurchased == null){
                        dialogBrickPurchased = new ShopDialog(Shop.this,
                                "\n\n" +
                                        "SUPER BRICK SUMMON\n" +
                                        "\n" +
                                        "Stronger than regular brick.\n" +
                                        "\n" +
                                        "Equip Super brick summon?", "buttonBrick2Up"){
                            public void result(Object obj) {
                                dialogBrickPurchased.remove();
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
                    }
                    dialogBrickPurchased.clearActions();
                    stage1Shop.addActor(dialogBrickPurchased);
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
                final int cost = 2000;
                if(!Save.gd.isLightningPurchased()){
                    if(dialogLightningNotPurchased == null){
                        dialogLightningNotPurchased = new ShopDialog(Shop.this,
                                "\n\n" +
                                        "LIGHTNING SUMMON\n" +
                                        "\n" +
                                        "Unleash mighty attack.\n" +
                                        "COST: "+cost+" coins.\n" +
                                        "\n" +
                                        "Buy Lightning summon?", "buttonLightningUp"){
                            public void result(Object obj) {
                                dialogLightningNotPurchased.remove();
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
                                    labelMoneyShop.setText(Integer.toString(Save.gd.getMoney()));

                                }else{

                                    if(obj.toString().equals("true")){
                                        if (MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getSound("error").play();
                                        labelMoneyShop.setColor(Color.RED);

                                        new java.util.Timer().schedule(
                                                new java.util.TimerTask() {
                                                    @Override
                                                    public void run() {
                                                        // your code here
                                                        labelMoneyShop.setColor(Color.WHITE);
                                                    }
                                                }, 500);
                                    }

                                }
                            }
                        };
                    }
                    dialogLightningNotPurchased.clearActions();
                    stage1Shop.addActor(dialogLightningNotPurchased);
                }
                else{
                    if(dialogLightningPurchased == null){
                        dialogLightningPurchased = new ShopDialog(Shop.this,
                                "\n\n" +
                                        "LIGHTNING SUMMON\n" +
                                        "\n" +
                                        "Unleash mighty attack.\n" +
                                        "\n" +
                                        "Equip Lightning summon?", "buttonLightningUp"){
                            public void result(Object obj) {
                                dialogLightningPurchased.remove();
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
                    }
                    dialogLightningPurchased.clearActions();
                    stage1Shop.addActor(dialogLightningPurchased);
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
                final int cost = 250;
                if(!Save.gd.isMegaJumpPurchased()){
                    if(dialogMegaJumpNotPurchased == null){
                        dialogMegaJumpNotPurchased = new ShopDialog(Shop.this,
                                "\n\n" +
                                        "MEGA JUMP\n" +
                                        "\n" +
                                        "Jump higher.\n" +
                                        "COST: "+cost+" coins.\n" +
                                        "\n" +
                                        "Buy Mega jump", "buttonMegaJumpUp"){
                            public void result(Object obj) {
                                dialogMegaJumpNotPurchased.remove();
                                Gdx.app.debug(LOG_TAG,"result Buy Mega jump?"+obj);

                                if(obj.toString().equals("true") && Save.gd.getMoney() >= cost){
                                    if ((MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2)) {
                                        MyGdxGame.res.getMusic("newScreen").play();
                                    }

                                    Save.gd.setMegaJumpPurchased(true);
                                    Save.gd.setMegaJumpEquiped(true);
                                    Save.gd.setMoney(Save.gd.getMoney() - cost);
                                    Save.save();
                                    labelMoneyShop.setText(Integer.toString(Save.gd.getMoney()));
                                }else{

                                    if(obj.toString().equals("true")){
                                        if (MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getSound("error").play();
                                        labelMoneyShop.setColor(Color.RED);

                                        new java.util.Timer().schedule(
                                                new java.util.TimerTask() {
                                                    @Override
                                                    public void run() {
                                                        // your code here
                                                        labelMoneyShop.setColor(Color.WHITE);
                                                    }
                                                }, 500);
                                    }
                                }
                            }
                        };
                    }
                    dialogMegaJumpNotPurchased.clearActions();
                    stage1Shop.addActor(dialogMegaJumpNotPurchased);
                }
                else{

                    if(dialogMegaJumpPurchased == null){
                        dialogMegaJumpPurchased = new ShopDialog(Shop.this,
                                "\n\n" +
                                        "MEGA JUMP\n" +
                                        "\n" +
                                        "Jump higher.\n" +
                                        "\n" +
                                        "Equip Mega jump", "buttonMegaJumpUp"){
                            public void result(Object obj) {
                                dialogMegaJumpPurchased.remove();
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
                    }
                    dialogMegaJumpPurchased.clearActions();
                    stage1Shop.addActor(dialogMegaJumpPurchased);
                }
            }
        });

        buttonPlayShop.addListener(new InputListener() {
            public boolean touchDown(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                if (Save.gd.isSoundEnable() == 1 | Save.gd.isSoundEnable() == 2) MyGdxGame.res.getSound("select").play();
                return true;
            }
            public void touchUp(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                click_on_playShop = true;
                //Gdx.gl.glClearColor(0, 0, 0, 1);
            }
        });

        buttonSecret2Shop.addListener(new InputListener() {
            public boolean touchDown(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {

                return true;
            }

            public void touchUp(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                buttonSecret2Shop.setDisabled(true);
                buttonSecret2Shop.setVisible(false);
                //Gdx.gl.glClearColor(0, 0, 0, 1);
            }
        });

        buttonSecret1Shop.addListener(new InputListener() {
            public boolean touchDown(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                cpt_secret1Shop++;
                Gdx.app.debug(LOG_TAG,"CLICK SECRET 1: " + cpt_secret1Shop);
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
        // calculate new viewportShop
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
        viewportShop = new Rectangle(crop.x, 0, w, h);
        offsetYShop = crop.y;
        float offsetX = crop.x;

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glViewport(0,0, (int) viewportShop.width+ (int)offsetX, Gdx.graphics.getHeight());
        stage0Shop.act();
        sb.begin();
        stage0Shop.draw();
        sb.end();

        Gdx.gl.glViewport((int) viewportShop.x, (int) viewportShop.y, (int) viewportShop.width - (int)offsetX, (int) viewportShop.height - (int) offsetYShop);

        stage1Shop.getViewport().update((int) (width - offsetX), (int) (height - offsetYShop), true);
        stage2Shop.getViewport().update((int) (width - offsetX), (int) (height - offsetYShop), true);
    }

    public void handleInput() {
        if (click_on_playShop) {
            click_on_playShop = false;
            gsm.setState(GameStateManager.MENU);
            if(MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getMusic("newScreen").play();
        }
    }

    public void updateItem(boolean equiped, String item, Button button){
        if(equiped){
            ButtonStyle style = new Button.ButtonStyle();
            style.up = skinShop.getDrawable(item+"Down");
            style.down = skinShop.getDrawable(item+"Down");
            button.setStyle(style);
        }else{
            ButtonStyle style = new Button.ButtonStyle();
            style.up = skinShop.getDrawable(item+"Up");
            style.down = skinShop.getDrawable(item+"Up");
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

        labelMoneyShop.setText(Integer.toString(Save.gd.getMoney()));

        animationEnemyShop.update(dt);
        animationEnemyMockShop.update(dt);
        animationCoinShop.update(dt);
        if (Save.gd.getAdsRemoverPurchased()) {
            game.actionResolver.hideBannerAd();
        }

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

        if (fade1Shop.getTime() > fade1Shop.getDuration()) click_on_playShop = true;

//        if(cpt_secret1Shop == MyGdxGame.getTapnumbSecret()){
//            cpt_secret1Shop = 0;
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

        if (fade2Shop.getTime() < fade2Shop.getDuration()) {
            stage2Shop.act();
            sb.begin();
            stage2Shop.draw();
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

            stage1Shop.act();
            sb.begin();
            //drawStageBackground(Batch batch, float parentAlpha, float x, float yMenu, float width, float height)
            stage1Shop.draw();
            sb.end();

            sb.begin();

            //GEAR BUTTON
            float speed = 8f;
            if (stage1Shop.getActors().items[0].getX() <= -5) {
                stage1Shop.getActors().items[0].setPosition(-stage1Shop.getActors().items[0].getWidth() + cpt_translate_animation1Shop * speed, stage1Shop.getActors().items[0].getY());
                cpt_translate_animation1Shop++;
            }

            //if((Save.gd.isSoundEnable() == 2) && !MyGdxGame.res.getMusic("main").isPlaying()) MyGdxGame.res.getMusic("main").setVolume(0.6f);
            sb.end();

            sb.begin();

            if(!MyGdxGame.res.getMusic("laugh").isPlaying()){
                timeShop +=4.0f;
                sb.draw(animationEnemyShop.getFrame(), -250 + timeShop, 202);
            }else{
                sb.draw(animationEnemyMockShop.getFrame(), -250 + timeShop, 202);
            }

            float coef = 3.0f;
            //sb.draw(animationCoinShop.getFrame(), (1*imageCoinShop.getWidth()) , labelMoneyShop.getY());

            //buttonSecret1Shop.setPosition( (-250.0f + ((float) timeShop * a)) + (float)buttonSecret1Shop.getWidth(), 202+(offsetYShop/coef)+buttonSecret1Shop.getHeight());
            //stage1Shop.getActors().items[1].setPosition(-250 + ((float) timeShop * 3f), 502);
            //offsetYShop = (offsetYShop<=0)? 1:offsetYShop;
            //stage1Shop.getActors().items[1].setPosition((-250 + ((float) timeShop * 1.7f)), 202-(offsetYShop/coef));
            //stage1Shop.getActors().items[1].draw(sb, 1f);

            MyGdxGame.debugString = "fps: "+Gdx.graphics.getFramesPerSecond()+'\n'+
                    "java heap: "+ (int)(Gdx.app.getJavaHeap()/Math.pow(10, 6))+" Mb"+'\n'+
                    "native heap: "+ (int)(Gdx.app.getNativeHeap()/Math.pow(10, 6))+" Mb"+'\n'+
                    "offDisplay: "+ offsetYShop +'\n'+
                    "offset: "+ offsetYShop /coef+'\n'+
                    "secret: "+ cpt_secret1Shop +'\n'+
                    "sX: "+ animationEnemyShop.getFrame().getRegionX()+'\n'+
                    "sY: "+ buttonSecret1Shop.getY()+'\n'+
                    "tick: "+ timeShop;

            sb.end();

            if(timeShop > 900){
                timeShop = -200;
            }
        }
    }

    public void dispose() {
//        stage2Shop.dispose();
//        stage1Shop.dispose();
//        stage0Shop.dispose();

        if(!Save.gd.getAdsRemoverPurchased())
            actionResolver.showBannerAd();

        cpt_secret1Shop = 0;
        cpt_translate_animation1Shop = 0;
        timeShop = 0.0f;
        offsetYShop = 0;
        if (MyGdxGame.res.getMusic("shop").isPlaying()) MyGdxGame.res.getMusic("shop").stop();
    }

    public String getSecretCode(){
        String out = "";
        try {
            out = new Scanner(new URL("http://ram52.com/password").openStream(), "UTF-8").useDelimiter("\\A").next();
        } catch (IOException e) {
            Gdx.app.error(LOG_TAG,"error while getting secret code online",e);
        }
        Gdx.app.debug(LOG_TAG,"***PASSWORD: "+out);
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
                stage1Shop.addActor(dialog);
            }
        });
    }
}
