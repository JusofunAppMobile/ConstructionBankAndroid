package com.qxb.jianhang.ui.data;

import java.util.List;

/**
 * @author liuguangdan
 * @version create at 2018/12/17/017 14:08
 * @Email lgd@jusfoun.com
 * @Description ${}
 */
public class StatModel {


    /**
     * clientCount : 72
     * addCount : 120
     * followCount : 64
     * formalCount : 8
     * addTrendList : [{"date":"2018-11-13","followCount":0,"formalCount":3},{"date":"2018-11-15","followCount":2,"formalCount":3},{"date":"2018-11-16","followCount":2,"formalCount":3},{"date":"2018-11-19","followCount":2,"formalCount":0},{"date":"2018-11-28","followCount":2,"formalCount":0},{"date":"2018-11-29","followCount":2,"formalCount":3}]
     * followRate : 0
     * formaltRate : 0
     * followSitList : [{"label":"已电话沟通","count":"20","followState":1},{"label":"拜访中","count":"7","followState":2},{"label":"已拜访","count":"6","followState":3},{"label":"合作建立","count":"4","followState":4},{"label":"正式合作","count":"15","followState":5}]
     * browseList : []
     */

    public int clientCount;
    public int addCount;
    public int followCount;
    public int formalCount;
    public double followRate;
    public double formaltRate;
    public int followRateState; // 0：下降 1：上升 2：平衡
    public int formaltRateState; // 0：下降 1：上升 2：平衡
    public List<AddTrendListBean> addTrendList;
    public List<FollowSitListBean> followSitList;
    public List<HistoryModel> browseList;

    public static class AddTrendListBean {
        /**
         * date : 2018-11-13
         * followCount : 0
         * formalCount : 3
         */

        public String date;
        public int followCount;
        public int formalCount;
    }

    public static class FollowSitListBean {
        /**
         * label : 已电话沟通
         * count : 20
         * followState : 1
         */

        public String label;
        public int count;
        public int followState;
    }
}
