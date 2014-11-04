/* ==================================================
 * 产品名: 亲情快递
 * 文件名: AddModeByConfirmActivity.java
 * --------------------------------------------------
 * 开发环境: JDK1.6
 * --------------------------------------------------
 * 修订履历    2012/07/07  1.00  初版发行
 * --------------------------------------------------
 * (C) Copyright XIKANG CORPORATION 2012 All Rights Reserved.
 */
package com.xikang.family.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xikang.channel.base.rpc.thrift.account.AccountInfo;
import com.xikang.channel.familyexpress.rpc.thrift.family.Gender;
import com.xikang.channel.familyexpress.rpc.thrift.family.MemberRelation;
import com.xikang.family.adapter.AbstractWheelTextAdapter;
import com.xikang.family.adapter.ArrayListWheelAdapter;
import com.xikang.family.common.ActivityJump;
import com.xikang.family.common.AsyncImageLoader;
import com.xikang.family.common.AsyncImageLoader.ImageCallback;
import com.xikang.family.common.Constants;
import com.xikang.family.common.ThriftOpe;
import com.xikang.family.view.OnWheelChangedListener;
import com.xikang.family.view.OnWheelScrollListener;
import com.xikang.family.view.WheelView;

/**
 * 
 * 添加家人确认页面
 * 
 * 
 * 
 * @author 闫继超
 * @version 1.00
 */

