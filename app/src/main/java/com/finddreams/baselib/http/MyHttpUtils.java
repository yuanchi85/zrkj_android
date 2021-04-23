package com.finddreams.baselib.http;

		import android.content.Context;

		import com.finddreams.baselib.R;
		import com.finddreams.baselib.utils.StringUtil;
		import com.finddreams.baselib.utils.ToastManager;
		import com.lidroid.xutils.HttpUtils;
		import com.lidroid.xutils.http.RequestParams;
		import com.lidroid.xutils.http.callback.RequestCallBack;
		import com.lidroid.xutils.http.client.HttpRequest;
		import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
		import com.lidroid.xutils.util.LogUtils;
		import com.rscja.utility.StringUtility;

		import org.apache.http.HttpEntity;
		import org.apache.http.entity.StringEntity;

		import java.io.IOException;

		import zrkj.project.util.Constant;
		import zrkj.project.util.Result;

/**
 * 自定义的HttpUtils
 *
 * @author blin
 * @since 2015年12月14日
 */
public class MyHttpUtils {

	private static final int CONNTIMEOUT = 1000 * 15;
	private static final int CACHE_TIME = 1000 * 20;
	private static HttpUtils http;
	private Context context;

	public MyHttpUtils(Context context) {
		this.context = context;
		http = new HttpUtils(CONNTIMEOUT);
	}

	public void setCache() {
		http.configCurrentHttpCacheExpiry(CACHE_TIME);
	}

	public void httpGet(String url, RequestCallBack<String> callback) {
		http(HttpMethod.GET, url, null, callback);
	}

	public void httpPost(String url, RequestParams params,
						 RequestCallBack<String> callback) {
		http(HttpMethod.POST, url, params, callback);
	}

	public void httpPostByJson(String url,String json, RequestCallBack<String> callback) {
		try{
			RequestParams params = new RequestParams();
			params.setBodyEntity(new StringEntity(json,"UTF-8"));
			params.setContentType("application/json");
			params.setHeader("X-Access-Token", Constant.TOKENOUP);
			http(HttpMethod.POST, url, params, callback);
		}catch (Exception e){

		}
	}

	public void httpGetByJson(String url,String json, RequestCallBack<String> callback) {
		try{
			RequestParams params = new RequestParams();
			params.setBodyEntity(new StringEntity(json,"UTF-8"));
			params.setContentType("application/json");
			params.setHeader("X-Access-Token", Constant.TOKENOUP);
			http(HttpMethod.GET, url, params, callback);
		}catch (Exception e){

		}
	}

	protected void http(HttpRequest.HttpMethod method, String url,
						RequestParams params, RequestCallBack<String> callback) {
		http.configCurrentHttpCacheExpiry(CACHE_TIME);
		LogUtils.allowD = false;
		if (params != null) {
			//params.setHeader("Content-type", "application/json;charset=UTF-8");
			//params.setContentType("application/json;charset=UTF-8");
			if (params.getQueryStringParams() != null)
				LogUtils.d(url + params.getQueryStringParams().toString());
			HttpEntity entity = params.getEntity();
			if (entity != null) {
				try {
					LogUtils.d(url + StringUtil.convertStreamToString(entity.getContent()));
				} catch (IOException e) {

					e.printStackTrace();
				}
			}
		} else {
			params = new RequestParams();
			//params.setHeader("Content-type", "application/json");
		}

		if (0 == NetStateUtil.isNetworkAvailable(context)) {
			showCustomToast(context.getString(R.string.no_net));
			http.send(method, url, params, callback);
		} else {
			http.send(method, url, params, callback);
		}
	}

	protected void showCustomToast(String str) {
		ToastManager.showShortText(context, str);
	}
}
