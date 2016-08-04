package com.mygdx.core.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.core.MyGdxGame;
/**
 * Created by Axel on 31/05/2016.
 */

public class Princess extends B2DSprite
{
    private int health = 20;
    private Sprite tex;
    private boolean normalRight = false;
    private boolean normalLeft = false;
    private boolean hitRight = false;
    private boolean hitLeft = false;
    private boolean cryRight = false;
    private boolean cryLeft = false;

    public boolean isDieRight() {
        return dieRight;
    }

    public void setDieRight(boolean dieRight) {
        this.dieRight = dieRight;
    }

    public boolean isDieLeft() {
        return dieLeft;
    }

    public void setDieLeft(boolean dieLeft) {
        this.dieLeft = dieLeft;
    }

    private boolean dieRight = false;
    private boolean dieLeft = false;
    private boolean dead = false;
    private boolean fadeInRight = false;
    private boolean fadeOutRight = false;
    private boolean fadeInLeft = false;
    private boolean fadeOutLeft = false;
    private int cptFadeOutRunning = 0;
    private int cptFadeInRunning = 0;
    private boolean left = false;

    public boolean isCry() {
        return cry;
    }

    public void setCry(boolean cry) {
        this.cry = cry;
    }

    private boolean cry = false;

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    private boolean right = false;

    public boolean isFading() {
        return fading;
    }

    public void setFading(boolean fading) {
        this.fading = fading;
    }

    private boolean fading = false;

    public boolean isTouched() {
        return isTouched;
    }

    public void setTouched(boolean touched) {
        isTouched = touched;
    }

    private boolean isTouched = false;

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public Princess(Body body)
    {
        super(body);
        tex = new Sprite(MyGdxGame.atlas.findRegion("princess"));
        TextureRegion[] sprites = tex.split( 64, 64)[0];
        setAnimation(sprites, 1 / 5f);

        normalRight = true;
        normalLeft = false;
        hitRight = false;
        hitLeft =false;
        cryRight = false;
        cryLeft = false;
        fadeInRight = false;
        fadeOutRight = false;
        fadeInLeft = false;
        fadeOutLeft = false;
    }

    public void fadeInAnimation(){
        tex = new Sprite(MyGdxGame.atlas.findRegion("enemyEscapeFadeIn"));
        TextureRegion[] sprites = tex.split( 64, 64)[0];
        setAnimation(sprites, 1 / 5f);
        normalRight = false;
        normalLeft = false;
        hitRight = false;
        hitLeft =false;
        cryRight = false;
        cryLeft = false;
        fadeInRight = true;
        fadeOutRight = false;
        fadeInLeft = false;
        fadeOutLeft = false;
        fading = true;
    }

    public boolean isFadeInRight() {
        return fadeInRight;
    }

    public boolean isFadeOutRight() {
        return fadeOutRight;
    }

    public boolean isFadeInLeft() {
        return fadeInLeft;
    }

    public boolean isFadeOutLeft() {
        return fadeOutLeft;
    }

    public void fadeInAnimation_rev(){
        tex = new Sprite(MyGdxGame.atlas.findRegion("enemyEscapeFadeIn"));
        TextureRegion[] sprites = tex.split( 64, 64)[0];
        for(int i=0;i<sprites.length;i++)
            sprites[i].flip(true,false);
        setAnimation(sprites, 1 / 5f);
        normalRight = false;
        normalLeft = false;
        hitRight = false;
        hitLeft =false;
        cryRight = false;
        cryLeft = false;
        fadeInRight = false;
        fadeOutRight = false;
        fadeInLeft = true;
        fadeOutLeft = false;
        fading = true;
    }

    public void fadeOutAnimation(){
        tex = new Sprite(MyGdxGame.atlas.findRegion("enemyEscapeFadeOut"));
        TextureRegion[] sprites = tex.split( 64, 64)[0];
        setAnimation(sprites, 1 / 8f);
        setLoop(false);
        normalRight = false;
        normalLeft = false;
        hitRight = false;
        hitLeft =false;
        cryRight = false;
        cryLeft = false;
        fadeInRight = false;
        fadeOutRight = true;
        fadeInLeft = false;
        fadeOutLeft = false;
        fading = true;
    }

    public void fadeOutAnimation_rev(){
        System.out.println("fadeOutAnimation_rev");
        tex = new Sprite(MyGdxGame.atlas.findRegion("enemyEscapeFadeOut"));
        TextureRegion[] sprites = tex.split( 64, 64)[0];
        for(int i=0;i<sprites.length;i++)
            sprites[i].flip(true,false);
        setAnimation(sprites, 1 / 8f);
        setLoop(false);
        normalRight = false;
        normalLeft = false;
        hitRight = false;
        hitLeft =false;
        cryRight = false;
        cryLeft = false;
        fadeInRight = false;
        fadeOutRight = false;
        fadeInLeft = false;
        fadeOutLeft = true;
        fading = true;
    }

    public void normalAnimation(){
        setLoop(true);
        tex = new Sprite(MyGdxGame.atlas.findRegion("princess"));
        TextureRegion[] sprites = tex.split( 64, 64)[0];
        setAnimation(sprites, 1 / 5f);
        normalRight = true;
        normalLeft = false;
        hitRight = false;
        hitLeft =false;
        cryRight = false;
        cryLeft = false;
    }

