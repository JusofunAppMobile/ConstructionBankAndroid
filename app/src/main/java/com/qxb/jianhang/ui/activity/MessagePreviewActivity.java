package com.qxb.jianhang.ui.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jusfoun.baselibrary.Util.ToastUtils;
import com.jusfoun.baselibrary.net.Api;
import com.jusfoun.baselibrary.net.NetModel;
import com.jusfoun.baselibrary.widget.xRecyclerView.XRecyclerView;
import com.jusfoun.jusfouninquire.view.TitleView;
import com.qxb.jianhang.R;
import com.qxb.jianhang.ui.adapter.MessageCompanyAdapter;
import com.qxb.jianhang.ui.base.BaseBackActivity;
import com.qxb.jianhang.ui.data.MessageCompanyModel;
import com.qxb.jianhang.ui.event.PushMessageEvent;
import com.qxb.jianhang.ui.net.ApiService;
import com.qxb.jianhang.ui.util.AppUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.functions.Action1;

/**
 * @author liuguangdan
 * @version create at 2018-8-29 16:35:47
 * @Email lgd@jusfoun.com
 * @Description ${我的消息预览页面}
 */
public class MessagePreviewActivity extends BaseBackActivity {

    private XRecyclerView recycler;
    private MessageCompanyAdapter adapter;

    @BindView(R.id.titleView)
    TitleView titleView;

    @BindView(R.id.tvContent)
    TextView tvContent;
    @BindView(R.id.tvTime)
    TextView tvTime;

    List<MessageCompanyModel.MessageCompanyItemModel> list = new ArrayList<>();

    private String content;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_message_preview;
    }

    @Override
    public void initDatas() {
        adapter = new MessageCompanyAdapter(this, false);
    }

    @Override
    public void initView() {
        recycler = (XRecyclerView) findViewById(R.id.recycler);
        recycler.setAdapter(adapter);

        content = getIntent().getStringExtra("content");

    }

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");

    private String parseDate(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        String date = sdf.format(calendar.getTime());

        if (date.equals(sdf.format(new Date()))) { // 今天

            StringBuilder sb = new StringBuilder();
            sb.append(calendar.get(Calendar.HOUR_OF_DAY) < 12 ? "上午 " : "下午 ");
            sb.append(sdf2.format(calendar.getTime()));
            return sb.toString();
        }
        return date;
    }

    @Override
    public void initAction() {

        ButterKnife.bind(this);
        titleView.setTitle("预览");

        tvContent.setText(content);
        tvTime.setText(parseDate(System.currentTimeMillis()));

        recycler.setLayoutManager(new LinearLayoutManager(mContext));

        String val = getIntent().getStringExtra("list");
        if (!TextUtils.isEmpty(val)) {
            try {
                JSONArray arr = new JSONArray(val);
                Gson gson = new Gson();
                list.clear();
                for (int i = 0; i < arr.length(); i++) {
                    list.add(gson.fromJson(arr.get(i).toString(), MessageCompanyModel.MessageCompanyItemModel.class));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        adapter.refreshList(list);
    }


    private String getIds() {
        StringBuilder sb = new StringBuilder();
        for (MessageCompanyModel.MessageCompanyItemModel model : list) {
            sb.append(model.entid + ",");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    public void commit(View view) {
        showLoadDialog();
        HashMap<String, Object> map = new HashMap<>();
        map.put("companyId", getIds());
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

}
