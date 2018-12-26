package com.jusfoun.jusfouninquire.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;


import com.jusfoun.baselibrary.Util.SystemIntentUtil;
import com.jusfoun.jusfouninquire.TimeOut;
import com.qxb.jianhang.R;
import com.jusfoun.jusfouninquire.util.WebUtil;
import com.jusfoun.jusfouninquire.activity.CompanyDetailActivity;
import com.jusfoun.jusfouninquire.animation.SceneAnimation;
import com.qxb.jianhang.ui.view.NetWorkErrorView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.impl.client.DefaultHttpClient;

import java.util.Set;

/**
 * Author  JUSFOUN
 * CreateDate 2015/11/27.
 * Description
 */
public class CustomWebViewClent extends WebViewClient {

    private static final String COMPANY_INFO = "companyinfo";
    private static final String COMPANY_HEAD = "companyinfo://";
    private static final String MD5_ENCRYPT_URL = "md5encryption://parameter";
    private static final String DETAILS = "details";
    private static final String SHAREHOLDER = "shareholder";
    private static final String USERCENTER = "usercenter";
    private View web, loadingView;
    private NetWorkErrorView webError;
    private Context mContext;
    private boolean isError;
    private SceneAnimation sceneAnimation;
    private String mainUrl;
    private boolean isFinish;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 404) {
                web.setVisibility(View.GONE);
                webError.setServerError();
                webError.setVisibility(View.VISIBLE);
            } else if (msg.what == 1) {
                stop();
                web.setVisibility(View.VISIBLE);
                webError.setVisibility(View.GONE);
            }
        }
    };

    public CustomWebViewClent(View web, NetWorkErrorView webError, final Context mContext, View loadingView) {
        this.web = web;
        this.webError = webError;
        this.mContext = mContext;
        this.loadingView = loadingView;
        isError = false;
        initDialog();
        showLoading();

    }

    public String getMainUrl() {
        return mainUrl;
    }

    public void setMainUrl(String mainUrl) {
        this.mainUrl = mainUrl;
    }

   /* @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        if (request!=null) {
            Log.e("intercept1", request.getRequestHeaders().toString());
        }else {
            Log.e("intercept1", "null");
        }
        return super.shouldInterceptRequest(view, request);
    }


    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
        Log.e("intercept2",url);
        return super.shouldInterceptRequest(view, url);
    }*/


    @Override
    public boolean shouldOverrideUrlLoading(WebView view, final String url) {
        Log.e("tag", "shouldOverrideUrlLoading-url=" + url);
        Uri uri = null;
        try {
            // url = URLDecoder.decode(url, "utf-8");
            uri = Uri.parse(url);
            Log.e("url", url);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (url.startsWith("tel:")) {
            SystemIntentUtil.goTel(mContext, url);
            return true;
        } else if (url.startsWith("md5encryption://parameter")) {
            try {
                String functionName = "encryption";
                Set<String> parameter = uri.getQueryParameterNames();

                if (parameter != null && parameter.size() > 0 && parameter.contains("fnName")) {
                    if (!TextUtils.isEmpty(uri.getQueryParameter("fnName"))) {
                        functionName = uri.getQueryParameter("fnName");
                    }
                }
                TimeOut timeOut = new TimeOut(mContext);
                view.loadUrl("javascript:" + functionName + "('" + timeOut.getParamTimeMollis() + "', '" + timeOut.MD5time() + "')");
            } catch (Exception e) {
                Log.d("TAG", e.toString());
                e.printStackTrace();
            }
            return true;
        } else if (url.contains("nbxx_detail")) {
            WebUtil.startDetialActivity(mContext, url, "年报详情");
            return true;
        }

        if (uri != null && !TextUtils.isEmpty(uri.getScheme()) && COMPANY_INFO.equals(uri.getScheme())) {
            String companyid = "", companyname = "";
            String temp = url.substring(COMPANY_HEAD.length(), url.length());
            String value[] = temp.split("&");
            if (value != null && value.length == 2) {
                String ids[] = value[0].split("=");
                if (ids != null && ids.length == 2) {
                    companyid = ids[1];
                }
                String names[] = value[1].split("=");
                if (names != null && names.length == 2) {
                    companyname = names[1];
                }
            }
            Bundle bundle = new Bundle();
            bundle.putString(CompanyDetailActivity.COMPANY_ID, companyid);
            bundle.putString(CompanyDetailActivity.COMPANY_NAME, companyname);
            goActivity(CompanyDetailActivity.class, bundle);
            return true;
        } else if (uri != null && !TextUtils.isEmpty(uri.getScheme()) && DETAILS.equals(uri.getScheme())) {
            String title = uri.getQueryParameter("title");
            String articleUrl = uri.getQueryParameter("url");
            if (!TextUtils.isEmpty(articleUrl))
                WebUtil.startDetialActivity(mContext, articleUrl, title);
            return true;
        } else if (uri != null && !TextUtils.isEmpty(uri.getScheme()) && SHAREHOLDER.equals(uri.getScheme())) {
            String shareholdertype = uri.getQueryParameter("shareholdertype");
            String name = uri.getQueryParameter("name");
            String companyid = uri.getQueryParameter("companyid");
            if (shareholdertype != null && shareholdertype.equals("2") && (!TextUtils.isEmpty(name) || !TextUtils.isEmpty(companyid))) {
                // 跳转企业 企业名称或者股东名称 至少一个不为空
                Bundle bundle = new Bundle();
                bundle.putString(CompanyDetailActivity.COMPANY_ID, companyid);
                bundle.putString(CompanyDetailActivity.COMPANY_NAME, name);
                goActivity(CompanyDetailActivity.class, bundle);
            }
//            else if (shareholdertype != null && shareholdertype.equals("1") && !TextUtils.isEmpty(name)) {
//                // 跳转股东高管搜索  名称不能为空
//                Intent intent = new Intent(mContext, SearchResultActivity.class);
//                intent.putExtra(SearchResultActivity.SEARCH_KEY, name);
//                intent.putExtra(SearchResultActivity.SEARCH_TYPE, SearchHistoryItemModel.SEARCH_SHAREHOLDER);
//                mContext.startActivity(intent);
//            }

            return true;
        }
//        else if (uri != null && !TextUtils.isEmpty(uri.getScheme()) && USERCENTER.equals(uri.getScheme())) {
//            if (url.contains("upgradeVip")) {
//                new AgreementDialog((BaseInquireActivity) mContext, new AgreementDialog.OnSelectListener() {
//                    @Override
//                    public void select() {
//                        boolean isContact = url.contains("upgradeVipPhone");// 企业通讯录
//                        int type = 0;
//                        if (url.contains("upgradeVipShareholder"))// 股东穿透
//                            type = 1;
//                        balipayUtil.getPayOrder(mContext, !isContact, type);
//                    }
//
//                }).show();
//            } else if (url.contains("renewVip")) {
//                boolean isContact = url.contains("renewVipPhone");// 企业通讯录
//                int type = 0;
//                if (url.contains("renewVipShareholder"))// 股东穿透
//                    type = 1;
//                balipayUtil.showRenewDialog(!isContact, type);
//            }
//        }

        if (url.startsWith("http://") || url.startsWith("https://") || url.startsWith("www")
                || url.startsWith("HTTP://") || url.startsWith("HTTPS://") || url.startsWith("WWW")) {
            view.loadUrl(url);
            showLoading();
            return true;
        }
//        showLoading();
        return true;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        isError = false;
        isFinish = false;
        /*try {
            url = URLDecoder.decode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }*/
        super.onPageStarted(view, url, favicon);
        Log.e("onWeb", "onPageStarted");
        if (onWebLisener != null) {
            onWebLisener.onPageStart(url, view);
        }
        final String finalUrl = url;
        Log.e("onWeb", "finalUrl===" + finalUrl);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = finalUrl;

                if (finalUrl.startsWith("http://") || finalUrl.startsWith("https://") || finalUrl.startsWith("www")
                        || finalUrl.startsWith("HTTP://") || finalUrl.startsWith("HTTPS://") || finalUrl.startsWith("WWW")) {
                    if (getRespStatus(finalUrl) == 404 || getRespStatus(finalUrl) == 500) {
                        msg.what = 404;
                        handler.sendMessage(msg);
                    }
                }
            }
        }).start();
    }

    @Override
    public void onLoadResource(WebView view, String url) {
        super.onLoadResource(view, url);
        //Log.e("onWeb", "onLoadResource");
        if (isFinish) {
            if (!isError) {
                stop();
                handler.removeMessages(1);
                web.setVisibility(View.VISIBLE);
                webError.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        Log.e("onWeb", "onPageFinished");
        super.onPageFinished(view, url);
        isFinish = true;
       /* try {
            url = URLDecoder.decode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }*/

        if (!isError) {
//            Log.d("TAG", "mainUrl=====" + mainUrl + "\nurl====" + url);
//            if (WebUtil.getUrlProtocol(mainUrl, url)) {
//                view.clearHistory();
//            }
            handler.sendEmptyMessageDelayed(1, 500);
        }
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        stop();
        Log.e("onWeb", "onReceivedError " + description);
        super.onReceivedError(view, errorCode, description, failingUrl);
        isError = true;
        webError.setNetWorkError();
        web.setVisibility(View.GONE);
        webError.setVisibility(View.VISIBLE);
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        stop();
        isError = true;
        webError.setNetWorkError();
        web.setVisibility(View.GONE);
        webError.setVisibility(View.VISIBLE);
        super.onReceivedSslError(view, handler, error);
        handler.proceed();
    }

    private void goActivity(Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(mContext, clazz);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }

    protected void initDialog() {
        ImageView imageView = (ImageView) loadingView.findViewById(R.id.loading_img);
        sceneAnimation = new SceneAnimation(imageView, 75);
    }

    public void showLoading() {
        if (loadingView != null) {
            loadingView.setVisibility(View.VISIBLE);
        }
        if (sceneAnimation != null && sceneAnimation.getIsStop()) {
            sceneAnimation.start();
        }

        web.setVisibility(View.GONE);
    }

    // 隐藏加载对话框
    protected void stop() {
        if (sceneAnimation != null) {
            sceneAnimation.stop();
        }
        if (loadingView != null) {
            loadingView.setVisibility(View.GONE);
        }
    }

    private OnWebLisener onWebLisener;

    public void setOnWebLisener(OnWebLisener onWebLisener) {
        this.onWebLisener = onWebLisener;
    }

    public interface OnWebLisener {
        public void onError(String url, int errorCode);

        public void onPageStart(String url, WebView webView);

    }

    private int getRespStatus(String url) {
        int status = -1;
        try {
            HttpHead head = new HttpHead(url);
            HttpClient client = new DefaultHttpClient();
            HttpResponse resp = client.execute(head);
            status = resp.getStatusLine().getStatusCode();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return status;
    }

}
