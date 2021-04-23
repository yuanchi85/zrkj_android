package com.finddreams.baselib.app;

import java.util.HashMap;
import java.util.LinkedList;

import android.app.Activity;
import android.app.Application;

import com.finddreams.baselib.utils.ConstantsValue;
/**
 * 
 * @Description 全局Application管理类
 * @author blin
 * @since 2015年12月14日
 * */
@SuppressWarnings("deprecation")
public class MyApplication extends Application {
	private static boolean isLogged = false;     
	private static MyApplication instance;
	private static LinkedList<Activity> activityList;
	private Activity activity;
	/**
	 * 用于全局传递数据（包括对象），getObject(),和putObject(String key)方法管理
	 * */
	private HashMap<String,Object> objects;
	@Override
	public void onCreate()
	{
		instance = this;
		super.onCreate();
		if (!ConstantsValue.DEBUG) {
			/* 全局异常崩溃处理 */
			CrashHandler catchExcep = new CrashHandler(this);
			Thread.setDefaultUncaughtExceptionHandler(catchExcep);  
		}
		activityList = new LinkedList<Activity>();
		super.onCreate();
	}
	
	public Activity getActivity() {
		return activity;
	}
	public void setActivity(Activity activity) {
		this.activity = activity;
	}
	public static MyApplication getInstance()
	{
		return instance;
	}
	/** 
	 * Activity关闭时，删除Activity列表中的Activity对象
	 * */  
	public void removeActivity(Activity a){  
		activityList.remove(a);  
	}  
	
	/** 
	 * 向Activity列表中添加Activity对象*/  
	public void addActivity(Activity a){  
		activityList.add(a);  
	}  
	
	/** 
	 * 关闭Activity列表中的所有Activity
	 * */  
	public void finishActivity(){  
		for (Activity activity : activityList) {    
			if (null != activity) {    
				activity.finish();    
			}    
		}
		activityList.clear();
		//杀死该应用进程  
		android.os.Process.killProcess(android.os.Process.myPid());    
	}
	
	public static boolean isLogged() {
		return isLogged;
	}
	public static void setLogged(boolean isLogged) {
		MyApplication.isLogged = isLogged;
	}   
	/**
	 * 用于全局传递数据（包括对象）
	 * */
	public void setObject(String key,Object o){
		if(null==objects)
			objects = new HashMap<String, Object>();
		objects.put(key, o);
	}
	/**
	 * 得到需要的对象
	 * */
	public Object getObject(String key){
		if(null==objects)
			return null;
		return objects.get(key);
	}
}
