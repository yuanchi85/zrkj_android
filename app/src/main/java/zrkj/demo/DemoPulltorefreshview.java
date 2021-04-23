package zrkj.demo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.pulltorefreshview.PullToRefreshBase;
import com.android.pulltorefreshview.PullToRefreshBase.OnRefreshListener;
import com.android.pulltorefreshview.PullToRefreshListView;
import com.finddreams.baselib.R;
import com.finddreams.baselib.base.BaseActivity;
import com.finddreams.baselib.base.MyBaseAdapter;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnItemLongClick;

import zrkj.project.util.Result;

public class DemoPulltorefreshview extends BaseActivity implements
		OnItemClickListener, OnItemLongClickListener {
	@ViewInject(R.id.pull_refresh_list)
	private PullToRefreshListView mPullRefreshListView;
	private ListView actualListView;
	final List<Map<String, String>> lm = new ArrayList<Map<String, String>>();
	MyBaseAdapter<Map<String, String>, ListView> adapter;

	@Override
	protected void initView() {
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.demo_pulltorefreshview);
		ViewUtils.inject(this);
	}


	private List<Map<String, String>> initListData() {
		for (int i = 0; i < 50; i++) {
			Map<String, String> m = new HashMap<String, String>();
			m.put("a", "a" + i);
			m.put("b", "b" + i);
			m.put("c", "c" + i);
			m.put("d", "d" + i);
			m.put("e", "e" + i);
			m.put("f", "f" + i);
			m.put("g", "g" + i);
			m.put("h", "h" + i);
			m.put("i", "i" + i);
			m.put("j", "j" + i);
			m.put("k", "k" + i);
			lm.add(m);
		}
		return lm;
	}

	private void initListView() {
		// /**
		// * 实现 接口 OnRefreshListener2<ListView> 以便与监听 滚动条到顶部和到底部
		// */
		mPullRefreshListView
				.setOnRefreshListener(new OnRefreshListener<ListView>() {

					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						for (int i = 1; i <= 5; i++) {
							Map<String, String> m = new HashMap<String, String>();
							m.put("a", "down" + i);
							m.put("b", "down" + i);
							m.put("c", "down" + i);
							m.put("d", "down" + i);
							m.put("e", "down" + i);
							m.put("f", "down" + i);
							m.put("g", "down" + i);
							m.put("h", "down" + i);
							m.put("i", "down" + i);
							m.put("j", "down" + i);
							m.put("k", "down" + i);
							lm.add(m);
						}
						new GetDataTask().execute();
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						for (int i = 1; i <= 5; i++) {
							Map<String, String> m = new HashMap<String, String>();
							m.put("a", "up" + i);
							m.put("b", "up" + i);
							m.put("c", "up" + i);
							m.put("d", "up" + i);
							m.put("e", "up" + i);
							m.put("f", "up" + i);
							m.put("g", "up" + i);
							m.put("h", "up" + i);
							m.put("i", "up" + i);
							m.put("j", "up" + i);
							m.put("k", "up" + i);
							lm.add(m);
						}
						new GetDataTask().execute();
					}
				});

		// 获取控件并注册
		actualListView = mPullRefreshListView.getRefreshableView();
		registerForContextMenu(actualListView);

		adapter = new MyBaseAdapter<Map<String, String>, ListView>(context, lm) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				ViewGroup layout = null;
				/**
				 * 进行ListView 的优化
				 */
				if (convertView == null) {
					layout = (ViewGroup) LayoutInflater.from(context).inflate(
							R.layout.demo_listview_item_layout, parent, false);
				} else {
					layout = (ViewGroup) convertView;
				}
				Map<String, String> mbean = lm.get(position);
				((TextView) layout.findViewById(R.id.xh))
						.setText(position + "");
				((TextView) layout.findViewById(R.id.a))
						.setText(mbean.get("a"));
				((TextView) layout.findViewById(R.id.b))
						.setText(mbean.get("b"));
				((TextView) layout.findViewById(R.id.c))
						.setText(mbean.get("c"));
				((TextView) layout.findViewById(R.id.d))
						.setText(mbean.get("d"));
				((TextView) layout.findViewById(R.id.e))
						.setText(mbean.get("e"));
				((TextView) layout.findViewById(R.id.f))
						.setText(mbean.get("f"));
				((TextView) layout.findViewById(R.id.g))
						.setText(mbean.get("g"));
				((TextView) layout.findViewById(R.id.h))
						.setText(mbean.get("h"));
				((TextView) layout.findViewById(R.id.i))
						.setText(mbean.get("i"));
				((TextView) layout.findViewById(R.id.j))
						.setText(mbean.get("j"));
				((TextView) layout.findViewById(R.id.k))
						.setText(mbean.get("k"));
				int[] colors = { Color.parseColor("#79B9B1"),
						Color.parseColor("#90D9FF") };// RGB颜色
				layout.setBackgroundColor(colors[position % 2]);// 每隔item之间颜色不同
				if (releativeOptionIndex == position)
					layout.setBackgroundColor(Color.RED);
				return layout;
			}
		};
		adapter.notifyDataSetChanged();
		actualListView.setAdapter(adapter);
	}

	@Override
	protected void initData() {
		initListData();
		initListView();
		actualListView.setOnItemClickListener(this);
		actualListView.setOnItemLongClickListener(this);
	}

	// 模拟网络加载数据的 异步请求类
	private class GetDataTask extends AsyncTask<Void, Void, String[]> {

		// 子线程请求数据
		@Override
		protected String[] doInBackground(Void... params) {
			// Simulates a background job.
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
			}
			return null;
		}

		// 主线程更新UI
		@Override
		protected void onPostExecute(String[] result) {
			adapter.notifyDataSetChanged();
			// Call onRefreshComplete when the list has been refreshed.
			mPullRefreshListView.onPullDownRefreshComplete(); // onRefreshComplete();
			mPullRefreshListView.onPullUpRefreshComplete(); // onRefreshComplete();
			// if (pullup) {
			// mPullRefreshListView.onPullDownRefreshComplete(); //
			// onRefreshComplete();
			// pullup=false;
			// }
			// if (pulldown) {
			// mPullRefreshListView.onPullUpRefreshComplete(); //
			// onRefreshComplete();
			// pulldown=false;
			// }
			super.onPostExecute(result);
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// 操作颜色标记
		// 操作颜色标记
		if (adapter.releativeOptionIndex != -1
				&& actualListView.getChildAt(adapter.releativeOptionIndex
						- actualListView.getFirstVisiblePosition()) != null)
			actualListView.getChildAt(
					adapter.releativeOptionIndex
							- actualListView.getFirstVisiblePosition())
					.setBackgroundColor(adapter.releativeOptionColor);
		adapter.releativeOptionIndex = arg2;
		adapter.releativeOptionColor = ((ColorDrawable) (arg1.getBackground()))
				.getColor();
		arg1.setBackgroundColor(Color.RED);
		// TextView c = (TextView) arg1.findViewById(R.id.tvPopUpItem);
		actualListView.setSelection(arg2);
		new AlertDialog.Builder(context).setItems(
				new String[] { "操作1", "操作2" },
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// 操作具体事件
						dialog.dismiss();
					}
				}).show();
		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// 操作颜色标记
		if (adapter.releativeOptionIndex != -1
				&& actualListView.getChildAt(adapter.releativeOptionIndex
						- actualListView.getFirstVisiblePosition()) != null)
			actualListView.getChildAt(
					adapter.releativeOptionIndex
							- actualListView.getFirstVisiblePosition())
					.setBackgroundColor(adapter.releativeOptionColor);
		adapter.releativeOptionIndex = arg2;
		adapter.releativeOptionColor = ((ColorDrawable) (arg1.getBackground()))
				.getColor();
		arg1.setBackgroundColor(Color.RED);
		// TextView c = (TextView) arg1.findViewById(R.id.tvPopUpItem);
		actualListView.setSelection(arg2);
	}


	@Override
	protected void onScan(String barcodeStr) {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void requestCallBack(Result ajax, int posi) {

	}
}
