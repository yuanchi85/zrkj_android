package zrkj.project.zrkj;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
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

@ContentView(R.layout.rukuzhijian_activity)
public class RuKuZhiJian_Activity extends BaseActivity {
    @ViewInject(R.id.spinner1)
    private Spinner m_spiModel;
    @ViewInject(R.id.spinner2)
    private Spinner spinner2;
    @ViewInject(R.id.saomiaotiaoma)
    private EditText saomiaotiaoma;
    @ViewInject(R.id.chanxian)
    private EditText chanxian;
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
    @ViewInject(R.id.rukushuliang)
    private EditText rukushuliang;
    @ViewInject(R.id.fuheshuliang)
    private EditText fuheshuliang;
    /*@ViewInject(R.id.hegeshuliang)
    private EditText hegeshuliang;
    @ViewInject(R.id.buliangshuliang)
    private EditText buliangshuliang;*/
    @ViewInject(R.id.beizhu)
    private EditText beizhu;
    @ViewInject(R.id.zhiliangbiaozhun)
    private EditText zhiliangbiaozhun;

    CmsAssembly cmsAssembly = new CmsAssembly();
    BaseCmsProductDialog commomDialog = null;

    @Override
    protected void initData() {

    }


    /**
     * 初始化所有控件
     */
    @Override
    protected void initView() {
        topTitle.mTvTitle.setText("入库质检");
        topTitle.mTvRight.setText("");
    }

    @OnTouch({R.id.login})
    private boolean login(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            switch (v.getId()) {
                case R.id.login:
                    //分包
                    if (beforeLogin()) {
                        CommomDialog commomDialog = new CommomDialog(RuKuZhiJian_Activity.this, R.style.dialog, "是否确定提交？", new CommomDialog.OnCloseListener() {
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

    @Override
    protected void onScan(String barcodeStr) {
        saomiaotiaoma.setText(barcodeStr);
        //根据条码查询所有产品信息
        queryCmsProductList(barcodeStr);
    }

    private boolean beforeLogin() {
        if (chanpinbianma.getText().toString().equals("")) {
            showCustomToast("请扫描有效产品条码");
            return false;
        }

        return true;
    }

    @Override
    protected void requestCallBack(Result ajax, int posi) {
        Gson gson = new Gson();
        if (posi == 1) {
            if (ajax.getCode() == 500) {
                List<Map<String, Object>> list = new ArrayList<>();
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
                        ((TextView) layout.findViewById(R.id.textView1)).setText(mbean.get("batch").toString());
                        if (releativeOptionIndex == position)
                            layout.setBackgroundColor(Color.YELLOW);
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
                        layout = (ViewGroup) convertView;
                    }
                    Map<String, Object> mbean = this.list.get(position);
                    ((TextView) layout.findViewById(R.id.textView1)).setText(mbean.get("batch").toString());
                    if (releativeOptionIndex == position) layout.setBackgroundColor(Color.YELLOW);
                    return layout;
                }
            });
            spinner2.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int arg2, long arg3) {
                    Map<String, Object> map = (Map<String, Object>) spinner2.getSelectedItem();
                    QureyQuantity(map.get("productCode").toString(), map.get("batch").toString());
                    zhiliangbiaozhun.setText(UpdateBatch(map.get("batch").toString()));
                }

                public void onNothingSelected(AdapterView<?> arg0) {
                    Map<String, Object> map = (Map<String, Object>) spinner2.getSelectedItem();
                }
            });

            if (ajax.getCode() == 500) {
                chanxian.setText("");
                chanpinbianma.setText("");
                chanpingmingcheng.setText("");
                guigexinhao.setText("");
                jiliangdanwei.setText("");
                rukushuliang.setText("");
                beizhu.setText("");
            }
            return;
        }
        if (posi == 3) {
            cmsAssembly = gson.fromJson(ajax.getResult().toString(), CmsAssembly.class);
            chanxian.setText(cmsAssembly.getProductionLineCode());
            chanpinbianma.setText(cmsAssembly.getProductCode());
            chanpingmingcheng.setText(cmsAssembly.getProductName());
            guigexinhao.setText(cmsAssembly.getSpecificationModel());
            jiliangdanwei.setText(cmsAssembly.getUnit());
            rukushuliang.setText(cmsAssembly.getQuantity().stripTrailingZeros().toPlainString());
            return;
        }
        if (posi == 4) {
            //调用ＥＲＰ接口
            /*if (ajax.getResult() != null && isNotEmpty(ajax.getResult())) {
                createErpProductReceive(ajax.getResult().toString());
            }*/
            //添加成功后刷新界面
//            queryAllProductionLine(saomiaotiaoma.getText().toString());
            ActivityUtil.goToActivity(context, RuKuZhiJian_Activity.class);
            finish();
            return;
        }

