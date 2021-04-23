package zrkj.project.zrkj;

import android.app.Dialog;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.finddreams.baselib.R;
import com.finddreams.baselib.base.BaseActivity;
import com.finddreams.baselib.utils.DeviceInfoUtil;
import com.finddreams.baselib.utils.StringUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zrkj.Doalog.GongDanLingLiaoXiuGaiDialog;
import zrkj.Doalog.TuiHuoDialog;
import zrkj.project.entity.CmsAssembly;
import zrkj.project.entity.CmsProduct;
import zrkj.project.entity.CmsSaleDetailed;
import zrkj.project.entity.CmsSaleReturnDetailed;
import zrkj.project.entity.ViewSaSaleDeliveryDetail;
import zrkj.project.util.Result;

@ContentView(R.layout.tuihuo_activity)
public class TuiHuo_Activity extends BaseActivity {
	@ViewInject(R.id.lv_list)
	ListView mList;
	TuiHuo_Activity.DemoAdapter adapter;
	@ViewInject(R.id.lv_list2)
	ListView mList2;
	TuiHuo_Activity.DemoAdapter2 adapter2;
	@ViewInject(R.id.xiaohuodanhao)
	EditText xiaohuodanhao;
	@ViewInject(R.id.tuopantiaoma)
	EditText tuopantiaoma;
	@ViewInject(R.id.canpintiaoma)
	EditText canpintiaoma;
	List<ViewSaSaleDeliveryDetail> viewEntity = new ArrayList<>();
	List<CmsSaleReturnDetailed> CmsSaleDetailedEntity = new ArrayList<>();

	public static CmsProduct cmsAssembly = null ;

	private TuiHuoDialog commomDialog=null;

	private int ab=-1;
	@Override
	protected void initData() {

	}

	@Override
	protected void onScan(String barcodeStr) {
		if (barcodeStr.startsWith("H") && barcodeStr.endsWith("H")) {
			xiaohuodanhao.setText(trimFirstAndLastChar(barcodeStr, "H"));
			querySaSaleDeliveryTh(xiaohuodanhao.getText().toString());
			return;
		}

		if (barcodeStr.startsWith("T") && barcodeStr.endsWith("T")) {
			tuopantiaoma.setText(trimFirstAndLastChar(barcodeStr, "T"));
			if (!StringUtil.isNotEmpty(canpintiaoma.getText().toString())) {
				showMsg("请扫描产品条码");
				return;
			}
			queryCmsProductTh(canpintiaoma.getText().toString());
		} else {
			canpintiaoma.setText(barcodeStr);
			if (!StringUtil.isNotEmpty(tuopantiaoma.getText().toString())) {
				showMsg("请扫描托盘条码");
				return;
			}
			queryCmsProductTh(barcodeStr);
		}
	}

	/**
	 * 初始化所有控件
	 */
	@Override
	protected void initView() {
		topTitle.mTvTitle.setText("退货扫描");
		topTitle.mTvRight.setText("");
		adapter = new TuiHuo_Activity.DemoAdapter();
		mList.setAdapter(adapter);
		adapter2 = new TuiHuo_Activity.DemoAdapter2();
		mList2.setAdapter(adapter2);
	}

