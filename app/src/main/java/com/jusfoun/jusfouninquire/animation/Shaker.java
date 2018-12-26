package com.jusfoun.jusfouninquire.animation;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;


import com.qxb.jianhang.net.event.AnimationEndEvent;

import org.greenrobot.eventbus.EventBus;


/**
 * Created by Albert on 2016-4-19.
 * Mail : lbh@jusfoun.com
 * TODO :
 * Description:企业详情12宫格“最新、最热”抖动动画
 */
public class Shaker {
    private View mView;
    private int mIndex;
    private ObjectAnimator shaker;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            EventBus.getDefault().post(new AnimationEndEvent(mIndex));
        }
    };

    /**
     *
     * @param view 动画容器view
     * @param position  所处宫格索引
     */
    public Shaker(View view, int position) {
        mView = view;
        mIndex = position;
    }

    /**
     * 左右移动动画
     */
    public void shake(){
//        ObjectAnimator shaker = ObjectAnimator.ofFloat(mView, "translationX", -10, 0);
//        shaker.setDuration(50);
//        shaker.setRepeatCount(Animation.INFINITE);
//        shaker.setRepeatMode(Animation.REVERSE);
//        shaker.start();
    }

    /**
     * 抖动，暂停，抖动
     */
    public void shakeAndPause(){

        shaker = ObjectAnimator.ofFloat(mView, "translationX", -10, 0);
        shaker.setDuration(50);
        shaker.setRepeatCount(5);
        shaker.setInterpolator(new DecelerateInterpolator());

        Animator.AnimatorListener shakeListener = new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //动画结束后，延迟3s通知到adapter
                handler.sendEmptyMessageDelayed(0,10000);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        };
        shaker.addListener(shakeListener);

        shaker.start();
    }


    /**
     * 左右旋转动画
     */
    public void shakeLeftRight(){
        ObjectAnimator anim = ObjectAnimator.ofFloat(mView, "rotation", -2f, 2f);
        anim.setDuration(50);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        anim.start();
    }

}
