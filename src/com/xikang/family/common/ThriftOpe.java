/* ==================================================
 * 产品名: 亲情快递
 * 文件名: ThriftOpe.java
 * --------------------------------------------------
 * 开发环境: JDK1.6
 * --------------------------------------------------
 * 修订履历    2012/07/07  1.00  初版发行
 * --------------------------------------------------
 * (C) Copyright XIKANG CORPORATION 2012 All Rights Reserved.
 */

package com.xikang.family.common;

import java.nio.ByteBuffer;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.http.client.HttpClient;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.THttpClient;
import org.apache.thrift.transport.TTransport;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.xikang.channel.base.rpc.thrift.account.AccountInfo;
import com.xikang.channel.base.rpc.thrift.account.AccountService;
import com.xikang.channel.base.rpc.thrift.app.AppService;
import com.xikang.channel.base.rpc.thrift.app.VersionInfo;
import com.xikang.channel.base.rpc.thrift.auth.AuthService;
import com.xikang.channel.base.rpc.thrift.auth.LoginResult;
import com.xikang.channel.base.rpc.thrift.user.UserInfo;
import com.xikang.channel.base.rpc.thrift.user.UserService;
import com.xikang.channel.common.rpc.thrift.message.AppInfo;
import com.xikang.channel.common.rpc.thrift.message.AuthMode;
import com.xikang.channel.common.rpc.thrift.message.BizException;
import com.xikang.channel.common.rpc.thrift.message.CommArgs;
import com.xikang.channel.common.rpc.thrift.message.DeviceType;
import com.xikang.channel.common.rpc.thrift.message.DigestAuthenticationReq;
import com.xikang.channel.common.rpc.thrift.message.I18nInfo;
import com.xikang.channel.common.rpc.thrift.message.Language;
import com.xikang.channel.common.rpc.thrift.message.Region;
import com.xikang.channel.common.rpc.thrift.message.TerminalInfo;
import com.xikang.channel.familyexpress.rpc.thrift.express.AppInitInfo;
import com.xikang.channel.familyexpress.rpc.thrift.express.ExpressChangedStatusAll;
import com.xikang.channel.familyexpress.rpc.thrift.express.ExpressInfo;
import com.xikang.channel.familyexpress.rpc.thrift.express.ExpressInfoAll;
import com.xikang.channel.familyexpress.rpc.thrift.express.ExpressService;
import com.xikang.channel.familyexpress.rpc.thrift.family.FamilyService;
import com.xikang.channel.familyexpress.rpc.thrift.family.Gender;
import com.xikang.channel.familyexpress.rpc.thrift.family.MemberInfo;
import com.xikang.channel.familyexpress.rpc.thrift.family.MemberRelation;
import com.xikang.channel.familyexpress.rpc.thrift.family.UserBaseInfo;

/**
 * 
 * Thrift工具类
 * 
 * 
 * 
 * @author 张荣
 * @version 1.00
 */

public class ThriftOpe {

