package com.finddreams.baselib.base;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import zrkj.project.toast.ToastView;
import zrkj.project.util.Result;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Resources.NotFoundException;
import android.device.ScanManager;
import android.device.scanner.configuration.Triggering;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.finddreams.baselib.R;
import com.finddreams.baselib.http.MyHttpClient;
import com.finddreams.baselib.http.MyHttpUtils;
import com.finddreams.baselib.http.URLUtils;
import com.finddreams.baselib.utils.AppManager;
import com.finddreams.baselib.utils.DeviceInfoUtil;
import com.finddreams.baselib.utils.ToastManager;
import com.finddreams.baselib.view.CustomProgressDialog;
import com.finddreams.baselib.view.TopBarView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.loopj.android.http.AsyncHttpClient;

/**
 * @Description:所有Activity的基类，是个抽象类，把整个项目中都需要用到的东西封装起来
 * @author blin
 * @since 2015年12月14日
 */
public abstract class BaseActivity extends FragmentActivity

// implements ServiceConnection, MqttCallback
// 开启MQTT再开启这个============================================================
{

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent home = new Intent(Intent.ACTION_MAIN);
			home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			home.addCategory(Intent.CATEGORY_HOME);
			startActivity(home);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	protected Context context;
	protected AsyncHttpClient httpClient;
	// 顶部控件
	protected TopBarView topTitle;
	protected CustomProgressDialog cpd = null;
	public TipUtil tu;
	//private BroadcastReceiver  mReceiver;

	//肖邦X8
	public static final String SCN_CUST_ACTION_SCODE = "com.android.server.scannerservice.broadcast";
	public static final String SCN_CUST_EX_SCODE = "scannerdata";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);// 不随屏幕旋转
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		AppManager.getAppManager().addActivity(this);
		context = this;
		tu = new TipUtil(context);
		httpClient = MyHttpClient.getHttpClient();
		// 初始化顶部控件
		topTitle = new TopBarView(context);

		topTitle.setActivity(this);//退出
		// 初始化相关等待提示框
		cpd = new CustomProgressDialog(context, "请设置提示框");
		ViewUtils.inject(this);
		initView();
		addTopBarView();
		initData();


		IntentFilter intentFilter = new IntentFilter(SCN_CUST_ACTION_SCODE);
		registerReceiver(mSamDataReceiver, intentFilter);



		//x8
       /* //新大陆
        registerReceiver();
        unRegisterReceiver();
        mReceiver= new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                final String scanResult_1=intent.getStringExtra("SCAN_BARCODE1");
                final String scanResult_2=intent.getStringExtra("SCAN_BARCODE2");
                final int barcodeType = intent.getIntExtra("SCAN_BARCODE_TYPE", -1); // -1:unknown
                final String scanStatus=intent.getStringExtra("SCAN_STATE");
                if("ok".equals(scanStatus)){
                    onScan(scanResult_1);
                }else{

                }
            }
        };
        //新大陆*/
	}

	// =====================肖邦U8=========================================================================================================================
	private final static String SCAN_ACTION = "scan.rcv.message";
	private int soundid;
	private SoundPool soundpool = null;
	private BroadcastReceiver mSamDataReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if(isscan){
				isscan=false;
				if (intent.getAction().equals(SCN_CUST_ACTION_SCODE)) {
					try {
						String message = intent.getStringExtra(SCN_CUST_EX_SCODE).toString();
						onScan(message.replaceAll("\uFEFF", "").replaceAll("，",",").replaceAll("（","(").replaceAll("）",")"));
						isscan=true;
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
						Log.e("in", e.toString());
						isscan=true;
					}
				}
			}else{
				showMsg("正在扫描处理中");
			}

		}
	};
	// ==============================================================================================================================================

	@Override
	protected void onStart() {
		super.onStart();
		//===优博讯驱动
        /*mScanManager = new ScanManager();
        mScanManager.openScanner();
        mScanManager.switchOutputMode(0);*/
		//===
		soundpool = new SoundPool(1, AudioManager.STREAM_NOTIFICATION, 100); //
		soundid = soundpool.load("/etc/Scan_new.ogg", 1);
	}

	@Override
	protected void onResume() {
		super.onResume();
		//===优博讯驱动
        /*IntentFilter ybxfilter = new IntentFilter();
        ybxfilter.addAction(YBXSCAN_ACTION);
        registerReceiver(ybxmScanReceiver, ybxfilter);
        if (mScanManager.getTriggerMode() == Triggering.CONTINUOUS)
            mScanManager.setTriggerMode(Triggering.HOST);*/
		//===

		//===肖邦驱动U8
		IntentFilter intentFilter = new IntentFilter(SCN_CUST_ACTION_SCODE);
		this.registerReceiver(mSamDataReceiver, intentFilter);
		//===

		//===新中大
		IntentFilter filter = new IntentFilter();
		filter.addAction(SCAN_ACTION);
		registerReceiver(mScanReceiver, filter);
		//===

		//===IDATA
        /*IntentFilter idataintentFilter = new IntentFilter();
        idataintentFilter.addAction(RES_ACTION);
        registerReceiver(scanReceiver, idataintentFilter);*/
		//===
	}

	@Override
	protected void onPause() {
		super.onPause();
		//===优博讯驱动
        /*if (mScanManager != null) {
            mScanManager.stopDecode();
            isScaning = false;
        }
        unregisterReceiver(ybxmScanReceiver);*/
		//====

		//===肖邦驱动U8
		this.unregisterReceiver(mSamDataReceiver); //世峰肖邦驱动
		//====

		//===新中大
		unregisterReceiver(mScanReceiver);
		//====

		//===IDATA
		//unregisterReceiver(scanReceiver);
		//===
	}


	private boolean isscan=true;
	private BroadcastReceiver mScanReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			byte[] barocode = intent.getByteArrayExtra("barocode");
			int barocodelen = intent.getIntExtra("length", 0);
			byte temp = intent.getByteExtra("barcodeType", (byte) 0);
			byte[] aimid = intent.getByteArrayExtra("aimid");
			String message = new String(barocode, 0, barocodelen);
			onScan(message.replaceAll("\uFEFF", "").replaceAll("，",",").replaceAll("（","(").replaceAll("）",")"));
		}
	};



	/**
	 * 设置xml文件
	 */
	/*
	 * protected abstract void loadXml();
	 */
	// 添加顶部控件
	@SuppressWarnings("deprecation")
	protected void addTopBarView() {
		// ***** addview
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.FILL_PARENT,
				FrameLayout.LayoutParams.WRAP_CONTENT);
		// 设置顶部,左边布局
		params.gravity = Gravity.TOP | Gravity.LEFT;
		// //设置底部
		// params3.gravity=Gravity.BOTTOM|Gravity.RIGHT;
		// //设置中间位置
		// params2.gravity=Gravity.CENTER;
		this.addContentView(topTitle, params);
	}

	/**
	 * 扫描到信息后处理
	 * */
	protected abstract void onScan(String barcodeStr);

	/**
	 * 初始化所有控件
	 * */
	protected abstract void initView();

	/**
	 * 初始化数据
	 * */
	protected abstract void initData();

	@Override
	protected void onDestroy() {
		super.onDestroy();
//		AppManager.getAppManager().finishActivity(this);
	}

	protected void showCustomToast(String str) {
		ToastManager.showShortText(context, str);
	}

	/**
	 * 长文本提示
	 */
	public void showLongText(String text) {
		Toast.makeText(this, text, Toast.LENGTH_LONG).show();
	}

	/**
	 * 短文本提示
	 */
	public void showShortText(String text) {
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 短文本提示
	 */
	public void showShortText(int resId) {
		try {
			Toast.makeText(this, context.getResources().getString(resId),
					Toast.LENGTH_SHORT).show();
		} catch (NotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 长文本提示
	 */
	public void showLongText(int resId) {
		try {
			Toast.makeText(this, context.getResources().getString(resId),
					Toast.LENGTH_LONG).show();
		} catch (NotFoundException e) {
			e.printStackTrace();
		}
	}

	public String getDeviceId() {
		String m_szUniqueID = DeviceInfoUtil.getOnlyID(this);
		m_szUniqueID = m_szUniqueID.substring(0, 6)
				+ m_szUniqueID.substring(10, 16)
				+ m_szUniqueID.substring(m_szUniqueID.length() - 7,
				m_szUniqueID.length() - 1);
		return m_szUniqueID;
	}




	public class TipUtil {
		public Context context;

		public TipUtil(Context context) {
			this.context = context;
			InitSound();
		}

		// 震动
		public void zd() {
			new Thread(new Runnable() {
				public void run() {
					Vibrator vibrator = (Vibrator) context
							.getSystemService(Context.VIBRATOR_SERVICE);
					long[] pattern = { 100, 400, 100, 400 }; // 停止 开启 停止 开启
					vibrator.vibrate(pattern, 3); // 重复两次上面的pattern //
					// 如果只想震动一次，index设为-1
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					vibrator.cancel();
				}
			}).start();
		}

		private SoundPool sp;
		private AudioManager am;

		// 1
		// 初始化声音 不加载无法播放
		public void InitSound() {
			am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
			sp = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
			sp.load(context, R.raw.systemerror, 1);
			sp.load(context, R.raw.neterror, 1);
			sp.load(context, R.raw.operateerror, 1);
			sp.load(context, R.raw.success, 1);
			sp.load(context, R.raw.zzwc, 1);
			sp.load(context, R.raw.tlwc, 1);
		}

		public void playSounds(int sound) {

			float audioMaxVolumn = am
					.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
			float volumnCurrent = am.getStreamVolume(AudioManager.STREAM_MUSIC);
			float volumnRatio = volumnCurrent / audioMaxVolumn;
			sp.play(sound, volumnRatio, volumnRatio, 1, 0, 1f);
		}

		// 2
		private MediaPlayer mMediaPlayer;

		public void playSound(int sid) {

			if (mMediaPlayer != null) {
				if (mMediaPlayer.isPlaying()) {
					mMediaPlayer.stop();
				}
				mMediaPlayer.release();
				mMediaPlayer = null;
			}

			mMediaPlayer = MediaPlayer.create(context, sid);
			/* 准备播放 */
			try {
				mMediaPlayer.prepare();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			/* 开始播放 */
			mMediaPlayer.start();
			zd();
		}

		// 播放声音
		public void playSounds(int sid, String tipstr) {

			if (mMediaPlayer != null) {
				if (mMediaPlayer.isPlaying()) {
					mMediaPlayer.stop();
				}
				mMediaPlayer.release();
				mMediaPlayer = null;
			}

			mMediaPlayer = MediaPlayer.create(context, sid);
			/* 准备播放 */
			try {
				mMediaPlayer.prepare();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			/* 开始播放 */
			mMediaPlayer.start();
			zd();
		}

		public AlertDialog dialogtip;

		// 窗口提示
		public void tipWithDialog(String msg) {
			zd();
			//closeScan();
			dialogtip = new AlertDialog.Builder(context).setMessage(msg)
					.setIcon(android.R.drawable.ic_dialog_info)
					.setCancelable(false).setPositiveButton("确定", null)
					.create();
			dialogtip.setOnShowListener(new DialogInterface.OnShowListener() {
				@Override
				public void onShow(DialogInterface dialogInterface) {
					Button btnPositive = dialogtip
							.getButton(DialogInterface.BUTTON_POSITIVE);
					btnPositive.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							//openScan();
							dialogtip.dismiss();
							dialogtip.cancel();
						}
					});
				}
			});
			dialogtip.setCanceledOnTouchOutside(false);
			dialogtip.show();
		}
	}

	public static <T> List<T> getDataList(String jsonString, Class<T> type) {
		List<T> list = new ArrayList<T>();
		Gson gson = new Gson();
		list = gson.fromJson(jsonString, new TypeToken<List<T>>() {}.getType());
		return list;
	}

	public static <T> T getData(String jsonString, Class<T> type) {
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
		return gson.fromJson(jsonString, new TypeToken<T>(){}.getType());
	}

	public void showMsg(String msg){
		//Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
		ToastView tv=new ToastView(context,msg);
		tv.show();
	}


	//根据泛型返回解析制定的类型
	public  <T> T fromToJson(String json,Type listType){
		Gson gson = new Gson();
		T t = null;
		t = gson.fromJson(json,listType);
		return t;
	}
	/**
	 * @param cla 点击界面图标，跳转不同界面
	 */
	public void jump(Context context,Class<?> cla) {
		Intent intent = getIntent();
		intent.setClass(context, cla);
		startActivity(intent);
	}

	/**
	 * @param str 判断两个字符是否为空
	 * @return
	 */
	public static boolean isNotEmpty(Object str) {
		boolean flag = true;
		if (str != null && !str.equals("")) {
			if (str.toString().length() > 0) {
				flag = true;
			}
		} else {
			flag = false;
		}
		return flag;
	}

	public void request(String json, String method, String message, final int posi){
		cpd.setTvMsgAndShow(message);
		new MyHttpUtils(context).httpPostByJson(
				URLUtils.getUrl(method), json,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						showCustomToast("网络异常或者服务器未开放" + arg1);
						cpd.dismiss();
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						Gson gson = new Gson();
						try {

							Result j = gson.fromJson(arg0.result.toString(),Result.class);
							showMsg(j.getMessage());
							if(j.isSuccess()==true){
								requestCallBack(j,posi);
							}
						} catch (Exception e) {
							e.printStackTrace();
							showCustomToast("服务端返回数据包错误");
						}
						cpd.dismiss();
					}
				});
	}
	public void requestButton(final Button button, String json, String method, String message, final int posi){
		cpd.setTvMsgAndShow(message);
		new MyHttpUtils(context).httpPostByJson(
				URLUtils.getUrl(method), json,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						button.setEnabled(true);
						showCustomToast("网络异常或者服务器未开放   " + arg1);
						cpd.dismiss();
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						Gson gson = new Gson();
						try {
							button.setEnabled(true);
							Result j = gson.fromJson(arg0.result.toString(),Result.class);
							showMsg(j.getMessage());
							if(j.isSuccess()==true){
								requestCallBack(j,posi);
							}
						} catch (Exception e) {
							e.printStackTrace();
							showCustomToast("服务端返回数据包错误");
						}
						cpd.dismiss();
					}
				});
	}

	public void requestNoSuccess(String json, String method, String message, final int posi){
		cpd.setTvMsgAndShow(message);
		new MyHttpUtils(context).httpPostByJson(
				URLUtils.getUrl(method), json,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						showCustomToast("网络异常或者服务器未开放   " + arg1);
						cpd.dismiss();
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						Gson gson = new Gson();
						try {
							Result j = gson.fromJson(arg0.result.toString(),Result.class);
							showMsg(j.getMessage());

							requestCallBack(j,posi);
						} catch (Exception e) {
							e.printStackTrace();
							showCustomToast("服务端返回数据包错误");
						}
						cpd.dismiss();
					}
				});
	}
	public void requestByGet(String json, String method, String message, final int posi){
		cpd.setTvMsgAndShow(message);
		new MyHttpUtils(context).httpGetByJson(
				URLUtils.getUrl(method), json,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						showCustomToast("网络异常或者服务器未开放   " + arg1);
						cpd.dismiss();
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						Gson gson = new Gson();
						try {
							Result j = gson.fromJson(arg0.result.toString(),Result.class);
							showMsg(j.getMessage());
							if(j.isSuccess()){
								requestCallBack(j,posi);
							}
						} catch (Exception e) {
							e.printStackTrace();
							showCustomToast("服务端返回数据包错误");
						}
						cpd.dismiss();
					}
				});
	}

	public void requestcloseZero(String json, String method, String message, final int posi){
		cpd.setTvMsgAndShow(message);
		new MyHttpUtils(context).httpPostByJson(
				method, json,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						showCustomToast("网络异常或者服务器未开放   " + arg1);
						cpd.dismiss();
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						Gson gson = new Gson();
						try {
							Result j = gson.fromJson(arg0.result.toString(),Result.class);
							showMsg(j.getMessage());
							if(j.isSuccess()){
								requestCallBack(j,posi);
							}
						} catch (Exception e) {
							e.printStackTrace();
							showCustomToast("服务端返回数据包错误");
						}
						cpd.dismiss();
					}
				});
	}

	protected abstract void  requestCallBack(Result ajax, int posi);

	//去掉首尾指定字符串
	public String trimFirstAndLastChar(String source,String element){
		boolean beginIndexFlag = true;
		boolean endIndexFlag = true;
		do{
			int beginIndex = source.indexOf(element) == 0 ? 1 : 0;
			int endIndex = source.lastIndexOf(element) + 1 == source.length() ? source.lastIndexOf(element) : source.length();
			source = source.substring(beginIndex, endIndex);
			beginIndexFlag = (source.indexOf(element) == 0);
			endIndexFlag = (source.lastIndexOf(element) + 1 == source.length());
		} while (beginIndexFlag || endIndexFlag);
		return source;
	}

	/**
	 * 截取替换批号
	 * @param Batch
	 * @return
	 */
	public String UpdateBatch(String Batch){
		if(Batch.length()<12) return "";
		String batch = Batch.substring(8,12);
		String upBatchs = batch.substring(0,1)+"Y"+batch.substring(2,4);
		return upBatchs;
	}
}
