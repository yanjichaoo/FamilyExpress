/* ==================================================
 * 产品名: 亲情快递
 * 文件名: ExpressDBService.java
 * --------------------------------------------------
 * 开发环境: JDK1.6
 * --------------------------------------------------
 * 修订履历    2012/07/07  1.00  初版发行
 * --------------------------------------------------
 * (C) Copyright XIKANG CORPORATION 2012 All Rights Reserved.
 */

package com.xikang.family.service;

import java.util.ArrayList;
import java.util.List;

import com.xikang.channel.common.rpc.thrift.message.DeviceType;
import com.xikang.channel.familyexpress.rpc.thrift.express.ExpressInfo;
import com.xikang.channel.familyexpress.rpc.thrift.express.ExpressStatus;
import com.xikang.channel.familyexpress.rpc.thrift.express.ExpressType;
import com.xikang.channel.familyexpress.rpc.thrift.express.FEContentType;
import com.xikang.channel.familyexpress.rpc.thrift.express.FEFormatType;
import com.xikang.family.common.Constants;
import com.xikang.family.common.Util;
import com.xikang.family.db.DBConstants;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 
 * DB服务
 * 
 * 
 * 
 * @author 张荣
 * @version 1.00
 */

public class ExpressDBService extends CommonDBService {
	private SQLiteDatabase wdb;
	private SQLiteDatabase rdb;
	private final String insertSQL = "insert into express "
			+ " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	private final String deleteByDateSQL = "delete from  express where etype = 0 and etime < ?";
	private final String deleteBy_idSQL = "delete from  express where _id = ?";
	private final String deleteByUserIdSQL = "delete from  express where ((( fromuid= '"
			+ Constants.USERID
			+ "') and (touid=?)) or ((fromuid = ?) and ( touid='"
			+ Constants.USERID + "')))";
	// private final String selectByUserIdSQL =
	// "select eid,ctype,etype,econtent,inout,fromuid,touid,otheruid,etime,ftype,length,termtype,filename,thumbnail,estatus,updatetime,upstatus,_id from  express where ((( fromuid= '"
	// + Constants.USERID
	// + "') and (touid=?)) or ((fromuid = ?) and ( touid='"
	// + Constants.USERID + "')))";

	private final String updateEidBy_id = "update express set eid = ? where _id = ?";
	private final String updateStatusByEid = "update express set estatus = ? where eid = ? and otheruid<>'"
			+ Constants.USERID + "'";
	private final String updateStatusBy_id = "update express set estatus = ? where _id = ? and otheruid<>'"
			+ Constants.USERID + "'";

	private final String selectSQLBy_id = "select eid,ctype,etype,econtent,inout,fromuid,touid,otheruid,etime,ftype,length,termtype,filename,thumbnail,estatus,updatetime,upstatus,_id from express where _id = ?";

	private final String selectSQLByDate = "select eid,ctype,etype,econtent,inout,fromuid,touid,otheruid,etime,ftype,length,termtype,filename,thumbnail,estatus,updatetime,upstatus,_id from express where etype = 0 and etime > ?";
	private final String selectHistorySQL = "select eid,ctype,etype,econtent,inout,fromuid,touid,otheruid,etime,ftype,length,termtype,filename,thumbnail,estatus,updatetime,upstatus,_id from express where "
			+ "( otheruid||etime in (select otheruid||max(etime) from express group by otheruid) "
			+ "and ((fromuid = '"
			+ Constants.USERID
			+ "') or (touid = '"
			+ Constants.USERID
			+ "'))) "
			+ " and otheruid<>'"
			+ Constants.USERID + "'  order by etime desc";
	
	private final String selectHistorySQL2 = "select eid,ctype,etype,econtent,inout,fromuid,touid,otheruid,etime,ftype,length,termtype,filename,thumbnail,estatus,updatetime,upstatus,_id from express where "
			+ "( otheruid||_id in (select otheruid||max(_id) from express group by otheruid) "
			+ "and ((fromuid = '"
			+ Constants.USERID
			+ "') or (touid = '"
			+ Constants.USERID
			+ "'))) "
			+ " and otheruid<>'"
			+ Constants.USERID + "'  order by _id desc";

