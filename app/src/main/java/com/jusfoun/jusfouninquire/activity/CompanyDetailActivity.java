package com.jusfoun.jusfouninquire.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jusfoun.baselibrary.Util.RegularUtils;
import com.jusfoun.baselibrary.Util.SystemIntentUtil;
import com.jusfoun.baselibrary.event.IEvent;
import com.jusfoun.baselibrary.net.NetModel;
import com.jusfoun.jusfouninquire.TimeOut;
import com.jusfoun.jusfouninquire.animation.SceneAnimation;
import com.jusfoun.jusfouninquire.callback.NetWorkCallBack;
import com.jusfoun.jusfouninquire.route.NetWorkCompanyDetails;
import com.jusfoun.jusfouninquire.view.TitleView;
import com.jusfoun.jusfouninquire.widget.ContactWayDialog;
import com.jusfoun.jusfouninquire.widget.DividerGridItemDecoration;
import com.jusfoun.jusfouninquire.widget.FullyGridLayoutManger;
import com.qxb.jianhang.BackApplication;
import com.qxb.jianhang.R;
import com.qxb.jianhang.net.event.AnimationEndEvent;
import com.qxb.jianhang.ui.activity.MessageListActivity;
import com.qxb.jianhang.ui.adapter.CompanyMenuAdapter;
import com.qxb.jianhang.ui.constant.Constant;
import com.qxb.jianhang.ui.data.BankCompanyDetailModel;
import com.qxb.jianhang.ui.data.CompanyDetailModel;
import com.qxb.jianhang.ui.data.ContactinFormationModel;
import com.qxb.jianhang.ui.data.UserInfoModel;
import com.qxb.jianhang.ui.sharedpreferences.LocationSharepreferences;
import com.qxb.jianhang.ui.util.AppUtils;
import com.qxb.jianhang.ui.view.CompanyDetailHeaderView;
import com.qxb.jianhang.ui.view.CompanyDetailMenuView;
import com.qxb.jianhang.ui.view.MyHeaderView;
import com.qxb.jianhang.ui.view.NetWorkErrorView;
import com.qxb.jianhang.ui.view.ObserveScrollView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhaoyapeng
 * @version create time:15/11/2下午4:55
 * @Email zyp@jusfoun.com
 * @Description ${公司详情页面}
 */
public class CompanyDetailActivity extends BaseInquireActivity {

    public static final String COMPANY_ID = "company_id";
    public static final String COMPANY_NAME = "company_name";
    public static final String COMPANY_STATE = "company_state";
    public static final String COMPANY_DISTANCE = "company_distance";
    private CompanyDetailHeaderView headerView;
    private HashMap<String, String> params;
    private NetWorkErrorView netWorkError;
    private RecyclerView mCompanyMenu;
    private CompanyMenuAdapter adapter;
    private CompanyDetailModel model;
    private String mCompanyId = "", mCompanyName,companyDistance;
    private UserInfoModel userInfo;
    private boolean isHasLatLng;
    private ContactWayDialog contactWayDialog;
//    private ShareDialog shareDialog;

    //    private ShareUtil shareUtil;
    private MyHeaderView myHeaderView;
    private RelativeLayout loadingLayout;
    private ImageView imageView;

    private String followStatue;
    private SceneAnimation sceneAnimation;
    private List<ContactinFormationModel> contactinList;
    private ObserveScrollView scrollView;
    private CompanyDetailMenuView riskInfoVide, operatingConditionsView, intangibleAssetsView;
    private TextView lookReportText;
    //    private Handler updateHandler;
    private final int UPDATE_MSG = 1;
    private final int UPDATE_DELAY = 20 * 1000;
    //    private View vBarEmpty1, vBarEmpty2;
    private View vTitleParent;
    private View vTypeSelect;
    private View vArrow;
    private TextView tvTypeLabel;

    public final static int TYPE_FENGXIAN = 1;
    public final static int TYPE_GUQUAN = 2;
    public final static int TYPE_QIYEBAOGAO = 3;

