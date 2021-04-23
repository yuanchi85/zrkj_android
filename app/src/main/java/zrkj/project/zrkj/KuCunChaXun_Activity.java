package zrkj.project.zrkj;

import android.app.Dialog;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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
import com.finddreams.baselib.utils.DeviceInfoUtil;
import com.google.gson.Gson;
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
import zrkj.project.entity.CmsPalletProduct;
import zrkj.project.entity.CmsProduct;
import zrkj.project.toast.CommomDialog;
import zrkj.project.util.JsonUtil;
import zrkj.project.util.Result;

@ContentView(R.layout.kucunchaxun_activity)
public class KuCunChaXun_Activity extends BaseActivity {
    @ViewInject(R.id.lv_list)
    ListView mList;
    DemoAdapter adapter;
    @ViewInject(R.id.tiaoma)
    private TextView tiaoma;
    @ViewInject(R.id.hangshu)
    private TextView hangshu;
    private List<CmsAssembly> productList = new ArrayList<>();
    List<Map<String, Object>> list = new ArrayList<>();
    BaseCmsProductDialog commomDialog = null;
    BaseCmsProductDialog commomDialogs = null;
    @ViewInject(R.id.zongshu)
    private TextView zongshu;
    @Override
    protected void initData() {

    }

    @Override
    protected void onScan(String barcodeStr) {
        if (barcodeStr.startsWith("T") && barcodeStr.endsWith("T")) {
            queryCmsPalletProduct(trimFirstAndLastChar(barcodeStr, "T"));
        }else{
            //根据产品编号查询所有产品信息
            queryCmsProductList(barcodeStr);
        }
    }

    /**
     * 初始化所有控件
     */
    @Override
    protected void initView() {
        topTitle.mTvTitle.setText("库存查询");
        topTitle.mTvRight.setText("");
        mList.setAdapter(adapter);
        adapter = new DemoAdapter();
        mList.setAdapter(adapter);
        qureyCmsLocation();
    }

    public class DemoAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return productList.size();
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
            final KuCunChaXun_Activity.DemoAdapter.ViewHolder holder;

            if (view == null) {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.pandianruku_tbaerl, null);
                holder = new KuCunChaXun_Activity.DemoAdapter.ViewHolder();
                holder.tv1 = view.findViewById(R.id.bianhao);
                holder.tv2 = view.findViewById(R.id.mingcheng);
                holder.tv3 = view.findViewById(R.id.xinghao);
                holder.tv4 = view.findViewById(R.id.danwei);
                holder.tv5 = view.findViewById(R.id.pihao);
                holder.tv6 = view.findViewById(R.id.cangkubianhao);
                holder.tv7 = view.findViewById(R.id.yuancangkubianhao);
                holder.tv8 = view.findViewById(R.id.shuliang);
                holder.okTable = view.findViewById(R.id.biaoge);
                view.setTag(holder);
            } else {
                holder = (KuCunChaXun_Activity.DemoAdapter.ViewHolder) view.getTag();
            }

            CmsAssembly cmsSaleDetailed = productList.get(i);
            holder.tv1.setText(cmsSaleDetailed.getProductCode());//产品编号
            holder.tv2.setText(cmsSaleDetailed.getProductName());//产品名称
            holder.tv3.setText(cmsSaleDetailed.getSpecificationModel());//规格型号
            holder.tv4.setText(cmsSaleDetailed.getUnit());//jiliangdnawei
            holder.tv5.setText(cmsSaleDetailed.getBatch());//托盘条码
            holder.tv6.setText(cmsSaleDetailed.getWarehouseName());//
            holder.tv7.setText(cmsSaleDetailed.getPalletCode());
            holder.tv8.setText(cmsSaleDetailed.getQuantity().stripTrailingZeros().toPlainString());
            return view;
        }

        class ViewHolder {
            TextView tv1, tv2, tv3, tv4, tv5, tv6, tv7, tv8;
            LinearLayout okTable;
        }
    }

    @Override
    protected void requestCallBack(Result ajax, int posi) {
        Gson gson = new Gson();
        if (posi == 5) {
            productList = new ArrayList<>();
             List<CmsAssembly> lists = new ArrayList<>();
            if (ajax.getResult() != null && !ajax.getResult().toString().equals("")) {
                lists = gson.fromJson(ajax.getResult().toString(), new TypeToken<List<CmsAssembly>>() {
                }.getType());
                BigDecimal big = new BigDecimal("0");
                for (CmsAssembly cms : lists) {
                    if(cms.getQuantity().intValue()!=0){
                        productList.add(cms);
                    }
                }
                for (CmsAssembly cms : productList) {
                    big = big.add(cms.getQuantity());
                    for (Map<String, Object> map : list) {
                        if (cms.getWarehouseCode().equals(map.get("warehouseCode").toString())) {
                            cms.setWarehouseName(map.get("warehouseName").toString());
                        }
                    }
                }
                zongshu.setText(big.stripTrailingZeros().toPlainString());
                hangshu.setText(String.valueOf(productList.size()));


            }
            adapter.notifyDataSetChanged();
            return;
        }

        if (posi == 2) {
            List<CmsProduct> list = gson.fromJson(ajax.getResult().toString(), new TypeToken<List<CmsProduct>>() {
            }.getType());
            if (list != null && list.size() == 1) {
                queryCmsPalletProductType(list.get(0).getProductCode());
            } else {
                commomDialogs = new BaseCmsProductDialog(list, KuCunChaXun_Activity.this, R.style.dialog, "是否确定提交？", new BaseCmsProductDialog.OnCloseListener() {
                    @Override
                    public void onClick(Dialog dialog1, boolean confirm) {
                        if (confirm == false) {
                            queryCmsPalletProductType(commomDialogs.getbianhao());
                        }
                    }
                });
                commomDialogs.setTitle("提示").show();
            }
            return;
        }

        if (posi == 3) {
            list = gson.fromJson(ajax.getResult().toString(), List.class);
        }

    }


    private void queryCmsPalletProduct(String palletCode) {
        Gson gson = new Gson();
        Map<String, Object> ms = new HashMap<String, Object>();
        ms.put("palletCode", palletCode);
        ms.put("device", DeviceInfoUtil.getOnlyID(this));
        request(gson.toJson(ms), "cms/cmsPalletProduct/cmsPalletProduct/queryCmsPalletProduct", "正在查询...", 5);
    }

    //根据产品编号查询所有产品库存信息
    private void queryCmsPalletProductType(String palletCode) {
        Gson gson = new Gson();
        Map<String, Object> ms = new HashMap<String, Object>();
        ms.put("productCode", palletCode);
        ms.put("device", DeviceInfoUtil.getOnlyID(this));
        request(gson.toJson(ms), "cms/cmsPalletProduct/cmsPalletProduct/queryCmsPalletProductType", "正在查询...", 5);
    }

    //根据扫描的产品条码查询所有的产品信息
    private void queryCmsProductList(String barCode) {
        Gson gson = new Gson();
        Map<String, Object> ms = new HashMap<String, Object>();
        ms.put("barCode", barCode);
        ms.put("device", DeviceInfoUtil.getOnlyID(this));
        request(gson.toJson(ms), "cms/cmsProduct/cmsProduct/queryCmsProductList", "正在查询...", 2);
    }

    private void qureyCmsLocation() {
        Gson gson = new Gson();
        Map<String, Object> ms = new HashMap<String, Object>();
        ms.put("device", DeviceInfoUtil.getOnlyID(this));
        request(gson.toJson(ms), "cms/cmsLocation/cmsLocation/qureyCmsLocation", "正在查询...", 3);
    }


}
