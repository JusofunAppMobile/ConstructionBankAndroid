package com.qxb.jianhang.ui.util;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Handler;
import android.os.Message;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

/**
 * @author zhaoyapeng
 * @version create time:17/10/1909:55
 * @Email zyp@jusfoun.com
 * @Description ${详情页面刷新动画}
 */
public class RefreshAnimUtil {

    private ObjectAnimator accelerateAnim;
    private long statrTime;
    private long endTime;
    private Handler handler;
    private ImageView imageView;
    public RefreshAnimUtil(ImageView imageView){
        this.imageView=imageView;
        accelerateAnim  = ObjectAnimator.ofFloat(imageView,"rotation",0,360 * 1000000);
        accelerateAnim.setDuration(700 * 1000000);
        accelerateAnim.setRepeatCount(ValueAnimator.INFINITE);
        accelerateAnim.setInterpolator(new LinearInterpolator());
        handler =new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                stopAnim();
            }
        };

    }

    public  void startAnim(){
        if(!accelerateAnim.isRunning()) {
            statrTime = System.currentTimeMillis();
            accelerateAnim.start();
        }
    }

    public void stopAnim(){
        endTime = System.currentTimeMillis();
        if(endTime-statrTime>=1000*5){
            statrTime = 0;
            endTime = 0;
            imageView.clearAnimation();
            accelerateAnim.cancel();
        }else{
            handler.sendEmptyMessageDelayed(0,5000-(endTime-statrTime));
        }
    }
}
