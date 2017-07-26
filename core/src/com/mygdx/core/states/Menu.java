package com.mygdx.core.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;
import com.mygdx.core.MyGdxGame;
import com.mygdx.core.handlers.Animation;
import com.mygdx.core.handlers.GameStateManager;
import com.mygdx.core.handlers.Save;

import static com.mygdx.core.MyGdxGame.PBlueMenu;
import static com.mygdx.core.MyGdxGame.PRedMenu;
import static com.mygdx.core.MyGdxGame.PYellowMenu;
import static com.mygdx.core.MyGdxGame.actionResolver;
import static com.mygdx.core.MyGdxGame.animPlayerIdle;
import static com.mygdx.core.MyGdxGame.animPlayerIdleFliped;
import static com.mygdx.core.MyGdxGame.animPrincessIdle;
import static com.mygdx.core.MyGdxGame.animPrincessIdleFliped;
import static com.mygdx.core.MyGdxGame.animTitle;
import static com.mygdx.core.MyGdxGame.blinkMenu;
import static com.mygdx.core.MyGdxGame.blinking_text_alphaMenu;
import static com.mygdx.core.MyGdxGame.buttonAchievement;
import static com.mygdx.core.MyGdxGame.buttonBackImage;
import static com.mygdx.core.MyGdxGame.buttonCloseUiOption;
import static com.mygdx.core.MyGdxGame.buttonCreditsMenu;
import static com.mygdx.core.MyGdxGame.buttonFacebook;
import static com.mygdx.core.MyGdxGame.buttonGearMenu;
import static com.mygdx.core.MyGdxGame.buttonLeaderBoardMenu;
import static com.mygdx.core.MyGdxGame.buttonPBlueMenu;
import static com.mygdx.core.MyGdxGame.buttonPGreen;
import static com.mygdx.core.MyGdxGame.buttonPRedMenu;
import static com.mygdx.core.MyGdxGame.buttonPYellow;
import static com.mygdx.core.MyGdxGame.buttonPlayMenu;
import static com.mygdx.core.MyGdxGame.buttonSoundMenu;
import static com.mygdx.core.MyGdxGame.buttonStore;
import static com.mygdx.core.MyGdxGame.buttonTutoMenu;
import static com.mygdx.core.MyGdxGame.buttonTwitter;
import static com.mygdx.core.MyGdxGame.click_on_creditsMenu;
import static com.mygdx.core.MyGdxGame.click_on_leaderboardMenu;
import static com.mygdx.core.MyGdxGame.click_on_playMenu;
import static com.mygdx.core.MyGdxGame.click_on_shopMenu;
import static com.mygdx.core.MyGdxGame.click_on_tutoMenu;
import static com.mygdx.core.MyGdxGame.cpt_blinkMenu;
import static com.mygdx.core.MyGdxGame.cpt_translate_animation1Menu;
import static com.mygdx.core.MyGdxGame.cpt_translate_animation2Menu;
import static com.mygdx.core.MyGdxGame.cpt_translate_animationMenu;
import static com.mygdx.core.MyGdxGame.im;
import static com.mygdx.core.MyGdxGame.imageCoinMenu;
import static com.mygdx.core.MyGdxGame.introMenu;
import static com.mygdx.core.MyGdxGame.isfadeOutStarted;
import static com.mygdx.core.MyGdxGame.labelCopyright;
import static com.mygdx.core.MyGdxGame.labelMoneyMenu;
import static com.mygdx.core.MyGdxGame.lastPlayerPosition;
import static com.mygdx.core.MyGdxGame.left2Menu;
import static com.mygdx.core.MyGdxGame.leftMenu;
import static com.mygdx.core.MyGdxGame.loadingMenu;
import static com.mygdx.core.MyGdxGame.optionUiBackImage;
import static com.mygdx.core.MyGdxGame.playerIsTouchedMenu;
import static com.mygdx.core.MyGdxGame.posX2Menu;
import static com.mygdx.core.MyGdxGame.posXMenu;
import static com.mygdx.core.MyGdxGame.right2Menu;
import static com.mygdx.core.MyGdxGame.rightMenu;
import static com.mygdx.core.MyGdxGame.sb2Menu;
import static com.mygdx.core.MyGdxGame.sb3Menu;
import static com.mygdx.core.MyGdxGame.skinMenu;
import static com.mygdx.core.MyGdxGame.stage0Menu;
import static com.mygdx.core.MyGdxGame.stage1Menu;
import static com.mygdx.core.MyGdxGame.stage2Menu;
import static com.mygdx.core.MyGdxGame.stage3Menu;
import static com.mygdx.core.MyGdxGame.stageUiOptionMenu;
import static com.mygdx.core.MyGdxGame.start2Menu;
import static com.mygdx.core.MyGdxGame.viewportMenu;
import static com.mygdx.core.MyGdxGame.jp;
import static com.mygdx.core.MyGdxGame.yMenu;
import static com.mygdx.core.MyGdxGame.animHelpMe;
import static com.mygdx.core.MyGdxGame.animPlayerSlash;
import static com.mygdx.core.MyGdxGame.cptMenu;

public class Menu extends GameState {

    private static String LOG_TAG = Menu.class.getSimpleName();

