package com.mygdx.core.handlers;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.mygdx.core.MyGdxGame;
import com.mygdx.core.entities.B2DSprite;
import com.mygdx.core.entities.Enemy;
import com.mygdx.core.states.Play;

import static com.mygdx.core.handlers.B2DVars.PPM;

public class MyContactListener implements ContactListener {

	private int numFootContacts = 0;
	private Boolean touch_enemy = false;
	private Array<Body> coinsToRemove;
    private boolean boosHit = false;
    private boolean touchBorder = false;
	private Array<Enemy> enemies;
	private Play play;

	public Array<Body> getEnemiesToRemove() {
		return enemiesToRemove;
	}

	public void setEnemiesToRemove(Array<Body> enemiesToRemove) {
		this.enemiesToRemove = enemiesToRemove;
	}

	private Array<Body> enemiesToRemove;

	public MyContactListener(Array<Enemy> enemies, Play play) {
		super();
		coinsToRemove = new Array<Body>();
		enemiesToRemove = new Array<Body>();
		this.enemies = enemies;
		this.play = play;
	}

	public boolean intersect(B2DSprite entity1, B2DSprite entity2){
		return entity1.getBoundingBox().intersects(entity2.getBoundingBox());
	}

	// called when two fixtures start to collide
	public void beginContact(Contact c) {

		Fixture f = c.getFixtureB();

		if (f == null)
			return;

		if (f.getUserData() != null && f.getUserData().equals("foot")) {
			numFootContacts++;
		}


		if (f.getUserData() != null && f.getUserData().equals("borders")) {
			touchBorder = true;

		}

		if (f.getUserData() != null && f.getUserData().equals("coin")) {
			coinsToRemove.add(f.getBody());
		}

		if (f.getUserData() != null && f.getUserData().equals("enemy")) {
			touch_enemy = true;
			enemiesToRemove.add(f.getBody());
			//System.out.println("TOUCH ENEMY!");

		}

		if (f.getUserData() != null && f.getUserData().equals("princess")) {


		}

		/*if (f.getUserData() != null && f.getUserData().equals("enemy")) {
			System.out.println("TOUCH BOSS HEAD");
            boosHit = true;
		}*/
	}

	// called when two fixtures no longer collide
	public void endContact(Contact c) {

		Fixture fa = c.getFixtureA();
		Fixture fb = c.getFixtureB();

		if (fa == null || fb == null)
			return;

		if (fb.getUserData() != null && fb.getUserData().equals("foot")) {
			numFootContacts--;
		}

        /*if (fb.getUserData() != null && fb.getUserData().equals("enemy")) {
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                 boosHit = false;
                }
            }, 0.35f);
        }*/

        if (fb.getUserData() != null && fb.getUserData().equals("borders")) {
            touchBorder = false;
        }

        if (fb.getUserData() != null && fb.getUserData().equals("enemy")) {
			touch_enemy = false;
			//System.out.println("DO NOT TOUCH ENEMY!");
        }

	}

	public boolean isPlayerOnGround() {
		return numFootContacts > 0;
	}

	public boolean isPlayerTouchingEnemy() {
		return touch_enemy;
	}

	public Array<Body> getCoinsBodies() {
		return coinsToRemove;
	}

	public void preSolve(Contact c, Manifold m) {
	}

	public void postSolve(Contact c, ContactImpulse ci) {
	}

    public boolean isBoosHit() {
        return boosHit;
    }

    public void setBoosHit(boolean boosHit) {
        this.boosHit = boosHit;
    }

	public boolean isTouchBorder() {
		return touchBorder;
	}

	public void setTouchBorder(boolean touchBorder) {
		this.touchBorder = touchBorder;
	}

}