    private int selectType = TYPE_QIYEBAOGAO;
    private TitleView titleView;
    private String companyType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        setStatusBarEnable(Color.TRANSPARENT);
        super.onCreate(savedInstanceState);
    }


    @Override
    public void initData() {

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.getString(COMPANY_ID) != null)
                mCompanyId = bundle.getString(COMPANY_ID);

            if (bundle.get(COMPANY_STATE) != null) {
                companyType = bundle.getString(COMPANY_STATE);
            }
            mCompanyName = bundle.getString(COMPANY_NAME);
            companyDistance=  bundle.getString(COMPANY_DISTANCE);
        }
        adapter = new CompanyMenuAdapter(mContext);
        userInfo = BackApplication.getUserInfo();
//        shareUtil = new ShareUtil(mContext);

//        shareDialog = new ShareDialog(mContext, R.style.tool_dialog);
//        shareDialog.setCancelable(true);
//        Window shareWindow = shareDialog.getWindow();
//        shareWindow.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
//        shareWindow.setWindowAnimations(R.style.share_dialog_style); // 添加动画

        contactWayDialog = new ContactWayDialog(mContext, R.style.tool_dialog);
        contactWayDialog.setCancelable(true);
        Window contactWayDialogWindow = contactWayDialog.getWindow();
        contactWayDialogWindow.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
//        contactWayDialogWindow.setWindowAnimations(R.style.share_dialog_style); // 添加动画

//        mSwipeBackLayout.setEnableGesture(true);

    }

//    @Override
//    public boolean isBarDark() {
//        return false;
//    }
//
//    @Override
//    public boolean isSetStatusBar() {
//        return false;
//    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_company_detail);
        mCompanyMenu = (RecyclerView) findViewById(R.id.company_menu);
        scrollView = (ObserveScrollView) findViewById(R.id.scroll_view);
//        title = (BackAndImageTitleView) findViewById(R.id.title);
//        loadingBack = (BackAndImageTitleView) findViewById(R.id.title_back);
        netWorkError = (NetWorkErrorView) findViewById(R.id.net_work_error);
        riskInfoVide = (CompanyDetailMenuView) findViewById(R.id.view_risk_info);
        operatingConditionsView = (CompanyDetailMenuView) findViewById(R.id.view_operating_conditions);
        intangibleAssetsView = (CompanyDetailMenuView) findViewById(R.id.view_intangible_assets);
        myHeaderView = new MyHeaderView(mContext);

        loadingLayout = (RelativeLayout) findViewById(R.id.loading);
        imageView = (ImageView) findViewById(R.id.loading_img);
        headerView = (CompanyDetailHeaderView) findViewById(R.id.headerview);
        lookReportText = (TextView) findViewById(R.id.text_look_report);
//        vBarEmpty1 = findViewById(R.id.vBarEmpty1);
//        vBarEmpty2 = findViewById(R.id.vBarEmpty2);
//        vTitleParent = findViewById(R.id.vTitleParent);
        vTypeSelect = findViewById(R.id.vTypeSelect);
        tvTypeLabel = (TextView) findViewById(R.id.tvTypeLabel);
        vArrow = findViewById(R.id.vArrow);

        titleView = (TitleView) findViewById(R.id.view_title);
//        if (scrollView.getTop() == 0) {
//            vBarEmpty1.setVisibility(View.VISIBLE);
//            vBarEmpty2.setVisibility(View.VISIBLE);
//        }

