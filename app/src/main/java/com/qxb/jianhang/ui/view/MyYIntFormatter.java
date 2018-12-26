package com.qxb.jianhang.ui.view;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

/**
 * @author liuguangdan
 * @version create at 2018/12/21/021 14:12
 * @Email lgd@jusfoun.com
 * @Description ${}
 */

public class MyYIntFormatter implements IAxisValueFormatter {

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return (int) value + "";
    }
}

