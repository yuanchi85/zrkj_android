package zrkj.Doalog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.finddreams.baselib.R;
import com.uhf.uhf.UHF6.tools.StringUtils;

import java.math.BigDecimal;

import zrkj.project.entity.CmsAssembly;
import zrkj.project.entity.CmsProduct;
import zrkj.project.zrkj.TuiHuoSaoMiao_Activity;
import zrkj.project.zrkj.TuiHuo_Activity;
import zrkj.project.zrkj.XiaoHuo_Activity;


/**
 * 时间：2019/7/19
 * 项目名：zrkj_tm_pda
 * 包名：com.zrkj.Base
 * 类名：Activity
 * 作者：杨亚明
 **/

public class TuiHuoDialog extends Dialog implements View.OnClickListener {
    private TextView contentTxt;
    private TextView titleTxt;
    private TextView submitTxt;
    private TextView cancelTxt;

    private TextView pinghao, mingcheng, guigexinghao,danwei;
    private EditText shuliang;
    private EditText pici;
    private Context mContext;
    private String content;
    private OnCloseListener listener;
    private String positiveName;
    private String negativeName;
    private String title;
    private ImageView view;
    private ImageView view2;
    private String shishishuliang;

    private BigDecimal bigmal = new BigDecimal("0");


    public TuiHuoDialog(Context context) {
        super(context);
        this.mContext = context;
    }


    public TuiHuoDialog(Context context, int themeResId, String content) {
        super(context, themeResId);
        this.mContext = context;
        this.content = content;
    }


    public TuiHuoDialog(Context context, int themeResId, String content, OnCloseListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.content = content;
        this.listener = listener;


    }


    protected TuiHuoDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }


    public TuiHuoDialog setTitle(String title) {
        this.title = title;
        return this;
    }


    public TuiHuoDialog setPositiveButton(String name) {
        this.positiveName = name;
        return this;
    }


    public TuiHuoDialog setNegativeButton(String name) {
        this.negativeName = name;
        return this;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xiaohuo_common_layout);
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

        pinghao = (TextView) findViewById(R.id.wuliaobianhao);
        mingcheng = (TextView) findViewById(R.id.wuliaomingcheng);
        guigexinghao = (TextView) findViewById(R.id.guigexinghao);
        danwei = (TextView) findViewById(R.id.danwei);
        shuliang = (EditText) findViewById(R.id.shuliang);
        pici = (EditText) findViewById(R.id.pici);

        if(StringUtils.isNotEmpty(TuiHuoSaoMiao_Activity.mingcheng)){
            mingcheng.setText(TuiHuoSaoMiao_Activity.mingcheng);
        }
        if(StringUtils.isNotEmpty(TuiHuoSaoMiao_Activity.bianhao)){
            pinghao.setText(TuiHuoSaoMiao_Activity.bianhao);
        }
        if(StringUtils.isNotEmpty(TuiHuoSaoMiao_Activity.guigexinghao)){
            guigexinghao.setText(TuiHuoSaoMiao_Activity.guigexinghao);
        }
        if(StringUtils.isNotEmpty(TuiHuoSaoMiao_Activity.danwei)){
            danwei.setText(TuiHuoSaoMiao_Activity.danwei);
        }

    }

    public String getshuliang() {
        return shuliang.getText().toString();
    }


    public String getPiCi(){
        return pici.getText().toString();
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel:
                BigDecimal big = new BigDecimal(shuliang.getText().toString());
                if(pici.getText().toString().equals("") || pici.getText().toString() == null){
                    Toast.makeText(mContext, "批次不能为空", Toast.LENGTH_LONG).show();
                }else  if (!pici.getText().toString().equals("")) {
                    pici.setText(pici.getText().toString().replaceAll("x","X"));
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
}
