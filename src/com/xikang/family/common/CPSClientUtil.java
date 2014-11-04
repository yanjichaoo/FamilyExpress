/* ==================================================
 * 产品名: 亲情快递
 * 文件名: CPSClientUtil.java
 * --------------------------------------------------
 * 开发环境: JDK1.6
 * --------------------------------------------------
 * 修订履历    2012/07/07  1.00  初版发行
 * --------------------------------------------------
 * (C) Copyright XIKANG CORPORATION 2012 All Rights Reserved.
 */
package com.xikang.family.common;

import com.xikang.cpsclient.NotificationService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

/**
 * 
 * 推送客户端工具类
 * 
 * 
 * 
 * @author 闫继超
 * @version 1.00
 */

public class CPSClientUtil {

	private static NotificationService mCPSService = null;
	private static boolean isBound = false;
	private static ServiceConnection mConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder service) {
			mCPSService = ((NotificationService.LocalBinder) service)
					.getService();
			// Bind APP User
			mCPSService.bindUser(Constants.APPID, Constants.CASTOKEN);
		}

		public void onServiceDisconnected(ComponentName className) {
			// UnBind APP User
			mCPSService.unbindUser(Constants.APPID);
			mCPSService = null;
		}
	};

	public static void connectCPS(Context context) {
		System.out.println("*********************connect====================");
		isBound = context.bindService(new Intent(
				"com.xikang.cpsclient.NotificationService"), mConnection,
				Context.BIND_AUTO_CREATE);
	}

	public static void disconnectCPS(Context context) {
		System.out
				.println("*********************disconnect====================");
		if (isBound) {
			mCPSService.unbindUser(Constants.APPID);
			context.unbindService(mConnection);
			isBound = false;
		}
	}
}
