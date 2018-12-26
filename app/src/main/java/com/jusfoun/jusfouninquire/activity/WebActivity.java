package com.jusfoun.jusfouninquire.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.DownloadListener;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.jusfoun.baselibrary.event.IEvent;
import com.qxb.jianhang.R;
import com.jusfoun.jusfouninquire.bean.AdItemModel;
import com.jusfoun.jusfouninquire.util.WebUtil;
import com.jusfoun.jusfouninquire.view.CustomWebViewClent;
import com.jusfoun.jusfouninquire.view.TitleView;
import com.qxb.jianhang.ui.view.NetWorkErrorView;
import com.readystatesoftware.systembartint.SystemBarTintManager;


/**
 * Author  JUSFOUN
 * CreateDate 2015/11/17.
 * Description
 */
public class WebActivity extends BaseInquireActivity {

    public static final String TITLE = "title";
    public static final String URL = "url";
    public static final String SHAREDATA = "share_data";
    public static final String IS_LOAD_DETAIL_DATA = "is_load_detail_data";
    public static final String JS_DATA = "js_data";
    private WebView webView;
    private TitleView titleView;
    private String url, title;
    private AdItemModel mShareData;
    private LinearLayout loading;

    private boolean isLoadDetailData;// 页面加载完成需要调用js方法setDetailsData


//    分享相关
//    private ShareUtil shareUtil;
//    private ShareDialog shareDialog;

    private NetWorkErrorView mNetWorkError;
    private CustomWebViewClent webViewClient;


    private boolean goHome = false;

    @Override
    protected void initData() {
        super.initData();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            url = bundle.getString(URL);
            title = bundle.getString(TITLE);
            mShareData = (AdItemModel) bundle.getSerializable(SHAREDATA);
            goHome = bundle.getBoolean("goHome", false);
            isLoadDetailData = bundle.getBoolean(IS_LOAD_DETAIL_DATA, false);
        }

        if (mShareData != null) {
//            initShare();
        }
        Log.e("tag", "yrl=" + url);
//        mSwipeBackLayout.setEnableGesture(true);
    }

    @Override
    public boolean isSetStatusBar() {
        if (getIntent().getBooleanExtra("isPersonCenter", false)) {
            return false;
        }
        return true;
    }

    @Override
    public boolean isBarDark() {
        return !getIntent().getBooleanExtra("isPersonCenter", false);
    }

    @Override
    protected void initView() {
        if (getIntent().getBooleanExtra("isPersonCenter", false))
            setContentView(R.layout.activity_web2);
        else
            setContentView(R.layout.activity_web);
        webView = (WebView) findViewById(R.id.web);
        mNetWorkError = (NetWorkErrorView) findViewById(R.id.net_work_error);
        titleView = (TitleView) findViewById(R.id.web_title);

        if (getIntent().getBooleanExtra("isPersonCenter", false)) {
            titleView.setLeftImage(R.drawable.img_back_white);
            setStatusBarEnable(Color.TRANSPARENT);
        }
        loading = (LinearLayout) findViewById(R.id.loading);
        initWebView();

    }

    @Override
    protected void initWidgetActions() {

        if ("搜索小贴士".equals(title) || "常见问题".equals(title)) {
//            titleView.setLeftSrc(R.mipmap.close_icon);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

//        if ("企业报告".equals(title)) {
//            titleView.setRightImage(R.mipmap.img_ig);
//            titleView.setRightClickListener(new TitleView.OnRightClickListener() {
//                @Override
//                public void onClick(View v) {
//                    UserInfoModel model = InquireApplication.getUserInfo();
//                    if (model == null) {
//                        goActivity(LoginActivity.class);
//                        return;
//                    }
//
//                    if (!TextUtils.isEmpty(model.getUserid()))
//                        getUserInfoFromNet(model.getUserid());
//
//                }
//            });
//        }


        if (getIntent().getBooleanExtra("isPersonCenter", false)) {

            titleView.setTitle("");
            titleView.setBackgroundTranlate();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                setTranslucentStatus(true);
                SystemBarTintManager tintManager = new SystemBarTintManager(this);
                tintManager.setStatusBarTintEnabled(true);
                tintManager.setStatusBarTintResource(android.R.color.transparent);
//            tintManager.setStatusBarTintResource(0);
                SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
                int statusBarHeight = config.getStatusBarHeight();

                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) titleView.getLayoutParams();
                lp.setMargins(0, statusBarHeight, 0, 0);
//                titleView.setPadding(0, statusBarHeight, 0, 0);
//                padding = config.getStatusBarHeight();
//                homeTopView.setPadding(padding);
                Log.e("tag", "底部虚拟=" + config.getPixelInsetBottom());
            }
        }


//        //处理运营活动的分享逻辑
//        if (mShareData != null) {
//            titleView.setRightImage(R.mipmap.will_share);
//            titleView.setRightClickListener(new TitleView.OnRightClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    showShareDialog();
//                }
//            });
//        }


        if (!TextUtils.isEmpty(url))
            webView.loadUrl(url);
        if (!TextUtils.isEmpty(title))
            titleView.setTitle(title);
        mNetWorkError.setListener(new NetWorkErrorView.OnRefreshListener() {
            @Override
            public void OnNetRefresh() {
                webViewClient.showLoading();
                webView.reload();
            }
        });
        titleView.setLeftClickListener(new TitleView.OnLeftClickListener() {
            @Override
            public void onClick(View v) {
                if (webView.canGoBack()) {
//                    webView.goBack();
                    finish();
                } else {
//                    if (goHome) {
//                        Intent intent = new Intent(mContext, MainActivity.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(intent);
//                    }

                    finish();
                }
            }
        });


