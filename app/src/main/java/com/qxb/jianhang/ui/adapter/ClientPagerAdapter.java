package com.qxb.jianhang.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.qxb.jianhang.ui.fragment.ClientFragment;

public class ClientPagerAdapter extends FragmentPagerAdapter {

    private String titleArr[] = {"跟进客户", "正式客户"};

    public ClientPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return ClientFragment.getInstance(position + 2);
    }

    @Override
    public int getCount() {
        return 2;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return titleArr[position];
    }
}
