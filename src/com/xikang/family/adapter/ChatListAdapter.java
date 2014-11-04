/* ==================================================
 * 产品名: 亲情快递
 * 文件名: ChatListAdapter.java
 * --------------------------------------------------
 * 开发环境: JDK1.6
 * --------------------------------------------------
 * 修订履历    2012/07/07  1.00  初版发行
 * --------------------------------------------------
 * (C) Copyright XIKANG CORPORATION 2012 All Rights Reserved.
 */
package com.xikang.family.adapter;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.xikang.channel.familyexpress.rpc.thrift.express.ExpressInfo;
import com.xikang.channel.familyexpress.rpc.thrift.express.ExpressStatus;
import com.xikang.family.activity.R;
import com.xikang.family.common.AsyncImageLoader;
import com.xikang.family.common.AsyncImageLoader.ImageCallback;
import com.xikang.family.common.Constants;
import com.xikang.family.common.Util;
import com.xikang.family.service.ExpressDBService;
import com.xikang.family.service.LocalExpressInfo;

/**
 * 
 * 聊天列表适配器
 * 
 * 
 * 
 * @author 闫继超
 * @version 1.00
 */

public class ChatListAdapter extends BaseAdapter {

	private static final int AUDIO_FLUSH = 5003;

	LayoutInflater mInflater = null;
	List<LocalExpressInfo> list = null;
	Context context = null;
	// 是否正在播放语音
	boolean isPlaying = false;
	int playPos = -1;
	Handler handler = null;

	private AsyncImageLoader mImageLoader = null;

	private ExpressDBService db = null;

	class ChatHolder {
		FrameLayout poplayout = null;
		// 快递时间
		TextView express_time = null;
		// 语音图片
		ImageView audio = null;
		// 图片快递
		ImageView image = null;
		// 文本快递信息
		TextView text = null;
		// 语音时长
		TextView audio_time = null;
		// 未读语音提示
		ImageView unread = null;
		// 快递状态显示
		TextView unreadStatus = null;
		// 测量种类图片
		ImageView measure_icon = null;
		// 测量具体信息
		TextView measure_info = null;
		// 测量时间
		TextView measure_time = null;
	}

	public void setList(List<LocalExpressInfo> list) {
		this.list = list;
	}

	public void setPlaying(boolean isPlaying) {
		this.isPlaying = isPlaying;
	}

	public void setPlayPos(int pos) {
		this.playPos = pos;
	}

	public ChatListAdapter(Context context, List<LocalExpressInfo> list,
			Handler handler) {
		super();

		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.list = list;
		this.context = context;
		this.handler = handler;
		db = new ExpressDBService(context);
		this.mImageLoader = new AsyncImageLoader();
	}

	@Override
	public int getCount() {
		return list != null ? list.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LocalExpressInfo info = (LocalExpressInfo) getItem(position);
		if (Constants.USERID.equals(info.getExpressinfo().getFrom())) {
			// 自己发的
			convertView = setView(true, convertView, position, info);
		} else {
			// 亲友发的
			convertView = setView(false, convertView, position, info);
		}
		return convertView;
	}

