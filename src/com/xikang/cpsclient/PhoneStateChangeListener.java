package com.xikang.cpsclient;

import com.xikang.family.common.Util;

import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * A listener class for monitoring changes in phone connection states.
 */
public class PhoneStateChangeListener extends PhoneStateListener {

	private static final String LOGTAG = Util
			.makeLogTag(PhoneStateChangeListener.class);

	private final NotificationService notificationService;

	public PhoneStateChangeListener(NotificationService notificationService) {
		this.notificationService = notificationService;
	}

	@Override
	public void onDataConnectionStateChanged(int state) {
		super.onDataConnectionStateChanged(state);
		Log.e(LOGTAG, "onDataConnectionStateChanged()...");
		Log.e(LOGTAG, "Data Connection State = " + getState(state));

		if (state == TelephonyManager.DATA_CONNECTED) {
			Log.e(LOGTAG,"data connected  begin reconnect");
			notificationService.connect();
		}
	}

	private String getState(int state) {
		switch (state) {
		case 0: // '\0'
			return "DATA_DISCONNECTED";
		case 1: // '\001'
			return "DATA_CONNECTING";
		case 2: // '\002'
			return "DATA_CONNECTED";
		case 3: // '\003'
			return "DATA_SUSPENDED";
		}
		return "DATA_<UNKNOWN>";
	}

}