/* ==================================================
 * 产品名: 亲情快递
 * 文件名: SettingHoldActivity.java
 * --------------------------------------------------
 * 开发环境: JDK1.6
 * --------------------------------------------------
 * 修订履历    2012/07/07  1.00  初版发行
 * --------------------------------------------------
 * (C) Copyright XIKANG CORPORATION 2012 All Rights Reserved.
 */
package com.xikang.family.activity;

import com.xikang.family.common.ActivityJump;
import com.xikang.family.common.Constants;

import android.app.ActivityGroup;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 
 * 设置保存时间页面
 * 
 * 
 * 
 * @author 张荣
 * @version 1.00
 */

public class SettingHoldActivity extends BaseActivity implements
		OnClickListener {

	private CheckBox check_month = null;
	private CheckBox check_halfyear = null;
	private CheckBox check_oneyear = null;
	private CheckBox check_always = null;

	private TextView tvTBContent = null;
	private Button btnTBLeft = null;

	private SharedPreferences pref = null;
	private int holdTerm = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_setting_hold);
		init();
	}

	private void init() {
		check_month = (CheckBox) findViewById(R.id.check_month);
		check_month.setOnClickListener(this);
		check_halfyear = (CheckBox) findViewById(R.id.check_halfyear);
		check_halfyear.setOnClickListener(this);
		check_oneyear = (CheckBox) findViewById(R.id.check_oneyear);
		check_oneyear.setOnClickListener(this);
		check_always = (CheckBox) findViewById(R.id.check_always);
		check_always.setOnClickListener(this);

		tvTBContent = (TextView) findViewById(R.id.tvTBContent);
		tvTBContent.setText(R.string.setting_info_1);
		btnTBLeft = (Button) findViewById(R.id.btnTBLeft);
		btnTBLeft.setVisibility(View.VISIBLE);
		btnTBLeft.setOnClickListener(this);

		pref = this.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME,
				Context.MODE_PRIVATE);
		setCheckItems();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnTBLeft:
			ActivityGroup ag = (ActivityGroup) SettingHoldActivity.this
					.getParent();
			ActivityJump.JumpSettingActivity(ag, (LinearLayout) ag.getWindow()
					.findViewById(R.id.layoutAG));
			break;
		case R.id.check_month:
			check_month.setChecked(true);
			check_halfyear.setChecked(false);
			check_oneyear.setChecked(false);
			check_always.setChecked(false);
			saveCheckItemIndex(1);
			break;
		case R.id.check_halfyear:
			check_month.setChecked(false);
			check_halfyear.setChecked(true);
			check_oneyear.setChecked(false);
			check_always.setChecked(false);
			saveCheckItemIndex(2);
			break;
		case R.id.check_oneyear:
			check_month.setChecked(false);
			check_halfyear.setChecked(false);
			check_oneyear.setChecked(true);
			check_always.setChecked(false);
			saveCheckItemIndex(3);
			break;
		case R.id.check_always:
			check_month.setChecked(false);
			check_halfyear.setChecked(false);
			check_oneyear.setChecked(false);
			check_always.setChecked(true);
			saveCheckItemIndex(4);
			break;
		default:
			break;
		}
	};

	private void setCheckItems() {
		holdTerm = pref.getInt(Constants.HOLD_TERM, 0);
		switch (holdTerm) {

		case 1:
			check_month.setChecked(true);
			check_halfyear.setChecked(false);
			check_oneyear.setChecked(false);
			check_always.setChecked(false);
			break;
		case 2:
			check_month.setChecked(false);
			check_halfyear.setChecked(true);
			check_oneyear.setChecked(false);
			check_always.setChecked(false);
			break;
		case 3:
			check_month.setChecked(false);
			check_halfyear.setChecked(false);
			check_oneyear.setChecked(true);
			check_always.setChecked(false);
			break;
		case 4:
			check_month.setChecked(false);
			check_halfyear.setChecked(false);
			check_oneyear.setChecked(false);
			check_always.setChecked(true);
			break;
		default:
			check_month.setChecked(true);
			check_halfyear.setChecked(false);
			check_oneyear.setChecked(false);
			check_always.setChecked(false);
			break;
		}

	}

	private void saveCheckItemIndex(int index) {
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt(Constants.HOLD_TERM, index);
		editor.commit();
	}
	
}