	private final String pageSQL = "select eid,ctype,etype,econtent,inout,fromuid,touid,otheruid,etime,ftype,length,termtype,filename,thumbnail,case estatus not null when 0 then 9 else estatus end,updatetime,upstatus,_id"
			+ " from express where "
			+ "( etime<? and ((( fromuid = '"
			+ Constants.USERID
			+ "') and (touid =?)) or ((fromuid = ?) and (touid = '"
			+ Constants.USERID
			+ "'))))"
			+ " and otheruid<>'"
			+ Constants.USERID
			+ "'  order by etime  desc limit "
			+ String.valueOf(DBConstants.PAGE_SIZE);
	
	private final String pageSQL2 = "select eid,ctype,etype,econtent,inout,fromuid,touid,otheruid,etime,ftype,length,termtype,filename,thumbnail,case estatus not null when 0 then 9 else estatus end,updatetime,upstatus,_id"
			+ " from express where "
			+ "( _id <? and ((( fromuid = '"
			+ Constants.USERID
			+ "') and (touid =?)) or ((fromuid = ?) and (touid = '"
			+ Constants.USERID
			+ "'))))"
			+ " and otheruid<>'"
			+ Constants.USERID
			+ "'  order by _id  desc limit "
			+ String.valueOf(DBConstants.PAGE_SIZE);

	private final String firstPageSQL = "select eid,ctype,etype,econtent,inout,fromuid,touid,otheruid,etime,ftype,length,termtype,filename,thumbnail,case estatus not null when 0 then 9 else estatus end,updatetime,upstatus,_id"
			+ " from express where ((( fromuid = '"
			+ Constants.USERID
			+ "') and ("
			+ "touid = ?)) or ((fromuid = ?) and (touid = '"
			+ Constants.USERID
			+ "')))"
			+ " and otheruid<>'"
			+ Constants.USERID
			+ "'  order by etime desc limit "
			+ String.valueOf(DBConstants.PAGE_SIZE);
	
	private final String firstPageSQL2 = "select eid,ctype,etype,econtent,inout,fromuid,touid,otheruid,etime,ftype,length,termtype,filename,thumbnail,case estatus not null when 0 then 9 else estatus end,updatetime,upstatus,_id"
			+ " from express where ((( fromuid = '"
			+ Constants.USERID
			+ "') and ("
			+ "touid = ?)) or ((fromuid = ?) and (touid = '"
			+ Constants.USERID
			+ "')))"
			+ " and otheruid<>'"
			+ Constants.USERID
			+ "'  order by _id desc limit "
			+ String.valueOf(DBConstants.PAGE_SIZE);

	private final String newSQL = "select eid,ctype,etype,econtent,inout,fromuid,touid,otheruid,etime,ftype,length,termtype,filename,thumbnail,case estatus not null when 0 then 9 else estatus end,updatetime,upstatus,_id"
			+ " from express where (etime > ? and ((( fromuid ='"
			+ Constants.USERID
			+ "') and (touid =?)) or ((fromuid= ?) and (touid ='"
			+ Constants.USERID
			+ "'))))"
			+ " and otheruid<>'"
			+ Constants.USERID + "'  order by etime";
	
	private final String newSQL2 = "select eid,ctype,etype,econtent,inout,fromuid,touid,otheruid,etime,ftype,length,termtype,filename,thumbnail,case estatus not null when 0 then 9 else estatus end,updatetime,upstatus,_id"
			+ " from express where ( _id > ? and ((( fromuid ='"
			+ Constants.USERID
			+ "') and (touid =?)) or ((fromuid= ?) and (touid ='"
			+ Constants.USERID
			+ "'))))"
			+ " and otheruid<>'"
			+ Constants.USERID + "'  order by _id";

	private final String readySQL = "select eid,ctype,etype,econtent,inout,fromuid,touid,otheruid,etime,ftype,length,termtype,filename,thumbnail,estatus,updatetime,upstatus,_id"
			+ " from express where "
			+ "estatus is null and  fromuid= '"
			+ Constants.USERID + "'  order by etime";
	
