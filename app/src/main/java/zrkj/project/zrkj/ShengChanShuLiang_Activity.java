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
import com.finddreams.baselib.utils.StringUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnTouch;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zrkj.Doalog.BaseCmsProductDialog;
import zrkj.project.entity.CmsCurrentProducts;
import zrkj.project.entity.CmsCurrentProductsBay;
import zrkj.project.entity.CmsCurrentProductsHis;
import zrkj.project.entity.CmsProduct;
import zrkj.project.toast.CommomDialog;
import zrkj.project.util.JsonUtil;
import zrkj.project.util.Result;

@ContentView(R.layout.xiugaishuliang_activity)
public class ShengChanShuLiang_Activity extends BaseActivity {
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
    @ViewInject(R.id.wangongshu)
    private EditText wangongshu;
    @ViewInject(R.id.xinshuliang)
    private EditText xinshuliang;
    @ViewInject(R.id.beizhu)
    private EditText beizhu;
    @ViewInject(R.id.zhiliangbiaozun)
    private TextView zhiliangbiaozun;
    @ViewInject(R.id.kaigong)
    private Button kaigong;
    @ViewInject(R.id.wupici)
    private Button wupici;
    BaseCmsProductDialog commomDialog = null;

    private CmsCurrentProductsHis cmsCurrentProducts;

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
        topTitle.mTvTitle.setText("数量调整");
        topTitle.mTvRight.setText("");
        queryAllProductionLine();
    }

    @Override
    protected void requestCallBack(Result ajax, int posi) {
        Gson gson = new Gson();
        if (posi == 1) {
            List<Map<String, Object>> list = gson.fromJson(ajax.getResult().toString(), List.class);
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
            commomDialog = new BaseCmsProductDialog(list, ShengChanShuLiang_Activity.this, R.style.dialog, "是否确定提交？", new BaseCmsProductDialog.OnCloseListener() {
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

        if (posi == 4) {
            List<CmsCurrentProductsHis> list = gson.fromJson(ajax.getResult().toString(), new TypeToken<List<CmsCurrentProductsHis>>() {
            }.getType());
            zhiliangbiaozun.setText(UpdateBatch(dangqianpihao.getText().toString()));
            if (list != null && list.size() != 0) {
                cmsCurrentProducts  = list.get(0);
                wangongshu.setText(list.get(0).getTotalQuantity().stripTrailingZeros().toEngineeringString());
            }
            return;
        }
    }

    private boolean beforeLogin(int i) {

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
                if (upperBatch.contains("X") || upperBatch.contains("L")) {
                    dangqianpihao.setText(upperBatch);
                } else {
                    showCustomToast("批次号必须包含大写[X]或[L]");
                    return false;
                }
            }
        }

        if(i==2){
            if(!StringUtil.isNotEmpty(xinshuliang.getText().toString())){
                showCustomToast("请输入新数量");
                return false;
            }
            if(!StringUtil.isNotEmpty(beizhu.getText().toString())){
                showCustomToast("请输入修改原因");
                return false;
            }
        }

        return true;
    }

    @OnTouch({R.id.kaigong, R.id.wangong, R.id.wupici})
    private boolean login(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            switch (v.getId()) {
                case R.id.kaigong:
                    if (beforeLogin(1)) {
                        Map<String, Object> map = (Map<String, Object>) m_spiModel.getSelectedItem();
                        InertCmszd(map.get("value").toString());

                    }

                    break;
                case R.id.wangong:
                    //完工
                    if (beforeLogin(2)) {
                        CommomDialog commomDialog = new CommomDialog(ShengChanShuLiang_Activity.this, R.style.dialog, "是否确认修改？", new CommomDialog.OnCloseListener() {
                            @Override
                            public void onClick(Dialog dialog1, boolean confirm) {
                                if (confirm == false) {
                                    Map<String, Object> map = (Map<String, Object>) m_spiModel.getSelectedItem();
                                  /*  CmsCurrentProductsHis bay = new CmsCurrentProductsHis();
                                    bay.setProductCode(chanpinbianma.getText().toString());
                                    bay.setProductName(chanpingmingcheng.getText().toString());
                                    bay.setSpecificationModel(guigexinghao.getText().toString());
                                    bay.setUnit(jiliangdanwei.getText().toString());
                                    bay.setBatch(dangqianpihao.getText().toString());
                                    bay.setProductionLineCode(map.get("value").toString());
                                    bay.setCurrentQuantity(cmsCurrentProducts.getCurrentQuantity());
                                    bay.setEquipmentId(cmsCurrentProducts.getEquipmentId());
                                    bay.setTotalQuantity(new BigDecimal(xinshuliang.getText().toString()));
                                   */
                                    cmsCurrentProducts.setRemarks(beizhu.getText().toString());
                                    cmsCurrentProducts.setTotalQuantity(new BigDecimal(xinshuliang.getText().toString()));
                                    updateQuintity(cmsCurrentProducts);
                                }
                            }
                        });
                        commomDialog.setTitle("警告").show();
                    }
                    break;
                case R.id.wupici:
                    //无批次
                    CommomDialog commomDialog = new CommomDialog(ShengChanShuLiang_Activity.this, R.style.dialog, "是否确认无批次？", new CommomDialog.OnCloseListener() {
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

    //根据产线编号，产品编号，批次查询生产数量
    private void InertCmszd(String productionLineCode) {
        Gson gson = new Gson();
        Map<String, Object> ms = new HashMap<>();
        ms.put("productionLineCode",productionLineCode);
        ms.put("productCode", chanpinbianma.getText().toString());
        ms.put("batch", dangqianpihao.getText().toString());
        ms.put("device", DeviceInfoUtil.getOnlyID(this));
        request(gson.toJson(ms), "cms/cmsAssembly/cmsAssembly/queryMaxNumber", "正在设置...", 4);
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


    //修改数量
    private void updateQuintity(CmsCurrentProductsHis cmszdEntity) {
        Gson gson = new Gson();
        Map<String, Object> ms = null;
        try {
            ms = JsonUtil.objectToMap(cmszdEntity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ms.put("device", DeviceInfoUtil.getOnlyID(this));
        request(gson.toJson(ms), "cms/cmsAssembly/cmsAssembly/updateQuintity", "正在设置...", 3);
    }


}
