package com.jusfoun.jusfouninquire.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jusfoun.baselibrary.event.IEvent;
import com.jusfoun.jusfouninquire.TimeOut;
import com.qxb.jianhang.R;
import com.jusfoun.jusfouninquire.adapter.InvestOrBranchAdapter;
import com.jusfoun.jusfouninquire.bean.InvestOrBranchItemModel;
import com.jusfoun.jusfouninquire.bean.InvestOrBranchModel;
import com.jusfoun.jusfouninquire.callback.NetWorkCallBack;
import com.jusfoun.jusfouninquire.event.InvestOrBranchEvent;
import com.jusfoun.jusfouninquire.route.GetInvestOrBranch;
import com.jusfoun.jusfouninquire.util.LoginSharePreference;
import com.jusfoun.jusfouninquire.xlistview.XListView;
import com.jusfoun.jusfouninquire.activity.CompanyDetailActivity;
import com.jusfoun.jusfouninquire.activity.CompanyDetailsActivity;
import com.qxb.jianhang.ui.adapter.AnimationAdapter;
import com.qxb.jianhang.ui.adapter.ScaleInAnimationAdapter;
import com.jusfoun.jusfouninquire.animation.SceneAnimation;
import com.qxb.jianhang.ui.data.CompanyDetailModel;
import com.qxb.jianhang.ui.data.UserInfoModel;
import com.qxb.jianhang.ui.util.AppUtils;
import com.qxb.jianhang.ui.view.NetWorkErrorView;

import java.util.HashMap;

/**
 * Author  JUSFOUN
 * CreateDate 2015/11/11.
 * Description 对外投资 和 分支机构
 */
public class CompanyInvestmentFragment extends BaseViewPagerFragment {

    /**
     * 常量
     **/
    private static final String TYPE_BRANCK = "2";
    private static final int PAGESIZE = 20;
    private static final int LOAD_REFRESH_MODE = 1;
    private static final int LOAD_MORE = 2;

    /**
     * 组件
     **/
    private XListView listView;
    private View nodatalayout;
    private NetWorkErrorView netErrorLayout;
    private TextView nodataText, netErrorText;
    private LinearLayout loading;
    private ImageView imageView;
    /**
     * 变量
     **/
    private static final String TYPE_INVEST = "1";
    private int position = -1;
    private int pageIndex = 1;

    /**
     * 对象
     **/
    private InvestOrBranchAdapter adapter;
    private CompanyDetailModel model;
    private AnimationAdapter mAnimAdapter;
    private SceneAnimation sceneAnimation;


    public static CompanyInvestmentFragment getInstance(Bundle argument) {
        CompanyInvestmentFragment fragment = new CompanyInvestmentFragment();
        argument.putString("fristload", "true");
        fragment.setArguments(argument);
        return fragment;
    }

    @Override
    protected void setViewHint() {

    }

    @Override
    protected void refreshData() {
        Bundle arguments = getArguments();
        if (arguments != null && arguments.getString("fristload").equals("true")) {
            pageIndex = 1;
            model = (CompanyDetailModel) arguments.getSerializable(CompanyDetailsActivity.COMPANY);
            position = arguments.getInt(CompanyDetailsActivity.POSITION, -1);

            getInvestOrBranch(LOAD_REFRESH_MODE, pageIndex);
            arguments.putSerializable("fristload", "false");
        }
    }

