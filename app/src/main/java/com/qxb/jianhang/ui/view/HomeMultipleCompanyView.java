package com.qxb.jianhang.ui.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.jusfoun.baselibrary.Util.PhoneUtil;
import com.jusfoun.baselibrary.Util.ToastUtils;
import com.jusfoun.baselibrary.base.BaseView;
import com.jusfoun.baselibrary.net.Api;
import com.jusfoun.baselibrary.net.NetModel;
import com.jusfoun.baselibrary.widget.xRecyclerView.XRecyclerView;
import com.qxb.jianhang.R;
import com.qxb.jianhang.ui.adapter.CompanyItemAdapter;
import com.qxb.jianhang.ui.data.HomeDataItemModel;
import com.qxb.jianhang.ui.data.SearchListModel;
import com.qxb.jianhang.ui.fragment.MapFragment;
import com.qxb.jianhang.ui.net.ApiService;

import java.util.HashMap;
import java.util.Map;

import rx.functions.Action1;

/**
 * @author zhaoyapeng
 * @version create time:18/8/2919:44
 * @Email zyp@jusfoun.com
 * @Description ${地图 底部弹出多个企业列表}
 */
public class HomeMultipleCompanyView extends BaseView {
    protected XRecyclerView recyclerView;
    protected ImageView viewZhanwei;
    private CompanyItemAdapter companyItemAdapter;

    private int pageSize = 20;
    private int pageIndex = 1;
    private HashMap<String, Object> params;

    public HomeMultipleCompanyView(Context context) {
        super(context);
    }

    public HomeMultipleCompanyView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HomeMultipleCompanyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initData() {
        companyItemAdapter = new CompanyItemAdapter(mContext);
        companyItemAdapter.setShort(true);
    }

    @Override
    protected void initViews() {

        LayoutInflater.from(mContext).inflate(R.layout.view_home_multop_company, this, true);
        recyclerView = (XRecyclerView) findViewById(R.id.recycler_view);
        viewZhanwei = (ImageView) findViewById(R.id.view_zhanwei);

        recyclerView.setFocusableInTouchMode(false);
    }

    @Override
    protected void initActions() {
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(companyItemAdapter);

        recyclerView.setLoadingMoreEnabled(false);
        recyclerView.setPullRefreshEnabled(true);

//        SearchListModel model = new Gson().fromJson(IOUtil.readTextFileFromRawResourceId(mContext, R.raw.ceshi), SearchListModel.class);
//        companyItemAdapter.refreshList(model.list;

        viewZhanwei.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                hide();
            }
        });

        companyItemAdapter.setCallBack(new CompanyItemAdapter.CallBack() {
            @Override
            public void tagCompany(HomeDataItemModel data) {
                if (tagCallBack != null) {
                    tagCallBack.setTag(data);
                }

            }
        });


        recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                getData(true);
            }

            @Override
            public void onLoadMore() {
                getData(false);
            }
        });

    }


    public void setData(SearchListModel model, HashMap<String, Object> params) {
        this.params = params;
        if (model != null) {
            companyItemAdapter.refreshList(model.list);

            if (companyItemAdapter.getItemCount() >= model.totalCount) {
                recyclerView.setLoadingMoreEnabled(false);
            } else {
                recyclerView.setLoadingMoreEnabled(true);
            }
            show();
        }
        if(params==null){
            recyclerView.setPullRefreshEnabled(false);
        }
    }


    public void show() {
        setVisibility(VISIBLE);
        ValueAnimator valueAnimator = ValueAnimator.ofInt(PhoneUtil.getDisplayHeight(mContext), 0);
        valueAnimator.setDuration(200);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (int) valueAnimator.getAnimatedValue();
                setY(value);
            }
        });
        valueAnimator.start();
    }


    public void hide() {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, PhoneUtil.getDisplayHeight(mContext));
        valueAnimator.setDuration(500);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (int) valueAnimator.getAnimatedValue();
                setY(value);
                if (value == PhoneUtil.getDisplayHeight(mContext)) {
                    setVisibility(GONE);
                }
            }

        });
        valueAnimator.start();
    }


    public void close(){
        if (getVisibility() == View.VISIBLE) {
            hide();
        }
    }

    private MapFragment.TagCallBack tagCallBack;

    public void setCallBck(MapFragment.TagCallBack tagCallBack) {
        this.tagCallBack = tagCallBack;
    }

    public void refresh() {
        companyItemAdapter.notifyDataSetChanged();
    }


    private void getData(final boolean isRefresh) {
        params.put("pageIndex", isRefresh ? 1 : pageIndex);
        addNetwork(Api.getInstance().getService(ApiService.class).getCompanyListByPoint(params), new Action1<NetModel>() {
            @Override
            public void call(NetModel net) {
                recyclerView.loadMoreComplete();
                recyclerView.refreshComplete();
                if (net.success()) {

                    if (isRefresh) {
                        pageIndex = 1;
                    } else {
                        pageIndex++;
                    }
                    SearchListModel model = net.dataToObject(SearchListModel.class);
                    if (model != null && model.list != null) {
                        companyItemAdapter.refreshList(model.list);
                        if (isRefresh) {
                            companyItemAdapter.refreshList(model.list);
                        } else {
                            companyItemAdapter.addList(model.list);
                        }


                        if (companyItemAdapter.getItemCount() >= model.totalCount) {
                            recyclerView.setLoadingMoreEnabled(false);
                        } else {
                            recyclerView.setLoadingMoreEnabled(true);
                        }
                    }
                } else {
//                    showToast(net.msg);
                }
                recyclerView.setFocusableInTouchMode(false);


            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                recyclerView.loadMoreComplete();
                recyclerView.refreshComplete();
                ToastUtils.showHttpError();
            }
        });
    }


    public boolean isShowGoClose(){
        if(getVisibility() == VISIBLE){
            close();
            return true;
        }
        return false;
    }

}

