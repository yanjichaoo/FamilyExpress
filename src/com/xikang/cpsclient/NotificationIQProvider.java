package com.xikang.cpsclient;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

/**
 * This class parses incoming IQ packets to NotificationIQ objects.
 */
public class NotificationIQProvider implements IQProvider {

	public NotificationIQProvider() {
	}

	@Override
	public IQ parseIQ(XmlPullParser parser) throws Exception {

		NotificationIQ notification = new NotificationIQ();
		for (boolean done = false; !done;) {
			int eventType = parser.next();
			if (eventType == XmlPullParser.START_TAG) {
				if ("id".equals(parser.getName())) {
					notification.setId(parser.nextText());
				}
				if ("no".equals(parser.getName())) {
					notification.setNo(parser.nextText());
				}
				if ("phrCode".equals(parser.getName())) {
					notification.setPhrCode(parser.nextText());
				}
				if ("appId".equals(parser.getName())) {
					notification.setAppId(parser.nextText());
				}
				if ("summary".equals(parser.getName())) {
					notification.setSummary(parser.nextText());
				}
				if ("body".equals(parser.getName())) {
					notification.setBody(parser.nextText());
				}
				if ("timestamp".equals(parser.getName())) {
					notification.setTimestamp(parser.nextText());
				}
			} else if (eventType == XmlPullParser.END_TAG
					&& "notification".equals(parser.getName())) {
				done = true;
			}
		}

		return notification;
	}

}