//        scrollView.setOnScrollChange(new ObserveScrollView.OnScrollChange() {
//            @Override
//            public void onChange(int l, int t, int oldl, int oldt) {
//
//                if (SCROLLHEIGHT == 0)
//                    SCROLLHEIGHT = vTitleParent.getHeight();
//
//                vTitleParent.setSelected(t > 0);
//                setStatusBarFontDark(vTitleParent.isSelected());
//
////                LogUtil.e("alpha", "t=" + t);
//
//                float alpha = (SCROLLHEIGHT - t) / (float) SCROLLHEIGHT;
////                LogUtil.e("alpha", "alpha=" + alpha);
//                if (alpha < 0)
//                    alpha = 0;
//                vTitleParent.setAlpha(1 - alpha);
//
//                if (t == 0)
//                    vTitleParent.setAlpha(1);
//            }
//        });
    }

    private int SCROLLHEIGHT = 0;

    @Override
    public void initWidgetActions() {
        titleView.setTitle("企业信息");
        titleView.setRightImage(R.drawable.img_send_msg);


        titleView.setRightClickListener(new TitleView.OnRightClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MessageListActivity.class);
                intent.putExtra(Constant.COMPANYID, mCompanyId);
                intent.putExtra(MessageListActivity.TYPE, MessageListActivity.TYPE_SINGLE);
                mContext.startActivity(intent);
            }
        });
        mCompanyMenu.setNestedScrollingEnabled(false);
//        scrollView.setOnScrollChange(new ObserveScrollView.OnScrollChange() {
//            @Override
//            public void onChange(int l, int t, int oldl, int oldt) {
//                if (t > netlib.util.PhoneUtil.dip2px(mContext, 5)) {
//                    title.setTitleAlpha(255);
//                } else {
//                    title.setTitleAlpha((int) (255f * t / netlib.util.PhoneUtil.dip2px(mContext, 5)));
//                }
//
//            }
//        });
//        title.setTitleAlpha(255);

        netWorkError.setListener(new NetWorkErrorView.OnRefreshListener() {
            @Override
            public void OnNetRefresh() {
                loadingLayout.setVisibility(View.VISIBLE);
                if (sceneAnimation == null)
                    sceneAnimation = new SceneAnimation(imageView, 75);
                sceneAnimation.start();
                getCompanyDetail();
            }
        });

        mCompanyMenu.setLayoutManager(new FullyGridLayoutManger(this, 4));
        mCompanyMenu.setAdapter(adapter);
        mCompanyMenu.addItemDecoration(new DividerGridItemDecoration(mContext));
        adapter.setOnItemClickListener(new CompanyMenuAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, String umeng) {

                if (model == null)
                    return;
                Bundle argument = new Bundle();

                argument.putSerializable(CompanyDetailsActivity.COMPANY, model);
                argument.putInt(CompanyDetailsActivity.POSITION, position);
                goActivity(CompanyDetailsActivity.class, argument);
            }
        });

//        headerView.setUpdateListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                MobclickAgent.onEvent(mContext, "Search59");
//                updateCompanyInfo();
//            }
//        });
       /* map_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MobclickAgent.onEvent(mContext, "Businessdetails06");
                if (!isHasLatLng)
                    return;
                if (model != null && !TextUtils.isEmpty(model.getCompanyname())) {
                    Bundle bundle = new Bundle();
                    bundle.putString(BaiduMapActivity.COMPANY_NAME, model.getCompanyname());
                    bundle.putString(BaiduMapActivity.LAT, model.getLatitude());
                    bundle.putString(BaiduMapActivity.LON, model.getLongitude());
                    goActivity(BaiduMapActivity.class, bundle);
                }
            }
        });*/


        contactinList = new ArrayList<>();

//        title.setmFollowListener(new BackAndImageTitleView.FollowListener() {
//            @Override
//            public void onClick() {
//                if (model == null)
//                    return;
//                EventUtils.event(mContext, EventUtils.DETAIL57);
//                putFollowState(model.getIsattention());
//            }
//        });
//
//        title.setmShareListener(new BackAndImageTitleView.ShareListener() {
//            @Override
//            public void onClick() {
//                EventUtils.event(mContext, EventUtils.DETAIL58);
//                showShareDialog();
//                EventUtils.event(mContext, EventUtils.BUSINESSDETAILS03);
//            }
//        });

        /*company_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (model == null)
                    return;
                putFollowState(model.getIsattention());
                MobclickAgent.onEvent(mContext, "Businessdetails05");
            }
        });*/
