package com.qxb.jianhang.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qxb.jianhang.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author liuguangdan
 * @version create at 2018/12/10/010 18:14
 * @Email lgd@jusfoun.com
 * @Description ${}
 */
public class StatPercentView extends LinearLayout {
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvPercent)
    TextView tvPercent;
    @BindView(R.id.ivArrow)
    ImageView ivArrow;

    public StatPercentView(Context context) {
        super(context);
        init();
    }

    public StatPercentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public StatPercentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_stat_percent, this);
        ButterKnife.bind(this);
    }

    public void setType(int type) {
        if (type == 1) {
            tvTitle.setText("跟进客户拓展率");
            tvPercent.setTextColor(Color.parseColor("#FF9D00"));
        } else {
            tvTitle.setText("正式客户转化率");
            tvPercent.setTextColor(Color.parseColor("#1D9FF4"));
        }
    }

    public void setValue(int percent, int state) {
        tvPercent.setText(String.valueOf(percent));
        // 0：下降 1：上升 2：平衡
        if (state == 2) {
            ivArrow.setImageResource(R.drawable.img_bolang);
        }
        else {
            ivArrow.setImageResource(R.drawable.selector_stat_arrow);
            ivArrow.setSelected(state == 1);
        }
    }

    @OnClick(R.id.tvCheck)
    public void onViewClicked() {
    }
}
