package com.qxb.jianhang.ui.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jusfoun.baselibrary.net.NetModel;
import com.jusfoun.jusfouninquire.activity.WebActivity;
import com.jusfoun.jusfouninquire.util.VolleyUtil;
import com.jusfoun.jusfouninquire.volley.VolleyPostRequest;
import com.qxb.jianhang.R;
import com.qxb.jianhang.ui.activity.FollowUpRecordActivity;
import com.qxb.jianhang.ui.constant.Constant;
import com.qxb.jianhang.ui.data.CompanyDetailModel;
import com.qxb.jianhang.ui.util.AppUtils;

import java.util.HashMap;
import java.util.Map;


/**
 * Author  wangchenchen
 * CreateDate 2016/8/10.
 * Email wcc@jusfoun.com
 * Description 企业详情
 */
public class CompanyDetailHeaderView extends LinearLayout {
    private TextView company_name, address_content, phone_content, website_content, locationText, stateText;

    private RelativeLayout address_layout, phone_layout, website_layout;
    private LinearLayout bgLayout;
    private TextView tagText;


    private CompanyDetailModel model;
    private Context context;

    private LinearLayout locationWebLayout;
    private ImageView jtImg;

    public CompanyDetailHeaderView(Context context) {
        super(context);
        initView(context);
        initAction();
    }

