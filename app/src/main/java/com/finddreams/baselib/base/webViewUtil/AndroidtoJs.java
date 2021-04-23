package com.finddreams.baselib.base.webViewUtil;

import zrkj.demo.DemoPulltorefreshview;

import com.finddreams.baselib.activity.CaptureActivity;
import com.finddreams.baselib.utils.ActivityUtil;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.webkit.JavascriptInterface;

public class AndroidtoJs extends Object {
    // 定义JS需要调用的方法
    // 被JS调用的方法必须加入@JavascriptInterface注解
    @JavascriptInterface
    public void hello(String msg) {
    	Log.v("cjj", "msg"+msg);
    }
}