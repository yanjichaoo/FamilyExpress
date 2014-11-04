/* ==================================================
 * 产品名: 亲情快递
 * 文件名: AddModeByUserActivity.java
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
import android.widget.Button;
import android.widget.EditText;

/**
 * 
 * 通过相关信息搜索页面
 * 
 * 
 * 
 * @author 闫继超
 * @version 1.00
 */

public class AddModeByUserActivity extends BaseActivity {
	// 搜索按钮
	private Button btn_search = null;
	// 搜索信息
	private EditText searchText = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_mode_user_search);
		init();
	}

	/**
	 * 初始化控件
	 */
	private void init() {
		btn_search = (Button) findViewById(R.id.btn_search);
		btn_search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String search = searchText.getText().toString().trim();
				// 判断输入框是否输入信息
				if (search != null && !"".equals(search.trim())) {
					Intent intent = new Intent(AddModeByUserActivity.this,
							AddModeSearchListActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("search", search);
					intent.putExtras(bundle);
					startActivity(intent);
				} else {
					// 提示用户输入信息
					showToastShort(mContext.getString(R.string.search_null));
				}
			}
		});
		searchText = (EditText) findViewById(R.id.searchText);
	};

}
