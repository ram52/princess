package com.moe.bindings.chartboost.protocol;


import org.moe.natj.general.ann.Generated;
import org.moe.natj.general.ann.Library;
import org.moe.natj.general.ann.NUInt;
import org.moe.natj.general.ann.Runtime;
import org.moe.natj.objc.ObjCRuntime;
import org.moe.natj.objc.ann.IsOptional;
import org.moe.natj.objc.ann.ObjCProtocolName;
import org.moe.natj.objc.ann.Selector;

@Generated
@Library("Chartboost")
@Runtime(ObjCRuntime.class)
@ObjCProtocolName("ChartboostDelegate")
public interface ChartboostDelegate {
	@Generated
	@IsOptional
	@Selector("didCacheInPlay:")
	default void didCacheInPlay(String location) {
		throw new java.lang.UnsupportedOperationException();
	}

	@Generated
	@IsOptional
	@Selector("didCacheInterstitial:")
	default void didCacheInterstitial(String location) {
		throw new java.lang.UnsupportedOperationException();
	}

	@Generated
	@IsOptional
	@Selector("didCacheMoreApps:")
	default void didCacheMoreApps(String location) {
		throw new java.lang.UnsupportedOperationException();
	}

	@Generated
	@IsOptional
	@Selector("didCacheRewardedVideo:")
	default void didCacheRewardedVideo(String location) {
		throw new java.lang.UnsupportedOperationException();
	}

	@Generated
	@IsOptional
	@Selector("didClickInterstitial:")
	default void didClickInterstitial(String location) {
		throw new java.lang.UnsupportedOperationException();
	}

	@Generated
	@IsOptional
	@Selector("didClickMoreApps:")
	default void didClickMoreApps(String location) {
		throw new java.lang.UnsupportedOperationException();
	}

	@Generated
	@IsOptional
	@Selector("didClickRewardedVideo:")
	default void didClickRewardedVideo(String location) {
		throw new java.lang.UnsupportedOperationException();
	}

	@Generated
	@IsOptional
	@Selector("didCloseInterstitial:")
	default void didCloseInterstitial(String location) {
		throw new java.lang.UnsupportedOperationException();
	}

	@Generated
	@IsOptional
	@Selector("didCloseMoreApps:")
	default void didCloseMoreApps(String location) {
		throw new java.lang.UnsupportedOperationException();
	}

	@Generated
	@IsOptional
	@Selector("didCloseRewardedVideo:")
	default void didCloseRewardedVideo(String location) {
		throw new java.lang.UnsupportedOperationException();
	}

	@Generated
	@IsOptional
	@Selector("didCompleteAppStoreSheetFlow")
	default void didCompleteAppStoreSheetFlow() {
		throw new java.lang.UnsupportedOperationException();
	}

	@Generated
	@IsOptional
	@Selector("didCompleteRewardedVideo:withReward:")
	default void didCompleteRewardedVideoWithReward(String location, int reward) {
		throw new java.lang.UnsupportedOperationException();
	}

	@Generated
	@IsOptional
	@Selector("didDismissInterstitial:")
	default void didDismissInterstitial(String location) {
		throw new java.lang.UnsupportedOperationException();
	}

	@Generated
	@IsOptional
	@Selector("didDismissMoreApps:")
	default void didDismissMoreApps(String location) {
		throw new java.lang.UnsupportedOperationException();
	}

	@Generated
	@IsOptional
	@Selector("didDismissRewardedVideo:")
	default void didDismissRewardedVideo(String location) {
		throw new java.lang.UnsupportedOperationException();
	}

	@Generated
	@IsOptional
	@Selector("didDisplayInterstitial:")
	default void didDisplayInterstitial(String location) {
		throw new java.lang.UnsupportedOperationException();
	}

	@Generated
	@IsOptional
	@Selector("didDisplayMoreApps:")
	default void didDisplayMoreApps(String location) {
		throw new java.lang.UnsupportedOperationException();
	}

	@Generated
	@IsOptional
	@Selector("didDisplayRewardedVideo:")
	default void didDisplayRewardedVideo(String location) {
		throw new java.lang.UnsupportedOperationException();
	}

	@Generated
	@IsOptional
	@Selector("didFailToLoadInPlay:withError:")
	default void didFailToLoadInPlayWithError(String location, @NUInt long error) {
		throw new java.lang.UnsupportedOperationException();
	}

	@Generated
	@IsOptional
	@Selector("didFailToLoadInterstitial:withError:")
	default void didFailToLoadInterstitialWithError(String location,
			@NUInt long error) {
		throw new java.lang.UnsupportedOperationException();
	}

	@Generated
	@IsOptional
	@Selector("didFailToLoadMoreApps:withError:")
	default void didFailToLoadMoreAppsWithError(String location,
			@NUInt long error) {
		throw new java.lang.UnsupportedOperationException();
	}

	@Generated
	@IsOptional
	@Selector("didFailToLoadRewardedVideo:withError:")
	default void didFailToLoadRewardedVideoWithError(String location,
			@NUInt long error) {
		throw new java.lang.UnsupportedOperationException();
	}

	@Generated
	@IsOptional
	@Selector("didFailToRecordClick:withError:")
	default void didFailToRecordClickWithError(String location,
			@NUInt long error) {
		throw new java.lang.UnsupportedOperationException();
	}

	@Generated
	@IsOptional
	@Selector("didInitialize:")
	default void didInitialize(boolean status) {
		throw new java.lang.UnsupportedOperationException();
	}

	@Generated
	@IsOptional
	@Selector("didPauseClickForConfirmation")
	default void didPauseClickForConfirmation() {
		throw new java.lang.UnsupportedOperationException();
	}

	@Generated
	@IsOptional
	@Selector("shouldDisplayInterstitial:")
	default boolean shouldDisplayInterstitial(String location) {
		throw new java.lang.UnsupportedOperationException();
	}

	@Generated
	@IsOptional
	@Selector("shouldDisplayMoreApps:")
	default boolean shouldDisplayMoreApps(String location) {
		throw new java.lang.UnsupportedOperationException();
	}

	@Generated
	@IsOptional
	@Selector("shouldDisplayRewardedVideo:")
	default boolean shouldDisplayRewardedVideo(String location) {
		throw new java.lang.UnsupportedOperationException();
	}

	@Generated
	@IsOptional
	@Selector("shouldRequestInterstitial:")
	default boolean shouldRequestInterstitial(String location) {
		throw new java.lang.UnsupportedOperationException();
	}

	@Generated
	@IsOptional
	@Selector("willDisplayVideo:")
	default void willDisplayVideo(String location) {
		throw new java.lang.UnsupportedOperationException();
	}
}