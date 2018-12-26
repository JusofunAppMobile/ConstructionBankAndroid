package com.qxb.jianhang.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.qxb.jianhang.ui.data.PoiInfoModel;
import com.qxb.jianhang.ui.data.SearchListModel;
import com.qxb.jianhang.ui.fragment.BankCompanyListFragment;

/**
 * @author zhaoyapeng
 * @version create time:18/8/2416:35
 * @Email zyp@jusfoun.com
 * @Description ${企业列表 fragment}
 */
public class CompayListAdapter extends FragmentPagerAdapter {

    private SearchListModel model;

    private PoiInfoModel poiInfoModel;

    private String titleArr[]={"新增企业","推荐客户","正式客户"};
    public CompayListAdapter(FragmentManager fm, SearchListModel model,PoiInfoModel poiInfoModel) {
        super(fm);
        this.model = model;
        this.poiInfoModel = poiInfoModel;
    }

    @Override
    public Fragment getItem(int position) {
        return BankCompanyListFragment.getInstance(position,model,poiInfoModel);
    }

    @Override
    public int getCount() {
        return 3;
    }


    @Override
    public CharSequence getPageTitle(int position) {

        return titleArr[position];
    }
}
