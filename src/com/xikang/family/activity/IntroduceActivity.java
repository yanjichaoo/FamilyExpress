/* ==================================================
 * 产品名: 亲情快递
 * 文件名: IntroduceActivity.java
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
import android.widget.TextView;

import com.xikang.family.view.MyScrollLayout;
import com.xikang.family.view.MyScrollLayout.SelectListener;
import com.xikang.family.view.MyScrollLayout.UnSelectListener;

/**
 * 
 * 介绍页面
 * 
 * 
 * 
 * @author 张荣
 * @version 1.00
 */

public class IntroduceActivity extends BaseActivity implements OnClickListener {

	private TextView btn_login = null;
	private TextView btn_regist = null;
	private MyScrollLayout layout = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_introduce);
		init();
	}

	public void init() {
		btn_login = (TextView) findViewById(R.id.btn_login);
		btn_login.setOnClickListener(this);
		btn_regist = (TextView) findViewById(R.id.btn_regist);
		btn_regist.setOnClickListener(this);
		layout = (MyScrollLayout) findViewById(R.id.scrollview);
		layout.setSelectListener(new SelectListener() {

			@Override
			public void select() {
				btn_regist.setPressed(true);
			}
		});
		layout.setUnSelectListener(new UnSelectListener() {

			@Override
			public void unSelect() {
				btn_regist.setPressed(false);
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_login:
			startActivity(new Intent(this, LoginActivity.class));
			break;
		case R.id.btn_regist:
			startActivity(new Intent(this, RegistActivity.class));
			break;
		default:
			break;
		}
	}

}
