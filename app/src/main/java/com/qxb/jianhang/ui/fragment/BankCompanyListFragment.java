package com.qxb.jianhang.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jusfoun.baselibrary.Util.PhoneUtil;
import com.jusfoun.baselibrary.Util.ToastUtils;
import com.jusfoun.baselibrary.base.BaseViewPagerFragment;
import com.jusfoun.baselibrary.event.IEvent;
import com.jusfoun.baselibrary.net.Api;
import com.jusfoun.baselibrary.net.NetModel;
import com.jusfoun.baselibrary.widget.xRecyclerView.XRecyclerView;
import com.qxb.jianhang.R;
import com.qxb.jianhang.net.event.SearchAddressEvent;
import com.qxb.jianhang.net.event.SearchEvent;
import com.qxb.jianhang.net.event.SearchRefreshEvent;
import com.qxb.jianhang.ui.adapter.CompanyItemAdapter;
import com.qxb.jianhang.ui.data.CompanyListModel;
import com.qxb.jianhang.ui.data.HomeDataItemModel;
import com.qxb.jianhang.ui.data.LonLatModel;
import com.qxb.jianhang.ui.data.PoiInfoModel;
import com.qxb.jianhang.ui.data.SearchListModel;
import com.qxb.jianhang.ui.event.UpdateFollowEvent;
import com.qxb.jianhang.ui.net.ApiService;
import com.qxb.jianhang.ui.pop.SelectDistancePopView;
import com.qxb.jianhang.ui.sharedpreferences.LocationSharepreferences;
import com.qxb.jianhang.ui.view.SearchMapTitleView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.functions.Action1;

/**
 * @author zhaoyapeng
 * @version create time:18/8/2714:35
 * @Email zyp@jusfoun.com
 * @Description ${公司列表fragment}
 */
public class BankCompanyListFragment extends BaseViewPagerFragment {
    protected XRecyclerView recyclerView;

    private CompanyItemAdapter companyItemAdapter;

    private int index = 0;
    private int pageSize = 20;
    private int pageIndex = 1;

    private SearchListModel model;

    private LinearLayout selectLayout;
    private TextView selectText;

    private String filterDistance = "";

    private SelectDistancePopView selectDistancePopView;

    private PoiInfoModel poiInfoModel;


    public static BankCompanyListFragment getInstance(int index, SearchListModel model, PoiInfoModel poiInfoModel) {
        BankCompanyListFragment fragment = new BankCompanyListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("index", index);
        if (model != null)
            bundle.putSerializable("model", model);

        if (poiInfoModel != null)
            bundle.putSerializable("poiInfoModel", poiInfoModel);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_company_list;
    }

    @Override
    public void initDatas() {
        index = getArguments().getInt("index");
        if (getArguments() != null && getArguments().get("model") != null) {
            model = (SearchListModel) getArguments().get("model");
        }
        if (getArguments() != null && getArguments().get("poiInfoModel") != null) {

            poiInfoModel = (PoiInfoModel) getArguments().get("poiInfoModel");
        }
        companyItemAdapter = new CompanyItemAdapter(mContext);

        selectDistancePopView = new SelectDistancePopView(mContext);

    }

    @Override
    public void initView(View rootView) {
        recyclerView = (XRecyclerView) rootView.findViewById(R.id.recycler_view);
        recyclerView.setAdapter(companyItemAdapter);
        selectLayout = (LinearLayout) rootView.findViewById(R.id.layout_select);
        selectText = (TextView) rootView.findViewById(R.id.text_select);

    }

    @Override
    public void initAction() {
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        companyItemAdapter.setCallBack(new CompanyItemAdapter.CallBack() {
            @Override
            public void tagCompany(HomeDataItemModel data) {
                addTag(data);
            }
        });

        recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                getCompanyList(true, true);
            }

