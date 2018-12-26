package com.qxb.jianhang.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qxb.jianhang.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author liuguangdan
 * @version create at 2018/12/11/011 10:08
 * @Email lgd@jusfoun.com
 * @Description ${}
 */
public class StatDateView extends LinearLayout {
    @BindView(R.id.tvNum)
    TextView tvNum;
    @BindView(R.id.tvTip)
    TextView tvTip;

    public StatDateView(Context context) {
        super(context);
        init();
    }

    public StatDateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public StatDateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_stat_date, this);
        ButterKnife.bind(this);
    }

    public void setTip(String tip) {
        tvTip.setText(tip);
    }

    public void setNum(int num) {
        tvNum.setText(String.valueOf(num));
    }

    public int getNum() {
        return Integer.parseInt(tvNum.getText().toString());
    }
}
