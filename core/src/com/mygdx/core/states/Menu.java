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
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.utils.Timer;
import com.mygdx.core.MyGdxGame;
import com.mygdx.core.handlers.Animation;
import com.mygdx.core.handlers.GameStateManager;
import com.mygdx.core.handlers.Save;

import static com.mygdx.core.MyGdxGame.lastPlayerPosition;

public class Menu extends GameState {

    private static String LOG_TAG = Menu.class.getSimpleName();

    private Animation animPlayerIdle, animPlayerIdleFliped, animPlayerSlash, animPrincessIdle, animPrincessIdleFliped, animTitle, animHelpMe;
    private boolean click_on_play, click_on_leaderboard, click_on_tuto;
    private int isfadeOutStarted = 0;
    private Stage stage1, stage2, stage3, stageUiOption;
    private Label labelCopyright;
    private Button buttonPlay, buttonLeaderBoard, buttonTuto, buttonSound, buttonGear, buttonCredits, buttonPBlue, buttonPRed,
            buttonPYellow, buttonPGreen, buttonStore, buttonBackImage, buttonFacebook, buttonTwitter, buttonAchievement, buttonCloseUiOption, optionUiBackImage, imageCoin;
    private Skin skin;
    private Image loading;
    private Rectangle viewport;
    private int cpt_blink = 0;
    private boolean blink = false;
    private float blinking_text_alpha = 0;
    private SpriteBatch sb2, sb3;
    private boolean start2 = false;
    private int cpt_translate_animation1 = 0;
    private int cpt_translate_animation2 = 0;
    private boolean click_on_credits = false;
    private boolean click_on_shop = false;
    private boolean PBlue = false, PRed = false, PYellow = false, playerIsTouched = false;
    private int cpt = 0;
    private int cpt_translate_animation = 0;
    private int cpt_translate_animation_title = 0;
    private Label labelMoney;
    private String code = "";
    float posX = (MyGdxGame.V_WIDTH / 2f);
    float posX2 = (MyGdxGame.V_WIDTH / 2f);
    boolean right = true;
    boolean left = false;
    boolean right2 = true;
    boolean left2 = false;
    boolean jp = false;
    float y = 670.0f;
    private Stage stage0;
    private Image intro;

