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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import zrkj.project.entity.CmsAssembly;
import zrkj.project.entity.CmsProduct;
import zrkj.project.zrkj.XiaoHuo_Activity;


/**
 * 时间：2019/7/19
 * 项目名：zrkj_tm_pda
 * 包名：com.zrkj.Base
 * 类名：Activity
 * 作者：杨亚明
 **/

public class BaseCmsProductDialog extends Dialog implements View.OnClickListener {
    private TextView contentTxt;
    private TextView titleTxt;
    private TextView submitTxt;
    private TextView cancelTxt;
    private Spinner spinner2;
    private TextView bianhao,mingcheng,xinhao,danwei;
    private Context mContext;
    private String content;
    private OnCloseListener listener;
    private String positiveName;
    private String negativeName;
    private String title;
    private ImageView view;
    private String shishishuliang;
    private List<CmsProduct> list = new ArrayList<>();

    private BigDecimal bigmal = new BigDecimal("0");


    public BaseCmsProductDialog(Context context) {
        super(context);
        this.mContext = context;
    }


    public BaseCmsProductDialog(Context context, int themeResId, String content) {
        super(context, themeResId);
        this.mContext = context;
        this.content = content;
    }


    public BaseCmsProductDialog(List<CmsProduct> listCms,Context context, int themeResId, String content, OnCloseListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.content = content;
        this.listener = listener;
        this.list = listCms;
    }


    protected BaseCmsProductDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }


    public BaseCmsProductDialog setTitle(String title) {
        this.title = title;
        return this;
    }


    public BaseCmsProductDialog setPositiveButton(String name) {
        this.positiveName = name;
        return this;
    }


    public BaseCmsProductDialog setNegativeButton(String name) {
        this.negativeName = name;
        return this;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.baesprount_layout);
        setCanceledOnTouchOutside(false);
        initView();
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
        spinner2 = (Spinner) findViewById(R.id.spinner2);

        bianhao = (TextView) findViewById(R.id.bianhao);
        mingcheng = (TextView) findViewById(R.id.mingcheng);
        xinhao = (TextView) findViewById(R.id.xinghao);
        danwei = (TextView) findViewById(R.id.danwei);
        spinner2.setAdapter(new MyBaseAdapter<CmsProduct, ListView>(this.mContext, list) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ViewGroup layout = null;
                if (convertView == null) {
                    layout = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.zdyspinner, parent, false);
                } else {
                    layout = (ViewGroup)convertView;
                }
                CmsProduct mbean = this.list.get(position);
                ((TextView)layout.findViewById(R.id.textView1)).setText(mbean.getProductName()+"_"+mbean.getSpecificationModel());
                if (releativeOptionIndex == position) layout.setBackgroundColor(Color.YELLOW);
                return layout;
            }
        });
        spinner2.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                CmsProduct cmsProduct = (CmsProduct) spinner2.getSelectedItem();
                bianhao.setText(cmsProduct.getProductCode());
                mingcheng.setText(cmsProduct.getProductName());
                xinhao.setText(cmsProduct.getSpecificationModel());
                danwei.setText(cmsProduct.getUnit());
            }
            public void onNothingSelected(AdapterView<?> arg0) {
                Map<String, Object> map = (Map<String, Object>) spinner2.getSelectedItem();

            }
        });
    }

    public String getdanwei(){
        return danwei.getText().toString();
    }
    public String getbianhao(){
        return bianhao.getText().toString();
    }
    public String getmingcheng(){
        return mingcheng.getText().toString();
    }
    public String getxinghao(){
        return xinhao.getText().toString();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel:
                if (listener != null) {
                    listener.onClick(this, false);
                }
                this.dismiss();
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


}
