package com.jusfoun.jusfouninquire.fragment;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jusfoun.baselibrary.Util.AppUtil;
import com.jusfoun.baselibrary.Util.PhoneUtil;
import com.jusfoun.baselibrary.event.IEvent;
import com.jusfoun.jusfouninquire.TimeOut;
import com.qxb.jianhang.R;
import com.jusfoun.jusfouninquire.activity.CompanyMapActivity;
import com.jusfoun.jusfouninquire.bean.CompanyMapDataModel;
import com.jusfoun.jusfouninquire.bean.CompanyMapModel;
import com.jusfoun.jusfouninquire.callback.NetWorkCallBack;
import com.jusfoun.jusfouninquire.event.CompanyMapEvent;
import com.jusfoun.jusfouninquire.route.GetCompanyMap;
import com.jusfoun.jusfouninquire.util.LoginSharePreference;
import com.jusfoun.jusfouninquire.view.LineDrawAnimView;
import com.jusfoun.jusfouninquire.view.LineDrawBaseView;
import com.jusfoun.jusfouninquire.activity.CompanyDetailsActivity;
import com.jusfoun.jusfouninquire.animation.SceneAnimation;
import com.qxb.jianhang.ui.data.CompanyDetailModel;
import com.qxb.jianhang.ui.data.UserInfoModel;
import com.qxb.jianhang.ui.util.AppUtils;
import com.qxb.jianhang.ui.view.NetWorkErrorView;

import java.util.HashMap;

/**
 * Author  JUSFOUN
 * CreateDate 2015/11/11.
 * Description 企业图谱
 */
public class CompanyMapFragment extends BaseViewPagerFragment {

    private TextView mGudongTxt,mTouziTxt;
    private Bundle argument;
    private String mCompanyId,mCompanyName;
    private HashMap<String,String> params;
    private CompanyMapModel mapModel;

    private LineDrawAnimView lineDrawAnimView;
    private ImageView mImageView;
    private AnimationDrawable imageAnim;
    private NetWorkErrorView netWorkError;
    private RelativeLayout layout;

    private LinearLayout loading;
    private ImageView imageView;
    private SceneAnimation sceneAnimation;

    public static CompanyMapFragment getInstance(Bundle argument) {
        CompanyMapFragment fragment = new CompanyMapFragment();
        fragment.setArguments(argument);
        return fragment;
    }

    @Override
    protected void setViewHint() {
        if (layout!=null)
            layout.setVisibility(View.GONE);
    }

    @Override
    protected void refreshData() {
        if (layout!=null)
            layout.setVisibility(View.VISIBLE);

        argument=getArguments();
        if (argument!=null){
            CompanyDetailModel model= (CompanyDetailModel) argument.getSerializable(CompanyDetailsActivity.COMPANY);
            if (model!=null) {
                mCompanyId = model.getCompanyid();
                mCompanyName = model.getCompanyname();
                getCompanyMap();
            }
        }
    }

