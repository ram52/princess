package com.mygdx.core.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.core.MyGdxGame;
import com.mygdx.core.handlers.Save;

public class Player extends B2DSprite 
{
    public static final int MAXFIREBALLCOUNT = 4 ;
    private int numCoins;
	private int totalCoins;
    private boolean runnigLeft = false;
    private boolean runningRight = false;
    private  boolean flyingLeft = false;
    private  boolean flyingRight = false;
    private boolean tiredRight = false;
    private boolean tiredLeft = false;
    private boolean stillRight = true;
    private boolean stillLeft = false;
    private boolean playerDead = false;
    private int fireBallCount = 3;
    private boolean right = false;
    private boolean left = false;
    private boolean isSlashingRight = false;
    private boolean isSlashingLeft = false;

	public Player(Body body, int selector)
	{
		super(body);

        Sprite tex = null;
        if(Save.gd.isExcaliburEquiped()){
            if(selector == 0) tex = new Sprite(MyGdxGame.atlas.findRegion("walkEx"));
            if(selector == 1) tex = new Sprite(MyGdxGame.atlas.findRegion("walkEx"));
            if(selector == 2) tex = new Sprite(MyGdxGame.atlas.findRegion("walkEx"));
            if(selector == 3) tex = new Sprite(MyGdxGame.atlas.findRegion("walkEx"));
        }else{
            if(selector == 0) tex = new Sprite(MyGdxGame.atlas.findRegion("walk"));
            if(selector == 1) tex = new Sprite(MyGdxGame.atlas.findRegion("walk"));
            if(selector == 2) tex = new Sprite(MyGdxGame.atlas.findRegion("walk"));
            if(selector == 3) tex = new Sprite(MyGdxGame.atlas.findRegion("walk"));
        }


		TextureRegion[] sprites = tex.split( 64, 64)[0];
		setAnimation(sprites, 1 / 5f);
        right = true;
        left = false;
	}

    public void slash_animation(int selector)
    {
        Sprite tex = null;

        if(Save.gd.isExcaliburEquiped()){
            if(selector == 0) tex = new Sprite(MyGdxGame.atlas.findRegion("slashEx"));
            if(selector == 1) tex = new Sprite(MyGdxGame.atlas.findRegion("slashEx"));
            if(selector == 2) tex = new Sprite(MyGdxGame.atlas.findRegion("slashEx"));
            if(selector == 3) tex = new Sprite(MyGdxGame.atlas.findRegion("slashEx"));
        }else{
            if(selector == 0) tex = new Sprite(MyGdxGame.atlas.findRegion("slash"));
            if(selector == 1) tex = new Sprite(MyGdxGame.atlas.findRegion("slash"));
            if(selector == 2) tex = new Sprite(MyGdxGame.atlas.findRegion("slash"));
            if(selector == 3) tex = new Sprite(MyGdxGame.atlas.findRegion("slash"));
        }

        TextureRegion[] sprites = tex.split(96, 64)[0];
        setAnimation(sprites, 1 / 15f);

        flyingRight = false;
        flyingLeft = false;
        runnigLeft = false;
        runningRight = false;
        tiredLeft = false;
        tiredRight = false;
        stillRight = false;
        stillLeft = false;
        isSlashingRight = true;
        isSlashingLeft = false;
        right = true;
        left = false;
    }

    public void slash_animation_rev(int selector)
    {
        Sprite tex = null;

        if(Save.gd.isExcaliburEquiped()){
            if(selector == 0) tex = new Sprite(MyGdxGame.atlas.findRegion("slashEx"));
            if(selector == 1) tex = new Sprite(MyGdxGame.atlas.findRegion("slashEx"));
            if(selector == 2) tex = new Sprite(MyGdxGame.atlas.findRegion("slashEx"));
            if(selector == 3) tex = new Sprite(MyGdxGame.atlas.findRegion("slashEx"));
        }else{
            if(selector == 0) tex = new Sprite(MyGdxGame.atlas.findRegion("slash"));
            if(selector == 1) tex = new Sprite(MyGdxGame.atlas.findRegion("slash"));
            if(selector == 2) tex = new Sprite(MyGdxGame.atlas.findRegion("slash"));
            if(selector == 3) tex = new Sprite(MyGdxGame.atlas.findRegion("slash"));
        }

        TextureRegion[] sprites = tex.split(96, 64)[0];

        for(int i=0;i<sprites.length;i++)
            sprites[i].flip(true,false);

        setAnimation(sprites, 1 / 15f);

        flyingRight = false;
        flyingLeft = false;
        runnigLeft = false;
        runningRight = false;
        tiredLeft = false;
        tiredRight = false;
        stillRight = false;
        stillLeft = false;
        isSlashingRight = false;
        isSlashingLeft = true;
        right = false;
        left = true;
    }

