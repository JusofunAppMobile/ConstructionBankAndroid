package com.qxb.jianhang.ui.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
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
import com.google.gson.Gson;
import com.jusfoun.baselibrary.Util.LogUtil;
import com.jusfoun.baselibrary.Util.PreferenceUtils;
import com.qxb.jianhang.R;
import com.qxb.jianhang.ui.adapter.PoiInfoAdapter;
import com.qxb.jianhang.ui.base.BaseBackActivity;
import com.qxb.jianhang.ui.constant.Constant;
import com.qxb.jianhang.ui.data.PoiInfoModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author liuguangdan
 * @version create at 2018/11/5/005 17:11
 * @Email lgd@jusfoun.com
 * @Description ${}
 */
public class MapAddressSearchActivity extends BaseBackActivity implements OnGetSuggestionResultListener, OnGetPoiSearchResultListener, TextWatcher {

    @BindView(R.id.etInput)
    EditText etInput;

    @BindView(R.id.recycler)
    RecyclerView recycler;

    @BindView(R.id.vEmpty)
    View vEmpty;

    private PoiInfoAdapter adapter;


    private SuggestionSearch mSuggestionSearch;
    private PoiSearch mPoiSearch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarEnable(Color.parseColor("#FAFAFA"));
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_map_address_search;
    }


    @Override
    public void initDatas() {

    }

    private void search() {
        vEmpty.setVisibility(View.GONE);
        //                    mSuggestionSearch.requestSuggestion((new SuggestionSearchOption())
//                            .keyword(getValue(etInput))
//                            .city("北京"));

        String city = PreferenceUtils.getString(this, Constant.LOC_CITY);
        if (TextUtils.isEmpty(city))
            city = "北京";
        mPoiSearch.searchInCity(new PoiCitySearchOption().city(city)
                .keyword(getValue(etInput))
                .pageCapacity(30)
                .pageNum(0));
    }


    @Override
    public void initView() {
        ButterKnife.bind(this);
        etInput.setOnEditorActionListener(new TextView.OnEditorActionListener()

        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {//搜索按键action
                    LogUtil.e(">>>>>>>>>>>>>>onEditorAction");
                    search();
                    return true;
                }
                return false;
            }
        });

        mSuggestionSearch = SuggestionSearch.newInstance();
        mSuggestionSearch.setOnGetSuggestionResultListener(this);

        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(this);

        adapter = new PoiInfoAdapter(this);
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        etInput.addTextChangedListener(this);
        etInput.requestFocus();
    }

    @Override
    public void initAction() {

    }

    @Override
    public void onGetSuggestionResult(SuggestionResult res) {

        if (res == null || res.getAllSuggestions() == null) {
            adapter.refreshList(null);
            vEmpty.setVisibility(View.VISIBLE);
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
            adapter.refreshList(list);
        } else {
            adapter.refreshList(null);
            vEmpty.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSuggestionSearch != null)
            mSuggestionSearch.destroy();
        if (mPoiSearch != null)
            mPoiSearch.destroy();
    }

    @Override
    public void onGetPoiResult(PoiResult res) {
        LogUtil.e(">>>>>>>>>>>onGetPoiResult");
        if (res == null || res.getAllPoi() == null) {   //未找到相关结果
            adapter.refreshList(null);
            vEmpty.setVisibility(View.VISIBLE);
            return;
        }
        if (res.getAllPoi() != null && !res.getAllPoi().isEmpty()) {
            List<PoiInfoModel> list = new ArrayList<>();
            for (PoiInfo info : res.getAllPoi()) {
                list.add(new PoiInfoModel(info));
            }
            adapter.refreshList(list);
        } else {
            adapter.refreshList(null);
            vEmpty.setVisibility(View.VISIBLE);
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
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() == 0) {
            adapter.refreshList(null);
            vEmpty.setVisibility(View.GONE);
        } else {
            search();
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }
}
