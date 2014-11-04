/* ==================================================
 * 产品名: 亲情快递
 * 文件名: AddModeByUserActivityGroup.java
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
import android.widget.LinearLayout;

/**
 * 
 * 通过搜索方式添加家人activityGroup
 * 
 * 
 * 
 * @author 闫继超
 * @version 1.00
 */

public class AddModeByUserActivityGroup extends ActivityGroup {
	private LinearLayout layout = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_activity_group);
		layout = (LinearLayout) findViewById(R.id.layoutAG);
		Bundle bundle = getIntent().getExtras();
		if (bundle == null) {
			// 正常进入的方式
			ActivityJump.JumpAddModeByUserActivity(this, layout);
		} else {
			// 从搜索列表点击指定人进入确认页面
			ActivityJump.JumpAddModeByConfirmActivity(this, layout, bundle);
		}
	}
}
