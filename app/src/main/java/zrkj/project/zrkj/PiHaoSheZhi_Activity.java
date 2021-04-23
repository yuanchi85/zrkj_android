package zrkj.project.zrkj;

import android.app.Dialog;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.finddreams.baselib.R;
import com.finddreams.baselib.base.BaseActivity;
import com.finddreams.baselib.base.MyBaseAdapter;
import com.finddreams.baselib.utils.ActivityUtil;
import com.finddreams.baselib.utils.DeviceInfoUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnTouch;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zrkj.Doalog.BaseCmsProductDialog;
import zrkj.project.entity.CmsCurrentProducts;
import zrkj.project.entity.CmsProduct;
import zrkj.project.toast.CommomDialog;
import zrkj.project.util.JsonUtil;
import zrkj.project.util.Result;

@ContentView(R.layout.pihaoshezi_activity)
public class PiHaoSheZhi_Activity extends BaseActivity {
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
    @ViewInject(R.id.zhiliangbiaozun)
    private TextView zhiliangbiaozun;
    @ViewInject(R.id.kaigong)
    private Button kaigong;
    @ViewInject(R.id.wupici)
    private Button wupici;
    BaseCmsProductDialog commomDialog = null;

    @Override
    protected void initData() {

    }

    @Override
    protected void onScan(String barcodeStr) {
        queryCmsProduct(barcodeStr);
    }

    /**
     * 初始化所有控件
     */
    @Override
    protected void initView() {
        topTitle.mTvTitle.setText("批号设置");
        topTitle.mTvRight.setText("");
        queryAllProductionLine();
    }

