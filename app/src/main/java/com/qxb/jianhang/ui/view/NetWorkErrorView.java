package com.qxb.jianhang.ui.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qxb.jianhang.R;

/**
 * Created by Albert on 2015/11/27.
 * Mail : lbh@jusfoun.com
 * TODO :
 * Description:网络异常或者服务器返回值异常View
 */
public class NetWorkErrorView extends RelativeLayout {
    private Context mContext;
    private ImageView mErrorImage;
    private RelativeLayout mRefreshLayout;
    private TextView mErrorText;

    public NetWorkErrorView(Context context) {
        super(context);
        mContext = context;
        initData();
        initView();
        initWidgetAction();
    }

    public NetWorkErrorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initData();
        initView();
        initWidgetAction();
    }

    public NetWorkErrorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initData();
        initView();
        initWidgetAction();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public NetWorkErrorView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
        initData();
        initView();
        initWidgetAction();
    }

    private void initData(){

    }

    private void initView(){
        LayoutInflater.from(mContext).inflate(R.layout.network_error_layout, this, true);
        mErrorImage = (ImageView) findViewById(R.id.error_image);
        mErrorText = (TextView)findViewById(R.id.error_text);
        mRefreshLayout = (RelativeLayout) findViewById(R.id.net_refresh_layout);
    }

    private void initWidgetAction(){
        mRefreshLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null){
                    listener.OnNetRefresh();
                }
            }
        });
    }

    public void setNetWorkError(){
//        mErrorImage.setImageResource(R.mipmap.net_error_icon);
//        mErrorText.setText("网络异常");
    }

    public void setServerError(){
//        mErrorImage.setImageResource(R.mipmap.server_error);
//        mErrorText.setText("数据服务器异常");
    }

    private OnRefreshListener listener;

    public void setListener(OnRefreshListener listener) {
        this.listener = listener;
    }

    public interface OnRefreshListener{
        public void OnNetRefresh();
    }
}
