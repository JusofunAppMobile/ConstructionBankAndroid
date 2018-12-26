package com.qxb.jianhang.ui.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jusfoun.baselibrary.Util.PhoneUtil;
import com.jusfoun.baselibrary.base.BaseModel;
import com.jusfoun.baselibrary.base.BaseView;
import com.jusfoun.jusfouninquire.activity.CompanyDetailActivity;
import com.qxb.jianhang.R;
import com.qxb.jianhang.ui.activity.FollowUpRecordActivity;
import com.qxb.jianhang.ui.constant.Constant;
import com.qxb.jianhang.ui.data.HomeDataItemModel;
import com.qxb.jianhang.ui.fragment.MapFragment;
import com.qxb.jianhang.ui.util.AppUtils;

/**
 * @author zhaoyapeng
 * @version create time:18/8/2919:44
 * @Email zyp@jusfoun.com
 * @Description ${地图 底部弹出单个企业列表}
 */
public class HomeOneCompanyView extends BaseView {
    protected ImageView viewZhanwei;

    private TextView mCompanyName, tvLegal, tvEstablish,
            mHotCompanyFollow, mAddress, mCountMoneny,
            followText, companyAddressText, tagBtnText, pushBtnText, folowStateText, distanceText;
    public HomeDataItemModel homeDataItemModel;
    private RelativeLayout rootLayout;

    private LinearLayout legalLayout, timeLayout, followStateLayout, threeTagLayout;
    private ImageView tagImg;

    public HomeOneCompanyView(Context context) {
        super(context);
    }

    public HomeOneCompanyView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HomeOneCompanyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initViews() {

        LayoutInflater.from(mContext).inflate(R.layout.view_home_one_company, this, true);
        viewZhanwei = (ImageView) findViewById(R.id.view_zhanwei);
        initView(this);
    }

    @Override
    protected void initActions() {


        viewZhanwei.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                hide();
            }
        });

//        SearchListModel model = new Gson().fromJson(IOUtil.readTextFileFromRawResourceId(mContext, R.raw.ceshi), SearchListModel.class);
//        setData(model.getBusinesslist().get(0));


        tagBtnText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tagCallBack != null) {
                    tagCallBack.setTag(homeDataItemModel);
                }
            }
        });
    }


    public void show() {
        setVisibility(VISIBLE);
        ValueAnimator valueAnimator = ValueAnimator.ofInt(PhoneUtil.getDisplayHeight(mContext), 0);
        valueAnimator.setDuration(200);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (int) valueAnimator.getAnimatedValue();
                setY(value);
            }
        });
        valueAnimator.start();
    }


    public void hide() {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, PhoneUtil.getDisplayHeight(mContext));
        valueAnimator.setDuration(500);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (int) valueAnimator.getAnimatedValue();
                setY(value);
                if (value == PhoneUtil.getDisplayHeight(mContext)) {
                    setVisibility(GONE);
                }
            }

        });
        valueAnimator.start();
    }

    public void close(){
        if (getVisibility() == View.VISIBLE) {
            hide();
        }
    }


    public void setData(BaseModel model) {
        final HomeDataItemModel data = (HomeDataItemModel) model;
        homeDataItemModel = data;

        tvLegal.setText(data.getLegal());
        tvEstablish.setText(data.establish);

//            if (!TextUtils.isEmpty(data.getRelated()))
//                tvRelate.setText(Html.fromHtml(data.getRelated()));


        if (!TextUtils.isEmpty(data.getCompanylightname())) {
            mCompanyName.setText(Html.fromHtml(data.getCompanylightname()));
        } else {
            mCompanyName.setText(data.getCompanyName());
        }


        if (TextUtils.isEmpty(data.getLocation())) {
            mAddress.setText("未公布");
        } else {
            mAddress.setText(data.getLocation());
        }

        if (TextUtils.isEmpty(data.getIndustry())) {
            mHotCompanyFollow.setText("未公布");
        } else {
            mHotCompanyFollow.setText(data.getIndustry());
        }
        if (TextUtils.isEmpty(data.getFunds())) {
            mCountMoneny.setText("未公布");
        } else {
            mCountMoneny.setText(data.getFunds());
        }

        companyAddressText.setText(data.address);

        tagBtnText.setVisibility(View.GONE);
        followText.setVisibility(View.GONE);
        pushBtnText.setVisibility(View.GONE);


        rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CompanyDetailActivity.class);
                intent.putExtra(CompanyDetailActivity.COMPANY_ID, data.getCompanyid());
                intent.putExtra(CompanyDetailActivity.COMPANY_NAME, data.getCompanyname());
                mContext.startActivity(intent);
            }
        });

        followText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, FollowUpRecordActivity.class);
                intent.putExtra(Constant.COMPANYID, data.getCompanyid());
                mContext.startActivity(intent);
            }
        });


        tagBtnText.setVisibility(View.GONE);
        pushBtnText.setVisibility(View.GONE);
        followText.setVisibility(View.GONE);
