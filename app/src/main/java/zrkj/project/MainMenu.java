package zrkj.project;

import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.finddreams.baselib.R;
import com.finddreams.baselib.base.BaseActivity;
import com.finddreams.baselib.utils.ActivityUtil;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnTouch;

import zrkj.project.util.Constant;
import zrkj.project.util.Result;
import zrkj.project.zrkj.BuLuRuKu_Activity;
import zrkj.project.zrkj.DiaoBo_Activity;
import zrkj.project.zrkj.FenTuoRkActivity;
import zrkj.project.zrkj.KuCunChaXun_Activity;
import zrkj.project.zrkj.PanDianRuKu_Activity;
import zrkj.project.zrkj.PiHaoSheZhi_Activity;
import zrkj.project.zrkj.PiLiang_XiaoHuo_Activity;
import zrkj.project.zrkj.PoDaiTiaoZheng_Activity;
import zrkj.project.zrkj.RuKuFuHe_Activity;
import zrkj.project.zrkj.RuKuZhiJian_Activity;
import zrkj.project.zrkj.ShengChanShuLiang_Activity;
import zrkj.project.zrkj.TuiHuoSaoMiao_Activity;
import zrkj.project.zrkj.TuiHuo_Activity;
import zrkj.project.zrkj.XiaoHuo_Activity;

@ContentView(R.layout.project_mainmenu)
public class MainMenu extends BaseActivity {
	@ViewInject(R.id.relativeLayout_pymian1)
	private RelativeLayout relativeLayout_pymian1;
	@ViewInject(R.id.relativeLayout_pymian2)
	private RelativeLayout relativeLayout_pymian2;
	@ViewInject(R.id.relativeLayout_pymian3)
	private RelativeLayout relativeLayout_pymian3;
	@ViewInject(R.id.relativeLayout_pymian4)
	private RelativeLayout relativeLayout_pymian4;
	@ViewInject(R.id.relativeLayout_pymian5)
	private RelativeLayout relativeLayout_pymian5;
	@ViewInject(R.id.relativeLayout_pymian6)
	private RelativeLayout relativeLayout_pymian6;
	@ViewInject(R.id.relativeLayout_pymian7)
	private RelativeLayout relativeLayout_pymian7;
	@ViewInject(R.id.relativeLayout_pymian8)
	private RelativeLayout relativeLayout_pymian8;
	@ViewInject(R.id.relativeLayout_pymian9)
	private RelativeLayout relativeLayout_pymian9;
	@ViewInject(R.id.relativeLayout_pymian11)
	private RelativeLayout relativeLayout_pymian11;
	@ViewInject(R.id.relativeLayout_pymian12)
	private RelativeLayout relativeLayout_pymian12;
	@ViewInject(R.id.relativeLayout_pymian13)
	private RelativeLayout relativeLayout_pymian13;
	@Override
	protected void onScan(String barcodeStr) {

	}

	@SuppressWarnings("unchecked")
	@Override
	protected void initView() {
		topTitle.mTvTitle.setText("菜单");
		topTitle.mTvBack.setText("退出登录");
//		Constant.PREMS+=",P8";
		String[] prems= Constant.PREMS.split(",");
		List<String> list = Arrays.asList(prems);

		permissions(list);
	}

	@Override
	protected void initData() {

	}

