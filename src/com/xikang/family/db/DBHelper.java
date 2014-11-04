/* ==================================================
 * 产品名: 亲情快递
 * 文件名: DBHelper.java
 * --------------------------------------------------
 * 开发环境: JDK1.6
 * --------------------------------------------------
 * 修订履历    2012/07/07  1.00  初版发行
 * --------------------------------------------------
 * (C) Copyright XIKANG CORPORATION 2012 All Rights Reserved.
 */

package com.xikang.family.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 
 * DB工具类
 * 
 * 
 * 
 * @author 张荣
 * @version 1.00
 */

public class DBHelper extends SQLiteOpenHelper {

	private static DBHelper dbhelper = null;

	private DBHelper(Context context) {
		super(context, DBConstants.DB_NAME, null, DBConstants.DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DBConstants.EXPRESS_CREATESQL);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Need backup DB
		db.execSQL("DROP TABLE IF EXISTS express");
		onCreate(db);
	}

	public synchronized static DBHelper getInstanse(Context context) {
		if (dbhelper == null) {
			dbhelper = new DBHelper(context);
		}
		return dbhelper;
	}
}