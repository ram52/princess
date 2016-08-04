package com.mygdx.core.states;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.core.MyGdxGame;
import com.mygdx.core.handlers.BoundedCamera;
import com.mygdx.core.handlers.GameStateManager;

public abstract class GameState {

	protected GameStateManager gsm;
	protected MyGdxGame game;
	protected SpriteBatch sb;
	protected ShapeRenderer shapeRenderer;
	protected BoundedCamera cam;
	protected OrthographicCamera hudCam;

	protected GameState(GameStateManager gsm) {
		this.gsm = gsm;
		game = gsm.game();
		sb = game.getSpriteBatch();
		shapeRenderer = game.getShapeRenderer();
		cam = game.getCamera();
		hudCam = game.getHUDCamera();
	}

	public abstract void handleInput();

	public abstract void update(float dt);

	public abstract void render();

	public abstract void dispose();

}