    @Override
    protected void initData() {
        params=new HashMap<>();
    }

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_company_map,container,false);
        mGudongTxt= (TextView) view.findViewById(R.id.gudong_Text);
        mTouziTxt= (TextView) view.findViewById(R.id.touzi_Text);
        lineDrawAnimView= (LineDrawAnimView) view.findViewById(R.id.line_draw_view);
        mImageView= (ImageView) view.findViewById(R.id.indicate_image);
        netWorkError= (NetWorkErrorView) view.findViewById(R.id.net_work_error);
        layout= (RelativeLayout) view.findViewById(R.id.layout);

        loading= (LinearLayout) view.findViewById(R.id.loading);
        imageView= (ImageView) view.findViewById(R.id.loading_img);
        return view;
    }

    @Override
    protected void initWeightActions() {
        lineDrawAnimView.setAllAnimStop(true);
        lineDrawAnimView.setmRaiusRatio(0.7f);
        lineDrawAnimView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (mapModel == null)
                        return true;
                    if (mapModel.getShareholders() == null && mapModel.getInvestments() == null
                            || mapModel.getShareholders() == null && mapModel.getInvestments().size() == 0
                            || mapModel.getShareholders().size() == 0 && mapModel.getInvestments() == null
                            || mapModel.getShareholders().size() == 0 && mapModel.getInvestments().size() == 0)
                        return true;
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(CompanyMapActivity.COMPANY_ATLAS_KEY, mapModel);
                    bundle.putString(CompanyMapActivity.COMPANY_ID, mCompanyId);
                    bundle.putString(CompanyMapActivity.COMPANY_NAME, mCompanyName);
                    goActivity(CompanyMapActivity.class, bundle);
                }
                return true;
            }
        });

        lineDrawAnimView.setViewSizeChange(new LineDrawBaseView.OnViewSizeChange() {
            @Override
            public void onSizeChange() {
                if (mapModel != null)
                    lineDrawAnimView.refresh(mapModel.getShareholders(), mapModel.getInvestments(), 0, 8, mapModel.getcEntShortName());
            }
        });

        netWorkError.setListener(new NetWorkErrorView.OnRefreshListener() {
            @Override
            public void OnNetRefresh() {
                getCompanyMap();
            }
        });
        mImageView.setImageResource(R.drawable.anim_indicate_image);
        imageAnim= (AnimationDrawable) mImageView.getDrawable();
        if (imageAnim!=null)
            imageAnim.start();
    }

    @Override
    public void onEvent(IEvent event) {
        super.onEvent(event);
        if (event instanceof CompanyMapEvent){
            //TODO:是否要刷新界面
            if (layout!=null)
                layout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (imageAnim!=null)
            imageAnim.stop();
    }

    private void getCompanyMap(){
        TimeOut timeOut = new TimeOut(mContext);
        params.put("userid", AppUtils.getUserInfo().id);
        params.put("entid", mCompanyId);
        params.put("entname",mCompanyName);
        params.put("t", timeOut.getParamTimeMollis()+"");
        params.put("m", timeOut.MD5time()+"");
        if (sceneAnimation==null)
            sceneAnimation=new SceneAnimation(imageView,75);
        sceneAnimation.start();


        Log.e("tag","getCompanyMap="+new Gson().toJson(params));
        GetCompanyMap.getCompanyMap(mContext, params,((Activity)mContext).getLocalClassName(), new NetWorkCallBack() {
            @Override
            public void onSuccess(Object data) {
                if(isAdded()){
//
//                    CompanyMapDataModel model = (CompanyMapDataModel)data;
//                    CompanyMapDataModel companyMapDataModel = new CompanyMapDataModel();
//                    companyMapDataModel.setResult(0);
//                    companyMapDataModel.setData(new CompanyMapModel());
//                    companyMapDataModel.getData().setShareholders(new ArrayList<ShareholderModel>());
//                    companyMapDataModel.getData().setInvestments(new ArrayList<InvestmentModel>());
//                    companyMapDataModel.getData().getInvestments().addAll(model.getData().getInvestments());
//                    companyMapDataModel.getData().getInvestments().addAll(model.getData().getInvestments());
//                    companyMapDataModel.getData().getInvestments().addAll(model.getData().getInvestments());
//                    companyMapDataModel.getData().getInvestments().addAll(model.getData().getInvestments());
//                    companyMapDataModel.getData().getInvestments().addAll(model.getData().getInvestments());
//                    companyMapDataModel.getData().getInvestments().addAll(model.getData().getInvestments());
//                    companyMapDataModel.getData().getInvestments().addAll(model.getData().getInvestments());
//
//                    companyMapDataModel.getData().getShareholders().addAll(model.getData().getShareholders());
//                    companyMapDataModel.getData().getShareholders().addAll(model.getData().getShareholders());
//                    companyMapDataModel.getData().getShareholders().addAll(model.getData().getShareholders());
//                    companyMapDataModel.getData().getShareholders().addAll(model.getData().getShareholders());
//                    companyMapDataModel.getData().getShareholders().addAll(model.getData().getShareholders());
//                    companyMapDataModel.getData().getShareholders().addAll(model.getData().getShareholders());
//                    companyMapDataModel.getData().getShareholders().addAll(model.getData().getShareholders());
//
//                    updateView(companyMapDataModel);
                    updateView((CompanyMapDataModel) data);


                }
            }

            @Override
            public void onFail(String error) {
                sceneAnimation.stop();
                netWorkError.setNetWorkError();
                loading.setVisibility(View.GONE);
                netWorkError.setVisibility(View.VISIBLE);
            }
        });
    }

    /**
     * 设置字符串大小和颜色不一样
     * @param start 开始字符串
     * @param end 结束字符串
     * @param colorId 颜色值
     * @return
     */
    private SpannableString setTextSizeAndColor(String start, String end, int colorId){
        SpannableString spannableString = null;
        spannableString =new SpannableString(start+end);
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(colorId)), 0,
                start.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        spannableString.setSpan(new AbsoluteSizeSpan(PhoneUtil.dip2px(mContext, 20)), 0, start.length(),
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        return spannableString;
    }

    private void updateView(CompanyMapDataModel model){
        sceneAnimation.stop();
        if (layout!=null&&layout.getVisibility()== View.GONE)
            layout.setVisibility(View.VISIBLE);
        loading.setVisibility(View.GONE);
        if (model!=null){
            if (model.getResult()==0){
                netWorkError.setVisibility(View.GONE);
                mapModel=model.getData();
                mapModel.setcEntShortName(mCompanyName);
                lineDrawAnimView.refresh(mapModel.getShareholders(), mapModel.getInvestments(), 0, 8, mapModel.getcEntShortName());
                if (mapModel.getInvestments()==null){
                    mTouziTxt.setText(setTextSizeAndColor("0\n",mContext.getString(R.string.investment),R.color.investment));
                }else {
                    mTouziTxt.setText(setTextSizeAndColor(mapModel.getInvestments().size()+"\n"
                            ,mContext.getString(R.string.investment),R.color.investment));
                }
                if (mapModel.getShareholders()==null){
                    mGudongTxt.setText(setTextSizeAndColor("0\n",mContext.getString(R.string.shareholder),R.color.shareholder));
                }else {
                    mGudongTxt.setText(setTextSizeAndColor(mapModel.getShareholders().size()+"\n"
                            ,mContext.getString(R.string.shareholder),R.color.shareholder));
                }
            }else {
                netWorkError.setServerError();
                netWorkError.setVisibility(View.VISIBLE);
            }
        }
    }
}
