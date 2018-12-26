package com.qxb.jianhang.ui.util;

import android.os.Bundle;
import android.util.Log;

import com.jusfoun.jusfouninquire.fragment.BaseInquireFragment;
import com.jusfoun.jusfouninquire.fragment.CompanyInvestmentFragment;
import com.jusfoun.jusfouninquire.fragment.CompanyMapFragment;
import com.jusfoun.jusfouninquire.fragment.CompanyWebFragment;
import com.jusfoun.jusfouninquire.activity.CompanyDetailsActivity;
import com.qxb.jianhang.ui.data.CompanyDetailMenuModel;

import java.util.List;

/**
 * Author  JUSFOUN
 * CreateDate 2015/11/9.
 * Description
 */
public class CompanyDetailUtil {

//    public static final int WEB_TYPE = 0;
//    public static final int COMPANY_MAP_TYPE = 1;
//    public static final int TYPE = 2;
//    public static final int TYPE_BRAND = 3; // 无形资产-商标
//    public static final int TYPE_PATENT = 4; // 无形资产-专利
//    public static final int TYPE_TAB = 5; // 选项卡
//    public static final int TYPE_BIDDING = 6; // 招投标
//    public static final int TYPE_RECRUITMENT = 7; // 招聘
//    //    public static final int TYPE_TAB = 5; // 选项卡


    public static BaseInquireFragment getInstance(int position, Bundle argument, List<CompanyDetailMenuModel> subclassMenu) {
        BaseInquireFragment fragment = null;
        if (subclassMenu != null && subclassMenu.get(position) != null)
            switch (subclassMenu.get(position).getType()) {
                case CompanyDetailsActivity.TYPE_WEB:
                    fragment = CompanyWebFragment.getInstance(argument);
                    break;
                case CompanyDetailsActivity.TYPE_MAP:
                    fragment = CompanyMapFragment.getInstance(argument);
                    break;
                case CompanyDetailsActivity.TYPE_INVEST:
                case CompanyDetailsActivity.TYPE_BRANCH:
                    fragment = CompanyInvestmentFragment.getInstance(argument);
                    break;
            }
        return fragment;
    }

    public static boolean canAlwaysSelectMenu(int type) {
        if (type == CompanyDetailsActivity.TYPE_BRAND || type == CompanyDetailsActivity.TYPE_PATENT || type == CompanyDetailsActivity.TYPE_TAB)
            return true;
        return false;
    }
}
