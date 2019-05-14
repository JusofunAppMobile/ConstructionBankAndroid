package com.qxb.jianhang.net.event;

import com.jusfoun.baselibrary.event.IEvent;
import com.qxb.jianhang.ui.data.PoiInfoModel;
import com.qxb.jianhang.ui.data.SearchListModel;

/**
 * @author zhaoyapeng
 * @version create time:2018/11/615:05
 * @Email zyp@jusfoun.com
 * @Description ${搜索刷新event}
 */
public class SearchRefreshEvent implements IEvent {
  public  PoiInfoModel poiInfoModel;
}
