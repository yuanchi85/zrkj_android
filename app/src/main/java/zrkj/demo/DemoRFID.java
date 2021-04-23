package zrkj.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
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
public class DemoRFID extends BaseActivity {

	@ViewInject(R.id.button_start)
	private Button button_start;
	@ViewInject(R.id.button_stop)
	private Button button_stop;
	@ViewInject(R.id.button_start)
	private Button button_readclear;
	@ViewInject(R.id.button_start)
	private Button button_set;

	@Override
	protected void onScan(String barcodeStr) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		button_stop.setEnabled(false);
	}

	// 全局变量
	public long exittime;
	List<Byte> LB = new ArrayList();
	int scanCode = 0;
	private Timer timer = null;
	private TimerTask task = null;
	private Message msg = null;
	private long mlCount = 0;
	private long mlTimerUnit = 10;
	private int totalSec, yushu, min, sec;
	private String strMsg = "";

	@Override
	protected void initData() {
		// 初始化设备
		initDevice();
	}

	private void initDevice() {
		Log.e("cjj", "1");
		Comm.repeatSound = true;// 声音
		Comm.app = new UHF1Application();
		Comm.spConfig = new SPconfig(this);
		Log.e("cjj", "2");
		soundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
		soundPool.load(this, R.raw.beep51, 1);
		Log.e("cjj", "3");
		checkDevice();// 检测模块
		Comm.powerUp();// 模块上电
		Comm.initWireless(Comm.app);
		Comm.connecthandler = connectH;
		Comm.moduleType = null;
		// 如何知道是哪种类型
		// Comm.moduleType = Comm.Module.UHF001;
		// Comm.moduleType = Comm.Module.UHF002;
		// Comm.moduleType = Comm.Module.UHF003;
		// Comm.moduleType = Comm.Module.UHF005;
		// Comm.moduleType = Comm.Module.UHF006;
		// Comm.moduleType = Comm.Module.UHF007;
		// 目前这把枪模块型号是008型号
		Comm.moduleType = Comm.Module.UHF008;
		// Comm.moduleType = Comm.Module.UHF009;
		if (Comm.moduleType != null) {
			ConnectModule();// 指定类型连接
		} else {
			Connect();// 自动适配连接 慢
		}
		Awl = new AndroidWakeLock(
				(PowerManager) getSystemService(context.POWER_SERVICE));
		Log.e("cjj", "4");

	}

	@SuppressLint("HandlerLeak")
	public Handler connectH = new Handler() {
		@SuppressLint("SetTextI18n")
		@SuppressWarnings({ "unchecked", "unused" })
		@Override
		public void handleMessage(Message msg) {
			try {
				if (Comm.moduleType == Comm.Module.UHF005) {
					// 是否 6B tags 具体干啥不清楚
				}
				// UHF001.mhandler = uhfhandler;
				// if (null != rfidOperate) {
				// rfidOperate.mHandler = uhfhandler;
				// cb_is6btag.setEnabled(true);
				// }
				// if (null != Comm.uhf6)
				// Comm.uhf6.UHF6handler = uhfhandler;
				Comm.mInventoryHandler = uhfhandler;

				Bundle bd = msg.getData();
				strMsg = bd.get("Msg").toString();
				if (!TextUtils.isEmpty(strMsg)) {
					showLongText("模块初始化成功  " + strMsg);
					Comm.SetInventoryTid(true);// 设置 TID 具体干啥不清楚
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

	@SuppressLint("HandlerLeak")
	public Handler uhfhandler = new Handler() {
		@SuppressWarnings({ "unchecked", "unused" })
		@Override
		public void handleMessage(Message msg) {
		}
	};

	@OnTouch({ R.id.button_start, R.id.button_stop, R.id.button_readclear,
			R.id.button_set })
	private <T> boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			v.getBackground().setAlpha(20);
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			v.getBackground().setAlpha(255);// 设置的透明度
			switch (v.getId()) {
			case R.id.button_start:
				try {
					Log.e("cjj", "按下开始");
					startTimerTask();
					Log.e("cjj", "1");
					button_readclear.performClick();
					Log.e("cjj", "2");
					Awl.WakeLock();
					Log.e("cjj", "3");
					Comm.startScan();
					Log.e("cjj", "4");
					ReadHandleUI();
					Log.e("cjj", "开始结束");
				} catch (Exception ex) {
					ex.printStackTrace();
					Log.e("cjj", ex.getMessage());
					showLongText("开始按钮报错:" + ex.getMessage());
					button_stop.performClick();
				}
				break;
			case R.id.button_stop:
				Log.e("cjj", "按下结束");
				Awl.ReleaseWakeLock();
				stopTimerTask();
				Comm.stopScan();
				showlist();
				StopHandleUI();
				Log.e("cjj", "结束结束");
				break;
			case R.id.button_readclear:
				Log.e("cjj", "按下清空");
				int i = Comm.lv.size();
				Comm.lv.clear();
				Comm.clean();
				showlist();
				Log.e("cjj", "结束清空");
				break;
			case R.id.button_set:
				// Comm.setParameters();// 快速扫描(只显示数量)与实时扫描切换 isQuick参数
				// 可以看出是否快速扫描
				Log.e("cjj", "isQuick:" + isQuick);
				if (Comm.setParameters()) {
					if (isQuick) {// 是否快速模式 true为是 false为否
						isQuick = false;
						showLongText("实时扫描设置成功");
					} else {
						isQuick = true;
						showLongText("快速扫描设置成功");
					}
				} else {
					showLongText("设置扫描模式失败");
				}
				break;
			default:
				break;
			}
		}
		return true;
	}

	// 开始计时
	public void startTimerTask() {
		if (null == timer) {
			if (null == task) {
				task = new TimerTask() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if (null == msg) {
							msg = new Message();
						} else {
							msg = Message.obtain();
						}
						msg.what = 1;
						handlerTimerTask.sendMessage(msg);
					}
				};
			}
			timer = new Timer(true);
			timer.schedule(task, mlTimerUnit, mlTimerUnit); // set timer
															// duration
		}
	}

	// 停止计时
	public void stopTimerTask() {
		if (null != timer) {
			task.cancel();
			task = null;
			timer.cancel(); // Cancel timer
			timer.purge();
			timer = null;
			handlerTimerTask.removeMessages(msg.what);
		}
		mlCount = 0;
	}

	private void ReadHandleUI() {
		button_start.setEnabled(false);
		button_stop.setEnabled(true);
		button_readclear.setEnabled(false);
		button_set.setEnabled(false);
	}

	private void StopHandleUI() {
		button_start.setEnabled(true);
		button_stop.setEnabled(false);
		button_readclear.setEnabled(true);
		button_set.setEnabled(true);
	}

	// 异步处理计时数据
	@SuppressLint("HandlerLeak")
	public Handler handlerTimerTask = new Handler() {
		@SuppressLint({ "SetTextI18n", "DefaultLocale" })
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 1:
				mlCount++;
				totalSec = 0;
				// 100 millisecond
				totalSec = (int) (mlCount / 100);
				yushu = (int) (mlCount % 100);
				// Set time display
				min = (totalSec / 60);
				sec = (totalSec % 60);
				try {
					// 100 millisecond
					// Log.e("cjj",
					// "计时任务:"
					// + String.format("%1$02d:%2$02d:%3$d", min,
					// sec, yushu));
				} catch (Exception e) {
					// Log.e("cjj", "计时任务错误:" + "" + min + ":" + sec + ":" +
					// yushu);
					// e.printStackTrace();
					// Log.e("MyTimer onCreate", "Format string error.");
				}
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}

	};

	// 结束扫描
	protected void onDestory() {
		Comm.stopScan();
		Comm.clean();
		Comm.powerDown();
		super.onDestroy();
	}

	private void showlist() {
		Log.e("cjj", "showlist获取数据");
		// try {
		// int index = 1;
		// int ReadCnt = 0;// 个数
		// List<Map<String, ?>> list = new ArrayList<Map<String, ?>>();
		// Map<String, String> h = new HashMap<String, String>();
		// for (int i = 0; i < Coname.length; i++)
		// h.put(Coname[i], Coname[i]);
		// list.add(h);
		// String epcstr = "";// epc
		//
		// int ListIndex = 0;
		// if (tagListSize > 0)
		// tv_tags.setText(String.valueOf(tagListSize));
		// if (isQuick && !Comm.isrun)
		// tv_state.setText(String.valueOf(R.string.please_wait));
		//
		// if (!isQuick || !Comm.isrun) {
		// while (tagListSize > 0) {
		// if (index < 100) {
		// if (Comm.is6BTag) {
		// epcstr = lsTagList6B.get(ListIndex).strUID;
		// if (epcstr != null && epcstr != "") {
		// epcstr = lsTagList6B.get(ListIndex).strUID
		// .replace(" ", "");
		// Map<String, String> m = new HashMap<String, String>();
		// m.put(Coname[0], String.valueOf(index));
		// m.put(Coname[1], epcstr);
		// ReadCnt = lsTagList6B.get(ListIndex).nTotal;
		// m.put(Coname[2], String.valueOf(ReadCnt));
		// // int
		// // mRSSI=Integer.parseInt(lsTagList.get(ListIndex).strRSSI);
		// index++;
		// list.add(m);
		// }
		// } else {
		// epcstr = lsTagList.get(ListIndex).strEPC;
		// if (Comm.dt == Comm.DeviceType.supoin_JT
		// && Comm.To433Index < index) {
		// Comm.mWirelessMg.writeTo433(epcstr + "\n");
		// Comm.To433Index++;
		// }
		//
		// if (epcstr != null && !epcstr.equals("")) {
		// epcstr = lsTagList.get(ListIndex).strEPC
		// .replace(" ", "");
		// if (epcstr.length() > 24) {
		// epcstr = epcstr.substring(0, 24) + "\r\n"
		// + epcstr.substring(24);
		// }
		//
		// Map<String, String> m = new HashMap<String, String>();
		// m.put(Coname[0], String.valueOf(index));
		// m.put(Coname[1], epcstr);
		// ReadCnt = lsTagList.get(ListIndex).nReadCount;
		// m.put(Coname[2], String.valueOf(ReadCnt));
		// // int
		// // mRSSI=Integer.parseInt(lsTagList.get(ListIndex).strRSSI);
		// index++;
		// list.add(m);
		// }
		// }
		// }
		// ListIndex++;
		// tagListSize--;
		// }
		//
		// }
		// if (!isQuick || !Comm.isrun) {
		// ListAdapter adapter = new MyAdapter(this, list,
		// R.layout.listitemview_inv, Coname, new int[] {
		// R.id.textView_readsort, R.id.textView_readepc,
		// R.id.textView_readcnt });
		//
		// // layout为listView的布局文件，包括三个TextView，用来显示三个列名所对应的值
		// // ColumnNames为数据库的表的列名
		// // 最后一个参数是int[]类型的，为view类型的id，用来显示ColumnNames列名所对应的值。view的类型为TextView
		// listView.setAdapter(adapter);
		//
		// }
		// } catch (NumberFormatException e) {
		// e.printStackTrace();
		// }
		// if (!isrun)
		// tv_state.setText(R.string.wait_operate);
	}

	@Override
	protected void requestCallBack(Result ajax, int posi) {

	}
}
