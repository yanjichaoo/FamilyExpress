package com.xikang.cpsclient;

import org.jivesoftware.smack.packet.IQ;

public class UserUnBinderIQ extends IQ{
	private String appId;

	public UserUnBinderIQ() {
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	@Override
	public String getChildElementXML() {
		StringBuilder buf = new StringBuilder();
		buf.append("<query xmlns=\"jabber:iq:unbinding\">");
	    buf.append("<appId>").append(appId).append("</appId>");
		buf.append("</query>");
		return buf.toString();
	}

}
