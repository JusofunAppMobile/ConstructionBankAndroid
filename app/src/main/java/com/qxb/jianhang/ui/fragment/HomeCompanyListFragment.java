package com.qxb.jianhang.ui.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.jusfoun.baselibrary.Util.LogUtil;
import com.jusfoun.baselibrary.Util.PreferenceUtils;
import com.jusfoun.baselibrary.Util.ToastUtils;
import com.jusfoun.baselibrary.base.BaseViewPagerFragment;
import com.jusfoun.baselibrary.event.IEvent;
import com.jusfoun.baselibrary.model.UserModel;
import com.jusfoun.baselibrary.net.Api;
import com.jusfoun.baselibrary.net.NetModel;
import com.jusfoun.baselibrary.view.HomeViewPager;
import com.jusfoun.jusfouninquire.view.TitleView;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.qxb.jianhang.R;
import com.qxb.jianhang.net.event.SearchEvent;
import com.qxb.jianhang.net.event.SearchRefreshEvent;
import com.qxb.jianhang.ui.adapter.CompayListAdapter;
import com.qxb.jianhang.ui.adapter.HommSearchTagAdapter;
import com.qxb.jianhang.ui.constant.Constant;
import com.qxb.jianhang.ui.data.LonLatModel;
import com.qxb.jianhang.ui.data.PoiInfoModel;
import com.qxb.jianhang.ui.data.SearchListModel;
import com.qxb.jianhang.ui.net.ApiService;
import com.qxb.jianhang.ui.sharedpreferences.LocationSharepreferences;
import com.qxb.jianhang.ui.util.AppUtils;
import com.qxb.jianhang.ui.view.SearchListTitleView;
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
public class HomeCompanyListFragment extends BaseViewPagerFragment implements OnGetSuggestionResultListener, OnGetPoiSearchResultListener {


    protected HomeViewPager viewpager;
    protected SmartTabLayout viewpagertab;
    private CompayListAdapter mainAdapter;
    protected TitleView viewTitleBar;
    private SearchListModel model;
    private SearchListTitleView searchListTitleView;


    private HommSearchTagAdapter adapter;

    private RecyclerView addressRecycler;

    private SuggestionSearch mSuggestionSearch;
    private PoiSearch mPoiSearch;
    private PoiInfoModel poiInfoModel;

    private int type = SearchMapTitleView.TYPE_COMPANY;

    public static HomeCompanyListFragment getInstance(SearchListModel model, PoiInfoModel poiInfoModel) {
        HomeCompanyListFragment fragment = new HomeCompanyListFragment();
        if (model != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("model", model);
            bundle.putSerializable("poiInfoModel", poiInfoModel);
            fragment.setArguments(bundle);

        }
        return fragment;
    }


    @Override
    public int getLayoutResId() {
        return R.layout.activity_company_list;
    }

    @Override
    public void initDatas() {

        if (getArguments() != null && getArguments().get("model") != null) {
            model = (SearchListModel) getArguments().get("model");
            type = model.searchType;
        }

        if (getArguments() != null && getArguments().get("poiInfoModel") != null) {
            poiInfoModel = (PoiInfoModel) getArguments().get("poiInfoModel");
        }

        mainAdapter = new CompayListAdapter(getChildFragmentManager(), model, poiInfoModel);

        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(this);


    }

    @Override
    public void initView(View rootView) {
        viewpager = (HomeViewPager) rootView.findViewById(R.id.viewpager);
        viewpagertab = (SmartTabLayout) rootView.findViewById(R.id.viewpagertab);
        viewTitleBar = (TitleView) rootView.findViewById(R.id.view_title_bar);
        searchListTitleView = (SearchListTitleView) rootView.findViewById(R.id.view_search_title_bar);
        addressRecycler = (RecyclerView) findViewById(R.id.recycler_address);

    }

