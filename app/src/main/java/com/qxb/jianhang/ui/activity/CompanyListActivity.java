package com.qxb.jianhang.ui.activity;

import com.jusfoun.baselibrary.view.HomeViewPager;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.qxb.jianhang.R;
import com.qxb.jianhang.ui.adapter.CompayListAdapter;
import com.qxb.jianhang.ui.base.BaseBackActivity;

public class CompanyListActivity extends BaseBackActivity {


    protected HomeViewPager viewpager;
    protected SmartTabLayout viewpagertab;
    private CompayListAdapter mainAdapter;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_company_list;
    }

    @Override
    public void initDatas() {
        mainAdapter = new CompayListAdapter(getSupportFragmentManager(),null,null);
    }

    @Override
    public void initView() {
        viewpager = (HomeViewPager) findViewById(R.id.viewpager);
        viewpagertab = (SmartTabLayout) findViewById(R.id.viewpagertab);

    }

    @Override
    public void initAction() {
        viewpager.setAdapter(mainAdapter);

        viewpagertab.setViewPager(viewpager);

    }

}
