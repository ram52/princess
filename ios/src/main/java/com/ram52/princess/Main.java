package com.ram52.princess;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.backends.iosmoe.IOSApplication;
import com.badlogic.gdx.backends.iosmoe.IOSApplicationConfiguration;
import com.badlogic.gdx.backends.iosmoe.IOSGraphics;
import com.moe.bindings.googlemobileads.GADAdReward;
import com.moe.bindings.googlemobileads.GADBannerView;
import com.moe.bindings.googlemobileads.GADMobileAds;
import com.moe.bindings.googlemobileads.GADRequest;
import com.moe.bindings.googlemobileads.GADRewardBasedVideoAd;
import com.moe.bindings.googlemobileads.c.GoogleMobileAds;
import com.moe.bindings.googlemobileads.protocol.GADRewardBasedVideoAdDelegate;
import com.mygdx.core.MyGdxGame;
import com.mygdx.core.entities.ActionResolver;
import com.mygdx.core.handlers.Save;

import apple.NSObject;
import apple.coregraphics.struct.CGAffineTransform;
import apple.coregraphics.struct.CGPoint;
import apple.coregraphics.struct.CGRect;
import apple.foundation.NSArray;
import apple.foundation.NSData;
import apple.foundation.NSDictionary;
import apple.foundation.NSError;
import apple.foundation.NSFileManager;
import apple.foundation.NSURL;
import apple.foundation.enums.NSSearchPathDirectory;
import apple.foundation.enums.NSSearchPathDomainMask;
import apple.gamekit.GKGameCenterViewController;
import apple.gamekit.GKLeaderboard;
import apple.gamekit.GKLocalPlayer;
import apple.gamekit.GKScore;
import apple.gamekit.enums.GKGameCenterViewControllerState;
import apple.gamekit.enums.GKLeaderboardPlayerScope;
import apple.gamekit.enums.GKLeaderboardTimeScope;
import apple.gamekit.protocol.GKGameCenterControllerDelegate;
import apple.uikit.UIActivityIndicatorView;
import apple.uikit.UIAlertView;
import apple.uikit.UIApplication;
import apple.uikit.UIScreen;
import apple.uikit.UIViewController;
import apple.uikit.UIWindow;
import apple.uikit.c.UIKit;
import apple.uikit.protocol.UIAlertViewDelegate;
import dalvik.system.VMDebug;

import org.moe.inapppurchase.common.ProductDetails;
import org.moe.inapppurchase.common.ProductsStore;
import org.moe.natj.general.Pointer;
import org.moe.natj.general.ann.NInt;
import org.moe.natj.general.ann.RegisterOnStartup;
import org.moe.natj.objc.ObjCRuntime;
import org.moe.natj.objc.ann.ObjCClassName;
import org.moe.natj.objc.ann.Selector;

import java.io.IOException;
import java.util.List;

import static apple.coregraphics.c.CoreGraphics.CGAffineTransformMakeScale;

@org.moe.natj.general.ann.Runtime(ObjCRuntime.class)
@ObjCClassName("Main")
@RegisterOnStartup
public class Main extends IOSApplication.Delegate implements ActionResolver {

    private static String LOG_TAG = Main.class.getSimpleName();

    private boolean loading = false;
    private IOSApplication iosApplication;
    private ProductsStore productsStore = null;
    private GKLeaderboard leaderboard = null;
    private UIViewController viewController;

    public boolean displayedRewardedVideo = false;
    private boolean rewardCompleted = false;
    private GADRewardBasedVideoAd gadRewardBasedVideoAd;
    private boolean silentCache = true;
    private UIActivityIndicatorView uiProgressView;
    //private boolean isSignInGC = false;
    private GADBannerView adView;
    private GKLocalPlayer localPlayer;

