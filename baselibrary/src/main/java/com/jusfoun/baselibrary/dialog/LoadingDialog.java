package com.jusfoun.baselibrary.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;

import com.jusfoun.baselibrary.R;

/**
 * @author zhaoyapeng
 * @date 2018/1/17
 * @describe 网络加载loading
 */

public class LoadingDialog extends Dialog {

    private AsyncTask<?, ?, ?> asyncTask;

    private MyProgressBar myProgressBar;

    private OnKeyCancelListener keyCancelListener;

    private int event = -1;

    private TextView loading_text;


    private Context mContext;
    Animation animation;

    public LoadingDialog(Context context) {
        super(context);
        mContext = context;
        initViews();
    }

    public LoadingDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        mContext = context;
        initViews();
    }

    public LoadingDialog(Context context, int theme) {
        super(context, theme);
        mContext = context;
        initViews();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("tag", "LoadingDialog");
    }

    private void initViews() {
        setContentView(R.layout.load_dialog);
        myProgressBar = (MyProgressBar) findViewById(R.id.progress_bar);
        loading_text = (TextView) findViewById(R.id.loading_text);
    }

    public void setText(String text) {
        Log.e("tag", "loading_text=" + loading_text);
        loading_text.setText(text);
    }

    public void setText(int textId) {
        loading_text.setText(textId);
    }

    public void setEvent(int event) {
        this.event = event;
    }

    @Override
    public void show() {
        super.show();
        myProgressBar.show();
    }

    public AsyncTask<?, ?, ?> getAsyncTask() {
        return asyncTask;
    }

    public void setAsyncTask(AsyncTask<?, ?, ?> asyncTask) {
        this.asyncTask = asyncTask;
    }

    @Override
    public void cancel() {
        super.cancel();
        if (asyncTask != null) {
            asyncTask.cancel(true);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
            if (keyCancelListener != null) {
                cancel();
                keyCancelListener.cancel(LoadingDialog.this.event);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void dismiss() {
        if (myProgressBar != null) {
            myProgressBar.stop();
        }

        super.dismiss();
    }

    public void setOnKeyCancelListener(OnKeyCancelListener keyCancelListener) {
        this.keyCancelListener = keyCancelListener;
    }

    public interface OnKeyCancelListener {
        public void cancel(int event);
    }


    public void closeHardwareAcceleration() {
        if (myProgressBar != null) {
            myProgressBar.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
    }
}
