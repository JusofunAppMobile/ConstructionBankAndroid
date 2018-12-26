package com.jusfoun.baselibrary.delegate;

import com.jusfoun.baselibrary.Util.LogUtil;
import com.jusfoun.baselibrary.base.BaseEvnet;

import org.greenrobot.eventbus.EventBus;

/**
 * @author zhaoyapeng
 * @date 2017/11/17
 * @describe event 委托类
 */

public class EventDelegate {

    public static void register(Object object) {
        if (object != null) {
            if (!EventBus.getDefault().isRegistered(object)) {
                EventBus.getDefault().register(object);
            } else {
                LogUtil.w("EventDelegate", "eventbus register class " + object.getClass().getName());
            }
        }
    }

    public static void unregister(Object object) {
        if (object != null) {
            if (EventBus.getDefault().isRegistered(object)) {
                EventBus.getDefault().unregister(object);
            } else {
                LogUtil.w("EventDelegate", "eventbus unregister class " + object.getClass().getName());
            }
        }
    }

    public static synchronized boolean isRegistered(Object subscriber) {
        if (subscriber != null) {
            return EventBus.getDefault().isRegistered(subscriber);
        }
        return false;
    }

    public static boolean hasSubscriberForEvent(Class<?> eventClass) {
        return EventBus.getDefault().hasSubscriberForEvent(eventClass);
    }

    //消息传递
    public static void sendStickyEventMsg(final BaseEvnet object) {
        if (object != null) {
            EventBus.getDefault().postSticky(object);
        }
    }

    public static void removeStickyEventMsg(final BaseEvnet object){
        if (object != null) {
            EventBus.getDefault().removeStickyEvent(object);
        }
    }

    //消息传递
    public static void sendEventMsg(final BaseEvnet object) {
        if (object != null) {
            EventBus.getDefault().post(object);
        }
    }
}
