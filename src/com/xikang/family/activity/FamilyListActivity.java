/* ==================================================
 * 产品名: 亲情快递
 * 文件名: FamilyListActivity.java
 * --------------------------------------------------
 * 开发环境: JDK1.6
 * --------------------------------------------------
 * 修订履历    2012/07/07  1.00  初版发行
 * --------------------------------------------------
 * (C) Copyright XIKANG CORPORATION 2012 All Rights Reserved.
 */
package com.xikang.family.activity;

import java.util.List;

import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.xikang.channel.familyexpress.rpc.thrift.family.AllowStatus;
import com.xikang.channel.familyexpress.rpc.thrift.family.UserBaseInfo;
import com.xikang.family.adapter.FamilyListAdapter;
import com.xikang.family.common.ActivityJump;
import com.xikang.family.common.Constants;
import com.xikang.family.common.ThriftOpe;
import com.xikang.family.service.ExpressDBService;

/**
 * 
 * 家人列表页面
 * 
 * 
 * 
 * @author 张荣
 * @version 1.00
 */

public class FamilyListActivity extends BaseActivity {

	private static final int MESSAGE_LOADING_ONE = 2001;
	private static final int MESSAGE_LOADING_TWO_READY = 2002;
	private static final int MESSAGE_LOADING_MANY = 2004;
	private static final int MESSAGE_LOADING_NONE = 2005;

	// 布局加载器
	//private LayoutInflater inflater = null;

	//private PopupWindow notePopWin = null;
	//private View notePopview = null;
	private TextView noteText = null;
	private ImageView noteArrow = null;

	private ListView familyListView = null;
	private TextView tvTBContent = null;
	private Button btnTBRight = null;
	private List<UserBaseInfo> familyMemberList = null;

	private ExpressDBService db = null;

	private FamilyListAdapter adapter = null;

