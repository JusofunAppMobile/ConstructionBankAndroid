package com.jusfoun.jusfouninquire.activity;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.jusfoun.baselibrary.dialog.LoadingDialog;
import com.jusfoun.baselibrary.event.IEvent;
import com.jusfoun.jusfouninquire.util.VolleyUtil;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


/**
 * @author zhaoyapeng
 * @version create time:15/10/30下午3:30
 * @Email zyp@jusfoun.com
 * @Description ${提取公共方法}
 */
public abstract class BaseInquireActivity extends BaseActivity {
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initDialog();
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);


//        Log.e("tag", "initData1");

//      mSwipeBackLayout.setEnableGesture(false);

        if (isSetStatusBar())
            setStatusBarEnable();
        if (isBarDark())
            setStatusBarFontDark(true);
    }

    @Override
    protected void initData() {
//

    }

    /**
     * 页面跳转
     */
    protected void goActivity(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }

    protected void goActivity(Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * 显示toast
     */
    protected void showToast(String msg) {
        if (msg != null && !msg.trim().equals("")) {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 显示toast
     */
    protected void showToast(int msgid) {
        Toast.makeText(this, msgid, Toast.LENGTH_SHORT).show();
    }

    /**
     * 接收event 重写此方法
     */
    @Subscribe
    public void onEvent(IEvent event) {

    }


    protected void initDialog() {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(this, com.jusfoun.baselibrary.R.style.my_dialog);
            loadingDialog.setCancelable(true);
            loadingDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {

                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        if (loadingDialog != null) {
                            loadingDialog.cancel();
                            try {
                                VolleyUtil.getQueue(mContext).cancelAll(getLocalClassName());

                            } catch (Exception e) {

                            }
                        }
                    }
                    return true;
                }
            });
            loadingDialog.setCanceledOnTouchOutside(false);
        }
    }

    public void showLoading() {
        if ((!isFinishing())) {
            if (loadingDialog != null && !loadingDialog.isShowing()) {
                loadingDialog.show();
            }
        }

    }

    // 隐藏加载对话框
    public void hideLoadDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.cancel();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            VolleyUtil.getQueue(this).cancelAll(getLocalClassName());
        } catch (Exception e) {

        }
        EventBus.getDefault().unregister(this);
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
        setStatusBarEnable(Color.parseColor("#FFFFFF"));
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
        return Color.BLACK;
    }

    public boolean isSetStatusBar() {
        return true;
    }

    public boolean isBarDark() {
        return true;
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

}