    private Net.HttpRequest httpRequest = new Net.HttpRequest("https://www.google.com");
    private Net.HttpResponseListener httpResponseListener = new Net.HttpResponseListener() {
        @Override
        public void handleHttpResponse(Net.HttpResponse httpResponse) {
            displayError(true);
            iosApplication.log(LOG_TAG,"internet is available");
        }

        @Override
        public void failed(Throwable t) {
            iosApplication.log(LOG_TAG,"error while httpRequest",t);
            displayError(false);
        }

        @Override
        public void cancelled() {
            displayError(true);
        }
    };

    private class CompletionHandler implements GKLeaderboard.Block_loadLeaderboardsWithCompletionHandler {
        private boolean silent = false;
        private int score = 0;

        public CompletionHandler(boolean silent, int score) {
            this.silent = silent;
            this.score = score;
        }

        @Override
        public void call_loadLeaderboardsWithCompletionHandler(NSArray<? extends GKLeaderboard> leaderboards, NSError nsError) {
            //loading = true;

            iosApplication.log(LOG_TAG,"call_loadLeaderboardsWithCompletionHandler...");
            if (nsError != null) {
                iosApplication.log("Error on loading leaderboards: %s", nsError.toString());
                MyGdxGame.clickedOnLeaderboard = false;
                uiProgressView.stopAnimating();
            }
            if (leaderboards != null && leaderboards.size() > 0) {
                MyGdxGame.clickedOnLeaderboard = false;
                leaderboards.get(0).setPlayerScope(GKLeaderboardPlayerScope.Global);
                leaderboards.get(0).setTimeScope(GKLeaderboardTimeScope.AllTime);
                leaderboards.get(0).loadScoresWithCompletionHandler(new GKLeaderboard.Block_loadScoresWithCompletionHandler() {
                    @Override
                    public void call_loadScoresWithCompletionHandler(NSArray<? extends GKScore> scores, NSError nsError) {

                        uiProgressView.stopAnimating();
                        if(nsError != null){
                            iosApplication.log("call_loadScoresWithCompletionHandler", nsError.toString());
                        }

                        if(scores != null){
                            iosApplication.log("scores", scores.toString());
                            iosApplication.log("scores", String.valueOf(scores.count()));
                        }

                        System.out.println("Got leaderboard scores");
                        leaderboard = leaderboards.get(0);
                        submitScoreGPGS(score);

                        GKGameCenterViewController gameCenterViewController = GKGameCenterViewController.alloc();
                        gameCenterViewController.init();
                        gameCenterViewController.setViewState(GKGameCenterViewControllerState.Leaderboards);
                        gameCenterViewController.setLeaderboardIdentifier(leaderboards.get(0).identifier());

                        gameCenterViewController.setGameCenterDelegate(new GKGameCenterControllerDelegate() {
                            @Override
                            public void gameCenterViewControllerDidFinish(GKGameCenterViewController gameCenterViewController) {
                                System.out.println("leaderboard finished loading...");
                                gameCenterViewController.dismissViewControllerAnimatedCompletion(true, null);
                            }
                        });

                        //window().rootViewController().presentViewControllerAnimatedCompletion(gameCenterViewController, true, null);

                        if(!silent){
                            System.out.println("leaderboard ! silent");
                            iosApplication.getUIWindow()
                                    .rootViewController()
                                    .presentViewControllerAnimatedCompletion(gameCenterViewController, true, null);

                        }else {
                            System.out.println("leaderboard silent");
                        }
                        //submitScoreGPGS(0);
                    }
                });
            }
        }
    }


    public static void main(String[] args) {
        UIKit.UIApplicationMain(0, null, null, Main.class.getSimpleName());
    }

    @Override
    protected IOSApplication createApplication() {
        IOSApplicationConfiguration config = new IOSApplicationConfiguration();
        iosApplication = new IOSApplication(new MyGdxGame(this), config);
        return iosApplication;
    }

