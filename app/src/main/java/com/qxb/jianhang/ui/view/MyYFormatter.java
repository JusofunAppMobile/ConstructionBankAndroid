package com.qxb.jianhang.ui.view;

import android.text.TextUtils;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.Map;

public class MyYFormatter implements IAxisValueFormatter {

    Map<Integer, String> map;

    public MyYFormatter(Map<Integer, String> map) {
        this.map = map;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        String res = map.get((int) value);
        if(TextUtils.isEmpty(res))
            return "";
        return res;
    }
}
