/* ==================================================
 * 产品名: 亲情快递
 * 文件名: ExpressRecordActivity.java
 * --------------------------------------------------
 * 开发环境: JDK1.6
 * --------------------------------------------------
 * 修订履历    2012/07/07  1.00  初版发行
 * --------------------------------------------------
 * (C) Copyright XIKANG CORPORATION 2012 All Rights Reserved.
 */
package com.xikang.family.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.xikang.channel.familyexpress.rpc.thrift.family.UserBaseInfo;
import com.xikang.family.adapter.RecordListAdapter;
import com.xikang.family.common.Constants;
import com.xikang.family.common.ThriftOpe;
import com.xikang.family.service.ExpressDBService;
import com.xikang.family.service.LocalExpressInfo;

/**
 * 
 * 会话记录页面
 * 
 * 
 * 
 * @author 闫继超
 * @version 1.00
 */

public class ExpressRecordActivity extends BaseActivity {
	private static final String TAG = "ExpressRecordActivity";

	// 推送的广播接收器
	private PushReceiver receiver = null;
	private IntentFilter filter = null;

	final int LOADING_LIST_SUCCESS = 1001; // 加载会话列表成功
	final int LOADING_LIST_FAILED = 1002; // 加载会话列表失败
	final int LOADING_LIST_NULL = 1003; // 会话列表为空
	final int LOADING_UNREAD_SUCCESS = 1004; // 有未读消息（推送）
	// 广播intent
	private final static String FILTER = "com.xikang.mainframe.tabchange";
	// title
	private TextView tvTBContent = null;
	private LayoutInflater inflater = null;
	// 查看添加家人按钮
	private Button btn_addmember = null;

