package com.mygdx.core.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.core.MyGdxGame;

public class Coin  extends B2DSprite{
	
	public Coin(Body body){	
		super(body);
		/*
		Texture tex = MyGdxGame.atlas.findRegion("coin");
		TextureRegion[] sprites = TextureRegion.split(tex, 64, 64)[0];
		setAnimation(sprites,0f);*/
	}

}
