package com.qxb.jianhang.ui.fragment;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.jusfoun.baselibrary.Util.ThreadPoolUtil;
import com.jusfoun.baselibrary.Util.ToastUtils;
import com.jusfoun.baselibrary.event.IEvent;
import com.jusfoun.baselibrary.net.Api;
import com.jusfoun.baselibrary.net.NetModel;
import com.qxb.jianhang.R;
import com.qxb.jianhang.net.event.SearchEvent;
import com.qxb.jianhang.ui.constant.Constant;
import com.qxb.jianhang.ui.data.CompanyListModel;
import com.qxb.jianhang.ui.data.HomeDataItemModel;
import com.qxb.jianhang.ui.data.SearchListModel;
import com.qxb.jianhang.ui.event.CloseHomeViewEvent;
import com.qxb.jianhang.ui.net.ApiService;
import com.qxb.jianhang.ui.sharedpreferences.LocationSharepreferences;
import com.qxb.jianhang.ui.util.LatLonUtil;
import com.qxb.jianhang.ui.util.MathHelper;
import com.qxb.jianhang.ui.view.HomeMultipleCompanyView;
import com.qxb.jianhang.ui.view.HomeOneCompanyView;
import com.qxb.jianhang.ui.view.OverlayView;
import com.qxb.jianhang.ui.view.SearchMapTitleView;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.functions.Action1;

/**
 * @author zhaoyapeng
 * @version create time:18/8/2416:51
 * @Email zyp@jusfoun.com
 * @Description ${地图fragment}
 */
public class MapFragment extends BaseBackFragment implements SensorEventListener, View.OnClickListener {

    protected ImageView imgLocation;
    protected HomeMultipleCompanyView viewMultipleCompany;
    protected HomeOneCompanyView viewOneCompany;
    // 定位相关
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    private MyLocationConfiguration.LocationMode mCurrentMode;
    private static final int accuracyCircleFillColor = 0xAAFFFF88;
    private static final int accuracyCircleStrokeColor = 0xAA00FF00;
    private SensorManager mSensorManager;
    private Double lastX = 0.0;
    private int mCurrentDirection = 0;
    private double mCurrentLat = 0.0;
    private double mCurrentLon = 0.0;
    private float mCurrentAccracy;
    private BDLocation bdLocation;

    MapView mMapView;
    BaiduMap mBaiduMap;

    boolean isFirstLoc = true; // 是否首次定位
    private MyLocationData locData;
    private float direction;


    private BitmapDescriptor addIcon;
    private BitmapDescriptor targetIcon;
    private BitmapDescriptor formalIcon;

    private List<BitmapDescriptor> moreBitmapDesList;

    private SearchListModel model;

    private Handler handler;

    private LinearLayout tagLayout;

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_map;
    }


    public static MapFragment getInstance(SearchListModel model) {
        MapFragment fragment = new MapFragment();
        if (model != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("model", model);
            fragment.setArguments(bundle);

        }
        return fragment;
    }


    @Override
    public void initDatas() {
        addIcon = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_home_tag_add_map);
        targetIcon = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_home_tag_mubiao_map);
        formalIcon = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_home_tag_zhengshi_map);

        moreBitmapDesList = new ArrayList<>();

        if (getArguments() != null && getArguments().get("model") != null) {
            model = (SearchListModel) getArguments().get("model");
        }

        handler = new Handler();


    }

    @Override
    public void initView(View rootView) {
        // 地图初始化
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        imgLocation = (ImageView) rootView.findViewById(R.id.img_location);
        imgLocation.setOnClickListener(MapFragment.this);
        viewMultipleCompany = (HomeMultipleCompanyView) rootView.findViewById(R.id.view_multiple_company);
        viewOneCompany = (HomeOneCompanyView) rootView.findViewById(R.id.view_one_company);
        tagLayout = (LinearLayout) rootView.findViewById(R.id.layout_tag);

    }

    @Override
    public void initAction() {

        if (model != null) {
            tagLayout.setVisibility(View.VISIBLE);
        }
        mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);//获取传感器管理服务
        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;