	private ListView list = null;
	private RecordListAdapter adapter = null;
	// DB中取出的聊天记录
	private List<LocalExpressInfo> recordlist = null;
	private List<UserBaseInfo> familylistCache = null;
	private List<UserBaseInfo> familylist = new ArrayList<UserBaseInfo>();
	// 本地DB
	private ExpressDBService db = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_express_record);
		init();
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			// 列表加载成功
			case LOADING_LIST_SUCCESS:
				adapter.setList(recordlist, familylist);
				adapter.notifyDataSetChanged();
				break;
			// 列表加载失败
			case LOADING_LIST_FAILED:
				adapter.setList(recordlist, familylist);
				adapter.notifyDataSetChanged();
				showToastShort(mContext.getString(R.string.loading_failed));
				break;
			// 聊天列表为空
			case LOADING_LIST_NULL:
				adapter.setList(recordlist, familylist);
				adapter.notifyDataSetChanged();
				showToastShort(getString(R.string.record_null));

				break;
			// 刷新未读消息
			case LOADING_UNREAD_SUCCESS:
				adapter.setList(recordlist, familylist);
				adapter.notifyDataSetChanged();
				break;
			default:
				break;
			}
		}

	};

	/**
	 * 初始化控件
	 */

	private void init() {
		inflater = getLayoutInflater();
		tvTBContent = (TextView) findViewById(R.id.tvTBContent);
		tvTBContent.setText(R.string.app_name);
		list = (ListView) findViewById(R.id.recordlist);
		View view = inflater.inflate(R.layout.layout_record_button, null);
		btn_addmember = (Button) view.findViewById(R.id.btn_addmember);
		btn_addmember.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 跳转页面
				mContext.sendBroadcast(new Intent(FILTER));
			}
		});
		list.addFooterView(view);
		adapter = new RecordListAdapter(this, recordlist, familylist);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 进入聊天页面
				Intent intent = new Intent(ExpressRecordActivity.this,
						ChatActivity.class);
				Bundle bundle = new Bundle();
				UserBaseInfo base = familylist.get(position);
				bundle.putString("otheruid", base.getUserId());
				bundle.putString("userName", base.getUserName());
				bundle.putString("relation", base.getRelation());
				intent.putExtras(bundle);
				startActivity(intent);
			}

		});
		// 长按删除会话
		list.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				final int pos = position;
				final UserBaseInfo user = familylist.get(position);
				showResult(
						getString(R.string.del_family_record_title),
						getString(R.string.del_family_record_info)
								.replace(
										"@member",
										user.getRelation().concat("（")
												.concat(user.getUserName())
												.concat("）")),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								db.deleteByUserId(recordlist.get(pos)
										.getOtheruid());
								recordlist.remove(pos);
								setFamilylist();
								adapter.setList(recordlist, familylist);
								adapter.notifyDataSetChanged();
							}

						}, new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

							}

						});
				return true;
			}

		});
		db = new ExpressDBService(mContext);
		filter = new IntentFilter();
		filter.addAction(Constants.ACTION_EXPRESS_NEW);
	}

	@Override
	protected void onResume() {
		super.onResume();
		receiver = new PushReceiver();
		registerReceiver(receiver, filter);
		showProgress(getString(R.string.wait_loading));
		new GetRecordList().start();
	};

	@Override
	protected void onStop() {
		super.onStop();
		if (receiver != null) {
			unregisterReceiver(receiver);
		}
	}

	/**
	 * 不需要下载家人列表
	 */
	private void getRecordFromDB() {
		recordlist = db.findHistory2();
		if (recordlist != null && recordlist.size() > 0) {
			recordlist = listFilter(recordlist);
			setFamilylist();
			adapter.setList(recordlist, familylist);
			adapter.notifyDataSetChanged();
		} else {
			//showToastShort(mContext.getString(R.string.loading_failed));
		}
	}

	/**
	 * 从本地DB中读取历史记录 并生成相对应的家人记录
	 */
	class GetRecordList extends Thread {

		@Override
		public void run() {
			recordlist = listFilter(db.findHistory2());
			ThriftOpe
					.login(mContext, Constants.ACCOUNTNAME, Constants.PASSWORD);
			familylistCache = ThriftOpe.getFamilyMemberDetailList(mContext);
			if (familylistCache != null) {
				setFamilylist();
			}else{
				Log.e(TAG,"getFamilyMemberDetailList null");
			}
			Message msg = Message.obtain();
			if (recordlist != null && recordlist.size() > 0
					&& familylistCache != null) {
				recordlist = listFilter(recordlist);
				msg.what = LOADING_LIST_SUCCESS;
			} else if (familylistCache == null) {
				msg.what = LOADING_LIST_FAILED;
			}else if(recordlist == null){
				msg.what = LOADING_LIST_NULL;
			}
			else {
				msg.what = LOADING_LIST_NULL;
			}
			closeProgress();
			handler.sendMessage(msg);
		}
	}

	/**
	 * 获取未读消息列表
	 */
	class GetUnreadList extends Thread {

		@Override
		public void run() {
			super.run();
			// TODO 目前未提供接口
			Message msg = Message.obtain();
			msg.what = LOADING_UNREAD_SUCCESS;
			handler.sendMessage(msg);
		}
	}

	/**
	 * 根据所有家人列表信息 生成与历史记录对应的家人列表 沒有家人用null代替
	 */
	private void setFamilylist() {
		familylist.clear();
		for (int i = 0; i < recordlist.size(); i++) {
			LocalExpressInfo info = recordlist.get(i);
			for (UserBaseInfo userinfo : familylistCache) {
				String userid = info.getOtheruid();
				if (userid.equals(userinfo.getUserId())) {
					familylist.add(userinfo);
					break;
				}
			}
			if (familylist.size() < i + 1) {
				familylist.add(null);
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return false;
	}

	/**
	 * 推送广播接收器
	 * 
	 * @author 闫继超
	 * @version 1.00
	 */
	class PushReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			Log.i("EXPRESSrECORDACTIVITY", "推送了一个有新消息的广播");
			if (familylistCache == null) {
				showProgress(getString(R.string.wait_loading));
				new GetRecordList().start();
			} else {
				getRecordFromDB();
			}
		}
	}

	/**
	 * 过滤同一个人的多条最后时间相同的信息
	 * 
	 * @param infos
	 * @return
	 */
	private List<LocalExpressInfo> listFilter(List<LocalExpressInfo> infos) {
		for (int i = 0; i < infos.size(); i++) {
			for (int j = i + 1; j < infos.size(); j++) {
				if ((infos.get(i).getOtheruid().equals(infos.get(j)
						.getOtheruid()))
						&& (infos.get(i).getExpressinfo().getTime()
								.equals(infos.get(j).getExpressinfo().getTime()))) {
					if (Integer.parseInt(infos.get(i).getExpressinfo()
							.getExpressId()) > Integer.parseInt(infos.get(j)
							.getExpressinfo().getExpressId())) {
						infos.remove(j);
					} else {
						infos.remove(i);
					}
				}
			}
		}
		return infos;
	}
}