//        if (shareDialog != null) {
//            //如果分享内容为空， content + “” 设置分享出去的显示内容为 “null”
//            shareDialog.setShareListener(new ShareDialog.ShareListener() {
//                @Override
//                public void onFriendersShare() {
//                    if (mShareData == null)
//                        return;
//                    ShareModel shareModel = new ShareModel();
//
//                    shareModel.setTitle(mShareData.getTitle());
//                    shareModel.setContent(mShareData.getDescribe() + "");
//                    if (!TextUtils.isEmpty(mShareData.getReUrl())) {
//                        shareModel.setUrl(mShareData.getReUrl());
//                    }
//                    if (!TextUtils.isEmpty(mShareData.getImgUrl())) {
//                        shareModel.setImage(mShareData.getImgUrl());
//                    }
//                    shareUtil.shareByType(mContext, shareModel, SHARE_MEDIA.WEIXIN_CIRCLE);
//                }
//
//                @Override
//                public void onWechatShare() {
//                    if (mShareData == null)
//                        return;
//                    ShareModel shareModel = new ShareModel();
//                    shareModel.setTitle(mShareData.getTitle());
//                    shareModel.setContent(mShareData.getDescribe() + "");
//                    if (!TextUtils.isEmpty(mShareData.getReUrl())) {
//                        shareModel.setUrl(mShareData.getReUrl());
//                    }
//                    shareUtil.shareByType(mContext, shareModel, SHARE_MEDIA.WEIXIN);
//                }
//
//                @Override
//                public void onSinaShare() {
//                    if (mShareData == null)
//                        return;
//                    ShareModel shareModel = new ShareModel();
//                    shareModel.setTitle(mShareData.getTitle());
//                    shareModel.setContent(mShareData.getDescribe() + "");
//                    if (!TextUtils.isEmpty(mShareData.getReUrl())) {
//                        shareModel.setUrl(mShareData.getReUrl());
//                    }
//                    shareUtil.shareByType(mContext, shareModel, SHARE_MEDIA.SINA);
//                }
//            });
//        }

        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {


                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });


    }

    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

//
//    /**
//     * 设置分享对话框样式并显示
//     */
//    private void showShareDialog() {
//        shareDialog.show();
//        WindowManager windowManager = getWindowManager();
//        Display display = windowManager.getDefaultDisplay();
//        WindowManager.LayoutParams lp = shareDialog.getWindow().getAttributes();
//        lp.width = (int) (display.getWidth()); // 设置宽度
//        shareDialog.getWindow().setAttributes(lp);
//
//
//    }
//
//    /**
//     * 初始化分享相关组件
//     */
//    private void initShare() {
//        shareUtil = new ShareUtil(mContext);
//        shareDialog = new ShareDialog(mContext, R.style.tool_dialog);
//        shareDialog.setCancelable(true);
//        Window shareWindow = shareDialog.getWindow();
//        shareWindow.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
//        shareWindow.setWindowAnimations(R.style.share_dialog_style); // 添加动画
//
//    }

    private void initWebView() {

        webViewClient = new CustomWebViewClent(webView, mNetWorkError, mContext, loading) {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (isLoadDetailData) {

                    Log.e("tag", "setDetailsData=" + getIntent().getStringExtra(JS_DATA));
                    webView.loadUrl("javascript:setDetailsData(" + getIntent().getStringExtra(JS_DATA) + ")");
                }
            }
        };
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setGeolocationEnabled(true);
        webView.getSettings().setAppCacheEnabled(false);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setSaveFormData(true);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        WebUtil.getUserAgentString(webView.getSettings(), mContext);
        webView.setWebViewClient(webViewClient);
        webView.setWebChromeClient(chromeClient);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
    }

    WebChromeClient chromeClient = new WebChromeClient() {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            return super.onJsAlert(view, url, message, result);
        }

    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) {
                Log.e("tag", "setmLeftClickListener2");
                webView.goBack();
            } else {
                Log.e("tag", "setmLeftClickListener3");
                finish();
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onEvent(IEvent event) {
        super.onEvent(event);
//        if (event instanceof PaySuccessEvent) {
//            if (!TextUtils.isEmpty(url))
//                webView.loadUrl(url);
//        }
    }

    /**
     * 获取个人信息
     *
     * @param userid
     */
//    private void getUserInfoFromNet(String userid) {
//        showLoading();
//        HashMap<String, String> map = new HashMap<String, String>();
//        map.put("userid", userid);
//
//        LoginRegisterHelper.doNetGetUserAllInfo(mContext, map, ((Activity) mContext).getLocalClassName(), new NetWorkCallBack() {
//            @Override
//            public void onSuccess(Object data) {
//                hideLoadDialog();
//                LoginModel model = (LoginModel) data;
//                if (model.getResult() == 0) {
//                    LoginSharePreference.saveUserInfo(model.getUserinfo(), mContext);
//                    InquireApplication.setUserInfo(model.getUserinfo());
//
//
//                    if (model.getUserinfo() != null && model.getUserinfo().vipType == 1)
//                        new EmailSendDialog(WebActivity.this, getIntent().getStringExtra("entName"), getIntent().getStringExtra("entId"),EmailSendDialog.TYPE_REPORT_ONE).show();
//                    else {
//                        WebActivity.startVipPage(WebActivity.this);
//                    }
//                } else {
//                    showToast(model.getMsg() + "");
//                }
//            }
//
//            @Override
//            public void onFail(String error) {
//                hideLoadDialog();
//                showToast(error);
//            }
//        });
//
//    }
}
