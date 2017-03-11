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
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.mygdx.core.MyGdxGame;
import com.mygdx.core.handlers.Animation;
import com.mygdx.core.handlers.GameStateManager;
import com.mygdx.core.handlers.Save;

import static com.mygdx.core.MyGdxGame.font;

public class Credits extends GameState {

    private static String LOG_TAG = Credits.class.getSimpleName();

    private boolean click_on_play;
    private Stage stage1, stage2;
    private AlphaAction fade1, fade2, fade3;
    private AlphaAction fade;
    private Button buttonPlay, buttonSecret1, buttonSecret2;
    private int cpt_secret1 = 0;
    private int cpt_translate_animation1 = 0;
    //private Animation animTitle;
    //private Animation animCredits;
    private Animation animationPBlue1;
    private Animation animationPrincess;
    private Animation animationEnemy;
    private Animation animationHya;
    private int time = 0;
    private Rectangle viewport;
    private Stage stage0;
    private Image intro;
    private static String CREDIT;
    private Vector2 credits_size = new Vector2(0,0);
    private ShapeRenderer shapeRenderer;
    private int numbOfLinesInCredits = 5;

    public Credits(GameStateManager gsm) {

        super(gsm);

        font.setScale(1.3f);

        CREDIT = getCreditFromFile();

        shapeRenderer = new ShapeRenderer();

        intro = new Image(MyGdxGame.atlas.findRegion("backgroundSky"));
        intro.setFillParent(true);
        stage0 = new Stage();
        stage0.addActor(intro);
        viewport = new Rectangle();

        if(MyGdxGame.isSoundEnable() == 2){
            if(!MyGdxGame.res.getMusic("shop").isPlaying()){
                MyGdxGame.res.getMusic("shop").setVolume(1.0f);
                MyGdxGame.res.getMusic("shop").play();
            }
        }

        Save.load();
        if (!Save.gd.getAdsRemoverPurchased()) {
            String network = game.actionResolver.getNetworkClass();
            if(network == null) network = "ABSENT";
            System.out.print("NETWORK: "+network);
            if(network.equals("4G")|network.equals("3G")|network.equals("WIFI"))
                game.actionResolver.showOrLoadInterstital();
            Gdx.app.debug(LOG_TAG,network);
        }

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

        //animTitle = new Animation(new Sprite(MyGdxGame.atlas.findRegion("secret")).split(95,24)[0], 1 / 5f);

        //animCredits = new Animation(new Sprite(MyGdxGame.atlas.findRegion("secret")).split(233,120)[0], 1 / 5f);

        MyGdxGame.background_cloud.setVector(+10, 0);
        //MyGdxGame.background_skyDay.setVector(0, 0);
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
        buttonPlay.setPosition(-400, Gdx.graphics.getHeight()/100f);

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

            public void touchUp(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                click_on_play = true;
            }

        });

        buttonSecret2.addListener(new InputListener() {
            public boolean touchDown(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                return true;
            }

            public void touchUp(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y, int pointer, int button) {
                buttonSecret2.setDisabled(true);
                buttonSecret2.setVisible(false);
                //Gdx.gl.glClearColor(0, 0, 0, 1);
            }
        });

        buttonSecret1.addListener(new InputListener() {
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

        MyGdxGame.setIsBoosTerritory(false);
    }

    private String getCreditFromFile(){
        String credit = ":( Sorry could not load credits file.";
        try {
            String credits = Gdx.files.internal("data/credits.txt").readString();
            String[] data = credits.split("\n");
            credit = "";
            for (String line : data) {
                numbOfLinesInCredits++;
                credit += line + "\n";
                if(font.getBounds(line).width > credits_size.x){
                    credits_size.x = font.getBounds(line).width;
                    credits_size.y = font.getBounds(credit).height;
                }
            }
        } catch (GdxRuntimeException e) {
            Gdx.app.error(LOG_TAG,"error while accessing file",e);
            credits_size.x = font.getBounds(credit).width;
            credits_size.y = font.getBounds(credit).height;
        }
        return credit;
    }

    public void handleInput() {
        if (click_on_play) {
            click_on_play = false;
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

        animationPBlue1.update(dt);
        animationEnemy.update(dt);
        animationPrincess.update(dt);

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

        if (fade1.getTime() > fade1.getDuration()) click_on_play = true;

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

        if (fade2.getTime() < fade2.getDuration()) {
            stage2.act();
            sb.begin();
            stage2.draw();
            sb.end();
        }
        else {

            sb.setProjectionMatrix(cam.combined);
            shapeRenderer.setProjectionMatrix(cam.combined);

            //MyGdxGame.background_skyDay.render(sb);

            MyGdxGame.background_wood1.render(sb);
            //bg1.render(sb);
            MyGdxGame.background_title.render(sb);

            stage1.act();
            sb.begin();
            stage1.draw();
            sb.end();

            float x = MyGdxGame.V_WIDTH/2 - credits_size.x/2;
            float y = MyGdxGame.V_HEIGHT/1.1f;

            Gdx.gl.glEnable(GL20.GL_BLEND);
            shapeRenderer.setColor(new Color(0, 0, 0, 0.8f));
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            float h = credits_size.y*numbOfLinesInCredits;
            float padding = 100;
            shapeRenderer.rect(x-padding/2,y-h-padding/2, credits_size.x+padding, h+padding);
            shapeRenderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);


            sb.begin();
            font.setColor(Color.WHITE);
            font.drawMultiLine(sb, CREDIT, x , y, credits_size.x, BitmapFont.HAlignment.CENTER);
            //GEAR BUTTON
            float speed = 8f;
            if (stage1.getActors().items[0].getX() <= -5) {
                stage1.getActors().items[0].setPosition(-stage1.getActors().items[0].getWidth() + cpt_translate_animation1 * speed, stage1.getActors().items[0].getY());
                cpt_translate_animation1++;
            }

            //if((Save.gd.isSoundEnable()==2) && !MyGdxGame.res.getMusic("main").isPlaying()) MyGdxGame.res.getMusic("main").setVolume(0.6f);
            sb.end();

            sb.begin();
            sb.draw(animationPBlue1.getFrame(), -80 + ((float) time * 1.7f), 202);
            sb.draw(animationPrincess.getFrame(), 0 + ((float) time * 1.7f), 202);
            sb.draw(animationHya.getFrame(), 0 + ((float) time * 1.7f), 250);
            sb.draw(animationEnemy.getFrame(), -250 + ((float) time * 1.7f), 202);
            sb.end();
            time+=2;


            if(time > 800){
                time = -200;
            }


        }
    }

    public void dispose() {
        font.setScale(1);
        if (MyGdxGame.res.getMusic("shop").isPlaying()) MyGdxGame.res.getMusic("shop").stop();
    }

}
