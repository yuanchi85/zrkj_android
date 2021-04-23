package zrkj.demo;

import HPRTAndroidSDKA300.HPRTPrinterHelper;
import HPRTAndroidSDKA300.PublicFunction;
import zrkj.project.util.Result;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.finddreams.baselib.R;
import com.finddreams.baselib.base.BaseActivity;
import com.finddreams.baselib.utils.ActivityUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

@ContentView(R.layout.demobluetooth)
public class DemoBlueToothPrint extends BaseActivity {
	private static HPRTPrinterHelper HPRTPrinter = null;
	// 打印图片提示框
	private ProgressDialog dialog;
	private Handler handler;
	// 基础设置信息
	// public static String paper = "0";
	// private PublicFunction PFun = null;

	// ui信息初始化
	@ViewInject(R.id.txtTips)
	private TextView txtTips;
	@Override
	protected void requestCallBack(Result ajax, int posi) {

	}
	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		ViewUtils.inject(this);
		topTitle.mTvTitle.setText("A300蓝牙打印机");
		topTitle.mTvTitle.setText("");
		topTitle.mTvBack.setText("");
		HPRTPrinter = new HPRTPrinterHelper(this,
				HPRTPrinterHelper.PRINT_NAME_A300);// 打印机类型
		if (HPRTPrinter.IsOpened()) {
			try {
				txtTips.setText("已连接蓝牙:" + HPRTPrinter.getPrintName());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			txtTips.setText("请连接打印机！");
		}
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				if (msg.what > 0) {
					// showCustomToast("打印完成");
					dialog.cancel();
				} else if (msg.what == -1) {
					showCustomToast("宽度或者高度超出打印机的范围");
					dialog.cancel();
				} else {
					showCustomToast("打印失败");
					dialog.cancel();
				}
			}
		};
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
	}

	@OnClick({ R.id.checkbluethooth })
	private <T> void checkbluethooth(View v) {
		if (Build.VERSION.SDK_INT >= 23) {
			// 校验是否已具有模糊定位权限
			Intent serverIntent = new Intent(this.context, DemoDeviceList.class);
			startActivityForResult(serverIntent,
					HPRTPrinterHelper.ACTIVITY_CONNECT_BT);
			return;
		} else {
			// 系统不高于6.0直接执行
			Intent serverIntent = new Intent(this.context, DemoDeviceList.class);
			startActivityForResult(serverIntent,
					HPRTPrinterHelper.ACTIVITY_CONNECT_BT);
		}
	}

	@OnClick({ R.id.print })
	private <T> void print(View v) {
		if (HPRTPrinter.IsOpened()) {
			dialog = new ProgressDialog(this);
			dialog.setMessage("Printing.....");
			dialog.setProgress(100);
			dialog.show();
			showCustomToast("0");
			new Thread() {
				public void run() {
					int a = 0;
					try {
						// 图片路径
						String sdStatus = Environment.getExternalStorageState();
						/* 检测sd是否可用 */
						if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
							showCustomToast("SD卡不可用");
							return;
						}
						Intent intent = new Intent(
								"android.media.action.IMAGE_CAPTURE");
						// 文件夹路径
						String pathUrl = Environment
								.getExternalStorageDirectory().getPath()
								+ "//myimage/print.jpg";
						// 0： 打印机准备就绪。1： 打印机打印中。2： 打印机缺纸。6： 打印机开盖。其他： 出错。
						// HPRTPrinterHelper.getstatus();//获取打印机状态
						/*
						 * 参数含义: 1.所有字段将水平偏移指定的单位数量 2.水平方向dpi 3.垂直方向dpi 4.标签高度
						 * 5.打印次数
						 */
						HPRTPrinterHelper.printAreaSize("0", "200", "200",
								"500", "1");
						// Pw： 指定页面宽度
						// HPRTPrinterHelper.PageWidth("100");
						/*
						 * 参数含义: 1.图片起始X坐标 2.图片起始y坐标 3.图片路径
						 */
						HPRTPrinterHelper.Expanded("0", "0", pathUrl);
						// 打印浓度， 0 默认 1 正常 2黑暗 3 非常深
						// HPRTPrinterHelper.Contrast("1");//打印浓度
						// 速度类型， 总的有 5 种： 从 0 到 5 越来越快； 5 是理想状态的最快速度
						// HPRTPrinterHelper.Speed("4");//打印速度
						HPRTPrinterHelper.Form();
						HPRTPrinterHelper.Print();
						// HPRTPrinterHelper .PoPrint()//旋转180度打印
						a = HPRTPrinterHelper.Print();
						handler.sendEmptyMessage(a);
					} catch (Exception e) {
						handler.sendEmptyMessage(a);
					}
				}
			}.start();
		} else {
			txtTips.setText("请连接打印机！");
			showCustomToast("请先连接蓝牙!");
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			final Intent data) {
		String strIsConnected;
		switch (resultCode) {
		case HPRTPrinterHelper.ACTIVITY_CONNECT_BT:
			int result = data.getExtras().getInt("is_connected");
			if (result == 0) {
				// txtTips.setText("连接成功！");
				try {
					txtTips.setText("已连接蓝牙:" + HPRTPrinter.getPrintName());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				txtTips.setText("连接失败！");
			}
			break;
		}
	}

	@Override
	protected void onScan(String barcodeStr) {
		// TODO Auto-generated method stub

	}
}
