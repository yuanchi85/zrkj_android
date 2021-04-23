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

import com.alibaba.fastjson.JSON;
import com.finddreams.baselib.R;
import com.finddreams.baselib.base.BaseActivity;
import com.finddreams.baselib.base.MyBaseAdapter;
import com.finddreams.baselib.utils.DeviceInfoUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnTouch;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zrkj.Doalog.BaseCmsProductDialog;
import zrkj.project.entity.CmsAssembly;
import zrkj.project.entity.CmsCurrentProducts;
import zrkj.project.entity.CmsCurrentProductsHis;
import zrkj.project.entity.CmsProduct;
import zrkj.project.toast.CommomDialog;
import zrkj.project.util.Constant;
import zrkj.project.util.JsonUtil;
import zrkj.project.util.Result;

@ContentView(R.layout.fenbao_activity)
public class FenTuoRkActivity extends BaseActivity {
    @ViewInject(R.id.saomiaotiaoma)
    private EditText saomiaotiaoma;
    @ViewInject(R.id.dangqianchanxian)
    private EditText dangqianchanxian;
    @ViewInject(R.id.spinner2)
    private Spinner spinner2;
    @ViewInject(R.id.chanpinbianma)
    private EditText chanpinbianma;
    @ViewInject(R.id.chanpingmingcheng)
    private EditText chanpingmingcheng;
    @ViewInject(R.id.guigexinghao)
    private EditText guigexinghao;
    @ViewInject(R.id.jiliangdanwei)
    private EditText jiliangdanwei;
    @ViewInject(R.id.tuopantiaoma)
    private EditText tuopantiaoma;
    @ViewInject(R.id.shuliang)
    private EditText shuliang;
    BaseCmsProductDialog commomDialog = null;
    @Override
    protected void initData() {

    }