	private View setView(final boolean isSelf, View convertView,
			final int position, final LocalExpressInfo local) {
		final ExpressInfo info = local.getExpressinfo();
		Bitmap mbitmap = null;
		// 加载布局文件
		if (isSelf) {

			if (Constants.ExpressType.NORMAL == info.getExpressType()
					.getValue()) {
				// 加载聊天布局文件
				convertView = mInflater
						.inflate(R.layout.layout_chat_self, null);
			} else {
				// 加载益体机测量数据布局文件
				convertView = mInflater.inflate(
						R.layout.layout_chat_self_measure, null);
			}
		} else {
			if (Constants.ExpressType.NORMAL == info.getExpressType()
					.getValue()) {
				// 加载聊天布局文件
				convertView = mInflater.inflate(R.layout.layout_chat_others,
						null);
			} else {
				// 加载益体机测量数据布局文件
				convertView = mInflater.inflate(
						R.layout.layout_chat_others_measure, null);
			}
		}

		final ChatHolder holder = new ChatHolder();
		// 文本信息
		if (Constants.FEContentType.TEXT == info.getContentType().getValue()) {
			if (Constants.ExpressType.NORMAL == info.getExpressType()
					.getValue()) {
				// 聊天
				holder.text = (TextView) convertView
						.findViewById(R.id.express_text);
				String text = info.getTextContent();
				holder.text.setText(text);
				holder.text.setVisibility(View.VISIBLE);
			} else {
				// 益体机测量数据
				holder.measure_icon = (ImageView) convertView
						.findViewById(R.id.measure_icon);
				if (Constants.ExpressType.BLOODSUGAR == info.getExpressType()
						.getValue()) {
					holder.measure_icon
							.setBackgroundResource(R.drawable.icon_xt);
				}
				if (Constants.ExpressType.BLOODOXYGEN == info.getExpressType()
						.getValue()) {
					// TODO 血氧图片未提供
				}
				if (Constants.ExpressType.BLOODPRESSURE == info
						.getExpressType().getValue()) {
					holder.measure_icon
							.setBackgroundResource(R.drawable.icon_xy);
				}
				holder.measure_info = (TextView) convertView.findViewById(R.id.measure_info);
				String text = info.getTextContent();
				holder.measure_info.setText(text);
				holder.measure_info.setVisibility(View.VISIBLE);
				holder.measure_time = (TextView) convertView.findViewById(R.id.measure_time);
			}
		}
		// 语音信息
		if (Constants.FEContentType.AUDIO == info.getContentType().getValue()) {
			holder.audio_time = (TextView) convertView.findViewById(R.id.audio_time);
			holder.audio_time.setText(String.valueOf(info.length) + '"');
			holder.audio_time.setVisibility(View.VISIBLE);
			holder.audio = (ImageView) convertView.findViewById(R.id.express_audio);
			// 是否正在播放语音
			if (isPlaying) {
				// 判断语音播放位置 是否是当前行
				if (playPos == position) {
					// 通知acitivity继续播放语音播放动画效果
					Message msg = Message.obtain();
					msg.what = AUDIO_FLUSH;
					msg.arg1 = isSelf ? 1 : 0;
					msg.obj = holder.audio;
					handler.sendMessage(msg);
				} else {
					// 设置默认语音背景图片
					if (isSelf) {
						holder.audio.setBackgroundResource(R.drawable.sound);
					} else {
						holder.audio
								.setBackgroundResource(R.drawable.sound_others);
					}
				}
			}
			holder.audio.setVisibility(View.VISIBLE);
			if (Constants.USERID.equals(info.getTo())
					&& info.getExpressStatus().getValue() == ExpressStatus.UNREAD
							.getValue()) {
				holder.unread = (ImageView) convertView
						.findViewById(R.id.audio_unread);
				holder.unread.setVisibility(View.VISIBLE);
			}

		}
		// 图片信息
		if (Constants.FEContentType.IMAGE == info.getContentType().getValue()) {

			Log.e("LLLLL", "图片快递");
			holder.image = (ImageView) convertView.findViewById(R.id.express_image);
			
			
			// 判断本地是否有缩略图
			if (local.getThumbnail() == null) {
				// 没有的话 从服务器下载
				Log.i("chatlistadapter ", "getThumbnail null");
				String url = info.getTextContent();
				String thumbnailUrl = url.substring(0, url.lastIndexOf("."))
						.concat("_160_160")
						.concat(url.substring(url.lastIndexOf(".")));

				Log.e("chatlistadapter", "url = " + thumbnailUrl);
				Bitmap bitmap = mImageLoader.loadBitmap(thumbnailUrl, null,
						holder.image, context, new ImageCallback() {

							@Override
							public void imageLoaded(String path, Bitmap imageBitmap, ImageView imageView) {
								imageView.setImageBitmap(imageBitmap);
								db.updateThumbNailByEid(info.getExpressId(),path);
								local.setThumbnail(path);
							}
						});
				if (bitmap != null) {
					Log.e("chatlistadapter", "bitmap not null");
					holder.image.setImageBitmap(bitmap);
				} else {
					Log.e("chatlistadapter", "bitmap null");
					
					bitmap = mImageLoader.loadBitmap(url, null,
							holder.image, context, new ImageCallback() {

								@Override
								public void imageLoaded(String path, Bitmap imageBitmap, ImageView imageView) {
									imageView.setImageBitmap(imageBitmap);
									db.updateThumbNailByEid(info.getExpressId(),path);
									local.setThumbnail(path);
								}
							});
					holder.image.setImageBitmap(bitmap);
				}
			} else {
				mbitmap = BitmapFactory.decodeFile(local.getThumbnail());
				holder.image.setImageBitmap(mbitmap);
			}
			holder.image.setVisibility(View.VISIBLE);

		}
		// 时间显示需要处理一下字符串
		holder.express_time = (TextView) convertView
				.findViewById(R.id.express_time);
		holder.express_time.setText(Util.parseMs2String(info.getTime()));
		// 设置快递状态（只有自己发的需要设置）
		if (Constants.USERID.equals(info.getFrom())) {
			holder.unreadStatus = (TextView) convertView
					.findViewById(R.id.express_status);
			if (info.getExpressStatus() != null) {
				switch (info.getExpressStatus().getValue()) {
				case Constants.ExpressStatus.SENT:
					holder.unreadStatus.setText(context
							.getString(R.string.status_sent));
					break;
				case Constants.ExpressStatus.FORWARDED:
					holder.unreadStatus.setText(context
							.getString(R.string.status_forward));
					break;
				case Constants.ExpressStatus.UNREAD:
					holder.unreadStatus.setText(context
							.getString(R.string.status_unread));
					break;
				case Constants.ExpressStatus.READ:
					holder.unreadStatus.setText(context
							.getString(R.string.status_read));
					break;
				case Constants.ExpressStatus.WAIT:
					holder.unreadStatus.setText(context
							.getString(R.string.status_wait));
					break;
				default:
					holder.unreadStatus.setText(context
							.getString(R.string.status_wait));
					break;
				}
			} else {
				holder.unreadStatus.setText(context
						.getString(R.string.status_wait));
			}
		}

		return convertView;
	}

}