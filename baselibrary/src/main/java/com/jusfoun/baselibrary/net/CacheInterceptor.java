package com.jusfoun.baselibrary.net;

import android.content.Context;

import com.jusfoun.baselibrary.Util.NetWorkUtil;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Author  wangchenchen
 * CreateDate 2016/7/5.
 * Email wcc@jusfoun.com
 * Description 缓存
 */
public class CacheInterceptor implements Interceptor {

    private Context mContext;
    public CacheInterceptor(Context mContext){
        this.mContext=mContext.getApplicationContext();
        CacheControl.Builder cacheBuilder=new CacheControl.Builder();
        cacheBuilder.maxAge(0, TimeUnit.SECONDS);//控制缓存的最大生命周期
        cacheBuilder.maxStale(365, TimeUnit.DAYS);//控制缓存过时时间
        CacheControl cacheControl=cacheBuilder.build();
    }
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request=chain.request();
        if (!NetWorkUtil.isNetConnected(mContext)){
            request=request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
        }
        Response response = null;
        try{
            response = chain.proceed(request);


        }catch (Exception e){
            e.printStackTrace();
        }
        if (NetWorkUtil.isNetConnected(mContext)){
            String cacheControl=request.cacheControl().toString();
            return response.newBuilder()
                    .header("Cache-Control", cacheControl)
                    .removeHeader("Pragma")
                    .build();
        }else {
            return response.newBuilder()
                    .header("Cache-Control","public, only-if-cached, max-stale=2419200")
                    .removeHeader("Pragma")
                    .build();
        }
    }
}
