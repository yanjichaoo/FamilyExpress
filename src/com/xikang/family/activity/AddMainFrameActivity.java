/* ==================================================
 * 产品名: 亲情快递
 * 文件名: AddMainFrameActivity.java
 * --------------------------------------------------
 * 开发环境: JDK1.6
 * --------------------------------------------------
 * 修订履历    2012/07/07  1.00  初版发行
 * --------------------------------------------------
 * (C) Copyright XIKANG CORPORATION 2012 All Rights Reserved.
 */
package com.xikang.family.activity;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

/**
 * 
 * 添加家人主框架页面
 * 
 * 
 * 
 * @author 闫继超
 * @version 1.00
 */

public class AddMainFrameActivity extends TabActivity implements
		OnClickListener {
	// title左按钮
	private Button btnTBLeft = null;
	// title右按钮
	private RelativeLayout btnTBRight = null;
	// tab相关
	private TabHost mTabHost;
	private TabSpec mTabSpec;

	private Context mContext;
	// 替代tabwidget的 跳转按钮
	private Button btn_mode_reg = null;
	private Button btn_mode_user = null;
	private Button btn_mode_userandpwd = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_add_mainframe);
		mContext = getBaseContext();
		init();
		mTabHost = getTabHost();
		createTabFrame();
	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	/**
	 * 初始化控件
	 */
	public void init() {
		btn_mode_reg = (Button) findViewById(R.id.btn_mode_reg);
		btn_mode_reg.setOnClickListener(this);
		btn_mode_user = (Button) findViewById(R.id.btn_mode_user);
		btn_mode_user.setOnClickListener(this);
		btn_mode_userandpwd = (Button) findViewById(R.id.btn_mode_userandpwd);
		btn_mode_userandpwd.setOnClickListener(this);

		btnTBLeft = (Button) findViewById(R.id.btnTBLeft);
		btnTBLeft.setOnClickListener(this);
		btnTBRight = (RelativeLayout) findViewById(R.id.btnTBRight);
		btnTBRight.setOnClickListener(this);
	}

	/**
	 * 初始化tab
	 */
	private void createTabFrame() {
		Intent mIntet;
		Bundle bundle = getIntent().getExtras();
		// 注册添加activity组
		mIntet = new Intent(mContext, AddModeByRegActivityGroup.class);
		addTab(mIntet, "regist");
		// 搜索添加activity组
		mIntet = new Intent(mContext, AddModeByUserActivityGroup.class);
		if (bundle != null) {
			if ("confirm".equals(bundle.getString("key"))) {
				// 控制页面跳转的判断条件
				mIntet.putExtras(bundle);
			}
		}
		addTab(mIntet, "user");
		// 帐号密码认证添加activity组
		mIntet = new Intent(mContext, AddModeByUserAndPwdActivityGroup.class);
		addTab(mIntet, "userandpwd");
		// 根据进入页面设置页面信息
		if (bundle != null) {
			String tag = bundle.getString("tag");
			if ("regist".equals(tag)) {
				jumpTab1();
			}
			if ("user".equals(tag)) {
				jumpTab2();
			}
			if ("userandpwd".equals(tag)) {
				jumpTab3();
			}
		}
	}

	/**
	 * 添加tab項
	 * 
	 * @param intent
	 * @param tag
	 */
	private void addTab(Intent intent, String tag) {
		mTabSpec = mTabHost.newTabSpec(tag).setIndicator(new View(mContext))
				.setContent(intent);
		mTabHost.addTab(mTabSpec);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_mode_reg:
			jumpTab1();
			break;
		case R.id.btn_mode_user:
			jumpTab2();
			break;
		case R.id.btn_mode_userandpwd:
			jumpTab3();
			break;
		case R.id.btnTBLeft:
			this.finish();
			break;
		case R.id.btnTBRight:
			Intent intent = new Intent(this, FamilyMainFrameActivity.class);
			intent.putExtra("tag", "family");
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	private void jumpTab1() {
		btn_mode_reg.setBackgroundResource(R.drawable.mode_icon_reg_check);
		btn_mode_user.setBackgroundResource(R.drawable.mode_icon_user);
		btn_mode_userandpwd
				.setBackgroundResource(R.drawable.mode_icon_userandpwd);
		this.mTabHost.setCurrentTabByTag("regist");
	}

	private void jumpTab2() {
		btn_mode_reg.setBackgroundResource(R.drawable.mode_icon_reg);
		btn_mode_user.setBackgroundResource(R.drawable.mode_icon_user_check);
		btn_mode_userandpwd
				.setBackgroundResource(R.drawable.mode_icon_userandpwd);
		this.mTabHost.setCurrentTabByTag("user");
	}

	private void jumpTab3() {
		btn_mode_reg.setBackgroundResource(R.drawable.mode_icon_reg);
		btn_mode_user.setBackgroundResource(R.drawable.mode_icon_user);
		btn_mode_userandpwd
				.setBackgroundResource(R.drawable.mode_icon_userandpwd_check);
		this.mTabHost.setCurrentTabByTag("userandpwd");
	}

}
