package com.finddreams.baselib.base;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.PluginState;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.finddreams.baselib.R;
import com.finddreams.baselib.base.webViewUtil.AndroidtoJs;

import zrkj.project.util.Result;

/**
 * WebView
 * 
 * @author blin
 * @since 2015年12月14日
 */
// http://blog.csdn.net/carson_ho/article/details/64904691
// http://blog.csdn.net/carson_ho/article/details/52693322 教程
@SuppressWarnings("deprecation")
public class BaseWebActivity extends BaseActivity {
	private WebView mWebview;
	private WebSettings mWebSettings;
	private ProgressBar pg;
	protected String url;

	@Override
	protected void initView() {
		setContentView(R.layout.activity_baseweb);
		mWebview = (WebView) findViewById(R.id.webView);
		pg = (ProgressBar) findViewById(R.id.progressBar);
		// CJJ 打印
		// mWebview.setDrawingCacheEnabled(true); //设置缓存 如果不需要截屏 则不需要缓存
		/*
		 * if (this.getIntent().getExtras().getInt("height") != 0 &&
		 * this.getIntent().getExtras().getInt("width") != 0)
		 * mWebview.setLayoutParams(new RelativeLayout.LayoutParams(this
		 * .getIntent().getExtras().getInt("width"), this.getIntent()
		 * .getExtras().getInt("height"))); topTitle.mTvRight.setText("截屏");
		 * topTitle.mTvRight.setOnClickListener(onClickListener);
		 */
	}

	// // 截屏操作
	// public OnClickListener onClickListener = new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// switch (v.getId()) {
	// case R.id.top_right_tv:
	// Bitmap bitmap = mWebview.getDrawingCache();
	// // 图片路径
	// String sdStatus = Environment.getExternalStorageState();
	// /* 检测sd是否可用 */
	// if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
	// showCustomToast("SD卡不可用");
	// return;
	// }
	// Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
	// // 文件夹路径
	// String pathUrl = Environment.getExternalStorageDirectory()
	// .getPath() + "//myimage/";
	//
	// File appDir = new File(pathUrl);
	// if (!appDir.exists()) {
	// appDir.mkdir();
	// }
	// String fileName = "print.jpg";
	// File file = new File(appDir, fileName);
	// try {
	// FileOutputStream fos = new FileOutputStream(file);
	// bitmap.compress(CompressFormat.JPEG, 100, fos);
	// fos.flush();
	// fos.close();
	// } catch (FileNotFoundException e) {
	// e.printStackTrace();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// break;
	// }
	//
	// }
	// };
	@Override
	protected void requestCallBack(Result ajax, int posi) {

	}
	@Override
	protected void initData() {
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

		// 设置WebChromeClient类
		mWebview.setWebChromeClient(new WebChromeClient() {

			// 获取网站标题
			@Override
			public void onReceivedTitle(WebView view, String title) {
				System.out.println("标题在这里");
				topTitle.mTvTitle.setText(title);
			}

			// 获取加载进度
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				pg.setProgress(newProgress);// 设置进度值
			}
		});

		// 设置WebViewClient类
		mWebview.setWebViewClient(new WebViewClient() {
			// 设置加载前的函数
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				pg.setVisibility(View.VISIBLE);// 开始加载网页时显示进度条
			}

			// 设置结束加载函数
			@Override
			public void onPageFinished(WebView view, String url) {
				pg.setVisibility(View.GONE);// 加载完网页进度条消失
			}
		});
	}

	private void setWeb() {
		// // 如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
		// SetAndroidtoJs1();// 建立映射
		// SetAndroidtoJs2();
		// SetAndroidtoJs3();
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
//		mWebSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); // 关闭webview中缓存
//		mWebSettings.setAllowFileAccess(true); // 设置可以访问文件
//		mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true); // 支持通过JS打开新窗口
//		mWebSettings.setLoadsImagesAutomatically(true); // 支持自动加载图片
//		mWebSettings.setDefaultTextEncodingName("utf-8");// 设置编码格式

		// 打印
		// mWebSettings.setUseWideViewPort(false); // 将图片调整到适合webview的大小
		// mWebSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
		// mWebSettings.setVerticalScrollBarEnabled(false);
		// mWebSettings.setHorizontalScrollBarEnabled(false);
		// mWebSettings.setScrollContainer(false);
	}

