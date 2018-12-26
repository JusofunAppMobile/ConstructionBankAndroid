package com.jusfoun.baselibrary.Util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.widget.TextView;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author zhaoyapeng
 * @date 2018/1/17
 * @describe 统一管理share
 */

public class SharePrefenceUtils {

    private SharedPreferences sharedPreferences;
    private String DefaultName="DefaultName";

    private static final class SingleHolder{
         private static SharePrefenceUtils instance=new SharePrefenceUtils();
    }

    private SharePrefenceUtils(){

    }

    public static SharePrefenceUtils getInstance(){
        return SingleHolder.instance;
    }

    public void register(Context context,String name){
        sharedPreferences=context.getSharedPreferences(TextUtils.isEmpty(name)?DefaultName:name,Context.MODE_PRIVATE);
    }

    public String getString(String key){
        return getString(key,"");
    }

    public String getString(String key,String def){
        checkShare();
        return sharedPreferences.getString(key,def);
    }

    public int getInt(String key){
        return getInt(key,-1);
    }

    public int getInt(String key,int def){
        checkShare();
        return sharedPreferences.getInt(key,def);
    }

    public void setInt(String key,int value){
        checkShare();
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putInt(key,value);
        editor.commit();
    }

    public void setString(String key,String value){
        checkShare();
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(key,value);
        editor.commit();
    }

    public Set<String> getSet(String key){
        checkShare();
        return sharedPreferences.getStringSet(key,new HashSet<String>());
    }

    public void setSet(String key,Set<String> value){
        checkShare();
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putStringSet(key,value);
        editor.apply();
    }

    private void checkShare(){
        if (sharedPreferences==null){
            new IllegalArgumentException("sharepreferenc not register");
        }
    }

}
