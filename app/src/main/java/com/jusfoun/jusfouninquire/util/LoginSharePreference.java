package com.jusfoun.jusfouninquire.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.qxb.jianhang.ui.data.UserInfoModel;

/**
 * @author lee
 * @version create time:2015/11/1817:47
 * @Email email
 * @Description $存储登录的model
 */

public class LoginSharePreference {

    private final static String USER_INFO ="userInfo";
    private final static String PRODUCT_SHAREDPREFERENCES = "sp_userInfo";

    public static void saveUserInfo(UserInfoModel loginModel, Context context) {
        String value = new Gson().toJson(loginModel);
        Log.d("TAG", "LoginSharePreference  userInfo:" + value);
        SharedPreferences userInfoPreferences = context.getSharedPreferences(PRODUCT_SHAREDPREFERENCES,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userInfoPreferences.edit();
        editor.putString(USER_INFO, value);
        editor.commit();
    }

    public static UserInfoModel getUserInfo(Context context){
        SharedPreferences userInfoPreferences = context.getSharedPreferences(PRODUCT_SHAREDPREFERENCES, Context.MODE_PRIVATE);
        String value = userInfoPreferences.getString(USER_INFO, "");
        try{
            UserInfoModel userInfo = new Gson().fromJson(value, UserInfoModel.class);
            return userInfo;
        } catch (Exception exception) {
            Toast.makeText(context, "服务器维护中", Toast.LENGTH_SHORT).show();
        } finally {

        }
        return new UserInfoModel();
    }

    public static void deleteUserInfo(Context context){
        SharedPreferences userInfoPreferences = context.getSharedPreferences(PRODUCT_SHAREDPREFERENCES,
                Context.MODE_PRIVATE);
        if(!userInfoPreferences.getAll().isEmpty()){
            SharedPreferences.Editor editor = userInfoPreferences.edit();
            editor.clear();
            editor.commit();
        }
    }
}
