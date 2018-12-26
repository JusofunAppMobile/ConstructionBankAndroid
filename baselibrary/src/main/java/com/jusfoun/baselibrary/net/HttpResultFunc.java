package com.jusfoun.baselibrary.net;

import com.jusfoun.baselibrary.base.BaseModel;

import rx.functions.Func1;

/**
 * Created by wang on 2016/11/11.
 */

public class HttpResultFunc<T> implements Func1<T,T> {
    @Override
    public T call(T t) {
        if (t instanceof BaseModel){
            if (((BaseModel) t).getResult()!=0)
            {
                new Exception(((BaseModel) t).getError());
            }
        }
        return t;
    }
}