    public Menu(final GameStateManager gsm) {

        super(gsm);



        if(MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) {
            if(MyGdxGame.res.getMusic("newScreen").isPlaying()){
                MyGdxGame.res.getMusic("newScreen").pause();
                MyGdxGame.res.getMusic("newScreen").stop();
            }
            MyGdxGame.res.getMusic("newScreen").play();
        }

        lastPlayerPosition = new Vector2(0,0);
        MyGdxGame.initFade();
        Timer.instance().start();



        intro = new Image(MyGdxGame.atlas.findRegion("backgroundSky"));
        intro.setFillParent(true);
        stage0 = new Stage();
        stage0.addActor(intro);

        Timer.instance().stop();
        Timer.instance().clear();
        if(MyGdxGame.res.getMusic("on").isPlaying())MyGdxGame.res.getMusic("on").stop();
        updateSelector();

        Save.load();
        if (!Save.gd.getAdsRemoverPurchased()) {
            String network = game.actionResolver.getNetworkClass();
            if(network == null) network = "ABSENT";
            Gdx.app.debug(LOG_TAG,"NETWORK: "+network);
            if(network.equals("4G")|network.equals("3G")|network.equals("WIFI"))
                game.actionResolver.showOrLoadBanner();
        }

        click_on_play = false;
        click_on_leaderboard = false;
        viewport = new Rectangle();
        sb2 = new SpriteBatch();
        sb3 = new SpriteBatch();

        MyGdxGame.background_cloud.setVector(+10, 0);
        //MyGdxGame.background_skyDay.setVector(0, 0);
        MyGdxGame.background_wood1.setVector(-3, 0);

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

        skin = new Skin();
        skin.addRegions(MyGdxGame.atlas);


        stage1 = new Stage();
        stage2 = new Stage();
        stage3 = new Stage();
        stageUiOption = new Stage();

        stage1.addAction(Actions.sequence(Actions.alpha(0.5f), Actions.fadeIn(1.5f)));
        stage2.addAction(Actions.sequence(Actions.alpha(0.5f), Actions.fadeIn(1.5f)));
        stage3.addAction(Actions.sequence(Actions.alpha(0.5f), Actions.fadeIn(1.5f)));
        stageUiOption.addAction(Actions.sequence(Actions.alpha(0.5f), Actions.fadeIn(0.1f)));

        ButtonStyle style = new ButtonStyle();
        style = new Button.ButtonStyle();
        style.up = skin.getDrawable("coin");
        style.down = skin.getDrawable("coin");
        imageCoin = new Button(style);
        imageCoin.setWidth(Gdx.graphics.getWidth() / 10f);
        imageCoin.setHeight(Gdx.graphics.getWidth() / 10f);
        imageCoin.setPosition(imageCoin.getWidth() / 4, (Gdx.graphics.getHeight() - imageCoin.getHeight() - 20));
        stageUiOption.addActor(imageCoin);

        labelMoney = new Label("0", new LabelStyle(new BitmapFont(Gdx.files.internal(MyGdxGame.fontPointPath), false), Color.WHITE));
        labelMoney.setFontScale(Gdx.graphics.getWidth() / 1200f);
        labelMoney.setWidth(Gdx.graphics.getWidth() / 2);
        labelMoney.setHeight(imageCoin.getHeight());
        labelMoney.setAlignment(Align.center | Align.left);
        labelMoney.setPosition(imageCoin.getRight() * 1.1f, imageCoin.getY());
        stageUiOption.addActor(labelMoney);

        style = new ButtonStyle();
        style.up = skin.getDrawable("coin");
        style.down = skin.getDrawable("coin");
        optionUiBackImage = new Button(style);
        optionUiBackImage.setWidth(Gdx.graphics.getWidth() / 1.05f);
        optionUiBackImage.setHeight(Gdx.graphics.getHeight() / 1.25f);
        optionUiBackImage.setPosition((Gdx.graphics.getWidth() - optionUiBackImage.getWidth()) / 2, (Gdx.graphics.getHeight() - optionUiBackImage.getHeight()) / 2f);
        stageUiOption.addActor(optionUiBackImage);


        style = new ButtonStyle();
        style.up = skin.getDrawable("coin");
        style.down = skin.getDrawable("coin");
        buttonBackImage = new Button(style);
        buttonBackImage.setWidth(Gdx.graphics.getHeight() / 7f);
        buttonBackImage.setHeight(Gdx.graphics.getHeight() / 7f);
        buttonBackImage.setPosition((Gdx.graphics.getWidth() - buttonBackImage.getWidth()) / 2, (Gdx.graphics.getHeight() - buttonBackImage.getHeight()) / 1.28f);
        stageUiOption.addActor(buttonBackImage);

        style = new ButtonStyle();
        style.up = skin.getDrawable("buttonUiOptionFacebookUp");
        style.down = skin.getDrawable("buttonUiOptionFacebookDown");
        buttonFacebook = new Button(style);
        buttonFacebook.setWidth(buttonBackImage.getWidth());
        buttonFacebook.setHeight(buttonBackImage.getHeight());
        buttonFacebook.setPosition((buttonBackImage.getX() - buttonFacebook.getWidth() - buttonBackImage.getWidth()/20f ) , (Gdx.graphics.getHeight() - buttonFacebook.getHeight()) / 1.28f);
        stageUiOption.addActor(buttonFacebook);

        style = new ButtonStyle();
        style.up = skin.getDrawable("buttonUiOptionTwitterUp");
        style.down = skin.getDrawable("buttonUiOptionTwitterDown");
        buttonTwitter = new Button(style);
        buttonTwitter.setWidth(buttonBackImage.getWidth());
        buttonTwitter.setHeight(buttonBackImage.getHeight());
        buttonTwitter.setPosition((buttonBackImage.getRight() + buttonBackImage.getWidth()/20f ) , (Gdx.graphics.getHeight() - buttonTwitter.getHeight()) / 1.28f);
        stageUiOption.addActor(buttonTwitter);


        style = new ButtonStyle();
        style.up = skin.getDrawable("coin");
        style.down = skin.getDrawable("coin");
        buttonStore = new Button(style);
        buttonStore.setWidth(Gdx.graphics.getWidth() / 1.26f);
        buttonStore.setHeight(Gdx.graphics.getHeight() / 7f);
        buttonStore.setPosition((Gdx.graphics.getWidth() - buttonStore.getWidth())/2f , buttonBackImage.getY() - buttonBackImage.getHeight() * 1.69f);
        stageUiOption.addActor(buttonStore);

        style = new ButtonStyle();
        style.up = skin.getDrawable("buttonUiOptionAchievementUp");
        style.down = skin.getDrawable("buttonUiOptionAchievementDown");
        buttonAchievement = new Button(style);
        buttonAchievement.setWidth(buttonStore.getWidth());
        buttonAchievement.setHeight(buttonStore.getHeight());
        buttonAchievement.setPosition(buttonStore.getX(), buttonStore.getY() - buttonStore.getHeight() * 1.69f);
        stageUiOption.addActor(buttonAchievement);

        style = new ButtonStyle();
        style.up = skin.getDrawable("buttonPlayUp");
        style.down = skin.getDrawable("buttonPlayUp");
        buttonCloseUiOption = new Button(style);
        buttonCloseUiOption.setWidth(Gdx.graphics.getWidth() / 6f);
        buttonCloseUiOption.setHeight(Gdx.graphics.getWidth() / 6f);
        buttonCloseUiOption.setPosition((Gdx.graphics.getWidth() - buttonCloseUiOption.getWidth()) / 1.01f, (Gdx.graphics.getHeight() - buttonCloseUiOption.getHeight()) / 1.1f);
        stageUiOption.addActor(buttonCloseUiOption);

        style = new ButtonStyle();
        style.up = skin.getDrawable("buttonPlayUp");
        style.down = skin.getDrawable("buttonPlayDown");
        buttonPlay = new Button(style);
        buttonPlay.setWidth(Gdx.graphics.getWidth() / 2.2f);
        buttonPlay.setHeight(Gdx.graphics.getHeight() / 5f);
        //buttonExit.setPosition(-buttonExit.getWidth()/2.6f, (Gdx.graphics.getHeight() - buttonExit.getHeight()) / 1.6f);

        buttonPlay.setPosition(-400, Gdx.graphics.getHeight() / 2f);
        stage1.addActor(buttonPlay);

        style = new ButtonStyle();
        style.up = skin.getDrawable("buttonLeaderBoardUp");
        style.down = skin.getDrawable("buttonLeaderBoardDown");
        buttonLeaderBoard = new Button(style);
        buttonLeaderBoard.setWidth(Gdx.graphics.getWidth() / 2.2f);
        buttonLeaderBoard.setHeight(Gdx.graphics.getHeight() / 5f);
        buttonLeaderBoard.setPosition((Gdx.graphics.getWidth() / 1f) - buttonLeaderBoard.getWidth() + 20, (Gdx.graphics.getHeight() - buttonPlay.getHeight()) / 1.6f);
        stage1.addActor(buttonLeaderBoard);


        style = new ButtonStyle();
        style.up = skin.getDrawable("buttonTutoUp");
        style.down = skin.getDrawable("buttonTutoDown");
        buttonTuto = new Button(style);
        buttonTuto.setVisible(true);
        buttonTuto.setWidth(Gdx.graphics.getWidth() / 1.7f);
        buttonTuto.setHeight(Gdx.graphics.getHeight() / 13f);
        buttonTuto.setPosition((Gdx.graphics.getWidth() / 2) - buttonTuto.getWidth() / 1.26f, 0);
        stage1.addActor(buttonTuto);

        labelCopyright = new Label("(c) RAM52 Credits 2017", new LabelStyle(new BitmapFont( Gdx.files.internal(MyGdxGame.fontScorePath), false), Color.WHITE));
        labelCopyright.setWidth(Gdx.graphics.getWidth() / 3f);
        labelCopyright.setHeight(Gdx.graphics.getHeight() / 40);
        labelCopyright.setFontScale(Gdx.graphics.getWidth() / 500f);
        labelCopyright.setAlignment(Align.center);
        labelCopyright.setPosition(0, -500);
        stage1.addActor(labelCopyright);


        style = new ButtonStyle();
        style.up = skin.getDrawable("buttonSound2Full");
        style.down = skin.getDrawable("buttonSound2Full");
        buttonSound = new Button(style);
        buttonSound.setWidth(Gdx.graphics.getWidth() / 5f);
        buttonSound.setHeight(Gdx.graphics.getHeight() / 7.8f);
        buttonSound.setPosition((Gdx.graphics.getWidth() / 1f) - buttonSound.getWidth() + 20, Gdx.graphics.getHeight() / 100f);

        if(MyGdxGame.isSoundEnable() == 0){
            style = new ButtonStyle();
            style.up = skin.getDrawable("buttonSound2Mute");
            style.down = skin.getDrawable("buttonSound2Mute");
            buttonSound.setStyle(style);
        }
        if(MyGdxGame.isSoundEnable() == 1){
            style = new ButtonStyle();
            style.up = skin.getDrawable("buttonSound2Fx");
            style.down = skin.getDrawable("buttonSound2Fx");
            buttonSound.setStyle(style);
        }
        if(MyGdxGame.isSoundEnable() == 2){
            style = new ButtonStyle();
            style.up = skin.getDrawable("buttonSound2Full");
            style.down = skin.getDrawable("buttonSound2Full");
            buttonSound.setStyle(style);
        }
        stage1.addActor(buttonSound);


        style = new ButtonStyle();
        style.up = skin.getDrawable("buttonPlayUp");
        style.down = skin.getDrawable("buttonPlayUp");
        buttonCredits = new Button(style);
        buttonCredits.setWidth(0);
        buttonCredits.setHeight(0);
        buttonCredits.setPosition(Gdx.graphics.getWidth() / 2 - buttonCredits.getWidth() / 2, Gdx.graphics.getHeight() / 2 - buttonCredits.getHeight() / 2);
        buttonCredits.setVisible(false);
        stage1.addActor(buttonCredits);

        loading = new Image(MyGdxGame.atlas.findRegion("buttonSecret"));
        loading.setWidth(Gdx.graphics.getWidth());
        loading.setHeight(Gdx.graphics.getHeight() / 12f);
        loading.setPosition(0, (Gdx.graphics.getHeight() - loading.getHeight()) / 2f);
        //loading.setVisible(true);
        loading.setScale(0);
        stage1.addActor(loading);


        style = new ButtonStyle();
        style.up = skin.getDrawable("buttonSound2Full");
        style.down = skin.getDrawable("buttonSound2Full");
        buttonPBlue = new Button(style);
        buttonPBlue.setWidth(Gdx.graphics.getWidth() / 8f);
        buttonPBlue.setHeight(Gdx.graphics.getWidth() / 4f);
        buttonPBlue.setPosition((Gdx.graphics.getWidth() - buttonPBlue.getWidth()) / 4f, (Gdx.graphics.getHeight() - buttonPBlue.getWidth()) / 4.3f);

        buttonPRed = new Button(style);
        buttonPRed.setWidth(Gdx.graphics.getWidth() / 8f);
        buttonPRed.setHeight(Gdx.graphics.getWidth() / 4f);
        buttonPRed.setPosition((Gdx.graphics.getWidth() - buttonPBlue.getWidth()) / 2.1f, (Gdx.graphics.getHeight() - buttonPBlue.getWidth()) / 4.3f);

        buttonPYellow = new Button(style);
        buttonPYellow.setWidth(Gdx.graphics.getWidth() / 8f);
        buttonPYellow.setHeight(Gdx.graphics.getWidth() / 4f);
        buttonPYellow.setPosition((Gdx.graphics.getWidth() - buttonPBlue.getWidth()) / 1.7f, (Gdx.graphics.getHeight() - buttonPBlue.getWidth()) / 4.3f);

        buttonPGreen = new Button(style);
        buttonPGreen.setWidth(Gdx.graphics.getWidth() / 8f);
        buttonPGreen.setHeight(Gdx.graphics.getWidth() / 4f);
        buttonPGreen.setPosition((Gdx.graphics.getWidth() - buttonPBlue.getWidth()) / 1.4f, (Gdx.graphics.getHeight() - buttonPBlue.getWidth()) / 4.3f);

        stage1.addActor(buttonPBlue);
        stage1.addActor(buttonPRed);
        stage1.addActor(buttonPYellow);
        stage1.addActor(buttonPGreen);

        style = new ButtonStyle();
        style.up = skin.getDrawable("buttonGearUp");
        style.down = skin.getDrawable("buttonGearDown");
        buttonGear = new Button(style);
        buttonGear.setWidth(Gdx.graphics.getWidth() / 5f);
        buttonGear.setHeight(Gdx.graphics.getHeight() / 7.8f);
        buttonGear.setPosition(-400, Gdx.graphics.getHeight() / 100f);
        stage1.addActor(buttonGear);

//        if(MyGdxGame.isSoundEnable() == 2) {
//            MyGdxGame.res.getMusic("main").setVolume(1f);
//            if(!MyGdxGame.res.getMusic("main").isPlaying())
//                MyGdxGame.res.getMusic("main").play();
//        }

        buttonGear.addListener(new InputListener() {
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
                click_on_shop = true;
//                stageUiOption.addAction(Actions.sequence(Actions.alpha(0.5f), Actions.fadeIn(0.1f)));
//                showOptionUi = true;
//                showMainUi = false;
//
//                Save.load();
//                labelMoney.setText(Integer.toString(Save.gd.getMoney()));

            }

            ;
        });

