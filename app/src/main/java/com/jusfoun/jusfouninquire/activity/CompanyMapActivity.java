package com.jusfoun.jusfouninquire.activity;

import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.jusfoun.baselibrary.Util.PhoneUtil;
import com.jusfoun.baselibrary.Util.TouchUtil;
import com.jusfoun.baselibrary.base.BaseModel;
import com.jusfoun.jusfouninquire.TimeOut;
import com.qxb.jianhang.R;
import com.jusfoun.jusfouninquire.bean.CompanyMapDataModel;
import com.jusfoun.jusfouninquire.bean.CompanyMapDetailDataModel;
import com.jusfoun.jusfouninquire.bean.CompanyMapDetailModel;
import com.jusfoun.jusfouninquire.bean.CompanyMapModel;
import com.jusfoun.jusfouninquire.bean.InvestmentModel;
import com.jusfoun.jusfouninquire.bean.ShareholderModel;
import com.jusfoun.jusfouninquire.callback.NetWorkCallBack;
import com.jusfoun.jusfouninquire.route.GetCompanyMap;
import com.jusfoun.jusfouninquire.util.LoginSharePreference;
import com.jusfoun.jusfouninquire.view.GestureView;
import com.jusfoun.jusfouninquire.view.LineDrawAnimView;
import com.jusfoun.jusfouninquire.view.MyImageSpan;
import com.qxb.jianhang.ui.data.UserInfoModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author  JUSFOUN
 * CreateDate 2015/11/18.
 * Description
 */
public class CompanyMapActivity extends BaseInquireActivity {

    /**
     * 常量
     */
    public static final String COMPANY_ATLAS_KEY = "mapModel";
    public static final String COMPANY_ID = "company_id";
    public static final String COMPANY_NAME = "company_name";
    //图谱展示类型 1 全展示，2股东隐藏 3 投资隐藏 4 全隐藏;
    private static final int TYPEONE = 1;
    private static final int TYPETWO = 2;
    private static final int TYPETHREE = 3;
    private static final int TYPEFOUR = 4;

    /**
     * 组件
     */
    private RelativeLayout replece_layout, replece_title_relative_layout;
    private TextView replace_Text;
    private ImageView close_window_image, dropdown_orange;
    private LinearLayout replace_linearLayout, uofold_linearLayout;
    private CheckBox checkRed, checkBlue;
    private View mShadeView;

    //------------------ 右侧企业股东-------------------------------
    /**
     * 右侧表单式布局
     */
    private GestureView right_content_layout, right_Natural_man_content_layout;
    private TextView shareholder_invetment_Text, legal_value, industry_value, investmentAmount_value, stock_value, createDate_value, shareHolders_value, investmentToOut_value,
            ent_detail, full_atlas, unfold_atlas, retract_atlas,
            Natural_man_shareholder_Text, shareholder_type_value, contribution_money_value, Natural_man_stock_value, certificate_value, companyTitle, uofold_companyTitle, replace;

    private ImageView close_right_window, close_Natural_man_right_window;
    private LineDrawAnimView lineDrawAnimView;
    private LinearLayout certificate, natural_man_stock, contribution_money, shareholder_type, investmentToOut, shareHolders, createDate, stock, investmentAmount, industry, legal, company_no_data, bottom_choice_layout;
    private ScrollView scrollView;
    /**
     * 变量
     */
    public int page = 0, index = 8,temp;
    private boolean childIsUnfold = false;
    private String companyId, companyName, currentUnfoldId;
    private boolean noNeedSizeChange;
    private String type;
    private String name;

    /**
     * 对象
     */
    private TranslateAnimation animationShow, animationHide;
    private CompanyMapModel mapModel;
    private CompanyMapModel childAtlasModel;
    private BaseModel tempModel;

    @Override
    protected void initData() {
        super.initData();
        animationShow = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        animationShow.setDuration(500);
        animationHide = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        animationHide.setDuration(500);
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            Toast.makeText(mContext, "数据为空", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        companyId = bundle.getString(COMPANY_ID);
        companyName = bundle.getString(COMPANY_NAME);
        mapModel = (CompanyMapModel) bundle.get(COMPANY_ATLAS_KEY);
        if (mapModel.getInvestments() == null)
            mapModel.setInvestments(new ArrayList<InvestmentModel>());
        if (mapModel.getShareholders() == null)
            mapModel.setShareholders(new ArrayList<ShareholderModel>());
        if (mapModel == null) {
            mapModel = new CompanyMapModel();
            mapModel.setShareholders(new ArrayList<ShareholderModel>());
            mapModel.setInvestments(new ArrayList<InvestmentModel>());
        }
    }

    @Override
    protected void initView() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_company_map);
        lineDrawAnimView = (LineDrawAnimView) findViewById(R.id.line_draw_view);
        replece_title_relative_layout = (RelativeLayout) findViewById(R.id.replece_title_relative_layout);
        replece_layout = (RelativeLayout) findViewById(R.id.replece_layout);
        replace_Text = (TextView) findViewById(R.id.replace_Text);
        close_window_image = (ImageView) findViewById(R.id.close_window_image);
        //企业公司的布局组件
        right_content_layout = (GestureView) findViewById(R.id.right_content_layout);
        close_right_window = (ImageView) findViewById(R.id.close_right_window);
        replace_linearLayout = (LinearLayout) findViewById(R.id.replace_linearLayout);
        uofold_linearLayout = (LinearLayout) findViewById(R.id.uofold_linearLayout);
        shareholder_invetment_Text = (TextView) findViewById(R.id.shareholder_invetment_Text);
        legal_value = (TextView) findViewById(R.id.legal_value);
        industry_value = (TextView) findViewById(R.id.industry_value);
        investmentAmount_value = (TextView) findViewById(R.id.investmentAmount_value);
        stock_value = (TextView) findViewById(R.id.stock_value);
        createDate_value = (TextView) findViewById(R.id.createDate_value);
        ent_detail = (TextView) findViewById(R.id.ent_detail);
        full_atlas = (TextView) findViewById(R.id.full_atlas);
        unfold_atlas = (TextView) findViewById(R.id.unfold_atlas);
        retract_atlas = (TextView) findViewById(R.id.retract_atlas);
        shareHolders_value = (TextView) findViewById(R.id.shareHolders_value);
        investmentToOut_value = (TextView) findViewById(R.id.investmentToOut_value);
        //自然人股东的布局组件
        right_Natural_man_content_layout = (GestureView) findViewById(R.id.right_Natural_man_content_layout);
        close_Natural_man_right_window = (ImageView) findViewById(R.id.close_Natural_man_right_window);
        Natural_man_shareholder_Text = (TextView) findViewById(R.id.Natural_man_shareholder_Text);
        shareholder_type_value = (TextView) findViewById(R.id.shareholder_type_value);
        contribution_money_value = (TextView) findViewById(R.id.contribution_money_value);
        Natural_man_stock_value = (TextView) findViewById(R.id.Natural_man_stock_value);
        certificate_value = (TextView) findViewById(R.id.certificate_value);

