/* ==================================================
 * 产品名: 亲情快递
 * 文件名: ImageSendActivity.java
 * --------------------------------------------------
 * 开发环境: JDK1.6
 * --------------------------------------------------
 * 修订履历    2012/07/07  1.00  初版发行
 * --------------------------------------------------
 * (C) Copyright XIKANG CORPORATION 2012 All Rights Reserved.
 */
package com.xikang.family.activity;

import java.io.File;
import java.util.Date;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.xikang.channel.common.rpc.thrift.message.DeviceType;
import com.xikang.channel.familyexpress.rpc.thrift.express.ExpressInfo;
import com.xikang.channel.familyexpress.rpc.thrift.express.ExpressType;
import com.xikang.channel.familyexpress.rpc.thrift.express.FEContentType;
import com.xikang.channel.familyexpress.rpc.thrift.express.FEFormatType;
import com.xikang.family.common.Constants;
import com.xikang.family.common.Util;
import com.xikang.family.service.ExpressDBService;
import com.xikang.family.service.LocalExpressInfo;

/**
 * 
 * 本地图片发送页面
 * 
 * 
 * 
 * @author 闫继超
 * @version 1.00
 */

public class ImageSendActivity extends BaseActivity {

	private TextView title = null;
	private Button send = null;
	private ImageView image = null;

	private String otheruid = null;

	private ExpressDBService db = null;

	private String path = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_image_send);
		init();
	}

	private void init() {
		db = new ExpressDBService(mContext);

		title = (TextView) findViewById(R.id.tvTBContent);
		title.setText(R.string.send_image);
		send = (Button) findViewById(R.id.btnTBRight);
		send.setBackgroundResource(R.drawable.btn_confirm_selector);
		send.setText(R.string.common_send);
		send.setVisibility(View.VISIBLE);
		// 图片发送按钮
		send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				saveExpress(new File(path));
				ImageSendActivity.this.finish();
			}
		});
		image = (ImageView) findViewById(R.id.imagesend);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			otheruid = bundle.getString("otheruid");
			path = bundle.getString("path");
		}
		// 显示图片
		image.setImageBitmap(Util.decodeByFile(new File(path), 540, 840));

	};

	// 将要发送的快递存入本地数据库
	private void saveExpress(final File file) {
		ExpressInfo mExpressInfo = new ExpressInfo();
		mExpressInfo.setFrom(Constants.USERID);
		mExpressInfo.setTo(otheruid);
		mExpressInfo.setFromTerminalType(DeviceType.ANDROID);
		mExpressInfo.setExpressType(ExpressType.NORMAL);
		mExpressInfo.setTime(Util.getCurrentDate());
		mExpressInfo.setUpdateTime(new Date().getTime());
		mExpressInfo.setExpressStatus(null);
		LocalExpressInfo local = new LocalExpressInfo();

		mExpressInfo.setContentType(FEContentType.IMAGE);
		mExpressInfo.setFormat(FEFormatType.JPG);
		mExpressInfo.setLength((short) 1);
		// 原图
		local.setFilename(file.getPath());
		// 缩略图
		String path = Util.getThumbnail(file);
		local.setThumbnail(path);
		local.setExpressinfo(mExpressInfo);
		Log.i("YANJCH", local.getFilename());
		Log.i("YANJCH", local.getThumbnail());
		db.insert(local);
	}
}
