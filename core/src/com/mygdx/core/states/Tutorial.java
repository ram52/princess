package com.mygdx.core.states;

import com.badlogic.gdx.Gdx;
import com.mygdx.core.handlers.GameStateManager;
import com.mygdx.core.handlers.Save;

public class Tutorial extends GameState {
    private static String LOG_TAG = Tutorial.class.getSimpleName();
    private Play play;

    public Tutorial(final GameStateManager gsm) {
        super(gsm);
        play = new Play(gsm, true);
        game.actionResolver.hideBannerAd();
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
        if (!Save.gd.getAdsRemoverPurchased()) {
            String network = game.actionResolver.getNetworkClass();
            if(network == null) network = "ABSENT";
            Gdx.app.debug(LOG_TAG,"NETWORK: "+network);
            if(network.equals("4G")|network.equals("3G")|network.equals("WIFI"))
                game.actionResolver.showOrLoadBanner();
        }
    }

}


