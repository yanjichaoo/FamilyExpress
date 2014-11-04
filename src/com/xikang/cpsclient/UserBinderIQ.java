package com.xikang.cpsclient;

import org.jivesoftware.smack.packet.IQ;

public class UserBinderIQ extends IQ{

	private String appId;

	private String casToken;

	public UserBinderIQ() {
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getCasToken() {
		return casToken;
	}

	public void setCasToken(String casToken) {
		this.casToken = casToken;
	}

	@Override
	public String getChildElementXML() {
		StringBuilder buf = new StringBuilder();
		buf.append("<query xmlns=\"jabber:iq:binding\">");
		buf.append("<tgt>").append(casToken).append("</tgt>");
	    buf.append("<appId>").append(appId).append("</appId>");
		buf.append("</query>");
		return buf.toString();
	}
}
