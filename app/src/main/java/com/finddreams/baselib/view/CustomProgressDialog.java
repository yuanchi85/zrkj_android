package com.finddreams.baselib.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.KeyEvent;
import android.widget.TextView;

import com.finddreams.baselib.R;

public class CustomProgressDialog extends Dialog {
	private boolean clickMiss = false;
	public TextView tvMsg = null;

	public void setclickMiss() {
		clickMiss = true;
	}

	public CustomProgressDialog(Context context, String strMessage) {
		this(context, R.style.CustomProgressDialog, strMessage);
	}

	public CustomProgressDialog(Context context, int theme, String strMessage) {
		super(context, theme);
		this.setContentView(R.layout.progressdialog);
		this.getWindow().getAttributes().gravity = Gravity.CENTER;
		this.setCanceledOnTouchOutside(false);
		tvMsg = (TextView) this.findViewById(R.id.id_tv_loadingmsg);
		if (tvMsg != null) {
			tvMsg.setText(strMessage);
		}
	}

	public void setTvMsgAndShow(String strMessage) {
		tvMsg.setText(strMessage);
		this.show();
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// 点击就会消失
		if (clickMiss && !hasFocus) {
			dismiss();
		}
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		}
		if (keyCode == KeyEvent.KEYCODE_CALL) {
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		}
		if (keyCode == KeyEvent.KEYCODE_CALL) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
