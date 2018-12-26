package com.qxb.jianhang.ui.dialog;

import android.app.Activity;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.qxb.jianhang.R;
import com.qxb.jianhang.ui.view.wheelview.adapter.ArrayWheelAdapter;
import com.qxb.jianhang.ui.view.wheelview.widget.WheelView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author liuguangdan
 * @version create at 2018/12/11/011 10:48
 * @Email lgd@jusfoun.com
 * @Description ${}
 */
public class DateSelectDialog extends BaseDialog implements WheelView.OnWheelItemSelectedListener {

    @BindView(R.id.wheelView)
    WheelView wheelView;
    @BindView(R.id.wheelView2)
    WheelView wheelView2;
    @BindView(R.id.wheelView3)
    WheelView wheelView3;

    private OnSelectListener listener;
    private int year, month, day;

    public DateSelectDialog(Activity activity, final String date, OnSelectListener listener) {
        super(activity);
        this.listener = listener;
        setContentView(R.layout.dialog_select_list);
        ButterKnife.bind(this);
        setWindowStyle(10, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.dialogWindowAnim);

        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);

        final List<String> yearList = new ArrayList<>();
        yearList.addAll(getYears());
        final List<String> monthList = new ArrayList<>();
        monthList.addAll(getMonths());
        final List<String> dayList = new ArrayList<>();
        dayList.addAll(getDays());
        initWheel(wheelView, yearList);
        initWheel(wheelView2, monthList);
        initWheel(wheelView3, dayList);


        if (!TextUtils.isEmpty(date)) {
            year = Integer.parseInt(date.substring(0, 4));
            month = Integer.parseInt(date.substring(5, 7));
            day = Integer.parseInt(date.substring(8, 10));
        }

        wheelView.setSelection(yearList.indexOf(year + "年"));
        wheelView2.setSelection(monthList.indexOf(month + "月"));
        wheelView3.setSelection(dayList.indexOf(day + "日"));

        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                wheelView.setOnWheelItemSelectedListener(DateSelectDialog.this);
                wheelView2.setOnWheelItemSelectedListener(DateSelectDialog.this);
            }
        }, 1000);
    }

    private List<String> getYears() {
        List<String> list = new ArrayList<>();
        for (int i = 1990; i <= year; i++)
            list.add(i + "年");
        return list;
    }

    private List<String> getMonths() {
        List<String> list = new ArrayList<>();
        for (int i = 1; i <= 12; i++)
            list.add(i + "月");
        return list;
    }

    private List<String> getDays() {
        int max = getMonthMaxDay(year, month - 1);
        List<String> list = new ArrayList<>();
        for (int i = 1; i <= max; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month - 1);
            calendar.set(Calendar.DAY_OF_MONTH, i);
            list.add((i) + "日");
        }
        return list;
    }

    private int getMonthMaxDay(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    @Override
    public void onItemSelected(int position, Object o) {
        year = Integer.parseInt(wheelView.getSelectionItem().toString().replace("年", ""));
        month = Integer.parseInt(wheelView2.getSelectionItem().toString().replace("月", ""));
        wheelView3.setWheelData(getDays());
    }


    public interface OnSelectListener {
        void select(String date);
    }

    private void initWheel(WheelView wheelView, List<String> list) {
        wheelView.setVisibility(View.VISIBLE);
        WheelView.WheelViewStyle style = new WheelView.WheelViewStyle();
        style.selectedTextSize = 16;
        style.textSize = 16;
        style.selectedTextColor = Color.parseColor("#333333");
        style.textColor = Color.parseColor("#666666");
        style.holoBorderColor = Color.parseColor("#DCDCDA");
        style.holoBorderWidth = 1;

        wheelView.setWheelAdapter(new ArrayWheelAdapter(getContext())); // 文本数据源
        wheelView.setSkin(WheelView.Skin.Holo); // common皮肤
        wheelView.setWheelData(list);  // 数据集合
        wheelView.setStyle(style);
//        wheelView.setExtraText("周一", Color.RED, (int) getContext().getResources().getDimension(R.dimen.wheel_label_size), 160);
        wheelView.setWheelSize(5);
        wheelView.setLoop(true);
        wheelView.setWheelClickable(false);
    }

    @OnClick({R.id.tvCancel, R.id.tvSure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvCancel:
                dismiss();
                break;
            case R.id.tvSure:
                int day = Integer.parseInt(wheelView3.getSelectionItem().toString().replace("日", ""));
                listener.select(year + "-" + (month < 10 ? "0" + month : month) + "-" + (day < 10 ? "0" + day : day));
                dismiss();
                break;
        }
    }
}