        if (posi == 6) {
            List<CmsProduct> list = gson.fromJson(ajax.getResult().toString(), new TypeToken<List<CmsProduct>>() {}.getType());
            if (list != null && list.size() == 1) {
                chanpinbianma.setText(list.get(0).getProductCode());
                chanpingmingcheng.setText(list.get(0).getProductName());
                guigexinhao.setText(list.get(0).getSpecificationModel());
                jiliangdanwei.setText(list.get(0).getUnit());

                queryAllProductionLine(chanpinbianma.getText().toString());
                return;
            }
            commomDialog = new BaseCmsProductDialog(list, RuKuZhiJian_Activity.this, R.style.dialog, "", new BaseCmsProductDialog.OnCloseListener() {
                @Override
                public void onClick(Dialog dialog1, boolean confirm) {
                    if (confirm == false) {
                        zhiliangbiaozhun.setText("");
                        chanxian.setText("");
                        rukushuliang.setText("");
                        chanpinbianma.setText(commomDialog.getbianhao());
                        chanpingmingcheng.setText(commomDialog.getmingcheng());
                        guigexinhao.setText(commomDialog.getxinghao());
                        jiliangdanwei.setText(commomDialog.getdanwei());
                        queryAllProductionLine(chanpinbianma.getText().toString());
                    }
                }
            });
            commomDialog.setTitle("提示").show();
            return;
        }

    }

    private void queryAllProductionLine(String productCode) {
        Gson gson = new Gson();
        Map<String, Object> ms = new HashMap<String, Object>();
        ms.put("productCode", productCode);
        ms.put("type", "质检");
        ms.put("device", DeviceInfoUtil.getOnlyID(this));
        requestNoSuccess(gson.toJson(ms), "cms/cmsAssembly/cmsAssembly/queryCmsAssembly", "正在查询...", 1);
    }

    //根据选择的批次和产品编号查询数量信息
    private void QureyQuantity(String productCode, String batch) {
        Gson gson = new Gson();
        Map<String, Object> ms = new HashMap<String, Object>();
        ms.put("productCode", productCode);
        ms.put("batch", batch);
        ms.put("device", DeviceInfoUtil.getOnlyID(this));
        request(gson.toJson(ms), "cms/cmsAssembly/cmsAssembly/qureyquantity", "正在查询...", 3);
    }

    private void InertCmsAssembly() {
        cmsAssembly.setRemarks(beizhu.getText().toString());
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        Map<String, Object> ms = null;
        try {
            cmsAssembly.setQualityInspector(((Spinner) findViewById(R.id.spinner1)).getSelectedItem().toString());
            cmsAssembly.setRemarks(beizhu.getText().toString());
            ms = JsonUtil.objectToMap(cmsAssembly);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ms.put("device", DeviceInfoUtil.getOnlyID(this));
        request(gson.toJson(ms), "cms/cmsAssembly/cmsAssembly/addpdate", "正在设置...", 4);
    }

    private void createErpProductReceive(String ids) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        Map<String, Object> ms = new HashMap<>();
        ms.put("device", DeviceInfoUtil.getOnlyID(this));
        requestByGet(gson.toJson(ms), "cms/openApi/createErpProductReceive" + "?ids=" + ids, "正在入库...", 5);
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
