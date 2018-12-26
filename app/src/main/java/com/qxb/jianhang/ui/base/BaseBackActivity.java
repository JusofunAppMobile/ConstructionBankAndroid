package com.qxb.jianhang.ui.base;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.jusfoun.baselibrary.base.BaseActivity;
import com.jusfoun.baselibrary.dialog.LoadingDialog;
import com.qxb.jianhang.R;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author zhaoyapeng
 * @version create time:18/8/2416:35
 * @Email zyp@jusfoun.com
 * @Description ${}
 */

public abstract class BaseBackActivity extends BaseActivity {

    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        initDialog();
        super.onCreate(savedInstanceState);
        setStatusBarEnable();
        setStatusBarFontDark(true);
    }



    protected void setTitle(String title) {
        if (this.tvTitle != null) {
            this.tvTitle.setText(title);
        }

    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
//    }

    private void initDialog() {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(this, com.jusfoun.baselibrary.R.style.my_dialog);
            loadingDialog.setCancelable(true);
            loadingDialog.setCanceledOnTouchOutside(false);
        }
    }

    protected void showLoadDialog() {
        if (mContext == null) {
            Log.e("TAG", "该Activity已经销毁，但仍欲显示dialog");
            return;
        }
        if (loadingDialog != null && !loadingDialog.isShowing()) {
            loadingDialog.show();
        }
    }

    protected void hideLoadDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.cancel();
        }
    }

    public boolean isSetStatusBar() {
        return true;
    }

    /**
     * 沉侵式状态栏设置
     */
    public void setStatusBarEnable(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        if (isSetStatusBar()) {
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintColor(color);

        }
        if (getVertualKeyboardBgColor() != 0) {
            tintManager.setNavigationBarTintEnabled(true);
            //  虚拟键盘的颜色
            tintManager.setNavigationBarTintColor(getVertualKeyboardBgColor());
        }
    }

    public void setStatusBarEnable() {
        setStatusBarEnable(ContextCompat.getColor(this, R.color.white));
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);

    }

    /**
     * 获取虚拟键盘背景颜色，如果为0，不设置
     *
     * @return
     */
    protected int getVertualKeyboardBgColor() {
        return Color.TRANSPARENT;
    }

    /**
     * 从启动页、广告页进入主页设置成无动画的效果
     */
    protected void setIntentNoAnim(Intent intent) {
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
    }

    /**
     * 设置Android状态栏的字体颜色，状态栏为亮色的时候字体和图标是黑色，状态栏为暗色的时候字体和图标为白色
     *
     * @param dark 状态栏字体和图标是否为深色
     */
    protected void setStatusBarFontDark(boolean dark) {
        // 小米MIUI
        try {
            Window window = getWindow();
            Class clazz = getWindow().getClass();
            Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            int darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            if (dark) {    //状态栏亮色且黑色字体
                extraFlagField.invoke(window, darkModeFlag, darkModeFlag);
            } else {       //清除黑色字体
                extraFlagField.invoke(window, 0, darkModeFlag);
            }
        } catch (Exception e) {
//            e.printStackTrace();
        }

        // 魅族FlymeUI
        try {
            Window window = getWindow();
            WindowManager.LayoutParams lp = window.getAttributes();
            Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
            Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
            darkFlag.setAccessible(true);
            meizuFlags.setAccessible(true);
            int bit = darkFlag.getInt(null);
            int value = meizuFlags.getInt(lp);
            if (dark) {
                value |= bit;
            } else {
                value &= ~bit;
            }
            meizuFlags.setInt(lp, value);
            window.setAttributes(lp);
        } catch (Exception e) {
//            e.printStackTrace();
        }

//         android6.0+系统
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (dark)
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            else {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            }
        }
    }

    private long lastBackPressTime;
    private int pressTime;



    /**
     * 连续点击两次返回按钮是否退出应用
     *
     * @param exit 是否退出应用
     */
    protected void setDoubleClickExitApp(boolean exit) {
        doubleClickExit = exit;
    }

    private boolean isGoExitApp = false;

    protected void hideBackImage() {
        if (ivBack != null)
            ivBack.setVisibility(View.INVISIBLE);
    }

    private boolean doubleClickExit;

    @Override
    public void onBackPressed() {
        if (doubleClickExit) {
            long time = System.currentTimeMillis();
            if (time - lastBackPressTime < 3 * 1000 && pressTime == 1) {
                isGoExitApp = true;
                finish();
            } else {
                pressTime = 1;
                lastBackPressTime = time;
                showToast("再按一次退出程序");
            }
        } else
            super.onBackPressed();
    }

    public void finishDelay() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 400);
    }

    public void startActivity(Class<?> clazz) {
        startActivity(new Intent(this, clazz));
    }
}
