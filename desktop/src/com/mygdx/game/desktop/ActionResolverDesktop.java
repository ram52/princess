package com.mygdx.game.desktop;

import com.mygdx.core.MyGdxGame.ConfirmInterface;
import com.mygdx.core.entities.ActionResolver;

public class ActionResolverDesktop implements ActionResolver {	
	boolean signedInStateGPGS = false;

	@Override
	public boolean getSignedInGPGS() {
		return signedInStateGPGS;
	}

	@Override
	public void loginGPGS() {
		System.out.println("loginGPGS");
		signedInStateGPGS = true;
	}

	@Override
	public void submitScoreGPGS(int score) {
		System.out.println("submitScoreGPGS " + score);
	}

	@Override
	public void unlockAchievementGPGS(String achievementId) {
		System.out.println("unlockAchievement " + achievementId);
	}

	@Override
	public void getLeaderboardGPGS() {
		System.out.println("getLeaderboardGPGS");
	}

	@Override
	public void getAchievementsGPGS() {
		System.out.println("getAchievementsGPGS");
	}

	@Override
	public void purchaseFullBar() {
		System.out.println("purchaseFullBar");
	}

	@Override
	public void purchaseExtraCoins() {
		System.out.println("purchaseExtraCoins");
	}

	@Override
	public void purchaseAdsRemover() {
		System.out.println("purchaseAdsRemover");
	}

	@Override
	public void purchaseExcalibur() {
		System.out.println("purchaseExcalibur");
	}

	@Override
	public void purchaseKamebeam() {
		System.out.println("purchaseExcalibur");
	}

	@Override
	public void purchaseBoots() {
		System.out.println("purchaseBoots");
	}

	@Override
	public void purchaseHadouBall() {
		System.out.println("purchaseHadouBall");
	}

	@Override
	public void purchaseMegaJump() {
		System.out.println("purchaseMegaJump");
	}

	@Override
	public void purchaseLightning() {
		System.out.println("purchaseLightning");
	}

	@Override
	public void purchaseSuperBrick() {
		System.out.println("purchaseSuperBrick");
	}

	@Override
	public void purchaseHundredCoins() {
		System.out.println("purchaseHundredCoins");
	}

	@Override
	public void purchaseThousandCoins() {
		System.out.println("purchaseThousandCoins");
	}

	@Override
	public void showOrLoadInterstitalVideo() {
		System.out.println("showOrLoadInterstitalVideo");
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
	    System.out.println("showOrLoadInterstital...");
	  }

	@Override
	public void shareOnFacebook() {
		System.out.println("shareOnFacebook");
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
	public void confirm(ConfirmInterface confirmInterface) {
		// TODO Auto-generated method stub
		
	}


}