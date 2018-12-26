package com.qxb.jianhang.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.jusfoun.baselibrary.Util.PhoneUtil;
import com.qxb.jianhang.R;


/**
 * Created by lsq on 2016/8/10.
 */
public class PhoneCallDialog extends Dialog {
    private Context mContext;
    private Button agree, hope;
    private TextView message;
    private String phone;

    public PhoneCallDialog(Context context, String phone) {
        super(context, R.style.tool_dialog);
        mContext = context;
        this.phone = phone;
        initViews();
        initAation();
    }

    private void initViews() {
        setContentView(R.layout.phone_call_dialog);
        hope = (Button) findViewById(R.id.positiveButton);
        agree = (Button) findViewById(R.id.negativeButton);
        message = (TextView) findViewById(R.id.message);

        Window window = this.getWindow();
        android.view.WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = (int) (PhoneUtil.getDisplayWidth(mContext) * 0.9);
        lp.height = (int) (PhoneUtil.getDisplayHeight(mContext) * 0.8);
        window.setGravity(Gravity.CENTER);
    }

    private void initAation() {
        message.setText(phone);
        hope.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+phone));
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
                dismiss();
            }
        });
        agree.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
