package com.qxb.jianhang.ui.util;

import android.app.Activity;
import android.os.Build;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.jusfoun.baselibrary.Util.LogUtil;

/**
 * 百度定位管理器，需保证密钥一致
 */
public class BdLocationManager {

    private LocationClient locationClient;

    /**
     * 只定位一次
     */
    private boolean runOnlyOneTime = true;
    private BDLocation bdLocation;
    private int scanSpan;

    private void requestLocation(final Activity activity,
                                 final BDAbstractLocationListener listener) {
        LogUtil.e("正在开启定位...");
        locationClient = new LocationClient(activity.getApplicationContext());
        locationClient.registerLocationListener(new BDAbstractLocationListener() {

            @Override
            public void onReceiveLocation(BDLocation location) {

                if (location == null) {
                    LogUtil.e(">>>>无法定位");
                    return;
                }

                if (runOnlyOneTime && bdLocation != null)
                    return;

                if (runOnlyOneTime)
                    locationClient.stop();


                // http://lbsyun.baidu.com/index.php?title=android-locsdk/guide/v5-0

                /**
                 * 返回值：
                 61 ： GPS定位结果，GPS定位成功。
                 62 ： 无法获取有效定位依据，定位失败，请检查运营商网络或者wifi网络是否正常开启，尝试重新请求定位。
                 63 ： 网络异常，没有成功向服务器发起请求，请确认当前测试手机网络是否通畅，尝试重新请求定位。
                 65 ： 定位缓存的结果。
                 66 ： 离线定位结果。通过requestOfflineLocaiton调用时对应的返回结果。
                 67 ： 离线定位失败。通过requestOfflineLocaiton调用时对应的返回结果。
                 68 ： 网络连接失败时，查找本地离线定位时对应的返回结果。
                 161： 网络定位结果，网络定位定位成功。
                 162： 请求串密文解析失败，一般是由于客户端SO文件加载失败造成，请严格参照开发指南或demo开发，放入对应SO文件。
                 167： 服务端定位失败，请您检查是否禁用获取位置信息权限，尝试重新请求定位。
                 502： key参数错误，请按照说明文档重新申请KEY。
                 505： key不存在或者非法，请按照说明文档重新申请KEY。
                 601： key服务被开发者自己禁用，请按照说明文档重新申请KEY。
                 602： key mcode不匹配，您的ak配置过程中安全码设置有问题，请确保：sha1正确，“;”分号是英文状态；且包名是您当前运行应用的包名，请按照说明文档重新申请KEY。
                 501～700：key验证失败，请按照说明文档重新申请KEY
                 */
                int locType = location.getLocType();
                LogUtil.e("定位结果LocType=" + locType);

                LogUtil.e(parseBDLocation(location));

                if (location.getLocType() == 62 && Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    LogUtil.e("定位失败，无定位权限");
//                    new PermissionTipDialog(activity, "定位失败", "定位", false).show();
                    return;
                }

                if (listener != null)
                    listener.onReceiveLocation(location);

                bdLocation = location;
            }
        });
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd09ll");
        if (scanSpan >= 1000)
            option.setScanSpan(scanSpan);
        option.setIsNeedAddress(true);
        locationClient.setLocOption(option);
        locationClient.start();
    }

    /**
     * 定位
     *
     * @param activity
     * @param scanSpan 定位的间隔时间，单位s，如果小于1000s，则只定位一次，否则，没间隔scanSpan时间定位
     * @param listener
     */
    public void start(Activity activity, int scanSpan,
                      final BDAbstractLocationListener listener) {
        this.runOnlyOneTime = scanSpan < 1000 ? true : false;
        requestLocation(activity, listener);
    }

    /**
     * 定位，只定位一次
     *
     * @param activity
     * @param listener
     */
    public void start(Activity activity, BDAbstractLocationListener listener) {
        start(activity, 0, listener);
    }

    public void stop() {
        locationClient.stop();
    }

    /**
     * 将定位的对象转化成字符串信息
     *
     * @param location
     * @return
     */
    public static String parseBDLocation(BDLocation location) {
        StringBuffer sb = new StringBuffer(256);
        sb.append("time : ");
        sb.append(location.getTime());
        sb.append("\nerror code : ");
        sb.append(location.getLocType());
        sb.append("\nlatitude : ");
        sb.append(location.getLatitude());
        sb.append("\nlontitude : ");
        sb.append(location.getLongitude());
        sb.append("\nradius : ");
        sb.append(location.getRadius());
        sb.append("\nprovince : ");
        sb.append(location.getProvince());
        sb.append("\ncity : ");
        sb.append(location.getCity());
        sb.append("\ncityCode : ");
        sb.append(location.getCityCode());
        sb.append("\ndistrict : ");
        sb.append(location.getDistrict());
        sb.append("\nstreet : ");
        sb.append(location.getStreet());
        sb.append("\nstreet number: ");
        sb.append(location.getStreetNumber());
        if (location.getLocType() == BDLocation.TypeGpsLocation) {
            sb.append("\nspeed : ");
            sb.append(location.getSpeed());
            sb.append("\nsatellite : ");
            sb.append(location.getSatelliteNumber());
            sb.append("\ndirection : ");
            sb.append("\naddr : ");
            sb.append(location.getAddrStr());
            sb.append(location.getDirection());
        } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
            sb.append("\naddr : ");
            sb.append(location.getAddrStr());
            sb.append("\noperationers : ");
            sb.append(location.getOperators());
        }
        return sb.toString();
    }
}
