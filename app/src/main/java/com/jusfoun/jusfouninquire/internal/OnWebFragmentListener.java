package com.jusfoun.jusfouninquire.internal;

import android.support.v4.app.Fragment;
import android.webkit.WebView;

/**
 * Author  JUSFOUN
 * CreateDate 2015/12/7.
 * Description
 */
public interface OnWebFragmentListener {
    public void onCurrentFragment(Fragment fragment);

    public void onWebViewStart(String url, WebView view);
}
