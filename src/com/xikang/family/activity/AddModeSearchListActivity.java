/* ==================================================
 * 产品名: 亲情快递
 * 文件名: AddModeSearchListActivity.java
 * --------------------------------------------------
 * 开发环境: JDK1.6
 * --------------------------------------------------
 * 修订履历    2012/07/07  1.00  初版发行
 * --------------------------------------------------
 * (C) Copyright XIKANG CORPORATION 2012 All Rights Reserved.
 */
package com.xikang.family.activity;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xikang.channel.base.rpc.thrift.user.UserInfo;
import com.xikang.family.adapter.SearchListAdapter;
import com.xikang.family.common.Constants;
import com.xikang.family.common.ThriftOpe;

/**
 * 
 * 根据指定信息搜索家人列表页面
 * 
 * 
 * 
 * @author 闫继超
 * @version 1.00
 */

public class AddModeSearchListActivity extends BaseActivity implements
		OnClickListener {
	// 成功
	final int LOADING_SUCCESS = 1001;
	// 失败
	final int LOADING_FAILED = 1002;
	// 无信息
	final int LOADING_NULL = 1003;
	// title左按鈕
	private Button btnTBLeft = null;
	// title右按钮
	private RelativeLayout btnTBRight = null;
	// 搜索按鈕
	private Button btn_search = null;
	// 搜索信息
	private EditText searchText = null;
	// 显示搜索信息
	private TextView searchInfo = null;
	// 搜索文本
	private String search = null;
	// 缓存的搜索文本（不为空后保存为search）
	private String cachesearch = null;
	// listview相关
	private ListView list = null;
	private SearchListAdapter adapter = null;
	// adapter数据
	private List<UserInfo> searchList = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_mode_user_list);
		// 获取搜索信息
		search = getIntent().getExtras().getString("search");
		// 初始化控件
		init();
		// 搜索
		exeSearch();
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case LOADING_SUCCESS:
				setSearchInfo(search);
				adapter.setList(searchList);
				adapter.notifyDataSetChanged();
				break;
			case LOADING_FAILED:
				setSearchInfo(search);
				searchList.clear();
				adapter.setList(searchList);
				adapter.notifyDataSetChanged();
				showToastShort(mContext.getString(R.string.search_error1));
				break;
			case LOADING_NULL:
				setSearchInfo(search);
				searchList.clear();
				adapter.setList(searchList);
				adapter.notifyDataSetChanged();
				showToastShort(mContext.getString(R.string.search_error2));
				break;
			default:
				break;

			}
		}

	};

	private void init() {

		btnTBLeft = (Button) findViewById(R.id.btnTBLeft);
		btnTBLeft.setOnClickListener(this);
		btnTBRight = (RelativeLayout) findViewById(R.id.btnTBRight);
		btnTBRight.setOnClickListener(this);
		btn_search = (Button) findViewById(R.id.btn_search);
		btn_search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				cachesearch = searchText.getText().toString();
				if (cachesearch != null && !"".equals(cachesearch)) {
					// 判断缓存信息是否可用
					search = cachesearch;
					// 搜索
					exeSearch();
				}
			}
		});
		searchText = (EditText) findViewById(R.id.searchText);
		searchInfo = (TextView) findViewById(R.id.search_info);

		list = (ListView) findViewById(R.id.searchlist);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Bundle bundle = new Bundle();
				bundle.putString("tag", "user");
				bundle.putString("key", "confirm");
				bundle.putString("tabTag", "user");
				UserInfo info = searchList.get(position);
				bundle.putString("otheruid", info.getUserId());
				bundle.putString("email", info.getEmail());
				bundle.putString("pet", info.getUserName());
				bundle.putString("url", info.getFigureUrl());
				bundle.putString("phonenum", info.getMobileNum());
				Intent intent = new Intent(AddModeSearchListActivity.this,
						AddMainFrameActivity.class);
				intent.putExtras(bundle);
				startActivity(intent);
			}

		});
		adapter = new SearchListAdapter(this, searchList);
		list.setAdapter(adapter);
	};

	/**
	 * 显示搜索信息
	 * 
	 * @param msg
	 */
	private void setSearchInfo(String msg) {
		searchInfo.setText(getString(R.string.search_info).replace("XXX", msg));
	}

	/**
	 * 搜索指定内容
	 */
	private void exeSearch() {
		showProgress(getString(R.string.wait_search));
		new Thread() {

			@Override
			public void run() {
				super.run();
				ThriftOpe.login(mContext, Constants.ACCOUNTNAME,
						Constants.PASSWORD);
				searchList = ThriftOpe.searchUsers(mContext, search);
				Message msg = Message.obtain();
				if (searchList != null && searchList.size() > 0) {
					msg.what = LOADING_SUCCESS;
				} else if (searchList == null) {
					msg.what = LOADING_FAILED;
				} else {
					msg.what = LOADING_NULL;
				}
				closeProgress();
				handler.sendMessage(msg);
			}

		}.start();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnTBLeft:
			this.finish();
			break;
		case R.id.btnTBRight:
			startActivity(new Intent(this, FamilyMainFrameActivity.class));
			break;
		default:
			break;
		}
	}

}
