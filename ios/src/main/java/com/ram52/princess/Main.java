package com.ram52.princess;

import com.badlogic.gdx.backends.iosmoe.IOSApplication;
import com.badlogic.gdx.backends.iosmoe.IOSApplicationConfiguration;
import com.badlogic.gdx.backends.iosmoe.IOSGraphics;
import com.mygdx.core.MyGdxGame;
import com.mygdx.core.entities.ActionResolver;

import apple.NSObject;
import apple.foundation.NSDictionary;
import apple.uikit.UIApplication;
import apple.uikit.UIWindow;
import apple.uikit.c.UIKit;

import org.moe.inapppurchase.common.ProductsStore;
import org.moe.natj.general.Pointer;
import org.moe.natj.general.ann.RegisterOnStartup;
import org.moe.natj.objc.ObjCRuntime;
import org.moe.natj.objc.ann.ObjCClassName;
import org.moe.natj.objc.ann.Selector;

@org.moe.natj.general.ann.Runtime(ObjCRuntime.class)
@ObjCClassName("Main")
@RegisterOnStartup
public class Main extends IOSApplication.Delegate implements ActionResolver {

    private static String LOG_TAG = Main.class.getSimpleName();

    private IOSApplication iosApplication;
    ProductsStore productsStore = null;
    MyGdxGame game;

    public static void main(String[] args) {
        UIKit.UIApplicationMain(0, null, null, Main.class.getSimpleName());
    }

    @Override
    protected IOSApplication createApplication() {
        IOSApplicationConfiguration config = new IOSApplicationConfiguration();
        game = new MyGdxGame(this);
        iosApplication = new IOSApplication(game, config);
        return iosApplication;
    }

    @Selector("alloc")
    public static native Main alloc();

    protected Main(Pointer peer) {
        super(peer);
    }


    @Override
    public void applicationWillEnterForeground (UIApplication application) {

    }

    @Override
    public void applicationWillResignActive (UIApplication application) {
        ((IOSGraphics)iosApplication.getGraphics()).pause();
    }

    @Override
    public void applicationWillTerminate (UIApplication application) {

    }

    @Override
    public void applicationDidBecomeActive(UIApplication application) {
        iosApplication.log(LOG_TAG,"applicationDidBecomeActive");
        productsStore = new ProductsStore("IAPHelperProductPurchasedNotification");
        productsStore.requestProductsWithCompletionHandler(new ProductsStore.RequestProductsCompletionHandler() {
            @Override
            public void callback(boolean status) {
                iosApplication.log(LOG_TAG,"status");
                if (status) {
                }
            }
        });
    }

    @Override
    public boolean getSignedInGPGS() {
        return false;
    }

    @Override
    public void loginGPGS() {

    }

    @Override
    public void submitScoreGPGS(int score) {

    }

    @Override
    public void unlockAchievementGPGS(String achievementId) {

    }

    @Override
    public void getLeaderboardGPGS() {

    }

    @Override
    public void getAchievementsGPGS() {

    }

    @Override
    public void purchaseFullBar() {

    }

    @Override
    public void purchaseExtraCoins() {

    }

    @Override
    public void purchaseAdsRemover() {

    }

    @Override
    public void purchaseExcalibur() {

    }

    @Override
    public void purchaseKamebeam() {

    }

    @Override
    public void purchaseBoots() {

    }

    @Override
    public void purchaseHadouBall() {

    }

    @Override
    public void purchaseMegaJump() {

    }

    @Override
    public void purchaseLightning() {

    }

    @Override
    public void purchaseSuperBrick() {

    }

    @Override
    public void purchaseHundredCoins() {

    }

    @Override
    public void purchaseThousandCoins() {

    }

    @Override
    public void showOrLoadInterstitalVideo() {

    }

    @Override
    public void showOrLoadBanner() {

    }

    @Override
    public String getNetworkClass() {
        return null;
    }

    @Override
    public void showOrLoadInterstital() {

    }

    @Override
    public void shareOnFacebook() {

    }

    @Override
    public void showBannerAd() {

    }

    @Override
    public void hideBannerAd() {

    }

    @Override
    public void showRewardedVideoChartBoost() {

    }

    @Override
    public void confirm(MyGdxGame.ConfirmInterface confirmInterface) {

    }
}
