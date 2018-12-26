package com.qxb.jianhang.ui.data;

import com.jusfoun.baselibrary.base.BaseModel;
import com.jusfoun.baselibrary.net.NetModel;

import java.io.Serializable;
import java.util.List;

/**
 * @author zhaoyapeng
 * @version create time:18/9/715:29
 * @Email zyp@jusfoun.com
 * @Description ${跟踪记录list}
 */
public class TrackRecordDataModel extends BaseModel implements Serializable {


    public List<TrackRecordItemModel>list;

    public int totalCount;



    public static class  TrackRecordItemModel extends NetModel implements Serializable{
        public String id;
        public String time;
        public String name;
        public String job;
        public String desc;
        public String address;
        public int followState;
        public String longitude;
        public String latitude;
        public String remark;
        public String phone;
        public String detailAddress;
        public boolean linkPhone;
        public String followId;
        public String companyid;
        public String companyname;


        public String demand;
        public String suggest;
        public String isDemandSolved;
        public String support;
        public String isSupportSolved;
    }
}
