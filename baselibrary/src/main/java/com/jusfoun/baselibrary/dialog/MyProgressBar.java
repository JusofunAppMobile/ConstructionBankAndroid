package com.jusfoun.baselibrary.dialog;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.jusfoun.baselibrary.R;

/**
 * @author zhaoyapeng
 * @date 2018/1/17
 * @describe
 */

public class MyProgressBar extends ImageView {
    private RotateAnimation mRotateAnimation;
    private Paint paint = null;
    private Drawable defaultDrawable = null;
    private int width = 0;
    private int height = 0;
    private boolean show;

    public MyProgressBar(Context context) {
        super(context);
        init();
    }

    public MyProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyProgressBar(Context context, boolean isCreate) {
        super(context);
        init();
        width = (int) getResources().getDimension(R.dimen.default_progress_bar_width);
        height = (int) getResources().getDimension(R.dimen.default_progress_bar_height);
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        this.setFocusable(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.TRANSPARENT);
        defaultDrawable = this.getBackground();
        if (defaultDrawable == null)
            defaultDrawable = getResources().getDrawable(R.mipmap.loading1);
        if (width == 0)
            width = this.getWidth();
        if (height == 0)
            height = this.getHeight();
        defaultDrawable.setBounds(0, 0, width, height);
        defaultDrawable.draw(canvas);
    }

    public void show() {

        this.setVisibility(View.VISIBLE);
        mRotateAnimation = new RotateAnimation(0, 360 * 1000000, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        mRotateAnimation.setDuration(1400 * 1000000);
        mRotateAnimation.setInterpolator(new LinearInterpolator());
        mRotateAnimation.setFillAfter(true);
        startAnimation(mRotateAnimation);
        show=true;
    }

    public void stop() {
        show=false;
        clearAnimation();
    }

    public void hide() {
        show=false;
        clearAnimation();
        this.setVisibility(View.GONE);
    }

    public boolean isShow() {
        return show;
    }
}