//        TouchUtil.createTouchDelegate(company_follow, PhoneUtil.dip2px(mContext, 40));
        contactWayDialog.setOnItemtClickListener(new ContactWayDialog.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ContactinFormationModel model = (ContactinFormationModel) parent.getItemAtPosition(position);
                if (model == null)
                    return;
                if (RegularUtils.checkEmail(model.getNumber())) {
                    Log.e(TAG, "email");
                    SystemIntentUtil.goEmail(mContext, model.getNumber(), "", "", null, null);
                } else {
                    String number = RegularUtils.getNumber(model.getNumber());
                        /*if (RegularUtils.checkMobile(number)
                                || RegularUtils.checkPhone(number)) {
                            SystemIntentUtil.goTel(mContext, number, null);
                        }*/
                    SystemIntentUtil.goTel(mContext, number);
                }

                contactWayDialog.dismiss();
            }
        });
//        shareDialog.setShareListener(new ShareDialog.ShareListener() {
//            @Override
//            public void onFriendersShare() {
//                if (model == null)
//                    return;
//                ShareModel shareModel = new ShareModel();
//                if (!TextUtils.isEmpty(model.getCompanyname()))
//                    shareModel.setContent(getString(R.string.share_content_company) + model.getCompanyname());
//                shareModel.setTitle(getString(R.string.share_title_company));
//                if (!TextUtils.isEmpty(model.getShareurl()))
//                    shareModel.setUrl(model.getShareurl());
//                shareUtil.shareByType(mContext, shareModel, SHARE_MEDIA.WEIXIN_CIRCLE);
//            }
//
//            @Override
//            public void onWechatShare() {
//                if (model == null)
//                    return;
//                ShareModel shareModel = new ShareModel();
//                if (!TextUtils.isEmpty(model.getCompanyname()))
//                    shareModel.setContent(getString(R.string.share_content_company) + model.getCompanyname());
//                shareModel.setTitle(getString(R.string.share_title_company));
//                if (!TextUtils.isEmpty(model.getShareurl()))
//                    shareModel.setUrl(model.getShareurl());
//                shareUtil.shareByType(mContext, shareModel, SHARE_MEDIA.WEIXIN);
//            }
//
//            @Override
//            public void onSinaShare() {
//                if (model == null)
//                    return;
//                ShareModel shareModel = new ShareModel();
//                if (!TextUtils.isEmpty(model.getCompanyname()))
//                    shareModel.setContent(getString(R.string.share_content_company) + model.getCompanyname());
//                shareModel.setTitle(getString(R.string.share_title_company));
//                if (!TextUtils.isEmpty(model.getShareurl()))
//                    shareModel.setUrl(model.getShareurl());
//                shareUtil.shareByType(mContext, shareModel, SHARE_MEDIA.SINA);
//            }
//        });

        loadingLayout.setVisibility(View.VISIBLE);
        if (sceneAnimation == null)
            sceneAnimation = new SceneAnimation(imageView, 75);
        sceneAnimation.start();
        getCompanyDetail();
//        loadingBack.setVGone(View.GONE);

//        riskInfoVide.setCallBack(new CompanyDetailMenuView.CallBack() {
//            @Override
//            public void showMenuLoading() {
////                showLoading();
//            }
//
//            @Override
//            public void hideMenuLoading() {
////                hideLoadDialog();
//            }
//        });

//        intangibleAssetsView.setCallBack(new CompanyDetailMenuView.CallBack() {
//            @Override
//            public void showMenuLoading() {
////                showLoading();
//            }
//
//            @Override
//            public void hideMenuLoading() {
////                hideLoadDialog();
//            }
//        });
//
//        operatingConditionsView.setCallBack(new CompanyDetailMenuView.CallBack() {
//            @Override
//            public void showMenuLoading() {
////                showLoading();
//            }
//
//            @Override
//            public void hideMenuLoading() {
////                hideLoadDialog();
//            }
//        });

//        title.setReportClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (model != null) {
//                    EventUtils.event(mContext, EventUtils.BUSINESSDETAILS01);
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("company", model);
//                    goActivity(CompanyAmendActivity.class, bundle);
//                }
//            }
//        });

