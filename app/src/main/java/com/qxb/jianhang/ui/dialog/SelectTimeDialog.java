package com.qxb.jianhang.ui.dialog;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.qxb.jianhang.R;
import com.weigan.loopview.LoopView;
import com.weigan.loopview.OnItemSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author liuguangdan
 * @version create at 2018/9/5/005 16:17
 * @Email lgd@jusfoun.com
 * @Description ${}
 */
public class SelectTimeDialog extends BaseDialog {

    @BindView(R.id.loop1)
    LoopView loop1; // 年
    @BindView(R.id.loop2)
    LoopView loop2; // 月
    @BindView(R.id.loop3)
    LoopView loop3; // 日
    @BindView(R.id.loop4)
    LoopView loop4; // 时
    @BindView(R.id.loop5)
    LoopView loop5; // 分

    private List<String> list1 = new ArrayList<>();// 年
    private List<String> list2 = new ArrayList<>();// 月
    private List<String> list3 = new ArrayList<>();// 日
    private List<String> list4 = new ArrayList<>();// 时
    private List<String> list5 = new ArrayList<>();// 分

    private OnSelectListener listener;

    private int year, month, day, hour, minute;

    private boolean isSendReport = false;

    public SelectTimeDialog(Activity activity, OnSelectListener listener) {
        super(activity);
        this.listener = listener;
        init();
    }

    public SelectTimeDialog(Activity activity, OnSelectListener listener, boolean isSendReport) {
        super(activity);
        this.listener = listener;
        this.isSendReport = isSendReport;
        init();
    }

    private void init() {
        if (isSendReport) {
            setContentView(R.layout.dialog_select_time_report);
        } else {
            setContentView(R.layout.dialog_select_time);
        }
        ButterKnife.bind(this);

        setWindowStyle(10, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM);
        initList();

        loop1.setItems(list1);
        loop1.setInitPosition(list1.size() - 1);
        loop1.setTag(list1.get(list1.size() - 1));

        loop2.setItems(list2);
        loop2.setInitPosition(month);
        loop2.setTag(month);

        calculateDay();
        loop3.setInitPosition(day - 1);

        loop4.setItems(list4);
        loop4.setInitPosition(hour - 1);

        loop5.setItems(list5);
        loop5.setInitPosition(minute);

        loop1.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                loop1.setTag(list1.get(index));
                calculateDay();
            }
        });

        loop2.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                loop2.setTag(index);
                calculateDay();
            }
        });
    }

    private void initList() {
        year = Calendar.getInstance().get(Calendar.YEAR);
        month = Calendar.getInstance().get(Calendar.MONTH);
        day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

        minute = Calendar.getInstance().get(Calendar.MINUTE);

        for (int i = year - 10; i <= year; i++)
            list1.add(String.valueOf(i));

        for (int i = 1; i <= 12; i++)
            list2.add(getNumVal(i));

        for (int i = 1; i <= 31; i++)
            list3.add(getNumVal(i));

        for (int i = 1; i <= 24; i++) {
            list4.add(String.valueOf(i));
        }


        for (int i = 1; i <= 60; i++) {
            list5.add(String.valueOf(i));
        }


//        list4.add("上午");
//        list4.add("下午");

    }

    private String getNumVal(int num) {
        return num < 10 ? "0" + num : String.valueOf(num);
    }

    /**
     * 计算年月有多少天
     */
    private void calculateDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Integer.parseInt(loop1.getTag().toString()));
        calendar.set(Calendar.MONTH, Integer.parseInt(loop2.getTag().toString()));
        int days = calendar.getActualMaximum(Calendar.DATE);
        List<String> list = new ArrayList<>();
        for (int i = 1; i <= days; i++) {
            list.add(getNumVal(i));
        }
        loop3.setItems(list);
    }

    @OnClick({R.id.tvCancel, R.id.tvSure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvCancel:
                dismiss();
                break;
            case R.id.tvSure:
                if (listener != null)
                    listener.select(getTime());
                dismiss();
                break;
        }
    }

    private String getTime() {
        if (isSendReport) {
            return list1.get(loop1.getSelectedItem()) + "-" + list2.get(loop2.getSelectedItem()) + "-" + list3.get(loop3.getSelectedItem()) ;
        }
        return list1.get(loop1.getSelectedItem()) + "-" + list2.get(loop2.getSelectedItem()) + "-" + list3.get(loop3.getSelectedItem()) + " " + list4.get(loop4.getSelectedItem()) + ":" + list5.get(loop5.getSelectedItem());
    }

    public interface OnSelectListener {
        void select(String time);
    }
}
