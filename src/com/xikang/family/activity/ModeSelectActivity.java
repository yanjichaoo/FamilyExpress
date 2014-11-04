/* ==================================================
 * 产品名: 亲情快递
 * 文件名: ModeSelectActivity.java
 * --------------------------------------------------
 * 开发环境: JDK1.6
 * --------------------------------------------------
 * 修订履历    2012/07/07  1.00  初版发行
 * --------------------------------------------------
 * (C) Copyright XIKANG CORPORATION 2012 All Rights Reserved.
 */
package com.xikang.family.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 
 * 添加家人模式选择页面
 * 
 * 
 * 
 * @author 张荣
 * @version 1.00
 */

public class ModeSelectActivity extends BaseActivity implements OnClickListener {
	private RelativeLayout btn_jump = null;
	private TextView btn_mode_1 = null;
	private TextView btn_mode_2 = null;
	private TextView btn_mode_3 = null;

	private Bundle bundle = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_modeselect);
		init();
	}

	public void init() {
		btn_jump = (RelativeLayout) findViewById(R.id.btn_jump);
		btn_jump.setOnClickListener(this);
		btn_mode_1 = (TextView) findViewById(R.id.btn_mode_1);
		btn_mode_1.setOnClickListener(this);
		btn_mode_2 = (TextView) findViewById(R.id.btn_mode_2);
		btn_mode_2.setOnClickListener(this);
		btn_mode_3 = (TextView) findViewById(R.id.btn_mode_3);
		btn_mode_3.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_mode_1:
			Intent intent1 = new Intent(ModeSelectActivity.this,
					AddMainFrameActivity.class);
			bundle = new Bundle();
			bundle.putString("tag", "regist");
			intent1.putExtras(bundle);
			startActivity(intent1);
			break;
		case R.id.btn_mode_2:
			Intent intent2 = new Intent(ModeSelectActivity.this,
					AddMainFrameActivity.class);
			bundle = new Bundle();
			bundle.putString("tag", "user");
			intent2.putExtras(bundle);
			startActivity(intent2);
			break;
		case R.id.btn_mode_3:
			Intent intent3 = new Intent(ModeSelectActivity.this,
					AddMainFrameActivity.class);
			bundle = new Bundle();
			bundle.putString("tag", "userandpwd");
			intent3.putExtras(bundle);
			startActivity(intent3);
			break;
		case R.id.btn_jump:
			Intent intent = new Intent(ModeSelectActivity.this,
					FamilyMainFrameActivity.class);
			intent.putExtra("tag", "family");
			startActivity(intent);
			break;
		default:
			break;
		}
	};

}
