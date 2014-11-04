/* ==================================================
 * 产品名: 亲情快递
 * 文件名: SettingActivityGroup.java
 * --------------------------------------------------
 * 开发环境: JDK1.6
 * --------------------------------------------------
 * 修订履历    2012/07/07  1.00  初版发行
 * --------------------------------------------------
 * (C) Copyright XIKANG CORPORATION 2012 All Rights Reserved.
 */
package com.xikang.family.activity;

import com.xikang.family.common.ActivityJump;

import android.app.ActivityGroup;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.LinearLayout;

/**
 * 
 * 设置activitygroup
 * 
 * 
 * 
 * @author 张荣
 * @version 1.00
 */

public class SettingActivityGroup extends ActivityGroup {

	LinearLayout layout = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_activity_group);
		layout = (LinearLayout) findViewById(R.id.layoutAG);
		ActivityJump.JumpSettingActivity(this, layout);
	}

	@Override
	protected void onResume() {
		super.onResume();
		ActivityJump.JumpSettingActivity(this, layout);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return false;
	}
}
