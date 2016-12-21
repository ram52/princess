package com.mygdx.core.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.mygdx.core.handlers.Animation;
import com.mygdx.core.handlers.B2DVars;


public class B2DSprite {

	protected Body body;
	protected Animation animation;
	protected float width;
	protected float height;
	protected BoundingBox boundingBox;
	protected Vector2 size;
	protected float fadeInTimeAlpha = 0;
	protected float fadeOutTimeAlpha = 1.0f;
	protected int regSize = 0;

	public boolean isLoop() {
		return loop;
	}

	public void setLoop(boolean loop) {
		this.loop = loop;
	}

	protected boolean loop = true;

	public B2DSprite(Body body) {
		this.body = body;
		animation = new Animation();
		size = new Vector2(0,0);
		width = 0;
		height = 0;
		boundingBox = new BoundingBox();
		boundingBox.set(new Vector3(0,0,0), new Vector3(0,0,0));
	}

	public B2DSprite(TextureRegion[] reg, float delay) {
		body = null;
		animation = new Animation();
		setAnimation(reg, delay);
		size = new Vector2(0,0);
		width = 0;
		height = 0;
		boundingBox = new BoundingBox();
		boundingBox.set(new Vector3(0,0,0), new Vector3(0,0,0));
	}

	public void updateBoundingBox(B2DSprite player){
		Vector2 origin = new Vector2(player.getPosition().x*100 - player.getWidth()/2, player.getPosition().y*100 - player.getHeight()/2);
		Vector2 originExt = new Vector2(player.getPosition().x*100 - (player.getWidth()/2)*3, player.getPosition().y*100 - player.getHeight()/2);
		//boundingBox = new BoundingBox(origin.x, origin.y, 64, 64);
		boundingBox.set(new Vector3((int)origin.x,(int)origin.y,0), new Vector3((int)origin.x+64,(int)origin.y+64,10));
	}

	public Vector2 getSize() {
		return size;
	}

	public void setSize(Vector2 size) {
		this.size = size;
	}

	public void setAnimation(TextureRegion[] reg, float delay) {
		animation.setFrames(reg, delay);
		width = reg[0].getRegionWidth();
		height = reg[0].getRegionHeight();
		regSize = reg.length;
	}

	public void reset(){
		animation.setCurrentFrame(0);
	}

	public void update(float dt) {
		if(!loop){
			if(animation.getCurrentFrame() != regSize-1){
				animation.update(dt);
			}
		}else{
			animation.update(dt);
		}
	}

	public void render(SpriteBatch sb) {
		sb.begin();
		sb.draw(
				animation.getFrame(),
				(body == null)? 0:body.getPosition().x * B2DVars.PPM - width / 2,
				(body == null)? 0:body.getPosition().y * B2DVars.PPM - height / 2,
				width,
				height
		);
		sb.end();
	}

	public Body getBody() { return body; }
	public Vector2 getPosition() { return body.getPosition(); }
	public float getWidth() { return width; }
	public float getHeight() { return height; }


	public void setBody(Body body) {
		this.body = body;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public float getFadeInTimeAlpha() {
		return fadeInTimeAlpha;
	}

	public void setFadeInTimeAlpha(int fadeInTimeAlpha) {
		this.fadeInTimeAlpha = fadeInTimeAlpha;
	}

	public float getFadeOutTimeAlpha() {
		return fadeOutTimeAlpha;
	}

	public void setFadeOutTimeAlpha(int fadeOutTimeAlpha) {
		this.fadeOutTimeAlpha = fadeOutTimeAlpha;
	}

	public BoundingBox getBoundingBox() {
		return boundingBox;
	}

	public Animation getAnimation() {
		return animation;
	}

	public void destroy(){
		for (Fixture fixture: getBody().getFixtureList()) {
			getBody().destroyFixture(fixture);
		}
	}
}


