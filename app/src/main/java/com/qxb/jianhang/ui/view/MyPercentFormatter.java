package com.qxb.jianhang.ui.view;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

/**
 * 说明
 *
 * @时间 2017/10/26
 * @作者 LiuGuangDan
 */

public class MyPercentFormatter extends PercentFormatter {

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        return ((PieEntry) entry).getLabel() + "\n" + (int)entry.getY();
    }
}
