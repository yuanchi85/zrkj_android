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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zrkj.Doalog.BaseCmsProductDialog;
import zrkj.project.entity.CmsAssembly;
import zrkj.project.entity.CmsProduct;
import zrkj.project.toast.CommomDialog;
import zrkj.project.util.Result;

@ContentView(R.layout.podaitiaozheng_activity)
public class PoDaiTiaoZheng_Activity extends BaseActivity {
	@ViewInject(R.id.saomiaotiaoma)
	private EditText saomiaotiaoma;
	@ViewInject(R.id.chanxian)
	private EditText chanxian;
	@ViewInject(R.id.pihao)
	private EditText pihao;
	@ViewInject(R.id.chanpinbianma)
	private EditText chanpinbianma;
	@ViewInject(R.id.chanpingmingcheng)
	private EditText chanpingmingcheng;
	@ViewInject(R.id.guigexinhao)
	private EditText guigexinhao;
	@ViewInject(R.id.jiliangdanwei)
	private EditText jiliangdanwei;
	@ViewInject(R.id.tuopantiaoma)
	private EditText tuopantiaoma;
	@ViewInject(R.id.danqianshuliang)
	private EditText dangqianshuliang;
	@ViewInject(R.id.podaishuliang)
	private EditText podaishuliang;
	@ViewInject(R.id.beizhu)
	private EditText beizhu;
	@ViewInject(R.id.zhiliangbiaozhun)
	private EditText zhiliangbiaozhun;
	CmsAssembly cmsAssembly =  new CmsAssembly();
	@ViewInject(R.id.spinner2)
	private Spinner spinner2;
	private String warehouseCode = "";

	BaseCmsProductDialog commomDialog = null;
	@Override
	protected void initData() {

	}

	@Override
	protected void onScan(String barcodeStr) {
		if(barcodeStr.startsWith("T") && barcodeStr.endsWith("T")){
			tuopantiaoma.setText(trimFirstAndLastChar(barcodeStr,"T"));
			/*if(!isNotEmpty(tuopantiaoma.getText().toString())){
				showMsg("请先扫描产品条码");
			}
			queryAllProductionLine(saomiaotiaoma.getText().toString(),tuopantiaoma.getText().toString());*/
		}else if(!barcodeStr.startsWith("T") && !barcodeStr.endsWith("T")){
			if(!StringUtil.isNotEmpty(tuopantiaoma.getText().toString())){
				showMsg("请先扫描托盘条码");
				return;
			}
			saomiaotiaoma.setText(barcodeStr);
			queryCmsProductList(barcodeStr);
		}
	}
	/**
	 * 初始化所有控件
	 */
	@Override
	protected void initView() {
		topTitle.mTvTitle.setText("破袋调整");
		topTitle.mTvRight.setText("");
	}

