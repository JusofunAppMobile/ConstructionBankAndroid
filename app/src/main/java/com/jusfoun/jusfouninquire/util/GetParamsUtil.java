package com.jusfoun.jusfouninquire.util;
import android.util.Log;

import com.jusfoun.jusfouninquire.TimeOut;
import com.qxb.jianhang.BackApplication;

import java.net.URLEncoder;
import java.util.Map;


/**
 * @author zhaoyapeng
 * @version create time:15/11/10上午9:09
 * @Email zyp@jusfoun.com
 * @Description ${TODO}
 */
public class GetParamsUtil {

    public static String getParmas(String url, Map<String, String> params) {
        String tempURL = url+"?";
        if(params!=null&&(!params.containsKey("t")||!params.containsKey("m"))){
            if(BackApplication.application!=null){
                TimeOut timeOut = new TimeOut(BackApplication.application);
                params.put("t", timeOut.getParamTimeMollis()+"");
                params.put("m", timeOut.MD5time()+"");
            }
        }
        try{

        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                tempURL = tempURL + key + "=" + URLEncoder.encode(params.get(key),"UTF-8") + "&";
            }
        }

        }catch (Exception e){

        }

        Log.e("tag","tempURL="+tempURL);
        return tempURL;
    }
}
