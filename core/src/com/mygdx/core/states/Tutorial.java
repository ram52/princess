package com.mygdx.core.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.mygdx.core.MyGdxGame;
import com.mygdx.core.handlers.GameStateManager;

public class Tutorial extends GameState {
    private static String LOG_TAG = Tutorial.class.getSimpleName();
    Rectangle viewport;
    float offset = 0;
    private Stage stage0;
    private Image intro;
    Button buttonExit, buttonReplay, buttonHeart, buttonStar, buttonFacebook, buttonAds;
    Skin skinPlay, skinLeaderboard, skinHeart, skinStar, skinEnergie, skinAds;
    boolean click1, click2, timerIsOn;

    public Tutorial(final GameStateManager gsm) {
        super(gsm);
        intro = new Image(MyGdxGame.atlas.findRegion("backgroundSky"));
        intro.setFillParent(true);
        stage0 = new Stage();
        stage0.addActor(intro);
        viewport = new Rectangle();

        skinPlay = new Skin();
        skinLeaderboard = new Skin();
        skinHeart = new Skin();
        skinStar = new Skin();
        skinEnergie = new Skin();
        skinAds = new Skin();

        skinPlay.addRegions(MyGdxGame.atlas);
        TextButton.TextButtonStyle buttonStylePlay = new TextButton.TextButtonStyle();
        buttonStylePlay.up = skinPlay.getDrawable("buttonSecret");
        buttonStylePlay.down = skinPlay.getDrawable("buttonSecret");
        buttonExit = new Button(buttonStylePlay);
        buttonExit.setWidth(Gdx.graphics.getWidth());
        buttonExit.setHeight(Gdx.graphics.getHeight());
        buttonExit.setPosition(0,0);
        stage0.addActor(buttonExit);
        Gdx.input.setInputProcessor(stage0);

        buttonExit.addListener(new InputListener() {
            public boolean touchDown(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                click2 = true;
                return true;
            };

            public void touchUp(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
            };
        });

    }

    public void handleInput() {
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
        handleInput();
        MyGdxGame.background_wood1.update(dt);
        MyGdxGame.background_cloud.update(dt);
    }

    public void render() {
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        //MyGdxGame.background_skyDay.render(sb);
        MyGdxGame.background_wood1.render(sb);
        MyGdxGame.background_title.render(sb);
    }

    public void dispose() {
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
    }
}