        buttonSound.addListener(new InputListener() {
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
                    style.up = skin.getDrawable("buttonSound2Mute");
                    style.down = skin.getDrawable("buttonSound2Mute");
                    buttonSound.setStyle(style);
                }

                if (MyGdxGame.isSoundEnable() == 1) {
                    if (MyGdxGame.res.getMusic("title").isPlaying()) MyGdxGame.res.getMusic("title").pause();
                    MyGdxGame.res.getSound("select").play();
                    ButtonStyle style = new ButtonStyle();
                    style.up = skin.getDrawable("buttonSound2Fx");
                    style.down = skin.getDrawable("buttonSound2Fx");
                    buttonSound.setStyle(style);
                }

                if (MyGdxGame.isSoundEnable() == 2) {
                    if (!MyGdxGame.res.getMusic("title").isPlaying()) MyGdxGame.res.getMusic("title").play();
                    ButtonStyle style = new ButtonStyle();
                    style.up = skin.getDrawable("buttonSound2Full");
                    style.down = skin.getDrawable("buttonSound2Full");
                    buttonSound.setStyle(style);
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

        buttonCredits.addListener(new InputListener() {
            public boolean touchDown(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                //Gdx.app.debug(LOG_TAG,"bt pressed!");
//                if(MyGdxGame.isSoundEnable() && buttonCredits.getScaleX()>0) MyGdxGame.res.getSound("select").play();
//                click_on_credits = false;
//                click_on_play = false;
//                buttonPlay.setVisible(true);
//                buttonLeaderBoard.setVisible(true);
//                buttonPBlue.setVisible(true);
//                buttonPRed.setVisible(true);
//                buttonPYellow.setVisible(true);
//                buttonPGreen.setVisible(true);

                return true;
            };

            public void touchUp(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
//                click_on_credits = false;
//                click_on_play = false;
//                Gdx.app.debug(LOG_TAG,"SHOW CREDITS: " + click_on_credits);
//                Gdx.app.debug(LOG_TAG,"CLICK ON PLAY: " + click_on_play);
            };
        });

        buttonPlay.addListener(new InputListener() {
            public boolean touchDown(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                if(MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getSound("select").play();
                return true;
            };

            public void touchUp(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                click_on_play = true;
                //Gdx.gl.glClearColor(0, 0, 0, 1);
                cpt = 0;
            };
        });

        buttonLeaderBoard.addListener(new InputListener() {
            public boolean touchDown(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                if(MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getSound("select").play();

                return true;
            };

            public void touchUp(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                click_on_leaderboard = true;
            };
        });

        buttonTuto.addListener(new InputListener() {
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
                click_on_tuto = true;
            };
        });

        labelCopyright.addListener(new InputListener() {
            public boolean touchDown(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                Gdx.app.debug(LOG_TAG,"bt copyright pressed!");
                if(MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getSound("select").play();
//                click_on_credits =! click_on_credits;
//                if(click_on_credits) {
//                    /*buttonPBlue.setScale(0);
//                    buttonPRed.setScale(0);
//                    buttonPYellow.setScale(0);
//                    buttonPGreen.setScale(0);
//                    /*buttonPBlue.setVisible(false);
//                    buttonPRed.setVisible(false);
//                    buttonPYellow.setVisible(false);
//                    buttonPGreen.setVisible(false);
//                    buttonExit.setVisible(false);*/
//                    buttonLeaderBoard.setVisible(false);
//                    buttonExit.setDisabled(false);
//                    buttonLeaderBoard.setDisabled(false);
//                    loading.setScale(0);
//                    buttonCredits.setWidth(Gdx.graphics.getWidth() / 1.1f);
//                    buttonCredits.setHeight(Gdx.graphics.getHeight() / 1.2f);
//                    buttonCredits.setPosition(Gdx.graphics.getWidth() / 2 - buttonCredits.getWidth() / 2, Gdx.graphics.getHeight() / 2 - buttonCredits.getHeight() / 2);
//                }else{
//                    /*buttonPBlue.setScale(1);
//                    buttonPRed.setScale(1);
//                    buttonPYellow.setScale(1);
//                    buttonPGreen.setScale(1);*/
//                }
                return true;
            };

            public void touchUp(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                click_on_credits = true;
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
                style.up = skin.getDrawable("buttonUiOptionSkyDown");
                style.down = skin.getDrawable("buttonUiOptionSkyDown");
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

                Save.load();
                if(Save.gd.getMoney()<MyGdxGame.getContinueMax()) {
                    /*Save.gd.setMoney(Save.gd.getMoney() + MyGdxGame.getContinueStorePack());
                    Save.save();
                    Save.load();*/
                    MyGdxGame.actionResolver.purchaseExtraCoins();
                    labelMoney.setText(Integer.toString(Save.gd.getMoney()));
                }else{
                    if(MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getSound("error").play();

                }
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
                    MyGdxGame.actionResolver.loginGPGS();
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


        InputMultiplexer im = new InputMultiplexer();
        im.addProcessor(stageUiOption);
        im.addProcessor(stage1);
        Gdx.input.setInputProcessor(im);

        //MyGdxGame.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),stages);

        //Gdx.input.setInputProcessor(stageUiOption);
        MyGdxGame.setIsBoosTerritory(false);

    }

    public void updateSelector(){
        if(PBlue) Save.gd.setSelector("blue");
        if(PRed)  Save.gd.setSelector("red");
        if(PYellow) Save.gd.setSelector("yellow");
        if(playerIsTouched) Save.gd.setSelector("green");
        if(!PBlue && !PRed && !PYellow && !playerIsTouched) Save.gd.setSelector("random");
        Gdx.app.debug(LOG_TAG,"*SELECTOR* "+"PBlue:"+ PBlue +" PRed:"+ PRed +" PYellow:"+ PYellow +" playerIsTouched:"+ playerIsTouched);
        Save.save();
    }

    public void handleInput() {

        hideUiOption();
        showMainUi();

        if (click_on_play) {
            if(MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) {
                //MyGdxGame.res.getMusic("newScreen").play();
            }



        }
        if (click_on_leaderboard) {
            click_on_leaderboard = false;
            if (MyGdxGame.actionResolver.getSignedInGPGS())
                MyGdxGame.actionResolver.getLeaderboardGPGS();
            else
                MyGdxGame.actionResolver.loginGPGS();
        }
        if (click_on_tuto) {
            click_on_tuto = false;
            gsm.setState(GameStateManager.TUTO);
            if(MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2)MyGdxGame.res.getMusic("newScreen").play();
        }

        if(click_on_credits){
            click_on_credits = false;
            gsm.setState(GameStateManager.CREDITS);
            if(MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2)MyGdxGame.res.getMusic("newScreen").play();
        }

        if(click_on_shop){
            click_on_shop = false;
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



        if (click_on_play){
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


        labelMoney.setText(Integer.toString(Save.gd.getMoney()));

        if(click_on_credits) {
            buttonPBlue.setScale(0);
            buttonPRed.setScale(0);
            buttonPYellow.setScale(0);
            buttonPGreen.setScale(0);
        }else{
            buttonPBlue.setScale(1);
            buttonPRed.setScale(1);
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
        viewport = new Rectangle(crop.x, 0, w, h);
        //Gdx.gl.glViewport((int) viewport.x, (int) viewport.y, (int) viewport.width, (int) viewport.height);

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
        stage2.getViewport().update((int) (width - offsetX), (int) (height - offsetY), true);

    }


    public void render() {

        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        //cpt_translate_animation_title++;


        //Gdx.gl.glClearColor(65f / 255f, 18f / 255f, 252f / 255f, 1);

        sb.setProjectionMatrix(cam.combined);
        shapeRenderer.setProjectionMatrix(cam.combined);

        //MyGdxGame.background_skyDay.render(sb);
        MyGdxGame.background_cloud.render(sb);
        MyGdxGame.background_wood1.render(sb);
        MyGdxGame.background_title.render(sb);

        sb2.begin();

        if (click_on_credits) {
            stage1.getActors().items[5].setVisible(true);
            stage1.getActors().items[5].draw(sb2, 1f);
            stage1.getActors().items[0].setVisible(false);
            stage1.getActors().items[1].setVisible(false);
            stage1.getActors().items[3].setVisible(false);
            stage1.getActors().items[4].setVisible(false);
            stage1.getActors().items[2].setVisible(false);

        } else {

            //SOUND BUTTON
            sb3.begin();

            float speed = 8f;

            if (stage1.getActors().items[4].getRight() >= Gdx.graphics.getWidth() + 5) {
                stage1.getActors().items[4].setPosition((Gdx.graphics.getWidth() / 1f) - (cpt_translate_animation * speed), stage1.getActors().items[4].getY());
                cpt_translate_animation++;
            }

            stage1.getActors().items[4].setVisible(true);
            stage1.getActors().items[4].draw(sb3, 1f);


            //GEAR BUTTON
            if (stage1.getActors().items[11].getX() <= -5)
                stage1.getActors().items[11].setPosition(-stage1.getActors().items[11].getWidth() + cpt_translate_animation * speed, stage1.getActors().items[11].getY());

            stage1.getActors().items[11].draw(sb3, 1f);
            sb3.end();

            //WINDOW CREDITS
            stage1.getActors().items[5].setVisible(false);

            cpt_blink++;
            float period = 100;
            if (cpt_blink >= period) {
                cpt_blink = 0;
                blink = !blink;
            }

            if (blink) blinking_text_alpha += (1f / period);
            else blinking_text_alpha -= (1f / period);

            //PLAYER BUTTONS
            stage1.getActors().items[7].draw(sb2, 1f);
            stage1.getActors().items[8].draw(sb2, 1f);
            stage1.getActors().items[9].draw(sb2, 1f);
            stage1.getActors().items[10].draw(sb2, 1f);

            sb3.begin();
            //BUTTON PLAY
            stage1.getActors().items[0].setVisible(true);
            stage1.getActors().items[0].setPosition(-stage1.getActors().items[0].getWidth() + Gdx.graphics.getWidth()/20 +cpt_translate_animation * speed, stage1.getActors().items[0].getY());
            stage1.getActors().items[0].draw(sb3, 1f);

            //BUTTON LEADERBOARD
            stage1.getActors().items[1].setVisible(true);
            stage1.getActors().items[1].setPosition((Gdx.graphics.getWidth() / 1f) - (Gdx.graphics.getWidth()/20+cpt_translate_animation * speed), stage1.getActors().items[1].getY());
            stage1.getActors().items[1].draw(sb3, 1f);

            //BUTTON CREDITS
            stage1.getActors().items[3].setVisible(true);
            stage1.getActors().items[3].draw(sb3, 1f);

            //BUTTON HOW TO
            stage1.getActors().items[2].setVisible(true);
            stage1.getActors().items[2].draw(sb3, Math.abs(blinking_text_alpha));
            sb3.end();

            //TEXT HOW TO ANIM
            if (stage1.getActors().items[2].getTop() <= Gdx.graphics.getHeight() / 7.5f) {
                stage1.getActors().items[2].setPosition((Gdx.graphics.getWidth() / 2f) - stage1.getActors().items[2].getWidth() / 2f, (-stage1.getActors().items[2].getHeight() + cpt_translate_animation1 * 10f));
                cpt_translate_animation1++;
            } else {
                start2 = true;
            }

            //TEXT CREDIT ANIM
            if (start2 && (stage1.getActors().items[3].getTop() <= Gdx.graphics.getHeight() / 19.5f)) {
                stage1.getActors().items[3].setPosition((Gdx.graphics.getWidth() / 2f) - stage1.getActors().items[3].getWidth() / 2f, (-stage1.getActors().items[3].getHeight() + cpt_translate_animation2 * 8f));
                cpt_translate_animation2++;
            }

            sb.begin();

            float w = 111*5f;
            float h = 44*5f;

            //sb.draw(animTitle.getFrame(), MyGdxGame.V_WIDTH/2 - w/2, 670.0f+ posX/100f , w, h);

            if(left2){
                y+=0.3f;
            } else{
                y-=0.3f;
            }

//            float titley = Gdx.graphics.getHeight() - cpt_translate_animation_title;
//            if(jp){
//                w = w*1.1f;
//                if(titley > 700){
//                    cpt_translate_animation_title+=20 ;
//                    sb.draw(animTitle.getFrame(), MyGdxGame.V_WIDTH/2 - w/2, Gdx.graphics.getHeight() - cpt_translate_animation_title, MyGdxGame.V_WIDTH/2, 670.0f -95/2 ,  w, h,1,1, 0);
//                }else{
//                    sb.draw(animTitle.getFrame(), MyGdxGame.V_WIDTH/2 - w/2, Gdx.graphics.getHeight() - cpt_translate_animation_title, MyGdxGame.V_WIDTH/2, 670.0f -95/2 ,  w, h,1,1, 0);
//                }
//            } else{
//                if(titley > 700){
//                    cpt_translate_animation_title+=20;
//                    sb.draw(animTitle.getFrame(), MyGdxGame.V_WIDTH/2 - w/2,  Gdx.graphics.getHeight() - cpt_translate_animation_title, MyGdxGame.V_WIDTH/2, 670.0f -95/2 ,  w, h,1,1, 0);
//                }else{
//                    sb.draw(animTitle.getFrame(), MyGdxGame.V_WIDTH/2 - w/2,  Gdx.graphics.getHeight() - cpt_translate_animation_title, MyGdxGame.V_WIDTH/2, 670.0f -95/2 ,  w, h,1,1, 0);
//                }
//            }

            if(jp){
                w = w*1.1f;
                sb.draw(animTitle.getFrame(), MyGdxGame.V_WIDTH/2 - w/2, MyGdxGame.V_HEIGHT - h*1.2f , MyGdxGame.V_WIDTH/2, 670.0f -95/2 ,  w, h,1,1, 0);
            } else{
                sb.draw(animTitle.getFrame(), MyGdxGame.V_WIDTH/2 - w/2, MyGdxGame.V_HEIGHT - h*1.2f , MyGdxGame.V_WIDTH/2, 670.0f -95/2 ,  w, h,1,1, 0);
            }

            if(posX+(96/2) >= MyGdxGame.V_WIDTH){
                left = true;
                right = false;
            }

            if (posX <= 0){
                right = true;
                left = false;
            }


            if(posX2 >= 380){
                right2 = true;
                left2 = false;
            }

            if (posX2 <= 200){
                left2 = true;
                right2 = false;
            }


            if(right){
                posX+=3.0f;
                sb.draw(animPlayerIdle.getFrame(), posX , 202);
            }
            if(left){
                posX-=3.0f;
                sb.draw(animPlayerIdleFliped.getFrame(), posX , 202);
            }

            if(right2){
                posX2-=2;
                sb.draw(animPrincessIdle.getFrame(), posX2 , 458);
            }
            if(left2){
                posX2+=2;
                sb.draw(animPrincessIdleFliped.getFrame(), posX2 , 458);
            }


            sb.end();
        }

        sb2.end();

        if(click_on_play){
            Gdx.gl.glViewport(0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
            MyGdxGame.fadeOut.render(sb2);
        }


    }

    public void dispose() {
        sb2.dispose();
        sb3.dispose();

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
        buttonPlay.setVisible(true);
        buttonLeaderBoard.setVisible(true);
        buttonGear.setVisible(true);
        buttonTuto.setVisible(true);
        buttonSound.setVisible(true);
        buttonCredits.setVisible(true);
        buttonPBlue.setVisible(true);
        buttonPRed.setVisible(true);
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
