package com.mygdx.core.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.mygdx.core.MyGdxGame;

public class Enemy extends B2DSprite 
{
	private double health = 20;
	private Sprite tex;
    private boolean normalRight = false;
    private boolean normalLeft = false;
    private boolean climbing = false;
    private boolean climbRight = false;
    private boolean climblLeft = false;
    private boolean hitRight = false;
    private boolean hitLeft = false;
    private boolean dieRight = false;
    private boolean dieLeft = false;
    private boolean dead = false;
    private boolean fadeInRight = false;
    private boolean fadeOutRight = false;
    private boolean fadeInLeft = false;
    private boolean fadeOutLeft = false;
    private int cptFadeOutRunning = 0;
    private int cptFadeInRunning = 0;
    private boolean mockRight = false;
    private boolean mockLeft = false;
    private int cptWaitRunning = 0;
    private boolean right = false;
    private int cptDieRunning = 0;
    private boolean hurtRight = false;
    private boolean hurtLeft = false;
    private float speed = 0.0f;
    private boolean stop = false;
    private  boolean isMalicious = false;
    private boolean left = false;
    private boolean fromLeft = false;
    private boolean isTouched = false;

    public boolean isJump() {
        return jump;
    }

    public void setJump(boolean jump) {
        this.jump = jump;
    }

    private boolean jump = false;

    public boolean isTouchBrick() {
        return touchBrick;
    }

    public void setTouchBrick(boolean touchBrick) {
        this.touchBrick = touchBrick;
    }

    private boolean touchBrick = false;

    public boolean isClimbRight() {
        return climbRight;
    }

    public void setClimbRight(boolean climbRight) {
        this.climbRight = climbRight;
    }

    public boolean isClimblLeft() {
        return climblLeft;
    }

    public void setClimblLeft(boolean climblLeft) {
        this.climblLeft = climblLeft;
    }

    public boolean isClimbing() {
        return climbing;
    }

    public void setClimbing(boolean climbing) {
        this.climbing = climbing;
    }

    public boolean isMockLeft() {
        return mockLeft;
    }

    public void setMockLeft(boolean mockLeft) {
        this.mockLeft = mockLeft;
    }

    public boolean isMockRight() {
        return mockRight;
    }

