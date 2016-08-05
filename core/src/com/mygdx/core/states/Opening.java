package com.mygdx.core.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Timer;
import com.mygdx.core.MyGdxGame;
import com.mygdx.core.handlers.Animation;
import com.mygdx.core.handlers.BoundedCamera;
import com.mygdx.core.handlers.GameStateManager;
import com.mygdx.core.handlers.Save;

public class Opening extends GameState {

    private Stage stage1;
    private Image imageOpening1;
    private Rectangle viewport;
    private float offset;
    private float offsetx;
    private BoundedCamera cam2;
    private boolean fade = false, touchEnable = false;
    private Animation animationPBlue1;
    private Animation animationPrincess;
    private Animation animationEnemy;
    private Animation animationHya;
    private int time = 0;

    public Opening(final GameStateManager gsm) {

        super(gsm);
        offset = 0;
        imageOpening1 = new Image(MyGdxGame.atlas.findRegion("opening1"));

        //imageStorylineText.setFillParent(true);
        imageOpening1.setFillParent(true);
        viewport = new Rectangle();

        stage1 = new Stage();
        stage1.addActor(imageOpening1);

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                touchEnable = true;
            }
        }, 0.3f);

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                gsm.setState(GameStateManager.MENU);
            }
        }, 5f);

        cam2 = new BoundedCamera();
        cam2.setToOrtho(false, MyGdxGame.V_WIDTH, MyGdxGame.V_HEIGHT);

        MyGdxGame.background_cloud.setVector(+10, 0);
        //MyGdxGame.background_storyLine.setVector(0, 0);
        MyGdxGame.background_skyNight.setVector(0, 0);
        MyGdxGame.background_skyDay.setVector(0, 0);
        MyGdxGame.background_wood1.setVector(-3, 0);

        Sprite tex = new Sprite(MyGdxGame.atlas.findRegion("walk"));
        TextureRegion[] sprites = tex.split(64, 64)[0];
        animationPBlue1 = new Animation(sprites, 1 / 5f);

        tex = new Sprite(MyGdxGame.atlas.findRegion("princesscry"));
        sprites = tex.split(64, 64)[0];
        for(int i=0;i<sprites.length;i++)
            sprites[i].flip(true,false);
        animationPrincess = new Animation(sprites, 1 / 5f);

        tex = new Sprite(MyGdxGame.atlas.findRegion("enemy"));
        sprites = tex.split(64, 64)[0];
        for(int i=0;i<sprites.length;i++)
            sprites[i].flip(true,false);
        animationEnemy = new Animation(sprites, 1 / 5f);



        animationHya = new Animation(new Sprite(MyGdxGame.atlas.findRegion("hyaa")).split(168, 60)[0], 1 / 5f);


    }

    public void handleInput() {
        if (Gdx.input.justTouched() && touchEnable) {
            //Gdx.gl.glClearColor(31f / 255f, 169f / 255f, 180f / 255f, 1);
            if (!MyGdxGame.gsm.flag_menu) {
                Timer.instance().stop();
                Timer.instance().clear();
                gsm.setState(GameStateManager.MENU);
            }
        }
    }

    public void update(float dt) {
        handleInput();
        if (!MyGdxGame.isNightEnable()) {
            MyGdxGame.background_cloud.update(dt);
            MyGdxGame.background_skyDay.update(dt);
        } else {
            MyGdxGame.background_skyNight.update(dt);
        }
        MyGdxGame.background_wood1.update(dt);
        animationPBlue1.update(dt);
        animationEnemy.update(dt);
        animationPrincess.update(dt);
        //MyGdxGame.background_storyLine.update(dt);

    }


    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glViewport((int) viewport.x, (int) viewport.y, (int) viewport.width, (int) viewport.height);
        if (!fade) {

            sb.setProjectionMatrix(cam2.combined);
            shapeRenderer.setProjectionMatrix(cam2.combined);
            Gdx.gl.glClearColor(65f / 255f, 18f / 255f, 252f / 255f, 1);

            if (MyGdxGame.isNightEnable()) {
                MyGdxGame.background_skyNight.render(sb);
            } else {
                MyGdxGame.background_skyDay.render(sb);
            }
            MyGdxGame.background_wood1.render(sb);
            if (MyGdxGame.isNightEnable()) MyGdxGame.displayBlinkingStars();

            stage1.act();
            sb.begin();
            stage1.draw();
            sb.end();
            sb.begin();
            sb.draw(animationPBlue1.getFrame(), -80 + ((float) time * 1.7f), 200);
            sb.draw(animationPrincess.getFrame(), 0 + ((float) time * 1.7f), 200);
            sb.draw(animationHya.getFrame(), 0 + ((float) time * 1.7f), 250);
            sb.draw(animationEnemy.getFrame(), -250 + ((float) time * 1.7f), 200);
            sb.end();
            time+=2;


            if(time > 800){
                time = 0;
            }
        } else {
            Gdx.gl.glClearColor(0, 0, 0, 1);
        }
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

    public void dispose() {
    }

}