public class AddModeByConfirmActivity extends BaseActivity implements
		OnClickListener {
	// 是否在滑动滚轮
	private boolean scrolling = false;
	// 是否准备好数据 准备弹出对话框
	private boolean show = false;

	final String RELATION_ELDER = "ELDER";
	final String RELATION_FELLOW = "FELLOW";
	final String RELATION_YOUNGER = "YOUNGER";

	final int CONFIRM_SUCCESS = 1001;
	final int CONFIRM_FAILED = 1002;
	final int LOADING_HEAD = 2001;
	final int JUMP = 4001;
	// 确认按钮
	private Button btn_confirm = null;
	// 添加成功图片
	private ImageView image_success = null;
	// 多选框
	private CheckBox check_man = null;
	private CheckBox check_woman = null;
	// 滚动选择控件选择后显示的文本
	private TextView pick = null;
	// 滚动选择控件相关
	private LayoutInflater mInflater = null;
	private View pickview = null;
	private WheelView pick1 = null;
	private WheelView pick2 = null;
	// 由哪个页面进入的
	private String tabTag = null;
	// 根据tabTag设置相关显示控件
	private TextView tabText = null;
	private LinearLayout tabArrow = null;
	// 用户头像
	private ImageView head = null;
	// 昵称
	private TextView pet = null;
	// Email
	private TextView email = null;
	// 电话号码
	private TextView phonenum = null;
	// 添加用户的userid
	private String otheruid = null;
	// 添加用户的头像url（用于从搜索进入方式使用）
	private String url = null;
	// 多选框文本
	private TextView tvMan = null;
	private TextView tvWoman = null;
	// 爱称输入框
	private EditText et_pet = null;
	// 上一个页面传入的email和password （用于通过验证用户密码接口 获取用户userid）
	private String str_email = null;
	private String str_password = null;

	// 从服务器端获取的所有称呼关系
	private List<MemberRelation> relation = null;
	// 长辈
	private List<MemberRelation> elder = new ArrayList<MemberRelation>();
	// 同辈
	private List<MemberRelation> fellow = new ArrayList<MemberRelation>();
	// 晚辈
	private List<MemberRelation> younger = new ArrayList<MemberRelation>();
	// 存储处理完的list
	private Map<Integer, List<MemberRelation>> map = new HashMap<Integer, List<MemberRelation>>();
	// 通过验证账户接口获得的 用户信息
	private AccountInfo info = null;
	// 辈份数组
	private String[] beifen = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_mode_confirm);
		// 初始化控件
		init();
		// 设置页面信息
		setInfo();
		// 从服务器端获取关系列表
		getRelation();
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case JUMP:
				Intent intent = new Intent(AddModeByConfirmActivity.this,
						FamilyMainFrameActivity.class);
				intent.putExtra("tag", "family");
				startActivity(intent);
				break;
			// 显示添加家人关系成功图标
			case CONFIRM_SUCCESS:
				closeGroupProgress();
				image_success.setVisibility(View.VISIBLE);
				new Thread() {

					@Override
					public void run() {
						try {
							sleep(1 * 1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						handler.sendEmptyMessage(JUMP);
					}
				}.start();
				break;
			// 显示添加家人关系失败文本
			case CONFIRM_FAILED:
				closeGroupProgress();
				image_success.setVisibility(View.INVISIBLE);
				if (Constants.ERRORMSG != null) {
					showToastShort(Constants.ERRORMSG);
				} else {
					showToastShort(mContext.getString(R.string.confirm_failed));
				}
				break;
			// 异步加載家人头像
			case LOADING_HEAD:
				closeGroupProgress();
				// 如果url为空 设置url（url在 从搜索页面进入时不为空）
				if (url == null) {
					url = info != null ? info.getFigureUrl() : null;
				}
				if (url != null) {
					new AsyncImageLoader().loadBitmap(url, otheruid, head,
							mContext, new ImageCallback() {

								@Override
								public void imageLoaded(String path,
										Bitmap imageBitmap, ImageView imageView) {
									imageView.setImageBitmap(imageBitmap);
								}
							});
				}
				// 处理relation关系列表 根据辈份重新分组
				if (relation != null && relation.size() > 0) {
					for (MemberRelation info : relation) {
						if (RELATION_ELDER.equals(info.getRelativeCode()
								.toString())) {
							elder.add(info);
							continue;
						}
						if (RELATION_FELLOW.equals(info.getRelativeCode()
								.toString())) {
							fellow.add(info);
							continue;
						}
						if (RELATION_YOUNGER.equals(info.getRelativeCode()
								.toString())) {
							younger.add(info);
							continue;
						}
					}
					map.put(0, elder);
					map.put(1, fellow);
					map.put(2, younger);
					// 允许显示滚动控件
					show = true;
				}
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
		// 初始化pop
		mInflater = getLayoutInflater();
		pickview = mInflater.inflate(R.layout.layout_pick_view, null);
		// 多选框 通过onclicklistener方式将功能设置成单选按钮功能
		check_man = (CheckBox) findViewById(R.id.check_man);
		check_man.setOnClickListener(this);
		check_woman = (CheckBox) findViewById(R.id.check_woman);
		check_woman.setOnClickListener(this);
		// 多选框文本
		tvMan = (TextView) findViewById(R.id.tv_man);
		tvMan.setVisibility(View.INVISIBLE);
		tvWoman = (TextView) findViewById(R.id.tv_woman);
		tvWoman.setVisibility(View.INVISIBLE);

		pick = (TextView) findViewById(R.id.pick);
		pick.setOnClickListener(this);

		tabText = (TextView) findViewById(R.id.tabtext);
		tabArrow = (LinearLayout) findViewById(R.id.tabarrow);

		btn_confirm = (Button) findViewById(R.id.btn_confirm);
		btn_confirm.setOnClickListener(this);

		image_success = (ImageView) findViewById(R.id.confirm_success);

		pet = (TextView) findViewById(R.id.confirm_pet);
		email = (TextView) findViewById(R.id.confirm_email);
		phonenum = (TextView) findViewById(R.id.confirm_phonenum);

		head = (ImageView) findViewById(R.id.confirm_head);
		// 初始化辈份数组
		String elder = getString(R.string.relation_elder);
		String fellow = getString(R.string.relation_fellow);
		String younger = getString(R.string.relation_younger);
		beifen = new String[] { elder, fellow, younger };

		et_pet = (EditText) findViewById(R.id.et_pet);
	}

	private void setInfo() {
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			tabTag = bundle.getString("tag");
			// 根据跳入页面的不同 设置不同的页面样式
			if ("regist".equals(tabTag)) {
				// 注册页面进入
				tabText.setText(R.string.add_message_1);
				tabArrow.setBackgroundResource(R.drawable.addmode_reg_bg);
			}
			if ("user".equals(tabTag)) {
				// 搜索列表页面进入
				tabText.setText(R.string.add_message_2);
				tabText.setGravity(Gravity.CENTER_HORIZONTAL);
				tabArrow.setBackgroundResource(R.drawable.addmode_user_bg);
			}
			if ("userandpwd".equals(tabTag)) {
				// 验证用户密码页面进入
				tabText.setText(R.string.add_message_3);
				tabText.setGravity(Gravity.RIGHT);
				tabArrow.setBackgroundResource(R.drawable.addmode_userandpwd_bg);
			}
			// 获取传入信息
			str_email = bundle.getString("email");
			str_password = bundle.getString("password");
			pet.setText(bundle.getString("pet"));
			email.setText(str_email);
			phonenum.setText(bundle.getString("phonenum"));
			// 仅搜索页面传入
			otheruid = bundle.getString("otheruid");
			// 仅搜索页面传入
			url = bundle.getString("url");
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 控制多选框只能单选
		case R.id.check_man:
			check_man.setChecked(true);
			check_woman.setChecked(false);
			break;
		// 控制多选框只能单选
		case R.id.check_woman:
			check_woman.setChecked(true);
			check_man.setChecked(false);
			break;
		// 点击选择关系按钮 弹出自定义滚轮 选择家人关系
		case R.id.pick:
			if (show) {
				this.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						pickview = mInflater.inflate(R.layout.layout_pick_view,
								null);
						pick1 = (WheelView) pickview.findViewById(R.id.pick1);
						pick2 = (WheelView) pickview.findViewById(R.id.pick2);
						// 是否循环显示
						pick2.setCyclic(true);
						// 滚轮可见范围
						pick1.setVisibleItems(5);
						// 添加数据
						pick1.setViewAdapter(new RelationAdapter(mContext));
						// 滚轮可见范围
						pick2.setVisibleItems(5);
						// 添加变更监听器
						pick1.addChangingListener(new OnWheelChangedListener() {
							public void onChanged(WheelView wheel,
									int oldValue, int newValue) {
								// 动态变更右侧滚轮的数据
								if (!scrolling) {
									updateCities(map.get(newValue), newValue);
								}
							}
						});
						pick1.addScrollingListener(new OnWheelScrollListener() {
							public void onScrollingStarted(WheelView wheel) {
								scrolling = true;
							}

							public void onScrollingFinished(WheelView wheel) {
								scrolling = false;
								// 在滚动结束后 重置数据項
								updateCities(map.get(pick1.getCurrentItem()),
										pick1.getCurrentItem());
							}
						});
						// 设置默认选中
						pick1.setCurrentItem(1);
						// 弹出自定义滚轮
						new AlertDialog.Builder(AddModeByConfirmActivity.this
								.getParent())
								.setView(pickview)
								.setPositiveButton("确认",
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												MemberRelation member = map
														.get(pick1
																.getCurrentItem())
														.get(pick2
																.getCurrentItem());
												// 设置文本显示内容
												pick.setText(member.getTitle());
												String man = getTileByCode(member
														.getReTitleCodeM());
												// 设置多选框文本
												if (man != null) {
													check_man
															.setVisibility(View.VISIBLE);
													tvMan.setText(man);
													tvMan.setVisibility(View.VISIBLE);
												} else {
													check_man.setChecked(false);
													check_man
															.setVisibility(View.GONE);
													tvMan.setVisibility(View.GONE);
												}
												String woman = getTileByCode(member
														.getReTitleCodeW());
												if (woman != null) {
													check_woman
															.setVisibility(View.VISIBLE);
													tvWoman.setText(woman);
													tvWoman.setVisibility(View.VISIBLE);
												} else {
													check_woman
															.setChecked(false);
													check_woman
															.setVisibility(View.GONE);
													tvWoman.setVisibility(View.GONE);
												}
											}
										}).show();
					}
				});
			} else {
				// 如果数据未准备充分 不可显示 提示用户加载失败 。避免出错
				showToastShort(getString(R.string.loading_failed));
			}
			break;
		case R.id.btn_confirm:
			// 判断用户是否已经填写相关内容（1.点击滚动控件选择称呼 2.多选框需要有一项为选中状态）
			if (!getString(R.string.pick_select).equals(
					pick.getText().toString())
					&& (check_man.isChecked() || check_woman.isChecked())) {
				showGroupProgress(mContext.getString(R.string.wait_confirm));
				new Thread() {

					@Override
					public void run() {

						boolean isSuccess = false;
						// otheruid为空 设置otheruid （otheruid只有在搜索页面进入时不为空）
						if (otheruid == null) {
							ThriftOpe.login(mContext, Constants.ACCOUNTNAME,
									Constants.PASSWORD);
							AccountInfo account = ThriftOpe.validateAccount(
									mContext, str_email, str_password);
							if (account != null) {
								otheruid = account.getUserId();
							} else {
								Message msg = Message.obtain();
								msg.what = CONFIRM_FAILED;
								handler.sendMessage(msg);
								return;
							}
						}
						// 判断进入页面
						if (tabTag != null) {
							ThriftOpe.login(mContext, Constants.ACCOUNTNAME,
									Constants.PASSWORD);
							if ("regist".equals(tabTag)
									|| "userandpwd".equals(tabTag)) {
								// 如果是从注册页面进入的 则接口最后一个参数设置为true ，表示可以直接添加成功
								// ，无需对方验证
								isSuccess = ThriftOpe.addFamilyMember(
										mContext,
										otheruid,
										map.get(pick1.getCurrentItem())
												.get(pick2.getCurrentItem())
												.getTitleCode().toString(),
										et_pet.getText().toString(), check_man
												.isChecked() ? Gender.MAN
												: Gender.WOMAN, true);

							} else {
								// 如果不是从注册页面进入的 则接口最后一个参数设置为false ，表示不能直接添加
								// ，需要对方验证
								isSuccess = ThriftOpe.addFamilyMember(
										mContext,
										otheruid,
										map.get(pick1.getCurrentItem())
												.get(pick2.getCurrentItem())
												.getTitleCode().toString(),
										et_pet.getText().toString(), check_man
												.isChecked() ? Gender.MAN
												: Gender.WOMAN, false);
							}
						}
						Message msg = Message.obtain();
						if (isSuccess) {
							msg.what = CONFIRM_SUCCESS;
						} else {
							msg.what = CONFIRM_FAILED;
						}
						handler.sendMessage(msg);
					}

				}.start();
			} else {
				// 通知用户填写內容不完整
				showToastShort(getString(R.string.pick_error));
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 根据称呼code获得称呼名称
	 * 
	 * @param code
	 * @return
	 */
	private String getTileByCode(String code) {
		for (MemberRelation member : relation) {
			if (code.equals(member.getTitleCode())) {
				return member.getTitle();
			}
		}
		return null;
	}

	/**
	 * 更新wheel
	 */
	private void updateCities(List<MemberRelation> list, int index) {
		ArrayListWheelAdapter listAdapter = new ArrayListWheelAdapter(mContext,
				list);
		// listAdapter.setTextSize(18);
		pick2.setViewAdapter(listAdapter);
		pick2.setCurrentItem(list.size() / 2);
	}

	/**
	 * 辈份adapter
	 */
	private class RelationAdapter extends AbstractWheelTextAdapter {

		protected RelationAdapter(Context context) {
			super(context, R.layout.layout_pick_relation, NO_RESOURCE);

			setItemTextResource(R.id.pick_relation);
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			View view = super.getItem(index, cachedView, parent);
			return view;
		}

		@Override
		public int getItemsCount() {
			return beifen.length;
		}

		@Override
		protected CharSequence getItemText(int index) {
			return beifen[index];
		}
	}

	/**
	 * 从服务器端获得关系列表
	 */
	private void getRelation() {
		showGroupProgress(this.getString(R.string.wait_loading));
		new Thread() {

			@Override
			public void run() {
				// 调用接口需要鉴权
				ThriftOpe.login(mContext, Constants.ACCOUNTNAME,
						Constants.PASSWORD);
				info = ThriftOpe.validateAccount(mContext, str_email,
						str_password);
				ThriftOpe.login(mContext, Constants.ACCOUNTNAME,
						Constants.PASSWORD);
				relation = ThriftOpe.getMemberRelationCodeList(mContext);
				handler.sendEmptyMessage(LOADING_HEAD);

			}
		}.start();
	}

	/**
	 * 监听back键 返回不同页面
	 */
	@Override
	public void onBackPressed() {
		if (tabTag != null) {
			if ("regist".equals(tabTag)) {
				ActivityGroup ag = (ActivityGroup) AddModeByConfirmActivity.this
						.getParent();
				ActivityJump.JumpAddModeByRegEditActivity(ag, (LinearLayout) ag
						.getWindow().findViewById(R.id.layoutAG));
				return;
			}
			if ("userandpwd".equals(tabTag)) {
				ActivityGroup ag = (ActivityGroup) AddModeByConfirmActivity.this
						.getParent();
				ActivityJump.JumpAddModeByUserAndPwdActivity(
						ag,
						(LinearLayout) ag.getWindow().findViewById(
								R.id.layoutAG));
				return;
			}
		}
		super.onBackPressed();
	}

}
