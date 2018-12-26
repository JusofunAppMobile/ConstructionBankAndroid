package com.qxb.jianhang.ui.event;

import com.jusfoun.baselibrary.event.IEvent;

/**
 * @author liuguangdan
 * @version create at 2018/9/5/005 19:16
 * @Email lgd@jusfoun.com
 * @Description ${复制消息内容}
 */
public class CopyMessageEvent implements IEvent {

    public String content;

    public CopyMessageEvent(String content) {
        this.content = content;
    }
}
