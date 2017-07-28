package com.mygdx.core.states;

import com.badlogic.gdx.Gdx;
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
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.mygdx.core.MyGdxGame;
import com.mygdx.core.handlers.Animation;
import com.mygdx.core.handlers.GameStateManager;
import com.mygdx.core.handlers.Save;

import static com.mygdx.core.MyGdxGame.animHappyGameOver;
import static com.mygdx.core.MyGdxGame.animSadGameOver;
import static com.mygdx.core.MyGdxGame.animTitleGameOver;
import static com.mygdx.core.MyGdxGame.backgroundGameOver;
import static com.mygdx.core.MyGdxGame.buttonAdsGameOver;
import static com.mygdx.core.MyGdxGame.buttonEnergie;
import static com.mygdx.core.MyGdxGame.buttonExitGameOver;
import static com.mygdx.core.MyGdxGame.buttonShop;
import static com.mygdx.core.MyGdxGame.buttonReplayGameOver;
import static com.mygdx.core.MyGdxGame.buttonStar;
import static com.mygdx.core.MyGdxGame.buttonStyleAdsGameOver;
import static com.mygdx.core.MyGdxGame.buttonStyleEnergieGameOver;
import static com.mygdx.core.MyGdxGame.buttonStyleHeartGameOver;
import static com.mygdx.core.MyGdxGame.buttonStyleLeaderboardGameOver;
import static com.mygdx.core.MyGdxGame.buttonStylePlayGameOver;
import static com.mygdx.core.MyGdxGame.buttonStyleStarGameOver;
import static com.mygdx.core.MyGdxGame.click1GameOver;
import static com.mygdx.core.MyGdxGame.click2GameOver;
import static com.mygdx.core.MyGdxGame.cpt_scoreGameOver;
import static com.mygdx.core.MyGdxGame.cpt_timerGameOver;
import static com.mygdx.core.MyGdxGame.cpt_translate_animation2GameOver;
import static com.mygdx.core.MyGdxGame.cpt_translate_animation3GameOver;
import static com.mygdx.core.MyGdxGame.cpt_translate_animationGameOver;
import static com.mygdx.core.MyGdxGame.fadeGameOver;
import static com.mygdx.core.MyGdxGame.highScores;
import static com.mygdx.core.MyGdxGame.introGameOver;
import static com.mygdx.core.MyGdxGame.labelBestScoreGameOver;
import static com.mygdx.core.MyGdxGame.labelScoreGameOver;
import static com.mygdx.core.MyGdxGame.offsetGameOver;
import static com.mygdx.core.MyGdxGame.scoreGameOver;
import static com.mygdx.core.MyGdxGame.score_offsetGameOver;
import static com.mygdx.core.MyGdxGame.skinAds;
import static com.mygdx.core.MyGdxGame.skinEnergie;
import static com.mygdx.core.MyGdxGame.skinHeart;
import static com.mygdx.core.MyGdxGame.skinLeaderboardGameOver;
import static com.mygdx.core.MyGdxGame.skinPlayGameOver;
import static com.mygdx.core.MyGdxGame.skinStar;
import static com.mygdx.core.MyGdxGame.stage0GameOver;
import static com.mygdx.core.MyGdxGame.stage1GameOver;
import static com.mygdx.core.MyGdxGame.timerIsOnGameOver;
import static com.mygdx.core.MyGdxGame.trophy_empty;
import static com.mygdx.core.MyGdxGame.viewportGameOver;


public class GameOver extends GameState {


    public static String padRight(String s, int n) {
        return String.format("%1$-" + n + "s", s);
    }

