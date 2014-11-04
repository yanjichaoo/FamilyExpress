/* ==================================================
 * 产品名: 亲情快递
 * 文件名: FamilyListAdapter.java
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

import com.xikang.channel.familyexpress.rpc.thrift.family.AllowStatus;
import com.xikang.channel.familyexpress.rpc.thrift.family.UserBaseInfo;
import com.xikang.family.activity.R;
import com.xikang.family.common.AsyncImageLoader;
import com.xikang.family.common.AsyncImageLoader.ImageCallback;

/**
 * 
 * 家人列表适配器
 * 
 * 
 * 
 * @author 张荣
 * @version 1.00
 */

public class FamilyListAdapter extends BaseAdapter {

	private LayoutInflater mInflater = null;
	private List<UserBaseInfo> mFamilyMemberlist = null;
	private AsyncImageLoader mImageLoader;
	private Context mContext = null;

	class MemberViewHolder {
		ImageView image = null;
		TextView name = null;
		TextView nikeName = null;
		TextView wait = null;
	}

	public FamilyListAdapter(Context context,
			List<UserBaseInfo> familyMemberlist) {
		super();
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mFamilyMemberlist = familyMemberlist;
		this.mContext = context;
		this.mImageLoader = new AsyncImageLoader();
	}

	public void setList(List<UserBaseInfo> familyMemberlist) {
		this.mFamilyMemberlist = familyMemberlist;
	}

	@Override
	public int getCount() {
		return mFamilyMemberlist != null ? mFamilyMemberlist.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return mFamilyMemberlist.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		MemberViewHolder memberViewHolder = null;
		UserBaseInfo userInfo = (UserBaseInfo) getItem(position);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.layout_family_list_item,
					null);
			memberViewHolder = new MemberViewHolder();
			memberViewHolder.image = (ImageView) convertView
					.findViewById(R.id.family_item_image);
			memberViewHolder.name = (TextView) convertView
					.findViewById(R.id.family_item_name);
			memberViewHolder.nikeName = (TextView) convertView
					.findViewById(R.id.family_item_nikename);
			memberViewHolder.wait = (TextView) convertView
					.findViewById(R.id.family_item_wait);
			convertView.setTag(memberViewHolder);
			setView(memberViewHolder, userInfo);
		} else {
			memberViewHolder = (MemberViewHolder) convertView.getTag();
			setView(memberViewHolder, userInfo);
		}
		return convertView;
	}

	private void setView(MemberViewHolder memberViewHolder,
			UserBaseInfo userInfo) {
		// 异步加载头像
		Bitmap bm = mImageLoader.loadBitmap(userInfo.getFigureUrl(),userInfo.getUserId(),
				memberViewHolder.image, mContext, new ImageCallback() {
					@Override
					public void imageLoaded(String path, Bitmap imageBitmap,
							ImageView imageView) {
						imageView.setImageBitmap(imageBitmap);
					}
				});

		if (bm != null) {
			memberViewHolder.image.setImageBitmap(bm);
		}
		String relation = userInfo.getRelation();
		StringBuffer buffer = new StringBuffer();
		if ((relation != null) && (!relation.trim().equals(""))) {
			buffer.append(relation);
		}
		String userName = userInfo.getUserName();
		if ((userName != null) && (!userName.trim().equals(""))) {
			buffer.append("—");
			buffer.append(userName);
		}

		memberViewHolder.name.setText(buffer.length() > 8 ? buffer.substring(
				0, 7) : buffer.toString());

		memberViewHolder.nikeName.setText(userInfo.getNikeName());

		if (userInfo.getAllowStatus() == AllowStatus.INVITED) {
			memberViewHolder.wait.setText(R.string.family_item_wait);
		} else {
			memberViewHolder.wait.setText("");
		}
	}
}
