/* ==================================================
 * 产品名: 亲情快递
 * 文件名: HttpClientUtil.java
 * --------------------------------------------------
 * 开发环境: JDK1.6
 * --------------------------------------------------
 * 修订履历    2012/07/07  1.00  初版发行
 * --------------------------------------------------
 * (C) Copyright XIKANG CORPORATION 2012 All Rights Reserved.
 */
package com.xikang.family.common;

import java.io.File;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.os.Environment;

import com.xikang.channel.familyexpress.rpc.thrift.express.FEFormatType;
import com.xikang.family.common.CustomSSL;

/**
 * 
 * HttpClient工具类
 * 
 * 
 * 
 * @author 张荣
 * @version 1.00
 */

public class HttpClientUtil {

	public static DefaultHttpClient getHttpClient() throws Exception {
		DefaultHttpClient httpClient = null;
		KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
		trustStore.load(null, null);
		SSLSocketFactory sf = new CustomSSL(trustStore);
		sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

		HttpParams params = new BasicHttpParams();
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
		HttpConnectionParams.setConnectionTimeout(params, 0);
		HttpConnectionParams.setSoTimeout(params, 60 * 1000);
		HttpConnectionParams.setSocketBufferSize(params, 8192);

		SchemeRegistry registry = new SchemeRegistry();

		registry.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));

		registry.register(new Scheme("https", sf, 443));
		ClientConnectionManager ccm = new ThreadSafeClientConnManager(params,
				registry);

		httpClient = new DefaultHttpClient(ccm, params);
		httpClient.getParams().setParameter(ClientPNames.COOKIE_POLICY,
				CookiePolicy.BROWSER_COMPATIBILITY);

		return httpClient;

	}

	public static String requestPost(String linkAddress, Map<String, String> map)
			throws Exception {
		String result = null;

		DefaultHttpClient httpClient = getHttpClient();

		HttpPost httpost = new HttpPost(linkAddress);
		if (map != null) {
			List<BasicNameValuePair> postData = new ArrayList<BasicNameValuePair>();
			for (Map.Entry<String, String> entry : map.entrySet()) {
				postData.add(new BasicNameValuePair(entry.getKey(), entry
						.getValue()));
			}
			httpost.setEntity(new UrlEncodedFormEntity(postData, HTTP.UTF_8));
		}

		HttpResponse response = httpClient.execute(httpost); // 执行
		String httpStatus = response.getStatusLine().toString();

		if (httpStatus.indexOf("4") > 0 || httpStatus.indexOf("5") > 0) {
			result = "error"; // 如果响应码为4开头,则代表访问不成功
		}
		HttpEntity httpEntity = response.getEntity();
		if (httpEntity != null) {
			String backStream = EntityUtils.toString(httpEntity, HTTP.UTF_8); // 回写内容
			result = backStream.toString();

			httpEntity.consumeContent();
		}
		return result;
	}

	/**
	 * 下载图片
	 * 
	 * @param linkAddress
	 * @param map
	 * @param ftype
	 * @return
	 * @throws Exception
	 */
	public static String requestPostStream(String linkAddress, String userid,
			Map<String, String> map, FEFormatType ftype) throws Exception {

		File temp = null;
		DefaultHttpClient httpClient = getHttpClient();
		HttpPost httpost = new HttpPost(linkAddress);
		if (map != null) {
			List<BasicNameValuePair> postData = new ArrayList<BasicNameValuePair>();
			for (Map.Entry<String, String> entry : map.entrySet()) {
				postData.add(new BasicNameValuePair(entry.getKey(), entry
						.getValue()));
			}
			httpost.setEntity(new UrlEncodedFormEntity(postData, HTTP.UTF_8));
		}

		HttpResponse response = httpClient.execute(httpost); // 执行
		String httpStatus = response.getStatusLine().toString();

		if (httpStatus.indexOf("4") > 0 || httpStatus.indexOf("5") > 0) {
			return null; // 如果响应码为4开头,则代表访问不成功
		}
		HttpEntity httpEntity = response.getEntity();
		if (httpEntity != null) {
			if (ftype == ftype.PNG) {
				File file = new File(Environment.getExternalStorageDirectory()
						.getAbsolutePath() + "/FamilyExpress/images/");
				if (!file.exists()) {
					file.mkdirs();
				}
				temp = new File(file, userid.concat(".png"));
			} else if (ftype == ftype.JPG) {
				File file = new File(Environment.getExternalStorageDirectory()
						.getAbsolutePath() + "/FamilyExpress/images/");
				if (!file.exists()) {
					file.mkdirs();
				}
				temp = new File(file, userid.concat(".jpg"));
			}
			Util.istreamToFile(httpEntity.getContent(), temp);

			httpEntity.consumeContent();
		}
		return temp.getPath();
	}
	
	/**
	 * 下载音频
	 * 
	 * @param linkAddress
	 * @param map
	 * @param ftype
	 * @return
	 * @throws Exception
	 */
	public static String requestPostStream(String linkAddress,
			Map<String, String> map, FEFormatType ftype) throws Exception {

		File temp = null;
		DefaultHttpClient httpClient = getHttpClient();
		HttpPost httpost = new HttpPost(linkAddress);
		if (map != null) {
			List<BasicNameValuePair> postData = new ArrayList<BasicNameValuePair>();
			for (Map.Entry<String, String> entry : map.entrySet()) {
				postData.add(new BasicNameValuePair(entry.getKey(), entry
						.getValue()));
			}
			httpost.setEntity(new UrlEncodedFormEntity(postData, HTTP.UTF_8));
		}

		HttpResponse response = httpClient.execute(httpost); // 执行
		String httpStatus = response.getStatusLine().toString();

		if (httpStatus.indexOf("4") > 0 || httpStatus.indexOf("5") > 0) {
			return null; // 如果响应码为4开头,则代表访问不成功
		}
		HttpEntity httpEntity = response.getEntity();
		if (httpEntity != null) {
			if (ftype == ftype.SPX) {
				File file = new File(Environment.getExternalStorageDirectory()
						.getAbsolutePath() + "/FamilyExpress/audios/");
				if (!file.exists()) {
					file.mkdirs();
				}
				temp = File.createTempFile("family", ".spx", file);
			} else if (ftype == ftype.PNG) {
				File file = new File(Environment.getExternalStorageDirectory()
						.getAbsolutePath() + "/FamilyExpress/images/");
				if (!file.exists()) {
					file.mkdirs();
				}
				temp = File.createTempFile("family", ".png", file);
			} else if (ftype == ftype.JPG) {
				File file = new File(Environment.getExternalStorageDirectory()
						.getAbsolutePath() + "/FamilyExpress/images/");
				if (!file.exists()) {
					file.mkdirs();
				}
				temp = File.createTempFile("family", ".jpg", file);
			}
			Util.istreamToFile(httpEntity.getContent(), temp);

			httpEntity.consumeContent();
		}
		return temp.getPath();
	}
}