//        folowStateText.setText("测试数据");
        if (Constant.TYPE_UNMARKED.equals(data.type)) {
            tagBtnText.setVisibility(View.VISIBLE);
            followStateLayout.setVisibility(View.GONE);
            tagImg.setImageResource(R.drawable.icon_home_tag_add);
        } else if (Constant.TYPE_TRACK.equals(data.type)) {
            followText.setVisibility(View.VISIBLE);
            followStateLayout.setVisibility(View.VISIBLE);
            tagImg.setImageResource(R.drawable.icon_home_tag_mubiao);
        } else if (Constant.TYPE_FORMAL.equals(data.type)) {
            pushBtnText.setVisibility(View.VISIBLE);
            followText.setVisibility(View.VISIBLE);
            followStateLayout.setVisibility(View.VISIBLE);
            tagImg.setImageResource(R.drawable.icon_home_tag_zhengshi);
        }


        legalLayout.setVisibility(View.GONE);
        timeLayout.setVisibility(View.GONE);
        threeTagLayout.setVisibility(View.GONE);


        distanceText.setText(Html.fromHtml("距您" +
                "<font color='#fe792d'>" + data.distance + "</font>" + "米"));

        if (!TextUtils.isEmpty(homeDataItemModel.followState)) {
            int followState = Integer.parseInt(homeDataItemModel.followState);
            if ((followState - 1) >= 0 && followState - 1 < AppUtils.getFollows().length) {
                folowStateText.setText(AppUtils.getFollows()[followState - 1]);
            }
        } else {
            folowStateText.setText("未跟进");
        }
        show();
    }

    private void initView(View view) {
        mCompanyName = (TextView) view.findViewById(R.id.company_name);
        tvLegal = (TextView) view.findViewById(R.id.tvLegal);
        tvEstablish = (TextView) view.findViewById(R.id.tvEstablish);
        rootLayout = (RelativeLayout) view.findViewById(R.id.layout_root);
        followText = (TextView) view.findViewById(R.id.text_follow);

        mHotCompanyFollow = (TextView) view.findViewById(R.id.hot_company_follow);
        mAddress = (TextView) view.findViewById(R.id.address);
        mCountMoneny = (TextView) view.findViewById(R.id.count_moneny);

        companyAddressText = (TextView) view.findViewById(R.id.company_address);
        tagBtnText = (TextView) view.findViewById(R.id.text_company_tag);
        pushBtnText = (TextView) view.findViewById(R.id.text_company_push);

        legalLayout = (LinearLayout) view.findViewById(R.id.layout_legal);
        timeLayout = (LinearLayout) view.findViewById(R.id.layout_time);
        followStateLayout = (LinearLayout) view.findViewById(R.id.layout_state);
        folowStateText = (TextView) view.findViewById(R.id.text_follow_state);
        tagImg = (ImageView) view.findViewById(R.id.img_tag);
        threeTagLayout = (LinearLayout) view.findViewById(R.id.layout_tag_three);
        distanceText = (TextView) view.findViewById(R.id.company_distance);
    }

    private MapFragment.TagCallBack tagCallBack;

    public void setCallBck(MapFragment.TagCallBack tagCallBack) {
        this.tagCallBack = tagCallBack;
    }

    public void refresh() {
        if (homeDataItemModel != null) {
            tagBtnText.setVisibility(View.GONE);
            pushBtnText.setVisibility(View.GONE);
            followText.setVisibility(View.GONE);
            if (Constant.TYPE_UNMARKED.equals(homeDataItemModel.type)) {
                tagBtnText.setVisibility(View.VISIBLE);
                followStateLayout.setVisibility(View.GONE);
                tagImg.setImageResource(R.drawable.icon_home_tag_add);
            } else if (Constant.TYPE_TRACK.equals(homeDataItemModel.type)) {
                followText.setVisibility(View.VISIBLE);
                followStateLayout.setVisibility(View.VISIBLE);
                tagImg.setImageResource(R.drawable.icon_home_tag_mubiao);
            } else if (Constant.TYPE_FORMAL.equals(homeDataItemModel.type)) {
                pushBtnText.setVisibility(View.VISIBLE);
                followText.setVisibility(View.VISIBLE);
                followStateLayout.setVisibility(View.VISIBLE);
                tagImg.setImageResource(R.drawable.icon_home_tag_zhengshi);
            }

            if (!TextUtils.isEmpty(homeDataItemModel.followState)) {
                int followState = Integer.parseInt(homeDataItemModel.followState);
                if ((followState - 1) >= 0 && followState - 1 < AppUtils.getFollows().length) {
                    folowStateText.setText(AppUtils.getFollows()[followState - 1]);
                }
            } else {
                folowStateText.setText("未跟进");
            }

        }
    }

    public boolean isShowGoClose(){
        if(getVisibility() == VISIBLE){
            close();
            return true;
        }
        return false;
    }

}

