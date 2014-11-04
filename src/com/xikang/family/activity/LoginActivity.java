/* ==================================================
 * 产品名: 亲情快递
 * 文件名: LoginActivity.java
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
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.xikang.channel.familyexpress.rpc.thrift.family.UserBaseInfo;
import com.xikang.family.common.CPSClientUtil;
import com.xikang.family.common.Constants;
import com.xikang.family.common.SendService;
import com.xikang.family.common.ThriftOpe;

/**
 * 
 * 登录页面
 * 
 * 
 * 
 * @author 张荣
 * @version 1.00
 */

public class LoginActivity extends BaseActivity implements OnClickListener {
	private Button btn_cancel = null;
	private Button btn_confirm = null;
	private EditText et_username = null;
	private EditText et_password = null;
	private String username = null;
	private String password = null;
	private static final String TAG = "LoginActivity";
	
	public static final int LOGIN_LOADING_SUCCESS = 9001;
	public static final int LOADING_FAIL = 9002;
	public static final int LOGIN_FAIL = 9003;

	private List<UserBaseInfo> familyMemberList = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_login);
		init();
	}

	public void init() {
		et_username = (EditText) findViewById(R.id.login_username);
		et_password = (EditText) findViewById(R.id.login_password);
		btn_cancel = (Button) findViewById(R.id.btn_cancel);
		btn_cancel.setOnClickListener(this);
		btn_confirm = (Button) findViewById(R.id.btn_confirm);
		btn_confirm.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_cancel:
			this.finish();
			break;
		case R.id.btn_confirm:
			username = et_username.getText().toString().trim();
			password = et_password.getText().toString().trim();

			if ("".equals(username) || "".equals(password)) {
				Toast.makeText(LoginActivity.this, R.string.common_login_error,
						Toast.LENGTH_LONG).show();
				return;
			}

			showProgress(getString(R.string.wait_login));

			new LoginThread().start();
			break;
		default:
			break;
		}

	}
	
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			closeProgress();
			if(msg.arg1 == LOGIN_LOADING_SUCCESS){
				Intent intent = new Intent(LoginActivity.this,
						FamilyMainFrameActivity.class);
				intent.putExtra("tag", "family");
				startActivity(intent);
			}
			else if(msg.arg1 == LOADING_FAIL){
				startActivity(new Intent(LoginActivity.this,
						ModeSelectActivity.class));
			}
			else if(msg.arg1 == LOGIN_FAIL){
				Toast.makeText(LoginActivity.this, R.string.common_login_error,Toast.LENGTH_LONG).show();
			}
		}
		
	};

	private class LoginThread extends Thread {

		@Override
		public void run() {
			
			Message msg = new Message();
			if ((!"".equals(username)) && (!"".equals(password))) {

				StrictMode.ThreadPolicy old = StrictMode.getThreadPolicy();
				StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder(
						old).permitNetwork().build());
				Constants.ACCESSTOKEN = ThriftOpe.login(LoginActivity.this,
						username, password);
				StrictMode.setThreadPolicy(old);
			}

			if (Constants.ACCESSTOKEN != null) {
				Log.e(TAG,"login OK");
				
				mContext.startService(new Intent(LoginActivity.this,
						SendService.class));
				CPSClientUtil.connectCPS(getApplicationContext());

				
				ThriftOpe.login(LoginActivity.this, Constants.ACCOUNTNAME, Constants.PASSWORD);
				StrictMode.ThreadPolicy old = StrictMode.getThreadPolicy();
				StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder(
						old).permitNetwork().build());

				familyMemberList = ThriftOpe
						.getFamilyMemberDetailList(LoginActivity.this);
				
				if (familyMemberList != null) {
					Log.e(TAG,"getFamilyList OK");
					msg.arg1 = LOGIN_LOADING_SUCCESS;
				
				} else {
					Log.e(TAG,"getFamilyList NG");
					msg.arg1 = LOADING_FAIL;		
				}						
				
			} else {						
				msg.arg1 = LOGIN_FAIL ;				
			}
			mHandler.sendMessage(msg);
		}

	}
	


}
