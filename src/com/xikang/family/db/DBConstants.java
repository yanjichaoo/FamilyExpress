/* ==================================================
 * 产品名: 亲情快递
 * 文件名: DBConstants.java
 * --------------------------------------------------
 * 开发环境: JDK1.6
 * --------------------------------------------------
 * 修订履历    2012/07/07  1.00  初版发行
 * --------------------------------------------------
 * (C) Copyright XIKANG CORPORATION 2012 All Rights Reserved.
 */

package com.xikang.family.db;

/**
 * 
 * DB常量
 * 
 * 
 * 
 * @author 张荣
 * @version 1.00
 */

public interface DBConstants {
	public static final String DB_NAME = "family.db";
	public static final int DB_VERSION = 1;
	public static final int PAGE_SIZE = 20;
	public static final String EXPRESS_CREATESQL = "create table express" 
			+ " (_id INTEGER PRIMARY KEY, "
			+ " eid TEXT, " 
			+ " ctype INTEGER, " 
			+ " etype INTEGER, "
			+ " econtent TEXT, " 
			+ " inout INTEGER, " 
			+ " fromuid TEXT, " 
			+ " touid TEXT, " 
			+ " otheruid TEXT, " 
			+ " etime INTEGER, "
			+ " ftype INTEGER, " 
			+ " length INTEGER, " 
			+ " termtype INTEGER, "
			+ " filename TEXT, " 
			+ " thumbnail TEXT, " 
			+ " estatus INTEGER,"
			+ " updatetime INTEGER," 
			+ " upstatus)";
}
