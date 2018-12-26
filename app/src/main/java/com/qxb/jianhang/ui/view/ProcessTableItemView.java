package com.qxb.jianhang.ui.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jusfoun.baselibrary.base.BaseView;
import com.qxb.jianhang.R;
import com.qxb.jianhang.ui.activity.FollowAddActivity;
import com.qxb.jianhang.ui.data.TrackRecordDataModel;

/**
 * @author zhaoyapeng
 * @version create time:2018/12/1014:24
 * @Email zyp@jusfoun.com
 * @Description ${跟进进程表 item}
 */
public class ProcessTableItemView extends BaseView {

    protected View rootView;
    protected TextView textCompany;
    protected ImageView tvStatus;
    protected TextView tvTime;
    protected TextView tvClientLable;
    protected TextView tvClient;
    protected TextView tvClientLablePhone;
    protected TextView tvClientphone;
    protected TextView tvJobLable;
    protected TextView tvJob;
    protected TextView tvDescLabel;
    protected TextView tvDesc;
    protected TextView tvAddress;
    private LinearLayout animLayout, rootLayout;


    private ImageView editImg, arrowBottomImg,iconStatus;

    private TrackRecordDataModel.TrackRecordItemModel model;

    public ProcessTableItemView(Context context) {
        super(context);
    }

    public ProcessTableItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ProcessTableItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initViews() {

        LayoutInflater.from(mContext).inflate(R.layout.item_process_table, this, true);
        animLayout = (LinearLayout) findViewById(R.id.layout_anim);
        rootLayout = (LinearLayout) findViewById(R.id.layout_root);
        editImg = (ImageView) findViewById(R.id.img_edit);
        arrowBottomImg = (ImageView) findViewById(R.id.img_arrow_bottom);
        iconStatus = (ImageView)findViewById(R.id.icon_status);
        initView(this);
    }

    @Override
    protected void initActions() {


        rootLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (animLayout.getVisibility() == GONE) {
                    startOpen();
                } else {
                    startClose();
                }
            }
        });

        editImg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(model!=null&&animLayout.getVisibility()==VISIBLE) {
                    Intent intent = new Intent(mContext, FollowAddActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("model", model);
                    intent.putExtra(FollowAddActivity.COMPANYID, model.companyid);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                }
            }
        });

    }

    private void startOpen() {
        animLayout.setVisibility(VISIBLE);
//        startOpenRotate();
        int widthSpec = MeasureSpec.makeMeasureSpec((1 << 30) - 1, MeasureSpec.AT_MOST);

        int heightSpec = MeasureSpec.makeMeasureSpec((1 << 30) - 1, MeasureSpec.AT_MOST);

        animLayout.measure(widthSpec, heightSpec);
        Log.e("tag", "startOpen=" + animLayout.getMeasuredHeight() + " " + mHieght);
        ValueAnimator animator = ValueAnimator.ofInt(0, mHieght);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (int) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) animLayout.getLayoutParams();
                params.height = value;
                animLayout.setLayoutParams(params);
                arrowBottomImg.setVisibility(VISIBLE);
                editImg.setImageResource(R.drawable.img_edit_genjin);
            }
        });
        animator.start();
    }


    private void startClose() {
//        startCloseRotate();
        int height = animLayout.getHeight();
        Log.e("tag", "animLayout=" + animLayout.getHeight());
        ValueAnimator animator = ValueAnimator.ofInt(height, 1);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (int) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) animLayout.getLayoutParams();
                params.height = value;
                animLayout.setLayoutParams(params);
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                animLayout.setVisibility(GONE);
                editImg.setImageResource(R.drawable.img_down_jiantou);
                arrowBottomImg.setVisibility(GONE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animator.start();
    }

    private int mHieght = 0;

    public void setData(final TrackRecordDataModel.TrackRecordItemModel model, final int position) {
//        if (position == 0) {
//            startOpen();
//        }
        this.model = model;

        tvTime.setText(model.time);
        textCompany.setText(model.companyname);

        switch (model.followState) {
            case 1:
                tvStatus.setImageResource(R.drawable.img_status_dianhuagoutong);
                iconStatus.setImageResource(R.drawable.img_staus_phone);
                break;
            case 2:
                tvStatus.setImageResource(R.drawable.img_status_baifangzhong);
                iconStatus.setImageResource(R.drawable.img_status_visit);
                break;
            case 3:
                tvStatus.setImageResource(R.drawable.img_status_yibaifang);
                iconStatus.setImageResource(R.drawable.img_status_sisited);
                break;
            case 4:
                tvStatus.setImageResource(R.drawable.img_status_hezuojianli);
                iconStatus.setImageResource(R.drawable.img_status_cooperation);
                break;
            case 5:
                tvStatus.setImageResource(R.drawable.img_status_zhengshihezuo);
                iconStatus.setImageResource(R.drawable.img_status_formal);
                break;
        }

        tvClient.setText(model.name);
        tvJob.setText(model.job);
        tvDesc.setText(model.desc);
        tvAddress.setText(model.address + " " + model.detailAddress);
        tvClientphone.setText(model.phone);


        this.post(new Runnable() {
            @Override
            public void run() {
                mHieght = animLayout.getHeight();
                if (position == 0) {
                    startOpen();
                } else {
                    animLayout.setVisibility(GONE);
                }
            }
        });
    }


    private void initView(View rootView) {
        textCompany = (TextView) rootView.findViewById(R.id.text_company);
        tvStatus = (ImageView) rootView.findViewById(R.id.tvStatus);
        tvTime = (TextView) rootView.findViewById(R.id.tvTime);
        tvClientLable = (TextView) rootView.findViewById(R.id.tvClientLable);
        tvClient = (TextView) rootView.findViewById(R.id.tvClient);
        tvClientLablePhone = (TextView) rootView.findViewById(R.id.tvClientLablePhone);
        tvClientphone = (TextView) rootView.findViewById(R.id.tvClientphone);
        tvJobLable = (TextView) rootView.findViewById(R.id.tvJobLable);
        tvJob = (TextView) rootView.findViewById(R.id.tvJob);
        tvDescLabel = (TextView) rootView.findViewById(R.id.tvDescLabel);
        tvDesc = (TextView) rootView.findViewById(R.id.tvDesc);
        tvAddress = (TextView) rootView.findViewById(R.id.tvAddress);
    }
}
