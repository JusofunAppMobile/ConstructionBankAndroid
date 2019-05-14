package com.jusfoun.baselibrary.net;

import android.content.Context;
import android.util.Log;

import com.jusfoun.baselibrary.BaseApplication;
import com.jusfoun.baselibrary.R;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CertificatePinner;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Author  wangchenchen
 * CreateDate 2016/7/5.
 * Email wcc@jusfoun.com
 * Description 请求网络
 */
public class Api {

    private int TIMEOUT = 60000;
    private OkHttpClient okHttpClient;
    private String baseUrl;

    public Retrofit retrofit;

    private static class SingletonHolder {
        private static final Api INSTANCE = new Api();
    }

    public static Api getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 注册网络连接
     *
     * @param mContext
     * @param baseUrl
     */
    public Api register(Context mContext, String baseUrl) {
        File cacheFile = new File(mContext.getApplicationContext().getCacheDir(), "cache");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 100);//100Mb
        //设置证书
        CertificatePinner.Builder builder = new CertificatePinner.Builder();
//        builder.add("sbbic.com","sha1");
        okHttpClient = new OkHttpClient.Builder()
                .readTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(TIMEOUT,TimeUnit.MILLISECONDS)
                .addInterceptor(new HeaderInterceptor(mContext))
//                .addInterceptor(new CommonInterceptor())
                .addInterceptor(new LoggingInterceptor())
                .addInterceptor(new CacheInterceptor(mContext))
                //https证书锁定
//                .certificatePinner(builder.build())
                .cache(cache)
                .build();

        this.baseUrl = baseUrl;
        return this;
    }


    public Api register(Context mContext, String baseUrl,Interceptor interceptor) {

        File cacheFile = new File(mContext.getApplicationContext().getCacheDir(), "cache");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 100);//100Mb
        //设置证书
        CertificatePinner.Builder builder = new CertificatePinner.Builder();
//        builder.add("sbbic.com","sha1");
        okHttpClient = new OkHttpClient.Builder()
                .readTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(TIMEOUT,TimeUnit.MILLISECONDS)
                .addInterceptor(new HeaderInterceptor(mContext))
                .addInterceptor(interceptor)
                .addInterceptor(new LoggingInterceptor())
                .addInterceptor(new CacheInterceptor(mContext))
                //https证书锁定
//                .certificatePinner(builder.build())
                .cache(cache)
                .build();

        this.baseUrl = baseUrl;
        return this;
    }

    public void build() {
        retrofit = new Retrofit.Builder()
                .addConverterFactory(JacksonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(new NullOnEmptyConverterFactory())
                .client(okHttpClient)
                .baseUrl(baseUrl)
                .build();
    }

    /**
     * 添加Interceptor
     *
     * @param interceptor
     */
    public Api addInterceptro(Interceptor interceptor) {
        if (okHttpClient != null) {
            okHttpClient = okHttpClient.newBuilder()
                    .addInterceptor(interceptor)
                    .build();
        }
        return this;
    }

    /**
     * 获取路由表
     *
     * @param apiService
     * @param <T>
     * @return
     */
    public <T> T getService(Class<T> apiService) {
        if (retrofit == null) {
            throw new IllegalArgumentException("retrofit not register build");
        }
        return retrofit.create(apiService);
    }

    private Api() {
//        register(BaseApplication.getBaseApplication(), BaseApplication.getBaseApplication().getString(R.string.req_jusfoun_url));
//        build();
    }
}
