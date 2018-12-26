package com.jusfoun.jusfouninquire.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.jusfoun.baselibrary.view.SwipeBackLayout;

/**
 * @author zhaoyapeng
 * @version create time:15/10/30下午3:22
 * @Email zyp@jusfoun.com
 * @Description ${所有activity 基类}
 */
public abstract class BaseActivity extends FragmentActivity {
    protected Context mContext;
    protected String TAG="";
    protected abstract void initData();

    protected abstract void initView();

    protected abstract void initWidgetActions();
    protected SwipeBackLayout mSwipeBackLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG=this.getLocalClassName();
        mContext = this;
//        mSwipeBackLayout = getSwipeBackLayout();
//        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
//        mSwipeBackLayout.addSwipeListener(new SwipeBackLayout.SwipeListener() {
//            @Override
//            public void onScrollStateChange(int state, float scrollPercent) {
//
//            }
//
//            @Override
//            public void onEdgeTouch(int edgeFlag) {
//            }
//
//            @Override
//            public void onScrollOverThreshold() {
//            }
//        });
        initData();
        initView();
        initWidgetActions();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        MobclickAgent.onPageStart(this.getClass().getSimpleName());
//        MobclickAgent.onResume(mContext);

//        String channel = AppUtil.getAppMetaData(this, "UMENG_CHANNEL");
//        if (channel.equals("baidu")||channel.equals("91zhushou")||channel.equals("anzhuoshichang")) {
//            StatService.onResume(this);
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
//        MobclickAgent.onPageEnd(this.getClass().getSimpleName());
//        MobclickAgent.onPause(mContext);

//        String channel = AppUtil.getAppMetaData(this, "UMENG_CHANNEL");
//        if (channel.equals("baidu")||channel.equals("91zhushou")||channel.equals("anzhuoshichang")) {
//            StatService.onPause(this);
//        }
    }
}
