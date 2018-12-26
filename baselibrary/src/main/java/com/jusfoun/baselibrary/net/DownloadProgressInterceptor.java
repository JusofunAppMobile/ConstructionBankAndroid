package com.jusfoun.baselibrary.net;

import com.jusfoun.baselibrary.listener.DownloadProgressListener;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Created by wang on 2016/11/14.
 * 下载interceptor
 */

public class DownloadProgressInterceptor implements Interceptor {

    private DownloadProgressListener listener;

    public DownloadProgressInterceptor(DownloadProgressListener listener){
        this.listener=listener;
    }
    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = null;
        try{
            response = chain.proceed(chain.request());

        }catch (Exception e){
            e.printStackTrace();
        }
        return response.newBuilder()
                .body(new DownloadProgressResponseBody(response.body(),listener))
                .build();
    }
}
