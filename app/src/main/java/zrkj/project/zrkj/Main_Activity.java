package zrkj.project.zrkj;

import android.view.MotionEvent;
import android.view.View;

import com.finddreams.baselib.R;
import com.finddreams.baselib.base.BaseActivity;
import com.finddreams.baselib.utils.ActivityUtil;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.event.OnTouch;

import zrkj.project.util.Result;

@ContentView(R.layout.activity_zujiemian)
public class Main_Activity extends BaseActivity {
    @SuppressWarnings("unchecked")
    @Override
    protected void initView() {
        topTitle.mTvTitle.setText("主界面");
        topTitle.mTvBack.setText("退出登录");
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onScan(String barcodeStr) {
    }


    @OnTouch({R.id.relativeLayout_pymian1,
            R.id.relativeLayout_pymian2,
            R.id.relativeLayout_pymian3,
            R.id.relativeLayout_pymian4,
            R.id.relativeLayout_pymian5,
            R.id.relativeLayout_pymian6,
            R.id.relativeLayout_pymian7,
            R.id.relativeLayout_pymian8})
    private void menu(final View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {

        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            //if(v!=null&&v.getBackground()!=null) v.getBackground().setAlpha(255);// 设置的透明度
            switch (v.getId()) {
                case R.id.relativeLayout_pymian1:
                    //分包
                    ActivityUtil.goToActivity(context,FenTuoRkActivity.class);
                    break;
                case R.id.relativeLayout_pymian2:
                    //入库复核
                    ActivityUtil.goToActivity(context, RuKuFuHe_Activity.class);
                    break;
                case R.id.relativeLayout_pymian3:
                    //入库质检
                    ActivityUtil.goToActivity(context, RuKuZhiJian_Activity.class);
                    break;
                case R.id.relativeLayout_pymian4:
                    //破袋调整
                    ActivityUtil.goToActivity(context,PoDaiTiaoZheng_Activity.class);
                    break;
                case R.id.relativeLayout_pymian5:
                    //调拨
                    ActivityUtil.goToActivity(context,DiaoBo_Activity.class);
                    break;
                case R.id.relativeLayout_pymian6:
                    //销货
                    ActivityUtil.goToActivity(context,XiaoHuo_Activity.class);
                    break;
                case R.id.relativeLayout_pymian7:
                    //退货
                    ActivityUtil.goToActivity(context,TuiHuo_Activity.class);
                    break;
                case R.id.relativeLayout_pymian8:
                    //退货
                    ActivityUtil.goToActivity(context,PiHaoSheZhi_Activity.class);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void requestCallBack(Result ajax, int posi) {

    }
}