    public CompanyDetailHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        initAction();
    }

    public CompanyDetailHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        initAction();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CompanyDetailHeaderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
        initAction();
    }

    private void initView(Context context) {
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.layout_company_header_view, this, true);
        company_name = (TextView) findViewById(R.id.company_name);

        address_content = (TextView) findViewById(R.id.address_content);
        phone_content = (TextView) findViewById(R.id.phone_content);
        website_content = (TextView) findViewById(R.id.website_content);
        address_layout = (RelativeLayout) findViewById(R.id.address_layout);
        phone_layout = (RelativeLayout) findViewById(R.id.phone_layout);
        website_layout = (RelativeLayout) findViewById(R.id.website_layout);
        locationWebLayout = (LinearLayout) findViewById(R.id.layout_location_website);

        jtImg = (ImageView) findViewById(R.id.img_jiantou);
        locationText = (TextView) findViewById(R.id.text_company_location);
        stateText = (TextView) findViewById(R.id.text_company_state);


        bgLayout = (LinearLayout) findViewById(R.id.layout_bg);
        tagText = (TextView) findViewById(R.id.text_tag);


    }

    private void initAction() {

        phone_content.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (model.getCompanyphonelist() != null && model.getCompanyphonelist().size() > 0) {
                    new PhoneCallDialog(getContext(), phone_content.getText().toString()).show();
                }
            }
        });


        phone_layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (model == null)
                    return;
                if (model.getCompanyphonelist() != null && model.getCompanyphonelist().size() > 0) {
//                    EventUtils.event(context, EventUtils.DETAIL61);

                    new PhoneCallDialog(getContext(), model.getCompanyphonelist().get(0).getNumber()).show();
                }
            }
        });

        website_layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (model == null)
                    return;
                if (model.getNeturl() != null && model.getNeturl().size() > 0) {
                    Intent intent = new Intent(context, WebActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(WebActivity.TITLE, "");
                    if (model.getNeturl().get(0).getUrl().startsWith("http://")) {
                        bundle.putString(WebActivity.URL, model.getNeturl().get(0).getUrl());
                    } else {
                        bundle.putString(WebActivity.URL, "http://" + model.getNeturl().get(0).getUrl());
                    }
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            }
        });

        address_layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (model == null)
                    return;

                try {

//                    EventUtils.event(context, EventUtils.DETAIL60);
//                    Double lat = Double.parseDouble(model.getLatitude());
//                    Double lon = Double.parseDouble(model.getLongitude());
//                    Intent intent = new Intent(context, BaiduMapActivity.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putString(BaiduMapActivity.COMPANY_NAME, model.getCompanyname());
//                    bundle.putString(BaiduMapActivity.LAT, model.getLatitude());
//                    bundle.putString(BaiduMapActivity.LON, model.getLongitude());
//                    intent.putExtras(bundle);
//                    context.startActivity(intent);
                } catch (Exception e) {

                }
            }
        });
        phone_layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (locationWebLayout.getVisibility() == View.GONE) {
                    startOpen();
                } else {
                    startClose();
                }
            }
        });


        tagText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if("2".equals(model.type)||"3".equals(model.type)){
                    Intent intent = new Intent(context, FollowUpRecordActivity.class);
                    intent.putExtra(Constant.COMPANYID, model.getCompanyid());
                    context.startActivity(intent);
                }else{
                    addTag();
                }

            }
        });
    }


    public void setInfo(CompanyDetailModel model) {
        this.model = model;
        company_name.setText(model.getCompanyname());

        locationText.setText(model.getAddress());


        if (TextUtils.isEmpty(model.getAddress())) {
            address_content.setText("未公布");
        } else {
            address_content.setText(model.getAddress());
        }
        if (model.getCompanyphonelist() != null && model.getCompanyphonelist().size() > 0) {
            phone_content.setText(model.getCompanyphonelist().get(0).getNumber());
//            phone_content.setTextColor(Color.parseColor("#ff8d57"));
        } else {
            phone_content.setText("企业选择不公示");
//            phone_content.setTextColor(Color.parseColor("#999999"));
        }
        if (model.getNeturl() != null && model.getNeturl().size() > 0) {
            website_content.setText(model.getNeturl().get(0).getUrl());
//            website_content.setTextColor(Color.parseColor("#ff8d57"));

        } else {
            website_content.setText("企业选择不公示");
//            website_content.setTextColor(Color.parseColor("#999999"));

        }

        try {
            Double lat = Double.parseDouble(model.getLatitude());
            Double lon = Double.parseDouble(model.getLongitude());
            address_content.setTextColor(Color.parseColor("#6182bd"));
        } catch (Exception e) {
            address_content.setTextColor(Color.parseColor("#333333"));
        }

        if (model.followState != 0) {
            if ((model.followState - 1) >= 0 && model.followState - 1 < AppUtils.getFollows().length) {
                stateText.setText("跟进状态:" + AppUtils.getFollows()[model.followState - 1]);
            }
        } else {
            stateText.setText("跟进状态:未跟进");
        }

//        stateText.setText(model.getStates());

        Log.e("tag","TYPE_UNMARKED="+model.type);
        if (Constant.TYPE_UNMARKED.equals(model.type+"")) {
            bgLayout.setBackgroundResource(R.drawable.img_company_detail_title_bg1);
            tagText.setText("标记为目标客户");
//            stateText.setVisibility(VISIBLE);
        } else if (Constant.TYPE_TRACK.equals(model.type+"")) {
            bgLayout.setBackgroundResource(R.drawable.img_company_detail_title_bg2);
            tagText.setText("跟进记录");
//            stateText.setVisibility(GONE);
        } else if (Constant.TYPE_FORMAL.equals(model.type+"")) {
            bgLayout.setBackgroundResource(R.drawable.img_company_detail_title_bg3);
            tagText.setText("跟进记录");
//            stateText.setVisibility(VISIBLE);
        }
    }


    private void startOpen() {
        startOpenRotate();
        locationWebLayout.setVisibility(VISIBLE);
        final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        locationWebLayout.measure(widthSpec, heightSpec);
        ValueAnimator animator = ValueAnimator.ofInt(0, locationWebLayout.getMeasuredHeight());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (int) valueAnimator.getAnimatedValue();
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) locationWebLayout.getLayoutParams();
                params.height = value;
                locationWebLayout.setLayoutParams(params);
            }
        });
        animator.start();
    }

    private void startClose() {
        startCloseRotate();
        int height = locationWebLayout.getHeight();
        ValueAnimator animator = ValueAnimator.ofInt(height, 0);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (int) valueAnimator.getAnimatedValue();
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) locationWebLayout.getLayoutParams();
                params.height = value;
                locationWebLayout.setLayoutParams(params);
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                locationWebLayout.setVisibility(GONE);
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

    /**
     * 箭头 旋转180度动画
     */
    private void startOpenRotate() {
        RotateAnimation animation = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setFillAfter(true);
        jtImg.startAnimation(animation);
    }

    /**
     * 箭头 恢复原位置
     */
    private void startCloseRotate() {
        RotateAnimation animation = new RotateAnimation(180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setFillAfter(true);
        jtImg.startAnimation(animation);
    }

    private void addTag() {
//        showLoading();
        final HashMap<String, String> mParams = new HashMap<>();
        mParams.put("userId", AppUtils.getUserInfo().id);
        mParams.put("entid", model.getCompanyid());
        VolleyPostRequest<NetModel> getRequest = new VolleyPostRequest<NetModel>(context.getString(R.string.req_jusfoun_url) + "/api/app/customermark", NetModel.class
                , new Response.Listener<NetModel>() {
            @Override
            public void onResponse(NetModel response) {
//                hideLoadDialog();
                if (response.success()) {
                    model.type = "2";
                    tagText.setText("跟进记录");
                    Toast.makeText(context, "标记成功", Toast.LENGTH_SHORT).show();
//                    showToast("");
                } else {
                    Toast.makeText(context, response.msg, Toast.LENGTH_SHORT).show();
//                    showToast(response.msg);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }, context) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return mParams;
            }
        };

        getRequest.setShouldCache(false);
        getRequest.setTag(((Activity) context).getLocalClassName());
        VolleyUtil.getQueue(context).add(getRequest);
    }
}