	private final String readySQL2 = "select eid,ctype,etype,econtent,inout,fromuid,touid,otheruid,etime,ftype,length,termtype,filename,thumbnail,estatus,updatetime,upstatus,_id"
			+ " from express where "
			+ "estatus is null and  fromuid= '"
			+ Constants.USERID + "'  order by _id";

	private final String countSQL = "select count(*) from express";

	private final String updateThumbnailSQL = "update express set thumbnail = ? where eid = ?  and otheruid<>'"
			+ Constants.USERID + "'";
	private final String updateFilenameSQL = "update express set filename = ? where eid = ?  and otheruid<>'"
			+ Constants.USERID + "'";

	public ExpressDBService(Context context) {
		super(context);
		wdb = this.getWritableDatabase();
		rdb = this.getReadableDatabase();
	}

	public long insert(LocalExpressInfo info) {
		wdb.execSQL(
				insertSQL,
				new Object[] {
						null,
						info.getExpressinfo().getExpressId(),
						Integer.valueOf(info.getExpressinfo().getContentType()
								.getValue()),
						Integer.valueOf(info.getExpressinfo().getExpressType()
								.getValue()),
						info.getExpressinfo().getTextContent(),
						info.getExpressinfo().getFrom()
								.equals(Constants.USERID) ? "1" : "0",
						info.getExpressinfo().getFrom(),
						info.getExpressinfo().getTo(),
						info.getExpressinfo().getFrom()
								.equals(Constants.USERID) ? info
								.getExpressinfo().getTo() : info
								.getExpressinfo().getFrom(),
						String.valueOf(Util.parseDateS(
								info.getExpressinfo().getTime()).getTime()),
						Integer.valueOf(info.getExpressinfo().getFormat()
								.getValue()),
						Integer.valueOf(info.getExpressinfo().getLength()),
						info.getExpressinfo().getFromTerminalType() == null ? null
								: Integer.valueOf(info.getExpressinfo()
										.getFromTerminalType().getValue()),
						info.getFilename(),
						info.getThumbnail(),
						info.getExpressinfo().getExpressStatus() != null ? info
								.getExpressinfo().getExpressStatus().getValue()
								: null,
						Long.valueOf(info.getExpressinfo().getUpdateTime()),
						Integer.valueOf(0) });
		Cursor cursor = rdb.rawQuery("select last_insert_rowid()", null);
		cursor.moveToNext();
		return cursor.getLong(0);
	}

	public void deleteBy_id(String _id) {
		wdb.execSQL(deleteBy_idSQL, new String[] { _id });
	}

	public void deleteByDate(String date) {
		wdb.execSQL(deleteByDateSQL, new String[] { date });
	}

	public void deleteByUserId(String otherUid) {
		wdb.execSQL(deleteByUserIdSQL, new String[] { otherUid, otherUid });
	}

	public void updateEidBy_id(String eid, String _id) {
		wdb.execSQL(updateEidBy_id, new String[] { eid, _id });
	}

	public void updateStatusByEid(String eid, String eStatus) {
		wdb.execSQL(updateStatusByEid, new String[] { eStatus, eid });
	}
	
	public void updateStatusBy_id(String _id, String eStatus) {
		wdb.execSQL(updateStatusBy_id, new String[] { eStatus, _id });
	}

	public LocalExpressInfo findByEid(String _id) {
		LocalExpressInfo info = null;
		ExpressInfo einfo = null;
		Cursor cursor = rdb.rawQuery(selectSQLBy_id, new String[] { _id });
		if (cursor.moveToNext()) {
			info = new LocalExpressInfo();
			einfo = new ExpressInfo();

			einfo.setExpressId(cursor.getString(0));
			einfo.setContentType(FEContentType.findByValue(cursor.getInt(1)));
			einfo.setExpressType(ExpressType.findByValue(cursor.getInt(2)));
			einfo.setTextContent(cursor.getString(3));
			einfo.setFrom(cursor.getString(5));
			einfo.setTo(cursor.getString(6));
			einfo.setTime(String.valueOf(cursor.getLong(8)));
			einfo.setFormat(FEFormatType.findByValue(cursor.getInt(9)));
			einfo.setLength(cursor.getShort(10));
			einfo.setFromTerminalType(DeviceType.findByValue(cursor.getInt(11)));
			einfo.setExpressStatus(ExpressStatus.findByValue(cursor.getInt(14)));
			einfo.setUpdateTime(cursor.getLong(15));
			info.setExpressinfo(einfo);
			info.setOtheruid(cursor.getString(7));
			info.setFilename(cursor.getString(12));
			info.setThumbnail(cursor.getString(13));
			info.setUpstatus(String.valueOf(cursor.getInt(16)));
			info.set_id(String.valueOf(cursor.getInt(17)));
		}
		cursor.close();
		return info;
	}

