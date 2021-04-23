package com.finddreams.baselib.broadcastReceiver;

import com.finddreams.baselib.service.MQTTService;

import zrkj.demo.Demo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class InstalledReceiver extends BroadcastReceiver {
	static final String action_boot1="android.intent.action.BOOT_COMPLETED";
	static final String action_boot2="android.media.AUDIO_BECOMING_NOISY";
	 
    @Override
    public void onReceive(Context context, Intent intent) {
    	//应用开机启动
        if (intent.getAction().equals(action_boot1)){
            Intent ootStartIntent=new Intent(context,zrkj.demo.Demo.class);
            ootStartIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(ootStartIntent);
        }
        /* 服务开机自启动 */
        if (intent.getAction().equals(action_boot2)){
    		Intent service = new Intent(context, MQTTService.class);
    		context.startService(service);
        }
    }

}
