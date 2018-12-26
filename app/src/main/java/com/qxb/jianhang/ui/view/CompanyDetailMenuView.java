package com.qxb.jianhang.ui.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jusfoun.jusfouninquire.TimeOut;
import com.qxb.jianhang.BackApplication;
import com.qxb.jianhang.R;
import com.jusfoun.jusfouninquire.callback.NetWorkCallBack;
import com.jusfoun.jusfouninquire.route.NetWorkCompanyDetails;
import com.jusfoun.jusfouninquire.activity.CompanyDetailsActivity;
import com.qxb.jianhang.ui.adapter.CompanyMenuAdapter;
import com.qxb.jianhang.ui.data.CompanyDetailModel;
import com.qxb.jianhang.ui.data.UserInfoModel;
import com.jusfoun.jusfouninquire.widget.DividerGridItemDecoration;
import com.jusfoun.jusfouninquire.widget.FullyGridLayoutManger;

import java.util.HashMap;


/**
 * @author zhaoyapeng
 * @version create time:17/9/711:21
 * @Email zyp@jusfoun.com
 * @Description ${企业详情 九宫格 view}
 */
public class CompanyDetailMenuView extends RelativeLayout {
    private Context mContext;
    private RecyclerView mCompanyMenu;
    private CompanyMenuAdapter adapter;
    private TextView titleText;
    private LinearLayout titleLayout;
    private ImageView jtImg;
    private String companyId, companyName;
    public static final int TYPE_RISKINFO = 1;
    public static final int TYPE_OPERATINGCONDITIONS = 2;
    public static final int TYPE_INTANGIBLEASSETS = 3;

    private UserInfoModel userInfo;
    private int type;
    private CompanyDetailModel companyDetailModel;
    private android.os.Handler handler;

    public CompanyDetailMenuView(Context context) {
        super(context);
        mContext = context;
        initData();
        initViews();
        initActions();
    }

