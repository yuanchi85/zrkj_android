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

@ContentView(R.layout.buluruku_activity)
public class BuLuRuKu_Activity extends BaseActivity {
    @ViewInject(R.id.lv_list)
    ListView mList;
    DemoAdapter adapter;
    @ViewInject(R.id.spinner1)
    private Spinner m_spiModel;
    @ViewInject(R.id.chanpinbianma)
    private TextView chanpinbianma;
    @ViewInject(R.id.chanpingmingcheng)
    private TextView chanpingmingcheng;
    @ViewInject(R.id.guigexinghao)
    private TextView guigexinghao;
    @ViewInject(R.id.jiliangdanwei)
    private TextView jiliangdanwei;
    @ViewInject(R.id.dangqianpihao)
    private EditText dangqianpihao;
    @ViewInject(R.id.jihuashsuliang)
    private EditText jihuashsuliang;
    @ViewInject(R.id.tuopantiaoma)
    private EditText tuopantiaoma;
    @ViewInject(R.id.zhiliangbiaozun)
    private TextView zhiliangbiaozun;
    @ViewInject(R.id.zongshu)
    private TextView zongshu;
    private List<CmsAssembly> productList = new ArrayList<>();
    List<Map<String, Object>> list = new ArrayList<>();
    BaseCmsProductDialog commomDialog = null;

    @Override
    protected void initData() {

    }

    @Override
    protected void onScan(String barcodeStr) {
        if (barcodeStr.startsWith("T") && barcodeStr.endsWith("T")) {
            tuopantiaoma.setText(trimFirstAndLastChar(barcodeStr, "T"));
            //查询该托盘下所有的
            queryCmsPalletProduct(trimFirstAndLastChar(barcodeStr, "T"));
        } else if (!barcodeStr.startsWith("T") && !barcodeStr.endsWith("T")) {
            chanpinbianma.setText(barcodeStr);
//
            queryCmsProductList(barcodeStr);
        }
    }

    /**
     * 初始化所有控件
     */
    @Override
    protected void initView() {
        topTitle.mTvTitle.setText("补录入库");
        topTitle.mTvRight.setText("");
        qureyCmsLocation();
        mList.setAdapter(adapter);
        adapter = new DemoAdapter();
        mList.setAdapter(adapter);
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
            final BuLuRuKu_Activity.DemoAdapter.ViewHolder holder;

            if (view == null) {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.pandianruku_tbaerl, null);
                holder = new BuLuRuKu_Activity.DemoAdapter.ViewHolder();
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
                holder = (BuLuRuKu_Activity.DemoAdapter.ViewHolder) view.getTag();
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
        if (posi == 1) {
            CmsProduct cmsProduct = gson.fromJson(ajax.getResult().toString(), CmsProduct.class);
            chanpinbianma.setText(cmsProduct.getProductCode());
            chanpingmingcheng.setText(cmsProduct.getProductName());
            guigexinghao.setText(cmsProduct.getSpecificationModel());
            jiliangdanwei.setText(cmsProduct.getUnit());

            return;
        }
        if (posi == 3) {
            list = gson.fromJson(ajax.getResult().toString(), List.class);
            m_spiModel.setAdapter(new MyBaseAdapter<Map<String, Object>, ListView>(this.context, list) {
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
            m_spiModel.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int arg2, long arg3) {
                    Map<String, Object> map = (Map<String, Object>) m_spiModel.getSelectedItem();
                }

                public void onNothingSelected(AdapterView<?> arg0) {
                    Map<String, Object> map = (Map<String, Object>) m_spiModel.getSelectedItem();
                }
            });
            if (ajax.getCode() == 500) {

            }
            return;
        }

        if (posi == 5) {
            productList = new ArrayList<>();
            if (ajax.getResult() != null && !ajax.getResult().toString().equals("")) {
                productList = gson.fromJson(ajax.getResult().toString(), new TypeToken<List<CmsAssembly>>() {
                }.getType());
                BigDecimal big = new BigDecimal("0");
                for (CmsAssembly cms : productList) {
                    big = big.add(cms.getQuantity());
                    for (Map<String, Object> map : list) {
                        if (cms.getWarehouseCode().equals(map.get("warehouseCode").toString())) {
                            cms.setWarehouseName(map.get("warehouseName").toString());
                        }
                    }
                }
                zongshu.setText(big.stripTrailingZeros().toPlainString());
            }
            adapter.notifyDataSetChanged();
            return;
        }

        if (posi == 4) {
            queryCmsPalletProduct(tuopantiaoma.getText().toString());
            return;
        }

