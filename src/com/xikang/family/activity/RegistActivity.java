/* ==================================================
 * 产品名: 亲情快递
 * 文件名: RegistActivity.java
 * --------------------------------------------------
 * 开发环境: JDK1.6
 * --------------------------------------------------
 * 修订履历    2012/07/07  1.00  初版发行
 * --------------------------------------------------
 * (C) Copyright XIKANG CORPORATION 2012 All Rights Reserved.
 */
package com.xikang.family.activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.apache.thrift.transport.THttpClient;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xikang.channel.base.rpc.thrift.account.AccountInfo;
import com.xikang.channel.common.rpc.thrift.message.CommArgs;
import com.xikang.channel.common.rpc.thrift.message.DigestAuthenticationReq;
import com.xikang.family.activity.ChatActivity.PushReceiver;
import com.xikang.family.common.CPSClientUtil;
import com.xikang.family.common.Constants;
import com.xikang.family.common.HttpClientUtil;
import com.xikang.family.common.SendService;
import com.xikang.family.common.ThriftOpe;
import com.xikang.family.common.Util;

/**
 * 
 * 注册页面
 * 
 * 
 * 
 * @author 张荣
 * @version 1.00
 */

public class RegistActivity extends BaseActivity implements OnClickListener {

	private static final int REQUEST_TAKE = 7001;
	private static final int REQUEST_IMAGES = 7002;
	private static final int REQUEST_PICK = 7003;

	private static final String ICON_PATH = "/sdcard/familyExpress/images/icon.jpg";

	private TextView tv_provision = null;

	private EditText et_email = null;
	private EditText et_password = null;
	private EditText et_neckname = null;
	private EditText et_mobileno = null;
	
	private String email = null;
	private String password = null;
	private String neckname = null;
	private String mobileno = null;
	
	private AccountInfo account = null;

	private Button btn_cancel = null;
	private Button btn_confirm = null;
	private ImageView icon = null;
	// 是否需要上传头像
	private boolean upload = false;
	private Bitmap bitmap = null;
	List<String> pathlist = new ArrayList<String>();

