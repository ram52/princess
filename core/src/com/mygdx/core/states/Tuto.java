package com.mygdx.core.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.core.MyGdxGame;
import com.mygdx.core.handlers.Animation;
import com.mygdx.core.handlers.GameStateManager;
import com.mygdx.core.handlers.Save;

public class Tuto extends GameState {
    private boolean click_on_play;
    private Stage stage1, stage2;
    private AlphaAction fade1, fade2, fade3;
    private Rectangle viewport;
    private AlphaAction fade;
    private float offset = 0;
    private float offsetx;
    private Button buttonPlay, buttonSecret1, buttonSecret2;
    private int cpt_secret1 = 0, cpt_secret2 = 0;
    private int cpt_translate_animation1 = 0;
    private Animation animationPlayer;
    private Animation animationPrincess;
    private Animation animationHya;

    public Tuto(GameStateManager gsm) {

        super(gsm);
        viewport = new Rectangle();

        animationHya = new Animation(new Sprite(MyGdxGame.atlas.findRegion("protectme")).split(123, 75)[0], 1 / 5f);

        Save.load();
        if (!Save.gd.getAdsRemoverPurchased()) {
            String network = game.actionResolver.getNetworkClass();
            if(network == null) network = "ABSENT";
            System.out.print("NETWORK: "+network);
            if(network.equals("4G")|network.equals("3G")|network.equals("WIFI"))
                game.actionResolver.showOrLoadInterstital();
            System.out.println(network);
        }

        Sprite tex = null;
        if(Save.gd.isExcaliburEquiped()){
            tex = new Sprite(MyGdxGame.atlas.findRegion("standEx"));
        }else{
            tex = new Sprite(MyGdxGame.atlas.findRegion("stand"));
        }
        TextureRegion[] sprites = tex.split(64, 64)[0];
        animationPlayer = new Animation(sprites, 1 / 5f);

        tex = new Sprite(MyGdxGame.atlas.findRegion("princesscry"));
        sprites = tex.split(64, 64)[0];
        //for(int i=0;i<sprites.length;i++)
            //sprites[i].flip(true,false);
        animationPrincess = new Animation(sprites, 1 / 5f);

        MyGdxGame.background_cloud.setVector(+10, 0);
        MyGdxGame.background_skyNight.setVector(0, 0);
        MyGdxGame.background_skyDay.setVector(0, 0);
        MyGdxGame.background_wood1.setVector(-3, 0);

        cam.setToOrtho(false, MyGdxGame.V_WIDTH, MyGdxGame.V_HEIGHT);
        click_on_play = false;
        stage1 = new Stage();
        stage2 = new Stage();
        Skin skinButtonPlay = new Skin();
        skinButtonPlay.addRegions(MyGdxGame.atlas);
        ButtonStyle buttonStylePlay = new ButtonStyle();
        buttonStylePlay.up = skinButtonPlay.getDrawable("buttonExit2Up");
        buttonStylePlay.down = skinButtonPlay.getDrawable("buttonExit2Down");
        buttonPlay = new Button(buttonStylePlay);
        buttonPlay.setWidth(Gdx.graphics.getWidth() / 5f);
        buttonPlay.setHeight(Gdx.graphics.getHeight() / 7.8f);
        buttonPlay.setPosition(-400, Gdx.graphics.getHeight()/1.4f);

        stage1.addActor(buttonPlay);

        Skin skinButtonSecret = new Skin();
        skinButtonSecret.addRegions(MyGdxGame.atlas);
        ButtonStyle buttonStyleSecret = new ButtonStyle();
        buttonStyleSecret.up = skinButtonSecret.getDrawable("buttonSecret");
        buttonStyleSecret.down = skinButtonSecret.getDrawable("buttonSecret");
        buttonSecret1 = new Button(buttonStyleSecret);
        buttonSecret1.setWidth(Gdx.graphics.getWidth() / 8f);
        buttonSecret1.setHeight(Gdx.graphics.getWidth() / 8f);
        buttonSecret1.setPosition((Gdx.graphics.getWidth() - buttonSecret1.getWidth())/ 2.05f, (Gdx.graphics.getHeight() - buttonSecret1.getWidth())/4.3f );
        stage1.addActor(buttonSecret1);

        buttonSecret2 = new Button(buttonStyleSecret);
        buttonSecret2.setWidth(Gdx.graphics.getWidth() / 8f);
        buttonSecret2.setHeight(Gdx.graphics.getWidth() / 8f);
        buttonSecret2.setPosition((Gdx.graphics.getWidth() - buttonSecret1.getWidth())/ 1.12f, (Gdx.graphics.getHeight() - buttonSecret1.getWidth())/21f );
        stage1.addActor(buttonSecret2);


        Gdx.input.setInputProcessor(stage1);
        fade = new AlphaAction();
        fade.setDuration(0f);
        //scoreLabel.setText(Float.toString(offset));
        fade1 = new AlphaAction();
        fade1.setDuration(0f);
        fade2 = new AlphaAction();
        fade2.setDuration(0f);
        stage2.addAction(fade2);
        fade3 = new AlphaAction();
        fade3.setDuration(0f);
        stage1.addAction(Actions.sequence(Actions.alpha(1), Actions.fadeIn(0f)));

        buttonPlay.addListener(new InputListener() {
            public boolean touchDown(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                if (Save.gd.isSoundEnable() == 1 | Save.gd.isSoundEnable() == 2) MyGdxGame.res.getSound("select").play();
                return true;
            }

            ;

            public void touchUp(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                click_on_play = true;
                //Gdx.gl.glClearColor(0, 0, 0, 1);
            }

            ;
        });

        buttonSecret2.addListener(new InputListener() {
            public boolean touchDown(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {


                return true;
            }

            ;

            public void touchUp(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                buttonSecret2.setDisabled(true);
                buttonSecret2.setVisible(false);
                //Gdx.gl.glClearColor(0, 0, 0, 1);
            }

            ;
        });

        buttonSecret1.addListener(new InputListener() {
            public boolean touchDown(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {

                cpt_secret1++;
                System.out.println("CLICK SECRET 1: " + cpt_secret1);
                //if (Save.gd.isSoundEnable() == 1 | Save.gd.isSoundEnable() == 2) MyGdxGame.res.getMusic("secretboss").play();

                return true;
            }

            ;

            public void touchUp(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                //Gdx.gl.glClearColor(0, 0, 0, 1);
            }

            ;
        });

        MyGdxGame.setIsBoosTerritory(false);
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

        float offsetY = crop.y;
        float offsetX = crop.x;

        stage1.getViewport().update((int) (width- offsetX), (int) (height- offsetY), true);
        stage2.getViewport().update((int) (width- offsetX), (int) (height- offsetY), true);
    }

    public void handleInput() {
        if (click_on_play) {
            click_on_play = false;
            gsm.setState(GameStateManager.MENU);
            if (Save.gd.isSoundEnable() == 1 | Save.gd.isSoundEnable() == 2) MyGdxGame.res.getSound("newScreen").play();
        }
    }

    public void update(float dt) {

        if(MyGdxGame.isSoundEnable() == 2) {
            MyGdxGame.res.getMusic("main").setVolume(0.4f);
            if(!MyGdxGame.res.getMusic("main").isPlaying())
                MyGdxGame.res.getMusic("main").play();
        }

        animationPlayer.update(dt);
        animationPrincess.update(dt);

        MyGdxGame.updateBGM();
        handleInput();
        if(!MyGdxGame.isNightEnable()) {
            MyGdxGame.background_cloud.update(dt);
            MyGdxGame.background_skyDay.update(dt);
        }
        else{
            MyGdxGame.background_skyNight.update(dt);
        }
        //MyGdxGame.background_secret1.update(dt);
        MyGdxGame.background_wood1.update(dt);

        if (fade1.getTime() > fade1.getDuration()) click_on_play = true;

        if(cpt_secret1 == MyGdxGame.getTapnumbSecret()){
            cpt_secret1 = 0;
            game.actionResolver.unlockAchievementGPGS(MyGdxGame.achievementSecretBossShortcut);

            System.out.println("BOSS SHORTCUT DISCOVERED");
            MyGdxGame.setShortcutDiscovered(true);
            gsm.setState(GameStateManager.PLAY);

            if(MyGdxGame.res.getMusic("main").isPlaying())MyGdxGame.res.getMusic("main").setVolume(0);

            if(MyGdxGame.isSoundEnable() == 1 | MyGdxGame.isSoundEnable() == 2) MyGdxGame.res.getMusic("secretUnlock").play();
        }
    }

    public void render() {

        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glViewport((int) viewport.x, (int) viewport.y,
                (int) viewport.width, (int) viewport.height);

        stage1.getViewport().update((int) (viewport.width), (int) (viewport.height
        ), true);
        stage2.getViewport().update((int) (viewport.width), (int) (viewport.height
        ), true);

        if (fade2.getTime() < fade2.getDuration()) {
            stage2.act();
            sb.begin();
            stage2.draw();
            sb.end();
        }
        else {

            if(MyGdxGame.isNightEnable())
                Gdx.gl.glClearColor(65f / 255f, 18f / 255f, 252f / 255f, 1);
            else
                Gdx.gl.glClearColor(65f / 255f, 18f / 255f, 252f / 255f, 1);

            resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            Gdx.gl.glViewport((int) viewport.x, (int) viewport.y,
                    (int) viewport.width, (int) viewport.height);
            sb.setProjectionMatrix(cam.combined);
            shapeRenderer.setProjectionMatrix(cam.combined);

            if(MyGdxGame.isNightEnable()) {
                MyGdxGame.background_skyNight.render(sb);
            }else{
                MyGdxGame.background_skyDay.render(sb);
            }
            MyGdxGame.background_wood1.render(sb);
            //bg1.render(sb);
            MyGdxGame.background_tuto.render(sb);
            if(MyGdxGame.isNightEnable())MyGdxGame.displayBlinkingStars();
            stage1.act();
            sb.begin();
            stage1.draw();
            sb.end();

            sb.begin();

            sb.draw(animationPlayer.getFrame(), (MyGdxGame.V_WIDTH-animationPlayer.getFrame().getRegionWidth())/2, 202);
            sb.draw(animationPrincess.getFrame(), (MyGdxGame.V_WIDTH-animationPrincess.getFrame().getRegionWidth())/5f, 455);
            sb.draw(animationHya.getFrame(), (MyGdxGame.V_WIDTH-animationPrincess.getFrame().getRegionWidth())/5f, 520);

            //GEAR BUTTON
            float speed = 8f;
            if (stage1.getActors().items[0].getX() <= -5) {
                stage1.getActors().items[0].setPosition(-stage1.getActors().items[0].getWidth() + cpt_translate_animation1 * speed, stage1.getActors().items[0].getY());
                cpt_translate_animation1++;
            }


            if((Save.gd.isSoundEnable() == 2) && !MyGdxGame.res.getMusic("main").isPlaying()) MyGdxGame.res.getMusic("main").setVolume(0.4f);
            sb.end();


            if(!MyGdxGame.isNightEnable())
                MyGdxGame.background_cloud.render(sb);

        }
    }

    public void dispose() {
    }

}
