package com.xikang.cpsclient;

import com.xikang.family.common.Constants;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ShutdownReceiver extends BroadcastReceiver {
	
	private static final String TAG = "ShutdownReceiver";
	private static final String action = "android.intent.action.ACTION_SHUTDOWN";

	@Override
	public void onReceive(Context context, Intent intent) {
		if (action.equals(intent.getAction())) {
			Log.e(TAG,"shutdown signal receive");
			XmppManager manager = Constants.xmppManager;
			if(manager != null ){
				Log.e(TAG,"terminate connnection");
				manager.terminatePersistentConnection();
			}
			
		}

	}

}