	public List<LocalExpressInfo> findHistory() {
		List<LocalExpressInfo> infos = new ArrayList<LocalExpressInfo>();
		Cursor cursor = rdb.rawQuery(selectHistorySQL, null);
		while (cursor.moveToNext()) {
			LocalExpressInfo info = new LocalExpressInfo();
			ExpressInfo einfo = new ExpressInfo();

			einfo.setExpressId(String.valueOf(cursor.getInt(0)));
			einfo.setContentType(FEContentType.findByValue(cursor.getInt(1)));
			einfo.setExpressType(ExpressType.findByValue(cursor.getInt(2)));
			einfo.setTextContent(cursor.getString(3));
			einfo.setFrom(cursor.getString(5));
			einfo.setTo(cursor.getString(6));
			einfo.setTime(String.valueOf(cursor.getLong(8)));
			einfo.setFormat(FEFormatType.findByValue(cursor.getInt(9)));
			einfo.setLength(cursor.getShort(10));
			einfo.setFromTerminalType(DeviceType.findByValue(cursor.getInt(11)));
			einfo.setExpressStatus(ExpressStatus.findByValue(cursor.getInt(14)));
			einfo.setUpdateTime(cursor.getLong(15));

			info.setExpressinfo(einfo);
			info.setOtheruid(cursor.getString(7));
			info.setFilename(cursor.getString(12));
			info.setThumbnail(cursor.getString(13));
			info.setUpstatus(String.valueOf(cursor.getInt(16)));
			info.set_id(String.valueOf(cursor.getInt(17)));
			infos.add(info);
		}
		cursor.close();
		return infos;
	}
	
	public List<LocalExpressInfo> findHistory2() {
		List<LocalExpressInfo> infos = new ArrayList<LocalExpressInfo>();
		Cursor cursor = rdb.rawQuery(selectHistorySQL2, null);
		while (cursor.moveToNext()) {
			LocalExpressInfo info = new LocalExpressInfo();
			ExpressInfo einfo = new ExpressInfo();

			einfo.setExpressId(String.valueOf(cursor.getInt(0)));
			einfo.setContentType(FEContentType.findByValue(cursor.getInt(1)));
			einfo.setExpressType(ExpressType.findByValue(cursor.getInt(2)));
			einfo.setTextContent(cursor.getString(3));
			einfo.setFrom(cursor.getString(5));
			einfo.setTo(cursor.getString(6));
			einfo.setTime(String.valueOf(cursor.getLong(8)));
			einfo.setFormat(FEFormatType.findByValue(cursor.getInt(9)));
			einfo.setLength(cursor.getShort(10));
			einfo.setFromTerminalType(DeviceType.findByValue(cursor.getInt(11)));
			einfo.setExpressStatus(ExpressStatus.findByValue(cursor.getInt(14)));
			einfo.setUpdateTime(cursor.getLong(15));

			info.setExpressinfo(einfo);
			info.setOtheruid(cursor.getString(7));
			info.setFilename(cursor.getString(12));
			info.setThumbnail(cursor.getString(13));
			info.setUpstatus(String.valueOf(cursor.getInt(16)));
			info.set_id(String.valueOf(cursor.getInt(17)));
			infos.add(info);
		}
		cursor.close();
		return infos;
	}

