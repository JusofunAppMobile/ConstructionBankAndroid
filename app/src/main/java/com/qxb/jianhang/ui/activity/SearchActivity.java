package com.qxb.jianhang.ui.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
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
import com.jusfoun.baselibrary.event.IEvent;
import com.jusfoun.baselibrary.model.UserModel;
import com.jusfoun.baselibrary.net.Api;
import com.jusfoun.baselibrary.net.NetModel;
import com.jusfoun.baselibrary.view.HomeViewPager;
import com.qxb.jianhang.R;
import com.qxb.jianhang.net.event.SearchAddressEvent;
import com.qxb.jianhang.net.event.SearchEvent;
import com.qxb.jianhang.net.event.SearchRefreshEvent;
import com.qxb.jianhang.ui.adapter.HommSearchTagAdapter;
import com.qxb.jianhang.ui.adapter.MainAdapter;
import com.qxb.jianhang.ui.base.BaseBackActivity;
import com.qxb.jianhang.ui.constant.Constant;
import com.qxb.jianhang.ui.data.CompanyListModel;
import com.qxb.jianhang.ui.data.LonLatModel;
import com.qxb.jianhang.ui.data.PoiInfoModel;
import com.qxb.jianhang.ui.data.SearchListModel;
import com.qxb.jianhang.ui.net.ApiService;
import com.qxb.jianhang.ui.sharedpreferences.LocationSharepreferences;
import com.qxb.jianhang.ui.util.AppUtils;
import com.qxb.jianhang.ui.view.SearchMapTitleView;
import com.qxb.jianhang.ui.view.SearchSesultMapTitleView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.functions.Action1;

public class SearchActivity extends BaseBackActivity implements View.OnClickListener, OnGetSuggestionResultListener, OnGetPoiSearchResultListener {


    protected HomeViewPager viewpager;
    protected TextView textList;
    private MainAdapter mainAdapter;

    public static String SEARCH_KEY = "search_key";

    private String searchKey;

    private SearchListModel model;
    private SearchSesultMapTitleView titleView;


    private HommSearchTagAdapter adapter;

    private RecyclerView addressRecycler;

    private SuggestionSearch mSuggestionSearch;
    private PoiSearch mPoiSearch;

