package com.qxb.jianhang.ui.util;

import android.support.v4.app.Fragment;

import com.qxb.jianhang.ui.data.SearchListModel;
import com.qxb.jianhang.ui.fragment.BaseBackFragment;
import com.qxb.jianhang.ui.fragment.MapFragment;

/**
 * @author zhaoyapeng
 * @version create time:18/8/2416:35
 * @Email zyp@jusfoun.com
 * @Description ${首页fragment util}
 */
public class MainFragmentUtil {

    public static int TYPE_MAP = 0;
    public static int TYPE_LIST = 1;


    public static Fragment getInstance(int index, SearchListModel model) {
        BaseBackFragment fragment = null;
        if (index == TYPE_MAP) {
            fragment = MapFragment.getInstance(model);
        } else if (index == TYPE_LIST) {
            fragment =  new MapFragment();
        }

        return fragment;
    }
}
