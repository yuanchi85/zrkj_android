package com.finddreams.baselib.base;

import com.finddreams.baselib.app.MyApplication;
import com.finddreams.baselib.http.MyHttpClient;
import com.loopj.android.http.AsyncHttpClient;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
/**
 * @Description:所有fragment的基类
 * @author blin
 * @since 2015年12月14日
 */ 
public abstract class BaseFragment extends Fragment implements OnClickListener {
	protected Context context;     
	public View rootView;
	//MyApplication  报错
	protected Application app;
	protected AsyncHttpClient httpClient;
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		
		super.onActivityCreated(savedInstanceState);
		initData();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		context = getActivity();
		app =  getActivity().getApplication();
		httpClient = MyHttpClient.getHttpClient();
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView  = initView(inflater);
		return rootView;
	}
	public View getRootView(){
		return rootView;
	}
	@Override
	public void onClick(View arg0) {
	
		
	}
	protected abstract View initView(LayoutInflater inflater);

	protected abstract void initData();

	protected abstract void processClick(View v);

}
