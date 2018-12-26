package com.qxb.jianhang.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jusfoun.baselibrary.Util.MD5Util;
import com.jusfoun.baselibrary.Util.PreferenceUtils;
import com.jusfoun.baselibrary.Util.RegularUtils;
import com.jusfoun.baselibrary.Util.ToastUtils;
import com.jusfoun.baselibrary.net.Api;
import com.jusfoun.baselibrary.net.NetModel;
import com.qxb.jianhang.R;
import com.qxb.jianhang.ui.base.BaseBackActivity;
import com.qxb.jianhang.ui.constant.Constant;
import com.qxb.jianhang.ui.data.OrganModel;
import com.qxb.jianhang.ui.event.FinishLoginEvent;
import com.qxb.jianhang.ui.net.ApiService;
import com.qxb.jianhang.ui.util.SendMessage;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.functions.Action1;

/**
 * @author liuguangdan
 * @version create at 2018/9/3/003 11:21
 * @Email lgd@jusfoun.com
 * @Description ${注册页面}
 */
public class RegisterActivity extends BaseBackActivity implements View.OnClickListener {

    @BindView(R.id.etUserName)
    EditText etUserName;
    @BindView(R.id.etRealName)
    EditText etRealName;
    @BindView(R.id.etPassword2)
    EditText etPassword2;
    @BindView(R.id.etPassword1)
    EditText etPassword1;
    @BindView(R.id.etOrgan)
    TextView etOrgan;
    @BindView(R.id.etPhone)
    EditText etPhone;
    @BindView(R.id.vSendCode)
    TextView vSendCode;
    @BindView(R.id.etCode)
    EditText etCode;
    @BindView(R.id.img_back)
    ImageView backImg;

    private SendMessage sendMessage;

    private List<OrganModel> organList = new ArrayList<>();

    @Override
    public int getLayoutResId() {
        return R.layout.activity_register;
    }

    @Override
    public void initDatas() {
        loadOrganData(false);
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        etOrgan.setOnClickListener(this);

        sendMessage = SendMessage.newInstant(this)
                .setClickView(vSendCode)
                .setInputView(etUserName)
                .setTipText("获取验证码")
                .setTime(60);
    }

    @Override
    public void initAction() {
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void loadOrganData(final boolean showDialog) {
        HashMap<String, Object> map = new HashMap<>();
        addNetwork(Api.getInstance().getService(ApiService.class).searchOrgList(map), new Action1<NetModel>() {
            @Override
            public void call(NetModel net) {
                hideLoadDialog();
                if (net.success()) {
                    organList.addAll(net.dataToList("list", OrganModel.class));
                    // 移除 name 为空的
                    Iterator<OrganModel> iterator = organList.iterator();
                    while (iterator.hasNext()) {
                        OrganModel model = iterator.next();
                        if (TextUtils.isEmpty(model.name)) {
                            iterator.remove();
                        }
                    }
                    if (showDialog)
                        showOrginDialog();
                } else
                    showToast(net.msg);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                hideLoadDialog();
                ToastUtils.showHttpError();
            }
        });
    }

    public void register(View view) {

        if (TextUtils.isEmpty(getValue(etUserName))) {
            showToast("请输入用户名");
            return;
        }

        if (TextUtils.isEmpty(getValue(etRealName))) {
            showToast("请输入真实姓名");
            return;
        }

        if (TextUtils.isEmpty(getValue(etPhone))) {
            showToast("请输入手机号");
            return;
        }

        if (!RegularUtils.checkMobile(getValue(etPhone))) {
            showToast("请输入正确的手机号");
            return;
        }

        if (TextUtils.isEmpty(getValue(etCode))) {
            showToast("请输入验证码");
            return;
        }

        if (TextUtils.isEmpty(getValue(etOrgan))) {
            showToast("请选择所属机构");
            return;
        }

        if (TextUtils.isEmpty(getValue(etPassword1))) {
            showToast("请输入密码");
            return;
        }

        if (getValue(etPassword1).length() < 6) {
            showToast("密码长度至少需要6位");
            return;
        }

        if (!RegularUtils.checkLetterAndNum(getValue(etPassword1))) {
            showToast("密码格式不正确");
            return;
        }

        if (TextUtils.isEmpty(getValue(etPassword2))) {
            showToast("请输入确认密码");
            return;
        }

        if (!getValue(etPassword1).equals(getValue(etPassword2))) {
            showToast("两次输入密码不一致");
            return;
        }

        showLoadDialog();

        HashMap<String, Object> map = new HashMap<>();
        map.put("phone", getValue(etPhone));
        map.put("code", getValue(etCode));
        map.put("username", getValue(etUserName)+"@ccb.com");
        map.put("realName", getValue(etRealName));
        map.put("organizationId", etOrgan.getTag());
        map.put("password", MD5Util.getMD5Str(getValue(etPassword1)));

        addNetwork(Api.getInstance().getService(ApiService.class).register(map), new Action1<NetModel>() {
            @Override
            public void call(NetModel net) {
                hideLoadDialog();
                if (net.success()) {
                    showToast("注册成功");
                    PreferenceUtils.setString(mContext, Constant.PRE_USER_INFO, new Gson().toJson(net.data));
                    Intent intent = new Intent(mContext, MainActivity.class);
                    setIntentNoAnim(intent);
                    startActivity(intent);
                    finishDelay();
                    EventBus.getDefault().post(new FinishLoginEvent());
                } else
                    showToast(net.msg);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                hideLoadDialog();
                ToastUtils.showHttpError();
            }
        });
    }

    /**
     * 列表
     */
    private void showOrginDialog() {

        if (organList.isEmpty()) {
            showToast("暂无组织机构数据");
            return;
        }

        String[] array = new String[organList.size()];
        for (int i = 0; i < organList.size(); i++) {
            array[i] = organList.get(i).name;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请选择所属机构");
        builder.setItems(array, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                OrganModel model = organList.get(which);
                etOrgan.setText(model.name);
                etOrgan.setTag(model.id);
            }
        });
        builder.create().show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.etOrgan:
                if (organList.isEmpty()) {
                    showLoadDialog();
                    loadOrganData(true);
                } else
                    showOrginDialog();
                break;
        }
    }

    @OnClick(R.id.vSendCode)
    public void onViewClicked() {

        String email = etUserName.getText().toString();
        if (TextUtils.isEmpty(email)) {
            showToast("请输入邮箱");
            return;
        }
        if (!RegularUtils.checkEmail(email)) {
            showToast("请输入正确的邮箱");
            return;
        }

        sendMessage.start();
        //  发送短信

        HashMap<String, Object> map = new HashMap<>();
        map.put("email", email);
        addNetwork(Api.getInstance().getService(ApiService.class).sendEmailCode(map), new Action1<NetModel>() {
            @Override
            public void call(NetModel net) {
                if (net.success()) {
                    showToast("邮箱验证码已发送成功，请注意查收");
                } else {
                    showToast(net.msg);
                    sendMessage.reset();
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                ToastUtils.showHttpError();
                sendMessage.reset();
            }
        });
    }
}
