package com.xikang.cpsclient;

import com.xikang.family.common.Util;

import android.util.Log;

/**
 * A thread class for reconnecting the server.
 */
public class ReconnectionThread extends Thread {

	private static final String LOGTAG = Util
			.makeLogTag(ReconnectionThread.class);

	private final XmppManager xmppManager;

	private int waiting;

	ReconnectionThread(XmppManager xmppManager) {
		this.xmppManager = xmppManager;
		this.waiting = 0;
	}

	public void run() {
		try {
			while (!isInterrupted()) {
				Log.e(LOGTAG, "Trying to reconnect in " + waiting() + " seconds");
				Thread.sleep((long) waiting() * 1000L);
				xmppManager.connect();
				waiting++;
			}
		} catch (final InterruptedException e) {
			xmppManager.getHandler().post(new Runnable() {
				public void run() {
					xmppManager.getConnectionListener().reconnectionFailed(e);
				}
			});
		}
	}

	private int waiting() {
		int seconds = 0;
		if (waiting < 1) {
			seconds = (int) (Math.random() * 2);
		}
		if (waiting >= 1 && waiting < 10) {
			seconds = (int) Math.pow(2, waiting + 1);
		}
		if (waiting >= 10) {
			seconds = (int) Math.pow(2, 10);
		}
		return seconds;
	}
}
