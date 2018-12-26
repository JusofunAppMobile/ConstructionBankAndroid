package com.jusfoun.baselibrary.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.jusfoun.baselibrary.R;
import com.jusfoun.baselibrary.Util.AppUtil;
import com.jusfoun.baselibrary.Util.PhoneUtil;



public class PermissionsDialog extends Dialog {
    private Button left, right;
    private TextView title, message;
    public static  int ACTION_APPLICATION_DETAILS_SETTINGS_CODE = 0x100;

    public PermissionsDialog(Context context) {
        super(context);
        init(context);
    }

    public PermissionsDialog(Context context, int theme) {
        super(context, theme);
        init(context);
    }

    protected PermissionsDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context);
    }

    private void init(final Context context) {
        setContentView(R.layout.dialog_permissions);
        Window window = this.getWindow();
        android.view.WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = PhoneUtil.getDisplayWidth(context) * 4 / 5;
        window.setGravity(Gravity.CENTER);
        window.setAttributes(lp);
        setCanceledOnTouchOutside(true);
        left = (Button) findViewById(R.id.left_btn);
        right = (Button) findViewById(R.id.right_btn);
        title = (TextView) findViewById(R.id.title);
        message = (TextView) findViewById(R.id.message);
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(callBack!=null){
                    callBack.onLeftClick();
                }
                if(isCanFinish){
                    ((Activity)context).finish();
                }
            }
        });
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", AppUtil.getPackageName(context), null);
                intent.setData(uri);
                ((Activity)context).startActivityForResult(intent,ACTION_APPLICATION_DETAILS_SETTINGS_CODE);
                if(callBack!=null){
                    callBack.onRightClick();
                }
            }
        });

        setCancelable(false);
    }



    public  interface  CallBack{
        void onLeftClick();
        void onRightClick();
    }

    private CallBack callBack;

    public void setCallBack(CallBack callBack){
        this.callBack=callBack;
    }

    public void setContent(String content){
        title.setText("权限申请");
        message.setText(content);
    }

    private boolean isCanFinish = false;
    public void setCanFinish(){
        isCanFinish = true;
    }

}