        uofold_companyTitle = (TextView) findViewById(R.id.uofold_companyTitle);
        companyTitle = (TextView) findViewById(R.id.companyTitle);
        dropdown_orange = (ImageView) findViewById(R.id.dropdown_orange);
        replace = (TextView) findViewById(R.id.replace);

        certificate = (LinearLayout) findViewById(R.id.certificate);
        natural_man_stock = (LinearLayout) findViewById(R.id.natural_man_stock);
        contribution_money = (LinearLayout) findViewById(R.id.contribution_money);
        shareholder_type = (LinearLayout) findViewById(R.id.shareholder_type);
        investmentToOut = (LinearLayout) findViewById(R.id.investmentToOut);
        shareHolders = (LinearLayout) findViewById(R.id.shareHolders);
        createDate = (LinearLayout) findViewById(R.id.createDate);
        stock = (LinearLayout) findViewById(R.id.stock);
        investmentAmount = (LinearLayout) findViewById(R.id.investmentAmount);
        industry = (LinearLayout) findViewById(R.id.industry);
        legal = (LinearLayout) findViewById(R.id.legal);

        checkRed = (CheckBox) findViewById(R.id.check_red);
        checkBlue = (CheckBox) findViewById(R.id.check_blue);
        company_no_data = (LinearLayout) findViewById(R.id.company_no_data);
        bottom_choice_layout = (LinearLayout) findViewById(R.id.bottom_choice_layout);
        scrollView = (ScrollView) findViewById(R.id.scrollView);

