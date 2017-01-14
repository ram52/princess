package com.mygdx.core.states;

import com.mygdx.core.handlers.GameStateManager;

public class Tutorial extends GameState {

    private Play play;

    public Tutorial(final GameStateManager gsm) {
        super(gsm);
        play = new Play(gsm, true);
    }

    public void handleInput() {

    }

    public void update(float dt) {
        play.update(dt);
    }

    public void render() {
       play.render();
    }


    public void dispose() {
        play.dispose();
    }

}


