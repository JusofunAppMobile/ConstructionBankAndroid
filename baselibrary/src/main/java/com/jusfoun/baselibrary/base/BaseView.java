package com.jusfoun.baselibrary.base;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.jusfoun.baselibrary.net.NetModel;

import rx.functions.Action1;

/**
 * @author zhaoyapeng
 * @version create time:17/12/2509:26
 * @Email zyp@jusfoun.com
 * @Description ${TODO}
 */
public abstract class BaseView extends RelativeLayout{
    protected Context mContext;
    protected RxManage rxManage;
    public BaseView(Context context) {
        super(context);
        mContext = context;
        initBaseData();
        initData();
        initViews();
        initActions();
    }

    public BaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initBaseData();
        initData();
        initViews();
        initActions();
    }

    public BaseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initBaseData();
        initData();
        initViews();
        initActions();
    }

    private void initBaseData(){
        rxManage = new RxManage();
    }

    protected abstract  void initData();

    protected  abstract void initViews();

    protected abstract  void initActions();


    /**
     * 添加网络请求
     * @param observable
     * @param action1
     */
    protected <T extends NetModel> void addNetwork(rx.Observable<T> observable, Action1<T> action1, Action1<Throwable> error){
        rxManage.add(observable, action1, error);
    }

}
