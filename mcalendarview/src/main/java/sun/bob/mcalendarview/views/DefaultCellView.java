package sun.bob.mcalendarview.views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import sun.bob.mcalendarview.CellConfig;
import sun.bob.mcalendarview.MarkStyleExp;
import sun.bob.mcalendarview.R;
import sun.bob.mcalendarview.data.CalendarData;
import sun.bob.mcalendarview.vo.DayData;

/**
 * Created by bob.sun on 15/8/28.
 */
public class DefaultCellView extends BaseCellView {
    public TextView textView;
    private AbsListView.LayoutParams matchParentParams;
    private Context mContext;
    private CalendarBottomIconView calendarBottomIconView;
    public DefaultCellView(Context context) {
        super(context);
        mContext =context;
        initViews();
        initLayout();
    }

    public DefaultCellView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext =context;
        initViews();
        initLayout();
    }


    private void initViews(){
        LayoutInflater.from(mContext).inflate(R.layout.view_item,this,true);
        textView = findViewById(R.id.text_sell);
        calendarBottomIconView = findViewById(R.id.view_calendar);
        calendarBottomIconView.setVisibility(GONE);
    }

    private void initLayout(){
        matchParentParams = new AbsListView.LayoutParams((int) CellConfig.cellWidth, (int) CellConfig.cellHeight);
        this.setLayoutParams(matchParentParams);
        this.setOrientation(VERTICAL);
//        textView = new TextView(getContext());
        textView.setGravity(Gravity.CENTER);
//        textView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, (float) 1.0));
//        this.addView(textView);
    }

    @Override
    public void setDisplayText(DayData day) {
        textView.setText(day.getText());
    }

    @Override
    protected void onMeasure(int measureWidthSpec,int measureHeightSpec){
        super.onMeasure(measureWidthSpec, measureHeightSpec);
    }

    public boolean setDateChoose() {
        setBackgroundDrawable(MarkStyleExp.choose);
        textView.setTextColor(Color.WHITE);
        return true ;
    }

    public void setDateToday(){
        setBackgroundDrawable(MarkStyleExp.today);
        textView.setTextColor(Color.rgb(105, 75, 125));
    }

    public void setDateNormal() {
        textView.setTextColor(Color.BLACK);
        setBackgroundDrawable(null);
    }

    public void setTextColor(String text, int color) {
        textView.setText(text);
        if (color != 0) {
            textView.setTextColor(color);
        }
    }

    public void setDayList(DayData day,Map<String,CalendarData.DayDate> dayDates) {

        if(dayDates!=null){
            String mDay = day.getDate().getYear()+"-"+day.getDate().getMonth()+"-"+day.getDate().getDay();
            if (dayDates.containsKey(mDay)){
                calendarBottomIconView.setData(dayDates.get(mDay));
                calendarBottomIconView.setVisibility(VISIBLE);
            }
        }

    }
}