    public Menu(final GameStateManager gsm) {

        super(gsm);
        if(MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) {
            if(MyGdxGame.res.getMusic("newScreen").isPlaying()){
                MyGdxGame.res.getMusic("newScreen").pause();
                MyGdxGame.res.getMusic("newScreen").stop();
            }
            MyGdxGame.res.getMusic("newScreen").play();
        }

        if(lastPlayerPosition == null){
            lastPlayerPosition = new Vector2(0,0);
        }else {
            lastPlayerPosition.set(0,0);
        }

        game.actionResolver.hideBannerAd();

        MyGdxGame.initFade();
        Timer.instance().start();



        if(introMenu == null){
            introMenu = new Image(MyGdxGame.atlas.findRegion("backgroundSky"));
            introMenu.setFillParent(true);
            stage0Menu = new Stage();
            stage0Menu.addActor(introMenu);
        }


        Timer.instance().stop();
        Timer.instance().clear();
        if(MyGdxGame.res.getMusic("on").isPlaying())MyGdxGame.res.getMusic("on").stop();
        updateSelector();

        Save.load();
//        if (!Save.gd.getAdsRemoverPurchased()) {
//            String network = game.actionResolver.getNetworkClass();
//            if(network == null) network = "ABSENT";
//            Gdx.app.debug(LOG_TAG,"NETWORK: "+network);
//            if(network.equals("4G")|network.equals("3G")|network.equals("WIFI"))
//                game.actionResolver.showOrLoadBanner();
//        }

        click_on_playMenu = false;
        click_on_leaderboardMenu = false;

        if(viewportMenu == null){
            viewportMenu = new Rectangle();
            sb2Menu = new SpriteBatch();
            sb3Menu = new SpriteBatch();
        }


        MyGdxGame.background_cloud.setVector(+10, 0);
        //MyGdxGame.background_skyDay.setVector(0, 0);
        MyGdxGame.background_wood1.setVector(-3, 0);


        if(animPlayerIdle == null){
            Sprite tex = null;
            if(Save.gd.isExcaliburEquiped()){
                tex = new Sprite(MyGdxGame.atlas.findRegion("walkEx"));
            }else{
                tex = new Sprite(MyGdxGame.atlas.findRegion("walk"));
            }
            TextureRegion[] sprites2 = tex.split(64, 64)[0];
            animPlayerIdle = new Animation(sprites2, 1 / 5f);

            TextureRegion[] sprites2Fliped = tex.split(64, 64)[0];
            for (int i = 0; i < sprites2Fliped.length; i++)
                sprites2Fliped[i].flip(true, false);
            animPlayerIdleFliped = new Animation(sprites2Fliped, 1 / 5f);


            animPrincessIdle = new Animation(new Sprite(MyGdxGame.atlas.findRegion("princesscry")).split(64, 64)[0], 1 / 5f);

            animTitle = new Animation(new Sprite(MyGdxGame.atlas.findRegion("princess!")).split(111, 44)[0], 1 / 5f);

            Gdx.app.debug(LOG_TAG,"LANG-->"+java.util.Locale.getDefault().toString());
            if(java.util.Locale.getDefault().toString().equals("ja_JP")){
                jp = true;
            }else{
                jp = false;
            }

            if(jp){
                animTitle = new Animation(new Sprite(MyGdxGame.atlas.findRegion("princessjp")).split(153, 52)[0], 1 / 5f);
            }


            animHelpMe = new Animation(new Sprite(MyGdxGame.atlas.findRegion("protectme")).split(123, 75)[0], 1 / 5f);

            TextureRegion[] princessFliped = new Sprite(MyGdxGame.atlas.findRegion("princesscry")).split(64, 64)[0];
            for (int i = 0; i < princessFliped.length; i++)
                princessFliped[i].flip(true, false);
            animPrincessIdleFliped = new Animation(princessFliped, 1 / 5f);


            tex = new Sprite(MyGdxGame.atlas.findRegion("slash"));
            TextureRegion[] sprites6 = tex.split(96, 64)[0];
            animPlayerSlash = new Animation(sprites6, 1 / 10f);

            cam.setToOrtho(false, MyGdxGame.V_WIDTH, MyGdxGame.V_HEIGHT);

            skinMenu = new Skin();
            skinMenu.addRegions(MyGdxGame.atlas);
        }


        if(stage1Menu == null){
            stage1Menu = new Stage();
            stage2Menu = new Stage();
            stage3Menu = new Stage();
            stageUiOptionMenu = new Stage();

            stage1Menu.addAction(Actions.sequence(Actions.alpha(0.5f), Actions.fadeIn(1.5f)));
            stage2Menu.addAction(Actions.sequence(Actions.alpha(0.5f), Actions.fadeIn(1.5f)));
            stage3Menu.addAction(Actions.sequence(Actions.alpha(0.5f), Actions.fadeIn(1.5f)));
            stageUiOptionMenu.addAction(Actions.sequence(Actions.alpha(0.5f), Actions.fadeIn(0.1f)));

            ButtonStyle style = new ButtonStyle();
            style = new Button.ButtonStyle();
            style.up = skinMenu.getDrawable("coin");
            style.down = skinMenu.getDrawable("coin");
            imageCoinMenu = new Button(style);
            imageCoinMenu.setWidth(Gdx.graphics.getWidth() / 10f);
            imageCoinMenu.setHeight(Gdx.graphics.getWidth() / 10f);
            imageCoinMenu.setPosition(imageCoinMenu.getWidth() / 4, (Gdx.graphics.getHeight() - imageCoinMenu.getHeight() - 20));
            stageUiOptionMenu.addActor(imageCoinMenu);

            labelMoneyMenu = new Label("0", new LabelStyle(new BitmapFont(Gdx.files.internal(MyGdxGame.fontPointPath), false), Color.WHITE));
            labelMoneyMenu.setFontScale(Gdx.graphics.getWidth() / 1200f);
            labelMoneyMenu.setWidth(Gdx.graphics.getWidth() / 2);
            labelMoneyMenu.setHeight(imageCoinMenu.getHeight());
            labelMoneyMenu.setAlignment(Align.center | Align.left);
            labelMoneyMenu.setPosition(imageCoinMenu.getRight() * 1.1f, imageCoinMenu.getY());
            stageUiOptionMenu.addActor(labelMoneyMenu);

            style = new ButtonStyle();
            style.up = skinMenu.getDrawable("coin");
            style.down = skinMenu.getDrawable("coin");
            optionUiBackImage = new Button(style);
            optionUiBackImage.setWidth(Gdx.graphics.getWidth() / 1.05f);
            optionUiBackImage.setHeight(Gdx.graphics.getHeight() / 1.25f);
            optionUiBackImage.setPosition((Gdx.graphics.getWidth() - optionUiBackImage.getWidth()) / 2, (Gdx.graphics.getHeight() - optionUiBackImage.getHeight()) / 2f);
            stageUiOptionMenu.addActor(optionUiBackImage);


            style = new ButtonStyle();
            style.up = skinMenu.getDrawable("coin");
            style.down = skinMenu.getDrawable("coin");
            buttonBackImage = new Button(style);
            buttonBackImage.setWidth(Gdx.graphics.getHeight() / 7f);
            buttonBackImage.setHeight(Gdx.graphics.getHeight() / 7f);
            buttonBackImage.setPosition((Gdx.graphics.getWidth() - buttonBackImage.getWidth()) / 2, (Gdx.graphics.getHeight() - buttonBackImage.getHeight()) / 1.28f);
            stageUiOptionMenu.addActor(buttonBackImage);

            style = new ButtonStyle();
            style.up = skinMenu.getDrawable("buttonUiOptionFacebookUp");
            style.down = skinMenu.getDrawable("buttonUiOptionFacebookDown");
            buttonFacebook = new Button(style);
            buttonFacebook.setWidth(buttonBackImage.getWidth());
            buttonFacebook.setHeight(buttonBackImage.getHeight());
            buttonFacebook.setPosition((buttonBackImage.getX() - buttonFacebook.getWidth() - buttonBackImage.getWidth()/20f ) , (Gdx.graphics.getHeight() - buttonFacebook.getHeight()) / 1.28f);
            stageUiOptionMenu.addActor(buttonFacebook);

            style = new ButtonStyle();
            style.up = skinMenu.getDrawable("buttonUiOptionTwitterUp");
            style.down = skinMenu.getDrawable("buttonUiOptionTwitterDown");
            buttonTwitter = new Button(style);
            buttonTwitter.setWidth(buttonBackImage.getWidth());
            buttonTwitter.setHeight(buttonBackImage.getHeight());
            buttonTwitter.setPosition((buttonBackImage.getRight() + buttonBackImage.getWidth()/20f ) , (Gdx.graphics.getHeight() - buttonTwitter.getHeight()) / 1.28f);
            stageUiOptionMenu.addActor(buttonTwitter);


            style = new ButtonStyle();
            style.up = skinMenu.getDrawable("coin");
            style.down = skinMenu.getDrawable("coin");
            buttonStore = new Button(style);
            buttonStore.setWidth(Gdx.graphics.getWidth() / 1.26f);
            buttonStore.setHeight(Gdx.graphics.getHeight() / 7f);
            buttonStore.setPosition((Gdx.graphics.getWidth() - buttonStore.getWidth())/2f , buttonBackImage.getY() - buttonBackImage.getHeight() * 1.69f);
            stageUiOptionMenu.addActor(buttonStore);

            style = new ButtonStyle();
            style.up = skinMenu.getDrawable("buttonUiOptionAchievementUp");
            style.down = skinMenu.getDrawable("buttonUiOptionAchievementDown");
            buttonAchievement = new Button(style);
            buttonAchievement.setWidth(buttonStore.getWidth());
            buttonAchievement.setHeight(buttonStore.getHeight());
            buttonAchievement.setPosition(buttonStore.getX(), buttonStore.getY() - buttonStore.getHeight() * 1.69f);
            stageUiOptionMenu.addActor(buttonAchievement);

            style = new ButtonStyle();
            style.up = skinMenu.getDrawable("buttonPlayUp");
            style.down = skinMenu.getDrawable("buttonPlayUp");
            buttonCloseUiOption = new Button(style);
            buttonCloseUiOption.setWidth(Gdx.graphics.getWidth() / 6f);
            buttonCloseUiOption.setHeight(Gdx.graphics.getWidth() / 6f);
            buttonCloseUiOption.setPosition((Gdx.graphics.getWidth() - buttonCloseUiOption.getWidth()) / 1.01f, (Gdx.graphics.getHeight() - buttonCloseUiOption.getHeight()) / 1.1f);
            stageUiOptionMenu.addActor(buttonCloseUiOption);

            style = new ButtonStyle();
            style.up = skinMenu.getDrawable("buttonPlayUp");
            style.down = skinMenu.getDrawable("buttonPlayDown");
            buttonPlayMenu = new Button(style);
            buttonPlayMenu.setWidth(Gdx.graphics.getWidth() / 2.2f);
            buttonPlayMenu.setHeight(Gdx.graphics.getHeight() / 5f);
            //buttonExit.setPosition(-buttonExit.getWidth()/2.6f, (Gdx.graphics.getHeight() - buttonExit.getHeight()) / 1.6f);

            buttonPlayMenu.setPosition(-400, Gdx.graphics.getHeight() / 2f);
            stage1Menu.addActor(buttonPlayMenu);

            style = new ButtonStyle();
            style.up = skinMenu.getDrawable("buttonLeaderBoardUp");
            style.down = skinMenu.getDrawable("buttonLeaderBoardDown");
            buttonLeaderBoardMenu = new Button(style);
            buttonLeaderBoardMenu.setWidth(Gdx.graphics.getWidth() / 2.2f);
            buttonLeaderBoardMenu.setHeight(Gdx.graphics.getHeight() / 5f);
            buttonLeaderBoardMenu.setPosition((Gdx.graphics.getWidth() / 1f) - buttonLeaderBoardMenu.getWidth() + 20, (Gdx.graphics.getHeight() - buttonPlayMenu.getHeight()) / 1.6f);
            stage1Menu.addActor(buttonLeaderBoardMenu);


            style = new ButtonStyle();
            style.up = skinMenu.getDrawable("buttonTutoUp");
            style.down = skinMenu.getDrawable("buttonTutoDown");
            buttonTutoMenu = new Button(style);
            buttonTutoMenu.setVisible(true);
            buttonTutoMenu.setWidth(Gdx.graphics.getWidth() / 1.7f);
            buttonTutoMenu.setHeight(Gdx.graphics.getHeight() / 13f);
            buttonTutoMenu.setPosition((Gdx.graphics.getWidth() / 2) - buttonTutoMenu.getWidth() / 1.26f, 0);
            stage1Menu.addActor(buttonTutoMenu);

            labelCopyright = new Label("(c) RAM52 Credits 2017", new LabelStyle(new BitmapFont( Gdx.files.internal(MyGdxGame.fontScorePath), false), Color.WHITE));
            labelCopyright.setWidth(Gdx.graphics.getWidth() / 2.8f);
            labelCopyright.setHeight(Gdx.graphics.getHeight() / 20);
            labelCopyright.setFontScale(Gdx.graphics.getWidth() / 500f);
            labelCopyright.setAlignment(Align.center);
            labelCopyright.setPosition(0, -500);
            stage1Menu.addActor(labelCopyright);


            style = new ButtonStyle();
            style.up = skinMenu.getDrawable("buttonSound2Full");
            style.down = skinMenu.getDrawable("buttonSound2Full");
            buttonSoundMenu = new Button(style);
            buttonSoundMenu.setWidth(Gdx.graphics.getWidth() / 5f);
            buttonSoundMenu.setHeight(Gdx.graphics.getHeight() / 7.8f);
            buttonSoundMenu.setPosition((Gdx.graphics.getWidth() / 1f) - buttonSoundMenu.getWidth() + 20, Gdx.graphics.getHeight() / 100f);

            if(MyGdxGame.isSoundEnable() == 0){
                style = new ButtonStyle();
                style.up = skinMenu.getDrawable("buttonSound2Mute");
                style.down = skinMenu.getDrawable("buttonSound2Mute");
                buttonSoundMenu.setStyle(style);
            }
            if(MyGdxGame.isSoundEnable() == 1){
                style = new ButtonStyle();
                style.up = skinMenu.getDrawable("buttonSound2Fx");
                style.down = skinMenu.getDrawable("buttonSound2Fx");
                buttonSoundMenu.setStyle(style);
            }
            if(MyGdxGame.isSoundEnable() == 2){
                style = new ButtonStyle();
                style.up = skinMenu.getDrawable("buttonSound2Full");
                style.down = skinMenu.getDrawable("buttonSound2Full");
                buttonSoundMenu.setStyle(style);
            }
            stage1Menu.addActor(buttonSoundMenu);


            style = new ButtonStyle();
            style.up = skinMenu.getDrawable("buttonPlayUp");
            style.down = skinMenu.getDrawable("buttonPlayUp");
            buttonCreditsMenu = new Button(style);
            buttonCreditsMenu.setWidth(0);
            buttonCreditsMenu.setHeight(0);
            buttonCreditsMenu.setPosition(Gdx.graphics.getWidth() / 2 - buttonCreditsMenu.getWidth() / 2, Gdx.graphics.getHeight() / 2 - buttonCreditsMenu.getHeight() / 2);
            buttonCreditsMenu.setVisible(false);
            stage1Menu.addActor(buttonCreditsMenu);

            loadingMenu = new Image(MyGdxGame.atlas.findRegion("buttonSecret"));
            loadingMenu.setWidth(Gdx.graphics.getWidth());
            loadingMenu.setHeight(Gdx.graphics.getHeight() / 12f);
            loadingMenu.setPosition(0, (Gdx.graphics.getHeight() - loadingMenu.getHeight()) / 2f);
            //loadingMenu.setVisible(true);
            loadingMenu.setScale(0);
            stage1Menu.addActor(loadingMenu);


            style = new ButtonStyle();
            style.up = skinMenu.getDrawable("buttonSound2Full");
            style.down = skinMenu.getDrawable("buttonSound2Full");
            buttonPBlueMenu = new Button(style);
            buttonPBlueMenu.setWidth(Gdx.graphics.getWidth() / 8f);
            buttonPBlueMenu.setHeight(Gdx.graphics.getWidth() / 4f);
            buttonPBlueMenu.setPosition((Gdx.graphics.getWidth() - buttonPBlueMenu.getWidth()) / 4f, (Gdx.graphics.getHeight() - buttonPBlueMenu.getWidth()) / 4.3f);

            buttonPRedMenu = new Button(style);
            buttonPRedMenu.setWidth(Gdx.graphics.getWidth() / 8f);
            buttonPRedMenu.setHeight(Gdx.graphics.getWidth() / 4f);
            buttonPRedMenu.setPosition((Gdx.graphics.getWidth() - buttonPBlueMenu.getWidth()) / 2.1f, (Gdx.graphics.getHeight() - buttonPBlueMenu.getWidth()) / 4.3f);

            buttonPYellow = new Button(style);
            buttonPYellow.setWidth(Gdx.graphics.getWidth() / 8f);
            buttonPYellow.setHeight(Gdx.graphics.getWidth() / 4f);
            buttonPYellow.setPosition((Gdx.graphics.getWidth() - buttonPBlueMenu.getWidth()) / 1.7f, (Gdx.graphics.getHeight() - buttonPBlueMenu.getWidth()) / 4.3f);

            buttonPGreen = new Button(style);
            buttonPGreen.setWidth(Gdx.graphics.getWidth() / 8f);
            buttonPGreen.setHeight(Gdx.graphics.getWidth() / 4f);
            buttonPGreen.setPosition((Gdx.graphics.getWidth() - buttonPBlueMenu.getWidth()) / 1.4f, (Gdx.graphics.getHeight() - buttonPBlueMenu.getWidth()) / 4.3f);

            stage1Menu.addActor(buttonPBlueMenu);
            stage1Menu.addActor(buttonPRedMenu);
            stage1Menu.addActor(buttonPYellow);
            stage1Menu.addActor(buttonPGreen);

            style = new ButtonStyle();
            style.up = skinMenu.getDrawable("buttonGearUp");
            style.down = skinMenu.getDrawable("buttonGearDown");
            buttonGearMenu = new Button(style);
            buttonGearMenu.setWidth(Gdx.graphics.getWidth() / 5f);
            buttonGearMenu.setHeight(Gdx.graphics.getHeight() / 7.8f);
            buttonGearMenu.setPosition(-400, Gdx.graphics.getHeight() / 100f);
            stage1Menu.addActor(buttonGearMenu);

            setupButtonMenu();
        }










//        if(MyGdxGame.isSoundEnable() == 2) {
//            MyGdxGame.res.getMusic("main").setVolume(1f);
//            if(!MyGdxGame.res.getMusic("main").isPlaying())
//                MyGdxGame.res.getMusic("main").play();
//        }


        im.addProcessor(stageUiOptionMenu);
        im.addProcessor(stage1Menu);
        Gdx.input.setInputProcessor(im);

        //MyGdxGame.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),stages);

        //Gdx.input.setInputProcessor(stageUiOptionMenu);
        MyGdxGame.setIsBoosTerritory(false);

    }

