package zrkj.project.util;

import android.content.Context;

import com.finddreams.baselib.utils.DeviceInfoUtil;
import com.finddreams.baselib.utils.SharePrefUtil;
import com.lidroid.xutils.http.RequestParams;

public class RequestParamsUtil {
	public static RequestParams getRequestParams(Context context) {
		RequestParams rp = new RequestParams();
		rp.addBodyParameter("userName",
				SharePrefUtil.getString(context, "userName", "admin"));
		rp.addBodyParameter("device", DeviceInfoUtil.getOnlyID(context));
		return rp;
	}
}
