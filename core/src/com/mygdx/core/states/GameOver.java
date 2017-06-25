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
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
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

public class GameOver extends GameState {

    Stage stage1;
    Label labelScore, labelBestScore;
    ButtonStyle buttonStylePlay, buttonStyleLeaderboard, buttonStyleHeart, buttonStyleStar, buttonStyleEnergie, buttonStyleAds;
    Button buttonExit, buttonReplay, buttonHeart, buttonStar, buttonEnergie, buttonAds;
    Skin skinPlay, skinLeaderboard, skinHeart, skinStar, skinEnergie, skinAds;
    Image background, trophy_empty, trophy_bronze, trophy_silver, trophy_gold, high_score;
    AlphaAction fade;
    private long[] highScores;
    long score;
    boolean click1, click2, timerIsOn;
    Rectangle viewport;
    int cpt_timer = 0;
    int cpt_score = 0;
    int score_offset = 100;
    float offset = 0;
    int cpt_translate_animation = 0;
    int cpt_translate_animation2 = 0;
    int cpt_translate_animation3 = 0;
    private Animation animTitle, animSad, animHappy;
    private Stage stage0;
    private Image intro;

    public static String padRight(String s, int n) {
        return String.format("%1$-" + n + "s", s);
    }

    public GameOver(final GameStateManager gsm) {

        super(gsm);

        //game.actionResolver.hideBannerAd();
        MyGdxGame.lastPlayerPosition = new Vector2(0,0);
        intro = new Image(MyGdxGame.atlas.findRegion("backgroundSky"));
        intro.setFillParent(true);
        stage0 = new Stage();
        stage0.addActor(intro);
        MyGdxGame.continueCount = 0;

        animTitle = new Animation(new Sprite(MyGdxGame.atlas.findRegion("game_over")).split(59,47)[0], 1 / 5f);

        MyGdxGame.lastPlayerPosition = new Vector2(0,0);
        MyGdxGame.lastBrickPosition = new Vector2(0,0);
        MyGdxGame.lastPrincessPosition = new Vector2(0,0);

        Sprite tex = null;
        if(Save.gd.isExcaliburEquiped()){
            tex = new Sprite(MyGdxGame.atlas.findRegion("sadEx"));
        }else{
            tex = new Sprite(MyGdxGame.atlas.findRegion("sad"));
        }

        TextureRegion[] sprites = tex.split(110, 168)[0];
        animSad = new Animation(sprites, 1/5f);


        if(Save.gd.isExcaliburEquiped()){
            tex = new Sprite(MyGdxGame.atlas.findRegion("happyEx"));
        }else{
            tex = new Sprite(MyGdxGame.atlas.findRegion("happy"));
        }

        sprites = tex.split(132, 169)[0];
        animHappy = new Animation(sprites, 1/5f);


        viewport = new Rectangle();
        click1 = false;
        click2 = false;
        timerIsOn = true;
        stage1 = new Stage();

        Save.load();
        /*if (!Save.gd.getAdsRemoverPurchased()) {
            String network = game.actionResolver.getNetworkClass();
            if(network == null) network = "ABSENT";
            Gdx.app.debug(LOG_TAG,"NETWORK: "+network);
            if(network.equals("4G")|network.equals("3G")|network.equals("WIFI"))
                game.actionResolver.showOrLoadBanner();
        }*/
        highScores = Save.gd.getHighScores();
        if (game.actionResolver.getSignedInGPGS()) {
            game.actionResolver.submitScoreGPGS((int) highScores[0]);
        }
        Save.gd.getNames();
        score = Save.gd.getTentativeScore();

        MyGdxGame.background_cloud.setVector(+10, 0);
        //MyGdxGame.background_skyDay.setVector(0, 0);
        MyGdxGame.background_wood1.setVector(0, 0);

        Sprite tex_background = null;

        if (Save.gd.getNewHighScore()) {
            tex_background = new Sprite(MyGdxGame.atlas.findRegion("backgroundTitle"));
            //if(MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getMusic("success").play();

        } else {

            if(score == 100) {
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

        if (score == 100)
            tex_background = new Sprite(MyGdxGame.atlas.findRegion("backgroundThanksYou"));

        Sprite tex_trophy_empty = new Sprite(MyGdxGame.atlas.findRegion("coin"));
        Sprite tex_trophy_bronze = new Sprite(MyGdxGame.atlas.findRegion("coin"));
        Sprite tex_trophy_silver = new Sprite(MyGdxGame.atlas.findRegion("coin"));
        Sprite tex_trophy_gold = new Sprite(MyGdxGame.atlas.findRegion("coin"));

        background = new Image(MyGdxGame.atlas.findRegion("buttonSecret"));
        background.setHeight(0);
        background.setWidth(0);

        trophy_empty = new Image(tex_trophy_empty);
        trophy_empty.setHeight(Gdx.graphics.getHeight() / 6.9f);
        trophy_empty.setWidth(Gdx.graphics.getWidth() / 4f);
        trophy_empty.setPosition(trophy_empty.getWidth() / 2 + (Gdx.graphics.getWidth() - trophy_empty.getWidth()) / 2, (Gdx.graphics.getHeight() - trophy_empty.getWidth()) / 2);
        trophy_empty.setVisible(false);

        trophy_bronze = new Image(tex_trophy_bronze);
        trophy_bronze.setHeight(Gdx.graphics.getHeight() / 6.9f);
        trophy_bronze.setWidth(Gdx.graphics.getWidth() / 4f);
        trophy_bronze.setPosition(trophy_bronze.getWidth() / 2 + (Gdx.graphics.getWidth() - trophy_bronze.getWidth()) / 2, (Gdx.graphics.getHeight() - trophy_bronze.getWidth()) / 2);

        trophy_silver = new Image(tex_trophy_silver);
        trophy_silver.setHeight(Gdx.graphics.getHeight() / 6.9f);
        trophy_silver.setWidth(Gdx.graphics.getWidth() / 4f);
        trophy_silver.setPosition(trophy_silver.getWidth() / 2 + (Gdx.graphics.getWidth() - trophy_silver.getWidth()) / 2, (Gdx.graphics.getHeight() - trophy_silver.getWidth()) / 2);

        trophy_gold = new Image(tex_trophy_gold);
        trophy_gold.setHeight(Gdx.graphics.getHeight() / 6.9f);
        trophy_gold.setWidth(Gdx.graphics.getWidth() / 4f);
        trophy_gold.setPosition(trophy_gold.getWidth() / 2 + (Gdx.graphics.getWidth() - trophy_gold.getWidth()) / 2, (Gdx.graphics.getHeight() - trophy_gold.getWidth()) / 2);

        stage1.addActor(trophy_empty);
//        if (score < 10)
//            stage1.addActor(trophy_empty);
//        if (score >= 10 && score < 40)
//            stage1.addActor(trophy_bronze);
//        if (score >= 40 && score < 70)
//            stage1.addActor(trophy_silver);
//        if (score >= 70)
//            stage1.addActor(trophy_gold);

        /*String score1 = "SCORE";
        String score1_padded = String.format("%5s", score1);
        String score2 = "0";
        String score2_padded = String.format("%5s", score2);
        String best1 = "BEST";
        String best1_padded = String.format("%4s", best1);
        String best2 = String.valueOf(highScores[0]);
        String best2_padded = String.format("%7s", best2);*/

        BitmapFont font = new BitmapFont(Gdx.files.internal(MyGdxGame.fontScorePath), false);
        labelScore = new Label("SCORE  "+String.valueOf(score), new LabelStyle(font, Color.valueOf("545454") )) ;
        labelBestScore = new Label("SCORE  "+String.valueOf(score)+"      "+"BEST SCORE  "+String.valueOf(highScores[0]), new LabelStyle(font,Color.valueOf("FFFFFF")));

        float fScale = Gdx.graphics.getWidth() / 400f;
        labelScore.setFontScale(fScale);
        labelScore.setAlignment(Align.center);
        labelScore.setPosition(0, 0+score_offset*2);
        labelScore.setWidth(Gdx.graphics.getWidth());
        labelScore.setHeight(Gdx.graphics.getHeight());

        labelBestScore.setFontScale(fScale);
        labelBestScore.setWidth(Gdx.graphics.getWidth());
        labelBestScore.setHeight(Gdx.graphics.getHeight());
        labelBestScore.setPosition(0, 0);
        labelBestScore.setAlignment(Align.center);

        if (score >= 10) game.actionResolver.unlockAchievementGPGS(MyGdxGame.achievement1);
        if (score >= 25) game.actionResolver.unlockAchievementGPGS(MyGdxGame.achievement2);
        if (score >= 50) game.actionResolver.unlockAchievementGPGS(MyGdxGame.achievement3);
        if (score >= 80) game.actionResolver.unlockAchievementGPGS(MyGdxGame.achievement4);
        if (score >= 100) game.actionResolver.unlockAchievementGPGS(MyGdxGame.achievement5);


        stage1.addActor(background);
        labelScore.setVisible(false);
        stage1.addActor(labelScore);
        labelBestScore.setVisible(true);
        stage1.addActor(labelBestScore);

        fade = new AlphaAction();
        fade.setDuration(0.3f);

        skinPlay = new Skin();
        skinLeaderboard = new Skin();
        skinHeart = new Skin();
        skinStar = new Skin();
        skinEnergie = new Skin();
        skinAds = new Skin();

        skinPlay.addRegions(MyGdxGame.atlas);
        buttonStylePlay = new TextButtonStyle();
        buttonStylePlay.up = skinPlay.getDrawable("buttonExitUp");
        buttonStylePlay.down = skinPlay.getDrawable("buttonExitDown");
        buttonExit = new Button(buttonStylePlay);
        buttonExit.setWidth(Gdx.graphics.getWidth() / 2.2f);
        buttonExit.setHeight(Gdx.graphics.getHeight() / 5f);
        buttonExit.setPosition(-400, Gdx.graphics.getHeight() / 200f);

        skinLeaderboard.addRegions(MyGdxGame.atlas);
        buttonStyleLeaderboard = new TextButtonStyle();
        buttonStyleLeaderboard.up = skinLeaderboard.getDrawable("buttonReplayUp");
        buttonStyleLeaderboard.down = skinLeaderboard.getDrawable("buttonReplayDown");
        buttonReplay = new Button(buttonStyleLeaderboard);
        buttonReplay.setWidth(Gdx.graphics.getWidth() / 2.2f);
        buttonReplay.setHeight(Gdx.graphics.getHeight() / 5f);
        buttonReplay.setPosition((Gdx.graphics.getWidth() / 1) + 20, Gdx.graphics.getHeight() / 200f);

        skinHeart.addRegions(MyGdxGame.atlas);
        buttonStyleHeart = new TextButtonStyle();
        buttonStyleHeart.up = skinHeart.getDrawable("buttonGearFlipUp");
        buttonStyleHeart.down = skinHeart.getDrawable("buttonGearFlipDown");
        buttonHeart = new Button(buttonStyleHeart);
        buttonHeart.setWidth(Gdx.graphics.getWidth() / 5f);
        buttonHeart.setHeight(Gdx.graphics.getHeight() / 7.8f);
        buttonHeart.setPosition((Gdx.graphics.getWidth() / 1f) - buttonHeart.getWidth() + 20, Gdx.graphics.getHeight() / 3.5f);

        skinStar.addRegions(MyGdxGame.atlas);
        buttonStyleStar = new TextButtonStyle();
        buttonStyleStar.up = skinStar.getDrawable("buttonPromoteUp");
        buttonStyleStar.down = skinStar.getDrawable("buttonPromoteDown");
        buttonStar = new Button(buttonStyleStar);
        buttonStar.setWidth(Gdx.graphics.getWidth() / 5f);
        buttonStar.setHeight(Gdx.graphics.getHeight() / 7.8f);
        buttonStar.setPosition((Gdx.graphics.getWidth() / 1f) - buttonStar.getWidth() + 20, buttonHeart.getY() - buttonHeart.getHeight());

        skinEnergie.addRegions(MyGdxGame.atlas);
        buttonStyleEnergie = new TextButtonStyle();
        buttonStyleEnergie.up = skinEnergie.getDrawable("buttonPromoteUp");
        buttonStyleEnergie.down = skinEnergie.getDrawable("buttonPromoteDown");
        buttonEnergie = new Button(buttonStyleEnergie);
        buttonEnergie.setWidth(Gdx.graphics.getWidth() / 5f);
        buttonEnergie.setHeight(Gdx.graphics.getHeight() / 7.8f);
        buttonEnergie.setPosition(-400, Gdx.graphics.getHeight() / 3.5f);

        buttonStyleAds = new TextButtonStyle();
        skinAds.addRegions(MyGdxGame.atlas);
        buttonStyleAds.up = skinAds.getDrawable("buttonSecret");
        buttonStyleAds.down = skinAds.getDrawable("buttonSecret");
        buttonAds = new Button(buttonStyleAds);
        buttonAds.setWidth(Gdx.graphics.getWidth() / 5f);
        buttonAds.setHeight(Gdx.graphics.getHeight() / 7.8f);
        buttonAds.setPosition(-400, buttonHeart.getY());

        stage1.addActor(buttonExit);
        stage1.addActor(buttonReplay);
        //buttonHeart.setVisible(false);
        stage1.addActor(buttonHeart);
        buttonStar.setVisible(false);
        stage1.addActor(buttonStar);
        //buttonEnergie.setVisible(false);
        stage1.addActor(buttonEnergie);
        stage1.addActor(buttonAds);

        Gdx.input.setInputProcessor(stage1);

        //batch = new SpriteBatch();
        if(MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getMusic("newScreen").play();

        buttonExit.addListener(new InputListener() {
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
                click1 = true;
                //game.actionResolver.hideBannerAd();
            };
        });

        buttonReplay.addListener(new InputListener() {
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
                click2 = true;
                //game.actionResolver.hideBannerAd();
            }

            ;
        });

        buttonHeart.addListener(new InputListener() {
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

        buttonAds.addListener(new InputListener() {
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

        cpt_translate_animation = 0;
        cpt_translate_animation2 = 0;
        cpt_translate_animation3 = 0;
        //Gdx.gl.glClearColor(31f / 255f, 169f / 255f, 180f / 255f, 1);
    }

    public void handleInput() {
        // mouse/touch input
        if (click1) {
            click1 = false;
            gsm.setState(GameStateManager.MENU);
            MyGdxGame.setStartCameraMotion(true);
            if(MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getMusic("newScreen").play();
            //if(MyGdxGame.res.getMusic("game_over").isPlaying()) MyGdxGame.res.getMusic("game_over").stop();
            if(MyGdxGame.res.getMusic("success").isPlaying()) MyGdxGame.res.getMusic("success").pause();
            //Gdx.gl.glClearColor(0, 0, 0, 1);
        }
        if (click2) {

            click2 = false;
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

        animSad.update(dt);
        animHappy.update(dt);
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
        offset = offsetY;

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glViewport(0,0, (int)viewport.width+ (int)offsetX, Gdx.graphics.getHeight());
        stage0.act();
        sb.begin();
        stage0.draw();
        sb.end();
        Gdx.gl.glViewport((int) viewport.x, (int) viewport.y, (int) viewport.width - (int)offsetX, (int) viewport.height - (int)offsetY);

        stage1.getViewport().update((int) (width - offsetX), (int) (height - offsetY), true);
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
        //shapeRenderer.rect(0, ((MyGdxGame.V_HEIGHT - height)/2.0f)+score_offset  , MyGdxGame.V_WIDTH, height);
        shapeRenderer.end();

        //labelBestScore.setPosition(labelBestScore.getX(),labelBestScore.getY());


        stage1.act();
        sb.begin();
        stage1.draw();


        float speed = 8f;
        //heart button
        if (stage1.getActors().items[6].getRight() >= Gdx.graphics.getWidth() + 5) {
            stage1.getActors().items[6].setPosition((Gdx.graphics.getWidth() / 1f) - (cpt_translate_animation * speed), stage1.getActors().items[6].getY());
            cpt_translate_animation++;
        }
        //start button
        if (stage1.getActors().items[7].getRight() >= Gdx.graphics.getWidth() + 5)
            stage1.getActors().items[7].setPosition((Gdx.graphics.getWidth() / 1f) - cpt_translate_animation * speed, stage1.getActors().items[7].getY());

        //start buyWings
        if (Save.gd.getFullBarPurchased()) {
            buttonEnergie.setDisabled(true);
            if (stage1.getActors().items[8].getX() >= -500)
                stage1.getActors().items[8].setPosition(stage1.getActors().items[8].getX() - cpt_translate_animation3 * 1, stage1.getActors().items[8].getY());
            cpt_translate_animation3++;
        } else {
            if (stage1.getActors().items[8].getX() <= -5)
                stage1.getActors().items[8].setPosition(-stage1.getActors().items[8].getWidth() + cpt_translate_animation * speed, stage1.getActors().items[8].getY());
        }
        //start adsRemover
        if (Save.gd.getAdsRemoverPurchased()) {
            //buttonAds.setDisabled(true);
            if (stage1.getActors().items[9].getX() >= -500)
                stage1.getActors().items[9].setPosition(stage1.getActors().items[9].getX() - cpt_translate_animation2 * 1, stage1.getActors().items[9].getY());
            cpt_translate_animation2++;
        } else {
            if (stage1.getActors().items[9].getX() <= -5)
                stage1.getActors().items[9].setPosition(-stage1.getActors().items[9].getWidth() + cpt_translate_animation * speed, stage1.getActors().items[9].getY());
        }


        //BUTTON PLAY
        stage1.getActors().items[4].setVisible(true);
        stage1.getActors().items[4].setPosition(-stage1.getActors().items[4].getWidth() + Gdx.graphics.getWidth()/20 +cpt_translate_animation * speed, stage1.getActors().items[4].getY());
        stage1.getActors().items[4].draw(sb, 1f);

        //BUTTON LEADERBOARD
        stage1.getActors().items[5].setVisible(true);
        stage1.getActors().items[5].setPosition((Gdx.graphics.getWidth() / 1f) - (Gdx.graphics.getWidth()/20+cpt_translate_animation * speed), stage1.getActors().items[4].getY());
        stage1.getActors().items[5].draw(sb, 1f);


        sb.end();

        if (timerIsOn) {
            Timer.schedule(new Task() {
                @Override
                public void run() {
                    if (cpt_timer <= 3) {
                        cpt_timer++;
                        if (cpt_score != 0)
                            labelScore.setText("SCORE  " + Integer.toString(cpt_score + 1));
                    } else {
                        cpt_timer = 0;
                        cpt_score++;
                    }
                    if (cpt_score >= score)
                        timerIsOn = false;
                }
            }, 0f // (delay)
            );
        } else {
            Timer.instance().clear();
            timerIsOn = false;
            cpt_timer = 0;
            // changeScreen();
        }

        float w = 59*6f;
        float h = 47*6f;
        sb.begin();
        sb.draw(animTitle.getFrame(), MyGdxGame.V_WIDTH/2 - w/2, MyGdxGame.V_HEIGHT - h*1.1f , MyGdxGame.V_WIDTH/2, 670.0f -95/2 ,  w, h,1,1, 0);

        if(score >=100){
            sb.draw(animHappy.getFrame(), MyGdxGame.V_WIDTH/2 - 132/2, 202);
        }else {
            sb.draw(animSad.getFrame(), MyGdxGame.V_WIDTH/2, 202);
        }

        sb.end();


    }

    public void dispose() {
        //sb.dispose();
        if (MyGdxGame.res.getMusic("game_over").isPlaying()) {
            MyGdxGame.res.getMusic("game_over").pause();
            MyGdxGame.res.getMusic("game_over").stop();
        }
    }

}
