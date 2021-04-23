package zrkj.project.zrkj;

import android.app.Dialog;
import android.graphics.Color;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zrkj.Doalog.BaseCmsProductDialog;
import zrkj.Doalog.GongDanLingLiaoXiuGaiDialog;
import zrkj.Doalog.PiLiangXiaoHuoDialog;
import zrkj.project.entity.CmsAssembly;
import zrkj.project.entity.CmsProduct;
import zrkj.project.entity.CmsSaleDetailed;
import zrkj.project.entity.ViewSaSaleDeliveryDetail;
import zrkj.project.util.Result;

@ContentView(R.layout.piliangxiaohuo_activity)
public class PiLiang_XiaoHuo_Activity extends BaseActivity {
    @ViewInject(R.id.lv_list)
    ListView mList;
    DemoAdapter adapter;
    @ViewInject(R.id.lv_list2)
    ListView mList2;
    DemoAdapter2 adapter2;
    @ViewInject(R.id.xiaohuodanhao)
    EditText xiaohuodanhao;
    @ViewInject(R.id.tuopantiaoma)
    EditText tuopantiaoma;
    @ViewInject(R.id.canpintiaoma)
    EditText canpintiaoma;
    List<ViewSaSaleDeliveryDetail> viewEntity = new ArrayList<>();
    List<CmsSaleDetailed> cms = new ArrayList<>();
    public static List<CmsAssembly> cmsAssemblyArrayList = new ArrayList<>();
    //记录列表选中的值
//    ViewSaSaleDeliveryDetail saSaleDeliveryDetail = null;
    PiLiangXiaoHuoDialog commomDialog = null;
    public static CmsAssembly cmsAssembly = null;
    public static List<Map<String, Object>> list = new ArrayList<>();
    BaseCmsProductDialog commomDia = null;
    private int ab = -1;

    @Override
    protected void initData() {

    }

    @Override
    protected void onScan(String barcodeStr) {
        if (barcodeStr.startsWith("F") && barcodeStr.endsWith("F")) {
            xiaohuodanhao.setText("");
            xiaohuodanhao.setText(trimFirstAndLastChar(barcodeStr, "F"));
            queryCmsProduct(xiaohuodanhao.getText().toString());
            return;
        }
        if (barcodeStr.startsWith("T") && barcodeStr.endsWith("T")) {
            tuopantiaoma.setText(trimFirstAndLastChar(barcodeStr, "T"));
            if (!StringUtil.isNotEmpty(canpintiaoma.getText().toString())) {
                showMsg("请扫描产品条码");
                return;
            }
            //查询该产品信息
            queryCmsAssemblyXhSm();
        }else{
            //根据产品条码查询产品信息
            queryCmsProductList(barcodeStr);
        }

    }

    /**
     * 初始化所有控件
     */
    @Override
    protected void initView() {
        topTitle.mTvTitle.setText("批量销货");
        topTitle.mTvRight.setText("");
        adapter = new PiLiang_XiaoHuo_Activity.DemoAdapter();
        mList.setAdapter(adapter);
        adapter2 = new PiLiang_XiaoHuo_Activity.DemoAdapter2();
        mList2.setAdapter(adapter2);
        Onseel();
    }


