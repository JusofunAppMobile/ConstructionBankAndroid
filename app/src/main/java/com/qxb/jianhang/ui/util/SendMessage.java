package com.qxb.jianhang.ui.util;

import android.app.Activity;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by xiaoma on 2018/5/7/007.
 */

public class SendMessage {
    private Timer timer = new Timer();
    private TimerTask timerTask;
    private int maxTime;
    private TextView clickView;
    private TextView inputView;
    private Activity activity ;
    private int defaultMaxTime = 60;
    private String tipText;

    private SendMessage(Activity activity) {
        this.activity = activity;
    }

    public static SendMessage newInstant(Activity activity) {
        return new SendMessage(activity);
    }

    public SendMessage setClickView(TextView view) {
        this.clickView = view;
        return this;
    }

    public SendMessage setInputView(TextView view) {
        this.inputView = view;
        return this;
    }

    public SendMessage setTipText(String text) {
        this.tipText = text;
        return this;
    }

    public SendMessage setTime(int time) {
        this.defaultMaxTime = time;
        return this;
    }

    public void start() {
        this.maxTime = this.defaultMaxTime;
        this.timerTask = new TimerTask() {
            public void run() {
                SendMessage.this.setCodeTime(SendMessage.this.maxTime--);
            }
        };
        this.timer.schedule(this.timerTask, 0L, 1000L);
        this.clickView.setClickable(false);
    }

    public void reset() {
        this.setCodeTime(0);
    }

    private void setCodeTime(final int time) {
        this.activity.runOnUiThread(new Runnable() {
            public void run() {
                if(time <= 0) {
                    SendMessage.this.clickView.setText(SendMessage.this.tipText);
                    SendMessage.this.clickView.setClickable(true);
                    SendMessage.this.maxTime = SendMessage.this.defaultMaxTime;
                    SendMessage.this.timerTask.cancel();
                } else {
                    SendMessage.this.clickView.setText("重新获取(" + time + ")");
                    SendMessage.this.clickView.setClickable(false);
                }

            }
        });
    }
}