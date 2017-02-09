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
    private Boolean playing = false;
    private Boolean fliped = false;
    private Boolean rotate45 = false;
    private Boolean rotate45f = true;
    private Boolean rotate90 = false;
    private Boolean rotate90f = true;

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
        fliped = false;
        rotate45 = false;
        rotate45f = false;
        rotate90 = false;
        rotate90f = false;
        System.out.println("normalAnimation");
    }

    public void rotateAnimation45(){
        setLoop(true);
        TextureRegion[] sprites;
        tex = new Sprite(MyGdxGame.atlas.findRegion("handrotated45"));
        sprites = tex.split( 200, 253)[0];
        setAnimation(sprites, 1 / 10f);
        playing = false;
        fliped = false;
        rotate45 = true;
        rotate45f = false;
        rotate90 = false;
        rotate90f = false;
        System.out.println("rotateAnimation45");
    }

    public void rotateAnimation45_rev(){
        setLoop(true);
        TextureRegion[] sprites;
        tex = new Sprite(MyGdxGame.atlas.findRegion("handrotated45"));
        sprites = tex.split( 200, 253)[0];
        for (int i = 0; i <sprites.length; i++) {
            sprites[i].flip(true, false);
        }
        setAnimation(sprites, 1 / 10f);
        playing = false;
        fliped = false;
        rotate45 = false;
        rotate45f = true;
        rotate90 = false;
        rotate90f = false;
        System.out.println("rotateAnimation45_rev");
    }


    public void rotateAnimation90(){
        setLoop(true);
        TextureRegion[] sprites;
        tex = new Sprite(MyGdxGame.atlas.findRegion("handrotated90"));
        sprites = tex.split( 200, 253)[0];
        setAnimation(sprites, 1 / 10f);
        playing = false;
        fliped = false;
        rotate45 = false;
        rotate45f = false;
        rotate90 = true;
        rotate90f = false;
        System.out.println("rotateAnimation90");
    }

    public void rotateAnimation90_rev(){
        setLoop(true);
        TextureRegion[] sprites;
        tex = new Sprite(MyGdxGame.atlas.findRegion("handrotated90"));
        sprites = tex.split( 200, 253)[0];
        for (int i = 0; i <sprites.length; i++) {
            sprites[i].flip(false, true);
        }
        setAnimation(sprites, 1 / 10f);
        playing = false;
        fliped = false;
        rotate45 = false;
        rotate45f = false;
        rotate90 = false;
        rotate90f = true;
        System.out.println("rotateAnimation90_rev");
    }

    public void flipedAnimation(){
        setLoop(true);
        TextureRegion[] sprites;
        tex = new Sprite(MyGdxGame.atlas.findRegion("hand_press"));
        sprites = tex.split( 85, 108)[0];
        for (int i = 0; i <sprites.length; i++) {
            sprites[i].flip(false, true);
        }
        setAnimation(sprites, 1 / 6f);
        fliped = true;
        playing = false;
    }

    public Boolean getPlaying() {
        return playing;
    }

    public void setPlaying(Boolean playing) {
        this.playing = playing;
    }

    public Boolean getFliped() {
        return fliped;
    }

    public void setFliped(Boolean fliped) {
        this.fliped = fliped;
    }

    public Boolean getRotate45() {
        return rotate45;
    }

    public Boolean getRotate45f() {
        return rotate45f;
    }

    public Boolean getRotate90() {
        return rotate90;
    }

    public Boolean getRotate90f() {
        return rotate90f;
    }

}
