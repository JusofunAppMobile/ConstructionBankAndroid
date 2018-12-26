package com.qxb.jianhang.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import com.jusfoun.baselibrary.model.UserModel;
import com.qxb.jianhang.ui.base.BaseBackActivity;
import com.qxb.jianhang.ui.util.AppUtils;


/**
 * @author liuguangdan
 * @version create at 2018/8/29/029 19:41
 * @Email lgd@jusfoun.com
 * @Description ${}
 */
public class SplashActivity extends BaseBackActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public int getLayoutResId() {
        return 0;
    }

    @Override
    public void initDatas() {


    }

    @Override
    public void initView() {

    }

    @Override
    public void initAction() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                UserModel user = AppUtils.getUserInfo();
                Class<?> clazz = MainActivity.class;
                if (user == null || TextUtils.isEmpty(user.id))
                    clazz = LoginActivity.class;
                Intent intent = new Intent(mContext, clazz);
                setIntentNoAnim(intent);
                startActivity(intent);
                finishDelay();
            }
        }, 0);
    }

    @Override
    public boolean isSetStatusBar() {
        return false;
    }

    @Override
    public void onBackPressed() {

    }





}
