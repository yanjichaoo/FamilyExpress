package com.xikang.cpsclient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.provider.ProviderManager;

import com.xikang.family.common.Constants;
import com.xikang.family.common.Util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;

/**
 * This class is to manage the XMPP connection between client and server.
 */
public class XmppManager {

	private static final String LOGTAG = Util.makeLogTag(XmppManager.class);

	private Context context;

	private NotificationService.TaskSubmitter taskSubmitter;

	private XMPPConnection connection;

	private String username;

	private String password;

	private ConnectionListener connectionListener;

	private PacketListener packetListener;

	private Handler handler;

	private List<Runnable> taskList;

	private boolean running = false;

	private Future<?> futureTask;

	private Thread reconnection;

	private SharedPreferences pref;

	public XmppManager(NotificationService notificationService) {
		context = notificationService;

		pref = notificationService.getSharedPreferences(
				Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);

		taskSubmitter = notificationService.getTaskSubmitter();

		username = Util.getDeviceId(notificationService);

		connectionListener = new PersistentConnectionListener(this);
		packetListener = new NotificationPacketListener(this);

		handler = new Handler();
		taskList = new ArrayList<Runnable>();
		reconnection = new ReconnectionThread(this);
	}

	public Context getContext() {
		return context;
	}

	public void connect() {
		Log.e(LOGTAG, "1111111111111111111111111111111  connect()...");
		submitLoginTask();
	}

	public void disconnect() {
		Log.d(LOGTAG, "disconnect()...");
		terminatePersistentConnection();
	}

	public void bindUser(String appId, String casToken) {
		Log.d(LOGTAG, "bindUser()...");
		submitBindUserTask(appId, casToken);
	}

	public void unBindUser(String appId) {
		Log.d(LOGTAG, "unBindUser()...");
		submitUnBindUserTask(appId);
	}

	public void terminatePersistentConnection() {
		Log.e(LOGTAG, "terminatePersistentConnection()...");
		Runnable runnable = new Runnable() {

			final XmppManager xmppManager = XmppManager.this;

			public void run() {
				Log.e(LOGTAG, "terminate run run run");
				if (xmppManager.isConnected()) {
					Log.e(LOGTAG, "terminatePersistentConnection()... run()");
					xmppManager.getConnection().removePacketListener(
							xmppManager.getPacketListener());
					xmppManager.getConnection().disconnect();
				}
				xmppManager.runTask();
			}

		};
		addTask(runnable);
	}

	public XMPPConnection getConnection() {
		return connection;
	}

