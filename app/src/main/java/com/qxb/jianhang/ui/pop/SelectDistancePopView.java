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
public class SelectDistancePopView extends PopupWindow implements View.OnClickListener {
    protected View rootView;
    protected TextView textTwo;
    protected TextView textFive;
    protected TextView textTen;
    protected TextView textTwenty;
    protected TextView textAll;
    private Context mContext;


    public SelectDistancePopView(Context context) {
        super(context);
        mContext = context;
        initData();
        initViews();
        initActions();
    }

    public SelectDistancePopView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initData();
        initViews();
        initActions();
    }

    public SelectDistancePopView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initData();
        initViews();
        initActions();
    }

    private void initData() {
    }

    private void initViews() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_select_distance, null);
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
        textTwo = (TextView) rootView.findViewById(R.id.text_two);
        textTwo.setOnClickListener(SelectDistancePopView.this);
        textFive = (TextView) rootView.findViewById(R.id.text_five);
        textFive.setOnClickListener(SelectDistancePopView.this);
        textTen = (TextView) rootView.findViewById(R.id.text_ten);
        textTen.setOnClickListener(SelectDistancePopView.this);
        textTwenty = (TextView) rootView.findViewById(R.id.text_twenty);
        textTwenty.setOnClickListener(SelectDistancePopView.this);
        textAll = (TextView) rootView.findViewById(R.id.text_all);
        textAll.setOnClickListener(SelectDistancePopView.this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.text_two) {
            if(callBack!=null){
                callBack.select("2km","2");
            }
        } else if (view.getId() == R.id.text_five) {
            if(callBack!=null){
                callBack.select("5km","5");
            }

        } else if (view.getId() == R.id.text_ten) {
            if(callBack!=null){
                callBack.select("10km","10");
            }

        } else if (view.getId() == R.id.text_twenty) {
            if(callBack!=null){
                callBack.select("20km","20");
            }

        } else if (view.getId() == R.id.text_all) {
            if(callBack!=null){
                callBack.select("全北京","-2");
            }

        }
    }


    public interface CallBack {
        void select(String key,String value);
    }

    public CallBack callBack;

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

}
