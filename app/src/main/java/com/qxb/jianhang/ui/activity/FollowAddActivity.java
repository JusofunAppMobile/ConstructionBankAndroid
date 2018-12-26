package com.qxb.jianhang.ui.activity;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jusfoun.baselibrary.Util.ToastUtils;
import com.jusfoun.baselibrary.event.IEvent;
import com.jusfoun.baselibrary.net.Api;
import com.jusfoun.baselibrary.net.NetModel;
import com.qxb.jianhang.R;
import com.qxb.jianhang.ui.base.BaseBackActivity;
import com.qxb.jianhang.ui.data.ClientInfoModel;
import com.qxb.jianhang.ui.data.HomeDataItemModel;
import com.qxb.jianhang.ui.data.PoiInfoModel;
import com.qxb.jianhang.ui.data.TrackRecordDataModel;
import com.qxb.jianhang.ui.dialog.SelectTimeDialog;
import com.qxb.jianhang.ui.event.SelectPoiInfoEvent;
import com.qxb.jianhang.ui.event.UpdateFollowEvent;
import com.qxb.jianhang.ui.net.ApiService;
import com.qxb.jianhang.ui.util.AppUtils;
import com.qxb.jianhang.ui.view.ClientView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.functions.Action1;

/**
 * @author liuguangdan
 * @version create at 2018/9/4/004 20:41
 * @Email lgd@jusfoun.com
 * @Description ${}
 */
public class FollowAddActivity extends BaseBackActivity {
    @BindView(R.id.vClient)
    LinearLayout vClient;
    @BindView(R.id.tvStatus)
    TextView tvStatus;
    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.tvAddress)
    TextView tvAddress;
    @BindView(R.id.edit_detail)
    EditText addressEdit;

    PoiInfoModel poiInfoModel;

    private TrackRecordDataModel.TrackRecordItemModel model;

    public static final String COMPANYID = "companyid";
    private String companyid = "";

    private ClientView clientView;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_follow_add;
    }

    @Override
    public void initDatas() {
        companyid = getIntent().getStringExtra(COMPANYID);
        Log.e("tag","companyidcompanyidcompanyid"+companyid);
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        setTitle("添加跟进日志");
        clientView = new ClientView(this, vClient.getChildCount());
        vClient.addView(clientView);

        if (getIntent() != null && getIntent().getSerializableExtra("model") != null) {
            model = (TrackRecordDataModel.TrackRecordItemModel) getIntent().getSerializableExtra("model");
            setData();
        }
    }

    @OnClick(R.id.view3)
    public void selectFollowStatus() {

        final String[] array = AppUtils.getFollows();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请选择跟进状态");
        builder.setItems(array, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tvStatus.setText(array[which]);
                tvStatus.setTag(which);
            }
        });
        builder.create().show();
    }

    @OnClick(R.id.ivAddClient)
    public void ivAddClient() {
//        vClient.addView(new ClientView(this, vClient.getChildCount()));
    }

    @OnClick(R.id.view1)
    public void selectTime() {
        new SelectTimeDialog(this, new SelectTimeDialog.OnSelectListener() {
            @Override
            public void select(String time) {
                tvTime.setText(time);
            }
        }).show();
    }

    @OnClick(R.id.view2)
    public void selectAddress() {
        startActivity(MapAddressActivity.class);
    }

    @Override
    public void initAction() {

    }

    public void save(View view) {
        addFollowNet();
    }

    @Override
    public void onEvent(IEvent event) {
        super.onEvent(event);
        if (event instanceof SelectPoiInfoEvent) {
            poiInfoModel = ((SelectPoiInfoEvent) event).model;
            tvAddress.setText(poiInfoModel.getFinalAddress());
        }
    }

    private void addFollowNet() {


        if(tvTime.getText().length()==0){
            showToast("请选择跟进时间");
            return;
        }
        if(poiInfoModel==null){
            showToast("请选择跟进地点");
            return;
        }

        if(tvStatus.getTag()==null){
            showToast("请选择跟进状态");
            return;
        }


        List<ClientInfoModel> list = new ArrayList<>();
        for (int i = 0; i < vClient.getChildCount(); i++) {
            ClientInfoModel model = ((ClientView) vClient.getChildAt(i)).getData();
            if (TextUtils.isEmpty(model.name) && TextUtils.isEmpty(model.phone) && TextUtils.isEmpty(model.job)) {
                continue;

            }
            list.add(model);
        }


        if (list.size() == 0) {
            showToast("请填写客户信息");
            return;
        }

        ClientInfoModel clientInfoModel = list.get(0);

        if(TextUtils.isEmpty(clientInfoModel.name)){
            showToast("请填写客户姓名");
            return;
        }

        if(TextUtils.isEmpty(clientInfoModel.job)){
            showToast("请填写客户职位");
            return;
        }


        if(TextUtils.isEmpty(clientInfoModel.phone)){
            showToast("请填写客户电话");
            return;
        }


        showLoadDialog();

        HashMap<String, Object> map = new HashMap<>();
        map.put("companyId", companyid);
        map.put("userId", AppUtils.getUserInfo().id);
        map.put("time", tvTime.getText().toString());
        map.put("customer", list);
        map.put("address", poiInfoModel.getFinalAddress());
        map.put("followState", (int) tvStatus.getTag() + 1);
        map.put("longitude", poiInfoModel.longitude);
        map.put("latitude", poiInfoModel.latitude);
        map.put("detailAddress", addressEdit.getText().toString());

        ClientView clientView = ((ClientView) vClient.getChildAt(0));
        map.put("demand", clientView.getDemand());
        map.put("suggest", clientView.getSuggest());
        map.put("isDemandSolved", clientView.getDemandState());
        map.put("support", clientView.getSupport());
        map.put("isSupportSolved", clientView.getSupportState());

        if (model!=null&&!TextUtils.isEmpty(model.id)) {
            map.put("followId", model.id.replace(".0",""));
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(map));

        addNetwork(Api.getInstance().getService(ApiService.class).addFollowNet(body), new Action1<TrackRecordDataModel.TrackRecordItemModel>() {
            @Override
            public void call(TrackRecordDataModel.TrackRecordItemModel net) {
                hideLoadDialog();
                if (net.success()) {
                    Log.e("tag","companyidcompanyidcompanyid2"+new Gson().toJson(net));
                    UpdateFollowEvent updateFollowEvent = new UpdateFollowEvent();
                    updateFollowEvent.companyId =companyid;
                    updateFollowEvent.followState = ( (int) tvStatus.getTag() + 1)+"";
                    EventBus.getDefault().post(updateFollowEvent);

                    showToast("添加成功");
                    finish();
                } else {
                    showToast(net.msg);
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                hideLoadDialog();
                ToastUtils.showHttpError();
            }
        });
    }

    private void setData() {
        tvTime.setText(model.time);

        if ((model.followState - 1) >= 0 && model.followState - 1 < AppUtils.getFollows().length) {
            tvStatus.setText(AppUtils.getFollows()[model.followState - 1]);
        }
        tvStatus.setTag(model.followState);
        tvAddress.setText(model.address);

        addressEdit.setText(model.detailAddress);
        clientView.setData(model);

        poiInfoModel = new PoiInfoModel();
        poiInfoModel.address = model.address;
        try {
            poiInfoModel.latitude = Double.parseDouble(model.latitude);
            poiInfoModel.longitude = Double.parseDouble(model.latitude);
        } catch (Exception e) {

        }
    }
}