	public List<LocalExpressInfo> findByDate(String date) {
		List<LocalExpressInfo> infos = new ArrayList<LocalExpressInfo>();
		Cursor cursor = rdb.rawQuery(selectSQLByDate, new String[] { date });
		while (cursor.moveToNext()) {
			LocalExpressInfo info = new LocalExpressInfo();
			ExpressInfo einfo = new ExpressInfo();

			einfo.setExpressId(String.valueOf(cursor.getInt(0)));
			einfo.setContentType(FEContentType.findByValue(cursor.getInt(1)));
			einfo.setExpressType(ExpressType.findByValue(cursor.getInt(2)));
			einfo.setTextContent(cursor.getString(3));
			einfo.setFrom(cursor.getString(5));
			einfo.setTo(cursor.getString(6));
			einfo.setTime(String.valueOf(cursor.getLong(8)));
			einfo.setFormat(FEFormatType.findByValue(cursor.getInt(9)));
			einfo.setLength(cursor.getShort(10));
			einfo.setFromTerminalType(DeviceType.findByValue(cursor.getInt(11)));
			einfo.setExpressStatus(ExpressStatus.findByValue(cursor.getInt(14)));
			einfo.setUpdateTime(cursor.getLong(15));

			info.setExpressinfo(einfo);
			info.setOtheruid(cursor.getString(7));
			info.setFilename(cursor.getString(12));
			info.setThumbnail(cursor.getString(13));
			info.setUpstatus(String.valueOf(cursor.getInt(16)));
			info.set_id(String.valueOf(cursor.getInt(17)));
			infos.add(info);
		}
		cursor.close();
		return infos;
	}

	public List<LocalExpressInfo> getNextPage(String time, String otherUid) {
		List<LocalExpressInfo> infos = new ArrayList<LocalExpressInfo>();
		Cursor cursor = null;
		if (time != null) {
			cursor = rdb.rawQuery(pageSQL, new String[] { time, otherUid,otherUid });
		} else {
			cursor = rdb.rawQuery(firstPageSQL, new String[] { otherUid,otherUid });
		}
		while (cursor.moveToNext()) {
			LocalExpressInfo info = new LocalExpressInfo();
			ExpressInfo einfo = new ExpressInfo();

			einfo.setExpressId(String.valueOf(cursor.getInt(0)));
			einfo.setContentType(FEContentType.findByValue(cursor.getInt(1)));
			einfo.setExpressType(ExpressType.findByValue(cursor.getInt(2)));
			einfo.setTextContent(cursor.getString(3));
			einfo.setFrom(cursor.getString(5));
			einfo.setTo(cursor.getString(6));
			einfo.setTime(String.valueOf(cursor.getLong(8)));
			einfo.setFormat(FEFormatType.findByValue(cursor.getInt(9)));
			einfo.setLength(cursor.getShort(10));
			einfo.setFromTerminalType(DeviceType.findByValue(cursor.getInt(11)));
			einfo.setExpressStatus(ExpressStatus.findByValue(cursor.getInt(14)));
			einfo.setUpdateTime(cursor.getLong(15));

			info.setExpressinfo(einfo);
			info.setOtheruid(cursor.getString(7));
			info.setFilename(cursor.getString(12));
			info.setThumbnail(cursor.getString(13));
			info.setUpstatus(String.valueOf(cursor.getInt(16)));
			info.set_id(String.valueOf(cursor.getInt(17)));
			infos.add(info);
		}
		cursor.close();
		return infos;
	}
	