        if (posi == 2) {
            List<CmsProduct> list = gson.fromJson(ajax.getResult().toString(), new TypeToken<List<CmsProduct>>() {
            }.getType());
            if (list != null && list.size() == 1) {
                chanpinbianma.setText(list.get(0).getProductCode());
                chanpingmingcheng.setText(list.get(0).getProductName());
                guigexinghao.setText(list.get(0).getSpecificationModel());
                jiliangdanwei.setText(list.get(0).getUnit());
            } else {
                commomDialog = new BaseCmsProductDialog(list, BuLuRuKu_Activity.this, R.style.dialog, "是否确定提交？", new BaseCmsProductDialog.OnCloseListener() {
                    @Override
                    public void onClick(Dialog dialog1, boolean confirm) {
                        if (confirm == false) {
                            chanpinbianma.setText(commomDialog.getbianhao());
                            chanpingmingcheng.setText(commomDialog.getmingcheng());
                            guigexinghao.setText(commomDialog.getxinghao());
                            jiliangdanwei.setText(commomDialog.getdanwei());
                        }
                    }
                });
                commomDialog.setTitle("提示").show();
            }

            return;
        }
    }

    private boolean beforeLogin() {
        if (chanpinbianma.getText().toString().equals("")) {
            showCustomToast("请扫描有效产品条码");
            return false;
        }
        if (dangqianpihao.getText().toString().equals("")) {
            showCustomToast("请输入批号");
            return false;
        }
        if (!dangqianpihao.getText().toString().equals("")) {
            if(dangqianpihao.getText().toString().indexOf("X")==-1){
                showCustomToast("请输入大写X");
                return false;
            }
        }
        if (jihuashsuliang.getText().toString().equals("")) {
            showCustomToast("请输入计划数量");
            return false;
        }
        if (tuopantiaoma.getText().toString().equals("")) {
            showCustomToast("请扫描托盘条码");
            return false;
        }
        return true;
    }

    @OnTouch({R.id.kaigong, R.id.wangong})
    private boolean login(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            switch (v.getId()) {
                case R.id.kaigong:
                    if (beforeLogin()) {
                        CommomDialog commomDialog = new CommomDialog(BuLuRuKu_Activity.this, R.style.dialog, "是否确定入库？", new CommomDialog.OnCloseListener() {
                            @Override
                            public void onClick(Dialog dialog1, boolean confirm) {
                                if (confirm == false) {
                                    zhiliangbiaozun.setText(UpdateBatch(dangqianpihao.getText().toString()));
                                    Map<String, Object> map = (Map<String, Object>) m_spiModel.getSelectedItem();
                                    CmsPalletProduct cmsPalletProduct = new CmsPalletProduct();
                                    cmsPalletProduct.setProductCode(chanpinbianma.getText().toString());
                                    cmsPalletProduct.setPalletCode(tuopantiaoma.getText().toString());
                                    cmsPalletProduct.setWarehouseCode(map.get("warehouseCode").toString());
                                    cmsPalletProduct.setQuantity(new BigDecimal(jihuashsuliang.getText().toString()));
                                    cmsPalletProduct.setBatch(dangqianpihao.getText().toString());
                                    addCmsPalletProduct(cmsPalletProduct);
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

    private void qureyCmsLocation() {
        Gson gson = new Gson();
        Map<String, Object> ms = new HashMap<String, Object>();
        ms.put("device", DeviceInfoUtil.getOnlyID(this));
        request(gson.toJson(ms), "cms/cmsLocation/cmsLocation/qureyCmsLocation", "正在查询...", 3);
    }

    private void queryCmsProduct(String barCode) {
        Gson gson = new Gson();
        Map<String, Object> ms = new HashMap<String, Object>();
        ms.put("barCode", barCode);
        ms.put("device", DeviceInfoUtil.getOnlyID(this));
        request(gson.toJson(ms), "cms/cmsProduct/cmsProduct/queryCmsProduct", "正在查询...", 1);
    }

    //添加汇总表
    private void addCmsPalletProduct(CmsPalletProduct cmsCurrentProducts) {
        Gson gson = new Gson();
        Map<String, Object> ms = null;
        try {
            ms = JsonUtil.objectToMap(cmsCurrentProducts);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ms.put("device", DeviceInfoUtil.getOnlyID(this));
        request(gson.toJson(ms), "cms/cmsPalletProduct/cmsPalletProduct/addCmsPalletProductBl", "正在设置...", 4);
    }


    private void queryCmsPalletProduct(String palletCode) {
        Gson gson = new Gson();
        Map<String, Object> ms = new HashMap<String, Object>();
        ms.put("palletCode", palletCode);
        ms.put("device", DeviceInfoUtil.getOnlyID(this));
        request(gson.toJson(ms), "cms/cmsPalletProduct/cmsPalletProduct/queryCmsPalletProduct", "正在查询...", 5);
    }

    //根据扫描的产品条码查询所有的产品信息
    private void queryCmsProductList(String barCode) {
        Gson gson = new Gson();
        Map<String, Object> ms = new HashMap<String, Object>();
        ms.put("barCode", barCode);
        ms.put("device", DeviceInfoUtil.getOnlyID(this));
        request(gson.toJson(ms), "cms/cmsProduct/cmsProduct/queryCmsProductList", "正在查询...", 2);
    }

}
