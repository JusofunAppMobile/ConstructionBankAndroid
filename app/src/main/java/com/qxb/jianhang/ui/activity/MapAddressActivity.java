package com.qxb.jianhang.ui.activity;

import android.graphics.Point;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.jusfoun.baselibrary.Util.LogUtil;
import com.jusfoun.baselibrary.Util.PreferenceUtils;
import com.jusfoun.jusfouninquire.view.TitleView;
import com.qxb.jianhang.R;
import com.qxb.jianhang.ui.adapter.PoiInfoAdapter;
import com.qxb.jianhang.ui.base.BaseBackActivity;
import com.qxb.jianhang.ui.constant.Constant;
import com.qxb.jianhang.ui.data.PoiInfoModel;
import com.qxb.jianhang.ui.event.SelectPoiInfoEvent;
import com.qxb.jianhang.ui.util.BdLocationManager;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author liuguangdan
 * @version create at 2018/9/5/005 20:14
 * @Email lgd@jusfoun.com
 * @Description ${地图选点页面}
 */
public class MapAddressActivity extends BaseBackActivity {

    private final static int ZOOM = 18;

    @BindView(R.id.titleView)
    TitleView titleView;
    @BindView(R.id.mapView)
    MapView mapView;
    @BindView(R.id.recycler)
    RecyclerView recycler;

    private PoiInfoAdapter adapter;

    private BaiduMap mBaiduMap;

    private BitmapDescriptor markerBitmap;

    private GeoCoder mSearch;


    @Override
    public int getLayoutResId() {
        return R.layout.activity_map_address;
    }

    @Override
    public void initDatas() {

    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        titleView.setTitle("选择地点");

        mBaiduMap = mapView.getMap();
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(listener);

        mBaiduMap.setOnMapStatusChangeListener(statusChangeListener);

        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PoiInfoAdapter(this);
        recycler.setAdapter(adapter);

        addCenterMarker();

        // 默认设置上次定位的位置为地图中心
        String latitude = PreferenceUtils.getString(this, Constant.LOC_LATITUDE);
        String longitude = PreferenceUtils.getString(this, Constant.LOC_LONGITUDE);
        if (!TextUtils.isEmpty(latitude) && !TextUtils.isEmpty(longitude)) {
            setMapCenter(new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude)));
        } else {
            // 逆编码搜索
            mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(mBaiduMap.getMapStatus().target));
        }

        location();

        titleView.setRightImage(R.drawable.img_search);
        titleView.setRightClickListener(new TitleView.OnRightClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(MapAddressSearchActivity.class);
            }
        });
    }

    /**
     * 开始定位
     */
    private void location() {
        new BdLocationManager().start(this, new BDAbstractLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                if (bdLocation != null) {
                    PreferenceUtils.setString(mContext, Constant.LOC_LATITUDE, String.valueOf(bdLocation.getLatitude()));
                    PreferenceUtils.setString(mContext, Constant.LOC_LONGITUDE, String.valueOf(bdLocation.getLongitude()));
                    PreferenceUtils.setString(mContext, Constant.LOC_CITY, bdLocation.getCity());

                    setMapCenter(new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude()));
                }
            }
        });
    }

    private BaiduMap.OnMapStatusChangeListener statusChangeListener = new BaiduMap.OnMapStatusChangeListener() {
        @Override
        public void onMapStatusChangeStart(MapStatus mapStatus) {

        }

        @Override
        public void onMapStatusChangeStart(MapStatus mapStatus, int i) {

        }

        @Override
        public void onMapStatusChange(MapStatus mapStatus) {

        }

        @Override
        public void onMapStatusChangeFinish(MapStatus mapStatus) {
            mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(mapStatus.target));
        }
    };

    OnGetGeoCoderResultListener listener = new OnGetGeoCoderResultListener() {

        public void onGetGeoCodeResult(GeoCodeResult result) {
            LogUtil.e(">>>>>>>>>>>onGetGeoCodeResult");
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                //没有检索到结果
                LogUtil.e("Search.没有检索到结果");
            }

            //获取地理编码结果
            LogUtil.e(result.getAddress());
        }

        @Override

        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
            LogUtil.e(">>>>>>>>>>>onGetReverseGeoCodeResult");
            if (result == null || result.getPoiList() == null || result.getPoiList().isEmpty() || result.error != SearchResult.ERRORNO.NO_ERROR) {
                //没有找到检索结果
                LogUtil.e("Search.没有检索到结果");
            }

            List<PoiInfoModel> list = new ArrayList<>();
            //获取反向地理编码结果
            for (PoiInfo poiInfo : result.getPoiList()) {
                LogUtil.e(poiInfo.name + "--->" + poiInfo.address);
                list.add(new PoiInfoModel(poiInfo));
            }
            adapter.refreshList(list);
        }
    };

    @Override
    public void initAction() {

    }

    /**
     * 设置地图中心位置并逆编码检索位置
     *
     * @param latLng
     */
    private void setMapCenter(LatLng latLng) {
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLng(latLng));
        mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(latLng));
    }

    /**
     * 添加中心固定Marker
     */
    public void addCenterMarker() {
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                int width = mapView.getWidth();
                int height = mapView.getHeight();

                //构建Marker图标
                markerBitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_home_tag_add_map);

                //构建MarkerOption，用于在地图上添加Marker
                OverlayOptions option = new MarkerOptions()
                        .position(mBaiduMap.getMapStatus().target)
                        .flat(true)
                        .fixedScreenPosition(new Point(width / 2, height / 2))
                        .icon(markerBitmap);
                //在地图上添加Marker，并显示
                mBaiduMap.addOverlay(option);
                mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(ZOOM));
            }
        }, 200);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (markerBitmap != null) {
            markerBitmap.recycle();
            markerBitmap = null;
        }
        if (mSearch != null)
            mSearch.destroy();
    }


    @Subscribe
    public void onEvent(SelectPoiInfoEvent event) {
        finish();
    }
}