	private BoundReceiver receiver = null;
	private IntentFilter filter = null;
	private static final int MESSAGE_BOUND_OK = 8001;
	private static final int MESSAGE_REGIST_OK = 8002;
	private static final int MESSAGE_REGIST_NG = 8003;
	private static final int MESSAGE_UPLOAD_OK = 8004;
	private static final int MESSAGE_UPLOAD_NG = 8005;
	private static final int MESSAGE_NOTICE = 8006;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_regist);
		init();

		receiver = new BoundReceiver();
		filter = new IntentFilter();
		filter.addAction(Constants.BIND_RESULT_NOTIFICATION);
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (receiver != null) {
			unregisterReceiver(receiver);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (receiver != null) {
			registerReceiver(receiver, filter);
		}
	}

	public void init() {
		et_email = (EditText) findViewById(R.id.regist_email);
		et_password = (EditText) findViewById(R.id.regist_password);
		et_neckname = (EditText) findViewById(R.id.regist_pet);
		et_mobileno = (EditText) findViewById(R.id.regist_phonenum);

		tv_provision = (TextView) findViewById(R.id.tv_provision);
		tv_provision.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showResult(getString(R.string.regist_info_2),
						getString(R.string.regist_info_3), null);
			}
		});
		btn_cancel = (Button) findViewById(R.id.btn_cancel);
		btn_cancel.setOnClickListener(this);
		btn_confirm = (Button) findViewById(R.id.btn_confirm);
		btn_confirm.setOnClickListener(this);

		icon = (ImageView) findViewById(R.id.regist_icon);
		icon.setOnClickListener(this);
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_cancel:
			this.finish();
			break;
		case R.id.btn_confirm:
			
			Log.e("ffffffff","button confirm clicked");
			email = et_email.getText().toString().trim();
			password = et_password.getText().toString().trim();
			neckname = et_neckname.getText().toString().trim();
			mobileno = et_mobileno.getText().toString().trim();
			if (("".equals(email)) || ("".equals(password))
					|| ("".equals(neckname))) {
				showToastShort(getString(R.string.regist_null));
				return;
			} else if (mobileno.length() > 11) {
				showToastShort(getString(R.string.regist_phone_err));
				return;
			}

			showProgress("注册中...");

			new RegistThread().start();

			break;
		case R.id.regist_icon:
			showResult(this.getString(R.string.select_head), null,
			// 照相
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent intent = new Intent(
									MediaStore.ACTION_IMAGE_CAPTURE);
							intent.putExtra(MediaStore.EXTRA_OUTPUT,
									Uri.fromFile(new File(ICON_PATH)));
							startActivityForResult(intent, REQUEST_TAKE);
						}
					}, new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

						}
					},
					// 本地图片库
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent intent = new Intent(
									Intent.ACTION_GET_CONTENT);
							intent.setType("image/*");
							startActivityForResult(intent, REQUEST_IMAGES);
						}
					});
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 图片库返回的信息
		if (resultCode == RESULT_OK) {
			if (requestCode == REQUEST_IMAGES) {
				try {
					Uri uri = data.getData();
					startActivity(uri);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
			// 照相返回的信息
			if (requestCode == REQUEST_TAKE) {
				File temp = new File(ICON_PATH);
				Uri uri = Uri.fromFile(temp);
				startActivity(uri);
			}

			if (requestCode == REQUEST_PICK) {
				Bundle extras = data.getExtras();
				if (extras != null) {
					bitmap = extras.getParcelable("data");
					icon.setImageBitmap(bitmap);
					upload = true;
					String path = Util.saveMyBitmap(bitmap, "/sdcard/", true,
							100);
					pathlist.add(path);
				}
			}
		}
	}

	public void showResult(final String title, final String message,
			final DialogInterface.OnClickListener listener) {

		this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				new AlertDialog.Builder(RegistActivity.this).setTitle(title)
						.setMessage(message)
						.setPositiveButton(R.string.common_confirm, listener)
						.show();
			}
		});
	}

	/**
	 * 获取剪切后的图片
	 */
	public void startActivity(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);// 裁剪框比例
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 280);// 输出图片大小
		intent.putExtra("outputY", 280);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, REQUEST_PICK);
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MESSAGE_BOUND_OK:	
				downloadPic();
				closeProgress();
				startActivity(new Intent(RegistActivity.this,
						ModeSelectActivity.class));
				break;
			case MESSAGE_NOTICE:
				closeProgress();
				Bundle bundle = msg.getData();
				String notice = bundle.getString("notice");
				showToastLong(notice);
			default:
				break;
			}
		}

	};

	private class RegistThread extends Thread {

		@Override
		public void run() {
			Message msg = Message.obtain();
			
			StrictMode.ThreadPolicy old = StrictMode.getThreadPolicy();
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder(old)
					.permitNetwork().build());
			Log.e("fffffffffff", "begin regist");
			account = ThriftOpe.registerAccount(
					RegistActivity.this, email, mobileno, password, neckname);
			Log.e("fffffffffff", "end regist");

			// 注册成功
			if (account != null) {
				//Toast.makeText(RegistActivity.this, R.string.regist_succeed,
				//		Toast.LENGTH_LONG).show();
				msg.what = MESSAGE_REGIST_OK;
				mHandler.sendMessage(msg);
				Log.e("fffffff","regist ok");
				Constants.ACCESSTOKEN = ThriftOpe.login(RegistActivity.this,email, password);
				mContext.startService(new Intent(RegistActivity.this,
						SendService.class));
				
				CPSClientUtil.connectCPS(getApplicationContext());
			}else{
				if (Constants.ERRORMSG != null) {
					//Toast.makeText(RegistActivity.this, Constants.ERRORMSG,
					//		Toast.LENGTH_LONG).show();
					Bundle bundle = new Bundle();
					bundle.putString("notice", Constants.ERRORMSG);
					msg.setData(bundle);
					msg.what = MESSAGE_NOTICE;
					mHandler.sendMessage(msg);
				} else {
					//Toast.makeText(RegistActivity.this, R.string.regist_failed,
					//		Toast.LENGTH_LONG).show();
					Bundle bundle = new Bundle();
					bundle.putString("notice", getString(R.string.regist_failed));
					msg.setData(bundle);
					msg.what = MESSAGE_NOTICE;
					mHandler.sendMessage(msg);
				}
			}
			StrictMode.setThreadPolicy(old);
			
		}

	};

	private class BoundReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (Constants.BIND_RESULT_NOTIFICATION.equals(action)) {
				Log.e("ffffff", "receive bound success");
				Message msg = Message.obtain();
				msg.what = MESSAGE_BOUND_OK;
				mHandler.sendMessage(msg);
			}

		}
	}
	
	
	private void downloadPic(){
		Log.e("ffffffffff","downloadPic start");
		ThriftOpe.firstUse(RegistActivity.this);
		
		Constants.ACCESSTOKEN = ThriftOpe.login(RegistActivity.this,email, password);
		if (Constants.ACCESSTOKEN != null) {					

			// 验证账户成功（获取userid）
			if (account != null && account.getUserId() != null) {
				// 取得最后一个有效图片地址
				if (upload) {
					String path = pathlist.get(pathlist.size() - 1);
					File file = new File(path);
					byte[] buf = new byte[(int) file.length()];
					try {
						new FileInputStream(file).read(buf);
					} catch (IOException e) {
						e.printStackTrace();
					}

					ThriftOpe.login(mContext, Constants.ACCOUNTNAME,
							Constants.PASSWORD);
					boolean uploadsuccess = ThriftOpe.saveAvatar(
							mContext, Constants.USERID,
							Util.getFormat(path), ByteBuffer.wrap(buf));
					for (String address : pathlist) {
						Util.deleteFile(address);
					}
					if (!uploadsuccess) {
						showToastShort(getString(R.string.common_upload_error));
					} else {
						// 上传成功
					}
				}
			}
		}else{
			Toast.makeText(RegistActivity.this,
					R.string.common_login_error, Toast.LENGTH_LONG)
					.show();
		}
		Log.e("ffffffffff","downloadPic end");
	}
	
	
}