    public void still_animation(int selector)
    {
        Sprite tex = null;

        if(Save.gd.isExcaliburEquiped()){
            if(selector == 0) tex = new Sprite(MyGdxGame.atlas.findRegion("standEx"));
            if(selector == 1) tex = new Sprite(MyGdxGame.atlas.findRegion("standEx"));
            if(selector == 2) tex = new Sprite(MyGdxGame.atlas.findRegion("standEx"));
            if(selector == 3) tex = new Sprite(MyGdxGame.atlas.findRegion("standEx"));
        }else{
            if(selector == 0) tex = new Sprite(MyGdxGame.atlas.findRegion("stand"));
            if(selector == 1) tex = new Sprite(MyGdxGame.atlas.findRegion("stand"));
            if(selector == 2) tex = new Sprite(MyGdxGame.atlas.findRegion("stand"));
            if(selector == 3) tex = new Sprite(MyGdxGame.atlas.findRegion("stand"));
        }

        TextureRegion[] sprites = tex.split(64, 64)[0];

        setAnimation(sprites, 1 / 5f);

        flyingRight = false;
        flyingLeft = false;
        runnigLeft = false;
        runningRight = false;
        tiredLeft = false;
        tiredRight = false;
        stillRight = true;
        stillLeft = false;
        isSlashingRight = false;
        isSlashingLeft = false;
        right = true;
        left = false;
    }

    public void still_animation_rev(int selector)
    {
        Sprite tex = null;

        if(Save.gd.isExcaliburEquiped()){
            if(selector == 0) tex = new Sprite(MyGdxGame.atlas.findRegion("standEx"));
            if(selector == 1) tex = new Sprite(MyGdxGame.atlas.findRegion("standEx"));
            if(selector == 2) tex = new Sprite(MyGdxGame.atlas.findRegion("standEx"));
            if(selector == 3) tex = new Sprite(MyGdxGame.atlas.findRegion("standEx"));
        }else{
            if(selector == 0) tex = new Sprite(MyGdxGame.atlas.findRegion("stand"));
            if(selector == 1) tex = new Sprite(MyGdxGame.atlas.findRegion("stand"));
            if(selector == 2) tex = new Sprite(MyGdxGame.atlas.findRegion("stand"));
            if(selector == 3) tex = new Sprite(MyGdxGame.atlas.findRegion("stand"));
        }

        TextureRegion[] sprites = tex.split(64, 64)[0];

        for(int i=0;i<sprites.length;i++)
            sprites[i].flip(true,false);

        setAnimation(sprites, 1 / 5f);

        flyingRight = false;
        flyingLeft = false;
        runnigLeft = false;
        runningRight = false;
        tiredLeft = false;
        tiredRight = false;
        stillRight = false;
        stillLeft = true;
        isSlashingRight = false;
        isSlashingLeft = false;
        right = false;
        left = true;

    }


	public void running_animation(int selector)
	{
		Sprite tex = null;

        if(Save.gd.isExcaliburEquiped()){
            if(selector == 0) tex = new Sprite(MyGdxGame.atlas.findRegion("walkEx"));
            if(selector == 1) tex = new Sprite(MyGdxGame.atlas.findRegion("walkEx"));
            if(selector == 2) tex = new Sprite(MyGdxGame.atlas.findRegion("walkEx"));
            if(selector == 3) tex = new Sprite(MyGdxGame.atlas.findRegion("walkEx"));
        }else {
            if(selector == 0) tex = new Sprite(MyGdxGame.atlas.findRegion("walk"));
            if(selector == 1) tex = new Sprite(MyGdxGame.atlas.findRegion("walk"));
            if(selector == 2) tex = new Sprite(MyGdxGame.atlas.findRegion("walk"));
            if(selector == 3) tex = new Sprite(MyGdxGame.atlas.findRegion("walk"));
        }


		TextureRegion[] sprites = tex.split(64, 64)[0];
		setAnimation(sprites, 1 / 5f);

        flyingRight = false;
        flyingLeft = false;
        runnigLeft = false;
        runningRight = true;
        tiredLeft = false;
        tiredRight = false;
        stillRight = false;
        isSlashingRight = false;
        isSlashingLeft = false;
        right = true;
        left = false;
	}

