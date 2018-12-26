package com.qxb.jianhang.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.jusfoun.baselibrary.base.BaseAdapter;
import com.jusfoun.baselibrary.base.BaseViewHolder;
import com.qxb.jianhang.R;
import com.qxb.jianhang.ui.data.MessageCompanyModel;

/**
 * @author liuguangdan
 * @version create at 2018/8/29/029 16:41
 * @Email lgd@jusfoun.com
 * @Description ${}
 */
public class MessageCompanyAdapter extends BaseAdapter<MessageCompanyModel.MessageCompanyItemModel> {

    private boolean canSelect = true;

    public MessageCompanyAdapter(Context context) {
        this(context, true);
    }

    public MessageCompanyAdapter(Context context, boolean canSelect) {
        super(context);
        this.canSelect = canSelect;
    }

    @Override
    public int getLayoutResId(int viewType) {
        return R.layout.item_message_select_company;
    }

    @Override
    protected BaseViewHolder getViewHolder(int viewType, View view) {
        return new ViewHolder(view, context);
    }

    class ViewHolder extends BaseViewHolder<MessageCompanyModel.MessageCompanyItemModel> {

        private View root;
        private View ivCheck;
        private TextView tvName;

        public ViewHolder(View itemView, Context mContext) {
            super(itemView, mContext);
            root = itemView;
            ivCheck = root.findViewById(R.id.ivCheck);
            tvName = (TextView)root.findViewById(R.id.tvName);
        }

        @Override
        public void update(final MessageCompanyModel.MessageCompanyItemModel model) {
            root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (canSelect) {
                        model.select = !model.select;
                        v.setSelected(model.select);
                    }
                }
            });
            tvName.setText(model.name);
            ivCheck.setVisibility(canSelect ? View.VISIBLE : View.GONE);

            ivCheck.setSelected(model.select);

            root.setSelected(model.select);
        }
    }
}