    private int type = SearchMapTitleView.TYPE_COMPANY;
    private PoiInfoModel poiInfoModel;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_search;
    }

    @Override
    public void initDatas() {
        searchKey = getIntent().getStringExtra(SEARCH_KEY);

        if (getIntent().getExtras() != null) {
            model = (SearchListModel) getIntent().getExtras().get("model");
            if(model!=null){
                type = model.searchType;
            }
            Log.e("tag","poiInfoModelpoiInfoModelpoiInfoModelpoiInfoModel1"+model.list.size());
            poiInfoModel = (PoiInfoModel) getIntent().getExtras().get("poiInfoModel");

        }
        searchKey = getIntent().getStringExtra(SEARCH_KEY);
        mainAdapter = new MainAdapter(getSupportFragmentManager(), searchKey, model,poiInfoModel);
    }

    @Override
    public void initView() {
        viewpager = (HomeViewPager) findViewById(R.id.viewpager);
        textList = (TextView) findViewById(R.id.text_list);
        textList.setOnClickListener(SearchActivity.this);
        titleView = (SearchSesultMapTitleView) findViewById(R.id.view_title_bar);

        addressRecycler = (RecyclerView) findViewById(R.id.recycler_address);

        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(this);
    }

    @Override
    public void initAction() {
        adapter = new HommSearchTagAdapter();
        addressRecycler.setAdapter(adapter);
        addressRecycler.setLayoutManager(new LinearLayoutManager(this));



        viewpager.setNotTouchScoll(true);
        viewpager.setAdapter(mainAdapter);
        mainAdapter.notifyDataSetChanged();
        titleView.startOpen();
        viewpager.setOffscreenPageLimit(3);

        textList.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        textList.setTag(1);


        titleView.setCallBack(new SearchSesultMapTitleView.CallBack() {
            @Override
            public void search(String searchKey, int type) {
                SearchActivity.this.type= type;
                if(type==SearchMapTitleView.TYPE_COMPANY) {
                    goSearch(searchKey,true);
                }else {
                    indexAddress(searchKey,type);
                }
            }

            @Override
            public void indexAddress(String searchKey, int type) {
                SearchActivity.this.type= type;
                if(type==SearchMapTitleView.TYPE_ADDRESS) {
                    searchAddress(searchKey);
                }
            }
        });

        titleView.setStatus(type);
        titleView.setSearchText(searchKey);


        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                PoiInfoModel model =  (PoiInfoModel)adapter.getItem(position);
                goSearch(model);
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.text_list) {

            textList.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            if (textList.getText().equals("列表显示")) {
                if ((int) textList.getTag() != 0) {
                    viewpager.setCurrentItem(1, false);
                    textList.setText("地图显示");

                } else {
                    viewpager.setCurrentItem(0, false);
                }
                titleView.setVisibility(View.GONE);


            } else {
                if ((int) textList.getTag() != 0) {
                    viewpager.setCurrentItem(0, false);
                    textList.setText("列表显示");

                } else {
                    viewpager.setCurrentItem(1, false);
                }
                titleView.setVisibility(View.VISIBLE);

            }
            textList.setTag(1);
        }
    }

    private void goSearch(String searchKey, boolean showLoading) {
        if (showLoading)
            showLoadDialog();
        HashMap<String, Object> map = new HashMap<>();
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
                    com.qxb.jianhang.net.event.SearchEvent event = new com.qxb.jianhang.net.event.SearchEvent();
                    model.searchKey = titleView.getSearchKey();
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


    private void goSearch(final PoiInfoModel poiInfoModel ) {
        showLoadDialog();
        addressRecycler.setVisibility(View.GONE);
        LonLatModel lonLatModel = LocationSharepreferences.getLocation(mContext);
        HashMap<String, String> map = new HashMap<>();
        map.put("myLongitude", lonLatModel.lon + "");
        map.put("myLatitude", lonLatModel.lat + "");
        map.put("precision", "6");
        map.put("address", poiInfoModel.address);
        map.put("longitude", poiInfoModel.latitude + "");
        map.put("latitude", poiInfoModel.longitude + "");
        map.put("merge", "1");
        addNetwork(Api.getInstance().getService(ApiService.class).getHomeMapNet(map), new Action1<NetModel>() {
            @Override
            public void call(NetModel net) {
                hideLoadDialog();

                if (net.success()) {

                    CompanyListModel companyListModel = net.dataToObject(CompanyListModel.class);
                    SearchListModel model = new SearchListModel();
                    model.list = companyListModel.list;
                    com.qxb.jianhang.net.event.SearchEvent event = new com.qxb.jianhang.net.event.SearchEvent();
                    model.searchKey = titleView.getSearchKey();
                    event.model = model;
                    EventBus.getDefault().post(event);


                    SearchAddressEvent searchAddressEvent = new SearchAddressEvent();
                    searchAddressEvent.poiInfoModel = poiInfoModel;
                    EventBus.getDefault().post(searchAddressEvent);


                } else {
                    hideLoadDialog();
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
    public void onEvent(final IEvent event) {
        super.onEvent(event);
        if (event instanceof SearchEvent) {
            if (((SearchEvent) event).model != null && model != null) {
                titleView.setSearchText(((SearchEvent) event).model.searchKey);
            }
        } else if (event instanceof SearchRefreshEvent) {
            goSearch(titleView.getSearchKey(),false);
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
    protected void onDestroy() {
        super.onDestroy();
        if (mSuggestionSearch != null)
            mSuggestionSearch.destroy();
        if (mPoiSearch != null)
            mPoiSearch.destroy();
    }
    private void searchAddress(String key) {
        String city = PreferenceUtils.getString(this, Constant.LOC_CITY);
        if (TextUtils.isEmpty(city))
            city = "北京";
        mPoiSearch.searchInCity(new PoiCitySearchOption().city(city)
                .keyword(key)
                .pageCapacity(30)
                .pageNum(0));
    }
}
