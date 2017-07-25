package com.moe.bindings.chartboost;


import apple.NSObject;
import apple.foundation.NSArray;
import apple.foundation.NSMethodSignature;
import apple.foundation.NSSet;
import apple.foundation.NSURL;
import apple.uikit.UIViewController;
import com.moe.bindings.chartboost.protocol.ChartboostDelegate;
import org.moe.natj.c.ann.FunctionPtr;
import org.moe.natj.general.NatJ;
import org.moe.natj.general.Pointer;
import org.moe.natj.general.ann.Generated;
import org.moe.natj.general.ann.Library;
import org.moe.natj.general.ann.Mapped;
import org.moe.natj.general.ann.MappedReturn;
import org.moe.natj.general.ann.NInt;
import org.moe.natj.general.ann.NUInt;
import org.moe.natj.general.ann.Owned;
import org.moe.natj.general.ann.Runtime;
import org.moe.natj.general.ptr.VoidPtr;
import org.moe.natj.objc.Class;
import org.moe.natj.objc.ObjCRuntime;
import org.moe.natj.objc.SEL;
import org.moe.natj.objc.ann.ObjCClassBinding;
import org.moe.natj.objc.ann.Selector;
import org.moe.natj.objc.map.ObjCObjectMapper;

@Generated
@Library("Chartboost")
@Runtime(ObjCRuntime.class)
@ObjCClassBinding
public class Chartboost extends NSObject {
	static {
		NatJ.register();
	}

	@Generated
	protected Chartboost(Pointer peer) {
		super(peer);
	}

	@Generated
	@Selector("accessInstanceVariablesDirectly")
	public static native boolean accessInstanceVariablesDirectly();

	@Generated
	@Owned
	@Selector("alloc")
	public static native Chartboost alloc();

	@Generated
	@Selector("allocWithZone:")
	@MappedReturn(ObjCObjectMapper.class)
	public static native Object allocWithZone(VoidPtr zone);

	@Generated
	@Selector("automaticallyNotifiesObserversForKey:")
	public static native boolean automaticallyNotifiesObserversForKey(String key);

	@Generated
	@Selector("cacheInPlay:")
	public static native void cacheInPlay(String location);

	@Generated
	@Selector("cacheInterstitial:")
	public static native void cacheInterstitial(String location);

	@Generated
	@Selector("cacheMoreApps:")
	public static native void cacheMoreApps(String location);

	@Generated
	@Selector("cacheRewardedVideo:")
	public static native void cacheRewardedVideo(String location);

	@Generated
	@Selector("cancelPreviousPerformRequestsWithTarget:")
	public static native void cancelPreviousPerformRequestsWithTarget(
			@Mapped(ObjCObjectMapper.class) Object aTarget);

	@Generated
	@Selector("cancelPreviousPerformRequestsWithTarget:selector:object:")
	public static native void cancelPreviousPerformRequestsWithTargetSelectorObject(
			@Mapped(ObjCObjectMapper.class) Object aTarget, SEL aSelector,
			@Mapped(ObjCObjectMapper.class) Object anArgument);

	@Generated
	@Selector("class")
	public static native Class class_objc_static();

	@Generated
	@Selector("classFallbacksForKeyedArchiver")
	public static native NSArray<String> classFallbacksForKeyedArchiver();

	@Generated
	@Selector("classForKeyedUnarchiver")
	public static native Class classForKeyedUnarchiver();

	@Generated
	@Selector("debugDescription")
	public static native String debugDescription_static();

	@Generated
	@Selector("description")
	public static native String description_static();

	@Generated
	@Selector("didPassAgeGate:")
	public static native void didPassAgeGate(boolean pass);

	@Generated
	@Selector("getAutoCacheAds")
	public static native boolean getAutoCacheAds();

	@Generated
	@Selector("getAutoIAPTracking")
	public static native boolean getAutoIAPTracking();

	@Generated
	@Selector("getCustomId")
	public static native String getCustomId();

	@Generated
	@Selector("getInPlay:")
	public static native CBInPlay getInPlay(String location);

	@Generated
	@Selector("getSDKVersion")
	public static native String getSDKVersion();

	@Generated
	@Selector("handleOpenURL:sourceApplication:")
	public static native boolean handleOpenURLSourceApplication(NSURL url,
			String sourceApplication);

	@Generated
	@Selector("handleOpenURL:sourceApplication:annotation:")
	public static native boolean handleOpenURLSourceApplicationAnnotation(
			NSURL url, String sourceApplication,
			@Mapped(ObjCObjectMapper.class) Object annotation);

	@Generated
	@Selector("hasInPlay:")
	public static native boolean hasInPlay(String location);

	@Generated
	@Selector("hasInterstitial:")
	public static native boolean hasInterstitial(String location);

