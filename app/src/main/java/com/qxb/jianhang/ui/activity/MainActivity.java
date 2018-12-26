package com.qxb.jianhang.ui.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
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
import com.jusfoun.baselibrary.model.UserModel;
import com.jusfoun.baselibrary.net.Api;
import com.jusfoun.baselibrary.net.NetModel;
import com.jusfoun.baselibrary.view.HomeViewPager;
import com.qxb.jianhang.R;
import com.qxb.jianhang.ui.adapter.HommSearchTagAdapter;
import com.qxb.jianhang.ui.adapter.MainAdapter;
import com.qxb.jianhang.ui.adapter.PoiInfoAdapter;
import com.qxb.jianhang.ui.base.BaseBackActivity;
import com.qxb.jianhang.ui.constant.Constant;
import com.qxb.jianhang.ui.data.CompanyListModel;
import com.qxb.jianhang.ui.data.LonLatModel;
import com.qxb.jianhang.ui.data.PoiInfoModel;
import com.qxb.jianhang.ui.data.SearchListModel;
import com.qxb.jianhang.ui.event.CloseHomeViewEvent;
import com.qxb.jianhang.ui.net.ApiService;
import com.qxb.jianhang.ui.sharedpreferences.LocationSharepreferences;
import com.qxb.jianhang.ui.util.AppUtils;
import com.qxb.jianhang.ui.view.SearchMapTitleView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.functions.Action1;

public class MainActivity extends BaseBackActivity implements View.OnClickListener, OnGetSuggestionResultListener, OnGetPoiSearchResultListener {


    protected HomeViewPager viewpager;
    protected TextView textList, mapText;
    protected TextView textCenter;
    protected SearchMapTitleView viewSearch;
    private MainAdapter mainAdapter;
    private HommSearchTagAdapter adapter;

    private RecyclerView addressRecycler;

    private SuggestionSearch mSuggestionSearch;
    private PoiSearch mPoiSearch;

    private int type = SearchMapTitleView.TYPE_COMPANY;// 搜索类型 1. 企业 2.地址



    @Override
    public int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    public void initDatas() {
        mainAdapter = new MainAdapter(getSupportFragmentManager());
        mSuggestionSearch = SuggestionSearch.newInstance();
        mSuggestionSearch.setOnGetSuggestionResultListener(this);

    }

    @Override
    public void initView() {
        viewpager = (HomeViewPager) findViewById(R.id.viewpager);
        textList = (TextView) findViewById(R.id.text_list);
        textList.setOnClickListener(MainActivity.this);
        textCenter = (TextView) findViewById(R.id.text_center);
        textCenter.setOnClickListener(MainActivity.this);
        viewSearch = (SearchMapTitleView) findViewById(R.id.view_search);
        mapText = (TextView) findViewById(R.id.text_map);
        mapText.setOnClickListener(MainActivity.this);

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

        viewpager.setOffscreenPageLimit(3);

        setDoubleClickExitApp(true);


        viewSearch.setCallBack(new SearchMapTitleView.CallBack() {
            @Override
            public void search(String searchKey, int type) {

                MainActivity.this.type= type;
                if(type==SearchMapTitleView.TYPE_COMPANY) {
                    goSearch(searchKey);
                }else {
                    indexAddress(searchKey,type);
                }
            }

            @Override
            public void indexAddress(String searchKey, int type) {
                MainActivity.this.type= type;
                if(type==SearchMapTitleView.TYPE_ADDRESS) {
                    searchAddress(searchKey);
                }
            }

            @Override
            public void hideAddressList() {
                addressRecycler.setVisibility(View.GONE);
            }
        });
        mapText.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

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
        textList.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        textCenter.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        mapText.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        viewSearch.setVisibility(View.GONE);

        if (view.getId() == R.id.text_map) {
            viewpager.setCurrentItem(0, false);
            viewSearch.setVisibility(View.VISIBLE);
            mapText.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        } else if (view.getId() == R.id.text_list) {
            viewpager.setCurrentItem(1, false);
            textList.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        } else if (view.getId() == R.id.text_center) {
            textCenter.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            viewpager.setCurrentItem(2, false);
        }
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
                    Intent intent = new Intent(mContext, SearchActivity.class);
                    Bundle bundle = new Bundle();
                    model.searchType = type;
                    bundle.putSerializable("model", model);
                    intent.putExtra(SearchActivity.SEARCH_KEY, viewSearch.getSearchText());
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
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


    private void goSearch(PoiInfoModel poiInfoModel ) {
        showLoadDialog();
        addressRecycler.setVisibility(View.GONE);

        HashMap<String, String> map = new HashMap<>();
        map.put("myLongitude", poiInfoModel.longitude + "");
        map.put("myLatitude", poiInfoModel.latitude + "");
        map.put("precision", "6");
        addNetwork(Api.getInstance().getService(ApiService.class).getHomeMapNet(map), new Action1<NetModel>() {
            @Override
            public void call(NetModel net) {
                hideLoadDialog();
                if (net.success()) {
                    CompanyListModel companyListModel = net.dataToObject(CompanyListModel.class);
                    SearchListModel model = new SearchListModel();
                    model.list = companyListModel.list2;
                    Intent intent = new Intent(mContext, SearchActivity.class);
                    Bundle bundle = new Bundle();

                    model.searchType = type;
                    bundle.putSerializable("model", model);
                    intent.putExtra(SearchActivity.SEARCH_KEY, viewSearch.getSearchText());
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);

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
    public boolean isSetStatusBar() {
        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();
//        viewSearch.close();
        EventBus.getDefault().post(new CloseHomeViewEvent());
    }


    @Override
    public void onBackPressed() {
        if (mainAdapter.canQuit()) {
            super.onBackPressed();
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