            @Override
            public void onLoadMore() {
                getCompanyList(false, true);
            }
        });

        if (model != null) {
            selectLayout.setVisibility(View.GONE);
            recyclerView.setLoadingMoreEnabled(false);
        } else {
            filterDistance = "2";
            if (index + 1 == 3) {
                filterDistance = "-2";//全北京
            }
        }

        if (index == 2) {
            selectLayout.setVisibility(View.GONE);
        }
        selectLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDistancePopView.showAsDropDown(selectText, -PhoneUtil.dip2px(mContext, 20), 0);
            }
        });
        selectDistancePopView.setCallBack(new SelectDistancePopView.CallBack() {
            @Override
            public void select(String key, String value) {
                selectDistancePopView.dismiss();
                filterDistance = value;
                selectText.setText(key);
                getCompanyList(true, true);
            }
        });
    }

    private void getCompanyList(final boolean isRefresh, boolean isShowLoading) {
        if (model != null && model.searchType != SearchMapTitleView.TYPE_ADDRESS) {
            complete();
            List<HomeDataItemModel> list = new ArrayList<>();
            if (model.list != null) {
                for (int i = 0; i < model.list.size(); i++) {
                    if (((index + 1) + "").equals(model.list.get(i).type)) {
                        list.add(model.list.get(i));
                    }

                }
            }
            companyItemAdapter.refreshList(list);
            return;
        }


        if (model != null && poiInfoModel != null && model.searchType == SearchMapTitleView.TYPE_ADDRESS) {
            goSearch(poiInfoModel, isRefresh);
            return;
        }


        if (isShowLoading)
            showLoadDialog();
        HashMap<String, Object> map = new HashMap<>();
        map.put("myLongitude", LocationSharepreferences.getLocation(mContext).lon);
        map.put("myLatitude", LocationSharepreferences.getLocation(mContext).lat);
        if (index + 1 == 2) {
            map.put("type", "10");
        } else {
            map.put("type", "" + (index + 1));
        }

        map.put("pageIndex", isRefresh ? 1 : pageIndex);
        map.put("pageSize", "" + pageSize);
        map.put("filterDistance", filterDistance);


        addNetwork(Api.getInstance().getService(ApiService.class).getCompanyList(map), new Action1<NetModel>() {
            @Override
            public void call(NetModel net) {
                complete();
                hideLoadDialog();
                if (net.success()) {
                    if (isRefresh) {
                        pageIndex = 1;
                    }
                    SearchListModel model = net.dataToObject(SearchListModel.class);
                    if (model != null && model.list != null) {
//                        companyItemAdapter.refreshList(model.list);
                        if (isRefresh) {
                            companyItemAdapter.refreshList(model.list);
                        } else {
                            companyItemAdapter.addList(model.list);
                        }
                        if (index == 2) {
                            if (companyItemAdapter.getItemCount() >= model.totalCount) {
                                recyclerView.setLoadingMoreEnabled(false);
                            } else {
                                pageIndex++;
                                recyclerView.setLoadingMoreEnabled(true);
                            }
                        }

                        if (index != 2) {
                            if (model.havePageIndex == 0) {
                                recyclerView.setLoadingMoreEnabled(false);
                            } else {
                                pageIndex = model.havePageIndex;//非正式客户使用havePageIndex 做分页
                                recyclerView.setLoadingMoreEnabled(true);
                            }

                        }
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
        recyclerView.loadMoreComplete();
        recyclerView.refreshComplete();
    }


    @Override
    protected void refreshData() {
//        isInit = true;
        getCompanyList(true, true);
    }

    @Override
    public void onResume() {
        super.onResume();
        getCompanyList(true, false);
    }

    private void addTag(final HomeDataItemModel model) {
        showLoadDialog();
        HashMap<String, Object> map = new HashMap<>();

        if (!TextUtils.isEmpty(model.getCompanyid())) {
            map.put("entid", model.getCompanyid());
        } else {
            map.put("entid", model.entid);
        }
        addNetwork(Api.getInstance().getService(ApiService.class).tagCompanyNet(map), new Action1<NetModel>() {
            @Override
            public void call(NetModel net) {
                hideLoadDialog();
                if (net.success()) {
//                    model.type = Constant.TYPE_TRACK;
                    companyItemAdapter.remove(model);
//                    companyItemAdapter.notifyDataSetChanged();


                    showToast("标记成功");

                    if (BankCompanyListFragment.this.model != null) {
                        EventBus.getDefault().post(new SearchRefreshEvent());
                    }
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

    @Override
    public void onEvent(IEvent event) {
        super.onEvent(event);
        if (event instanceof SearchEvent) {
            if (model != null) {
                if (((SearchEvent) event).model != null) {
                    model = ((SearchEvent) event).model;
                    List<HomeDataItemModel> list = new ArrayList<>();
                    for (int i = 0; i < model.list.size(); i++) {
                        if (((index + 1) + "").equals(model.list.get(i).type)) {
                            list.add(model.list.get(i));
                        }
                    }
                    companyItemAdapter.refreshList(list);
                }
            }
        } else if (event instanceof UpdateFollowEvent) {
            if (model != null && model.list != null) {
                EventBus.getDefault().post(new SearchRefreshEvent());
            }
        }else if(event instanceof SearchAddressEvent){
            poiInfoModel = ((SearchAddressEvent) event).poiInfoModel;
            refreshData();
        }
    }


    private void goSearch(PoiInfoModel poiInfoModel, final boolean isRefresh) {
        showLoadDialog();

        LonLatModel lonLatModel = LocationSharepreferences.getLocation(mContext);
        HashMap<String, Object> map = new HashMap<>();
        map.put("myLongitude", lonLatModel.lon + "");
        map.put("myLatitude", lonLatModel.lat + "");
        map.put("precision", "6");
        map.put("address", poiInfoModel.address);
        map.put("longitude", poiInfoModel.latitude + "");
        map.put("latitude", poiInfoModel.longitude + "");
        map.put("type", "" + (index + 1));
        map.put("pageIndex", isRefresh ? 1 : pageIndex);
        map.put("pageSize", "" + pageSize);
        addNetwork(Api.getInstance().getService(ApiService.class).searchNew(map), new Action1<NetModel>() {
            @Override
            public void call(NetModel net) {

                complete();
                hideLoadDialog();
                if (net.success()) {
                    if (isRefresh) {
                        pageIndex = 1;
                    }

                    CompanyListModel companyListModel = net.dataToObject(CompanyListModel.class);
                    SearchListModel model = new SearchListModel();
                    model.list = companyListModel.list;

                    if (model.list != null) {
//                        companyItemAdapter.refreshList(model.list);
                        if (isRefresh) {
                            companyItemAdapter.refreshList(model.list);
                        } else {
                            companyItemAdapter.addList(model.list);
                        }
                        if (index == 2) {
                            if (companyItemAdapter.getItemCount() >= model.totalCount) {
                                recyclerView.setLoadingMoreEnabled(false);
                            } else {
                                pageIndex++;
                                recyclerView.setLoadingMoreEnabled(true);
                            }
                        }

                        if (index != 2) {
                            if (model.havePageIndex == 0) {
                                recyclerView.setLoadingMoreEnabled(false);
                            } else {
                                pageIndex = model.havePageIndex;//非正式客户使用havePageIndex 做分页
                                recyclerView.setLoadingMoreEnabled(true);
                            }

                        }
                    }
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
