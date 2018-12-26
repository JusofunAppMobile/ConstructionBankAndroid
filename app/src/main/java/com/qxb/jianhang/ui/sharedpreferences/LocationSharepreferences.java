package com.qxb.jianhang.ui.sharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.baidu.mapapi.model.LatLng;
import com.qxb.jianhang.ui.data.LonLatModel;

/**
 * @author zhaoyapeng
 * @version create time:16/7/2122:29
 * @Email zyp@jusfoun.com
 * @Description ${TODO}
 */
public class LocationSharepreferences {

    private static final String LOCATION="location";
    private static final String LAT="lat";
    private static final String LON="lon";



    public static LonLatModel getLocation(Context context){
        SharedPreferences sharedPreferences=context.getSharedPreferences(LOCATION, Context.MODE_PRIVATE);
        LonLatModel model = new LonLatModel();
        model.lat = sharedPreferences.getString(LAT,"");
        model.lon = sharedPreferences.getString(LON,"");
        return model;
    }

    public static void saveLocation(Context context, double lat, double lon){
        SharedPreferences sharedPreferences=context.getSharedPreferences(LOCATION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(LAT,lat+"");
        editor.putString(LON,lon+"");
        editor.apply();
    }
}
