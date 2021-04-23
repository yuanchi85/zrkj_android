package zrkj.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import com.finddreams.baselib.R;
import com.finddreams.baselib.base.BaseFragment;
import com.finddreams.baselib.base.MyBaseAdapter;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class DemoFragment2 extends BaseFragment {
	private ListView list;
	final List<Map<String, String>> lm = new ArrayList<Map<String, String>>();
	MyBaseAdapter<Map<String, String>, ListView> adapter;

	@Override
	protected View initView(LayoutInflater inflater) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.demo_fragment2, null);
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		ViewUtils.inject(rootView);
		list = (ListView) rootView.findViewById(R.id.list);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// 操作颜色标记
				// 操作颜色标记
				if (adapter.releativeOptionIndex != -1&&list.getChildAt(adapter.releativeOptionIndex-list.getFirstVisiblePosition())!=null)
					list.getChildAt(adapter.releativeOptionIndex-list.getFirstVisiblePosition()).setBackgroundColor(
							adapter.releativeOptionColor);
				adapter.releativeOptionIndex=arg2;
				adapter.releativeOptionColor=((ColorDrawable)(arg1.getBackground())).getColor();
				arg1.setBackgroundColor(Color.RED);
				//TextView c = (TextView) arg1.findViewById(R.id.tvPopUpItem);
				list.setSelection(arg2);
			}
		});
		list.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// 操作颜色标记
				// 操作颜色标记
				if (adapter.releativeOptionIndex != -1&&list.getChildAt(adapter.releativeOptionIndex-list.getFirstVisiblePosition())!=null)
					list.getChildAt(adapter.releativeOptionIndex-list.getFirstVisiblePosition()).setBackgroundColor(
							adapter.releativeOptionColor);
				adapter.releativeOptionIndex=arg2;
				adapter.releativeOptionColor=((ColorDrawable)(arg1.getBackground())).getColor();
				arg1.setBackgroundColor(Color.RED);
				//TextView c = (TextView) arg1.findViewById(R.id.tvPopUpItem);
				list.setSelection(arg2);
				new AlertDialog.Builder(context).setItems(
						new String[] { "操作1", "操作2" },
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// 操作具体事件
								dialog.dismiss();
							}
						}).show();
				return false;
			}
		});
		initListData();
		initListView();
	}

	@Override
	protected void processClick(View v) {
		// TODO Auto-generated method stub

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

	@SuppressWarnings("unchecked")
	private void initListView() {
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
				if(releativeOptionIndex==position)
					layout.setBackgroundColor(Color.RED);
				return layout;
			}
		};
		adapter.notifyDataSetChanged();
		list.setAdapter(adapter);
		// fixListViewHeight(list);
	}

	public void fixListViewHeight(ListView listView) { // 计算listview高度
		// 如果没有设置数据适配器，则ListView没有子项，返回。
		ListAdapter listAdapter = listView.getAdapter();
		int totalHeight = 0;
		if (listAdapter == null) {
			return;
		}
		for (int index = 0, len = listAdapter.getCount(); index < len; index++) {
			View listViewItem = listAdapter.getView(index, null, listView);
			// 计算子项View 的宽高
			listViewItem.measure(0, 0);
			// 计算所有子项的高度和
			totalHeight += listViewItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		// listView.getDividerHeight()获取子项间分隔符的高度
		// params.height设置ListView完全显示需要的高度
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}
}