//        lookReportText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                UserInfoModel model = InquireApplication.getUserInfo();
//                if (model == null) {
//                    goActivity(LoginActivity.class);
//                    return;
//                }
//
//                showLoading();
//                TimeOut timeOut = new TimeOut(mContext);
//                params = new HashMap<>();
//                params.put("entId", mCompanyId);
//
//                params.put("entName", mCompanyName == null ? "" : mCompanyName);
//
//                if (userInfo != null && !TextUtils.isEmpty(userInfo.getUserid()))
//                    params.put("userid", userInfo.getUserid());
//                else {
//                    params.put("userid", "");
//                }
//                params.put("t", timeOut.getParamTimeMollis() + "");
//                params.put("m", timeOut.MD5time() + "");
//                NetWorkCompanyDetails.getReportUrl(CompanyDetailActivity.this, params, getLocalClassName(), new NetWorkCallBack() {
//                    @Override
//                    public void onSuccess(Object data) {
//                        hideLoadDialog();
//                        ReportModel model = (ReportModel) data;
//                        if (((ReportModel) data).getResult() == 0) {
//                            if (model.getData() != null && !TextUtils.isEmpty(model.getData().getReportUrl())) {
//
//                                String cId = "";
//                                if (!TextUtils.isEmpty(mCompanyId)) {
//                                    cId = mCompanyId;
//                                } else {
//                                    if (CompanyDetailActivity.this.model != null && !TextUtils.isEmpty(CompanyDetailActivity.this.model.getCompanyid())) {
//                                        cId = CompanyDetailActivity.this.model.getCompanyid();
//                                    }
//                                }
//
//
//                                WebActivity.startCompanyReportActivity(CompanyDetailActivity.this, model.getData().getReportUrl(), mCompanyName, cId, "1".equals(model.getData().getVipType()));
//                            }
//                        } else {
//                            Toast.makeText(mContext, model.getMsg(), Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//                    @Override
//                    public void onFail(String error) {
//                        Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show();
//                        hideLoadDialog();
//                    }
//                });
////                AppUtil.startReportDetialActivity(CompanyDetailActivity.this,"http://www.baidu.com","企业报告");
//
//            }
//        });

//        updateHandler = new Handler() {
//            @Override
//            public void handleMessage(Message msg) {
//                super.handleMessage(msg);
//                if (msg.what == UPDATE_MSG) {
//                    getUpdateState();
//                }
//            }
//        };

//        findViewById(R.id.vTypeSelect).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new TypeSelectPop(mContext, vArrow, selectType, new TypeSelectPop.OnSelectListener() {
//                    @Override
//                    public void select(int type, String text) {
//                        selectType = type;
//                        tvTypeLabel.setText(text);
//                    }
//                }).showAtLocation(vTypeSelect, Gravity.BOTTOM | Gravity.LEFT, 20, vTypeSelect.getHeight());
//            }
//        });
    }


