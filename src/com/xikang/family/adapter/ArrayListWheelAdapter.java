/* ==================================================
 * 产品名: 亲情快递
 * 文件名: ArrayListWheelAdapter.java
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

import com.xikang.channel.familyexpress.rpc.thrift.family.MemberRelation;

public class ArrayListWheelAdapter extends AbstractWheelTextAdapter {

	/** The default items length */
	public static final int DEFAULT_LENGTH = -1;

	// items
	private List<MemberRelation> items;

	public ArrayListWheelAdapter(Context context, List<MemberRelation> items) {
		super(context);
		this.items = items;
	}

	@Override
	public CharSequence getItemText(int index) {
		if (index >= 0 && index < items.size()) {
			return items.get(index).getTitle();
		}
		return null;
	}

	@Override
	public int getItemsCount() {
		return items.size();
	}

}
