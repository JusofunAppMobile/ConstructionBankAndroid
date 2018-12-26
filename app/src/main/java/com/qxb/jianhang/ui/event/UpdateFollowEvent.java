package com.qxb.jianhang.ui.event;

import com.jusfoun.baselibrary.event.IEvent;
import com.qxb.jianhang.ui.data.HomeDataItemModel;

/**
 * @author zhaoyapeng
 * @version create time:2018/12/417:48
 * @Email zyp@jusfoun.com
 * @Description ${TODO}
 */
public class UpdateFollowEvent implements IEvent {
    public String companyId;
    public String followState;
    public int type;
}