    @Override
    public void applicationDidReceiveMemoryWarning(UIApplication application) {
        iosApplication.log(LOG_TAG,"applicationDidReceiveMemoryWarning");
        Runtime.getRuntime().gc();
        List<NSURL> l = (List<NSURL>) NSFileManager.defaultManager().
                URLsForDirectoryInDomains(
                        NSSearchPathDirectory.DocumentDirectory,
                        NSSearchPathDomainMask.UserDomainMask);
        NSURL docDirURL = l.get(0); // Error handling is for cowards :)
        String fsPath = docDirURL.fileSystemRepresentation();
        try {
            VMDebug.dumpHprofData(fsPath + "/MOE-Dump.hprof");
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        iosApplication.log(LOG_TAG,"applicationWillResignActive");
        ((IOSGraphics)iosApplication.getGraphics()).pause();
    }

    @Override
    public void applicationWillTerminate (UIApplication application) {
        iosApplication.log(LOG_TAG,"applicationWillTerminate");
    }

    private void initProgressView(){
        uiProgressView = UIActivityIndicatorView.alloc().init();
        uiProgressView.setCenter(new CGPoint(UIScreen.mainScreen().bounds().size().width() * 0.5f, UIScreen.mainScreen().bounds().size().height() * 0.5f));
        CGAffineTransform transform = CGAffineTransformMakeScale(2f, 2f);
        uiProgressView.setTransform(transform);
        uiProgressView.setHidesWhenStopped(true);
        uiProgressView.setHidden(true);
        iosApplication.getUIWindow().addSubview(uiProgressView);
    }

    public void displayError(boolean internetAvailable){
        UIAlertView alert = UIAlertView.alloc().init();
        alert.setTitle("Sorry :(");
        if(!internetAvailable){
            alert.setMessage("Internet connection is off. Your score will be submitted the next time you play with internet available");
        }else {
            alert.setMessage("An error occurred.");
        }
        alert.addButtonWithTitle("Ok");
        alert.show();
    }

    public void isInternetAvailable(){
        Gdx.net.sendHttpRequest(httpRequest, httpResponseListener);
    }

    private void initAdMobReward(){
        System.out.println("initAdMobReward...");
        gadRewardBasedVideoAd = GADRewardBasedVideoAd.alloc().init();
        gadRewardBasedVideoAd.setDelegate(new GADRewardBasedVideoAdDelegate(){

            @Override
            public void rewardBasedVideoAdDidFailToLoadWithError(GADRewardBasedVideoAd rewardBasedVideoAd, NSError error) {
                System.out.println("rewardBasedVideoAdDidFailToLoadWithError");
                Gdx.app.debug(LOG_TAG,"rewardBasedVideoAdDidFailToLoadWithError");
                iosApplication.log(LOG_TAG,"rewardBasedVideoAdDidFailToLoadWithError");
                displayedRewardedVideo = true;
                // Called after a rewarded video has attempted to load from the Chartboost API
                // servers but failed.
                iosApplication.log("CHARTBOOST", error + "***");
                //if(!error.toString().contains("IMPRESSION_ALREADY_VISIBLE"))
                //{
                uiProgressView.stopAnimating();

                if(!silentCache){
                    UIAlertView alert = UIAlertView.alloc().init();
                    alert.setTitle("Sorry :-(");
                    alert.setMessage("Video is not available. Please try again later.");
                    alert.addButtonWithTitle("Ok");
                    alert.show();
                }

            }

            @Override
            public void rewardBasedVideoAdDidRewardUserWithReward(GADRewardBasedVideoAd rewardBasedVideoAd, GADAdReward reward) {
                System.out.println("rewardBasedVideoAdDidRewardUserWithReward");
                Gdx.app.debug(LOG_TAG,"rewardBasedVideoAdDidRewardUserWithReward");
                iosApplication.log(LOG_TAG,"rewardBasedVideoAdDidRewardUserWithReward");
                rewardCompleted = true;
                displayedRewardedVideo = true;
                uiProgressView.stopAnimating();
            }

            @Override
            public void rewardBasedVideoAdDidClose(GADRewardBasedVideoAd rewardBasedVideoAd) {
                System.out.println("rewardBasedVideoAdDidClose");
                Gdx.app.debug(LOG_TAG,"rewardBasedVideoAdDidClose");
                iosApplication.log(LOG_TAG,"rewardBasedVideoAdDidClose");
                if(rewardCompleted) {
                    MyGdxGame.setPause(false);
                    //MyGdxGame.playContinueSound();
                    MyGdxGame.setContinue(true);
                    rewardCompleted = false;
                }
                displayedRewardedVideo = true;
                uiProgressView.stopAnimating();
                silentCache = false;
                gadRewardBasedVideoAd.loadRequestWithAdUnitID(GADRequest.request(),"ca-app-pub-8573807632898403/9496833224");
            }

            @Override
            public void rewardBasedVideoAdDidOpen(GADRewardBasedVideoAd rewardBasedVideoAd) {
                System.out.println("rewardBasedVideoAdDidOpen");
                Gdx.app.debug(LOG_TAG,"rewardBasedVideoAdDidOpen");
                iosApplication.log(LOG_TAG,"rewardBasedVideoAdDidOpen");
            }

            @Override
            public void rewardBasedVideoAdDidReceiveAd(GADRewardBasedVideoAd rewardBasedVideoAd) {
                System.out.println("rewardBasedVideoAdDidReceiveAd");
                Gdx.app.debug(LOG_TAG,"rewardBasedVideoAdDidReceiveAd");
                iosApplication.log(LOG_TAG,"rewardBasedVideoAdDidReceiveAd");
                if(!displayedRewardedVideo && !silentCache){
                    uiProgressView.stopAnimating();
                    displayedRewardedVideo = true;
                    //gadRewardBasedVideoAd.loadRequestWithAdUnitID(GADRequest.request(),"ca-app-pub-8573807632898403/7967956571");
                    showRewardedVideoChartBoost();
                }
            }

            @Override
            public void rewardBasedVideoAdDidStartPlaying(GADRewardBasedVideoAd rewardBasedVideoAd) {
                System.out.println("rewardBasedVideoAdDidStartPlaying");
                Gdx.app.debug(LOG_TAG,"rewardBasedVideoAdDidStartPlaying");
                iosApplication.log(LOG_TAG,"rewardBasedVideoAdDidStartPlaying");
                uiProgressView.stopAnimating();
            }

            @Override
            public void rewardBasedVideoAdWillLeaveApplication(GADRewardBasedVideoAd rewardBasedVideoAd) {
                System.out.println("rewardBasedVideoAdWillLeaveApplication");
                Gdx.app.debug(LOG_TAG,"rewardBasedVideoAdWillLeaveApplication");
                iosApplication.log(LOG_TAG,"rewardBasedVideoAdWillLeaveApplication");
            }
        });
        System.out.println("loadRequestWithAdUnitID");
        gadRewardBasedVideoAd.loadRequestWithAdUnitID(GADRequest.request(),"ca-app-pub-8573807632898403/9496833224");
        //showOrLoadRewardedVideoChartboost();
        silentCache = true;
        System.out.println("silentCache="+silentCache);
    }

    @Override
    public void showRewardedVideoChartBoost() {
        if(gadRewardBasedVideoAd.isReady()){
            gadRewardBasedVideoAd.presentFromRootViewController(iosApplication.getUIViewController());
        }
    }

    @Override
    public void showOrLoadRewardedVideoChartboost() {
        UIAlertView alert = UIAlertView.alloc().init();
        alert.setTitle("FREE COINS");
        alert.setMessage("Watch a video to earn 40 coins.");
        alert.addButtonWithTitle("Cancel");
        alert.addButtonWithTitle("Ok");
        alert.show();
        alert.setDelegate(new UIAlertViewDelegate(){
                              @Override
                              public void alertViewClickedButtonAtIndex(UIAlertView alertView, @NInt long buttonIndex) {
                                  if (buttonIndex == 0) {
                                      // user clicked "Cancel" button
                                      iosApplication.log(LOG_TAG,"user clicked \"Cancel\" button");
                                  } else if (buttonIndex == 1) {
                                      // user clicked "Ok" button
                                      iosApplication.log(LOG_TAG,"user clicked \"Ok\" button");
                                      displayedRewardedVideo = false;
                                      uiProgressView.startAnimating();
                                      iosApplication.log(LOG_TAG,"showOrLoadRewardedVideoChartboost");
                                      if(gadRewardBasedVideoAd.isReady()){
                                          gadRewardBasedVideoAd.presentFromRootViewController(iosApplication.getUIViewController());
                                      }else{
                                          silentCache = false;
                                          gadRewardBasedVideoAd.loadRequestWithAdUnitID(GADRequest.request(),"ca-app-pub-8573807632898403/9496833224");
                                      }
                                  }
                              }
                              @Override
                              public void alertViewCancel(UIAlertView alertView) {
                              }
                          }
        ) ;
    }

    @Override
    public void applicationDidBecomeActive(UIApplication application) {
        leaderboard = GKLeaderboard.alloc();
        initProgressView();
        initAdMobReward();

        ((IOSGraphics)iosApplication.getGraphics()).resume();

        initBannerAd();
        iosApplication.log(LOG_TAG,"applicationDidBecomeActive");

        productsStore = new ProductsStore("IAPHelperProductPurchasedNotification");

        productsStore.requestProductsWithCompletionHandler(new ProductsStore.RequestProductsCompletionHandler() {
            @Override
            public void callback(boolean status) {
                //iosApplication.log(LOG_TAG,"status");
                if (status) {
                    iosApplication.log(LOG_TAG,"="+status);
                }
            }
        });
    }

    private void initBannerAd(){
        ((IOSGraphics)iosApplication.getGraphics()).resume();
        GADMobileAds.configureWithApplicationID("1091447865811");

        Save.load();
        if(adView == null && !Save.gd.getAdsRemoverPurchased()){
            adView = GADBannerView.alloc().initWithAdSize(GoogleMobileAds.kGADAdSizeBanner());
            adView.setAdUnitID("ca-app-pub-8573807632898403/6307563109");

            CGRect cgRect = new CGRect();
            cgRect.setSize(GoogleMobileAds.kGADAdSizeBanner().size());

            adView.setFrame(cgRect);
            adView.setRootViewController(iosApplication.getUIViewController());

            GADRequest request = GADRequest.request();

            request.setTestDevices(NSArray.arrayWithObject(GoogleMobileAds.kGADSimulatorID()));
            request.setTestDevices(NSArray.arrayWithObject("7de108e015b09b642915e7fb34456ad5"));

            adView.loadRequest(request);

            adView.setCenter( new CGPoint(UIScreen.mainScreen().bounds().size().width() * 0.5f, 20));

            iosApplication.getUIWindow().addSubview(adView);
            hideBannerAd();
        }
    }

    @Override
    public boolean getSignedInGPGS() {
        iosApplication.log(LOG_TAG,"getSignedInGPGS...");
        return GKLocalPlayer.localPlayer().isAuthenticated();
    }

    @Override
    public void loginGPGS(boolean silent) {
        if(!loading){
            loading = true;
            iosApplication.log(LOG_TAG,"loginGPGS...");

            if(!silent){
                iosApplication.log(LOG_TAG,"uiProgressView.startAnimating");
                uiProgressView.startAnimating();
                uiProgressView.setHidden(false);
            }

            if(GKLocalPlayer.localPlayer().authenticateHandler() == null){
                iosApplication.log(LOG_TAG,"GKLocalPlayer.localPlayer().authenticateHandler()==null");
                localPlayer = GKLocalPlayer.localPlayer();
                localPlayer.setAuthenticateHandler(new GKLocalPlayer.Block_setAuthenticateHandler() {
                    @Override
                    public void call_setAuthenticateHandler(UIViewController viewController, NSError nsError) {

                        iosApplication.log(LOG_TAG,"nsError=");
                        if (viewController != null) {
                            Main.this.viewController = viewController;
                            uiProgressView.stopAnimating();
                            iosApplication.log(LOG_TAG,"viewController != null");
                            iosApplication.getUIWindow().rootViewController().showDetailViewControllerSender(viewController, null);
                        } else if (localPlayer.isAuthenticated()) {
                            iosApplication.log(LOG_TAG,"log"+localPlayer.isAuthenticated());
                            localPlayer.generateIdentityVerificationSignatureWithCompletionHandler(
                                    new GKLocalPlayer.Block_generateIdentityVerificationSignatureWithCompletionHandler() {
                                        @Override
                                        public void call_generateIdentityVerificationSignatureWithCompletionHandler(
                                                NSURL publicKeyUrl, NSData signature, NSData salt, long timestamp, NSError error) {
                                            iosApplication.log(LOG_TAG,"successfully logged into gamecenter");
                                            loading = false;
                                            iosApplication.log(LOG_TAG,"clicked_leaderboard="+MyGdxGame.clickedOnLeaderboard);
                                            if(MyGdxGame.clickedOnLeaderboard){
                                                getLeaderboardGPGS(false,0);
                                                if(!silent){
                                                    iosApplication.log(LOG_TAG,"uiProgressView.startAnimating");
                                                    uiProgressView.startAnimating();
                                                }

                                            }else{
                                                uiProgressView.stopAnimating();
                                                getLeaderboardGPGS(true,0);
                                            }

                                        }
                                    });
                        } else {
                            iosApplication.log(LOG_TAG,"failed to log into gamecenter");
                            //canceled by the user or GameCenter is disabled
                            //display alert view
                            loading = false;
                            uiProgressView.stopAnimating();
                            UIAlertView alert = UIAlertView.alloc().init();
                            alert.setTitle("");
                            alert.setMessage(
                                    "Please login to GameCenter " +
                                            "\n1)Go to Settings ⚙       " +
                                            "\n2)Select GameCenter      " +
                                            "\n3)Sign In                ");
                            alert.addButtonWithTitle("Ok");
                            alert.show();
                        }
                    }
                });
            }else{
                UIApplication.sharedApplication().openURL(NSURL.URLWithString("gamecenter:"));
                loading = false;
                uiProgressView.stopAnimating();
                iosApplication.log(LOG_TAG,"already init!");
                //loginGPGS();
//                if (viewController != null) {
//                    iosApplication.log(LOG_TAG,"already init! "+viewController.isBeingPresented());
//                    if(!viewController.isBeingPresented()){
//                        iosApplication.getUIWindow().rootViewController().showDetailViewControllerSender(viewController, null);
//                    }
//                }
            }
        }
    }

    void purchaseProduct(int id) {
        iosApplication.log(LOG_TAG,"purchaseProduct="+id);
        ProductDetails product = productsStore.getProductAt(id);
        if(product != null){
            if (productsStore != null) {
                iosApplication.log(LOG_TAG,"product="+product.toString());
                productsStore.purchaseProduct(product, new ProductsStore.RequestPurchaseProductsHandler() {
                    @Override
                    public void callback(boolean status) {
                        iosApplication.log(LOG_TAG,"status="+status);
                        if(status){
                            applyPurchase(product.getProductID());
                        }
                    }
                });
            }
        }else{
            iosApplication.log(LOG_TAG,"product=null");
        }

    }

    public int findProductId(String id){
        int index = 0;
        for (int i = 0;i<productsStore.getNumberOfProducts();i++){
            if(id.equals(productsStore.getProductAt(i).getProductID())){
                index = i;
                break;
            }
        }
        iosApplication.log(LOG_TAG,"findProductId id="+id+" index="+index);
        return index;
    }

    public void displayBuy(String id){
        iosApplication.log(LOG_TAG,"displayBuy id="+id);
        ProductDetails product = null;
        if(productsStore != null){
            product = productsStore.getProductAt(findProductId(id));
            iosApplication.log(LOG_TAG,"getNumberOfProducts="+productsStore.getNumberOfProducts());
        }else {
            iosApplication.log(LOG_TAG,"productsStore == null");
        }

        if(product == null){
            //displayError();
            isInternetAvailable();
            iosApplication.log(LOG_TAG,"product == null");
        }else{

            UIAlertView alert = UIAlertView.alloc().init();
            alert.setTitle(product.getTitle());
            alert.setMessage(product.getDescription()+"\n"+product.getPrice());
            alert.addButtonWithTitle("Cancel");
            alert.addButtonWithTitle("Ok");
            alert.show();
            alert.setDelegate(new UIAlertViewDelegate(){
                                  @Override
                                  public void alertViewClickedButtonAtIndex(UIAlertView alertView, @NInt long buttonIndex) {
                                      if (buttonIndex == 0) {
                                          // user clicked "Cancel" button
                                          iosApplication.log(LOG_TAG,"user clicked \"Cancel\" button");
                                      } else if (buttonIndex == 1) {
                                          // user clicked "Ok" button
                                          iosApplication.log(LOG_TAG,"user clicked \"Ok\" button");
                                          purchaseProduct(findProductId(id));
                                      }
                                  }
                                  @Override
                                  public void alertViewCancel(UIAlertView alertView) {
                                  }
                              }
            ) ;
        }

    }

    private void applyPurchase(String productId){

        String purchased = "";

        System.out.println("applyPurchase for productId="+productId);
        if(productId.equals(productsStore.item2_100Coins)){
            Save.load();
            Save.gd.setMoney(Save.gd.getMoney() + 100);
            Save.save();
            if (MyGdxGame.isSoundEnable() == 1) {
                MyGdxGame.res.getSound("point").play();
            }
            purchased = purchased+"100 Coins ";
        }
        if(productId.equals(productsStore.item3_1000Coins)){
            Save.load();
            Save.gd.setMoney(Save.gd.getMoney() + 1000);
            Save.save();
            if (MyGdxGame.isSoundEnable() == 1) {
                MyGdxGame.res.getSound("point").play();
            }
            purchased = purchased+"1000 Coins ";
        }
        if(productId.equals(productsStore.item_removeads)){
            Save.gd.setAdsRemoverPurchased(true);
            hideBannerAd();
            Save.save();
            Save.load();
            purchased = purchased+"Ads Remover ";
        }


        UIAlertView alert = UIAlertView.alloc().init();
        alert.setTitle("Thank you!");
        alert.setMessage("Your purchase was successfull.\n"+purchased);
        alert.addButtonWithTitle("Ok");
        alert.show();
    }

    public void displayRestore(){
        iosApplication.log(LOG_TAG,"displayRestore");

        if(productsStore == null){
            //displayError();
            isInternetAvailable();
            iosApplication.log(LOG_TAG,"displayRestore == null");
        }else{
            UIAlertView alert = UIAlertView.alloc().init();
            alert.setTitle("Restore Purchases");
            alert.setMessage("I want to restore my purchases.");
            alert.addButtonWithTitle("Cancel");
            alert.addButtonWithTitle("Ok");
            alert.show();
            alert.setDelegate(new UIAlertViewDelegate(){
                                  @Override
                                  public void alertViewClickedButtonAtIndex(UIAlertView alertView, @NInt long buttonIndex) {
                                      if (buttonIndex == 0) {
                                          // user clicked "Cancel" button
                                          iosApplication.log(LOG_TAG,"user clicked \"Cancel\" button");
                                      } else if (buttonIndex == 1) {
                                          // user clicked "Ok" button
                                          iosApplication.log(LOG_TAG,"user clicked \"Ok\" button");
                                          productsStore.restoreCompletedTransactions(new ProductsStore.RequestRestoreProductsHandler() {
                                              @Override
                                              public void callback(String id) {
                                                  iosApplication.log(LOG_TAG,"restored= "+id);
                                                  applyPurchase(id);
                                              }
                                          });



                                      }
                                  }
                                  @Override
                                  public void alertViewCancel(UIAlertView alertView) {
                                  }
                              }
            ) ;
        }

    }

    @Override
    public void submitScoreGPGS(int score) {
        try {
            Save.load();
            int highScore = (int) Save.gd.getHighScores()[0];
            iosApplication.log(LOG_TAG,"submitScoreGPGS...");
            iosApplication.log(LOG_TAG,"score="+score+" highscore="+highScore);
            if(score >= highScore){
                score = highScore;
                if( localPlayer.isAuthenticated() && leaderboard != null){
                    GKScore gkScore = GKScore.alloc();
                    gkScore = gkScore.initWithLeaderboardIdentifier(leaderboard.identifier());
                    gkScore.setValue(score);
                    NSArray<GKScore> nsArray = (NSArray<GKScore>) NSArray.arrayWithObject(gkScore);
                    GKScore.reportScoresWithCompletionHandler(nsArray, new GKScore.Block_reportScoresWithCompletionHandler() {
                        @Override
                        public void call_reportScoresWithCompletionHandler(NSError nsError) {
                            if (nsError != null) {
                                Gdx.app.debug(LOG_TAG,"submitted score not successfully");
                                //displayError();
                                isInternetAvailable();
                            } else {
                                Gdx.app.debug(LOG_TAG,"submitted score successfully");
                            }
                        }
                    });
                }else{
                    iosApplication.log(LOG_TAG,"leaderbord null");
                    //displayError();
                    getLeaderboardGPGS(true,score);
                }
            }else{
                iosApplication.log(LOG_TAG,"no need to submit score="+score);
            }
        }catch (ClassCastException | ArrayIndexOutOfBoundsException e){
            iosApplication.log(LOG_TAG,"could not cast score",e);
            //score = Integer.MAX_VALUE;
        }
    }

    @Override
    public void unlockAchievementGPGS(String achievementId) {

    }

    @Override
    public void getLeaderboardGPGS(boolean silent, int score) {
        iosApplication.log(LOG_TAG,"getLeaderboardGPGS...");
        iosApplication.log(LOG_TAG,"uiProgressView.startAnimating");
        if(!silent){
            uiProgressView.startAnimating();
        }

        leaderboard.loadLeaderboardsWithCompletionHandler(new CompletionHandler(silent, score));
    }


    @Override
    public void getAchievementsGPGS() {
        displayRestore();
    }

    @Override
    public void purchaseFullBar() {

    }

    @Override
    public void purchaseExtraCoins() {

    }

    @Override
    public void purchaseAdsRemover() {
        displayBuy("com.ram52.princess.inapppurchase.removeads");
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
        displayBuy("com.ram52.princess.inapppurchase.100coins");

    }

    @Override
    public void purchaseThousandCoins() {
        displayBuy("com.ram52.princess.inapppurchase.1000coins");
    }

    @Override
    public void showOrLoadInterstitalVideo() {

    }

    @Override
    public void showOrLoadBanner() {
        showBannerAd();
    }

    @Override
    public String getNetworkClass() {
        return "WIFI";
    }

    @Override
    public void showOrLoadInterstital() {

    }

    @Override
    public void shareOnFacebook() {

    }

    @Override
    public void showBannerAd() {
        if(adView != null){
            adView.setHidden(false);
        }
    }

    @Override
    public void hideBannerAd() {
        if(adView != null){
            adView.setHidden(true);
        }
    }


    @Override
    public void confirm(MyGdxGame.ConfirmInterface confirmInterface) {

    }
}
