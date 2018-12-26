package com.jusfoun.jusfouninquire.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


/**
 * @author zhaoyapeng
 * @version create time:15/10/30上午10:02
 * @Email zyp@jusfoun.com
 * @Description ${所有 fragment 基类}
 */
public abstract class BaseFragment extends Fragment {

    protected Context mContext;
    protected String TAG;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG=this.getClass().getSimpleName();
        mContext = getActivity();
    }

    protected abstract void initData();

    protected abstract View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    protected abstract void initWeightActions();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initData();
        View view = initViews(inflater, container, savedInstanceState);
        initWeightActions();
        return view;
    }

    protected void goActivity(Class<?> clazz) {
        Intent intent = new Intent(getActivity(), clazz);
        startActivity(intent);
    }

    /**
     * 显示toast
     */
    protected void showToast(String msg) {
        if (msg != null && !msg.equals("")) {
            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
