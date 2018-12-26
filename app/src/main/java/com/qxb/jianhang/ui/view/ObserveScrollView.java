package com.qxb.jianhang.ui.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Author  wangchenchen
 * CreateDate 2016/8/15.
 * Email wcc@jusfoun.com
 * Description 监听scroll滚动view
 */
public class ObserveScrollView extends ScrollView {

    public ObserveScrollView(Context context) {
        super(context);
    }

    public ObserveScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ObserveScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ObserveScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (onScrollChange != null)
            onScrollChange.onChange(l, t, oldl, oldt);
//        LogUtil.e("Obs", "l=" + l + ",t=" + t + "oldl=" + oldl + ",oldt=" + oldt);
    }

    private OnScrollChange onScrollChange;

    public void setOnScrollChange(OnScrollChange onScrollChange) {
        this.onScrollChange = onScrollChange;
    }

    public interface OnScrollChange {
        void onChange(int l, int t, int oldl, int oldt);
    }
}
