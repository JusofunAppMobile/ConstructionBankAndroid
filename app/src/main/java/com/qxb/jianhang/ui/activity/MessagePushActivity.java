package com.qxb.jianhang.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.jusfoun.baselibrary.Util.ToastUtils;
import com.jusfoun.baselibrary.base.BaseAdapter;
import com.jusfoun.baselibrary.event.IEvent;
import com.jusfoun.baselibrary.net.Api;
import com.jusfoun.baselibrary.net.NetModel;
import com.jusfoun.jusfouninquire.view.TitleView;
import com.qxb.jianhang.R;
import com.qxb.jianhang.ui.adapter.MessagePushAdapter;
import com.qxb.jianhang.ui.base.BaseListActivity;
import com.qxb.jianhang.ui.constant.Constant;
import com.qxb.jianhang.ui.data.MessageAllModel;
import com.qxb.jianhang.ui.event.CopyMessageEvent;
import com.qxb.jianhang.ui.event.PushMessageEvent;
import com.qxb.jianhang.ui.net.ApiService;
import com.qxb.jianhang.ui.util.AppUtils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.functions.Action1;

/**
 * @author liuguangdan
 * @version create at 2018-8-29 16:35:47
 * @Email lgd@jusfoun.com
 * @Description ${推送消息页面}
 */
public class MessagePushActivity extends BaseListActivity {

    private View headerView;

    @BindView(R.id.titleView)
    TitleView titleView;
    @BindView(R.id.button)
    Button pushBtn;


    EditText etInput;

    private int type;

    private String companyId = "";

    @Override
    public int getLayoutResId() {
        return R.layout.activity_message_push;
    }

    @Override
    public void initDatas() {

    }

    @Override
    public void initView() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        headerView = LayoutInflater.from(this).inflate(R.layout.view_message_push_header, null);
        headerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        etInput = (EditText) headerView.findViewById(R.id.etInput);
        ButterKnife.bind(this);

        recycler.addHeaderView(headerView);
        type = getIntent().getIntExtra(MessageListActivity.TYPE, 0);
        if (type == MessageListActivity.TYPE_SINGLE) {
            pushBtn.setText("推送");
            companyId = getIntent().getStringExtra(Constant.COMPANYID);
        } else {
            pushBtn.setText("下一步");
        }
        titleView.setTitle("消息编辑");
    }

    @Override
    public void initAction() {

    }

    public void next(View view) {
        if (TextUtils.isEmpty(getValue(etInput))) {
            showToast("请输入消息内容");
            return;
        }


        if (type == MessageListActivity.TYPE_SINGLE) {
            commit();
        } else {

            Intent intent = new Intent(this, MessageCompanyActivity.class);
            intent.putExtra("content", getValue(etInput));
            startActivity(intent);
        }
    }

    @Override
    public void onEvent(IEvent event) {
        super.onEvent(event);
        if (event instanceof CopyMessageEvent) {
            etInput.setText(((CopyMessageEvent) event).content);
        } else if (event instanceof PushMessageEvent) {
            finish();
        }
    }

    @Override
    protected BaseAdapter getAdapter() {

        return new MessagePushAdapter(this);
    }

    @Override
    protected void loadData() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageSize", pageSize);
        map.put("pageIndex", pageIndex);
        if (TextUtils.isEmpty(companyId))
            companyId = getIntent().getStringExtra(Constant.COMPANYID);
        if (!TextUtils.isEmpty(companyId))
            map.put("companyId", companyId);
        addNetwork(Api.getInstance().getService(ApiService.class).messageList(map), new Action1<NetModel>() {
            @Override
            public void call(NetModel net) {
                if (net.success()) {
                    MessageAllModel model = net.dataToObject("list", MessageAllModel.class);
                    completeLoadData(model.list);
                } else {
                    completeLoadData(null);
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                completeLoadDataError();
            }
        });
    }

    @Override
    protected boolean isRefreshEnable() {
        return false;
    }

    public void commit() {
        showLoadDialog();
        HashMap<String, Object> map = new HashMap<>();
        map.put("companyId", companyId);
        map.put("message", getValue(etInput));
        map.put("userId", AppUtils.getUserInfo().id);


        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(map));
        addNetwork(Api.getInstance().getService(ApiService.class).pushMessage(body), new Action1<NetModel>() {
            @Override
            public void call(NetModel net) {
                hideLoadDialog();
                if (net.success()) {
                    showToast("提交成功");
                    finish();
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
}
