package com.mygdx.core.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.core.MyGdxGame;

/**
 * Created by Axel on 23/11/2016.
 */

public class Block extends B2DSprite{

    private Sprite tex;
    private Boolean broken;

    public Boolean getDead() {
        return dead;
    }

    public void setDead(Boolean dead) {
        this.dead = dead;
    }

    private Boolean dead;
    private int life = 3;

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public Boolean getBroken() {
        return broken;
    }

    public void setBroken(Boolean broken) {
        this.broken = broken;
    }

    public Block(Body body) {
        super(body);
        setLoop(false);
        tex = new Sprite(MyGdxGame.atlas.findRegion("block"));
        TextureRegion[] sprites = tex.split( 64, 64)[0];
        setAnimation(sprites, 0);
        broken = false;
        dead = false;
    }

    public void brokeAnimation(){
        setLoop(false);
        tex = new Sprite(MyGdxGame.atlas.findRegion("block"));
        TextureRegion[] sprites = tex.split( 64, 64)[0];
        setAnimation(sprites, 1 / 5f);
        broken = true;
    }

}

