/* ==================================================
 * 产品名: 亲情快递
 * 文件名: SendService.java
 * --------------------------------------------------
 * 开发环境: JDK1.6
 * --------------------------------------------------
 * 修订履历    2012/07/07  1.00  初版发行
 * --------------------------------------------------
 * (C) Copyright XIKANG CORPORATION 2012 All Rights Reserved.
 */

package com.xikang.family.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.xikang.channel.familyexpress.rpc.thrift.express.ExpressStatus;
import com.xikang.family.service.ExpressDBService;
import com.xikang.family.service.LocalExpressInfo;

/**
 * 
 * 发送快递服务
 * 
 * 
 * 
 * @author 闫继超
 * @version 1.00
 */

public class SendService extends Service {
	
	private static final String TAG = "SendService";

	private Timer timer = null;
	private SendTask task = null;
	private ExpressDBService db = null;
	private Context mContext = null;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.e(TAG,"onStartCommand");
		mContext = getApplicationContext();
		db = new ExpressDBService(mContext);
		timer = new Timer();
		task = new SendTask();
		timer.scheduleAtFixedRate(task, 0, 10 * 1000);
		return super.onStartCommand(intent, flags, startId);
	}

	class SendTask extends TimerTask {

		@Override
		public void run() {
			
			//Log.e(TAG,"begin running");
			
			// 待发列表
			List<LocalExpressInfo> sendList = db.getReadyInfos2();
			if (sendList != null && sendList.size() > 0) {
				Log.e(TAG,"sendList size = "+sendList.size());
				for (LocalExpressInfo info : sendList) {
					db.updateStatusBy_id(info.get_id(), "9");
				}
				for (LocalExpressInfo info : sendList) {
					if ((Constants.FEContentType.AUDIO == info.getExpressinfo()
							.getContentType().getValue())) {
						File file = new File(info.getFilename());
						byte[] buf = new byte[(int) file.length()];
						try {
							new FileInputStream(file).read(buf);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
						info.getExpressinfo().setDataContent(buf);
					}
					if ((Constants.FEContentType.IMAGE == info.getExpressinfo()
							.getContentType().getValue())) {
						String path = Util.formatPhoto(info.getFilename());
						File file = new File(path);
						byte[] buf = new byte[(int) file.length()];
						try {
							new FileInputStream(file).read(buf);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
						info.getExpressinfo().setDataContent(buf);
						if (!info.getFilename().equals(path)) {
							Util.deleteFile(path);
						}
					}
					ThriftOpe.login(mContext, Constants.ACCOUNTNAME,
							Constants.PASSWORD);
					String expressId = ThriftOpe.sendExpress(mContext,
							info.getExpressinfo());
					Log.e(TAG,"expressId = " + expressId);
					if (expressId != null) {
						// 更新快递ID
						db.updateEidBy_id(expressId, info.get_id());
						// 更新快递状态为已发
						db.updateStatusByEid(expressId,
								String.valueOf(ExpressStatus.SENT.getValue()));
						Intent intent = new Intent(
								Constants.SEND_EXPRESS_INTENT);
						intent.putExtra("_id", info.get_id());
						intent.putExtra("expressId", expressId);
						intent.putExtra("otheruid", info.getOtheruid());
						// 更新UI
						Log.e(TAG,"send broadcast sent");
						sendBroadcast(intent);
					} else {
						Log.e(TAG,"send express failed");
						db.updateStatusBy_id(info.get_id(), null);
					}
				}
			}
		}
	}
}
