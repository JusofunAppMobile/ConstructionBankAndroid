package com.jusfoun.baselibrary.listener;

import com.jusfoun.baselibrary.base.UploadProgressModel;

/**
 * Created by wang on 2016/11/14.
 * 文件下载监听
 */

public interface DownloadProgressListener {
    /**
     * @param model 下载进度model
     */
    void update(UploadProgressModel model);
}