//	private void SetAndroidtoJs1() {
//		mWebSettings.setJavaScriptEnabled(true);
//		// 通过addJavascriptInterface()将Java对象映射到JS对象
//		// 参数1：Javascript对象名
//		// 参数2：Java对象名
//		mWebview.addJavascriptInterface(new AndroidtoJs(), "test");// AndroidtoJS类对象映射到js的test对象
//	}
//
//	private void SetAndroidtoJs2() {
//		// 设置与Js交互的权限
//		mWebSettings.setJavaScriptEnabled(true);
//		// 设置允许JS弹窗
//		mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);
//		mWebview.setWebViewClient(new WebViewClient() {
//			@Override
//			public boolean shouldOverrideUrlLoading(WebView view, String url) {
//
//				// 步骤2：根据协议的参数，判断是否是所需要的url
//				// 一般根据scheme（协议格式） & authority（协议名）判断（前两个参数）
//				// 假定传入进来的 url = "js://webview?arg1=111&arg2=222"（同时也是约定好的需要拦截的）
//
//				Uri uri = Uri.parse(url);
//				// 如果url的协议 = 预先约定的 js 协议
//				// 就解析往下解析参数
//				if (uri.getScheme().equals("js")) {
//					// 如果 authority = 预先约定协议里的 webview，即代表都符合约定的协议
//					// 所以拦截url,下面JS开始调用Android需要的方法
//					if (uri.getAuthority().equals("webview")) {
//
//						// 步骤3：
//						// 执行JS所需要调用的逻辑
//						System.out.println("js调用了Android的方法");
//						// 可以在协议上带有参数并传递到Android上
//						HashMap<String, String> params = new HashMap<>();
//						Set<String> collection = uri.getQueryParameterNames();
//						Log.v("cjj", collection.toString());
//					}
//
//					return true;
//				}
//				return super.shouldOverrideUrlLoading(view, url);
//			}
//		});
//	}
//
//	private void SetAndroidtoJs3() {
//		// 设置与Js交互的权限
//		mWebSettings.setJavaScriptEnabled(true);
//		// 设置允许JS弹窗
//		mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);
//
//		// mWebview.setWebChromeClient(new WebChromeClient() {
//		// // 拦截输入框(原理同方式2)
//		// // 参数message:代表promt（）的内容（不是url）
//		// // 参数result:代表输入框的返回值
//		// @Override
//		// public boolean onJsPrompt(WebView view, String url, String message,
//		// String defaultValue, JsPromptResult result) {
//		// // 根据协议的参数，判断是否是所需要的url(原理同方式2)
//		// // 一般根据scheme（协议格式） & authority（协议名）判断（前两个参数）
//		// // 假定传入进来的 url = "js://webview?arg1=111&arg2=222"（同时也是约定好的需要拦截的）
//		//
//		// Uri uri = Uri.parse(message);
//		// // 如果url的协议 = 预先约定的 js 协议
//		// // 就解析往下解析参数
//		// if (uri.getScheme().equals("js")) {
//		//
//		// // 如果 authority = 预先约定协议里的 webview，即代表都符合约定的协议
//		// // 所以拦截url,下面JS开始调用Android需要的方法
//		// if (uri.getAuthority().equals("webview")) {
//		//
//		// //
//		// // 执行JS所需要调用的逻辑
//		// System.out.println("js调用了Android的方法");
//		// // 可以在协议上带有参数并传递到Android上
//		// HashMap<String, String> params = new HashMap<>();
//		// Set<String> collection = uri.getQueryParameterNames();
//		//
//		// // 参数result:代表消息框的返回值(输入值)
//		// result.confirm("js调用了Android的方法成功啦");
//		// }
//		// return true;
//		// }
//		// return super.onJsPrompt(view, url, message, defaultValue,
//		// result);
//		// }
//		//
//		// // 通过alert()和confirm()拦截的原理相同，此处不作过多讲述
//		//
//		// // 拦截JS的警告框
//		// @Override
//		// public boolean onJsAlert(WebView view, String url, String message,
//		// JsResult result) {
//		// return super.onJsAlert(view, url, message, result);
//		// }
//		//
//		// // 拦截JS的确认框
//		// @Override
//		// public boolean onJsConfirm(WebView view, String url,
//		// String message, JsResult result) {
//		// return super.onJsConfirm(view, url, message, result);
//		// }
//		// });
//
//	}

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
			CookieSyncManager.createInstance(context); // Create a singleton
														// CookieSyncManager
														// within a context
			CookieManager cookieManager = CookieManager.getInstance(); // the
																		// singleton
																		// CookieManager
																		// instance
			cookieManager.removeAllCookie();// Removes all cookies.
			CookieSyncManager.getInstance().sync(); // forces sync manager to
													// sync now
		}
		super.onDestroy();
	}

	@Override
	protected void onScan(String barcodeStr) {
		// TODO Auto-generated method stub

	}
}
