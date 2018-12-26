package com.qxb.jianhang.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.qxb.jianhang.R;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.indicator.PtrIndicator;


/**
 * @author zhaoyapeng
 * @version create time:15/11/29下午6:31
 * @Email zyp@jusfoun.com
 * @Description ${TODO}
 */
public class MyHeaderView extends FrameLayout implements PtrUIHandler {
    private Context mContext;
    private ImageView image;
    private PPTVLoading loadingView;
    private TextView textView;

    public MyHeaderView(Context context) {
        super(context);
        mContext = context;
        initView();
        initActions();
    }

    public MyHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
        initActions();
    }

    public MyHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
        initActions();
    }


    private void initView() {
        LayoutInflater.from(mContext).inflate(R.layout.head_view_layout, this, true);
        image = (ImageView) findViewById(R.id.image);
        loadingView = (PPTVLoading) findViewById(R.id.view_loading);
        textView= (TextView) findViewById(R.id.txt);
    }

    private void initActions() {

    }

    public void setTxt(String txt){
        textView.setText(txt);
    }

    @Override
    public void onUIReset(PtrFrameLayout frame) {
        loadingView.reSet();
    }

    @Override
    public void onUIRefreshPrepare(PtrFrameLayout frame) {

    }

    @Override
    public void onUIRefreshBegin(PtrFrameLayout frame) {

        loadingView.startAnim();
    }

    @Override
    public void onUIRefreshComplete(PtrFrameLayout frame) {

        loadingView.reSet();
    }

    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {


        final int mOffsetToRefresh = frame.getOffsetToRefresh();
        final int currentPos = ptrIndicator.getCurrentPosY();
        final int lastPos = ptrIndicator.getLastPosY();
//
      /*  Log.e("tag", "MyHeaderView-currentPos=" + currentPos);
        Log.e("tag", "MyHeaderView-mOffsetToRefresh=" + mOffsetToRefresh);
        Log.e("tag", "MyHeaderView-getRatioOfHeaderToHeightRefresh=" + frame.getRatioOfHeaderToHeightRefresh());*/

        if(currentPos==0){
            loadingView.reSet();
        }

        if (currentPos < mOffsetToRefresh) {
            float offset = currentPos / (mOffsetToRefresh*1.0f);
          //  Log.e("tag", "--------1");
            loadingView.setOffset(offset, true);
        } else {
            loadingView.setOffset(1f, false);  //Log.e("tag", "--------2");
        }
//
//        if (currentPos < mOffsetToRefresh && lastPos >= mOffsetToRefresh) {
//            if (isUnderTouch && status == PtrFrameLayout.PTR_STATUS_PREPARE) {
//
//            }
//        } else if (currentPos > mOffsetToRefresh && lastPos <= mOffsetToRefresh) {
//            if (isUnderTouch && status == PtrFrameLayout.PTR_STATUS_PREPARE) {
//
//
//            }
//        }
    }
}
