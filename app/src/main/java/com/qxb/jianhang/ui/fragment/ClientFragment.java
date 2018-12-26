package com.qxb.jianhang.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.jusfoun.baselibrary.Util.PreferenceUtils;
import com.jusfoun.baselibrary.base.BaseAdapter;
import com.jusfoun.baselibrary.net.Api;
import com.jusfoun.baselibrary.net.NetModel;
import com.qxb.jianhang.R;
import com.qxb.jianhang.ui.adapter.ClientAdapter;
import com.qxb.jianhang.ui.base.BaseListFragment;
import com.qxb.jianhang.ui.constant.Constant;
import com.qxb.jianhang.ui.data.ClientDataModel;
import com.qxb.jianhang.ui.data.ClientModel;
import com.qxb.jianhang.ui.net.ApiService;
import com.qxb.jianhang.ui.sharedpreferences.LocationSharepreferences;

import java.util.HashMap;

import rx.functions.Action1;

/**
 * @author lgd
 * @version create time:18/8/2714:35
 * @Email lgd@jusfoun.com
 * @Description ${我的客户fragment}
 */
public class ClientFragment extends BaseListFragment {

    private int type;
    public static ClientFragment getInstance(int index) {
        ClientFragment fragment = new ClientFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.frag_recycler_common;
    }

    @Override
    public void initDatas() {
         type = getArguments().getInt("type");
    }

    @Override
    public void initView(View rootView) {
    }

    @Override
    public void initAction() {
    }

    @Override
    protected BaseAdapter getAdapter() {
        return new ClientAdapter(mContext,type );
    }

    @Override
    protected void loadData() {
        HashMap<String, Object> map = new HashMap<>();

        if(type==2){
            map.put("type", "11");
        }else{
            map.put("type", type);
        }

        map.put("pageSize", pageSize);
        map.put("pageIndex", pageIndex);
//        if (!TextUtils.isEmpty(PreferenceUtils.getString(mContext, Constant.LOC_LONGITUDE)))
//            map.put("myLongitude", PreferenceUtils.getString(mContext, Constant.LOC_LONGITUDE));
//        if (!TextUtils.isEmpty(PreferenceUtils.getString(mContext, Constant.LOC_LATITUDE)))
//            map.put("myLatitude", PreferenceUtils.getString(mContext, Constant.LOC_LATITUDE));

        map.put("myLongitude", LocationSharepreferences.getLocation(mContext).lon);
        map.put("myLatitude", LocationSharepreferences.getLocation(mContext).lat);
//        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(map));
        addNetwork(Api.getInstance().getService(ApiService.class).myCustomer(map), new Action1<NetModel>() {
            @Override
            public void call(NetModel net) {
                hideLoadDialog();
                if (net.success()) {

                    completeLoadData(net.dataToList("list", ClientModel.class));


                    ClientDataModel model =  net.dataToObject(ClientDataModel.class);
                    if(adapter.getItemCount()>=model.totalCount){
                        recycler.setLoadingMoreEnabled(false);
                    }else{
                        recycler.setLoadingMoreEnabled(true);
                    }

                } else {
                    completeLoadData(null);
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                completeLoadDataError();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }
}
