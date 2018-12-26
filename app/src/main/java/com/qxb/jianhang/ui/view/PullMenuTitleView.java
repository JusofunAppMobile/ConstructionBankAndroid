package com.qxb.jianhang.ui.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jusfoun.baselibrary.Util.PhoneUtil;
import com.jusfoun.baselibrary.Util.TouchUtil;
import com.qxb.jianhang.R;


/**
 * Author  JUSFOUN
 * CreateDate 2015/11/25.
 * Description
 */
public class PullMenuTitleView extends LinearLayout {

    private ImageView left;
    private TextView title;
    private View right;
    private Context context;

    public PullMenuTitleView(Context context) {
        super(context);
        initView(context);
    }

    public PullMenuTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public PullMenuTitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.layout_tilte_pull_menu, this, true);
        left = (ImageView) findViewById(R.id.leftTxt);
        right = findViewById(R.id.rightTxt);
        title = (TextView) findViewById(R.id.titleTxt);
    }

    public void setTitle(String string) {
        title.setText(string);
    }

    public void setRightTxt(String string) {
//        right.setText(string);
    }

    public void setRightImg(int resid) {
//        if (resid==-1)
//            right.setBackgroundDrawable(null);
//        right.setBackgroundResource(resid);
    }

    public void setRightHite() {
        right.setVisibility(View.GONE);
    }

    public void setRightVisible() {
        right.setVisibility(VISIBLE);
    }

    public void setTitleRightDrawable(int resid) {
        if (resid == -1)
            title.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        else {
            Drawable drawable = context.getResources().getDrawable(resid);
            title.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
        }
    }

    public void setTitleClickListener(OnClickListener onClickListener) {
        if (onClickListener != null)
            title.setOnClickListener(onClickListener);
    }

    public void setLeftClickListener(OnClickListener onClickListener) {
        TouchUtil.createTouchDelegate(left, PhoneUtil.dip2px(context, 10));
        if (onClickListener != null)
            left.setOnClickListener(onClickListener);
    }

    public void setRightClickListener(OnClickListener onClickListener) {
        if (onClickListener != null)
            right.setOnClickListener(onClickListener);
    }

}
