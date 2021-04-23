package zrkj.demo;

import java.io.IOException;
import java.net.URI;

import org.apache.http.Header;
import org.apache.http.HttpResponse;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.finddreams.baselib.R;
import com.finddreams.baselib.activity.CaptureActivity;
import com.finddreams.baselib.base.BaseActivity;
import com.finddreams.baselib.http.MyHttpUtils;
import com.finddreams.baselib.http.NetStateUtil;
import com.finddreams.baselib.http.URLUtils;
import com.finddreams.baselib.service.MQTTService;
import com.finddreams.baselib.utils.ActivityUtil;
import com.finddreams.baselib.utils.InputMethodUtil;
import com.finddreams.baselib.utils.SpinnerUtil;
import com.finddreams.baselib.utils.ToastManager;
import com.finddreams.baselib.view.ClearEditText;
import com.finddreams.baselib.view.CustomProgressDialog;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.ResponseHandlerInterface;

import zrkj.project.util.Result;

@ContentView(R.layout.demo)
public class Demo extends BaseActivity {
	@ViewInject(R.id.userName)
	private ClearEditText userName;
	@ViewInject(R.id.password)
	private EditText password;
	@ViewInject(R.id.select)
	private Spinner select;
	@ViewInject(R.id.radio)
	private RadioGroup group;
	@ViewInject(R.id.cb01)
	private CheckBox cb01;
	@ViewInject(R.id.cb02)
	private CheckBox cb02;
	@ViewInject(R.id.cb03)
	private CheckBox cb03;
	@ViewInject(R.id.btn01)
	private Button btn01;
	@Override
	protected void requestCallBack(Result ajax, int posi) {

	}
	@Override
	protected void initData() {
	}

