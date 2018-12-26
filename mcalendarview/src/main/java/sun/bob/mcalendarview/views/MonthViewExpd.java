package sun.bob.mcalendarview.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import sun.bob.mcalendarview.CellConfig;
import sun.bob.mcalendarview.adapters.CalendarExpAdapter;
import sun.bob.mcalendarview.data.CalendarData;
import sun.bob.mcalendarview.vo.DayData;
import sun.bob.mcalendarview.vo.MonthWeekData;

/**
 * Created by Bigflower on 2015/12/8.
 */
public class MonthViewExpd extends MonthView {
    private MonthWeekData monthWeekData;
    private CalendarExpAdapter adapter;

    public MonthViewExpd(Context context) {
        super(context);
    }

    public MonthViewExpd(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void initMonthAdapter(int pagePosition, int cellView, int markView, Map<String,CalendarData.DayDate> calendarData) {
        getMonthWeekData(pagePosition);
        adapter = new CalendarExpAdapter(getContext(), 1, monthWeekData.getData(), calendarData).setCellViews(cellView, markView);
        this.setAdapter(adapter);
    }

    private void getMonthWeekData(int position) {
        monthWeekData = new MonthWeekData(position);
    }


}
