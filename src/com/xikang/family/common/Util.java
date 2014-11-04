/* ==================================================
 * 产品名: 亲情快递
 * 文件名: Util.java
 * --------------------------------------------------
 * 开发环境: JDK1.6
 * --------------------------------------------------
 * 修订履历    2012/07/07  1.00  初版发行
 * --------------------------------------------------
 * (C) Copyright XIKANG CORPORATION 2012 All Rights Reserved.
 */

package com.xikang.family.common;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.webkit.URLUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.binary.Hex;

import com.xikang.channel.base.rpc.thrift.auth.LoginResult;
import com.xikang.channel.common.rpc.thrift.message.DigestAuthenticationReq;
import com.xikang.family.common.Constants;

/**
 * 
 * 工具类
 * 
 * 
 * 
 * @author 张荣
 * @version 1.00
 */

public class Util {

	public static String getPackageVersion(Context context) {
		String version = "";
		PackageManager pm = context.getPackageManager();
		try {
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			version = pi.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return version;
	}

	public static String getPhoneNum(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getLine1Number();
	}

	public static Date spellDate(String time) {
		SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat formatTime = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String nowDate = formatDate.format(new Date());
		try {
			return formatTime.parse(nowDate + " " + time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String[] makeStrDate(String str) {
		try {
			Date date = parseDateS(str);
			String[] arrayStr = new String[2];
			SimpleDateFormat f = new SimpleDateFormat("MM月dd日");
			arrayStr[0] = f.format(date);
			f = new SimpleDateFormat("HH:mm");
			arrayStr[1] = f.format(date);
			return arrayStr;
		} catch (Exception e) {
			return null;
		}

	}

	public static Date parseDateS(String dateString) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date str = null;
		try {
			str = format.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return str;
	}

	public static String getCurrentDate() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentDate = formatter.format(new Date(System
				.currentTimeMillis()));
		return currentDate;
	}

	public static Date addDate(Date date, long milliseconds) {
		long num = date.getTime() + milliseconds;
		return new Date(num);
	}

	public static Date parseDate(String dateString) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date str = null;
		try {
			str = format.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return str;
	}

	public static String getDate(int year, int month, int day) {
		Date date = null;
		String dateString = null;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			date = df.parse(year + "-" + month + "-" + day);
			dateString = df.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dateString;
	}

	public static String readTextFile(InputStream inputStream)
			throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		byte buf[] = new byte[1024];
		int len;
		while ((len = inputStream.read(buf)) != -1) {
			outputStream.write(buf, 0, len);
		}
		outputStream.close();
		inputStream.close();
		return outputStream.toString();
	}

	public static void istreamToFile(InputStream in, File file) {
		try {
			OutputStream os = new FileOutputStream(file);
			int bytesRead = 0;
			byte[] buffer = new byte[8192];
			while ((bytesRead = in.read(buffer, 0, 8192)) != -1) {
				os.write(buffer, 0, bytesRead);
			}
			os.close();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void downloadAPK(final String strPath, final Context context) {
		try {
			getDataSource(strPath, context);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void getDataSource(String strPath, Context context)
			throws Exception {
		if (!URLUtil.isNetworkUrl(strPath)) {
			throw new Exception();
		} else {
			URL myURL = new URL(strPath);
			URLConnection conn = myURL.openConnection();
			conn.connect();
			InputStream is = conn.getInputStream();
			if (is == null) {
				throw new RuntimeException("stream is null");
			}
			File myTempFile = File.createTempFile("XKFamily", ".apk");
			FileOutputStream fos = new FileOutputStream(myTempFile);
			byte buf[] = new byte[128];
			do {
				int numread = is.read(buf);
				if (numread <= 0) {
					break;
				}
				fos.write(buf, 0, numread);
			} while (true);

			openFile(myTempFile, context);
			try {
				is.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	private static void openFile(File f, Context context) {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);

		String type = getMIMEType(f);
		intent.setDataAndType(Uri.fromFile(f), type);
		context.startActivity(intent);
	}

	/**
	 * 删除文件
	 * 
	 * @param path
	 */
	public static void deleteFile(String path) {
		if (path != null) {
			File file = new File(path);
			file.delete();
		}
	}

	private static String getMIMEType(File f) {
		String type = "";
		String fName = f.getName();
		String end = fName
				.substring(fName.lastIndexOf(".") + 1, fName.length())
				.toLowerCase();

		if (end.equals("m4a") || end.equals("mp3") || end.equals("mid")
				|| end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
			type = "audio";
		} else if (end.equals("3gp") || end.equals("mp4")) {
			type = "video";
		} else if (end.equals("jpg") || end.equals("gif") || end.equals("png")
				|| end.equals("jpeg") || end.equals("bmp")) {
			type = "image";
		} else if (end.equals("apk")) {
			type = "application/vnd.android.package-archive";
		} else {
			type = "*";
		}
		if (end.equals("apk")) {
		} else {
			type += "/*";
		}
		return type;
	}

	/**
	 * bitmap生成本地图片
	 * 
	 * @param mBitmap
	 * @param path
	 * @param random
	 * @param compressRate
	 * @return
	 */
	public static String saveMyBitmap(Bitmap mBitmap, String path,
			boolean random, int compressRate) {
		File f = null;
		if (!random) {
			f = new File(path + "/temp.jpg");
			if (f.exists()) {
				f.delete();
			}
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			File folfer = new File(path + "/familyExpress/images");
			if (!folfer.exists()) {
				folfer.mkdirs();
			}
			try {
				f = File.createTempFile("family", ".jpg", folfer);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			FileOutputStream fOut = null;
			fOut = new FileOutputStream(f);
			mBitmap.compress(Bitmap.CompressFormat.JPEG, compressRate, fOut);
			fOut.flush();
			fOut.close();
			return f.getPath();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 生成可以上传的图片（避免图片过大）
	 * 
	 * @param path
	 * @return
	 */
	public static String formatPhoto(String path) {
		try {
			FileInputStream in = new FileInputStream(path);
			int size = in.available();
			int requireSize = 200000;
			if (requireSize > size) {
				return path;
			} else {
				BitmapFactory.Options opt = new BitmapFactory.Options();
				opt.inSampleSize = (int) size / requireSize;
				Bitmap bitmap = BitmapFactory.decodeFile(path, opt);
				String newPath = Util
						.saveMyBitmap(bitmap, "/sdcard/", true, 60);
				bitmap.recycle();
				return newPath;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return path;
	}

	/**
	 * 生成缩略图返回路径
	 * 
	 * @param file
	 * @return
	 */
	public static String getThumbnail(File file) {
		Bitmap oldBm = decodeByFile(file, 540, 840);
		// Bitmap oldBm = BitmapFactory.decodeFile(file.getPath());
		int width = oldBm.getWidth();
		int height = oldBm.getHeight();
		Bitmap thum = null;
		// 是否需要缩略
		if (width > 137 || height > 137) {
			int newWidth = 0;
			int newHeight = 0;
			if (width > height) {
				float scale = width / 137;
				newWidth = 137;
				newHeight = (int) (height / scale);
			} else {
				float scale = height / 137;
				newHeight = 137;
				newWidth = (int) (width / scale);
			}
			thum = ThumbnailUtils.extractThumbnail(oldBm, newWidth, newHeight);
			File thumFile = null;
			try {
				File dic = new File(Environment.getExternalStorageDirectory()
						.getAbsolutePath() + "/FamilyExpress/images/");
				if (!dic.exists()) {
					dic.mkdirs();
				}
				thumFile = File.createTempFile("family", ".jpg", dic);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			// File thumFile = new
			// File("/sdcard/FamilyExpress/images/thum.jpg");
			if (thumFile.exists()) {
				thumFile.delete();
			}
			try {
				thumFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}

			FileOutputStream out = null;
			try {
				out = new FileOutputStream(thumFile);
				thum.compress(Bitmap.CompressFormat.JPEG, 100, out);
				out.flush();
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (oldBm != null) {
				oldBm.recycle();
			}

			return thumFile.getPath();
		} else {
			if (oldBm != null) {
				oldBm.recycle();
			}
			return file.getPath();
		}
	}

	/**
	 * 传入毫秒数 转为对应时间的文本
	 * 
	 * @param time
	 * @return
	 */
	public static String parseMs2String(String time) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date(Long.parseLong(time));
		return format.format(date);
	}

	/**
	 * 传入时间文本 转为毫秒数
	 * 
	 * @param time
	 * @return
	 */
	public static String parseString2Ms(String time) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date date = format.parse(time);
			return String.valueOf(date.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获得图片的文件格式
	 * 
	 * @param path
	 * @return
	 */
	public static String getFormat(String path) {
		if (path.contains(Constants.JPG)) {
			return Constants.JPG_UP;
		}
		if (path.contains(Constants.GIF)) {
			return Constants.GIF_UP;
		}
		if (path.contains(Constants.PNG)) {
			return Constants.PNG_UP;
		}
		return Constants.JPG_UP;

	}

	public static String getDeviceId(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getDeviceId();
	}

	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager mgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] info = mgr.getAllNetworkInfo();
		if (info != null) {
			for (int i = 0; i < info.length; i++) {
				if (info[i].getState() == NetworkInfo.State.CONNECTED) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 获取SD卡剩余空间(M)
	 */
	public static long getSDCardAvailableSize() {
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
		if (sdCardExist) {
			File sdcard = Environment.getExternalStorageDirectory();
			StatFs statFs = new StatFs(sdcard.getPath());
			int blockSize = statFs.getBlockSize();
			int avaliableBlocks = statFs.getAvailableBlocks();
			return (long) blockSize * (long) avaliableBlocks / 1024 / 1024;
		} else {
			return 0;
		}
	}

	/**
	 * 检查推送客户端是否安装
	 */
	public static boolean isCPSClientInstalled(Context context) {
		try {
			context.getPackageManager().getApplicationInfo(
					"com.xikang.cpsclient",
					PackageManager.GET_UNINSTALLED_PACKAGES);
			return true;
		} catch (NameNotFoundException e) {
			return false;
		}
	}

	/**
	 * 检查应用是否在运行。
	 */
	public static boolean isActivityRunning(Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> list = am.getRunningTasks(100);
		System.out.println(list.size());
		boolean isRunning = false;
		for (RunningTaskInfo info : list) {
			if (info.topActivity.getPackageName().equals(
					"com.xikang.family.activity")
					&& info.baseActivity.getPackageName().equals(
							"com.xikang.family.activity")) {
				isRunning = true;
				break;
			}
		}
		return isRunning;
	}
	
    public static boolean isApplicationBroughtToBackground(final Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;

    }



	/**
	 * 根据登录信息，生成响应签名字符串。
	 */
	public static String calResSign(LoginResult loginResult, String account) {

		StringBuffer buf = new StringBuffer();
		buf.append(account);
		buf.append(":");
		buf.append(loginResult.getDigestAuthorizationRes().getClientId());
		buf.append(":");
		buf.append(Constants.CCS1);
		buf.append(":");
		buf.append(loginResult.getAuthUserInfo().getUserId());

		String result = new String(Hex.encodeHex(DigestUtils.sha256(buf
				.toString())));

		return result;
	}

	/**
	 * 根据已有的数据，生成访问令牌字符串。
	 */

	public static String calAccessToken(Context context,
			DigestAuthenticationReq authReq, String initialToken) {
		StringBuffer hs1Buf = new StringBuffer();
		hs1Buf.append(Constants.TERMTYPE);
		hs1Buf.append("|");
		hs1Buf.append(Util.getDeviceId(context));
		hs1Buf.append("|");
		hs1Buf.append(Constants.APPID);
		hs1Buf.append("|");
		hs1Buf.append(Util.getPackageVersion(context));
		hs1Buf.append("|");
		hs1Buf.append(Constants.USERID);
		hs1Buf.append("|");
		hs1Buf.append(authReq.getClientRandom());
		hs1Buf.append("|");
		hs1Buf.append(authReq.getClientId());
		String hs1 = new String(Hex.encodeHex(DigestUtils.sha256(hs1Buf
				.toString())));
		StringBuffer hs2Buf = new StringBuffer();
		hs2Buf.append(initialToken);
		hs2Buf.append("||");
		hs2Buf.append(Long.toString(authReq.getClientCount()));
		hs2Buf.append("||");
		hs2Buf.append(Constants.CCS2);
		String hs2 = new String(Hex.encodeHex(DigestUtils.sha256(hs2Buf
				.toString())));
		StringBuffer tokenBuf = new StringBuffer();
		tokenBuf.append(hs1);
		tokenBuf.append("DIGEST001");
		tokenBuf.append(hs2);
		String accessToken = new String(Hex.encodeHex(DigestUtils
				.sha256(tokenBuf.toString())));

		return accessToken;
	}

	public static long addCientCount(Context context) {
		SharedPreferences pref = context.getSharedPreferences(
				Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
		long cnt = pref.getLong(Constants.CLIENT_COUNT, 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putLong(Constants.CLIENT_COUNT, cnt + 1);
		editor.commit();
		return cnt + 1;
	}

	/**
	 * 应用初始化
	 */
	public static void init(Context context) {
		SharedPreferences pref = context.getSharedPreferences(
				Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);

		Constants.APPVERSION = getPackageVersion(context);
		Constants.DEVICEID = getDeviceId(context);
		Constants.DEVICEID = (Constants.DEVICEID == null ? "Unknown"
				: Constants.DEVICEID);
		Constants.ACCOUNTNAME = pref.getString(Constants.ACCOUNT_NAME, null);
		Constants.PASSWORD = pref.getString(Constants.PASS_WORD, null);
		Constants.LOGINTIME = pref.getString(Constants.LOGIN_TIME, null);
		Constants.AUTHTTL = pref.getString(Constants.AUTH_TTL, null);
		Constants.CLIENTCOUNT = pref.getLong(Constants.CLIENT_COUNT, 0);
	}

	public static String getRandomString(int len) {
		String returnStr = "";
		char[] ch = new char[len];
		Random rd = new Random();
		for (int i = 0; i < len; i++) {
			ch[i] = (char) (rd.nextInt(9) + 97);
		}
		returnStr = new String(ch);
		return returnStr;
	}

	public static String makeLogTag(Class cls) {
		return "CPSClient_" + cls.getSimpleName();
	}

	public static String getDeviceMD5PWD(Context context) {
		String deviceNo = getDeviceId(context);
		String plaintext = deviceNo.concat(Constants.KEY).concat(
				Constants.TERMTYPE);

		MessageDigest md5Digest = null;
		try {
			md5Digest = MessageDigest.getInstance("MD5");
			md5Digest.update(plaintext.getBytes("UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		byte[] pwdArray = md5Digest.digest();

		StringBuilder pwdBuilder = new StringBuilder(pwdArray.length * 2);
		for (byte aByte : pwdArray) {
			if (((int) aByte & 0xff) < 0x10) {
				pwdBuilder.append("0");
			}
			pwdBuilder.append(Integer.toString((int) aByte & 0xff, 16));

		}
		String password = pwdBuilder.toString();
		return password;
	}

	public static Date addDateTime(Date time, long milliseconds) {
		long temp = time.getTime();
		long num = temp + milliseconds;
		return new Date(num);
	}

	public static String makeDate(int year, int month, int day) {
		Date date = null;
		String dateString = null;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			date = df.parse(year + "-" + month + "-" + day);
			dateString = df.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dateString;
	}

	public static String parseDate2String(Date date) {
		SimpleDateFormat format = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss|SSS");
		return format.format(date);
	}

	public static String parseString2ms(String time) {
		SimpleDateFormat format = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss|SSS");
		try {
			Date date = format.parse(time);
			return String.valueOf(date.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static int computeSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {

		int initialSize = computeInitialSampleSize(options, minSideLength,

		maxNumOfPixels);

		int roundedSize;

		if (initialSize <= 8) {

			roundedSize = 1;

			while (roundedSize < initialSize) {

				roundedSize <<= 1;

			}

		} else {

			roundedSize = (initialSize + 7) / 8 * 8;

		}

		return roundedSize;

	}

	private static int computeInitialSampleSize(BitmapFactory.Options options,

	int minSideLength, int maxNumOfPixels) {

		double w = options.outWidth;

		double h = options.outHeight;

		int lowerBound = (maxNumOfPixels == -1) ? 1 :

		(int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));

		int upperBound = (minSideLength == -1) ? 128 :

		(int) Math.min(Math.floor(w / minSideLength),

		Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {

			// return the larger one when there is no overlapping zone.

			return lowerBound;

		}

		if ((maxNumOfPixels == -1) &&

		(minSideLength == -1)) {

			return 1;

		} else if (minSideLength == -1) {

			return lowerBound;

		} else {

			return upperBound;

		}

	}

	public static Bitmap decodeByFile(File file, int width, int height) {
		BitmapFactory.Options opts = new BitmapFactory.Options();

		opts.inJustDecodeBounds = true;

		BitmapFactory.decodeFile(file.getPath(), opts);

		opts.inSampleSize = computeSampleSize(opts, -1, width * height);

		opts.inJustDecodeBounds = false;
		Bitmap oldBm = BitmapFactory.decodeFile(file.getPath(), opts);

		return oldBm;
	}
}
