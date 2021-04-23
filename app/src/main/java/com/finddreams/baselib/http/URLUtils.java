package com.finddreams.baselib.http;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import com.lidroid.xutils.util.LogUtils;

/**
 * @Description:url的帮助类
 * @author blin
 * @since 2015年12月14日
 */
public class URLUtils {
//	public final static String IP = "192.168.3.8";//本地
//		public final static String HOST = IP + ":8090";//本地
	public final static String IP = "192.168.91.231";//服务器  端口9090
	public final static String HOST = IP + ":9090";//服务器
	public static final String PATH = "/";
	public static final String SCHEME = "http";
	public static final String defaultEncoding = "UTF-8";

	public static String getUrl(String baseurl) {
		if (baseurl != null) {
			String url = SCHEME + "://" + HOST + "/" +PATH+ baseurl;
			return url;
		}
		return null;
	}

	public static String getUrls() {
			String url = SCHEME + "://" + HOST + "/" +PATH;
			return url;
	}

	/**
	 * 创建Url
	 * @param resource
	 * @return
	 */
	public static String createURL(String resource) {
		try {
			String url = URIUtils.createURI(SCHEME, HOST, -1, PATH + resource,
					"", null).toString();
			LogUtils.d(url);
			return url;
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String createURL(String resource, Map<String, String> params) {
		return createURL(resource, params, defaultEncoding);
	}

	public static String createURL(String resource, Map<String, String> params,
			String encoding) {
		List<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
		for (Iterator<Map.Entry<String, String>> it = params.entrySet()
				.iterator(); it.hasNext();) {
			Map.Entry<String, String> entry = (Map.Entry<String, String>) it
					.next();
			parameters.add(new BasicNameValuePair(entry.getKey(), entry
					.getValue()));
		}

		try {
			String url = URIUtils.createURI(SCHEME, HOST, -1, PATH + resource,
					URLEncodedUtils.format(parameters, encoding), null)
					.toString();

			LogUtils.d(url);
			return url;
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}
}
