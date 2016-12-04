package com.mygdx.game.android;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
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

/*import com.mopub.common.MoPub;
import com.mopub.common.MoPubReward;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubRewardedVideoListener;
import com.mopub.nativeads.RequestParameters;*/
import com.mygdx.core.MyGdxGame;
import com.mygdx.core.MyGdxGame.ConfirmInterface;
import com.mygdx.core.MyGdxGame.RequestHandler;
import com.mygdx.core.entities.ActionResolver;
import com.mygdx.core.handlers.Save;
import com.ram52.princess.R;

import java.util.ArrayList;
import java.util.Set;

import io.fabric.sdk.android.Fabric;

public class AndroidLauncher extends AndroidApplication implements
        GameHelperListener, ActionResolver, RequestHandler,PlayStorePurchaseListener, BillingProcessor.IBillingHandler {

    public static final int BILLING_RESPONSE_RESULT_OK = 0;
    private GameHelper gameHelper;
    public AdView bannerAdView;
    public View gameView;
    private InterstitialAd interstitialAd, interstitialVideoAd;
    private CustomDialogClass dialog;
    public boolean created;
    private BillingProcessor bp;
    private boolean fullBarPurchased = false;
    private boolean adsRemoverPurchased = false;
    private String productId = "";
    private String res = "";
    private Display display;
    public boolean debug = false; //IN DEBUG DO NOT SUBMIT SCORE && DO NOT SHOW ADS && DO NOT UNLOCK ACHIEVEMENTS
    public ProgressDialog pdialog;
    private static boolean b = true;
    //private MoPubRewardedVideoListener rewardedVideoListener;
    private boolean rewardCompleted = false;

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
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        Fabric.with(this, new Crashlytics());

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


    // Native video ads work by delegating to your existing Adaptor. Use this method if
    // you don't already have an Adaptor in your app with at least 10 rows of data.
    private Adapter makeSampleAdapter() {
        ArrayList<String> sampleItems = new ArrayList<String>();
        for (int i = 1; i <= 20; i++) {
            sampleItems.add("Item " + i);
        }

        ArrayAdapter<String> sampleAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                sampleItems
        );

        return sampleAdapter;
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
    public void loadRewardedVideoMoPub() {
        runOnUiThread(new Runnable() {
            public void run() {
                pdialog = new ProgressDialog(AndroidLauncher.this);
                pdialog.setCancelable(false);
                pdialog.setMessage("Loading...");
                pdialog.show();
            }
        });

        //MoPub.loadRewardedVideo(getResources().getString(R.string.ad_unit_mopub_reward_video));
    }

    @Override
    public void userClickedToWatchAdMoPub() {
        //MoPub.showRewardedVideoChartBoost(getResources().getString(R.string.ad_unit_mopub_reward_video));
    }

    @Override
    public void showRewardedVideoChartBoost() {
    }

    @Override
    public void showOrLoadRewardedVideoChartboost() {
        runOnUiThread(new Runnable() {
            public void run() {
                pdialog = new ProgressDialog(AndroidLauncher.this);
                pdialog.setCanceledOnTouchOutside(false);
                pdialog.setCancelable(false);
                pdialog.setMessage("Loading...");
                pdialog.show();
            }
        });
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
        //MoPub.onPause(this);

        if(MyGdxGame.isSoundEnable() != 0) {
            if(Save.gd != null) {
                Save.gd.setSoundPause(true);
            }
        }
        // when the screen is about to turn off
        if (ScreenReceiver.wasScreenOn) {
            // this is the case when onPause() is called by the system due to a screen state change
            System.out.println("SCREEN TURNED OFF");
        } else {
            // this is when onPause() is called when the screen state has not changed
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        //MoPub.onResume(this);
        // Optional targeting parameters
        /*RequestParameters parameters = new RequestParameters.Builder()
                //.keywords("your target words here")
                .build();*/


        if(MyGdxGame.isSoundEnable() != 0) {
            if(Save.gd != null) {
                Save.gd.setSoundPause(false);
            }
        }
        // only when screen turns on
        if (!ScreenReceiver.wasScreenOn) {
            // this is when onResume() is called due to a screen state change
            System.out.println("SCREEN TURNED ON");
        } else {
            // this is when onResume() is called when the screen state has not changed
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        //MoPub.onStart(this);
        gameHelper.onStart(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        //MoPub.onRestart(this);
        gameHelper.onStop();
    }

    @Override
    public void onBackPressed() {
        if(!dialog.isShowing()) {
            showDialog();
            MyGdxGame.gsm.game().pause();
        }

        super.onBackPressed();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //MoPub.onRestart(this);
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
    public boolean fullBarPurchased() {
        return fullBarPurchased;
    }

    @Override
    public boolean adsRemoverPurchased() {
        return adsRemoverPurchased;
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

            if (bp.getPurchaseTransactionDetails(getResources().getString(R.string.full_bar)) == null) {
                Save.gd.setFullBarPurchased(false);
                Save.save();
                fullBarPurchased = false;
                Save.load();

            } else {

                if (bp.getPurchaseTransactionDetails(getResources().getString(R.string.full_bar)).purchaseInfo.parseResponseData().purchaseState.ordinal() == 0) {
                    Save.gd.setFullBarPurchased(true);
                    Save.save();
                    fullBarPurchased = true;
                    Save.load();

                    try {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(getApplicationContext(), "Full Bar Unlocked!",
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
                adsRemoverPurchased = false;
                Save.load();
            } else {

                if (bp.getPurchaseTransactionDetails(getResources().getString(R.string.ads_remover)).purchaseInfo.parseResponseData().purchaseState.ordinal() == 0) {
                    Save.gd.setAdsRemoverPurchased(true);
                    Save.save();
                    adsRemoverPurchased = true;
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

