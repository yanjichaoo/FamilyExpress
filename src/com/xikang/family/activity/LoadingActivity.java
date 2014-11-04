/* ==================================================
 * 产品名: 亲情快递
 * 文件名: LoadingActivity.java
 * --------------------------------------------------
 * 开发环境: JDK1.6
 * --------------------------------------------------
 * 修订履历    2012/07/07  1.00  初版发行
 * --------------------------------------------------
 * (C) Copyright XIKANG CORPORATION 2012 All Rights Reserved.
 */
package com.xikang.family.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.xikang.channel.base.rpc.thrift.app.VersionInfo;
import com.xikang.channel.base.rpc.thrift.app.VersionStatus;
import com.xikang.cpsclient.ServiceManager;
import com.xikang.family.common.CPSClientUtil;
import com.xikang.family.common.Constants;
import com.xikang.family.common.SendService;
import com.xikang.family.common.ThriftOpe;
import com.xikang.family.common.Util;
import com.xikang.family.service.ExpressDBService;

/**
 * 
 * Loading页面
 * 
 * 
 * 
 * @author 张荣
 * @version 1.00
 */

public class LoadingActivity extends BaseActivity {
	private static final String TAG = "LoadingActivity";

	private Resources mRes;

	// 登录消息ID
	private static final int LOGIN_SUCCESS = 1001;
	private static final int LOGIN_FAILED = 1002;
	private static final int LOGIN_FIRST = 1003;

	private static final int VERSION_SUCCESS = 2001;
	private static final int VERSION_FAILED = 2002;

	// 版本信息
	private VersionInfo versionInfo = null;

	private LayoutInflater inflater = null;
	private AlertDialog updateDialog = null;
	private SharedPreferences pref = null;

	private String accountname = null;
	private String password = null;

