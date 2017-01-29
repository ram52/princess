package com.mygdx.game.android;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.chartboost.sdk.Model.CBError;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.purchase.InAppPurchaseResult;
import com.google.android.gms.ads.purchase.PlayStorePurchaseListener;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesActivityResultCodes;
import com.google.example.games.basegameutils.GameHelper;
import com.google.example.games.basegameutils.GameHelper.GameHelperListener;
import com.mygdx.core.MyGdxGame;
import com.mygdx.core.MyGdxGame.ConfirmInterface;
import com.mygdx.core.MyGdxGame.RequestHandler;
import com.mygdx.core.entities.ActionResolver;
import com.mygdx.core.handlers.Save;
import com.ram52.princess.R;

import io.fabric.sdk.android.Fabric;
import java.util.ArrayList;


import com.chartboost.sdk.Chartboost;
import com.chartboost.sdk.CBLocation;
import com.chartboost.sdk.ChartboostDelegate;

import static com.chartboost.sdk.CBLocation.LOCATION_ITEM_STORE;

public class AndroidLauncher extends AndroidApplication implements
        GameHelperListener, ActionResolver, RequestHandler,PlayStorePurchaseListener, BillingProcessor.IBillingHandler {

    private static String LOG_TAG = AndroidLauncher.class.getSimpleName();
    public static final int BILLING_RESPONSE_RESULT_OK = 0;
    private GameHelper gameHelper;
    public AdView bannerAdView;
    public View gameView;
    private InterstitialAd interstitialAd, interstitialVideoAd;
    private CustomDialogClass dialog;
    public boolean created;
    private BillingProcessor bp;
    private Display display;
    public boolean debug = false; //IN DEBUG DO NOT SUBMIT SCORE && DO NOT SHOW ADS && DO NOT UNLOCK ACHIEVEMENTS
    public ProgressDialog pdialog;

    /**
     * Chartboost Delegates
     */

    public ChartboostDelegate delegate = new ChartboostDelegate() {

        @Override
        public boolean shouldRequestInterstitial(String location) {
            Log.e(LOG_TAG,"Should request interstitial at " + location + "?");
            return true;
        }

        @Override
        public boolean shouldDisplayInterstitial(String location) {
            Log.e(LOG_TAG,"Should display interstitial at " + location + "?");
            return true;
        }

        @Override
        public void didCacheInterstitial(String location) {
            Log.e(LOG_TAG,"Interstitial cached at " + location);
        }

        @Override
        public void didFailToLoadInterstitial(String location, CBError.CBImpressionError error) {
            Log.e(LOG_TAG,"Interstitial failed to load at " + location + " with error: " + error.name());
        }

        @Override
        public void didDismissInterstitial(String location) {
            Log.e(LOG_TAG,"Interstitial dismissed at " + location);
        }

        @Override
        public void didCloseInterstitial(String location) {
            Log.e(LOG_TAG,"Interstitial closed at " + location);
        }

        @Override
        public void didClickInterstitial(String location) {
            Log.e(LOG_TAG,"Interstitial clicked at " + location );
        }

        @Override
        public void didDisplayInterstitial(String location) {
            Log.e(LOG_TAG,"Interstitial displayed at " + location);
        }

        @Override
        public boolean shouldRequestMoreApps(String location) {
            Log.e(LOG_TAG,"Should request More Apps at " + location + "?");
            return true;
        }

        @Override
        public boolean shouldDisplayMoreApps(String location) {
            Log.e(LOG_TAG,"Should display More Apps at " + location + "?");
            return true;
        }

        @Override
        public void didFailToLoadMoreApps(String location, CBError.CBImpressionError error) {
            Log.e(LOG_TAG,"More Apps failed to load at " + location + " with error: " + error.name());
        }

        @Override
        public void didCacheMoreApps(String location) {
            Log.e(LOG_TAG,"More Apps cached at " + location);
        }

        @Override
        public void didDismissMoreApps(String location) {
            Log.e(LOG_TAG,"More Apps dismissed at " + location);
        }

        @Override
        public void didCloseMoreApps(String location) {
            Log.e(LOG_TAG,"More Apps closed at " + location);
        }

        @Override
        public void didClickMoreApps(String location) {
            Log.e(LOG_TAG,"More Apps clicked at " + location);
        }

        @Override
        public void didDisplayMoreApps(String location) {
            Log.e(LOG_TAG,"More Apps displayed at " + location);
        }

        @Override
        public void didFailToRecordClick(String uri, CBError.CBClickError error) {
            Log.e(LOG_TAG,"Failed to record click " + (uri != null ? uri : "null") + ", error: " + error.name());
        }

        @Override
        public boolean shouldDisplayRewardedVideo(String location) {
            Log.e(LOG_TAG,"Should display rewarded video at " + location + "?");
            return true;
        }

        @Override
        public void didCacheRewardedVideo(String location) {
            Log.e(LOG_TAG,"Did cache rewarded video " + location);
            runOnUiThread(new Runnable() {
                public void run() {

                    if(pdialog.isShowing()){
                        Chartboost.showRewardedVideo(LOCATION_ITEM_STORE);
                        pdialog.dismiss();
                    }

                }
            });
        }

        @Override
        public void didFailToLoadRewardedVideo(String location,
                                               CBError.CBImpressionError error) {
            Log.e(LOG_TAG,"Rewarded Video failed to load at " + location + " with error: " + error.name());
            runOnUiThread(new Runnable() {
                public void run() {
                    pdialog.dismiss();
                    new AlertDialog.Builder(AndroidLauncher.this)
                            .setTitle("Sorry :(")
                            .setMessage("No video available. Make sure that your internet connection is working.")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            });
        }

        @Override
        public void didDismissRewardedVideo(String location) {
            Log.e(LOG_TAG,"Rewarded video dismissed at " + location);
            runOnUiThread(new Runnable() {
                public void run() {
                    pdialog.dismiss();
                }
            });
        }

        @Override
        public void didCloseRewardedVideo(String location) {
            Log.e(LOG_TAG,"Rewarded video closed at " + location);
            runOnUiThread(new Runnable() {
                public void run() {
                    pdialog.dismiss();
                }
            });
        }

        @Override
        public void didClickRewardedVideo(String location) {
            Log.e(LOG_TAG,"Rewarded video clicked at " + location);
            runOnUiThread(new Runnable() {
                public void run() {
                    pdialog.dismiss();
                }
            });
        }

        @Override
        public void didCompleteRewardedVideo(String location, int reward) {
            Log.e(LOG_TAG,"Rewarded video completed at " + location + "for reward: " + reward);

            runOnUiThread(new Runnable() {
                public void run() {
                    //TODO Give free coins
                    Save.load();
                    Save.gd.setMoney(Save.gd.getMoney() + MyGdxGame.TWENTY_COINS_STORE_PACK);
                    Save.save();
                }
            });
        }

        @Override
        public void didDisplayRewardedVideo(String location) {
            Log.e(LOG_TAG,"Rewarded video displayed at " + location);
        }

        @Override
        public void willDisplayVideo(String location) {
            Log.e(LOG_TAG,"Will display rewarded video at " + location);
        }

        @Override
        public void didCacheInPlay(String location) {
            Log.e(LOG_TAG,"In Play loaded at " + location);
        }

        @Override
        public void didFailToLoadInPlay(String location, CBError.CBImpressionError error) {
            Log.e(LOG_TAG,"In play failed to load at " + location + ", with error: " + error);
        }

        @Override
        public void didInitialize() {
            Log.e(LOG_TAG,"Chartboost SDK is initialized and ready!");
        }

    };

    public void displayDialogBillingNonAvailable(){
        runOnUiThread(new Runnable() {
            public void run() {
                new AlertDialog.Builder(AndroidLauncher.this)
                        .setTitle("Error")
                        .setMessage("Sorry Billing services is not available for your device.")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
    }

    public void forceCrash() {
        throw new RuntimeException("This is a crash");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        Chartboost.startWithAppId(this, getString(R.string.chartboost_app_id), getString(R.string.chartboost_appSignature));
        Chartboost.setDelegate(delegate);
        Chartboost.onCreate(this);

        display = getWindowManager().getDefaultDisplay();
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getResources().getString(R.string.ad_unit_id_interstitial));
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.numSamples = 0;
        initialize(new MyGdxGame(this, this), config);

        if (gameHelper == null) {
            gameHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);
            gameHelper.enableDebugLog(true);
        }
        gameHelper.setup(this);

        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                //Toast.makeText(getApplicationContext(), "Finished Loading Interstitial", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdClosed() {
                AdRequest interstitialRequest = new AdRequest.Builder()
                        .addTestDevice(getResources().getString(R.string.ad_test_device1))
                        .addTestDevice(getResources().getString(R.string.ad_test_device2))
                        .build();
                interstitialAd.loadAd(interstitialRequest);
                //Toast.makeText(getApplicationContext(), "Loading Interstitial", Toast.LENGTH_SHORT).show();
            }
        });

        RelativeLayout layout = new RelativeLayout(this);
        gameView = initializeForView(new MyGdxGame(this, this));
        layout.addView(gameView);

        //Banner ad
        bannerAdView = new AdView(this);
        RelativeLayout.LayoutParams adParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        adParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        adParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        bannerAdView.setAdUnitId(getResources().getString(R.string.banner_ad_unit_id));
        bannerAdView.setAdSize(AdSize.SMART_BANNER);
        layout.addView(bannerAdView, adParams);

        setContentView(layout);

        dialog = new CustomDialogClass(AndroidLauncher.this);
        dialog.setLauncher(AndroidLauncher.this);
        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        if(BillingProcessor.isIabServiceAvailable(this))
            bp = new BillingProcessor(this,getResources().getString(R.string.billing), this);
        else
            bp = null;

    }

    private void hideVirtualButtons() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    public void showDialog(){
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);
        dialog.show();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideVirtualButtons();
            gameView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);}
    }

    @Override
    public void confirm(final ConfirmInterface confirmInterface) {
        gameView.post(new Runnable() {
            @SuppressLint("NewApi") public void run() {

                if(!dialog.isShowing()) {
                    //dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                    showDialog();
                    MyGdxGame.gsm.game().pause();
                }
            }
        });
    }

    @Override
    public void showOrLoadInterstital() {
        try {
            runOnUiThread(new Runnable() {
                public void run() {
                    if (interstitialAd.isLoaded()) {
                        interstitialAd.show();
                        //Toast.makeText(getApplicationContext(), "Showing Interstitial", Toast.LENGTH_SHORT).show();
                    } else {
                        AdRequest interstitialRequest = new AdRequest.Builder()
                                .addTestDevice(getResources().getString(R.string.ad_test_device1))
                                .addTestDevice(getResources().getString(R.string.ad_test_device2))
                                .build();
                        interstitialAd.loadAd(interstitialRequest);
                        //Toast.makeText(getApplicationContext(), "Loading Interstitial", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception ex) {
            Log.e("EXCEPTION",ex.getMessage());
        }
    }

    @Override
    public void shareOnFacebook() {

    }

    @Override
    public void showBannerAd() {
        bannerAdView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideBannerAd() {
        runOnUiThread(new Runnable() {
            public void run() {
                bannerAdView.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void showRewardedVideoChartBoost() {
        runOnUiThread(new Runnable() {
            public void run() {
                pdialog = new ProgressDialog(AndroidLauncher.this);
                pdialog.setCancelable(false);
                pdialog.setMessage("Loading...");
                pdialog.show();
            }
        });
        if(Chartboost.hasRewardedVideo(LOCATION_ITEM_STORE)){
            Chartboost.showRewardedVideo(LOCATION_ITEM_STORE);
        }else{
            Chartboost.cacheRewardedVideo(CBLocation.LOCATION_ITEM_STORE);
        }

    }



    @Override
    public String getNetworkClass() {
        ConnectivityManager cm = (ConnectivityManager) AndroidLauncher.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if(info==null || !info.isConnected())
            return "-"; //not connected
        if(info.getType() == ConnectivityManager.TYPE_WIFI)
            return "WIFI";
        if(info.getType() == ConnectivityManager.TYPE_MOBILE){
            int networkType = info.getSubtype();
            switch (networkType) {
                case TelephonyManager.NETWORK_TYPE_GPRS:
                case TelephonyManager.NETWORK_TYPE_EDGE:
                case TelephonyManager.NETWORK_TYPE_CDMA:
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
                    return "2G";
                case TelephonyManager.NETWORK_TYPE_UMTS:
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                case TelephonyManager.NETWORK_TYPE_HSPA:
                case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
                case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
                case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
                    return "3G";
                case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
                    return "4G";
                default:
                    return "?";
            }
        }
        return "?";
    }

    @Override
    protected void onPause() {
        super.onPause();
        Chartboost.onPause(this);

        if(MyGdxGame.isSoundEnable() != 0) {
            if(Save.gd != null) {
                Save.gd.setSoundPause(true);
            }
        }
        // when the screen is about to turn off
        if (ScreenReceiver.wasScreenOn) {
            // this is the case when onPause() is called by the system due to a screen state change
            System.out.println("SCREEN TURNED OFF");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Chartboost.onResume(this);


        if(MyGdxGame.isSoundEnable() != 0) {
            if(Save.gd != null) {
                Save.gd.setSoundPause(false);
            }
        }
        // only when screen turns on
        if (!ScreenReceiver.wasScreenOn) {
            // this is when onResume() is called due to a screen state change
            System.out.println("SCREEN TURNED ON");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Chartboost.onStart(this);
        gameHelper.onStart(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        Chartboost.onStop(this);
        gameHelper.onStop();
    }

    @Override
    public void onBackPressed() {
        if(!dialog.isShowing()) {
            showDialog();
            MyGdxGame.gsm.game().pause();
        }
        // If an interstitial is on screen, close it.
        if (Chartboost.onBackPressed())
            return;
        else
            super.onBackPressed();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Chartboost.onDestroy(this);
        if (bp != null)
            bp.release();

        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    public void onActivityResult(int request, int response, Intent data) {
        super.onActivityResult(request, response, data);

        if(bp!=null) if (!bp.handleActivityResult(request, response, data))

            if ( response == GamesActivityResultCodes.RESULT_RECONNECT_REQUIRED && request == 100 ){
                gameHelper.disconnect();
            }
        gameHelper.onActivityResult(request, response, data);

    }

    @Override
    public boolean getSignedInGPGS() {
        return gameHelper.isSignedIn();
    }

    @Override
    public void loginGPGS() {
        try {
            runOnUiThread(new Runnable() {
                public void run() {
                    gameHelper.beginUserInitiatedSignIn();
                }
            });
        } catch (final Exception ex) {
            Log.e("EXCEPTION",ex.getMessage());
        }
    }

    @Override
    public void submitScoreGPGS(int score) {
        try {
            if(!debug) Games.Leaderboards.submitScore(gameHelper.getApiClient(), getResources().getString(R.string.leaderboard_submit), score);
        } catch (final Exception ex) {
            Log.e("EXCEPTION",ex.getMessage());
        }
    }

    @Override
    public void unlockAchievementGPGS(String achievementId) {
        try {
            if(!debug) Games.Achievements.unlock(gameHelper.getApiClient(), achievementId);
        } catch (final Exception ex) {
            Log.e("EXCEPTION",ex.getMessage());
        }
    }

    @Override
    public void getLeaderboardGPGS() {
        try {
            if (gameHelper.isSignedIn()) {
                startActivityForResult(
                        Games.Leaderboards.getLeaderboardIntent(gameHelper.getApiClient(), getResources().getString(R.string.leaderboard_intent)),
                        100);
            } else if (!gameHelper.isConnecting()) {
                loginGPGS();
            }
        } catch (final Exception ex) {
            Log.e("EXCEPTION",ex.getMessage());
        }
    }

    @Override
    public void getAchievementsGPGS() {
        try {
            if (gameHelper.isSignedIn()) {
                startActivityForResult(
                        Games.Achievements.getAchievementsIntent(gameHelper.getApiClient()), 101);
            } else if (!gameHelper.isConnecting()) {
                loginGPGS();
            }
        } catch (final Exception ex) {
            Log.e("EXCEPTION",ex.getMessage());
        }
    }

    @Override
    public void purchaseAdsRemover() {
        try {
            if(bp!=null)
                bp.purchase(this,getResources().getString(R.string.ads_remover));
            else{
                displayDialogBillingNonAvailable();
            }
        } catch (final Exception ex) {
            Log.e("EXCEPTION",ex.getMessage());
        }
    }

    @Override
    public void purchaseExcalibur() {
        try {
            if(bp!=null)
                bp.purchase(this,getResources().getString(R.string.excalibur));
            else{
                displayDialogBillingNonAvailable();
            }
        } catch (final Exception ex) {
            Log.e("EXCEPTION",ex.getMessage());
        }
    }

    @Override
    public void purchaseKamebeam() {
        try {
            if(bp!=null)
                bp.purchase(this,getResources().getString(R.string.kame_beam));
            else{
                displayDialogBillingNonAvailable();
            }
        } catch (final Exception ex) {
            Log.e("EXCEPTION",ex.getMessage());
        }
    }

    @Override
    public void purchaseBoots() {
        try {
            if(bp!=null)
                bp.purchase(this,getResources().getString(R.string.boots));
            else{
                displayDialogBillingNonAvailable();
            }
        } catch (final Exception ex) {
            Log.e("EXCEPTION",ex.getMessage());
        }
    }

    @Override
    public void purchaseHadouBall() {
        try {
            if(bp!=null)
                bp.purchase(this,getResources().getString(R.string.hadou_ball));
            else{
                displayDialogBillingNonAvailable();
            }
        } catch (final Exception ex) {
            Log.e("EXCEPTION",ex.getMessage());
        }
    }

    @Override
    public void purchaseMegaJump() {
        try {
            if(bp!=null)
                bp.purchase(this,getResources().getString(R.string.mega_jump));
            else{
                displayDialogBillingNonAvailable();
            }
        } catch (final Exception ex) {
            Log.e("EXCEPTION",ex.getMessage());
        }
    }

    @Override
    public void purchaseLightning() {
        try {
            if(bp!=null)
                bp.purchase(this,getResources().getString(R.string.lightning_summon));
            else{
                displayDialogBillingNonAvailable();
            }
        } catch (final Exception ex) {
            Log.e("EXCEPTION",ex.getMessage());
        }
    }

    @Override
    public void purchaseSuperBrick() {
        try {
            if(bp!=null)
                bp.purchase(this,getResources().getString(R.string.super_brick));
            else{
                displayDialogBillingNonAvailable();
            }
        } catch (final Exception ex) {
            Log.e("EXCEPTION",ex.getMessage());
        }
    }

    @Override
    public void purchaseHundredCoins() {
        try {
            if(bp!=null)
                bp.purchase(this,getResources().getString(R.string.hundred_coins));
            else{
                displayDialogBillingNonAvailable();
            }
        } catch (final Exception ex) {
            Log.e("EXCEPTION",ex.getMessage());
        }
    }

    @Override
    public void purchaseThousandCoins() {
        try {
            if(bp!=null)
                bp.purchase(this,getResources().getString(R.string.thousand_coins));
            else{
                displayDialogBillingNonAvailable();
            }
        } catch (final Exception ex) {
            Log.e("EXCEPTION",ex.getMessage());
        }
    }



    @Override
    public void showOrLoadInterstitalVideo() {

        try
        {
            String network = getNetworkClass();
            if(network == null) network = "ABSENT";
            System.out.print("NETWORK: " + network);
            if(network.equals("4G")|network.equals("3G")|network.equals("WIFI")) {

                runOnUiThread(new Runnable() {
                    public void run() {
                        pdialog = new ProgressDialog(AndroidLauncher.this);
                        pdialog.setCanceledOnTouchOutside(true);
                        pdialog.setCancelable(true);
                        pdialog.setMessage("Loading...");
                        pdialog.show();
                    }
                });

                interstitialVideoAd = new InterstitialAd(AndroidLauncher.this);
                //interstitialAd.setPlayStorePurchaseParams(AndroidLauncher.this, getResources().getString(R.string.billing));
                interstitialVideoAd.setAdUnitId(getResources().getString(R.string.ad_unit_id_video));
                interstitialVideoAd.setAdListener(new AdListener() {
                    @Override
                    public void onAdLoaded() {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                pdialog.dismiss();
                                interstitialVideoAd.show();

                            }
                        });

                    }

                    @Override
                    public void onAdClosed() {
                    /*MyGdxGame.setPause(false);
                    MyGdxGame.playContinueSound();
                    MyGdxGame.setContinue(true);*/

                        //pdialog.dismiss();
                        // Optional: your custom code here.
                        Log.i("VIDEO AD", "CLOSED");
                    }
                });
                final AdRequest interstitialVideoRequest = new AdRequest.Builder()
                        .addTestDevice(getResources().getString(R.string.ad_test_device1))
                        .addTestDevice(getResources().getString(R.string.ad_test_device2))
                        .build();

                runOnUiThread(new Runnable() {
                    public void run() {
                        interstitialVideoAd.loadAd(interstitialVideoRequest);
                    }
                });
            }

        } catch (final Exception ex) {
            Log.e("EXCEPTION",ex.getMessage());
        }
    }

    @Override
    public void showOrLoadBanner() {
        try{
            runOnUiThread(new Runnable() {
                public void run() {
                    AdRequest adRequest = new AdRequest.Builder()
                            .addTestDevice(getResources().getString(R.string.ad_test_device1))
                            .addTestDevice(getResources().getString(R.string.ad_test_device2))
                            .build();
                    bannerAdView.loadAd(adRequest);
                    bannerAdView.setVisibility(View.VISIBLE);
                }
            });
        } catch (final Exception ex) {
            Log.e("EXCEPTION",ex.getMessage());
        }
    }

    @Override
    public void purchaseFullBar() {
        try{
            if(bp!=null)
                bp.purchase(this,getResources().getString(R.string.full_bar));
            else
                displayDialogBillingNonAvailable();
        } catch (final Exception ex) {
            Log.e("EXCEPTION",ex.getMessage());
        }
    }

    @Override
    public void purchaseExtraCoins() {
        try{
            if(bp!=null)
                bp.purchase(this,getResources().getString(R.string.extra_coins));
            else
                displayDialogBillingNonAvailable();
        } catch (final Exception ex) {
            Log.e("EXCEPTION",ex.getMessage());
        }
    }

    @Override
    public void onSignInFailed() {
    }

    @Override
    public void onSignInSucceeded() {
    }

    @Override
    public void onProductPurchased( String s,  final TransactionDetails transactionDetails) {

        if(bp!=null) {

            Save.load();

            if (bp.getPurchaseTransactionDetails(getResources().getString(R.string.excalibur)) == null) {
                Save.gd.setExcaliburPurchased(false);
                Save.save();
                Save.load();

            } else {

                if (bp.getPurchaseTransactionDetails(getResources().getString(R.string.excalibur)).purchaseInfo.parseResponseData().purchaseState.ordinal() == 0) {
                    Save.gd.setExcaliburPurchased(true);
                    Save.gd.setExcaliburEquiped(true);
                    Save.save();
                    Save.load();

                    try {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(getApplicationContext(), "Excalibur Unlocked!",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (final Exception ex) {
                    }
                }
            }



            if (bp.getPurchaseTransactionDetails(getResources().getString(R.string.kame_beam)) == null) {
                Save.gd.setKamehamehaPurchased(false);
                Save.save();
                Save.load();

            } else {

                if (bp.getPurchaseTransactionDetails(getResources().getString(R.string.kame_beam)).purchaseInfo.parseResponseData().purchaseState.ordinal() == 0) {
                    Save.gd.setKamehamehaPurchased(true);
                    Save.gd.setKamehamehaEquiped(true);
                    Save.gd.setFireBall2Equiped(false);
                    Save.gd.setFireBallEquiped(false);
                    Save.gd.setLightningEquiped(false);
                    Save.save();
                    Save.load();

                    try {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(getApplicationContext(), "Kame beam Unlocked!",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (final Exception ex) {
                    }
                }
            }


            if (bp.getPurchaseTransactionDetails(getResources().getString(R.string.hadou_ball)) == null) {
                Save.gd.setFireBall2Purchased(false);
                Save.save();
                Save.load();

            } else {

                if (bp.getPurchaseTransactionDetails(getResources().getString(R.string.hadou_ball)).purchaseInfo.parseResponseData().purchaseState.ordinal() == 0) {
                    Save.gd.setFireBall2Purchased(true);
                    Save.gd.setKamehamehaEquiped(false);
                    Save.gd.setFireBall2Equiped(true);
                    Save.gd.setFireBallEquiped(false);
                    Save.gd.setLightningEquiped(false);
                    Save.save();
                    Save.load();

                    try {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(getApplicationContext(), "Hadou ball Unlocked!",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (final Exception ex) {
                    }
                }
            }



            if (bp.getPurchaseTransactionDetails(getResources().getString(R.string.boots)) == null) {
                Save.gd.setBootPurchased(false);
                Save.save();
                Save.load();

            } else {

                if (bp.getPurchaseTransactionDetails(getResources().getString(R.string.boots)).purchaseInfo.parseResponseData().purchaseState.ordinal() == 0) {
                    Save.gd.setBootPurchased(true);
                    Save.gd.setBootEquiped(true);
                    Save.save();
                    Save.load();

                    try {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(getApplicationContext(), "Boot Unlocked!",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (final Exception ex) {
                    }
                }
            }



            if (bp.getPurchaseTransactionDetails(getResources().getString(R.string.mega_jump)) == null) {
                Save.gd.setMegaJumpPurchased(false);
                Save.save();
                Save.load();

            } else {

                if (bp.getPurchaseTransactionDetails(getResources().getString(R.string.mega_jump)).purchaseInfo.parseResponseData().purchaseState.ordinal() == 0) {
                    Save.gd.setMegaJumpPurchased(true);
                    Save.gd.setMegaJumpEquiped(true);
                    Save.save();
                    Save.load();

                    try {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(getApplicationContext(), "Mega jump Unlocked!",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (final Exception ex) {
                    }
                }
            }



            if (bp.getPurchaseTransactionDetails(getResources().getString(R.string.lightning_summon)) == null) {
                Save.gd.setLightningPurchased(false);
                Save.save();
                Save.load();

            } else {

                if (bp.getPurchaseTransactionDetails(getResources().getString(R.string.lightning_summon)).purchaseInfo.parseResponseData().purchaseState.ordinal() == 0) {
                    Save.gd.setLightningPurchased(true);
                    Save.gd.setLightningEquiped(true);
                    Save.gd.setKamehamehaEquiped(false);
                    Save.gd.setFireBall2Equiped(false);
                    Save.gd.setFireBallEquiped(false);
                    Save.save();
                    Save.load();

                    try {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(getApplicationContext(), "Lightning Unlocked!",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (final Exception ex) {
                    }
                }
            }



            if (bp.getPurchaseTransactionDetails(getResources().getString(R.string.super_brick)) == null) {
                Save.gd.setBrick2Purchased(false);
                Save.save();
                Save.load();

            } else {

                if (bp.getPurchaseTransactionDetails(getResources().getString(R.string.super_brick)).purchaseInfo.parseResponseData().purchaseState.ordinal() == 0) {
                    Save.gd.setBrick2Purchased(true);
                    Save.save();
                    Save.load();

                    try {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(getApplicationContext(), "Super Brick Unlocked!",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (final Exception ex) {
                    }
                }
            }


            if (bp.getPurchaseTransactionDetails(getResources().getString(R.string.ads_remover)) == null) {
                Save.gd.setAdsRemoverPurchased(false);
                Save.save();
                Save.load();
            } else {

                if (bp.getPurchaseTransactionDetails(getResources().getString(R.string.ads_remover)).purchaseInfo.parseResponseData().purchaseState.ordinal() == 0) {
                    Save.gd.setAdsRemoverPurchased(true);
                    Save.save();
                    Save.load();
                    try {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(getApplicationContext(), "Ads Removed!",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (final Exception ex) {
                    }
                }
            }



            if (bp.getPurchaseTransactionDetails(getResources().getString(R.string.hundred_coins)) == null) {
                //do nothing
            } else {
                if (bp.getPurchaseTransactionDetails(getResources().getString(R.string.hundred_coins)).purchaseInfo.parseResponseData().purchaseState.ordinal() == 0) {
                    Save.load();
                    Save.gd.setMoney(Save.gd.getMoney() + MyGdxGame.HUNDRED_COINS_STORE_PACK);
                    Save.save();
                    try {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(getApplicationContext(), "You purchased " + MyGdxGame.HUNDRED_COINS_STORE_PACK + " coins !",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (final Exception ex) {
                    }
                    bp.consumePurchase(getResources().getString(R.string.hundred_coins));
                    if (MyGdxGame.isSoundEnable() != 0) MyGdxGame.res.getSound("point").play();
                }
            }


            if (bp.getPurchaseTransactionDetails(getResources().getString(R.string.thousand_coins)) == null) {
                //do nothing
            } else {
                if (bp.getPurchaseTransactionDetails(getResources().getString(R.string.thousand_coins)).purchaseInfo.parseResponseData().purchaseState.ordinal() == 0) {
                    Save.load();
                    Save.gd.setMoney(Save.gd.getMoney() + MyGdxGame.THOUSAND_COINS_STORE_PACK);
                    Save.save();
                    try {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(getApplicationContext(), "You purchased " + MyGdxGame.THOUSAND_COINS_STORE_PACK + " coins !",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (final Exception ex) {
                    }
                    bp.consumePurchase(getResources().getString(R.string.thousand_coins));
                    if (MyGdxGame.isSoundEnable() != 0) MyGdxGame.res.getSound("point").play();
                }
            }




            if (bp.getPurchaseTransactionDetails(getResources().getString(R.string.extra_coins)) == null) {
                //do nothing
            } else {
                if (bp.getPurchaseTransactionDetails(getResources().getString(R.string.extra_coins)).purchaseInfo.parseResponseData().purchaseState.ordinal() == 0) {
                    Save.load();
                    Save.gd.setMoney(Save.gd.getMoney() + MyGdxGame.getContinueStorePack());
                    Save.save();
                    try {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(getApplicationContext(), "You purchased " + MyGdxGame.getContinueStorePack() + " coins !",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (final Exception ex) {
                    }
                    bp.consumePurchase(getResources().getString(R.string.extra_coins));
                    if (MyGdxGame.isSoundEnable() != 0) MyGdxGame.res.getSound("point").play();
                }
            }

        }else{
            displayDialogBillingNonAvailable();
        }
    }

    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    public void onBillingError(int i, Throwable throwable) {

    }

    @Override
    public void onBillingInitialized() {

    }


    @Override
    public boolean isValidPurchase(String s) {

        //CONTINUE

        // Optional: check if the product has already been purchased.
        /*try {
            if (getOwnedProducts().contains(sku)) {
                // Handle the case if product is already purchased.
                return false;
            }
        } catch (RemoteException e) {
            return false;
        }*/
        return true;
    }

    @Override
    public void onInAppPurchaseFinished(InAppPurchaseResult result) {


        /*Log.i("Iap-Ad", "onInAppPurchaseFinished Start");
        int resultCode = result.getResultCode();
        Log.i("Iap-Ad", "result code: " + resultCode);
        String sku = result.getProductId();
        if (resultCode == Activity.RESULT_OK) {
            Log.i("Iap-Ad", "purchased product id: " + sku);
            int responseCode = result.getPurchaseData().getIntExtra("RESPONSE_CODE", BILLING_RESPONSE_RESULT_OK);
            String purchaseData = result.getPurchaseData().getStringExtra("INAPP_PURCHASE_DATA");
            Log.i("Iap-Ad", "response code: " + responseCode);
            Log.i("Iap-Ad", "purchase data: " + purchaseData);

            // Finish purchase and consume product.
            if(!purchaseData.equals(getResources().getString(R.string.extra_coins))|
            !purchaseData.equals(getResources().getString(R.string.ads_remover))|
            !purchaseData.equals(getResources().getString(R.string.full_bar))
                    ){
                result.finishPurchase();
                MyGdxGame.setContinue(true);
            }
            // if (responseCode == BILLING_RESPONSE_RESULT_OK) {
            // Optional: your custom process goes here, e.g., add coins after purchase.
            //  }
        } else {
            Log.w("Iap-Ad", "Failed to purchase product: " + sku);
        }
        Log.i("Iap-Ad", "onInAppPurchaseFinished End");

        /*int resultCode = inAppPurchaseResult.getResultCode();
        if (resultCode == Activity.RESULT_OK) {
            // Credit the user with goods.
            MyGdxGame.setContinue(true);
            // Call finishPurchase() to consume the product.
            inAppPurchaseResult.finishPurchase();
        }*/
    }




}

