package com.jusfoun.baselibrary.Util;

import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.jusfoun.baselibrary.BuildConfig;


/**
 * Author  wangchenchen
 * CreateDate 2016/7/6.
 * Email wcc@jusfoun.com
 * Description log工具，debug打印日志 release不打印
 */
public class LogUtil {

    public final static String DEFAULT_TAG="base_tag";
    public static boolean debugable = true;

    public static void setDebugable(boolean debug) {
        debugable = debug;
    }

    public static void d(String msg){
        d(DEFAULT_TAG,msg);
    }

    public static void v(String msg){
        v(DEFAULT_TAG,msg);
    }

    public static void w(String msg){
        w(DEFAULT_TAG,msg);
    }

    public static void i(String msg){
        i(DEFAULT_TAG,msg);
    }

    public static void e(String msg){
        e(DEFAULT_TAG,msg);
    }


    public static void d(String tag, String msg) {
        if (!debugable) {
            return;
        }
        if (TextUtils.isEmpty(tag)){
            tag=DEFAULT_TAG;
        }
        Log.d(tag, msg);
    }

    public static void v(String tag, String msg) {
        if (!debugable) {
            return;
        }
        if (TextUtils.isEmpty(tag)){
            tag=DEFAULT_TAG;
        }
        Log.v(tag, msg);
    }

    public static void w(String tag, String msg) {
        if (!debugable) {
            return;
        }
        if (TextUtils.isEmpty(tag)){
            tag=DEFAULT_TAG;
        }
        Log.w(tag, msg);
    }

    public static void e(String tag, String msg) {
        if (!debugable) {
            return;
        }
        if (TextUtils.isEmpty(tag)){
            tag=DEFAULT_TAG;
        }
        Log.e(tag, msg);
    }

    public static void i(String tag, String msg) {
        if (!debugable) {
            return;
        }
        if (TextUtils.isEmpty(tag)){
            tag=DEFAULT_TAG;
        }
        Log.i(tag, msg);
    }

    public static void e(String tag, Exception e) {
        if (!debugable) {
            return;
        }
        if (TextUtils.isEmpty(tag)){
            tag=DEFAULT_TAG;
        }
        if (BuildConfig.DEBUG)
            Log.d(tag, e.toString());
    }

    private static ILogOutPutListener mListenr;

    public static void setLogOutputlistener(ILogOutPutListener listener) {
        mListenr = listener;
    }

    /**
     * 外部 日志，用于打印 友盟或者buggly等统计sdk日志
     */
    public interface ILogOutPutListener {
        void v(String tag, String str);

        void d(String tag, String str);

        void i(String tag, String str);

        void w(String tag, String str);

        void e(String tag, String str);

        void e(String tag, String str, Throwable e);
    }
}