	/**
	 * 启动构造器，显示列表，初始化列表，响应事件
	 */
	public class DemoAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return viewEntity.size();
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
			final TuiHuo_Activity.DemoAdapter.ViewHolder holder;
			if (view == null) {
				view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.xiaohuo3_tbaerl, null);
				holder = new TuiHuo_Activity.DemoAdapter.ViewHolder();
				holder.tv1 = view.findViewById(R.id.bianhao);
				holder.tv2 = view.findViewById(R.id.mingcheng);
				holder.tv3 = view.findViewById(R.id.xinghao);
				holder.tv4 = view.findViewById(R.id.danwei);
				holder.tv5 = view.findViewById(R.id.xiaohuoshuliang);
				holder.tv6 = view.findViewById(R.id.shijishuliang);
				holder.okTable = view.findViewById(R.id.biaoge);
				view.setTag(holder);
			} else {
				holder = (TuiHuo_Activity.DemoAdapter.ViewHolder) view.getTag();
			}
			ViewSaSaleDeliveryDetail tail = viewEntity.get(i);
			holder.tv1.setText(tail.getProductCode());//产品编号
			holder.tv2.setText(tail.getProductName());//产品名称
			holder.tv3.setText(tail.getSpecificationModel());//规格型号
			holder.tv4.setText(tail.getUnit());//jiliangdnawei
			holder.tv5.setText(tail.getQuantity().stripTrailingZeros().toPlainString());//shuliang
			holder.tv6.setText(tail.getActualQuantity().stripTrailingZeros().toPlainString());//实际数量
			if(ab==i){
				view.setBackgroundColor(Color.YELLOW);
			}
			return view;
		}

		class ViewHolder {
			TextView tv1, tv2, tv3, tv4, tv5, tv6;
			LinearLayout okTable;
		}
	}

	public class DemoAdapter2 extends BaseAdapter {

		@Override
		public int getCount() {
			return CmsSaleDetailedEntity.size();
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
			final TuiHuo_Activity.DemoAdapter2.ViewHolder2 holder2;

			if (view == null) {
				view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.xiaohuo_tbaerl, null);
				holder2 = new TuiHuo_Activity.DemoAdapter2.ViewHolder2();
				holder2.tv1 = view.findViewById(R.id.bianhao);
				holder2.tv2 = view.findViewById(R.id.mingcheng);
				holder2.tv3 = view.findViewById(R.id.xinghao);
				holder2.tv4 = view.findViewById(R.id.danwei);
				holder2.tv5 = view.findViewById(R.id.pihao);
				holder2.tv6 = view.findViewById(R.id.cangkubianhao);
				holder2.tv7 = view.findViewById(R.id.yuancangkubianhao);
				holder2.tv8 = view.findViewById(R.id.shuliang);
				holder2.okTable = view.findViewById(R.id.biaoge);
				view.setTag(holder2);
			} else {
				holder2 = (TuiHuo_Activity.DemoAdapter2.ViewHolder2) view.getTag();
			}
			CmsSaleReturnDetailed cmsSaleDetailed = CmsSaleDetailedEntity.get(i);
			holder2.tv1.setText(cmsSaleDetailed.getProductCode());//产品编号
			holder2.tv2.setText(cmsSaleDetailed.getProductName());//产品名称
			holder2.tv3.setText(cmsSaleDetailed.getSpecificationModel());//规格型号
			holder2.tv4.setText(cmsSaleDetailed.getUnit());//jiliangdnawei
			holder2.tv5.setText(cmsSaleDetailed.getBatch());//托盘条码
			holder2.tv6.setText(cmsSaleDetailed.getWarehouseCode());//数量
			holder2.tv7.setText(cmsSaleDetailed.getPalletCode());
			holder2.tv8.setText(cmsSaleDetailed.getQuantity().stripTrailingZeros().toPlainString());

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
		if (posi == 1) {
			viewEntity = gson.fromJson(ajax.getResult().toString(),new TypeToken<List<ViewSaSaleDeliveryDetail>>(){}.getType());
			adapter.notifyDataSetChanged();
			queryCmsSaleReturnDetailed();
			return;
		}

		if (posi == 2) {
			if (ajax.getResult() != null && !ajax.getResult().toString().equals("")) {
				CmsSaleDetailedEntity = gson.fromJson(ajax.getResult().toString(),new TypeToken<List<CmsSaleReturnDetailed>>(){}.getType());
				adapter2.notifyDataSetChanged();
				if(ab>=0){
					mList.setSelection(ab);//定位到某行
				}
				return;
			}
		}

		if (posi == 3) {
			querySaSaleDeliveryTh(xiaohuodanhao.getText().toString());
			return;
		}

		if (posi == 4) {
			cmsAssembly = gson.fromJson(ajax.getResult().toString(), CmsProduct.class);
			int i=0;
			for(ViewSaSaleDeliveryDetail cm : viewEntity){
				if(cmsAssembly.getProductCode().equals(cm.getProductCode())){
					ab = i;
					break;
				}
				i++;
			}

			if(ab<0){
				showMsg("该产品不是该销货单，不能出库");
				return;
			}
			ViewSaSaleDeliveryDetail tail = viewEntity.get(ab);
			BigDecimal quantity = tail.getQuantity().abs().setScale(0, BigDecimal.ROUND_HALF_UP);
			BigDecimal ActualQuantity = tail.getActualQuantity();
			BigDecimal zongshu = quantity.subtract(ActualQuantity);
			if(zongshu.intValue()<=0) {
				showMsg("该产品已退完");
				return;
			}
			cmsAssembly.setZongshu(zongshu);
			commomDialog = new TuiHuoDialog(TuiHuo_Activity.this, R.style.dialog, "是否确定提交？", new TuiHuoDialog.OnCloseListener() {
				@Override
				public void onClick(Dialog dialog1, boolean confirm) {
					if (confirm == false) {
						String quantity = commomDialog.getshuliang();
						String pici  = commomDialog.getPiCi();
						InsetCmsSaleDetailed(quantity,pici);
					}
				}
			});
			commomDialog.setTitle("提示").show();

		}
	}

