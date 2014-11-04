/* ==================================================
 * 产品名: 亲情快递
 * 文件名: Constants.java
 * --------------------------------------------------
 * 开发环境: JDK1.6
 * --------------------------------------------------
 * 修订履历    2012/07/07  1.00  初版发行
 * --------------------------------------------------
 * (C) Copyright XIKANG CORPORATION 2012 All Rights Reserved.
 */
package com.xikang.family.common;

import com.xikang.cpsclient.XmppManager;

/**
 * 
 *  全局常量类
 * 
 * 
 * 
 * @author 闫继超 张荣
 * @version 1.00
 */

public class Constants {

	public static XmppManager xmppManager;
	
	public static final String H = "SHA-256";
	public static final String CCS1 = "d7dc3f7f4856ee0adc5391dc8e380af13bf7dbb86fafb512a10e72c746d3149a";
	public static final String CCS2 = "736a8abda54f48f9ea8fd97f7ce2c698ed691a971dda3032727181359e6568d942d32a7b6555173d151d795adaa31b05ff29a6ee1656985afc2513df93551c71";
	public static final int CTTL = 86400;
	public static final int NTID = 666;
	public static final String SHARED_PREFERENCE_NAME = "family_express";
	public static final String TERMTYPE = "1001";
	public static final String XMPPHOST = "dlpush.xikang.com";
//	public static final String XMPPHOST = "59.46.63.130";
	public static final int XMPPPORT = 5222;
	public static final String APPKEY = "xikangcps";
	public static final String KEY = "xikang";
	public static final String VERSION = "0.0.1";
	public static boolean IS_EXPRESS_SERVICE_RUN = false;
	public static boolean IS_CPS_SERVICE_RUN = false;
	public static boolean isBound = false;

	// PREFERENCE KEYS
	public static final String SETTINGS_NOTIFICATION_ENABLED = "SETTINGS_NOTIFICATION_ENABLED";
	public static final String SETTINGS_SOUND_ENABLED = "SETTINGS_SOUND_ENABLED";
	public static final String SETTINGS_VIBRATE_ENABLED = "SETTINGS_VIBRATE_ENABLED";
	public static final String SETTINGS_TOAST_ENABLED = "SETTINGS_TOAST_ENABLED";

	public static final String APPID = "f65a2b2a99194778a9cd2ec1799eea55";
	public static String APPVERSION = null;

	public static String DEVICEID = null;
	public static String PASSWORD = null;
	public static String USERID = null;
	public static String ACCOUNTNAME = null;
	public static String FIGUREURL = null;
	public static String CLIENTID = null;
	public static String CLIENTRANDOM = null;
	public static String ACCESSTOKEN = null;
	public static long CLIENTCOUNT = 0;
	public static String LOGINTIME = null;
	public static String AUTHTTL = null;
	public static String INITOKEN = null;
	public static String CASTOKEN = null;
	public static String RANDOMSTR = null;

	public static String ERRORMSG = null;
	
	// THRIFT接口URL
//	public static final String AUTH_HTTP = "http://www.xikang365.net/base/rpc/thrift/auth-service.copa";
//	public static final String AUTH_HTTPS = "https://www.xikang365.net:1936/base/rpc/thrift/auth-service.copa";
//	public static final String ACCOUNT_HTTP = "http://www.xikang365.net/base/rpc/thrift/account-service.copa";
//	public static final String ACCOUNT_HTTPS = "https://www.xikang365.net:1936/base/rpc/thrift/account-service.copa";
//	public static final String APP_HTTP = "http://www.xikang365.net/base/rpc/thrift/app-service.copa";
//	public static final String APP_HTTPS = "https://www.xikang365.net:1936/base/rpc/thrift/app-service.copa";
//	public static final String FAMILY_HTTP = "http://www.xikang365.net/familyexpress/rpc/thrift/family-service.copa";
//	public static final String FAMILY_HTTPS = "https://www.xikang365.net:1936/familyexpress/rpc/thrift/family-service.copa";
//	public static final String USER_HTTP = "http://www.xikang365.net/base/rpc/thrift/user-service.copa";
//	public static final String USER_HTTPS = "https://www.xikang365.net:1936/base/rpc/thrift/user-service.copa";
//	public static final String EXPRESS_HTTP = "http://www.xikang365.net/familyexpress/rpc/thrift/express-service.copa";
//	public static final String EXPRESS_HTTPS = "https://www.xikang365.net:1936/familyexpress/rpc/thrift/express-service.copa";

	// For Test3
	public static final String AUTH_HTTP = "http://dli.xikang.com/base/rpc/thrift/auth-service.copa";
	public static final String AUTH_HTTPS = "https://dli.xikang.com/base/rpc/thrift/auth-service.copa";
	public static final String ACCOUNT_HTTP = "http://dli.xikang.com/base/rpc/thrift/account-service.copa";
	public static final String ACCOUNT_HTTPS = "https://dli.xikang.com/base/rpc/thrift/account-service.copa";
	public static final String APP_HTTP = "http://dli.xikang.com/base/rpc/thrift/app-service.copa";
	public static final String APP_HTTPS = "https://dli.xikang.com/base/rpc/thrift/app-service.copa";
	public static final String FAMILY_HTTP = "http://dli.xikang.com/familyexpress/rpc/thrift/family-service.copa";
	public static final String FAMILY_HTTPS = "https://dli.xikang.com/familyexpress/rpc/thrift/family-service.copa";
	public static final String USER_HTTP = "http://dli.xikang.com/base/rpc/thrift/user-service.copa";
	public static final String USER_HTTPS = "https://dli.xikang.com/base/rpc/thrift/user-service.copa";
	public static final String EXPRESS_HTTP = "http://dli.xikang.com/familyexpress/rpc/thrift/express-service.copa";
	public static final String EXPRESS_HTTPS = "https://dli.xikang.com/familyexpress/rpc/thrift/express-service.copa";

