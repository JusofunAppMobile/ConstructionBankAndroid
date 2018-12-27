package com.qxb.jianhang.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.qxb.jianhang.ui.data.PoiInfoModel;
import com.qxb.jianhang.ui.data.SearchListModel;
import com.qxb.jianhang.ui.fragment.HomeCompanyListFragment;
import com.qxb.jianhang.ui.fragment.MapFragment;
import com.qxb.jianhang.ui.fragment.PersonFragment;
import com.qxb.jianhang.ui.util.MainFragmentUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhaoyapeng
 * @version create time:18/8/2416:35
 * @Email zyp@jusfoun.com
 * @Description ${首页mainAdapter}
 */
public class MainAdapter extends FragmentPagerAdapter {

    private List<Fragment> list = new ArrayList<>();

    public MainAdapter(FragmentManager fm) {
        super(fm);
        list.add(MainFragmentUtil.getInstance(MainFragmentUtil.TYPE_MAP,null));
        list.add(new HomeCompanyListFragment());
        list.add(new PersonFragment());
    }

    public MainAdapter(FragmentManager fm, String searchKey, SearchListModel model, PoiInfoModel poiInfoModel) {
        super(fm);
        model.searchKey=searchKey;
        list.add(MainFragmentUtil.getInstance(MainFragmentUtil.TYPE_MAP,model));
        Log.e("tag","poiInfoModelpoiInfoModelpoiInfoModelpoiInfoModel2"+model.list.size());
        list.add(HomeCompanyListFragment.getInstance(model,poiInfoModel));
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    public boolean canQuit(){
        if(list!=null&&list.size()>0){
            return ((MapFragment)list.get(0)).onBackPressed();
        }
        return true;
    }
}
