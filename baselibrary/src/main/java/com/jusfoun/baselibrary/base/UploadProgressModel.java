package com.jusfoun.baselibrary.base;

import java.io.Serializable;

/**
 * Author  wangchenchen
 * CreateDate 2017-2-28.
 * Email wcc@jusfoun.com
 * Description 下载进度model
 */
public class UploadProgressModel implements Serializable {

    private long bytesRead;
    private long contentLength;
    private long bytesLength;
    private boolean done;

    public long getBytesRead() {
        return bytesRead;
    }

    public void setBytesRead(long bytesRead) {
        this.bytesRead = bytesRead;
    }

    public long getContentLength() {
        return contentLength;
    }

    public void setContentLength(long contentLength) {
        this.contentLength = contentLength;
    }

    public long getBytesLength() {
        return bytesLength;
    }

    public void setBytesLength(long bytesLength) {
        this.bytesLength = bytesLength;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
