package com.finddreams.baselib.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.internal.MemoryPersistence;

import zrkj.demo.DemoWebView;
import zrkj.project.util.AjaxJson;

import com.finddreams.baselib.R;
import com.finddreams.baselib.http.MyHttpUtils;
import com.finddreams.baselib.http.URLUtils;
import com.finddreams.baselib.utils.DeviceInfoUtil;
import com.finddreams.baselib.utils.SharePrefUtil;
import com.google.gson.Gson;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

/**
 * @author Dominik Obermaier
 */
public class MQTTService extends Service {
	public String getID() {
		return DeviceInfoUtil.getDeviceUniqID(this);
	}

	// public String getID() {
	// String m_szUniqueID = DeviceInfoUtil.getOnlyID(this);
	// m_szUniqueID = m_szUniqueID.substring(0, 6)
	// + m_szUniqueID.substring(10, 16)
	// + m_szUniqueID.substring(m_szUniqueID.length() - 7,
	// m_szUniqueID.length() - 1);
	// return m_szUniqueID;
	// }

	// onCreate方法只会调用一次，onStart将会被调用多次
	@Override
	public IBinder onBind(Intent intent) {
		return new MqttBinder();
	}

	public class MqttBinder extends Binder {
		/**
		 * 获取当前Service的实例
		 * 
		 * @return
		 */
		public MQTTService getService() {
			return MQTTService.this;
		}
	}

	private String userName = "admin";
	private String passWord = "password";
	public static final String BROKER_URL = "tcp://" + URLUtils.IP + ":61613";
	private MqttConnectOptions options;
	public static String clientId = "";
	public static String TOPIC = "";
	private MqttClient mqttClient;

	public MqttClient getMqttClient() {
		return mqttClient;
	}

	public void setMqttClient(MqttClient mqttClient) {
		this.mqttClient = mqttClient;
	}

