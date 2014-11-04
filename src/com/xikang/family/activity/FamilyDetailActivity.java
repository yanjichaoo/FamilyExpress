/* ==================================================
 * 产品名: 亲情快递
 * 文件名: FamilyDetailActivity.java
 * --------------------------------------------------
 * 开发环境: JDK1.6
 * --------------------------------------------------
 * 修订履历    2012/07/07  1.00  初版发行
 * --------------------------------------------------
 * (C) Copyright XIKANG CORPORATION 2012 All Rights Reserved.
 */
package com.xikang.family.activity;

import com.xikang.channel.familyexpress.rpc.thrift.family.AllowStatus;
import com.xikang.family.common.ActivityJump;
import com.xikang.family.common.AsyncImageLoader;
import com.xikang.family.common.AsyncImageLoader.ImageCallback;
import com.xikang.family.common.ThriftOpe;

import android.app.ActivityGroup;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 
 * 家人详细信息页面
 * 
 * 
 * 
 * @author 张荣
 * @version 1.00
 */

public class FamilyDetailActivity extends BaseActivity {

	private TextView tvTBContent = null;
	private Button btnTBLeft = null;
	private TextView tvUseName = null;
	private TextView tvNikeName = null;
	private TextView tvRelation = null;
	private TextView tvMobile = null;
	private TextView tvEmail = null;
	private ImageView ivImage = null;
	private ImageView ivWaitImage = null;
	private Button btnSend = null;
	private Button btnNotify = null;

	private Bundle mBundle = null;
	private AsyncImageLoader mImageLoader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_family_detail);
		mBundle = this.getIntent().getExtras();
		mImageLoader = new AsyncImageLoader();
		init();
	}

	@Override
	protected void onResume() {
		super.onResume();
		loaddata();
	}

	private void init() {

		tvTBContent = (TextView) findViewById(R.id.tvTBContent);
		btnTBLeft = (Button) findViewById(R.id.btnTBLeft);
		tvUseName = (TextView) findViewById(R.id.family_detail_username);
		tvNikeName = (TextView) findViewById(R.id.family_detail_nikename);
		tvRelation = (TextView) findViewById(R.id.family_detail_relation);
		tvMobile = (TextView) findViewById(R.id.family_detail_mobile);
		tvEmail = (TextView) findViewById(R.id.family_detail_email);
		ivImage = (ImageView) findViewById(R.id.family_detail_icon);
		ivWaitImage = (ImageView) findViewById(R.id.family_detail_wait);
		btnSend = (Button) findViewById(R.id.family_detail_send);
		btnNotify = (Button) findViewById(R.id.family_detail_notify);

		btnTBLeft.setVisibility(View.VISIBLE);
		btnTBLeft.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ActivityGroup ag = (ActivityGroup) FamilyDetailActivity.this
						.getParent();
				ActivityJump.JumpFamilyListActivity(ag, (LinearLayout) ag
						.getWindow().findViewById(R.id.layoutAG));
			}
		});

		btnSend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 加载ChatActivity
				Intent mIntent = new Intent(FamilyDetailActivity.this,
						ChatActivity.class);

				Bundle bundle = new Bundle();
				bundle.putString("otheruid", mBundle.getString("userId"));
				bundle.putString("userName", mBundle.getString("userName"));
				bundle.putString("relation", mBundle.getString("relation"));
				mIntent.putExtras(bundle);
				startActivity(mIntent);
			}
		});

		btnNotify.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				String sms = ThriftOpe
						.getNoteMessage(FamilyDetailActivity.this);
				if (sms != null) {
					// 发送短信
					Uri smsToUri = Uri.parse("smsto:");
					Intent mIntent = new Intent(
							android.content.Intent.ACTION_SENDTO, smsToUri);
					mIntent.putExtra("sms_body", sms);
					startActivity(mIntent);
				}
			}
		});
	}

	private void loaddata() {
		tvTBContent.setText(mBundle.getString("userName") == null ? mBundle
				.getString("relation") : mBundle.getString("relation")
				.concat("-").concat(mBundle.getString("userName")));
		tvUseName.setText(mBundle.getString("userName") == null ? "" : mBundle
				.getString("userName"));
		tvRelation.setText(mBundle.getString("relation"));
		tvNikeName.setText(tvNikeName
				.getText()
				.toString()
				.replace(
						"@nikename",
						mBundle.getString("nikeName") == null ? "" : mBundle
								.getString("nikeName")));
		tvMobile.setText(tvMobile
				.getText()
				.toString()
				.replace(
						"@mobile",
						mBundle.getString("mobile") == null ? "" : mBundle
								.getString("mobile")));
		tvEmail.setText(tvEmail
				.getText()
				.toString()
				.replace(
						"@email",
						mBundle.getString("email") == null ? "" : mBundle
								.getString("email")));

		if (mBundle.getInt("allowStatus") == AllowStatus.INVITED.getValue()) {
			btnSend.setVisibility(View.GONE);
			ivWaitImage.setVisibility(View.VISIBLE);
		} else {
			btnSend.setVisibility(View.VISIBLE);
			ivWaitImage.setVisibility(View.GONE);
		}
		// 异步加载头像
		mImageLoader.loadBitmap(mBundle.getString("figureUrl"),
				mBundle.getString("userId"), ivImage, this,
				new ImageCallback() {
					@Override
					public void imageLoaded(String path, Bitmap imageBitmap,
							ImageView imageView) {
						imageView.setImageBitmap(imageBitmap);
					}
				});

	}

}
