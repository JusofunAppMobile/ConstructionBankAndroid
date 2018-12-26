package com.qxb.jianhang.ui.pop;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jusfoun.baselibrary.Util.PhoneUtil;
import com.qxb.jianhang.R;


/**
 * @author zhaoyapeng
 * @version create time:17/12/1417:05
 * @Email zyp@jusfoun.com
 * @Description ${TODO}
 */
public class SelectSearchPopView extends PopupWindow implements View.OnClickListener {
    protected View rootView;
    protected TextView textCompany;
    protected TextView textAddress;
    private Context mContext;


    public SelectSearchPopView(Context context) {
        super(context);
        mContext = context;
        initData();
        initViews();
        initActions();
    }

    public SelectSearchPopView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initData();
        initViews();
        initActions();
    }

    public SelectSearchPopView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initData();
        initViews();
        initActions();
    }

    private void initData() {
    }

    private void initViews() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_select_search_type, null);
        setContentView(view);
        setWidth(PhoneUtil.dip2px(mContext, 90));
        setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
        setBackgroundDrawable(new BitmapDrawable());
        //设置点击空白处消失
        setTouchable(true);
        setOutsideTouchable(true);
        setFocusable(true);
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        initView(view);


    }

    private void initActions() {


    }

    private void initView(View rootView) {
        textCompany = (TextView) rootView.findViewById(R.id.text_company);
        textCompany.setOnClickListener(SelectSearchPopView.this);
        textAddress = (TextView) rootView.findViewById(R.id.text_address);
        textAddress.setOnClickListener(SelectSearchPopView.this);
    }

    @Override
    public void onClick(View view) {
    if (view.getId() == R.id.text_company) {
            if (callBack != null) {
                callBack.select("搜公司", 1);
            }
        } else if (view.getId() == R.id.text_address) {
            if (callBack != null) {
                callBack.select("搜地址", 2);
            }
        }
    }


    public interface CallBack {
        void select(String key, int value);
    }

    public CallBack callBack;

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

}