    public void Onseel() {
        xiaohuodanhao.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //当actionId == XX_SEND 或者 XX_DONE时都触发
                //或者event.getKeyCode == ENTER 且 event.getAction == ACTION_DOWN时也触发
                //注意，这是一定要判断event != null。因为在某些输入法上会返回null。
                if (actionId == EditorInfo.IME_ACTION_SEND
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {
                    //处理事件
                    queryCmsProduct(xiaohuodanhao.getText().toString());
                }
                return false;
            }
        });
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
            final PiLiang_XiaoHuo_Activity.DemoAdapter.ViewHolder holder;
            if (view == null) {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.xiaohuo2_tbaerl, null);
                holder = new PiLiang_XiaoHuo_Activity.DemoAdapter.ViewHolder();
                holder.tv1 = view.findViewById(R.id.bianhao);
                holder.tv2 = view.findViewById(R.id.mingcheng);
                holder.tv3 = view.findViewById(R.id.xinghao);
                holder.tv4 = view.findViewById(R.id.danwei);
                holder.tv5 = view.findViewById(R.id.xiaohuoshuliang);
                holder.tv6 = view.findViewById(R.id.shijishuliang);
                holder.tv7 = view.findViewById(R.id.pici);
                holder.okTable = view.findViewById(R.id.biaoge);
                view.setTag(holder);
            } else {
                holder = (PiLiang_XiaoHuo_Activity.DemoAdapter.ViewHolder) view.getTag();
            }
            ViewSaSaleDeliveryDetail tail = viewEntity.get(i);
            holder.tv1.setText(tail.getProductCode());//产品编号
            holder.tv2.setText(tail.getProductName());//产品名称
            holder.tv3.setText(tail.getSpecificationModel());//规格型号
            holder.tv4.setText(tail.getUnit());//jiliangdnawei
            holder.tv5.setText(tail.getQuantity().stripTrailingZeros().toPlainString());//shuliang
            holder.tv6.setText(tail.getActualQuantity().stripTrailingZeros().toPlainString());//实际数量
            holder.tv7.setText(tail.getBatch());//pici

            if (ab == i) {
                view.setBackgroundColor(Color.GREEN);
            } else if (tail.getQuantity().compareTo(tail.getActualQuantity()) == 0) {
                view.setBackgroundColor(Color.RED);
            } else {
                view.setBackgroundColor(Color.parseColor("#dfd0d5"));
            }
            return view;
        }

        class ViewHolder {
            TextView tv1, tv2, tv3, tv4, tv5, tv6,tv7;
            LinearLayout okTable;
        }
    }

    public class DemoAdapter2 extends BaseAdapter {

        @Override
        public int getCount() {
            return cms.size();
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
            final PiLiang_XiaoHuo_Activity.DemoAdapter2.ViewHolder2 holder2;

            if (view == null) {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.xiaohuo_tbaerl, null);
                holder2 = new PiLiang_XiaoHuo_Activity.DemoAdapter2.ViewHolder2();
                holder2.tv1 = view.findViewById(R.id.bianhao);
                holder2.tv2 = view.findViewById(R.id.mingcheng);
                holder2.tv3 = view.findViewById(R.id.xinghao);
                holder2.tv4 = view.findViewById(R.id.danwei);
                holder2.tv5 = view.findViewById(R.id.pihao);
                holder2.tv6 = view.findViewById(R.id.cangkubianhao);
                holder2.tv7 = view.findViewById(R.id.tuopantiaoma);
                holder2.tv8 = view.findViewById(R.id.shuliang);
                holder2.okTable = view.findViewById(R.id.biaoge);
                view.setTag(holder2);
            } else {
                holder2 = (PiLiang_XiaoHuo_Activity.DemoAdapter2.ViewHolder2) view.getTag();
            }

            CmsSaleDetailed cmsSaleDetailed = cms.get(i);
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
            viewEntity = gson.fromJson(ajax.getResult().toString(), new TypeToken<List<ViewSaSaleDeliveryDetail>>() {
            }.getType());
            adapter.notifyDataSetChanged();
            queryCmsSaleDetailed();
            return;
        }
        if (posi == 2) {
            if (ajax.getResult() != null && !ajax.getResult().toString().equals("")) {
                cms = gson.fromJson(ajax.getResult().toString(), new TypeToken<List<CmsSaleDetailed>>() {
                }.getType());
                adapter2.notifyDataSetChanged();
                if (ab >= 0) {
                    mList.setSelection(ab);//定位到某行
                }
                return;
            }
        }

        if (posi == 3) {
            queryCmsProduct(xiaohuodanhao.getText().toString());
            return;
        }

        if (posi == 4) {
            cmsAssemblyArrayList = gson.fromJson(ajax.getResult().toString(), new TypeToken<List<CmsAssembly>>() {
            }.getType());

            Boolean len = false;//判断批次是否相等
            Boolean len2 = false;//判断数量是否出完
            int i=-1;
            for (CmsAssembly duct : cmsAssemblyArrayList) {
                for (ViewSaSaleDeliveryDetail tail : viewEntity) {
                    //判断批次是否相等
                    if (tail.getBatch().equals(duct.getBatch())) {
                        len = true;
                        //判断数量是否出完
                        if (tail.getActualQuantity().compareTo(tail.getQuantity()) == -1) {
                            len2 = true;
                        }
                        duct.setZongshu(tail.getQuantity());
                        duct.setYilingshuliang(tail.getQuantity().subtract(tail.getActualQuantity()));
                        break;
                    }
                }
                i++;
            }

            if (!len) {
                showMsg("扫描的产品与销货单不相符");
                return;
            }
            if (!len2) {
                showMsg("该产品已出完，不能超出");
                return;
            }
            cmsAssembly = cmsAssemblyArrayList.get(i);
            commomDialog = new PiLiangXiaoHuoDialog(PiLiang_XiaoHuo_Activity.this, R.style.dialog, "是否确定提交？", new PiLiangXiaoHuoDialog.OnCloseListener() {
                @Override
                public void onClick(Dialog dialog1, boolean confirm) {
                    if (confirm == false) {
                        String quantity = commomDialog.getshuliang();
                        InsetCmsSaleDetailed(quantity);
                    }
                }
            });
            commomDialog.setTitle("提示").show();


            return;
        }

        if (posi == 5) {
            List<CmsProduct> list = gson.fromJson(ajax.getResult().toString(), new TypeToken<List<CmsProduct>>() {
            }.getType());
            Boolean lean = true;
            for (CmsProduct duct : list) {
            for (ViewSaSaleDeliveryDetail tail : viewEntity) {
                    if (tail.getProductCode().equals(duct.getProductCode())) {
                        lean = false;
                        break;
                    }
                    if(!lean)break;
                }
            }

            if (lean) {
                showMsg("扫描的产品和销货单产品不相符");
                return;
            }
            if (list != null && list.size() == 1) {
                canpintiaoma.setText(list.get(0).getProductCode());
                if (!isNotEmpty(tuopantiaoma.getText().toString())) {
                    showMsg("请扫描托盘条码");
                    return;
                }
                queryCmsAssemblyXhSm();
                return;
            }
            commomDia = new BaseCmsProductDialog(list, PiLiang_XiaoHuo_Activity.this, R.style.dialog, "", new BaseCmsProductDialog.OnCloseListener() {
                @Override
                public void onClick(Dialog dialog1, boolean confirm) {
                    if (confirm == false) {
                        canpintiaoma.setText("");
                        canpintiaoma.setText(commomDia.getbianhao());
                        if (!isNotEmpty(tuopantiaoma.getText().toString())) {
                            showMsg("请扫描托盘条码");
                            return;
                        }
                        queryCmsAssemblyXhSm();
                    }
                }
            });
            commomDia.setTitle("提示").show();
            return;
        }
    }


    private void queryCmsProduct(String barCode) {
        Gson gson = new Gson();
        Map<String, Object> ms = new HashMap<String, Object>();
        ms.put("code", barCode);
        ms.put("device", DeviceInfoUtil.getOnlyID(this));
        request(gson.toJson(ms), "cms/saSaleDelivery/saSaleDelivery/queryBatchSaSaleDelivery", "正在查询...", 1);
    }

    private void queryCmsSaleDetailed() {
        Gson gson = new Gson();
        Map<String, Object> ms = new HashMap<String, Object>();
        ms.put("code", xiaohuodanhao.getText().toString());
        ms.put("device", DeviceInfoUtil.getOnlyID(this));
        request(gson.toJson(ms), "cms/cmsSaleDetailed/cmsSaleDetailed/queryCmsSaleDetailedYl", "正在查询...", 2);
    }


    private void InsetCmsSaleDetailed(String quantity) {
        Gson gson = new Gson();
        Map<String, Object> ms = new HashMap<String, Object>();
        ms.put("palletCode", tuopantiaoma.getText().toString());//托盘条码
        ms.put("productCode", cmsAssembly.getProductCode());//产品编号
        ms.put("productName", cmsAssembly.getProductName());
        ms.put("specificationModel", cmsAssembly.getSpecificationModel());
        ms.put("unit", cmsAssembly.getUnit());
        ms.put("batch", cmsAssembly.getBatch());
        ms.put("warehouseCode", cmsAssembly.getWarehouseCode());
        ms.put("barCode", canpintiaoma.getText().toString());
        ms.put("quantity", quantity);
        ms.put("salesNumber", xiaohuodanhao.getText().toString());
        ms.put("device", DeviceInfoUtil.getOnlyID(this));
        request(gson.toJson(ms), "cms/cmsSaleDetailed/cmsSaleDetailed/InsetCmsSaleDetailedPl", "正在查询...", 3);
    }

    private void queryCmsAssemblyXhSm() {
        Gson gson = new Gson();
        Map<String, Object> ms = new HashMap<String, Object>();
        ms.put("palletCode", tuopantiaoma.getText().toString());
        ms.put("productCode", canpintiaoma.getText().toString());
        ms.put("device", DeviceInfoUtil.getOnlyID(this));
        request(gson.toJson(ms), "cms/cmsAssembly/cmsAssembly/queryCmsAssemblyXhSm", "正在查询...", 4);
    }

    //根据扫描的产品条码查询所有的产品信息
    private void queryCmsProductList(String barCode) {
        Gson gson = new Gson();
        Map<String, Object> ms = new HashMap<String, Object>();
        ms.put("barCode", barCode);
        ms.put("device", DeviceInfoUtil.getOnlyID(this));
        request(gson.toJson(ms), "cms/cmsProduct/cmsProduct/queryCmsProductList", "正在查询...", 5);
    }


}
