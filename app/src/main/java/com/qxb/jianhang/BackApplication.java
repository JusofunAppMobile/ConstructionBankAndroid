package com.qxb.jianhang;

import android.content.Context;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jusfoun.baselibrary.BaseApplication;
import com.jusfoun.baselibrary.net.Api;
import com.jusfoun.jusfouninquire.util.LoginSharePreference;
import com.qxb.jianhang.net.Interceptor.CommonInterceptor;
import com.qxb.jianhang.ui.data.UserInfoModel;

/**
 * @author zhaoyapeng
 * @version create time:18/8/2416:35
 * @Email zyp@jusfoun.com
 * @Description ${TODO}
 */
public class BackApplication extends BaseApplication {
    private static UserInfoModel mUserInfo;
    public static Context application = null;
    @Override
    public void onCreate() {
        super.onCreate();
        application = this;

        Api.getInstance().register(this,getString(R.string.req_jusfoun_url),new CommonInterceptor())
                .build();

        initMap();

    }


    // 初始化百度地图
    private void initMap(){
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        SDKInitializer.initialize(this);
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);
    }

    public static UserInfoModel getUserInfo() {
        if (mUserInfo == null) {
            mUserInfo = LoginSharePreference.getUserInfo(application);
        } else {
//            Log.d("TAG", "全局userInfo" + mUserInfo.toString());
        }
        return mUserInfo;
    }
}


