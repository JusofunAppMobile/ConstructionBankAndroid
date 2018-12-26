package com.qxb.jianhang.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.jusfoun.baselibrary.base.BaseAdapter;
import com.jusfoun.baselibrary.base.BaseViewHolder;
import com.qxb.jianhang.R;
import com.qxb.jianhang.ui.data.PoiInfoModel;
import com.qxb.jianhang.ui.event.SelectPoiInfoEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author liuguangdan
 * @version create at 2018/9/6/006 9:00
 * @Email lgd@jusfoun.com
 * @Description ${}
 */
public class PoiInfoAdapter extends BaseAdapter<PoiInfoModel> {
    public PoiInfoAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutResId(int viewType) {
        return R.layout.item_poi_info;
    }

    @Override
    protected BaseViewHolder getViewHolder(int viewType, View view) {
        return new ViewHolder(view, context);
    }


    static class ViewHolder extends BaseViewHolder<PoiInfoModel> {

        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.tvAddress)
        TextView tvAddress;

        PoiInfoModel model;

        public ViewHolder(View itemView, Context mContext) {
            super(itemView, mContext);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void update(PoiInfoModel model) {
            this.model = model;
            tvName.setText(model.name);
            tvAddress.setText(model.address);
        }

        @OnClick(R.id.constraint)
        public void itemClick() {
            EventBus.getDefault().post(new SelectPoiInfoEvent(model));
            ((Activity) mContext).finish();
        }

    }

}
