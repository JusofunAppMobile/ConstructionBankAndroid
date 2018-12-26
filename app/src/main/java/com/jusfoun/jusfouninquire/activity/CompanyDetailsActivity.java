package com.jusfoun.jusfouninquire.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;

import com.qxb.jianhang.R;
import com.jusfoun.jusfouninquire.event.CompanyMapEvent;
import com.jusfoun.jusfouninquire.event.CompanyWebEvent;
import com.jusfoun.jusfouninquire.event.InvestOrBranchEvent;
import com.jusfoun.jusfouninquire.fragment.CompanyWebFragment;
import com.jusfoun.jusfouninquire.internal.OnWebFragmentListener;
import com.qxb.jianhang.ui.adapter.CompanyDetailAdapter;
import com.qxb.jianhang.ui.adapter.MenuAdapter;
import com.jusfoun.jusfouninquire.constant.CompanyDetailTypeConstant;
import com.qxb.jianhang.ui.data.CompanyDetailModel;
import com.qxb.jianhang.ui.view.PullMenuTitleView;
import com.jusfoun.jusfouninquire.widget.CompanyDetailsViewPager;
import com.jusfoun.jusfouninquire.widget.MenuWindow;

import org.greenrobot.eventbus.EventBus;


/**
 * Author  JUSFOUN
 * CreateDate 2015/11/9.
 * Description 企业详情详细页面
 */
public class CompanyDetailsActivity extends BaseInquireActivity implements OnWebFragmentListener, CompanyDetailTypeConstant {

    public static final String COMPANY = "company";
    public static final String POSITION = "position";


    private CompanyDetailsViewPager pager;
    private CompanyDetailAdapter adapter;
    private Bundle argument;
    private MenuWindow menuWindow;
    private PullMenuTitleView title;

    private boolean isShowMenuOut = false;
    private int position = -1;
    private CompanyDetailModel model;
    private View view;
    private Fragment fragment = null;

    private boolean isCanOpenMenu = true;

    @Override
    public void initData() {
        argument = getIntent().getExtras() == null ? new Bundle() : getIntent().getExtras();
        model = (CompanyDetailModel) argument.getSerializable(COMPANY);
        position = argument.getInt(POSITION);
        adapter = new CompanyDetailAdapter(getSupportFragmentManager(), argument);

        isShowMenuOut = false;
//        mSwipeBackLayout.setEnableGesture(true);
    }

    @Override
    public void initView() {

        setContentView(R.layout.activity_company_details);
        pager = (CompanyDetailsViewPager) findViewById(R.id.pager_company);
        title = (PullMenuTitleView) findViewById(R.id.title);
        menuWindow = new MenuWindow(this);
        view = findViewById(R.id.view);
    }

    @Override
    public void initWidgetActions() {
        if (model == null)
            return;
        title.setTitle(model.getSubclassMenu().get(position).getMenuname());
//        title.setRightImg(R.mipmap.company_amend);
//        title.setRightTxt("纠错");
        title.setTitleClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                if (isShowMenuOut) {
                    isShowMenuOut = false;
                    return;
                }
                if (menuWindow.isShowing()) {
                    isShowMenuOut = false;
                    menuWindow.setWindowHide();
                    darkenBackgroud(false);
                    title.setTitleRightDrawable(R.mipmap.company_menu_down);
                } else {
                    if (!isCanOpenMenu)
                        return;
                    isShowMenuOut = true;
                    menuWindow.showAsDropDown(title);
                    menuWindow.setWindowShow();
                    title.setTitleRightDrawable(R.mipmap.company_menu_up);
                    darkenBackgroud(true);
                }
            }
        });

        title.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragment != null && fragment instanceof CompanyWebFragment) {
                    if (((CompanyWebFragment) fragment).getWeb() != null) {
                        if (((CompanyWebFragment) fragment).getWeb().canGoBack()) {

                            ((CompanyWebFragment) fragment).getWeb().goBack();
                            return;
                        }
                    }
                }
                finish();
            }
        });
//        title.setRightClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (model != null) {
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("company", model);
//                    bundle.putInt(CompanyAmendActivity.POSITION, position);
//                    goActivity(CompanyAmendActivity.class, bundle);
//                }
//            }
//        });


        pager.setIsCanScroll(false);
        pager.setAdapter(adapter);
//        if (model != null) {
//            int type = model.getSubclassMenu().get(position).getType();
//            switch (type) {
//                case TYPE_WEB:
//                    pager.setCurrentItem(WEB_TYPE);
//                    break;
//                case TYPE_MAP:
//                    pager.setCurrentItem(CompanyDetailUtil.COMPANY_MAP_TYPE);
//                    break;
//                case TYPE_INVEST:
//                case TYPE_BRANCH:
//                    pager.setCurrentItem(CompanyDetailUtil.TYPE);
//                    break;
//                case TYPE_BRAND:  // 无形资产-商标
//                    pager.setCurrentItem(CompanyDetailUtil.TYPE_BRAND);
//                    break;
//                case TYPE_PATENT:  // 无形资产-专利
//                    pager.setCurrentItem(CompanyDetailUtil.TYPE_PATENT);
//                    break;
//                case TYPE_TAB:  // 选项卡
//                    pager.setCurrentItem(CompanyDetailUtil.TYPE_TAB);
//                    break;
//                case TYPE_BIDDING:  // 中标
//                    pager.setCurrentItem(CompanyDetailUtil.TYPE_BIDDING);
//                    break;
//                case TYPE_RECRUITMENT:  // 招聘
//                    pager.setCurrentItem(CompanyDetailUtil.TYPE_RECRUITMENT);
//                    break;
//            }
//    }

