/* ==================================================
 * 产品名: 亲情快递
 * 文件名: LocalExpressService.java
 * --------------------------------------------------
 * 开发环境: JDK1.6
 * --------------------------------------------------
 * 修订履历    2012/07/07  1.00  初版发行
 * --------------------------------------------------
 * (C) Copyright XIKANG CORPORATION 2012 All Rights Reserved.
 */

package com.xikang.family.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONException;
import org.json.JSONObject;

import com.xikang.channel.familyexpress.rpc.thrift.express.ExpressChangedStatus;
import com.xikang.channel.familyexpress.rpc.thrift.express.ExpressChangedStatusAll;
import com.xikang.channel.familyexpress.rpc.thrift.express.ExpressInfo;
import com.xikang.channel.familyexpress.rpc.thrift.express.ExpressInfoAll;
import com.xikang.channel.familyexpress.rpc.thrift.family.UserBaseInfo;
import com.xikang.family.service.ExpressDBService;
import com.xikang.family.service.LocalExpressInfo;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.StrictMode;
import android.util.Log;

/**
 * 
 * 本地下载服务
 * 
 * 
 * 
 * @author 张荣
 * @version 1.00
 */

public class LocalExpressService extends Service {

	private String command = null;
	private String count = null;
	private String time = null;
	private String feId = null;
	private String feStatus = null;
	private Context context = this;
	private ExecutorService executorService = null;

