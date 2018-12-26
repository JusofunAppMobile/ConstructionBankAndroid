package com.qxb.jianhang.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.qxb.jianhang.R;

/**
 * @author liuguangdan
 * @version create at 2018/9/5/005 16:23
 * @Email lgd@jusfoun.com
 * @Description ${}
 */
public class BaseDialog extends Dialog  {
    protected Activity activity;
    public static final int MATCH_PARENT = 10;

    public BaseDialog(Activity activity) {
        super(activity, R.style.MCTheme_Widget_Dialog);
        this.setCanceledOnTouchOutside(true);
        this.setCancelable(true);
        this.activity = activity;
        this.setOwnerActivity(activity);
        this.requestWindowFeature(1);
    }

    public void setWindowStyle(int wid) {
        this.setWindowStyle(wid, -2, 17);
    }

    public void setWindowStyle(int wid, int heightParams, int gravity) {
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        DisplayMetrics dm = new DisplayMetrics();
        this.activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int parentWidth = dm.widthPixels;
        lp.width = parentWidth * wid / 10;
        lp.height = heightParams;
        lp.gravity = gravity;
        this.getWindow().setAttributes(lp);
    }

}
