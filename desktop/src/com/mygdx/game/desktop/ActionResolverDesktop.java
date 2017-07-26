package com.mygdx.game.desktop;

import com.badlogic.gdx.Gdx;
import com.mygdx.core.MyGdxGame.ConfirmInterface;
import com.mygdx.core.entities.ActionResolver;
import com.mygdx.core.handlers.SimpleDirectionGestureDetector;

public class ActionResolverDesktop implements ActionResolver {
	private static String LOG_TAG = ActionResolverDesktop.class.getSimpleName();
	boolean signedInStateGPGS = false;

	@Override
	public boolean getSignedInGPGS() {
		return signedInStateGPGS;
	}

	@Override
	public void loginGPGS(boolean silent) {

	}

	@Override
	public void submitScoreGPGS(int score) {
		Gdx.app.debug(LOG_TAG,"submitScoreGPGS " + score);
	}

	@Override
	public void unlockAchievementGPGS(String achievementId) {
		Gdx.app.debug(LOG_TAG,"unlockAchievement " + achievementId);
	}

	@Override
	public void getLeaderboardGPGS(boolean silent, int score) {

	}


	@Override
	public void getAchievementsGPGS() {
		Gdx.app.debug(LOG_TAG,"getAchievementsGPGS");
	}

	@Override
	public void purchaseFullBar() {
		Gdx.app.debug(LOG_TAG,"purchaseFullBar");
	}

	@Override
	public void purchaseExtraCoins() {
		Gdx.app.debug(LOG_TAG,"purchaseExtraCoins");
	}

	@Override
	public void purchaseAdsRemover() {
		Gdx.app.debug(LOG_TAG,"purchaseAdsRemover");
	}

	@Override
	public void purchaseExcalibur() {
		Gdx.app.debug(LOG_TAG,"purchaseExcalibur");
	}

	@Override
	public void purchaseKamebeam() {
		Gdx.app.debug(LOG_TAG,"purchaseExcalibur");
	}

	@Override
	public void purchaseBoots() {
		Gdx.app.debug(LOG_TAG,"purchaseBoots");
	}

	@Override
	public void purchaseHadouBall() {
		Gdx.app.debug(LOG_TAG,"purchaseHadouBall");
	}

	@Override
	public void purchaseMegaJump() {
		Gdx.app.debug(LOG_TAG,"purchaseMegaJump");
	}

	@Override
	public void purchaseLightning() {
		Gdx.app.debug(LOG_TAG,"purchaseLightning");
	}

	@Override
	public void purchaseSuperBrick() {
		Gdx.app.debug(LOG_TAG,"purchaseSuperBrick");
	}

	@Override
	public void purchaseHundredCoins() {
		Gdx.app.debug(LOG_TAG,"purchaseHundredCoins");
	}

	@Override
	public void purchaseThousandCoins() {
		Gdx.app.debug(LOG_TAG,"purchaseThousandCoins");
	}

	@Override
	public void showOrLoadInterstitalVideo() {
		Gdx.app.debug(LOG_TAG,"showOrLoadInterstitalVideo");
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
	    Gdx.app.debug(LOG_TAG,"showOrLoadInterstital...");
	  }

	@Override
	public void shareOnFacebook() {
		Gdx.app.debug(LOG_TAG,"shareOnFacebook");
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
	public void showOrLoadRewardedVideoChartboost() {

	}

	@Override
	public void confirm(ConfirmInterface confirmInterface) {
		// TODO Auto-generated method stub
		
	}


}