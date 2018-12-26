package com.jusfoun.baselibrary.base;

import android.Manifest;
import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jusfoun.baselibrary.BaseApplication;
import com.jusfoun.baselibrary.R;
import com.jusfoun.baselibrary.Util.KeyBoardUtil;
import com.jusfoun.baselibrary.Util.PhoneUtil;
import com.jusfoun.baselibrary.Util.ToastUtils;
import com.jusfoun.baselibrary.event.IEvent;
import com.jusfoun.baselibrary.net.NetModel;
import com.jusfoun.baselibrary.view.PermissionsDialog;
import com.jusfoun.baselibrary.view.SwipeBackLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.ref.WeakReference;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;
import rx.functions.Action1;

/**
 * @author zhaoyapeng
 * @version create time:18/8/2416:35
 * @Email zyp@jusfoun.com
 * @Description ${base activtiy}
 */
@RuntimePermissions
public abstract class BaseActivity extends AppCompatActivity {
    protected ImageView ivBack;
    protected ImageView ivRight;
    protected TextView tvTitle;
    protected TextView tvRight;

    protected String TAG = "";
    private SwipeBackLayout swipeBackLayout;
    private LinearLayout linearLayout;
    private ImageView isShadow;
    public Context mContext;
    protected RxManage rxManage;
    private BaseApplication mApplication;
    /**
     * 当前Activity的弱引用，防止内存泄露
     */
    private WeakReference<Activity> mActivity;
    private PermissionsDialog readPhonePermissionsDialog, writePermissionsDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
//        setStatusBarLight(false);
        TAG = getClass().getSimpleName();
        mContext = this;
        if (getLayoutResId() != 0)
            setContentView(getLayoutResId());
        initBase();
        rxManage = new RxManage();

        readPhonePermissionsDialog = new PermissionsDialog(this, com.jusfoun.baselibrary.R.style.my_dialog);
        writePermissionsDialog = new PermissionsDialog(this, com.jusfoun.baselibrary.R.style.my_dialog);

        readPhonePermissionsDialog.setContent(getString(com.jusfoun.baselibrary.R.string.permissio_name_read_phone));
        writePermissionsDialog.setContent(getString(com.jusfoun.baselibrary.R.string.permissio_name_write));

        readPhonePermissionsDialog.setCanFinish();
        writePermissionsDialog.setCanFinish();

