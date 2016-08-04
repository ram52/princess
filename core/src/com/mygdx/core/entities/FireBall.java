package com.mygdx.core.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.core.MyGdxGame;

/**
 * Created by Axel on 26/07/2016.
 */
public class FireBall extends B2DSprite{

    private Sprite tex;

    public Boolean getLeft() {
        return left;
    }

    public void setLeft(Boolean left) {
        this.left = left;
    }

    public Boolean getRight() {
        return right;
    }

    public void setRight(Boolean right) {
        this.right = right;
    }

    private Boolean left;
    private Boolean right;

    public Boolean getDead() {
        return dead;
    }

    public void setDead(Boolean dead) {
        this.dead = dead;
    }

    private Boolean dead;

    public FireBall(Body body) {
        super(body);
        tex = new Sprite(MyGdxGame.atlas.findRegion("fireball"));
        TextureRegion[] sprites = tex.split( 64, 64)[0];
        setAnimation(sprites, 1 / 5f);
        left = false;
        right = false;
        dead = false;
    }

    public void normalAnimation(){
        setLoop(true);
        tex = new Sprite(MyGdxGame.atlas.findRegion("fireball"));
        TextureRegion[] sprites = tex.split( 64, 64)[0];
        setAnimation(sprites, 1 / 5f);
        left = false;
        right = true;
    }

    public void normalAnimation_rev(){
        setLoop(true);
        tex = new Sprite(MyGdxGame.atlas.findRegion("fireball"));
        TextureRegion[] sprites = tex.split( 64, 64)[0];
        for(int i=0;i<sprites.length;i++)
            sprites[i].flip(true,false);
        setAnimation(sprites, 1 / 5f);
        left = true;
        right = false;
    }

}
