package com.qxb.jianhang.ui.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.jusfoun.baselibrary.Util.ToastUtils;
import com.jusfoun.baselibrary.net.Api;
import com.jusfoun.baselibrary.net.NetModel;
import com.jusfoun.baselibrary.widget.xRecyclerView.XRecyclerView;
import com.qxb.jianhang.R;
import com.qxb.jianhang.ui.adapter.FollowAdapter;
import com.qxb.jianhang.ui.base.BaseBackActivity;
import com.qxb.jianhang.ui.constant.Constant;
import com.qxb.jianhang.ui.data.TrackRecordDataModel;
import com.qxb.jianhang.ui.net.ApiService;

import java.util.HashMap;

import rx.functions.Action1;

/**
 * @author zhaoyapeng
 * @version create time:18/8/2810:03
 * @Email zyp@jusfoun.com
 * @Description ${跟进记录activity}
 */
public class FollowUpRecordActivity extends BaseBackActivity {

    private XRecyclerView recycler;
    private FollowAdapter adapter;
    private int pagenumber = 1;
    private String companyId = "";

    @Override
    public int getLayoutResId() {
        return R.layout.activity_follow_record;
    }

    @Override
    public void initDatas() {
        companyId = getIntent().getStringExtra(Constant.COMPANYID);
        adapter = new FollowAdapter(this,companyId);
    }

    @Override
    public void initView() {
        recycler = (XRecyclerView) findViewById(R.id.recycler);
        recycler.setAdapter(adapter);
    }

    @Override
    public void initAction() {
        setTitle("跟进记录");

        recycler.setLayoutManager(new LinearLayoutManager(mContext));

        recycler.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                getListNet(true,true);
            }

            @Override
            public void onLoadMore() {
                getListNet(false,true);
            }
        });

        getListNet(true,true);
    }

    public void add(View view) {
        Intent intent = new Intent(this, FollowAddActivity.class);
        intent.putExtra(FollowAddActivity.COMPANYID, companyId);
        startActivity(intent);
    }


    private void getListNet(final boolean isRefresh,boolean isLoding) {
        if(isLoding) {
            showLoadDialog();
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put("companyId", companyId);
//        map.put("userId", AppUtils.getUserInfo().id);

        map.put("pagenumber", isRefresh ? 1 : pagenumber);
        map.put("pagesize", "10");
        addNetwork(Api.getInstance().getService(ApiService.class).getFollowList(map), new Action1<NetModel>() {
            @Override
            public void call(NetModel net) {
                complete();
                hideLoadDialog();
                if (net.success()) {
                    if (isRefresh) {
                        pagenumber = 1;
                    } else {
                        pagenumber++;
                    }
//                    PreferenceUtils.setString(mContext, Constant.PRE_USER_INFO, new Gson().toJson(net.data));
//                    Intent intent = new Intent(mContext, MainActivity.class);
//                    setIntentNoAnim(intent);
//                    startActivity(intent);
//                    finishDelay();

                    TrackRecordDataModel model = net.dataToObject(TrackRecordDataModel.class);
                    if (isRefresh) {
                        adapter.refreshList(model.list);
                    } else {
                        adapter.addList(model.list);
                    }

                    if (adapter.getItemCount() >= model.totalCount) {
                        recycler.setLoadingMoreEnabled(false);
                    } else {
                        recycler.setLoadingMoreEnabled(true);
                    }

                } else {
                    showToast(net.msg);
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                complete();
                hideLoadDialog();
                ToastUtils.showHttpError();
            }
        });
    }

    private void complete() {
        recycler.loadMoreComplete();
        recycler.refreshComplete();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getListNet(true,false);
    }
}