	@OnTouch({R.id.login})
	private boolean login(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP) {
			switch (v.getId()) {
				case R.id.login:
					//分包
					if (beforeLogin()) {
						CommomDialog commomDialog = new CommomDialog(PoDaiTiaoZheng_Activity.this, R.style.dialog, "是否确定提交？", new CommomDialog.OnCloseListener() {
							@Override
							public void onClick(Dialog dialog1, boolean confirm) {
								if (confirm == false) {
									InsetCmsTornBags();
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
		if (podaishuliang.getText().toString().equals("")) {
			showCustomToast("请输入破袋数量");
			return false;
		}

		if (Integer.parseInt(podaishuliang.getText().toString())>Integer.parseInt(dangqianshuliang.getText().toString())) {
			showCustomToast("破袋数量不能大于当前数量");
			return false;
		}
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
					chanpinbianma.setText(map.get("productCode").toString());
					chanpingmingcheng.setText(map.get("productName").toString());
					guigexinhao.setText(map.get("specificationModel").toString());
					jiliangdanwei.setText(map.get("unit").toString());
					dangqianshuliang.setText(map.get("quantity").toString());
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

		if(posi==2){
			ActivityUtil.goToActivity(context, PoDaiTiaoZheng_Activity.class);
			finish();
			//调用ERP接口
			/*if(ajax.getResult()!=null && isNotEmpty(ajax.getResult())){
				createErpProductReceive(ajax.getResult().toString());
			}*/
			return;
		}
		if(posi==6){
			List<CmsProduct> list = gson.fromJson(ajax.getResult().toString(),new TypeToken<List<CmsProduct>>(){}.getType());
			if (list != null && list.size() == 1) {
				chanpinbianma.setText(list.get(0).getProductCode());
				chanpingmingcheng.setText(list.get(0).getProductName());
				guigexinhao.setText(list.get(0).getSpecificationModel());
				jiliangdanwei.setText(list.get(0).getUnit());
				//如果托盘条码不为空，则查询是否有可检批次
				if(isNotEmpty(tuopantiaoma.getText().toString())){
					queryAllProductionLine(chanpinbianma.getText().toString(),tuopantiaoma.getText().toString());
				}
				return;
			}

			commomDialog = new BaseCmsProductDialog(list,PoDaiTiaoZheng_Activity.this, R.style.dialog, "", new BaseCmsProductDialog.OnCloseListener() {
				@Override
				public void onClick(Dialog dialog1, boolean confirm) {
					if (confirm == false) {
						zhiliangbiaozhun.setText("");
						dangqianshuliang.setText("");
						podaishuliang.setText("");
						chanpinbianma.setText(commomDialog.getbianhao());
						chanpingmingcheng.setText(commomDialog.getmingcheng());
						guigexinhao.setText(commomDialog.getxinghao());
						jiliangdanwei.setText(commomDialog.getdanwei());

						//如果托盘条码不为空，则查询是否有可检批次
						if(isNotEmpty(tuopantiaoma.getText().toString())){
							queryAllProductionLine(chanpinbianma.getText().toString(),tuopantiaoma.getText().toString());
						}
					}
				}
			});
			commomDialog.setTitle("提示").show();
			return;
		}
	}

	private void queryAllProductionLine(String productCode,String palletCode) {
		Gson gson = new Gson();
		Map<String, Object> ms = new HashMap<String, Object>();
		ms.put("palletCode", palletCode);
		ms.put("barCode",productCode);
		ms.put("type","");
		ms.put("device", DeviceInfoUtil.getOnlyID(this));
		requestNoSuccess(gson.toJson(ms), "cms/cmsPalletProduct/cmsPalletProduct/QueryCmsPalletProductBatch", "正在查询...", 1);
	}


	private void InsetCmsTornBags() {
		Map<String, Object> map = (Map<String, Object>) spinner2.getSelectedItem();
		Gson gson = new Gson();
		Map<String, Object> ms = new HashMap<String, Object>();
		ms.put("assemblyId", cmsAssembly.getId());
		ms.put("remarks",beizhu.getText().toString());
		ms.put("batch",map.get("batch").toString());
		ms.put("palletCode",tuopantiaoma.getText().toString());
		ms.put("productCode",chanpinbianma.getText().toString());
		ms.put("quantity",podaishuliang.getText().toString());
		ms.put("warehouseCode",warehouseCode);
		ms.put("device", DeviceInfoUtil.getOnlyID(this));
		request(gson.toJson(ms), "cms/cmsTornBags/cmsTornBags/add", "正在保存...", 2);
	}

	private void createErpProductReceive(String ids) {
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
		Map<String, Object> ms = new HashMap<>();
		ms.put("device", DeviceInfoUtil.getOnlyID(this));
		requestByGet(gson.toJson(ms), "cms/openApi/createErpInventoryLoss"+"?ids="+ids, "正在生成...", 5);
	}

	//根据扫描的产品条码查询所有的产品信息
	private void queryCmsProductList(String barCode) {
		Gson gson = new Gson();
		Map<String, Object> ms = new HashMap<String, Object>();
		ms.put("barCode", barCode);
		ms.put("device", DeviceInfoUtil.getOnlyID(this));
		request(gson.toJson(ms), "cms/cmsProduct/cmsProduct/queryCmsProductList", "正在查询...", 6);
	}
}
