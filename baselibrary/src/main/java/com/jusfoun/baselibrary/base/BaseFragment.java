package com.jusfoun.baselibrary.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jusfoun.baselibrary.R;
import com.jusfoun.baselibrary.dialog.LoadingDialog;
import com.jusfoun.baselibrary.event.IEvent;
import com.jusfoun.baselibrary.net.NetModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.concurrent.ThreadPoolExecutor;

import rx.functions.Action1;

/**
 * Created by wang on 2016/11/8.
 * fragment 基类
 */

public abstract class BaseFragment extends Fragment {
    protected Context mContext;
    protected RxManage rxManage;
    protected View rootView;
    private LoadingDialog loadingDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext=context;
        rxManage=new RxManage();
        initDatas();

        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView=inflater.inflate(getLayoutResId(),container,false);
        initDialog();
        initView(rootView);
        initAction();
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        rxManage.clear();//fragment销毁清除rxbus事件及网络请求，防止内存泄漏
        EventBus.getDefault().unregister(this);
    }

    public void showToast(String text){
        Toast.makeText(mContext,text, Toast.LENGTH_SHORT).show();
    }
    public void showToast(int stringId){
        Toast.makeText(mContext,getString(stringId), Toast.LENGTH_SHORT).show();
    }

    public <T extends View> T findViewById(int resId){
        if (rootView!=null){
            return (T) rootView.findViewById(resId);
        }
        return null;
    }



    /**
     * 添加网络请求
     * @param observable
     * @param action1
     */
    protected <T extends NetModel> void addNetwork(rx.Observable<T> observable, Action1<T> action1, Action1<Throwable> error){
        rxManage.add(observable, action1, error);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public abstract int getLayoutResId();
    public abstract void initDatas();
    public abstract void initView(View rootView);
    public abstract void initAction();
    protected void showLoadDialog(){
        if(mContext == null){
            return;
        }
        if (loadingDialog != null && !loadingDialog.isShowing()) {
            loadingDialog.show();
        }
    }

    protected void hideLoadDialog(){
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.cancel();
        }
    }

    private void initDialog(){
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(mContext, R.style.my_dialog);
            loadingDialog.setCancelable(true);
            loadingDialog.setCanceledOnTouchOutside(false);
        }
    }


    @Subscribe
    public void onEvent(IEvent event) {
    }
}
