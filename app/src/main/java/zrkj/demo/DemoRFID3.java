package zrkj.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.finddreams.baselib.R;
import com.finddreams.baselib.base.BaseActivity;
import com.finddreams.baselib.view.ClearEditText;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnTouch;
import com.uhf.uhf.Common.Comm;
import com.uhf.uhf.UHF1.UHF001;
import com.uhf.uhf.UHF1.UHF1Application;
import com.uhf.uhf.UHF1.UHF1Function.AndroidWakeLock;
import com.uhf.uhf.UHF1.UHF1Function.SPconfig;
import com.uhf.uhf.UHF8.UHF008;

import zrkj.project.util.Result;

import static com.uhf.uhf.Common.Comm.Awl;
import static com.uhf.uhf.Common.Comm.Connect;
import static com.uhf.uhf.Common.Comm.ConnectModule;
import static com.uhf.uhf.Common.Comm.checkDevice;
import static com.uhf.uhf.Common.Comm.context;
import static com.uhf.uhf.Common.Comm.isQuick;
import static com.uhf.uhf.Common.Comm.isrun;
import static com.uhf.uhf.Common.Comm.lsTagList;
import static com.uhf.uhf.Common.Comm.lsTagList6B;
import static com.uhf.uhf.Common.Comm.soundPool;
import static com.uhf.uhf.Common.Comm.tagListSize;

@ContentView(R.layout.demorfid)
public class DemoRFID3 extends BaseActivity {

	@Override
	protected void onScan(String barcodeStr) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initView() {
	}

	// 全局变量

	@Override
	protected void initData() {
		// 初始化设备
		initDevice();
	}

	private void initDevice() {

		Comm.repeatSound = true;
		// getApplication()迷惑客户，往往导致出错
		Comm.app = new UHF1Application();
		Comm.spConfig = new SPconfig(this);
		checkDevice();
		Comm.powerUp();
		Comm.initWireless(Comm.app);
		Comm.connecthandler = connectH;
		Comm.uhf6.UHF6handler=uhfhandler;
		Comm.uhf8.mUHF8handler=uhfhandler;
		Comm.mInventoryHandler=uhfhandler;
		Comm.mOtherHandler=uhfhandler;
		Comm.mRWLHandler=uhfhandler;
		Comm.moduleType = Comm.Module.UHF008;
		if (Comm.moduleType != null) {
			Log.e("cjj", "1");
			ConnectModule();
		} else {
			Log.e("cjj", "2");
			Connect();
		}
		// // 设置连接处理器
		// Comm.connecthandler = connectH;
		// // 1
		// // Comm.moduleType = Comm.Module.UHF008;
		// // Comm.ConnectModule();
		// // 2
		// Comm.Connect();
		// // 3
		// // Comm.Connect(1);
		// // Comm.Connect(2);
		// // Comm.Connect(5);
		// // Comm.Connect(6);
		// //Comm.Connect(8);
		Timer timer = new Timer(true);
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				Log.e("cjj", "开始检测RFID数据");
				if (lsTagList6B.size() > 0) {
					Log.e("cjj", lsTagList6B.size() + "");
				}
				if (lsTagList.size() > 0) {
					Log.e("cjj", lsTagList.size() + "");
				}
			}
		}, 0 * 1000, 1 * 1000); // set timer duration
	}

	public Handler connectH = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			try {
				Bundle bd = msg.getData();
				String strMsg = bd.get("Msg").toString();
				if (!TextUtils.isEmpty(strMsg)) {
					showLongText(strMsg);
					// UHF008.mUHF8handler = uhfhandler;
					// 设置RDIF接收器
					Comm.mInventoryHandler = uhfhandler;
					// Comm.SetInventoryTid(true);// 设置 TID 具体干啥不清楚
					Comm.SetInventoryTid(false);
					Awl.WakeLock();
					Comm.startScan();
					Log.e("cjj", "模块初始化成功  " + strMsg);
				} else {
					showLongText("模块初始化失败");
					Log.e("cjj", "模块初始化失败");
				}
			} catch (Exception e) {
				e.printStackTrace();
				Log.e("cjj", e.getMessage());
			}
		}
	};
	public Handler uhfhandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Log.e("cjj", "收到消息  ");
		}
	};

	// 结束扫描
	protected void onDestory() {
		Awl.ReleaseWakeLock();
		Comm.stopScan();
		Comm.clean();
		Comm.powerDown();
		super.onDestroy();
	}

	private void setCS() {
		// 设置声音模式(盘点到重复标签)：
		// 盘点到重复标签是出声音
		// Comm.repeatSound=true;
		// 盘点到重复标签是不出声音
		// Comm.repeatSound=false;
		// try {
		// // 天线
		// Comm.opeT = Comm.operateType.setAntCheck;
		// Comm.setANTCheck(1);// 0不检查 1检查
		// } catch (Exception e) {
		// showLongText("是否检测天线 错误");
		// }
		try {
			// 天线功率
			Comm.opeT = Comm.operateType.setPower;
			int ant1pow = 33;// 5-33
			int ant2pow = 5;// 5
			int ant3pow = 5;// 5
			int ant4pow = 5;// 5
			Comm.setAntPower(ant1pow, ant2pow, ant3pow, ant4pow);
		} catch (Exception e) {
			showLongText("天线功率设置 错误");
		}
		try {
			// 区域
			Comm.opeT = Comm.operateType.setReg;
			Comm.setAntReg(1); // CHN(China), FCC(America),ETSI(Europe),OTHER
		} catch (Exception e) {
			showLongText("区域设置 错误");
		}
		// try {
		// // 频率 频点设置
		// Comm.opeT = Comm.operateType.setReg;
		// Comm.setAntReg(1); // CHN(China), FCC(America),ETSI(Europe),OTHER
		// } catch (Exception e) {
		// showLongText("区域设置 错误");
		// }
	}

	@Override
	protected void requestCallBack(Result ajax, int posi) {

	}
}
