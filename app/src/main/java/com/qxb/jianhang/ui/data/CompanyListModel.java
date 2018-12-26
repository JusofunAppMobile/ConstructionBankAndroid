package com.qxb.jianhang.ui.data;

import com.jusfoun.baselibrary.base.BaseModel;

import java.io.Serializable;
import java.util.List;

/**
 * @author zhaoyapeng
 * @version create time:18/9/1310:20
 * @Email zyp@jusfoun.com
 * @Description ${公司列表model}
 */
public class CompanyListModel extends BaseModel implements Serializable{

    public List<HomeDataItemModel> list;
    public List<HomeDataItemModel> list2;
    public PointModel org;


    public class PointModel extends BaseModel implements Serializable{
        public double lat;
        public double lon;
    }
}
