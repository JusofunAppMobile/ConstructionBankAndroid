package com.qxb.jianhang.ui.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jusfoun.baselibrary.Util.ToastUtils;
import com.jusfoun.baselibrary.base.BaseAdapter;
import com.jusfoun.baselibrary.event.IEvent;
import com.jusfoun.baselibrary.net.Api;
import com.jusfoun.baselibrary.net.NetModel;
import com.jusfoun.jusfouninquire.view.TitleView;
import com.qxb.jianhang.R;
import com.qxb.jianhang.ui.adapter.MessageCompanyAdapter;
import com.qxb.jianhang.ui.base.BaseListActivity;
import com.qxb.jianhang.ui.data.MessageCompanyModel;
import com.qxb.jianhang.ui.event.PushMessageEvent;
import com.qxb.jianhang.ui.net.ApiService;
import com.qxb.jianhang.ui.util.AppUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.functions.Action1;

/**
 * @author liuguangdan
 * @version create at 2018-8-29 16:35:47
 * @Email lgd@jusfoun.com
 * @Description ${我的消息选择企业页面}
 */
public class MessageCompanyActivity extends BaseListActivity implements TextWatcher {

    @BindView(R.id.ivSelectAll)
    ImageView ivSelectAll;
    @BindView(R.id.ivDelete)
    ImageView ivDelete;
    @BindView(R.id.etInput)
    EditText etInput;
    @BindView(R.id.titleView)
    TitleView titleView;

    private String content;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_message_select_company;
    }

    @Override
    public void initDatas() {
        adapter = new MessageCompanyAdapter(this);
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
    }

    @Override
    public void initAction() {

        titleView.setTitle("接收对象");

        content = getIntent().getStringExtra("content");
        etInput.addTextChangedListener(this);
    }

    public void commit(View view) {
        StringBuilder sb = new StringBuilder();

        for (Object o : adapter.getList()) {
            MessageCompanyModel.MessageCompanyItemModel model = (MessageCompanyModel.MessageCompanyItemModel) o;
            if (model.select)
                sb.append(model.entid + ",");
        }

        if (sb.length() == 0) {
            showToast("请选择企业");
            return;
        }

        if (sb.toString().endsWith(","))
            sb.deleteCharAt(sb.length() - 1);

        showLoadDialog();
        HashMap<String, Object> map = new HashMap<>();
        map.put("companyId", sb.toString());
        map.put("message", content);
        map.put("userId", AppUtils.getUserInfo().id);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(map));
        addNetwork(Api.getInstance().getService(ApiService.class).pushMessage(body), new Action1<NetModel>() {
            @Override
            public void call(NetModel net) {
                hideLoadDialog();
                if (net.success()) {
                    showToast("提交成功");
                    EventBus.getDefault().post(new PushMessageEvent());
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

    public void preview(View view) {

        List<MessageCompanyModel.MessageCompanyItemModel> list = new ArrayList<>();

        for (Object o : adapter.getList()) {
            MessageCompanyModel.MessageCompanyItemModel model = (MessageCompanyModel.MessageCompanyItemModel) o;
            if (model.select)
                list.add(model);
        }

        if (list.isEmpty()) {
            showToast("请选择企业");
            return;
        }

        Intent intent = new Intent(this, MessagePreviewActivity.class);
        intent.putExtra("list", new Gson().toJson(list));
        intent.putExtra("content", content);
        startActivity(intent);
    }


    @Override
    public void onEvent(IEvent event) {
        super.onEvent(event);
        if (event instanceof PushMessageEvent) {
            finish();
        }
    }

    @OnClick({R.id.ivSelectAll, R.id.tvSelectAll, R.id.vSearch, R.id.ivDelete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivSelectAll:
            case R.id.tvSelectAll:
                for (Object model : adapter.getList()) {
                    MessageCompanyModel.MessageCompanyItemModel cModel = (MessageCompanyModel.MessageCompanyItemModel) model;
                    cModel.select = !ivSelectAll.isSelected();
                }
                adapter.notifyDataSetChanged();
                ivSelectAll.setSelected(!ivSelectAll.isSelected());
                break;
            case R.id.vSearch:
                refresh();
                break;
            case R.id.ivDelete:

                etInput.setText("");
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        ivDelete.setVisibility(s.length() == 0 ? View.INVISIBLE : View.VISIBLE);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    protected BaseAdapter getAdapter() {
        return new MessageCompanyAdapter(this);
    }

    @Override
    protected void loadData() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", etInput.getText().toString());
//        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(map));
        addNetwork(Api.getInstance().getService(ApiService.class).searchMessage(map), new Action1<NetModel>() {
            @Override
            public void call(NetModel net) {
                if (net.success()) {
                    List<MessageCompanyModel.MessageCompanyItemModel> list = new Gson().fromJson(net.list.toString(), new TypeToken<List<MessageCompanyModel.MessageCompanyItemModel>>() {
                    }.getType());
                    completeLoadData(list);
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
}
