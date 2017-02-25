package com.mygdx.core.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.mygdx.core.MyGdxGame;
import com.mygdx.core.states.Shop;

/**
 * Created by Axel on 03/08/2016.
 */
public class ShopDialog extends Dialog{


    public ShopDialog(String title, Skin skin) {
        super(title, skin);
    }


    public ShopDialog(Shop shop, String description, String drawable){
        super("", new Skin(Gdx.files.internal("uiskin.json")), "dialog");

//        Dialog dialog = new Dialog("", new Skin(Gdx.files.internal("uiskin.json")), "dialog") {
//            public void result(Object obj) {
//                Gdx.app.debug(LOG_TAG,"result "+obj);
//            }
//        };


        padTop(-Gdx.graphics.getHeight()/40f);
        //dialog.padBottom(Gdx.graphics.getHeight()/60);
        BitmapFont font = new BitmapFont( Gdx.files.internal(MyGdxGame.fontCreditsPath));
        setStyle(new Window.WindowStyle(font, Color.BLACK, shop.skin.getDrawable("black")));

        Label label = new Label(description, new Label.LabelStyle(new BitmapFont(Gdx.files.internal(MyGdxGame.fontTextPath), false), Color.WHITE));
        label.setFontScaleY(Gdx.graphics.getWidth() / 600f);
        label.setFontScaleX(Gdx.graphics.getWidth() / 600f);

        Label yes = new Label("Yes", new Label.LabelStyle(new BitmapFont(Gdx.files.internal(MyGdxGame.fontTextPath), false), Color.WHITE));
        yes.setFontScaleY(Gdx.graphics.getWidth() / 600f);
        yes.setFontScaleX(Gdx.graphics.getWidth() / 600f);
        Label no = new Label("No", new Label.LabelStyle(new BitmapFont(Gdx.files.internal(MyGdxGame.fontTextPath), false), Color.WHITE));
        no.setFontScaleY(Gdx.graphics.getWidth() / 600f);
        no.setFontScaleX(Gdx.graphics.getWidth() / 600f);

        label.setFontScaleY(Gdx.graphics.getWidth() / 600f);
        label.setFontScaleX(Gdx.graphics.getWidth() / 600f);
        text(label);
        //dialog.text("\nFIRE BALL\n\nFire ball can destroy 1 enemy in 1 hit.\nCOST: 1000 coins.\n\nEquip Fire ball?");

        Button.ButtonStyle style = new Button.ButtonStyle();
        style.up = shop.skin.getDrawable("buttonYesUp");
        style.down = shop.skin.getDrawable("buttonYesDown");
        Button buttonYes = new Button(style);
        buttonYes.setWidth(Gdx.graphics.getWidth()/5);
        buttonYes.setHeight(Gdx.graphics.getWidth()/5);
        button(buttonYes, true); //sends "true" as the result
        style = new Button.ButtonStyle();
        style.up = shop.skin.getDrawable("buttonNoUp");
        style.down = shop.skin.getDrawable("buttonNoDown");
        Button buttonNo = new Button(style);
        buttonYes.setWidth(Gdx.graphics.getWidth()/5);
        buttonYes.setHeight(Gdx.graphics.getWidth()/5);
        button(buttonNo, false);  //sends "false" as the result


        getContentTable().getCells().get(0).padLeft(Gdx.graphics.getWidth()/4);

        getButtonTable().getCells().get(0).width(Gdx.graphics.getWidth()/5);
        getButtonTable().getCells().get(0).height(Gdx.graphics.getWidth()/5);
        getButtonTable().getCells().get(0).padBottom(Gdx.graphics.getWidth()/20f);
        getButtonTable().getCells().get(0).padLeft(Gdx.graphics.getWidth()/7.5f);

        getButtonTable().getCells().get(1).width(Gdx.graphics.getWidth()/5);
        getButtonTable().getCells().get(1).height(Gdx.graphics.getWidth()/5);
        getButtonTable().getCells().get(1).padBottom(Gdx.graphics.getWidth()/20f);
        getButtonTable().getCells().get(1).padLeft(Gdx.graphics.getWidth()/10);

        key(Input.Keys.ENTER, true); //sends "true" when the ENTER key is pressed
        Image image = new Image(shop.skin.getDrawable(drawable));
        image.setSize(Gdx.graphics.getWidth()/4, Gdx.graphics.getWidth()/4);
        setHeight(image.getHeight()*2.2f);
        setWidth(Gdx.graphics.getWidth());

        addActor(image);

        for(Actor actor : getChildren()){
            actor.setY(actor.getY()+actor.getWidth()*1.0f);
            actor.setX(actor.getX()+actor.getWidth()/8);
        }

        setPosition(Gdx.graphics.getWidth()/2 - getWidth()/2, Gdx.graphics.getHeight()/2 - getHeight()/2);
    }
}
