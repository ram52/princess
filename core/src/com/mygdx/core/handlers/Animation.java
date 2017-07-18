package com.mygdx.core.handlers;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class Animation {

	public TextureRegion[] getFrames() {
		return frames;
	}

	public void setFrames(TextureRegion[] frames) {
		this.frames = frames;
	}

	private TextureRegion[] frames;
	private float time;
	private float delay;
    private boolean loopEnable = true;
    private int currentFrame;
    private int timesPlayed;
	private int stepSpeed = 1;

    public void setTimesPlayed(int timesPlayed) {
        this.timesPlayed = timesPlayed;
    }
	
	public Animation() {}

	public Animation(TextureRegion[] frames) {
		this(frames, 1 / 5f);
	}
	
	public Animation(TextureRegion[] frames, float delay) {
		setFrames(frames, delay);
	}
	
	public void setFrames(TextureRegion[] frames, float delay) {
		this.frames = frames;
		this.delay = delay;
		time = 0;
		currentFrame = 0;
		timesPlayed = 0;
	}
	
	public void update(float dt) {
            if (delay <= 0) return;
            time += dt;
            while (time >= delay) {
                step();
            }
	}

	private void step() {
		time -= delay;
		currentFrame += stepSpeed;
		if(currentFrame == frames.length) {
            if(loopEnable)
			    currentFrame = 0;
            else
                currentFrame = frames.length-1;
			timesPlayed++;
		}

	}

	public TextureRegion getFrame() {
		return frames[currentFrame];
    }

	public int getTimesPlayed() { return timesPlayed; }

    public boolean isLoopEnable() {
        return loopEnable;
    }

    public void setLoopEnable(boolean loopEnable) {
        this.loopEnable = loopEnable;
    }

    public int getCurrentFrame() {
        return currentFrame;
    }

    public void setCurrentFrame(int currentFrame) {
        this.currentFrame = currentFrame;
    }

	public int getStepSpeed() {
		return stepSpeed;
	}

	public void setStepSpeed(int stepSpeed) {
		this.stepSpeed = stepSpeed;
	}
	
}
