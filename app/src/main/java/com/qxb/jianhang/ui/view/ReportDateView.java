package com.qxb.jianhang.ui.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jusfoun.baselibrary.Util.ToastUtils;
import com.qxb.jianhang.R;
import com.qxb.jianhang.ui.dialog.SelectTimeDialog;

/**
 * @author zhaoyapeng
 * @version create time:2018/12/1216:11
 * @Email zyp@jusfoun.com
 * @Description ${TODO}
 */
public class ReportDateView extends Dialog {
    protected TextView textStart;
    protected TextView textEnd;
    protected EditText editEmail;
    protected TextView tvCancel;
    protected TextView tvAfirm;

    protected RelativeLayout startLayout, endLayout;


    private Context mContext;

    public ReportDateView(@NonNull Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public ReportDateView(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
        initView();
    }

    protected ReportDateView(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        mContext = context;
        initView();
    }

    private void initView() {
        setContentView(R.layout.view_report_date);
        textStart = (TextView) findViewById(R.id.text_start);
        textEnd = (TextView) findViewById(R.id.text_end);
        editEmail = (EditText) findViewById(R.id.edit_email);
        tvCancel = (TextView) findViewById(R.id.tvCancel);
        tvAfirm = (TextView) findViewById(R.id.tvAfirm);


        startLayout = (RelativeLayout) findViewById(R.id.layout_start);
        endLayout = (RelativeLayout) findViewById(R.id.layout_end);

        tvAfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.show("发送成功");
                if (callBack != null) {
                    if (textStart.getTag() == null) {
                        ToastUtils.show("请选择开始时间");
                        return;
                    }

                    if (textEnd.getTag() == null) {
                        ToastUtils.show("请选择结束时间");
                        return;
                    }

                    if (TextUtils.isEmpty(editEmail.getText().toString())) {
                        ToastUtils.show("请输入邮箱");
                        return;
                    }

                    callBack.sendReport(textStart.getText().toString(), textEnd.getText().toString(), editEmail.getText().toString());
                }
                dismiss();
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        startLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SelectTimeDialog((Activity) mContext, new SelectTimeDialog.OnSelectListener() {
                    @Override
                    public void select(String time) {
                        textStart.setTag(true);
                        textStart.setText(time);
                    }
                }, true).show();
            }
        });

        endLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SelectTimeDialog((Activity) mContext, new SelectTimeDialog.OnSelectListener() {
                    @Override
                    public void select(String time) {
                        textEnd.setTag(true);
                        textEnd.setText(time);
                    }
                }, true).show();
            }
        });


    }

    public interface CallBack {
        void sendReport(String startTime, String endTime, String email);
    }

    public CallBack callBack;

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }
}
