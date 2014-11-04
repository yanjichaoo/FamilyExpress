/* ==================================================
 * 产品名: 亲情快递
 * 文件名: FaceIconOpe.java
 * --------------------------------------------------
 * 开发环境: JDK1.6
 * --------------------------------------------------
 * 修订履历    2012/07/07  1.00  初版发行
 * --------------------------------------------------
 * (C) Copyright XIKANG CORPORATION 2012 All Rights Reserved.
 */

package com.xikang.family.common;

import java.util.HashMap;
import java.util.Map;

import android.text.format.Time;

import com.xikang.channel.familyexpress.rpc.thrift.express.FEFormatType;
import com.xikang.family.common.HttpClientUtil;

/**
 * 
 * 头像操作类
 * 
 * 
 * 
 * @author 闫继超
 * @version 1.00
 */

public class FaceIconOpe {

	private static final String HTTP_ADDRESS_ICON = "http://i.xikang.com/online/getFace.jpg";

	public static String getTimeStamp() {
		Time t = new Time();
		t.setToNow();
		String timeStamp = String.valueOf(t.year).concat("-")
				.concat(String.valueOf(t.month)).concat("-")
				.concat(String.valueOf(t.monthDay)).concat(" ")
				.concat(String.valueOf(t.hour)).concat(":")
				.concat(String.valueOf(t.minute)).concat(":")
				.concat(String.valueOf(t.second));

		return timeStamp;
	}

	public static String getFaceIcon(String userid) {
		try {
			Map<String, String> mapUrl = new HashMap<String, String>();
			mapUrl.put("personPhrCode", userid);
			String result = HttpClientUtil.requestPostStream(HTTP_ADDRESS_ICON,
					userid, mapUrl, FEFormatType.PNG);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String getFaceIconBig(String userid) {
		try {
			Map<String, String> mapUrl = new HashMap<String, String>();
			mapUrl.put("personPhrCode", userid);
			mapUrl.put("type", "1");
			String result = HttpClientUtil.requestPostStream(HTTP_ADDRESS_ICON,
					userid, mapUrl, FEFormatType.PNG);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
