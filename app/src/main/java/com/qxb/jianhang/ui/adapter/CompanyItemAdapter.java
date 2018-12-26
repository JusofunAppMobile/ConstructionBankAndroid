package com.qxb.jianhang.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jusfoun.baselibrary.base.BaseAdapter;
import com.jusfoun.baselibrary.base.BaseModel;
import com.jusfoun.baselibrary.base.BaseViewHolder;
import com.jusfoun.jusfouninquire.activity.CompanyDetailActivity;
import com.qxb.jianhang.R;
import com.qxb.jianhang.ui.activity.FollowUpRecordActivity;
import com.qxb.jianhang.ui.activity.MessageListActivity;
import com.qxb.jianhang.ui.constant.Constant;
import com.qxb.jianhang.ui.data.HomeDataItemModel;
import com.qxb.jianhang.ui.util.AppUtils;

/**
 * @author zhaoyapeng
 * @version create time:17/12/2115:44
 * @Email zyp@jusfoun.com
 * @Description ${公司item}
 */

public class CompanyItemAdapter<T> extends BaseAdapter<BaseModel> {


    private boolean isShort = false;

    public CompanyItemAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutResId(int viewType) {
        return R.layout.item_search_result;
    }

    @Override
    protected BaseViewHolder getViewHolder(int viewType, View view) {
        return new CompantItemViewHolder(view, context);
    }

    class CompantItemViewHolder extends BaseViewHolder<BaseModel> {

        private TextView mCompanyName, tvLegal, tvEstablish,
                mHotCompanyFollow, mAddress, mCountMoneny,
                followText, companyAddressText, tagBtnText, pushBtnText, folowStateText, distanceText;
        public HomeDataItemModel model;
        private RelativeLayout rootLayout;

        private LinearLayout legalLayout, timeLayout, followStateLayout, threeTagLayout;
        private ImageView tagImg;


        public CompantItemViewHolder(View itemView, Context mContext) {
            super(itemView, mContext);
            initView(itemView);


        }

        @Override
        public void update(BaseModel model) {
            final HomeDataItemModel data = (HomeDataItemModel) model;
            tvLegal.setText(data.getLegal());
            tvEstablish.setText(data.registDate);

//            if (!TextUtils.isEmpty(data.getRelated()))
//                tvRelate.setText(Html.fromHtml(data.getRelated()));

            if (!TextUtils.isEmpty(data.getCompanylightname())) {
                mCompanyName.setText(Html.fromHtml(data.getCompanylightname()));
            } else if (!TextUtils.isEmpty(data.getCompanyName())) {
                mCompanyName.setText(data.getCompanyName());
            } else {
                mCompanyName.setText(data.name);
            }


            if (TextUtils.isEmpty(data.province) || TextUtils.equals("0", data.province)) {
                mAddress.setVisibility(View.GONE);
            } else {
                mAddress.setVisibility(View.VISIBLE);
                mAddress.setText(data.province);
            }

            if (TextUtils.isEmpty(data.classify) || TextUtils.equals("0", data.classify)) {
                mHotCompanyFollow.setVisibility(View.GONE);
            } else {
                mHotCompanyFollow.setVisibility(View.VISIBLE);
                mHotCompanyFollow.setText(data.classify);
            }


            if (TextUtils.isEmpty(data.registMoney) || TextUtils.equals("0", data.registMoney)) {
                mCountMoneny.setVisibility(View.GONE);
            } else {
                mCountMoneny.setText(data.registMoney);
                mCountMoneny.setVisibility(View.VISIBLE);
            }

            companyAddressText.setText(data.address);

            tagBtnText.setVisibility(View.GONE);
            followText.setVisibility(View.GONE);
            pushBtnText.setVisibility(View.GONE);


            rootLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, CompanyDetailActivity.class);
                    if (!TextUtils.isEmpty(data.getCompanyid())) {
                        intent.putExtra(CompanyDetailActivity.COMPANY_ID, data.getCompanyid());
                    } else {
                        intent.putExtra(CompanyDetailActivity.COMPANY_ID, data.entid);
                    }

                    intent.putExtra(CompanyDetailActivity.COMPANY_NAME, data.getCompanyname());

                    intent.putExtra(CompanyDetailActivity.COMPANY_STATE, data.type);
                    mContext.startActivity(intent);
                }
            });

            followText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, FollowUpRecordActivity.class);

                    if (!TextUtils.isEmpty(data.getCompanyid())) {
                        intent.putExtra(Constant.COMPANYID, data.getCompanyid());
                    } else {
                        intent.putExtra(Constant.COMPANYID, data.entid);
                    }
                    mContext.startActivity(intent);
                }
            });


//            folowStateText.setText("测试数据");
            tagBtnText.setVisibility(View.GONE);
            pushBtnText.setVisibility(View.GONE);
            followText.setVisibility(View.GONE);

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


            tagBtnText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callBack != null) {
                        callBack.tagCompany(data);
                    }
                }
            });

            pushBtnText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, MessageListActivity.class);
                    if (!TextUtils.isEmpty(data.getCompanyid())) {
                        intent.putExtra(Constant.COMPANYID, data.getCompanyid());
                    } else {
                        intent.putExtra(Constant.COMPANYID, data.entid);
                    }
                    intent.putExtra(MessageListActivity.TYPE, MessageListActivity.TYPE_SINGLE);
                    mContext.startActivity(intent);
                }
            });

            if (TextUtils.isEmpty(data.distance)) {
                data.distance = "0";
            }
            distanceText.setText(Html.fromHtml("距您" +
                    "<font color='#fe792d'>" + data.distance + "</font>" + "米"));


            if (!TextUtils.isEmpty(data.followState)) {
                int followState = Integer.parseInt(data.followState);
                if ((followState - 1) >= 0 && followState - 1 < AppUtils.getFollows().length) {
                    folowStateText.setText(AppUtils.getFollows()[followState - 1]);
                }else{
                    folowStateText.setText("未跟进");
                }
            } else {
                folowStateText.setText("未跟进");
            }


            if (isShort) {
                legalLayout.setVisibility(View.GONE);
                timeLayout.setVisibility(View.GONE);
                threeTagLayout.setVisibility(View.GONE);
            }

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
    }


    public void setShort(boolean aShort) {
        isShort = aShort;
    }

    public interface CallBack {
        void tagCompany(HomeDataItemModel data);
    }

    private CallBack callBack;

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

}
