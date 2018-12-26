package com.qxb.jianhang.ui.view;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jusfoun.baselibrary.Util.PhoneUtil;
import com.jusfoun.baselibrary.Util.ToastUtils;
import com.jusfoun.baselibrary.base.BaseView;
import com.qxb.jianhang.R;

/**
 * @author zhaoyapeng
 * @version create time:2018/11/510:23
 * @Email zyp@jusfoun.com
 * @Description ${地图页面搜索title}
 */
public class SearchListTitleView extends BaseView {
    protected View rootView;
    protected EditText editSearch;
    protected TextView textSearch;
    protected ImageView imgSearch,backImg;
    private int type;

    public SearchListTitleView(Context context) {
        super(context);
    }

    public SearchListTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SearchListTitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initViews() {
        LayoutInflater.from(mContext).inflate(R.layout.view_serach_list_titbar, this, true);
        initView(this);
    }

    @Override
    protected void initActions() {


        backImg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity)mContext).finish();
            }
        });



        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (callBack != null) {
                    if (type == SearchMapTitleView.TYPE_ADDRESS && showAddress) {
                        callBack.indexAddress(editSearch.getText().toString(), type);
                    }
                    if (!showAddress) {
                        showAddress = true;
                    }
                }
            }
        });

        textSearch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editSearch.getText().length() == 0) {
                    if (type == SearchMapTitleView.TYPE_COMPANY) {
                        ToastUtils.show("请输入企业名称");
                    } else {
                        ToastUtils.show("请输入地址");
                    }
                    return;
                }

                if (callBack != null) {
                    callBack.search(editSearch.getText().toString(), type);
                }
            }
        });
    }

    private void initView(View rootView) {
        editSearch = (EditText) rootView.findViewById(R.id.edit_search);
        textSearch = (TextView) rootView.findViewById(R.id.text_search);
        imgSearch = (ImageView) rootView.findViewById(R.id.img_search);
        backImg = (ImageView)rootView.findViewById(R.id.img_back);
    }

    public interface CallBack {
        void search(String searchKey, int type);

        void indexAddress(String searchKey, int type);

    }

    private CallBack callBack;

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }


    public String getSearchKey(){
        return editSearch.getText().toString();

    }

    public void setStatus(int type) {
        this.type = type;
        if(type==SearchMapTitleView.TYPE_COMPANY) {
            editSearch.setHint("请输入企业名称");
        }else{
            editSearch.setHint("请输入地址");
        }
    }

    private boolean showAddress = false;

    public void setSearchText(String text) {
        editSearch.setText(text);
        showAddress = false;
    }

}
