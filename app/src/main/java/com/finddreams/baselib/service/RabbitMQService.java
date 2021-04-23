package com.finddreams.baselib.service;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.finddreams.baselib.http.URLUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class RabbitMQService extends Service {
	public ConnectionFactory factory;
	public Connection connection;;

	@Override
	public void onCreate() {
		initFactory();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (connection != null && connection.isOpen()) {
			try {
				connection.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	// 发送消息
	public void publishMessage(String QUEUE_NAME, String msg) {
		if (connection != null && connection.isOpen()) {
			try {
				// 创建一个新连接
				Connection connection = factory.newConnection();
				// 创建一个通道
				Channel channel = connection.createChannel();
				// 声明一个队列
				channel.queueDeclare(QUEUE_NAME, false, false, false, null);
				// 发送消息
				channel.basicPublish("", QUEUE_NAME, null, msg.getBytes("UTF-8"));
				// 消息发送完成后，需要关闭通道和连接
				channel.close();

				connection.close();
				Log.v("cjj", "发送消息成功");
			} catch (TimeoutException e) {
				Log.v("cjj", "发送消息超时");
				e.printStackTrace();
			} catch (IOException e) {
				Log.v("cjj", "发送消息错误");
				e.printStackTrace();
			}
		} else {
			Log.v("cjj", "连接错误");
		}
	}

	public void initConnection() {
		try {
			connection = factory.newConnection();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void initFactory() {
		factory = new ConnectionFactory();
		factory.setHost(URLUtils.IP);
		factory.setUsername("admin");
		factory.setPassword("zrkj");
	}

	private final IBinder binder = new RabbitBinder(); // 绑定器

	public class RabbitBinder extends Binder {
		public RabbitMQService getService() {
			Log.e("cjj", "获取service:" + RabbitMQService.this);
			return RabbitMQService.this;
		}
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return binder;
	}
}
