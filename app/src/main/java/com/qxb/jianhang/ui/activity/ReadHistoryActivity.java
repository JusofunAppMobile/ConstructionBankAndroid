package com.qxb.jianhang.ui.activity;

import com.google.gson.Gson;
import com.jusfoun.baselibrary.base.BaseAdapter;
import com.jusfoun.jusfouninquire.view.TitleView;
import com.qxb.jianhang.R;
import com.qxb.jianhang.ui.adapter.ReadHistoryAdapter;
import com.qxb.jianhang.ui.base.BaseListActivity;
import com.qxb.jianhang.ui.data.HistoryModel;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author liuguangdan
 * @version create at 2018/12/11/011 14:55
 * @Email lgd@jusfoun.com
 * @Description ${}
 */
public class ReadHistoryActivity extends BaseListActivity {

    @BindView(R.id.titleView)
    TitleView titleView;

    @Override
    protected BaseAdapter getAdapter() {
        return new ReadHistoryAdapter(this);
    }

    @Override
    protected void loadData() {
        List<HistoryModel> statList = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(getIntent().getStringExtra("model"));
            Gson gson = new Gson();
            for (int i = 0; i < array.length(); i++) {
                HistoryModel model = gson.fromJson(array.getString(i), HistoryModel.class);
                statList.add(model);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        completeLoadData(statList);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_recycler_common;
    }

    @Override
    public void initDatas() {
        ButterKnife.bind(this);
        titleView.setTitle("浏览记录");
    }

    @Override
    protected boolean isLoadMoreEnable() {
        return false;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initAction() {

    }
}
