package com.finddreams.baselib.utils;

import java.util.List;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;

/**
 * @deprecated 一个管理apk的工具类
 * @author blin
 * @since 2015年12月14日
 */
public class InstallManager {
    private Context mContext = null;

    public InstallManager(Context context)
    {
        mContext = context;
    }

    public int getInstalledVersion(String packageName, boolean getSysPackages) {
        List<PackageInfo> packs = mContext.getPackageManager().getInstalledPackages(0);
        int curVersion = -1;
        for (int i = 0; i < packs.size(); i++) {
            PackageInfo p = packs.get(i);

            if (p.packageName.equals(packageName)) {
                curVersion = p.versionCode;
                break;
            }
        }

        return curVersion;
    }

    public boolean appIsStart(String packageName) {
        boolean ret = false;
        ActivityManager mActivityManager = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> mServiceList = mActivityManager
                .getRunningServices(1000);
        for (int i = 0; i < mServiceList.size(); i++) {
            if (packageName.equals(mServiceList.get(i).service.getPackageName())) {
                ret = true;
                break;
            }
        }

        return ret;
    }
}