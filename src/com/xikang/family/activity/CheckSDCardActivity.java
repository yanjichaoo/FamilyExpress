/* ==================================================
 * 产品名: 亲情快递
 * 文件名: CheckSDCardActivity.java
 * --------------------------------------------------
 * 开发环境: JDK1.6
 * --------------------------------------------------
 * 修订履历    2012/07/07  1.00  初版发行
 * --------------------------------------------------
 * (C) Copyright XIKANG CORPORATION 2012 All Rights Reserved.
 */
package com.xikang.family.activity;

import com.xikang.family.common.Constants;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;

/**
 * 
 * 检查SD剩余大小
 * 
 * 
 * 
 * @author 张荣
 * @version 1.00
 */

public class CheckSDCardActivity extends BaseActivity implements
		OnClickListener {

	private CheckBox check = null;
	private Button close = null;
	SharedPreferences pref = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_sdcard_remind);
		pref = this.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME,
				Context.MODE_PRIVATE);
		check = (CheckBox) findViewById(R.id.check_know);
		if (pref.getString(Constants.IS_SDCARD_REMIND, "no").equals("yes")) {
			check.setChecked(true);
		} else {
			check.setChecked(false);
		}
		check.setOnClickListener(this);

		close = (Button) findViewById(R.id.close);
		close.setOnClickListener(this);

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			this.setResult(0);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		SharedPreferences.Editor editor = pref.edit();
		switch (v.getId()) {
		case R.id.check_know:
			if (check.isChecked()) {
				editor.putString(Constants.IS_SDCARD_REMIND, "yes");
			} else {
				editor.putString(Constants.IS_SDCARD_REMIND, "no");
			}
			editor.commit();
			break;
		case R.id.close:
			finish();
			break;
		default:
			break;
		}
	}

}