        mShadeView=findViewById(R.id.shade_view);
    }

    private int lastClickCount = -1;
    private int lastClickType = -1;
    private int clickCount = -1;
    private int[] listSize = new int[2];

    @Override
    protected void initWidgetActions() {

        TouchUtil.createTouchDelegate(close_right_window,
                PhoneUtil.dip2px(mContext, 60), PhoneUtil.dip2px(mContext, 60), PhoneUtil.dip2px(mContext, 60), PhoneUtil.dip2px(mContext, 150));
        TouchUtil.createTouchDelegate(close_Natural_man_right_window,
                PhoneUtil.dip2px(mContext, 60), PhoneUtil.dip2px(mContext, 60), PhoneUtil.dip2px(mContext, 60), PhoneUtil.dip2px(mContext, 150));
        TouchUtil.createTouchDelegate(close_window_image,
                PhoneUtil.dip2px(mContext, 60), PhoneUtil.dip2px(mContext, 60), PhoneUtil.dip2px(mContext, 60), PhoneUtil.dip2px(mContext, 60));

        lineDrawAnimView.setAllAnimStop(false);
        lineDrawAnimView.setmRaiusRatio(1);
        lineDrawAnimView.setClickPot(new LineDrawAnimView.OnClickPotListener() {
            @Override
            public void onClickPot(int type, int clickable, BaseModel baseModel) {

                if (right_Natural_man_content_layout.getVisibility() == View.VISIBLE) {
                    right_Natural_man_content_layout.startAnimation(animationHide);
                    right_Natural_man_content_layout.setVisibility(View.GONE);
                    mShadeView.setVisibility(View.GONE);
                }

                if (right_content_layout.getVisibility() == View.VISIBLE) {
                    right_content_layout.startAnimation(animationHide);
                    right_content_layout.setVisibility(View.GONE);
                    mShadeView.setVisibility(View.GONE);
                }

                clickCount = clickable;
//                if (clickable == lastClickCount && lastClickCount != -1 && type == lastClickType) {
//                    lastClickCount = -1;
//                    return;
//                }
                lastClickType = type;
                lastClickCount = clickable;
                if (baseModel != null) {
//                        if (baseModel instanceof ShareholderModel) {
//                            setCompanyGuDong((ShareholderModel) baseModel, type);
//                        }
//                        else if (baseModel instanceof InvestmentModel) {
//                        }
                    getCompanyAtlas(companyId, baseModel);
                }
            }

        });

        lineDrawAnimView.setOnTouchView(new LineDrawAnimView.OnTouchView() {
            @Override
            public void onTouch() {
                if (replece_layout.getVisibility() == View.VISIBLE) {
                    replece_layout.setVisibility(View.GONE);
                }
            }
        });

        lineDrawAnimView.setViewSizeChange(new LineDrawAnimView.OnViewSizeChange() {
            @Override
            public void onSizeChange() {

                if (noNeedSizeChange) {
                    return;
                }
                noNeedSizeChange = true;
                if (right_Natural_man_content_layout.getVisibility() == View.VISIBLE) {
                    right_Natural_man_content_layout.startAnimation(animationHide);
                    right_Natural_man_content_layout.setVisibility(View.GONE);
                    mShadeView.setVisibility(View.GONE);
                }

                if (right_content_layout.getVisibility() == View.VISIBLE) {
                    right_content_layout.startAnimation(animationHide);
                    right_content_layout.setVisibility(View.GONE);
                    mShadeView.setVisibility(View.GONE);
                }
                setShareholder_invetment_replaceBtn(mapModel);
                page = 0;
                swicthRefresh();
                page++;
                childIsUnfold = false;

            }
        });

        unfold_atlas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCompanyAtlas(childCompanyId == null ? "" : childCompanyId);
                childIsUnfold = true;
                currentUnfoldId = childCompanyId;
                right_content_layout.startAnimation(animationHide);
                right_content_layout.setVisibility(View.GONE);
                mShadeView.setVisibility(View.GONE);
                replece_title_relative_layout.setEnabled(true);
                replace_Text.setTextColor(getResources().getColor(R.color.company_map_cleck_text));
                replace_Text.setCompoundDrawablesWithIntrinsicBounds
                        (getResources().getDrawable(R.mipmap.refresh_red), null, null, null);
                dropdown_orange.setVisibility(View.VISIBLE);
            }
        });

        retract_atlas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (childAtlasModel != null)
                    lineDrawAnimView.startClickAnim(clickCount, childAtlasModel);
                childIsUnfold = false;
                currentUnfoldId = "";
                right_content_layout.startAnimation(animationHide);
                right_content_layout.setVisibility(View.GONE);
                mShadeView.setVisibility(View.GONE);
                dropdown_orange.setVisibility(View.GONE);
                int type = getSwicthType();
                switch (type) {
                    case TYPETWO:
                        if (mapModel.getInvestments().size() < index * 2) {
                            replece_title_relative_layout.setEnabled(false);
                            replace_Text.setTextColor(getResources().getColor(R.color.company_map_un_click_text));
                            replace_Text.setCompoundDrawablesWithIntrinsicBounds
                                    (getResources().getDrawable(R.mipmap.refresh_grey), null, null, null);
                        } else {
                            replece_title_relative_layout.setEnabled(true);
                            replace_Text.setTextColor(getResources().getColor(R.color.company_map_cleck_text));
                            replace_Text.setCompoundDrawablesWithIntrinsicBounds
                                    (getResources().getDrawable(R.mipmap.refresh_red), null, null, null);
                        }
                        break;
                    case TYPETHREE:
                        if (mapModel.getShareholders().size() < index * 2) {
                            replece_title_relative_layout.setEnabled(false);
                            replace_Text.setTextColor(getResources().getColor(R.color.company_map_un_click_text));
                            replace_Text.setCompoundDrawablesWithIntrinsicBounds
                                    (getResources().getDrawable(R.mipmap.refresh_grey), null, null, null);
                        } else {
                            replece_title_relative_layout.setEnabled(true);
                            replace_Text.setTextColor(getResources().getColor(R.color.company_map_cleck_text));
                            replace_Text.setCompoundDrawablesWithIntrinsicBounds
                                    (getResources().getDrawable(R.mipmap.refresh_red), null, null, null);
                        }
                        break;
                }
            }
        });

        full_atlas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                right_content_layout.startAnimation(animationHide);
                right_content_layout.setVisibility(View.GONE);
                mShadeView.setVisibility(View.GONE);
                page = 0;
                if (lastClickType == LineDrawAnimView.CHILD)
                    getCompanyAtlas(childCompanyId == null ? "" : childCompanyId);
                else
                    unfoldChildAtlas();
            }
        });


        close_right_window.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lastClickCount = -1;
                right_content_layout.startAnimation(animationHide);
                right_content_layout.setVisibility(View.GONE);
                mShadeView.setVisibility(View.GONE);
            }
        });
        close_Natural_man_right_window.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lastClickCount = -1;
                right_Natural_man_content_layout.startAnimation(animationHide);
                right_Natural_man_content_layout.setVisibility(View.GONE);
                mShadeView.setVisibility(View.GONE);
            }
        });

        close_window_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO
                finish();
            }
        });

        checkRed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!checkRed.isEnabled())
                    return;
                if (!b) {
                    if (!checkBlue.isChecked()) {
                        Toast toast = Toast.makeText(mContext, "不可全部隐藏", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP | Gravity.LEFT, PhoneUtil.dip2px(mContext, 20), PhoneUtil.dip2px(mContext, 60));
                        toast.show();
                        checkRed.setChecked(true);
                        return;
                    }
                    if (right_Natural_man_content_layout.getVisibility() == View.VISIBLE) {
                        right_Natural_man_content_layout.startAnimation(animationHide);
                        right_Natural_man_content_layout.setVisibility(View.GONE);
                        mShadeView.setVisibility(View.GONE);
                    }

                    if (right_content_layout.getVisibility() == View.VISIBLE) {
                        right_content_layout.startAnimation(animationHide);
                        right_content_layout.setVisibility(View.GONE);
                        mShadeView.setVisibility(View.GONE);
                    }
                    if (replece_layout.getVisibility()== View.VISIBLE)
                        replece_layout.setVisibility(View.GONE);
                    dropdown_orange.setVisibility(View.GONE);
                    if (mapModel.getInvestments().size() < index * 2) {
                        replece_title_relative_layout.setEnabled(false);
                        replace_Text.setTextColor(getResources().getColor(R.color.company_map_un_click_text));
                        replace_Text.setCompoundDrawablesWithIntrinsicBounds
                                (getResources().getDrawable(R.mipmap.refresh_grey), null, null, null);
                    } else {
                        replece_title_relative_layout.setEnabled(true);
                        replace_Text.setTextColor(getResources().getColor(R.color.company_map_cleck_text));
                        replace_Text.setCompoundDrawablesWithIntrinsicBounds
                                (getResources().getDrawable(R.mipmap.refresh_red), null, null, null);
                    }
                } else {
                    checkRed.setTextColor(getResources().getColor(R.color.company_map_cleck_text));
                    setShareholder_invetment_replaceBtn(mapModel);
                }
                page = 0;
                swicthRefresh();
                page++;
            }
        });

        checkBlue.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (!checkBlue.isEnabled())
                    return;
                if (!b) {
                    if (!checkRed.isChecked()) {
                        Toast toast = Toast.makeText(mContext, "不可全部隐藏", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP | Gravity.LEFT, PhoneUtil.dip2px(mContext, 160), PhoneUtil.dip2px(mContext, 60));
                        toast.show();
                        checkBlue.setChecked(true);
                        return;
                    }
                    if (right_Natural_man_content_layout.getVisibility() == View.VISIBLE) {
                        right_Natural_man_content_layout.startAnimation(animationHide);
                        right_Natural_man_content_layout.setVisibility(View.GONE);
                        mShadeView.setVisibility(View.GONE);
                    }

                    if (right_content_layout.getVisibility() == View.VISIBLE) {
                        right_content_layout.startAnimation(animationHide);
                        right_content_layout.setVisibility(View.GONE);
                        mShadeView.setVisibility(View.GONE);
                    }
                    if (replece_layout.getVisibility()== View.VISIBLE)
                        replece_layout.setVisibility(View.GONE);
                    dropdown_orange.setVisibility(View.GONE);
                    if (mapModel.getShareholders().size() < index * 2) {
                        replece_title_relative_layout.setEnabled(false);
                        replace_Text.setTextColor(getResources().getColor(R.color.company_map_un_click_text));
                        replace_Text.setCompoundDrawablesWithIntrinsicBounds
                                (getResources().getDrawable(R.mipmap.refresh_grey), null, null, null);
                    } else {
                        replece_title_relative_layout.setEnabled(true);
                        replace_Text.setTextColor(getResources().getColor(R.color.company_map_cleck_text));
                        replace_Text.setCompoundDrawablesWithIntrinsicBounds
                                (getResources().getDrawable(R.mipmap.refresh_red), null, null, null);
                    }
                } else {
                    checkBlue.setTextColor(getResources().getColor(R.color.company_map_cleck_text));
                    setShareholder_invetment_replaceBtn(mapModel);
                }
                page = 0;
                swicthRefresh();
                page++;
            }
        });

        replece_title_relative_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("tag","replece_title_relative_layout1");

                if (right_content_layout.getVisibility() == View.VISIBLE) {
                    right_content_layout.startAnimation(animationHide);
                    right_content_layout.setVisibility(View.GONE);
                    mShadeView.setVisibility(View.GONE);
                }
                Log.e("tag","replece_title_relative_layout2");
                if (right_Natural_man_content_layout.getVisibility() == View.VISIBLE) {
                    right_Natural_man_content_layout.startAnimation(animationHide);
                    right_Natural_man_content_layout.setVisibility(View.GONE);
                    mShadeView.setVisibility(View.GONE);
                }
                if (!childIsUnfold) {
                    replace();
                } else {
                    Log.e("tag","replece_title_relative_layout4");
                    if (replece_layout.getVisibility() == View.VISIBLE)
                        replece_layout.setVisibility(View.GONE);
                    else
                        replece_layout.setVisibility(View.VISIBLE);
                    if (mapModel != null) {
                        companyTitle.setText(mapModel.getcEntShortName().length() > 3
                                ? mapModel.getcEntShortName().substring(0, 3) + "..."
                                : mapModel.getcEntShortName() + "...");
                        setReplaceEnable();

                    }

                    if (childAtlasModel != null) {
                        uofold_companyTitle.setText(childAtlasModel.getcEntShortName().length() > 3
                                ? childAtlasModel.getcEntShortName().substring(0, 3) + "..."
                                : childAtlasModel.getcEntShortName() + "...");
                    }
                }
            }
        });

        replace_linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replace();
                childIsUnfold = false;
                dropdown_orange.setVisibility(View.GONE);
                replece_layout.setVisibility(View.GONE);
            }
        });
        uofold_linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unfoldChildAtlas();
                dropdown_orange.setVisibility(View.GONE);
                replece_layout.setVisibility(View.GONE);
            }
        });

        ent_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                right_content_layout.startAnimation(animationHide);
                right_content_layout.setVisibility(View.GONE);
                mShadeView.setVisibility(View.GONE);
                Bundle bundle = new Bundle();
                bundle.putString(CompanyDetailActivity.COMPANY_ID, childCompanyId);
                bundle.putString(CompanyDetailActivity.COMPANY_NAME, companyName);
                goActivity(CompanyDetailActivity.class, bundle);
            }
        });

        right_content_layout.setOnGestureListener(new GestureView.OnGestureListener() {
            @Override
            public void onGesture() {
                if (right_content_layout.getVisibility() == View.VISIBLE) {
                    right_content_layout.startAnimation(animationHide);
                    right_content_layout.setVisibility(View.GONE);
                    mShadeView.setVisibility(View.GONE);
                }
            }
        });

        right_Natural_man_content_layout.setOnGestureListener(new GestureView.OnGestureListener() {
            @Override
            public void onGesture() {
                if (right_Natural_man_content_layout.getVisibility() == View.VISIBLE) {
                    right_Natural_man_content_layout.startAnimation(animationHide);
                    right_Natural_man_content_layout.setVisibility(View.GONE);
                    mShadeView.setVisibility(View.GONE);
                }
            }
        });

        mShadeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (right_Natural_man_content_layout.getVisibility() == View.VISIBLE) {
                    right_Natural_man_content_layout.startAnimation(animationHide);
                    right_Natural_man_content_layout.setVisibility(View.GONE);
                    mShadeView.setVisibility(View.GONE);
                }else if (right_content_layout.getVisibility() == View.VISIBLE) {
                    right_content_layout.startAnimation(animationHide);
                    right_content_layout.setVisibility(View.GONE);
                    mShadeView.setVisibility(View.GONE);
                }
            }
        });
        noNeedSizeChange = false;
        setShareholder_invetment_replaceBtn(mapModel);
    }

    @Override
    protected void onPause() {
        super.onPause();
        lineDrawAnimView.setAllAnimStop(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        lineDrawAnimView.setAllAnimStop(false);
    }

    /**
     * 获取企业图谱
     *
     * @param companyId
     */
    private void getCompanyAtlas(String companyId) {


        Log.e("tag","getCompanyAtlas1");
        TimeOut timeOut = new TimeOut(mContext);
        HashMap<String, String> params = new HashMap<>();
        String userid = "";
        UserInfoModel userInfoModel = LoginSharePreference.getUserInfo(mContext);
        if (userInfoModel != null && !TextUtils.isEmpty(userInfoModel.getUserid())){
            userid = userInfoModel.getUserid();
        }
        params.put("userid",userid);
        params.put("entid", companyId);
        params.put("entname", companyName);
        params.put("t", timeOut.getParamTimeMollis()+"");
        params.put("m", timeOut.MD5time()+"");
        Log.e("tag","getCompanyAtlas2");

        showLoading();
        GetCompanyMap.getCompanyMap(mContext, params, getLocalClassName(),new NetWorkCallBack() {
            @Override
            public void onSuccess(Object data) {
                hideLoadDialog();
                updateView(((CompanyMapDataModel) data).getData());
            }

            @Override
            public void onFail(String error) {
                hideLoadDialog();
                currentUnfoldId = "";
                if (lastClickType != LineDrawAnimView.CHILD)
                    childIsUnfold = false;
                Toast.makeText(mContext, R.string.net_error, Toast.LENGTH_LONG).show();
            }
        });

    }

    /**
     * 获取企业图谱详情
     *
     * @param companyId
     * @param model
     */
    private void getCompanyAtlas(String companyId, BaseModel model) {
        HashMap<String, String> params = new HashMap<>();
        params.put("entid", companyId);
        if (model instanceof ShareholderModel) {
            ShareholderModel shareholderModel= (ShareholderModel) model;
            params.put("type", "1");
            if (shareholderModel.getType() == 2) {
                type="0";
                params.put("shareholdername", shareholderModel.getName());
                name=shareholderModel.getName();
            } else if (shareholderModel.getType() == 1) {
                type="1";
                if (TextUtils.isEmpty(shareholderModel.getEntId())
                        && TextUtils.isEmpty(shareholderModel.getCompanyName())){
                    Toast.makeText(mContext,"公司信息为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                params.put("companyid", shareholderModel.getEntId());
                params.put("entName",shareholderModel.getCompanyName());
                name=shareholderModel.getCompanyName();
            }
        } else if (model instanceof InvestmentModel) {
            InvestmentModel investmentModel= (InvestmentModel) model;
            params.put("type","2");
            type="2";

            if (TextUtils.isEmpty(investmentModel.getEntId())
                    && TextUtils.isEmpty(investmentModel.getCompanyName())){
                Toast.makeText(mContext,"公司信息为空", Toast.LENGTH_SHORT).show();
            }
            params.put("companyid", ((InvestmentModel) model).getEntId());
            params.put("entName", ((InvestmentModel) model).getCompanyName());
            name=((InvestmentModel) model).getCompanyName();
        }
        showLoading();
        GetCompanyMap.getCompanyMapDetail(mContext, params,getLocalClassName(), new NetWorkCallBack() {
            @Override
            public void onSuccess(Object data) {
                hideLoadDialog();
                updateInfo((CompanyMapDetailDataModel) data,name);
            }

            @Override
            public void onFail(String error) {
                hideLoadDialog();
                currentUnfoldId = "";
                if (lastClickType != LineDrawAnimView.CHILD)
                    childIsUnfold = false;
                Toast.makeText(mContext, R.string.net_error, Toast.LENGTH_LONG).show();
            }
        });

    }

    private String childCompanyId="";
    /**
     * 更新右侧公司或投资人信息
     *
     * @param model
     */
    private void updateInfo(CompanyMapDetailDataModel model,String name) {
        CompanyMapDetailModel dataModel = model.getData();
        if ("0".equals(type)) {

            if (right_Natural_man_content_layout.getVisibility() == View.GONE) {
                right_Natural_man_content_layout.startAnimation(animationShow);
                right_Natural_man_content_layout.setVisibility(View.VISIBLE);
                mShadeView.setVisibility(View.VISIBLE);
            }
            if (!TextUtils.isEmpty(dataModel.getName())) {
                Natural_man_shareholder_Text.setText(dataModel.getName());
                        image_textBuilder(dataModel.getName(), R.mipmap.label_gudong, Natural_man_shareholder_Text);
            }else {
                Natural_man_shareholder_Text.setText(name);
                image_textBuilder(name, R.mipmap.label_gudong, Natural_man_shareholder_Text);
            }

            if (!TextUtils.isEmpty(dataModel.getStock())) {
                natural_man_stock.setVisibility(View.VISIBLE);
                Natural_man_stock_value.setText(dataModel.getStock());
            } else {
                natural_man_stock.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(dataModel.getInvestmentAmount())) {
                contribution_money.setVisibility(View.VISIBLE);
                contribution_money_value.setText(dataModel.getInvestmentAmount());
            } else {
                contribution_money.setVisibility(View.GONE);
            }
            shareholder_type_value.setText("自然人股东");

            if (!TextUtils.isEmpty(dataModel.getPapers())) {
                certificate.setVisibility(View.VISIBLE);
                certificate_value.setText(dataModel.getPapers());
            } else {
                certificate.setVisibility(View.GONE);
            }
        } else if ("1".equals(type)||"2".equals(type)) {
            if (right_content_layout.getVisibility() == View.GONE) {
                mShadeView.setVisibility(View.VISIBLE);
                right_content_layout.startAnimation(animationShow);
                right_content_layout.setVisibility(View.VISIBLE);
//                darkenBackgroud(0.4f);
            }
            if (!TextUtils.isEmpty(dataModel.getCompanyName())) {
                shareholder_invetment_Text.setText(dataModel.getCompanyName());
                if ("1".equals(type))
                        image_textBuilder(dataModel.getCompanyName(), R.mipmap.label_gudong, shareholder_invetment_Text);
                else
                    image_textBuilder(dataModel.getCompanyName(), R.mipmap.touzi, shareholder_invetment_Text);
            }else {
                shareholder_invetment_Text.setText(name);
                if ("1".equals(type))
                    image_textBuilder(name, R.mipmap.label_gudong, shareholder_invetment_Text);
                else
                    image_textBuilder(name, R.mipmap.touzi, shareholder_invetment_Text);
            }
            String companyId = dataModel.getEntId();
            if (!TextUtils.isEmpty(companyId)){
                childCompanyId=companyId;
            }

            if (TextUtils.isEmpty(companyId)) {
                companyName = dataModel.getCompanyName();

                bottom_choice_layout.setVisibility(View.GONE);
                scrollView.setVisibility(View.GONE);
                company_no_data.setVisibility(View.VISIBLE);
            } else {
                bottom_choice_layout.setVisibility(View.VISIBLE);
                scrollView.setVisibility(View.VISIBLE);
                company_no_data.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(dataModel.getLegal())) {
                legal.setVisibility(View.VISIBLE);
                legal_value.setText(dataModel.getLegal());
            } else {
                legal.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(dataModel.getIndustry())) {
                industry.setVisibility(View.VISIBLE);
                industry_value.setText(dataModel.getIndustry());
            } else {
                industry.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(dataModel.getInvestmentAmount())) {
                investmentAmount.setVisibility(View.VISIBLE);
                investmentAmount_value.setText(dataModel.getInvestmentAmount());
            } else {
                investmentAmount.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(dataModel.getStock())) {
                stock.setVisibility(View.VISIBLE);
                stock_value.setText(dataModel.getStock());
            } else {
                stock.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(dataModel.getCreateDate())) {
                createDate.setVisibility(View.VISIBLE);
                createDate_value.setText(dataModel.getCreateDate());
            } else {
                createDate.setVisibility(View.GONE);
            }

            shareHolders_value.setText(dataModel.getShareHolders() + "");
            investmentToOut_value.setText(dataModel.getInvestmentToOut() + "");
            checkMapButton(dataModel);
        }
    }

    protected void updateView(CompanyMapModel model) {

        Log.d("TAG", model.toString());
        if (model.getResult() == 0) {
            if (model != null) {
                if (lastClickType == LineDrawAnimView.CHILD) {
                    mapModel = model;
                    setShareholder_invetment_replaceBtn(mapModel);
                    page = 0;
                    swicthRefresh();
                    page++;
                } else {
                    childAtlasModel = model;
                    int count=0;
                    for (ShareholderModel shareholderModel:model.getShareholders()){
                        if (shareholderModel.getEntId().equals(companyId)){
                            model.getShareholders().remove(shareholderModel);
                            tempModel=shareholderModel;
                            temp=count;
                            break;
                        }
                        count++;
                    }
                    count=0;
                    for (InvestmentModel investmentModel:model.getInvestments()){
                        if (investmentModel.getEntId().equals(companyId)){
                            model.getInvestments().remove(investmentModel);
                            tempModel=investmentModel;
                            temp=count;
                            break;
                        }
                        count++;
                    }
                    lineDrawAnimView.startClickAnim(clickCount, childAtlasModel);
                }
            } else {
                currentUnfoldId = "";
            }
        }
    }

    private void image_textBuilder(String str, int imageId, TextView textview) {
        String text = str+" " + "★";
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        String rexgString = "★";
        Pattern pattern = Pattern.compile(rexgString);
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            builder.setSpan(
                    new MyImageSpan(this, imageId), matcher.start(), matcher
                            .end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        textview.setText(builder);
    }

    private void swicthRefresh() {
        int type = getSwicthType();
        childIsUnfold = false;
        currentUnfoldId = "";
        switch (type) {
            case TYPEONE:
                lineDrawAnimView.refresh(mapModel.getShareholders(), mapModel.getInvestments(), page, index, mapModel.getcEntShortName());
                setComCount(TYPEONE, mapModel.getShareholders().size(), mapModel.getInvestments().size());
                break;
            case TYPETWO:
                lineDrawAnimView.refresh(new ArrayList<ShareholderModel>(), mapModel.getInvestments(), page, index, mapModel.getcEntShortName());
                lineDrawAnimView.getListSize(listSize);
                setComCount(TYPETWO, mapModel.getShareholders().size(), mapModel.getInvestments().size());
                break;
            case TYPETHREE:
                lineDrawAnimView.refresh(mapModel.getShareholders(), new ArrayList<InvestmentModel>(), page, index, mapModel.getcEntShortName());
                lineDrawAnimView.getListSize(listSize);
                setComCount(TYPETHREE, mapModel.getShareholders().size(), mapModel.getInvestments().size());
                break;
            case TYPEFOUR:
                lineDrawAnimView.refresh(new ArrayList<ShareholderModel>(), new ArrayList<InvestmentModel>(), page, index, mapModel.getcEntShortName());
                setComCount(TYPEFOUR, mapModel.getShareholders().size(), mapModel.getInvestments().size());
                break;
        }
    }

    private int getSwicthType() {
        if (checkRed.isChecked() && checkBlue.isChecked()) {
            return TYPEONE;
        } else if (checkBlue.isChecked()) {
            return TYPETWO;
        } else if (checkRed.isChecked()) {
            return TYPETHREE;
        } else {
            return TYPEFOUR;
        }
    }

    public void unfoldChildAtlas() {
        if (childAtlasModel != null) {
            mapModel = childAtlasModel;
            companyId=childCompanyId;
            if (tempModel!=null){
                if (tempModel instanceof ShareholderModel){
                    mapModel.getShareholders().add(temp, (ShareholderModel) tempModel);
                }else if (tempModel instanceof InvestmentModel){
                    mapModel.getInvestments().add(temp, (InvestmentModel) tempModel);
                }
            }
            setShareholder_invetment_replaceBtn(mapModel);
            page = 0;
            swicthRefresh();
            page++;
        } else {
            Toast.makeText(mContext, "获取公司信息失败", Toast.LENGTH_SHORT).show();
        }

    }

    private void checkMapButton(CompanyMapDetailModel model) {
        int count = 0;
        String companyId = "";
        if (TextUtils.isEmpty(currentUnfoldId))
            currentUnfoldId = "";
        if (model != null
                && !TextUtils.isEmpty(model.getInvestmentToOut())
                && !TextUtils.isEmpty(model.getShareHolders())) {
            try {
                count = Integer.parseInt(model.getInvestmentToOut())
                        + Integer.parseInt(model.getShareHolders());
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            companyId = model.getEntId();
        }
        if (count==1){
            unfold_atlas.setVisibility(View.INVISIBLE);
            retract_atlas.setVisibility(View.INVISIBLE);
            full_atlas.setVisibility(View.INVISIBLE);
            ent_detail.setVisibility(View.VISIBLE);
            return;
        }
        if (count > 0 && !TextUtils.isEmpty(companyId)) {
            if (!currentUnfoldId.equals(companyId)) {
                if (lastClickType == LineDrawAnimView.CHILD) {
                    unfold_atlas.setVisibility(View.INVISIBLE);
                    full_atlas.setVisibility(View.VISIBLE);
                } else {
                    unfold_atlas.setVisibility(View.VISIBLE);
                    full_atlas.setVisibility(View.INVISIBLE);
                }
                retract_atlas.setVisibility(View.GONE);
                ent_detail.setVisibility(View.VISIBLE);
            } else {
                if (lastClickType == LineDrawAnimView.CHILD) {
                    retract_atlas.setVisibility(View.GONE);

                } else {
                    retract_atlas.setVisibility(View.VISIBLE);
                }
                unfold_atlas.setVisibility(View.GONE);
                full_atlas.setVisibility(View.VISIBLE);
                ent_detail.setVisibility(View.VISIBLE);
            }
        } else {
            unfold_atlas.setVisibility(View.INVISIBLE);
            retract_atlas.setVisibility(View.INVISIBLE);
            full_atlas.setVisibility(View.INVISIBLE);
            ent_detail.setVisibility(View.VISIBLE);
        }
    }

    private void setShareholder_invetment_replaceBtn(CompanyMapModel model) {
        if (model == null){
            return;
        }

        dropdown_orange.setVisibility(View.GONE);
        if (model.getShareholders() != null && model.getInvestments() != null) {
            if (model.getShareholders().size() > 0) {
                checkRed.setButtonDrawable(R.drawable.selector_bg_check_red);
                checkRed.setTextColor(getResources().getColor(R.color.company_map_cleck_text));
                checkRed.setChecked(true);
                checkRed.setEnabled(true);
            } else {
                checkRed.setEnabled(false);
                checkRed.setTextColor(getResources().getColor(R.color.company_map_un_click_text));
                checkRed.setChecked(false);
                checkRed.setButtonDrawable(R.mipmap.tick_grey);
            }

            if (model.getInvestments().size() > 0) {
                checkBlue.setButtonDrawable(R.drawable.selector_bg_check_blue);
                checkBlue.setTextColor(getResources().getColor(R.color.company_map_cleck_text));
                checkBlue.setChecked(true);
                checkBlue.setEnabled(true);
            } else {
                checkBlue.setEnabled(false);
                checkBlue.setTextColor(getResources().getColor(R.color.company_map_un_click_text));
                checkBlue.setChecked(false);
                checkBlue.setButtonDrawable(R.mipmap.tick_grey);
            }

            if (model.getInvestments().size() == 0 || model.getShareholders().size() == 0) {
                if ((model.getShareholders().size() + model.getInvestments().size()) <= index * 2) {
                    replece_title_relative_layout.setEnabled(false);
                    replace_Text.setTextColor(getResources().getColor(R.color.company_map_un_click_text));
                    replace_Text.setCompoundDrawablesWithIntrinsicBounds
                            (getResources().getDrawable(R.mipmap.refresh_grey), null, null, null);
                } else {
                    replece_title_relative_layout.setEnabled(true);
                    replace_Text.setTextColor(getResources().getColor(R.color.company_map_cleck_text));
                    replace_Text.setCompoundDrawablesWithIntrinsicBounds
                            (getResources().getDrawable(R.mipmap.refresh_red), null, null, null);
                }
            } else {
                if (model.getShareholders().size() <= index && model.getInvestments().size() <= index) {
                    replece_title_relative_layout.setEnabled(false);
                    replace_Text.setTextColor(getResources().getColor(R.color.company_map_un_click_text));
                    replace_Text.setCompoundDrawablesWithIntrinsicBounds
                            (getResources().getDrawable(R.mipmap.refresh_grey), null, null, null);
                } else {
                    replece_title_relative_layout.setEnabled(true);
                    replace_Text.setTextColor(getResources().getColor(R.color.company_map_cleck_text));
                    replace_Text.setCompoundDrawablesWithIntrinsicBounds
                            (getResources().getDrawable(R.mipmap.refresh_red), null, null, null);
                }
            }

        }
    }

    private void setComCount(int type, int shareholderCount, int invetmentCount) {
        lineDrawAnimView.getListSize(listSize);
        switch (type) {
            case TYPEONE:
                checkRed.setText("股东(" + listSize[0] + "/" + shareholderCount + ")");
                checkBlue.setText("投资(" + listSize[1] + "/" + invetmentCount + ")");
                break;
            case TYPETWO:
                checkRed.setText("股东(0/" + mapModel.getShareholders().size() + ")");
                checkBlue.setText("投资(" + (listSize[0] + listSize[1]) + "/" + invetmentCount + ")");
                break;
            case TYPETHREE:
                checkRed.setText("股东(" + (listSize[0] + listSize[1]) + "/" + shareholderCount + ")");
                checkBlue.setText("投资(0/" + mapModel.getInvestments().size() + ")");
                break;
            case TYPEFOUR:
                checkRed.setText("股东(0/0)");
                checkBlue.setText("投资(0/0)");
                break;
        }
    }

    private void setReplaceEnable() {
        int type = getSwicthType();
        switch (type) {
            case TYPEONE:
                if (mapModel.getInvestments().size() <= index && mapModel.getShareholders().size() <= index) {
                    replace_linearLayout.setEnabled(false);
                    replace.setTextColor(getResources().getColor(R.color.company_map_un_click_text));
                } else {
                    replace_linearLayout.setEnabled(true);
                    replace.setTextColor(getResources().getColor(R.color.company_map_cleck_text));
                }
                break;
            case TYPETWO:
                if (mapModel.getInvestments().size() <= index * 2) {
                    replace_linearLayout.setEnabled(false);
                    replace.setTextColor(getResources().getColor(R.color.company_map_un_click_text));
                } else {
                    replace_linearLayout.setEnabled(true);
                    replace.setTextColor(getResources().getColor(R.color.company_map_cleck_text));
                }
                break;
            case TYPETHREE:
                if (mapModel.getShareholders().size() <= index * 2) {
                    replace_linearLayout.setEnabled(false);
                    replace.setTextColor(getResources().getColor(R.color.company_map_un_click_text));
                } else {
                    replace_linearLayout.setEnabled(true);
                    replace.setTextColor(getResources().getColor(R.color.company_map_cleck_text));
                }
                break;
            case TYPEFOUR:
                replace_linearLayout.setEnabled(false);
                replace.setTextColor(getResources().getColor(R.color.company_map_un_click_text));
                break;
        }
    }

    public void replace() {
        Log.e("tag","replece_title_relative_layout3");
        int type = getSwicthType();
        switch (type) {
            case TYPEONE:
                Log.e("tag","replece_title_relative_layout4");
                if (mapModel.getShareholders().size() <= page * index
                        && mapModel.getInvestments().size() <= page * index) {
                    page = 0;
                    swicthRefresh();
                    page++;
                } else {
                    swicthRefresh();
                    page++;
                }
                break;
            case TYPETWO:
                Log.e("tag","replece_title_relative_layout5");
                if (mapModel.getInvestments().size() <= page * index * 2) {
                    page = 0;
                    swicthRefresh();
                    page++;
                } else {
                    swicthRefresh();
                    page++;
                }
                break;
            case TYPETHREE:
                Log.e("tag","replece_title_relative_layout6");
                if (mapModel.getShareholders().size() <= page * index * 2) {
                    page = 0;
                    swicthRefresh();
                    page++;
                } else {
                    swicthRefresh();
                    page++;
                }
                break;
            case TYPEFOUR:
                break;
        }
    }

//    private void darkenBackgroud(Float bgcolor) {
//        WindowManager.LayoutParams lp = getWindow().getAttributes();
//        lp.alpha = bgcolor;
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//        getWindow().setAttributes(lp);
//    }


    @Override
    public boolean isSetStatusBar() {
        return false;
    }

    @Override
    public boolean isBarDark() {
        return false;
    }
}