    @Override
    protected void initData() {
        adapter = new InvestOrBranchAdapter(mContext);
        mAnimAdapter = new ScaleInAnimationAdapter(adapter);
    }

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_company_investment, container, false);
        listView = (XListView) view.findViewById(R.id.list_company);
        nodatalayout = view.findViewById(R.id.no_data_layout);
        nodataText = (TextView) nodatalayout.findViewById(R.id.nodata_text);

        netErrorLayout = (NetWorkErrorView) view.findViewById(R.id.net_work_error);
        netErrorText = (TextView) netErrorLayout.findViewById(R.id.error_text);
        loading= (LinearLayout) view.findViewById(R.id.loading);
        imageView= (ImageView) view.findViewById(R.id.loading_img);
        return view;
    }

    @Override
    protected void initWeightActions() {

        mAnimAdapter.setAbsListView(listView);
        listView.setAdapter(mAnimAdapter);

        listView.setPullLoadEnable(true);
        listView.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                pageIndex = 1;
                getInvestOrBranch(LOAD_REFRESH_MODE, pageIndex);
            }

            @Override
            public void onLoadMore() {
                getInvestOrBranch(LOAD_MORE, pageIndex + 1);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                InvestOrBranchItemModel model = (InvestOrBranchItemModel) parent.getItemAtPosition(position);
                Bundle bundle = new Bundle();
                bundle.putString(CompanyDetailActivity.COMPANY_ID, model.getCompanyid());
                bundle.putString(CompanyDetailActivity.COMPANY_NAME, model.getCompanyname());
                goActivity(CompanyDetailActivity.class, bundle);
            }
        });

        netErrorLayout.setListener(new NetWorkErrorView.OnRefreshListener() {
            @Override
            public void OnNetRefresh() {
                pageIndex = 1;
                getInvestOrBranch(LOAD_REFRESH_MODE, pageIndex);
            }
        });


    }

    @Override
    public void onEvent(IEvent event) {
        super.onEvent(event);
        if (event instanceof InvestOrBranchEvent) {
            InvestOrBranchEvent investOrBranchEvent = (InvestOrBranchEvent) event;
            Bundle bundle = ((InvestOrBranchEvent) event).getArgument();
            if (bundle != null) {
                model = (CompanyDetailModel) bundle.getSerializable(CompanyDetailsActivity.COMPANY);
                adapter.setInvest(model.getSubclassMenu().get(position).getType() == CompanyDetailsActivity.TYPE_INVEST);
                int position = bundle.getInt(CompanyDetailsActivity.POSITION, -1);
                if (this.position != position)
                    pageIndex = 1;
                this.position = position;
                getInvestOrBranch(LOAD_REFRESH_MODE, pageIndex);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (sceneAnimation!=null){
            sceneAnimation.stop();
        }
    }

    private void getInvestOrBranch(final int mode, int pageIndex) {
        if (model == null && TextUtils.isEmpty(model.getCompanyid()) && position < 0)
            return;

        TimeOut timeOut = new TimeOut(mContext);
        HashMap<String, String> params = new HashMap<>();
        params.put("userid", AppUtils.getUserInfo().id);
        params.put("pageSize", PAGESIZE + "");
        params.put("pageIndex", pageIndex + "");
        params.put("entid", model.getCompanyid());
        params.put("entname",model.getCompanyname());
        if (sceneAnimation==null)
            sceneAnimation=new SceneAnimation(imageView,75);
        sceneAnimation.start();
        if (model.getSubclassMenu().get(position).getType() == CompanyDetailsActivity.TYPE_INVEST) {
            params.put("type", TYPE_INVEST);
        } else if (model.getSubclassMenu().get(position).getType() == CompanyDetailsActivity.TYPE_BRANCH) {
            params.put("type", TYPE_BRANCK);
        }

        adapter.setInvest(model.getSubclassMenu().get(position).getType() == CompanyDetailsActivity.TYPE_INVEST);

        params.put("t",timeOut.getParamTimeMollis()+"");
        params.put("m",timeOut.MD5time()+"");
        GetInvestOrBranch.getInvestOrBranch(mContext, params, ((Activity)mContext).getLocalClassName(),new NetWorkCallBack() {
            @Override
            public void onSuccess(Object data) {
                finishLoadStatus();
                InvestOrBranchModel model = (InvestOrBranchModel) data;
                if (model.getResult() == 0) {
                    updateView(model, mode);
                } else {
                    netErrorLayout.setServerError();
                    netErrorLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFail(String error) {
                finishLoadStatus();
                if (mode == LOAD_REFRESH_MODE) {
                    netErrorLayout.setNetWorkError();
                    netErrorLayout.setVisibility(View.VISIBLE);
                } else if (mode == LOAD_MORE) {
                    showToast("网络连接失败");
                }

            }
        });
    }

    private void finishLoadStatus() {
        loading.setVisibility(View.GONE);
        if (sceneAnimation!=null)
            sceneAnimation.stop();
        nodatalayout.setVisibility(View.GONE);
        netErrorLayout.setVisibility(View.GONE);

        listView.stopLoadMore();
        listView.stopRefresh();
    }

    public void updateView(InvestOrBranchModel model, int mode) {
        if ("true".equals(model.getIsmore()))
            listView.setPullLoadEnable(true);
        else {
            listView.setPullLoadEnable(false);
        }
        if (model.getList() != null && model.getList().size() > 0) {

            if (mode == LOAD_REFRESH_MODE) {
                adapter.refresh(model.getList());
            } else if (mode == LOAD_MORE) {
                pageIndex++;
                adapter.addMore(model.getList());
            }
        } else {
            if (mode == LOAD_REFRESH_MODE) {
                nodatalayout.setVisibility(View.VISIBLE);
            } else if (mode == LOAD_MORE) {
                pageIndex++;
                showToast("获取失败");
            }
        }
    }
}
