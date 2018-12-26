package com.qxb.jianhang.net.event;

import com.jusfoun.baselibrary.event.IEvent;

/**
 * Created by Albert on 2016-4-20.
 * Mail : lbh@jusfoun.com
 * TODO :
 * Description:企业详情宫格动画结束发送该event
 */
public class AnimationEndEvent implements IEvent{
    private int index;

    public AnimationEndEvent(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