	@OnTouch({R.id.relativeLayout_pymian1,
			R.id.relativeLayout_pymian2,
			R.id.relativeLayout_pymian3,
			R.id.relativeLayout_pymian4,
			R.id.relativeLayout_pymian5,
			R.id.relativeLayout_pymian6,
			R.id.relativeLayout_pymian7,
			R.id.relativeLayout_pymian8,
			R.id.relativeLayout_pymian9,
			R.id.relativeLayout_pymian10,
			R.id.relativeLayout_pymian11,
			R.id.relativeLayout_pymian12,
			R.id.relativeLayout_pymian13})
	private boolean menu(final View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if(v!=null&&v.getBackground()!=null){
				v.getBackground().setAlpha(20);
				TimerTask task = new TimerTask() {
					@Override
					public void run() {
						new Handler(Looper.getMainLooper()).post(new Runnable() {
							@Override
							public void run() {
								v.getBackground().setAlpha(255);// 设置的透明度
							}
						});
					}
				};
				Timer timer = new Timer();
				timer.schedule(task, 1000);
			}
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			if(v!=null&&v.getBackground()!=null) v.getBackground().setAlpha(255);// 设置的透明度
			switch (v.getId()) {
				case R.id.relativeLayout_pymian1:
					//分包
					ActivityUtil.goToActivity(context, FenTuoRkActivity.class);
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
					ActivityUtil.goToActivity(context, PoDaiTiaoZheng_Activity.class);
					break;
				case R.id.relativeLayout_pymian5:
					//调拨
					ActivityUtil.goToActivity(context, DiaoBo_Activity.class);
					break;
				case R.id.relativeLayout_pymian6:
					//销货
					ActivityUtil.goToActivity(context, XiaoHuo_Activity.class);
					break;
				case R.id.relativeLayout_pymian7:
					//退货
					ActivityUtil.goToActivity(context,TuiHuoSaoMiao_Activity.class);
					break;
				case R.id.relativeLayout_pymian8:
					//退货
					ActivityUtil.goToActivity(context, PiHaoSheZhi_Activity.class);
					break;
				case R.id.relativeLayout_pymian9:
					//盘点入库
					ActivityUtil.goToActivity(context, PanDianRuKu_Activity.class);
					break;
				case R.id.relativeLayout_pymian10:
					//库存查询
					ActivityUtil.goToActivity(context, KuCunChaXun_Activity.class);
					break;
				case R.id.relativeLayout_pymian11:
					//库存查询
					ActivityUtil.goToActivity(context, PiLiang_XiaoHuo_Activity.class);
					break;
				case R.id.relativeLayout_pymian12:
					//生产数量
					ActivityUtil.goToActivity(context, ShengChanShuLiang_Activity.class);
					break;
				case R.id.relativeLayout_pymian13:
					//补录入库
					ActivityUtil.goToActivity(context, BuLuRuKu_Activity.class);
					break;
				default:
					break;
			}
		}
		return true;
	}

	@Override
	protected void requestCallBack(Result ajax, int posi) {

	}

	public void permissions(List<String> list){
		int i=0;
		int left=0;
		int top=15;
		for(String s:list){
			if (s=="P1"||"P1".equals(s)) {
				relativeLayout_pymian1.setVisibility(View.VISIBLE);
				i++;
			}else if (s=="P2"||"P2".equals(s)) {
				relativeLayout_pymian2.setVisibility(View.VISIBLE);
				i++;
			}else if (s=="P3" ||"P3".equals(s)) {
				relativeLayout_pymian3.setVisibility(View.VISIBLE);
				i++;
			}else if (s=="P4"||"P4".equals(s)) {
				relativeLayout_pymian4.setVisibility(View.VISIBLE);
				i++;
			}else if (s=="P5"||"P5".equals(s)) {
				relativeLayout_pymian5.setVisibility(View.VISIBLE);
				i++;
			}else if (s=="P6" ||"P6".equals(s)) {
				relativeLayout_pymian6.setVisibility(View.VISIBLE);
				i++;
			}else if (s=="P7"||"P7".equals(s)) {
				relativeLayout_pymian7.setVisibility(View.VISIBLE);
				i++;
			}else if (s=="P8"||"P8".equals(s)) {
				relativeLayout_pymian8.setVisibility(View.VISIBLE);
				i++;
			}else if (s=="P9"||"P9".equals(s)) {
				relativeLayout_pymian9.setVisibility(View.VISIBLE);
				i++;
			}else if (s=="P11"||"P11".equals(s)) {
				relativeLayout_pymian11.setVisibility(View.VISIBLE);
				i++;
			}else if (s=="P12"||"P12".equals(s)) {
				relativeLayout_pymian12.setVisibility(View.VISIBLE);
				i++;
			}else if (s=="P13"||"P13".equals(s)) {
				relativeLayout_pymian13.setVisibility(View.VISIBLE);
				i++;
			}else if(i%3==0&&i>=3){
				//top-=zujiemian_activity.getTop(this);
			}
		}
	}
}
