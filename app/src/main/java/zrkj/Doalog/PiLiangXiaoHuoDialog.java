package zrkj.Doalog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.finddreams.baselib.R;
import com.finddreams.baselib.base.MyBaseAdapter;

import java.math.BigDecimal;
import java.util.Map;

import zrkj.project.entity.CmsAssembly;
import zrkj.project.zrkj.PiLiang_XiaoHuo_Activity;
import zrkj.project.zrkj.XiaoHuo_Activity;


/**
 * 时间：2019/7/19
 * 项目名：zrkj_tm_pda
 * 包名：com.zrkj.Base
 * 类名：Activity
 * 作者：杨亚明
 **/

public class PiLiangXiaoHuoDialog extends Dialog implements View.OnClickListener {
    private TextView contentTxt;
    private TextView titleTxt;
    private TextView submitTxt;
    private TextView cancelTxt;
    private TextView pici, mingcheng, xunlingshuliang, kelingshuliang,zhiliangbiaozhun;
    private EditText shuliang;
    private Context mContext;
    private String content;
    private OnCloseListener listener;
    private String positiveName;
    private String negativeName;
    private String title;
    private ImageView view;
    private String shishishuliang;

    private BigDecimal bigmal = new BigDecimal("0");


    public PiLiangXiaoHuoDialog(Context context) {
        super(context);
        this.mContext = context;
    }


    public PiLiangXiaoHuoDialog(Context context, int themeResId, String content) {
        super(context, themeResId);
        this.mContext = context;
        this.content = content;
    }


    public PiLiangXiaoHuoDialog(Context context, int themeResId, String content, OnCloseListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.content = content;
        this.listener = listener;


    }


    protected PiLiangXiaoHuoDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }


    public PiLiangXiaoHuoDialog setTitle(String title) {
        this.title = title;
        return this;
    }


    public PiLiangXiaoHuoDialog setPositiveButton(String name) {
        this.positiveName = name;
        return this;
    }


    public PiLiangXiaoHuoDialog setNegativeButton(String name) {
        this.negativeName = name;
        return this;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pilinagxiaohou_dialog_layout);
        setCanceledOnTouchOutside(false);
        initView();
        viewClick();
    }


    public void viewClick() {
        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                shuliang.setText("");
            }
        });
    }


    /**
     *
     */
    private void initView() {
        titleTxt = (TextView) findViewById(R.id.title);
        submitTxt = (TextView) findViewById(R.id.submit);
        submitTxt.setOnClickListener(this);
        cancelTxt = (TextView) findViewById(R.id.cancel);
        cancelTxt.setOnClickListener(this);
        view = (ImageView) findViewById(R.id.ImageView1);
        mingcheng = (TextView) findViewById(R.id.wuliaomingcheng);
        xunlingshuliang = (TextView) findViewById(R.id.xunlingshuliang);
        kelingshuliang = (TextView) findViewById(R.id.kelingshuliang);
        shuliang = (EditText) findViewById(R.id.chanpintiaoma);
        zhiliangbiaozhun = (TextView) findViewById(R.id.zhiliangbiaozhun);
        pici = (TextView) findViewById(R.id.pici);


        if (PiLiang_XiaoHuo_Activity.cmsAssembly != null) {
            CmsAssembly cmsAssemblys = PiLiang_XiaoHuo_Activity.cmsAssembly;
            pici.setText(cmsAssemblys.getBatch());
            zhiliangbiaozhun.setText(UpdateBatch(cmsAssemblys.getBatch()));
            mingcheng.setText(cmsAssemblys.getProductName());
            xunlingshuliang.setText(cmsAssemblys.getZongshu().stripTrailingZeros().toPlainString());
            kelingshuliang.setText(cmsAssemblys.getYilingshuliang().stripTrailingZeros().toPlainString());

            //判断可伶数量是否小于库存数量
            if(cmsAssemblys.getYilingshuliang().compareTo(cmsAssemblys.getQualifiedQuantity())<1){
                shuliang.setText(cmsAssemblys.getYilingshuliang().stripTrailingZeros().toEngineeringString());
            }else{
                shuliang.setText(cmsAssemblys.getQualifiedQuantity().stripTrailingZeros().toEngineeringString());
            }
        }
    }


    public String getshuliang(){
       return shuliang.getText().toString();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel:
                BigDecimal big = new BigDecimal(shuliang.getText().toString());
                BigDecimal bigDecimal = new BigDecimal(xunlingshuliang.getText().toString());
                bigmal = new BigDecimal(kelingshuliang.getText().toString());
                if (big.intValue() > bigmal.intValue()) {
                    Toast.makeText(mContext, "数量超出", Toast.LENGTH_LONG).show();
                }else if(bigDecimal.intValue()<big.intValue()){
                    Toast.makeText(mContext, "数量超出", Toast.LENGTH_LONG).show();
                }else {
                    if (listener != null) {
                        listener.onClick(this, false);
                    }
                    this.dismiss();
                }

                break;
            case R.id.submit:
                if (listener != null) {
                    listener.onClick(this, true);
                }
                this.dismiss();
                break;
        }
    }


    public interface OnCloseListener {
        void onClick(Dialog dialog1, boolean confirm);

    }

    /**
     * 截取替换批号
     * @param Batch
     * @return
     */
    public String UpdateBatch(String Batch){
        String batch = Batch.substring(8,12);
        String upBatchs = batch.substring(0,1)+"Y"+batch.substring(2,4);
        return upBatchs;
    }
}
