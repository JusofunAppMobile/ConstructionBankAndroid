package com.qxb.jianhang.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qxb.jianhang.R;
import com.qxb.jianhang.ui.data.PoiInfoModel;
import com.qxb.jianhang.ui.event.SelectPoiInfoEvent;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author liuguangdan
 * @version create at 2018/9/6/006 9:00
 * @Email lgd@jusfoun.com
 * @Description ${}
 */
public class HommSearchTagAdapter extends BaseQuickAdapter<PoiInfoModel, BaseViewHolder> {



    public HommSearchTagAdapter() {
        super(R.layout.item_poi_info);
    }

    @Override
    protected void convert(BaseViewHolder helper, PoiInfoModel item) {
        TextView tvName = (TextView) helper.getView(R.id.tvName);
        tvName.setText(item.name);

        TextView tvAddress = (TextView) helper.getView(R.id.tvAddress);
        tvAddress.setText(item.address);
    }
}
