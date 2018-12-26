package com.jusfoun.baselibrary.Util;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author zhaoyapeng
 * @version create time:2018/11/1517:29
 * @Email zyp@jusfoun.com
 * @Description ${TODO}
 */
public class ThreadPoolUtil {
    private static final int DEFAULT_THREAD_POOL_SIZE = 10;

    public static final ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
            DEFAULT_THREAD_POOL_SIZE, 10, 1L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>());
}
