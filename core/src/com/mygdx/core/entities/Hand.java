package com.mygdx.core.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.core.MyGdxGame;

/**
 * Created by Axel on 22/01/2017.
 */

public class Hand extends B2DSprite{

    private Sprite tex;

    public Boolean getPlaying() {
        return playing;
    }

    public void setPlaying(Boolean playing) {
        this.playing = playing;
    }

    private Boolean playing = false;

    public Hand(Body body) {
        super(body);
        TextureRegion[] sprites;
        tex = new Sprite(MyGdxGame.atlas.findRegion("hand_press"));
        sprites = tex.split( 85, 108)[0];
        setAnimation(sprites, 1 / 6f);
        //playing = true;
    }

    public void normalAnimation(){
        setLoop(true);
        TextureRegion[] sprites;
        tex = new Sprite(MyGdxGame.atlas.findRegion("hand_press"));
        sprites = tex.split( 85, 108)[0];
        setAnimation(sprites, 1 / 6f);
        playing = true;
    }

}
