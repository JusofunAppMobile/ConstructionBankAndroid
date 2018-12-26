package com.qxb.jianhang.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jusfoun.baselibrary.base.BaseView;
import com.qxb.jianhang.R;

/**
 * @author zhaoyapeng
 * @version create time:18/9/1411:17
 * @Email zyp@jusfoun.com
 * @Description ${TODO}
 */
public class OverlayView extends BaseView {
    protected TextView textView;

    public OverlayView(Context context) {
        super(context);
    }

    public OverlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OverlayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initViews() {
        LayoutInflater.from(mContext).inflate(R.layout.view_overlayview, this);
        initView(this);
    }

    @Override
    protected void initActions() {

    }

    private void initView(View rootView) {
        textView = (TextView) rootView.findViewById(R.id.textView);
    }

    public void setData(int count ){
        textView.setText(count+"");
    }
}
