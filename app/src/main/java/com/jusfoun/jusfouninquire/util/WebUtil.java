package com.jusfoun.jusfouninquire.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebSettings;

import com.jusfoun.jusfouninquire.activity.WebActivity;

/**
 * Created by JUSFOUN on 2015/8/28.
 * Description
 */
public class WebUtil {

    public static final String USERAGENT="device/AndroidInquireClient";
    public static void getUserAgentString(WebSettings settings, Context context){
        PackageManager packageManager=context.getPackageManager();
        String pkName=context.getPackageName();
        String versionName="";
        try {
            versionName = "(v"+packageManager.getPackageInfo(pkName,0).versionName+")";
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        settings.setUserAgentString(settings.getUserAgentString()+" "+USERAGENT+versionName);
        Log.e("useragent",settings.getUserAgentString());
    }

    public static boolean getUrlProtocol(String oldUrl, String newUrl){
        if(TextUtils.isEmpty(oldUrl) || TextUtils.isEmpty(newUrl)){
            return false;
        }

        int index=-1;
        if(oldUrl.contains("?")){
            index = oldUrl.indexOf("?");
        }else if (oldUrl.contains(".html")){
            index=oldUrl.indexOf(".html");
        }else if (oldUrl.contains(".HTML")){
            index=oldUrl.indexOf(".HTML");
        }
        if(index <= 0){
            return false;
        }
        if(newUrl.length() >= index){
            if (oldUrl.substring(0,index).equals(newUrl.substring(0,index)))
                return true;
        }
        return false;


    }

    /**
     * webvew跳转新页面
     * @param context
     * @param url
     * @param title
     * @param data
     */
    public static void startDetialActivity(Context context, String url, String title) {
        Bundle bundle = new Bundle();
        bundle.putString(WebActivity.URL, url);
        bundle.putString(WebActivity.TITLE, title);
        bundle.putBoolean(WebActivity.IS_LOAD_DETAIL_DATA, false);
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

}
