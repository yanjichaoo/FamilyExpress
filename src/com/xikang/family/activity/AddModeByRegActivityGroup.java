/* ==================================================
 * 产品名: 亲情快递
 * 文件名: AddModeByRegActivityGroup.java
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
import android.widget.LinearLayout;
import com.xikang.family.common.ActivityJump;

/**
 * 
 * 注册方式添加家人的activityGroup
 * 
 * 
 * 
 * @author 闫继超
 * @version 1.00
 */

public class AddModeByRegActivityGroup extends ActivityGroup {

	private LinearLayout layout = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_activity_group);
		layout = (LinearLayout) findViewById(R.id.layoutAG);
		// 将activity加载进layout中
		ActivityJump.JumpAddModeByRegEditActivity(this, layout);
	}

	/**
	 * 返回键实现方式根本具体的activity实现
	 */
	@Override
	public void onBackPressed() {
		getCurrentActivity().onBackPressed();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		AddModeByRegEditActivity ac = (AddModeByRegEditActivity) getLocalActivityManager()
				.getCurrentActivity();
		ac.handleActivityResult(requestCode, resultCode, data);
	}

}
