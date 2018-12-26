package com.jusfoun.jusfouninquire.callback;

/**
 * @author zhaoyapeng
 * @version create time:15/11/2上午11:15
 * @Email zyp@jusfoun.com
 * @Description ${网络 请求回调}
 */
public interface NetWorkCallBack {
    void onSuccess(Object data);

    void onFail(String error);
}
