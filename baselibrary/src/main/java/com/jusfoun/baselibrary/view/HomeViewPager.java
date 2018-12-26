package com.jusfoun.baselibrary.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @author lee
 * @version create time:2015年7月15日_上午11:24:11
 * @Description ViewPager 管理fragment
 */

public class HomeViewPager extends ViewPager{
	/**
	 * 是否不让viewpager滑动
	 */
	private boolean notTouchScoll = false;
	
	public HomeViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public HomeViewPager(Context context) {
		super(context);
	}

	public void setNotTouchScoll(boolean scoll) {
		this.notTouchScoll = scoll;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (notTouchScoll) {
			return false;
		} else {
			return super.onTouchEvent(ev);
		}

	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (notTouchScoll) {
			return false;
		} else {
			return super.onInterceptTouchEvent(ev);
		}

	}
	
	
}
