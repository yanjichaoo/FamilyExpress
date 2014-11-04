/* ==================================================
 * 产品名: 亲情快递
 * 文件名: CommonNotificationReceiver.java
 * --------------------------------------------------
 * 开发环境: JDK1.6
 * --------------------------------------------------
 * 修订履历    2012/07/07  1.00  初版发行
 * --------------------------------------------------
 * (C) Copyright XIKANG CORPORATION 2012 All Rights Reserved.
 */
package com.xikang.family.common;

import com.xikang.family.activity.LoadingActivity;
import com.xikang.family.activity.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

/**
 * 
 * 接收到推送广播的receiver
 * 
 * 
 * 
 * @author 张荣
 * @version 1.00
 */

public class CommonNotificationReceiver extends BroadcastReceiver {
	private static final String familyExpressAction = Constants.ACTION_APP_NOTIFICATION
			.concat(Constants.APPID);
	private static String notificationSummary = null;
	private MediaPlayer mp = null;

	@Override
	public void onReceive(Context context, Intent intent) {
		System.out.println("Receive broadcast...");
		if (familyExpressAction.equals(intent.getAction())) {

			notificationSummary = intent
					.getStringExtra(Constants.NOTIFICATION_SUMMARY);
			
			//Util.isActivityRunning(context))
			if ((Util.isApplicationBroughtToBackground(context))
					&& (notificationSummary != null)
					&& !("".equals(notificationSummary))) {
				mp = new MediaPlayer();
				mp.reset();
				mp = MediaPlayer.create(context, R.raw.tip);
				mp.start();
				
				showNotification(
						intent.getStringExtra(Constants.NOTIFICATION_NO),
						intent.getStringExtra(Constants.NOTIFICATION_BODY),
						intent.getStringExtra(Constants.NOTIFICATION_SUMMARY),
						context);

			}
			Intent mIntent = new Intent(context, LocalExpressService.class);
			Bundle mBundle = new Bundle();
			mBundle.putString(Constants.NOTIFICATION_PHRCODE,
					intent.getStringExtra(Constants.NOTIFICATION_PHRCODE));
			mBundle.putString(Constants.NOTIFICATION_SUMMARY,
					intent.getStringExtra(Constants.NOTIFICATION_SUMMARY));
			mBundle.putString(Constants.NOTIFICATION_BODY,
					intent.getStringExtra(Constants.NOTIFICATION_BODY));
			mBundle.putString(Constants.NOTIFICATION_TIMESTAMP,
					intent.getStringExtra(Constants.NOTIFICATION_TIMESTAMP));
			mIntent.putExtras(mBundle);
			context.startService(mIntent);
		}
	}

	private void showNotification(String ntid, String body, String summary,
			Context context) {
		Notification mNotification = new Notification();
		Intent mIntent = new Intent(context, LoadingActivity.class);
		Bundle mBundle = new Bundle();
		mBundle.putString("MSG", body);
		mIntent.putExtras(mBundle);
		mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent mPendingIntent = PendingIntent.getActivity(context, 0,
				mIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		mNotification.icon = android.R.drawable.stat_notify_voicemail;
		mNotification.flags |= Notification.FLAG_AUTO_CANCEL;

		mNotification.setLatestEventInfo(context, "亲情快递", summary,
				mPendingIntent);
		NotificationManager mNotificationManager = (NotificationManager) context
				.getSystemService("notification");
		mNotificationManager.notify(9999, mNotification);
	}

}
