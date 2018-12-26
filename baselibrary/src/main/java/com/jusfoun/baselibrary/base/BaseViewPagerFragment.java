package com.jusfoun.baselibrary.base;

import android.os.Bundle;
import android.view.View;

/**
 * @author zhaoyapeng
 * @date 2018/1/18
 * @describe 延迟加载数据 ViewPager中处于显示状态的Fragment才加载数据
 */

public abstract class BaseViewPagerFragment extends BaseFragment {

    protected boolean isInit; // 是否可以开始加载数据

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        // 每次切换fragment时调用的方法
        if (isVisibleToUser) {
            showData();
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isInit = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        // 判断当前fragment是否显示
        if (getUserVisibleHint()) {
            showData();
        }
    }

    /**
     * 初始化数据
     */
    private void showData() {
        if (isInit) {
            isInit = false;// 加载数据完成
            // 加载各种刷新数据
            refreshData();
        }
    }

    protected abstract void refreshData();
}