    /**
     * @param barcodeStr
     */
    @Override
    protected void onScan(String barcodeStr) {
        if (barcodeStr.startsWith("T") && barcodeStr.endsWith("T")) {
            tuopantiaoma.setText(trimFirstAndLastChar(barcodeStr,"T"));
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
        topTitle.mTvTitle.setText("分托入库");
        topTitle.mTvRight.setText("");
    }

    @OnTouch({R.id.login})
    private boolean login(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            switch (v.getId()) {
                case R.id.login:
                    //分包
                    if (beforeLogin()) {
                        CommomDialog commomDialog = new CommomDialog(FenTuoRkActivity.this, R.style.dialog, "是否确定提交？", new CommomDialog.OnCloseListener() {
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

    private void InertCmsAssembly() {
        CmsCurrentProductsHis map = (CmsCurrentProductsHis)spinner2.getSelectedItem();
        CmsAssembly cmsAssembly = new CmsAssembly();
        cmsAssembly.setProductCode(chanpinbianma.getText().toString());
        cmsAssembly.setProductName(chanpingmingcheng.getText().toString());
        cmsAssembly.setSpecificationModel(guigexinghao.getText().toString());
        cmsAssembly.setUnit(jiliangdanwei.getText().toString());
        cmsAssembly.setBatch(map.getBatch());
        cmsAssembly.setPalletCode(tuopantiaoma.getText().toString());
        cmsAssembly.setQuantity(new BigDecimal(shuliang.getText().toString()));
        cmsAssembly.setProductionLineCode(dangqianchanxian.getText().toString());
        cmsAssembly.setBarCode(saomiaotiaoma.getText().toString());
        cmsAssembly.setType(Constant.TYPE_POINTS);
        Gson gson = new Gson();
        Map<String, Object> ms = null;
        try {
            ms = JsonUtil.objectToMap(cmsAssembly);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ms.put("device", DeviceInfoUtil.getOnlyID(this));
        request(gson.toJson(ms), "cms/cmsAssembly/cmsAssembly/add", "正在设置...", 2);
    }


    private boolean beforeLogin() {
        if (dangqianchanxian.getText().toString().equals("")) {
            showCustomToast("请扫描有效产品条码");
            return false;
        }
        if (tuopantiaoma.getText().toString().equals("")) {
            showCustomToast("请扫描托盘条码");
            return false;
        }

        return true;
    }


    @Override
    protected void requestCallBack(Result ajax, int posi) {
        Gson gson = new Gson();
        if (posi == 1) {
//            List<CmsCurrentProductsHis> list =  JSON.parseArray(ajax.getResult().toString(),CmsCurrentProductsHis.class);

            List<CmsCurrentProductsHis> list = gson.fromJson(ajax.getResult().toString(),new TypeToken<List<CmsCurrentProductsHis>>(){}.getType());

            spinner2.setAdapter(new MyBaseAdapter<CmsCurrentProductsHis, ListView>(this.context, list) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    ViewGroup layout = null;
                    if (convertView == null) {
                        layout = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.zdyspinner, parent, false);
                    } else {
                        layout = (ViewGroup)convertView;
                    }
                    CmsCurrentProductsHis mbean = this.list.get(position);
                    ((TextView)layout.findViewById(R.id.textView1)).setText(mbean.getBatch());
                    if (releativeOptionIndex == position) layout.setBackgroundColor(Color.YELLOW);
                    return layout;
                }
            });
            spinner2.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int arg2, long arg3) {
                    CmsCurrentProductsHis map = (CmsCurrentProductsHis)spinner2.getSelectedItem();
                    chanpinbianma.setText(map.getProductCode());
                    chanpingmingcheng.setText(map.getProductName());
                    guigexinghao.setText(map.getSpecificationModel());
                    jiliangdanwei.setText(map.getUnit());
                    dangqianchanxian.setText(map.getProductionLineCode());
                    shuliang.setText(map.getQuantity().stripTrailingZeros().toPlainString());
                }

                public void onNothingSelected(AdapterView<?> arg0) {
                    Map<String, Object> map = (Map<String, Object>) spinner2.getSelectedItem();
                }
            });
           /* CmsProduct cmsProduct = gson.fromJson(ajax.getResult().toString(), CmsProduct.class);
            CmsCurrentProducts cmsCurrentProducts = gson.fromJson(ajax.getResult().toString(), CmsCurrentProducts.class);
            chanpinbianma.setText(cmsProduct.getProductCode());
            chanpingmingcheng.setText(cmsProduct.getProductName());
            guigexinghao.setText(cmsProduct.getSpecificationModel());
            jiliangdanwei.setText(cmsProduct.getUnit());
            dangqianchanxian.setText(cmsCurrentProducts.getProductionLineCode());

            shuliang.setText(cmsProduct.getPalletsNumber().stripTrailingZeros().toPlainString());*/
            return;
        }
        if(posi==2){
            if(ajax.getMessage().contains("剩余可入库数量")){
                shuliang.setText(ajax.getResult().toString());
                CommomDialog commomDialog = new CommomDialog(FenTuoRkActivity.this, R.style.dialog, ajax.getMessage()+"是否继续？", new CommomDialog.OnCloseListener() {
                    @Override
                    public void onClick(Dialog dialog1, boolean confirm) {
                        if (confirm == false) {
                            InertCmsAssembly();
                        }
                    }
                });
                commomDialog.setTitle("警告").show();

            }
        }

        if(posi==3){
            List<CmsProduct> list = gson.fromJson(ajax.getResult().toString(),new TypeToken<List<CmsProduct>>(){}.getType());
            if (list != null && list.size() == 1) {
                chanpinbianma.setText(list.get(0).getProductCode());
                chanpingmingcheng.setText(list.get(0).getProductName());
                guigexinghao.setText(list.get(0).getSpecificationModel());
                jiliangdanwei.setText(list.get(0).getUnit());
                queryCmsProduct(list.get(0).getProductCode());
                return;
            }
            commomDialog = new BaseCmsProductDialog(list,FenTuoRkActivity.this, R.style.dialog, "", new BaseCmsProductDialog.OnCloseListener() {
                @Override
                public void onClick(Dialog dialog1, boolean confirm) {
                    if (confirm == false) {

                        shuliang.setText("");
                        chanpinbianma.setText(commomDialog.getbianhao());
                        chanpingmingcheng.setText(commomDialog.getmingcheng());
                        guigexinghao.setText(commomDialog.getxinghao());
                        jiliangdanwei.setText(commomDialog.getdanwei());
                        queryCmsProduct(commomDialog.getbianhao());
                    }
                }
            });
            commomDialog.setTitle("提示").show();
            return;
        }
    }

    private void queryCmsProduct(String barCode) {
        Gson gson = new Gson();
        Map<String, Object> ms = new HashMap<String, Object>();
        ms.put("barCode", barCode);
        ms.put("device", DeviceInfoUtil.getOnlyID(this));
        request(gson.toJson(ms), "cms/cmsProduct/cmsProduct/queryCmsProducts", "正在查询...", 1);
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
