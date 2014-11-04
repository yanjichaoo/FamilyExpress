/* ==================================================
 * 产品名: 亲情快递
 * 文件名: SettingActivity.java
 * --------------------------------------------------
 * 开发环境: JDK1.6
 * --------------------------------------------------
 * 修订履历    2012/07/07  1.00  初版发行
 * --------------------------------------------------
 * (C) Copyright XIKANG CORPORATION 2012 All Rights Reserved.
 */
package com.xikang.family.activity;

import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xikang.family.common.ActivityJump;
import com.xikang.family.common.CPSClientUtil;
import com.xikang.family.common.Constants;
import com.xikang.family.common.ThriftOpe;

/**
 * 
 * 设置页面
 * 
 * 
 * 
 * @author 张荣
 * @version 1.00
 */

public class SettingActivity extends BaseActivity implements OnClickListener {

	ActivityGroup ag;

	private TextView tvTBContent = null;

	private FrameLayout btn_hold = null;
	private FrameLayout btn_version = null;
	private Button btn_quit = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_setting);
		init();
	}

	private void init() {
		tvTBContent = (TextView) findViewById(R.id.tvTBContent);
		tvTBContent.setText(R.string.common_setting);
		btn_hold = (FrameLayout) findViewById(R.id.btn_hold);
		btn_hold.setOnClickListener(this);
		btn_version = (FrameLayout) findViewById(R.id.btn_version);
		btn_version.setOnClickListener(this);
		btn_quit = (Button) findViewById(R.id.btn_quit);
		btn_quit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_hold:
			ag = (ActivityGroup) this.getParent();
			ActivityJump.JumpSettingHoldActivity(ag, (LinearLayout) ag
					.getWindow().findViewById(R.id.layoutAG));
			break;
		case R.id.btn_version:
			ag = (ActivityGroup) this.getParent();
			ActivityJump.JumpSettingVersionActivity(ag, (LinearLayout) ag
					.getWindow().findViewById(R.id.layoutAG));
			break;
		case R.id.btn_quit:
			StrictMode.ThreadPolicy old = StrictMode.getThreadPolicy();
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder(old)
					.permitNetwork().build());
			ThriftOpe.login(this, Constants.ACCOUNTNAME, Constants.PASSWORD);
			ThriftOpe.logout(this, Constants.ACCOUNTNAME, Constants.PASSWORD);
			StrictMode.setThreadPolicy(old);
			CPSClientUtil.disconnectCPS(getApplicationContext());
			getApplicationContext().sendBroadcast(new Intent("finish"));
			break;
		default:
			break;
		}
	};

}
