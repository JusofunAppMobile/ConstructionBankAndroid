package com.jusfoun.jusfouninquire.constant;

import java.util.Arrays;
import java.util.List;

/**
 * @author zhaoyapeng
 * @version create time:17/9/1410:31
 * @Email zyp@jusfoun.com
 * @Description ${企业详情 九宫格 常量}
 */
public interface CompanyDetailTypeConstant {
    int TYPE_WEB = 1;
    int TYPE_MAP = 2;
    int TYPE_INVEST = 3;
    int TYPE_BRANCH = 4;
    int TYPE_TAB = 5; // 选项卡
    int TYPE_BIDDING = 6; // 招投标
    int TYPE_RECRUITMENT = 7; // 招聘
    int TYPE_BRAND = 9;  // 无形资产-商标
    int TYPE_PATENT = 8; // 无形资产-专利
    int TYPE_TAXATION = 10; // 税务 欠税公招
    int TYPE_PUBLISH = 11; // 行政处罚
    int TYPE_STOCK = 12; // 股权出质
    int TYPE_BIDS= 13; // 招标
    Integer type[] = {
            TYPE_WEB,
            TYPE_MAP,
            TYPE_INVEST,
            TYPE_BRANCH,
            TYPE_TAB,
            TYPE_BIDDING,
            TYPE_RECRUITMENT,
            TYPE_BRAND,
            TYPE_PATENT,
            TYPE_TAXATION,
            TYPE_PUBLISH,
            TYPE_STOCK,
            TYPE_BIDS
    };
    List<Integer> types = Arrays.asList(type);

}
