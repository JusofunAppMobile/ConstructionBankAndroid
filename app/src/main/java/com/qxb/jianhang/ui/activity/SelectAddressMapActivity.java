package com.qxb.jianhang.ui.activity;

import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.animation.Animation;
import com.baidu.mapapi.animation.Transformation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.google.gson.Gson;
import com.qxb.jianhang.R;
import com.qxb.jianhang.ui.base.BaseMapActivity;

/**
 * @author zhaoyapeng
 * @version create time:18/8/2416:51
 * @Email zyp@jusfoun.com
 * @Description ${选择地址fragment}
 */
public class SelectAddressMapActivity extends BaseMapActivity  implements OnGetGeoCoderResultListener {


    protected MapView bmapView;
    protected TextView textAddress;
    private LatLng latLngPoint;

    private Point mScreenCenterPoint;
    // 初始化全局 bitmap 信息，不用时及时 recycle
    BitmapDescriptor bdA,bdCenter;

    private Marker marker;
    GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_select_address_map;
    }

    @Override
    public void initDatas() {
        locByChild = true;
        bdA = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_marka);

        bdCenter = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_geo);


    }

    @Override
    public void initView() {
        initMap();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        bmapView = (MapView) findViewById(R.id.bmapView);
        textAddress = (TextView) findViewById(R.id.text_address);

        // 初始化搜索模块，注册事件监听
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(this);
        // 地图点击事件处理
        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {

                Log.e("tag","onMapPoiClick="+new Gson().toJson(mapPoi));
                return false;
            }
        });
    }

    @Override
    public void initAction() {


    }

    /**
     * 创建平移坐标动画
     */
    private Animation getTransformationPoint() {

        if (null != mScreenCenterPoint) {
            Point pointTo = new Point(mScreenCenterPoint.x, mScreenCenterPoint.y - 100);
            Transformation mTransforma = new Transformation(mScreenCenterPoint, pointTo, mScreenCenterPoint);
            mTransforma.setDuration(500);
            mTransforma.setRepeatMode(Animation.RepeatMode.RESTART);//动画重复模式
            mTransforma.setRepeatCount(1);//动画重复次数
            mTransforma.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart() {
                }

                @Override
                public void onAnimationEnd() {
                }

                @Override
                public void onAnimationCancel() {
                }

                @Override
                public void onAnimationRepeat() {

                }
            });
            return mTransforma;
        }

        return null;
    }

    @Override
    protected void mapLoaded() {

    }

    @Override
    protected void finishFirstLocation(BDLocation location) {
        isFirstLoc = false;
        LatLng ll = new LatLng(location.getLatitude(),
                location.getLongitude());
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(ll).zoom(16.0f);
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        LatLng llCircle = new LatLng(location.getLatitude(), location.getLongitude());
//        OverlayOptions ooCircle = new CircleOptions().fillColor(0x000000FF)
//                .center(llCircle).stroke(new Stroke(2, 0xAA000000))
//                .radius(500);
//        mBaiduMap.addOverlay(ooCircle);

        MarkerOptions ooA = new MarkerOptions().position(llCircle).icon(bdCenter);
        mBaiduMap.addOverlay(ooA);



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                latLngPoint = mBaiduMap.getMapStatus().target;
                mScreenCenterPoint = mBaiduMap.getProjection().toScreenLocation(latLngPoint);
                MarkerOptions ooF = new MarkerOptions().position(latLngPoint).icon(bdA).perspective(true)
                        .fixedScreenPosition(mScreenCenterPoint);
                marker = (Marker) (mBaiduMap.addOverlay(ooF));
            }
        },500);


        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus status) {

            }

            @Override
            public void onMapStatusChangeStart(MapStatus status, int reason) {

            }

            @Override
            public void onMapStatusChange(MapStatus status) {

            }

            @Override
            public void onMapStatusChangeFinish(MapStatus status) {
                if (null == marker) {
                    return;
                }
                marker.setAnimation(getTransformationPoint());
                marker.startAnimation();
                mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                        .location(status.target).newVersion(0));
            }
        });


    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
        Toast.makeText(this, reverseGeoCodeResult.getAddress()+" adcode: "+reverseGeoCodeResult.getAdcode(),
                Toast.LENGTH_LONG).show();
        textAddress.setMovementMethod(ScrollingMovementMethod.getInstance());
//        textAddress
//                .setText(new Gson().toJson(reverseGeoCodeResult));

//        textAddress.setVisibility(View.VISIBLE);

        Log.e("tag","ReverseGeoCodeResult="+new Gson().toJson(reverseGeoCodeResult));
        for(int i=0;i<reverseGeoCodeResult.getPoiList().size();i++){
            Log.e("tag","ReverseGeoCodeResult11="+reverseGeoCodeResult.getPoiList().get(i).name);
        }

        if(reverseGeoCodeResult.getPoiList()!=null&&reverseGeoCodeResult.getPoiList().size()>0){
            textAddress.setText(reverseGeoCodeResult.getPoiList().get(0).name);
        }

    }
}