//    public void showShareDialog() {
//        shareDialog.show();
//        WindowManager windowManager = getWindowManager();
//        Display display = windowManager.getDefaultDisplay();
//        WindowManager.LayoutParams lp = shareDialog.getWindow().getAttributes();
//        lp.width = (int) (display.getWidth()); // 设置宽度
//        shareDialog.getWindow().setAttributes(lp);
//    }
//
//    public void showContactWayDialog() {
//        contactWayDialog.show();
//        WindowManager windowManager = getWindowManager();
//        Display display = windowManager.getDefaultDisplay();
//        WindowManager.LayoutParams lp = contactWayDialog.getWindow().getAttributes();
//        lp.width = (int) (display.getWidth()); // 设置宽度
//        contactWayDialog.getWindow().setAttributes(lp);
//    }

    /**
     * 下载企业详情
     */
    private void getCompanyDetail() {

        // 暂时屏蔽 存在加密逻辑
//        CompanyDetailModel model= new Gson().fromJson(IOUtil.readTextFileFromRawResourceId(mContext, R.raw.company),CompanyDetailModel.class);
//        updateView(model);

        TimeOut timeOut = new TimeOut(mContext);
        params = new HashMap<>();
        params.put("companyId", mCompanyId);
        params.put("entName", mCompanyName == null ? "" : mCompanyName);
        params.put("userId", AppUtils.getUserInfo().id);
        params.put("myLatitude", LocationSharepreferences.getLocation(mContext).lat);
        params.put("myLongitude", LocationSharepreferences.getLocation(mContext).lon);
        params.put("t", timeOut.getParamTimeMollis() + "");
        params.put("m", timeOut.MD5time() + "");
        NetWorkCompanyDetails.getCompanyDetails(mContext, params, getLocalClassName(), new NetWorkCallBack() {
            @Override
            public void onSuccess(Object data) {
                if (data instanceof NetModel) {
                        BankCompanyDetailModel model = ((NetModel) data).dataToObject(BankCompanyDetailModel.class);
                        updateView(model.list);
                }
            }

            @Override
            public void onFail(String error) {
                sceneAnimation.stop();
//                title.setVGone(View.GONE);
                loadingLayout.setVisibility(View.GONE);
                netWorkError.setNetWorkError();
                netWorkError.setVisibility(View.VISIBLE);
            }
        });
    }

    private void updateView(final CompanyDetailModel model) {
        loadingLayout.setVisibility(View.GONE);
        sceneAnimation.stop();
        if(model==null){
            netWorkError.setVisibility(View.VISIBLE);
            return;
        }
        this.model = model;

//        //企业信息无数据时处理
//        if (model.getResult() == 0) {
//            if (model.getHasCompanyData() == 1) {
//                Intent intent = new Intent(mContext, SearchResultActivity.class);
//                intent.putExtra(SearchResultActivity.SEARCH_KEY, mCompanyName);
//                intent.putExtra(SearchResultActivity.SEARCH_TYPE, SearchHistoryItemModel.SEARCH_COMMON);
//                startActivity(intent);
//                finish();
//                return;
//            }
//        }


        if (model.getResult() == 0) {
//            title.setTitleAlpha(0);
//            title.setVGone(View.VISIBLE);


            if (TextUtils.isEmpty(model.getCompanyid())) {
                //企业ID为空，隐藏分享、打电话、关注
                model.setCompanyid(mCompanyId);
            } else {
                if (model.getCompanyphonelist() == null
                        || model.getCompanyphonelist().size() == 0
                        ) {

                } else {
                    contactinList.clear();
                    for (ContactinFormationModel contactinFormationModel : model.getCompanyphonelist()) {
                        if (!TextUtils.isEmpty(contactinFormationModel.getNumber())) {
                            contactinList.add(contactinFormationModel);
                        }
                    }
                    if (contactinList.size() == 0) {
                    }
                }
            }

//            if ("true".equals(model.getIsattention())) {
//                title.setFollow(true);
//            } else {
//                title.setFollow(false);
//            }

//            model.type = companyType;
            model.distance =companyDistance;
            headerView.setInfo(model);
            netWorkError.setVisibility(View.GONE);
            if (!TextUtils.isEmpty(model.getUpdatestate()))
                myHeaderView.setTxt(model.getUpdatestate());

            if (!TextUtils.isEmpty(model.getLatitude()) && !TextUtils.isEmpty(model.getLongitude())) {
                try {
                    Double lat = Double.parseDouble(model.getLatitude());
                    Double log = Double.parseDouble(model.getLongitude());
//                    map_image.setImageURI(BaiduMapUtil.getBaiduMapUrl(new LatLng(log, lat)));
                    isHasLatLng = true;
                } catch (Exception e) {
                    isHasLatLng = false;
                    Log.e(TAG, e.toString());
                }

            } else {
                isHasLatLng = false;
            }
            adapter.refresh(model.getSubclassMenu());
//            riskInfoVide.setData(CompanyDetailMenuView.TYPE_RISKINFO, model, mCompanyId, mCompanyName);
//            operatingConditionsView.setData(CompanyDetailMenuView.TYPE_OPERATINGCONDITIONS, model, mCompanyId, mCompanyName);
//            intangibleAssetsView.setData(CompanyDetailMenuView.TYPE_INTANGIBLEASSETS, model, mCompanyId, mCompanyName);

//            //查看公司详情后，更新“我的关注”公司更新显示
//            HideUpdateEvent event = new HideUpdateEvent();
//            if (!TextUtils.isEmpty(model.getCompanyid())) {
//                event.setCompanyid(model.getCompanyid());
//            } else {
//                event.setCompanyid(mCompanyId);
//            }
//
//            EventBus.getDefault().post(event);

//            UpdateAttentionEvent updateAttentionEvent = new UpdateAttentionEvent();
//            if (!TextUtils.isEmpty(model.getCompanyid())) {
//                updateAttentionEvent.setCompanyId(model.getCompanyid());
//            } else {
//                updateAttentionEvent.setCompanyId(mCompanyId);
//            }
//            updateAttentionEvent.setAttention(model.getAttentioncount());
//            EventBus.getDefault().post(updateAttentionEvent);

//            riskInfoVide.startLoad();
//            operatingConditionsView.startLoad();
//            intangibleAssetsView.startLoad();
        } else {
            netWorkError.setServerError();
            netWorkError.setVisibility(View.VISIBLE);
//            title.setVGone(View.GONE);
            Toast.makeText(mContext, model.getMsg(), Toast.LENGTH_SHORT).show();
        }

    }

