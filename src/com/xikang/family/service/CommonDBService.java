/* ==================================================
 * 产品名: 亲情快递
 * 文件名: CommonDBService.java
 * --------------------------------------------------
 * 开发环境: JDK1.6
 * --------------------------------------------------
 * 修订履历    2012/07/07  1.00  初版发行
 * --------------------------------------------------
 * (C) Copyright XIKANG CORPORATION 2012 All Rights Reserved.
 */

package com.xikang.family.service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.xikang.family.db.DBHelper;

/**
 * 
 * DB通用接口
 * 
 * 
 * 
 * @author 张荣
 * @version 1.00
 */

public class CommonDBService {
	private DBHelper dbhelper;

	public CommonDBService(Context context) {
		dbhelper = DBHelper.getInstanse(context);
	}

	public SQLiteDatabase getReadableDatabase() {
		return dbhelper.getReadableDatabase();
	}

	public SQLiteDatabase getWritableDatabase() {
		return dbhelper.getWritableDatabase();
	}

	public void closeDatabase() {
		dbhelper.close();
	}

}