	@Generated
	@Selector("hasMoreApps:")
	public static native boolean hasMoreApps(String location);

	@Generated
	@Selector("hasRewardedVideo:")
	public static native boolean hasRewardedVideo(String location);

	@Generated
	@Selector("hash")
	@NUInt
	public static native long hash_static();

	@Generated
	@Selector("init")
	public native Chartboost init();

	@Generated
	@Selector("initialize")
	public static native void initialize();

	@Generated
	@Selector("instanceMethodForSelector:")
	@FunctionPtr(name = "call_instanceMethodForSelector_ret")
	public static native NSObject.Function_instanceMethodForSelector_ret instanceMethodForSelector(
			SEL aSelector);

	@Generated
	@Selector("instanceMethodSignatureForSelector:")
	public static native NSMethodSignature instanceMethodSignatureForSelector(
			SEL aSelector);

	@Generated
	@Selector("instancesRespondToSelector:")
	public static native boolean instancesRespondToSelector(SEL aSelector);

	@Generated
	@Selector("isAnyViewVisible")
	public static native boolean isAnyViewVisible();

	@Generated
	@Selector("isSubclassOfClass:")
	public static native boolean isSubclassOfClass(Class aClass);

	@Generated
	@Selector("keyPathsForValuesAffectingValueForKey:")
	public static native NSSet<String> keyPathsForValuesAffectingValueForKey(
			String key);

	@Generated
	@Selector("load")
	public static native void load_objc_static();

	@Generated
	@Owned
	@Selector("new")
	@MappedReturn(ObjCObjectMapper.class)
	public static native Object new_objc();

	@Generated
	@Selector("resolveClassMethod:")
	public static native boolean resolveClassMethod(SEL sel);

	@Generated
	@Selector("resolveInstanceMethod:")
	public static native boolean resolveInstanceMethod(SEL sel);

	@Generated
	@Selector("setAutoCacheAds:")
	public static native void setAutoCacheAds(boolean shouldCache);

	@Generated
	@Selector("setChartboostWrapperVersion:")
	public static native void setChartboostWrapperVersion(
			String chartboostWrapperVersion);

	@Generated
	@Selector("setCustomId:")
	public static native void setCustomId(String customId);

	@Generated
	@Selector("setDelegate:")
	public static native void setDelegate(
			@Mapped(ObjCObjectMapper.class) ChartboostDelegate del);

	@Generated
	@Deprecated
	@Selector("setFramework:")
	public static native void setFramework(@NUInt long framework);

	@Generated
	@Selector("setFramework:withVersion:")
	public static native void setFrameworkWithVersion(@NUInt long framework,
			String version);

	@Generated
	@Deprecated
	@Selector("setFrameworkVersion:")
	public static native void setFrameworkVersion(String frameworkVersion);

	@Generated
	@Selector("setMediation:withVersion:")
	public static native void setMediationWithVersion(@NUInt long library,
			String libraryVersion);

	@Generated
	@Selector("setShouldDisplayLoadingViewForMoreApps:")
	public static native void setShouldDisplayLoadingViewForMoreApps(
			boolean shouldDisplay);

	@Generated
	@Selector("setShouldPauseClickForConfirmation:")
	public static native void setShouldPauseClickForConfirmation(
			boolean shouldPause);

	@Generated
	@Selector("setShouldPrefetchVideoContent:")
	public static native void setShouldPrefetchVideoContent(
			boolean shouldPrefetch);

	@Generated
	@Selector("setShouldRequestInterstitialsInFirstSession:")
	public static native void setShouldRequestInterstitialsInFirstSession(
			boolean shouldRequest);

	@Generated
	@Selector("setStatusBarBehavior:")
	public static native void setStatusBarBehavior(@NUInt long statusBarBehavior);

	@Generated
	@Selector("setVersion:")
	public static native void setVersion(@NInt long aVersion);

	@Generated
	@Selector("showInterstitial:")
	public static native void showInterstitial(String location);

	@Generated
	@Selector("showMoreApps:")
	public static native void showMoreApps(String location);

	@Generated
	@Selector("showMoreApps:location:")
	public static native void showMoreAppsLocation(
			UIViewController viewController, String location);

	@Generated
	@Selector("showRewardedVideo:")
	public static native void showRewardedVideo(String location);

	@Generated
	@Selector("startWithAppId:appSignature:delegate:")
	public static native void startWithAppIdAppSignatureDelegate(String appId,
			String appSignature,
			@Mapped(ObjCObjectMapper.class) ChartboostDelegate delegate);

	@Generated
	@Selector("superclass")
	public static native Class superclass_static();

	@Generated
	@Selector("version")
	@NInt
	public static native long version_static();
}