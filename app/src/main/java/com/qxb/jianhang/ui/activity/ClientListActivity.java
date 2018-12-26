package com.qxb.jianhang.ui.activity;

import android.graphics.Typeface;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.jusfoun.baselibrary.view.HomeViewPager;
import com.jusfoun.jusfouninquire.view.TitleView;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.qxb.jianhang.R;
import com.qxb.jianhang.ui.adapter.ClientPagerAdapter;
import com.qxb.jianhang.ui.base.BaseBackActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author liuguangdan
 * @version create at 2018/8/29/029 16:36
 * @Email lgd@jusfoun.com
 * @Description ${我的客户列表页面}
 */
public class ClientListActivity extends BaseBackActivity implements ViewPager.OnPageChangeListener {

    @BindView(R.id.titleView2)
    TitleView titleView;
    @BindView(R.id.smartTabLayout)
    SmartTabLayout smartTabLayout;
    @BindView(R.id.viewpager)
    HomeViewPager viewpager;

    private ClientPagerAdapter adapter;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_client_list;
    }

    @Override
    public void initDatas() {
        adapter = new ClientPagerAdapter(getSupportFragmentManager());
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
    }

    @Override
    public void initAction() {
        viewpager.setAdapter(adapter);
        smartTabLayout.setViewPager(viewpager);
        titleView.setTitle("我的客户");
        titleView.hideLineView();
        dealTabText();
    }

    private void dealTabText() {
        viewpager.addOnPageChangeListener(this);
        TextView textView = (TextView) smartTabLayout.getTabAt(0);
        textView.setTextColor(0xff333333);
        textView.setTextSize(16);
        textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < 2; i++) {
            TextView textView = (TextView) smartTabLayout.getTabAt(i);
            textView.setTextColor(0xff666666);
            textView.setTextSize(14);
            textView.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        }
        TextView textView = (TextView) smartTabLayout.getTabAt(position);
        textView.setTextColor(0xff333333);
        textView.setTextSize(16);
        textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