//        menuWindow.setAnimationStyle(R.style.menu_window);


        menuWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    menuWindow.setWindowHide();
                    darkenBackgroud(false);
                    isShowMenuOut = true;
                    title.setTitleRightDrawable(R.mipmap.company_menu_down);
                    return true;
                }
                return false;
            }
        });

        menuWindow.setOutsideTouchable(true);
        menuWindow.updateView(model.getSubclassMenu());
        menuWindow.setCheckCount(position);
        menuWindow.setOnClickListener(new MenuAdapter.OnClickListener() {
            @Override
            public void onClick(int position) {
                CompanyDetailsActivity.this.position = position;
                int type = model.getSubclassMenu().get(position).getType();
                argument.putInt(POSITION, position);
                CompanyDetailsActivity.this.position = position;
                title.setTitle(model.getSubclassMenu().get(position).getMenuname());
                pager.setCurrentItem(position);
                switch (type) {
                    case TYPE_WEB:
//                        pager.setCurrentItem(CompanyDetailUtil.WEB_TYPE);
                        EventBus.getDefault().post(new CompanyWebEvent(argument));
                        break;
                    case TYPE_MAP:
//                        pager.setCurrentItem(CompanyDetailUtil.COMPANY_MAP_TYPE);
                        EventBus.getDefault().post(new CompanyMapEvent());
                        break;
                    case TYPE_INVEST:
                    case TYPE_BRANCH:
//                        pager.setCurrentItem(CompanyDetailUtil.TYPE);
                        EventBus.getDefault().post(new InvestOrBranchEvent(argument));
                        break;
                    case TYPE_BRAND:// 无形资产-商标
                        pager.setCurrentItem(position);
                        break;
                    case TYPE_PATENT:// 无形资产-专利
                        pager.setCurrentItem(position);
                        break;
                    case TYPE_TAB: // 选项卡
                        pager.setCurrentItem(position);
                        break;
                    case TYPE_BIDDING:  // 中标
                        pager.setCurrentItem(position);
                        break;
                    case TYPE_RECRUITMENT:  // 招聘
                        pager.setCurrentItem(position);
                        break;
                }

                menuWindow.setWindowHide();
                darkenBackgroud(false);
                isShowMenuOut = false;
                menuWindow.setCheckCount(position);
                title.setTitleRightDrawable(R.mipmap.company_menu_down);
            }
        });
        if (model != null && model.getSubclassMenu() != null) {
            adapter.refresh(model.getSubclassMenu());
            pager.setCurrentItem(position);
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isShowMenuOut) {
                menuWindow.setWindowHide();
                title.setTitleRightDrawable(R.mipmap.company_menu_down);
                darkenBackgroud(false);
                isShowMenuOut = false;
                return true;
            }
            if (fragment != null && fragment instanceof CompanyWebFragment) {
                if (((CompanyWebFragment) fragment).getWeb() != null) {
                    if (((CompanyWebFragment) fragment).getWeb().canGoBack()) {
                        ((CompanyWebFragment) fragment).getWeb().goBack();
                        return true;
                    }
                }
            }
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void darkenBackgroud(boolean isShow) {
        if (isShow)
            view.setVisibility(View.VISIBLE);
        else
            view.setVisibility(View.GONE);
    }

    @Override
    public void onCurrentFragment(Fragment fragment) {
        this.fragment = fragment;
    }


    @Override
    public void onWebViewStart(String url, WebView webView) {
        if (position < 0 && model.getSubclassMenu().size() <= position)
            return;
      /*  try {
            url= URLDecoder.decode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }*/

//        if(canAlwaysSelectMenu(model.getSubclassMenu().get(position).getType()))
//            return;

//        if (WebUtil.getUrlProtocol(url, model.getSubclassMenu().get(position).getApplinkurl())) {
//
//        } else {
//            title.setTitleRightDrawable(-1);
//            title.setRightHite();
//            isCanOpenMenu = false;
//            String titleTxt = model.getSubclassMenu().get(position).getMenuname();
//            if (!TextUtils.isEmpty(titleTxt)) {
//                if ("企业舆情".equals(titleTxt))
//                    title.setTitle("资讯详情");
//                else if ("企业年报".equals(titleTxt)) {
//                    title.setTitle("年报详情");
//                } else
//                    title.setTitle(titleTxt.substring(0, titleTxt.length() - 2) + "详情");
//            }
//
//
//        }

//        if(url.contains("nbxx_detail")){
//            title.setTitleRightDrawable(-1);
//            title.setRightHite();
//            isCanOpenMenu = false;
//            title.setTitle("年报详情");
//        }else{
//            title.setTitle(model.getSubclassMenu().get(position).getMenuname());
//            isCanOpenMenu = true;
//            title.setRightVisible();
//            title.setTitleRightDrawable(R.mipmap.company_menu_down);
//        }
    }
}
