package com.jusfoun.jusfouninquire.volley;

import com.android.volley.VolleyError;

/**
 * @author zhaoyapeng
 * @version create time:15-3-25下午2:27
 * @Email zhaoyp@witmob.com
 * @Description ${volley 网络请求 错误处理类}
 */
public class VolleyErrorUtil {
    public static String disposeError(VolleyError volleyError) {
        String error = volleyError.toString();
        if (error.contains("TimeoutError")) {
            return "您的网络状况不好";
        }else if(error.contains("NoConnectionError")){
            return "请检查网络";
        }else if(error.contains("NetworkError")){
            return "网络不给力，请稍后重试";
        }
        return "网络不给力，请稍后重试";
    }
}
