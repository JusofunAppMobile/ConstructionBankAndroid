package com.qxb.jianhang.ui.pop;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jusfoun.baselibrary.Util.ToastUtils;
import com.qxb.jianhang.R;
import com.qxb.jianhang.ui.dialog.DateSelectDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author liuguangdan
 * @version create at 2018/12/11/011 10:04
 * @Email lgd@jusfoun.com
 * @Description ${}
 */
public class StatDatePop extends PopupWindow {

    @BindView(R.id.tvStart)
    TextView tvStart;
    @BindView(R.id.tvEnd)
    TextView tvEnd;
    private Activity activity;

    private OnSelectListener listener;

    public StatDatePop(Activity activity, OnSelectListener listener) {
        this.activity = activity;
        this.listener = listener;
        View view = LayoutInflater.from(activity).inflate(R.layout.pop_stat_date, null);
        setContentView(view);
        ButterKnife.bind(this, view);
        setOutsideTouchable(true);
        setTouchable(true);

        setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.img_pop_date));
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        setWidth(LinearLayout.LayoutParams.MATCH_PARENT);

        initStartEndDate();

    }

    private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private void initStartEndDate() {
        Calendar calendar = Calendar.getInstance();
        String date = sdf.format(calendar.getTime());
        tvStart.setText(date.substring(0, 8) + "01");
        tvEnd.setText(date);
    }

    @OnClick({R.id.vSure})
    public void vSure(View view) {
        if (TextUtils.isEmpty(tvStart.getText().toString())) {
            ToastUtils.show("请选择开始时间");
            return;
        }
        if (TextUtils.isEmpty(tvEnd.getText().toString())) {
            ToastUtils.show("请选择结束时间");
            return;
        }
        int month = Integer.parseInt(tvEnd.getText().toString().substring(5, 7));
        if (listener != null)
            listener.select(tvStart.getText().toString(), tvEnd.getText().toString(), month);

        dismiss();
    }

    @OnClick({R.id.vStart, R.id.vEnd})
    public void onViewClicked(View view) {
        if (view.getTag() != null) {
            DateSelectDialog dialog = (DateSelectDialog) view.getTag();
            dialog.show();
            return;
        }

        TextView textView = null;

        switch (view.getId()) {
            case R.id.vStart:
                textView = tvStart;
                break;
            case R.id.vEnd:
                textView = tvEnd;
                break;
        }

        final TextView finalTextView = textView;
        DateSelectDialog dialog = new DateSelectDialog(activity, textView.getText().toString(), new DateSelectDialog.OnSelectListener() {
            @Override
            public void select(String date) {
                finalTextView.setText(date);
            }
        });
        dialog.show();
    }

    public interface OnSelectListener {
        void select(String start, String end, int month);
    }
}
