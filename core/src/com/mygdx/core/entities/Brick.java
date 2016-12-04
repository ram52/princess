package com.mygdx.core.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.core.MyGdxGame;

/**
 * Created by Axel on 23/11/2016.
 */

public class Brick extends B2DSprite{

    private Sprite tex;
    private Boolean broken;
    private Boolean normal;
    private Boolean dead;
    private Boolean hurt;
    private int life = 300;

    public Boolean getNormal() {
        return normal;
    }

    public void setNormal(Boolean normal) {
        this.normal = normal;
    }

    public Boolean getHurt() {
        return hurt;
    }

    public void setHurt(Boolean hurt) {
        this.hurt = hurt;
    }

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

    public Boolean getDead() {
        return dead;
    }

    public void setDead(Boolean dead) {
        this.dead = dead;
    }

    public Brick(Body body) {
        super(body);
        setLoop(false);
        tex = new Sprite(MyGdxGame.atlas.findRegion("brick"));
        TextureRegion[] sprites = tex.split( 64, 64)[0];
        setAnimation(sprites, 0);
        broken = false;
        hurt = false;
        dead = false;
        normal = false;
    }

    public void normalAnimation(){
        if (!broken){
            setLoop(false);
            tex = new Sprite(MyGdxGame.atlas.findRegion("brick"));
            TextureRegion[] sprites = tex.split( 64, 64)[0];
            setAnimation(sprites, 1 / 5f);
            normal = true;
            broken = false;
            hurt = false;
        }
    }

    public void brokeAnimation(){
        if (!broken){
            setLoop(false);
            tex = new Sprite(MyGdxGame.atlas.findRegion("brick_breacking"));
            TextureRegion[] sprites = tex.split( 64, 64)[0];
            setAnimation(sprites, 1 / 5f);
            normal = false;
            broken = true;
            hurt = false;

            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            setDead(true);
                            destroy();
                        }
                    },
                    800
            );
        }
    }

    public void hurtAnimation(){
        if(!hurt && !broken){
            setLoop(true);
            tex = new Sprite(MyGdxGame.atlas.findRegion("brick_hurt"));
            TextureRegion[] sprites = tex.split( 64, 64)[0];
            setAnimation(sprites, 1 / 5f);
            normal = false;
            broken = false;
            hurt = true;
        }
    }

}