	public List<LocalExpressInfo> getNextPage2(String id, String otherUid) {
		List<LocalExpressInfo> infos = new ArrayList<LocalExpressInfo>();
		Cursor cursor = null;
		if (id != null) {
			cursor = rdb.rawQuery(pageSQL2, new String[] { id, otherUid,otherUid });
		} else {
			cursor = rdb.rawQuery(firstPageSQL2, new String[] { otherUid,otherUid });
		}
		while (cursor.moveToNext()) {
			LocalExpressInfo info = new LocalExpressInfo();
			ExpressInfo einfo = new ExpressInfo();

			einfo.setExpressId(String.valueOf(cursor.getInt(0)));
			einfo.setContentType(FEContentType.findByValue(cursor.getInt(1)));
			einfo.setExpressType(ExpressType.findByValue(cursor.getInt(2)));
			einfo.setTextContent(cursor.getString(3));
			einfo.setFrom(cursor.getString(5));
			einfo.setTo(cursor.getString(6));
			einfo.setTime(String.valueOf(cursor.getLong(8)));
			einfo.setFormat(FEFormatType.findByValue(cursor.getInt(9)));
			einfo.setLength(cursor.getShort(10));
			einfo.setFromTerminalType(DeviceType.findByValue(cursor.getInt(11)));
			einfo.setExpressStatus(ExpressStatus.findByValue(cursor.getInt(14)));
			einfo.setUpdateTime(cursor.getLong(15));

			info.setExpressinfo(einfo);
			info.setOtheruid(cursor.getString(7));
			info.setFilename(cursor.getString(12));
			info.setThumbnail(cursor.getString(13));
			info.setUpstatus(String.valueOf(cursor.getInt(16)));
			info.set_id(String.valueOf(cursor.getInt(17)));
			infos.add(info);
		}
		cursor.close();
		return infos;
	}

	public List<LocalExpressInfo> getNewInfos(String time, String otherUid) {
		List<LocalExpressInfo> infos = new ArrayList<LocalExpressInfo>();
		Cursor cursor = rdb.rawQuery(newSQL, new String[] { time, otherUid,
				otherUid });
		while (cursor.moveToNext()) {
			LocalExpressInfo info = new LocalExpressInfo();
			ExpressInfo einfo = new ExpressInfo();

			einfo.setExpressId(String.valueOf(cursor.getInt(0)));
			einfo.setContentType(FEContentType.findByValue(cursor.getInt(1)));
			einfo.setExpressType(ExpressType.findByValue(cursor.getInt(2)));
			einfo.setTextContent(cursor.getString(3));
			einfo.setFrom(cursor.getString(5));
			einfo.setTo(cursor.getString(6));
			einfo.setTime(String.valueOf(cursor.getLong(8)));
			einfo.setFormat(FEFormatType.findByValue(cursor.getInt(9)));
			einfo.setLength(cursor.getShort(10));
			einfo.setFromTerminalType(DeviceType.findByValue(cursor.getInt(11)));
			einfo.setExpressStatus(ExpressStatus.findByValue(cursor.getInt(14)));
			einfo.setUpdateTime(cursor.getLong(15));

			info.setExpressinfo(einfo);
			info.setOtheruid(cursor.getString(7));
			info.setFilename(cursor.getString(12));
			info.setThumbnail(cursor.getString(13));
			info.setUpstatus(String.valueOf(cursor.getInt(16)));
			info.set_id(String.valueOf(cursor.getInt(17)));
			infos.add(info);
		}

		cursor.close();
		return infos;
	}
	
	public List<LocalExpressInfo> getNewInfos2(String id, String otherUid) {
		List<LocalExpressInfo> infos = new ArrayList<LocalExpressInfo>();
		Cursor cursor = rdb.rawQuery(newSQL2, new String[] { id, otherUid,otherUid });
		while (cursor.moveToNext()) {
			LocalExpressInfo info = new LocalExpressInfo();
			ExpressInfo einfo = new ExpressInfo();

			einfo.setExpressId(String.valueOf(cursor.getInt(0)));
			einfo.setContentType(FEContentType.findByValue(cursor.getInt(1)));
			einfo.setExpressType(ExpressType.findByValue(cursor.getInt(2)));
			einfo.setTextContent(cursor.getString(3));
			einfo.setFrom(cursor.getString(5));
			einfo.setTo(cursor.getString(6));
			einfo.setTime(String.valueOf(cursor.getLong(8)));
			einfo.setFormat(FEFormatType.findByValue(cursor.getInt(9)));
			einfo.setLength(cursor.getShort(10));
			einfo.setFromTerminalType(DeviceType.findByValue(cursor.getInt(11)));
			einfo.setExpressStatus(ExpressStatus.findByValue(cursor.getInt(14)));
			einfo.setUpdateTime(cursor.getLong(15));

			info.setExpressinfo(einfo);
			info.setOtheruid(cursor.getString(7));
			info.setFilename(cursor.getString(12));
			info.setThumbnail(cursor.getString(13));
			info.setUpstatus(String.valueOf(cursor.getInt(16)));
			info.set_id(String.valueOf(cursor.getInt(17)));
			infos.add(info);
		}

		cursor.close();
		return infos;
	}

