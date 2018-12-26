package com.qxb.jianhang.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.jusfoun.baselibrary.base.BaseAdapter;
import com.jusfoun.baselibrary.base.BaseViewHolder;
import com.qxb.jianhang.R;
import com.qxb.jianhang.ui.data.MessageModel;
import com.qxb.jianhang.ui.event.CopyMessageEvent;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author liuguangdan
 * @version create at 2018/8/29/029 16:41
 * @Email lgd@jusfoun.com
 * @Description ${}
 */
public class MessagePushAdapter<T> extends BaseAdapter<MessageModel> {


    public MessagePushAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutResId(int viewType) {
        return R.layout.item_message_push;
    }

    @Override
    protected BaseViewHolder getViewHolder(int viewType, View view) {
        return new ViewHolder(view, context);
    }

    class ViewHolder extends BaseViewHolder<MessageModel> {

        @BindView(R.id.tvContent)
        TextView tvContent;

        public ViewHolder(View itemView, Context mContext) {
            super(itemView, mContext);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void update(MessageModel model) {
            tvContent.setText(model.message);
        }

        @OnClick(R.id.tvCopy)
        public void copy() {
            EventBus.getDefault().post(new CopyMessageEvent(tvContent.getText().toString()));
        }
    }
}
