package com.mygdx.core.entities;

import com.mygdx.core.MyGdxGame.ConfirmInterface;

public interface ActionResolver {
	public boolean getSignedInGPGS();
	public void loginGPGS();
	public void submitScoreGPGS(int score);
	public void unlockAchievementGPGS(String achievementId);
	public void getLeaderboardGPGS();
	public void getAchievementsGPGS();
    public void purchaseFullBar();
	public void purchaseExtraCoins();
    public void purchaseAdsRemover();
    public void showOrLoadInterstitalVideo();
	public void showOrLoadBanner();
    public boolean fullBarPurchased();
    public boolean adsRemoverPurchased();
    public String getNetworkClass();
	public void showOrLoadInterstital();
    public void shareOnFacebook();
    public void showBannerAd();
    public void hideBannerAd();
	public void loadRewardedVideoMoPub();
	public void userClickedToWatchAdMoPub();
	public void showRewardedVideoChartBoost();
	public void showOrLoadRewardedVideoChartboost();
	void confirm(ConfirmInterface confirmInterface);
}
