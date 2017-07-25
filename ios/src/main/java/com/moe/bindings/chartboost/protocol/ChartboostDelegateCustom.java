package com.moe.bindings.chartboost.protocol;

import com.badlogic.gdx.Gdx;
import com.moe.bindings.chartboost.CBLocation;
import com.moe.bindings.chartboost.Chartboost;
import com.ram52.princess.Main;

/**
 * Created by axelmontout on 12/06/17.
 */

public class ChartboostDelegateCustom implements ChartboostDelegate {

    private static String LOG_TAG = Main.class.getSimpleName();

    @Override
    public void didInitialize(boolean status) {
        System.out.println("didInitialize status="+status);
        Gdx.app.debug(LOG_TAG,"didInitialize"+status);
    }

    @Override
    public void didCacheRewardedVideo(String location) {
        System.out.println("didCacheRewardedVideo");
        Gdx.app.debug(LOG_TAG,"didCacheRewardedVideo");

    }
}
