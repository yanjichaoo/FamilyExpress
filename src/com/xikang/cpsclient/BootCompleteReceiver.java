package com.xikang.cpsclient;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootCompleteReceiver extends BroadcastReceiver {
	private static final String action = "android.intent.action.BOOT_COMPLETED";

	@Override
	public void onReceive(Context context, Intent intent) {
		if (action.equals(intent.getAction())) {
			ServiceManager serviceManager = new ServiceManager(context);
			serviceManager.startService();
		}
	}
}
