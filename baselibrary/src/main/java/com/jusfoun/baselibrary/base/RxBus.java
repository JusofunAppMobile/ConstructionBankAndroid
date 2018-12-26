package com.jusfoun.baselibrary.base;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.jusfoun.baselibrary.Util.LogUtil;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

/**
 * Author  wangchenchen
 * CreateDate 2016/6/17.
 * Email wcc@jusfoun.com
 * Description 获取rxbus事件实例
 */
public class RxBus {

    private String TAG=RxBus.class.getSimpleName();

    private ConcurrentMap<Object,List<Subject>> subjectMapper=new ConcurrentHashMap<>();

    /**
     * 静态内部类单例
     */
    private static class RxBusHolder{
        private static final RxBus instance=new RxBus();

    }

    public static synchronized RxBus getInstance(){
        return RxBusHolder.instance;
    }

    public <T> Observable<T> register(@NonNull Object tag){
        List<Subject> subjects=subjectMapper.get(tag);
        if (null==subjects){
            subjects=new ArrayList<>();
            subjectMapper.put(tag,subjects);
        }

        Subject<T,T> subject=PublishSubject.create();
        subjects.add(subject);
        return subject;
    }

    public void unRegister(@NonNull Object tag, Observable<?> mObservable){
        if (null==mObservable)
            return;
        List<Subject> subjects=subjectMapper.get(tag);
        if (subjects!=null) {
            subjects.remove(mObservable);
            if (subjects.isEmpty())
                subjectMapper.remove(tag);
        }
    }

    public void post(@NonNull Object tag, @NonNull Object content){
        LogUtil.e(TAG,"post"+tag);
        List<Subject> subjectList=subjectMapper.get(tag);
        if (subjectList!=null&&subjectList.size()>0){
            for (Subject subject:subjectList){
                subject.onNext(content);
                LogUtil.e(TAG,"eventName=="+tag);
            }
        }
    }

    public void post(@NonNull Object evnet){
        if (evnet==null){
            throw  new IllegalArgumentException("event not null");
        }
        for (Map.Entry<Object, List<Subject>> objectListEntry : subjectMapper.entrySet()) {
            Object object=objectListEntry.getKey();
            Class cls=object.getClass();
            for (Method method : cls.getMethods()) {
                if (method==null)
                    continue;
                if (TextUtils.equals(method.getName(),"onEvent")){
                    Class<?>[] parameterType=method.getParameterTypes();
                    for (Class<?> aClass : parameterType) {
                        if (aClass==evnet.getClass())
                        {
                            List<Subject> subjectList=objectListEntry.getValue();
                            if (subjectList!=null&&subjectList.size()>0){
                                for (Subject subject:subjectList){
                                    EventEntry entry=new EventEntry();
                                    entry.tag=object;
                                    entry.evnet=evnet;
                                    subject.onNext(entry);
                                    LogUtil.e(TAG,"eventName=="+evnet);
                                }
                            }
                            return;
                        }
                    }
                }
            }
        }
       /* List<Subject> subjectList=subjectMapper.get(evnet);
        if (subjectList!=null&&subjectList.size()>0){
            for (Subject subject:subjectList){
                subject.onNext(evnet);
                LogUtil.e(TAG,"eventName=="+evnet);
            }
        }*/
    }
    private RxBus(){
    }

    public final class EventEntry{
        public Object tag;
        public Object evnet;
    }
}