    public CompanyDetailMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initData();
        initViews();
        initActions();
    }

    public CompanyDetailMenuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initData();
        initViews();
        initActions();
    }

    private void initData() {
        adapter = new CompanyMenuAdapter(mContext);
        userInfo = BackApplication.getUserInfo();
        handler =new Handler();
    }

    private void initViews() {
        LayoutInflater.from(mContext).inflate(R.layout.view_company_detail_menu, this, true);
        mCompanyMenu = (RecyclerView) findViewById(R.id.recyclerview);
        titleText = (TextView) findViewById(R.id.text_title);
        titleLayout = (LinearLayout) findViewById(R.id.layout_title);
        jtImg = (ImageView) findViewById(R.id.img_jiantou);

    }

    private void initActions() {
        mCompanyMenu.setNestedScrollingEnabled(false);
        mCompanyMenu.setLayoutManager(new FullyGridLayoutManger(mContext, 4) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        mCompanyMenu.setAdapter(adapter);
        mCompanyMenu.addItemDecoration(new DividerGridItemDecoration(mContext));


        titleLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCompanyMenu.getVisibility() == GONE) {
                    if (adapter.getItemCount() == 0) {
                        getMenuData(type);
                    } else {
                        startOpen();
                    }

                } else {
                    startClose();
                }
            }
        });

        adapter.setOnItemClickListener(new CompanyMenuAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, String umeng) {
//                EventUtils.event2(mContext,umeng);
                if (companyDetailModel == null)
                    return;

                Bundle argument = new Bundle();
                argument.putSerializable(CompanyDetailsActivity.COMPANY, companyDetailModel);
                argument.putInt(CompanyDetailsActivity.POSITION, position);

                Intent intent = new Intent(mContext, CompanyDetailsActivity.class);
                intent.putExtras(argument);
                mContext.startActivity(intent);
            }
        });


    }

    public void setData(int type, CompanyDetailModel companyDetailModel, String companyId, String compantName) {
        this.companyDetailModel = companyDetailModel.getCloneModel();
        this.companyId = companyId;
        this.companyName = compantName;
        this.type = type;

        if(type==TYPE_RISKINFO){
            titleText.setText("风险信息");
        }else if(type==TYPE_OPERATINGCONDITIONS){
            titleText.setText("经营状况");
        }else if(type==TYPE_INTANGIBLEASSETS){
            titleText.setText("无形资产");
        }
//        adapter.refresh(list);
//
    }

    public void setOnItemClickListener(CompanyMenuAdapter.OnItemClickListener listener) {
        adapter.setOnItemClickListener(listener);
    }

    private void startOpen() {

        mCompanyMenu.setVisibility(VISIBLE);
        startOpenRotate();
        final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        mCompanyMenu.measure(widthSpec, heightSpec);
        Log.e("tag","startOpen="+mCompanyMenu.getMeasuredHeight());
        ValueAnimator animator = ValueAnimator.ofInt(0, mCompanyMenu.getMeasuredHeight());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (int) valueAnimator.getAnimatedValue();
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mCompanyMenu.getLayoutParams();
                params.height = value;
                mCompanyMenu.setLayoutParams(params);
            }
        });
        animator.start();
    }


    private void startClose() {
        startCloseRotate();
        int height = mCompanyMenu.getHeight();
        ValueAnimator animator = ValueAnimator.ofInt(height, 1);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (int) valueAnimator.getAnimatedValue();
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mCompanyMenu.getLayoutParams();
                params.height = value;
                mCompanyMenu.setLayoutParams(params);
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                mCompanyMenu.setVisibility(GONE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animator.start();
    }

    /**
     * 箭头 旋转180度动画
     */
    private void startOpenRotate() {
        RotateAnimation animation = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setFillAfter(true);
        jtImg.startAnimation(animation);
    }

    /**
     * 箭头 恢复原位置
     */
    private void startCloseRotate() {
        RotateAnimation animation = new RotateAnimation(180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setFillAfter(true);
        jtImg.startAnimation(animation);
    }

    private void getMenuData(int type) {
        if(callBack!=null){
            callBack.showMenuLoading();
        }
        HashMap<String, String> params = new HashMap<String,String>();
        TimeOut timeOut = new TimeOut(mContext);
        params = new HashMap<>();
        params.put("companyid", companyId);
        params.put("entname", companyName == null ? "" : companyName);
        params.put("companyname", companyName == null ? "" : companyName);

        if (userInfo != null && !TextUtils.isEmpty(userInfo.getUserid()))
            params.put("userid", userInfo.getUserid());
        else {
            params.put("userid", "");
        }
        params.put("t", timeOut.getParamTimeMollis() + "");
        params.put("m", timeOut.MD5time() + "");
        NetWorkCompanyDetails.getMenuList(mContext, type, params, "CompanyDetailsItemHttp", new NetWorkCallBack() {
            @Override
            public void onSuccess(Object data) {
                if(callBack!=null){
                    callBack.hideMenuLoading();
                }
                CompanyDetailModel model = (CompanyDetailModel) data;
                if (model.getResult() == 0) {

                    // TODO 测试测试
//                    for (CompanyDetailMenuModel m : model.getSubclassMenu()) {
//                        if(m.getMenuid().equals("29")) {
//                            m.setCount("33");
//                            m.setHasData("1");
//                        }
//                    }

                    companyDetailModel.setSubclassMenu(model.getSubclassMenu());
                    adapter.refresh(model.getSubclassMenu());

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startOpen();
                        }
                    },500);

                } else {
                    Toast.makeText(mContext, model.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFail(String error) {
                if(callBack!=null){
                    callBack.hideMenuLoading();
                }
            }
        });
    }


    public interface CallBack {
        void showMenuLoading();

        void hideMenuLoading();
    }

    private CallBack callBack;

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void startLoad(){
        titleLayout.performClick();
    }

}
