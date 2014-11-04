/* ==================================================
 * 产品名: 亲情快递
 * 文件名: RecordListAdapter.java
 * --------------------------------------------------
 * 开发环境: JDK1.6
 * --------------------------------------------------
 * 修订履历    2012/07/07  1.00  初版发行
 * --------------------------------------------------
 * (C) Copyright XIKANG CORPORATION 2012 All Rights Reserved.
 */
package com.xikang.family.adapter;

import java.util.List;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xikang.channel.familyexpress.rpc.thrift.family.UserBaseInfo;
import com.xikang.family.activity.R;
import com.xikang.family.common.AsyncImageLoader;
import com.xikang.family.common.AsyncImageLoader.ImageCallback;
import com.xikang.family.common.Constants;
import com.xikang.family.common.Util;
import com.xikang.family.service.LocalExpressInfo;

/**
 * 
 * 会话列表适配器
 * 
 * 
 * 
 * @author 闫继超
 * @version 1.00
 */

public class RecordListAdapter extends BaseAdapter {

	private static final String TAG = "RecordListAdapter";
	LayoutInflater mInflater = null;
	List<LocalExpressInfo> list = null;
	List<UserBaseInfo> familylist = null;
	Context mContext = null;
	AsyncImageLoader imageLoader = null;

	class Record {
		ImageView image = null;
		TextView name = null;
		TextView time = null;
		TextView record = null;
		ImageView record_icon = null;
	}

	public void setList(List<LocalExpressInfo> list,
			List<UserBaseInfo> familylist) {
		this.list = list;
		this.familylist = familylist;
	}

	public RecordListAdapter(Context context, List<LocalExpressInfo> list,
			List<UserBaseInfo> familylist) {
		super();
		mContext = context;
		this.list = list;
		this.familylist = familylist;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader = new AsyncImageLoader();
	}

	@Override
	public int getCount() {
		return list == null ? 0 : list.size();
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
		Record record = null;
		LocalExpressInfo info = (LocalExpressInfo) getItem(position);
		if (convertView == null) {
			convertView = mInflater.inflate(
					R.layout.layout_express_record_item, null);
			record = new Record();
			record.image = (ImageView) convertView
					.findViewById(R.id.record_head);
			record.name = (TextView) convertView
					.findViewById(R.id.record__name);
			record.time = (TextView) convertView
					.findViewById(R.id.record__time);
			record.record = (TextView) convertView
					.findViewById(R.id.record_text);
			record.record_icon = (ImageView) convertView
					.findViewById(R.id.record_icon);
			convertView.setTag(record);
			setInfo(record, position, info);
		} else {
			record = (Record) convertView.getTag();
			setInfo(record, position, info);
		}
		return convertView;
	}

	private void setInfo(Record record, int position, LocalExpressInfo info) {
		if(familylist == null){
			Log.e(TAG,"FAMILYLIST NULL");
			return;
		}
		if(familylist.size() == 0 || position < 0){
			Log.e(TAG,"FAMILYLIST POSTION NG");
			return;
		}
		UserBaseInfo user = familylist.get(position);
		if (familylist != null && user != null) {
			// 设置名字
			record.name
					.setText(user.getUserName() == null ? user.getRelation()
							: user.getRelation().concat("-")
									.concat(user.getUserName()));
			// 异步加载头像
			Bitmap cache = imageLoader.loadBitmap(user.getFigureUrl(),user.getUserId(),
					record.image, mContext, new ImageCallback() {

						@Override
						public void imageLoaded(String path,
								Bitmap imageBitmap, ImageView imageView) {
							imageView.setImageBitmap(imageBitmap);
						}
					});
			if (cache != null) {
				record.image.setImageBitmap(cache);
			}

		}
		// 设置时间
		record.time.setText(Util
				.parseMs2String(info.getExpressinfo().getTime()));
		// 根据快递类型 处理显示的文本
		int type = info.getExpressinfo().getContentType().getValue();
		if (Constants.FEContentType.TEXT == type) {
			if (familylist != null && user != null) {
				String str = null;
				// 自己发的 "我说"
				if (Constants.USERID.equals(info.getExpressinfo().getFrom())) {
					str = mContext.getString(R.string.record_text_self).concat(
							mContext.getString(R.string.record_text_send));
				} else {
					// 别人发的 "对方说"
					str = user.getRelation().concat(
							mContext.getString(R.string.record_text_send));
				}
				String text = str
						.concat(info.getExpressinfo().getTextContent());
				if (text.length() > 22) {
					text = text.substring(0, 21).concat("......");
				}
				record.record.setText(text);
			}
			record.record_icon.setVisibility(View.GONE);
		} else {
			if (Constants.USERID.equals(info.getExpressinfo().getFrom())) {
				record.record.setText(mContext
						.getString(R.string.record_image_send));
			} else {
				record.record.setText(mContext
						.getString(R.string.record_image_get));
			}
		}
		if (Constants.FEContentType.AUDIO == type) {
			record.record_icon
					.setBackgroundResource(R.drawable.record_icon_audio);
			record.record_icon.setVisibility(View.VISIBLE);
		}
		if (Constants.FEContentType.IMAGE == type) {
			record.record_icon
					.setBackgroundResource(R.drawable.record_icon_image);
			record.record_icon.setVisibility(View.VISIBLE);
		}
	}
}