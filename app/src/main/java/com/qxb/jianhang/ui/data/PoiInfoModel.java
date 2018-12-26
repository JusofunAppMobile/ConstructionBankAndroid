package com.qxb.jianhang.ui.data;

import android.text.TextUtils;

import com.baidu.mapapi.search.core.PoiInfo;

import java.io.Serializable;

/**
 * @author liuguangdan
 * @version create at 2018/9/6/006 9:01
 * @Email lgd@jusfoun.com
 * @Description ${}
 */
public class PoiInfoModel implements Serializable {

    private static final long serialVersionUID = 2547023143335053992L;

    public String name;
    public String address;
    public double latitude;
    public double longitude;

    public PoiInfoModel(PoiInfo poiInfo) {
        name = poiInfo.name;
        address = poiInfo.address;
        latitude = poiInfo.location.latitude;
        longitude = poiInfo.location.longitude;
    }

    public PoiInfoModel() {

    }

    public String getFinalAddress() {
        if (TextUtils.isEmpty(address))
            return name;
        return address;
    }

}
