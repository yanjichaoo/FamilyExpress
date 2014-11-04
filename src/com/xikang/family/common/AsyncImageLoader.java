/* ==================================================
 * 产品名: 亲情快递
 * 文件名: AsyncImageLoader.java
 * --------------------------------------------------
 * 开发环境: JDK1.6
 * --------------------------------------------------
 * 修订履历    2012/07/07  1.00  初版发行
 * --------------------------------------------------
 * (C) Copyright XIKANG CORPORATION 2012 All Rights Reserved.
 */
package com.xikang.family.common;

import java.lang.ref.SoftReference;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import com.xikang.channel.familyexpress.rpc.thrift.express.FEFormatType;

/**
 * 
 * 异步加载图片
 * 
 * 
 * 
 * @author 张荣
 * @version 1.00
 */

public class AsyncImageLoader {
	private HashMap<String, SoftReference<Bitmap>> imageCache;

	public AsyncImageLoader() {
		imageCache = new HashMap<String, SoftReference<Bitmap>>();
	}

	public Bitmap loadBitmap(final String linkAddress, final String userid,
			final ImageView imageView, final Context context,
			final ImageCallback imageCallback) {
		if (imageCache.containsKey(linkAddress)) {
			SoftReference<Bitmap> softReference = imageCache.get(linkAddress);
			Bitmap bitmap = softReference.get();
			if (bitmap != null) {
				return bitmap;

			}
		}
		final Handler handler = new Handler() {
			public void handleMessage(Message message) {
				switch (message.what) {
				case 1:
					Bitmap bitmap = BitmapFactory
							.decodeFile((String) message.obj);
					imageCache.put(linkAddress, new SoftReference<Bitmap>(
							bitmap));
					imageCallback.imageLoaded((String) message.obj, bitmap,
							imageView);
					break;
				case 0:
					break;
				default:
					break;
				}
			}
		};
		new Thread() {
			@Override
			public void run() {
				String path = null;
				try {
					if (userid != null) {
						path = HttpClientUtil.requestPostStream(linkAddress,
								userid, null, FEFormatType.JPG);
					} else {
						path = HttpClientUtil.requestPostStream(linkAddress,
								null, FEFormatType.JPG);
					}
					System.out.println("地址为  " + linkAddress
							+ "  的图片下载到本地的地址为 " + path);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (path != null) {

					Message message = handler.obtainMessage(1, path);
					handler.sendMessage(message);
				} else {
					handler.sendEmptyMessage(0);
				}
			}
		}.start();
		return null;
	}

	public interface ImageCallback {
		public void imageLoaded(String path, Bitmap imageBitmap,
				ImageView imageView);
	}
}