	public static void firstUse(Context context) {
		
		SharedPreferences pref = context.getSharedPreferences(
				Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();

		TerminalInfo terminalInfo = new TerminalInfo(DeviceType.ANDROID,
				Constants.DEVICEID, android.os.Build.VERSION.RELEASE);
		AppInfo appInfo = new AppInfo(Constants.APPID, Constants.APPVERSION);
		I18nInfo i18nInfo = new I18nInfo(Region.CN, Language.ZH_CN);
		

		Log.e("fffffffffff", "call first use");
		Constants.RANDOMSTR = Util.getRandomString(100);
		Constants.CLIENTCOUNT = Constants.CLIENTCOUNT + 1;
		DigestAuthenticationReq authReqFirst = new DigestAuthenticationReq(
				Constants.CLIENTID, Constants.CLIENTCOUNT, Constants.RANDOMSTR,
				null);
		Constants.ACCESSTOKEN = Util.calAccessToken(context, authReqFirst,
				Constants.INITOKEN);
		authReqFirst = new DigestAuthenticationReq(Constants.CLIENTID,
				Constants.CLIENTCOUNT, Constants.RANDOMSTR,
				Constants.ACCESSTOKEN);
		CommArgs commArgsFirst = new CommArgs(terminalInfo, appInfo,
				Constants.USERID, i18nInfo, AuthMode.DIGEST, authReqFirst);

		try {
			HttpClient httpClient = HttpClientUtil.getHttpClient();
			TTransport transport = new THttpClient(Constants.EXPRESS_HTTP,
					httpClient);
			transport.open();
			TProtocol protocol = new TCompactProtocol(transport);
			ExpressService.Client client = new ExpressService.Client(protocol);
			Log.e("ffffffffffffffffffff",
					"call firstlyUseExpress(CommArgs commArgs) ");

			AppInitInfo appInitInfo = client.firstlyUseExpress(commArgsFirst);
			editor.putString(Constants.USERID, appInitInfo.getGuideAsstId());
			transport.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		editor.putLong(Constants.CLIENT_COUNT, Constants.CLIENTCOUNT);
		editor.putString(Constants.INI_TOKEN, Constants.INITOKEN);
		editor.commit();

	}

	/**
	 * 登录
	 */
	public static String login(Context context, String accountname,
			String password) {
		DigestAuthenticationReq authReq = null;
		SharedPreferences pref = context.getSharedPreferences(
				Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);

		Constants.RANDOMSTR = Util.getRandomString(100);
		if (Constants.ACCOUNTNAME != null && Constants.PASSWORD != null
				&& Constants.LOGINTIME != null && Constants.AUTHTTL != null
				&& Constants.ACCOUNTNAME.equals(accountname)
				&& Constants.PASSWORD.equals(password)) {

			Date lastLoginDate = Util.addDate(
					Util.parseDateS(Constants.LOGINTIME),
					Long.parseLong(Constants.AUTHTTL) * 1000);
			if (Util.parseDateS(Util.getCurrentDate()).before(lastLoginDate)) {
				Constants.RANDOMSTR = Util.getRandomString(100);
				Constants.CLIENTCOUNT = Util.addCientCount(context);
				Constants.CLIENTID = pref.getString(Constants.CLIENT_ID, null);
				Constants.USERID = pref.getString(Constants.USER_ID, null);
				Constants.FIGUREURL = pref
						.getString(Constants.FIGURE_URL, null);
				Constants.INITOKEN = pref.getString(Constants.INI_TOKEN, null);
				authReq = new DigestAuthenticationReq(Constants.CLIENTID,
						Constants.CLIENTCOUNT, Constants.RANDOMSTR, null);
				Constants.ACCESSTOKEN = Util.calAccessToken(context, authReq,
						Constants.INITOKEN);

				return Constants.ACCESSTOKEN;
			}
		}

		// Thrift Login
		TerminalInfo terminalInfo = new TerminalInfo(DeviceType.ANDROID,
				Constants.DEVICEID, android.os.Build.VERSION.RELEASE);
		AppInfo appInfo = new AppInfo(Constants.APPID, Constants.APPVERSION);
		I18nInfo i18nInfo = new I18nInfo(Region.CN, Language.ZH_CN);
		CommArgs commArgs = new CommArgs(terminalInfo, appInfo, null, i18nInfo,
				AuthMode.NONE, null);
		LoginResult result = null;

		try {
			HttpClient httpClient = HttpClientUtil.getHttpClient();
			TTransport transport = new THttpClient(Constants.AUTH_HTTPS,
					httpClient);
			transport.open();
			TProtocol protocol = new TCompactProtocol(transport);
			AuthService.Client client = new AuthService.Client(protocol);

			result = client.login(commArgs, accountname, password, null);
			transport.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		Constants.AUTHTTL = String.valueOf(result.getDigestAuthorizationRes()
				.getAuthTtl());
		Constants.LOGINTIME = Util.getCurrentDate();
		Constants.INITOKEN = result.getDigestAuthorizationRes()
				.getInitialToken();
		Constants.CLIENTID = result.getDigestAuthorizationRes().getClientId();
		Constants.CLIENTCOUNT = result.getDigestAuthorizationRes()
				.getInitialCount() + 1;
		authReq = new DigestAuthenticationReq(Constants.CLIENTID,
				Constants.CLIENTCOUNT, Constants.RANDOMSTR, null);

		Constants.USERID = result.getAuthUserInfo().getUserId();
		System.out.println("***********************Constants.USERID="
				+ Constants.USERID);
		Constants.ACCOUNTNAME = accountname;
		Constants.PASSWORD = password;
		Constants.FIGUREURL = result.getAuthUserInfo().getFigureUrl();
		Constants.CASTOKEN = result.getLoginResultExtInfo().getCasTgt();
		Constants.ACCESSTOKEN = Util.calAccessToken(context, authReq,
				Constants.INITOKEN);

		SharedPreferences.Editor editor = pref.edit();

		/*if (pref.getString(Constants.USERID, null) == null && Constants.isBound) {
			Log.e("fffffffffff", "call bound first use");
			Constants.RANDOMSTR = Util.getRandomString(100);
			Constants.CLIENTCOUNT = Constants.CLIENTCOUNT + 1;
			DigestAuthenticationReq authReqFirst = new DigestAuthenticationReq(
					Constants.CLIENTID, Constants.CLIENTCOUNT,
					Constants.RANDOMSTR, null);
			Constants.ACCESSTOKEN = Util.calAccessToken(context, authReqFirst,
					Constants.INITOKEN);
			authReqFirst = new DigestAuthenticationReq(Constants.CLIENTID,
					Constants.CLIENTCOUNT, Constants.RANDOMSTR,
					Constants.ACCESSTOKEN);
			CommArgs commArgsFirst = new CommArgs(terminalInfo, appInfo,
					Constants.USERID, i18nInfo, AuthMode.DIGEST, authReqFirst);

			try {
				HttpClient httpClient = HttpClientUtil.getHttpClient();
				TTransport transport = new THttpClient(Constants.EXPRESS_HTTP,
						httpClient);
				transport.open();
				TProtocol protocol = new TCompactProtocol(transport);
				ExpressService.Client client = new ExpressService.Client(
						protocol);
				Log.e("ffffffffffffffffffff",
						"call firstlyUseExpress(CommArgs commArgs) ");

				AppInitInfo appInitInfo = client
						.firstlyUseExpress(commArgsFirst);
				editor.putString(Constants.USERID, appInitInfo.getGuideAsstId());
				transport.close();

			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		} else {
			Log.e("fffffffffffffff", "user id not null");
		}*/

		editor.putString(Constants.LOGIN_TIME, Util.getCurrentDate());
		editor.putString(Constants.AUTH_TTL,
				String.valueOf(result.getDigestAuthorizationRes().getAuthTtl()));
		editor.putString(Constants.USER_ID, Constants.USERID);
		editor.putString(Constants.ACCOUNT_NAME, Constants.ACCOUNTNAME);
		editor.putString(Constants.PASS_WORD, Constants.PASSWORD);
		editor.putString(Constants.FIGURE_URL, Constants.FIGUREURL);
		editor.putString(Constants.CLIENT_ID, Constants.CLIENTID);
		editor.putLong(Constants.CLIENT_COUNT, Constants.CLIENTCOUNT);
		editor.putString(Constants.INI_TOKEN, Constants.INITOKEN);
		editor.commit();

		return Constants.ACCESSTOKEN;
	}

	/**
	 * 注销
	 */
	public static boolean logout(Context context, String account,
			String password) {
		SharedPreferences pref = context.getSharedPreferences(
				Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);

		TerminalInfo terminalInfo = new TerminalInfo(DeviceType.ANDROID,
				Constants.DEVICEID, android.os.Build.VERSION.RELEASE);
		AppInfo appInfo = new AppInfo(Constants.APPID, Constants.APPVERSION);
		I18nInfo i18nInfo = new I18nInfo(Region.CN, Language.ZH_CN);

		DigestAuthenticationReq authReq = new DigestAuthenticationReq(
				Constants.CLIENTID, Constants.CLIENTCOUNT, Constants.RANDOMSTR,
				Constants.ACCESSTOKEN);

		CommArgs commArgs = new CommArgs(terminalInfo, appInfo,
				Constants.USERID, i18nInfo, AuthMode.DIGEST, authReq);

		try {
			HttpClient httpClient = HttpClientUtil.getHttpClient();
			TTransport transport = new THttpClient(Constants.AUTH_HTTP,
					httpClient);
			transport.open();
			TProtocol protocol = new TCompactProtocol(transport);
			AuthService.Client client = new AuthService.Client(protocol);

			client.logout(commArgs, null);
			transport.close();

			Constants.LOGINTIME = null;
			Constants.AUTHTTL = null;
			Constants.ACCESSTOKEN = null;
			Constants.USERID = null;
			Constants.ACCOUNTNAME = null;
			Constants.FIGUREURL = null;
			Constants.CLIENTID = null;
			Constants.CLIENTCOUNT = 0;

			SharedPreferences.Editor editor = pref.edit();
			editor.remove(Constants.LOGIN_TIME);
			editor.remove(Constants.AUTH_TTL);
			editor.remove(Constants.USER_ID);
			editor.remove(Constants.ACCOUNT_NAME);
			editor.remove(Constants.FIGURE_URL);
			editor.remove(Constants.CLIENT_ID);
			editor.remove(Constants.CLIENT_COUNT);
			editor.commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	/**
	 * 验证当前应用的版本，返回当前版本信息
	 */
	public static VersionInfo validateVersion(Context context) {

		TerminalInfo terminalInfo = new TerminalInfo(DeviceType.ANDROID,
				Constants.DEVICEID, android.os.Build.VERSION.RELEASE);
		AppInfo appInfo = new AppInfo(Constants.APPID, Constants.APPVERSION);
		I18nInfo i18nInfo = new I18nInfo(Region.CN, Language.ZH_CN);

		CommArgs commArgs = new CommArgs(terminalInfo, appInfo,
				Constants.USERID, i18nInfo, AuthMode.NONE, null);

		try {
			HttpClient httpClient = HttpClientUtil.getHttpClient();
			TTransport transport = new THttpClient(Constants.APP_HTTP,
					httpClient);
			transport.open();
			TProtocol protocol = new TCompactProtocol(transport);
			AppService.Client client = new AppService.Client(protocol);
			VersionInfo versionInfo = client.validateVersion(commArgs);
			transport.close();
			return versionInfo;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * 注册帐户
	 */
	public static AccountInfo registerAccount(Context context, String email,
			String mobileno, String password, String userName) {

		TerminalInfo terminalInfo = new TerminalInfo(DeviceType.ANDROID,
				Constants.DEVICEID, android.os.Build.VERSION.RELEASE);
		AppInfo appInfo = new AppInfo(Constants.APPID, Constants.APPVERSION);
		I18nInfo i18nInfo = new I18nInfo(Region.CN, Language.ZH_CN);

		CommArgs commArgs = new CommArgs(terminalInfo, appInfo, null, i18nInfo,
				AuthMode.NONE, null);

		AccountInfo accountInfo = null;
		try {
			HttpClient httpClient = HttpClientUtil.getHttpClient();
			TTransport transport = new THttpClient(Constants.ACCOUNT_HTTPS,
					httpClient);
			transport.open();
			TProtocol protocol = new TCompactProtocol(transport);
			AccountService.Client client = new AccountService.Client(protocol);

			accountInfo = client.registerAccount(commArgs, email, mobileno,
					password, userName);
			transport.close();

		} catch (BizException e1) {
			Constants.ERRORMSG = e1.message;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return accountInfo;
	}

	/**
	 * 验证所提出的用户帐户和用户口令是否存在和匹配，如果验证通过则取得基本用户信息
	 */
	public static AccountInfo validateAccount(Context context,
			String userAccount, String password) {

		TerminalInfo terminalInfo = new TerminalInfo(DeviceType.ANDROID,
				Constants.DEVICEID, android.os.Build.VERSION.RELEASE);
		AppInfo appInfo = new AppInfo(Constants.APPID, Constants.APPVERSION);
		I18nInfo i18nInfo = new I18nInfo(Region.CN, Language.ZH_CN);

		DigestAuthenticationReq authReq = new DigestAuthenticationReq(
				Constants.CLIENTID, Constants.CLIENTCOUNT, Constants.RANDOMSTR,
				Constants.ACCESSTOKEN);

		CommArgs commArgs = new CommArgs(terminalInfo, appInfo,
				Constants.USERID, i18nInfo, AuthMode.DIGEST, authReq);

		try {
			HttpClient httpClient = HttpClientUtil.getHttpClient();
			TTransport transport = new THttpClient(Constants.ACCOUNT_HTTPS,
					httpClient);
			transport.open();
			TProtocol protocol = new TCompactProtocol(transport);
			AccountService.Client client = new AccountService.Client(protocol);

			AccountInfo accountInfo = client.validateAccount(commArgs,
					userAccount, password);

			transport.close();

			return accountInfo;
		} catch (BizException be) {
			Constants.ERRORMSG = be.message;
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * 根据指定的查询条件查询用户信息
	 */
	public static List<UserInfo> searchUsers(Context context, String condition) {

		TerminalInfo terminalInfo = new TerminalInfo(DeviceType.ANDROID,
				Constants.DEVICEID, android.os.Build.VERSION.RELEASE);
		AppInfo appInfo = new AppInfo(Constants.APPID, Constants.APPVERSION);
		I18nInfo i18nInfo = new I18nInfo(Region.CN, Language.ZH_CN);

		DigestAuthenticationReq authReq = new DigestAuthenticationReq(
				Constants.CLIENTID, Constants.CLIENTCOUNT, Constants.RANDOMSTR,
				Constants.ACCESSTOKEN);

		CommArgs commArgs = new CommArgs(terminalInfo, appInfo,
				Constants.USERID, i18nInfo, AuthMode.DIGEST, authReq);

		try {
			HttpClient httpClient = HttpClientUtil.getHttpClient();
			TTransport transport = new THttpClient(Constants.USER_HTTP,
					httpClient);
			transport.open();
			TProtocol protocol = new TCompactProtocol(transport);
			UserService.Client client = new UserService.Client(protocol);

			List<UserInfo> userInfos = client.searchUsers(commArgs, condition);

			transport.close();

			return userInfos;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * 获得"通知家人安装亲情快递"的短信内容
	 */
	public static String getNoteMessage(Context context) {

		TerminalInfo terminalInfo = new TerminalInfo(DeviceType.ANDROID,
				Constants.DEVICEID, android.os.Build.VERSION.RELEASE);
		AppInfo appInfo = new AppInfo(Constants.APPID, Constants.APPVERSION);
		I18nInfo i18nInfo = new I18nInfo(Region.CN, Language.ZH_CN);

		DigestAuthenticationReq authReq = new DigestAuthenticationReq(
				Constants.CLIENTID, Constants.CLIENTCOUNT, Constants.RANDOMSTR,
				Constants.ACCESSTOKEN);

		CommArgs commArgs = new CommArgs(terminalInfo, appInfo,
				Constants.USERID, i18nInfo, AuthMode.DIGEST, authReq);

		try {
			HttpClient httpClient = HttpClientUtil.getHttpClient();
			TTransport transport = new THttpClient(Constants.FAMILY_HTTP,
					httpClient);
			transport.open();
			TProtocol protocol = new TCompactProtocol(transport);
			FamilyService.Client client = new FamilyService.Client(protocol);

			String note = client.getNoteMessage(commArgs);

			transport.close();

			return note;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * 根据家人的用户ID，将家人从自己的家人列表中删除
	 */
	public static boolean deleteFamilyMember(Context context,
			String familyUserId) {

		TerminalInfo terminalInfo = new TerminalInfo(DeviceType.ANDROID,
				Constants.DEVICEID, android.os.Build.VERSION.RELEASE);
		AppInfo appInfo = new AppInfo(Constants.APPID, Constants.APPVERSION);
		I18nInfo i18nInfo = new I18nInfo(Region.CN, Language.ZH_CN);

		DigestAuthenticationReq authReq = new DigestAuthenticationReq(
				Constants.CLIENTID, Constants.CLIENTCOUNT, Constants.RANDOMSTR,
				Constants.ACCESSTOKEN);

		CommArgs commArgs = new CommArgs(terminalInfo, appInfo,
				Constants.USERID, i18nInfo, AuthMode.DIGEST, authReq);

		try {
			HttpClient httpClient = HttpClientUtil.getHttpClient();
			TTransport transport = new THttpClient(Constants.FAMILY_HTTP,
					httpClient);
			transport.open();
			TProtocol protocol = new TCompactProtocol(transport);
			FamilyService.Client client = new FamilyService.Client(protocol);

			client.deleteFamilyMember(commArgs, familyUserId);

			transport.close();

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	/**
	 * 根据家人的用户ID，修改对家人的称呼，目前只能修改"对他的称呼"字段
	 */
	public static boolean updateFamilyMember(Context context,
			String familyUserId, String nickName) {

		TerminalInfo terminalInfo = new TerminalInfo(DeviceType.ANDROID,
				Constants.DEVICEID, android.os.Build.VERSION.RELEASE);
		AppInfo appInfo = new AppInfo(Constants.APPID, Constants.APPVERSION);
		I18nInfo i18nInfo = new I18nInfo(Region.CN, Language.ZH_CN);

		DigestAuthenticationReq authReq = new DigestAuthenticationReq(
				Constants.CLIENTID, Constants.CLIENTCOUNT, Constants.RANDOMSTR,
				Constants.ACCESSTOKEN);

		CommArgs commArgs = new CommArgs(terminalInfo, appInfo,
				Constants.USERID, i18nInfo, AuthMode.DIGEST, authReq);

		try {
			HttpClient httpClient = HttpClientUtil.getHttpClient();
			TTransport transport = new THttpClient(Constants.FAMILY_HTTP,
					httpClient);
			transport.open();
			TProtocol protocol = new TCompactProtocol(transport);
			FamilyService.Client client = new FamilyService.Client(protocol);

			client.updateFamilyMember(commArgs, familyUserId, nickName);

			transport.close();

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	/**
	 * 通过家人的用户ID及与自己的关系信息，添加家人
	 */
	public static boolean addFamilyMember(Context context, String familyUserId,
			String titleCode, String nickName, Gender gender,
			boolean isDirectlyAdd) {

		TerminalInfo terminalInfo = new TerminalInfo(DeviceType.ANDROID,
				Constants.DEVICEID, android.os.Build.VERSION.RELEASE);
		AppInfo appInfo = new AppInfo(Constants.APPID, Constants.APPVERSION);
		I18nInfo i18nInfo = new I18nInfo(Region.CN, Language.ZH_CN);

		DigestAuthenticationReq authReq = new DigestAuthenticationReq(
				Constants.CLIENTID, Constants.CLIENTCOUNT, Constants.RANDOMSTR,
				Constants.ACCESSTOKEN);

		CommArgs commArgs = new CommArgs(terminalInfo, appInfo,
				Constants.USERID, i18nInfo, AuthMode.DIGEST, authReq);

		try {
			HttpClient httpClient = HttpClientUtil.getHttpClient();
			TTransport transport = new THttpClient(Constants.FAMILY_HTTP,
					httpClient);
			transport.open();
			TProtocol protocol = new TCompactProtocol(transport);
			FamilyService.Client client = new FamilyService.Client(protocol);

			client.addFamilyMember(commArgs, familyUserId, titleCode, nickName,
					gender, isDirectlyAdd);

			transport.close();

			return true;
		} catch (BizException e1) {
			Constants.ERRORMSG = e1.message;
			System.out.println("捕获到了BizException异常     " + e1.message);
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	/**
	 * 查询家人辈分及称呼关系代码的列表，供添加家人页面的"辈分、称呼"两个下拉框的初始化使用
	 */
	public static List<MemberRelation> getMemberRelationCodeList(Context context) {

		TerminalInfo terminalInfo = new TerminalInfo(DeviceType.ANDROID,
				Constants.DEVICEID, android.os.Build.VERSION.RELEASE);
		AppInfo appInfo = new AppInfo(Constants.APPID, Constants.APPVERSION);
		I18nInfo i18nInfo = new I18nInfo(Region.CN, Language.ZH_CN);

		DigestAuthenticationReq authReq = new DigestAuthenticationReq(
				Constants.CLIENTID, Constants.CLIENTCOUNT, Constants.RANDOMSTR,
				Constants.ACCESSTOKEN);

		CommArgs commArgs = new CommArgs(terminalInfo, appInfo,
				Constants.USERID, i18nInfo, AuthMode.DIGEST, authReq);

		try {
			HttpClient httpClient = HttpClientUtil.getHttpClient();
			TTransport transport = new THttpClient(Constants.FAMILY_HTTP,
					httpClient);
			transport.open();
			TProtocol protocol = new TCompactProtocol(transport);
			FamilyService.Client client = new FamilyService.Client(protocol);

			List<MemberRelation> memberRelations = client
					.getMemberRelationCodeList(commArgs);

			transport.close();

			return memberRelations;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * 根据当前登录人ID查询其家人及最后一条快递信息(包含小助手，对于彼此未发送过快递的家人不显示)
	 */
	public static List<MemberInfo> getFamilyMemberList(Context context) {

		TerminalInfo terminalInfo = new TerminalInfo(DeviceType.ANDROID,
				Constants.DEVICEID, android.os.Build.VERSION.RELEASE);
		AppInfo appInfo = new AppInfo(Constants.APPID, Constants.APPVERSION);
		I18nInfo i18nInfo = new I18nInfo(Region.CN, Language.ZH_CN);

		DigestAuthenticationReq authReq = new DigestAuthenticationReq(
				Constants.CLIENTID, Constants.CLIENTCOUNT, Constants.RANDOMSTR,
				Constants.ACCESSTOKEN);

		CommArgs commArgs = new CommArgs(terminalInfo, appInfo,
				Constants.USERID, i18nInfo, AuthMode.DIGEST, authReq);

		try {
			HttpClient httpClient = HttpClientUtil.getHttpClient();
			TTransport transport = new THttpClient(Constants.FAMILY_HTTP,
					httpClient);
			transport.open();
			TProtocol protocol = new TCompactProtocol(transport);
			FamilyService.Client client = new FamilyService.Client(protocol);

			List<MemberInfo> memberInfos = client.getFamilyMemberList(commArgs);

			transport.close();

			return memberInfos;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * 根据当前登录人ID查询其所有家人的详细信息(包含小助手)
	 */
	public static List<UserBaseInfo> getFamilyMemberDetailList(Context context) {

		TerminalInfo terminalInfo = new TerminalInfo(DeviceType.ANDROID,
				Constants.DEVICEID, android.os.Build.VERSION.RELEASE);
		AppInfo appInfo = new AppInfo(Constants.APPID, Constants.APPVERSION);
		I18nInfo i18nInfo = new I18nInfo(Region.CN, Language.ZH_CN);

		DigestAuthenticationReq authReq = new DigestAuthenticationReq(
				Constants.CLIENTID, Constants.CLIENTCOUNT, Constants.RANDOMSTR,
				Constants.ACCESSTOKEN);

		CommArgs commArgs = new CommArgs(terminalInfo, appInfo,
				Constants.USERID, i18nInfo, AuthMode.DIGEST, authReq);

		try {

			HttpClient httpClient = HttpClientUtil.getHttpClient();
			TTransport transport = new THttpClient(Constants.FAMILY_HTTP,
					httpClient);
			transport.open();
			TProtocol protocol = new TCompactProtocol(transport);
			FamilyService.Client client = new FamilyService.Client(protocol);

			List<UserBaseInfo> userBaseInfos = client
					.getFamilyMemberDetailList(commArgs);

			transport.close();

			// for (UserBaseInfo ub : userBaseInfos) {
			// System.out.println(ub.userId + "|");
			// System.out.println(ub.userName + "|");
			//
			// System.out.println(ub.nikeName + "|");
			// System.out.println(ub.relation + "|");
			// System.out.println(ub.mobile + "|");
			// System.out.println(ub.email + "|");
			// System.out.println(ub.allowStatus + "|");
			// System.out.println(ub.figureUrl + "|");
			// }
			return userBaseInfos;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * 查询由指定家人发出的接收者是当前用户，且状态为“发出”的快递列表，同时后台将状态改为“送达” 返回值：
	 * 快递信息按家人查询结果的Map集合，Map中的key为家人的UserID，
	 * value为家人发给自己的快递列表，一次返回的所有家人的快递条数的总和最多为500条， 当服务器上待取条数大于500条时：
	 * (1)对于各个家人，将优先取得userIds参数中靠前的家人的列表。 (2)对于同一个家人，将优先取得较旧的快递。
	 * (3)各个家人本次未取的剩余快递的条数记录在返回值的ExpressInfoAll.leftUnreadNum中。
	 */
	public static Map<String, ExpressInfoAll> getExpressList(Context context,
			List<String> userIds) {

		TerminalInfo terminalInfo = new TerminalInfo(DeviceType.ANDROID,
				Constants.DEVICEID, android.os.Build.VERSION.RELEASE);
		AppInfo appInfo = new AppInfo(Constants.APPID, Constants.APPVERSION);
		I18nInfo i18nInfo = new I18nInfo(Region.CN, Language.ZH_CN);

		DigestAuthenticationReq authReq = new DigestAuthenticationReq(
				Constants.CLIENTID, Constants.CLIENTCOUNT, Constants.RANDOMSTR,
				Constants.ACCESSTOKEN);

		CommArgs commArgs = new CommArgs(terminalInfo, appInfo,
				Constants.USERID, i18nInfo, AuthMode.DIGEST, authReq);

		try {
			HttpClient httpClient = HttpClientUtil.getHttpClient();
			TTransport transport = new THttpClient(Constants.EXPRESS_HTTP,
					httpClient);
			transport.open();
			TProtocol protocol = new TCompactProtocol(transport);
			ExpressService.Client client = new ExpressService.Client(protocol);

			Map<String, ExpressInfoAll> expressMap = client.getExpressList(
					commArgs, userIds);

			transport.close();

			return expressMap;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * 根据上次查询时间及家人ID的集合，将该时间之后的由当前用户发出给指定家人的且状态被修改过的所有快递的ID及状态进行返回
	 * 同时返回服务器当前时间，供下次查询使用。 返回值： 所有当前用户发出的状态有变化的快递状态集合(快递ID升序)
	 * 服务器当前时间，格式：1970年1月1日开始经历的毫秒数
	 */
	public static ExpressChangedStatusAll getChangedStatusList(Context context,
			List<String> userIds) {

		SharedPreferences pref = context.getSharedPreferences(
				Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
		long lastObtainTime = pref.getLong(Constants.LAST_OBTAIN_TIME, 0);

		TerminalInfo terminalInfo = new TerminalInfo(DeviceType.ANDROID,
				Constants.DEVICEID, android.os.Build.VERSION.RELEASE);
		AppInfo appInfo = new AppInfo(Constants.APPID, Constants.APPVERSION);
		I18nInfo i18nInfo = new I18nInfo(Region.CN, Language.ZH_CN);

		DigestAuthenticationReq authReq = new DigestAuthenticationReq(
				Constants.CLIENTID, Constants.CLIENTCOUNT, Constants.RANDOMSTR,
				Constants.ACCESSTOKEN);

		CommArgs commArgs = new CommArgs(terminalInfo, appInfo,
				Constants.USERID, i18nInfo, AuthMode.DIGEST, authReq);

		try {
			HttpClient httpClient = HttpClientUtil.getHttpClient();
			TTransport transport = new THttpClient(Constants.EXPRESS_HTTP,
					httpClient);
			transport.open();
			TProtocol protocol = new TCompactProtocol(transport);
			ExpressService.Client client = new ExpressService.Client(protocol);

			ExpressChangedStatusAll expressChangedStatusAll = client
					.getChangedStatusList(commArgs, userIds, lastObtainTime);
			transport.close();

			SharedPreferences.Editor editor = pref.edit();
			editor.putLong(Constants.LAST_OBTAIN_TIME,
					expressChangedStatusAll.serverTime);
			editor.commit();

			return expressChangedStatusAll;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * 第一次使用亲情快递接口（注意：本接口只有在该用户第一次在该终端使用亲情快递产品时才需要调用。） 服务端收到该请求后，会向终端发送小助手的欢迎快递
	 */
	public static AppInitInfo firstlyUseExpress(Context context) {

		TerminalInfo terminalInfo = new TerminalInfo(DeviceType.ANDROID,
				Constants.DEVICEID, android.os.Build.VERSION.RELEASE);
		AppInfo appInfo = new AppInfo(Constants.APPID, Constants.APPVERSION);
		I18nInfo i18nInfo = new I18nInfo(Region.CN, Language.ZH_CN);

		DigestAuthenticationReq authReq = new DigestAuthenticationReq(
				Constants.CLIENTID, Constants.CLIENTCOUNT, Constants.RANDOMSTR,
				Constants.ACCESSTOKEN);

		CommArgs commArgs = new CommArgs(terminalInfo, appInfo,
				Constants.USERID, i18nInfo, AuthMode.DIGEST, authReq);

		try {
			HttpClient httpClient = HttpClientUtil.getHttpClient();
			TTransport transport = new THttpClient(Constants.EXPRESS_HTTP,
					httpClient);
			transport.open();
			TProtocol protocol = new TCompactProtocol(transport);
			ExpressService.Client client = new ExpressService.Client(protocol);

			AppInitInfo appInitInfo = client.firstlyUseExpress(commArgs);
			transport.close();

			return appInitInfo;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 发送快递,如接收者是“小助手”，后台自动生成相应的回复信息 返回值：快递ID
	 */
	public static String sendExpress(Context context, ExpressInfo expressInfo) {

		TerminalInfo terminalInfo = new TerminalInfo(DeviceType.ANDROID,
				Constants.DEVICEID, android.os.Build.VERSION.RELEASE);
		AppInfo appInfo = new AppInfo(Constants.APPID, Constants.APPVERSION);
		I18nInfo i18nInfo = new I18nInfo(Region.CN, Language.ZH_CN);

		DigestAuthenticationReq authReq = new DigestAuthenticationReq(
				Constants.CLIENTID, Constants.CLIENTCOUNT, Constants.RANDOMSTR,
				Constants.ACCESSTOKEN);

		CommArgs commArgs = new CommArgs(terminalInfo, appInfo,
				Constants.USERID, i18nInfo, AuthMode.DIGEST, authReq);

		try {
			HttpClient httpClient = HttpClientUtil.getHttpClient();
			TTransport transport = new THttpClient(Constants.EXPRESS_HTTP,
					httpClient);
			transport.open();
			TProtocol protocol = new TCompactProtocol(transport);
			ExpressService.Client client = new ExpressService.Client(protocol);

			String expressId = client.sendExpress(commArgs, expressInfo);
			transport.close();

			return expressId;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 批量更新快递状态，由送达到已读(文字、图像)或未听（音频）
	 */
	public static boolean updateFromForwardedStatus(Context context,
			String familyUserId, long lastUpdateTime) {
		TerminalInfo terminalInfo = new TerminalInfo(DeviceType.ANDROID,
				Constants.DEVICEID, android.os.Build.VERSION.RELEASE);
		AppInfo appInfo = new AppInfo(Constants.APPID, Constants.APPVERSION);
		I18nInfo i18nInfo = new I18nInfo(Region.CN, Language.ZH_CN);

		DigestAuthenticationReq authReq = new DigestAuthenticationReq(
				Constants.CLIENTID, Constants.CLIENTCOUNT, Constants.RANDOMSTR,
				Constants.ACCESSTOKEN);

		CommArgs commArgs = new CommArgs(terminalInfo, appInfo,
				Constants.USERID, i18nInfo, AuthMode.DIGEST, authReq);

		try {
			HttpClient httpClient = HttpClientUtil.getHttpClient();
			TTransport transport = new THttpClient(Constants.EXPRESS_HTTP,
					httpClient);
			transport.open();
			TProtocol protocol = new TCompactProtocol(transport);
			ExpressService.Client client = new ExpressService.Client(protocol);

			System.out.println("lastUpdateTime=" + lastUpdateTime);
			System.out.println("familyUserId=" + familyUserId);
			client.updateFromForwardedStatus(commArgs, lastUpdateTime,
					familyUserId);
			transport.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 根据快递ID及给定的状态对快递进行状态更新
	 */
	public static boolean updateToReadStatus(Context context, String expressId) {

		TerminalInfo terminalInfo = new TerminalInfo(DeviceType.ANDROID,
				Constants.DEVICEID, android.os.Build.VERSION.RELEASE);
		AppInfo appInfo = new AppInfo(Constants.APPID, Constants.APPVERSION);
		I18nInfo i18nInfo = new I18nInfo(Region.CN, Language.ZH_CN);

		DigestAuthenticationReq authReq = new DigestAuthenticationReq(
				Constants.CLIENTID, Constants.CLIENTCOUNT, Constants.RANDOMSTR,
				Constants.ACCESSTOKEN);

		CommArgs commArgs = new CommArgs(terminalInfo, appInfo,
				Constants.USERID, i18nInfo, AuthMode.DIGEST, authReq);

		try {
			HttpClient httpClient = HttpClientUtil.getHttpClient();
			TTransport transport = new THttpClient(Constants.EXPRESS_HTTP,
					httpClient);
			transport.open();
			TProtocol protocol = new TCompactProtocol(transport);
			ExpressService.Client client = new ExpressService.Client(protocol);

			client.updateToReadStatus(commArgs, expressId);
			transport.close();

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 保存头像
	 */
	public static boolean saveAvatar(Context context, String userid,
			String formatType, ByteBuffer dataContent) {

		TerminalInfo terminalInfo = new TerminalInfo(DeviceType.ANDROID,
				Constants.DEVICEID, android.os.Build.VERSION.RELEASE);
		AppInfo appInfo = new AppInfo(Constants.APPID, Constants.APPVERSION);
		I18nInfo i18nInfo = new I18nInfo(Region.CN, Language.ZH_CN);

		DigestAuthenticationReq authReq = new DigestAuthenticationReq(
				Constants.CLIENTID, Constants.CLIENTCOUNT, Constants.RANDOMSTR,
				Constants.ACCESSTOKEN);

		CommArgs commArgs = new CommArgs(terminalInfo, appInfo,
				Constants.USERID, i18nInfo, AuthMode.DIGEST, authReq);

		try {
			HttpClient httpClient = HttpClientUtil.getHttpClient();
			TTransport transport = new THttpClient(Constants.ACCOUNT_HTTP,
					httpClient);
			transport.open();
			TProtocol protocol = new TCompactProtocol(transport);
			AccountService.Client client = new AccountService.Client(protocol);
			client.saveAvatar(commArgs, userid, formatType, dataContent);
			transport.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
