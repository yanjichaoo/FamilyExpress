package com.xikang.cpsclient;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.xikang.family.common.Constants;
import com.xikang.family.common.Util;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class NotificationService extends Service {

	private static final String LOGTAG = Util
			.makeLogTag(NotificationService.class);

	public static final String SERVICE_NAME = "com.xikang.cpsclient.NotificationService";

	private TelephonyManager telephonyManager;

	private BroadcastReceiver connectivityReceiver;

	private PhoneStateListener phoneStateListener;

	private ExecutorService executorService;

	private TaskSubmitter taskSubmitter;

	private XmppManager xmppManager;

	private String deviceId;

	public class LocalBinder extends Binder {
		public NotificationService getService() {
			return NotificationService.this;
		}
	}

	private final IBinder mBinder = new LocalBinder();

	public void bindUser(String appId, String casToken) {
System.out.println("******************bindUserbindUserbindUser*********");		
		xmppManager.bindUser(appId, casToken);
	}

	public void unbindUser(String appId) {
		xmppManager.unBindUser(appId);
	}

	public NotificationService() {
		connectivityReceiver = new ConnectivityReceiver(this);
		phoneStateListener = new PhoneStateChangeListener(this);
		executorService = Executors.newSingleThreadExecutor();
		taskSubmitter = new TaskSubmitter(this);
	}

	@Override
	public void onCreate() {
		deviceId = Util.getDeviceId(this);
		telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		// If running on an emulator
		if (deviceId == null || deviceId.trim().length() == 0
				|| deviceId.matches("0+")) {
			deviceId = (new StringBuilder("EMU")).append(
					(new Random(System.currentTimeMillis())).nextLong())
					.toString();
		}

		xmppManager = new XmppManager(this);
		Constants.xmppManager = xmppManager;

		taskSubmitter.submit(new Runnable() {
			public void run() {
				NotificationService.this.start();
			}
		});
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(LOGTAG, "onStartCommand()...");
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		Log.d(LOGTAG, "onDestroy()...");
		stop();
	}

	@Override
	public IBinder onBind(Intent intent) {
		Log.d(LOGTAG, "onBind()...");
		return mBinder;
	}

	@Override
	public void onRebind(Intent intent) {
		Log.d(LOGTAG, "onRebind()...");
	}

//	@Override
//	public boolean onUnbind(Intent intent) {
//		Log.d(LOGTAG, "onUnbind()...");
//		return true;
//	}

	public static Intent getIntent() {
		return new Intent(SERVICE_NAME);
	}

	public ExecutorService getExecutorService() {
		return executorService;
	}

	public TaskSubmitter getTaskSubmitter() {
		return taskSubmitter;
	}

	public XmppManager getXmppManager() {
		return xmppManager;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void connect() {
		Log.d(LOGTAG, "connect()...");
		taskSubmitter.submit(new Runnable() {
			public void run() {
				NotificationService.this.getXmppManager().connect();
			}
		});
	}

	public void disconnect() {
		Log.d(LOGTAG, "disconnect()...");
		taskSubmitter.submit(new Runnable() {
			public void run() {
				NotificationService.this.getXmppManager().disconnect();
			}
		});
	}

	private void start() {
		telephonyManager.listen(phoneStateListener,
				PhoneStateListener.LISTEN_DATA_CONNECTION_STATE);

		IntentFilter filter = new IntentFilter();
		filter = new IntentFilter();
		filter.addAction(android.net.ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(connectivityReceiver, filter);

		xmppManager.connect();
	}

	private void stop() {
		Log.e(LOGTAG,"ffffffffffffffffffffffffffffffffffffffffff");
		telephonyManager.listen(phoneStateListener,
				PhoneStateListener.LISTEN_NONE);
		unregisterReceiver(connectivityReceiver);

		xmppManager.disconnect();
		executorService.shutdown();
		executorService = null ;
	}

	/**
	 * Class for submitting a new runnable task.
	 */
	public class TaskSubmitter {

		final NotificationService notificationService;

		public TaskSubmitter(NotificationService notificationService) {
			this.notificationService = notificationService;
		}

		public Future<?> submit(Runnable task) {
			Log.e(LOGTAG,"submit a task to run");
			Future<?> result = null;
			if (!notificationService.getExecutorService().isTerminated()
					&& !notificationService.getExecutorService().isShutdown()
					&& task != null) {
				Log.e(LOGTAG,"rrrrrrrrrrrrrrrrrrrrrrrrrrr");
				result = notificationService.getExecutorService().submit(task);
				notificationService.getExecutorService().execute(task);
			}else{
				Log.e(LOGTAG,"nnnnnnnnnnnnnnnnnnnnnnnnnnnnnn");
			}
			return result;
		}
	}
}
