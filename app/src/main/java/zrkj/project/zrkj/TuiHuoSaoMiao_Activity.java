package zrkj.project.zrkj;

import android.app.Dialog;
import android.graphics.Color;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.finddreams.baselib.R;
import com.finddreams.baselib.base.BaseActivity;
import com.finddreams.baselib.base.MyBaseAdapter;
import com.finddreams.baselib.utils.ActivityUtil;
import com.finddreams.baselib.utils.DeviceInfoUtil;
import com.finddreams.baselib.utils.StringUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnTouch;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zrkj.Doalog.BaseCmsProductDialog;
import zrkj.Doalog.TuiHuoDialog;
import zrkj.project.entity.CmsAssembly;
import zrkj.project.entity.CmsCurrentProductsHis;
import zrkj.project.entity.CmsPalletProduct;
import zrkj.project.entity.CmsPartner;
import zrkj.project.entity.CmsProduct;
import zrkj.project.entity.CmsSaleDeliverySingle;
import zrkj.project.entity.CmsSaleReturnDetailed;
import zrkj.project.entity.ViewSaSaleDeliveryDetail;
import zrkj.project.toast.CommomDialog;
import zrkj.project.util.JsonUtil;
import zrkj.project.util.Result;

@ContentView(R.layout.tuihuosaomiao_activity)
public class TuiHuoSaoMiao_Activity extends BaseActivity {
	@ViewInject(R.id.lv_list2)
	ListView mList2;
	TuiHuoSaoMiao_Activity.DemoAdapter2 adapter2;
	@ViewInject(R.id.tuopantiaoma)
	EditText tuopantiaoma;
	@ViewInject(R.id.kehu)
	EditText kehu;
	@ViewInject(R.id.canpintiaoma)
	EditText canpintiaoma;
	@ViewInject(R.id.spinner1)
	private Spinner spinner2;

	@ViewInject(R.id.spinner2)
	private Spinner spinner1;

	List<CmsSaleDeliverySingle> lists = new ArrayList<>();
	BaseCmsProductDialog commomDia = null;
	private TuiHuoDialog commomDialog=null;

	public static String bianhao=null,mingcheng = null,guigexinghao=null,danwei=null;
	private CmsPartner cmsPartner = null;
	private List<CmsPartner> list = new ArrayList<>();

	@Override
	protected void initData() {

	}

	@Override
	protected void onScan(String barcodeStr) {
		if (barcodeStr.startsWith("T") && barcodeStr.endsWith("T")) {
			tuopantiaoma.setText(trimFirstAndLastChar(barcodeStr, "T"));
		}else if(barcodeStr.startsWith("K") && barcodeStr.endsWith("K")){
			queryCmsPartner(trimFirstAndLastChar(barcodeStr, "K"));
		}else {

			if (!StringUtil.isNotEmpty(tuopantiaoma.getText().toString())) {
				showMsg("请扫描托盘条码");
				return;
			}
			/*if (!StringUtil.isNotEmpty(kehu.getText().toString())) {
				showMsg("请扫描客户");
				return;
			}*/
			canpintiaoma.setText(barcodeStr);
			queryCmsProductList(barcodeStr);
		}
	}

	/**
	 * 初始化所有控件
	 */
	@Override
	protected void initView() {
		topTitle.mTvTitle.setText("退货扫描");
		topTitle.mTvRight.setText("");
		adapter2 = new TuiHuoSaoMiao_Activity.DemoAdapter2();
		mList2.setAdapter(adapter2);
		qureyCmsLocation();
		Onseel();
	}


