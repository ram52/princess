package com.moe.bindings.chartboost.enums;


import org.moe.natj.general.ann.Generated;
import org.moe.natj.general.ann.NUInt;

@Generated
public final class CBLoadError {
	@Generated
	private CBLoadError() {
	}

	@Generated
	@NUInt
	public static final long Internal = 0x0000000000000000L;
	@Generated
	@NUInt
	public static final long InternetUnavailable = 0x0000000000000001L;
	@Generated
	@NUInt
	public static final long TooManyConnections = 0x0000000000000002L;
	@Generated
	@NUInt
	public static final long WrongOrientation = 0x0000000000000003L;
	@Generated
	@NUInt
	public static final long FirstSessionInterstitialsDisabled = 0x0000000000000004L;
	@Generated
	@NUInt
	public static final long NetworkFailure = 0x0000000000000005L;
	@Generated
	@NUInt
	public static final long NoAdFound = 0x0000000000000006L;
	@Generated
	@NUInt
	public static final long SessionNotStarted = 0x0000000000000007L;
	@Generated
	@NUInt
	public static final long ImpressionAlreadyVisible = 0x0000000000000008L;
	@Generated
	@NUInt
	public static final long UserCancellation = 0x000000000000000AL;
	@Generated
	@NUInt
	public static final long NoLocationFound = 0x000000000000000BL;
	@Generated
	@NUInt
	public static final long AssetDownloadFailure = 0x0000000000000010L;
	@Generated
	@NUInt
	public static final long PrefetchingIncomplete = 0x0000000000000015L;
	@Generated
	@NUInt
	public static final long WebViewScriptError = 0x0000000000000016L;
}