package com.qxb.jianhang.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jusfoun.baselibrary.Util.LogUtil;
import com.jusfoun.baselibrary.Util.ToastUtils;
import com.jusfoun.baselibrary.base.BaseAdapter;
import com.jusfoun.baselibrary.widget.xRecyclerView.ProgressStyle;
import com.jusfoun.baselibrary.widget.xRecyclerView.XRecyclerView;
import com.qxb.jianhang.R;

import java.util.List;

/**
 * @author liuguangdan
 * @version create at 2018/9/12/012 15:23
 * @Email lgd@jusfoun.com
 * @Description ${}
 */
public abstract class BaseListActivity extends BaseBackActivity {

    protected XRecyclerView recycler;
    protected BaseAdapter adapter;

    private boolean isLoadingData = false;
    private boolean isLoadMoreData = false;

    public final static int INIT_PAGE_INDEX = 1;
    protected int pageIndex = INIT_PAGE_INDEX;
    protected int pageSize = 20;

    protected View vEmpty;
    private TextView tvEmpty;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recycler = (XRecyclerView) findViewById(R.id.recycler);
        adapter = getAdapter();
        if (recycler == null)
            throw new RuntimeException("not found R.id.recycle.");
        if (adapter == null)
            throw new RuntimeException("adapter can not be null.");
        if (!isLoadMoreEnable())
            pageSize = Integer.MAX_VALUE;

        vEmpty = findViewById(R.id.vEmpty);
        if (vEmpty != null) {
            tvEmpty = (TextView) vEmpty.findViewById(R.id.tvEmpty);
            vEmpty.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vEmpty.setVisibility(View.GONE);
                    refresh();
                }
            });
        }

        recycler.setLoadingMoreEnabled(isLoadMoreEnable());
        recycler.setPullRefreshEnabled(isRefreshEnable());

        recycler.setAdapter(adapter);
        recycler.setLayoutManager(getLayoutManager());

        recycler.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);


        recycler.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                refresh();
            }

            @Override
            public void onLoadMore() {
                if (isLoadingData)
                    return;
                isLoadingData = true;
                LogUtil.e(">>>加载更多, pageIndex=" + pageIndex);
                isLoadMoreData = true;
                loadData();
            }
        });

        if (isAutoLoad()) {
            refresh();
        }
    }

    protected abstract BaseAdapter getAdapter();

    protected String getEmptyTipText() {
        return "没有相关数据";
    }

    protected String getHttpErrorTip() {
        return "网络连接错误，点击重试";
    }

    /**
     * 访问接口加载数据
     */
    protected abstract void loadData();

    protected RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(mContext);
    }

    /**
     * 是否能加载更多
     *
     * @return
     */
    protected boolean isLoadMoreEnable() {
        return true;
    }

    /**
     * 是否能刷新页面
     *
     * @return
     */
    protected boolean isRefreshEnable() {
        return true;
    }

    /**
     * 是否打开页面就开始加载
     *
     * @return
     */
    protected boolean isAutoLoad() {
        return true;
    }

    public void refresh() {
        if (isLoadingData)
            return;
        isLoadingData = true;
        pageIndex = INIT_PAGE_INDEX;
        recycler.setLoadingMoreEnabled(isLoadMoreEnable());
        LogUtil.e(">>>正在刷新, pageIndex=" + pageIndex);
        loadData();
    }

    protected void completeLoadData(List list) {
        completeLoadData(list, false);
    }

    /**
     * 网络错误时调用
     */
    protected void completeLoadDataError() {
        completeLoadData(null, true);
    }

    private void completeLoadData(List list, boolean isHttpError) {
        if (isHttpError) {
            if (pageIndex == INIT_PAGE_INDEX)
                showEmptyView(getHttpErrorTip());
            else
                ToastUtils.showHttpError();
        } else {
            if (list == null || list.isEmpty()) {
                if (pageIndex == INIT_PAGE_INDEX)
                    showEmptyView(getEmptyTipText());
                else
                    recycler.setLoadingMoreEnabled(false);
            } else {
                if (isLoadMoreData)
                    adapter.addList(list);
                else
                    adapter.refreshList(list);

                if (list.size() < pageSize)
                    recycler.setLoadingMoreEnabled(false);
                pageIndex++;
            }
        }

        if (isLoadMoreData)
            recycler.loadMoreComplete();
        else
            recycler.refreshComplete();

        isLoadMoreData = false;
        if (isLoadingData)
            isLoadingData = false;
    }

    private void showEmptyView(String text) {
        if (vEmpty != null) {
            vEmpty.setVisibility(View.VISIBLE);
            if (tvEmpty != null) {
                tvEmpty.setText(text);
            }
        }
    }

}
