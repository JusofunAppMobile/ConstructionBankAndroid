package com.jusfoun.jusfouninquire.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.PopupWindow;

import com.qxb.jianhang.R;
import com.qxb.jianhang.ui.adapter.MenuAdapter;
import com.qxb.jianhang.ui.data.CompanyDetailMenuModel;

import java.util.List;

/**
 * Author  JUSFOUN
 * CreateDate 2015/11/9.
 * Description
 */
public class MenuWindow extends PopupWindow {

    private GridView gridView;
    private MenuAdapter adapter;
    private Animation mHideAction, mShowAction;
    public MenuWindow(Context context){
        super(context);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setBackgroundDrawable(context.getResources()
                .getDrawable(android.R.color.transparent));


        gridView=new GridView(context);
        adapter=new MenuAdapter(context);
        gridView.setAdapter(adapter);
        gridView.setNumColumns(3);
       /* gridView.setHorizontalSpacing(1);
        gridView.setVerticalSpacing(1);*/
        gridView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        gridView.setBackgroundColor(context.getResources().getColor(R.color.white));
        mHideAction = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, -1.0f);

        mHideAction.setDuration(500);

        mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, -1.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
        mShowAction.setDuration(500);
        setContentView(gridView);
    }

    public void setWindowShow() {
        gridView.startAnimation(mShowAction);
        gridView.setVisibility(View.VISIBLE);
    }

    public void setWindowHide() {
        mHideAction.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                gridView.setVisibility(View.GONE);
                dismiss();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        gridView.startAnimation(mHideAction);

    }

    public void setCheckCount(int checkCount){
        adapter.setCheckCount(checkCount);
    }

    public void updateView(List<CompanyDetailMenuModel> strings){
        adapter.refresh(strings);
    }

    public void setOnClickListener(MenuAdapter.OnClickListener onClickListener){
        adapter.setOnClickListener(onClickListener);
    }
}
