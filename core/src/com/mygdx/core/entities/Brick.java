package com.mygdx.core.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
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
    private int life = 400;
    private float falling_cpt = 0;
    private boolean falling = false;
    private boolean summoned = false;
    private float x = 0;
    private float y = 0;
    public Array<Enemy> enemies;

    public Array<Enemy> getEnemies() {
        return enemies;
    }

    public void setEnemies(Array<Enemy> enemies) {
        this.enemies = enemies;
    }

    public boolean isSummoned() {
        return summoned;
    }

    public void setSummoned(boolean summoned) {
        this.summoned = summoned;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getFalling_cpt() {
        return falling_cpt;
    }

    public void setFalling_cpt(float falling_cpt) {
        this.falling_cpt = falling_cpt;
    }

    public boolean isFalling() {
        return falling;
    }

    public void setFalling(boolean falling) {
        this.falling = falling;
    }

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
        enemies = new Array<Enemy>();
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

            for (Enemy enemy: enemies) {
                enemy.setStop(false);
            }

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

