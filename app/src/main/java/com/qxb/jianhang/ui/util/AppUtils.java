package com.qxb.jianhang.ui.util;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.Display;
import android.view.inputmethod.InputMethodManager;

import com.google.gson.Gson;
import com.jusfoun.baselibrary.BaseApplication;
import com.jusfoun.baselibrary.Util.PreferenceUtils;
import com.jusfoun.baselibrary.model.UserModel;
import com.qxb.jianhang.ui.constant.Constant;

/**
 * @author liuguangdan
 * @version create at 2018/9/5/005 11:09
 * @Email lgd@jusfoun.com
 * @Description ${}
 */
public class AppUtils {


    /**
     * 获取跟进状态
     * 1：已电话沟通 2：拜访中 3：已拜访 4：合作建立 5：正式合作
     *
     * @return
     */
    public static String[] getFollows() {
        return new String[]{"已电话沟通", "拜访中", "已拜访", "合作建立", "正式合作"};
    }

    public static UserModel getUserInfo() {
        String userInfo = PreferenceUtils.getString(BaseApplication.getBaseApplication(), Constant.PRE_USER_INFO);
        if (!TextUtils.isEmpty(userInfo)) {
            return new Gson().fromJson(userInfo, UserModel.class);
        }

        UserModel userModel = new UserModel();
        return userModel;
    }

    /**
     * dp转换成px
     */
    public static int dp2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int getScreenWidth(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        return display.getWidth();
    }

    /**
     * 显示软键盘
     */
    public static void showSoftInput(Activity activity) {
        InputMethodManager manager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

}