	public List<LocalExpressInfo> getReadyInfos() {
		List<LocalExpressInfo> infos = new ArrayList<LocalExpressInfo>();
		Cursor cursor = rdb.rawQuery(readySQL, null);
		while (cursor.moveToNext()) {
			LocalExpressInfo info = new LocalExpressInfo();
			ExpressInfo einfo = new ExpressInfo();

			einfo.setExpressId(String.valueOf(cursor.getInt(0)));
			einfo.setContentType(FEContentType.findByValue(cursor.getInt(1)));
			einfo.setExpressType(ExpressType.findByValue(cursor.getInt(2)));
			einfo.setTextContent(cursor.getString(3));
			einfo.setFrom(cursor.getString(5));
			einfo.setTo(cursor.getString(6));
			einfo.setTime(String.valueOf(cursor.getLong(8)));
			einfo.setFormat(FEFormatType.findByValue(cursor.getInt(9)));
			einfo.setLength(cursor.getShort(10));
			einfo.setFromTerminalType(DeviceType.findByValue(cursor.getInt(11)));
			einfo.setExpressStatus(ExpressStatus.findByValue(cursor.getInt(14)));
			einfo.setUpdateTime(cursor.getLong(15));

			info.setExpressinfo(einfo);
			info.setOtheruid(cursor.getString(7));
			info.setFilename(cursor.getString(12));
			info.setThumbnail(cursor.getString(13));
			info.setUpstatus(String.valueOf(cursor.getInt(16)));
			info.set_id(String.valueOf(cursor.getInt(17)));
			infos.add(info);
		}

		cursor.close();
		return infos;
	}
	
	public List<LocalExpressInfo> getReadyInfos2() {
		List<LocalExpressInfo> infos = new ArrayList<LocalExpressInfo>();
		Cursor cursor = rdb.rawQuery(readySQL2, null);
		while (cursor.moveToNext()) {
			LocalExpressInfo info = new LocalExpressInfo();
			ExpressInfo einfo = new ExpressInfo();

			einfo.setExpressId(String.valueOf(cursor.getInt(0)));
			einfo.setContentType(FEContentType.findByValue(cursor.getInt(1)));
			einfo.setExpressType(ExpressType.findByValue(cursor.getInt(2)));
			einfo.setTextContent(cursor.getString(3));
			einfo.setFrom(cursor.getString(5));
			einfo.setTo(cursor.getString(6));
			einfo.setTime(String.valueOf(cursor.getLong(8)));
			einfo.setFormat(FEFormatType.findByValue(cursor.getInt(9)));
			einfo.setLength(cursor.getShort(10));
			einfo.setFromTerminalType(DeviceType.findByValue(cursor.getInt(11)));
			einfo.setExpressStatus(ExpressStatus.findByValue(cursor.getInt(14)));
			einfo.setUpdateTime(cursor.getLong(15));

			info.setExpressinfo(einfo);
			info.setOtheruid(cursor.getString(7));
			info.setFilename(cursor.getString(12));
			info.setThumbnail(cursor.getString(13));
			info.setUpstatus(String.valueOf(cursor.getInt(16)));
			info.set_id(String.valueOf(cursor.getInt(17)));
			infos.add(info);
		}

		cursor.close();
		return infos;
	}

	public long getCount() {
		long cnt = 0;
		Cursor cursor = rdb.rawQuery(countSQL, null);
		if (cursor.moveToFirst()) {
			cnt = cursor.getLong(0);
		}
		cursor.close();
		return cnt;
	}

	public void updateThumbNailByEid(String eid, String thum) {
		wdb.execSQL(updateThumbnailSQL, new String[] { thum, eid });
	}

	public void updateFilenameByEid(String eid, String filename) {
		wdb.execSQL(updateFilenameSQL, new String[] { filename, eid });
	}
}
