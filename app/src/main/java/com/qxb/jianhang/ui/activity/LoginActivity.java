package com.qxb.jianhang.ui.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;
import com.jusfoun.baselibrary.Util.MD5Util;
import com.jusfoun.baselibrary.Util.PreferenceUtils;
import com.jusfoun.baselibrary.Util.ToastUtils;
import com.jusfoun.baselibrary.event.IEvent;
import com.jusfoun.baselibrary.net.Api;
import com.jusfoun.baselibrary.net.NetModel;
import com.qxb.jianhang.R;
import com.qxb.jianhang.ui.base.BaseBackActivity;
import com.qxb.jianhang.ui.constant.Constant;
import com.qxb.jianhang.ui.event.FinishLoginEvent;
import com.qxb.jianhang.ui.net.ApiService;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;

/**
 * @author liuguangdan
 * @version create at 2018/9/3/003 9:17
 * @Email lgd@jusfoun.com
 * @Description ${登录页面}
 */
public class LoginActivity extends BaseBackActivity implements View.OnClickListener {

    @BindView(R.id.etAccount)
    EditText etAccount;
    @BindView(R.id.etPassword)
    EditText etPassword;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_login;
    }

    @Override
    public void initDatas() {

    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        findViewById(R.id.vReg).setOnClickListener(this);
    }

    @Override
    public void initAction() {

    }

    public void login(View view) {
        if (TextUtils.isEmpty(getValue(etAccount))) {
            showToast("请输入账号");
            return;
        }
        if (TextUtils.isEmpty(getValue(etPassword))) {
            showToast("请输入密码");
            return;
        }
        showLoadDialog();

        HashMap<String, Object> map = new HashMap<>();
        String userName = getValue(etAccount);

        if(!userName.endsWith("@ccb.com")){
            userName = userName+"@ccb.com";
        }
        map.put("username",userName);
        map.put("password", MD5Util.getMD5Str(getValue(etPassword)));
        addNetwork(Api.getInstance().getService(ApiService.class).login(map), new Action1<NetModel>() {
            @Override
            public void call(NetModel net) {
                hideLoadDialog();
                if (net.success()) {
                    PreferenceUtils.setString(mContext, Constant.PRE_USER_INFO, new Gson().toJson(net.data));
                    Intent intent = new Intent(mContext, MainActivity.class);
                    setIntentNoAnim(intent);
                    startActivity(intent);
                    finishDelay();
                } else {
                    showToast(net.msg);
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                hideLoadDialog();
                ToastUtils.showHttpError();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.vReg:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
        }
    }

    @Override
    public void onEvent(IEvent event) {
        super.onEvent(event);
        if (event instanceof FinishLoginEvent) {
            finishDelay();
        }
    }

    @Override
    public boolean isNeedSwipe() {
        return false;
    }
}
