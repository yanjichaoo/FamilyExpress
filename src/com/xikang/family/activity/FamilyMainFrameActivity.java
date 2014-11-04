/* ==================================================
 * 产品名: 亲情快递
 * 文件名: FamilyMainFrameActivity.java
 * --------------------------------------------------
 * 开发环境: JDK1.6
 * --------------------------------------------------
 * 修订履历    2012/07/07  1.00  初版发行
 * --------------------------------------------------
 * (C) Copyright XIKANG CORPORATION 2012 All Rights Reserved.
 */
package com.xikang.family.activity;

import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.TabHost.TabSpec;

/**
 * 
 * 家人主框架页面
 * 
 * 
 * 
 * @author 闫继超
 * @version 1.00
 */

public class FamilyMainFrameActivity extends TabActivity implements
		OnClickListener {

	// 广播intent
	private final static String FILTER = "com.xikang.mainframe.tabchange";

	public static TabHost mTabHost = null;
	private TabSpec mTabSpec = null;
	private Context mContext = null;

	private RadioGroup tabgroup = null;
	private RadioButton tab_record = null;
	private RadioButton tab_family = null;
	private RadioButton tab_setting = null;

	private TabChangeListener listener = null;
	private IntentFilter filter = null;

	private Handler handler = new Handler();

	private Boolean isExit = false;

	Runnable runExit = new Runnable() {
		@Override
		public void run() {
			isExit = false;
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_family_mainframe);
		mContext = getBaseContext();
		init();
		mTabHost = getTabHost();
		createTabFrame();
		String tag = getIntent().getStringExtra("tag");
		if (tag != null && "family".equals(tag)) {
			pressFamily();
		}
	}

	public void init() {
		tabgroup = (RadioGroup) findViewById(R.id.tabgroup);
		tab_record = (RadioButton) findViewById(R.id.tab_record);
		tab_record.setOnClickListener(this);
		tab_family = (RadioButton) findViewById(R.id.tab_family);
		tab_family.setOnClickListener(this);
		tab_setting = (RadioButton) findViewById(R.id.tab_setting);
		tab_setting.setOnClickListener(this);
		listener = new TabChangeListener();
		filter = new IntentFilter(FILTER);

	}

	@Override
	protected void onResume() {
		super.onResume();
		this.registerReceiver(listener, filter);
	}

	@Override
	protected void onStop() {
		super.onStop();
		this.unregisterReceiver(listener);
	}

	private void createTabFrame() {
		Intent mIntet;
		mIntet = new Intent(mContext, ExpressRecordActivity.class);
		addTab(mIntet, "record");
		mIntet = new Intent(mContext, FamilyActivityGroup.class);
		addTab(mIntet, "family");
		mIntet = new Intent(mContext, SettingActivityGroup.class);
		addTab(mIntet, "setting");
	}

	private void addTab(Intent intent, String tag) {
		mTabSpec = mTabHost.newTabSpec(tag).setIndicator(new View(mContext))
				.setContent(intent);
		mTabHost.addTab(mTabSpec);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.tab_record:
			pressRecord();
			break;
		case R.id.tab_family:
			pressFamily();
			break;
		case R.id.tab_setting:
			pressSetting();
		default:
			break;
		}
	}

	/**
	 * 点击会话记录按鈕
	 */
	private void pressRecord() {
		tab_record.setTextColor(Color.WHITE);
		tab_family.setTextColor(Color.GRAY);
		tab_setting.setTextColor(Color.GRAY);
		mTabHost.setCurrentTabByTag("record");
	}

	/**
	 * 点击家人关系按鈕
	 */
	private void pressFamily() {
		tab_record.setTextColor(Color.GRAY);
		tab_family.setTextColor(Color.WHITE);
		tab_setting.setTextColor(Color.GRAY);
		mTabHost.setCurrentTabByTag("family");
		tab_family.setChecked(true);
	}

	/**
	 * 点击设置按鈕
	 */
	private void pressSetting() {
		tab_record.setTextColor(Color.GRAY);
		tab_family.setTextColor(Color.GRAY);
		tab_setting.setTextColor(Color.WHITE);
		mTabHost.setCurrentTabByTag("setting");
	}

	/**
	 * tab页面切换
	 * 
	 * @author 闫继超
	 */
	class TabChangeListener extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			pressFamily();
			tabgroup.check(R.id.tab_family);

		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		/**
		 * 2秒内连续2次back 退出程序
		 */
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (isExit == false) {
				isExit = true;
				Toast.makeText(this, getString(R.string.common_exit),
						Toast.LENGTH_SHORT).show();
				handler.postDelayed(runExit, 2000);
			} else {
				sendBroadcast(new Intent("finish"));
			}
		}
		return false;
	}
}