//    /**
//     * 关注 or 已关注
//     *
//     * @param followState 现在关注状态
//     */
//    public void putFollowState(final String followState) {
//        followStatue = followState;
//        HashMap<String, String> params = new HashMap<>();
//        if (TextUtils.isEmpty(followState))
//            return;
//        if (userInfo != null && !TextUtils.isEmpty(userInfo.getUserid())) {
//            params.put("userid", userInfo.getUserid());
//        } else {
//            startActivity(new Intent(mContext, LoginActivity.class));
//            return;
//        }
//        if ("true".equals(followState)) {
//            params.put("type", "2");
//        } else {
//            params.put("type", "1");
//        }
//        showLoading();
//        params.put("companyid", model.getCompanyid());
//        NetCompanyFollow.putCompanyFollow(mContext, params, getLocalClassName(), new NetWorkCallBack() {
//            @Override
//            public void onSuccess(Object data) {
//                if (data instanceof FollowModel) {
//                    FollowModel followModel = (FollowModel) data;
//                    updateView(followModel, followState);
//                }
//
//            }
//
//            @Override
//            public void onFail(String error) {
//                hideLoadDialog();
//                Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (sceneAnimation != null)
            sceneAnimation.stop();
//        updateHandler.removeMessages(UPDATE_MSG);


//        VolleyUtil.getQueue(this).cancelAll("CompanyDetailsItemHttp");
    }

    @Override
    public void onEvent(IEvent event) {
        super.onEvent(event);
//        if (event instanceof CompleteLoginEvent) {
//            if (((CompleteLoginEvent) event).getIsLogin() == LoginConstant.REGISTER) {
//                userInfo = InquireApplication.getUserInfo();
//                putFollowState(followStatue);
//            }
//        } else
        if (event instanceof AnimationEndEvent) {
            AnimationEndEvent endEvent = (AnimationEndEvent) event;
            adapter.doAnimate(endEvent.getIndex());
        }
    }

//    public void updateView(FollowModel model, String followState) {
//        hideLoadDialog();
//        if (model.getResult() == 0) {
//            try {
//                FollowSucceedEvent event = new FollowSucceedEvent();
//                int count = 0;
//                if (followState.equals("true")) {
//                    Toast.makeText(mContext, "取消关注成功", Toast.LENGTH_SHORT).show();
//                    title.setFollow(false);
//                    this.model.setIsattention("false");
//                    count = -1;
//                } else {
//                    Toast.makeText(mContext, "关注成功", Toast.LENGTH_SHORT).show();
//                    title.setFollow(true);
//                    this.model.setIsattention("true");
//                    count = 1;
//                }
//                if (userInfo != null) {
//                    int focuscount = Integer.parseInt(userInfo.getMyfocuscount());
//                    focuscount += count;
//                    userInfo.setMyfocuscount(focuscount + "");
//                    event.setCount(focuscount);
//                }
//                EventBus.getDefault().post(event);
//                headerView.setFollow_count(model.getAttentioncount());
//                if (this.model != null)
//                    this.model.setAttentioncount(model.getAttentioncount());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        } else {
//            Toast.makeText(mContext, model.getMsg(), Toast.LENGTH_SHORT).show();
//        }
//    }

