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
	public void purchaseExcalibur();
	public void purchaseKamebeam();
	public void purchaseBoots();
	public void purchaseHadouBall();
	public void purchaseMegaJump();
	public void purchaseLightning();
	public void purchaseSuperBrick();
	public void purchaseHundredCoins();
	public void purchaseThousandCoins();
    public void showOrLoadInterstitalVideo();
	public void showOrLoadBanner();
    public String getNetworkClass();
	public void showOrLoadInterstital();
    public void shareOnFacebook();
    public void showBannerAd();
    public void hideBannerAd();
	public void showRewardedVideoChartBoost();
	void confirm(ConfirmInterface confirmInterface);
}