	private boolean isOver = true;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			isOver = true;
			closeGroupProgress();
			switch (msg.what) {

			case MESSAGE_LOADING_ONE:
				// 只有快递小助手
				adapter.setList(familyMemberList);
				adapter.notifyDataSetChanged();
				familyListView.post(new Runnable() {
					public void run() {
						while (isOver) {
							if (familyListView.getChildCount() == familyListView
									.getCount()) {
								noteArrow.setVisibility(View.VISIBLE);
								noteText.setText(R.string.note_info_1);
								noteText.setVisibility(View.VISIBLE);
								/*notePopWin.setFocusable(true);
								notePopWin.update();
								notePopWin.showAsDropDown(
										familyListView.getChildAt(0), -200, -60);*/
								isOver = false;
							}
						}
					}
				});
				break;
			case MESSAGE_LOADING_TWO_READY:
				adapter.setList(familyMemberList);
				adapter.notifyDataSetChanged();
				familyListView.post(new Runnable() {
					public void run() {
						while (isOver) {
							if (familyListView.getChildCount() == familyListView
									.getCount()) {
								/*noteArrow.setVisibility(View.GONE);
								noteText.setText(R.string.note_info_2);
								notePopWin.setFocusable(true);
								notePopWin.update();
								notePopWin.showAsDropDown(
										familyListView.getChildAt(1), 0, 20);*/
								noteArrow.setVisibility(View.GONE);
								noteText.setText(R.string.note_info_2);
								noteText.setVisibility(View.VISIBLE);
								isOver = false;
							}
						}
					}
				});
				break;
			case MESSAGE_LOADING_MANY:
				adapter.setList(familyMemberList);
				adapter.notifyDataSetChanged();
				if (familyMemberList != null && familyMemberList.size() != 0) {
					familyListView.setVisibility(View.VISIBLE);
					adapter.setList(familyMemberList);
					adapter.notifyDataSetChanged();
				} else {
					familyListView.setVisibility(View.GONE);
				}
				
				noteArrow.setVisibility(View.GONE);
				noteText.setVisibility(View.GONE);
				break;
			case MESSAGE_LOADING_NONE:
				showToastLong("加载成员列表失败");
				noteArrow.setVisibility(View.GONE);
				noteText.setVisibility(View.GONE);
				break;
			default:
				break;
			}
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return false;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_family_list);
		init();
	}

	@Override
	protected void onResume() {
		super.onResume();
		showGroupProgress(getString(R.string.wait_loading));
		new GetFamilyMemberListThread().start();
	}

	private void init() {
		db = new ExpressDBService(mContext);

		tvTBContent = (TextView) findViewById(R.id.tvTBContent);
		tvTBContent.setText(R.string.family_title);
		btnTBRight = (Button) findViewById(R.id.btnTBRight);
		btnTBRight.setVisibility(View.VISIBLE);
		btnTBRight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(FamilyListActivity.this,
						AddMainFrameActivity.class));
			}
		});
		
		noteText = (TextView) findViewById(R.id.note_text);
		noteArrow = (ImageView) findViewById(R.id.note_arrow);

		/*inflater = getLayoutInflater();
		notePopview = inflater.inflate(R.layout.layout_note_pop, null);
		noteText = (TextView) notePopview.findViewById(R.id.note_text);
		noteArrow = (ImageView) notePopview.findViewById(R.id.note_arrow);
		notePopWin = new PopupWindow(notePopview, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		notePopWin.setBackgroundDrawable(new BitmapDrawable());
		notePopWin.setTouchInterceptor(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				System.out.println("拦截  " + event.getAction());
				switch (event.getAction()) {
				case MotionEvent.ACTION_UP:
					float x = event.getRawX();
					float y = event.getRawY() - getBarHeight();
					System.out.println(x + "   " + y);
					System.out.println(btnTBRight.getLeft() + "   "
							+ btnTBRight.getRight() + "     "
							+ btnTBRight.getBottom() + "    "
							+ btnTBRight.getTop());
					if (x >= btnTBRight.getLeft() && x <= btnTBRight.getRight()
							&& y <= btnTBRight.getBottom()
							&& y >= btnTBRight.getTop()) {
						if (notePopWin.isShowing()) {
							notePopWin.dismiss();
						}
						startActivity(new Intent(FamilyListActivity.this,
								AddMainFrameActivity.class));
					}
					break;
				default:
					break;
				}
				return true;
			}
		});*/
		familyListView = (ListView) findViewById(R.id.familylist);
		adapter = new FamilyListAdapter(this, familyMemberList);
		familyListView.setAdapter(adapter);
		familyListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				UserBaseInfo ub = (UserBaseInfo) parent.getAdapter().getItem(
						position);
				if (!ub.getUserId().equals("fe.a.guide")) {
					ActivityGroup ag = (ActivityGroup) FamilyListActivity.this
							.getParent();
					ActivityJump.JumpFamilyDetailActivity(ag, (LinearLayout) ag
							.getWindow().findViewById(R.id.layoutAG), ub);
				} else {
					Intent intent = new Intent(FamilyListActivity.this,
							ChatActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("otheruid", ub.getUserId());
					bundle.putString("userName", ub.getUserName());
					bundle.putString("relation", ub.getRelation());
					intent.putExtras(bundle);
					startActivity(intent);
				}
			}
		});

		familyListView
				.setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> parent,
							View view, final int position, long id) {

						final UserBaseInfo ub = (UserBaseInfo) parent
								.getAdapter().getItem(position);

						if (!ub.getUserId().equals("fe.a.guide")) {
							new AlertDialog.Builder(FamilyListActivity.this
									.getParent())
									.setTitle(
											getString(R.string.del_family_member_title))
									.setMessage(
											getString(
													R.string.del_family_member_info)
													.replace(
															"@member",
															ub.getRelation()
																	.concat("（")
																	.concat(ub
																			.getUserName())
																	.concat("）")))
									.setPositiveButton(
											R.string.common_confirm,
											new DialogInterface.OnClickListener() {
												@Override
												public void onClick(
														DialogInterface dialog,
														int which) {
													StrictMode.ThreadPolicy old = StrictMode
															.getThreadPolicy();
													StrictMode
															.setThreadPolicy(new StrictMode.ThreadPolicy.Builder(
																	old)
																	.permitNetwork()
																	.build());
													boolean ok = ThriftOpe
															.deleteFamilyMember(
																	FamilyListActivity.this
																			.getParent(),
																	ub.getUserId());
													StrictMode
															.setThreadPolicy(old);
													if (ok) {
														db.deleteByUserId(ub
																.getUserId());

														familyMemberList
																.remove(position);
														adapter.notifyDataSetChanged();
														Toast.makeText(
																FamilyListActivity.this
																		.getParent(),
																R.string.del_family_member_success,
																Toast.LENGTH_LONG)
																.show();

													} else {
														Toast.makeText(
																FamilyListActivity.this
																		.getParent(),
																R.string.del_family_member_error,
																Toast.LENGTH_LONG)
																.show();
													}
												}
											})
									.setNegativeButton(R.string.common_cancel,
											null).show();
						}
						return true;
					}

				});
	}

	class GetFamilyMemberListThread extends Thread {

		@Override
		public void run() {
			super.run();
			ThriftOpe
					.login(mContext, Constants.ACCOUNTNAME, Constants.PASSWORD);
			familyMemberList = ThriftOpe
					.getFamilyMemberDetailList(FamilyListActivity.this);
			Message msg = Message.obtain();
			if (familyMemberList != null) {
				if (familyMemberList.size() == 1) {
					msg.what = MESSAGE_LOADING_ONE;
				} else if (familyMemberList.size() == 2) {
					if (familyMemberList.get(1).allowStatus == AllowStatus.INVITED) {
						msg.what = MESSAGE_LOADING_TWO_READY;
					} else {
						msg.what = MESSAGE_LOADING_MANY;
					}
				} else {
					msg.what = MESSAGE_LOADING_MANY;
				}
			} else {
				msg.what = MESSAGE_LOADING_NONE;
			}
			handler.sendMessage(msg);
		}
	}

	private int getBarHeight() {
		Rect frame = new Rect();
		getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		int statusBarHeight = frame.top;
		System.out.println("statusBarHeight   =   " + statusBarHeight);
		return statusBarHeight;
	}
}