//    public void updateCompanyInfo() {
//        if (model == null)
//            return;
//        if (!TextUtils.isEmpty(model.getUpdatestate()))
//            if (model.getUpdatestate().contains("已是最新")) {
//                showToast("已是最新数据");
//                return;
//            }
//
//        myHeaderView.setTxt(getString(R.string.refreshing));
//        headerView.setUpdateText(getString(R.string.refreshing));
//        HashMap<String, String> params = new HashMap<>();
//        if (userInfo != null && !TextUtils.isEmpty(userInfo.getUserid())) {
//            params.put("userId", userInfo.getUserid());
//        } else {
//            params.put("userId", "");
//        }
//
//        params.put("entid", model.getCompanyid());
//        params.put("companyname", mCompanyName == null ? "" : mCompanyName);
//        params.put("entname", mCompanyName == null ? "" : mCompanyName);
//        showLoading();
//        NetWorkCompanyDetails.updateCompanyDetails(mContext, params, getLocalClassName(), new NetWorkCallBack() {
//            @Override
//            public void onSuccess(Object data) {
//                hideLoadDialog();
//                BaseModel baseModel = (BaseModel) data;
//                if (baseModel.getResult() == 0) {
//                    getUpdateState();
////                    Toast.makeText(mContext,"更新成功",Toast.LENGTH_SHORT).show();
////                    headerView.setUpdate();
//                } else {
//                    showToast(baseModel.getMsg());
//                }
//            }
//
//            @Override
//            public void onFail(String error) {
//                hideLoadDialog();
//                showToast(error);
//            }
//        });
////    }

//    private void getUpdateState() {
//
//        TimeOut timeOut = new TimeOut(mContext);
//
//        HashMap<String, String> params = new HashMap<>();
//        if (userInfo != null && !TextUtils.isEmpty(userInfo.getUserid())) {
//            params.put("userId", userInfo.getUserid());
//        } else {
//            params.put("userId", "");
//        }
//        params.put("t", timeOut.getParamTimeMollis() + "");
//        params.put("m", timeOut.MD5time() + "");
//        params.put("entId", model.getCompanyid());
//        params.put("entName", mCompanyName == null ? "" : mCompanyName);
//        NetWorkCompanyDetails.getCompanyUpdateState(this, params, getLocalClassName(), new NetWorkCallBack() {
//            @Override
//            public void onSuccess(Object data) {
//                CompanyDetailModel model = (CompanyDetailModel) data;
//                if (model.getResult() == 0) {
//                    if ("1".equals(model.getCurrentstate())) {
//                        // 更新成功
//                        headerView.setUpdate(model);
//                        updateHandler.removeMessages(UPDATE_MSG);
//                    } else {
//                        // 0 正在更新或者未更新
//                        updateHandler.removeMessages(UPDATE_MSG);
//                        updateHandler.sendEmptyMessageDelayed(UPDATE_MSG, UPDATE_DELAY);
//                    }
//                }
//            }
//
//            @Override
//            public void onFail(String error) {
//                updateHandler.removeMessages(UPDATE_MSG);
//                updateHandler.sendEmptyMessageDelayed(UPDATE_MSG, UPDATE_DELAY);
//            }
//        });
////    }
//
////    @Override
////    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
////        super.onActivityResult(requestCode, resultCode, data);
////        shareUtil.onActivityResult(requestCode, resultCode, data);
//////        presenter.onActivityResult(requestCode, resultCode, data, currentPath);
////
////    }
//


}
