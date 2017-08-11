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
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.mygdx.core.MyGdxGame;
import com.mygdx.core.handlers.Animation;
import com.mygdx.core.handlers.GameStateManager;
import com.mygdx.core.handlers.Save;

import static com.mygdx.core.MyGdxGame.font2;
import static com.mygdx.core.MyGdxGame.glyphLayout;


public class Credits extends GameState {

    private static String LOG_TAG = Credits.class.getSimpleName();
    public static boolean click_on_playCredit;
    public static Stage stage1Credit, stage2Credit;
    public static AlphaAction fade1Credit, fade2Credit, fade3Credit;
    public static AlphaAction fadeCredit;
    public static Button buttonPlayCredit, buttonSecret1Credit, buttonSecret2Credit;
    public static int cpt_secret1 = 0;
    public static int cpt_translate_animation1Credit = 0;
    public static Animation animationPBlue1Credit;
    public static Animation animationPrincessCredit;
    public static Animation animationEnemyCredit;
    public static Animation animationHyaCredit;
    public static int timeCredit = 0;
    public static Rectangle viewportCredit;
    public static Stage stage0Credit;
    public static Image introCredit;
    public static String CREDIT = "";
    public static Vector2 credits_size = new Vector2(0,0);
    public static ShapeRenderer shapeRendererCredit;

    public Credits(GameStateManager gsm) {
        super(gsm);



        //font.setScale(1.3f);
        if(credits_size.y == 0)
            getCreditFromFile();

        glyphLayout.setText(font2,Gdx.files.internal("data/credits.txt").readString(),Color.WHITE,Gdx.graphics.getWidth(), Align.center,true);


        if(shapeRendererCredit == null){
            shapeRendererCredit = new ShapeRenderer();
            introCredit = new Image(MyGdxGame.atlas.findRegion("backgroundSkyHTML"));
            introCredit.setFillParent(true);
            stage0Credit = new Stage();
            stage0Credit.addActor(introCredit);
            viewportCredit = new Rectangle();
        }


        if(MyGdxGame.isSoundEnable() == 2){
            if(!MyGdxGame.res.getMusic("shop").isPlaying()){
                MyGdxGame.res.getMusic("shop").setVolume(1.0f);
                MyGdxGame.res.getMusic("shop").play();
            }
        }




        if(animationPBlue1Credit == null){
            Sprite tex = null;
            if(Save.gd.isExcaliburEquiped()){
                tex = new Sprite(MyGdxGame.atlas.findRegion("walkEx"));
            }else{
                tex = new Sprite(MyGdxGame.atlas.findRegion("walk"));
            }

            TextureRegion[] sprites = tex.split(64, 64)[0];
            animationPBlue1Credit = new Animation(sprites, 1 / 5f);

            tex = new Sprite(MyGdxGame.atlas.findRegion("princesscry"));
            sprites = tex.split(64, 64)[0];
            for(int i=0;i<sprites.length;i++)
                sprites[i].flip(true,false);
            animationPrincessCredit = new Animation(sprites, 1 / 5f);

            tex = new Sprite(MyGdxGame.atlas.findRegion("enemy"));
            sprites = tex.split(64, 64)[0];
            for(int i=0;i<sprites.length;i++)
                sprites[i].flip(true,false);
            animationEnemyCredit = new Animation(sprites, 1 / 5f);

            animationHyaCredit = new Animation(new Sprite(MyGdxGame.atlas.findRegion("hyaa")).split(168, 60)[0], 1 / 5f);
        }


        //animTitle = new Animation(new Sprite(MyGdxGame.atlas.findRegion("secret")).split(95,24)[0], 1 / 5f);

        //animCredits = new Animation(new Sprite(MyGdxGame.atlas.findRegion("secret")).split(233,120)[0], 1 / 5f);

        MyGdxGame.background_cloud.setVector(+10, 0);
        //MyGdxGame.background_skyDay.setVector(0, 0);
        MyGdxGame.background_wood1.setVector(-3, 0);

        cam.setToOrtho(false, MyGdxGame.V_WIDTH, MyGdxGame.V_HEIGHT);
        click_on_playCredit = false;


        if(stage1Credit == null){
            stage1Credit = new Stage();
            stage2Credit = new Stage();
            Skin skinButtonPlay = new Skin();
            skinButtonPlay.addRegions(MyGdxGame.atlas);
            ButtonStyle buttonStylePlay = new ButtonStyle();
            buttonStylePlay.up = skinButtonPlay.getDrawable("buttonSecret");
            buttonStylePlay.down = skinButtonPlay.getDrawable("buttonSecret");
            buttonPlayCredit = new Button(buttonStylePlay);
            buttonPlayCredit.setWidth(Gdx.graphics.getWidth());
            buttonPlayCredit.setHeight(Gdx.graphics.getHeight());
            buttonPlayCredit.setPosition(0,0);

            stage1Credit.addActor(buttonPlayCredit);

            Skin skinButtonSecret = new Skin();
            skinButtonSecret.addRegions(MyGdxGame.atlas);
            ButtonStyle buttonStyleSecret = new ButtonStyle();
            buttonStyleSecret.up = skinButtonSecret.getDrawable("buttonSecret");
            buttonStyleSecret.down = skinButtonSecret.getDrawable("buttonSecret");
            buttonSecret1Credit = new Button(buttonStyleSecret);
            buttonSecret1Credit.setWidth(Gdx.graphics.getWidth() / 8f);
            buttonSecret1Credit.setHeight(Gdx.graphics.getWidth() / 8f);
            buttonSecret1Credit.setPosition((Gdx.graphics.getWidth() - buttonSecret1Credit.getWidth())/ 2.05f, (Gdx.graphics.getHeight() - buttonSecret1Credit.getWidth())/4.3f );
            stage1Credit.addActor(buttonSecret1Credit);

            buttonSecret2Credit = new Button(buttonStyleSecret);
            buttonSecret2Credit.setWidth(Gdx.graphics.getWidth() / 8f);
            buttonSecret2Credit.setHeight(Gdx.graphics.getWidth() / 8f);
            buttonSecret2Credit.setPosition((Gdx.graphics.getWidth() - buttonSecret1Credit.getWidth())/ 1.12f, (Gdx.graphics.getHeight() - buttonSecret1Credit.getWidth())/21f );
            stage1Credit.addActor(buttonSecret2Credit);
        }

        Gdx.input.setInputProcessor(stage1Credit);

        if(fadeCredit == null){
            fadeCredit = new AlphaAction();
            fadeCredit.setDuration(0f);
            //scoreLabel.setText(Float.toString(offsetGameOver));
            fade1Credit = new AlphaAction();
            fade1Credit.setDuration(0f);
            fade2Credit = new AlphaAction();
            fade2Credit.setDuration(0f);
            stage2Credit.addAction(fade2Credit);
            fade3Credit = new AlphaAction();
            fade3Credit.setDuration(0f);
            stage1Credit.addAction(Actions.sequence(Actions.alpha(1), Actions.fadeIn(0f)));
            setupButtonsInUi();
        }


        MyGdxGame.setIsBoosTerritory(false);
    }