    public void normalAnimation_rev(){
        setLoop(true);
        tex = new Sprite(MyGdxGame.atlas.findRegion("princess"));
        TextureRegion[] sprites = tex.split( 64, 64)[0];
        for(int i=0;i<sprites.length;i++)
            sprites[i].flip(true,false);
        setAnimation(sprites, 1 / 5f);
        normalRight = false;
        normalLeft = true;
        hitRight = false;
        hitLeft =false;
        cryRight = false;
        cryLeft = false;
    }

    public void attackAnimation(){
        tex = new Sprite(MyGdxGame.atlas.findRegion("walk"));
        TextureRegion[] sprites = tex.split( 64, 64)[0];
        setAnimation(sprites, 1 / 5f);
    }

    public void cryAnimation(){
        tex = new Sprite(MyGdxGame.atlas.findRegion("princesscry"));
        TextureRegion[] sprites = tex.split( 64, 64)[0];
        setAnimation(sprites, 1 / 5f);
        normalRight = false;
        normalLeft = false;
        hitRight = false;
        hitLeft = false;
        cryRight = true;
        cryLeft = false;
    }

    public void cryAnimation_rev(){
        tex = new Sprite(MyGdxGame.atlas.findRegion("princesscry"));
        TextureRegion[] sprites = tex.split( 64, 64)[0];
        for(int i=0;i<sprites.length;i++)
            sprites[i].flip(true,false);
        setAnimation(sprites, 1 / 5f);
        normalRight = false;
        normalLeft = false;
        hitRight = false;
        hitLeft = false;
        cryRight = false;
        cryLeft = true;
        fadeInRight = false;
        fadeOutRight = false;
        fadeInLeft = false;
        fadeOutLeft = false;
    }

    public void dieAnimation(){
        setLoop(false);
        tex = new Sprite(MyGdxGame.atlas.findRegion("princessdie"));
        TextureRegion[] sprites = tex.split( 64, 64)[0];
        setAnimation(sprites, 1 / 5f);
        normalRight = false;
        normalLeft = false;
        hitRight = false;
        hitLeft = false;
        cryRight = true;
        cryLeft = false;
        dieLeft = false;
        dieRight = true;
    }

    public void dieAnimation_rev(){
        setLoop(false);
        tex = new Sprite(MyGdxGame.atlas.findRegion("princessdie"));
        TextureRegion[] sprites = tex.split( 64, 64)[0];
        for(int i=0;i<sprites.length;i++)
            sprites[i].flip(true,false);
        setAnimation(sprites, 1 / 5f);
        normalRight = false;
        normalLeft = false;
        hitRight = false;
        hitLeft = false;
        cryRight = false;
        cryLeft = true;
        fadeInRight = false;
        fadeOutRight = false;
        fadeInLeft = false;
        fadeOutLeft = false;
        dieLeft = true;
        dieRight = false;
    }

    public void hurtAnimation(){
        tex = new Sprite(MyGdxGame.atlas.findRegion("walk"));
        TextureRegion[] sprites = tex.split( 64, 64)[0];
        setAnimation(sprites, 1 / 5f);

        normalRight = false;
        normalLeft = false;
        hitRight = true;
        hitLeft =false;
        cryRight = false;
        cryLeft = false;
        fadeInRight = false;
        fadeOutRight = false;
        fadeInLeft = false;
        fadeOutLeft = false;
    }

    public void hurtAnimation_rev(){
        tex = new Sprite(MyGdxGame.atlas.findRegion("walk"));
        TextureRegion[] sprites = tex.split( 64, 64)[0];
        for(int i=0;i<sprites.length;i++)
            sprites[i].flip(true,false);
        setAnimation(sprites, 1 / 5f);
        normalRight = false;
        normalLeft = false;
        hitRight = false;
        hitLeft = true;
        cryRight = false;
        cryLeft = false;
        fadeInRight = false;
        fadeOutRight = false;
        fadeInLeft = false;
        fadeOutLeft = false;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public boolean isNormalRight() {
        return normalRight;
    }

    public void setNormalRight(boolean normalRight) {
        this.normalRight = normalRight;
    }

    public boolean isNormalLeft() {
        return normalLeft;
    }

    public void setNormalLeft(boolean normalLeft) {
        this.normalLeft = normalLeft;
    }

    public boolean isHitRight() {
        return hitRight;
    }

    public void setHitRight(boolean hitRight) {
        this.hitRight = hitRight;
    }

    public boolean isHitLeft() {
        return hitLeft;
    }

    public void setHitLeft(boolean hitLeft) {
        this.hitLeft = hitLeft;
    }

    public boolean isCryRight() {
        return cryRight;
    }

    public void setCryRight(boolean cryRight) {
        this.cryRight = cryRight;
    }

    public boolean isCryLeft() {
        return cryLeft;
    }

    public void setCryLeft(boolean cryLeft) {
        this.cryLeft = cryLeft;
    }

    public boolean isJumpOver() {
        return jumpOver;
    }

    public void setJumpOver(boolean jumpOver) {
        this.jumpOver = jumpOver;
    }

    private boolean jumpOver = false;

    public int getCptFadeOutRunning() {
        return cptFadeOutRunning;
    }

    public void setCptFadeOutRunning(int cptFadeOutRunning) {
        this.cptFadeOutRunning = cptFadeOutRunning;
    }

    public int getCptFadeInRunning() {
        return cptFadeInRunning;
    }

    public void setCptFadeInRunning(int cptFadeInRunning) {
        this.cptFadeInRunning = cptFadeInRunning;
    }
}