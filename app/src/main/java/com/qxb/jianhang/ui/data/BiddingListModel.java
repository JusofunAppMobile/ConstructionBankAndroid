package com.qxb.jianhang.ui.data;

import com.jusfoun.baselibrary.base.BaseModel;

import java.util.List;

/**
 * @author zhaoyapeng
 * @version create time:17/9/1117:54
 * @Email zyp@jusfoun.com
 * @Description ${招投标model}
 */
public class BiddingListModel extends BaseModel {
    public List<BiddingItemModel> dataResult;
    public int totalCount;

    public static class BiddingItemModel extends BaseModel {
        public String url;
        public String id;
        public String procurementName;
        public String administrativeRegion;
        public String province;
        public String city;
        public String district;
        public String announcementTime;
        public String announcementType;
        public String biddingAnnouncemntDate;
        public String winBidDate;
        public String winningAmount;
        public String projectContactPerson;
        public String projectContactTel;
        public String purchaseCompanyName;
        public String purchaseUnitAddress;
        public String purchaseUnitContact;
        public String agencyName;
        public String agencyAddress;
        public String agencyContact;
        public String website;
        public String importTime;
        public String createTime;
        public String updateTime;
        public String assessmentExpertList;
        public String winBidSupplier;
        public String content;
        public String item;
    }
}

