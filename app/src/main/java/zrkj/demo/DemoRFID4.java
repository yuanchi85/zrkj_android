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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListAdapter;
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
public class DemoRFID4 extends BaseActivity {
	@ViewInject(R.id.button_start)
	private Button button_start;
	@ViewInject(R.id.button_stop)
	private Button button_stop;
	@ViewInject(R.id.button_readclear)
	private Button button_readclear;
	@ViewInject(R.id.button_set)
	private Button button_set;
	boolean cb_is6btag = false;
	boolean cb_readtid = false;

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
		Awl = new AndroidWakeLock(
				(PowerManager) getSystemService(Context.POWER_SERVICE));
		exittime = System.currentTimeMillis();
		// 初始化设备
		InitDevice();
	}

	private ListView listView;
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
	public void onStop() {
		super.onStop();// ATTENTION: This was auto-generated to implement the
						// App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		release();
		Log.d("Activity", "onStop()");
	}

	public void release() {
		try {
			if (isrun)
				Comm.stopScan();
			Comm.Exit();
			Comm.powerDown();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public class MyEpListAdapter extends ArrayAdapter<Object> {
		@SuppressWarnings("unchecked")
		public MyEpListAdapter(Context context, int resource,
				int textViewResourceId,
				@SuppressWarnings("rawtypes") List objects) {
			super(context, resource, textViewResourceId, objects);
			// TODO Auto-generated constructor stub
		}
	}

	public void InitDevice() {
		Comm.repeatSound = true;
		// getApplication()迷惑客户，往往导致出错
		// Comm.app = getApplication();
		Comm.app = new UHF1Application();
		Comm.spConfig = new SPconfig(this);
		soundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
		soundPool.load(this, R.raw.beep51, 1);
		Log.d("test", "soundPool");

		checkDevice();
		Comm.initWireless(Comm.app);
		Comm.connecthandler = connectH;
		// Comm.moduleType = null;
		if (Comm.moduleType != null)
			ConnectModule();
		else
			Connect();
		Log.d("test", "connect");
	}

	@SuppressLint("HandlerLeak")
	public Handler connectH = new Handler() {
		@SuppressLint("SetTextI18n")
		@SuppressWarnings({ "unchecked", "unused" })
		@Override
		public void handleMessage(Message msg) {
			try {
				if (Comm.moduleType == Comm.Module.UHF005)
					cb_is6btag = true;
				else
					cb_is6btag = false;
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
					showLongText(strMsg);
					Comm.SetInventoryTid(cb_readtid);
				} else {
					showLongText("连接失败1");
				}

			} catch (Exception e) {
				e.printStackTrace();
				showLongText("连接失败2");
				Log.e("connectH", e.getMessage());
			}
		}
	};

	@SuppressLint("HandlerLeak")
	public Handler uhfhandler = new Handler() {
		@SuppressWarnings({ "unchecked", "unused" })
		@Override
		public void handleMessage(Message msg) {
			try {
				if (Comm.is6BTag)
					tagListSize = lsTagList6B.size();
				else
					tagListSize = lsTagList.size();
				Bundle bd = msg.getData();

				int readCount = bd.getInt("readCount");
				if (readCount > 0) {
					showLongText("读取到数量:" + readCount);
				}

				// if (tagListSize > 0 && (System.currentTimeMillis() -
				// exittime) > 2000) {
				// showlist();
				// exittime = System.currentTimeMillis();
				// }
				if (tagListSize > 0)
					showlist();
				Log.e("uhfhandler", "readCount : " + String.valueOf(readCount));
			} catch (Exception e) {
				e.printStackTrace();
				Log.e("Tag", e.getMessage());
			}
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
					Log.e("cjj", "开始开始");
					start();
					Log.e("cjj", "开始结束");
				} catch (Exception ex) {
					ex.printStackTrace();
					Log.e("cjj", ex.getMessage());
				}
				break;
			case R.id.button_stop:
				Log.e("cjj", "按下结束");
				end();
				Log.e("cjj", "结束结束");
				break;
			case R.id.button_readclear:
				Log.e("cjj", "按下清空");
				clear();
				Log.e("cjj", "结束清空");
				break;
			case R.id.button_set:
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

	private void clear() {

		int i = Comm.lv.size();
		Comm.lv.clear();
		Comm.clean();
		showlist();
	}

	private void end() {
		// TODO Auto-generated method stub
		Awl.ReleaseWakeLock();
		stopTimerTask();
		Comm.stopScan();
		showlist();
		StopHandleUI();
	}

	private void start() {
		clear();
		try {
			startTimerTask();
			Awl.WakeLock();
			Comm.startScan();
			ReadHandleUI();
		} catch (Exception ex) {
			showLongText("启动报错:" + ex.getMessage());
		}

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
				// try {
				// // 100 millisecond
				// textView_time.setText(String.format("%1$02d:%2$02d:%3$d",
				// min, sec, yushu));
				// } catch (Exception e) {
				// textView_time.setText("" + min + ":" + sec + ":" + yushu);
				// e.printStackTrace();
				// Log.e("MyTimer onCreate", "Format string error.");
				// }
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}

	};

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

	String[] Coname = new String[] { "NO", "                    EPC ID ",
			"Count" };

	private void showlist() {
		try {
			int index = 1;
			int ReadCnt = 0;// 个数
			List<Map<String, ?>> list = new ArrayList<Map<String, ?>>();
			Map<String, String> h = new HashMap<String, String>();
			for (int i = 0; i < Coname.length; i++)
				h.put(Coname[i], Coname[i]);
			list.add(h);
			String epcstr = "";// epc

			int ListIndex = 0;
			if (tagListSize > 0) {
				showLongText("数量:" + String.valueOf(tagListSize));
			}
			if (isQuick && !Comm.isrun) {
				showShortText("等");
			}

			if (!isQuick || !Comm.isrun) {
				while (tagListSize > 0) {
					if (index < 100) {
						if (Comm.is6BTag) {
							epcstr = lsTagList6B.get(ListIndex).strUID;
							if (epcstr != null && epcstr != "") {
								epcstr = lsTagList6B.get(ListIndex).strUID
										.replace(" ", "");
								Map<String, String> m = new HashMap<String, String>();
								m.put(Coname[0], String.valueOf(index));
								m.put(Coname[1], epcstr);
								ReadCnt = lsTagList6B.get(ListIndex).nTotal;
								m.put(Coname[2], String.valueOf(ReadCnt));
								// int
								// mRSSI=Integer.parseInt(lsTagList.get(ListIndex).strRSSI);
								index++;
								list.add(m);
							}
						} else {
							epcstr = lsTagList.get(ListIndex).strEPC;
							if (Comm.dt == Comm.DeviceType.supoin_JT
									&& Comm.To433Index < index) {
								Comm.mWirelessMg.writeTo433(epcstr + "\n");
								Comm.To433Index++;
							}

							if (epcstr != null && !epcstr.equals("")) {
								epcstr = lsTagList.get(ListIndex).strEPC
										.replace(" ", "");
								if (epcstr.length() > 24) {
									epcstr = epcstr.substring(0, 24) + "\r\n"
											+ epcstr.substring(24);
								}

								Map<String, String> m = new HashMap<String, String>();
								m.put(Coname[0], String.valueOf(index));
								m.put(Coname[1], epcstr);
								ReadCnt = lsTagList.get(ListIndex).nReadCount;
								m.put(Coname[2], String.valueOf(ReadCnt));
								// int
								// mRSSI=Integer.parseInt(lsTagList.get(ListIndex).strRSSI);
								index++;
								list.add(m);
							}
						}
					}
					ListIndex++;
					tagListSize--;
				}

			}
			if (!isQuick || !Comm.isrun) {
				showLongText("数量:" + list);
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		if (!isrun) {
			showLongText("等待操作");
		}
	}

	@Override
	protected void requestCallBack(Result ajax, int posi) {

	}
}