	@Override
	public void onCreate() {
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if (msg.what == 1) {
					tipONTop((String) msg.obj);
				} else if (msg.what == 2) {
					//连接成功
				} else if (msg.what == 3) {
					Toast.makeText(MQTTService.this, "推送平台连接失败",
							Toast.LENGTH_SHORT).show();
				}
			}
		};

		if (mqttClient == null || !mqttClient.isConnected()) {
			try {
				clientId = getID();
				Log.e("cjj", "========机器编号=======" + clientId);
				TOPIC = SharePrefUtil.getString(this, "userName", "");
				mqttClient = new MqttClient(BROKER_URL, clientId,
						new MemoryPersistence());
				// MQTT的连接设置
				options = new MqttConnectOptions();
				// 设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，这里设置为true表示每次连接到服务器都以新的身份连接
				options.setCleanSession(true);
				// 设置连接的用户名
				options.setUserName(userName);
				// 设置连接的密码
				options.setPassword(passWord.toCharArray());
				// 设置超时时间 单位为秒
				options.setConnectionTimeout(10);
				// 设置会话心跳时间 单位为秒
				// 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
				options.setKeepAliveInterval(20);

				message = new MqttMessage();
				// 设置回调
//				mqttClient.setCallback(new MqttCallback() {
//
//					@Override
//					public void connectionLost(Throwable cause) {
//						// 连接丢失后，一般在这里面进行重连
//						Log.e("cjj", "connectionLost----------");
//					}
//
//					@Override
//					public void deliveryComplete(MqttDeliveryToken arg0) {
//						// TODO Auto-generated method stub
//
//					}
//
//					@Override
//					public void messageArrived(MqttTopic topicName,
//							MqttMessage message) throws Exception {
//						// subscribe后得到的消息会执行到这里面
//						Log.e("cjj", "messageArrived----------");
//						Message msg = new Message();
//						msg.what = 1;
//						msg.obj = topicName + "---" + message.toString();
//						handler.sendMessage(msg);
//						tipONTop(message.toString());
//					}
//				});
			} catch (MqttException e) {
				Log.e("cjj", "连接推送平台异常" + clientId);
				e.printStackTrace();
			}
		}
		startReconnect();
		super.onCreate();
	}

	@Override
	public void onStart(Intent intent, int startId) {

		super.onStart(intent, startId);
	}

	private Handler handler;
	private ScheduledExecutorService scheduler;

	private void startReconnect() {
		scheduler = Executors.newSingleThreadScheduledExecutor();
		scheduler.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				if (!mqttClient.isConnected()) {
					connect();
				}
			}
		}, 0 * 1000, 5 * 1000, TimeUnit.MILLISECONDS);
	}

	private void connect() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					mqttClient.connect(options);
					mqttClient.subscribe(TOPIC);
					mqttClient.subscribe("web");
					Message msg = new Message();
					msg.what = 2;
					handler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
					Message msg = new Message();
					msg.what = 3;
					handler.sendMessage(msg);
				}
			}
		}).start();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		try {
			scheduler.shutdown();
			mqttClient.disconnect();
		} catch (MqttException e) {
			e.printStackTrace();
		}
		super.onDestroy();
	}

	// 关注主题 返回true则关注成功
	public boolean subscribe(String topic) {
		try {
			mqttClient.subscribe(topic);
		} catch (MqttSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	// 取消关注主题
	public boolean unsubscribe(String topic) {
		try {
			mqttClient.unsubscribe(topic);
		} catch (MqttSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private MqttMessage message;
	private MqttTopic topic;

	// 发送消息
	public boolean sendmesg(String topicstr, String msg) {
		topic = mqttClient.getTopic(topicstr);
		try {
			if (mqttClient.isConnected()) {
				message.setPayload(msg.getBytes());
				MqttDeliveryToken token = topic.publish(message);
				token.waitForCompletion();
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	int i = (int) new Date().getTime();
	private Notification notify;
	Bitmap LargeBitmap = null;
	private NotificationManager mNManager = null;

	private void tipONTop(String key) {
		RequestParams rp = new RequestParams();
		rp.addBodyParameter("key", key);
		new MyHttpUtils(this).httpPost(
				URLUtils.getUrl("cjjAgriculController.do?getMqttData"), rp,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						Gson gson = new Gson();
						try {
							AjaxJson j = gson.fromJson(arg0.result.toString(),
									AjaxJson.class);
						} catch (Exception e) {
							Log.e("cjj", e.getMessage() + "");
						}
					}
				});
		LargeBitmap = BitmapFactory.decodeResource(getResources(),
				R.mipmap.iv_lc_icon);
		mNManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		Intent it = new Intent(this, DemoWebView.class);
		PendingIntent pit = PendingIntent.getActivity(this, 0, it, 0);
		// 设置图片,通知标题,发送时间,提示方式等属性
		Notification.Builder mBuilder = new Notification.Builder(this);
		mBuilder.setContentTitle("收到一条来自管理员的生产计划任务" + i)
				// 标题
				.setContentText("通知内容" + i)
				// 内容
				.setSubText("内容下面的一小段文字" + i)
				// 内容下面的一小段文字
				.setTicker("收到信息后状态栏显示的文字信息")
				// 收到信息后状态栏显示的文字信息
				.setWhen(System.currentTimeMillis())
				// 设置通知时间
				.setSmallIcon(R.mipmap.ic_lol_icon)
				// 设置小图标
				.setLargeIcon(LargeBitmap)
				// 设置大图标
				.setDefaults(
						Notification.DEFAULT_LIGHTS
								| Notification.DEFAULT_VIBRATE) // 设置默认的三色灯与振动器
				.setSound(
						Uri.parse("android.resource://" + getPackageName()
								+ "/" + R.raw.biaobiao)) // 设置自定义的提示音
				.setAutoCancel(true) // 设置点击后取消Notification
				.setContentIntent(pit); // 设置PendingIntent
		notify = mBuilder.build();
		mNManager.notify(i, notify);
		i++;
	}
}
