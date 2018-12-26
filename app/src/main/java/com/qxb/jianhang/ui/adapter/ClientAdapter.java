package com.qxb.jianhang.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jusfoun.baselibrary.base.BaseAdapter;
import com.jusfoun.baselibrary.base.BaseViewHolder;
import com.jusfoun.jusfouninquire.activity.CompanyDetailActivity;
import com.qxb.jianhang.R;
import com.qxb.jianhang.ui.activity.FollowUpRecordActivity;
import com.qxb.jianhang.ui.activity.MessageListActivity;
import com.qxb.jianhang.ui.constant.Constant;
import com.qxb.jianhang.ui.data.ClientModel;
import com.qxb.jianhang.ui.util.AppUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author liuguangdan
 * @version create at 2018/9/5/005 15:18
 * @Email lgd@jusfoun.com
 * @Description ${}
 */
public class ClientAdapter extends BaseAdapter<ClientModel> {

    private int type;

    public ClientAdapter(Context context, int type) {
        super(context);
        this.type = type;
    }

    @Override
    public int getLayoutResId(int viewType) {
        return R.layout.item_client;
    }

    @Override
    protected BaseViewHolder getViewHolder(int viewType, View view) {
        return new ViewHolder(view, context, type);
    }

    static class ViewHolder extends BaseViewHolder<ClientModel> {

        @BindView(R.id.ivLoc)
        ImageView ivLoc;
        @BindView(R.id.tvCompany)
        TextView tvCompany;
        @BindView(R.id.tvAddress)
        TextView tvAddress;
        @BindView(R.id.followTip)
        View followTip;
        @BindView(R.id.tvFollow)
        TextView tvFollow;
        @BindView(R.id.tvDistance)
        TextView tvDistance;
        @BindView(R.id.btMessage)
        Button btMessage;
        @BindView(R.id.btFollow)
        Button btFollow;

        private int type;
        private ClientModel model;

        public ViewHolder(View itemView, Context mContext, int type) {
            super(itemView, mContext);
            ButterKnife.bind(this, itemView);
            this.type = type;
        }

        @Override
        public void update(final ClientModel model) {
            this.model = model;
            tvCompany.setText(model.companyName);
            tvAddress.setText(model.address);

            if (!TextUtils.isEmpty(model.distance))
                tvDistance.setText(Html.fromHtml("距您<font color='#FB8B4E'>" + (int) Double.parseDouble(model.distance) + "</font>米"));
            else
                tvDistance.setText("");

            String[] fos = AppUtils.getFollows();
            if (model.followState > 0 && model.followState <= fos.length) {
                tvFollow.setText(fos[model.followState - 1]);
            } else {
                tvFollow.setText("未跟进");
            }

            if (model.type==3) {
                btMessage.setVisibility(View.VISIBLE);
            } else {
                btMessage.setVisibility(View.GONE);
//                followTip.setVisibility(View.GONE);
//                tvFollow.setVisibility(View.GONE);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, CompanyDetailActivity.class);
                    intent.putExtra(CompanyDetailActivity.COMPANY_ID, model.companyId);
                    intent.putExtra(CompanyDetailActivity.COMPANY_NAME, model.companyName);
                    mContext.startActivity(intent);
                }
            });

        }

        @OnClick(R.id.btMessage)
        public void message() {
            Intent intent = new Intent(mContext, MessageListActivity.class);
            intent.putExtra(Constant.COMPANYID, model.companyId);
            intent.putExtra(MessageListActivity.TYPE, MessageListActivity.TYPE_SINGLE);

            mContext.startActivity(intent);
        }

        @OnClick(R.id.btFollow)
        public void follow() {
            Intent intent = new Intent(mContext, FollowUpRecordActivity.class);
            intent.putExtra(Constant.COMPANYID, model.companyId);
            mContext.startActivity(intent);
        }
    }
}
