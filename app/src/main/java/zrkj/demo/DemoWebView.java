package zrkj.demo;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.PluginState;

import com.finddreams.baselib.R;
import com.finddreams.baselib.http.MyHttpClient;
import com.finddreams.baselib.utils.AppManager;
import com.loopj.android.http.AsyncHttpClient;

public class DemoWebView extends FragmentActivity {
	private WebView mWebview;
	private WebSettings mWebSettings;
	protected String url = "https://pro.modao.cc/app/i9o8CAI0Dty4qwlhOQqd2TGROaqCt6Q#screen=sC6C9F2343D1528127357289";
	protected Context context;
	protected AsyncHttpClient httpClient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);// 不随屏幕旋转
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		AppManager.getAppManager().addActivity(this);
		context = this;
		httpClient = MyHttpClient.getHttpClient();
		setContentView(R.layout.activity_web);
		mWebview = (WebView) findViewById(R.id.webView);
		mWebSettings = mWebview.getSettings();
		setWeb();
		mWebview.loadUrl(url);

		// 设置不用系统浏览器打开,直接显示在当前Webview
		mWebview.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});

		// // 设置WebChromeClient类
		// mWebview.setWebChromeClient(new WebChromeClient() {
		//
		// // 获取网站标题
		// @Override
		// public void onReceivedTitle(WebView view, String title) {
		// System.out.println("标题在这里");
		// topTitle.mTvTitle.setText(title);
		// }
		//
		// // 获取加载进度
		// @Override
		// public void onProgressChanged(WebView view, int newProgress) {
		// pg.setProgress(newProgress);// 设置进度值
		// }
		// });

		// // 设置WebViewClient类
		// mWebview.setWebViewClient(new WebViewClient() {
		// // 设置加载前的函数
		// @Override
		// public void onPageStarted(WebView view, String url, Bitmap favicon) {
		// pg.setVisibility(View.VISIBLE);// 开始加载网页时显示进度条
		// }
		//
		// // 设置结束加载函数
		// @Override
		// public void onPageFinished(WebView view, String url) {
		// pg.setVisibility(View.GONE);// 加载完网页进度条消失
		// }
		// });
	}

	private void setWeb() {
		mWebSettings.setJavaScriptEnabled(true);
		// 设置允许JS弹窗
		mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);
		// 支持插件
		mWebSettings.setPluginState(PluginState.ON);

		// 设置自适应屏幕，两者合用
		mWebSettings.setUseWideViewPort(true); // 将图片调整到适合webview的大小
		mWebSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

		// 缩放操作
		// mWebSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
		// mWebSettings.setBuiltInZoomControls(true);
		// //设置内置的缩放控件。若为false，则该WebView不可缩放
		mWebSettings.setSupportZoom(false); // 支持缩放，默认为true。是下面那个的前提。
		mWebSettings.setBuiltInZoomControls(false); // 设置内置的缩放控件。若为false，则该WebView不可缩放
		mWebSettings.setDisplayZoomControls(false); // 隐藏原生的缩放控件

		// 其他细节操作 //清除缓存
		// 关闭webview中缓存
		// mWebSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //
		// mWebSettings.setAllowFileAccess(true); // 设置可以访问文件
		mWebSettings.setLoadsImagesAutomatically(true); // 支持自动加载图片
		mWebSettings.setDefaultTextEncodingName("utf-8");// 设置编码格式

	}

	// 点击返回上一页面而不是退出浏览器
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && mWebview.canGoBack()) {
			mWebview.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	// 销毁Webview
	@Override
	protected void onDestroy() {
		if (mWebview != null) {
			mWebview.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
			// 清除网页访问留下的缓存
			// 由于内核缓存是全局的因此这个方法不仅仅针对webview而是针对整个应用程序.
			mWebview.clearCache(true);

			// 清除当前webview访问的历史记录
			// 只会webview访问历史记录里的所有记录除了当前访问记录
			mWebview.clearHistory();

			// 这个api仅仅清除自动完成填充的表单数据，并不会清除WebView存储到本地的数据
			mWebview.clearFormData();
			mWebview.setWebChromeClient(null);
			mWebview.setWebViewClient(null);
			mWebview.getSettings().setJavaScriptEnabled(false);
			((ViewGroup) mWebview.getParent()).removeView(mWebview);
			mWebview.destroy();
			mWebview = null;

			// 清空所有Cookie
			CookieSyncManager.createInstance(context); // Create a singleton CookieSyncManager within a context
			CookieManager cookieManager = CookieManager.getInstance(); // the singleton CookieManager instance
			cookieManager.removeAllCookie();// Removes all cookies.
			CookieSyncManager.getInstance().sync(); // forces sync manager to   sync now
		}
		super.onDestroy();
	}

}