	public void Onseel(){
		kehu.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				//当actionId == XX_SEND 或者 XX_DONE时都触发
				//或者event.getKeyCode == ENTER 且 event.getAction == ACTION_DOWN时也触发
				//注意，这是一定要判断event != null。因为在某些输入法上会返回null。
				if (actionId == EditorInfo.IME_ACTION_SEND
						|| actionId == EditorInfo.IME_ACTION_DONE
						|| (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {
					//处理事件
					queryCmsPartner(kehu.getText().toString());
				}
				return false;
			}
		});
	}

	public class DemoAdapter2 extends BaseAdapter {

		@Override
		public int getCount() {
			return lists.size();
		}

		@Override
		public Object getItem(int i) {
			return null;
		}

		@Override
		public long getItemId(int i) {
			return i;
		}


		@Override
		public View getView(final int i, View view, ViewGroup viewGroup) {
			final TuiHuoSaoMiao_Activity.DemoAdapter2.ViewHolder2 holder2;

			if (view == null) {
				view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.xiaohuo_tbaerl, null);
				holder2 = new TuiHuoSaoMiao_Activity.DemoAdapter2.ViewHolder2();
				holder2.tv1 = view.findViewById(R.id.bianhao);
				holder2.tv2 = view.findViewById(R.id.mingcheng);
				holder2.tv3 = view.findViewById(R.id.xinghao);
				holder2.tv4 = view.findViewById(R.id.danwei);
				holder2.tv5 = view.findViewById(R.id.pihao);
				holder2.tv6 = view.findViewById(R.id.cangkubianhao);
				holder2.tv7 = view.findViewById(R.id.tuopantiaoma);
				holder2.tv8 = view.findViewById(R.id.shuliang);
				holder2.okTable = view.findViewById(R.id.biaoge);
				view.setTag(holder2);
			} else {
				holder2 = (TuiHuoSaoMiao_Activity.DemoAdapter2.ViewHolder2) view.getTag();
			}
			CmsSaleDeliverySingle cms = lists.get(i);
			holder2.tv1.setText(cms.getProductCode());//产品编号
			holder2.tv2.setText(cms.getProductName());//产品名称
			holder2.tv3.setText(cms.getSpecificationModel());//规格型号
			holder2.tv4.setText(cms.getUnit());//jiliangdnawei
			holder2.tv5.setText(cms.getBatch());//托盘条码
			holder2.tv6.setText(cms.getWarehouseName());//仓库编号
			holder2.tv7.setText(cms.getPalletCode());//托盘条码
			holder2.tv8.setText(cms.getQuantity().stripTrailingZeros().toPlainString());

			return view;
		}

		class ViewHolder2 {
			TextView tv1, tv2, tv3, tv4, tv5, tv6, tv7, tv8;
			LinearLayout okTable;
		}
	}



	@Override
	protected void requestCallBack(Result ajax, int posi) {
		Gson gson = new Gson();
		if(posi==1){
			List<CmsProduct> list = gson.fromJson(ajax.getResult().toString(),new TypeToken<List<CmsProduct>>(){}.getType());
			if (list != null && list.size() == 1) {
				canpintiaoma.setText(list.get(0).getProductCode());
				bianhao = list.get(0).getProductCode();
				mingcheng = list.get(0).getProductName();
				guigexinghao = list.get(0).getSpecificationModel();
				danwei = list.get(0).getUnit();
				commomDialog = new TuiHuoDialog(TuiHuoSaoMiao_Activity.this, R.style.dialog, "是否确定提交？", new TuiHuoDialog.OnCloseListener() {
					@Override
					public void onClick(Dialog dialog1, boolean confirm) {
						if (confirm == false) {
							CmsSaleDeliverySingle cmsSaleDeliverySingle = new CmsSaleDeliverySingle();
							cmsSaleDeliverySingle.setProductCode(bianhao);
							cmsSaleDeliverySingle.setProductName(mingcheng);
							cmsSaleDeliverySingle.setBatch(commomDialog.getPiCi());
							cmsSaleDeliverySingle.setSpecificationModel(guigexinghao);
							cmsSaleDeliverySingle.setUnit(danwei);
							cmsSaleDeliverySingle.setQuantity(new BigDecimal(commomDialog.getshuliang()));
							cmsSaleDeliverySingle.setPalletCode(tuopantiaoma.getText().toString());
							Map<String, Object> map = (Map<String, Object>) spinner2.getSelectedItem();
							cmsSaleDeliverySingle.setWarehouseCode(map.get("warehouseCode").toString());
							cmsSaleDeliverySingle.setWarehouseName(map.get("warehouseName").toString());
							cmsSaleDeliverySingle.setPartnerCode(cmsPartner.getCode());
							lists.add(cmsSaleDeliverySingle);
							adapter2.notifyDataSetChanged();
						}
					}
				});
				commomDialog.setTitle("提示").show();
				return;
			}
			commomDia = new BaseCmsProductDialog(list,TuiHuoSaoMiao_Activity.this, R.style.dialog, "", new BaseCmsProductDialog.OnCloseListener() {
				@Override
				public void onClick(Dialog dialog1, boolean confirm) {
					if (confirm == false) {
						canpintiaoma.setText("");
						canpintiaoma.setText(commomDia.getbianhao());
						bianhao = commomDia.getbianhao();
						mingcheng = commomDia.getmingcheng();
						guigexinghao = commomDia.getxinghao();
						danwei = commomDia.getdanwei();
						commomDialog = new TuiHuoDialog(TuiHuoSaoMiao_Activity.this, R.style.dialog, "是否确定提交？", new TuiHuoDialog.OnCloseListener() {
							@Override
							public void onClick(Dialog dialog1, boolean confirm) {
								if (confirm == false) {
									CmsSaleDeliverySingle cmsSaleDeliverySingle = new CmsSaleDeliverySingle();
									cmsSaleDeliverySingle.setProductCode(bianhao);
									cmsSaleDeliverySingle.setProductName(mingcheng);
									cmsSaleDeliverySingle.setBatch(commomDialog.getPiCi());
									cmsSaleDeliverySingle.setSpecificationModel(guigexinghao);
									cmsSaleDeliverySingle.setUnit(danwei);
									cmsSaleDeliverySingle.setQuantity(new BigDecimal(commomDialog.getshuliang()));
									cmsSaleDeliverySingle.setPalletCode(tuopantiaoma.getText().toString());
									Map<String, Object> map = (Map<String, Object>) spinner2.getSelectedItem();
									cmsSaleDeliverySingle.setWarehouseCode(map.get("warehouseCode").toString());
									cmsSaleDeliverySingle.setWarehouseName(map.get("warehouseName").toString());
									cmsSaleDeliverySingle.setPartnerCode(cmsPartner.getCode());
									lists.add(cmsSaleDeliverySingle);
									adapter2.notifyDataSetChanged();
								}
							}
						});
						commomDialog.setTitle("提示").show();
					}
				}
			});
			commomDia.setTitle("提示").show();
			return;
		}
		if (posi == 3) {
			List<Map<String, Object>> list = gson.fromJson(ajax.getResult().toString(), List.class);
			spinner2.setAdapter(new MyBaseAdapter<Map<String, Object>, ListView>(this.context, list) {
				@Override
				public View getView(int position, View convertView, ViewGroup parent) {
					ViewGroup layout = null;
					if (convertView == null) {
						layout = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.zdyspinner, parent, false);
					} else {
						layout = (ViewGroup) convertView;
					}
					Map<String, Object> mbean = this.list.get(position);
					((TextView) layout.findViewById(R.id.textView1)).setText(mbean.get("warehouseName").toString());
					if (releativeOptionIndex == position) layout.setBackgroundColor(Color.YELLOW);
					return layout;
				}
			});
			spinner2.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
				public void onItemSelected(AdapterView<?> arg0, View arg1,
										   int arg2, long arg3) {
					Map<String, Object> map = (Map<String, Object>) spinner2.getSelectedItem();
				}

				public void onNothingSelected(AdapterView<?> arg0) {
					Map<String, Object> map = (Map<String, Object>) spinner2.getSelectedItem();
				}
			});
			if (ajax.getCode() == 500) {

			}
			return;
		}

		if(posi==2){
			list = gson.fromJson(ajax.getResult().toString(),new TypeToken<List<CmsPartner>>(){}.getType());
			spinner1.setAdapter(new MyBaseAdapter<CmsPartner, ListView>(this.context, list) {
				@Override
				public View getView(int position, View convertView, ViewGroup parent) {
					ViewGroup layout = null;
					if (convertView == null) {
						layout = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.zdyspinner, parent, false);
					} else {
						layout = (ViewGroup)convertView;
					}
					CmsPartner mbean = this.list.get(position);
					((TextView)layout.findViewById(R.id.textView1)).setText(mbean.getName());
					if (releativeOptionIndex == position) layout.setBackgroundColor(Color.YELLOW);
					return layout;
				}
			});
			spinner1.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
				public void onItemSelected(AdapterView<?> arg0, View arg1,
										   int arg2, long arg3) {
					CmsPartner map = (CmsPartner)spinner1.getSelectedItem();
					cmsPartner = map;
				}

				public void onNothingSelected(AdapterView<?> arg0) {
					Map<String, Object> map = (Map<String, Object>) spinner1.getSelectedItem();
				}
			});


			return;
		}

		if(posi==4){
			ActivityUtil.goToActivity(context, TuiHuoSaoMiao_Activity.class);
			finish();
		}
	}

	@OnTouch({R.id.login })
	private boolean login(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP) {
			switch (v.getId()) {
				case R.id.login:
					if (beforeLogin()) {
						CommomDialog commomDialog = new CommomDialog(TuiHuoSaoMiao_Activity.this, R.style.dialog, "是否确认提交？", new CommomDialog.OnCloseListener() {
							@Override
							public void onClick(Dialog dialog1, boolean confirm) {
								if (confirm == false) {
									AddCmsSaleDeliverySingle();
								}
							}
						});
						commomDialog.setTitle("警告").show();
					}
					break;
				default:
					break;
			}
		}
		return true;
	}

	private boolean beforeLogin() {
		if (tuopantiaoma.getText().toString().equals("")) {
			showCustomToast("请扫描托盘条码");
			return false;
		}
		if (lists==null || lists.size()==0) {
			showCustomToast("至少扫描一个退货产品");
			return false;
		}
		return true;
	}


	//根据扫描的产品条码查询所有的产品信息
	private void queryCmsProductList(String barCode) {
		Gson gson = new Gson();
		Map<String, Object> ms = new HashMap<String, Object>();
		ms.put("barCode", barCode);
		ms.put("device", DeviceInfoUtil.getOnlyID(this));
		request(gson.toJson(ms), "cms/cmsProduct/cmsProduct/queryCmsProductList", "正在查询...", 1);
	}
	//获取仓库信息
	private void qureyCmsLocation() {
		Gson gson = new Gson();
		Map<String, Object> ms = new HashMap<String, Object>();
		ms.put("device", DeviceInfoUtil.getOnlyID(this));
		request(gson.toJson(ms), "cms/cmsLocation/cmsLocation/qureyCmsLocation", "正在查询...", 3);
	}


	//根据扫描的客户查询客户信息
	private void queryCmsPartner(String barCode) {
		Gson gson = new Gson();
		Map<String, Object> ms = new HashMap<String, Object>();
		ms.put("name", barCode);
		ms.put("device", DeviceInfoUtil.getOnlyID(this));
		request(gson.toJson(ms), "cms/cmsPartner/cmsPartner/queryCmsPartner", "正在查询...", 2);
	}


	private void AddCmsSaleDeliverySingle() {
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
		Map<String, Object> ms = new HashMap<String, Object>();
		ms.put("code", lists);
		ms.put("device", DeviceInfoUtil.getOnlyID(this));
		request(gson.toJson(ms), "cms/cmsSaleDeliverySingle/cmsSaleDeliverySingle/AddCmsSaleDeliverySingle", "正在设置...", 4);
	}


}
