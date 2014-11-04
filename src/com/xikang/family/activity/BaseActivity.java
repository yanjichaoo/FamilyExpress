/* ==================================================
 * 产品名: 亲情快递
 * 文件名: BaseActivity.java
 * --------------------------------------------------
 * 开发环境: JDK1.6
 * --------------------------------------------------
 * 修订履历    2012/07/07  1.00  初版发行
 * --------------------------------------------------
 * (C) Copyright XIKANG CORPORATION 2012 All Rights Reserved.
 */
package com.xikang.family.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * 提供activity基础功能
 * 
 * 
 * 
 * @author 闫继超
 * @version 1.00
 */

public class BaseActivity extends Activity {
	// 进度条
	private ProgressDialog processDialog;

	protected Context mContext;

	private boolean isReg = false;

	private BroadcastReceiver mFinishReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if ("finish".equals(intent.getAction())) {
				finish();
			}
		}
	};

	// @Override
	// protected void onResume() {
	// super.onResume();
	// if (isReg) {
	// this.unregisterReceiver(mFinishReceiver);
	// isReg = false;
	// }
	// }

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (isReg) {
			this.unregisterReceiver(mFinishReceiver);
			isReg = false;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this.getApplicationContext();
		IntentFilter filter = new IntentFilter();
		filter.addAction("finish");

		if (!isReg) {
			registerReceiver(mFinishReceiver, filter);
			isReg = true;
		}

	}

	/**
	 * 标题
	 * 
	 * @param title
	 */
	public void showTitle(String title) {
		TextView tvTBContent = (TextView) findViewById(R.id.tvTBContent);
		tvTBContent.setText(title);
	}

	/**
	 * 左按钮
	 * 
	 * @param leftText
	 * @param listener
	 */
	public void showLeftButton(String leftText, OnClickListener listener) {
		Button btnTBLeft = (Button) findViewById(R.id.btnTBLeft);
		btnTBLeft.setVisibility(View.VISIBLE);
		btnTBLeft.setText(leftText);
		btnTBLeft.setOnClickListener(listener);
	}

	/**
	 * 右按钮
	 * 
	 * @param rightText
	 * @param listener
	 */
	public void showRightButton(String rightText, OnClickListener listener) {
		Button btnTBRight = (Button) findViewById(R.id.btnTBRight);
		btnTBRight.setVisibility(View.VISIBLE);
		btnTBRight.setText(rightText);
		btnTBRight.setOnClickListener(listener);
	}

	/**
	 * 组弹出进度框
	 * 
	 * @param message
	 */
	public void showGroupProgress(final String message) {
		BaseActivity.this.getParent().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				processDialog = new ProgressDialog(BaseActivity.this
						.getParent());
				processDialog.setMessage(message);
				processDialog.setIndeterminate(true);
				processDialog.show();

			}
		});

	}

	/**
	 * 组关闭进度框
	 * 
	 * @param message
	 */
	public void closeGroupProgress() {
		BaseActivity.this.getParent().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (processDialog != null && processDialog.isShowing()) {
					processDialog.dismiss();
				}
			}
		});
	}

	/**
	 * 弹出进度框
	 * 
	 * @param message
	 */
	public void showProgress(final String message) {
		BaseActivity.this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				processDialog = new ProgressDialog(BaseActivity.this);
				processDialog.setMessage(message);
				processDialog.setIndeterminate(true);
				processDialog.show();

			}
		});

	}

	/**
	 * 关闭进度框
	 * 
	 * @param message
	 */
	public void closeProgress() {
		BaseActivity.this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (processDialog != null && processDialog.isShowing()) {
					processDialog.dismiss();
				}
			}
		});
	}

	/**
	 * 提示信息
	 * 
	 * @param message
	 */
	public void showToastShort(String message) {
		Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 提示信息
	 * 
	 * @param message
	 */
	public void showToastLong(String message) {
		Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
	}

	/**
	 * 显示结果
	 * 
	 * @param message
	 */
	public void showResult(final String message) {
		showResult(null, message, null);
	}

	/**
	 * 显示结果
	 * 
	 * @param message
	 */
	public void showResult(final String title, final String message) {
		showResult(title, message, null);
	}

	/**
	 * 显示结果
	 * 
	 * @param message
	 */
	public void showResult(final String title, final String message,
			final DialogInterface.OnClickListener listener) {

		BaseActivity.this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				new AlertDialog.Builder(BaseActivity.this).setTitle(title)
						.setMessage(message)
						.setPositiveButton(R.string.common_confirm, listener)
						.show();
			}
		});
	}

	/**
	 * 显示结果
	 * 
	 * @param message
	 */
	public void showResult(final String title, final String message,
			final DialogInterface.OnClickListener listenerPositive,
			final DialogInterface.OnClickListener listenerNegative) {

		BaseActivity.this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				new AlertDialog.Builder(BaseActivity.this)
						.setTitle(title)
						.setMessage(message)
						.setPositiveButton(R.string.common_confirm,
								listenerPositive)
						.setNegativeButton(R.string.common_cancel,
								listenerNegative).show();
			}
		});
	}

	/**
	 * 显示结果 对话框显示期间 忽略back键
	 * 
	 * @param message
	 */
	public void showResultDisableBack(final String title, final String message,
			final DialogInterface.OnClickListener listenerPositive,
			final DialogInterface.OnClickListener listenerNegative) {

		BaseActivity.this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				new AlertDialog.Builder(BaseActivity.this)
						.setTitle(title)
						.setMessage(message)
						.setPositiveButton(R.string.common_confirm,
								listenerPositive)
						.setNegativeButton(R.string.common_cancel,
								listenerNegative).setCancelable(false).show();
			}
		});
	}

	/**
	 * 单选对话框
	 * 
	 * @param message
	 */
	public void showSingleDialog(final String title, final int itemId,
			final DialogInterface.OnClickListener listenerPositive) {

		BaseActivity.this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				new AlertDialog.Builder(BaseActivity.this).setTitle(title)
						.setSingleChoiceItems(itemId, 1, listenerPositive)
						.show();
			}
		});
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

		BaseActivity.this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				new AlertDialog.Builder(BaseActivity.this)
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
