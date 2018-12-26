package com.qxb.jianhang.ui.view;

import android.text.TextUtils;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.Map;

public class MyAxisValueFormatter implements IAxisValueFormatter {

    private Map<Integer, String> dateMap;

    public MyAxisValueFormatter(Map<Integer, String> dateMap) {
        this.dateMap = dateMap;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        try {
            String date = dateMap.get((int) value);
            if (TextUtils.isEmpty(date))
                return "";
            return date;
        } catch (Exception e) {
            return "";
        }
    }
}
