package com.jusfoun.jusfouninquire.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

public class GestureView extends RelativeLayout {
	private GestureDetector detector;
	private Context context;

	public GestureView(Context context) {
		super(context);
		initData();
		this.context=context;
	}

	public GestureView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initData();
		this.context=context;
	}

	public GestureView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initData();
		this.context=context;
	}

	private void initData() {
		detector = new GestureDetector(context,new YScrollDetector());
		setFadingEdgeLength(0);
	}

	private class YScrollDetector extends SimpleOnGestureListener {
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			final int FLING_MIN_DISTANCE = 20, FLING_MIN_VELOCITY = 5;
			if (e1.getX() - e2.getX() > FLING_MIN_DISTANCE && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
				// Fling left
			} else if (e2.getX() - e1.getX() > FLING_MIN_DISTANCE && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
				// Fling right
				if (onGestureListener != null) {
					onGestureListener.onGesture();
				}
				return true;
			} else if (e2.getY() - e1.getY() > FLING_MIN_DISTANCE && Math.abs(velocityY) > FLING_MIN_VELOCITY) {
				// Fling down

			} else if (e1.getY() - e2.getY() > FLING_MIN_DISTANCE && Math.abs(velocityY) > FLING_MIN_VELOCITY) {
				// Fling up
			}
			return super.onFling(e1, e2, velocityX, velocityY);
		}
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		return detector.onTouchEvent(event);
	}

	public interface OnGestureListener {
		public void onGesture();
	}

	private OnGestureListener onGestureListener;

	public OnGestureListener getOnGestureListener() {
		return onGestureListener;
	}

	public void setOnGestureListener(OnGestureListener onGestureListener) {
		this.onGestureListener = onGestureListener;
	}

}
