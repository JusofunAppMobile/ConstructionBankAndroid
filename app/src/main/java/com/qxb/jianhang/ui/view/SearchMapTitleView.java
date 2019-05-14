package com.qxb.jianhang.ui.view;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jusfoun.baselibrary.Util.PhoneUtil;
import com.jusfoun.baselibrary.Util.ToastUtils;
import com.jusfoun.baselibrary.base.BaseView;
import com.qxb.jianhang.R;
import com.qxb.jianhang.ui.pop.SelectDistancePopView;
import com.qxb.jianhang.ui.pop.SelectSearchPopView;

/**
 * @author zhaoyapeng
 * @version create time:2018/11/510:23
 * @Email zyp@jusfoun.com
 * @Description ${地图页面搜索title}
 */
public class SearchMapTitleView extends BaseView {
    protected View rootView;
    protected EditText editSearch;
    protected LinearLayout layoutSearchEdit;
    protected ImageView imgSearch, backImg;
    private TextView typeText,searchText;

    private SelectSearchPopView selectSearchPopView;
    private int type=TYPE_COMPANY;

    public static int TYPE_COMPANY = 1;
    public static int TYPE_ADDRESS = 2;
    private ImageView delImg;



    public SearchMapTitleView(Context context) {
        super(context);
    }

    public SearchMapTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SearchMapTitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initData() {
        selectSearchPopView = new SelectSearchPopView(mContext);
    }

    @Override
    protected void initViews() {
        LayoutInflater.from(mContext).inflate(R.layout.view_serach_map_titbar, this, true);
        initView(this);
    }

    @Override
    protected void initActions() {

        typeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectSearchPopView.showAsDropDown(typeText, -PhoneUtil.dip2px(mContext, 20), 0);
            }
        });

        selectSearchPopView.setCallBack(new SelectSearchPopView.CallBack() {
            @Override
            public void select(String key, int value) {
                typeText.setText(key);
                type = value;
                if(type==TYPE_COMPANY) {
                    editSearch.setHint("请输入企业名称");
                    if(callBack!=null){
                        callBack.hideAddressList();
                    }
                }else{
                    editSearch.setHint("请输入地址");
                }
                selectSearchPopView.dismiss();
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
                    if(type==TYPE_ADDRESS) {
                        callBack.indexAddress(editSearch.getText().toString(), type);
                    }
                }
            }
        });

        searchText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editSearch.getText().length() == 0) {
                    if(type==TYPE_COMPANY) {
                        ToastUtils.show("请输入企业名称");
                    }else{
                        ToastUtils.show("请输入地址");
                    }
                    return ;
                }

                if (callBack != null) {
                    callBack.search(editSearch.getText().toString(),type);
                }
            }
        });
        delImg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                editSearch.setText("");
            }
        });
    }

    private void initView(View rootView) {
        editSearch = (EditText) rootView.findViewById(R.id.edit_search);
        layoutSearchEdit = (LinearLayout) rootView.findViewById(R.id.layout_search_edit);
        imgSearch = (ImageView) rootView.findViewById(R.id.img_search);
        typeText = (TextView)rootView.findViewById(R.id.text_type);
        searchText  =(TextView)rootView.findViewById(R.id.text_search);
        delImg = (ImageView)rootView.findViewById(R.id.img_del);
    }




    public interface CallBack {
        void search(String searchKey,int type);

        void indexAddress(String searchKey,int type);

        void hideAddressList();
    }

    private CallBack callBack;

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }



    public String getSearchText(){
        return  editSearch.getText().toString();
    }
}
