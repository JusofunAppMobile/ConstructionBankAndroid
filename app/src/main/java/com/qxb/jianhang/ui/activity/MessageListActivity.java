package com.qxb.jianhang.ui.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.jusfoun.baselibrary.base.BaseAdapter;
import com.jusfoun.baselibrary.event.IEvent;
import com.jusfoun.baselibrary.net.Api;
import com.jusfoun.baselibrary.net.NetModel;
import com.jusfoun.jusfouninquire.view.TitleView;
import com.qxb.jianhang.R;
import com.qxb.jianhang.ui.adapter.MessageAdapter;
import com.qxb.jianhang.ui.base.BaseListActivity;
import com.qxb.jianhang.ui.constant.Constant;
import com.qxb.jianhang.ui.data.MessageAllModel;
import com.qxb.jianhang.ui.event.PushMessageEvent;
import com.qxb.jianhang.ui.net.ApiService;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;

/**
 * @author liuguangdan
 * @version create at 2018-8-29 16:35:47
 * @Email lgd@jusfoun.com
 * @Description ${我的消息列表页面}
 */
public class MessageListActivity extends BaseListActivity {

    @BindView(R.id.titleView)
    TitleView titleView;

    public static final int TYPE_MULTIPLE = 0;// 多个（个人中心）
    public static final int TYPE_SINGLE = 1;// 单个（企业列表）
    public static final String TYPE = "type";
    private int type;
    private String companyId;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_message_list;
    }

    @Override
    public void initDatas() {
        companyId = getIntent().getStringExtra(Constant.COMPANYID);
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
    }

    @Override
    public void initAction() {
        titleView.setTitle("消息推送");
    }

    public void pushMessage(View view) {
        Intent intent = new Intent(this, MessagePushActivity.class);
        intent.putExtra(MessageListActivity.TYPE, type);
        intent.putExtra(Constant.COMPANYID, companyId);
        startActivity(intent);
    }

    @Override
    protected BaseAdapter getAdapter() {
        type = getIntent().getIntExtra(TYPE, 0);
        return new MessageAdapter(this, type);
    }

    @Override
    protected void loadData() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageSize", pageSize);
        map.put("pageIndex", pageIndex);
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
    public void onEvent(IEvent event) {
        super.onEvent(event);
        if(event instanceof PushMessageEvent)
            refresh();
    }
}
