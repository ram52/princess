package com.mygdx.game.android;

import com.google.android.gms.ads.purchase.InAppPurchaseResult;

/**
 * Created by Axel on 24/10/2015.
 */
public interface PlayStorePurchaseListener {
    boolean isValidPurchase(String productId);
    void onInAppPurchaseFinished(InAppPurchaseResult inAppPurchaseResult);
}