	private ExpressDBService dbService = null;

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case VERSION_SUCCESS:
				if (versionInfo.currentStatus == VersionStatus.RECOMMENDED) {
					showProgress(LoadingActivity.this
							.getString(R.string.wait_login));
					login();
				} else {
					showUpdateDialog();
				}
				break;
			case VERSION_FAILED:
				showResultDisableBack(
						LoadingActivity.this.getString(R.string.update_error),
						LoadingActivity.this.getString(R.string.version_error),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								showProgress(LoadingActivity.this
										.getString(R.string.wait_login));
								login();
							}

						}, new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								LoadingActivity.this.finish();
							}
						});
				break;
			case LOGIN_SUCCESS:
				Log.e(TAG,"start sendservice");
				mContext.startService(new Intent(LoadingActivity.this,
						SendService.class));
				CPSClientUtil.connectCPS(getApplicationContext());
				Intent intent = new Intent(LoadingActivity.this,
						FamilyMainFrameActivity.class);
				startActivity(intent);
				if (updateDialog != null) {
					updateDialog.dismiss();
				}
				LoadingActivity.this.finish();
				break;
			case LOGIN_FAILED:
				Intent intent1 = new Intent(LoadingActivity.this,
						LoginActivity.class);
				startActivity(intent1);
				if (updateDialog != null) {
					updateDialog.dismiss();
				}
				LoadingActivity.this.finish();
				break;
			case LOGIN_FIRST:
				Intent intent2 = new Intent(LoadingActivity.this,
						IntroduceActivity.class);
				startActivity(intent2);
				if (updateDialog != null) {
					updateDialog.dismiss();
				}
				LoadingActivity.this.finish();
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_loading);
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			// sd card 可用
			if (!Constants.IS_CPS_SERVICE_RUN) {
				ServiceManager serviceManager = new ServiceManager(this);
				serviceManager.startService();
			}

			dbService = new ExpressDBService(this);
			if (Util.isNetworkAvailable(this)) {
				init();
				if (Util.getSDCardAvailableSize() <= 10
						&& pref.getString(Constants.IS_SDCARD_REMIND, "no")
								.equals("no")) {
					Intent sdcard = new Intent(LoadingActivity.this,
							CheckSDCardActivity.class);
					startActivityForResult(sdcard, 0);
				} else {
					loadingData();
				}
			} else {
				Toast.makeText(this, R.string.loading_not_connected,
						Toast.LENGTH_LONG).show();
				new TimerThread().start();
			}
		} else {
			// 当前不可用
			showToastShort(getString(R.string.sd_error));
			this.finish();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		loadingData();
	}

	private void init() {
		inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		mRes = getResources();
		pref = getSharedPreferences(Constants.SHARED_PREFERENCE_NAME,
				Context.MODE_PRIVATE);
		Util.init(LoadingActivity.this);
	};

	private void loadingData() {
		deleteOldData();
		showProgress(this.getString(R.string.update_version));
		getVersionInfo();
	};

	private void deleteOldData() {
		long holdTime = System.currentTimeMillis();

		int holdTerm = pref.getInt(Constants.HOLD_TERM, 1);
		switch (holdTerm) {
		case 1:
			holdTime -= 24 * 60 * 60 * 1000 * 30L;
			break;
		case 2:
			holdTime -= 24 * 60 * 60 * 1000 * 180L;
			break;
		case 3:
			holdTime -= 24 * 60 * 60 * 1000 * 365L;
			break;
		case 4:
			return;
		}

		dbService.deleteByDate(String.valueOf(holdTime));
	}

	private void showUpdateDialog() {
		updateDialog = new AlertDialog.Builder(this).create();
		View view = inflater.inflate(R.layout.layout_version_dialog, null);
		updateDialog.setTitle(this.getString(R.string.update_title));
		updateDialog.setView(view);
		Button button_download = (Button) view.findViewById(R.id.download);
		Button button_exit = (Button) view.findViewById(R.id.exit);
		TextView tvInfo = (TextView) view.findViewById(R.id.dialoginfo);
		if (versionInfo.currentStatus == VersionStatus.INCOMPATIBLE) {
			tvInfo.setText(R.string.update_disable_msg);
		} else {
			tvInfo.setText(versionInfo.currentMessage);
		}

		if (versionInfo.currentStatus == VersionStatus.COMPATIBLE) {
			button_download.setText(mRes.getString(R.string.update_download)
					.replace("@version", versionInfo.recommendedVersion));
			button_download.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					showProgress(LoadingActivity.this
							.getString(R.string.download));
					new Thread() {

						@Override
						public void run() {
							super.run();
							Util.downloadAPK(versionInfo.linkUrl,
									LoadingActivity.this);
							closeProgress();
						}

					}.start();
				}
			});
			button_exit.setText(R.string.update_skip);
			button_exit.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					showProgress(LoadingActivity.this
							.getString(R.string.wait_login));
					login();
				}
			});
		}
		if (versionInfo.currentStatus == VersionStatus.INCOMPATIBLE) {
			button_download.setText(mRes.getString(R.string.update_download)
					.replace("@version", versionInfo.recommendedVersion));
			button_download.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					showProgress(LoadingActivity.this
							.getString(R.string.download));
					new Thread() {

						@Override
						public void run() {
							super.run();
							Util.downloadAPK(versionInfo.linkUrl,
									LoadingActivity.this);
							closeProgress();
						}
					}.start();
				}
			});
			button_exit.setText(R.string.update_exit);
			button_exit.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					updateDialog.dismiss();
					LoadingActivity.this.finish();
				}
			});
		}

		// 显示对话框
		updateDialog.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					if (versionInfo.currentStatus == VersionStatus.INCOMPATIBLE) {
						updateDialog.dismiss();
						LoadingActivity.this.finish();
					}
					if (versionInfo.currentStatus == VersionStatus.COMPATIBLE) {
						dialog.dismiss();
						showProgress(LoadingActivity.this
								.getString(R.string.wait_login));
						login();
					}
				}
				return false;
			}
		});

		updateDialog.show();
	}

	private void getVersionInfo() {
		new Thread() {

			@Override
			public void run() {
				super.run();
				Message msg = Message.obtain();
				VersionInfo vi = ThriftOpe
						.validateVersion(LoadingActivity.this);

				if (vi != null) {
					msg.what = VERSION_SUCCESS;
					versionInfo = vi;
				} else {
					msg.what = VERSION_FAILED;
				}
				closeProgress();
				handler.sendMessage(msg);
			}
		}.start();
	}

	/**
	 * 登录验证
	 */
	private void login() {
		// 上传验证线程
		new Thread() {

			@Override
			public void run() {
				super.run();
				accountname = pref.getString(Constants.ACCOUNT_NAME, "");
				password = pref.getString(Constants.PASS_WORD, "");
				System.out
						.println("loading   " + accountname + "  " + password);
				Message msg = Message.obtain();
				if (!"".equals(accountname) && !"".equals(password)) {
					String accessToken = ThriftOpe.login(LoadingActivity.this,
							accountname, password);
					if (accessToken != null) {
						msg.what = LOGIN_SUCCESS;
						Constants.ACCESSTOKEN = accessToken;
					} else {
						msg.what = LOGIN_FAILED;
					}
				} else {
					msg.what = LOGIN_FIRST;
				}
				closeProgress();
				handler.sendMessage(msg);
			}
		}.start();
	}

	class TimerThread extends Thread {

		@Override
		public void run() {
			super.run();
			try {
				sleep(3000);
				finish();

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}
}