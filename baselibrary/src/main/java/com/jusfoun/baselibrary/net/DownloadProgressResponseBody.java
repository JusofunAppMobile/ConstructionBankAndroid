package com.jusfoun.baselibrary.net;

import android.util.Log;

import com.jusfoun.baselibrary.base.UploadProgressModel;
import com.jusfoun.baselibrary.listener.DownloadProgressListener;

import java.io.IOException;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * Created by wang on 2016/11/14.
 */

public class DownloadProgressResponseBody extends ResponseBody {

    private ResponseBody body;
    private DownloadProgressListener listener;
    private BufferedSource source;
    private UploadProgressModel model;
    public DownloadProgressResponseBody(ResponseBody body
            , DownloadProgressListener listener){
        this.body=body;
        this.listener=listener;
        model=new UploadProgressModel();
        model.setContentLength(body.contentLength());
    }

    @Override
    public MediaType contentType() {
        return body.contentType();
    }

    @Override
    public long contentLength() {
        return body.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (source==null)
            source= Okio.buffer(source(body.source()));
        return source;
    }

    private Source source(Source source){
        return new ForwardingSource(source){
            long totalBytesRead=0L;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead=super.read(sink,byteCount);
                totalBytesRead+=bytesRead!=-1?bytesRead:0;
                model.setBytesLength(totalBytesRead);
                model.setBytesRead(bytesRead!=-1?bytesRead:0);
                model.setDone(bytesRead==-1);
                if (listener!=null){
                    listener.update(model);
                }
                return byteCount;
            }
        };
    }

}
