package com.qxb.jianhang.ui.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jusfoun.baselibrary.Util.PreferenceUtils;
import com.jusfoun.baselibrary.model.UserModel;
import com.makeramen.roundedimageview.RoundedImageView;
import com.qxb.jianhang.R;
import com.qxb.jianhang.ui.activity.ClientListActivity;
import com.qxb.jianhang.ui.activity.FollowUpScheduleActivity;
import com.qxb.jianhang.ui.activity.LoginActivity;
import com.qxb.jianhang.ui.activity.MessageListActivity;
import com.qxb.jianhang.ui.activity.StatActivity;
import com.qxb.jianhang.ui.base.BaseBackActivity;
import com.qxb.jianhang.ui.constant.Constant;
import com.qxb.jianhang.ui.util.AppUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author liuguangdan
 * @version create at 2018/8/29/029
 * @Email lgd@jusfoun.com
 * @Description ${个人中心}
 */
public class PersonFragment extends BaseBackFragment implements View.OnClickListener {

    @BindView(R.id.vHeadIcon)
    RoundedImageView ivHeadIcon;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvOrgan)
    TextView tvOrgan;

    @BindView(R.id.tvTitle)
    TextView titleText;

    @BindView(R.id.ivBack)
    ImageView backImg;



    @Override
    public int getLayoutResId() {
        return R.layout.frag_person;
    }

    @Override
    public void initDatas() {

    }

    @Override
    public void initView(View rootView) {
        ButterKnife.bind(this, rootView);
        findViewById(R.id.vCustom).setOnClickListener(this);
        findViewById(R.id.vMessage).setOnClickListener(this);
        findViewById(R.id.layout_jincheng).setOnClickListener(this);
        findViewById(R.id.text_huibao).setOnClickListener(this);
        findViewById(R.id.text_about).setOnClickListener(this);

        UserModel user = AppUtils.getUserInfo();
        if (user != null) {
            tvName.setText(user.realName);
            tvOrgan.setText(user.organName);
        }
    }

    @Override
    public void initAction() {

        Glide.with(this)
                .load(R.mipmap.ic_launcher)
                .into(ivHeadIcon);

        titleText.setText("个人中心");
        backImg.setVisibility(View.GONE);

//        Glide.with(mContext).load("https://pic.qqtn.com/up/2018-8/2018082710001443218.jpg").into(ivHeadIcon);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.vCustom:
                startActivity(new Intent(mContext, ClientListActivity.class));
                break;
            case R.id.vMessage:
                Intent intent = new Intent(mContext, MessageListActivity.class);
                intent.putExtra(MessageListActivity.TYPE,MessageListActivity.TYPE_MULTIPLE);
                mContext.startActivity(intent);
                break;
            case R.id.layout_jincheng:
                mContext.startActivity(new Intent(mContext, FollowUpScheduleActivity.class));
                break;
            case R.id.text_huibao:
                mContext.startActivity(new Intent(mContext, StatActivity.class));
                break;

            case R.id.text_about:
//                mContext.startActivity(new Intent(mContext, FollowUpScheduleActivity.class));
                break;




        }
    }

    @OnClick(R.id.btLogout)
    public void logout() {
        PreferenceUtils.remove(mContext, Constant.PRE_USER_INFO);
        startActivity(new Intent(mContext, LoginActivity.class));
        ((BaseBackActivity) getActivity()).finishDelay();
    }

}
