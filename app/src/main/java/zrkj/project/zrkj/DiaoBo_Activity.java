package zrkj.project.zrkj;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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
import com.finddreams.baselib.utils.ActivityUtil;
import com.finddreams.baselib.utils.DeviceInfoUtil;
import com.finddreams.baselib.utils.StringUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnTouch;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zrkj.Doalog.BaseCmsProductDialog;
import zrkj.project.entity.CmsAssembly;
import zrkj.project.entity.CmsLocation;
import zrkj.project.entity.CmsProduct;
import zrkj.project.entity.CmsSaleDetailed;
import zrkj.project.entity.CmsTransferOrderDetailed;
import zrkj.project.toast.CommomDialog;
import zrkj.project.util.Constant;
import zrkj.project.util.JsonUtil;
import zrkj.project.util.Result;

@ContentView(R.layout.diaobo_activity)
public class DiaoBo_Activity extends BaseActivity {
    @ViewInject(R.id.saomiaotiaoma)
    private EditText saomiaotiaoma;
    @ViewInject(R.id.chanpingmingcheng)
    private EditText chanpingmingcheng;
    @ViewInject(R.id.chanpinbianma)
    private EditText chanpinbianma;
    @ViewInject(R.id.guigexinhao)
    private EditText guigexinhao;
    @ViewInject(R.id.jiliangdanwei)
    private EditText jiliangdanwei;
    @ViewInject(R.id.tuopantiaoma)
    private EditText tuopantiaoma;
    @ViewInject(R.id.cangku)
    private EditText cangku;
    @ViewInject(R.id.cangkumingcheng)
    private EditText cangkumingcheng;
    @ViewInject(R.id.rukushuliang)
    private EditText rukushuliang;
    @ViewInject(R.id.canxian)
    private EditText canxian;
    @ViewInject(R.id.xintuopantiaoma)
    private EditText xintuopantiaoma;
    @ViewInject(R.id.beizhu)
    private EditText beizhu;
    @ViewInject(R.id.zhiliangbiaozhun)
    private EditText zhiliangbiaozhun;
    @ViewInject(R.id.spinner2)
    private Spinner spinner1;
    @ViewInject(R.id.spinner1)
    private Spinner spinner2;
    CmsAssembly cmsAssembly = new CmsAssembly();
    BaseCmsProductDialog commomDialog = null;

    @Override
    protected void initData() {

    }

    @Override
    protected void onScan(String barcodeStr) {
        if (barcodeStr.startsWith("T") && barcodeStr.endsWith("T")) {
            if (isNotEmpty(tuopantiaoma.getText().toString())) {
                xintuopantiaoma.setText(trimFirstAndLastChar(barcodeStr, "T"));
                return;
            } else {
                tuopantiaoma.setText(trimFirstAndLastChar(barcodeStr, "T"));
            }

            if (!StringUtil.isNotEmpty(saomiaotiaoma.getText().toString())) {
                showMsg("请扫描产品条码");
                return;
            }
            queryAllProductionLine(chanpinbianma.getText().toString(), tuopantiaoma.getText().toString());
        } else if (!barcodeStr.startsWith("T") && !barcodeStr.endsWith("T")) {
            saomiaotiaoma.setText(barcodeStr);
            queryCmsProductList(barcodeStr);
        }
    }

    /**
     * 初始化所有控件
     */
    @Override
    protected void initView() {
        topTitle.mTvTitle.setText("调拨");
        topTitle.mTvRight.setText("");
        qureyCmsLocation();
    }

