/* ==================================================
 * 产品名: 亲情快递
 * 文件名: LocalExpressInfo.java
 * --------------------------------------------------
 * 开发环境: JDK1.6
 * --------------------------------------------------
 * 修订履历    2012/07/07  1.00  初版发行
 * --------------------------------------------------
 * (C) Copyright XIKANG CORPORATION 2012 All Rights Reserved.
 */

package com.xikang.family.service;

import com.xikang.channel.familyexpress.rpc.thrift.express.ExpressInfo;

/**
 * 
 * 本地保存快递对象
 * 
 * 
 * 
 * @author 张荣
 * @version 1.00
 */

public class LocalExpressInfo {

	private ExpressInfo expressinfo;
	private String otheruid;
	private String filename;
	private String thumbnail;
	private String upstatus;
	private String _id;

	public ExpressInfo getExpressinfo() {
		return expressinfo;
	}

	public void setExpressinfo(ExpressInfo expressinfo) {
		this.expressinfo = expressinfo;
	}

	public String getOtheruid() {
		return otheruid;
	}

	public void setOtheruid(String otheruid) {
		this.otheruid = otheruid;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getUpstatus() {
		return upstatus;
	}

	public void setUpstatus(String upstatus) {
		this.upstatus = upstatus;
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

}
