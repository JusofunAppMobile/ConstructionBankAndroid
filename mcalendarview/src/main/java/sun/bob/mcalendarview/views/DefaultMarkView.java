package sun.bob.mcalendarview.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import sun.bob.mcalendarview.CellConfig;
import sun.bob.mcalendarview.MarkStyle;
import sun.bob.mcalendarview.R;
import sun.bob.mcalendarview.data.CalendarData;
import sun.bob.mcalendarview.vo.DayData;

/**
 * Created by bob.sun on 15/8/28.
 */
public class DefaultMarkView extends BaseMarkView {
    private TextView textView;
    private AbsListView.LayoutParams matchParentParams;
    private int orientation;

    private View sideBar;
    private TextView markTextView;
    private GradientDrawable circleDrawable;

    private Context mContext;

    private CalendarBottomIconView calendarBottomIconView;
    private List<CalendarData.DayDate> dayDates;

    public DefaultMarkView(Context context) {
        super(context);
        mContext = context;
        initViews();
    }

    public DefaultMarkView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initViews();
    }

    private void initViews() {
        LayoutInflater.from(mContext).inflate(R.layout.view_item, this, true);
        textView = findViewById(R.id.text_sell);
        calendarBottomIconView = findViewById(R.id.view_calendar);
        calendarBottomIconView.setVisibility(GONE);
    }

    private void initLayoutWithStyle(MarkStyle style) {
//        textView = new TextView(getContext());
        textView.setGravity(Gravity.CENTER);
        matchParentParams = new AbsListView.LayoutParams((int) CellConfig.cellWidth, (int) CellConfig.cellHeight);
        switch (style.getStyle()) {
            case MarkStyle.DEFAULT:
                this.setLayoutParams(matchParentParams);
                this.setOrientation(HORIZONTAL);
                textView.setTextColor(Color.WHITE);
                circleDrawable = new GradientDrawable();
                circleDrawable.setShape(GradientDrawable.OVAL);
                int colors[] = { 0xffFA6939 , 0xffFB7B36, 0xffFD9731, 0xffFFB22C};
                circleDrawable.setColors(colors);

//                circleDrawable.getPaint().setColor(style.getColor());
//                this.setPadding(20, 20, 20, 20);
//                textView.setLayoutParams(new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, (float) 1.0));
                textView.setBackgroundDrawable(circleDrawable);
//                this.addView(textView);

                return;
            case MarkStyle.BACKGROUND:
                this.setLayoutParams(matchParentParams);
                this.setOrientation(HORIZONTAL);
                textView.setTextColor(Color.WHITE);
//                circleDrawable = new ShapeDrawable(new OvalShape());
//                circleDrawable.getPaint().setColor(style.getColor());
                this.setPadding(20, 20, 20, 20);
                textView.setLayoutParams(new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, (float) 1.0));
                textView.setBackground(circleDrawable);
                this.addView(textView);
                return;
            case MarkStyle.DOT:
                this.setLayoutParams(matchParentParams);
                this.setOrientation(VERTICAL);
                textView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, (float) 2.0));

                this.addView(new PlaceHolderVertical(getContext()));
                this.addView(textView);
                this.addView(new Dot(getContext(), style.getColor()));
                return;
            case MarkStyle.RIGHTSIDEBAR:
                this.setLayoutParams(matchParentParams);
                this.setOrientation(HORIZONTAL);
                textView.setLayoutParams(new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, (float) 3.0));

                this.addView(new PlaceHolderHorizontal(getContext()));
                this.addView(textView);
                PlaceHolderHorizontal barRight = new PlaceHolderHorizontal(getContext());
                barRight.setBackgroundColor(style.getColor());
                this.addView(barRight);
                return;
            case MarkStyle.LEFTSIDEBAR:
                this.setLayoutParams(matchParentParams);
                this.setOrientation(HORIZONTAL);
                textView.setLayoutParams(new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, (float) 3.0));

                PlaceHolderHorizontal barLeft = new PlaceHolderHorizontal(getContext());
                barLeft.setBackgroundColor(style.getColor());
                this.addView(barLeft);
                this.addView(textView);
                this.addView(new PlaceHolderHorizontal(getContext()));

                return;
            default:
                throw new IllegalArgumentException("Invalid Mark Style Configuration!");
        }
    }

    @Override
    public void setDisplayText(DayData day) {
        initLayoutWithStyle(day.getDate().getMarkStyle());
        textView.setText(day.getText());
    }

    public void setDayList(DayData day,List<CalendarData.DayDate> dayDates) {
        if(dayDates!=null){
            String mDay = day.getDate().getYear()+"-"+day.getDate().getMinute()+"-"+day.getDate().getDay();
            for(int i=0;i<dayDates.size();i++){
                if(mDay.equals(dayDates.get(i).day)){
                    calendarBottomIconView.setVisibility(VISIBLE);
                    break;
                }
            }
        }

    }

    class PlaceHolderHorizontal extends View {

        LayoutParams params;

        public PlaceHolderHorizontal(Context context) {
            super(context);
            params = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, (float) 1.0);
            this.setLayoutParams(params);
        }

        public PlaceHolderHorizontal(Context context, AttributeSet attrs) {
            super(context, attrs);
            params = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, (float) 1.0);
            this.setLayoutParams(params);
        }
    }

    class PlaceHolderVertical extends View {

        LayoutParams params;

        public PlaceHolderVertical(Context context) {
            super(context);
            params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, (float) 1.0);
            this.setLayoutParams(params);
        }

        public PlaceHolderVertical(Context context, AttributeSet attrs) {
            super(context, attrs);
            params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, (float) 1.0);
            this.setLayoutParams(params);
        }
    }

    class Dot extends RelativeLayout {

        public Dot(Context context, int color) {
            super(context);
            this.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, (float) 1.0));
            View dotView = new View(getContext());
            LayoutParams lp = new RelativeLayout.LayoutParams(10, 10);
            lp.addRule(CENTER_IN_PARENT, TRUE);
            dotView.setLayoutParams(lp);
            ShapeDrawable dot = new ShapeDrawable(new OvalShape());

            dot.getPaint().setColor(color);
//            dotView.setBackground(dot);
            this.addView(dotView);
        }
    }

}