    public void setMockRight(boolean mockRight) {
        this.mockRight = mockRight;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public boolean isStop() {
        return stop;
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }

    public boolean isHurtRight() {
        return hurtRight;
    }

    public void setHurtRight(boolean hurtRight) {
        this.hurtRight = hurtRight;
    }

    public boolean isHurtLeft() {
        return hurtLeft;
    }

    public void setHurtLeft(boolean hurtLeft) {
        this.hurtLeft = hurtLeft;
    }

    public int getCptDieRunning() {
        return cptDieRunning;
    }

    public void setCptDieRunning(int cptDieRunning) {
        this.cptDieRunning = cptDieRunning;
    }

    public boolean isMalicious() {
        return isMalicious;
    }

    public void setMalicious(boolean malicious) {
        isMalicious = malicious;
    }

    public int getCptWaitRunning() {
        return cptWaitRunning;
    }

    public void setCptWaitRunning(int cptWaitRunning) {
        this.cptWaitRunning = cptWaitRunning;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean isFromLeft() {
        return fromLeft;
    }

    public void setFromLeft(boolean fromLeft) {
        this.fromLeft = fromLeft;
    }

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

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

	public Enemy(Body body, boolean fromLeft, boolean isMalicious, float speed)
	{	
		super(body);
        this.fromLeft = fromLeft;
        this.isMalicious = isMalicious;
        this.speed = speed;
		tex = new Sprite(MyGdxGame.atlas.findRegion("enemy"));
		TextureRegion[] sprites = tex.split( 64, 64)[0];
		setAnimation(sprites, 1 / 5f);

        normalRight = true;
        normalLeft = false;
        hitRight = false;
        hitLeft =false;
        dieRight = false;
        dieLeft = false;
        fadeInRight = false;
        fadeOutRight = false;
        fadeInLeft = false;
        fadeOutLeft = false;
        climblLeft = false;
        climbRight = false;
        climbing = false;
        hurtRight = false;
        hurtLeft = false;
	}

    public void fadeInAnimation(){
        tex = new Sprite(MyGdxGame.atlas.findRegion("enemyEscapeFadeIn"));
        TextureRegion[] sprites = tex.split( 64, 64)[0];
        setAnimation(sprites, 1 / 5f);
        normalRight = false;
        normalLeft = false;
        hitRight = false;
        hitLeft =false;
        dieRight = false;
        dieLeft = false;
        fadeInRight = true;
        fadeOutRight = false;
        fadeInLeft = false;
        fadeOutLeft = false;
        fading = true;
        climblLeft = false;
        climbRight = false;
        climbing = false;
        hurtRight = false;
        hurtLeft = false;
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
        dieRight = false;
        dieLeft = false;
        fadeInRight = false;
        fadeOutRight = false;
        fadeInLeft = true;
        fadeOutLeft = false;
        fading = true;
        climblLeft = false;
        climbRight = false;
        climbing = false;
        hurtRight = false;
        hurtLeft = false;
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
        dieRight = false;
        dieLeft = false;
        fadeInRight = false;
        fadeOutRight = true;
        fadeInLeft = false;
        fadeOutLeft = false;
        fading = true;
        climblLeft = false;
        climbRight = false;
        climbing = false;
        hurtRight = false;
        hurtLeft = false;
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
        dieRight = false;
        dieLeft = false;
        fadeInRight = false;
        fadeOutRight = false;
        fadeInLeft = false;
        fadeOutLeft = true;
        fading = true;
        climblLeft = false;
        climbRight = false;
        climbing = false;
        hurtRight = false;
        hurtLeft = false;
    }

    public void climbAnimation(){
        setLoop(true);
        tex = new Sprite(MyGdxGame.atlas.findRegion("enemyclimb"));
        TextureRegion[] sprites = tex.split( 64, 64)[0];

        setAnimation(sprites, 1 / 5f);
        normalRight = false;
        normalLeft = false;
        hitRight = false;
        hitLeft =false;
        dieRight = false;
        dieLeft = false;
        climblLeft = false;
        climbRight = true;
        climbing = true;
        hurtRight = false;
        hurtLeft = false;
    }

    public void climbAnimation_rev(){
        setLoop(true);
        tex = new Sprite(MyGdxGame.atlas.findRegion("enemyclimb"));
        TextureRegion[] sprites = tex.split( 64, 64)[0];
        for(int i=0;i<sprites.length;i++)
            sprites[i].flip(true,false);
        setAnimation(sprites, 1 / 5f);
        normalRight = false;
        normalLeft = false;
        hitRight = false;
        hitLeft =false;
        dieRight = false;
        dieLeft = false;
        climblLeft = true;
        climbRight = false;
        climbing = true;
        hurtRight = false;
        hurtLeft = false;
    }

    public void normalAnimation(){
        setLoop(true);
        tex = new Sprite(MyGdxGame.atlas.findRegion("enemy"));
        TextureRegion[] sprites = tex.split( 64, 64)[0];
        setAnimation(sprites, 1 / 5f);
        normalRight = true;
        normalLeft = false;
        hitRight = false;
        hitLeft =false;
        dieRight = false;
        dieLeft = false;
        climblLeft = false;
        climbRight = false;
        climbing = false;
        hurtRight = false;
        hurtLeft = false;
        fading = false;
    }

    public void normalAnimation_rev(){
        setLoop(true);
        tex = new Sprite(MyGdxGame.atlas.findRegion("enemy"));
        TextureRegion[] sprites = tex.split( 64, 64)[0];
        for(int i=0;i<sprites.length;i++)
            sprites[i].flip(true,false);
        setAnimation(sprites, 1 / 5f);
        normalRight = false;
        normalLeft = true;
        hitRight = false;
        hitLeft =false;
        dieRight = false;
        dieLeft = false;
        climblLeft = false;
        climbRight = false;
        climbing = false;
        hurtRight = false;
        hurtLeft = false;
        fading = false;
    }

    public void attackAnimation(){
        tex = new Sprite(MyGdxGame.atlas.findRegion("walk"));
        TextureRegion[] sprites = tex.split( 64, 64)[0];
        setAnimation(sprites, 1 / 5f);
    }

    public void dieAnimation(boolean isClimbing){
        setLoop(false);
        if(isClimbing){
            tex = new Sprite(MyGdxGame.atlas.findRegion("enemyClimbDie"));
        }else{
            tex = new Sprite(MyGdxGame.atlas.findRegion("enemyGroundDie"));
        }
        TextureRegion[] sprites = tex.split( 64, 64)[0];
        setAnimation(sprites, 1 / 5f);

        for (Fixture fixture: getBody().getFixtureList()) {
            fixture.setSensor(true);
        }

        normalRight = false;
        normalLeft = false;
        hitRight = false;
        hitLeft = false;
        dieRight = true;
        dieLeft = false;
        hurtRight = false;
        hurtLeft = false;
    }

    public void dieAnimation_rev(boolean isClimbing){
        setLoop(false);
        if(isClimbing){
            tex = new Sprite(MyGdxGame.atlas.findRegion("enemyClimbDie"));
        }else{
            tex = new Sprite(MyGdxGame.atlas.findRegion("enemyGroundDie"));
        }
        TextureRegion[] sprites = tex.split( 64, 64)[0];
        for(int i=0;i<sprites.length;i++)
            sprites[i].flip(true,false);
        setAnimation(sprites, 1 / 5f);
        normalRight = false;
        normalLeft = false;
        hitRight = false;
        hitLeft = false;
        dieRight = false;
        dieLeft = true;
        fadeInRight = false;
        fadeOutRight = false;
        fadeInLeft = false;
        fadeOutLeft = false;
        hurtRight = false;
        hurtLeft = false;
    }

    public void mockAnimation(){
        tex = new Sprite(MyGdxGame.atlas.findRegion("enemyMock"));
        TextureRegion[] sprites = tex.split( 64, 64)[0];
        setAnimation(sprites, 1 / 10f);
        mockRight = true;
        mockLeft = false;
    }

    public void mockAnimation_rev(){
        tex = new Sprite(MyGdxGame.atlas.findRegion("enemyMock"));
        TextureRegion[] sprites = tex.split( 64, 64)[0];
        for(int i=0;i<sprites.length;i++)
            sprites[i].flip(true,false);
        setAnimation(sprites, 1 / 10f);
        mockRight = false;
        mockLeft = true;
    }

    public void hurtAnimation(boolean isClimbing){
        setLoop(true);
        if(isClimbing){
            tex = new Sprite(MyGdxGame.atlas.findRegion("enemyClimbHurt"));
        }else{
            tex = new Sprite(MyGdxGame.atlas.findRegion("enemyGroundHurt"));
        }
        TextureRegion[] sprites = tex.split( 64, 64)[0];
        setAnimation(sprites, 1 / 10f);
        normalRight = false;
        normalLeft = false;
        hitRight = false;
        hitLeft = false;
        dieRight = false;
        dieLeft = false;
        hurtRight = true;
        hurtLeft = false;
        fading = false;
    }

    public void hurtAnimation_rev(boolean isClimbing){
        setLoop(true);
        if(isClimbing){
            tex = new Sprite(MyGdxGame.atlas.findRegion("enemyClimbHurt"));
        }else{
            tex = new Sprite(MyGdxGame.atlas.findRegion("enemyGroundHurt"));
        }
        TextureRegion[] sprites = tex.split( 64, 64)[0];
        for(int i=0;i<sprites.length;i++)
            sprites[i].flip(true,false);
        setAnimation(sprites, 1 / 10f);
        normalRight = false;
        normalLeft = false;
        hitRight = false;
        hitLeft = false;
        dieRight = false;
        dieLeft = false;
        fadeInRight = false;
        fadeOutRight = false;
        fadeInLeft = false;
        fadeOutLeft = false;
        hurtRight = false;
        hurtLeft = true;
    }


	public double getHealth() {
		return health;
	}

	public void setHealth(double health) {
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

    public boolean isJumpOver() {
        return jumpOver;
    }

    public void setJumpOver(boolean jumpOver) {
        this.jumpOver = jumpOver;
    }

    private boolean jumpOver = false;

    public boolean isWaited() {
        return waited;
    }

    public void setWaited(boolean waited) {
        this.waited = waited;
    }

    private boolean waited = false;

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

    @Override
    public String toString() {
        return "Enemy:="+"isDead:"+isDead()+" "+"health:"+health+" "+"normalRight:"+normalRight+" "+"normalLeft:"+normalLeft+" "+"fromLeft:"+fromLeft+" "+"climbRight:"+climbRight+" "+"climblLeft:"+climblLeft;
    }


}


