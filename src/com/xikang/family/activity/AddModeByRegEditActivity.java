/* ==================================================
 * 产品名: 亲情快递
 * 文件名: AddModeByRegEditActivity.java
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

import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.xikang.channel.base.rpc.thrift.account.AccountInfo;
import com.xikang.family.common.ActivityJump;
import com.xikang.family.common.Constants;
import com.xikang.family.common.ThriftOpe;
import com.xikang.family.common.Util;

/**
 * 
 * 注册方式添加家人 填写注册信息页面
 * 
 * 
 * 
 * @author 闫继超
 * @version 1.00
 */

public class AddModeByRegEditActivity extends BaseActivity implements
		OnClickListener {

	private static final int REQUEST_TAKE = 7001; // 拍照
	private static final int REQUEST_IMAGES = 7002; // 图片
	private static final int REQUEST_PICK = 7003; // 剪裁
	private static final int UPLOAD_SUCCESS = 8001; // 上传成功
	private static final int UPLOAD_FAILED = 8002; // 上传失败

	// 拍照剪裁的图片位置
	private static final String ICON_PATH = "/sdcard/familyExpress/images/icon.jpg";

	final int REGIST_SUCCESS = 1001; // 注册成功
	final int REGIST_FAILED = 1002; // 注册失败
	// 注册按钮
	private Button btn_reg = null;
	// 需要填写的信息
	private EditText email = null;
	private EditText password = null;
	private EditText pet = null;
	private EditText phonenum = null;

	private ImageView icon = null;
	// 是否需要上传图片
	private boolean upload = false;
	private Bitmap bitmap = null;
	// 存放图片路径list
	List<String> pathlist = new ArrayList<String>();

	private boolean isSuccess = false;

	private String str_email = null;
	private String str_password = null;
	private String str_pet = null;
	private String str_phonenum = null;

	private AccountInfo account = null;

	private byte[] buffer = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_mode_reg_edit);
		init();
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case REGIST_SUCCESS:
				// 将activity加载进layout 并传入信息表示是从注册页面进入的
				if (upload) {
					new UploadHead().start();
				} else {
					closeGroupProgress();
					jump();
				}
				break;
			case REGIST_FAILED:
				// 注册失败 提示用户
				closeGroupProgress();
				if (Constants.ERRORMSG != null) {
					showToastShort(Constants.ERRORMSG);
				} else {
					showToastShort(getString(R.string.regist_failed));
				}
				break;
			case UPLOAD_SUCCESS:
				jump();
				break;
			case UPLOAD_FAILED:
				showToastShort(getString(R.string.common_upload_error));
				jump();
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
		btn_reg = (Button) findViewById(R.id.btn_mode_edit_reg);
		btn_reg.setOnClickListener(this);
		email = (EditText) findViewById(R.id.regist_email);
		password = (EditText) findViewById(R.id.regist_password);
		pet = (EditText) findViewById(R.id.regist_pet);
		phonenum = (EditText) findViewById(R.id.regist_phonenum);
		icon = (ImageView) findViewById(R.id.regist_icon);
		icon.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
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
							getParent().startActivityForResult(intent,
									REQUEST_TAKE);
						}
					},
					// 取消
					new DialogInterface.OnClickListener() {

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
							getParent().startActivityForResult(intent,
									REQUEST_IMAGES);
						}
					});
			break;
		case R.id.btn_mode_edit_reg:
			str_email = email.getText().toString().trim();
			str_password = password.getText().toString().trim();
			str_pet = pet.getText().toString().trim();
			str_phonenum = phonenum.getText().toString().trim();
			// 判断注册信息填写是否完整
			if (str_phonenum.length() > 11) {
				showToastShort(mContext.getString(R.string.regist_phone_err));
			} else {
				if (str_email != null && !"".equals(str_email)
						&& str_password != null && !"".equals(str_password)
						&& str_pet != null && !"".equals(str_pet)) {
					showGroupProgress(mContext.getString(R.string.wait_regist));
					new Thread() {

						@Override
						public void run() {
							super.run();
							account = ThriftOpe.registerAccount(mContext,
									str_email, str_phonenum, str_password,
									str_pet);
							Message msg = Message.obtain();
							if (account != null) {
								// 注册成功
								msg.what = REGIST_SUCCESS;
							} else {
								// 注册失败
								msg.what = REGIST_FAILED;
								// closeGroupProgress();
							}
							handler.sendMessage(msg);

						}
					}.start();
				} else {
					// 提示用户完整填写信息
					showToastShort(mContext.getString(R.string.regist_null));
				}
			}
			break;
		default:
			break;
		}
	}

	public void handleActivityResult(int requestCode, int resultCode,
			Intent data) {
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
		intent.putExtra("outputY", 280);// 输出图片大小
		intent.putExtra("return-data", true);
		getParent().startActivityForResult(intent, REQUEST_PICK);
	}

	/**
	 * 上传头像
	 * 
	 * @author 闫继超
	 * @version 1.00
	 */

	class UploadHead extends Thread {

		@Override
		public void run() {
			super.run();

			// 验证账户成功（获取userid）
			if (account != null && account.getUserId() != null) {
				String path = pathlist.get(pathlist.size() - 1);
				// 生成datacontent
				File file = new File(path);
				byte[] buf = new byte[(int) file.length()];
				try {
					new FileInputStream(file).read(buf);
				} catch (IOException e) {
					e.printStackTrace();
				}
				String format = Util.getFormat(path);
				// ByteArrayOutputStream baos = new ByteArrayOutputStream();
				// bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
				ByteBuffer bb = ByteBuffer.wrap(buf);
				ThriftOpe.login(mContext, Constants.ACCOUNTNAME,
						Constants.PASSWORD);
				boolean uploadsuccess = ThriftOpe.saveAvatar(mContext,
						account.getUserId(), format, bb);
				for (String address : pathlist) {
					Util.deleteFile(address);
				}
				closeGroupProgress();
				if (!uploadsuccess) {
					handler.sendEmptyMessage(UPLOAD_FAILED);
				} else {
					handler.sendEmptyMessage(UPLOAD_SUCCESS);
				}
			}
		}

	}

	private void jump() {
		Bundle bundle = new Bundle();
		bundle.putString("email", str_email);
		bundle.putString("password", str_password);
		bundle.putString("pet", str_pet);
		bundle.putString("phonenum", str_phonenum);
		bundle.putString("tag", "regist");
		ActivityGroup ag = (ActivityGroup) AddModeByRegEditActivity.this
				.getParent();
		ActivityJump.JumpAddModeByConfirmActivity(ag, (LinearLayout) ag
				.getWindow().findViewById(R.id.layoutAG), bundle);
	}

	/**
	 * 图库选择对话框
	 * 
	 * @param title
	 * @param message
	 * @param listenerPositive
	 * @param listenerNegative
	 * @param listenerNeutral
	 */
	public void showResult(final String title, final String message,
			final DialogInterface.OnClickListener listenerPositive,
			final DialogInterface.OnClickListener listenerNegative,
			final DialogInterface.OnClickListener listenerNeutral) {

		AddModeByRegEditActivity.this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				new AlertDialog.Builder(AddModeByRegEditActivity.this
						.getParent())
						.setTitle(title)
						.setMessage(message)
						.setPositiveButton(R.string.common_take,
								listenerPositive)
						.setNegativeButton(R.string.common_cancel,
								listenerNegative)
						.setNeutralButton(R.string.common_image,
								listenerNeutral).show();
			}
		});
	}
}
