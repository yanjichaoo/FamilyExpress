/* ==================================================
 * 产品名: 亲情快递
 * 文件名: ImageCheckActivity.java
 * --------------------------------------------------
 * 开发环境: JDK1.6
 * --------------------------------------------------
 * 修订履历    2012/07/07  1.00  初版发行
 * --------------------------------------------------
 * (C) Copyright XIKANG CORPORATION 2012 All Rights Reserved.
 */
package com.xikang.family.activity;

import java.io.File;

import android.os.Bundle;
import android.widget.ImageView;

import com.xikang.family.common.Util;

/**
 * 
 * 聊天页面点击缩略图进入的原图页面
 * 
 * 
 * 
 * @author 闫继超
 * @version 1.00
 */

public class ImageCheckActivity extends BaseActivity {

	private ImageView image = null;
	private String path = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_image_check);
		init();
		// 加载图片
		loadImage();
	}

	private void init() {
		// 读取传入的信息
		path = getIntent().getStringExtra("path");
		image = (ImageView) findViewById(R.id.bigimage);
	}

	private void loadImage() {
		if (path == null) {
			showToastShort(getString(R.string.loading_failed));
			this.finish();
			return;
		} else {
			image.setImageBitmap(Util.decodeByFile(new File(path), 540, 840));
		}
	}
}
