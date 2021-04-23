package zrkj.demo;

import com.finddreams.baselib.base.BaseWebActivity;

import android.support.v4.app.FragmentActivity;

public class DemoWebPrint extends BaseWebActivity{

	@Override
	protected void initView() {
		url=this.getIntent().getExtras().getString("url").toString();
		super.initView();
	}
}
