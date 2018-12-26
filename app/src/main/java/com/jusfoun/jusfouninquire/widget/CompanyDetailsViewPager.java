package com.jusfoun.jusfouninquire.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Author  JUSFOUN
 * CreateDate 2015/11/10.
 * Description 禁止viewpager滑动
 */
public class CompanyDetailsViewPager extends ViewPager {

    private boolean isCanScroll=false;
    public CompanyDetailsViewPager(Context context) {
        super(context);
    }

    public CompanyDetailsViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return isCanScroll;
    }

    public void setIsCanScroll(boolean isCanScroll) {
        this.isCanScroll = isCanScroll;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return isCanScroll;
    }
}
