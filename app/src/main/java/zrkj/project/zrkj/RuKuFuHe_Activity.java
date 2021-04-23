package zrkj.project.zrkj;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
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
import zrkj.project.entity.CmsAssembly;
import zrkj.project.entity.CmsProduct;
import zrkj.project.toast.CommomDialog;
import zrkj.project.util.JsonUtil;
import zrkj.project.util.Result;

@ContentView(R.layout.rukufuhe_activity)
public class RuKuFuHe_Activity extends BaseActivity {
	@ViewInject(R.id.saomiaotiaoma)
	private EditText saomiaotiaoma;
	@ViewInject(R.id.chanxian)
	private EditText chanxian;
	@ViewInject(R.id.pihao)
	private EditText pihao;
	@ViewInject(R.id.fuhepihao)
	private EditText fuhepihao;
	@ViewInject(R.id.chanpinbianhao)
	private EditText chanpinbianhao;
	@ViewInject(R.id.chanpinmingcheng)
	private EditText chanpinmingcheng;
	@ViewInject(R.id.guigexinghao)
	private EditText guigexinghao;
	@ViewInject(R.id.jiliangdanwei)
	private EditText jiliangdanwei;
	@ViewInject(R.id.tuopantiaoma)
	private EditText tuopantiaoma;
	@ViewInject(R.id.rukushuliang)
	private EditText rukushuliang;
	@ViewInject(R.id.fuheshuliang)
	private EditText fuheshuliang;
	@ViewInject(R.id.zhiliangbiaozhun)
	private EditText zhiliangbiaozhun;
	@ViewInject(R.id.spinner2)
	private Spinner spinner2;
	CmsAssembly cmsAssembly =  new CmsAssembly();
	BaseCmsProductDialog commomDialog = null;
	String warehouseCode = "";
	@Override
	protected void initData() {

	}

	@Override
	protected void onScan(String barcodeStr) {
		if(barcodeStr.startsWith("T") && barcodeStr.endsWith("T")){
			if(!StringUtil.isNotEmpty(saomiaotiaoma.getText().toString())){
				showMsg("请先扫描产品条码");
				return;
			}
			tuopantiaoma.setText(trimFirstAndLastChar(barcodeStr,"T"));
			queryAllProductionLine(tuopantiaoma.getText().toString(),chanpinbianhao.getText().toString());
		}else if(!barcodeStr.startsWith("T") && !barcodeStr.endsWith("T")){
			saomiaotiaoma.setText(barcodeStr);
			/*if(!StringUtil.isNotEmpty(tuopantiaoma.getText().toString())){
				showMsg("请扫描托盘条码");
				return;
			}*/
			queryCmsProductList(barcodeStr);
//			queryAllProductionLine(tuopantiaoma.getText().toString(),saomiaotiaoma.getText().toString());
		}
	}

	/**
	 * 初始化所有控件
	 */
	@Override
	protected void initView() {
		topTitle.mTvTitle.setText("入库复核");
		topTitle.mTvRight.setText("");
	}