	public void setConnection(XMPPConnection connection) {
		this.connection = connection;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public ConnectionListener getConnectionListener() {
		return connectionListener;
	}

	public PacketListener getPacketListener() {
		return packetListener;
	}

	public void startReconnectionThread() {
		Log.e(LOGTAG, "startReconnectionThread");
		synchronized (reconnection) {
			Log.e(LOGTAG, "aaaaaaaaaaaaa");
			if (!reconnection.isAlive()) {
				Log.e(LOGTAG, "in Reconnection Thread");
				reconnection.setName("Xmpp Reconnection Thread");
				reconnection.start();
			}
		}
	}

	public Handler getHandler() {
		return handler;
	}

	public void reregisterAccount() {
		Log.e(LOGTAG, "reregisterAccount");
		submitLoginTask();
		runTask();
	}

	public List<Runnable> getTaskList() {
		return taskList;
	}

	public Future<?> getFutureTask() {
		return futureTask;
	}

	public void runTask() {
		Log.e(LOGTAG, "runTask()...");
		synchronized (taskList) {
			Log.e(LOGTAG, "in tasklist runtask");
			running = false;
			futureTask = null;
			if (!taskList.isEmpty()) {
				Log.e(LOGTAG, "tasklist select one task to run");
				Runnable runnable = (Runnable) taskList.get(0);
				taskList.remove(0);
				running = true;
				futureTask = taskSubmitter.submit(runnable);
			} else {
				Log.e(LOGTAG, "tasklist empty empty empty");
			}
		}
		Log.d(LOGTAG, "runTask()...done");
	}

	public boolean isConnected() {
		return connection != null && connection.isConnected();
	}

	private boolean isAuthenticated() {
		return connection != null && connection.isConnected()
				&& connection.isAuthenticated();
	}

	private boolean isBound(String appId) {
		return pref.contains(appId + "Bound");
	}

	private void submitConnectTask() {
		Log.d(LOGTAG, "submitConnectTask()...");
		addTask(new ConnectTask());
	}

	private void submitBindUserTask(String appId, String casToken) {
		Log.d(LOGTAG, "submitBindUserTask()..." + "#" + appId + "#" + casToken);
		addTask(new BindUserTask(appId, casToken));
		// XmppManager.this.runTask();
	}

	private void submitUnBindUserTask(String appId) {
		Log.d(LOGTAG, "submitUnBindUserTask()..." + "#" + appId);
		addTask(new UnBindUserTask(appId));
	}

	private void submitLoginTask() {
		Log.d(LOGTAG, "submitLoginTask()...");
		submitConnectTask();
		addTask(new LoginTask());
	}

	private void addTask(Runnable runnable) {
		Log.d(LOGTAG, "addTask(runnable)...");
		synchronized (taskList) {
			Log.e(LOGTAG, "in addTask");
			if (taskList.isEmpty() && !running) {
				Log.e(LOGTAG, "tasklist empty and run right now");
				running = true;
				futureTask = taskSubmitter.submit(runnable);

			} else {
				Log.e(LOGTAG, "not empty not empty or is running");
				taskList.add(runnable);
				// taskSubmitter.submit(runnable);
			}
		}
		Log.d(LOGTAG, "addTask(runnable)... done");
	}

	/**
	 * A runnable task to connect the server.
	 */
	private class ConnectTask implements Runnable {

		final XmppManager xmppManager;

		private ConnectTask() {
			this.xmppManager = XmppManager.this;
		}

		public void run() {
			Log.e(LOGTAG,
					"**************ConnectTask.run()***********************");

			if (!xmppManager.isConnected()) {
				Log.e(LOGTAG, "1111111111111111111  XMPP not connected");
				// Create the configuration for this new connection
				ConnectionConfiguration connConfig = new ConnectionConfiguration(
						Constants.XMPPHOST, Constants.XMPPPORT);
				// connConfig.setSecurityMode(SecurityMode.disabled);
				connConfig.setSecurityMode(SecurityMode.required);
				connConfig.setSASLAuthenticationEnabled(false);
				connConfig.setCompressionEnabled(false);
				connConfig.setDebuggerEnabled(true);

				XMPPConnection connection = new XMPPConnection(connConfig);
				xmppManager.setConnection(connection);

				try {
					Log.e(LOGTAG, "try connect to server");
					// Connect to the server
					connection.connect();
					Log.e(LOGTAG,
							"11111111111111111111 XMPP connected successfully");

					// packet provider
					ProviderManager.getInstance().addIQProvider("notification",
							"xkcpns:iq:notification",
							new NotificationIQProvider());

				} catch (XMPPException e) {
					Log.e(LOGTAG, "11111111111111111  XMPP connection failed",
							e);
				}

				xmppManager.runTask();

			} else {
				Log.i(LOGTAG, "1111111111111111111111 XMPP connected already");
				xmppManager.runTask();
			}
		}
	}

	/**
	 * A runnable task to log into the server.
	 */
	private class LoginTask implements Runnable {

		final XmppManager xmppManager;

		private LoginTask() {
			this.xmppManager = XmppManager.this;
		}

		public void run() {
			Log.e(LOGTAG, "LoginTask.run()...");
			if (!xmppManager.isAuthenticated()) {
				Log.d(LOGTAG, "username=" + username);
				Log.d(LOGTAG, "password=" + password);
				String degist = Util.getDeviceMD5PWD(context);
				try {
					xmppManager.getConnection().login(
							xmppManager.getUsername(), degist,
							Constants.TERMTYPE);
					Log.d(LOGTAG, "Loggedn in successfully");

					// connection listener
					if (xmppManager.getConnectionListener() != null) {
						xmppManager.getConnection().addConnectionListener(
								xmppManager.getConnectionListener());
					}

					// packet filter
					PacketFilter packetFilter = new PacketTypeFilter(
							NotificationIQ.class);
					// packet listener
					PacketListener packetListener = xmppManager
							.getPacketListener();
					connection.addPacketListener(packetListener, packetFilter);

					xmppManager.runTask();

				} catch (XMPPException e) {
					Log.e(LOGTAG, "LoginTask.run()... xmpp error");
					Log.e(LOGTAG, "Failed to login to xmpp server. Caused by: "
							+ e.getMessage());
					String INVALID_CREDENTIALS_ERROR_CODE = "401";
					String errorMessage = e.getMessage();
					if (errorMessage != null
							&& errorMessage
									.contains(INVALID_CREDENTIALS_ERROR_CODE)) {
						Log.e(LOGTAG, "invalid credentials error");
						xmppManager.reregisterAccount();
						return;
					}
					xmppManager.startReconnectionThread();

				} catch (Exception e) {
					Log.e(LOGTAG, "LoginTask.run()... other error");
					Log.e(LOGTAG, "Failed to login to xmpp server. Caused by: "
							+ e.getMessage());
					xmppManager.startReconnectionThread();
				}

			} else {
				Log.i(LOGTAG, "Logged in already");
				xmppManager.runTask();
			}

		}
	}

	private class BindUserTask implements Runnable {

		final XmppManager xmppManager;
		final String mAppId;
		final String mCasToken;

		private BindUserTask(String appId, String casToken) {
			xmppManager = XmppManager.this;
			mAppId = appId;
			mCasToken = casToken;
		}

		public void run() {
			// if (!xmppManager.isBound(mAppId, mCasToken)) {
			UserBinderIQ userBinderIQ = new UserBinderIQ();
			userBinderIQ.setAppId(mAppId);
			userBinderIQ.setCasToken(mCasToken);

			PacketFilter packetFilter = new AndFilter(new PacketIDFilter(
					userBinderIQ.getPacketID()), new PacketTypeFilter(IQ.class));

			PacketListener packetListener = new PacketListener() {

				public void processPacket(Packet packet) {

					if (packet instanceof IQ) {
						IQ response = (IQ) packet;
						if (response.getType() == IQ.Type.ERROR) {
							Constants.isBound = false;
							if (response.getError().toString().contains("404")) {
								Log.e(LOGTAG, "CasToken invalid! "
										+ response.getError().getCondition());
							} else if (response.getError().toString()
									.contains("406")) {
								Log.e(LOGTAG, "appId invalid! "
										+ response.getError().getCondition());
							} else if (response.getError().toString()
									.contains("500")) {
								Log.e(LOGTAG, "server internal error! "
										+ response.getError().getCondition());
							} else {
								Log.e(LOGTAG, "unknown error! "
										+ response.getError().getCondition());

							}
							Intent intent = new Intent(
									Constants.BIND_ERROR_NOTIFICATION);
							xmppManager.getContext().sendBroadcast(intent);
						} else if (response.getType() == IQ.Type.RESULT) {
							SharedPreferences.Editor editor = pref.edit();
							editor.putString(mAppId + "Bound", mCasToken);
							editor.commit();
							Log.e(LOGTAG, "Bound Success! ");
							Constants.isBound = true;
							Intent intent = new Intent(
									Constants.BIND_RESULT_NOTIFICATION);
							xmppManager.getContext().sendBroadcast(intent);
							xmppManager.runTask();
						}
					}
				}
			};

			connection.addPacketListener(packetListener, packetFilter);

			userBinderIQ.setType(IQ.Type.SET);
			connection.sendPacket(userBinderIQ);

			// } else {
			// Log.i(LOGTAG, "User bound already");
			// xmppManager.runTask();
			// }
		}
	}

	private class UnBindUserTask implements Runnable {

		final XmppManager xmppManager;
		final String mAppId;

		private UnBindUserTask(String appId) {
			xmppManager = XmppManager.this;
			mAppId = appId;
		}

		public void run() {
			// if (xmppManager.isBound(mAppId)) {
			Log.i(LOGTAG, "User  Unbound.");
			UserUnBinderIQ userUnBinderIQ = new UserUnBinderIQ();
			userUnBinderIQ.setAppId(mAppId);

			PacketFilter packetFilter = new AndFilter(new PacketIDFilter(
					userUnBinderIQ.getPacketID()), new PacketTypeFilter(
					IQ.class));

			PacketListener packetListener = new PacketListener() {

				public void processPacket(Packet packet) {
					Log.i("ufo", packet.toXML());

					if (packet instanceof IQ) {
						IQ response = (IQ) packet;
						if (response.getType() == IQ.Type.ERROR) {
							if (response.getError().toString().contains("406")) {
								Log.e(LOGTAG, "appId invalid! "
										+ response.getError().getCondition());
							} else if (response.getError().toString()
									.contains("500")) {
								Log.e(LOGTAG, "server internal error! "
										+ response.getError().getCondition());
							} else {
								Log.e(LOGTAG, "unknown error! "
										+ response.getError().getCondition());

							}
							Intent intent = new Intent(
									Constants.UNBIND_ERROR_NOTIFICATION);
							xmppManager.getContext().sendBroadcast(intent);
						} else if (response.getType() == IQ.Type.RESULT) {

							Log.e("YANJCH", "RESULT");
							SharedPreferences.Editor editor = pref.edit();
							editor.remove(mAppId + "Bound");
							editor.commit();
							Intent intent = new Intent(
									Constants.UNBIND_RESULT_NOTIFICATION);
							xmppManager.getContext().sendBroadcast(intent);
							xmppManager.runTask();
						}
					}
				}
			};

			connection.addPacketListener(packetListener, packetFilter);

			userUnBinderIQ.setType(IQ.Type.SET);
			Log.i(LOGTAG,
					"***************sendPacket(userUnBinderIQ)***************");
			connection.sendPacket(userUnBinderIQ);

			// } else {
			// Log.i(LOGTAG, "User not bound.");
			// xmppManager.runTask();
			// }
		}
	}

}