	public void running_animation_rev(int selector)
	{
		Sprite tex = null;

        if(Save.gd.isExcaliburEquiped()){
            if(selector == 0) tex = new Sprite(MyGdxGame.atlas.findRegion("walkEx"));
            if(selector == 1) tex = new Sprite(MyGdxGame.atlas.findRegion("walkEx"));
            if(selector == 2) tex = new Sprite(MyGdxGame.atlas.findRegion("walkEx"));
            if(selector == 3) tex = new Sprite(MyGdxGame.atlas.findRegion("walkEx"));
        }else {
            if(selector == 0) tex = new Sprite(MyGdxGame.atlas.findRegion("walk"));
            if(selector == 1) tex = new Sprite(MyGdxGame.atlas.findRegion("walk"));
            if(selector == 2) tex = new Sprite(MyGdxGame.atlas.findRegion("walk"));
            if(selector == 3) tex = new Sprite(MyGdxGame.atlas.findRegion("walk"));
        }

		TextureRegion[] sprites = tex.split(64, 64)[0];

		for(int i=0;i<sprites.length;i++)
			sprites[i].flip(true,false);

		setAnimation(sprites, 1 / 5f);

        flyingRight = false;
        flyingLeft = false;
        runnigLeft = true;
        runningRight = false;
        tiredLeft = false;
        tiredRight = false;
        stillRight = false;
        isSlashingRight = false;
        isSlashingLeft = false;
        right = false;
        left = true;
	}
	
	public void collectCoin() {
        numCoins++;
        Save.load();
        Save.gd.setMoney(Save.gd.getMoney() + 5);
        Save.save();
    }

    public int getNumCoins() { return numCoins; }

    public void setNumCoins(int i) { numCoins = i; }

    public void setTotalCoins(int i) { totalCoins = i; }

    public int getTotalCoins() { return totalCoins; }

    public boolean isRunningLeft() {
        return runnigLeft;
    }

    public void setRunnigLeft(boolean runnigLeft) {
        this.runnigLeft = runnigLeft;
    }

    public boolean isRunningRight() {
        return runningRight;
    }

    public void setRunningRight(boolean runningRight) {
        this.runningRight = runningRight;
    }

    public boolean isFlyingLeft() {
        return flyingLeft;
    }

    public void setFlyingLeft(boolean flying) {
        this.flyingLeft = flying;
    }

    public boolean isFlyingRight() {
        return flyingRight;
    }

    public void setFlyingRight(boolean flyingRight) {
        this.flyingRight = flyingRight;
    }

    public boolean isTiredLeft() {
        return tiredLeft;
    }

    public void setTiredLeft(boolean tiredLeft) {
        this.tiredLeft = tiredLeft;
    }

    public boolean isTiredRight() {
        return tiredRight;
    }

    public void setTiredRight(boolean tiredRight) {
        this.tiredRight = tiredRight;
    }

    public boolean isStillRight() {
        return stillRight;
    }

    public void setStillRight(boolean stillRight) {
        this.stillRight = stillRight;
    }

    public boolean isStillLeft() {
        return stillLeft;
    }

    public boolean isPlayerDead() {
        return playerDead;
    }

    public void setPlayerDead(boolean playerDead) {
        this.playerDead = playerDead;
    }

    public boolean isSlashingRight() {
        return isSlashingRight;
    }

    public boolean isSlashingLeft() {
        return isSlashingLeft;
    }

    public int getFireBallCount() {
        return fireBallCount;
    }

    public void setFireBallCount(int fireBallCount) {
        this.fireBallCount = fireBallCount;
    }

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

    public void setSlashingRight(boolean slashingRight) {
        isSlashingRight = slashingRight;
    }

    public void setSlashingLeft(boolean slashingLeft) {
        isSlashingLeft = slashingLeft;
    }

}


