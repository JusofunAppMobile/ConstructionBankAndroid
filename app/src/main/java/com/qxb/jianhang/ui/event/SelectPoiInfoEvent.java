package com.qxb.jianhang.ui.event;

import com.jusfoun.baselibrary.event.IEvent;
import com.qxb.jianhang.ui.data.PoiInfoModel;

/**
 * @author liuguangdan
 * @version create at 2018/9/6/006 10:54
 * @Email lgd@jusfoun.com
 * @Description ${}
 */
public class SelectPoiInfoEvent implements IEvent {

    public PoiInfoModel model;

    public SelectPoiInfoEvent(PoiInfoModel model) {
        this.model = model;
    }
}