    public GameOver(final GameStateManager gsm) {

        super(gsm);

        //game.actionResolver.hideBannerAd();
        MyGdxGame.lastPlayerPosition.set(0,0);
        if(introGameOver == null)
            introGameOver = new Image(MyGdxGame.atlas.findRegion("backgroundSky"));
        introGameOver.setFillParent(true);

        if(stage0GameOver == null){
            stage0GameOver = new Stage();

            stage0GameOver.addActor(introGameOver);
        }

        MyGdxGame.continueCount = 0;

        if(animTitleGameOver == null)
            animTitleGameOver = new Animation(new Sprite(MyGdxGame.atlas.findRegion("game_over")).split(59,47)[0], 1 / 5f);

        MyGdxGame.lastPlayerPosition.set(0,0);
        MyGdxGame.lastBrickPosition.set(0,0);
        MyGdxGame.lastPrincessPosition.set(0,0);

        Sprite tex = null;
        if(Save.gd.isExcaliburEquiped()){
            tex = new Sprite(MyGdxGame.atlas.findRegion("sadEx"));
        }else{
            tex = new Sprite(MyGdxGame.atlas.findRegion("sad"));
        }

        TextureRegion[] sprites = tex.split(110, 168)[0];
        animSadGameOver = new Animation(sprites, 1/5f);


        if(Save.gd.isExcaliburEquiped()){
            tex = new Sprite(MyGdxGame.atlas.findRegion("happyEx"));
        }else{
            tex = new Sprite(MyGdxGame.atlas.findRegion("happy"));
        }

        sprites = tex.split(132, 169)[0];
        animHappyGameOver = new Animation(sprites, 1/5f);


        if(viewportGameOver == null){
            viewportGameOver = new Rectangle();
            stage1GameOver = new Stage();
        }

        click1GameOver = false;
        click2GameOver = false;
        timerIsOnGameOver = true;


        Save.load();
        /*if (!Save.gd.getAdsRemoverPurchased()) {
            String network = game.actionResolver.getNetworkClass();
            if(network == null) network = "ABSENT";
            Gdx.app.debug(LOG_TAG,"NETWORK: "+network);
            if(network.equals("4G")|network.equals("3G")|network.equals("WIFI"))
                game.actionResolver.showOrLoadBanner();
        }*/
        highScores = Save.gd.getHighScores();
        if(MyGdxGame.actionResolver != null){
            if (game.actionResolver.getSignedInGPGS()) {
                game.actionResolver.getLeaderboardGPGS(true,(int) highScores[0]);
            }
        }
        Save.gd.getNames();
        scoreGameOver = Save.gd.getTentativeScore();

        MyGdxGame.background_cloud.setVector(+10, 0);
        //MyGdxGame.background_skyDay.setVector(0, 0);
        MyGdxGame.background_wood1.setVector(0, 0);

        Sprite tex_background = null;

        if (Save.gd.getNewHighScore()) {
            tex_background = new Sprite(MyGdxGame.atlas.findRegion("backgroundTitle"));
            //if(MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getMusic("success").play();

        } else {

            if(scoreGameOver == 100) {
                //if(MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getMusic("success").play();
            }else {
                //tex_background = new Sprite(MyGdxGame.atlas.findRegion("background_game_over"));
                tex_background = new Sprite(MyGdxGame.atlas.findRegion("backgroundTitle"));
                if(MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) {
                    MyGdxGame.res.getMusic("death").play();
                    MyGdxGame.res.getMusic("death").setVolume(1);
                    //MyGdxGame.res.getMusic("game_over").play();
                }
            }
        }

        if (scoreGameOver == 100)
            tex_background = new Sprite(MyGdxGame.atlas.findRegion("backgroundThanksYou"));


//        Sprite tex_trophy_bronze = new Sprite(MyGdxGame.atlas.findRegion("coin"));
//        Sprite tex_trophy_silver = new Sprite(MyGdxGame.atlas.findRegion("coin"));
//        Sprite tex_trophy_gold = new Sprite(MyGdxGame.atlas.findRegion("coin"));



        if(trophy_empty == null){
            Sprite tex_trophy_empty = new Sprite(MyGdxGame.atlas.findRegion("coin"));
            trophy_empty = new Image(tex_trophy_empty);
            trophy_empty.setHeight(Gdx.graphics.getHeight() / 6.9f);
            trophy_empty.setWidth(Gdx.graphics.getWidth() / 4f);
            trophy_empty.setPosition(trophy_empty.getWidth() / 2 + (Gdx.graphics.getWidth() - trophy_empty.getWidth()) / 2, (Gdx.graphics.getHeight() - trophy_empty.getWidth()) / 2);
            trophy_empty.setVisible(false);
            stage1GameOver.addActor(trophy_empty);
        }


//        trophy_bronze = new Image(tex_trophy_bronze);
//        trophy_bronze.setHeight(Gdx.graphics.getHeight() / 6.9f);
//        trophy_bronze.setWidth(Gdx.graphics.getWidth() / 4f);
//        trophy_bronze.setPosition(trophy_bronze.getWidth() / 2 + (Gdx.graphics.getWidth() - trophy_bronze.getWidth()) / 2, (Gdx.graphics.getHeight() - trophy_bronze.getWidth()) / 2);
//
//        trophy_silver = new Image(tex_trophy_silver);
//        trophy_silver.setHeight(Gdx.graphics.getHeight() / 6.9f);
//        trophy_silver.setWidth(Gdx.graphics.getWidth() / 4f);
//        trophy_silver.setPosition(trophy_silver.getWidth() / 2 + (Gdx.graphics.getWidth() - trophy_silver.getWidth()) / 2, (Gdx.graphics.getHeight() - trophy_silver.getWidth()) / 2);
//
//        trophy_gold = new Image(tex_trophy_gold);
//        trophy_gold.setHeight(Gdx.graphics.getHeight() / 6.9f);
//        trophy_gold.setWidth(Gdx.graphics.getWidth() / 4f);
//        trophy_gold.setPosition(trophy_gold.getWidth() / 2 + (Gdx.graphics.getWidth() - trophy_gold.getWidth()) / 2, (Gdx.graphics.getHeight() - trophy_gold.getWidth()) / 2);


//        if (scoreGameOver < 10)
//            stage1GameOver.addActor(trophy_empty);
//        if (scoreGameOver >= 10 && scoreGameOver < 40)
//            stage1GameOver.addActor(trophy_bronze);
//        if (scoreGameOver >= 40 && scoreGameOver < 70)
//            stage1GameOver.addActor(trophy_silver);
//        if (scoreGameOver >= 70)
//            stage1GameOver.addActor(trophy_gold);

        /*String score1 = "SCORE";
        String score1_padded = String.format("%5s", score1);
        String score2 = "0";
        String score2_padded = String.format("%5s", score2);
        String best1 = "BEST";
        String best1_padded = String.format("%4s", best1);
        String best2 = String.valueOf(highScores[0]);
        String best2_padded = String.format("%7s", best2);*/


        if(labelScoreGameOver == null){
            BitmapFont font = new BitmapFont(Gdx.files.internal(MyGdxGame.fontScorePath), false);
            labelScoreGameOver = new Label("SCORE  "+String.valueOf(scoreGameOver), new LabelStyle(font, Color.valueOf("545454") )) ;
            labelBestScoreGameOver = new Label("SCORE  "+String.valueOf(scoreGameOver)+"      "+"BEST SCORE  "+String.valueOf(highScores[0]), new LabelStyle(font,Color.valueOf("FFFFFF")));
        }else{
            labelScoreGameOver.setText("SCORE  "+String.valueOf(scoreGameOver));
            labelBestScoreGameOver.setText("SCORE  "+String.valueOf(scoreGameOver)+"      "+"BEST SCORE  "+String.valueOf(highScores[0]));
        }

        float fScale = Gdx.graphics.getWidth() / 400f;
        labelScoreGameOver.setFontScale(fScale);
        labelScoreGameOver.setAlignment(Align.center);
        labelScoreGameOver.setPosition(0, 0+ score_offsetGameOver *2);
        labelScoreGameOver.setWidth(Gdx.graphics.getWidth());
        labelScoreGameOver.setHeight(Gdx.graphics.getHeight());

        labelBestScoreGameOver.setFontScale(fScale);
        labelBestScoreGameOver.setWidth(Gdx.graphics.getWidth());
        labelBestScoreGameOver.setHeight(Gdx.graphics.getHeight());
        labelBestScoreGameOver.setPosition(0, 0);
        labelBestScoreGameOver.setAlignment(Align.center);

        if (scoreGameOver >= 10) game.actionResolver.unlockAchievementGPGS(MyGdxGame.achievement1);
        if (scoreGameOver >= 25) game.actionResolver.unlockAchievementGPGS(MyGdxGame.achievement2);
        if (scoreGameOver >= 50) game.actionResolver.unlockAchievementGPGS(MyGdxGame.achievement3);
        if (scoreGameOver >= 80) game.actionResolver.unlockAchievementGPGS(MyGdxGame.achievement4);
        if (scoreGameOver >= 100) game.actionResolver.unlockAchievementGPGS(MyGdxGame.achievement5);

        if(backgroundGameOver == null){
            backgroundGameOver = new Image(MyGdxGame.atlas.findRegion("buttonSecret"));


            backgroundGameOver.setHeight(0);
            backgroundGameOver.setWidth(0);

            stage1GameOver.addActor(backgroundGameOver);
            labelScoreGameOver.setVisible(false);
            stage1GameOver.addActor(labelScoreGameOver);
            labelBestScoreGameOver.setVisible(true);
            stage1GameOver.addActor(labelBestScoreGameOver);

            fadeGameOver = new AlphaAction();
            fadeGameOver.setDuration(0.3f);

            skinPlayGameOver = new Skin();
            skinLeaderboardGameOver = new Skin();
            skinHeart = new Skin();
            skinStar = new Skin();
            skinEnergie = new Skin();
            skinAds = new Skin();

            skinPlayGameOver.addRegions(MyGdxGame.atlas);
            buttonStylePlayGameOver = new TextButtonStyle();
            buttonStylePlayGameOver.up = skinPlayGameOver.getDrawable("buttonExitUp");
            buttonStylePlayGameOver.down = skinPlayGameOver.getDrawable("buttonExitDown");
            buttonExitGameOver = new Button(buttonStylePlayGameOver);
            buttonExitGameOver.setWidth(Gdx.graphics.getWidth() / 2.2f);
            buttonExitGameOver.setHeight(Gdx.graphics.getHeight() / 5f);
            buttonExitGameOver.setPosition(-400, Gdx.graphics.getHeight() / 200f);

            skinLeaderboardGameOver.addRegions(MyGdxGame.atlas);
            buttonStyleLeaderboardGameOver = new TextButtonStyle();
            buttonStyleLeaderboardGameOver.up = skinLeaderboardGameOver.getDrawable("buttonReplayUp");
            buttonStyleLeaderboardGameOver.down = skinLeaderboardGameOver.getDrawable("buttonReplayDown");
            buttonReplayGameOver = new Button(buttonStyleLeaderboardGameOver);
            buttonReplayGameOver.setWidth(Gdx.graphics.getWidth() / 2.2f);
            buttonReplayGameOver.setHeight(Gdx.graphics.getHeight() / 5f);
            buttonReplayGameOver.setPosition((Gdx.graphics.getWidth() / 1) + 20, Gdx.graphics.getHeight() / 200f);

            skinHeart.addRegions(MyGdxGame.atlas);
            buttonStyleHeartGameOver = new TextButtonStyle();
            buttonStyleHeartGameOver.up = skinHeart.getDrawable("buttonGearFlipUp");
            buttonStyleHeartGameOver.down = skinHeart.getDrawable("buttonGearFlipDown");
            buttonShop = new Button(buttonStyleHeartGameOver);
            buttonShop.setWidth(Gdx.graphics.getWidth() / 5f);
            buttonShop.setHeight(Gdx.graphics.getHeight() / 7.8f);
            buttonShop.setPosition((Gdx.graphics.getWidth() / 1f) - buttonShop.getWidth() + 20, Gdx.graphics.getHeight() / 3.5f);

            skinStar.addRegions(MyGdxGame.atlas);
            buttonStyleStarGameOver = new TextButtonStyle();
            buttonStyleStarGameOver.up = skinStar.getDrawable("buttonPromoteUp");
            buttonStyleStarGameOver.down = skinStar.getDrawable("buttonPromoteDown");
            buttonStar = new Button(buttonStyleStarGameOver);
            buttonStar.setWidth(Gdx.graphics.getWidth() / 5f);
            buttonStar.setHeight(Gdx.graphics.getHeight() / 7.8f);
            buttonStar.setPosition((Gdx.graphics.getWidth() / 1f) - buttonStar.getWidth() + 20, buttonShop.getY() - buttonShop.getHeight());

            skinEnergie.addRegions(MyGdxGame.atlas);
            buttonStyleEnergieGameOver = new TextButtonStyle();
            buttonStyleEnergieGameOver.up = skinEnergie.getDrawable("buttonPromoteUp");
            buttonStyleEnergieGameOver.down = skinEnergie.getDrawable("buttonPromoteDown");
            buttonEnergie = new Button(buttonStyleEnergieGameOver);
            buttonEnergie.setWidth(Gdx.graphics.getWidth() / 5f);
            buttonEnergie.setHeight(Gdx.graphics.getHeight() / 7.8f);
            buttonEnergie.setPosition(-400, Gdx.graphics.getHeight() / 3.5f);

            buttonStyleAdsGameOver = new TextButtonStyle();
            skinAds.addRegions(MyGdxGame.atlas);
            buttonStyleAdsGameOver.up = skinAds.getDrawable("buttonSecret");
            buttonStyleAdsGameOver.down = skinAds.getDrawable("buttonSecret");
            buttonAdsGameOver = new Button(buttonStyleAdsGameOver);
            buttonAdsGameOver.setWidth(Gdx.graphics.getWidth() / 5f);
            buttonAdsGameOver.setHeight(Gdx.graphics.getHeight() / 7.8f);
            buttonAdsGameOver.setPosition(-400, buttonShop.getY());

            stage1GameOver.addActor(buttonExitGameOver);
            stage1GameOver.addActor(buttonReplayGameOver);
            //buttonShop.setVisible(false);
            stage1GameOver.addActor(buttonShop);
            buttonStar.setVisible(false);
            stage1GameOver.addActor(buttonStar);
            //buttonEnergie.setVisible(false);
            stage1GameOver.addActor(buttonEnergie);
            stage1GameOver.addActor(buttonAdsGameOver);

            //batch = new SpriteBatch();
            if(MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getMusic("newScreen").play();

            buttonExitGameOver.addListener(new InputListener() {
                public boolean touchDown(
                        com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                        float y, int pointer, int button) {
                    if(MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getSound("select").play();
                    if(MyGdxGame.res.getMusic("death").isPlaying()) MyGdxGame.res.getMusic("death").pause();
                    return true;
                };

                public void touchUp(
                        com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                        float y, int pointer, int button) {
                    click1GameOver = true;
                    //game.actionResolver.hideBannerAd();
                };
            });

            buttonReplayGameOver.addListener(new InputListener() {
                public boolean touchDown(
                        com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                        float y, int pointer, int button) {
                    if(MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getSound("select").play();
                    //if (MyGdxGame.isSoundEnable()) MyGdxGame.res.getMusic("main").setVolume(0.15f);
                    return true;
                }

                ;

                public void touchUp(
                        com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                        float y, int pointer, int button) {
                    click2GameOver = true;
                    //game.actionResolver.hideBannerAd();
                }

                ;
            });

            buttonShop.addListener(new InputListener() {
                public boolean touchDown(
                        com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                        float y, int pointer, int button) {
                    if(MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getSound("select").play();

                    return true;
                };

                public void touchUp(
                        com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                        float y, int pointer, int button) {
                    //Gdx.net.openURI(MyGdxGame.AndroidPlayStoreGameUrl);
                    gsm.setState(GameStateManager.SHOP);
                };
            });

            buttonStar.addListener(new InputListener() {
                public boolean touchDown(
                        com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                        float y, int pointer, int button) {
                    if(MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getSound("select").play();
                    MyGdxGame.actionResolver.shareOnFacebook();
                    return true;
                }
                ;

                public void touchUp(
                        com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                        float y, int pointer, int button) {
                };
            });

            buttonEnergie.addListener(new InputListener() {
                public boolean touchDown(
                        com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                        float y, int pointer, int button) {
                    if(MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getSound("select").play();
                    return true;
                };

                public void touchUp(
                        com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                        float y, int pointer, int button) {
                    //MyGdxGame.actionResolver.purchaseFullBar();
                    Gdx.net.openURI("http://ram52.com");
                };
            });

            buttonAdsGameOver.addListener(new InputListener() {
                public boolean touchDown(
                        com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                        float y, int pointer, int button) {
                    if(MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getSound("select").play();

                    return true;
                };

                public void touchUp(
                        com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                        float y, int pointer, int button) {

                };
            });
        }

        Gdx.input.setInputProcessor(stage1GameOver);


        cpt_translate_animationGameOver = 0;
        cpt_translate_animation2GameOver = 0;
        cpt_translate_animation3GameOver = 0;
        //Gdx.gl.glClearColor(31f / 255f, 169f / 255f, 180f / 255f, 1);
    }

    public void handleInput() {
        // mouse/touch input
        if (click1GameOver) {
            click1GameOver = false;
            gsm.setState(GameStateManager.MENU);
            MyGdxGame.setStartCameraMotion(true);
            if(MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getMusic("newScreen").play();
            //if(MyGdxGame.res.getMusic("game_over").isPlaying()) MyGdxGame.res.getMusic("game_over").stop();
            if(MyGdxGame.res.getMusic("success").isPlaying()) MyGdxGame.res.getMusic("success").pause();
            //Gdx.gl.glClearColor(0, 0, 0, 1);
        }
        if (click2GameOver) {

            click2GameOver = false;
            gsm.setState(GameStateManager.PLAY);
            MyGdxGame.setStartCameraMotion(false);
            if(MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getMusic("newScreen").play();
            //if(MyGdxGame.res.getMusic("game_over").isPlaying()) MyGdxGame.res.getMusic("game_over").stop();
            if(MyGdxGame.res.getMusic("success").isPlaying()) MyGdxGame.res.getMusic("success").pause();
            if(MyGdxGame.res.getMusic("death").isPlaying()) MyGdxGame.res.getMusic("death").pause();
            /*if (MyGdxGame.actionResolver.getSignedInGPGS())
                MyGdxGame.actionResolver.getLeaderboardGPGS();
            else
                MyGdxGame.actionResolver.loginGPGS();*/
        }

    }

    public void update(float dt) {


        if(MyGdxGame.isSoundEnable() == 2){
            if(!MyGdxGame.res.getMusic("game_over").isPlaying() && !MyGdxGame.res.getMusic("death").isPlaying() ){
                MyGdxGame.res.getMusic("game_over").setVolume(1.0f);
                MyGdxGame.res.getMusic("game_over").play();
            }
        }

        //MyGdxGame.updateBGM();
        handleInput();
        MyGdxGame.background_wood1.update(dt);
        MyGdxGame.background_cloud.update(dt);
        //MyGdxGame.background_skyDay.update(dt);

        animSadGameOver.update(dt);
        animHappyGameOver.update(dt);
    }

    public void resize(int width, int height) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // calculate new viewportGameOver
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
        viewportGameOver = new Rectangle(crop.x, 0, w, h);
        //Gdx.gl.glViewport((int) viewportGameOver.x, (int) viewportGameOver.yMenu, (int) viewportGameOver.width, (int) viewportGameOver.height);
        float offsetY = crop.y;
        float offsetX = crop.x;
        offsetGameOver = offsetY;

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glViewport(0,0, (int) viewportGameOver.width+ (int)offsetX, Gdx.graphics.getHeight());
        stage0GameOver.act();
        sb.begin();
        stage0GameOver.draw();
        sb.end();
        Gdx.gl.glViewport((int) viewportGameOver.x, (int) viewportGameOver.y, (int) viewportGameOver.width - (int)offsetX, (int) viewportGameOver.height - (int)offsetY);

        stage1GameOver.getViewport().update((int) (width - offsetX), (int) (height - offsetY), true);
    }

    public void render() {

        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        //MyGdxGame.background_skyDay.render(sb);
        MyGdxGame.background_wood1.render(sb);
        MyGdxGame.background_title.render(sb);

        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        float height = MyGdxGame.V_HEIGHT/15;
        shapeRenderer.rect(0, (MyGdxGame.V_HEIGHT - height)/2.0f  , MyGdxGame.V_WIDTH, height);
        shapeRenderer.setColor(Color.valueOf("f4f4f4"));
        //shapeRenderer.rect(0, ((MyGdxGame.V_HEIGHT - height)/2.0f)+score_offsetGameOver  , MyGdxGame.V_WIDTH, height);
        shapeRenderer.end();

        //labelBestScoreGameOver.setPosition(labelBestScoreGameOver.getX(),labelBestScoreGameOver.getY());


        stage1GameOver.act();
        sb.begin();
        stage1GameOver.draw();


        float speed = 8f;
        //heart button
        if (stage1GameOver.getActors().items[6].getRight() >= Gdx.graphics.getWidth() + 5) {
            stage1GameOver.getActors().items[6].setPosition((Gdx.graphics.getWidth() / 1f) - (cpt_translate_animationGameOver * speed), stage1GameOver.getActors().items[6].getY());
            cpt_translate_animationGameOver++;
        }
        //start button
        if (stage1GameOver.getActors().items[7].getRight() >= Gdx.graphics.getWidth() + 5)
            stage1GameOver.getActors().items[7].setPosition((Gdx.graphics.getWidth() / 1f) - cpt_translate_animationGameOver * speed, stage1GameOver.getActors().items[7].getY());

        //start buyWings
        if (Save.gd.getFullBarPurchased()) {
            buttonEnergie.setDisabled(true);
            if (stage1GameOver.getActors().items[8].getX() >= -500)
                stage1GameOver.getActors().items[8].setPosition(stage1GameOver.getActors().items[8].getX() - cpt_translate_animation3GameOver * 1, stage1GameOver.getActors().items[8].getY());
            cpt_translate_animation3GameOver++;
        } else {
            if (stage1GameOver.getActors().items[8].getX() <= -5)
                stage1GameOver.getActors().items[8].setPosition(-stage1GameOver.getActors().items[8].getWidth() + cpt_translate_animationGameOver * speed, stage1GameOver.getActors().items[8].getY());
        }
        //start adsRemover
        if (Save.gd.getAdsRemoverPurchased()) {
            //buttonAdsGameOver.setDisabled(true);
            if (stage1GameOver.getActors().items[9].getX() >= -500)
                stage1GameOver.getActors().items[9].setPosition(stage1GameOver.getActors().items[9].getX() - cpt_translate_animation2GameOver * 1, stage1GameOver.getActors().items[9].getY());
            cpt_translate_animation2GameOver++;
        } else {
            if (stage1GameOver.getActors().items[9].getX() <= -5)
                stage1GameOver.getActors().items[9].setPosition(-stage1GameOver.getActors().items[9].getWidth() + cpt_translate_animationGameOver * speed, stage1GameOver.getActors().items[9].getY());
        }


        //BUTTON PLAY
        stage1GameOver.getActors().items[4].setVisible(true);
        stage1GameOver.getActors().items[4].setPosition(-stage1GameOver.getActors().items[4].getWidth() + Gdx.graphics.getWidth()/20 + cpt_translate_animationGameOver * speed, stage1GameOver.getActors().items[4].getY());
        stage1GameOver.getActors().items[4].draw(sb, 1f);

        //BUTTON LEADERBOARD
        stage1GameOver.getActors().items[5].setVisible(true);
        stage1GameOver.getActors().items[5].setPosition((Gdx.graphics.getWidth() / 1f) - (Gdx.graphics.getWidth()/20+ cpt_translate_animationGameOver * speed), stage1GameOver.getActors().items[4].getY());
        stage1GameOver.getActors().items[5].draw(sb, 1f);


        sb.end();

        if (timerIsOnGameOver) {
            Timer.schedule(new Task() {
                               @Override
                               public void run() {
                                   if (cpt_timerGameOver <= 3) {
                                       cpt_timerGameOver++;
                                       if (cpt_scoreGameOver != 0)
                                           labelScoreGameOver.setText("SCORE  " + Integer.toString(cpt_scoreGameOver + 1));
                                   } else {
                                       cpt_timerGameOver = 0;
                                       cpt_scoreGameOver++;
                                   }
                                   if (cpt_scoreGameOver >= scoreGameOver)
                                       timerIsOnGameOver = false;
                               }
                           }, 0f // (delay)
            );
        } else {
            Timer.instance().clear();
            timerIsOnGameOver = false;
            cpt_timerGameOver = 0;
            // changeScreen();
        }

        float w = 59*6f;
        float h = 47*6f;
        sb.begin();
        sb.draw(animTitleGameOver.getFrame(), MyGdxGame.V_WIDTH/2 - w/2, MyGdxGame.V_HEIGHT - h*1.1f , MyGdxGame.V_WIDTH/2, 670.0f -95/2 ,  w, h,1,1, 0);

        if(scoreGameOver >=100){
            sb.draw(animHappyGameOver.getFrame(), MyGdxGame.V_WIDTH/2 - 132/2, 202);
        }else {
            sb.draw(animSadGameOver.getFrame(), MyGdxGame.V_WIDTH/2, 202);
        }

        sb.end();


    }

    public void dispose() {
        //sb.dispose();
        buttonExitGameOver.setPosition(-400, Gdx.graphics.getHeight() / 200f);
        buttonReplayGameOver.setPosition((Gdx.graphics.getWidth() / 1) + 20, Gdx.graphics.getHeight() / 200f);
        buttonStar.setPosition((Gdx.graphics.getWidth() / 1f) - buttonStar.getWidth() + 20, buttonShop.getY() - buttonShop.getHeight());
        buttonEnergie.setPosition(-400, Gdx.graphics.getHeight() / 3.5f);
        buttonShop.setPosition((Gdx.graphics.getWidth() / 1f) - buttonShop.getWidth() + 20, Gdx.graphics.getHeight() / 3.5f);


        cpt_timerGameOver = 0;
        cpt_scoreGameOver = 0;
        score_offsetGameOver = 100;
        offsetGameOver = 0;
        cpt_translate_animationGameOver = 0;
        cpt_translate_animation2GameOver = 0;
        cpt_translate_animation3GameOver = 0;
        if (MyGdxGame.res.getMusic("game_over").isPlaying()) {
            MyGdxGame.res.getMusic("game_over").pause();
            MyGdxGame.res.getMusic("game_over").stop();
        }
    }

}
