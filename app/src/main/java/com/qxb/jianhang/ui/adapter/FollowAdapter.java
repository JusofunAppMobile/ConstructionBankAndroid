package com.qxb.jianhang.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jusfoun.baselibrary.base.BaseAdapter;
import com.jusfoun.baselibrary.base.BaseViewHolder;
import com.qxb.jianhang.R;
import com.qxb.jianhang.ui.activity.FollowAddActivity;
import com.qxb.jianhang.ui.data.FollowModel;
import com.qxb.jianhang.ui.data.TrackRecordDataModel;
import com.qxb.jianhang.ui.util.AppUtils;

/**
 * @author liuguangdan
 * @version create at 2018/9/4/004 16:33
 * @Email lgd@jusfoun.com
 * @Description ${跟进记录}
 */
public class FollowAdapter extends BaseAdapter<TrackRecordDataModel.TrackRecordItemModel> {

    private String companyId;

    public FollowAdapter(Context context,String companyId) {
        super(context);
        this.companyId=companyId;
    }

    @Override
    public int getLayoutResId(int viewType) {
        return R.layout.item_follow;
    }

    @Override
    protected BaseViewHolder getViewHolder(int viewType, View view) {
        return new ViewHolder(view, context);
    }



    class ViewHolder extends BaseViewHolder<TrackRecordDataModel.TrackRecordItemModel> {

        protected TextView tvTime;
        protected TextView tvStatus;
        protected TextView tvClient;
        protected TextView tvJob;
        protected TextView tvDesc;
        protected TextView tvAddress;
        protected TextView phoneText;


        public ViewHolder(View itemView, Context mContext) {
            super(itemView, mContext);
            initView(itemView);
        }

        @Override
        public void update(final TrackRecordDataModel.TrackRecordItemModel model) {
            tvTime.setText(model.time);

            if((model.followState-1)>=0&&model.followState-1< AppUtils.getFollows().length) {
                tvStatus.setText(AppUtils.getFollows()[model.followState-1]);
            }
            tvClient.setText(model.name);
            tvJob.setText(model.job);
            tvDesc.setText(model.desc);
            tvAddress.setText(model.address+" "+model.detailAddress);
            phoneText.setText(model.phone);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, FollowAddActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("model",model);
                    intent.putExtra(FollowAddActivity.COMPANYID, companyId);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                }
            });
        }

        private void initView(View rootView) {
            tvTime = (TextView) rootView.findViewById(R.id.tvTime);
            tvStatus = (TextView) rootView.findViewById(R.id.tvStatus);
            tvClient = (TextView) rootView.findViewById(R.id.tvClient);
            tvJob = (TextView) rootView.findViewById(R.id.tvJob);
            tvDesc = (TextView) rootView.findViewById(R.id.tvDesc);
            tvAddress = (TextView) rootView.findViewById(R.id.tvAddress);
            phoneText = (TextView)rootView.findViewById(R.id.tvClientphone);
        }
    }
}
