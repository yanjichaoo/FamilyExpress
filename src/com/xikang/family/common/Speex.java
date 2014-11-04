/* ==================================================
 * 产品名: 亲情快递
 * 文件名: Speex.java
 * --------------------------------------------------
 * 开发环境: JDK1.6
 * --------------------------------------------------
 * 修订履历    2012/07/07  1.00  初版发行
 * --------------------------------------------------
 * (C) Copyright XIKANG CORPORATION 2012 All Rights Reserved.
 */

package com.xikang.family.common;

/**
 * 
 * JNI
 * 
 * 
 * 
 * @author 张荣
 * @version 1.00
 */

public class Speex {
	static {
		try {
			System.loadLibrary("speex");
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public native int speexenc(String infile, String outfile);

	public native int speexdec(String infile, String outfile);

}