        BaseActivityPermissionsDispatcher.startReadPhoneSteteWithPermissionCheck(this);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        if (!isNeedSwipe()) {
            super.setContentView(layoutResID);
        } else {
            setContentView(getSwipeContainer());
            View view = LayoutInflater.from(this).inflate(layoutResID, null);
            swipeBackLayout.addView(view);
        }
    }

    /**
     * 是否需要侧滑返回
     *
     * @return
     */
    public boolean isNeedSwipe() {
        return true;
    }

    ;

    private void initBase() {

        EventBus.getDefault().register(this);
        // 将当前Activity压入栈
        mApplication = BaseApplication.getBaseApplication();
        mActivity = new WeakReference<Activity>(this);
        mApplication.pushTask(mActivity);
    }

    private View getSwipeContainer() {
        RelativeLayout container = new RelativeLayout(this);
        swipeBackLayout = new SwipeBackLayout(this);
        swipeBackLayout.setDragEdge(SwipeBackLayout.DragEdge.LEFT);
        swipeBackLayout.setBackgroundResource(R.color.transparent);
        isShadow = new ImageView(this);
        isShadow.setImageResource(R.color.app_bg_color);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                , ViewGroup.LayoutParams.MATCH_PARENT);
        container.addView(isShadow, params);
        container.addView(swipeBackLayout);
        swipeBackLayout.setOnSwipeBackListener(new SwipeBackLayout.SwipeBackListener() {
            @Override
            public void onViewPositionChanged(float fractionAnchor, float fractionScreen) {
                Log.e("isShadow", "1-fractionScreen" + (1 - fractionScreen));
                isShadow.setAlpha(1 - fractionScreen);
            }
        });

        return container;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        rxManage.clear();//fragment销毁清除rxbus事件及网络请求，防止内存泄漏
        KeyBoardUtil.hideSoftKeyboard(this);
        mApplication.removeTask(mActivity);

        EventBus.getDefault().unregister(this);
    }

    public void showToast(String text) {
        ToastUtils.show(text);
    }

    public void showToast(int stringId) {
        ToastUtils.show(stringId);
    }

    /**
     * 跳转界面
     *
     * @param bundle 传递数据，为NULL不传递
     * @param cls    跳转的界面
     */
    protected void goActivity(Bundle bundle, Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(mContext, cls);
        if (bundle != null)
            intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * 跳转界面并在销毁时返回结果
     *
     * @param bundle
     * @param cls
     * @param requestCode
     */
    protected void goActivityForResult(Bundle bundle, Class<?> cls, int requestCode) {
        Intent intent = new Intent(mContext, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    /**
     * 设置状态栏颜色 状态栏为亮色，字体为黑色 反之字体为白色
     *
     * @param drakMode true 字体黑色 false 字体白色
     */
    protected void setStatusBarLight(boolean drakMode) {
        PhoneUtil.applyKitKatTranslucency(getWindow(), drakMode);
        PhoneUtil.setMiuiStatusBarDarkMode(this, drakMode);
        PhoneUtil.setMeizuStatusBarDarkIcon(this, drakMode);

    }

    /**
     * 添加网络请求
     *
     * @param observable
     * @param next
     */
    protected <T extends NetModel> void addNetwork(rx.Observable<T> observable, Action1<T> next, Action1<Throwable> error) {
        rxManage.add(observable, next, error);
    }

    public abstract int getLayoutResId();

    public abstract void initDatas();

    public abstract void initView();

    public abstract void initAction();

    @Subscribe
    public void onEvent(IEvent event) {
    }

    protected String getValue(TextView et) {
        return et.getText().toString();
    }


    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    protected void statrInit() {


        if ("xiaomi".equalsIgnoreCase(Build.MANUFACTURER) && Build.VERSION.SDK_INT >= 23) {
            AppOpsManager appOpsManager = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
            int checkOp = appOpsManager.checkOp(AppOpsManager.OPSTR_READ_PHONE_STATE, Process.myUid(), getPackageName());
            if (checkOp == AppOpsManager.MODE_IGNORED) {
                // 权限被拒绝了
                readPhonePermissionsDialog.show();
                return;
            }
        }

        if ("xiaomi".equalsIgnoreCase(Build.MANUFACTURER) && Build.VERSION.SDK_INT >= 23) {
            AppOpsManager appOpsManager = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
            int checkOp = appOpsManager.checkOp(AppOpsManager.OPSTR_WRITE_EXTERNAL_STORAGE, Process.myUid(), getPackageName());
            if (checkOp == AppOpsManager.MODE_IGNORED) {
                // 权限被拒绝了
                writePermissionsDialog.show();
                return;
            }
        }

        readPhonePermissionsDialog.dismiss();
        writePermissionsDialog.dismiss();

        initNavigationView();
        initDatas();
        initView();
        initAction();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        Log.e("tag", "onRequestPermissionsResult");
        BaseActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @OnPermissionDenied(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    protected void showDeniedForWriteExternal() {
        Log.e("tag", "showDeniedForWriteExternal1");

        if (writePermissionsDialog.isShowing()) {
            return;
        }
        writePermissionsDialog.show();
    }

    @OnNeverAskAgain(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void showNeverAskForWriteExternal() {
        Log.e("tag", "showDeniedForWriteExternal2");
        if (writePermissionsDialog.isShowing()) {
            return;
        }
        writePermissionsDialog.show();
    }

    @OnShowRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void showRationaleForWriteExternal(final PermissionRequest request) {
        Log.e("tag", "showDeniedForWriteExternal3");
        writePermissionsDialog.setCallBack(new PermissionsDialog.CallBack() {
            @Override
            public void onLeftClick() {
//              request.cancel();
                writePermissionsDialog.dismiss();
                finish();
            }

            @Override
            public void onRightClick() {
//              request.proceed();
                writePermissionsDialog.dismiss();
            }
        });
        if (writePermissionsDialog.isShowing()) {
            return;
        }
        writePermissionsDialog.show();
    }


    @NeedsPermission(Manifest.permission.READ_PHONE_STATE)
    protected void startReadPhoneStete() {
        Log.e("tag", "showDeniedForWriteExternal4");
        BaseActivityPermissionsDispatcher.statrInitWithPermissionCheck(this);
    }


    @OnPermissionDenied(Manifest.permission.READ_PHONE_STATE)
    protected void showDeniedForReadPhoneState() {
        Log.e("tag", "showDeniedForWriteExternal5");
        if (readPhonePermissionsDialog.isShowing()) {
            return;
        }
        readPhonePermissionsDialog.show();
    }

    @OnNeverAskAgain(Manifest.permission.READ_PHONE_STATE)
    void showNeverAskForReadPhoneState() {
        Log.e("tag", "showDeniedForWriteExternal6");
        if (readPhonePermissionsDialog.isShowing()) {
            return;
        }
        readPhonePermissionsDialog.show();
    }

    @OnShowRationale(Manifest.permission.READ_PHONE_STATE)
    void showRationaleForReadPhoneState(final PermissionRequest request) {
        Log.e("tag", "showRationaleForReadPhoneState1");
        readPhonePermissionsDialog.setCallBack(new PermissionsDialog.CallBack() {
            @Override
            public void onLeftClick() {
//                request.cancel();
                readPhonePermissionsDialog.dismiss();
                finish();
            }

            @Override
            public void onRightClick() {
//                request.proceed();
                readPhonePermissionsDialog.dismiss();
            }
        });

        if (readPhonePermissionsDialog.isShowing()) {
            return;
        }
        try {
            readPhonePermissionsDialog.show();
        } catch (Exception e) {
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PermissionsDialog.ACTION_APPLICATION_DETAILS_SETTINGS_CODE) {
            BaseActivityPermissionsDispatcher.startReadPhoneSteteWithPermissionCheck(this);
        }
    }

    protected void initNavigationView() {
        if (getLayoutResId() != 0) {
            ivBack = (ImageView) findViewById(R.id.ivBack);
            if (ivBack != null) {
                ivBack.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        onBackClick();
                    }
                });
            }

            ivRight = (ImageView) findViewById(R.id.ivRight);
            if (ivRight != null) {
                ivRight.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        onRightClick();
                    }
                });
            }

            tvTitle = (TextView) findViewById(R.id.tvTitle);
            tvRight = (TextView) findViewById(R.id.tvRight);
            if (tvRight != null) {
                tvRight.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        onRightClick();
                    }
                });
            }

            String title = getIntent().getStringExtra("title");
            if (!TextUtils.isEmpty(title))
                setTitle(title);
        }
    }

    protected void onBackClick() {
        finish();
    }

    protected void onRightClick() {

    }

}
