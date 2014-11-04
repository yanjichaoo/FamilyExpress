/* ==================================================
 * 产品名: 亲情快递
 * 文件名: AddModeByUserAndPwdActivity.java
 * --------------------------------------------------
 * 开发环境: JDK1.6
 * --------------------------------------------------
 * 修订履历    2012/07/07  1.00  初版发行
 * --------------------------------------------------
 * (C) Copyright XIKANG CORPORATION 2012 All Rights Reserved.
 */
package com.xikang.family.activity;

import android.app.ActivityGroup;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.xikang.channel.base.rpc.thrift.account.AccountInfo;
import com.xikang.family.common.ActivityJump;
import com.xikang.family.common.Constants;
import com.xikang.family.common.ThriftOpe;

/**
 * 
 * 帐号密码验证页面（通过帐号密码验证方式添加家人）
 * 
 * 
 * 
 * @author 闫继超
 * @version 1.00
 */

public class AddModeByUserAndPwdActivity extends BaseActivity {

	final int CONFIRM_SUCCESS = 1001;  //确认成功
	final int CONFIRM_FAILED = 1002;	//确认失败
	//验证按钮
	private Button btn_userandpwd = null;
	//email
	private EditText email = null;
	//password
	private EditText password = null;
	//从edittext获得字符串信息
	private String str_email = null;
	private String str_password = null;
	//通过验证获得的用户信息
	private AccountInfo info = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_mode_userandpwd_edit);
		init();
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case CONFIRM_SUCCESS:
				Bundle bundle = new Bundle();
				bundle.putString("tag", "userandpwd");
				bundle.putString("email", str_email);
				bundle.putString("password", str_password);
				bundle.putString("pet", info.getUserName());
				bundle.putString("phonenum", info.getMobileNum());

				ActivityGroup ag = (ActivityGroup) AddModeByUserAndPwdActivity.this
						.getParent();
				ActivityJump.JumpAddModeByConfirmActivity(ag, (LinearLayout) ag
						.getWindow().findViewById(R.id.layoutAG), bundle);
				break;
			case CONFIRM_FAILED:
				showToastShort(mContext.getString(R.string.confirm_failed));
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
		btn_userandpwd = (Button) findViewById(R.id.btn_userandpwd);
		btn_userandpwd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				str_email = email.getText().toString().trim();
				str_password = password.getText().toString().trim();
				//輸入框不为空
				if (str_email != null && !"".equals(str_email)
						&& str_password != null && !"".equals(str_password)) {
					showGroupProgress(mContext.getString(R.string.wait_confirm));
					new Thread() {

						@Override
						public void run() {
							super.run();
							ThriftOpe.login(mContext, Constants.ACCOUNTNAME,
									Constants.PASSWORD);
							info = ThriftOpe.validateAccount(mContext,
									str_email, str_password);
							Message msg = Message.obtain();
							if (info != null && info.getUserId() != null
									&& !"".equals(info.getUserId())) {
								msg.what = CONFIRM_SUCCESS;
							} else {
								msg.what = CONFIRM_FAILED;
							}
							closeGroupProgress();
							handler.sendMessage(msg);
						}

					}.start();
				} else {
					//提示信息输入内容
					showToastShort(mContext.getString(R.string.regist_null));
				}
			}
		});
		email = (EditText) findViewById(R.id.userandpwd_email);
		password = (EditText) findViewById(R.id.userandpwd_password);
	}
}