	// PREFERENCE KEYS
	public static final String IS_SDCARD_REMIND = "IS_SDCARD_REMIND";
	public static final String HOLD_TERM = "HOLD_TERM";

	public static final String LOGIN_TIME = "LOGIN_TIME";
	public static final String AUTH_TTL = "AUTH_TTL";
	public static final String USER_ID = "USER_ID";
	public static final String ACCOUNT_NAME = "ACCOUNT_NAME";
	public static final String PASS_WORD = "PASS_WORD";
	public static final String FIGURE_URL = "FIGURE_URL";
	public static final String CLIENT_ID = "CLIENT_ID";
	public static final String CLIENT_COUNT = "CLIENT_COUNT";
	public static final String INI_TOKEN = "INI_TOKEN";

	public static final String LAST_OBTAIN_TIME = "LAST_OBTAIN_TIME";
	public static final String LAST_UPDATE_TIME = "LAST_UPDATE_TIME";

	// NOTIFICATION FIELDS
	public static final String NOTIFICATION_ID = "NOTIFICATION_ID";
	public static final String NOTIFICATION_NO = "NOTIFICATION_NO";
	public static final String NOTIFICATION_PHRCODE = "NOTIFICATION_PHRCODE";
	public static final String NOTIFICATION_APPID = "NOTIFICATION_APPID";
	public static final String NOTIFICATION_SUMMARY = "NOTIFICATION_SUMMARY";
	public static final String NOTIFICATION_BODY = "NOTIFICATION_BODY";
	public static final String NOTIFICATION_TIMESTAMP = "NOTIFICATION_TIMESTAMP";
	public static final String EXPRESS_ID_LIST = "EXPRESS_ID_LIST";
	public static final String EXPRESS_STATUS_LIST = "EXPRESS_STATUS_LIST";
	public static final String EXPRESS_USERID_LIST = "EXPRESS_USERID_LIST";

	// INTENT ACTIONS
	public static final String ACTION_APP_NOTIFICATION = "com.xikang.cps.app.";
	public static final String BIND_RESULT_NOTIFICATION = "com.xikang.bind.result";
	public static final String BIND_ERROR_NOTIFICATION = "com.xikang.bind.error";
	public static final String UNBIND_RESULT_NOTIFICATION = "com.xikang.unbind.result";
	public static final String UNBIND_ERROR_NOTIFICATION = "com.xikang.unbind.error";
	public static final String SEND_EXPRESS_INTENT = "com.xikang.express.send";

	public static final String ACTION_EXPRESS_STATUS_UPDATEED = "com.xikang.express.status.update";
	public static final String ACTION_EXPRESS_NEW = "com.xikang.express.new";

	public static final String TEMPFILE = "temp.wav";
	public static final String SPXFILE = "family.spx";
	public static final String WAVFILE = "family.wav";

	public static final String AUDIO = "AUDIO";
	public static final String IMAGE = "IMAGE";
	public static final String VIDEO = "VIDEO";
	public static final String BINARY = "BINARY";
	public static final String TEXT = "TEXT";

	public static final String SPX = ".spx";
	public static final String OGG = ".ogg";
	public static final String MP3 = ".mp3";
	public static final String PNG = ".png";
	public static final String JPG = ".jpg";
	public static final String GIF = ".gif";
	public static final String TGP = ".3gp";

	public static final String SPX_UP = "SPX";
	public static final String OGG_UP = "OGG";
	public static final String MP3_UP = "MP3";
	public static final String PNG_UP = "PNG";
	public static final String JPG_UP = "JPG";
	public static final String GIF_UP = "GIF";
	public static final String TGP_UP = "3GP";

	public static final String SPEEX = "SPEEX";

	public static class FEContentType {
		public static int TEXT = 1; // 文本
		public static int AUDIO = 2; // 音频
		public static int IMAGE = 3; // 图像
	}

	public static class ExpressType {
		public static int NORMAL = 0; // 普通快递
		public static int BLOODPRESSURE = 101; // 血压
		public static int BLOODSUGAR = 102; // 血糖
		public static int BLOODOXYGEN = 103; // 血氧
		public static int SPORT = 201; // 运动
	}

	public static class ExpressStatus {
		public static final int SENT = 0; // 已发
		public static final int FORWARDED = 1; // 送达
		public static final int UNREAD = 2; // 未读、未看、未听
		public static final int READ = 3; // 已读、已看、已听
		public static final int WAIT = 9; // 待发
	}

	public static class FEFormatType {
		public static int TEXT = 0; // 文本格式
		public static int SPX = 1; // 音频：spx
		public static int OGG = 2; // 音频：ogg
		public static int MP3 = 3; // 音频：mp3
		public static int PNG = 4; // 图像：png
		public static int JPG = 5; // 图像：jpg、jpeg
		public static int GIF = 6; // 图像：gif
		public static int _3GP = 7; // 视频：3gp
	}

}