//退货扫描，根据单号获取产品信息
	private void querySaSaleDeliveryTh(String barCode) {
		Gson gson = new Gson();
		Map<String, Object> ms = new HashMap<String, Object>();
		ms.put("code", barCode);
		ms.put("device", DeviceInfoUtil.getOnlyID(this));
		request(gson.toJson(ms), "cms/saSaleDelivery/saSaleDelivery/querySaSaleDeliveryTh", "正在查询...", 1);
	}

	//根据退货单号获取以退货的产品信息
	private void queryCmsSaleReturnDetailed() {
		Gson gson = new Gson();
		Map<String, Object> ms = new HashMap<String, Object>();
		ms.put("code",xiaohuodanhao.getText().toString());
		ms.put("device", DeviceInfoUtil.getOnlyID(this));
		request(gson.toJson(ms), "cms/cmsSaleReturnDetailed/cmsSaleReturnDetailed/queryCmsSaleReturnDetailed", "正在查询...", 2);
	}

//	退货扫描。添加数据
	private void InsetCmsSaleDetailed(String quantity,String batch ) {
		Gson gson = new Gson();
		Map<String, Object> ms = new HashMap<String, Object>();
		ms.put("palletCode", tuopantiaoma.getText().toString());//托盘条码
		ms.put("productCode", cmsAssembly.getProductCode());//产品编号
		ms.put("productName", cmsAssembly.getProductName());
		ms.put("specificationModel", cmsAssembly.getSpecificationModel());
		ms.put("unit", cmsAssembly.getUnit());
		ms.put("batch", batch);
		/*ms.put("warehouseCode", cmsAssembly.getWarehouseCode());*///仓库字段现在不确定，现场在修改
		ms.put("barCode", canpintiaoma.getText().toString());
		ms.put("quantity", quantity);
		ms.put("salesNumber", xiaohuodanhao.getText().toString());
		ms.put("device", DeviceInfoUtil.getOnlyID(this));
		request(gson.toJson(ms), "cms/cmsSaleReturnDetailed/cmsSaleReturnDetailed/InsetCmsSaleReturnDetailed", "正在查询...", 3);
	}

	//退货扫描根据扫描的产品条码获取产品信息
	private void queryCmsProductTh(String barCode) {
		Gson gson = new Gson();
		Map<String, Object> ms = new HashMap<String, Object>();
		ms.put("barCode", barCode);
		ms.put("device", DeviceInfoUtil.getOnlyID(this));
		request(gson.toJson(ms), "cms/cmsProduct/cmsProduct/queryCmsProductTh", "正在查询...", 4);
	}


}
