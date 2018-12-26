package com.qxb.jianhang.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.jusfoun.baselibrary.base.BaseAdapter;
import com.jusfoun.baselibrary.base.BaseViewHolder;
import com.qxb.jianhang.R;
import com.qxb.jianhang.ui.data.HistoryModel;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author liuguangdan
 * @version create at 2018/12/10/010 17:47
 * @Email lgd@jusfoun.com
 * @Description ${}
 */
public class StatAdapter extends BaseAdapter<HistoryModel> {

    public StatAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutResId(int viewType) {
        return R.layout.item_stat;
    }

    @Override
    protected BaseViewHolder getViewHolder(int viewType, View view) {
        return new ViewHolder(view, context);
    }

    static class ViewHolder extends BaseViewHolder<HistoryModel> {

        @BindView(R.id.tvTitle)
        TextView tvTitle;
        @BindView(R.id.tvNum)
        TextView tvNum;
        @BindView(R.id.root)
        View root;

        public ViewHolder(View itemView, Context mContext) {
            super(itemView, mContext);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void update(final HistoryModel model) {
            tvTitle.setText(model.companyname);
            tvNum.setText(String.valueOf(model.count));
            root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent = new Intent(mContext, CompanyDetailActivity.class);
//                    intent.putExtra(CompanyDetailActivity.COMPANY_ID, model.companyid);
//                    intent.putExtra(CompanyDetailActivity.COMPANY_NAME, model.companyname);
//                    mContext.startActivity(intent);
                }
            });
        }
    }
}
