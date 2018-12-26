package com.qxb.jianhang.ui.adapter;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.jusfoun.baselibrary.Util.PhoneUtil;
import com.jusfoun.baselibrary.base.BaseAdapter;
import com.jusfoun.baselibrary.base.BaseViewHolder;
import com.qxb.jianhang.R;
import com.qxb.jianhang.ui.activity.MessageListActivity;
import com.qxb.jianhang.ui.data.MessageModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author liuguangdan
 * @version create at 2018/8/29/029 16:41
 * @Email lgd@jusfoun.com
 * @Description ${}
 */
public class MessageAdapter<T> extends BaseAdapter<MessageModel> {

    private int type = 0;

    public MessageAdapter(Context context, int type) {
        super(context);
        this.type = type;
    }

    @Override
    public int getLayoutResId(int viewType) {
        return R.layout.item_message;
    }

    @Override
    protected BaseViewHolder getViewHolder(int viewType, View view) {
        return new ViewHolder(view, context);
    }


    class ViewHolder extends BaseViewHolder<MessageModel> {


        protected TextView tvTime;
        protected TextView tvContent;
        protected TextView tvCompany;
        protected ConstraintLayout constraintLayout;


        public ViewHolder(View itemView, Context mContext) {
            super(itemView, mContext);
            initView(itemView);
        }

        @Override
        public void update(MessageModel model) {

            if (type == MessageListActivity.TYPE_SINGLE) {
                constraintLayout.setVisibility(View.GONE);
                tvContent.setBackgroundResource(R.drawable.img_item_bg);
                tvContent.setPadding(PhoneUtil.dip2px(mContext, 16), PhoneUtil.dip2px(mContext, 20), PhoneUtil.dip2px(mContext, 16), PhoneUtil.dip2px(mContext, 20));
            } else {
                constraintLayout.setVisibility(View.VISIBLE);
            }

            tvTime.setText(parseDate(model.pushDate));
            tvCompany.setText(model.company);
            tvContent.setText(model.message);
        }

        private void initView(View rootView) {
            tvTime = (TextView) rootView.findViewById(R.id.tvTime);
            tvContent = (TextView) rootView.findViewById(R.id.tvContent);
            tvCompany = (TextView) rootView.findViewById(R.id.tvCompany);
            constraintLayout = (ConstraintLayout) rootView.findViewById(R.id.constraintLayout);
        }
    }

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");

    private String parseDate(String time) {
        if (TextUtils.isEmpty(time))
            return "";
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(time));
        String date = sdf.format(calendar.getTime());

        if (date.equals(sdf.format(new Date()))) { // 今天

            StringBuilder sb = new StringBuilder();
            sb.append(calendar.get(Calendar.HOUR_OF_DAY) < 12 ? "上午 " : "下午 ");
            sb.append(sdf2.format(calendar.getTime()));
            return sb.toString();
        }
        return date;
    }
}
