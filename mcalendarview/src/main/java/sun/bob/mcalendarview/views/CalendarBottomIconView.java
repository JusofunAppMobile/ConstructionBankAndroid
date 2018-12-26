package sun.bob.mcalendarview.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import sun.bob.mcalendarview.R;
import sun.bob.mcalendarview.data.CalendarData;


/**
 * @author zhaoyapeng
 * @version create time:2018/12/1015:21
 * @Email zyp@jusfoun.com
 * @Description ${TODO}
 */
public class CalendarBottomIconView extends LinearLayout {
    protected View rootView;
    protected ImageView imgPhone;
    protected ImageView imgVisit;
    protected ImageView imgSisited;
    protected ImageView imgCooperation;
    protected ImageView imgFormal;
    private Context mContext;

    public CalendarBottomIconView(Context context) {
        super(context);
        mContext = context;
        initData();
        initViews();
        initActions();
    }

    public CalendarBottomIconView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initData();
        initViews();
        initActions();
    }

    public CalendarBottomIconView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initData();
        initViews();
        initActions();
    }

    protected void initData() {

    }

    protected void initViews() {
        LayoutInflater.from(mContext).inflate(R.layout.view_calenar_bottom, this, true);
        initView(this);
    }

    protected void initActions() {

    }

    private boolean isFirstVisible = false;

    public void setData(CalendarData.DayDate model) {
        setState(imgPhone,model.statusPhone);
        setState(imgVisit,model.statusVisit);
        setState(imgSisited,model.statusVisited);
        setState(imgCooperation,model.statusCooperation);
        setState(imgFormal,model.statusformal);
    }

    private void initView(View rootView) {
        imgPhone = (ImageView) rootView.findViewById(R.id.img_phone);
        imgVisit = (ImageView) rootView.findViewById(R.id.img_visit);
        imgSisited = (ImageView) rootView.findViewById(R.id.img_sisited);
        imgCooperation = (ImageView) rootView.findViewById(R.id.img_cooperation);
        imgFormal = (ImageView) rootView.findViewById(R.id.img_formal);
    }

    private void setState(ImageView imageView, boolean isShow) {
        if (isShow) {
            if (!isFirstVisible) {
                isFirstVisible = true;
                LayoutParams layoutParams = (LayoutParams) imageView.getLayoutParams();
                layoutParams.setMarginStart(0);
            }
            imageView.setVisibility(VISIBLE);
        } else {
            imageView.setVisibility(GONE);
        }
    }
}
