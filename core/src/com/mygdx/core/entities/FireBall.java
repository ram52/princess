package com.mygdx.core.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.core.MyGdxGame;
import com.mygdx.core.handlers.Save;

/**
 * Created by Axel on 26/07/2016.
 */
public class FireBall extends B2DSprite{

    private Sprite tex;
    private Boolean plasma = false;
    private Boolean left;
    private Boolean right;
    private Boolean dead;
    private int health;

    public FireBall(Body body) {
        super(body);
        TextureRegion[] sprites;
        if(Save.gd.isFireBall2Equiped()){
            tex = new Sprite(MyGdxGame.atlas.findRegion("fireball2"));
            sprites = tex.split( 179, 128)[0];
            health = 3;
        }else{
            tex = new Sprite(MyGdxGame.atlas.findRegion("fireball"));
            sprites = tex.split( 64, 64)[0];
            health = 1;
        }

        setAnimation(sprites, 1 / 6f);
        left = false;
        right = false;
        dead = false;

    }

    public void normalAnimation(){
        setLoop(true);
        TextureRegion[] sprites;
        if(Save.gd.isFireBall2Equiped()){
            tex = new Sprite(MyGdxGame.atlas.findRegion("fireball2"));
            sprites = tex.split( 179, 128)[0];
        }else{
            tex = new Sprite(MyGdxGame.atlas.findRegion("fireball"));
            sprites = tex.split( 64, 64)[0];
        }
        setAnimation(sprites, 1 / 6f);
        left = false;
        right = true;
    }

    public void normalAnimation_rev(){
        setLoop(true);
        TextureRegion[] sprites;
        if(Save.gd.isFireBall2Equiped()){
            tex = new Sprite(MyGdxGame.atlas.findRegion("fireball2"));
            sprites = tex.split( 179, 128)[0];
        }else{
            tex = new Sprite(MyGdxGame.atlas.findRegion("fireball"));
            sprites = tex.split( 64, 64)[0];
        }
        for(int i=0;i<sprites.length;i++)
            sprites[i].flip(true,false);
        setAnimation(sprites, 1 / 6f);
        left = true;
        right = false;
    }

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

    public Boolean getPlasma() {
        return plasma;
    }

    public void setPlasma(Boolean plasma) {
        this.plasma = plasma;
    }

    public Boolean getDead() {
        return dead;
    }

    public void setDead(Boolean dead) {
        this.dead = dead;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

}
