package com.jusfoun.baselibrary.view;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.ScrollView;

/**
 * Author  wangchenchen
 * CreateDate 2016/5/17.
 * Email wcc@jusfoun.com
 * Description 解决nestedscrollview和viewpager手势冲突
 */
public class InterceptScrollView extends NestedScrollView {

    private int downY;
    private int mTouchSlop;
    public InterceptScrollView(Context context) {
        super(context);
        mTouchSlop= ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public InterceptScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTouchSlop= ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public InterceptScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop= ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                downY= (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e("mTouchSlop", "" + mTouchSlop);
                if (Math.abs(ev.getRawY() - downY)>mTouchSlop){
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }
}
