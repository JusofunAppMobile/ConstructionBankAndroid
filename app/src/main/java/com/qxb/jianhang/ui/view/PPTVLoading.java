/*
 * Copyright 2015 guohuanwen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.qxb.jianhang.ui.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by Administrator on 2015/11/9.
 */
public class PPTVLoading extends View {
    private Paint paint1;
    private Paint paint2;
    //default color
    private int color1 = Color.parseColor("#ffff6207");
    private int color2 = Color.parseColor("#ffffac32");

    private boolean init = false;
    private ValueAnimator valueAnimator;
    private float numb = 0;

    private boolean stop = false;

    private int R = 0;


    private boolean isRefresh = false;

    private float offset =0;

    public PPTVLoading(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint1 = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint1.setColor(color1);
        paint2.setColor(color2);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!init) {
            init = true;
            R = getWidth() / 8;
//            start();
        }


        if (isRefresh) {
            numb = (float) valueAnimator.getAnimatedValue();
            if (numb < 0) {
                canvas.drawCircle((getWidth() - 2 * R) * (1 - Math.abs(numb)) + R, getHeight() / 2, R - 5, paint2);
                canvas.drawCircle((getWidth() - 2 * R) * Math.abs(numb) + R, getHeight() / 2, R - 4 * (float) Math.abs(Math.abs(numb) - 0.5), paint1);

            } else {
                canvas.drawCircle((getWidth() - 2 * R) * (1 - Math.abs(numb - 1)) + R, getHeight() / 2, R - 5, paint1);
                canvas.drawCircle((getWidth() - 2 * R) * Math.abs(numb - 1) + R, getHeight() / 2, R - 4 * (float) Math.abs(Math.abs(numb) - 0.5), paint2);
            }
            if (valueAnimator.isRunning()) {
                invalidate();
            }
        }else{
            //Log.e("tag", "下拉-----offset=" + offset);
            canvas.drawCircle((getWidth() - 2 * R) * (1 - Math.abs(0.5f)) + R, getHeight() / 2, R - 5, paint2);
            canvas.drawCircle((getWidth() - 2 * R) * Math.abs(0.5f) + R, getHeight() / 2, (R - 5) * offset, paint1);
        }
        //Log.e("tag", "onDraw=" + isRefresh);

    }

    public void start() {
        valueAnimator = getValueAnimator();
        if (stop == false) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    start();
                    invalidate();
                }
            }, valueAnimator.getDuration());
        }
    }

    public void startAnim(){
        this.stop = false;
        isRefresh = true;
        start();
    }

    public void stopAnim() {
        this.stop = true;
    }

    private ValueAnimator getValueAnimator() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(-1f, 1f);
        valueAnimator.setDuration(1000);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.start();
        return valueAnimator;
    }

    public void setOffset(float offset,boolean animStart){
        //Log.e("tag", "下拉-----setOffset=" + offset);
        this.offset=offset;
        this.stop = animStart;
        if(offset<1){
            isRefresh = false;
        }
        invalidate();
    }

    public void reSet(){
        isRefresh = false;
        offset=0;
    }

}