    public void setupButtonMenu(){
        buttonGearMenu.addListener(new InputListener() {
            public boolean touchDown(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                if (MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getSound("select").play();
                Gdx.app.debug(LOG_TAG,"bt gear pressed!");

                return true;
            }

            ;

            public void touchUp(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {

                Gdx.app.debug(LOG_TAG,"bt gear pressed!");
                click_on_shopMenu = true;
//                stageUiOptionMenu.addAction(Actions.sequence(Actions.alpha(0.5f), Actions.fadeIn(0.1f)));
//                showOptionUi = true;
//                showMainUi = false;
//
//                Save.load();
//                labelMoneyMenu.setText(Integer.toString(Save.gd.getMoney()));

            }

            ;
        });

        buttonSoundMenu.addListener(new InputListener() {
            public boolean touchDown(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                if (MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getSound("select").play();
                Gdx.app.debug(LOG_TAG,"bt sound pressed!");
                if(MyGdxGame.isSoundEnable() == 2){
                    MyGdxGame.setSoundEnable(0);
                }else{
                    MyGdxGame.setSoundEnable(MyGdxGame.isSoundEnable()+1);
                }


                if (MyGdxGame.isSoundEnable() == 0) {
                    if (MyGdxGame.res.getMusic("title").isPlaying()) MyGdxGame.res.getMusic("title").pause();
                    ButtonStyle style = new ButtonStyle();
                    style.up = skinMenu.getDrawable("buttonSound2Mute");
                    style.down = skinMenu.getDrawable("buttonSound2Mute");
                    buttonSoundMenu.setStyle(style);
                }

                if (MyGdxGame.isSoundEnable() == 1) {
                    if (MyGdxGame.res.getMusic("title").isPlaying()) MyGdxGame.res.getMusic("title").pause();
                    MyGdxGame.res.getSound("select").play();
                    ButtonStyle style = new ButtonStyle();
                    style.up = skinMenu.getDrawable("buttonSound2Fx");
                    style.down = skinMenu.getDrawable("buttonSound2Fx");
                    buttonSoundMenu.setStyle(style);
                }

                if (MyGdxGame.isSoundEnable() == 2) {
                    if (!MyGdxGame.res.getMusic("title").isPlaying()) MyGdxGame.res.getMusic("title").play();
                    ButtonStyle style = new ButtonStyle();
                    style.up = skinMenu.getDrawable("buttonSound2Full");
                    style.down = skinMenu.getDrawable("buttonSound2Full");
                    buttonSoundMenu.setStyle(style);
                }

                return true;
            }
            ;

            public void touchUp(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {

            }

            ;
        });

        buttonCreditsMenu.addListener(new InputListener() {
            public boolean touchDown(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                //Gdx.app.debug(LOG_TAG,"bt pressed!");
//                if(MyGdxGame.isSoundEnable() && buttonCreditsMenu.getScaleX()>0) MyGdxGame.res.getSound("select").play();
//                click_on_creditsMenu = false;
//                click_on_playMenu = false;
//                buttonPlayMenu.setVisible(true);
//                buttonLeaderBoardMenu.setVisible(true);
//                buttonPBlueMenu.setVisible(true);
//                buttonPRedMenu.setVisible(true);
//                buttonPYellow.setVisible(true);
//                buttonPGreen.setVisible(true);

                return true;
            };

            public void touchUp(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
//                click_on_creditsMenu = false;
//                click_on_playMenu = false;
//                Gdx.app.debug(LOG_TAG,"SHOW CREDITS: " + click_on_creditsMenu);
//                Gdx.app.debug(LOG_TAG,"CLICK ON PLAY: " + click_on_playMenu);
            };
        });

        buttonPlayMenu.addListener(new InputListener() {
            public boolean touchDown(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                if(MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getSound("select").play();
                return true;
            };

            public void touchUp(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                click_on_playMenu = true;
                //Gdx.gl.glClearColor(0, 0, 0, 1);
                cptMenu = 0;
            };
        });

        buttonLeaderBoardMenu.addListener(new InputListener() {
            public boolean touchDown(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                if(MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getSound("select").play();

                return true;
            };

            public void touchUp(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                click_on_leaderboardMenu = true;
            };
        });

        buttonTutoMenu.addListener(new InputListener() {
            public boolean touchDown(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                Gdx.app.debug(LOG_TAG,"bt tuto pressed!");
                if(MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getSound("select").play();
                return true;
            };

            public void touchUp(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                click_on_tutoMenu = true;
            };
        });

        labelCopyright.addListener(new InputListener() {
            public boolean touchDown(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                Gdx.app.debug(LOG_TAG,"bt copyright pressed!");
                if(MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getSound("select").play();
//                click_on_creditsMenu =! click_on_creditsMenu;
//                if(click_on_creditsMenu) {
//                    /*buttonPBlueMenu.setScale(0);
//                    buttonPRedMenu.setScale(0);
//                    buttonPYellow.setScale(0);
//                    buttonPGreen.setScale(0);
//                    /*buttonPBlueMenu.setVisible(false);
//                    buttonPRedMenu.setVisible(false);
//                    buttonPYellow.setVisible(false);
//                    buttonPGreen.setVisible(false);
//                    buttonExit.setVisible(false);*/
//                    buttonLeaderBoardMenu.setVisible(false);
//                    buttonExit.setDisabled(false);
//                    buttonLeaderBoardMenu.setDisabled(false);
//                    loadingMenu.setScale(0);
//                    buttonCreditsMenu.setWidth(Gdx.graphics.getWidth() / 1.1f);
//                    buttonCreditsMenu.setHeight(Gdx.graphics.getHeight() / 1.2f);
//                    buttonCreditsMenu.setPosition(Gdx.graphics.getWidth() / 2 - buttonCreditsMenu.getWidth() / 2, Gdx.graphics.getHeight() / 2 - buttonCreditsMenu.getHeight() / 2);
//                }else{
//                    /*buttonPBlueMenu.setScale(1);
//                    buttonPRedMenu.setScale(1);
//                    buttonPYellow.setScale(1);
//                    buttonPGreen.setScale(1);*/
//                }
                return true;
            };

            public void touchUp(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                click_on_creditsMenu = true;
            };
        });


        buttonBackImage.addListener(new InputListener() {
            public boolean touchDown(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                Gdx.app.debug(LOG_TAG,"bt back pressed!");
                if(MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getSound("select").play();
                Gdx.app.debug(LOG_TAG,"bt backimage pressed!");


                ButtonStyle style = new ButtonStyle();
                style.up = skinMenu.getDrawable("buttonUiOptionSkyDown");
                style.down = skinMenu.getDrawable("buttonUiOptionSkyDown");
                buttonBackImage.setStyle(style);

                return true;
            };

            public void touchUp(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
            };
        });

        buttonFacebook.addListener(new InputListener() {
            public boolean touchDown(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                Gdx.app.debug(LOG_TAG,"bt facebook pressed!");
                return true;
            }
            ;
            public void touchUp(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                //Gdx.net.openURI(MyGdxGame.AndroidFacebookGameUrl);

            };
        });

        buttonTwitter.addListener(new InputListener() {
            public boolean touchDown(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                Gdx.app.debug(LOG_TAG,"bt twitter pressed!");
                return true;
            }
            ;
            public void touchUp(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                //Gdx.net.openURI(MyGdxGame.AndroidTwitterGameUrl);

            };
        });

        buttonStore.addListener(new InputListener() {
            public boolean touchDown(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                Gdx.app.debug(LOG_TAG,"bt store pressed!");
                return true;
            }

            ;

            public void touchUp(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
//
//                Save.load();
//                if(Save.gd.getMoney()<MyGdxGame.getContinueMax()) {
//                    /*Save.gd.setMoney(Save.gd.getMoney() + MyGdxGame.getContinueStorePack());
//                    Save.save();
//                    Save.load();*/
//                    MyGdxGame.actionResolver.purchaseExtraCoins();
//                    labelMoneyMenu.setText(Integer.toString(Save.gd.getMoney()));
//                }else{
//                    if(MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getSound("error").play();
//
//                }
            };
        });


        buttonAchievement.addListener(new InputListener() {
            public boolean touchDown(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                Gdx.app.debug(LOG_TAG,"bt achievement pressed!");
                if(MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getSound("select").play();
                return true;
            }

            ;

            public void touchUp(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                if (MyGdxGame.actionResolver.getSignedInGPGS())
                    MyGdxGame.actionResolver.getAchievementsGPGS();
                else
                    MyGdxGame.actionResolver.loginGPGS(false);
            };
        });

        buttonCloseUiOption.addListener(new InputListener() {
            public boolean touchDown(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                Gdx.app.debug(LOG_TAG,"bt close pressed!");
                if(MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getSound("select").play();
                return true;
            }

            ;

            public void touchUp(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {

            }

            ;
        });
    }

    public void updateSelector(){
        if(PBlueMenu) Save.gd.setSelector("blue");
        if(PRedMenu)  Save.gd.setSelector("red");
        if(PYellowMenu) Save.gd.setSelector("yellow");
        if(playerIsTouchedMenu) Save.gd.setSelector("green");
        if(!PBlueMenu && !PRedMenu && !PYellowMenu && !playerIsTouchedMenu) Save.gd.setSelector("random");
        Gdx.app.debug(LOG_TAG,"*SELECTOR* "+"PBlueMenu:"+ PBlueMenu +" PRedMenu:"+ PRedMenu +" PYellowMenu:"+ PYellowMenu +" playerIsTouchedMenu:"+ playerIsTouchedMenu);
        Save.save();
    }

    public void handleInput() {

        hideUiOption();
        showMainUi();

        if (click_on_playMenu) {
            if(MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) {
                //MyGdxGame.res.getMusic("newScreen").play();
            }



        }
        if(actionResolver != null){
            if (click_on_leaderboardMenu) {
                click_on_leaderboardMenu = false;
                MyGdxGame.clickedOnLeaderboard = true;
                if (actionResolver.getSignedInGPGS())
                    actionResolver.getLeaderboardGPGS(false,0);
                else
                    actionResolver.loginGPGS(false);
            }
        }
        if (click_on_tutoMenu) {
            click_on_tutoMenu = false;
            gsm.setState(GameStateManager.TUTO);
            if(MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2)MyGdxGame.res.getMusic("newScreen").play();
        }

        if(click_on_creditsMenu){
            click_on_creditsMenu = false;
            gsm.setState(GameStateManager.CREDITS);
            if(MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2)MyGdxGame.res.getMusic("newScreen").play();
        }

        if(click_on_shopMenu){
            click_on_shopMenu = false;
            gsm.setState(GameStateManager.SHOP);
            if(MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2)MyGdxGame.res.getMusic("newScreen").play();
        }
    }

    public void update(float dt) {

        if(MyGdxGame.isSoundEnable() == 2){
            if(!MyGdxGame.res.getMusic("title").isPlaying() /*&& !MyGdxGame.res.getMusic("newScreen").isPlaying()*/){
                MyGdxGame.res.getMusic("title").setVolume(1.0f);
                MyGdxGame.res.getMusic("title").play();
            }
        }

        if (click_on_playMenu){
            MyGdxGame.fadeOut.update(dt);
            isfadeOutStarted++;

            if(isfadeOutStarted > 30){
                Save.load();

                if(!Save.gd.isFirstPlay()){
                    gsm.setState(GameStateManager.PLAY);
                }else{
                    gsm.setState(GameStateManager.TUTO);
                    Save.gd.setFirstPlay(false);
                    Save.save();
                }

                isfadeOutStarted = 0;
            }
        }


//        if(MyGdxGame.isSoundEnable() == 2) {
//            //MyGdxGame.res.getMusic("main").setVolume(0.6f);
//            if(!MyGdxGame.res.getMusic("main").isPlaying())
//                MyGdxGame.res.getMusic("main").play();
//        }


        labelMoneyMenu.setText(Integer.toString(Save.gd.getMoney()));

        if(click_on_creditsMenu) {
            buttonPBlueMenu.setScale(0);
            buttonPRedMenu.setScale(0);
            buttonPYellow.setScale(0);
            buttonPGreen.setScale(0);
        }else{
            buttonPBlueMenu.setScale(1);
            buttonPRedMenu.setScale(1);
            buttonPYellow.setScale(1);
            buttonPGreen.setScale(1);
        }

        //MyGdxGame.updateBGM();
        handleInput();
        //animationRain.update(dt);
        MyGdxGame.background_wood1.update(dt);
        MyGdxGame.background_cloud.update(dt);
        //MyGdxGame.background_skyDay.update(dt);

        animPlayerIdle.update(dt);
        //animTitle.update(dt);
        animPlayerIdleFliped.update(dt);
        animPrincessIdle.update(dt);
        animPrincessIdleFliped.update(dt);


    }

    public void resize(int width, int height) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // calculate new viewportMenu
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
        viewportMenu = new Rectangle(crop.x, 0, w, h);
        //Gdx.gl.glViewport((int) viewportMenu.x, (int) viewportMenu.yMenu, (int) viewportMenu.width, (int) viewportMenu.height);

        float offsetY = crop.y;
        float offsetX = crop.x;

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glViewport(0,0, (int) viewportMenu.width+ (int)offsetX, Gdx.graphics.getHeight());
        stage0Menu.act();
        sb.begin();
        stage0Menu.draw();
        sb.end();
        Gdx.gl.glViewport((int) viewportMenu.x, (int) viewportMenu.y, (int) viewportMenu.width - (int)offsetX, (int) viewportMenu.height - (int)offsetY);

        stage1Menu.getViewport().update((int) (width - offsetX), (int) (height - offsetY), true);
        stage2Menu.getViewport().update((int) (width - offsetX), (int) (height - offsetY), true);

    }


    public void render() {

        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        //cpt_translate_animation_titleMenu++;


        //Gdx.gl.glClearColor(65f / 255f, 18f / 255f, 252f / 255f, 1);

        sb.setProjectionMatrix(cam.combined);
        shapeRenderer.setProjectionMatrix(cam.combined);

        //MyGdxGame.background_skyDay.render(sb);
        MyGdxGame.background_cloud.render(sb);
        MyGdxGame.background_wood1.render(sb);
        MyGdxGame.background_title.render(sb);

        sb2Menu.begin();

        if (click_on_creditsMenu) {
            stage1Menu.getActors().items[5].setVisible(true);
            stage1Menu.getActors().items[5].draw(sb2Menu, 1f);
            stage1Menu.getActors().items[0].setVisible(false);
            stage1Menu.getActors().items[1].setVisible(false);
            stage1Menu.getActors().items[3].setVisible(false);
            stage1Menu.getActors().items[4].setVisible(false);
            stage1Menu.getActors().items[2].setVisible(false);

        } else {

            //SOUND BUTTON
            sb3Menu.begin();

            float speed = 8f;

            if (stage1Menu.getActors().items[4].getRight() >= Gdx.graphics.getWidth() + 5) {
                System.out.println("cpt_translate_animationMenu="+cpt_translate_animationMenu);
                stage1Menu.getActors().items[4].setPosition((Gdx.graphics.getWidth() / 1f) - (cpt_translate_animationMenu * speed), stage1Menu.getActors().items[4].getY());
                cpt_translate_animationMenu++;
            }

            stage1Menu.getActors().items[4].setVisible(true);
            stage1Menu.getActors().items[4].draw(sb3Menu, 1f);


            //GEAR BUTTON
            if (stage1Menu.getActors().items[11].getX() <= -5)
                stage1Menu.getActors().items[11].setPosition(-stage1Menu.getActors().items[11].getWidth() + cpt_translate_animationMenu * speed, stage1Menu.getActors().items[11].getY());

            stage1Menu.getActors().items[11].draw(sb3Menu, 1f);
            sb3Menu.end();

            //WINDOW CREDITS
            stage1Menu.getActors().items[5].setVisible(false);

            cpt_blinkMenu++;
            float period = 100;
            if (cpt_blinkMenu >= period) {
                cpt_blinkMenu = 0;
                blinkMenu = !blinkMenu;
            }

            if (blinkMenu) blinking_text_alphaMenu += (1f / period);
            else blinking_text_alphaMenu -= (1f / period);

            //PLAYER BUTTONS
            stage1Menu.getActors().items[7].draw(sb2Menu, 1f);
            stage1Menu.getActors().items[8].draw(sb2Menu, 1f);
            stage1Menu.getActors().items[9].draw(sb2Menu, 1f);
            stage1Menu.getActors().items[10].draw(sb2Menu, 1f);

            sb3Menu.begin();
            //BUTTON PLAY
            stage1Menu.getActors().items[0].setVisible(true);
            stage1Menu.getActors().items[0].setPosition(-stage1Menu.getActors().items[0].getWidth() + Gdx.graphics.getWidth()/20 + cpt_translate_animationMenu * speed, stage1Menu.getActors().items[0].getY());
            stage1Menu.getActors().items[0].draw(sb3Menu, 1f);

            //BUTTON LEADERBOARD
            stage1Menu.getActors().items[1].setVisible(true);
            stage1Menu.getActors().items[1].setPosition((Gdx.graphics.getWidth() / 1f) - (Gdx.graphics.getWidth()/20+ cpt_translate_animationMenu * speed), stage1Menu.getActors().items[1].getY());
            stage1Menu.getActors().items[1].draw(sb3Menu, 1f);

            //BUTTON CREDITS
            stage1Menu.getActors().items[3].setVisible(true);
            stage1Menu.getActors().items[3].draw(sb3Menu, 1f);

            //BUTTON HOW TO
            stage1Menu.getActors().items[2].setVisible(true);
            stage1Menu.getActors().items[2].draw(sb3Menu, Math.abs(blinking_text_alphaMenu));
            sb3Menu.end();

            //TEXT HOW TO ANIM
            if (stage1Menu.getActors().items[2].getTop() <= Gdx.graphics.getHeight() / 7.5f) {
                stage1Menu.getActors().items[2].setPosition((Gdx.graphics.getWidth() / 2f) - stage1Menu.getActors().items[2].getWidth() / 2f, (-stage1Menu.getActors().items[2].getHeight() + cpt_translate_animation1Menu * 10f));
                cpt_translate_animation1Menu++;
            } else {
                start2Menu = true;
            }

            //TEXT CREDIT ANIM
            if (start2Menu && (stage1Menu.getActors().items[3].getTop() <= Gdx.graphics.getHeight() / 19.5f)) {
                stage1Menu.getActors().items[3].setPosition((Gdx.graphics.getWidth() / 2f) - stage1Menu.getActors().items[3].getWidth() / 2f, (-stage1Menu.getActors().items[3].getHeight() + cpt_translate_animation2Menu * 8f));
                cpt_translate_animation2Menu++;
            }

            sb.begin();

            float w = 111*5f;
            float h = 44*5f;

            //sb.draw(animTitle.getFrame(), MyGdxGame.V_WIDTH/2 - w/2, 670.0f+ posXMenu/100f , w, h);

            if(left2Menu){
                yMenu +=0.3f;
            } else{
                yMenu -=0.3f;
            }

//            float titley = Gdx.graphics.getHeight() - cpt_translate_animation_titleMenu;
//            if(jp){
//                w = w*1.1f;
//                if(titley > 700){
//                    cpt_translate_animation_titleMenu+=20 ;
//                    sb.draw(animTitle.getFrame(), MyGdxGame.V_WIDTH/2 - w/2, Gdx.graphics.getHeight() - cpt_translate_animation_titleMenu, MyGdxGame.V_WIDTH/2, 670.0f -95/2 ,  w, h,1,1, 0);
//                }else{
//                    sb.draw(animTitle.getFrame(), MyGdxGame.V_WIDTH/2 - w/2, Gdx.graphics.getHeight() - cpt_translate_animation_titleMenu, MyGdxGame.V_WIDTH/2, 670.0f -95/2 ,  w, h,1,1, 0);
//                }
//            } else{
//                if(titley > 700){
//                    cpt_translate_animation_titleMenu+=20;
//                    sb.draw(animTitle.getFrame(), MyGdxGame.V_WIDTH/2 - w/2,  Gdx.graphics.getHeight() - cpt_translate_animation_titleMenu, MyGdxGame.V_WIDTH/2, 670.0f -95/2 ,  w, h,1,1, 0);
//                }else{
//                    sb.draw(animTitle.getFrame(), MyGdxGame.V_WIDTH/2 - w/2,  Gdx.graphics.getHeight() - cpt_translate_animation_titleMenu, MyGdxGame.V_WIDTH/2, 670.0f -95/2 ,  w, h,1,1, 0);
//                }
//            }

            if(jp){
                w = w*1.1f;
                sb.draw(animTitle.getFrame(), MyGdxGame.V_WIDTH/2 - w/2, MyGdxGame.V_HEIGHT - h*1.2f , MyGdxGame.V_WIDTH/2, 670.0f -95/2 ,  w, h*0.9f,1,1, 0);
            } else{
                sb.draw(animTitle.getFrame(), MyGdxGame.V_WIDTH/2 - w/2, MyGdxGame.V_HEIGHT - h*1.2f , MyGdxGame.V_WIDTH/2, 670.0f -95/2 ,  w, h,1,1, 0);
            }

            if(posXMenu +(96/2) >= MyGdxGame.V_WIDTH){
                leftMenu = true;
                rightMenu = false;
            }

            if (posXMenu <= 0){
                rightMenu = true;
                leftMenu = false;
            }


            if(posX2Menu >= 380){
                right2Menu = true;
                left2Menu = false;
            }

            if (posX2Menu <= 200){
                left2Menu = true;
                right2Menu = false;
            }


            if(rightMenu){
                posXMenu +=3.0f;
                sb.draw(animPlayerIdle.getFrame(), posXMenu, 202);
            }
            if(leftMenu){
                posXMenu -=3.0f;
                sb.draw(animPlayerIdleFliped.getFrame(), posXMenu, 202);
            }

            if(right2Menu){
                posX2Menu -=2;
                sb.draw(animPrincessIdle.getFrame(), posX2Menu, 458);
            }
            if(left2Menu){
                posX2Menu +=2;
                sb.draw(animPrincessIdleFliped.getFrame(), posX2Menu, 458);
            }


            sb.end();
        }

        sb2Menu.end();

        if(click_on_playMenu){
            Gdx.gl.glViewport(0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
            MyGdxGame.fadeOut.render(sb2Menu);
        }


    }

    public void dispose() {
        //sb2Menu.dispose();
        //sb3Menu.dispose();
        if(!Save.gd.getAdsRemoverPurchased())
            actionResolver.showBannerAd();

        click_on_playMenu = false;
        click_on_leaderboardMenu = false;
        click_on_tutoMenu = false;
        isfadeOutStarted = 0;
        cpt_blinkMenu = 0;
        blinkMenu = false;
        blinking_text_alphaMenu = 0;
        start2Menu = false;
//        cpt_translate_animation1Menu = 0;
//        cpt_translate_animation2Menu = 0;
        click_on_creditsMenu = false;
        click_on_shopMenu = false;
//        PBlueMenu = false;
//        PRedMenu = false;
//        PYellowMenu = false;
//        playerIsTouchedMenu = false;
//        cptMenu = 0;
//        cpt_translate_animationMenu = 0;
//        rightMenu = true;
//        leftMenu = false;
//        right2Menu = true;
//        left2Menu = false;
//        jp = false;
//        yMenu = 670.0f;

        if(MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2)
            if(MyGdxGame.res.getMusic("title").isPlaying()){
                MyGdxGame.res.getMusic("title").pause();
                MyGdxGame.res.getMusic("title").stop();
            }

        if(MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) {
            if(MyGdxGame.res.getMusic("newScreen").isPlaying()){
                MyGdxGame.res.getMusic("newScreen").pause();
                MyGdxGame.res.getMusic("newScreen").stop();
            }
        }
    }

    public void showMainUi(){
        buttonPlayMenu.setVisible(true);
        buttonLeaderBoardMenu.setVisible(true);
        buttonGearMenu.setVisible(true);
        buttonTutoMenu.setVisible(true);
        buttonSoundMenu.setVisible(true);
        buttonCreditsMenu.setVisible(true);
        buttonPBlueMenu.setVisible(true);
        buttonPRedMenu.setVisible(true);
        buttonPYellow.setVisible(true);
        buttonPGreen.setVisible(true);
    }

    public void showUiOption(){
        buttonCloseUiOption.setVisible(true);
        buttonAchievement.setVisible(true);
        buttonStore.setVisible(true);
        buttonBackImage.setVisible(true);
        optionUiBackImage.setVisible(true);
        buttonTwitter.setVisible(true);
        buttonFacebook.setVisible(true);
    }

    public void hideUiOption(){
        buttonCloseUiOption.setVisible(false);
        buttonAchievement.setVisible(false);
        buttonStore.setVisible(false);
        buttonBackImage.setVisible(false);
        optionUiBackImage.setVisible(false);
        buttonTwitter.setVisible(false);
        buttonFacebook.setVisible(false);
    }

}