	@Override
	protected void initView() {
		// ***** 开启注解 快速开发
		ViewUtils.inject(this);
		topTitle.mTvTitle.setText("我是主页");
		topTitle.mTvTitle.setText("");
		topTitle.mTvBack.setText("");
		// 初始化下拉菜单
		initSelect(new String[] { "0", "1", "2", "3" });
		// 下拉菜单默认选中
		SpinnerUtil.setSpinnerItemSelectedByValue(select, "3");
		InputMethodUtil.hiddenInputMethod(this, userName);
		InputMethodUtil.hiddenInputMethod(this, password);
		InputMethodUtil.hideBottomSoftInputMethod(this);

		group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				// 获取变更后的选中项的ID
				int radioButtonId = arg0.getCheckedRadioButtonId();
				// 根据ID获取RadioButton的实例
				RadioButton rb = (RadioButton) Demo.this
						.findViewById(radioButtonId);
				// 更新文本内容，以符合选中项
				showShortText("您的性别是：" + rb.getText());
			}
		});

		initcheckbox();
	}

	private void initcheckbox() {

		/**
		 * 为Button绑定监听
		 */
		btn01.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				String text = "您选择了：";
				if (cb01.isChecked()) {
					text += cb01.getText().toString();
				}
				if (cb02.isChecked()) {
					text += cb02.getText().toString();
				}
				if (cb03.isChecked()) {
					text += cb03.getText().toString();
				}
				Toast.makeText(Demo.this, text, Toast.LENGTH_SHORT).show();
			}
		});

	}

	private void initSelect(String[] strs) {
		ArrayAdapter adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, strs);

		// 设置下拉列表的风格
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// 将adapter 添加到spinner中
		select.setAdapter(adapter);
		// 添加事件Spinner事件监听
		select.setOnItemSelectedListener(new OnItemSelectedListener() {
			int count = 0;

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				showShortText(select.getSelectedItem().toString());
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
	}

	@OnClick({ R.id.tz })
	private <T> void tz(View v) {
		// showShortText("开始登陆..." + userName.getText().toString()
		// + password.getText().toString());
		// ***** UI 自定义转圈圈
		// ***** 定义一个弹窗
		// tipDialog();
		// ***** 访问网络(同步异步) *****GSON
		// visitWeb();
		// *****页面跳转(传递参数)
		tz();// 不等待结果
		// tzCapture();//拍照识别二维码 (等待结果的跳转)
		// tzWeb();//跳转到指定网页
	}

	@OnClick({ R.id.tzCapture })
	private <T> void tzCapture(View v) {
		tzCapture();// 拍照识别二维码 (等待结果的跳转)
	}

	@OnClick({ R.id.tzPulltorefreshview })
	private <T> void tzPulltorefreshview(View v) {
		ActivityUtil.goToActivity(context, DemoPulltorefreshview.class);
	}

	@OnClick({ R.id.fragment_parent })
	private <T> void fragment_parent(View v) {
		ActivityUtil.goToActivity(context, DemoFragmentParnet.class);
	}

	@OnClick({ R.id.tzPictureUpload })
	private <T> void tzPictureUpload(View v) {
		ActivityUtil.goToActivity(context, DemoPictureUpload.class);
	}

	@OnClick({ R.id.tzWeb })
	private <T> void tzWeb(View v) {
		tzWeb();// 跳转到指定网页
	}

	@OnClick({ R.id.tzDemoScroll_Fragment })
	private <T> void tzDemoScroll_Fragment(View v) {
		ActivityUtil.goToActivity(context, DemoScroll_Fragment_list.class);
	}

	@OnClick({ R.id.DemoBlueBootPrint })
	private <T> void DemoBlueBootPrint(View v) {
		ActivityUtil.goToActivity(context, DemoBlueToothPrint.class);
	}

	// ======================================================================================
	private void tzWeb() {
		Bundle bundle = new Bundle();
		// bundle.putString("url", "http://www.baidu.com");
		bundle.putString(
				"url",
				"https://pro.modao.cc/app/i9o8CAI0Dty4qwlhOQqd2TGROaqCt6Q#screen=sC6C9F2343D1528127357289");
		ActivityUtil.goToActivity(context, DemoWebPrint.class, bundle);
	}

	private void tz() {
		Bundle bundle = new Bundle();
		bundle.putString("key", "value");
		ActivityUtil.goToActivity(context, Demo2.class, bundle);
	}

	private void tzCapture() {
		int nextcode = 0;
		Intent intent = new Intent();
		intent.setClass(this, CaptureActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivityForResult(intent, nextcode);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 0:
			if (resultCode == RESULT_OK) {
				Bundle bundle = data.getBundleExtra("bundle");
				showShortText("二维码的结果是:" + bundle.getString("result"));
			}
			break;
		}
	}

	public void tipDialog() {
		Dialog dialog = new AlertDialog.Builder(this).setTitle("我是提示!")
				.setIcon(android.R.drawable.ic_dialog_info)
				// .setView(view);
				.setPositiveButton("确认", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// TODO
						dialog.cancel();// 取消弹出框
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();// 取消弹出框
					}
				}).create();
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
	}

	private void visitWeb() {
		// *****GSON
		// System.out.println(new Gson().fromJson(new Gson().toJson(new User()),
		// User.class));
		// 异步请求
		Log.v("cjj", "我跳过同步请求了吗0");
		// 建议使用这种方式访问
		RequestParams rp = new RequestParams();
		rp.addBodyParameter("userName", userName.getText().toString());
		rp.addBodyParameter("password", password.getText().toString());
		cpd.setTvMsgAndShow("正在登陆...");
		new MyHttpUtils(context).httpPost(URLUtils.getUrl("User_test02"), rp,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub
						Log.v("cjj", "2异步请求onFailure");
						Log.v("cjj", "arg:" + arg1);
						cpd.dismiss();
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						// TODO Auto-generated method stub
						Log.v("cjj", "2异步请求onSuccess");
						Log.v("cjj", "arg:" + arg0.result.toString());
						cpd.dismiss();
					}
				});

		// 异步请求
		Log.v("cjj", "我跳过同步请求了吗1");
		com.loopj.android.http.RequestParams rp2 = new com.loopj.android.http.RequestParams();
		rp2.put("userName", userName.getText().toString());
		rp2.put("password", password.getText().toString());

		if (0 == NetStateUtil.isNetworkAvailable(context)) {
			ToastManager.showShortText(context,
					context.getString(R.string.no_net));
		} else {
			cpd.setTvMsgAndShow("正在登陆...");
			httpClient.post(URLUtils.getUrl("User_test01"), rp2,
					new AsyncHttpResponseHandler() {

						@Override
						public void onFailure(int arg0, Header[] arg1,
								byte[] arg2, Throwable arg3) {
							Log.v("cjj", "1异步请求onFailure");
							if (arg2 != null)// 一定要加这个判断
								Log.v("cjj", "arg:" + new String(arg2));
							cpd.dismiss();
						}

						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2) {
							Log.v("cjj", "1异步请求onSuccess");
							if (arg2 != null)// 一定要加这个判断
								Log.v("cjj", "arg:" + new String(arg2));
							cpd.dismiss();
						}
					});
		}
		Log.v("cjj", "我跳过同步请求了吗2");
		// 异步请求 //用于控制进度条
		com.loopj.android.http.RequestParams rp3 = new com.loopj.android.http.RequestParams();
		rp3.put("userName", userName.getText().toString());
		rp3.put("password", password.getText().toString());
		httpClient.post(context, URLUtils.getUrl("User_test03"), rp3,
				new ResponseHandlerInterface() {

					@Override
					public void setUseSynchronousMode(boolean arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void setRequestURI(URI arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void setRequestHeaders(Header[] arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void sendSuccessMessage(int arg0, Header[] arg1,
							byte[] arg2) {
						// TODO Auto-generated method stub

					}

					@Override
					public void sendStartMessage() {
						// TODO Auto-generated method stub

					}

					@Override
					public void sendRetryMessage(int arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void sendResponseMessage(HttpResponse arg0)
							throws IOException {
						// TODO Auto-generated method stub

					}

					@Override
					public void sendProgressMessage(int arg0, int arg1) {
						// TODO Auto-generated method stub

					}

					@Override
					public void sendFinishMessage() {
						// TODO Auto-generated method stub

					}

					@Override
					public void sendFailureMessage(int arg0, Header[] arg1,
							byte[] arg2, Throwable arg3) {
						// TODO Auto-generated method stub

					}

					@Override
					public void sendCancelMessage() {
						// TODO Auto-generated method stub

					}

					@Override
					public void onPreProcessResponse(
							ResponseHandlerInterface arg0, HttpResponse arg1) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onPostProcessResponse(
							ResponseHandlerInterface arg0, HttpResponse arg1) {
						// TODO Auto-generated method stub

					}

					@Override
					public boolean getUseSynchronousMode() {
						// TODO Auto-generated method stub
						return false;
					}

					@Override
					public URI getRequestURI() {
						// TODO Auto-generated method stub
						return null;
					}

					@Override
					public Header[] getRequestHeaders() {
						// TODO Auto-generated method stub
						return null;
					}
				});
		Log.v("cjj", "我跳过同步请求了吗4");
		// 同步请求 HttpUtil
	}

	@Override
	protected void onScan(String barcodeStr) {
		// TODO Auto-generated method stub

	}

}