    @Override
    public void initAction() {

        adapter = new HommSearchTagAdapter();
        addressRecycler.setAdapter(adapter);
        addressRecycler.setLayoutManager(new LinearLayoutManager(mContext));

        viewTitleBar.setTitle("企业列表");
        viewTitleBar.hideLineView();
        viewTitleBar.hideBack();
        searchListTitleView.setStatus(type);

        searchListTitleView.setCallBack(new SearchListTitleView.CallBack() {
            @Override
            public void search(String searchKey, int type) {
                HomeCompanyListFragment.this.type = type;
                if (type == SearchMapTitleView.TYPE_COMPANY) {
                    goSearch(searchKey);
                } else {
                    indexAddress(searchKey, type);
                }
            }

            @Override
            public void indexAddress(String searchKey, int type) {
                HomeCompanyListFragment.this.type = type;
                if (type == SearchMapTitleView.TYPE_ADDRESS) {
                    searchAddress(searchKey);
                }
            }

        });

        if (model != null) {
            Log.e("tag", "initActioninitAction=" + model.searchKey);
            searchListTitleView.setSearchText(model.searchKey);
            searchListTitleView.setVisibility(View.VISIBLE);
            viewTitleBar.setVisibility(View.GONE);
        }

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                PoiInfoModel model = (PoiInfoModel) adapter.getItem(position);
                goSearch(model);
            }
        });
    }

    private void goSearch(String searchKey) {
        showLoadDialog();
        HashMap<String, Object> map = new HashMap<>();
        UserModel userInfoModel = AppUtils.getUserInfo();
//        map.put("userId", userInfoModel.id);
        map.put("companyname", searchKey);

        LonLatModel lonLatModel = LocationSharepreferences.getLocation(mContext);
        map.put("myLongitude", lonLatModel.lon);
        map.put("myLatitude", lonLatModel.lat);

        addNetwork(Api.getInstance().getService(ApiService.class).search(map), new Action1<NetModel>() {
            @Override
            public void call(NetModel net) {
                hideLoadDialog();
                if (net.success()) {
                    SearchListModel model = net.dataToObject(SearchListModel.class);
                    SearchEvent event = new SearchEvent();
                    model.searchKey = searchListTitleView.getSearchKey();
                    event.model = model;
                    EventBus.getDefault().post(event);
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
    protected void refreshData() {

        viewpager.setAdapter(mainAdapter);
        viewpagertab.setViewPager(viewpager);
        viewpagertab.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


            }

            @Override
            public void onPageSelected(int position) {


                for (int i = 0; i < 3; i++) {
                    TextView textView = (TextView) viewpagertab.getTabAt(i);
                    textView.setTextColor(0xff666666);
                    textView.setTextSize(14);

                    textView.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                }
                TextView textView = (TextView) viewpagertab.getTabAt(position);
                textView.setTextColor(0xff333333);
                textView.setTextSize(16);
                textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        TextView textView = (TextView) viewpagertab.getTabAt(0);
        textView.setTextColor(0xff333333);
        textView.setTextSize(16);
        textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

        // TODO 如果 搜索地址  请求接口，
    }

    @Override
    public void onEvent(final IEvent event) {
        super.onEvent(event);
        if (event instanceof SearchEvent) {
            if (((SearchEvent) event).model != null && model != null) {
                searchListTitleView.setSearchText(((SearchEvent) event).model.searchKey);
            }
        }
    }


    @Override
    public void onGetSuggestionResult(SuggestionResult res) {

        if (res == null || res.getAllSuggestions() == null) {
            adapter.setNewData(null);
            addressRecycler.setVisibility(View.GONE);
            return;
            //未找到相关结果
        }

        if (res.getAllSuggestions() != null && !res.getAllSuggestions().isEmpty()) {
            List<PoiInfoModel> list = new ArrayList<>();
            for (SuggestionResult.SuggestionInfo info : res.getAllSuggestions()) {
                LogUtil.e(new Gson().toJson(info));
                if (info.pt != null) {
                    PoiInfoModel model = new PoiInfoModel();
                    model.address = info.key;
                    model.latitude = info.pt.latitude;
                    model.longitude = info.pt.longitude;
                    model.name = info.key;
                    list.add(model);
                }
            }
            addressRecycler.setVisibility(View.VISIBLE);
            adapter.setNewData(list);
        } else {
            adapter.setNewData(null);
            addressRecycler.setVisibility(View.GONE);
        }
    }

    @Override
    public void onGetPoiResult(PoiResult res) {
        LogUtil.e(">>>>>>>>>>>onGetPoiResult");
        if (res == null || res.getAllPoi() == null) {   //未找到相关结果
            adapter.setNewData(null);
            addressRecycler.setVisibility(View.GONE);
            return;
        }
        if (res.getAllPoi() != null && !res.getAllPoi().isEmpty()) {
            if (searchListTitleView.getSearchKey().length() > 0) {
                List<PoiInfoModel> list = new ArrayList<>();
                for (PoiInfo info : res.getAllPoi()) {
                    list.add(new PoiInfoModel(info));
                }
                addressRecycler.setVisibility(View.VISIBLE);
                adapter.setNewData(list);
            } else {
                adapter.setNewData(null);
                addressRecycler.setVisibility(View.GONE);
            }
        } else {
            adapter.setNewData(null);
            addressRecycler.setVisibility(View.GONE);
        }
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
        LogUtil.e(">>>>>>>>>>>onGetPoiDetailResult");
    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {
        LogUtil.e(">>>>>>>>>>>onGetPoiIndoorResult");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mSuggestionSearch != null)
            mSuggestionSearch.destroy();
        if (mPoiSearch != null)
            mPoiSearch.destroy();
    }

    private void searchAddress(String key) {
        String city = PreferenceUtils.getString(mContext, Constant.LOC_CITY);
        if (TextUtils.isEmpty(city))
            city = "北京";
        mPoiSearch.searchInCity(new PoiCitySearchOption().city(city)
                .keyword(key)
                .pageCapacity(30)
                .pageNum(0));
    }

    private void goSearch(PoiInfoModel poiInfoModel) {
//        showLoadDialog();
        addressRecycler.setVisibility(View.GONE);
//        LonLatModel lonLatModel = LocationSharepreferences.getLocation(mContext);
//        HashMap<String, Object> map = new HashMap<>();
//        map.put("myLongitude", lonLatModel.lon + "");
//        map.put("myLatitude", lonLatModel.lat + "");
//        map.put("precision", "6");
//        map.put("address", poiInfoModel.name);
//
//
//        map.put("longitude", poiInfoModel.longitude + "");
//        map.put("latitude", poiInfoModel.latitude + "");
//        addNetwork(Api.getInstance().getService(ApiService.class).searchNew(map), new Action1<NetModel>() {
//            @Override
//            public void call(NetModel net) {
//                hideLoadDialog();
//                if (net.success()) {
//
//                    CompanyListModel companyListModel = net.dataToObject(CompanyListModel.class);
//                    SearchListModel model = new SearchListModel();
//                    model.list = companyListModel.list;
//                    com.qxb.jianhang.net.event.SearchEvent event = new com.qxb.jianhang.net.event.SearchEvent();
//                    model.searchKey = searchListTitleView.getSearchKey();
//                    event.model = model;
//                    EventBus.getDefault().post(event);
//
//                } else {
//                    hideLoadDialog();
//                    showToast(net.msg);
//                }
//            }
//        }, new Action1<Throwable>() {
//            @Override
//            public void call(Throwable throwable) {
//                hideLoadDialog();
//                ToastUtils.showHttpError();
//            }
//        });
        SearchRefreshEvent event = new SearchRefreshEvent();
        event.poiInfoModel = poiInfoModel;
        EventBus.getDefault().post(event);
    }
}
