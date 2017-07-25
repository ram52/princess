package com.moe.bindings.chartboost.c;


import org.moe.natj.c.CRuntime;
import org.moe.natj.c.ann.CVariable;
import org.moe.natj.general.NatJ;
import org.moe.natj.general.ann.Generated;
import org.moe.natj.general.ann.Library;
import org.moe.natj.general.ann.MappedReturn;
import org.moe.natj.general.ann.Runtime;
import org.moe.natj.objc.map.ObjCStringMapper;

@Generated
@Library("Chartboost")
@Runtime(CRuntime.class)
public final class Chartboost {
	static {
		NatJ.register();
	}

	@Generated
	private Chartboost() {
	}

	@Generated
	@CVariable()
	@MappedReturn(ObjCStringMapper.class)
	public static native String CBLocationStartup();

	@Generated
	@CVariable()
	@MappedReturn(ObjCStringMapper.class)
	public static native String CBLocationHomeScreen();

	@Generated
	@CVariable()
	@MappedReturn(ObjCStringMapper.class)
	public static native String CBLocationMainMenu();

	@Generated
	@CVariable()
	@MappedReturn(ObjCStringMapper.class)
	public static native String CBLocationGameScreen();

	@Generated
	@CVariable()
	@MappedReturn(ObjCStringMapper.class)
	public static native String CBLocationAchievements();

	@Generated
	@CVariable()
	@MappedReturn(ObjCStringMapper.class)
	public static native String CBLocationQuests();

	@Generated
	@CVariable()
	@MappedReturn(ObjCStringMapper.class)
	public static native String CBLocationPause();

	@Generated
	@CVariable()
	@MappedReturn(ObjCStringMapper.class)
	public static native String CBLocationLevelStart();

	@Generated
	@CVariable()
	@MappedReturn(ObjCStringMapper.class)
	public static native String CBLocationLevelComplete();

	@Generated
	@CVariable()
	@MappedReturn(ObjCStringMapper.class)
	public static native String CBLocationTurnComplete();

	@Generated
	@CVariable()
	@MappedReturn(ObjCStringMapper.class)
	public static native String CBLocationIAPStore();

	@Generated
	@CVariable()
	@MappedReturn(ObjCStringMapper.class)
	public static native String CBLocationItemStore();

	@Generated
	@CVariable()
	@MappedReturn(ObjCStringMapper.class)
	public static native String CBLocationGameOver();

	@Generated
	@CVariable()
	@MappedReturn(ObjCStringMapper.class)
	public static native String CBLocationLeaderBoard();

	@Generated
	@CVariable()
	@MappedReturn(ObjCStringMapper.class)
	public static native String CBLocationSettings();

	@Generated
	@CVariable()
	@MappedReturn(ObjCStringMapper.class)
	public static native String CBLocationQuit();

	@Generated
	@CVariable()
	@MappedReturn(ObjCStringMapper.class)
	public static native String CBLocationDefault();
}