    @Override
    protected void requestCallBack(Result ajax, int posi) {
        Gson gson = new Gson();
        if (posi == 1) {
//            List<Map<String, Object>> list = gson.fromJson(ajax.getResult().toString(), List.class);
            List<Map<String,Object>> list = gson.fromJson(ajax.getResult().toString(),new TypeToken<List<Map<String, Object>>>(){}.getType());

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
                    ((TextView) layout.findViewById(R.id.textView1)).setText(mbean.get("text").toString() + "_" + mbean.get("value").toString());
                    if (releativeOptionIndex == position) layout.setBackgroundColor(Color.YELLOW);
                    return layout;
                }
            });
            m_spiModel.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int arg2, long arg3) {
                    Map<String, Object> map = (Map<String, Object>) m_spiModel.getSelectedItem();
                    chanpinbianma.setText("");
                    chanpingmingcheng.setText("");
                    guigexinghao.setText("");
                    jiliangdanwei.setText("");
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
                    String dateString = formatter.format(new Date());
                    zhiliangbiaozun.setText("");
                    if(Integer.valueOf(map.get("value").toString())<10){
                        dangqianpihao.setText(dateString+"X");
                    }else{
                        dangqianpihao.setText(dateString+"L");
                    }
                    if (map.get("productCode") != null)
                        chanpinbianma.setText(map.get("productCode").toString());
                    if (map.get("productName") != null)
                        chanpingmingcheng.setText(map.get("productName").toString());
                    if (map.get("specificationModel") != null)
                        guigexinghao.setText(map.get("specificationModel").toString());
                    if (map.get("unit") != null)
                        jiliangdanwei.setText(map.get("unit").toString());
                    if (map.get("batch") != null) {
                        dangqianpihao.setText(map.get("batch").toString());
                        if (isNotEmpty(dangqianpihao.getText().toString())) {
                            zhiliangbiaozun.setText(UpdateBatch(map.get("batch").toString()));
                        }
                    }
                    if (map.get("currentQuantity") != null)
                        jihuashsuliang.setText(new BigDecimal(map.get("currentQuantity").toString()).stripTrailingZeros().toPlainString());

                }

                public void onNothingSelected(AdapterView<?> arg0) {
                    Map<String, Object> map = (Map<String, Object>) m_spiModel.getSelectedItem();
                }
            });

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
                return;
            }
            commomDialog = new BaseCmsProductDialog(list, PiHaoSheZhi_Activity.this, R.style.dialog, "是否确定提交？", new BaseCmsProductDialog.OnCloseListener() {
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
            return;
        }
        if (posi == 5) {
            CmsCurrentProducts cmszdEntity = new CmsCurrentProducts();
            Map<String, Object> map = (Map<String, Object>) m_spiModel.getSelectedItem();
            cmszdEntity.setProductionLineCode(map.get("value").toString());
            cmszdEntity.setProductCode(chanpinbianma.getText().toString());
            cmszdEntity.setBatch(dangqianpihao.getText().toString());
            removeWg(cmszdEntity);
            return;
        }

        if (posi == 3) {
            ActivityUtil.goToActivity(context, PiHaoSheZhi_Activity.class);
            finish();
            return;
        }

        if (posi == 4) {
            Map<String, Object> map = (Map<String, Object>) m_spiModel.getSelectedItem();
            resetQuantityKg(map.get("value").toString());
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
            if (!"999999".equals(dangqianpihao.getText().toString())) {
                String upperBatch = dangqianpihao.getText().toString().toUpperCase();
                String c = "X";
                if (upperBatch.contains("X") ||upperBatch.contains("L")) {
                    dangqianpihao.setText(upperBatch);
                }else{
                    showCustomToast("批次号必须包含大写[X]或[L]");
                    return false;
                }


            }
        }
        if (jihuashsuliang.getText().toString().equals("")) {
            showCustomToast("请输入计划数量");
            return false;
        }
        return true;
    }

    @OnTouch({R.id.kaigong, R.id.wangong, R.id.wupici})
    private boolean login(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            switch (v.getId()) {
                case R.id.kaigong:
                    kaigong.setEnabled(false);
                    //开工
                    if (beforeLogin()) {
                        CommomDialog commomDialog = new CommomDialog(PiHaoSheZhi_Activity.this, R.style.dialog, "是否确认开工？", new CommomDialog.OnCloseListener() {
                            @Override
                            public void onClick(Dialog dialog1, boolean confirm) {
                                if (confirm == false) {
                                    zhiliangbiaozun.setText(UpdateBatch(dangqianpihao.getText().toString()));
                                    CmsCurrentProducts cmszdEntity = new CmsCurrentProducts();
                                    Map<String, Object> map = (Map<String, Object>) m_spiModel.getSelectedItem();
                                    cmszdEntity.setProductionLineCode(map.get("value").toString());
                                    cmszdEntity.setProductCode(chanpinbianma.getText().toString());
                                    cmszdEntity.setBatch(dangqianpihao.getText().toString());
                                    cmszdEntity.setCurrentQuantity(new BigDecimal(jihuashsuliang.getText().toString()));
                                    InertCmszd(cmszdEntity);
                                    kaigong.setEnabled(true);
                                } else {
                                    kaigong.setEnabled(true);
                                }
                            }
                        });
                        commomDialog.setTitle("警告").show();
                    } else {
                        kaigong.setEnabled(true);
                    }
                    break;
                case R.id.wangong:
                    //完工
                    if (beforeLogin()) {
                        CommomDialog commomDialog = new CommomDialog(PiHaoSheZhi_Activity.this, R.style.dialog, "是否确认完工？", new CommomDialog.OnCloseListener() {
                            @Override
                            public void onClick(Dialog dialog1, boolean confirm) {
                                if (confirm == false) {
                                    Map<String, Object> map = (Map<String, Object>) m_spiModel.getSelectedItem();
                                    resetQuantity(map.get("value").toString());

                                    /*CmsCurrentProducts cmszdEntity = new CmsCurrentProducts();
                                    Map<String, Object> map = (Map<String, Object>) m_spiModel.getSelectedItem();
                                    cmszdEntity.setProductionLineCode(map.get("value").toString());
                                    cmszdEntity.setProductCode(chanpinbianma.getText().toString());
                                    cmszdEntity.setBatch(dangqianpihao.getText().toString());
                                    removeWg(cmszdEntity);*/
                                }
                            }
                        });
                        commomDialog.setTitle("警告").show();
                    }
                    break;
                case R.id.wupici:
                    //无批次
                    CommomDialog commomDialog = new CommomDialog(PiHaoSheZhi_Activity.this, R.style.dialog, "是否确认无批次？", new CommomDialog.OnCloseListener() {
                        @Override
                        public void onClick(Dialog dialog1, boolean confirm) {
                            if (confirm == false) {
                                dangqianpihao.setText("999999");
                            }
                        }
                    });
                    commomDialog.setTitle("警告").show();
                    break;
                default:
                    break;
            }
        }
        return true;
    }


    private void InertCmszd(CmsCurrentProducts cmszdEntity) {
        Gson gson = new Gson();
        Map<String, Object> ms = null;
        try {
            ms = JsonUtil.objectToMap(cmszdEntity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ms.put("device", DeviceInfoUtil.getOnlyID(this));
        request(gson.toJson(ms), "cms/cmsCurrentProducts/cmsCurrentProducts/add", "正在设置...", 4);
    }


    //完工保存接口
    private void removeWg(CmsCurrentProducts cmszdEntity) {
        Gson gson = new Gson();
        Map<String, Object> ms = null;
        try {
            ms = JsonUtil.objectToMap(cmszdEntity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ms.put("device", DeviceInfoUtil.getOnlyID(this));
        request(gson.toJson(ms), "cms/cmsCurrentProducts/cmsCurrentProducts/removeWg", "正在设置...", 3);
    }

    private void queryAllProductionLine() {
        Gson gson = new Gson();
        Map<String, Object> ms = new HashMap<String, Object>();
        ms.put("device", DeviceInfoUtil.getOnlyID(this));
        request(gson.toJson(ms), "cms/sys/queryAllProductionLine", "正在查询...", 1);
//        request(gson.toJson(ms), "cms/cmsProductionLine/cmsProductionLine/queryAllProductionLine", "正在查询...", 1);
    }

    //根据扫描的产品条码查询所有的产品信息
    private void queryCmsProduct(String barCode) {
        Gson gson = new Gson();
        Map<String, Object> ms = new HashMap<String, Object>();
        ms.put("barCode", barCode);
        ms.put("device", DeviceInfoUtil.getOnlyID(this));
        request(gson.toJson(ms), "cms/cmsProduct/cmsProduct/queryCmsProductList", "正在查询...", 2);
    }

    //调用清零接口
    private void resetQuantity(String productionLineCode) {
        Gson gson = new Gson();
        Map<String, Object> ms = new HashMap<String, Object>();
        ms.put("productLineCode", productionLineCode);
        ms.put("device", DeviceInfoUtil.getOnlyID(this));
        requestcloseZero(gson.toJson(ms), "http://192.168.11.52:9090/cms/closeZero", "正在设置...", 5);
    }

    //调用清零接口
    private void resetQuantityKg(String productionLineCode) {
        Gson gson = new Gson();
        Map<String, Object> ms = new HashMap<String, Object>();
        ms.put("productLineCode", productionLineCode);
        ms.put("device", DeviceInfoUtil.getOnlyID(this));
        requestcloseZero(gson.toJson(ms), "http://192.168.11.52:9090/cms/kgcloseZero", "正在设置...", 6);
    }
}
