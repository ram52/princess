package com.mygdx.core.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Timer;
import com.mygdx.core.MyGdxGame;
import com.mygdx.core.handlers.Animation;
import com.mygdx.core.handlers.GameStateManager;
import com.mygdx.core.handlers.Save;

public class Opening extends GameState {

    private static String LOG_TAG = Opening.class.getSimpleName();

    private Stage stage1;
    private Image imageOpening1;
    private Rectangle viewport;
    private boolean touchEnable = false;
    private Animation animationPBlue1;
    private Animation animationPrincess;
    private Animation animationEnemy;
    private Animation animationHya;
    private int time = 0;
    private Stage stage0;
    private Image intro;


    public Opening(final GameStateManager gsm) {

        super(gsm);
        intro = new Image(MyGdxGame.atlas.findRegion("backgroundSky"));
        intro.setFillParent(true);
        stage0 = new Stage();
        stage0.addActor(intro);

        imageOpening1 = new Image(MyGdxGame.atlas.findRegion("opening1"));
        imageOpening1.setFillParent(true);
        viewport = new Rectangle();

        stage1 = new Stage();
        stage1.addActor(imageOpening1);

        //stage0.addAction(Actions.sequence(Actions.alpha(0.5f), Actions.fadeIn(1.5f)));
        //stage1.addAction(Actions.sequence(Actions.alpha(0.5f), Actions.fadeIn(1.5f)));

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

        MyGdxGame.background_cloud.setVector(+10, 0);
        //MyGdxGame.background_skyDay.setVector(0, 0);
        MyGdxGame.background_wood1.setVector(-3, 0);

        Sprite tex = null;
        if(Save.gd.isExcaliburEquiped()){
            tex = new Sprite(MyGdxGame.atlas.findRegion("walkEx"));
        }else{
            tex = new Sprite(MyGdxGame.atlas.findRegion("walk"));
        }
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
        // MyGdxGame.background_skyDay.update(dt);
        MyGdxGame.background_cloud.update(dt);
        MyGdxGame.background_wood1.update(dt);
        animationPBlue1.update(dt);
        animationEnemy.update(dt);
        animationPrincess.update(dt);
        MyGdxGame.fadeIn.update(dt);
        //MyGdxGame.background_storyLine.update(dt);
    }

    public void render() {
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        sb.setProjectionMatrix(cam.combined);
        shapeRenderer.setProjectionMatrix(cam.combined);
        //Gdx.gl.glClearColor(65f / 255f, 18f / 255f, 252f / 255f, 1);
        Gdx.gl.glClearColor(0, 0, 0, 1);

        //MyGdxGame.background_skyDay.render(sb);
        MyGdxGame.background_cloud.render(sb);
        MyGdxGame.background_wood1.render(sb);

        stage1.act();
        sb.begin();
        stage1.draw();
        sb.end();
        sb.begin();
        sb.draw(animationPBlue1.getFrame(), -80 + ((float) time * 1.7f), 200);
        sb.draw(animationPrincess.getFrame(), 0 + ((float) time * 1.7f), 200);
        sb.draw(animationHya.getFrame(), 0 + ((float) time * 1.7f), 250);
        sb.draw(animationEnemy.getFrame(), -250 + ((float) time * 1.7f), 200);
        //sb.draw(fadeIn.animation.getFrame(),0,0,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),1,1,0);
        sb.end();

        Gdx.gl.glViewport(0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        MyGdxGame.fadeIn.render(sb);

        time+=2;

        if(time > 800){
            time = 0;
        }
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
    }

    public void dispose() {
    }

}
