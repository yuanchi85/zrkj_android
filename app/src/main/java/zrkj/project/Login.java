package zrkj.project;

import zrkj.project.util.CommonConstant;
import zrkj.project.util.Constant;
import zrkj.project.util.Result;
import zrkj.project.util.UpdateManager;
import zrkj.project.zrkj.Main_Activity;

import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;

import com.finddreams.baselib.R;
import com.finddreams.baselib.base.BaseActivity;
import com.finddreams.baselib.utils.ActivityUtil;
import com.finddreams.baselib.utils.DeviceInfoUtil;
import com.finddreams.baselib.utils.SharePrefUtil;
import com.finddreams.baselib.view.ClearEditText;
import com.google.gson.Gson;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnTouch;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ContentView(R.layout.zrkj_login)
public class Login extends BaseActivity {
    private Drawable mIconPerson;
    private Drawable mIconLock;
    @ViewInject(R.id.userName)
    private ClearEditText userName;
    @ViewInject(R.id.password)
    private ClearEditText password;



    @Override
    protected void initView() {
        topTitle.mTvTitle.setText("康宏粮油");
        mIconPerson = getResources().getDrawable(R.drawable.txt_person_icon);
        mIconPerson.setBounds(5, 1, 60, 50);
        mIconLock = getResources().getDrawable(R.drawable.txt_lock_icon);
        mIconLock.setBounds(5, 1, 60, 50);
        userName.setCompoundDrawables(mIconPerson, null, null, null);
        password.setCompoundDrawables(mIconLock, null, null, null);
        UpdateManager updateManager = new UpdateManager(this);
        updateManager.checkUpdate();
    }

    @Override
    protected void initData() {
        // 填充默认值
        userName.setText(SharePrefUtil.getString(context, "userName", ""));
        password.setText(SharePrefUtil.getString(context, "password", ""));
        SharePrefUtil.saveString(context, "device", DeviceInfoUtil.getOnlyID(this));
        // 程序更新
//		new UpdateManager(context).checkUpdate();
    }


    @OnTouch({R.id.login})
    private boolean login(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            v.getBackground().setAlpha(20);
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            if (beforeLogin())
                login();
            v.getBackground().setAlpha(255);
        }
        return true;
    }

    private boolean beforeLogin() {
        if (userName.getText().toString().equals("")) {
            showCustomToast("请输入账号");
            userName.requestFocus();// 获取焦点
            userName.setSelection(userName.getText().toString().length());
            return false;
        }
        if (password.getText().toString().equals("")) {
            showCustomToast("请输入密码");
            password.requestFocus();// 获取焦点
            password.setSelection(password.getText().toString().length());
            return false;
        }
        return true;
    }

    private void login() {
        Gson gson = new Gson();
        Map<String, Object> ms = new HashMap<String, Object>();
        ms.put("username", userName.getText().toString());
        ms.put("password", password.getText().toString());
        ms.put("device", DeviceInfoUtil.getOnlyID(this));
        request(gson.toJson(ms), "cms/sys/login", "正在登陆...", 1);
    }



    @Override
    protected void onScan(String barcodeStr) {
    }

    @Override
    protected void requestCallBack(Result ajax, int posi) {
        Gson gson = new Gson();
        Map j = gson.fromJson(gson.toJson(ajax.getResult()), Map.class);
        String PREMS =j.get("perms").toString();

        if(isNotEmpty(j.get("token"))){
            Constant.TOKENOUP = j.get("token").toString();
        }
        Constant.PREMS = PREMS;
        ActivityUtil.goToActivity(context,MainMenu.class);
    }

}
