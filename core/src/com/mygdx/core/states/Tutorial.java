package com.mygdx.core.states;

import com.badlogic.gdx.Gdx;
import com.mygdx.core.handlers.GameStateManager;
import com.mygdx.core.handlers.Save;

import static com.mygdx.core.MyGdxGame.actionResolver;
import static com.mygdx.core.MyGdxGame.buttonPlayPlay;
import static com.mygdx.core.MyGdxGame.buttonSoundPlay;

public class Tutorial extends GameState {

    private Play play;

    public Tutorial(final GameStateManager gsm) {
        super(gsm);
        play = new Play(gsm, true);
        actionResolver.hideBannerAd();
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
        //play.dispose();
        if(!Save.gd.getAdsRemoverPurchased())
            actionResolver.showBannerAd();
    }

}


