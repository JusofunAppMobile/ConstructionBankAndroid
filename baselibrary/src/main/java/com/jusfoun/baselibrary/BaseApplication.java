package com.jusfoun.baselibrary;

import android.app.Activity;
import android.app.Application;

import com.jusfoun.baselibrary.db.DataBaseManager;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.PrettyFormatStrategy;

import java.lang.ref.WeakReference;
import java.util.Stack;

/**
 * Created by zhaoyapeng on 2017/10/11.
 * describe application 基类
 */
//
public class BaseApplication extends Application {

    /**
     * 播放服务是否被强迫回收
     */
    private boolean playServiceForceDestroy = false;

    protected static BaseApplication mApp;

    @Override
    public void onCreate() {
        super.onCreate();
        mApp=this;

        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)
                .methodCount(0)
                .tag("MyTag")
                .build();
        com.orhanobut.logger.Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));
    }

    public static BaseApplication getBaseApplication(){
        return mApp;
    }



    /***
     * 寄存整个应用Activity
     **/
    protected final Stack<WeakReference<Activity>> activitys = new Stack<WeakReference<Activity>>();

    /******************************************* Application中存放的Activity操作（压栈/出栈）API（开始） *****************************************/

    /**
     * 将Activity压入Application栈
     *
     * @param task 将要压入栈的Activity对象
     */
    public void pushTask(WeakReference<Activity> task) {
        activitys.push(task);
    }

    /**
     * 将传入的Activity对象从栈中移除
     *
     * @param task
     */
    public void removeTask(WeakReference<Activity> task) {
        activitys.remove(task);
    }

    /**
     * 根据指定位置从栈中移除Activity
     *
     * @param taskIndex Activity栈索引
     */
    public void removeTask(int taskIndex) {
        if (activitys.size() > taskIndex)
            activitys.remove(taskIndex);
    }

    /**
     * 将栈中Activity移除至栈顶
     */
    public void removeToTop() {
        int end = activitys.size();
        int start = 1;
        for (int i = end - 1; i >= start; i--) {
            if (!activitys.get(i).get().isFinishing()) {
                activitys.get(i).get().finish();
            }
        }
    }

    /**
     * 移除全部（用于整个应用退出）
     */
    public void removeAll() {
        // finish所有的Activity
        for (WeakReference<Activity> task : activitys) {
            if (!task.get().isFinishing()) {
                task.get().finish();
            }
        }

        DataBaseManager.getInstance().closeAllDB();
    }

    /******************************************* Application中存放的Activity操作（压栈/出栈）API（结束） *****************************************/
}