	@OnTouch({R.id.login})
	private boolean login(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP) {
			switch (v.getId()) {
				case R.id.login:
					//分包
					if (beforeLogin()) {
						CommomDialog commomDialog = new CommomDialog(RuKuFuHe_Activity.this, R.style.dialog, "是否确定提交？", new CommomDialog.OnCloseListener() {
							@Override
							public void onClick(Dialog dialog1, boolean confirm) {
								if (confirm == false) {
									InertCmsAssembly();
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
		if (saomiaotiaoma.getText().toString().equals("")) {
			showCustomToast("请扫描有效产品条码");
			return false;
		}
		if (tuopantiaoma.getText().toString().equals("")) {
			showCustomToast("请扫描托盘条码");
			return false;
		}
		/*if (fuhepihao.getText().toString().equals("")) {
			showCustomToast("请输入复核批号");
			return false;
		}*/

		if (!fuhepihao.getText().toString().equals("")) {
			Map<String, Object> map = (Map<String, Object>) spinner2.getSelectedItem();
			if(!isNotEmpty(map.get("batch"))){
			showCustomToast("请扫描条码");
				return false;
			}
			/*String a = map.get("batch").toString();
			String s = fuhepihao.getText().toString();
			if(!map.get("batch").toString().equals(fuhepihao.getText().toString())){
			showMsg("复核批次不相符");
			return false;
			}*/
		}
		if (fuheshuliang.getText().toString().equals("")) {
			showCustomToast("请输入复核数量");
			return false;
		}

		/*if (!fuheshuliang.getText().toString().equals("")) {
			int rusuhlaing = Integer.valueOf(rukushuliang.getText().toString());
			int fushuilang = Integer.valueOf(fuheshuliang.getText().toString());
			if(rusuhlaing<fushuilang){
			showCustomToast("复核数量不能大于入库数量");
			return false;
			}
		}*/

		return true;
	}

	@Override
	protected void requestCallBack(Result ajax, int posi) {
		Gson gson = new Gson();
		if (posi == 1) {
			if(ajax.getCode()==500){
				List<Map<String, Object>> list = new ArrayList<>();
				spinner2.setAdapter(new MyBaseAdapter<Map<String, Object>, ListView>(this.context, list) {
					@Override
					public View getView(int position, View convertView, ViewGroup parent) {
						ViewGroup layout = null;
						if (convertView == null) {
							layout = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.zdyspinner, parent, false);
						} else {
							layout = (ViewGroup)convertView;
						}
						Map<String, Object> mbean = this.list.get(position);
						((TextView)layout.findViewById(R.id.textView1)).setText(mbean.get("batch").toString());
						if (releativeOptionIndex == position) layout.setBackgroundColor(Color.YELLOW);
						return layout;
					}
				});
				return;
			}
			List<Map<String, Object>> list = gson.fromJson(ajax.getResult().toString(), List.class);
			spinner2.setAdapter(new MyBaseAdapter<Map<String, Object>, ListView>(this.context, list) {
				@Override
				public View getView(int position, View convertView, ViewGroup parent) {
					ViewGroup layout = null;
					if (convertView == null) {
						layout = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.zdyspinner, parent, false);
					} else {
						layout = (ViewGroup)convertView;
					}
					Map<String, Object> mbean = this.list.get(position);
					((TextView)layout.findViewById(R.id.textView1)).setText(mbean.get("batch").toString());
					if (releativeOptionIndex == position) layout.setBackgroundColor(Color.YELLOW);
					return layout;
				}
			});
			spinner2.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
				public void onItemSelected(AdapterView<?> arg0, View arg1,
										   int arg2, long arg3) {
					Map<String, Object> map = (Map<String, Object>) spinner2.getSelectedItem();
					zhiliangbiaozhun.setText(UpdateBatch(map.get("batch").toString()));
					chanpinbianhao.setText(map.get("productCode").toString());
					chanpinmingcheng.setText(map.get("productName").toString());
					guigexinghao.setText(map.get("specificationModel").toString());
					jiliangdanwei.setText(map.get("unit").toString());
					rukushuliang.setText(map.get("quantity").toString());
					warehouseCode = map.get("warehouseCode").toString();
				}

				public void onNothingSelected(AdapterView<?> arg0) {
					Map<String, Object> map = (Map<String, Object>) spinner2.getSelectedItem();
				}
			});

			if(ajax.getCode()==500){

			}
			return;
		}

		if(posi == 2){
			ActivityUtil.goToActivity(context, RuKuFuHe_Activity.class);
			finish();
		}

		if(posi==3){
			List<CmsProduct> list = gson.fromJson(ajax.getResult().toString(),new TypeToken<List<CmsProduct>>(){}.getType());
			if (list != null && list.size() == 1) {
				chanpinbianhao.setText(list.get(0).getProductCode());
				chanpinmingcheng.setText(list.get(0).getProductName());
				guigexinghao.setText(list.get(0).getSpecificationModel());
				jiliangdanwei.setText(list.get(0).getUnit());
				//如果托盘条码不为空，则查询是否有可检批次
				if(isNotEmpty(tuopantiaoma.getText().toString())){
					queryAllProductionLine(tuopantiaoma.getText().toString(),chanpinbianhao.getText().toString());
				}
				return;
			}
			commomDialog = new BaseCmsProductDialog(list,RuKuFuHe_Activity.this, R.style.dialog, "", new BaseCmsProductDialog.OnCloseListener() {
				@Override
				public void onClick(Dialog dialog1, boolean confirm) {
					if (confirm == false) {
						zhiliangbiaozhun.setText("");
						rukushuliang.setText("");
						fuheshuliang.setText("");
						chanpinbianhao.setText(commomDialog.getbianhao());
						chanpinmingcheng.setText(commomDialog.getmingcheng());
						guigexinghao.setText(commomDialog.getxinghao());
						jiliangdanwei.setText(commomDialog.getdanwei());

						//如果托盘条码不为空，则查询是否有可检批次
						if(isNotEmpty(tuopantiaoma.getText().toString())){
							queryAllProductionLine(tuopantiaoma.getText().toString(),chanpinbianhao.getText().toString());
						}
					}
				}
			});
			commomDialog.setTitle("提示").show();
			return;
		}
	}

	private void queryAllProductionLine(String palletCode,String productCode) {
		Gson gson = new Gson();
		Map<String, Object> ms = new HashMap<String, Object>();
		ms.put("palletCode", palletCode);
		ms.put("barCode",productCode);
		ms.put("device", DeviceInfoUtil.getOnlyID(this));
		requestNoSuccess(gson.toJson(ms), "cms/cmsPalletProduct/cmsPalletProduct/QueryCmsPalletProductBatch", "正在查询...", 1);
	}

	private void InertCmsAssembly() {
		Map<String, Object> map = (Map<String, Object>) spinner2.getSelectedItem();
		cmsAssembly.setReviewQuantity(new BigDecimal(fuheshuliang.getText().toString()));
		cmsAssembly.setReviewerBatch(fuhepihao.getText().toString());
		cmsAssembly.setProductCode(chanpinbianhao.getText().toString());
		cmsAssembly.setProductName(chanpinmingcheng.getText().toString());
		cmsAssembly.setSpecificationModel(guigexinghao.getText().toString());
		cmsAssembly.setUnit(jiliangdanwei.getText().toString());
		cmsAssembly.setBatch(map.get("batch").toString());
		cmsAssembly.setPalletCode(tuopantiaoma.getText().toString());
		cmsAssembly.setWarehouseCode(warehouseCode);
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
		Map<String, Object> ms = null;
		try {
			ms = JsonUtil.objectToMap(cmsAssembly);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ms.put("device", DeviceInfoUtil.getOnlyID(this));
		request(gson.toJson(ms), "cms/cmsAssembly/cmsAssembly/fHAddUpdate", "正在设置...", 2);
	}

	//根据扫描的产品条码查询所有的产品信息
	private void queryCmsProductList(String barCode) {
		Gson gson = new Gson();
		Map<String, Object> ms = new HashMap<String, Object>();
		ms.put("barCode", barCode);
		ms.put("device", DeviceInfoUtil.getOnlyID(this));
		request(gson.toJson(ms), "cms/cmsProduct/cmsProduct/queryCmsProductList", "正在查询...", 3);
	}



}
