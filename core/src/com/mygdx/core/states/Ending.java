package com.mygdx.core.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Timer;
import com.mygdx.core.MyGdxGame;
import com.mygdx.core.handlers.GameStateManager;
import com.mygdx.core.handlers.Save;

public class Ending extends GameState {

    private Stage stage0;
    private Image intro;
    private Rectangle viewport;
    private float offset;
    private float offsetx;
    private boolean start = false, touchEnable = false, reset = false;

    public Ending(final GameStateManager gsm) {

        super(gsm);
        Gdx.gl.glClearColor(65f / 255f, 18f / 255f, 252f / 255f, 1);
        offset = 0;
        intro = new Image(MyGdxGame.atlas.findRegion("ending"));
        intro.setFillParent(true);
        viewport = new Rectangle();
        stage0 = new Stage();
        stage0.addActor(intro);
        stage0.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(1f)));

        if (!MyGdxGame.isNightEnable()) {
            reset = true;
            MyGdxGame.setNightEnable(true);
        }

        //if (Save.gd.isSoundEnable()) MyGdxGame.res.getMusic("success").play();

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                touchEnable = true;
            }
        }, 0.3f);


        /*Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                Save.load();
                if (Save.gd.isSoundEnable()) MyGdxGame.res.getMusic("on").play();
            }
        }, 0.2f);
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                if(MyGdxGame.isNightEnable())
                    Gdx.gl.glClearColor(3f / 255f, 23 / 255f, 41 / 255f, 1);
                else
                    Gdx.gl.glClearColor(22f / 255f, 119f / 255f, 126f / 255f, 1);

                gsm.setState(GameStateManager.STORY);
            }
        }, 2.2f);*/

        //timer1.run();
        //timer2.run();
    }

    public void resize(int width, int height) {
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
        offset = crop.y;
        offsetx = crop.x;
    }

    public void handleInput() {
        if(Gdx.input.justTouched() && touchEnable){
            /*if(MyGdxGame.isNightEnable())
                Gdx.gl.glClearColor(3f / 255f, 23 / 255f, 41 / 255f, 1);
            else
                Gdx.gl.glClearColor(22f / 255f, 119f / 255f, 126f / 255f, 1);*/
            if(reset)
                MyGdxGame.setNightEnable(false);

                gsm.setState(GameStateManager.GAME_OVER);
        }
    }

    public void update(float dt) {
        handleInput();
        //MyGdxGame.background_skyNight.update(dt);
    }

    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glViewport((int) viewport.x, (int) viewport.y, (int) viewport.width, (int) viewport.height);

        //sb.setProjectionMatrix(cam.combined);
        //MyGdxGame.background_skyNight.render(sb);
        MyGdxGame.displayBlinkingStars();

            stage0.act();
            sb.begin();
            stage0.draw();
            sb.end();


    }

    public void dispose() {

    }

}