    private void setupButtonsInUi(){
        buttonPlayCredit.addListener(new InputListener() {
            public boolean touchDown(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                if (Save.gd.isSoundEnable() == 1 | Save.gd.isSoundEnable() == 2) MyGdxGame.res.getSound("select").play();
                click_on_playCredit = true;
                Gdx.app.debug(LOG_TAG,"click_on_playCredit: " + click_on_playCredit);
                return true;
            }

            public void touchUp(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {

            }

        });

        buttonSecret2Credit.addListener(new InputListener() {
            public boolean touchDown(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                return true;
            }

            public void touchUp(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                buttonSecret2Credit.setDisabled(true);
                buttonSecret2Credit.setVisible(false);
                //Gdx.gl.glClearColor(0, 0, 0, 1);
            }
        });

        buttonSecret1Credit.addListener(new InputListener() {
            public boolean touchDown(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {

                cpt_secret1++;
                Gdx.app.debug(LOG_TAG,"CLICK SECRET 1: " + cpt_secret1);
                //if (Save.gd.isSoundEnable() == 1 | Save.gd.isSoundEnable() == 2) MyGdxGame.res.getMusic("secretboss").play();

                return true;
            }

            public void touchUp(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                //Gdx.gl.glClearColor(0, 0, 0, 1);
            }
        });
    }



    public void handleInput() {
        if (click_on_playCredit) {
            click_on_playCredit = false;
            gsm.setState(GameStateManager.MENU);
            if (Save.gd.isSoundEnable() == 1 | Save.gd.isSoundEnable() == 2) MyGdxGame.res.getMusic("newScreen").play();
        }
    }

    public void update(float dt) {

        if(MyGdxGame.isSoundEnable() == 2){
            if(!MyGdxGame.res.getMusic("shop").isPlaying()){
                MyGdxGame.res.getMusic("shop").setVolume(1.0f);
                MyGdxGame.res.getMusic("shop").play();
            }
        }

        animationPBlue1Credit.update(dt);
        animationEnemyCredit.update(dt);
        animationPrincessCredit.update(dt);

//        if(MyGdxGame.isSoundEnable() == 2) {
//            //MyGdxGame.res.getMusic("main").setVolume(0.6f);
//            if(!MyGdxGame.res.getMusic("main").isPlaying())
//                MyGdxGame.res.getMusic("main").play();
//        }
//
//        MyGdxGame.updateBGM();
        handleInput();
        MyGdxGame.background_cloud.update(dt);
        //MyGdxGame.background_skyDay.update(dt);

        //MyGdxGame.background_secret1.update(dt);
        MyGdxGame.background_wood1.update(dt);

        if (fade1Credit.getTime() > fade1Credit.getDuration()) click_on_playCredit = true;

//        if(cpt_secret1 == MyGdxGame.getTapnumbSecret()){
//            cpt_secret1 = 0;
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

    public void resize(int width, int height) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // calculate new viewportCredit
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
        viewportCredit = new Rectangle(crop.x, 0, w, h);
        //Gdx.gl.glViewport((int) viewportCredit.x, (int) viewportCredit.yMenu, (int) viewportCredit.width, (int) viewportCredit.height);
        float offsetY = crop.y;
        float offsetX = crop.x;

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glViewport(0,0, (int) viewportCredit.width+ (int)offsetX, Gdx.graphics.getHeight());
        stage0Credit.act();
        sb.begin();
        stage0Credit.draw();
        sb.end();
        Gdx.gl.glViewport((int) viewportCredit.x, (int) viewportCredit.y, (int) viewportCredit.width - (int)offsetX, (int) viewportCredit.height - (int)offsetY);

        stage1Credit.getViewport().update((int) (width - offsetX), (int) (height - offsetY), true);
        stage2Credit.getViewport().update((int) (width - offsetX), (int) (height - offsetY), true);
    }

    public void render() {

        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        if (fade2Credit.getTime() < fade2Credit.getDuration()) {
            stage2Credit.act();
            sb.begin();
            stage2Credit.draw();
            sb.end();
        }
        else {

            sb.setProjectionMatrix(cam.combined);
            shapeRendererCredit.setProjectionMatrix(cam.combined);

            //MyGdxGame.background_skyDay.render(sb);

            MyGdxGame.background_wood1.render(sb);
            //bg1.render(sb);
            MyGdxGame.background_title.render(sb);

            stage1Credit.act();
            sb.begin();
            stage1Credit.draw();
            sb.end();



            //GEAR BUTTON
            float speed = 8f;
            if (stage1Credit.getActors().items[0].getX() <= -5) {
                stage1Credit.getActors().items[0].setPosition(-stage1Credit.getActors().items[0].getWidth() + cpt_translate_animation1Credit * speed, stage1Credit.getActors().items[0].getY());
                cpt_translate_animation1Credit++;
            }

            //if((Save.gd.isSoundEnable()==2) && !MyGdxGame.res.getMusic("main").isPlaying()) MyGdxGame.res.getMusic("main").setVolume(0.6f);


            sb.begin();
            sb.draw(animationPBlue1Credit.getFrame(), -80 + ((float) timeCredit * 1.7f), 202);
            sb.draw(animationPrincessCredit.getFrame(), 0 + ((float) timeCredit * 1.7f), 202);
            sb.draw(animationHyaCredit.getFrame(), 0 + ((float) timeCredit * 1.7f), 250);
            sb.draw(animationEnemyCredit.getFrame(), -250 + ((float) timeCredit * 1.7f), 202);
            sb.end();

            drawCredits();

            timeCredit +=2;

            if(timeCredit > 800){
                timeCredit = -200;
            }


        }
    }

    public static void getCreditFromFile(){
        String credits = ":( Sorry could not load credits file.";

        try {
            credits = Gdx.files.internal("data/credits.txt").readString();
        } catch (GdxRuntimeException e) {
            Gdx.app.error(LOG_TAG,"error while accessing file",e);
        }

        Gdx.app.error(LOG_TAG,"credit="+credits);

        font2.getData().setScale(0.85f);
        glyphLayout.setText(font2,credits);
        credits_size.set(glyphLayout.width,glyphLayout.height);

        credits_size.y = glyphLayout.height;


    }

    public void drawCredits(){
        float x = MyGdxGame.V_WIDTH/2 - credits_size.x/2;
        float y = MyGdxGame.V_HEIGHT/1.15f;

        Gdx.gl.glEnable(GL20.GL_BLEND);
        shapeRenderer.setColor(new Color(0, 0, 0, 0.8f));
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        float h = credits_size.y;
        float padding = 100;
        shapeRenderer.rect(x-padding/2,y-h-padding/2, credits_size.x+padding, h+padding);
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);


        sb.begin();
        font2.setColor(Color.WHITE);
        font2.draw(sb, glyphLayout, 0 , y);
        sb.end();
    }


    public void dispose() {
        //font.setScale(1);
        if (MyGdxGame.res.getMusic("shop").isPlaying()) MyGdxGame.res.getMusic("shop").stop();
        cpt_secret1 = 0;
        cpt_translate_animation1Credit = 0;
        timeCredit = 0;
        click_on_playCredit = false;
        glyphLayout.reset();
    }

}