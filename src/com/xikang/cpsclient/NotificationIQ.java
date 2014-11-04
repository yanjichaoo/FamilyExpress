package com.xikang.cpsclient;

import org.jivesoftware.smack.packet.IQ;

/**
 * This class represents a notification IQ packet.
 */
public class NotificationIQ extends IQ {

	private String id;

	private String no;

	private String phrCode;

	private String appId;

	private String summary;

	private String body;
	private String timestamp;

	public NotificationIQ() {
	}

	@Override
	public String getChildElementXML() {
		StringBuilder buf = new StringBuilder();
		buf.append("<").append("notification").append(" xmlns=\"")
				.append("xkcpns:iq:notification").append("\">");
		if (id != null) {
			buf.append("<id>").append(id).append("</id>");
		}
		buf.append("</").append("notification").append("> ");
		return buf.toString();
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPhrCode() {
		return phrCode;
	}

	public void setPhrCode(String phrCode) {
		this.phrCode = phrCode;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

}