//        mCurrentMarker = BitmapDescriptorFactory
//                .fromResource(R.drawable.__leak_canary_icon);
        mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(
                mCurrentMode, true, null,
                accuracyCircleFillColor, accuracyCircleStrokeColor));

        mBaiduMap.getLocationConfiguration().accuracyCircleFillColor = 0x00000000;
        mBaiduMap.getLocationConfiguration().accuracyCircleStrokeColor = 0x00000000;
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(mContext);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setIsNeedAddress(true);
        option.setScanSpan(1000);
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        mLocClient.setLocOption(option);
        mLocClient.start();


        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                HomeDataItemModel homeDataItemModel = markerMap.get(marker);
                if (model == null || model.searchType == SearchMapTitleView.TYPE_ADDRESS) {
                    getCompanyByPoint(homeDataItemModel);
                } else {
                    ArrayList<HomeDataItemModel> list = searchMap.get(homeDataItemModel.latitude + "," + homeDataItemModel.longitude);

                    if (list!=null&&list.size() > 1) {
                        SearchListModel searchListModel = new SearchListModel();
                        searchListModel.list = list;
                        searchListModel.totalCount = list.size();
                        viewMultipleCompany.setData(searchListModel, null);
                    } else if (list!=null&&list.size() == 1) {
                        viewOneCompany.setData(list.get(0));
                    }
                }

                return false;
            }
        });

        viewMultipleCompany.setCallBck(new TagCallBack() {
            @Override
            public void setTag(HomeDataItemModel model) {
                addTag(model);
            }
        });

        viewOneCompany.setCallBck(new TagCallBack() {
            @Override
            public void setTag(HomeDataItemModel model) {
                addTag(model);
            }
        });


    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        double x = sensorEvent.values[SensorManager.DATA_X];
        if (Math.abs(x - lastX) > 1.0) {
            mCurrentDirection = (int) x;
            locData = new MyLocationData.Builder()
                    .accuracy(mCurrentAccracy)
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(mCurrentDirection).latitude(mCurrentLat)
                    .longitude(mCurrentLon).build();
            mBaiduMap.setMyLocationData(locData);
        }
        lastX = x;

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.img_location) {
            isFirstLoc = true;
            mLocClient.start();
        }
    }


    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            mCurrentLat = location.getLatitude();
            mCurrentLon = location.getLongitude();
            mCurrentAccracy = location.getRadius();
            locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(mCurrentDirection).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            bdLocation = location;

            if (isFirstLoc) {
                LocationSharepreferences.saveLocation(mContext, location.getLatitude(), location.getLongitude());
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(14.8f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                if (model != null) {
                    if(model.searchType == SearchMapTitleView.TYPE_ADDRESS){
                        drawCompany(model.list);
                    }else{
                        drawSearchCompany(model.list);

                    }
                    drawCompanyLocationCircle(location);

                } else {
                    getMapNet(location);
                }
            }

        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onResume() {
        mMapView.onResume();
        super.onResume();
        //为系统的方向传感器注册监听器
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onStop() {
        //取消注册传感器监听
        mSensorManager.unregisterListener(this);
        super.onStop();
    }

    @Override
    public void onDestroy() {
        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }


    private void drawCompanyLocationCircle(BDLocation location) {

        // 绘制定位图层
        LatLng llCircle = new LatLng(location.getLatitude(), location.getLongitude());
        OverlayOptions ooCircle = new CircleOptions().fillColor(0x11FFCC35)
                .center(llCircle).stroke(new Stroke(2, 0x11FFCC35))
                .radius(2000);
        mBaiduMap.addOverlay(ooCircle);


        ////绘制500米虚线
        LatLng p1 = new LatLng(location.getLatitude(), location.getLongitude());
        LatLng p2 = LatLonUtil.getLatLonByAngleAndDis(location.getLongitude(), location.getLatitude(), 90, 2000);
        List<LatLng> points = new ArrayList<>();
        points.add(p1);
        points.add(p2);

        OverlayOptions ooPolyline = new PolylineOptions().width(2)
                .color(0xffffac00).points(points);
        Polyline mPolyline = (Polyline) mBaiduMap.addOverlay(ooPolyline);
        // 设置为虚线
        mPolyline.setDottedLine(true);


        // 绘制文字
        LatLng textLatLng = LatLonUtil.getLatLonByAngleAndDis(location.getLongitude(), location.getLatitude(), 85, 400);
        OverlayOptions textOption = new TextOptions()
                .fontSize(34)
                .fontColor(0xFFFE9C31)
                .text("2000m")
                .rotate(0)
                .position(textLatLng);

//在地图上添加该文字对象并显示
        mBaiduMap.addOverlay(textOption);

    }


    private Map<Marker, HomeDataItemModel> markerMap = new HashMap<>();

    private void drawCompany(final List<HomeDataItemModel> list) {
        builder = new LatLngBounds.Builder();
        ThreadPoolUtil.threadPool.execute(new Runnable() {
            @Override
            public void run() {

                for (int i = 0; i < moreBitmapDesList.size(); i++) {
                    moreBitmapDesList.get(i).recycle();
                }

                for (int i = 0; i < list.size(); i++) {

                    if (list.get(i) != null && list.get(i).topLeft != null && list.get(i).bottomRight != null) {
                        try {

                            Double lon = Math.random() * (list.get(i).bottomRight.lon - list.get(i).topLeft.lon) + list.get(i).topLeft.lon;
                            Double lat = Math.random() * (list.get(i).topLeft.lat - list.get(i).bottomRight.lat) + list.get(i).bottomRight.lat;

                            LatLng llA = new LatLng(lat, lon);
                            MarkerOptions ooA;


                            if (Constant.TYPE_UNMARKED.equals(list.get(i).type + "")) {
                                ooA = new MarkerOptions().position(llA).icon(addIcon)
                                        .zIndex(9).draggable(true);
                            } else if (Constant.TYPE_TRACK.equals(list.get(i).type + "")) {
                                ooA = new MarkerOptions().position(llA).icon(targetIcon)
                                        .zIndex(9).draggable(true);
                            } else if (Constant.TYPE_FORMAL.equals(list.get(i).type + "")) {
                                ooA = new MarkerOptions().position(llA).icon(formalIcon)
                                        .zIndex(9).draggable(true);
                            } else if (Constant.TYPE_MORE.equals(list.get(i).type + "")) {
                                OverlayView overlayView = new OverlayView(mContext);
                                overlayView.setData(list.get(i).docCount);
                                BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory
                                        .fromView(overlayView);
                                moreBitmapDesList.add(bitmapDescriptor);

                                ooA = new MarkerOptions().position(llA).icon(bitmapDescriptor)
                                        .zIndex(9).draggable(true);
                            } else {
                                ooA = new MarkerOptions().position(llA).icon(addIcon)
                                        .zIndex(9).draggable(true);
                            }

                            // 掉下动画
//                    ooA.animateType(MarkerOptions.MarkerAnimateType.drop);

                            Marker marker = (Marker) (mBaiduMap.addOverlay(ooA));
//                            markerMap.put((Marker) (mBaiduMap.addOverlay(ooA)), list.get(i));
                            markerMap.put(marker, list.get(i));
                            builder.include(marker.getPosition());


                        } catch (Exception e) {
                        }

                    }
                }


                mBaiduMap.animateMapStatus(MapStatusUpdateFactory
                        .newLatLngBounds(builder.build()));

            }
        });

    }


    private Map<String, ArrayList<HomeDataItemModel>> searchMap = new HashMap<>();
    LatLngBounds.Builder builder;

    private void drawSearchCompany(List<HomeDataItemModel> list) {

        markerMap.clear();
        mBaiduMap.clear();
        moreBitmapDesList.clear();
        searchMap.clear();
        builder = new LatLngBounds.Builder();
        for (int i = 0; i < moreBitmapDesList.size(); i++) {
            moreBitmapDesList.get(i).recycle();
        }

        for (int i = 0; i < list.size(); i++) {
            HomeDataItemModel model = list.get(i);
            String key = model.latitude + "," + model.longitude;
            if (!TextUtils.equals(key, ",")) {
                if (searchMap.containsKey(key)) {
                    searchMap.get(key).add(model);
                } else {
                    ArrayList<HomeDataItemModel> l = new ArrayList<>();
                    l.add(model);
                    searchMap.put(key, l);
                }
            }
        }


        for (ArrayList<HomeDataItemModel> cList : searchMap.values()) {

            try {
                MarkerOptions ooA;
                if (cList != null && cList.size() > 0) {
                    HomeDataItemModel model = cList.get(0);
                    Double lon = Double.parseDouble(model.longitude);
                    Double lat = Double.parseDouble(model.latitude);


                    if (lon == 0.0 && lat == 0.0)
                        continue;
                    LatLng llA = new LatLng(lat, lon);
                    if (cList.size() == 1) {
                        if (Constant.TYPE_UNMARKED.equals(model.type + "")) {
                            ooA = new MarkerOptions().position(llA).icon(addIcon)
                                    .zIndex(9).draggable(true);
                        } else if (Constant.TYPE_TRACK.equals(model.type + "")) {
                            ooA = new MarkerOptions().position(llA).icon(targetIcon)
                                    .zIndex(9).draggable(true);
                        } else if (Constant.TYPE_FORMAL.equals(model.type + "")) {
                            ooA = new MarkerOptions().position(llA).icon(formalIcon)
                                    .zIndex(9).draggable(true);
                        } else {
                            ooA = new MarkerOptions().position(llA).icon(addIcon)
                                    .zIndex(9).draggable(true);
                        }
                    } else {
                        OverlayView overlayView = new OverlayView(mContext);
                        overlayView.setData(cList.size());
                        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory
                                .fromView(overlayView);
                        moreBitmapDesList.add(bitmapDescriptor);

                        ooA = new MarkerOptions().position(llA).icon(bitmapDescriptor)
                                .zIndex(9).draggable(true);
                    }
                    // 掉下动画
//                    ooA.animateType(MarkerOptions.MarkerAnimateType.drop);
                    Marker marker = (Marker) (mBaiduMap.addOverlay(ooA));
                    markerMap.put(marker, model);
                    builder.include(marker.getPosition());
                }


            } catch (Exception e) {
                Log.e("tag","listlistlist3="+searchMap.size());
            }

        }

        mBaiduMap.animateMapStatus(MapStatusUpdateFactory
                .newLatLngBounds(builder.build()));


    }


    private boolean isShow = true;

    private void getMapNet(final BDLocation location) {
        showLoadDialog();

        HashMap<String, String> map = new HashMap<>();
        map.put("myLongitude", location.getLongitude() + "");
        map.put("myLatitude", location.getLatitude() + "");

//        map.put("myLongitude",  "120.2118950000");
//        map.put("myLatitude",  "30.2645270000");
        map.put("precision", "6");
//        map.put("userId", "1");
        map.put("merge", "1");
        addNetwork(Api.getInstance().getService(ApiService.class).getHomeMapNet(map), new Action1<NetModel>() {
            @Override
            public void call(NetModel net) {

                if (net.success()) {
                    CompanyListModel model = net.dataToObject(CompanyListModel.class);

                    markerMap.clear();
//                    if (model.list != null && model.list.size() > 0) {
//                        // 绘制企业 附近
//                        if (model.org != null) {
//                            BDLocation bdLocation = new BDLocation();
//                            bdLocation.setLatitude(model.org.lat);
//                            bdLocation.setLongitude(model.org.lon);
//                            mBaiduMap.clear();
//                            drawCompanyLocationCircle(location);
//                            drawCompany(model.list);
//                        }
//
//
//                    }

                    if (model.list2 != null && model.list2.size() > 0) {
                        mBaiduMap.clear();
                        // 绘制个人定位周围附近
                        drawCompanyLocationCircle(location);
                        drawCompany(model.list2);

                        if (isShow) {
                            getCompanyByPoint(model.list.get(0));
                        }

                    }
                    hideLoadDialog();
//                    drawCompany(model.list);

//                        showToast("标记成功");
                } else {
                    hideLoadDialog();
                    showToast(net.msg);
                }
                isShow = false;
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                hideLoadDialog();
                ToastUtils.showHttpError();
            }
        });
    }

    private void getCompanyByPoint(HomeDataItemModel model) {
        showLoadDialog();

        final HashMap<String, Object> map = new HashMap<>();

        Log.e("tag", "model.topLeft=" + model.topLeft);
        try {
            double leftLat = model.topLeft.lat;
            double leftLon = model.topLeft.lon;

            double rightLat = model.bottomRight.lat;
            double rightLon = model.bottomRight.lon;


            if ((model.topLeft.lat == model.bottomRight.lat)) {
//                topLeft = URLEncoder.encode((MathHelper.doubleKeepDecimals(model.topLeft.lat + 0.000001)) + "," + MathHelper.doubleKeepDecimals(model.topLeft.lon), "utf-8");

                leftLat = MathHelper.doubleKeepDecimals(model.topLeft.lat + 0.000001);
                rightLat = MathHelper.doubleKeepDecimals(model.bottomRight.lat);
            }


            if (model.topLeft.lon == model.bottomRight.lon) {
//                bottomRight = URLEncoder.encode(MathHelper.doubleKeepDecimals(model.topLeft.lat) + "," + (MathHelper.doubleKeepDecimals(model.topLeft.lon + 0.000001)), "utf-8");

                leftLon = MathHelper.doubleKeepDecimals(model.topLeft.lon);
                rightLon = MathHelper.doubleKeepDecimals(model.bottomRight.lon + 0.000001);
            }

            String topLeft = URLEncoder.encode(leftLat + "," + leftLon, "utf-8");
            String bottomRight = URLEncoder.encode(rightLat + "," + rightLon, "utf-8");
//            map.put("userId", "1");
            map.put("topLeft", topLeft);
            map.put("bottomRight", bottomRight);

            map.put("myLongitude", mCurrentLon + "");
            map.put("myLatitude", mCurrentLat + "");

            map.put("pageSize", "20");
            map.put("pageIndex", "1");
        } catch (Exception e) {
            Log.e("tag", "map ==" + e);
        }


        addNetwork(Api.getInstance().getService(ApiService.class).getCompanyListByPoint(map), new Action1<NetModel>() {
            @Override
            public void call(NetModel net) {
                hideLoadDialog();
                if (net.success()) {
                    SearchListModel model = net.dataToObject(SearchListModel.class);
                    if (model.list.size() > 1) {
//                        model.totalCount = net.totalCount;
                        viewMultipleCompany.setData(model, map);
                    } else if (model.list.size() == 1) {
                        viewOneCompany.setData(model.list.get(0));
                    }
//                        showToast("标记成功");
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
    public void onDestroyView() {
        super.onDestroyView();
        addIcon.recycle();
        targetIcon.recycle();
        formalIcon.recycle();

        for (int i = 0; i < moreBitmapDesList.size(); i++) {
            moreBitmapDesList.get(i).recycle();
        }
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
                    model.type = Constant.TYPE_TRACK;
                    viewOneCompany.refresh();
                    viewMultipleCompany.refresh();
                    showToast("标记成功");
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

    public interface TagCallBack {
        void setTag(HomeDataItemModel model);

    }

    @Override
    public void onEvent(final IEvent event) {
        super.onEvent(event);
        if (event instanceof SearchEvent) {
            if (((SearchEvent) event).model != null && model != null) {
                model = ((SearchEvent) event).model;
                if(model.searchType == SearchMapTitleView.TYPE_ADDRESS){
                    drawCompany(model.list);
                }else{
                    drawSearchCompany(model.list);

                }
                drawCompanyLocationCircle(bdLocation);
//                    }

            }
        } else if (event instanceof CloseHomeViewEvent) {
            viewMultipleCompany.close();
            viewOneCompany.close();
        }
    }

    public boolean onBackPressed() {
        if (viewMultipleCompany.isShowGoClose() || viewOneCompany.isShowGoClose()) {
            return false;
        }
        return true;
    }


}