    @OnTouch({R.id.login})
    private boolean login(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            switch (v.getId()) {
                case R.id.login:
                    //分包
                    if (beforeLogin()) {
                        CommomDialog commomDialog = new CommomDialog(DiaoBo_Activity.this, R.style.dialog, "是否确定提交？", new CommomDialog.OnCloseListener() {
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
        if (chanpinbianma.getText().toString().equals("")) {
            showCustomToast("请扫描有效产品条码");
            return false;
        }
        if (tuopantiaoma.getText().toString().equals("")) {
            showCustomToast("请扫描托盘条码");
            return false;
        }
        return true;
    }


    //批次选择
    @Override
    protected void requestCallBack(Result ajax, int posi) {
        Gson gson = new Gson();
        if (posi == 1) {
            if(ajax.getCode()==500){
                List<Map<String, Object>> list = new ArrayList<>();
                spinner1.setAdapter(new MyBaseAdapter<Map<String, Object>, ListView>(this.context, list) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        ViewGroup layout = null;
                        if (convertView == null) {
                            layout = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.zdyspinner, parent, false);
                        } else {
                            layout = (ViewGroup) convertView;
                        }
                        Map<String, Object> mbean = this.list.get(position);
                        ((TextView) layout.findViewById(R.id.textView1)).setText(mbean.get("batch").toString());
                        if (releativeOptionIndex == position) layout.setBackgroundColor(Color.YELLOW);
                        return layout;
                    }
                });
                return;
            }
            List<Map<String, Object>> list = gson.fromJson(ajax.getResult().toString(), List.class);
            spinner1.setAdapter(new MyBaseAdapter<Map<String, Object>, ListView>(this.context, list) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    ViewGroup layout = null;
                    if (convertView == null) {
                        layout = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.zdyspinner, parent, false);
                    } else {
                        layout = (ViewGroup) convertView;
                    }
                    Map<String, Object> mbean = this.list.get(position);
                    ((TextView) layout.findViewById(R.id.textView1)).setText(mbean.get("batch").toString());
                    if (releativeOptionIndex == position) layout.setBackgroundColor(Color.YELLOW);
                    return layout;
                }
            });
            spinner1.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int arg2, long arg3) {
                    Map<String, Object> map = (Map<String, Object>) spinner1.getSelectedItem();
                    zhiliangbiaozhun.setText(UpdateBatch(map.get("batch").toString()));
                    chanpinbianma.setText("");
                    chanpingmingcheng.setText("");
                    guigexinhao.setText("");
                    jiliangdanwei.setText("");
                    cangku.setText("");
                    cangkumingcheng.setText("");
                    rukushuliang.setText("");

                    if (map.get("productCode") != null)
                        chanpinbianma.setText(map.get("productCode").toString());
                    if (map.get("productName") != null)
                        chanpingmingcheng.setText(map.get("productName").toString());
                    if (map.get("specificationModel") != null)
                        guigexinhao.setText(map.get("specificationModel").toString());
                    if (map.get("unit") != null)
                        jiliangdanwei.setText(map.get("unit").toString());
                    if (map.get("qualifiedQuantity") != null)
                        rukushuliang.setText(map.get("qualifiedQuantity").toString());
                    if (map.get("warehouseCode") != null)
                        cangku.setText(map.get("warehouseCode").toString());
                    if (map.get("warehouseName") != null)
                        cangkumingcheng.setText(map.get("warehouseName").toString());

//                    QureyQuantity(map.get("productCode").toString(), map.get("batch").toString());
                }

                public void onNothingSelected(AdapterView<?> arg0) {
                    Map<String, Object> map = (Map<String, Object>) spinner1.getSelectedItem();
                }
            });
            if (ajax.getCode() == 500) {
                tuopantiaoma.setText("");
            }
            return;
        }

        if (posi == 2) {
            cmsAssembly = gson.fromJson(ajax.getResult().toString(), CmsAssembly.class);
            chanpingmingcheng.setText(cmsAssembly.getProductName());
            chanpinbianma.setText(cmsAssembly.getProductCode());
            guigexinhao.setText(cmsAssembly.getSpecificationModel());
            jiliangdanwei.setText(cmsAssembly.getUnit());
            cangku.setText(cmsAssembly.getWarehouseCode());
            rukushuliang.setText(cmsAssembly.getQuantity().stripTrailingZeros().toPlainString());
            canxian.setText(cmsAssembly.getProductionLineCode());
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
        if (posi == 6) {
            List<CmsProduct> list = gson.fromJson(ajax.getResult().toString(), new TypeToken<List<CmsProduct>>() {
            }.getType());
            if (list != null && list.size() == 1) {
                chanpinbianma.setText(list.get(0).getProductCode());
                chanpingmingcheng.setText(list.get(0).getProductName());
                guigexinhao.setText(list.get(0).getSpecificationModel());
                jiliangdanwei.setText(list.get(0).getUnit());
                //如果托盘条码不为空，则查询是否有可检批次
                if (isNotEmpty(tuopantiaoma.getText().toString())) {
                    queryAllProductionLine(chanpinbianma.getText().toString(), tuopantiaoma.getText().toString());
                }
                return;
            }
            commomDialog = new BaseCmsProductDialog(list, DiaoBo_Activity.this, R.style.dialog, "", new BaseCmsProductDialog.OnCloseListener() {
                @Override
                public void onClick(Dialog dialog1, boolean confirm) {
                    if (confirm == false) {
                        zhiliangbiaozhun.setText("");
                        rukushuliang.setText("");
                        chanpinbianma.setText(commomDialog.getbianhao());
                        chanpingmingcheng.setText(commomDialog.getmingcheng());
                        guigexinhao.setText(commomDialog.getxinghao());
                        jiliangdanwei.setText(commomDialog.getdanwei());

                        //如果托盘条码不为空，则查询是否有可检批次
                        if (isNotEmpty(tuopantiaoma.getText().toString())) {
                            queryAllProductionLine(chanpinbianma.getText().toString(), tuopantiaoma.getText().toString());
                        }
                    }
                }
            });
            commomDialog.setTitle("提示").show();
            return;
        }

        if(posi==4){
            ActivityUtil.goToActivity(context, DiaoBo_Activity.class);
            finish();
            return;
        }


    }

    private void queryAllProductionLine(String productCode, String palletCode) {
        Gson gson = new Gson();
        Map<String, Object> ms = new HashMap<String, Object>();
        ms.put("productCode", productCode);
        ms.put("palletCode", palletCode);
        ms.put("type", "质检");
        ms.put("device", DeviceInfoUtil.getOnlyID(this));
        requestNoSuccess(gson.toJson(ms), "cms/cmsAssembly/cmsAssembly/queryCmsAssemblyDb", "正在查询...", 1);
    }

    //根据选择的批次和产品编号查询数量信息
    private void QureyQuantity(String productCode, String batch) {
        Gson gson = new Gson();
        Map<String, Object> ms = new HashMap<String, Object>();
        ms.put("productCode", productCode);
        ms.put("batch", batch);
        ms.put("palletCode", tuopantiaoma.getText().toString());
        ms.put("device", DeviceInfoUtil.getOnlyID(this));
        request(gson.toJson(ms), "cms/cmsAssembly/cmsAssembly/qureyquantity", "正在查询...", 2);
    }

    private void qureyCmsLocation() {
        Gson gson = new Gson();
        Map<String, Object> ms = new HashMap<String, Object>();
        ms.put("device", DeviceInfoUtil.getOnlyID(this));
        request(gson.toJson(ms), "cms/cmsLocation/cmsLocation/qureyCmsLocation", "正在查询...", 3);
    }


    private void InertCmsAssembly() {
        Map<String, Object> map = (Map<String, Object>) spinner1.getSelectedItem();
        Map<String, Object> map2 = (Map<String, Object>) spinner2.getSelectedItem();
        CmsTransferOrderDetailed cmsLocation = new CmsTransferOrderDetailed();
        cmsLocation.setProductCode(chanpinbianma.getText().toString());
        cmsLocation.setProductName(chanpingmingcheng.getText().toString());
        cmsLocation.setSpecificationModel(guigexinhao.getText().toString());
        cmsLocation.setUnit(jiliangdanwei.getText().toString());
        cmsLocation.setBarCode(saomiaotiaoma.getText().toString());
        cmsLocation.setBatch(map.get("batch").toString());
        cmsLocation.setQuantity(new BigDecimal(rukushuliang.getText().toString()));
        cmsLocation.setOriginalWarehouse(cangku.getText().toString());
        cmsLocation.setOriginalLocation(cmsAssembly.getLocationCode());
        cmsLocation.setOriginalPalletCode(tuopantiaoma.getText().toString());
        cmsLocation.setNewWarehouse(map2.get("warehouseCode").toString());
        if(!isNotEmpty(xintuopantiaoma.getText().toString())){
            cmsLocation.setNewPalletCode(tuopantiaoma.getText().toString());
        }else{
            cmsLocation.setNewPalletCode(xintuopantiaoma.getText().toString());
        }

        cmsLocation.setRemarks(beizhu.getText().toString());
        Gson gson = new Gson();
        Map<String, Object> ms = null;
        try {
            ms = JsonUtil.objectToMap(cmsLocation);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ms.put("device", DeviceInfoUtil.getOnlyID(this));
        request(gson.toJson(ms), "cms/cmsTransferOrderDetailed/cmsTransferOrderDetailed/addCmsTransferOrderDetailed", "正在设置...", 4);
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
