package com.jusfoun.jusfouninquire.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

import com.jusfoun.baselibrary.dialog.LoadingDialog;
import com.jusfoun.baselibrary.event.IEvent;
import com.jusfoun.jusfouninquire.util.VolleyUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


/**
 * @author zhaoyapeng
 * @version create time:15/10/30上午10:19
 * @Email zyp@jusfoun.com
 * @Description ${提取公共方法}
 */
public abstract class BaseInquireFragment extends BaseFragment {
    private LoadingDialog loadingDialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        initDialog();
    }

    /**
     * 页面跳转
     */
    protected void goActivity(Class<?> clazz) {
        Intent intent = new Intent(mContext, clazz);
        startActivity(intent);
    }

    protected void goActivity(Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(mContext, clazz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * 显示toast
     */
    protected void showToast(String msg) {
        if (msg != null && !msg.equals("")) {
            Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * 显示toast
     */
    protected void showToast(int msgid) {
        Toast.makeText(mContext, msgid, Toast.LENGTH_SHORT).show();
    }

    /**
     * 接收event 重写此方法
     * */
    @Subscribe
    public void onEvent(IEvent event){

    }

    protected void initDialog() {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(mContext, com.jusfoun.baselibrary.R.style.my_dialog);
            loadingDialog.setCancelable(true);
            loadingDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {

                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        if (loadingDialog != null)
                            loadingDialog.cancel();
                    }
                    return true;
                }
            });
            loadingDialog.setCanceledOnTouchOutside(false);
        }
    }

    public void showLoading() {
        if (isDetached()){
            return;
        }

        if (loadingDialog != null && !loadingDialog.isShowing()) {
            loadingDialog.show();
        }
    }

    // 隐藏加载对话框
    protected void hideLoadDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.cancel();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            VolleyUtil.getQueue(mContext).cancelAll(((Activity) mContext).getLocalClassName());
        }catch (Exception e){

        }
        EventBus.getDefault().unregister(this);
    }

    protected enum ViewType {
        RangeBegin,
        URL,
        ShareDialog,
        FeedBack,
        EDITPERSON,

        RangeEnd
    }
}
