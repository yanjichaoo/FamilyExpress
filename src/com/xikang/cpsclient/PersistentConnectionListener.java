package com.xikang.cpsclient;

import org.jivesoftware.smack.ConnectionListener;

import com.xikang.family.common.Util;

import android.util.Log;

/**
 * A listener class for monitoring connection closing and reconnection events.
 */
public class PersistentConnectionListener implements ConnectionListener {

	private static final String LOGTAG = Util
			.makeLogTag(PersistentConnectionListener.class);

	private final XmppManager xmppManager;

	public PersistentConnectionListener(XmppManager xmppManager) {
		this.xmppManager = xmppManager;
	}

	@Override
	public void connectionClosed() {
		Log.d(LOGTAG, "connectionClosed()...");
	}

	@Override
	public void connectionClosedOnError(Exception e) {
		Log.d(LOGTAG, "connectionClosedOnError()...");
		if (xmppManager.getConnection() != null
				&& xmppManager.getConnection().isConnected()) {
			xmppManager.getConnection().disconnect();
		}
		xmppManager.startReconnectionThread();
	}

	@Override
	public void reconnectingIn(int seconds) {
		Log.d(LOGTAG, "reconnectingIn()...");
	}

	@Override
	public void reconnectionFailed(Exception e) {
		Log.d(LOGTAG, "reconnectionFailed()...");
	}

	@Override
	public void reconnectionSuccessful() {
		Log.d(LOGTAG, "reconnectionSuccessful()...");
	}

}