	private List<UserBaseInfo> familyList = null;
	private ExpressDBService expressDBService = new ExpressDBService(context);
	private static final String TAG = "LocalExpressService";

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		Constants.IS_EXPRESS_SERVICE_RUN = true;
		String body = intent.getExtras().getString(Constants.NOTIFICATION_BODY);
		JSONObject jsonObject;
		try {
			Log.e(TAG, "*****************onStartCommand...");
			jsonObject = new JSONObject(body);
			command = jsonObject.getString("command");
			count = jsonObject.getString("count");
			time = jsonObject.getString("time");
			Log.e(TAG, "******command=" + command);
			Log.e(TAG, "******count=" + count);
			Log.e(TAG, "******time=" + time);
			if ("2".equals(command) && "1".equals(count)) {
				feId = jsonObject.getString("feId");
				feStatus = jsonObject.getString("feStatus");
				Log.e(TAG, "******feId=" + feId);
				Log.e(TAG, "******feStatus=" + feStatus);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		executorService.execute(expressGetter);

		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		executorService = Executors.newSingleThreadExecutor();

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Constants.IS_EXPRESS_SERVICE_RUN = false;
		executorService.shutdown();
	}

	Runnable expressGetter = new Runnable() {
		@Override
		public void run() {
			ThriftOpe.login(context, Constants.ACCOUNTNAME, Constants.PASSWORD);
			ArrayList<String> idList = new ArrayList<String>();
			ArrayList<String> statusList = new ArrayList<String>();
			Intent intent = null;
			try {
				if ("2".equals(command)) {
					Log.e(TAG,"接收到快递变更消息");
					intent = new Intent(
							Constants.ACTION_EXPRESS_STATUS_UPDATEED);

					if ("1".equals(count)) {
						// 更新本地DB快递状态
						expressDBService.updateStatusByEid(feId, feStatus);

						idList.add(feId);
						statusList.add(feStatus);

					} else {
						// 查询用户所有家人的详细信息列表
						familyList = ThriftOpe
								.getFamilyMemberDetailList(context);
						if(familyList == null){
							Log.e(TAG,"getFamilyMemberDetailList null");
						}

						List<String> userIds = new ArrayList<String>();
						for (UserBaseInfo user : familyList) {
							
							userIds.add(user.getUserId());
						}

						// 取服务端快递状态
						ExpressChangedStatusAll expressStatusAll = ThriftOpe
								.getChangedStatusList(context, userIds);

						// 更新本地DB快递状态
						for (String userid : userIds) {
							List<ExpressChangedStatus> expressList = expressStatusAll
									.getStatusChangedMap().get(userid);
							if (expressList != null) {
								for (ExpressChangedStatus expressStatus : expressList) {
									// 更新本地DB快递状态
									expressDBService.updateStatusByEid(
											expressStatus.getExpressId(),
											String.valueOf(expressStatus
													.getExpressStatus()
													.getValue()));
									idList.add(expressStatus.getExpressId());
									statusList.add(String.valueOf(expressStatus
											.getExpressStatus().getValue()));

								}
							}
						}
					}

					// 广播通知UI
					intent.putStringArrayListExtra(Constants.EXPRESS_ID_LIST,
							idList);
					intent.putStringArrayListExtra(
							Constants.EXPRESS_STATUS_LIST, statusList);
					Log.e(TAG,"服务发送通知给UI！！！！！！！！！！！！状态更新");
					context.sendBroadcast(intent);

				} else if ("1".equals(command)) {
					Log.e(TAG,"接收到新快递消息");

					intent = new Intent(Constants.ACTION_EXPRESS_NEW);

					// 查询用户所有家人的详细信息列表
					StrictMode.ThreadPolicy old = StrictMode.getThreadPolicy();
					StrictMode
							.setThreadPolicy(new StrictMode.ThreadPolicy.Builder(
									old).permitNetwork().build());
					familyList = ThriftOpe.getFamilyMemberDetailList(context);
					if(familyList == null){
						Log.e(TAG,"getFamilyMemberDetailList null");
					}

					List<String> userIds = new ArrayList<String>();
					for (UserBaseInfo user : familyList) {
						Log.e(TAG,"userid = " + user.getUserId());
						userIds.add(user.getUserId());
					}

					// 取得服务端快递
					Map<String, ExpressInfoAll> mapExpress = null;
					mapExpress = ThriftOpe.getExpressList(context, userIds);
					// 更新本地DB
					SharedPreferences pref = context.getSharedPreferences(
							Constants.SHARED_PREFERENCE_NAME,
							Context.MODE_PRIVATE);
					SharedPreferences.Editor editor = pref.edit();
					ArrayList<String> uids = new ArrayList<String>();
					for (String user : userIds) {
						ExpressInfoAll expressInfoALL = mapExpress.get(user);
						editor.remove(user);
						if (expressInfoALL != null) {
							editor.putInt(user,
									expressInfoALL.getLeftUnreadNum());
							List<ExpressInfo> expressInfos = expressInfoALL
									.getExpressInfos();
							System.out.println(user+" 推送过来  "+expressInfos.size()+"  条消息");
							for (ExpressInfo einfo : expressInfos) {
								
								
								Log.e(TAG,"接收消息文本是 " + einfo.getTextContent());
								Log.e(TAG,"接收消息类型是 " + einfo.getExpressType());
								Log.e(TAG, "快递发出时间是" + einfo.getTime());
								Log.e(TAG, "快递更新时间是" + einfo.getUpdateTime());
								
								LocalExpressInfo info = new LocalExpressInfo();
								String filename = null;
								String thumbnail = null;
								if (einfo.getContentType().getValue() != 1) {
									filename = HttpClientUtil
											.requestPostStream(
													einfo.getTextContent(),
													null,
													einfo.getFormat());
									if (einfo.getContentType().getValue() == 3) {
										String url = einfo.getTextContent();
										String thumbnailUrl = url
												.substring(0,
														url.lastIndexOf("."))
												.concat("_160_160")
												.concat(url.substring(url
														.lastIndexOf(".")));
										thumbnail = HttpClientUtil
												.requestPostStream(
														thumbnailUrl,
														
														null, einfo.getFormat());

									}

								}

								info.setExpressinfo(einfo);
								info.setFilename(filename);
								info.setThumbnail(thumbnail);
								String otheruid = einfo.getFrom().equals(
										Constants.USERID) ? einfo.getTo()
										: einfo.getFrom();
								Log.e(TAG,"otheruid    " + otheruid);
								info.setOtheruid(otheruid);
								info.setUpstatus("0");
								uids.add(otheruid);
								expressDBService.insert(info);
								Long lastExpressTime = pref.getLong(
										Constants.LAST_UPDATE_TIME, 0);
								if (lastExpressTime < einfo.getUpdateTime()) {
									editor.putLong(Constants.LAST_UPDATE_TIME,
											einfo.getUpdateTime()).commit();
								}
								Log.e(TAG,"服务发送通知给UI！！！！！！！！！！！！新快递");
							}
						}

					}
					editor.commit();

					// 广播通知UI
					intent.putStringArrayListExtra(
							Constants.EXPRESS_USERID_LIST, uids);
					context.sendBroadcast(intent);

					StrictMode.setThreadPolicy(old);
				}

			} catch (Exception x) {
				x.printStackTrace();
			}

		}
	};

}
