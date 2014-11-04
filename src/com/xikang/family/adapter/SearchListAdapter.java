/* ==================================================
 * 产品名: 亲情快递
 * 文件名: SearchListAdapter.java
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xikang.channel.base.rpc.thrift.user.UserInfo;
import com.xikang.family.activity.R;
import com.xikang.family.common.AsyncImageLoader;
import com.xikang.family.common.AsyncImageLoader.ImageCallback;

/**
 * 
 * 搜索家人列表适配器
 * 
 * 
 * 
 * @author 闫继超
 * @version 1.00
 */

public class SearchListAdapter extends BaseAdapter {

	LayoutInflater mInflater = null;
	List<UserInfo> list = null;
	Context mContext = null;
	AsyncImageLoader imageLoader = null;

	class User {
		ImageView image = null;
		TextView email = null;
		TextView phone = null;
	}

	public void setList(List<UserInfo> list) {
		this.list = list;
	}

	public SearchListAdapter(Context context, List<UserInfo> list) {
		super();
		mContext = context;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.list = list;
		imageLoader = new AsyncImageLoader();
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
		UserInfo info = (UserInfo) getItem(position);
		User user;
		if (convertView == null) {
			convertView = mInflater.inflate(
					R.layout.layout_mode_user_list_item, null);
			user = new User();
			user.image = (ImageView) convertView.findViewById(R.id.item_head);
			user.email = (TextView) convertView.findViewById(R.id.item_email);
			user.phone = (TextView) convertView.findViewById(R.id.item_phone);
			convertView.setTag(user);
			Bitmap bitmap = imageLoader.loadBitmap(info.getFigureUrl(),
					info.getUserId(), user.image, mContext,
					new ImageCallback() {

						@Override
						public void imageLoaded(String path,
								Bitmap imageBitmap, ImageView imageView) {
							imageView.setImageBitmap(imageBitmap);
						}
					});
			if (bitmap != null) {
				user.image.setImageBitmap(bitmap);
			}
			user.email.setText(info.getEmail());
			user.phone.setText(info.getMobileNum());
		} else {
			user = (User) convertView.getTag();
			user.email.setText(info.getEmail());
			user.phone.setText(info.getMobileNum());
			Bitmap bitmap = imageLoader.loadBitmap(info.getFigureUrl(),
					info.getUserId(), user.image, mContext,
					new ImageCallback() {

						@Override
						public void imageLoaded(String path,
								Bitmap imageBitmap, ImageView imageView) {
							imageView.setImageBitmap(imageBitmap);
						}
					});
			if (bitmap != null) {
				user.image.setImageBitmap(bitmap);
			}
		}
		return convertView;
	}

}
