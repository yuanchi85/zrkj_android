package com.finddreams.baselib.view;


import com.finddreams.baselib.R;
import com.finddreams.baselib.utils.ActivityUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @Description:每个应用都需要用到的顶部导航条自定义，包含有返回，标题等五个控件
 * @author blin
 * @since 2015年12月14日
 */
public class TopBarView extends LinearLayout {

	private LinearLayout mTopBack;
	public TextView mTvBack;
	public TextView mTvTitle;
	public ImageView mIvRight;
	public TextView mTvRight;
	private Activity mActivity;

	public TopBarView(Context context) {
		super(context);
		LayoutInflater.from(context).inflate(R.layout.view_top_bar, this, true);
		mTopBack = (LinearLayout) this.findViewById(R.id.top_back_btn);
		mTvBack = (TextView) this.findViewById(R.id.top_back_tv);
		mTvTitle = (TextView) this.findViewById(R.id.top_title);
		mTvRight = (TextView) this.findViewById(R.id.top_right_tv);
		mIvRight = (ImageView) this.findViewById(R.id.top_right_btn);
		mTopBack.setOnClickListener(onClickListener);
	}

	public TopBarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.view_top_bar, this, true);
		mTopBack = (LinearLayout) this.findViewById(R.id.top_back_btn);
		mTvBack = (TextView) this.findViewById(R.id.top_back_tv);
		mTvTitle = (TextView) this.findViewById(R.id.top_title);
		mTvRight = (TextView) this.findViewById(R.id.top_right_tv);
		mIvRight = (ImageView) this.findViewById(R.id.top_right_btn);
		mTopBack.setOnClickListener(onClickListener);
	}

	public OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.top_back_btn:
				if (mActivity != null) {
					Dialog dialog = new AlertDialog.Builder(mActivity)
							.setTitle("您确认要退出吗?")
							.setIcon(android.R.drawable.ic_dialog_info)
							.setPositiveButton("确认",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											mActivity.finish();//调用
											dialog.cancel();// 取消弹出框
										}
									})
							.setNegativeButton("取消",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											dialog.cancel();// 取消弹出框
										}
									}).create();
					dialog.setCanceledOnTouchOutside(false);
					dialog.show();
				}
				break;
			}
		}
	};

	public void setActivity(Activity activity) {
		this.mActivity = activity;
	}

	public void setTitle(String title) {
		mTvTitle.setText(title);
	}

	public void setRightText(String text) {
		mTvRight.setText(text);
	}
}
