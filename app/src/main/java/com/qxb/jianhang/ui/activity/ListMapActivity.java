package com.qxb.jianhang.ui.activity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;
import com.google.gson.Gson;
import com.jusfoun.baselibrary.Util.IOUtil;
import com.qxb.jianhang.R;
import com.qxb.jianhang.ui.adapter.CompanyItemAdapter;
import com.qxb.jianhang.ui.base.BaseBackActivity;
import com.qxb.jianhang.ui.base.BaseMapActivity;
import com.qxb.jianhang.ui.data.SearchListModel;

/**
 * @author zhaoyapeng
 * @version create time:18/8/2416:51
 * @Email zyp@jusfoun.com
 * @Description ${地图fragment}
 */
public class ListMapActivity extends BaseMapActivity {



    private RecyclerView recyclerView;
    private CompanyItemAdapter companyItemAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_list_map;
    }

    @Override
    public void initDatas() {
        companyItemAdapter = new CompanyItemAdapter(mContext);

    }

    @Override
    public void initView() {
        initMap();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

    }

    @Override
    public void initAction() {

        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(companyItemAdapter);
//        SearchListModel model = new Gson().fromJson(IOUtil.readTextFileFromRawResourceId(mContext, R.raw.ceshi), SearchListModel.class);
//        companyItemAdapter.refreshList(model.getBusinesslist());

    }







}
