/* ==================================================
 * 产品名: 亲情快递
 * 文件名: SettingVersionActivity.java
 * --------------------------------------------------
 * 开发环境: JDK1.6
 * --------------------------------------------------
 * 修订履历    2012/07/07  1.00  初版发行
 * --------------------------------------------------
 * (C) Copyright XIKANG CORPORATION 2012 All Rights Reserved.
 */
package com.xikang.family.activity;

import com.xikang.channel.base.rpc.thrift.app.VersionInfo;
import com.xikang.channel.base.rpc.thrift.app.VersionStatus;
import com.xikang.family.common.ActivityJump;
import com.xikang.family.common.ThriftOpe;
import com.xikang.family.common.Util;

import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnKeyListener;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * 程序版本页面
 * 
 * 
 * 
 * @author 张荣
 * @version 1.00
 */

public class SettingVersionActivity extends BaseActivity {

	private Resources mRes;

	private TextView tvTBContent = null;
	private Button btnTBLeft = null;
	private Button btnCheck = null;

	private LayoutInflater inflater = null;
	private AlertDialog updateDialog = null;

	private TextView version = null;
	// 版本信息
	private VersionInfo versionInfo = null;

	private static final int VERSION_SUCCESS = 2001;
	private static final int VERSION_FAILED = 2002;

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case VERSION_SUCCESS:
				if (versionInfo.currentStatus == VersionStatus.RECOMMENDED) {
					Toast.makeText(SettingVersionActivity.this,
							R.string.check_info1, Toast.LENGTH_LONG).show();
				} else {
					showUpdateDialog();
				}
				break;
			case VERSION_FAILED:
				showResult(mRes.getString(R.string.update_error),
						mRes.getString(R.string.version_error),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}

						}, new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_setting_version);
		init();
	}

	private void init() {
		inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		mRes = getResources();

		tvTBContent = (TextView) findViewById(R.id.tvTBContent);
		tvTBContent.setText(R.string.setting_info_2);
		btnTBLeft = (Button) findViewById(R.id.btnTBLeft);

		btnCheck = (Button) findViewById(R.id.btn_check);

		btnTBLeft.setVisibility(View.VISIBLE);

		btnTBLeft.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ActivityGroup ag = (ActivityGroup) SettingVersionActivity.this
						.getParent();
				ActivityJump.JumpSettingActivity(ag, (LinearLayout) ag
						.getWindow().findViewById(R.id.layoutAG));
			}
		});

		btnCheck.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showGroupProgress(mRes.getString(R.string.check_version));
				getVersionInfo();
			}
		});
		version = (TextView) findViewById(R.id.app_version);
		version.setText("v ".concat(Util.getPackageVersion(this)));
	}

	private void getVersionInfo() {
		new Thread() {

			@Override
			public void run() {
				super.run();
				Message msg = Message.obtain();
				VersionInfo vi = ThriftOpe
						.validateVersion(SettingVersionActivity.this);

				if (vi != null) {
					msg.what = VERSION_SUCCESS;
					versionInfo = vi;
				} else {
					msg.what = VERSION_FAILED;
				}
				closeGroupProgress();
				handler.sendMessage(msg);
			}
		}.start();
	}

	private void showUpdateDialog() {
		updateDialog = new AlertDialog.Builder(this.getParent()).create();
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
					showGroupProgress(SettingVersionActivity.this
							.getString(R.string.download));
					new Thread() {

						@Override
						public void run() {
							super.run();
							Util.downloadAPK(versionInfo.linkUrl,
									SettingVersionActivity.this);
							closeGroupProgress();
						}

					}.start();
				}
			});
			button_exit.setText(R.string.update_skip);
			button_exit.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					updateDialog.dismiss();
				}
			});
		}
		if (versionInfo.currentStatus == VersionStatus.INCOMPATIBLE) {
			button_download.setText(mRes.getString(R.string.update_download)
					.replace("@version", versionInfo.recommendedVersion));
			button_download.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					showGroupProgress(SettingVersionActivity.this
							.getString(R.string.download));
					new Thread() {

						@Override
						public void run() {
							super.run();
							Util.downloadAPK(versionInfo.linkUrl,
									SettingVersionActivity.this);
							closeGroupProgress();
						}
					}.start();
				}
			});
			button_exit.setText(R.string.update_exit);
			button_exit.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					updateDialog.dismiss();
					getApplicationContext().sendBroadcast(new Intent("finish"));
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
					}
					if (versionInfo.currentStatus == VersionStatus.COMPATIBLE) {
						dialog.dismiss();
					}
				}
				return false;
			}
		});

		updateDialog.show();
	}

}
