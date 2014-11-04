package com.xikang.cpsclient;

import com.xikang.family.common.Constants;

import android.content.Context;
import android.content.Intent;

public final class ServiceManager {

	private Context context;

	// private static final String familyExpressAction =
	// Constants.ACTION_APP_NOTIFICATION
	// .concat(Constants.APPID);
	// private BroadcastReceiver myReceiver = new CommonNotificationReceiver();
	public ServiceManager(Context context) {
		this.context = context;
	}

	public void startService() {
		Thread serviceThread = new Thread(new Runnable() {
			@Override
			public void run() {
				Intent intent = NotificationService.getIntent();
				context.startService(intent);
				Constants.IS_CPS_SERVICE_RUN = true;
				// IntentFilter filter = new IntentFilter();
				// filter.addAction(familyExpressAction);
				// filter.setPriority(Integer.MAX_VALUE);
				// context.registerReceiver(myReceiver, filter);
			}
		});
		serviceThread.start();
	}

	public void stopService() {
		Intent intent = NotificationService.getIntent();
		context.stopService(intent);
		Constants.IS_CPS_SERVICE_RUN = false;
		// context.unregisterReceiver(myReceiver);
	}

}
