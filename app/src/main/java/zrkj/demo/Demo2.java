package zrkj.demo;

import java.io.IOException;
import java.net.URI;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.finddreams.baselib.R;
import com.finddreams.baselib.base.BaseActivity;
import com.finddreams.baselib.http.MyHttpUtils;
import com.finddreams.baselib.http.NetStateUtil;
import com.finddreams.baselib.http.URLUtils;
import com.finddreams.baselib.utils.ActivityUtil;
import com.finddreams.baselib.utils.ToastManager;
import com.finddreams.baselib.view.CustomProgressDialog;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.EventBase;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.ResponseHandlerInterface;

import zrkj.project.util.Result;

@ContentView(R.layout.demo)
public class Demo2 extends BaseActivity {
	@Override
	protected void initView() {
		// ***** 开启注解 快速开发
		setContentView(R.layout.demo);
		ViewUtils.inject(this);
		topTitle.mTvTitle.setText("我是被跳转过来的");
		showLongText("跳转过来了...参数是:" +this.getIntent().getExtras().get("key") );
	}
	@Override
	protected void requestCallBack(Result ajax, int posi) {

	}

	@Override
	protected void initData() {
		
	}
	@ViewInject(R.id.userName)
	private EditText userName;
	@ViewInject(R.id.password)
	private EditText password;
	@Override
	protected void onScan(String barcodeStr) {
		// TODO Auto-generated method stub
		
	}

}

// ***** GsonUtil
