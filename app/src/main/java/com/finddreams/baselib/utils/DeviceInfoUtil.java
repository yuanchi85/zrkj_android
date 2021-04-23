package com.finddreams.baselib.utils;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * @Description:手机设备的相关信息
 * @author blin
 * @since 2015年12月14日
 */
public class DeviceInfoUtil {
	public static String getDeviceUniqID(Context context) {
		android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String unique_id;
		unique_id = tm.getDeviceId();
		if (TextUtils.isEmpty(unique_id)) {
			unique_id = android.os.Build.SERIAL;
		}
		return unique_id;
	}

	public static String getOnlyID(Context context) {
		TelephonyManager TelephonyMgr = (TelephonyManager) context
				.getSystemService(context.TELEPHONY_SERVICE);
		String m_szDevIDShort = "35"
				+ // we make this look like a valid IMEI
				Build.BOARD.length() % 10 + Build.BRAND.length() % 10
				+ Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10
				+ Build.DISPLAY.length() % 10 + Build.HOST.length() % 10
				+ Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10
				+ Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10
				+ Build.TAGS.length() % 10 + Build.TYPE.length() % 10
				+ Build.USER.length() % 10; // 13 digits
		String m_szImei = TelephonyMgr.getDeviceId();
		String m_szAndroidID = Secure.getString(context.getContentResolver(),
				Secure.ANDROID_ID);
		String m_szWLANMAC = getMacWifi(context);
		// BluetoothAdapter m_BluetoothAdapter = null; // Local Bluetooth
		// adapter
		// m_BluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		String m_szBTMAC = getMacBluetooth(context);
		String m_szLongID = m_szImei + m_szDevIDShort + m_szAndroidID
				+ m_szWLANMAC + m_szBTMAC;
		// compute md5
		MessageDigest m = null;
		try {
			m = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		m.update(m_szLongID.getBytes(), 0, m_szLongID.length());
		// get md5 bytes
		byte p_md5Data[] = m.digest();
		// create a hex string
		String m_szUniqueID = new String();
		for (int i = 0; i < p_md5Data.length; i++) {
			int b = (0xFF & p_md5Data[i]);
			// if it is a single digit, make sure it have 0 in front (proper
			// padding)
			if (b <= 0xF)
				m_szUniqueID += "0";
			// add number to string
			m_szUniqueID += Integer.toHexString(b);
		} // hex string to uppercase
		m_szUniqueID = m_szUniqueID.toUpperCase();
		return m_szUniqueID;
	}

	public static String getImei(Context context) {
		try {
			TelephonyManager tm = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			String id = tm.getDeviceId();
			if (id != null) {
				return tm.getDeviceId();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "A000002CBD64E7";
	}

	public static String getMacWifi(Context context) {
		WifiManager wifi = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		String s = info.getMacAddress();
		if (s != null) {
			return s;
		}
		return "";
	}

	public static String getMacBluetooth(Context context) {
		BluetoothAdapter bAdapt = BluetoothAdapter.getDefaultAdapter();
		if (bAdapt != null) {
			if (bAdapt.isEnabled()) {
				return bAdapt.getAddress();
			}
		}
		return "";
	}

	public static String getImsi(Context context) {
		try {
			TelephonyManager tm = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			String imsi = tm.getSubscriberId();
			if (imsi == null) {
				imsi = "";
			}
			return imsi;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public static float getDensity(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		dm = context.getApplicationContext().getResources().getDisplayMetrics();
		return dm.density;
	}

	public static String getModel() {
		return android.os.Build.MODEL;
	}

	@SuppressWarnings("deprecation")
	public static int getScreenWidth(Context context) {
		WindowManager mWindowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		return mWindowManager.getDefaultDisplay().getWidth();
	}

	@SuppressWarnings("deprecation")
	public static int getScreenHeight(Context context) {
		WindowManager mWindowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		return mWindowManager.getDefaultDisplay().getHeight();
	}

	public static boolean isSDAva() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)
				|| Environment.getExternalStorageDirectory().exists()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获得SD卡总大小
	 * 
	 * @return
	 */
	public String getSDTotalSize(Context context) {
		File path = Environment.getExternalStorageDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long totalBlocks = stat.getBlockCount();
		return Formatter.formatFileSize(context, blockSize * totalBlocks);
	}

	/**
	 * 获得sd卡剩余容量，即可用大小
	 * 
	 * @return
	 */
	public String getSDAvailableSize(Context context) {
		File path = Environment.getExternalStorageDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		return Formatter.formatFileSize(context, blockSize * availableBlocks);
	}

	/**
	 * 获得机身内容总大小
	 * 
	 * @return
	 */
	public String getRomTotalSize(Context context) {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long totalBlocks = stat.getBlockCount();
		return Formatter.formatFileSize(context, blockSize * totalBlocks);
	}

	/**
	 * 获得机身可用内存
	 * 
	 * @return
	 */
	public String getRomAvailableSize(Context context) {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		return Formatter.formatFileSize(context, blockSize * availableBlocks);
	}
}
