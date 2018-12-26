package com.qxb.jianhang.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jusfoun.baselibrary.Util.TouchUtil;
import com.jusfoun.baselibrary.base.BaseView;
import com.qxb.jianhang.R;

import java.util.Calendar;
import java.util.Map;

import sun.bob.mcalendarview.data.CalendarData;
import sun.bob.mcalendarview.listeners.OnDateClickListener;
import sun.bob.mcalendarview.listeners.OnExpDateClickListener;
import sun.bob.mcalendarview.listeners.OnMonthScrollListener;
import sun.bob.mcalendarview.views.ExpCalendarView;
import sun.bob.mcalendarview.vo.DateData;

/**
 * @author zhaoyapeng
 * @version create time:2018/12/1013:55
 * @Email zyp@jusfoun.com
 * @Description ${日历 view}
 */
public class MyCalendarView extends BaseView {


    private TextView YearMonthTv;
    private ExpCalendarView expCalendarView;
    private DateData selectedDate;

    private ImageView leftMonthImg, rightMonthImg;
    private LinearLayout leftYearLayout, rightYearLayout;

    private Map<String, CalendarData.DayDate> calendarData;


    public MyCalendarView(Context context) {
        super(context);
    }

    public MyCalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyCalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initViews() {
        LayoutInflater.from(mContext).inflate(R.layout.view_my_calendar, this, true);
        expCalendarView = ((ExpCalendarView) findViewById(R.id.calendar_exp));
        YearMonthTv = (TextView) findViewById(R.id.main_YYMM_Tv);

        leftMonthImg = (ImageView) findViewById(R.id.img_left_money);
        rightMonthImg = (ImageView) findViewById(R.id.img_right_money);
        leftYearLayout = (LinearLayout) findViewById(R.id.layout_left_year);
        rightYearLayout = (LinearLayout) findViewById(R.id.layout_right_year);


    }

    @Override
    protected void initActions() {
//      Set up listeners.
        expCalendarView.setOnDateClickListener(new OnExpDateClickListener()).setOnMonthScrollListener(new OnMonthScrollListener() {
            @Override
            public void onMonthChange(int year, int month) {
                YearMonthTv.setText(String.format("%d月 %d", month, year));
            }

            @Override
            public void onMonthScroll(float positionOffset) {
//                Log.i("listener", "onMonthScroll:" + positionOffset);
            }
        });

        expCalendarView.setOnDateClickListener(new OnDateClickListener() {
            @Override
            public void onDateClick(View view, DateData date) {

                Log.e("tag", "expCalendarViewexpCalendarView=" + date.getYear() + " " + date.getMonth() + " " + date.getDay());
                expCalendarView.getMarkedDates().removeAdd();
                expCalendarView.markDate(date);
                selectedDate = date;

                if (callBack != null) {
                    callBack.selectDate(date.getYear() + "-" + date.getMonth() + "-" + date.getDay());
                }
            }
        });

        Calendar calendar = Calendar.getInstance();
        selectedDate = new DateData(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
        expCalendarView.markDate(selectedDate);
        YearMonthTv.setText(String.format("%d年%d月", selectedDate.getYear(), selectedDate.getMonth()));

        leftMonthImg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                expCalendarView.setCurrentItem(expCalendarView.getCurrentItem() - 1, false);

            }
        });

        rightMonthImg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                expCalendarView.setCurrentItem(expCalendarView.getCurrentItem() + 1, false);
            }
        });

        leftYearLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                expCalendarView.setCurrentItem(expCalendarView.getCurrentItem() - 12, false);
//                expCalendarView.travelTo(new DateData(selectedDate.getYear()-1, selectedDate.getMonth(), selectedDate.getDay()));
            }
        });

        rightYearLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                expCalendarView.setCurrentItem(expCalendarView.getCurrentItem() + 12, false);
//                expCalendarView.travelTo(new DateData(selectedDate.getYear()+1, selectedDate.getMonth(), selectedDate.getDay()));
            }
        });

        TouchUtil.createTouchDelegate(leftMonthImg, 40);
        TouchUtil.createTouchDelegate(rightMonthImg, 40);
        TouchUtil.createTouchDelegate(leftYearLayout, 40);
        TouchUtil.createTouchDelegate(rightYearLayout, 40);
    }


    public void setData(Map<String, CalendarData.DayDate> data) {
        calendarData = data;
        expCalendarView.setData(data);
        if (callBack != null) {
            callBack.selectDate(selectedDate.getYear() + "-" + selectedDate.getMonth() + "-" + selectedDate.getDay());
        }
    }


    public interface CallBack {
        void selectDate(String date);
    }

    public CallBack callBack;

    public void setCallback(CallBack callBack) {
        this.callBack = callBack;
    }
}
